package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

/**
 * A handler for requests made to the Host
 */

public class HostRequestHandler extends RequestHandler implements HostRequestInterface {
    Host host;

    public HostRequestHandler(Host host) {
        this.host = host;
    }


    @Override
    public void handle(Request request, Callback callback) {
        Response response = null;

        switch (request.getIdentifier()) {
            case HostRequestInterface.GET_PLAYERS:
                response = getPlayers();
                break;
            case HostRequestInterface.PLAY:
                play();
                break;
        }

        if (response == null) {
            callback.reply(null);
        } else {
            callback.reply(response.toString());
        }
    }


    @Override
    public Response getPlayers() {
        final GetPlayersResponse response = new GetPlayersResponse();

        host.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void reply(List<String> names) {
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
