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


public class PlayerProxy implements PlayerActions {
    private static final String TAG = "PlayerProxy";
    private PlayerConnection connection;
    private HashMap<String, RequestHandler> requestHandlers = new HashMap<>();


    public PlayerProxy(Host host, PlayerConnection.SetupListener listener) throws Exception {
        Log.i(TAG, "ctor: invoked");
        connection = new PlayerConnection(host, listener);
        connection.setPlayerProxy(this);
    }


    public void addRequestHandler(String key, RequestHandler requestHandler) {
        requestHandlers.put(key, requestHandler);
    }


    public void setPlayerTcpListener(TcpConnection.Listener listener) {
        connection.setPlayerTcpListener(listener);
    }


    public Handler getThreadHandler() {
        return connection.getThreadHandler();
    }


    public int getPlayerIndex(Sender sender) {
        TcpConnection conn = (TcpConnection) sender;
        return connection.getConnectionIndex(conn);
    }


    public void handleHostRequest(Request request, Sender replyToSender) {
        requestHandlers.get(request.getIdentifier())
                .handle(request, replyToSender);
    }


    @Override
    public void showQuestion(QuestionContract question) {
        Log.i(TAG, "showQuestion: invoked");
        ShowQuestionRequest request = new ShowQuestionRequest();

        request.serialize(question);

        request.addSender(connection);
        request.send();
    }


    public void sendPlayerNames(List<Participant> players) {
        List<String> names = new ArrayList<>();
        for(Participant participant : players) {
            names.add(participant.name);
        }
        SendPlayerNamesRequest request = new SendPlayerNamesRequest();
        request.serialize(names);
        request.addSender(connection);
        request.send();
    }


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


    @Override
    public void gameOver(List<Participant> players) {
        Log.i(TAG, String.format("gameOver: invoked with %s players", players.size()));
        GameOverRequest request = new GameOverRequest();
        request.setPlayers(players);
        request.addSender(connection);
        request.send();
    }


    @Override
    public void everyoneFailed() {
        Log.i(TAG, "everyone: invoked");
        InterruptRequest request = new InterruptRequest();
        try {
            request.put(InterruptRequest.INTERRUPT_TYPE, InterruptRequest.InterruptionType.EVERYONE_FAILED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addSender(connection);
        request.send();
    }

}
