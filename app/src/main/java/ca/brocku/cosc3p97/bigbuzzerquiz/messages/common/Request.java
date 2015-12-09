package ca.brocku.cosc3p97.bigbuzzerquiz.messages.common;

import org.json.JSONException;

public class Request extends JSONMessage implements Sendable {
    public static final String REQUEST = "REQUEST";
    private Sender sender;
    private Callback callback;


    public Request(String string) throws JSONException {
        super(string);
    }

    public Request() {
        super();
    }


    public void setType() {
        try {
            put(JSONMessage.TYPE, REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean is(JSONMessage message) {
        return message.getType().equals(REQUEST);
    }


    @Override
    public void send() {
        sender.send(this.toString());
    }


    @Override
    public void addSender(Sender sender) {
        this.sender = sender;
    }


    public interface Callback {
        void reply(Object result);
    }
}


