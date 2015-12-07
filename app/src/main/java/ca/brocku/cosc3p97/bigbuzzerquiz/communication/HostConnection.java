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

public class HostConnection implements Handler.Callback, TcpConnection.Listener, Sender {
    private static final String TAG = "HostConnection";
    private HostProxy hostProxy;
    private TcpConnection tcpConnection;
    private ConnectedListener connectedListener;


    public HostConnection(InetAddress hostAddress, Host host) {
        ClientSocketThread thread;

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


    public void setHostProxy(HostProxy proxy) {
        hostProxy = proxy;
    }


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


    public void setConnectedListener(HostConnection.ConnectedListener listener) {
        connectedListener = listener;
    }

    @Override
    public void send(String message) {
        tcpConnection.write(message);
    }


    public interface ConnectedListener {
        void onConnected();
    }


    @Override
    public void onConnected(TcpConnection tcpConnection) {
        this.tcpConnection = tcpConnection;
        connectedListener.onConnected();
    }


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


    @Override
    public void onDisconnected(TcpConnection connection) {

    }
}
