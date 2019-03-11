package edu.wit.mobileapp.languagetravelapp;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Stack;


public class CrosswordActivity extends AppCompatActivity {

    LinearLayout crosswordGrid;
    LinearLayout crosswordNumberGrid;
    RootNode[] acrossRoots;
    RootNode[] downRoots;
    Keyboard crosswordKeyboard;
    KeyboardView crosswordKeyboardView;
    CellNode selected;
    WordOrientation wordOrientation;
    String[][] prioritizedWords;
    MenuItem checkWordAlwaysItemView;
    Button hintButton;
    int wordCount;
    int nextNumber;
    float crosswordNumberTextSize;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("myapp1", "menu made right now");
        getMenuInflater().inflate(R.menu.crossword_menu, menu);
        Log.v("myapp1", "" + menu);
        checkWordAlwaysItemView = menu.findItem(R.id.check_word_always_option);
        Log.v("myapp1", "" + checkWordAlwaysItemView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.reveal_letter_option:
                showLetter();
                break;
            case R.id.reveal_word_option:
                showLettersForWord();
                break;
            case R.id.reveal_puzzle_option:
                showLettersForPuzzle();
                break;
            case R.id.check_letter_option:
                checkLetter();
                break;
            case R.id.check_word_option:
                checkWord();
                break;
            case R.id.check_word_always_option:
                if (checkWordAlwaysItemView.isChecked()) {
                    checkWordAlwaysItemView.setChecked(false);
                } else {
                    checkWordAlwaysItemView.setChecked(true);
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
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        int[] crosswordFromDB = new int[]{
            0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 1, 0, 1, 0, 1, 0, 1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 1, 0, 1, 1, 1, 0,
            0, 1, 0, 0, 0
        };


        int crosswordDim = (int) Math.sqrt(crosswordFromDB.length * 2 - 1);

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

        int[][] crossword = new int[crosswordDim][crosswordDim];

        ArrayList<RootNode> acrossRootsList = new ArrayList<>();
        ArrayList<RootNode> downRootsList = new ArrayList<>();
        boolean[] wordLengthUsed = new boolean[crosswordDim];

        nextNumber = 1;

        for (int i = 0; i < crosswordFromDB.length; i++) {
            int x = i / crosswordDim;
            int y = i % crosswordDim;
            crossword[x][y] = crossword[crosswordDim - 1 - x][crosswordDim - 1 - y] = crosswordFromDB[i];
        }

        crosswordGrid = (LinearLayout) findViewById(R.id.crossword_grid);
        for (int[] crosswordColumn : crossword) {
            LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
            row.setBackgroundColor(getResources().getColor(R.color.colorDefaultBlack));

            for (int squareValue : crosswordColumn) {
                if (squareValue == 0 || squareValue == 2) {
                    getLayoutInflater().inflate(R.layout.crossword_white_square, row);
                } else {
                    getLayoutInflater().inflate(R.layout.crossword_black_square, row);
                }
            }
            crosswordGrid.addView(row);
        }

        if (crosswordDim <= 11) {
            crosswordNumberGrid = (LinearLayout) findViewById(R.id.crossword_grid2);
            for (int[] crosswordColumn : crossword) {
                LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
                row.setBackgroundColor(getResources().getColor(R.color.colorCrosswordNumberBackground));

                for (int squareValue : crosswordColumn) {
                    getLayoutInflater().inflate(R.layout.crossword_number_square, row);
                }
                crosswordNumberGrid.addView(row);
            }
        }

        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword.length; j++) {
                if (crossword[i][j] == 0) {
                    //if it's the start of an across word:
                    if ((j == 0 || crossword[i][j - 1] == 1) && j != crosswordDim - 1 && crossword[i][j + 1] == 0) {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.ACROSS, acrossRootsList.size());
                        acrossRootsList.add(root);
                        root.setRoot(WordOrientation.ACROSS, root);

                        CellNode left = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = j + 1;
                        while (k < crosswordDim && crossword[i][k] == 0) {
                            CellNode currentCellNode = getOrCreateCellNode(i, k, left, null);
                            currentCellNode.setRoot(WordOrientation.ACROSS, root);
                            left = currentCellNode;
                            k++;
                        }
                        int wordLength = k - j;
                        wordLengthUsed[wordLength - 1] = true;
                        root.setWordLength(WordOrientation.ACROSS, wordLength);
                    }
                    //if it's the start of a down word:
                    if ((i == 0 || crossword[i - 1][j] == 1) && i != crosswordDim - 1 && crossword[i + 1][j] == 0) {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.DOWN, downRootsList.size());
                        downRootsList.add(root);
                        root.setRoot(WordOrientation.DOWN, root);

                        CellNode up = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = i + 1;
                        while (k < crosswordDim && crossword[k][j] == 0) {
                            CellNode currentCellNode = getOrCreateCellNode(k, j, null, up);
                            currentCellNode.setRoot(WordOrientation.DOWN, root);
                            up = currentCellNode;
                            k++;
                        }
                        int wordLength = k - i;
                        wordLengthUsed[wordLength - 1] = true;
                        root.setWordLength(WordOrientation.DOWN, wordLength);
                    }
                }
            }
        }

        acrossRoots = acrossRootsList.toArray(new RootNode[acrossRootsList.size()]);
        downRoots = downRootsList.toArray(new RootNode[downRootsList.size()]);
        for (RootNode rootNode : downRoots) {
            rootNode.setIndex(WordOrientation.DOWN, rootNode.getIndex(WordOrientation.DOWN) + acrossRoots.length);
        }
        wordCount = acrossRoots.length + downRoots.length;
        prioritizedWords = getPrioritizedWordsByLength(wordLengthUsed);

        // Create the Keyboard
        crosswordKeyboard = new Keyboard(this, R.xml.keyboard);

        // Lookup the KeyboardView
        crosswordKeyboardView = (KeyboardView) findViewById(R.id.crossword_keyboard_view);

        // Attach the keyboard to the view
//        ((KeyboardView)findViewById(R.id.crossword_keyboard_popup_view)).setPreviewEnabled(false);
        crosswordKeyboardView.isPreviewEnabled();
        crosswordKeyboardView.setKeyboard(crosswordKeyboard);

        // Install the key handler
        crosswordKeyboardView.setOnKeyboardActionListener(cwOnKeyboardActionListener);

        crosswordKeyboardView.setVisibility(View.VISIBLE);
        crosswordKeyboardView.setEnabled(true);

        //fillCrossword();

        ImageButton previousWordButton = findViewById(R.id.previous_word_button);
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

        ImageButton nextWordButton = findViewById(R.id.next_word_button);
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

        hintButton = findViewById(R.id.hint_button);
        hintButton.setOnClickListener((View v) -> {
            if (selected != null) {
                switchWordOrientation();
            } else {
                Tuple newlySelected = getRootNode(0);
                setSelected(newlySelected.rootNode, newlySelected.wordOrientation);
            }
        });
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
        hintButton.setText(wordRootNode.getNumber() + (wordOrientation == WordOrientation.ACROSS ? "A" : "D")
            + ". " + wordRootNode.getHint(wordOrientation));
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

    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateCellNode(int i, int j, CellNode left, CellNode up) {
        PuzzleWhiteSquare square = (PuzzleWhiteSquare) ((LinearLayout) crosswordGrid.getChildAt(i)).getChildAt(j);
        if (square.cellNode == null) {
            square.cellNode = new CellNode(left, up, square);
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
                        if (checkWordAlwaysItemView.isChecked() && isWordComplete(selected, WordOrientation.ACROSS)) {
                            checkWord(selected, WordOrientation.ACROSS);
                        }
                        if (checkWordAlwaysItemView.isChecked() && isWordComplete(selected, WordOrientation.DOWN)) {
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

    private String[][] getPrioritizedWordsByLength(boolean[] wordLengthUsed) {
        String[][] prioritizedWords = new String[wordLengthUsed.length][];
        for (int i = 0; i < wordLengthUsed.length; i++) {
            if (wordLengthUsed[i]) {
                // prioritizedWords[i] = access db and get the words of length i + 1
            }
        }
        prioritizedWords = new String[][]{
            null,
            {"ab", "or", "br", "bz", "zr", "za", "az"},
            {"bro", "arb", "bar", "zob", "bor", "rob", "zab"}
        };
        return prioritizedWords;
    }

    private void fillCrossword() {
        boolean[] nodeHasWord = new boolean[wordCount];
        RootNode[] partiallyCompleteWords = new RootNode[wordCount];
        String[] selectedWords = new String[wordCount];
        int[] filledInCounts = new int[wordCount];
        Stack<RootNode> selectedNodes = new Stack<>();

        Tuple nextToBeSelected = getNextToBeSelected(nodeHasWord, partiallyCompleteWords);
        WordOrientation wordOrientation = nextToBeSelected.wordOrientation;
        RootNode nodeToBeSelected = nextToBeSelected.rootNode;
        int nodeToBeSelectedIndex;

        while (nodeToBeSelected != null) {
            nodeToBeSelectedIndex = nodeToBeSelected.getIndex(wordOrientation);
            if (filledInCounts[nodeToBeSelected.getIndex(wordOrientation)] == 0) {
                String word = getNextWordFromPriorityList(nodeToBeSelected.getWordLength(wordOrientation), selectedWords);
                nodeToBeSelected.setWordSolution(wordOrientation, word, word);
                selectedWords[nodeToBeSelectedIndex] = word;
                nodeHasWord[nodeToBeSelectedIndex] = true;
                selectedNodes.push(nodeToBeSelected);
            }
            nextToBeSelected = getNextToBeSelected(nodeHasWord, partiallyCompleteWords);
            wordOrientation = nextToBeSelected.wordOrientation;
            nodeToBeSelected = nextToBeSelected.rootNode;
        }
        for (RootNode root : acrossRoots) {
            root.finalizeSolution();
        }
        for (RootNode root : downRoots) {
            root.finalizeSolution();
        }
    }

    private void showLetter() {
        showLetter(selected);
    }

    private void showLetter(CellNode cellNode) {
        cellNode.getSquare().setText(Character.toString(cellNode.getSolutionLetter()));
        cellNode.setCurrentLetter(cellNode.getSolutionLetter());
        if (checkWordAlwaysItemView.isChecked() && isWordComplete(cellNode, WordOrientation.ACROSS)) {
            checkWord(cellNode, WordOrientation.ACROSS);
        }
        if (checkWordAlwaysItemView.isChecked() && isWordComplete(cellNode, WordOrientation.DOWN)) {
            checkWord(cellNode, WordOrientation.DOWN);
        }
    }

    private void checkLetter() {
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

    private void showLettersForWord() {
        showLettersForWord(selected, wordOrientation);
    }

    private void showLettersForWord(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            showLetter(currentCellNode);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
    }

    private void checkWord() {
        checkWord(selected, wordOrientation);
    }

    private void checkWord(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            checkLetter(currentCellNode);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
    }

    private void showLettersForPuzzle() {
        for (RootNode rootNode : acrossRoots) {
            showLettersForWord(rootNode, WordOrientation.ACROSS);
        }
        for (RootNode rootNode : downRoots) {
            showLettersForWord(rootNode, WordOrientation.DOWN);
        }
    }

    private String getNextWordFromPriorityList(int wordLength, String[] selectedWords) {
        // work down word list to find the first string that priorityList that isn't in the
        // selectedWords
        return prioritizedWords[wordLength - 1][0];
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

    // given the partiallyCompleteWords ordered by priority and ordered boolean array of complete words
    // return next word that should be selected
    private Tuple getNextToBeSelected(boolean[] nodeHasWord, RootNode[] partiallyCompleteWords) {
        // get next partiallyCompleteWord
        if (partiallyCompleteWords[0] != null) {
            return new Tuple(acrossRoots.length > 0 ? WordOrientation.ACROSS : WordOrientation.DOWN, partiallyCompleteWords[0]);
        }
        // find next incomplete word
        int next = 0;
        Log.v("myapp", "" + nodeHasWord[0]);
        while (next < nodeHasWord.length && nodeHasWord[next]) {
            Log.v("myapp", "hi" + next);
            next++;
        }
        Log.v("myapp", "hey there!");
        getRootNode(next);
        Log.v("myapp", "ayy!");
        return getRootNode(next);
    }

    private class Tuple {
        public final WordOrientation wordOrientation;
        public final RootNode rootNode;

        public Tuple(WordOrientation wordOrientation, RootNode rootNode) {
            this.wordOrientation = wordOrientation;
            this.rootNode = rootNode;
        }
    }

}
