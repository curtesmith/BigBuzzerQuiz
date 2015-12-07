package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

public class GetPlayersRequestHandler extends HostRequestHandler {

    public GetPlayersRequestHandler(Host host) {
        super(host);
    }

    @Override
    public void handle(Request request, Request.Callback callback) {
        //we can ignore the request of the request object

        Response response = getPlayers();

        if (response == null) {
            callback.reply(null);
        } else {
            callback.reply(response.toString());
        }
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        Response response = getPlayers();
        response.addSender(replyToSender);
        response.send();
    }


    private Response getPlayers() {
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
}
