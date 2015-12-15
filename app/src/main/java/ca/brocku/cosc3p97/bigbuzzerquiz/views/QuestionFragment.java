package ca.brocku.cosc3p97.bigbuzzerquiz.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.brocku.cosc3p97.bigbuzzerquiz.R;
import ca.brocku.cosc3p97.bigbuzzerquiz.database.QuestionContract;
import ca.brocku.cosc3p97.bigbuzzerquiz.messages.player.ShowQuestionRequest;

/**
 * This Fragment handels a Question
 */
public class QuestionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private QuestionFragmentListener listener;
    private QuestionContract question;

    /**
     * {@inheritDoc}
     *
     * Handles the click on a Answer.
     * If the player answers a question wrong, the buttons are enabled
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int answer = -1;

        switch(view.getId()) {
            case R.id.answer1:
                answer = 0;
                break;
            case R.id.answer2:
                answer = 1;
                break;
            case R.id.answer3:
                answer = 2;
                break;
            case R.id.answer4:
                answer = 3;
                break;
        }

        // do a check to see if it was the correct answer -> block the player
        if (answer != question.indexOfCorrectAnswer) {
            Toast.makeText(getActivity(), "Your answer is wrong. Your not allowed to give another answer now", Toast.LENGTH_LONG).show();
            getActivity().findViewById(R.id.answer1).setEnabled(false);
            getActivity().findViewById(R.id.answer2).setEnabled(false);
            getActivity().findViewById(R.id.answer3).setEnabled(false);
            getActivity().findViewById(R.id.answer4).setEnabled(false);
        }

        listener.onAnswerButtonClick(answer == question.indexOfCorrectAnswer);
    }

    /**
     * An interface that provides the method for the click on a answer-button
     */
    public interface QuestionFragmentListener {
        /**
         * This method handle the click on an answer button
         * @param correct if the correct answer was clicked or not
         */
        void onAnswerButtonClick(boolean correct);
    }

    /**
     * The empty default constructor
     */
    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * {@inheritDoc}
     *
     * It receivs the Question and Answer.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        question = new QuestionContract();
        question.indexOfCorrectAnswer = getArguments().getInt(ShowQuestionRequest.CORRECT_ANSWER);
        question.text = getArguments().getString(ShowQuestionRequest.TEXT);
        question.answers = getArguments().getStringArray(ShowQuestionRequest.ANSWERS);

        view = inflater.inflate(R.layout.fragment_question, container, false);
        return view;
    }


    /**
     * {@inheritDoc}
     *
     * It also sets the Text of the question and the answers onto the button
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView) view.findViewById(R.id.questionTextView)).setText(question.text);

        Button answer1 = (Button) view.findViewById(R.id.answer1);
        answer1.setText(question.answers[0]);
        answer1.setOnClickListener(this);

        Button answer2 = (Button) view.findViewById(R.id.answer2);
        answer2.setText(question.answers[1]);
        answer2.setOnClickListener(this);

        Button answer3 = (Button) view.findViewById(R.id.answer3);
        answer3.setText(question.answers[2]);
        answer3.setOnClickListener(this);

        Button answer4 = (Button) view.findViewById(R.id.answer4);
        answer4.setText(question.answers[3]);
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
