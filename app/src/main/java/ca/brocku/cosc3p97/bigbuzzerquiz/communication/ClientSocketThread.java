package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketThread extends Thread {

    private static final String TAG = "ClientSocketThread";
    private Handler threadHandler;
    private TcpConnection tcpConnection;
    private InetAddress serverAddress;

    public ClientSocketThread(Handler threadHandler, InetAddress serverAddress) {
        this.threadHandler = threadHandler;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(serverAddress.getHostAddress(),
                    TcpConnection.PORT), 5000);
            tcpConnection = new TcpConnection(socket, threadHandler, TcpConnection.CLIENT);
            new Thread(tcpConnection).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
    }
}
