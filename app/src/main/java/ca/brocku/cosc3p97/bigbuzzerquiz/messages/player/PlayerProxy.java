package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import android.os.Handler;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuestionContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Participant;


/**
 * Responsible for managing the requests from the host to the player and from the player back to the
 * host
 */
public class PlayerProxy implements PlayerActions {
    private static final String TAG = "PlayerProxy";
    private PlayerConnection connection;
    private HashMap<String, RequestHandler> requestHandlers = new HashMap<>();


    /**
     * Constructor which takes a reference of the host itself as well as a listener which
     * will be invoked when the onSetup event is fired from the player's connection handler
     * @param host a reference to the host object
     * @param listener a reference to the onSetup listener
     */
    public PlayerProxy(Host host, PlayerConnection.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        connection = new PlayerConnection(host, listener);
        connection.setPlayerProxy(this);
    }


    /**
     * Add a request handler for a specific request from the player
     * @param key id of the request
     * @param requestHandler a reference to the handler that will be be invoked when a matching
     *                             request is made
     */
    public void addRequestHandler(String key, RequestHandler requestHandler) {
        requestHandlers.put(key, requestHandler);
    }


    /**
     * Assign a listener that can be invoked when specific Tcp Connection events occur
     * @param listener a reference to the Tcp connection listener
     */
    public void setPlayerTcpListener(TcpConnection.Listener listener) {
        connection.setPlayerTcpListener(listener);
    }


    /**
     * Retrieve a reference to the thread handler associated with the underlying connection
     * @return the connection thread handler
     */
    public Handler getThreadHandler() {
        return connection.getThreadHandler();
    }


    /**
     * Get the player list index of the player in the list that is associated with the Sender
     * reference that is passed in as an argument
     * @param sender the Sender reference of this player proxy
     * @return an integer that is the index of this player in the player list
     */
    public int getPlayerIndex(Sender sender) {
        TcpConnection conn = (TcpConnection) sender;
        return connection.getConnectionIndex(conn);
    }


    /**
     * Search through and invoke the appropriate host request handler that is associated
     * with the request identifier
     * @param request request details
     * @param replyToSender a reference to the host connection where the request was read
     */
    public void handleHostRequest(Request request, Sender replyToSender) {
        requestHandlers.get(request.getIdentifier())
                .handle(request, replyToSender);
    }


    /**
     * Makes a call to the player to present the question details
     * @param question contains the question details
     */
    @Override
    public void showQuestion(QuestionContract question) {
        Log.i(TAG, "showQuestion: invoked");
        ShowQuestionRequest request = new ShowQuestionRequest();

        request.serialize(question);

        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the player to update the list of players
     * @param players this list of players
     */
    public void sendPlayerNames(List<Participant> players) {
        List<String> names = new ArrayList<>();
        for (Participant participant : players) {
            names.add(participant.name);
        }
        SendPlayerNamesRequest request = new SendPlayerNamesRequest();
        request.serialize(names);
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the player to present the a timeout alert
     */
    @Override
    public void timeout() {
        Log.i(TAG, "timeout: invoked");
        InterruptRequest request = new InterruptRequest();
        try {
            request.put(InterruptRequest.INTERRUPT_TYPE, InterruptRequest.InterruptionType.TIMEOUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the player to present that someone has already answered the question
     * successfully
     * @param playerName the name of the player that answered the question correctly
     */
    public void success(String playerName) {
        Log.i(TAG, "success: invoked");
        InterruptRequest request = new InterruptRequest();
        try {
            request.put(InterruptRequest.INTERRUPT_TYPE, InterruptRequest.InterruptionType.SOMEBODY_SUCCEEDED);
            request.put(InterruptRequest.PLAYER_NAME, playerName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the player to present the game over information
     * @param players the results of each player in the game
     */
    @Override
    public void gameOver(List<Participant> players) {
        Log.i(TAG, String.format("gameOver: invoked with %s players", players.size()));
        GameOverRequest request = new GameOverRequest();
        request.serialize(players);
        request.addSender(connection);
        request.send();
    }


    /**
     * Makes a call to the player to inform them that all participants failed to
     * answer the question successfully
     */
    @Override
    public void everyoneFailed() {
        Log.i(TAG, "everyone: invoked");
        InterruptRequest request = new InterruptRequest();
        request.serialize(InterruptRequest.InterruptionType.EVERYONE_FAILED);
        request.addSender(connection);
        request.send();
    }

}
