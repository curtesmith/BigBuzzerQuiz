package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerSocketHandler extends Thread {
    private static final int THREAD_COUNT = 10;
    private ServerSocket socket;
    private Handler handler;


    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());


    public ServerSocketHandler(Handler handler) throws IOException {
        try {
            socket = new ServerSocket(TcpManager.PORT);
            socket.getLocalPort();
            this.handler = handler;
        } catch (IOException e) {
            e.printStackTrace();
            pool.shutdownNow();
            throw e;
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                pool.execute(new TcpManager(socket.accept(), handler));
            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                pool.shutdownNow();
                break;
            }
        }
    }
}
