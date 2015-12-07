package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.RequestBuilder;

public class HostRequestBuilder extends RequestBuilder implements HostActions {
    private static final String TAG = "HostRequestBuilder";
    private HashMap<String, Request.Callback> callbacks = new HashMap<>();
    private HostProxy hostProxy;


    public HostRequestBuilder(HostProxy hostProxy) {
        this.hostProxy = hostProxy;
    }


    public void build(String requestID, final Request.Callback callback) {
        switch(requestID) {
            case HostRequestInterface.GET_PLAYERS:
                callbacks.put(HostRequestInterface.GET_PLAYERS, callback);

                getPlayers(new GetPlayersCallback() {
                    @Override
                    public void reply(List<String> names) {
                        callback.reply(names);
                    }
                });
                break;
            case HostRequestInterface.PLAY:
                play();
                break;
        }
    }


    @Override
    public void handleResponse(JsonMessage jsonMessage) {
        Log.i(TAG, String.format("handleServerResponse: invoked with response identifier {%s}",
                jsonMessage.getIdentifier()));

        switch (jsonMessage.getIdentifier()) {
            case HostRequestInterface.GET_PLAYERS:
                if (callbacks.containsKey(HostRequestInterface.GET_PLAYERS)) {
                    try {
                        GetPlayersResponse response = new GetPlayersResponse(jsonMessage.toString());
                        callbacks.get(HostRequestInterface.GET_PLAYERS).reply(response.getResult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }


    @Override
    public void addPlayer(String name) {

    }


    @Override
    public void getPlayers(GetPlayersCallback callback) {
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }


    @Override
    public void play() {

    }
}
