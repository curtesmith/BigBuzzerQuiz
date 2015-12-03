package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketHandler extends Thread {

    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private TcpConnection tcpManager;
    private InetAddress serverAddress;

    public ClientSocketHandler(Handler handler, InetAddress serverAddress) {
        this.handler = handler;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(serverAddress.getHostAddress(),
                    TcpConnection.PORT), 5000);
            tcpManager = new TcpConnection(socket, handler, TcpConnection.CLIENT_MODE);
            new Thread(tcpManager).start();
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
