package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DeviceListAdapter extends BaseAdapter {
    private List<WifiP2pDeviceDecorator> peers;
    private Activity activity;
    private LayoutInflater inflater;


    public DeviceListAdapter(Activity activity, List<WifiP2pDeviceDecorator> peers) {
        this.activity = activity;
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
    public View getView(int index, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.peer_list_item, null);
        }

        ((CheckBox) view.findViewById(R.id.deviceNameCheckBox)).setText(peers.get(index).deviceName);
        ((TextView) view.findViewById(R.id.statusTextView)).setText(WifiP2pHelper.convertStatus(peers.get(index).status));
        ((ProgressBar) view.findViewById(R.id.progressBar)).setVisibility(peers.get(index).isConnecting ? View.VISIBLE : View.INVISIBLE);

        return view;
    }
}
