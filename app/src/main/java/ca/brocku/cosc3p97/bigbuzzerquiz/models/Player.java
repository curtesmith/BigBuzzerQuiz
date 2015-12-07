package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.HostProxy;

public class Player {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostActions hostActions;


    private Player(InetAddress hostAddress, Host host) {
        this.hostActions = new HostProxy(hostAddress, host);
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
        Log.i(TAG, "getInstance with hostActions argument");
        if (instance == null) {
            instance = new Player(hostAddress, host);
        }

        return instance;
    }


    public List<String> getPlayers() {
        Log.i(TAG, "getPlayers: invoked");
        final List<String> result = new ArrayList<>();

        hostActions.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void callback(List<String> names) {
                Log.i(TAG, String.format("getPlayers callback invoked with %s names", names.size()));
                for (String name : names) {
                    result.add(name);
                }
            }
        });

        Log.i(TAG, "getPlayers: completed");
        return result;
    }


    public void play() {
        hostActions.play();
    }

    public interface CallbackListener {
        void onCallback(Object object);
    }

}
