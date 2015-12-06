package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.util.Log;

import java.net.InetAddress;

public class Player {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostProxy hostProxy;


    private Player(InetAddress host, Host gameServer) {
        hostProxy = new HostProxy(host, gameServer);
    }


    public static Player getInstance() throws Exception {
        if (instance != null) {
            return instance;
        } else {
            throw new Exception("instance cannot be null");
        }
    }


    public static Player getInstance(InetAddress host) {
        return getInstance(host, null);
    }


    public static Player getInstance(InetAddress host, Host gameServer) {
        Log.i(TAG, "getInstance with host argument");
        if (instance == null) {
            instance = new Player(host, gameServer);
        }

        return instance;
    }


    public void getPlayers(final CallbackListener callback) {
        hostProxy.getPlayers(new CallbackListener() {
            @Override
            public void onCallback(Object result) {
                callback.onCallback(result);
            }
        });
    }


    public interface CallbackListener {
        void onCallback(Object result);
    }
}
