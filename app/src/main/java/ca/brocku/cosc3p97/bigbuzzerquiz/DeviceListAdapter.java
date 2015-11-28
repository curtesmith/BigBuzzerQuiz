package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;


public class DeviceListAdapter extends BaseAdapter {
    private List<String> peers;
    private Activity activity;
    private LayoutInflater inflater;


    public DeviceListAdapter(Activity activity, List<String> peers) {
        this.activity = activity;
        this.peers = peers;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ((CheckBox) view.findViewById(R.id.deviceNameCheckBox)).setText(peers.get(index));
        return view;
    }
}
