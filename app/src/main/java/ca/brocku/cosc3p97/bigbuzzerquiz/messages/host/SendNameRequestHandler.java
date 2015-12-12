package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import org.json.JSONException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


public class SendNameRequestHandler extends HostRequestHandler {
    public static final String TAG = "SendNameRequestHandler";


    public SendNameRequestHandler(Host host) {
        super(host);
    }


    @Override
    public void handle(Request request, Sender replyToSender) {
        SendNameRequest sendNameRequest = null;
        try {
            sendNameRequest = new SendNameRequest(request.toString());
            updateName(sendNameRequest.getString(SendNameRequest.NAME), host.getPlayerIndex(replyToSender));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateName(String name, int playerIndex) {
        host.updateName(name, playerIndex);
    }

}
