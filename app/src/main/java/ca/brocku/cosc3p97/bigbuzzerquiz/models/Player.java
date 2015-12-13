package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.InetAddress;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuestionContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.GetPlayersResponseHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.GameOverRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.InterruptRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerMessageContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.SendPlayerNamesRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.ShowQuestionRequestHandler;

public class Player implements PlayerActions {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostProxy hostProxy;
    private AppCompatActivity activity;
    private Fragment fragment;


    private Player(InetAddress hostAddress, Host host) {
        hostProxy = new HostProxy(hostAddress, host);
        hostProxy.addPlayerRequestHandler(PlayerMessageContract.SHOW_QUESTION, new ShowQuestionRequestHandler(this));
        hostProxy.addHostResponseHandler(HostRequestContract.GET_PLAYERS, new GetPlayersResponseHandler(this));
        hostProxy.addPlayerRequestHandler(PlayerMessageContract.INTERRUPT, new InterruptRequestHandler(this));
        hostProxy.addPlayerRequestHandler(PlayerMessageContract.GAME_OVER, new GameOverRequestHandler(this));
        hostProxy.addPlayerRequestHandler(PlayerMessageContract.SEND_PLAYER_NAMES, new SendPlayerNamesRequestHandler(this));
    }


    public static Player getInstance() throws Exception {
        if (instance != null) {
            return instance;
        } else {
            throw new Exception("instance cannot be null");
        }
    }


    public static Player getInstance(InetAddress hostAddress) {
        return getInstance(hostAddress, null);
    }


    public static Player getInstance(InetAddress hostAddress, Host host) {
        Log.i(TAG, "getInstance with hostProxy argument");

        if (instance == null) {
            Log.i(TAG, "getInstance: instance is null");
            instance = new Player(hostAddress, host);
        } else if (!instance.isConnected()) {
            Log.i(TAG, "getInstance: not connected. try to reconnect with new player");
            instance = new Player(hostAddress, host);
        }

        return instance;
    }


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    public void setActivity(Fragment fragment) {
        this.fragment = fragment;
    }


    public void setConnectedListener(HostConnection.ConnectedListener listener) {
        hostProxy.setConnectedListener(listener);
    }


    public boolean isConnected() {
        return hostProxy.isConnected();
    }


    public void stop() {
        hostProxy.stop();
    }


    public void getPlayers(HostActions.GetPlayersCallback callback) {
        hostProxy.getPlayers(callback);
    }


    public void play(int numberOfQuestions, List<Integer> categories) {
        hostProxy.play(numberOfQuestions, categories);
    }


    @Override
    public void showQuestion(QuestionContract question) {
        Log.i(TAG, "showQuestion: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((Playable) activity).showQuestion(question);
        }
    }


    public interface Playable {
        void showQuestion(QuestionContract question);
        void showTimeout();
        void showSomebodySucceeded(String playerName);
        void showEveryoneFailed();
        void showGameOver(List<Participant> players);
        void updatePlayersNames(List<String> names);
    }


    @Override
    public void timeout() {
        Log.i(TAG, "timeout: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((Playable) activity).showTimeout();
        }
    }


    public void success(String playerName) {
        Log.i(TAG, "success: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((Playable) activity).showSomebodySucceeded(playerName);
        }
    }


    @Override
    public void gameOver(List<Participant> players) {
        Log.i(TAG, "gameOver: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((Playable) activity).showGameOver(players);
        }
    }


    @Override
    public void everyoneFailed() {
        Log.i(TAG, "everyoneFailed: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((Playable) activity).showEveryoneFailed();
        }
    }


    public void ready() {
        hostProxy.ready();
    }


    public void answer(boolean correct) {
        hostProxy.answer(correct);
    }


    public void sendName(String name) {
        hostProxy.sendName(name);
    }


    public void updatePlayerNames(List<String> names) {
        Log.i(TAG, "updatePlayerNames: invoked");

        if(activity != null) {
            ((Playable) activity).updatePlayersNames(names);
        } else {
            Log.i(TAG, "updatePlayerNames: could not find the activity");
        }
    }

}
