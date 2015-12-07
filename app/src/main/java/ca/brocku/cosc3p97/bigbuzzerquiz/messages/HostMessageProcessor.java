package ca.brocku.cosc3p97.bigbuzzerquiz.messages;


public class HostMessageProcessor {
    PlayerProxy playerProxy;
    HostActions host;

    public HostMessageProcessor(HostActions host, PlayerProxy playerProxy) {
        this.host = host;
        this.playerProxy = playerProxy;
    }


    public void createRequest(String requestID) {
        switch (requestID) {
            case PlayerMessageInterface.BEGIN_GAME:
                beginGame();
                break;
        }
    }


    private void beginGame() {
        Request request = new BeginGameRequest();
        playerProxy.write(request.toString());
    }
}
