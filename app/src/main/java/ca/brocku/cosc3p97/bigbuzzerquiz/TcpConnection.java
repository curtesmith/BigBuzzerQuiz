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
    public Type type;
    public static final int HANDLE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int DISCONNECTED = 3;
    public static final int PORT = 8988;
    
    public enum Type {
        CLIENT, SERVER
    }


    public TcpConnection(Socket socket, Handler handler, Type tyoe) {
        this.socket = socket;
        this.handler = handler;
        this.type = tyoe;
    }


    private BufferedReader in;
    private PrintWriter out;


    @Override
    public void run() {
        Log.i(TAG, "run called");

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            handler.obtainMessage(HANDLE, this).sendToTarget();

            while (true) {
                try {
                    message = in.readLine();

                    if (message == null) {
                        handler.obtainMessage(DISCONNECTED, this).sendToTarget();
                        break;
                    }

                    handler.obtainMessage(MESSAGE_READ, message).sendToTarget();

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
}
