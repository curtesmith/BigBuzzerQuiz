package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;


public class MasterSetupFragment extends Fragment {
    private static final String TAG = "MasterSetupFragment";
    private WiFiConnectionsModel wifi;
    private Player player;
    private View view;
    private List<String> playerNames = new ArrayList<>();
    private OnClickListener listener;


    public MasterSetupFragment() {
        // Required empty public constructor
    }


    public static MasterSetupFragment newInstance(WiFiConnectionsModel wifi) {
        MasterSetupFragment fragment = new MasterSetupFragment();
        fragment.wifi = wifi;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            player = Player.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, String.format("onCreate: ending and number of players is %s", playerNames.size()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_master_setup, container, false);
        view.findViewById(R.id.startGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.startGameButtonClicked();
            }
        });

        final ListView listView = (ListView) view.findViewById(R.id.playerListView);

        try {
            player = Player.getInstance();
            player.getPlayers(new HostActions.GetPlayersCallback() {
                @Override
                public void reply(List<String> names) {
                    loadListView(listView, names);
                }
            });
            player.setActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private void loadListView(final ListView listView, List<String> players) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, players);

        listView.setAdapter(adapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            return;
        }

        player.setActivity(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnClickListener {
        void startGameButtonClicked();
    }

}
