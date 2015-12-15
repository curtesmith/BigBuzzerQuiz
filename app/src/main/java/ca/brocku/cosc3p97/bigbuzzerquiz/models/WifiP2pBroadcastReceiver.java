package ca.brocku.cosc3p97.bigbuzzerquiz.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

/**
 * Responsible for receiving the WifiP2p intent broadcasts and handling them accordingly
 */
public class WifiP2pBroadcastReceiver extends BroadcastReceiver{
    private WifiP2pManager manager;
    private Channel channel;
    private static final String TAG = "WifiBroadcastReceiver";
    private WifiP2pBroadcastListener listener;


    /**
     * An interface which exposes some callbacks for some key events
     */
    public interface WifiP2pBroadcastListener {
        void onPeersAvailable(WifiP2pDeviceList devices);
        void onConnectionInfoAvailable(NetworkInfo networkInfo, WifiP2pInfo wifiP2pInfo);
    }


    /**
     * Constructor
     * @param manager the WifiP2pManager required to interact with the WifiP2p framework
     * @param channel a channel with which to communicate over wifi
     * @param listener a reference to an object that implemenets the WifiP2pBroadcastListener interface
     *                 that exposes callback methods to be invoked when certain events occur
     */
    public WifiP2pBroadcastReceiver(WifiP2pManager manager, Channel channel, WifiP2pBroadcastListener listener) {
        this.manager = manager;
        this.channel = channel;
        this.listener = listener;
    }


    /**
     * Invoked when a filtered intent is received. This class is filtering for the
     * WIFI_P2P_PEERS_CHANGED_ACTION intent and the WIFI_P2P_CONNECTION_CHANGED_ACTION intent.
     * @param context a reference to the activity
     * @param intent a reference to the intent that was caught
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                Log.i(TAG, "WIFI P2P PEERS CHANGED ACTION intent has been captured");

                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                        Log.i(TAG, "onPeersAvailable fired wtih " + wifiP2pDeviceList.getDeviceList().size() + " peers");
                        listener.onPeersAvailable(wifiP2pDeviceList);
                    }
                });
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                Log.i(TAG, "WIFI P2P CONNECTION CHANGED ACTION intent has been captured");
                final NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                if (networkInfo.isConnected()) {
                    manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                        @Override
                        public void onConnectionInfoAvailable(WifiP2pInfo info) {
                            Log.i(TAG, "onConnectionInfoAvailable fired with group owner " + info.groupOwnerAddress);
                            listener.onConnectionInfoAvailable(networkInfo, info);
                        }
                    });
                } else {
                    listener.onConnectionInfoAvailable(networkInfo, null);
                }
                break;
        }
    }
}
