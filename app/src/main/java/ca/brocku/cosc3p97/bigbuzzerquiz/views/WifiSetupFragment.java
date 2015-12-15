package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;

/**
 * This Fragment lets the player connect to other devices
 */
public class WifiSetupFragment extends Fragment implements Observer, View.OnClickListener {
    private static final String TAG = "WifiSetupFragment";
    private WiFiConnectionsModel wifi;
    private Player player;
//    private onClickListener2 mListener;

    /**
     * Factory Method
     * @param wifi
     * @return
     */
    public static WifiSetupFragment newInstance(WiFiConnectionsModel wifi) {
        WifiSetupFragment fragment = new WifiSetupFragment();
        fragment.wifi = wifi;
        return fragment;
    }

    /**
     * Default empty constructor
     */
    public WifiSetupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: invoked");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: invoked");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_setup, container, false);
    }


    /**
     * {@inheritDoc}
     *
     * It fills the ListView with discovered peers and sets the OnClickListener for the ScanWifi-button
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "onActivityCreated: invoked");

        if(savedInstanceState != null) {
            return;
        }

        try {
            player = Player.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView list = (ListView) getActivity().findViewById(R.id.devicesList);
        list.setAdapter(wifi.getPeersListAdapter());

        Button scanWifi = (Button) getActivity().findViewById(R.id.scanWifiButton);
        scanWifi.setOnClickListener(this);

//        getActivity().findViewById(R.id.masterButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.letsPlayButtonClicked();
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: invoked");
        wifi.addObserver(this);
        update(wifi, null);
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "onPause: invoked");

        wifi.deleteObserver(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.scanWifiButton:
                wifi.discoverPeers();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (onClickListener2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement some listener interface");
        }
    }


   @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: invoked");

    }

    @Override
    public void update(Observable observable, Object o) {
        WiFiConnectionsModel model = (WiFiConnectionsModel) observable;

        setScanningWidgetVisibility(model.isScanning());

        ((TextView) getActivity().findViewById(R.id.groupOwnerFyiTextView))
                .setText("Group Owner: {" + model.getGroupOwner() + "}");

        if(o != null) {
            Toast.makeText(getContext(), (String) o, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the Visibility of the elements
     * @param isVisible
     */
    private void setScanningWidgetVisibility(boolean isVisible) {
        if(isVisible) {
            getActivity().findViewById(R.id.scanWifiButton).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.scanWifiTextView).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.scanWifiProgressBar).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.scanWifiButton).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.scanWifiTextView).setVisibility(View.GONE);
            getActivity().findViewById(R.id.scanWifiProgressBar).setVisibility(View.GONE);
        }
    }
}
