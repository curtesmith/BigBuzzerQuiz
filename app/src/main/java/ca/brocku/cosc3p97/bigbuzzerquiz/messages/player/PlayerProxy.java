package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.TcpConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


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


    public void handleHostRequest(Request request, Sender replyToSender) {
        requestHandlers.get(request.getIdentifier())
                .handle(request, replyToSender);
    }


    @Override
    public void beginGame() {
        Request request = new BeginGameRequest();
        request.addSender(connection);
        request.send();
    }

}
