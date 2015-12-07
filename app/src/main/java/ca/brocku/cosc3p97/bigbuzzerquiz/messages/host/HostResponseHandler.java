package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.ResponseHandler;

public class HostResponseHandler extends ResponseHandler{
    private static final String TAG = "HostResponseHandler";
    HashMap<String, Request.Callback> callbacks = new HashMap<>();


    @Override
    public void handle(Response response) {
        Log.i(TAG, String.format("handleServerResponse: invoked with response identifier {%s}",
                response.getIdentifier()));

        switch (response.getIdentifier()) {
            case HostRequestInterface.GET_PLAYERS:
                getPlayers(response);
                break;
        }
    }


    public void addCallback(String ID, Request.Callback callback) {
        callbacks.put(ID, callback);
    }


    private void getPlayers(Response response) {
        if (callbacks.containsKey(HostRequestInterface.GET_PLAYERS)) {
            try {
                GetPlayersResponse getPlayersResponse = new GetPlayersResponse(response.toString());
                callbacks.get(HostRequestInterface.GET_PLAYERS).reply(getPlayersResponse.getResult());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
