package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;

/**
 * A handler for requests made to the Host
 */

public class HostRequestHandler extends RequestHandler {
    Host host;

    public HostRequestHandler(Host host) {
        this.host = host;
    }


    @Override
    public void handle(Request request, Request.Callback callback) {
        Response response = null;

//        switch (request.getIdentifier()) {
//            case HostRequestInterface.PLAY:
//                play();
//                break;
//        }

        if (response == null) {
            callback.reply(null);
        } else {
            callback.reply(response.toString());
        }
    }


//    public void play() {
//        host.play();
//    }
}
