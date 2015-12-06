package ca.brocku.cosc3p97.bigbuzzerquiz;


import org.json.JSONArray;
import org.json.JSONException;

public class ServerProcessor implements ClientRequestHandler{
    PlayerProxy playerProxy;
    Host host;

    public ServerProcessor(Host server, PlayerProxy playerProxy) {
        host = server;
        this.playerProxy = playerProxy;
    }


    @Override
    public void execute(Request request) {
        try {
            switch (request.getIdentifier()) {
                case GetPlayersRequest.IDENTIFIER:
                    this.onGetPlayers(new GetPlayersRequest(request.toString()));
                    break;
                case SetGameMasterRequest.IDENTIFIER:
                    this.onSetGameMaster(new SetGameMasterRequest(request.toString()));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGetPlayers(Request request) {
        GetPlayersResponse response = new GetPlayersResponse();

        try {
            JSONArray names = new JSONArray();
            for (String name : host.getPlayers()) {
                names.put(name);
            }
            response.put("NAMES", names);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        playerProxy.write(response.toString());
    }


    @Override
    public void onSetGameMaster(Request request) {

    }
}
