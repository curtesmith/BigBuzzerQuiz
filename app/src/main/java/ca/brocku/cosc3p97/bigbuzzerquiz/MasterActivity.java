package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MasterActivity extends AppCompatActivity
        implements WifiP2pBroadcastReceiver.WifiP2pBroadcastListener, View.OnClickListener {
    private WifiP2pManager manager;
    private Channel channel;
    private static final String TAG = "MasterActivity";
    private WifiP2pBroadcastReceiver receiver;
    private IntentFilter filter;
    private List<WifiP2pDeviceDecorator> peers = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiP2pBroadcastReceiver(manager, channel, this);
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        ListView list = (ListView) findViewById(R.id.devicesList);
        deviceListAdapter = new DeviceListAdapter(this, manager, channel, peers);
        list.setAdapter(deviceListAdapter);
        deviceListAdapter.registerDataSetObserver(new ListDataSetObserver(this));

        Button scanWifi = (Button) findViewById(R.id.scanWifiButton);
        scanWifi.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void discoverPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "call to discoverPeers was successful");
                setScanningWidgetVisibility(true);
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

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "call to discoverPeers failed, code = " + i);
            }
        });
    }


    @Override
    public void onPeersAvailable(WifiP2pDeviceList devices) {
        setScanningWidgetVisibility(false);
        refreshDeviceList(devices);
    }


    private void refreshDeviceList(WifiP2pDeviceList devices) {
        //deviceList = devices;
        WifiP2pDeviceDecorator.copy(devices, peers);
        deviceListAdapter.notifyDataSetChanged();

    }

    public void updateSelectedPlayers() {
        String selected = String.format("%s/%s", WifiP2pDeviceDecorator.countSelected(peers), peers.size());
        ((TextView) findViewById(R.id.selectedPlayersTextView)).setText(selected);
    }


    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(peers.size() == 0) {
            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList devices) {
                    refreshDeviceList(devices);
                }
            });
        }

        ((TextView) findViewById(R.id.groupOwnerFyiTextView))
                .setText(info.isGroupOwner ? R.string.message_group_owner : R.string.message_not_group_owner);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.scanWifiButton:
                discoverPeers();
                break;
        }
    }


    private void setScanningWidgetVisibility(boolean isVisible) {
        if(isVisible) {
            findViewById(R.id.scanWifiButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.scanWifiTextView).setVisibility(View.VISIBLE);
            findViewById(R.id.scanWifiProgressBar).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.scanWifiButton).setVisibility(View.VISIBLE);
            findViewById(R.id.scanWifiTextView).setVisibility(View.GONE);
            findViewById(R.id.scanWifiProgressBar).setVisibility(View.GONE);
        }

    }
}
