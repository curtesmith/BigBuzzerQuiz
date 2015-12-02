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
    private List<TcpManager> tcpManagers = new ArrayList<>();
    private Handler handler = new Handler(this);
    private static GameServer instance = null;
    private List<GameServerListener> listeners = new ArrayList<>();

    static {
        try {
            instance = new GameServer();
        } catch (Exception e) {
            Log.e(TAG, "message = " + e.getMessage() + "}");
            e.printStackTrace();
        }
    }


    public interface GameServerListener {
        void onSetup();
    }

    public void addListener(GameServerListener listener) {
        listeners.add(listener);
    }


    private GameServer() throws Exception {
        startServerSocket();
    }


    public static GameServer getInstance() {
        return instance;
    }


    public Handler getHandler() {
        return handler;
    }


    private void startServerSocket() throws Exception{
        Log.i(TAG, "startServerSocket has been invoked");

        try {
            socketHandler = new ServerSocketHandler(handler);
            socketHandler.addListener(new ServerSocketHandler.ServerSocketListener() {
                @Override
                public void onSetup() {
                    Log.i(TAG, "server socket is ready, initiating callback");
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
        switch(msg.what) {
            case TcpManager.HANDLE:
                if(msg.arg1 == TcpManager.SERVER_MODE) {
                    tcpManagers.add((TcpManager) msg.obj);
                    ((TcpManager) msg.obj).write(("Hello client#" + tcpManagers.size() + ", from the server").getBytes());
                } else {
                    ((TcpManager) msg.obj).write("Hello Server, from the client".getBytes());
                }
                break;
            case TcpManager.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg2);
                Log.i(TAG, "A message has been read {" + readMessage + "}");
                break;
            case TcpManager.DISCONNECTED:
                if(msg.arg1 == TcpManager.SERVER_MODE) {
                    tcpManagers.remove(msg.obj);
                    Log.i(TAG, "TcpManager is disconnected so removing him from the list. Size is now " + tcpManagers.size());
                }
                break;
        }

        return true;
    }


    private void callback() {
        for(GameServerListener listener : listeners) {
            listener.onSetup();
        }
    }


    public void write(String message) {
        for(TcpManager tcpManager : tcpManagers) {
            tcpManager.write(message.getBytes());
        }
    }

}
