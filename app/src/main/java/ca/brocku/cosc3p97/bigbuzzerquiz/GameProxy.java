package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;

public class GameProxy implements Handler.Callback, TcpConnection.TcpListener {
    private static final String TAG = "GameProxy";
    private ClientSocketHandler socketHandler;
    private TcpConnection tcpManager;
    private Player.CallbackListener getPlayerCallback;


    public GameProxy(InetAddress host, GameServer gameServer) {
        if(gameServer != null) {
            gameServer.setTcpListener(this);
            socketHandler = new ClientSocketHandler(gameServer.getHandler(), host);
        } else {
            socketHandler = new ClientSocketHandler(new Handler(this), host);
        }

        socketHandler.start();
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case TcpConnection.HANDLE:
                onConnected((TcpConnection) msg.obj);
                break;
            case TcpConnection.MESSAGE_READ:
                onRead((String) msg.obj);
                break;
            case TcpConnection.DISCONNECTED:
                onDisconnected((TcpConnection) msg.obj);
                break;
        }

        return true;
    }


    private void write(String message) {
        tcpManager.write(message);
    }


    public void getPlayers(Player.CallbackListener callback) {
        getPlayerCallback = callback;
        Request request = new GetPlayersRequest();
        write(request.toString());
    }


    @Override
    public void onConnected(TcpConnection tcpConnection) {
        tcpManager = tcpConnection;
    }


    @Override
    public void onRead(String string) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", string));

        try {
            JsonMessage message = new JsonMessage(string);
            Log.i(TAG, String.format("onRead: message type is {%s}", message.getType()));

            if (Request.is(message)) {
                handleServerRequest(message);
            } else {
                handleServerResponse(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleServerRequest(JsonMessage request) {
        Log.i(TAG, String.format("handleServerRequest: invoked for request identifier [%s]",
                request.getIdentifier()));
    }


    private void handleServerResponse(JsonMessage jsonMessage) {
        Log.i(TAG, String.format("handleServerResponse: invoked with response identifier {%s}",
                jsonMessage.getIdentifier()));

        switch (jsonMessage.getIdentifier()) {
            case GetPlayersResponse.IDENTIFIER:
                if (getPlayerCallback != null) {
                    try {
                        GetPlayersResponse response = new GetPlayersResponse(jsonMessage.toString());
                        getPlayerCallback.onCallback(response.getResult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }


    @Override
    public void onDisconnected(TcpConnection connection) {

    }
}
