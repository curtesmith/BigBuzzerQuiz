package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;

import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.RequestBuilder;

public class HostRequestBuilder extends RequestBuilder implements HostActions {
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
                getPlayers(new GetPlayersCallback() {
                    @Override
                    public void reply(List<String> names) {
                        callback.reply(names);
                    }
                });
                break;
            case HostRequestInterface.PLAY:
                play();
                break;
        }
    }


    @Override
    public void addPlayer(String name) {

    }


    @Override
    public void getPlayers(GetPlayersCallback callback) {
        Request request = new GetPlayersRequest();
        hostProxy.write(request.toString());
    }


    @Override
    public void play() {

    }
}
