package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class GameServer {
    private static final String TAG = "GameServer";
    private static GameServer instance = null;
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


    private GameServer(ClientProxy.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        clientProxy = new ClientProxy(this, listener);
        clientProxy.setRequestHandler(new ServerProcessor(this, clientProxy));
    }


    public static GameServer getInstance(ClientProxy.SetupListener listener) {
        if (instance == null) {
            try {
                return new GameServer(listener);
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
