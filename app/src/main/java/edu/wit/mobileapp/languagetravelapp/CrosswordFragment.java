package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CrosswordFragment extends Fragment {

    public static final String CROSSWORD_SOLUTION = "crosswordSolution";

    private LinearLayout crosswordGrid;
    private float crosswordNumberTextSize;
    private char[] crosswordSolution;
    private LinearLayout crosswordNumberGrid;
    private int nextNumber;
    private Keyboard crosswordKeyboard;
    private KeyboardView crosswordKeyboardView;
    private Button hintButton;
    private CellNode selected;
    private WordOrientation wordOrientation;
    private RootNode[] acrossRoots;
    private RootNode[] downRoots;
    private int wordCount;
    private boolean checkWordAlways = false;


    public CrosswordFragment() {
    }

    public static CrosswordFragment newInstance(char[] crosswordSolution) {
        CrosswordFragment fragment = new CrosswordFragment();
        Bundle args = new Bundle();
        args.putCharArray(CROSSWORD_SOLUTION, crosswordSolution);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            crosswordSolution = getArguments().getCharArray(CROSSWORD_SOLUTION);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crossword, container, false);

        crosswordGrid = (LinearLayout) view.findViewById(R.id.crossword_grid);
        Log.v("myapp", "here2");
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        double widthToHeightRatio = 1.0 * width / height;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) crosswordGrid.getLayoutParams();
        if (widthToHeightRatio >= 2.9 / 4) {
            params.matchConstraintPercentWidth = (float) 0.55;
            crosswordGrid.setLayoutParams(params);
        }

        int crosswordDim = (int) Math.sqrt(crosswordSolution.length);

        char[][] crossword = new char[crosswordDim][crosswordDim];

        for (int i = 0; i < crosswordDim; i++) {
            for (int j = 0; j < crosswordDim; j++) {
                crossword[i][j] = crosswordSolution[i * crosswordDim + j];
                Log.v("myapp", ""+crossword[i][j]);
            }
        }

        switch (crosswordDim) {
            case 3:
                crosswordNumberTextSize = getResources().getDimension(R.dimen.crosswordNumber3);
                break;
            case 5:
                crosswordNumberTextSize = getResources().getDimension(R.dimen.crosswordNumber5);
                break;
            case 7:
                crosswordNumberTextSize = getResources().getDimension(R.dimen.crosswordNumber7);
                break;
            case 9:
                crosswordNumberTextSize = getResources().getDimension(R.dimen.crosswordNumber9);
                break;
            case 11:
                crosswordNumberTextSize = getResources().getDimension(R.dimen.crosswordNumber11);
                break;
        }

        Log.v("myapp", "here1");

        for (char[] crosswordColumn : crossword) {
            LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
            row.setBackgroundColor(getResources().getColor(R.color.colorDefaultBlack));

            for (char squareValue : crosswordColumn) {
                if (squareValue == ' ') {
                    getLayoutInflater().inflate(R.layout.crossword_black_square, row);
                } else {
                    getLayoutInflater().inflate(R.layout.crossword_white_square, row);
                }
            }
            crosswordGrid.addView(row);
        }
        Log.v("myapp", "here3");
        ArrayList<RootNode> acrossRootsList = new ArrayList<>();
        ArrayList<RootNode> downRootsList = new ArrayList<>();

        if (crosswordDim <= 11) {
            crosswordNumberGrid = (LinearLayout) view.findViewById(R.id.crossword_grid2);
            for (char[] crosswordColumn : crossword) {
                LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
                row.setBackgroundColor(getResources().getColor(R.color.colorCrosswordNumberBackground));

                for (char squareValue : crosswordColumn) {
                    getLayoutInflater().inflate(R.layout.crossword_number_square, row);
                }
                crosswordNumberGrid.addView(row);
            }
        }

        nextNumber = 1;Log.v("myapp", "here6");

        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword.length; j++) {
                if (crossword[i][j] != ' ') {
                    //if it's the start of an across word:
                    if ((j == 0 || crossword[i][j - 1] == ' ') && j != crosswordDim - 1 && crossword[i][j + 1] != ' ') {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.ACROSS, acrossRootsList.size());
                        root.setSolutionLetter(crossword[i][j]);
                        acrossRootsList.add(root);
                        root.setRoot(WordOrientation.ACROSS, root);

                        CellNode left = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = j + 1;
                        while (k < crosswordDim && crossword[i][k] != ' ') {
                            CellNode currentCellNode = getOrCreateCellNode(i, k, left, null);
                            currentCellNode.setSolutionLetter(crossword[i][k]);
                            currentCellNode.setRoot(WordOrientation.ACROSS, root);
                            left = currentCellNode;
                            k++;
                        }
                        int wordLength = k - j;
                        root.setWordLength(WordOrientation.ACROSS, wordLength);
                    }
                    //if it's the start of a down word:
                    if ((i == 0 || crossword[i - 1][j] == ' ') && i != crosswordDim - 1 && crossword[i + 1][j] != ' ') {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.DOWN, downRootsList.size());
                        root.setSolutionLetter(crossword[i][j]);
                        downRootsList.add(root);
                        root.setRoot(WordOrientation.DOWN, root);

                        CellNode up = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = i + 1;
                        while (k < crosswordDim && crossword[k][j] != ' ') {
                            CellNode currentCellNode = getOrCreateCellNode(k, j, null, up);
                            currentCellNode.setSolutionLetter(crossword[k][j]);
                            currentCellNode.setRoot(WordOrientation.DOWN, root);
                            up = currentCellNode;
                            k++;
                        }
                        int wordLength = k - i;
                        root.setWordLength(WordOrientation.DOWN, wordLength);
                    }
                }
            }
        }

        Log.v("myapp", "here8");
        acrossRoots = acrossRootsList.toArray(new RootNode[acrossRootsList.size()]);
        downRoots = downRootsList.toArray(new RootNode[downRootsList.size()]);
        for (RootNode rootNode : downRoots) {
            rootNode.setIndex(WordOrientation.DOWN, rootNode.getIndex(WordOrientation.DOWN) + acrossRoots.length);
        }

        for (RootNode rootNode: acrossRoots) {
            String solution = "";
            CellNode current = rootNode;
            while (current != null) {
                solution += current.getSolutionLetter();
                current = current.getNext(WordOrientation.ACROSS);
            }
            Log.v("myapp", solution);
            rootNode.setWordSolution(WordOrientation.ACROSS, solution, "blah");
            Log.v("myapp", rootNode.getSolutionWord(WordOrientation.ACROSS));
        }

        for (RootNode rootNode: downRoots) {
            String solution = "";
            CellNode current = rootNode;
            while (current != null) {
                solution += current.getSolutionLetter();
                Log.v("myapp", ""+current.getSolutionLetter());
                current = current.getNext(WordOrientation.DOWN);
            }
            Log.v("myapp", solution);
            rootNode.setWordSolution(WordOrientation.DOWN, solution, "blah");
            Log.v("myapp", rootNode.getSolutionWord(WordOrientation.DOWN));
        }

        wordCount = acrossRoots.length + downRoots.length;

        Log.v("myapp", "here9");
        // Create the Keyboard
        crosswordKeyboard = new Keyboard(getActivity(), R.xml.keyboard);

        // Lookup the KeyboardView
        crosswordKeyboardView = (KeyboardView) view.findViewById(R.id.crossword_keyboard_view);

        // Attach the keyboard to the view
//        ((KeyboardView)findViewById(R.id.crossword_keyboard_popup_view)).setPreviewEnabled(false);
        crosswordKeyboardView.isPreviewEnabled();
        crosswordKeyboardView.setKeyboard(crosswordKeyboard);

        // Install the key handler
        crosswordKeyboardView.setOnKeyboardActionListener(cwOnKeyboardActionListener);

        crosswordKeyboardView.setVisibility(View.VISIBLE);
        crosswordKeyboardView.setEnabled(true);

        //fillCrossword();

        ImageButton previousWordButton = view.findViewById(R.id.previous_word_button);
        previousWordButton.setOnClickListener((View v) -> {
            if (selected != null) {
                unselectCellNode();
                int rootNodeIndex = (selected.getRoot(wordOrientation).getIndex(wordOrientation) - 1 + wordCount) % wordCount;
                Tuple newlySelected = getRootNode(rootNodeIndex);
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            } else {
                Tuple newlySelected = getRootNode(wordCount - 1);
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            }
        });

        ImageButton nextWordButton = view.findViewById(R.id.next_word_button);
        nextWordButton.setOnClickListener((View v) -> {
            if (selected != null) {
                unselectCellNode();
                int rootNodeIndex = (selected.getRoot(wordOrientation).getIndex(wordOrientation) + 1) % wordCount;
                Tuple newlySelected = getRootNode(rootNodeIndex);
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            } else {
                Tuple newlySelected = getRootNode(0);
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            }
        });

        hintButton = view.findViewById(R.id.hint_button);
        hintButton.setOnClickListener((View v) -> {
            if (selected != null) {
                switchWordOrientation();
            } else {
                Tuple newlySelected = getRootNode(0);
                Log.v("myapp", "" + newlySelected.rootNode.getSolutionWord(WordOrientation.ACROSS));
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            }
        });

        return view;
    }

    private CellNode getOrCreateCellNode(int i, int j, CellNode left, CellNode up) {
        PuzzleWhiteSquare square = (PuzzleWhiteSquare) ((LinearLayout) crosswordGrid.getChildAt(i)).getChildAt(j);
        if (square.cellNode == null) {
            square.cellNode = new CellNode(left, up, square);
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onWhiteSquareClick(v);
                }
            });
        } else {
            square.cellNode.setPrev(left, up);
        }
        return square.cellNode;
    }

    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateRootNode(int i, int j, CellNode left, CellNode up, WordOrientation wordOrientation, int index) {
        PuzzleWhiteSquare square = (PuzzleWhiteSquare) ((LinearLayout) crosswordGrid.getChildAt(i)).getChildAt(j);
        if (square.cellNode == null) {
            if (crosswordNumberGrid != null) {
                PuzzleWhiteSquare numberSquare = (PuzzleWhiteSquare) ((LinearLayout) crosswordNumberGrid.getChildAt(i)).getChildAt(j);
                numberSquare.setText(Integer.toString(nextNumber));
                numberSquare.setTextSize(TypedValue.COMPLEX_UNIT_PX, crosswordNumberTextSize);
                numberSquare.setHeight(square.getHeight());
            }
            square.cellNode = new RootNode(left, up, square, wordOrientation, index, nextNumber++);
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("myapp", "WHY");
                    onWhiteSquareClick(v);
                }
            });
        } else {
            if (!(square.cellNode instanceof RootNode)) {
                if (crosswordNumberGrid != null) {
                    PuzzleWhiteSquare numberSquare = (PuzzleWhiteSquare) ((LinearLayout) crosswordNumberGrid.getChildAt(i)).getChildAt(j);
                    numberSquare.setText(Integer.toString(nextNumber));
                    numberSquare.setTextSize(TypedValue.COMPLEX_UNIT_PX, crosswordNumberTextSize);
                }
                square.cellNode = new RootNode(square.cellNode, wordOrientation, index, nextNumber++);
            } else {
                ((RootNode) square.cellNode).setIndex(wordOrientation, index);
            }
            square.cellNode.setPrev(left, up);
        }
        return square.cellNode;
    }

    private void setSelected(CellNode newlySelected, WordOrientation newWordOrientation) {
        unselectCellNode();
        selectCellNode(newlySelected, newWordOrientation);
    }

    private boolean isWordComplete(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            String text = currentCellNode.getSquare().getText().toString();
            if (text == null || text == "" || isCharacterAccent(text.charAt(0))) {
                return false;
            }
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
        return true;
    }

    private boolean isCharacterVowel(char character) {
        if (character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U') {
            return true;
        }
        return false;
    }

    private boolean isCharacterAccent(char character) {
        if (character == '´' || character == '`' || character == '^' || character == '~' || character == '¨') {
            return true;
        }
        return false;
    }

    // when WhiteSquare is clicked, uncolor the previous words and set color for new word
    public void onWhiteSquareClick(View v) {
        if (selected != null) {
            unselectCellNode();
        }
        CellNode newlySelected = ((PuzzleWhiteSquare) v).cellNode;
        calculateOrientation(newlySelected);
        selectCellNode(newlySelected, wordOrientation);
    }

    private void unselectCellNode() {
        if (selected != null) {
            selected.uncolorWord(wordOrientation, getResources());
        }
    }

    private void selectCellNode(CellNode newlySelected, WordOrientation newWordOrientation) {
        selected = newlySelected;
        wordOrientation = newWordOrientation;
        selected.colorWord(wordOrientation, getResources());
        RootNode wordRootNode = selected.getRoot(wordOrientation);
        String hintText = wordRootNode.getNumber() + (wordOrientation == WordOrientation.ACROSS ? "A" : "D")
                + ". " + wordRootNode.getHint(wordOrientation);
        hintButton.setText(hintText);
    }

    // sets the field wordOrientation based on certain state. Used when a user
    // clicks a new square
    private void calculateOrientation(CellNode newlySelected) {
        // if it's the same as the already selected square, reverse the wordOrientation if possible
        if (newlySelected == selected) {
            switchWordOrientation();
            // assume across if wordOrientation is null, unless across isn't available
        } else if (wordOrientation == null) {
            wordOrientation = newlySelected.getRoot(WordOrientation.ACROSS) != null ? WordOrientation.ACROSS : WordOrientation.DOWN;
            // assume same orientation as previously selected if the same orientation is available
        } else if (newlySelected.getRoot(wordOrientation) != selected.getRoot(wordOrientation)) {
            wordOrientation = newlySelected.getRoot(wordOrientation) != null ? wordOrientation : getOppositeWordOrientation();
        }
    }

    // gets the opposite WordOrientation as the current (across -> down or down -> across)
    private WordOrientation getOppositeWordOrientation() {
        switch (wordOrientation) {
            case ACROSS:
                return WordOrientation.DOWN;
            case DOWN:
                return WordOrientation.ACROSS;
            default:
                return null;
        }
    }

    private void switchWordOrientation() {
        WordOrientation oppositeWordOrientation = getOppositeWordOrientation();
        if (selected.getRoot(oppositeWordOrientation) != null) {
            setSelected(selected, oppositeWordOrientation);
        }

    }

    private KeyboardView.OnKeyboardActionListener cwOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            //Here check the primaryCode to see which key is pressed
            //based on the android:codes property
            if (selected != null) {
                if (primaryCode == 0 && keyCodes.length > 0 && keyCodes[0] > 0) {
                    primaryCode = keyCodes[0];
                }
                selected.unsetBackground(getResources());
                if (primaryCode == -2) {
                    boolean squareHasContent = selected.getSquare().getText().toString().length() > 0;
                    if (!squareHasContent) {
                        CellNode prev = selected.getPrev(wordOrientation);
                        if (prev != null) {
                            setSelected(prev, wordOrientation);
                        }
                    }
                    selected.unsetBackground(getResources());
                    selected.getSquare().setText("");
                    selected.setCurrentLetter(' ');
                } else if (primaryCode > 0) {
                    String currentText = selected.getSquare().getText().toString();
                    int currChar = currentText.length() == 0 ? -1 : currentText.charAt(0);
                    switch (currChar) {
                        case 'Ã':
                        case 'Á':
                        case 'À':
                        case 'Â':
                            currChar = 'A';
                            break;
                        case 'É':
                        case 'Ê':
                            currChar = 'E';
                            break;
                        case 'Í':
                            currChar = 'I';
                            break;
                        case 'Õ':
                        case 'Ó':
                        case 'Ô':
                            currChar = 'O';
                            break;
                        case 'Ú':
                        case 'Ü':
                            currChar = 'U';
                            break;
                    }
                    int newChar = primaryCode;
                    if (currChar != primaryCode && currChar != -1 &&
                            (isCharacterVowel((char) currChar) && isCharacterAccent((char) primaryCode) ||
                                    (isCharacterVowel((char) primaryCode) && isCharacterAccent((char) currChar)))) {
                        switch (currChar * primaryCode) {
                            case 'A' * '~':
                                newChar = 'Ã';
                                break;
                            case 'A' * '´':
                                newChar = 'Á';
                                break;
                            case 'A' * '`':
                                newChar = 'À';
                                break;
                            case 'A' * '^':
                                newChar = 'Â';
                                break;
                            case 'E' * '´':
                                newChar = 'É';
                                break;
                            case 'E' * '^':
                                newChar = 'Ê';
                                break;
                            case 'I' * '´':
                                newChar = 'Í';
                                break;
                            case 'O' * '~':
                                newChar = 'Õ';
                                break;
                            case 'O' * '´':
                                newChar = 'Ó';
                                break;
                            case 'O' * '^':
                                newChar = 'Ô';
                                break;
                            case 'U' * '´':
                                newChar = 'Ú';
                                break;
                            case 'U' * '¨':
                                newChar = 'Ü';
                        }
                    }
                    selected.getSquare().setText(Character.toString((char) newChar));
                    selected.setCurrentLetter((char) newChar);
                    if (!isCharacterAccent((char) newChar)) {
                        if (checkWordAlways && isWordComplete(selected, WordOrientation.ACROSS)) {
                            checkWord(selected, WordOrientation.ACROSS);
                        }
                        if (checkWordAlways && isWordComplete(selected, WordOrientation.DOWN)) {
                            checkWord(selected, WordOrientation.DOWN);
                        }
                        CellNode next = selected.getNext(wordOrientation);
                        if (next != null) {
                            setSelected(next, wordOrientation);

                        }
                    }
                }
            }
        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

    public void showLetter() {
        showLetter(selected);
    }

    private void showLetter(CellNode cellNode) {
        cellNode.getSquare().setText(Character.toString(cellNode.getSolutionLetter()));
        cellNode.setCurrentLetter(cellNode.getSolutionLetter());
        if (checkWordAlways && isWordComplete(cellNode, WordOrientation.ACROSS)) {
            checkWord(cellNode, WordOrientation.ACROSS);
        }
        if (checkWordAlways && isWordComplete(cellNode, WordOrientation.DOWN)) {
            checkWord(cellNode, WordOrientation.DOWN);
        }
    }

    public void checkLetter() {
        checkLetter(selected);
    }

    private void checkLetter(CellNode cellNode) {
        if (cellNode.getCurrentLetter() == cellNode.getSolutionLetter()) {
            cellNode.unsetBackground(getResources());
            cellNode.setBackground(Background.CORRECT, getResources());
        } else {
            cellNode.unsetBackground(getResources());
            cellNode.setBackground(Background.INCORRECT, getResources());
        }
    }

    public void showLettersForWord() {
        showLettersForWord(selected, wordOrientation);
    }

    private void showLettersForWord(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            showLetter(currentCellNode);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
    }

    public void checkWord() {
        checkWord(selected, wordOrientation);
    }

    private void checkWord(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            checkLetter(currentCellNode);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
    }

    public void showLettersForPuzzle() {
        for (RootNode rootNode : acrossRoots) {
            showLettersForWord(rootNode, WordOrientation.ACROSS);
        }
        for (RootNode rootNode : downRoots) {
            showLettersForWord(rootNode, WordOrientation.DOWN);
        }
    }

    public boolean alwaysCheckWord() {
        if (!checkWordAlways) {
            checkWordAlways = true;
            for (RootNode root : acrossRoots) {
                if (isWordComplete(root, WordOrientation.ACROSS)) {
                    checkWord(root, WordOrientation.ACROSS);
                }
            }
            for (RootNode root : downRoots) {
                if (isWordComplete(root, WordOrientation.DOWN)) {
                    checkWord(root, WordOrientation.DOWN);
                }
            }
            return true;
        } else {
            checkWordAlways = false;
            return false;
        }
    }

    private Tuple getRootNode(int index) {
        // if all are selected
        if (index >= wordCount) {
            return new Tuple(null, null);
        }
        // if all across are selected, return next down
        if (index >= acrossRoots.length) {
            return new Tuple(WordOrientation.DOWN, downRoots[index - acrossRoots.length]);
        }
        // otherwise, return next across
        return new Tuple(WordOrientation.ACROSS, acrossRoots[index]);
    }
}
