package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;

public class GetPlayersResponse extends Response {
    public GetPlayersResponse(String string) throws JSONException {
        super(string);
    }

    public GetPlayersResponse() {
        super();
        setIdentifier();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestInterface.GET_PLAYERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Object getResult() {
        List<String> names = new ArrayList<>();
        try {
            JSONArray jsonNames = getJSONArray("NAMES");

            for (int i = 0; i < jsonNames.length(); i++) {
                names.add((String) jsonNames.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return names;
    }
}
