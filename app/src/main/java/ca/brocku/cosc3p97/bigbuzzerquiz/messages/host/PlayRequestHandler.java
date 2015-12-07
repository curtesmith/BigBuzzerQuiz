package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class PlayRequestHandler extends HostRequestHandler {

    public PlayRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Request.Callback callback) {
        Response response = null;
        play();

        switch (request.getIdentifier()) {
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

    public void play() {
        host.play();
    }
}
