package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class PlayRequestHandler extends HostRequestHandler {

    public PlayRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        play();
    }


    public void play() {
        host.play();
    }

}
