package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpConnection implements Runnable {
    private static final String TAG = "TcpConnection";
    private Socket socket;
    private Handler handler;
    private Thread thread;
    private int mode;
    public static final int HANDLE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int DISCONNECTED = 3;
    public static final int PORT = 8988;
    public static final int SERVER_MODE = 1;
    public static final int CLIENT_MODE = 2;


    public TcpConnection(Socket socket, Handler handler, int mode) {

        this.socket = socket;
        this.handler = handler;
        this.mode = mode;
        this.thread = Thread.currentThread();
    }


    private InputStream in;
    private OutputStream out;


    @Override
    public void run() {
        Log.i(TAG, "run called");

        try {

            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(HANDLE, mode, -1, this).sendToTarget();
            while (true) {
                try {
                    bytes = in.read(buffer);
                    if (bytes == -1) {
                        handler.obtainMessage(DISCONNECTED, mode, -1, this).sendToTarget();
                        break;
                    }

                    handler.obtainMessage(MESSAGE_READ, mode,
                            bytes, buffer).sendToTarget();
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

    public void write(byte[] buffer) {
        try {
            out.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
