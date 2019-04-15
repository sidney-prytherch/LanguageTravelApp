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


public class TestQuestionFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ENG_WORD = "englishWord";
    public static final String ENG_DEF = "definition";

    private String englishWord;
    private String definition;

//    private OnFragmentInteractionListener mListener;

    public TestQuestionFragment() {
        // Required empty public constructor
    }

    public static TestQuestionFragment newInstance(String englishWord, String definition) {
        TestQuestionFragment fragment = new TestQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ENG_WORD, englishWord);
        args.putString(ENG_DEF, definition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            englishWord = getArguments().getString(ENG_WORD);
            definition = getArguments().getString(ENG_DEF);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_question, container, false);

        TextView portugueseWordTV = view.findViewById(R.id.word_and_definition);
        String text = englishWord + ": " + definition;
        portugueseWordTV.setText(text);

        EditText editText = view.findViewById(R.id.user_answer);

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String input = editText.getText().toString();
                            ((TestActivity) Objects.requireNonNull(getHost())).goToSolution(input);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        view.findViewById(R.id.next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                ((TestActivity) Objects.requireNonNull(getHost())).goToSolution(input);
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