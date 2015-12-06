package ca.brocku.cosc3p97.bigbuzzerquiz;


import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerSocketThread extends Thread {
    private static final String TAG = "ServerSocketThread";
    private static final int THREAD_COUNT = 10;
    private ServerSocket serverSocket;
    private Handler threadHandler;
    private List<ServerSocketListener> listeners = new ArrayList<>();

    public interface ServerSocketListener {
        void onSetup();
    }

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());


    public ServerSocketThread(Handler threadHandler) throws IOException {
        try {
            serverSocket = new ServerSocket(TcpConnection.PORT);
            this.threadHandler = threadHandler;
        } catch (Exception e) {
            Log.e(TAG, "message = {" + e.getMessage() + "}");
            e.printStackTrace();
            threadPool.shutdownNow();
            throw e;
        }
    }


    @Override
    public void run() {
        callback();

        while (true) {
            try {
                threadPool.execute(new TcpConnection(serverSocket.accept(), threadHandler, TcpConnection.SERVER));
                Log.i(TAG, "getPoolSize={" + threadPool.getPoolSize() + "}, getCompletedTaskCount={" + threadPool.getCompletedTaskCount() + "}");
            } catch (IOException e) {
                try {
                    if (serverSocket != null && !serverSocket.isClosed())
                        serverSocket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                threadPool.shutdownNow();
                break;
            }
        }
    }


    private void callback() {
        Log.i(TAG, "callback to onSetup listeners");
        for(ServerSocketListener listener : listeners) {
            listener.onSetup();
        }
    }


    public void addListener(ServerSocketListener listener) {
        listeners.add(listener);
    }
}
