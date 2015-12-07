package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

import org.json.JSONException;

public class Request extends JsonMessage implements Requestable {
    public static final String REQUEST = "REQUEST";
    private RequestSender sender;
    private Callback callback;


    public Request(String string) throws JSONException {
        super(string);
    }

    public Request() {
        super();
    }


    public void setType() {
        try {
            put(JsonMessage.TYPE, REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean is(JsonMessage message) {
        return message.getType().equals(REQUEST);
    }


    @Override
    public void send() {
        sender.send(this.toString());
    }


    @Override
    public void addSender(RequestSender sender) {
        this.sender = sender;
    }


    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public interface Callback {
        void reply(Object result);
    }
}


