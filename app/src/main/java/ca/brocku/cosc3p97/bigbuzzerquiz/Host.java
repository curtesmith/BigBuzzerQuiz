package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Host {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<PlayerProxy.SetupListener> listeners = new ArrayList<>();
    private PlayerProxy playerProxy;
    private List<String> players = new ArrayList<>();


    private Host(PlayerProxy.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        playerProxy = new PlayerProxy(this, listener);
        playerProxy.setRequestHandler(new ServerProcessor(this, playerProxy));
    }


    public static Host getInstance(PlayerProxy.SetupListener listener) {
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

    public void addPlayer(String name) {
        players.add(name);
    }


    public List<String> getPlayers() {
        return players;
    }


    public void addListener(PlayerProxy.SetupListener listener) {
        listeners.add(listener);
    }


    public void setTcpListener(TcpConnection.Listener listener) {
        playerProxy.setPlayerTcpListener(listener);
    }


    public Handler getHandler() {
        return playerProxy.getThreadHandler();
    }

}
