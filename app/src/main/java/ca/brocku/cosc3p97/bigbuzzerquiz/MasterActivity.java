package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MasterActivity extends AppCompatActivity implements WifiP2pBroadcastReceiver.WifiP2pBroadcastListener {
    private WifiP2pManager manager;
    private Channel channel;
    private static final String TAG = "MasterActivity";
    private WifiP2pBroadcastReceiver receiver;
    private ProgressDialog progressDialog;
    private IntentFilter filter;
    private WifiP2pDeviceList deviceList = null;
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
        deviceListAdapter = new DeviceListAdapter(this, peers);
        list.setAdapter(deviceListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                WifiP2pDevice device = getDevice(deviceList, position);
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        showProgressDialog();
                        Log.i(TAG, "call to connect was successful");
                    }

                    @Override
                    public void onFailure(int i) {
                        Log.e(TAG, "call to connect failed with status: " + WifiP2pHelper.convertFailureStatus(i));
                    }
                });
            }
        });

    }


    private static WifiP2pDevice getDevice(WifiP2pDeviceList list, int position) {
        List<WifiP2pDevice> l = new ArrayList<>();
        for (WifiP2pDevice d : list.getDeviceList()) {
            l.add(d);
        }

        return l.get(position);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_discover:
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "call to discoverPeers was successful");
                        showProgressDialog();
                    }

                    @Override
                    public void onFailure(int i) {
                        String message = "";

                        switch(i) {
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
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "Press back to cancel", "Working ...", true,
                    true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
        } else {
            progressDialog.show();
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList devices) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        refreshDeviceList(devices);
    }


    private void refreshDeviceList(WifiP2pDeviceList devices) {
        deviceList = devices;
        peers.clear();
        peers.addAll(DeviceListAdapter.copy(devices));
        deviceListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(deviceList == null) {
            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList devices) {
                    refreshDeviceList(devices);
                }
            });
        }

        ((TextView) findViewById(R.id.groupOwnerFyiTextView))
                .setText(info.isGroupOwner ? R.string.message_group_owner : R.string.message_not_group_owner);

        ((TextView) findViewById(R.id.groupOwnerTextView)).setText(info.groupOwnerAddress.getHostAddress());
    }


    private ArrayList<String> getNames(WifiP2pDeviceList devices) {
        ArrayList<String> names = new ArrayList<>();

        for(WifiP2pDevice device : devices.getDeviceList()) {
            names.add(String.format("%s (%s)", device.deviceName, WifiP2pHelper.convertStatus(device.status)));
        }

        return names;

    }
}
