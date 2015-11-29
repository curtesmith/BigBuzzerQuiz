package ca.brocku.cosc3p97.bigbuzzerquiz;

import android.net.wifi.p2p.WifiP2pDevice;


public class WifiP2pDeviceDecorator extends WifiP2pDevice {
    public boolean isConnecting;

    public WifiP2pDeviceDecorator(WifiP2pDevice source) {
        super(source);
        isConnecting = false;
    }

}
