package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class PlayerProxy implements Handler.Callback, TcpConnection.Listener {
    private static final String TAG = "PlayerProxy";

    private ServerSocketThread serverSocketThread;
    private List<TcpConnection> tcpConnections = new ArrayList<>();
    private Handler threadHandler = new Handler(this);
    private List<SetupListener> listeners = new ArrayList<>();
    private TcpConnection.Listener playerTcpListener;
    private HostMessageInterface requestHandler;
    private Host host;


    public PlayerProxy(Host host, SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        this.host = host;
        addListener(listener);
        startServerSocket();
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

    public void setRequestHandler(HostMessageInterface requestHandler) {
        this.requestHandler = requestHandler;
    }


    public void setPlayerTcpListener(TcpConnection.Listener listener) {
        playerTcpListener = listener;
    }


    public Handler getThreadHandler() {
        return threadHandler;
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

        requestHandler.execute(obj.message, new Callback() {
            @Override
            public void done(String message) {
                if (message != null) {
                    write(obj.conn, message);
                }
            }
        });

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


    public void write(TcpConnection conn, String message) {
        conn.write(message);
    }


    public interface SetupListener {
        void onSetup(Host host);
    }


    private void callback() {
        Log.i(TAG, "callback: invoked");
        for (SetupListener listener : listeners) {
            listener.onSetup(host);
        }
    }


    interface Callback {
        void done(String message);
    }
}
