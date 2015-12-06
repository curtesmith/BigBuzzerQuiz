package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;
import java.util.List;

public class HostProxy implements Handler.Callback, TcpConnection.Listener, HostActions {
    private static final String TAG = "HostProxy";
    private ClientSocketThread socketHandler;
    private TcpConnection tcpManager;
    private Player.CallbackListener getPlayerCallback;
    private PlayerMessageProcessor messageProcessor;


    public HostProxy(InetAddress hostAddress, Host host) {
        if (host != null) {
            Log.i(TAG, "ctor: host is not null");
            host.setTcpListener(this);
            socketHandler = new ClientSocketThread(host.getHandler(), hostAddress);
        } else {
            Log.i(TAG, "ctor: host is null");
            socketHandler = new ClientSocketThread(new Handler(this), hostAddress);
        }

        messageProcessor = new PlayerMessageProcessor(this);

        socketHandler.start();
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case TcpConnection.HANDLE:
                onConnected((TcpConnection) msg.obj);
                break;
            case TcpConnection.MESSAGE_READ:
                onRead((TcpConnection.ReadObject) msg.obj);
                break;
            case TcpConnection.DISCONNECTED:
                onDisconnected((TcpConnection) msg.obj);
                break;
        }

        return true;
    }


    public void write(String message) {
        tcpManager.write(message);
    }


    @Override
    public void onConnected(TcpConnection tcpConnection) {
        tcpManager = tcpConnection;
    }


    @Override
    public void onRead(TcpConnection.ReadObject obj) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", obj.message));

        try {
            JsonMessage message = new JsonMessage(obj.message);
            Log.i(TAG, String.format("onRead: message type is {%s}", message.getType()));

            if (Request.is(message)) {
                handleServerRequest(message);
            } else {
                messageProcessor.handleServerResponse(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleServerRequest(JsonMessage request) {
        Log.i(TAG, String.format("handleServerRequest: invoked for request identifier [%s]",
                request.getIdentifier()));
    }


    @Override
    public void onDisconnected(TcpConnection connection) {

    }

    @Override
    public void addPlayer(String name) {

    }


    // This is the one we want to keep
    @Override
    public void getPlayers(final GetPlayersCallback callback) {
        messageProcessor.createRequest(HostMessageInterface.GET_PLAYERS,
                new Player.CallbackListener() {
                    @Override
                    public void onCallback(Object object) {
                        callback.callback(((List<String>) object));
                    }
                });

//        getPlayers(new Player.CallbackListener() {
//            @Override
//            public void onCallback(Object object) {
//                callback.callback(((List<String>) object));
//            }
//        });
    }


    //this is the one we want to trash
    public void getPlayers(Player.CallbackListener callback) {
        getPlayerCallback = callback;
        Request request = new GetPlayersRequest();
        write(request.toString());
    }

}
