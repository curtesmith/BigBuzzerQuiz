package ca.brocku.cosc3p97.bigbuzzerquiz;


import org.json.JSONArray;
import org.json.JSONException;

public class ServerProcessor implements ClientRequestHandler{
    ClientProxy clientProxy;
    GameServer gameServer;

    public ServerProcessor(GameServer server, ClientProxy clientProxy) {
        gameServer = server;
        this.clientProxy = clientProxy;
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
            for (String name : gameServer.getPlayers()) {
                names.put(name);
            }
            response.put("NAMES", names);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        clientProxy.write(response.toString());
    }


    @Override
    public void onSetGameMaster(Request request) {

    }
}
