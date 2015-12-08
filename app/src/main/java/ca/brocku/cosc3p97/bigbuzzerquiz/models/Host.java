package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.AnswerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.GetPlayersRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.PlayRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.ReadyRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerProxy;


public class Host implements HostActions {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<PlayerConnection.SetupListener> listeners = new ArrayList<>();
    private PlayerProxy playerProxy;
    private List<Player> players = new ArrayList<>();
    public enum State {
        Play, Stop
    }
    private State state = State.Stop;
    private int readyCounter = 0;
    private int questionCounter = 0;
    private int maxQuestions = 2;
    private int answers = 0;


    public class Player {
        public String name;
        public int score;
    }


    private Host(PlayerConnection.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        playerProxy = new PlayerProxy(this, listener);

        addRequestHandlers();

    }


    private void addRequestHandlers() {
        playerProxy.addRequestHandler(HostRequestInterface.GET_PLAYERS, new GetPlayersRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestInterface.PLAY, new PlayRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestInterface.READY, new ReadyRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestInterface.ANSWER, new AnswerRequestHandler(this));
    }


    public static Host getInstance(PlayerConnection.SetupListener listener) {
        if (instance == null) {
            try {
                return new Host(listener);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return instance;
        }
    }


    public void addListener(PlayerConnection.SetupListener listener) {
        listeners.add(listener);
    }


    public void setTcpListener(TcpConnection.Listener listener) {
        playerProxy.setPlayerTcpListener(listener);
    }


    public Handler getHandler() {
        return playerProxy.getThreadHandler();
    }


    public int getPlayerIndex(Sender sender) {
        return playerProxy.getPlayerIndex(sender);
    }


    @Override
    public void addPlayer(String name) {
        Player player = new Player();
        player.name = name;
        player.score = 0;
        players.add(player);
    }


    @Override
    public void getPlayers(GetPlayersCallback callback) {
        List<String> names = new ArrayList<>();
        for(Player player : players) {
            names.add(player.name);
        }
        callback.reply(names);
    }


    @Override
    public void play() {
        Log.i(TAG, "play: invoked");
        if(state == State.Stop) {
            state = State.Play;
            sendNextQuestion();
        }
    }


    @Override
    public void ready() {
        readyCounter++;
        if(readyCounter == players.size()) {
            readyCounter = 0;
            if(questionCounter == maxQuestions) {
                state = State.Stop;
                questionCounter = 0;
                playerProxy.gameOver(players);
            } else {
                sendNextQuestion();
            }
        }
    }

    @Override
    public void answer(boolean correct) {
        answers++;

        if(correct) {
            // TODO: 2015-12-08 interrupt the other players
        } else {
            if(answers == players.size()) {
                answers = 0;
                // TODO: 2015-12-08 if the counter is equal to the number of players then tell everyone they failed
            }
        }
    }


    public void adjustPoints(boolean correct, int playerIndex) {
        Log.i(TAG, String.format("answer: invoked with correct=[%s]", correct));
        if(correct) {
            players.get(playerIndex).score++;
        } else {
            players.get(playerIndex).score--;
        }

        answer(correct);

    }


    private void sendNextQuestion() {
        questionCounter++;
        readyCounter = 0;
        playerProxy.showQuestion();

        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(5000);
                    playerProxy.timeout();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
    }

}
