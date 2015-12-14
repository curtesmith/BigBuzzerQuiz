package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.ResponseHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;


/**
 * Responsible for handling the incoming answer response from the host
 */
public class HostResponseHandler extends ResponseHandler{
    private static final String TAG = "HostResponseHandler";
    Player player;

    /**
     * A constructor which takes a reference to the player that it is serving
     * @param player the player which this handler is serving
     */
    public HostResponseHandler(Player player) {
        super();
        this.player = player;
    }

    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling player using the replyToSender reference passed
     * as an argument.
     * @param response the response details
     * @param replyToSender a reference to the host that is sending the response
     */
    @Override
    public void handle(Response response, Sender replyToSender) {
        throw new RuntimeException("Stub");
    }
}
