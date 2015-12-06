package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.util.Log;

import org.json.JSONException;

public class PlayerMessageProcessor implements PlayerMessageInterface {
    private static final String TAG = "PlayerMessageProcessor";
    private Player.CallbackListener getPlayerCallback;
    private HostProxy hostProxy;


    public PlayerMessageProcessor(HostProxy hostProxy) {
        this.hostProxy = hostProxy;
    }


    public void createRequest(String requestId, Player.CallbackListener callback) {
        switch(requestId) {
            case HostMessageInterface.GET_PLAYERS:
                getPlayers(callback);
                break;
        }
    }


    public void getPlayers(Player.CallbackListener callback) {
        getPlayerCallback = callback;
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }


    @Override
    public void execute(String string, HostProxy.Callback callback) {
        try {
            Request request = new Request(string);
            Response response = null;

            switch (request.getIdentifier()) {
                case PlayerMessageInterface.LETS_PLAY:
                    //response = getPlayers();
                    break;
            }

            callback.done(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void letsPlay() {

    }


    public void handleServerResponse(JsonMessage jsonMessage) {
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

}
