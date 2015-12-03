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

public class ServerSocketHandler extends Thread {
    private static final String TAG = "ServerSocketHandler";
    private static final int THREAD_COUNT = 10;
    private ServerSocket socket;
    private Handler handler;
    private List<ServerSocketListener> listeners = new ArrayList<>();


    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());


    public interface ServerSocketListener {
        void onSetup();
    }


    public void addListener(ServerSocketListener listener) {
        listeners.add(listener);
    }

    public ServerSocketHandler(Handler handler) throws IOException {
        try {
            socket = new ServerSocket(TcpConnection.PORT);
            this.handler = handler;
        } catch (Exception e) {
            Log.e(TAG, "message = {" + e.getMessage() + "}");
            e.printStackTrace();
            pool.shutdownNow();
            throw e;
        }
    }


    @Override
    public void run() {
        callback();

        while (true) {
            try {
                pool.execute(new TcpConnection(socket.accept(), handler, TcpConnection.Type.SERVER));
                Log.i(TAG, "getPoolSize={" + pool.getPoolSize() + "}, getCompletedTaskCount={" + pool.getCompletedTaskCount() + "}");
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


    private void callback() {
        Log.i(TAG, "callback to onSetup listeners");
        for(ServerSocketListener listener : listeners) {
            listener.onSetup();
        }
    }


}
