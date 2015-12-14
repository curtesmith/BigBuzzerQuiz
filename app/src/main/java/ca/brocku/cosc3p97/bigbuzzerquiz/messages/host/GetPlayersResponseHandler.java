package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONException;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

/**
 * Responsible for handling the incoming answer response from the host
 */
public class GetPlayersResponseHandler extends HostResponseHandler {


    /**
     * A constructor which takes a reference to the player that it is serving
     * @param player the player which this handler is serving
     */
    public GetPlayersResponseHandler(Player player) {
        super(player);
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling player using the replyToSender reference passed
     * as an argument.
     * Return the list of players from the host, place them in a response object and then
     * deserialize them into a list<String> for the calling object to consume.
     * @param response the response details
     * @param replyToSender a reference to the host that is sending the response
     */
    @Override
    public void handle(Response response, Sender replyToSender) {
        try {
            GetPlayersResponse getPlayersResponse = new GetPlayersResponse(response.toString());
            List<String> names = (List<String>) getPlayersResponse.deserialize();
            respond(names);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invoke the reply method of the callback object that is expecting the response from the host.
     * @param names the list of names that were requested
     */
    private void respond(List<String> names) {
        callback.reply(names);
    }

}
