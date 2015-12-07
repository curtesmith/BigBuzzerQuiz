package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestBuilder;

public class HostRequestBuilder extends RequestBuilder {
    private static final String TAG = "HostRequestBuilder";
    private HostProxy hostProxy;
    private HostResponseHandler hostResponseHandler;


    public HostRequestBuilder(HostProxy hostProxy, HostResponseHandler hostResponseHandler) {
        this.hostProxy = hostProxy;
        this.hostResponseHandler = hostResponseHandler;
    }


    public void build(String requestID, final Request.Callback callback) {
        switch(requestID) {
            case HostRequestInterface.GET_PLAYERS:
                hostResponseHandler.addCallback(HostRequestInterface.GET_PLAYERS, callback);
                getPlayers();
                break;
            case HostRequestInterface.PLAY:
                play();
                break;
        }
    }


    public void getPlayers() {
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }



    public void play() {
        Request request = new PlayRequest();
        hostProxy.write(request.toString());
    }
}
