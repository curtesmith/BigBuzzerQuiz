package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.GameContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.PlayerRequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * Responsible for managing the requests from the player to the host and from the host back to the
 * player
 */
public class HostProxy implements HostActions {
    private static final String TAG = "HostProxy";
    private HashMap<String, PlayerRequestHandler> playerRequestHandlers = new HashMap<>();
    private HashMap<String, HostResponseHandler> hostResponseHandlers = new HashMap<>();
    private HostConnection connection;


    /**
     * Constructor which takes the address of the remote host and a reference of the host itself
     * @param hostAddress used to connect via socket connection
     * @param host a reference to the host object
     */
    public HostProxy(InetAddress hostAddress, Host host) {
        connection = new HostConnection(hostAddress, host);
        connection.setHostProxy(this);
    }


    /**
     * Assign the listener passed as an argument to listen for the connected event of the connection
     * @param listener object which implements the ConnectedListener interface
     */
    public void setConnectedListener(HostConnection.ConnectedListener listener) {
        connection.setConnectedListener(listener);
    }


    /**
     * Add a request handler for a specific request from the player
     * @param key id of the request
     * @param playerRequestHandler a reference to the handler that will be be invoked when a matching
     *                             request is made
     */
    public void addPlayerRequestHandler(String key, PlayerRequestHandler playerRequestHandler) {
        playerRequestHandlers.put(key, playerRequestHandler);
    }


    /**
     * Add a response handler for a specific response from the host
     * @param key id of the response
     * @param hostResponseHandler a reference to the handler that will be invoked when a matching
     *                            response is received
     */
    public void addHostResponseHandler(String key, HostResponseHandler hostResponseHandler) {
        hostResponseHandlers.put(key, hostResponseHandler);
    }


    /**
     * Invoked when a request is received it will search for a matching request handler and
     * execute it
     * @param request a reference to the request object
     * @throws JSONException
     */
    public void handlePlayerRequest(JsonMessage request) throws JSONException {
        Log.i(TAG, String.format("handlePlayerRequest: invoked for request identifier [%s]",
                request.getIdentifier()));

        if (playerRequestHandlers.containsKey(request.getIdentifier())) {
            playerRequestHandlers.get(request.getIdentifier())
                    .handle(new Request(request.toString()), connection);
        }
    }


    /**
     * Invoked when a response is received it will search for a matching response handler and
     * execute it
     * @param response a reference to the response object
     * @throws JSONException
     */
    public void handleHostResponse(JsonMessage response) throws JSONException {
        Log.i(TAG, String.format("handleHostResponse: invoked for identifier [%s]", response.getIdentifier()));
        if (hostResponseHandlers.containsKey(response.getIdentifier())) {
            hostResponseHandlers.get(response.getIdentifier())
                    .handle(new Response(response.toString()), connection);
        }
    }


    /**
     * Returns the boolean value of the connection status associated with the underlying connection
     * @return boolean value of the connection status
     */
    public boolean isConnected() {
        return connection.isConnected();
    }


    /**
     * Responsible for stopping the underlying connection
     */
    public void stop() {
        connection.stop();
    }



    @Override
    public void addPlayer(String name) {

    }


    /**
     * Makes a call to the host to obtain a list of the connected player names
     * @param callback the callback to invoke when the method processing is completed
     */
    @Override
    public void getPlayers(final GetPlayersCallback callback) {
        GetPlayersRequest request = new GetPlayersRequest();

        HostResponseHandler handler = hostResponseHandlers.get(request.getIdentifier());
        handler.setCallback(new Request.Callback() {
            @Override
            public void reply(Object result) {
                callback.reply((List<String>) result);
            }
        });

        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the host to request to begin a new game
     * @param game details of the game
     */
    @Override
    public void play(GameContract game) {
        PlayRequest request = new PlayRequest();
        request.serialize(game);
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the host to inform it that the current player is ready for
     * the next question
     */
    @Override
    public void ready() {
        ReadyRequest request = new ReadyRequest();
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the host to inform it that the current player has answered
     * the current question either correctly or incorrectly as specified by the boolean
     * value passed as an argument
     * @param correct boolean value indicating if the player answered correctly
     */
    @Override
    public void answer(boolean correct) {
        AnswerRequest request = new AnswerRequest();
        request.serialize(correct);
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the host to submit the name of the current connected player
     * @param name is the name of the player
     */
    @Override
    public void sendName(String name) {
        SendNameRequest request = new SendNameRequest();
        request.serialize(name);
        request.addSender(connection);
        request.send();
    }

}
