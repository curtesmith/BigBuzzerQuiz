package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpConnection implements Runnable {
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

    public interface Listener {
        void onConnected(TcpConnection connection);
        void onRead(ReadObject message);
        void onDisconnected(TcpConnection connection);
    }


    public TcpConnection(Socket socket, Handler handler, int type) {
        this.socket = socket;
        this.handler = handler;
        this.type = type;
    }


    private BufferedReader in;
    private PrintWriter out;


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

    public void write(String message) {
        out.println(message);
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
