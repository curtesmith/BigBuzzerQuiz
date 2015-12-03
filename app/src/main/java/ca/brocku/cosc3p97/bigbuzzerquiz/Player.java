package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.InetAddress;

public class Player implements Handler.Callback{
    private static final String TAG = "Player";
    private String name;
    private ClientSocketHandler socketHandler;
    private static Player instance = null;
    private TcpConnection tcpManager;

    private Player(InetAddress host, Handler handler) {
        Log.i(TAG, "ctor invoked");
        if (handler == null) {
            handler = new Handler(this);
        }
        socketHandler = new ClientSocketHandler(handler, host);
        socketHandler.start();
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


    public String getName() {
        return name;
    }


    public void getListOfPlayers() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case TcpConnection.HANDLE:
                Log.i(TAG, "HANDLE caught");
                tcpManager = (TcpConnection) msg.obj;
                ((TcpConnection) msg.obj).write("Hello Server, from the client");
                break;
            case TcpConnection.MESSAGE_READ:
                Log.i(TAG, "MESSAGE_READ caught");
                Log.i(TAG, "message received ={" + msg.obj + "}");
        }

        return true;
    }
}
