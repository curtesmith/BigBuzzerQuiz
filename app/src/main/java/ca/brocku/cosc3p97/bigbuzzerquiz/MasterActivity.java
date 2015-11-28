package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private List<String> peers = new ArrayList<>();
    private ArrayAdapter<String> deviceListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiP2pBroadcastReceiver(manager, channel, this);
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        ListView list = (ListView) findViewById(R.id.devicesList);
        deviceListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, peers);
        list.setAdapter(deviceListAdapter);

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
            progressDialog = ProgressDialog.show(this, "Press back to cancel", "searching for peers", true,
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

        peers.clear();
        peers.addAll(getNames(devices));
        deviceListAdapter.notifyDataSetChanged();
    }


    private ArrayList<String> getNames(WifiP2pDeviceList devices) {
        ArrayList<String> names = new ArrayList<>();

        for(WifiP2pDevice device : devices.getDeviceList()) {
            names.add(String.format("%s %s", device.deviceName, convertStatus(device.status)));
        }

        return names;

    }

    private String convertStatus(int status) {
        String result = "";

        switch(status) {
            case WifiP2pDevice.AVAILABLE:
                result = "AVAILABLE";
                break;
            case WifiP2pDevice.FAILED:
                result = "FAILED";
                break;
            case WifiP2pDevice.INVITED:
                result = "INVITED";
                break;
            case WifiP2pDevice.CONNECTED:
                result = "CONNECTED";
                break;
            case WifiP2pDevice.UNAVAILABLE:
                result = "UNAVAILABLE";
                break;
        }

        return result;
    }
}
