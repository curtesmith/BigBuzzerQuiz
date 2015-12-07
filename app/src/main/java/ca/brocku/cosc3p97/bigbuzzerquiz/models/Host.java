package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostMessageProcessor;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerProxy;


public class Host implements HostActions {
    private static final String TAG = "Host";
    private static Host instance = null;
    private List<PlayerProxy.SetupListener> listeners = new ArrayList<>();
    private PlayerProxy playerProxy;
    private List<String> players = new ArrayList<>();
    public enum State {
        Play, Stop
    }
    private State state = State.Stop;


    private Host(PlayerProxy.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        playerProxy = new PlayerProxy(this, listener);
        playerProxy.setHostMessageInterface(new HostMessageProcessor(this, playerProxy));
        playerProxy.setRequestHandler(new HostRequestHandler(this));
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


    public void addListener(PlayerProxy.SetupListener listener) {
        listeners.add(listener);
    }


    public void setTcpListener(TcpConnection.Listener listener) {
        playerProxy.setPlayerTcpListener(listener);
    }


    public Handler getHandler() {
        return playerProxy.getThreadHandler();
    }


    @Override
    public void addPlayer(String name) {
        players.add(name);
    }


    @Override
    public void getPlayers(GetPlayersCallback callback) {
        callback.reply(players);
    }


    @Override
    public void play() {
        if(state == State.Stop) {
            state = State.Play;
            playerProxy.beginGame();
        }
    }

}
