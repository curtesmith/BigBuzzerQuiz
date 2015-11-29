package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DeviceListAdapter extends BaseAdapter {
    private static final String TAG = "DeviceListAdapter";
    private List<WifiP2pDeviceDecorator> peers;
    private Activity activity;
    private LayoutInflater inflater;
    WifiP2pManager manager;
    Channel channel;


    public DeviceListAdapter(Activity activity, WifiP2pManager manager, Channel channel, List<WifiP2pDeviceDecorator> peers) {
        this.activity = activity;
        this.manager = manager;
        this.channel = channel;
        this.peers = peers;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public static List<WifiP2pDeviceDecorator> copy (WifiP2pDeviceList list) {
        List<WifiP2pDeviceDecorator> result = new ArrayList<>();
        for(WifiP2pDevice device : list.getDeviceList()) {
           result.add(new WifiP2pDeviceDecorator(device));
        }
        return result;
    }


    @Override
    public int getCount() {
        return peers.size();
    }

    @Override
    public Object getItem(int index) {
        return peers.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }


    @Override
    public View getView(final int index, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.peer_list_item, null);
        }

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.deviceNameCheckBox);
        checkBox.setText(peers.get(index).deviceName);
        checkBox.setOnCheckedChangeListener(getOnCheckedChangeListener(index));

        ((TextView) view.findViewById(R.id.statusTextView)).setText(WifiP2pHelper.convertStatus(peers.get(index).status));

        ((ProgressBar) view.findViewById(R.id.progressBar)).setVisibility(peers.get(index).isConnecting ? View.VISIBLE : View.INVISIBLE);

        return view;
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(final int index) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                peers.get(index).isSelected = isChecked;

                switch (peers.get(index).status) {
                    case WifiP2pDevice.AVAILABLE:
                        WifiP2pDevice device = peers.get(index);
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        config.wps.setup = WpsInfo.PBC;

                        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                peers.get(index).isConnecting = true;
                                notifyDataSetChanged();
                                Log.i(TAG, "call to connect was successful");
                            }

                            @Override
                            public void onFailure(int i) {
                                Log.e(TAG, "call to connect failed with status: " + WifiP2pHelper.convertFailureStatus(i));
                            }
                        });

                        break;
                }

                notifyDataSetChanged();
            }
        };
    }
}
