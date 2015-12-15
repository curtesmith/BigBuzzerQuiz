package ca.brocku.cosc3p97.bigbuzzerquiz.models;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for extending the WifiP2pDevice class with additional information needed by
 * the application
 */
public class WifiP2pDeviceDecorator extends WifiP2pDevice {
    private static final String TAG = "WifiP2pDeviceDecorator";
    public boolean isConnecting;
    public boolean isSelected;
    public boolean isEnabled;


    /**
     * Constructor
     * @param source is a WifiP2pDevice instance that is to be used as a source
     *               for this instance of the class
     */
    public WifiP2pDeviceDecorator(WifiP2pDevice source) {
        super(source);
        Log.i(TAG, "ctor name={" + source.deviceName + "}, isGroupOwner={" + source.isGroupOwner() + "}");
        isConnecting = false;
        isSelected = false;
        isEnabled = true;
    }


    /**
     * Compare this instance to that of the instance passed as an argument and response with
     * a boolean value of true if the two instances match otherwise return a value of false
     * @param device an instance to compare to
     * @return boolean true = there is a match, false = no match
     */
    public boolean matches(WifiP2pDevice device) {
        return deviceName.equals(device.deviceName);
    }


    /**
     * Copy the values in the "from" argument to the destination of the "to" argument
     * @param from source argument
     * @param to destination argument
     * @return an updated copy of the destination argument with the updates from the source
     */
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


    /**
     * Update the current instance from the information of a matching instance from the list
     * provided as an argument
     * @param source the list of devices from which to find a match and update
     */
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
}
