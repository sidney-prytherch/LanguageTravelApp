package edu.wit.mobileapp.languagetravelapp;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Stack;


public class CrosswordActivity extends AppCompatActivity {

    LinearLayout crosswordGrid;
    RootNode[] acrossRoots;
    RootNode[] downRoots;
    Keyboard crosswordKeyboard;
    KeyboardView crosswordKeyboardView;
    CellNode selected;
    WordOrientation wordOrientation;
    String[][] prioritizedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        int[] crosswordFromDB = new int[]{
            1, 0, 0, 0, 0};

        int crosswordDim = (int) Math.sqrt(crosswordFromDB.length * 2 - 1);

        int[][] crossword = new int[crosswordDim][crosswordDim];

        ArrayList<RootNode> acrossRootsList = new ArrayList<>();
        ArrayList<RootNode> downRootsList = new ArrayList<>();
        boolean[] wordLengthUsed = new boolean[crosswordDim];


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
        prioritizedWords = getPrioritizedWordsByLength(wordLengthUsed);

        // Create the Keyboard
        crosswordKeyboard = new Keyboard(this, R.xml.keyboard);

        // Lookup the KeyboardView
        crosswordKeyboardView = (KeyboardView) findViewById(R.id.crossword_keyboard_view);

        // Attach the keyboard to the view
        crosswordKeyboardView.setPreviewEnabled(false);
        crosswordKeyboardView.setKeyboard(crosswordKeyboard);

        // Install the key handler
        crosswordKeyboardView.setOnKeyboardActionListener(cwOnKeyboardActionListener);

        crosswordKeyboardView.setVisibility(View.VISIBLE);
        crosswordKeyboardView.setEnabled(true);

        fillCrossword();
    }

    // when WhiteSquare is clicked, uncolor the previous words and set color for new word
    public void onWhiteSquareClick(View v) {
        if (selected != null) {
            selected.uncolorWord(getResources());
        }
        calculateOrientation(((PuzzleWhiteSquare) v).cellNode);
        selected = ((PuzzleWhiteSquare) v).cellNode;
        selected.colorWord(wordOrientation, getResources());
    }

    // sets the field wordOrientation based on certain state. Used when a user
    // clicks a new square
    private void calculateOrientation(CellNode newlySelected) {
        // if it's the same as the already selected square, reverse the wordOrientation if possible
        if (newlySelected == selected) {
            WordOrientation oppositeWordOrientation = oppositeWordOrientation();
            if (selected.getRoot(oppositeWordOrientation) != null) {
                wordOrientation = oppositeWordOrientation;
            }
            // assume across if wordOrientation is null, unless across isn't available
        } else if (wordOrientation == null) {
            wordOrientation = newlySelected.getRoot(WordOrientation.ACROSS) != null ? WordOrientation.ACROSS : WordOrientation.DOWN;
            // assume same orientation as previously selected if the same orientation is available
        } else if (newlySelected.getRoot(wordOrientation) != selected.getRoot(wordOrientation)) {
            wordOrientation = newlySelected.getRoot(wordOrientation) != null ? wordOrientation : oppositeWordOrientation();
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
            square.cellNode = new RootNode(left, up, square, wordOrientation, index);

        } else {
            if (!(square.cellNode instanceof RootNode)) {
                square.cellNode = new RootNode(square.cellNode, wordOrientation, index);
            } else {
                ((RootNode) square.cellNode).setIndex(wordOrientation, index);
            }
            square.cellNode.setPrev(left, up);
        }
        return square.cellNode;
    }

    // gets the opposite WordOrientation as the current (across -> down or down -> across)
    private WordOrientation oppositeWordOrientation() {
        switch (wordOrientation) {
            case ACROSS:
                return WordOrientation.DOWN;
            case DOWN:
                return WordOrientation.ACROSS;
            default:
                return null;
        }
    }

    private KeyboardView.OnKeyboardActionListener cwOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            //Here check the primaryCode to see which key is pressed
            //based on the android:codes property
            if (selected != null) {
                if (primaryCode == -2) {
                    selected.getSquare().setText("");
                    CellNode prev = selected.getPrev(wordOrientation);
                    if (prev != null) {
                        selected.uncolorWord(getResources());
                        selected = prev;
                        prev.colorWord(wordOrientation, getResources());
                    }
                } else {
                    selected.getSquare().setText(Character.toString((char) primaryCode));
                    CellNode next = selected.getNext(wordOrientation);
                    if (next != null) {
                        selected.uncolorWord(getResources());
                        selected = next;
                        next.colorWord(wordOrientation, getResources());
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
        boolean[] nodeHasWord = new boolean[acrossRoots.length + downRoots.length];
        RootNode[] partiallyCompleteWords = new RootNode[acrossRoots.length + downRoots.length];
        String[] selectedWords = new String[acrossRoots.length + downRoots.length];
        int[] filledInCounts = new int[acrossRoots.length + downRoots.length];
        Stack<RootNode> selectedNodes = new Stack<>();

        Tuple nextToBeSelected = getNextToBeSelected(nodeHasWord, partiallyCompleteWords);
        WordOrientation wordOrientation = nextToBeSelected.wordOrientation;
        RootNode nodeToBeSelected = nextToBeSelected.rootNode;
        int nodeToBeSelectedIndex;

        while (nodeToBeSelected != null) {
            nodeToBeSelectedIndex = nodeToBeSelected.getIndex(wordOrientation);
            if (filledInCounts[nodeToBeSelected.getIndex(wordOrientation)] == 0) {
                String word = getNextWordFromPriorityList(nodeToBeSelected.getWordLength(wordOrientation), selectedWords);
                nodeToBeSelected.setWordSolution(wordOrientation, word);
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
    }

    private void showLetter(PuzzleWhiteSquare whiteSquare) {
        whiteSquare.setText(Character.toString(whiteSquare.cellNode.getSolutionLetter()));
    }

    private void showLettersOfAdjacentCellNodes(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode prev = cellNode.getPrev(wordOrientation);
        while (prev != null) {
            Log.v("myapp", "" + prev.getSolutionLetter());
            prev.getSquare().setText(Character.toString(prev.getSolutionLetter()));
            prev = prev.getPrev(wordOrientation);
        }
        CellNode next = cellNode.getNext(wordOrientation);
        while (next != null) {
            Log.v("myapp", "" + next.getSolutionLetter());
            next.getSquare().setText(Character.toString(next.getSolutionLetter()));
            next = next.getNext(wordOrientation);
        }
    }

    private void showLettersForWord() {
        showLetter();
        showLettersOfAdjacentCellNodes(selected, wordOrientation);
    }

    private void showLettersForWord(CellNode cellNode, WordOrientation wordOrientation) {
        showLetter(cellNode);
        showLettersOfAdjacentCellNodes(cellNode, wordOrientation);
    }

    private void showLettersForWord(PuzzleWhiteSquare whiteSquare, WordOrientation wordOrientation) {
        showLetter(whiteSquare);
        showLettersOfAdjacentCellNodes(whiteSquare.cellNode, wordOrientation);
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
        if (index >= acrossRoots.length + downRoots.length) {
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
