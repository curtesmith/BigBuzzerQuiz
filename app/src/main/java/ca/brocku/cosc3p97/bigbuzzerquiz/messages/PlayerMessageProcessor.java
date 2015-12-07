package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;

import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;


public class PlayerMessageProcessor implements PlayerMessageInterface {
    private static final String TAG = "PlayerMessageProcessor";
    private HashMap<String, Player.CallbackListener> callbacks = new HashMap<>();
    private HostProxy hostProxy;


    public PlayerMessageProcessor(HostProxy hostProxy) {
        this.hostProxy = hostProxy;
    }


    @Override
    public void createRequest(String requestID, Player.CallbackListener callback) {
        switch (requestID) {
            case HostRequestInterface.GET_PLAYERS:
                getPlayers(callback);
                break;
            case HostRequestInterface.PLAY:
                play();
        }
    }

    @Override
    public void beginGame() {
        // TODO: 2015-12-06 add code to handle this message
    }


    public void getPlayers(Player.CallbackListener callback) {
        callbacks.put(HostRequestInterface.GET_PLAYERS, callback);
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }


    public void play() {
        Request request = new PlayRequest();
        hostProxy.write(request.toString());
    }


    @Override
    public void handleServerResponse(JsonMessage jsonMessage) {
        Log.i(TAG, String.format("handleServerResponse: invoked with response identifier {%s}",
                jsonMessage.getIdentifier()));

        switch (jsonMessage.getIdentifier()) {
            case HostRequestInterface.GET_PLAYERS:
                if (callbacks.containsKey(HostRequestInterface.GET_PLAYERS)) {
                    try {
                        GetPlayersResponse response = new GetPlayersResponse(jsonMessage.toString());
                        callbacks.get(HostRequestInterface.GET_PLAYERS).onCallback(response.getResult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
