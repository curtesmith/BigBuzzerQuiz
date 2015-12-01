package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpManager implements Runnable {
    private Socket socket;
    private Handler handler;
    public static final int TCP_HANDLE = 1;
    public static final int TCP_MESSAGE_READ = 2;
    public static final int PORT = 8988;

    public interface ClientConnectionListener {
        void onClientConnected();
    }

    public TcpManager(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }


    private InputStream in;
    private OutputStream out;


    @Override
    public void run() {
        try {

            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(TCP_HANDLE, this)
                    .sendToTarget();

            while (true) {
                try {
                    bytes = in.read(buffer);
                    if (bytes == -1) {
                        break;
                    }

                    handler.obtainMessage(TCP_MESSAGE_READ,
                            bytes, -1, buffer).sendToTarget();
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
