package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.GetPlayersResponseHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.GameOverRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerMessageInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.ShowQuestionRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.InterruptRequestHandler;

public class Player implements PlayerActions {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostProxy hostProxy;
    private AppCompatActivity activity;
    private Fragment fragment;


    private Player(InetAddress hostAddress, Host host) {
        hostProxy = new HostProxy(hostAddress, host);
        hostProxy.addPlayerRequestHandler(PlayerMessageInterface.SHOW_QUESTION, new ShowQuestionRequestHandler(this));
        hostProxy.addHostResponseHandler(HostRequestInterface.GET_PLAYERS, new GetPlayersResponseHandler(this));
        hostProxy.addPlayerRequestHandler(PlayerMessageInterface.INTERRUPT, new InterruptRequestHandler(this));
        hostProxy.addPlayerRequestHandler(PlayerMessageInterface.GAME_OVER, new GameOverRequestHandler(this));
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
        }

        if(!instance.isConnected()) {
            Log.i(TAG, "getInstance: not connected. try to reconnect");
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


    public void getPlayers(HostActions.GetPlayersCallback callback) {
        hostProxy.getPlayers(callback);
    }


    public List<String> getPlayers() throws InterruptedException {
        Log.i(TAG, "getPlayers: invoked");
        final List<String> result = new ArrayList<>();
        final Object lock = new Object();


        hostProxy.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void reply(List<String> names) {
                Log.i(TAG, String.format("getPlayers reply invoked with %s names", names.size()));
                for (String name : names) {
                    result.add(name);
                }
                Log.i(TAG, "getPlayers: result is ready");
            }

        });

        Log.i(TAG, "getPlayers: completed");
        return result;
    }


    public void play() {
        hostProxy.play();
    }


    public interface ShowQuestionable {
        void showQuestion();
    }


    @Override
    public void showQuestion() {
        Log.i(TAG, "showQuestion: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((ShowQuestionable) activity).showQuestion();
        }
    }

    public interface ShowTimeoutable {
        void showTimeout();
    }


    @Override
    public void timeout() {
        Log.i(TAG, "timeout: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((ShowTimeoutable) activity).showTimeout();
        }
    }


    public void youLose() {
        Log.i(TAG, "youLose: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((ShowTimeoutable) activity).showTimeout();
        }
    }

    public interface ShowGameOverable {
        void showGameOver(List<Participant> players);
    }


    @Override
    public void gameOver(List<Participant> players) {
        Log.i(TAG, "gameOver: invoked, activity is null? " + (activity == null));
        if(activity != null) {
            ((ShowGameOverable) activity).showGameOver(players);
        }
    }


    public void ready() {
        hostProxy.ready();
    }


    public void answer(boolean correct) {
        hostProxy.answer(correct);
    }

}
