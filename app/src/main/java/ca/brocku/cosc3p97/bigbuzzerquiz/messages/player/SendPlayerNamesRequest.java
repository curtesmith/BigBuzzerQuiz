package ca.brocku.cosc3p97.bigbuzzerquiz.messages.player;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;


public class SendPlayerNamesRequest extends Request {
    public static final String NAMES = "NAMES";


    public SendPlayerNamesRequest(String string) throws JSONException {
        super(string);
    }

    public SendPlayerNamesRequest() {
        super();
    }


    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, PlayerMessageContract.SEND_PLAYER_NAMES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void serialize(List<String> names) {
        try {
            JSONArray jsonNames = new JSONArray();
            for(String name : names) {
                jsonNames.put(name);
            }
            put(SendPlayerNamesRequest.NAMES, jsonNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public List<String> deserialize() {
        List<String> names = new ArrayList<>();
        try {
            JSONArray jsonNames = getJSONArray(SendPlayerNamesRequest.NAMES);

            for (int i = 0; i < jsonNames.length(); i++) {
                names.add((String) jsonNames.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return names;
    }

}
