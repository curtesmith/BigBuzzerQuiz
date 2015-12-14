package ca.brocku.cosc3p97.bigbuzzerquiz.messages.host;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Response;

/**
 * A response object responsible for sending a response to the player from the host responding
 * with a the list of names of the players that are connected to the host.
 */
public class GetPlayersResponse extends Response {
    public static final String NAMES = "NAMES";


    /**
     * Constructor which exposes the constructor of the super class and builds the internal
     * JSONObject frpm the string passed in as an argument
     * @param string JSON string of values
     * @throws JSONException
     */
    public GetPlayersResponse(String string) throws JSONException {
        super(string);
    }


    /**
     * Constructor that calls an override for the setIdentifier method
     */
    public GetPlayersResponse() {
        super();
        setIdentifier();
    }


    /**
     * Override the setIdentifier method with the value GET_PLAYERS
     */
    @Override
    public void setIdentifier() {
        try {
            put(JsonMessage.IDENTIFIER, HostRequestContract.GET_PLAYERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create the NAMES attribute and assign to it the value passed as an argument
     * @param toBeSerialized the boolean value to assign to the attribute
     */
    @Override
    public void serialize(Object toBeSerialized) {
        List<String> names = (List<String>) toBeSerialized;

        try {
            JSONArray jsonNames = new JSONArray();
            for (String name : names) {
                jsonNames.put(name);
            }

            put(GetPlayersResponse.NAMES, jsonNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Convert the internal value of the NAMES attribute into a List<String> value and return
     * it to the calling method
     */
    @Override
    public Object deserialize() {
        List<String> names = new ArrayList<>();
        try {
            JSONArray jsonNames = getJSONArray(GetPlayersResponse.NAMES);

            for (int i = 0; i < jsonNames.length(); i++) {
                names.add((String) jsonNames.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return names;
    }


}
