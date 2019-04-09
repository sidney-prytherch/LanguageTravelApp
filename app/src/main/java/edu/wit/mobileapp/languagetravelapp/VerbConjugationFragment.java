package edu.wit.mobileapp.languagetravelapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;


public class VerbConjugationFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String VERB_TYPE = "verbType";
    public static final String PORTUGUESE_VERB = "portugueseVerb";
    public static final String PERSON = "person";
    public static final String QUESTION = "question";

    private String verbType;
    private String portugueseVerb;
    private String person;
    private String question;
    private EditText editText;

//    private OnFragmentInteractionListener mListener;

    public VerbConjugationFragment() {
        // Required empty public constructor
    }

    public static VerbConjugationFragment newInstance(String verbType, String portugueseVerb, String person,  String question) {
        VerbConjugationFragment fragment = new VerbConjugationFragment();
        Bundle args = new Bundle();
        args.putString(VERB_TYPE, verbType);
        args.putString(PORTUGUESE_VERB, portugueseVerb);
        args.putString(PERSON, person);
        args.putString(QUESTION, question);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verb_conjugation, container, false);

        TextView questionTV = view.findViewById(R.id.verb_and_form_to_conjugate);
        questionTV.setText(question);

        TextView portugueseVerbTV = view.findViewById(R.id.verb_to_conjugate);
        portugueseVerbTV.setText(portugueseVerb);

        TextView verbTypeTV = view.findViewById(R.id.verb_form_to_conjugate);
        verbTypeTV.setText(verbType);

        TextView personTV = view.findViewById(R.id.person_to_conjugate);
        personTV.setText(person);

        editText = view.findViewById(R.id.user_verb_response);

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String input = editText.getText().toString();
                            ((VerbConjugationActivity) Objects.requireNonNull(getHost())).goToSolution(input);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        view.findViewById(R.id.next_verb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                ((VerbConjugationActivity) Objects.requireNonNull(getHost())).goToSolution(input);
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


//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
