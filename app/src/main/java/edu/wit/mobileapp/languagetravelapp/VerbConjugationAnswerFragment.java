package edu.wit.mobileapp.languagetravelapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


public class VerbConjugationAnswerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String VERB_TYPE = "verbType";
    public static final String PORTUGUESE_VERB = "portugueseVerb";
    public static final String PERSON = "person";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String INPUT = "input";

    private String verbType;
    private String portugueseVerb;
    private String person;
    private String question;
    private String answer;
    private String input;

//    private OnFragmentInteractionListener mListener;

    public VerbConjugationAnswerFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VerbConjugationAnswerFragment newInstance(String verbType, String portugueseVerb, String person, String question, String answer, String input) {
        VerbConjugationAnswerFragment fragment = new VerbConjugationAnswerFragment();
        Bundle args = new Bundle();
        args.putString(VERB_TYPE, verbType);
        args.putString(PORTUGUESE_VERB, portugueseVerb);
        args.putString(PERSON, person);
        args.putString(QUESTION, question);
        args.putString(ANSWER, answer);
        args.putString(INPUT, input);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verbType = getArguments().getString(VERB_TYPE);
            portugueseVerb = getArguments().getString(PORTUGUESE_VERB);
            person = getArguments().getString(PERSON);
            question = getArguments().getString(QUESTION);
            answer = getArguments().getString(ANSWER);
            input = getArguments().getString(INPUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verb_conjugation_answer, container, false);

        TextView questionTV = view.findViewById(R.id.verb_and_form_to_conjugate);
        questionTV.setText(question);

        TextView portugueseVerbTV = view.findViewById(R.id.verb_to_conjugate);
        portugueseVerbTV.setText(portugueseVerb);

        TextView verbTypeTV = view.findViewById(R.id.verb_form_to_conjugate);
        verbTypeTV.setText(verbType);

        TextView personTV = view.findViewById(R.id.person_to_conjugate);
        personTV.setText(person);

        TextView answerTV = view.findViewById(R.id.verb_conjugation_solution);
        answerTV.setText(input);

        TextView userMessageTV = view.findViewById(R.id.user_message);
        if (input.toLowerCase().equals(answer.toLowerCase())) {
            userMessageTV.setText(getString(R.string.you_were_right));
        } else {
            String output = getString(R.string.the_correct_answer_is) + " " + answer;
            userMessageTV.setText(output);
        }

        view.findViewById(R.id.next_verb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VerbConjugationActivity) Objects.requireNonNull(getHost())).goToNext();
            }
        });

        view.findViewById(R.id.prev_verb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VerbConjugationActivity) Objects.requireNonNull(getHost())).goToPrevious();
            }
        });

        return view;


    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
