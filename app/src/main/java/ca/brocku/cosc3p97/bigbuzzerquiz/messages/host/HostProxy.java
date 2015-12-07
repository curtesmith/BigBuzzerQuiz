package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class HostProxy implements HostActions {
    private static final String TAG = "HostProxy";

    public HostResponseHandler responseHandler;
    private HashMap<String, PlayerRequestHandler> playerRequestHandlers = new HashMap<>();
    private HostConnection connection;


    public HostProxy(InetAddress hostAddress, Host host) {
        connection = new HostConnection(hostAddress, host);
        connection.setHostProxy(this);
        responseHandler = new HostResponseHandler();
    }


    public void setConnectedListener(HostConnection.ConnectedListener listener) {
        connection.setConnectedListener(listener);
    }


    public void addPlayerRequestHandler(String ID, PlayerRequestHandler playerRequestHandler) {
        playerRequestHandlers.put(ID, playerRequestHandler);
    }


    public void handlePlayerRequest(JsonMessage request) throws JSONException {
        Log.i(TAG, String.format("handlePlayerRequest: invoked for request identifier [%s]",
                request.getIdentifier()));

        if(playerRequestHandlers.containsKey(request.getIdentifier())) {
            playerRequestHandlers.get(request.getIdentifier())
                    .handle(new Request(request.toString()), connection);
        }
    }


    @Override
    public void addPlayer(String name) {

    }


    @Override
    public void getPlayers(final GetPlayersCallback callback) {
        GetPlayersRequest request = new GetPlayersRequest();
        request.addSender(connection);
        responseHandler.addCallback(request.getIdentifier(), new Request.Callback() {
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
        request.addSender(connection);
        request.send();
    }

}
