package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import org.json.JSONException;

public class Response extends JsonMessage {
    public static final String RESPONSE = "RESPONSE";


    public Response(String string) throws JSONException {
        super(string);
    }


    public Response() {
        super();
        setType();
    }

    public void setType() {
        try {
            put(JsonMessage.TYPE, RESPONSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Object getResult() {
        throw new RuntimeException("Stub");
    }

}
