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
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerMessageInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerMessageProcessor;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class HostProxy implements Handler.Callback, TcpConnection.Listener, HostActions {
    private static final String TAG = "HostProxy";
    private ClientSocketThread clientSocketThread;
    private TcpConnection tcpConnection;
    private PlayerMessageInterface messenger;
    private HostRequestBuilder hostRequestBuilder;
    private HostResponseHandler hostResponseHandler;
    private HashMap<String, PlayerRequestHandler> playerRequestHandlers = new HashMap<>();
    private boolean isConnected = false;

    public HostProxy(InetAddress hostAddress, Host host) {
        if (host != null) {
            Log.i(TAG, "ctor: host is not null");
            host.setTcpListener(this);
            clientSocketThread = new ClientSocketThread(host.getHandler(), hostAddress);
        } else {
            Log.i(TAG, "ctor: host is null");
            clientSocketThread = new ClientSocketThread(new Handler(this), hostAddress);
        }

        messenger = new PlayerMessageProcessor(this);
        hostResponseHandler = new HostResponseHandler();
        hostRequestBuilder = new HostRequestBuilder(this, hostResponseHandler);

        clientSocketThread.start();
    }


    public boolean isConnected() {
        return isConnected;
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
        tcpConnection.write(message);
    }

    private HostProxy.ConnectedListener connectedListener;

    public void setConnectedListener(HostProxy.ConnectedListener listener) {
        connectedListener = listener;
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


    public void addPlayerRequestHander(String ID, PlayerRequestHandler playerRequestHandler) {
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
        hostRequestBuilder.build(HostRequestInterface.GET_PLAYERS, new Request.Callback() {
            @Override
            public void reply(Object result) {
                callback.reply((List<String>) result);
            }
        });
    }


    @Override
    public void play() {
        hostRequestBuilder.build(HostRequestInterface.PLAY, new Request.Callback() {
            @Override
            public void reply(Object result) {
                //ignore
            }
        });

//        messenger.createRequest(HostRequestInterface.PLAY,
//                new Player.CallbackListener() {
//                    @Override
//                    public void onCallback(Object object) {
//                        //ignore
//                    }
//                });
    }

}
