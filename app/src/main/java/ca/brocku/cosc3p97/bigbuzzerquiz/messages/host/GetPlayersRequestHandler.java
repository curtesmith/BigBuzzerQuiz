package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

/**
 * Responsible for handling the incoming answer request from the player
 */
public class GetPlayersRequestHandler extends HostRequestHandler {


    /**
     * A constructor which takes a reference to the host that it is serving
     * @param host the host which this handler is serving
     */
    public GetPlayersRequestHandler(Host host) {
        super(host);
    }


    /**
     * Handle the request passed as an argument. If a response is necessary then it can be
     * built and then passed back the calling player using the replyToSender reference passed
     * as an argument.
     * Get the list of players from the host, place them in a response object and send them
     * back to the player the made the request.
     * @param request the request details
     * @param replyToSender a reference to the player that is making the request
     */
    @Override
    public void handle(Request request, Sender replyToSender) {
        Response response = getPlayers();
        response.addSender(replyToSender);
        response.send();
    }


    /**
     * Invoke the appropriate method of the host to process this request from the player
     * @return a response object that can be sent back to the player that submitted the request
     */
    private Response getPlayers() {
        final GetPlayersResponse response = new GetPlayersResponse();

        host.getPlayers(new HostActions.GetPlayersCallback() {
            @Override
            public void reply(List<String> names) {
                response.serialize(names);
            }
        });

        return response;
    }
}
