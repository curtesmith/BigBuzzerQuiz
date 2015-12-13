package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import ca.brocku.cosc3p97.bigbuzzerquiz.messages.common.Sender;


/**
 * Manages the socket connection interaction for this application
 */
public class TcpConnection implements Runnable, Sender {
    private static final String TAG = "TcpConnection";
    private Socket socket;
    private Handler handler;
    public int type;
    public static final int HANDLE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int DISCONNECTED = 3;
    public static final int PORT = 8988;
    public static final int SERVER = 1;
    public static final int CLIENT = 2;
    private BufferedReader in;
    private PrintWriter out;


    /**
     * An interface which exposes the core events available for the connection.
     */
    public interface Listener {
        void onConnected(TcpConnection connection);
        void onRead(ReadObject message);
        void onDisconnected(TcpConnection connection);
    }


    /**
     * A constructor which takes a reference to the socket connection that it will be managing,
     * a thread handler to allow communication back to the UI thread and finally a type value
     * that indicates whether or not this class instance will be managing a connection from the
     * server side or from the client side
     * @param socket the socket connection to manage
     * @param handler a thread handler to communicate with the UI thread
     * @param type used to indicate a if this is a server side or client side connection manager
     */
    public TcpConnection(Socket socket, Handler handler, int type) {
        this.socket = socket;
        this.handler = handler;
        this.type = type;
    }


    /**
     * When invoked this method will send a message to the UI thread that a connection has been
     * established and will prepare input stream and output stream to handle reading and writing of
     * data along the connection. If there is a disconnection then it will send a disconnected message
     * back to the UI thread
     */
    @Override
    public void run() {
        Log.i(TAG, "run: invoked");

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            handler.obtainMessage(HANDLE, type, -1, this).sendToTarget();

            while (true) {
                try {
                    message = in.readLine();

                    if (message == null) {
                        handler.obtainMessage(DISCONNECTED, type, -1, this).sendToTarget();
                        break;
                    }

                    handler.obtainMessage(MESSAGE_READ, type, -1, new ReadObject(this, message))
                            .sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Exposed by the Sender interface which in turn encapsulates and calls the write method to
     * pass data to the output stream of the socket
     * @param message
     */
    @Override
    public void send(String message) {
        write(message);
    }


    /**
     * Write data to the output stream of the socket
     * @param message
     */
    public void write(String message) {
        out.println(message);
    }


    /**
     * Shutdown the input and output streams of the socket and then close the socket connection
     */
    public void stop() {
        try {
            Log.i(TAG, "stop: shutdownInput");
            socket.shutdownInput();
            Log.i(TAG, "stop: shutdownOutput");
            socket.shutdownOutput();
            Log.i(TAG, "stop: close");
            socket.close();
            Log.i(TAG, "stop: exiting");
        }catch (SocketException e) {
            Log.i(TAG, "stop: closed socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ReadObject {
        public TcpConnection conn;
        public String message;

        ReadObject(TcpConnection conn, String message) {
            this.conn = conn;
            this.message = message;
        }
    }
}
