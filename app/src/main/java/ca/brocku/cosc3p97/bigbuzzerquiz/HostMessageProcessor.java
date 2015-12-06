package ca.brocku.cosc3p97.bigbuzzerquiz;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

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
            }

            callback.done(response.toString());

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
    public void getPlayers(GetPlayersCallback callback) {

    }
}
