package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import java.util.ArrayList;
import java.util.List;


public class WifiP2pDeviceDecorator extends WifiP2pDevice {
    private static final String TAG = "WifiP2pDeviceDecorator";
    public boolean isConnecting;

    public WifiP2pDeviceDecorator(WifiP2pDevice source) {
        super(source);
        isConnecting = false;
    }


    public boolean matches(WifiP2pDevice device) {
        return deviceName.equals(device.deviceName);
    }

    public static List<WifiP2pDeviceDecorator> copy(WifiP2pDeviceList from, List<WifiP2pDeviceDecorator> to) {
        ArrayList<WifiP2pDeviceDecorator> list = new ArrayList<>();

        for(WifiP2pDevice device : from.getDeviceList()) {
            WifiP2pDeviceDecorator dd = new WifiP2pDeviceDecorator(device);
            dd.update(to);
            list.add(dd);
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
            }
        }
    }
}
