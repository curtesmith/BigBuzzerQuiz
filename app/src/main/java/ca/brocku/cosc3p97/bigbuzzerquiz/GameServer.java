package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class GameServer implements Handler.Callback {
    private static final String TAG = "GameServer";

    private List<Player> players;
    private ServerSocketHandler socketHandler;
    private List<TcpConnection> tcpManagers = new ArrayList<>();
    private Handler handler = new Handler(this);
    private static GameServer instance = null;
    private List<SetupListener> listeners = new ArrayList<>();
    private TcpConnection.TcpListener tcpListener;


    public interface SetupListener {
        void onSetup();
    }


    public void addListener(SetupListener listener) {
        Log.i(TAG, "addListener: invoked");
        listeners.add(listener);
    }


    public void setTcpListener(TcpConnection.TcpListener listener) {
        tcpListener = listener;
    }


    private GameServer(SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        addListener(listener);
        startServerSocket();
    }


    public static GameServer getInstance(SetupListener listener) {
        if(instance == null) {
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
        return handler;
    }


    private void startServerSocket() throws Exception{
        Log.i(TAG, "startServerSocket: invoked");

        try {
            socketHandler = new ServerSocketHandler(handler);
            socketHandler.addListener(new ServerSocketHandler.ServerSocketListener() {
                @Override
                public void onSetup() {
                    Log.i(TAG, "startServerSocket: SERVER socket is ready, initiating callback");
                    callback();
                }
            });
            socketHandler.start();
        } catch (Exception e) {
            Log.e(TAG, "message = {" + e.getMessage() + "}");
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        Log.i(TAG, "handleMessage: invoked");
        TcpConnection t;

        switch(msg.what) {
            case TcpConnection.HANDLE:
                if(msg.arg1 == TcpConnection.SERVER) {
                    tcpManagers.add((TcpConnection) msg.obj);
                    Log.i(TAG, "handleMessage: HANDLE SERVER - write hello CLIENT");
                    ((TcpConnection) msg.obj).write("Hello CLIENT#" + tcpManagers.size() + ", from the SERVER");
                    callback();
                } else {
                    Log.i(TAG, "handleMessage: HANDLE CLIENT - write hello SERVER");
                    tcpListener.onConnected((TcpConnection) msg.obj);
                }
                break;
            case TcpConnection.MESSAGE_READ:
                if(msg.arg1 == TcpConnection.SERVER) {
                    Log.i(TAG, String.format("handleMessage: Type {} A message has been read {%s}", (String) msg.obj));
                    onRead((String) msg.obj);
                } else {
                    tcpListener.onRead((String) msg.obj);
                }
                break;
            case TcpConnection.DISCONNECTED:
                t = (TcpConnection) msg.obj;
                if(msg.arg1 == TcpConnection.SERVER) {
                    tcpManagers.remove(t);
                    Log.i(TAG, "handleMessage: TcpConnection is disconnected so removing him from the list. Size is now " + tcpManagers.size());
                } else {
                    tcpListener.onDisconnected();
                }
                break;
        }

        return true;
    }
    
    
    private void onRead(String message) {
        Log.i(TAG, "onRead: invoked");
        // TODO: 2015-12-05 depending on the request type, do some work then respond if necessary
        switch (message) {
            case GameProxy.GET_PLAYERS:
                write("Bobby,Danny,Mickey");
        }
    }


    private void callback() {
        Log.i(TAG, "callback: invoked");
        for(SetupListener listener : listeners) {
            listener.onSetup();
        }
    }


    public void write(String message) {
        Log.i(TAG, "write: invoked");
        for(TcpConnection tcpManager : tcpManagers) {
            tcpManager.write(message);
        }
    }

}
