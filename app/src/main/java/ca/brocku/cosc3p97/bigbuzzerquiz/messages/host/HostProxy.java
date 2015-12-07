package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.ClientSocketThread;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class HostProxy implements Handler.Callback, TcpConnection.Listener, HostActions, Sender {
    private static final String TAG = "HostProxy";
    private TcpConnection tcpConnection;
    private HostResponseHandler hostResponseHandler;
    private HashMap<String, PlayerRequestHandler> playerRequestHandlers = new HashMap<>();


    public HostProxy(InetAddress hostAddress, Host host) {
        ClientSocketThread thread;

        if (host != null) {
            Log.i(TAG, "ctor: host is not null");
            host.setTcpListener(this);
            thread = new ClientSocketThread(host.getHandler(), hostAddress);
        } else {
            Log.i(TAG, "ctor: host is null");
            thread = new ClientSocketThread(new Handler(this), hostAddress);
        }

        hostResponseHandler = new HostResponseHandler();

        thread.start();
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


    private HostProxy.ConnectedListener connectedListener;


    public void setConnectedListener(HostProxy.ConnectedListener listener) {
        connectedListener = listener;
    }


    @Override
    public void send(String request) {
        tcpConnection.write(request);
    }


    public interface ConnectedListener {
        void onConnected();
    }


    @Override
    public void onConnected(TcpConnection tcpConnection) {
        this.tcpConnection = tcpConnection;
        connectedListener.onConnected();
    }


    @Override
    public void onRead(TcpConnection.ReadObject obj) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", obj.message));

        try {
            JsonMessage message = new JsonMessage(obj.message);
            Log.i(TAG, String.format("onRead: message type is {%s}", message.getType()));

            if (Request.is(message)) {
                handlePlayerRequest(message);
            } else {
                hostResponseHandler.handle(new Response(obj.message));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addPlayerRequestHandler(String ID, PlayerRequestHandler playerRequestHandler) {
        playerRequestHandlers.put(ID, playerRequestHandler);
    }


    private void handlePlayerRequest(JsonMessage request) throws JSONException {
        Log.i(TAG, String.format("handlePlayerRequest: invoked for request identifier [%s]",
                request.getIdentifier()));

        if(playerRequestHandlers.containsKey(request.getIdentifier())) {
            playerRequestHandlers.get(request.getIdentifier()).handle(new Request(request.toString()), null);
        }
    }


    @Override
    public void onDisconnected(TcpConnection connection) {

    }


    @Override
    public void addPlayer(String name) {

    }


    @Override
    public void getPlayers(final GetPlayersCallback callback) {
        GetPlayersRequest request = new GetPlayersRequest();
        request.addSender(this);
        hostResponseHandler.addCallback(request.getIdentifier(), new Request.Callback() {
            @Override
            public void reply(Object result) {
                callback.reply((List<String>) result);
            }
        });

        request.send();
    }


    @Override
    public void play() {
        PlayRequest request = new PlayRequest();
        request.addSender(this);
        request.send();
    }

}
