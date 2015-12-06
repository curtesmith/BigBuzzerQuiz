package ca.brocku.cosc3p97.bigbuzzerquiz;


public interface PlayerMessageInterface {
    String LETS_PLAY = "LETS_PLAY";

    void execute(String string, HostProxy.Callback callback);
    void letsPlay();
}
