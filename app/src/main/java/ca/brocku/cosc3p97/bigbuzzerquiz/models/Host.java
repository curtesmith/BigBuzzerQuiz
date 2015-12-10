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


public class Host implements HostActions, TimeoutListener {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<PlayerConnection.SetupListener> listeners = new ArrayList<>();
    private PlayerProxy playerProxy;
    private List<Participant> players = new ArrayList<>();
    private Question question;

    @Override
    public void onTimeout() {
        playerProxy.timeout();
    }

    public enum State {
        Play, Stop
    }

    private State state = State.Stop;

    private int readyCounter = 0;
    private int questionCounter = 0;
    private int maxQuestions = 2;


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
        Participant player = new Participant();
        player.name = name;
        player.score = 0;
        players.add(player);
    }


    @Override
    public void getPlayers(GetPlayersCallback callback) {
        List<String> names = new ArrayList<>();
        for (Participant player : players) {
            names.add(player.name);
        }
        callback.reply(names);
    }


    @Override
    public void play() {
        Log.i(TAG, "play: invoked");
        if (state == State.Stop) {
            state = State.Play;
            questionCounter = 0;
            resetPlayerScores();
            sendNextQuestion();
        }
    }


    private void resetPlayerScores() {
        for(Participant participant : players){
            participant.score = 0;
        }
    }


    @Override
    public void ready() {
        readyCounter++;

        if (isEverybodyReady()) {
            readyCounter = 0;

            if (isGameOver()) {
                state = State.Stop;
                playerProxy.gameOver(players);
            } else {
                sendNextQuestion();
            }
        }
    }


    @Override
    public void answer(boolean correct) {
        //ignoring for now
    }


    private boolean isEverybodyReady() {
        return readyCounter == players.size();
    }


    private boolean isGameOver() {
        return questionCounter == maxQuestions;
    }


    public void answer(boolean correct, String playerName) {
        if (correct) {
            playerProxy.success(playerName);
        } else {
            if(question.isBlocked()) {
                playerProxy.everyoneFailed();
            }
        }
    }


    public void handleAnswer(boolean isCorrect, int playerIndex) {
        Log.i(TAG, String.format("handleAnswer: invoked with isCorrect=[%s], playerIndex=[%s]", isCorrect, playerIndex));
        Participant p = players.get(playerIndex);

        if (p.isBlocked()) {
            Log.i(TAG, String.format("handleAnswer: player is blocked, returning"));
            return;
        }

        if (question.isBlocked()) {
            Log.i(TAG, String.format("handleAnswer: question is blocked, returning"));
            return;
        }

        p.block();
        question.block(isCorrect);
        p.adjustScore(isCorrect);

        answer(isCorrect, players.get(playerIndex).name);
    }


    private void sendNextQuestion() {
        questionCounter++;
        readyCounter = 0;

        question = new Question(players, this);
        playerProxy.showQuestion(questionCounter);
        question.startTimer();
    }

}
