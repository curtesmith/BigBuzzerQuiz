package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class HostRequestHandler extends RequestHandler {
    Host host;

    public HostRequestHandler(Host host) {
        this.host = host;
    }


    @Override
    public void handle(Request request, Callback callback) {
        Response response = null;

        switch (request.getIdentifier()) {
            case HostMessageInterface.GET_PLAYERS:
                response = getPlayers();
                break;
            case HostMessageInterface.PLAY:
                play();
                break;
        }

        if (response == null) {
            callback.reply(null);
        } else {
            callback.reply(response.toString());
        }
    }


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


    public void play() {
        host.play();
    }
}
