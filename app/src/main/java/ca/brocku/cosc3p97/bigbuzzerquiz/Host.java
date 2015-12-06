package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Host {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<ClientProxy.SetupListener> listeners = new ArrayList<>();
    private ClientProxy clientProxy;
    private List<String> players = new ArrayList<>();


    public void addPlayer(String name) {
        players.add(name);
    }


    public List<String> getPlayers() {
        return players;
    }


    public void addListener(ClientProxy.SetupListener listener) {
        listeners.add(listener);
    }


    public void setTcpListener(TcpConnection.TcpListener listener) {
        clientProxy.setTcpListener(listener);
    }


    private Host(ClientProxy.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        clientProxy = new ClientProxy(this, listener);
        clientProxy.setRequestHandler(new ServerProcessor(this, clientProxy));
    }


    public static Host getInstance(ClientProxy.SetupListener listener) {
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


    public Handler getHandler() {
        return clientProxy.getHandler();
    }

}
