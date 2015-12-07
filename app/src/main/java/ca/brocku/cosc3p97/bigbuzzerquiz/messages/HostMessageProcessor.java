package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.GetPlayersResponse;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.HostMessageInterface;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.PlayerProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.Response;

public class HostMessageProcessor implements HostMessageInterface {
    PlayerProxy playerProxy;
    HostActions host;

    public HostMessageProcessor(HostActions host, PlayerProxy playerProxy) {
        this.host = host;
        this.playerProxy = playerProxy;
    }


    @Override
    public void execute(String string, PlayerProxy.Callback callback) {
        try {
            Request request = new Request(string);
            Response response = null;

            switch (request.getIdentifier()) {
                case HostMessageInterface.GET_PLAYERS:
                    response = getPlayers();
                    break;
                case HostMessageInterface.PLAY:
                    play();
                    break;
            }

            if(response == null) {
                callback.done(null);
            } else {
                callback.done(response.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Response getPlayers() {
        final GetPlayersResponse response = new GetPlayersResponse();

        host.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void callback(List<String> names) {
                try {
                    JSONArray jsonNames = new JSONArray();
                    for (String name : names) {
                        jsonNames.put(name);
                    }
                    response.put("NAMES", jsonNames);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return response;
    }

    @Override
    public void play() {
        host.play();
    }
}
