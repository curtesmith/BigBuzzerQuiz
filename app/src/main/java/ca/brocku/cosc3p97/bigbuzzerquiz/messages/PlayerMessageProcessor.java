package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import android.util.Log;

import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class PlayerMessageProcessor implements PlayerMessageInterface {
    private static final String TAG = "PlayerMessageProcessor";
    private Player.CallbackListener getPlayerCallback;
    private List<Player.CallbackListener> callbacks;
    private HostProxy hostProxy;


    public PlayerMessageProcessor(HostProxy hostProxy) {
        this.hostProxy = hostProxy;
    }


    @Override
    public void createRequest(String requestId, Player.CallbackListener callback) {
        switch (requestId) {
            case HostMessageInterface.GET_PLAYERS:
                getPlayers(callback);
                break;
            case HostMessageInterface.PLAY:
                play();
        }
    }

    @Override
    public void begin() {
        // TODO: 2015-12-06 add code to handle this message
    }


    public void getPlayers(Player.CallbackListener callback) {
        getPlayerCallback = callback;
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }


    public void play() {
        Request request = new PlayRequest();
        hostProxy.write(request.toString());
    }


    @Override
    public void execute(String string, HostProxy.Callback callback) {
        try {
            Request request = new Request(string);
            Response response = null;

            switch (request.getIdentifier()) {
                case PlayerMessageInterface.BEGIN:
                    begin();
                    break;
            }

            callback.done(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleServerResponse(JsonMessage jsonMessage) {
        Log.i(TAG, String.format("handleServerResponse: invoked with response identifier {%s}",
                jsonMessage.getIdentifier()));

        switch (jsonMessage.getIdentifier()) {
            case HostMessageInterface.GET_PLAYERS:
                if (getPlayerCallback != null) {
                    try {
                        GetPlayersResponse response = new GetPlayersResponse(jsonMessage.toString());
                        getPlayerCallback.onCallback(response.getResult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
