package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;


public class QuestionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private QuestionFragmentListener listener;
    private int key;

    @Override
    public void onClick(View view) {
        int answer = -1;

        switch(view.getId()) {
            case R.id.answer1:
                answer = 1;
                break;
            case R.id.answer2:
                answer = 2;
                break;
            case R.id.answer3:
                answer = 3;
                break;
            case R.id.answer4:
                answer = 4;
                break;
        }

        listener.onAnswerButtonClick(answer);
    }

    public interface QuestionFragmentListener {
        void onAnswerButtonClick(int buttonNbr);
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        key = getArguments().getInt("KEY");

        view = inflater.inflate(R.layout.fragment_question, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView) view.findViewById(R.id.questionTextView)).setText(String.format("This is question key={%d}", key));

        Button answer1 = (Button) view.findViewById(R.id.answer1);
        answer1.setOnClickListener(this);

        Button answer2 = (Button) view.findViewById(R.id.answer2);
        answer2.setOnClickListener(this);

        Button answer3 = (Button) view.findViewById(R.id.answer3);
        answer3.setOnClickListener(this);

        Button answer4 = (Button) view.findViewById(R.id.answer4);
        answer4.setOnClickListener(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (QuestionFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
