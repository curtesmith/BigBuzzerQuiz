package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.Question;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuizDatabase;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.AnswerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.GetPlayersRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.PlayRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.ReadyRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.SendNameRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerProxy;


public class Host implements HostActions, TimeoutListener {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<PlayerConnection.SetupListener> listeners = new ArrayList<>();
    private PlayerProxy playerProxy;
    private List<Participant> players = new ArrayList<>();
    private Turn turn;
    private Stack<Question> questions;
    private QuizDatabase quizDatabase;

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
        playerProxy.addRequestHandler(HostRequestContract.GET_PLAYERS, new GetPlayersRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestContract.PLAY, new PlayRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestContract.READY, new ReadyRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestContract.ANSWER, new AnswerRequestHandler(this));
        playerProxy.addRequestHandler(HostRequestContract.SEND_NAME, new SendNameRequestHandler(this));
    }


    public void setDatabase(QuizDatabase db) {
        quizDatabase = db;
    }


    public static Host getInstance(QuizDatabase db, PlayerConnection.SetupListener listener) {
        if (instance == null) {
            Log.i(TAG, "getInstance: instance is null");
            try {
                instance = new Host(listener);
                instance.setDatabase(db);
                return instance;
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


    public void removePlayer(Sender sender) {
        players.remove(playerProxy.getPlayerIndex(sender));
        playerProxy.sendPlayerNames(players);
    }


    @Override
    public void addPlayer(String name) {
        Participant player = new Participant();
        player.name = name;
        player.score = 0;
        players.add(player);
    }


    public void updateName(String name, int playerIndex) {
        players.get(playerIndex).name = name;
        playerProxy.sendPlayerNames(players);
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
    public void play(int numberOfQuestions, List<Integer> keys) {
        Log.i(TAG, "play: invoked");
        if (state == State.Stop) {
            state = State.Play;
            maxQuestions = numberOfQuestions;
            questionCounter = 0;
            resetPlayerScores();

            questions = getSomeQuestions(numberOfQuestions, keys);
            if(questions.size() > 0) {
                sendNextQuestion(questions.pop());
            }
        }
    }


    private Stack<Question> getSomeQuestions(int numberOfQuestions, List<Integer> keys) {
        return quizDatabase.selectQuestions(numberOfQuestions, keys);
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
                if (questions.size() > 0) {
                    sendNextQuestion(questions.pop());
                }
            }
        }
    }


    @Override
    public void answer(boolean correct) {
        //ignoring for now
    }

    @Override
    public void sendName(String name) {
        //ignoring for now
    }


    public void answer(boolean correct, String playerName) {
        if (correct) {
            playerProxy.success(playerName);
        } else {
            if(turn.isBlocked()) {
                playerProxy.everyoneFailed();
            }
        }
    }


    private boolean isEverybodyReady() {
        return readyCounter == players.size();
    }


    private boolean isGameOver() {
        Log.i(TAG, String.format("isGameOver: counter=%d, maxQuestions=%d ... returning? %s",
                questionCounter, maxQuestions, (questionCounter == maxQuestions)));
        return questionCounter == maxQuestions;
    }


    public void handleAnswer(boolean isCorrect, int playerIndex) {
        Log.i(TAG, String.format("handleAnswer: invoked with isCorrect=[%s], playerIndex=[%s]", isCorrect, playerIndex));
        Participant p = players.get(playerIndex);

        if (p.isBlocked()) {
            Log.i(TAG, String.format("handleAnswer: player is blocked, returning"));
            return;
        }

        if (turn.isBlocked()) {
            Log.i(TAG, String.format("handleAnswer: turn is blocked, returning"));
            return;
        }

        p.block();
        turn.block(isCorrect);
        p.adjustScore(isCorrect);

        answer(isCorrect, players.get(playerIndex).name);
    }


    private void sendNextQuestion(Question question) {
        questionCounter++;
        readyCounter = 0;

        // TODO: 2015-12-09 Get the next turn from cached list retrieved and pass its key to the players
        turn = new Turn(players, this);

        playerProxy.showQuestion(question);
        turn.startTimer();
    }

}
