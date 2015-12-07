package ca.brocku.cosc3p97.bigbuzzerquiz.models;


import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ca.brocku.cosc3p97.bigbuzzerquiz.communication.HostConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.communication.PlayerConnection;
import ca.brocku.cosc3p97.bigbuzzerquiz.views.DeviceListAdapter;

public class WiFiConnectionsModel extends Observable implements WifiP2pBroadcastReceiver.WifiP2pBroadcastListener,
        HostConnection.ConnectedListener {
    private static final String TAG = "WiFiConnectionsModel";
    private WifiP2pInfo p2pInfo;
    private WifiP2pManager manager;
    private Channel channel;
    private List<WifiP2pDeviceDecorator> peers = new ArrayList<>();
    private WifiP2pBroadcastReceiver receiver;
    private IntentFilter filter;
    private DeviceListAdapter deviceListAdapter;
    private Host host;
    private Player player;
    private boolean isScanning = false;


    public WiFiConnectionsModel(AppCompatActivity activity) {
        manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(activity, Looper.getMainLooper(), null);
        receiver = new WifiP2pBroadcastReceiver(manager, channel, this);

        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        deviceListAdapter = new DeviceListAdapter(activity, manager, channel, peers);
    }


    public ListAdapter getPeersListAdapter() {
        return deviceListAdapter;
    }


    public boolean isScanning() {
        return isScanning;
    }


    public String getGroupOwner() {
        if(p2pInfo != null && p2pInfo.groupFormed) {
            if(p2pInfo.isGroupOwner) {
                return "You are the group owner";
            } else {
                WifiP2pDevice groupOwner = getGroupOwnerDevice(p2pInfo);
                if (groupOwner == null) {
                    return "";
                } else {
                    return groupOwner.deviceName;
                }
            }
        }

        return "";
    }


    public void registerReceiver(AppCompatActivity activity) {
        activity.registerReceiver(receiver, filter);
    }


    public void unregisterReceiver(AppCompatActivity activity) {
        activity.unregisterReceiver(receiver);
    }


    public void discoverPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "call to discoverPeers was successful beginning the scan");
                isScanning = true;
                setChanged();
                notifyObservers();
            }

            @Override
            public void onFailure(int i) {
                String message = "";

                switch (i) {
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        message = "Discovery failed. Device does not support Wi-Fi P2P";
                        break;
                    case WifiP2pManager.BUSY:
                        message = "Discovery failed. The framework is busy. Try again.";
                        break;
                    case WifiP2pManager.ERROR:
                        message = "Discovery failed due to an internal error. Try again.";
                        break;
                }

                setChanged();
                notifyObservers(message);
                Log.e(TAG, "call to discoverPeers failed, code = " + i);
            }
        });
    }


    @Override
    public void onPeersAvailable(WifiP2pDeviceList devices) {
        Log.i(TAG, "onPeersAvailable called");
        isScanning = false;
        refreshDeviceList(devices);

        setChanged();
        notifyObservers();
    }


    private void refreshDeviceList(WifiP2pDeviceList devices) {
        WifiP2pDeviceDecorator.copy(devices, peers);
        deviceListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        Log.i(TAG, "onConnectionInfoAvailable called with info?" + (info == null ? "FALSE" : "TRUE"));
        final WiFiConnectionsModel me = this;

        p2pInfo = info;

        if(peers.size() == 0) {
            requestPeers();
        }

        if(info != null) {
            if (info.isGroupOwner) {
                Log.i(TAG, "Connected as group owner");
                host = Host.getInstance(new PlayerConnection.SetupListener() {
                    @Override
                    public void onSetup(Host host) {
                        Log.i(TAG, "onSetup: game SERVER is ready, creating player");
                        player = Player.getInstance(info.groupOwnerAddress, host);
                        player.setConnectedListener(me);
                    }
                });
            } else {
                Log.i(TAG, "Connected as a CLIENT");
                player = Player.getInstance(info.groupOwnerAddress);
                player.setConnectedListener(me);
            }
        }

        Log.i(TAG, "onConnectionInfoAvailable: completed calling notifyObservers");
        setChanged();
        notifyObservers();
    }


    private void blockPeers() {

    }


    private void requestPeers() {
        Log.i(TAG, "requestPeers called");
        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                refreshDeviceList(wifiP2pDeviceList);
                setChanged();
                notifyObservers();
            }
        });
    }


    private WifiP2pDevice getGroupOwnerDevice(WifiP2pInfo info) {

        if(info == null) {
            return null;
        }

        for(WifiP2pDevice peer : peers) {
            if(peer.isGroupOwner()) {
                return peer;
            }
        }

        return null;
    }


    private boolean isConnected = false;

    public boolean isConnected() {
            Log.i(TAG, "isConnected returning " + isConnected);
            return isConnected;
    }


    public void onConnected() {
        Log.i(TAG, "onConnected: invoked");
        isConnected = true;
        setChanged();
        notifyObservers();
    }


    public Player getPlayer() {
        return player;
    }

}
