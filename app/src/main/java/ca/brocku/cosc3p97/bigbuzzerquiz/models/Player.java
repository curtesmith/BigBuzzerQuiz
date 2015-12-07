package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.BeginGameRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerMessageInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.views.QuestionActivity;

public class Player implements PlayerActions {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostProxy hostProxy;
    private AppCompatActivity activity;


    private Player(InetAddress hostAddress, Host host) {
        hostProxy = new HostProxy(hostAddress, host);
        hostProxy.addPlayerRequestHandler(PlayerMessageInterface.BEGIN_GAME, new BeginGameRequestHandler(this));
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
            instance = new Player(hostAddress, host);
        }

        return instance;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    public List<String> getPlayers() {
        Log.i(TAG, "getPlayers: invoked");
        final List<String> result = new ArrayList<>();

        hostProxy.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void reply(List<String> names) {
                Log.i(TAG, String.format("getPlayers reply invoked with %s names", names.size()));
                for (String name : names) {
                    result.add(name);
                }
            }
        });

        Log.i(TAG, "getPlayers: completed");
        return result;
    }


    public void play() {
        hostProxy.play();
    }


    public void setConnectedListener(HostProxy.ConnectedListener listener) {
        hostProxy.setConnectedListener(listener);
    }


    @Override
    public void beginGame() {
        if(activity != null) {
            Intent intent = new Intent(activity, QuestionActivity.class);
            activity.startActivity(intent);
        }
    }

}
