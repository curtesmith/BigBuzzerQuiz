package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.host.HostActions;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.Player;
import ca.brocku.cosc3p97.bigbuzzerquiz.models.WiFiConnectionsModel;


public class MasterSetupFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MasterSetupFragment";
    private WiFiConnectionsModel wifi;
    private Player player;
    private View view;
    private List<String> playerNames = new ArrayList<>();
    private OnClickListener listener;
    int numberOfQuestions = 0;
    List<Integer> categories = new ArrayList<>();


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

        Log.i(TAG, String.format("onCreate: ending and number of players is %s",
                playerNames.size()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_master_setup, container, false);

        view.findViewById(R.id.startGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.startGameButtonClicked(numberOfQuestions, categories);
            }
        });

        view.findViewById(R.id.CheckboxArt).setOnClickListener(this);
        view.findViewById(R.id.CheckboxGeography).setOnClickListener(this);
        view.findViewById(R.id.CheckboxScience).setOnClickListener(this);
        view.findViewById(R.id.plusButton).setOnClickListener(this);
        view.findViewById(R.id.minusButton).setOnClickListener(this);

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
        listener = null;
        player.stop();

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.CheckboxArt:
            case R.id.CheckboxGeography:
            case R.id.CheckboxScience:
                clickCheckbox(view);
                break;
            case R.id.plusButton:
                clickMore(this.view);
                break;
            case R.id.minusButton:
                clickLess(this.view);
                break;
        }
    }


    public interface OnClickListener {
        void startGameButtonClicked(int numberOfQuestions, List<Integer> categories);
    }


    /**
     * Method that is called when you click the More-Button
     *
     * @param view
     */
    public void clickMore(View view) {
        TextView amountView = (TextView) view.findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        if (amount == amountPossibleQuestions()) {

            CharSequence text = "You have reached the maximum amount of questions for the chosen categories";

            Toast toast = Toast.makeText(getContext(), text,  Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        increaseAmountView(1);
    }

    /**
     * Method that is called when you click the Less-Button
     *
     * @param view
     */
    public void clickLess(View view) {
        TextView amountView = (TextView) view.findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        if (amount == 0) {
            return;
        }

        increaseAmountView(-1);
    }


    /**
     * Calculates the amount of possible Questions based on the checkboxes that are checked
     * For every Categorie there are 10 Questions
     *
     * @return
     */
    private int amountPossibleQuestions() {
        int amount = 0;

        boolean checkboxArt = ((CheckBox) view.findViewById(R.id.CheckboxArt)).isChecked();
        boolean checkboxScience = ((CheckBox) view.findViewById(R.id.CheckboxScience)).isChecked();
        boolean checkboxGeography = ((CheckBox) view.findViewById(R.id.CheckboxGeography)).isChecked();

        if (checkboxArt) {
            amount += 10;
        }

        if (checkboxScience) {
            amount += 10;
        }

        if (checkboxGeography) {
            amount += 10;
        }

        return amount;
    }

    /**
     * if a checkbox is checked, the default value of the amountQuestions view should increase by 10,
     * if a checkbox is unchecked, it should decrease by 10
     */
    public void clickCheckbox(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.CheckboxArt:
                adjustCategories(1, checked);
                break;
            case R.id.CheckboxScience:
                adjustCategories(2, checked);
                break;
            case R.id.CheckboxGeography:
                adjustCategories(3, checked);
                break;
        }

        if (checked) {
            increaseAmountView(10);
        } else {
            increaseAmountView(-10);
        }
    }


    private void adjustCategories(int key, boolean add) {
        if(add) {
            categories.add(key);
        } else {
            categories.remove(categories.indexOf(key));
        }
    }


    /**
     * Increases the number in the AmountView by the transferred number.
     * Of Course - if the number is negative, the number will be decreased
     *
     * @param byNumber amount by which the number should be increased
     */
    private void increaseAmountView(int byNumber) {
        TextView amountView = (TextView) view.findViewById(R.id.questionAmount);
        int amount = Integer.decode(amountView.getText().toString());

        amount += byNumber;
        numberOfQuestions = amount;

        amountView.setText(Integer.toString(amount));
    }
}
