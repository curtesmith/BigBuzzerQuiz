package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class GetPlayersResponseHandler extends HostResponseHandler {

    public GetPlayersResponseHandler(Player player) {
        super(player);
    }


    @Override
    public void handle(Response response, Sender replyToSender) {
        getPlayers(response);
    }


    private void getPlayers(Response response) {
        try {
            GetPlayersResponse getPlayersResponse = new GetPlayersResponse(response.toString());
            callback.reply(getPlayersResponse.getResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
