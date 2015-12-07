package ca.brocku.cosc3p97.bigbuzzerquiz.models;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

public class WifiP2pHelper {
    public static String convertStatus(int status) {
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

    public static String convertFailureStatus(int status) {
        String result = "";
        switch(status){
            case WifiP2pManager.P2P_UNSUPPORTED:
                result = "P2P_UNSUPPORTED";
                break;
            case WifiP2pManager.ERROR:
                result = "INTERNAL ERROR";
                break;
            case WifiP2pManager.BUSY:
                result = "FRAMWORK IS BUSY";
                break;
        }

        return result;
    }
}
