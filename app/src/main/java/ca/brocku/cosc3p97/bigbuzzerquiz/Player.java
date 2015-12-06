package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.util.Log;

import java.net.InetAddress;

public class Player {
    private static final String TAG = "Player";
    private static Player instance = null;
    private HostProxy hostProxy;


    private Player(InetAddress hostAddress, Host host) {
        hostProxy = new HostProxy(hostAddress, host);
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
        Log.i(TAG, "getInstance with host argument");
        if (instance == null) {
            instance = new Player(hostAddress, host);
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
