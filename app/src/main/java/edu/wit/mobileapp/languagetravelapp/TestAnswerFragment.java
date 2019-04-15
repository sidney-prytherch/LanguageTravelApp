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


public class TestAnswerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PORT_WORD = "portugueseWord";
    public static final String ENG_WORD = "englishWord";
    public static final String ENG_DEF = "definition";
    public static final String INPUT = "input";

    private String portugueseWord;
    private String englishWord;
    private String definition;
    private String input;

//    private OnFragmentInteractionListener mListener;

    public TestAnswerFragment() {
        // Required empty public constructor
    }

    public static TestAnswerFragment newInstance(String portugueseWord, String englishWord, String definition, String input) {
        TestAnswerFragment fragment = new TestAnswerFragment();
        Bundle args = new Bundle();
        args.putString(PORT_WORD, portugueseWord);
        args.putString(ENG_WORD, englishWord);
        args.putString(ENG_DEF, definition);
        args.putString(INPUT, input);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portugueseWord = getArguments().getString(PORT_WORD);
            englishWord = getArguments().getString(ENG_WORD);
            definition = getArguments().getString(ENG_DEF);
            input = getArguments().getString(INPUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_answer, container, false);

        TextView answerTV = view.findViewById(R.id.user_answer);
        answerTV.setText(input);

        TextView portugueseWordTV = view.findViewById(R.id.word_and_definition);
        String text = englishWord + ": " + definition;
        portugueseWordTV.setText(text);

        TextView userMessageTV = view.findViewById(R.id.user_message);
        if (input.toLowerCase().equals(portugueseWord.toLowerCase())) {
            userMessageTV.setText(getString(R.string.you_were_right));
        } else {
            String output = getString(R.string.the_correct_answer_is) + " " + portugueseWord;
            userMessageTV.setText(output);
        }

        view.findViewById(R.id.next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity) Objects.requireNonNull(getHost())).goToNext();
            }
        });

        view.findViewById(R.id.prev_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity) Objects.requireNonNull(getHost())).goToPrevious();
            }
        });


        return view;
    }
}