package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class PlayerConnection implements Handler.Callback, TcpConnection.Listener, Sender {
    private static final String TAG = "PlayerConnection";
    private Host host;
    private Handler threadHandler = new Handler(this);
    private List<SetupListener> listeners = new ArrayList<>();
    private ServerSocketThread serverSocketThread;
    private TcpConnection.Listener playerTcpListener;
    private List<TcpConnection> tcpConnections = new ArrayList<>();
    private PlayerProxy playerProxy;


    public interface SetupListener {
        void onSetup(Host host);
    }


    public PlayerConnection(Host host, SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        this.host = host;
        addListener(listener);
        startServerSocket();
    }


    public Handler getThreadHandler() {
        return threadHandler;
    }


    public void setPlayerTcpListener(TcpConnection.Listener listener) {
        playerTcpListener = listener;
    }


    public void setPlayerProxy(PlayerProxy proxy) {
        this.playerProxy = proxy;
    }


    public void addListener(SetupListener listener) {
        Log.i(TAG, "addListener: invoked");
        listeners.add(listener);
    }


    private void startServerSocket() throws Exception {
        Log.i(TAG, "startServerSocket: invoked");

        try {
            serverSocketThread = new ServerSocketThread(threadHandler);
            serverSocketThread.addListener(new ServerSocketThread.ServerSocketListener() {
                @Override
                public void onSetup() {
                    callback();
                }
            });
            serverSocketThread.start();
        } catch (Exception e) {
            Log.e(TAG, "exception message = {" + e.getMessage() + "}");
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        Log.i(TAG, "handleMessage: invoked");
        TcpConnection t;

        switch (msg.what) {
            case TcpConnection.HANDLE:
                if (msg.arg1 == TcpConnection.SERVER) {
                    onConnected((TcpConnection) msg.obj);
                } else {
                    playerTcpListener.onConnected((TcpConnection) msg.obj);
                }
                break;
            case TcpConnection.MESSAGE_READ:
                if (msg.arg1 == TcpConnection.SERVER) {
                    onRead((TcpConnection.ReadObject) msg.obj);
                } else {
                    playerTcpListener.onRead((TcpConnection.ReadObject) msg.obj);
                }
                break;
            case TcpConnection.DISCONNECTED:
                if (msg.arg1 == TcpConnection.SERVER) {
                    onDisconnected((TcpConnection) msg.obj);
                } else {
                    playerTcpListener.onDisconnected(null);
                }
                break;
        }

        return true;
    }


    @Override
    public void onConnected(TcpConnection connection) {
        tcpConnections.add(connection);
        host.addPlayer(String.format("Player #%d", tcpConnections.size()));
        callback();
    }


    @Override
    public void onRead(final TcpConnection.ReadObject obj) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", obj.message));

        try {
            JsonMessage jsonMessage = new JsonMessage(obj.message);
            if (jsonMessage.getType().equals(Request.REQUEST)) {
                playerProxy.handleHostRequest(new Request(jsonMessage.toString()), this);

//                requestHandlers.get(jsonMessage.getIdentifier())
//                        .handle(new Request(obj.message), obj.conn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDisconnected(TcpConnection connection) {
        tcpConnections.remove(connection);
        Log.i(TAG, "handleMessage: TcpConnection is disconnected so removing him from the list. Size is now " + tcpConnections.size());
    }


    @Override
    public void send(String message) {
        write(message);
    }


    public void write(String message) {
        Log.i(TAG, "write: invoked");
        for (TcpConnection tcpManager : tcpConnections) {
            tcpManager.write(message);
        }
    }


    private void callback() {
        Log.i(TAG, "reply: invoked");
        for (SetupListener listener : listeners) {
            listener.onSetup(host);
        }
    }
}
