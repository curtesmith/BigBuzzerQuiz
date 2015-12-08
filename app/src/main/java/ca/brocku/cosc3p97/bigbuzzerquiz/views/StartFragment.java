package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;


public class StartFragment extends Fragment implements Observer {
    private static final String TAG = "StartFragment";
    private WiFiConnectionsModel wifi;


    private OnClickListener mListener;


    public StartFragment() {
        // Required empty public constructor
    }


    public static StartFragment newInstance(WiFiConnectionsModel wifi) {
        StartFragment fragment = new StartFragment();
        fragment.wifi = wifi;
        return fragment;
    }


    /**
     * #2 during startup
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: invoked");


    }

    /**
     * #3 during startup
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: invoked");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "onActivityCreated: invoked");

        if(savedInstanceState != null) {
            return;
        }

        getActivity().findViewById(R.id.wiFiSetupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.wifiSetupButtonClicked();
            }
        });

        getActivity().findViewById(R.id.masterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.masterButtonClicked();
            }
        });
    }


    /**
     * #7 during startup
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: invoked");
        wifi.addObserver(this);
        update(wifi, null);
    }



    /**
     * #1 during startup
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * #1 when being shut down
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: invoked");

        wifi.deleteObserver(this);
    }

    /**
     * #5 when being shut down
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: invoked");
        mListener = null;
    }


    @Override
    public void update(Observable observable, Object o) {
        boolean isConnected = ((WiFiConnectionsModel) observable).isConnected();
        Log.i(TAG, String.format("update: invoked and is connected is[%s]", isConnected));

        getActivity().findViewById(R.id.masterButton).setEnabled(isConnected);
        getActivity().findViewById(R.id.playerButton).setEnabled(isConnected);
    }


    public interface OnClickListener {
        void wifiSetupButtonClicked();
        void masterButtonClicked();
    }

}
