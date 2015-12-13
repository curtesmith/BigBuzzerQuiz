package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A Thread used to manage a socket connection from a client to a server
 */
public class ClientSocketThread extends Thread {

    private static final String TAG = "ClientSocketThread";
    private Handler threadHandler;
    private TcpConnection tcpConnection;
    private InetAddress serverAddress;


    /**
     * Constructor which takes a handler and an server address to connect to
     * @param threadHandler a handler to respond to socket events
     * @param serverAddress the address to connect to
     */
    public ClientSocketThread(Handler threadHandler, InetAddress serverAddress) {
        this.threadHandler = threadHandler;
        this.serverAddress = serverAddress;
    }


    /**
     * The method invoked with the thread is started which will initiate the socket and
     * then pass it to its own tcp connection thread for processing
     */
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
