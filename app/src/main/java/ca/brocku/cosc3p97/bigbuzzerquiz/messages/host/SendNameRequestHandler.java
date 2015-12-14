package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * Responsible for handling the incoming send name request from the player
 */
public class SendNameRequestHandler extends HostRequestHandler {
    public static final String TAG = "SendNameRequestHandler";


    /**
     * A constructor which takes a reference to the host that it is serving
     * @param host the host which this handler is serving
     */
    public SendNameRequestHandler(Host host) {
        super(host);
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling player using the replyToSender reference passed
     * as an argument.
     * @param request the request details
     * @param replyToSender a reference to the player that is making the request
     */
    @Override
    public void handle(Request request, Sender replyToSender) {
        try {
            SendNameRequest sendNameRequest = new SendNameRequest(request.toString());
            String name = (String) sendNameRequest.deserialize();
            updateName(name, host.getPlayerIndex(replyToSender));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invoke the updateName method on the host to update the player's name that is associated with
     * the incoming request
     * @param name the String value to use for the player's name
     * @param playerIndex the player list index of the player that is requesting the update
     */
    private void updateName(String name, int playerIndex) {
        host.updateName(name, playerIndex);
    }

}
