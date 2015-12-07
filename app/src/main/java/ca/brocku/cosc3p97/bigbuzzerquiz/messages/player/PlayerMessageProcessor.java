package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostProxy;


public class PlayerMessageProcessor implements PlayerMessageInterface {
    private static final String TAG = "PlayerMessageProcessor";
    private HostProxy hostProxy;


    public PlayerMessageProcessor(HostProxy hostProxy) {
        this.hostProxy = hostProxy;
    }


    @Override
    public void beginGame() {
        // TODO: 2015-12-06 add code to handle this message
    }

}
