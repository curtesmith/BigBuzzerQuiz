package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;


import org.json.JSONException;

public class Response extends JSONMessage implements Sendable {
    public static final String RESPONSE = "RESPONSE";
    private Sender sender;

    public Response(String string) throws JSONException {
        super(string);
    }


    public Response() {
        super();
        setType();
    }

    public void setType() {
        try {
            put(JSONMessage.TYPE, RESPONSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Object getResult() {
        throw new RuntimeException("Stub");
    }


    @Override
    public void send() {
        sender.send(this.toString());
    }


    @Override
    public void addSender(Sender sender) {
        this.sender = sender;
    }
}
