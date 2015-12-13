package ca.brocku.cosc3p97.bigbuzzerquiz.communication;


import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A thread used to manage the socket connection from a server to a set of clients
 */
public class ServerSocketThread extends Thread {
    private static final String TAG = "ServerSocketThread";
    private static final int THREAD_COUNT = 10;
    private ServerSocket serverSocket;
    private Handler threadHandler;
    private List<ServerSocketListener> listeners = new ArrayList<>();


    /**
     * An interface that exposes a callback for the onSetup event
     */
    public interface ServerSocketListener {
        void onSetup();
    }


    /**
     * Used to manage a pool of up to 10 threads intended to run the socket
     * connections from the client connections
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());


    /**
     * A constuctor which takes a thread handler passed from the UI thread with which
     * to allow communication between the socket threads back to the UI
     * @param threadHandler a thread handler passed from the UI thread
     * @throws IOException
     */
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


    /**
     * This thread will wait for an incoming socket request and when one is received it
     * will spawn a new thread to handle communication. It will also invoke the callback
     * method so that an listening objects can be notified that the server socket is ready
     * to accept new client requests
     */
    @Override
    public void run() {
        callback();

        while (true) {
            try {
                threadPool.execute(new TcpConnection(serverSocket.accept(),
                        threadHandler, TcpConnection.SERVER));
                Log.i(TAG, "getPoolSize={" + threadPool.getPoolSize()
                        + "}, getCompletedTaskCount={" + threadPool.getCompletedTaskCount() + "}");
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


    /**
     * Callback to the objects that are listening for the onSetup event
     */
    private void callback() {
        Log.i(TAG, "reply to onSetup listeners");
        for(ServerSocketListener listener : listeners) {
            listener.onSetup();
        }
    }


    /**
     * Add a ServerSocketListener to the list of listeners. These listeners will be
     * notified when the onSetup event has been fired by this class.
     * @param listener
     */
    public void addListener(ServerSocketListener listener) {
        listeners.add(listener);
    }
}
