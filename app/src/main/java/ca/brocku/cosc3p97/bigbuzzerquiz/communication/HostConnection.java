package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.net.InetAddress;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.JsonMessage;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Request;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostProxy;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Host;


/**
 * Manages the connection to the host from the player
 */
public class HostConnection implements Handler.Callback, TcpConnection.Listener, Sender {
    private static final String TAG = "HostConnection";
    private HostProxy hostProxy;
    private TcpConnection tcpConnection;
    private ConnectedListener connectedListener;
    private boolean isConnected = false;
    private ClientSocketThread thread;


    /**
     * Constructor which takes the host address and a reference to the host itself. The
     * host reference will only hold a reference if the current device is also acting
     * as the host of the game.
     * @param hostAddress the socket address of the host
     * @param host a reference to the host object
     */
    public HostConnection(InetAddress hostAddress, Host host) {
        if (host != null) {
            Log.i(TAG, "ctor: host is not null");
            host.setTcpListener(this);
            thread = new ClientSocketThread(host.getHandler(), hostAddress);
        } else {
            Log.i(TAG, "ctor: host is null");
            thread = new ClientSocketThread(new Handler(this), hostAddress);
        }

        thread.start();
    }


    /**
     * Set a reference to the host proxy object which manages the requests and responses
     * with the host
     * @param proxy a reference to the host proxy
     */
    public void setHostProxy(HostProxy proxy) {
        hostProxy = proxy;
    }


    /**
     * An override of the Handler.Callback interface which will be invoked when connection
     * events are invoked such as when a connection is established, when a connection
     * reads a data stream from its input stream and when a connection is closed.
     * @param msg the message sent from the source
     * @return response back to the caller
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case TcpConnection.HANDLE:
                onConnected((TcpConnection) msg.obj);
                break;
            case TcpConnection.MESSAGE_READ:
                onRead((TcpConnection.ReadObject) msg.obj);
                break;
            case TcpConnection.DISCONNECTED:
                onDisconnected((TcpConnection) msg.obj);
                break;
        }

        return true;
    }


    /**
     * Save a reference to a listener that can be invoked to callback when a connection
     * has been established
     * @param listener a reference to the object that implements the associated interface
     */
    public void setConnectedListener(HostConnection.ConnectedListener listener) {
        connectedListener = listener;
    }


    /**
     * Send a message to the connected host
     * @param message the message to be sent to the host
     */
    @Override
    public void send(String message) {
        tcpConnection.write(message);
    }


    /**
     * Invoked when a message is read from the connected socket
     * @param obj a reference to the object passed from the connection handler
     */
    @Override
    public void onRead(TcpConnection.ReadObject obj) {
        Log.i(TAG, String.format("onRead: invoked with string {%s}", obj.message));

        try {
            JsonMessage message = new JsonMessage(obj.message);
            Log.i(TAG, String.format("onRead: message type is {%s}", message.getType()));

            if (Request.is(message)) {
                hostProxy.handlePlayerRequest(message);
            } else {
                hostProxy.handleHostResponse(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Exposes the private isConnected field
     * @return a boolean value of the isConnected field
     */
    public boolean isConnected() {
        return  isConnected;
    }


    /**
     * An interface that exposes callback methods for listening objects to handle
     * when this connection is connected and also when it becomes disconnected
     */
    public interface ConnectedListener {
        void onConnected();
        void onDisconnected();
    }


    /**
     * Callback exposed by the ConnectedListener interface to allow listeners to be
     * invoked when the connection is established
     * @param tcpConnection a reference to the connection that has been established
     */
    @Override
    public void onConnected(TcpConnection tcpConnection) {
        isConnected = true;
        this.tcpConnection = tcpConnection;
        connectedListener.onConnected();
    }


    /**
     * Callback exposed by the ConnectedListener interface to allow listeners to be
     * invoked when the connection is closed
     * @param tcpConnection a reference to the connection that has been established
     */
    @Override
    public void onDisconnected(TcpConnection connection) {
        isConnected = false;
        connectedListener.onDisconnected();
    }


    /**
     * Allows the associated connection to be terminated and it corresponding thread
     * to be interrupted
     */
    public void stop() {
        tcpConnection.stop();
        thread.interrupt();
    }
}
