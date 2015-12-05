package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.net.InetAddress;

public class Player {
    private static final String TAG = "Player";
    private static Player instance = null;
    private GameProxy gameProxy;


    private Player(InetAddress host, Handler handler) {
        Log.i(TAG, "ctor invoked");
        gameProxy = new GameProxy(host, handler);
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


    public static Player getInstance(InetAddress host, Handler handler) {
        Log.i(TAG, "getInstance invoked");
        if (instance == null) {
            instance = new Player(host, handler);
            return instance;
        } else {
            return instance;
        }
    }
    

    public void getPlayers(final CallbackListener callback) {
        gameProxy.getPlayers(new CallbackListener() {
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
