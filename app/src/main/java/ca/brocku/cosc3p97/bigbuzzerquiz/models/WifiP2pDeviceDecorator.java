package ca.brocku.cosc3p97.bigbuzzerquiz.models;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class WifiP2pDeviceDecorator extends WifiP2pDevice {
    private static final String TAG = "WifiP2pDeviceDecorator";
    public boolean isConnecting;
    public boolean isSelected;

    public WifiP2pDeviceDecorator(WifiP2pDevice source) {
        super(source);
        Log.i(TAG, "ctor name={" + source.deviceName + "}, isGroupOwner={" + source.isGroupOwner() + "}");
        isConnecting = false;
        isSelected = false;
    }


    public boolean matches(WifiP2pDevice device) {
        return deviceName.equals(device.deviceName);
    }


    public static List<WifiP2pDeviceDecorator> copy(WifiP2pDeviceList from, List<WifiP2pDeviceDecorator> to) {
        ArrayList<WifiP2pDeviceDecorator> list = new ArrayList<>();

        for(WifiP2pDevice device : from.getDeviceList()) {
            WifiP2pDeviceDecorator decorator = new WifiP2pDeviceDecorator(device);
            Log.i(TAG, "copy device name={" + decorator.deviceName + "}, isgroupowner={" + decorator.isGroupOwner() + "}");
            decorator.update(to);
            list.add(decorator);
        }

        to.clear();
        to.addAll(list);
        return to;
    }


    private void update(List<WifiP2pDeviceDecorator> source) {
        for(WifiP2pDeviceDecorator device : source) {
            if(matches(device)) {
                if(status == WifiP2pDevice.CONNECTED) {
                    isConnecting = false;
                } else {
                    isConnecting = device.isConnecting;
                }

                isSelected = device.isSelected;
            }
        }
    }


    public static int countSelected(List<WifiP2pDeviceDecorator> list) {
        int count = 0;
        for(WifiP2pDeviceDecorator device : list) {
            if (device.isSelected) {
                count++;
            }
        }
        return count;
    }
}
