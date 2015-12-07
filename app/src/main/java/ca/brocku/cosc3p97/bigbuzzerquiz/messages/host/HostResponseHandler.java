package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.ResponseHandler;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;

public class HostResponseHandler extends ResponseHandler{
    private static final String TAG = "HostResponseHandler";
    Player player;


    public HostResponseHandler(Player player) {
        super();
        this.player = player;
    }


    @Override
    public void handle(Response response, Sender replyToSender) {
        throw new RuntimeException("Stub");
    }
}
