package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class WifiP2pBroadcastReceiver extends BroadcastReceiver{
    private WifiP2pManager manager;
    private Channel channel;
    private static final String TAG = "WifiBroadcastReceiver";
    private WifiP2pBroadcastListener listener;


    public interface WifiP2pBroadcastListener {
        void onPeersAvailable(WifiP2pDeviceList devices);
    }

    public WifiP2pBroadcastReceiver(WifiP2pManager manager, Channel channel, WifiP2pBroadcastListener listener) {
        this.manager = manager;
        this.channel = channel;
        this.listener = listener;
    }


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
        }
    }
}
