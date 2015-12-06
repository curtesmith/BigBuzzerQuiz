package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ClientProxy implements Handler.Callback, TcpConnection.TcpListener {
    private static final String TAG = "ClientProxy";

    private ServerSocketHandler socketHandler;
    private List<TcpConnection> tcpConnections = new ArrayList<>();
    private Handler handler = new Handler(this);
    private List<SetupListener> listeners = new ArrayList<>();
    private TcpConnection.TcpListener tcpListener;
    private ClientRequestHandler requestHandler;
    private GameServer gameServer;


    public ClientProxy(GameServer gamseServer, SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        this.gameServer = gamseServer;
        addListener(listener);
        startServerSocket();
    }


    public void setRequestHandler(ClientRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    public void addListener(SetupListener listener) {
        Log.i(TAG, "addListener: invoked");
        listeners.add(listener);
    }


    private void startServerSocket() throws Exception {
        Log.i(TAG, "startServerSocket: invoked");

        try {
            socketHandler = new ServerSocketHandler(handler);
            socketHandler.addListener(new ServerSocketHandler.ServerSocketListener() {
                @Override
                public void onSetup() {
                    callback();
                }
            });
            socketHandler.start();
        } catch (Exception e) {
            Log.e(TAG, "exception message = {" + e.getMessage() + "}");
            e.printStackTrace();
            throw e;
        }
    }


    public void setTcpListener(TcpConnection.TcpListener listener) {
        tcpListener = listener;
    }


    public Handler getHandler() {
        return handler;
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
                    tcpListener.onConnected((TcpConnection) msg.obj);
                }
                break;
            case TcpConnection.MESSAGE_READ:
                if (msg.arg1 == TcpConnection.SERVER) {
                    onRead((String) msg.obj);
                } else {
                    tcpListener.onRead((String) msg.obj);
                }
                break;
            case TcpConnection.DISCONNECTED:
                if (msg.arg1 == TcpConnection.SERVER) {
                    onDisconnected((TcpConnection) msg.obj);
                } else {
                    tcpListener.onDisconnected(null);
                }
                break;
        }

        return true;
    }


    @Override
    public void onConnected(TcpConnection connection) {
        tcpConnections.add(connection);
        gameServer.addPlayer(String.format("Player #%d", tcpConnections.size()));
        callback();
    }


    @Override
    public void onRead(String string) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", string));

        try {
            JsonMessage message = new JsonMessage(string);

            if (Request.is(message)) {
                requestHandler.execute(new Request(message.toString()));
            } else {
                handleClientResponse(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleClientResponse(JsonMessage json) {

    }


    @Override
    public void onDisconnected(TcpConnection connection) {
        tcpConnections.remove(connection);
        Log.i(TAG, "handleMessage: TcpConnection is disconnected so removing him from the list. Size is now " + tcpConnections.size());
    }


    public void write(String message) {
        Log.i(TAG, "write: invoked");
        for (TcpConnection tcpManager : tcpConnections) {
            tcpManager.write(message);
        }
    }


    public interface SetupListener {
        void onSetup();
    }


    private void callback() {
        Log.i(TAG, "callback: invoked");
        for (SetupListener listener : listeners) {
            listener.onSetup();
        }
    }

}
