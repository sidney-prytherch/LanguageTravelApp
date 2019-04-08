package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;


public class CrosswordActivity extends AppCompatActivity {

    private String[][] prioritizedWords;
    private int wordCount;
    private int nextNumber;
    private CellNode[][] crosswordGrid;
    private NavigationView navigationView;
    private CrosswordFragment crosswordFragment = null;
    private MenuItem checkWordAlwaysItemView;
    private RootNode[] acrossRoots;
    private RootNode[] downRoots;
    private VerbForm[] verbForms;
    private boolean portugal;
//    private char[] letters = new char[]{
//            ' ', 'A', ' ', 'B', ' ',
//            'C', 'D', 'E', 'F', 'G',
//            ' ', 'H', 'I', 'J', ' ',
//            'K', 'L', 'M', 'N', 'O',
//            ' ', 'P', ' ', 'R', ' '};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("myapp1", "menu made right now");
        getMenuInflater().inflate(R.menu.crossword_menu, menu);
        Log.v("myapp1", "" + menu);
        checkWordAlwaysItemView = menu.findItem(R.id.check_word_always_option);
        checkWordAlwaysItemView.setChecked(false);
        Log.v("myapp1", "" + checkWordAlwaysItemView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (crosswordFragment != null) {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.reveal_letter_option:
                    crosswordFragment.showLetter();
                    break;
                case R.id.reveal_word_option:
                    crosswordFragment.showLettersForWord();
                    break;
                case R.id.reveal_puzzle_option:
                    crosswordFragment.showLettersForPuzzle();
                    break;
                case R.id.check_letter_option:
                    crosswordFragment.checkLetter();
                    break;
                case R.id.check_word_option:
                    crosswordFragment.checkWord();
                    break;
                case R.id.check_word_always_option:
                    checkWordAlwaysItemView.setChecked(crosswordFragment.alwaysCheckWord());

            }
        } else {
            fillCrossword();
//            char[] letters = new char[crosswordGrid.length * crosswordGrid.length];
//            for (int i = 0; i < crosswordGrid.length; i++) {
//                for (int j = 0; j < crosswordGrid.length; j++) {
//                    letters[i * crosswordGrid.length + j] = crosswordGrid[i][j].getSolutionLetter();
//                }
//            }

//            char[] letters = new char[]{
//                    ' ', 'A', ' ', 'B', ' ',
//                    'C', 'D', 'E', 'F', 'G',
//                    ' ', 'H', 'I', 'J', ' ',
//                    'K', 'L', 'M', 'N', 'O',
//                    ' ', 'P', ' ', 'R', ' '};

//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();
//            crosswordFragment = new CrosswordFragment();
//            Bundle bundle = new Bundle();
//            bundle.putCharArray(CrosswordFragment.CROSSWORD_SOLUTION, letters);
//            crosswordFragment.setArguments(bundle);
//            transaction.replace(R.id.container, crosswordFragment);
//            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(5).setChecked(true);

        int[] crosswordFromDB = new int[]{
                0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 1, 0, 1, 0, 1, 0, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 0, 1, 0, 1, 1, 1, 0,
                0, 1, 0, 0, 0
        };

        int crosswordDim = (int) Math.sqrt(crosswordFromDB.length * 2 - 1);

        crosswordGrid = new CellNode[crosswordDim][crosswordDim];

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

//        char[] letters = new char[crosswordDim * crosswordDim];
//        for (int i = 0; i < crosswordGrid.length; i++) {
//            for (int j = 0; j < crosswordGrid.length; j++) {
//                letters[i * crosswordDim + j] = crosswordGrid[i][j].getSolutionLetter();
//            }
//        }


        ArrayList<VerbForm> verbFormsArrayList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("present", false)) {
            verbFormsArrayList.add(VerbForm.PRES_IND);
        }
        if (prefs.getBoolean("preterite", false)) {
            verbFormsArrayList.add(VerbForm.PRET_IND);
        }
        if (prefs.getBoolean("imperfect", false)) {
            verbFormsArrayList.add(VerbForm.IMP_IND);
        }
        if (prefs.getBoolean("simple_pluperfect", false)) {
            verbFormsArrayList.add(VerbForm.SIMP_PLUP_IND);
        }
        if (prefs.getBoolean("future", false)) {
            verbFormsArrayList.add(VerbForm.FUT_IND);
        }
        if (prefs.getBoolean("conditional", false)) {
            verbFormsArrayList.add(VerbForm.COND_IND);
        }
        if (prefs.getBoolean("present_perfect", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF);
        }
        if (prefs.getBoolean("pluperfect", false)) {
            verbFormsArrayList.add(VerbForm.PLUP);
        }
        if (prefs.getBoolean("future_perfect", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF);
        }
        if (prefs.getBoolean("present_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PROG);
        }
        if (prefs.getBoolean("preterite_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRET_PROG);
        }
        if (prefs.getBoolean("imperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.IMP_PROG);
        }
        if (prefs.getBoolean("simple_pluperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.SIMP_PLUP_PROG);
        }
        if (prefs.getBoolean("future_progressive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PROG);
        }
        if (prefs.getBoolean("conditional_progressive", false)) {
            verbFormsArrayList.add(VerbForm.COND_PROG);
        }
        if (prefs.getBoolean("present_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF_PROG);
        }
        if (prefs.getBoolean("pluperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PLUP_PROG);
        }
        if (prefs.getBoolean("future_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF_PROG);
        }
        if (prefs.getBoolean("conditional_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.COND_PERF_PROG);
        }
        if (prefs.getBoolean("present_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_SUBJ);
        }
        if (prefs.getBoolean("present_perfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF_SUBJ);
        }
        if (prefs.getBoolean("imperfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.IMP_SUBJ);
        }
        if (prefs.getBoolean("pluperfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PLUP_SUBJ);
        }
        if (prefs.getBoolean("future_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_SUBJ);
        }
        if (prefs.getBoolean("future_perfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF_SUBJ);
        }
        portugal = Objects.requireNonNull(prefs.getString("country", "")).equals("portugal");

        verbForms = new VerbForm[verbFormsArrayList.size()];
        for (int i = 0; i < verbFormsArrayList.size(); i++) {
            verbForms[i] = verbFormsArrayList.get(i);
        }
        Log.v("blahstuff", "" + verbForms.length);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment loadingFragment = new LoadingFragment();
            transaction.replace(R.id.container, loadingFragment);
            transaction.commit();
        }

        prioritizedWords = getPrioritizedWordsByLength(wordLengthUsed);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().getItem(5).setChecked(true);
    }


    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateCellNode(int i, int j, CellNode left, CellNode up) {
        CellNode cellNode = crosswordGrid[i][j];
        if (cellNode == null) {
            cellNode = new CellNode(left, up, null);
            crosswordGrid[i][j] = cellNode;
        } else {
            cellNode.setPrev(left, up);
        }
        return cellNode;
    }

    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateRootNode(int i, int j, CellNode left, CellNode up, WordOrientation wordOrientation, int index) {
        CellNode cellNode = crosswordGrid[i][j];
        if (cellNode == null) {
            cellNode = new RootNode(left, up, null, wordOrientation, index, nextNumber++);
            crosswordGrid[i][j] = cellNode;
        } else {
            if (!(cellNode instanceof RootNode)) {
                cellNode = new RootNode(cellNode, wordOrientation, index, nextNumber++);
                crosswordGrid[i][j] = cellNode;
            } else {
                ((RootNode) cellNode).setIndex(wordOrientation, index);
            }
            cellNode.setPrev(left, up);
        }
        return cellNode;
    }


    private String[][] getPrioritizedWordsByLength(boolean[] wordLengthUsed) {
        String[][] prioritizedWords = new String[wordLengthUsed.length][];
        ArrayList[] lists = new ArrayList[wordLengthUsed.length];
        for (int i = 0; i < wordLengthUsed.length; i++) {
            if (wordLengthUsed[i]) {
                lists[i] = new ArrayList<String>();
            }
        }
        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        try {
            ArrayList<String> verbsConjugated = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split("\\|");
                String word = lineData[4].replaceAll(" ", "").replaceAll("-", "");
                int wordLength = word.length();
                int wordLengthMinusOne = wordLength - 1;
                int maxWordLength = wordLengthUsed.length;
                if (wordLength > 0 && wordLengthMinusOne < maxWordLength && wordLengthUsed[wordLengthMinusOne] && !lists[wordLengthMinusOne].contains(word)) {
                    lists[wordLengthMinusOne].add(word);
                }
                if (lineData[2].equals("v")) {
                    if (!verbsConjugated.contains(word)) {
                        verbsConjugated.add(word);
                        String[][] conjugatedVerbs = Conjugator.conjugate(lineData[4], verbForms, portugal);
                        if (conjugatedVerbs != null) {
                            for (String[] fullConjugation : conjugatedVerbs) {
                                if (fullConjugation != null && fullConjugation.length == 6) {
                                    conjugateLoop:
                                    for (int i = 0; i < 6; i++) {
                                        String conjugated = fullConjugation[i];
                                        if (conjugated != null) {
                                            int conjugatedLength = conjugated.length();
                                            int conjugatedLengthMinusOne = conjugatedLength - 1;
                                            if (conjugatedLength > 0) {
                                                if (conjugatedLengthMinusOne < maxWordLength) {
                                                    if (wordLengthUsed[conjugatedLengthMinusOne]) {
                                                        lists[conjugatedLengthMinusOne].add(conjugated);
                                                    }
                                                    int conjugatedLengthPlusOne = conjugatedLength + 1;
                                                    int conjugatedLengthPlusTwo = conjugatedLength + 2;
                                                    if (conjugatedLengthPlusTwo < maxWordLength) {
                                                        if (wordLengthUsed[conjugatedLengthPlusOne]) {
                                                            switch (i) {
                                                                case 0:
                                                                    lists[conjugatedLengthPlusOne].add("eu" + conjugated);
                                                                    Log.v("blahstuff", "eu " + conjugated + ": " + word);
                                                                    continue conjugateLoop;
                                                                case 1:
                                                                    lists[conjugatedLengthPlusOne].add("tu" + conjugated);
                                                                    continue conjugateLoop;
                                                            }
                                                        }
                                                        int conjugatedLengthPlusThree = conjugatedLength + 3;
                                                        if (conjugatedLengthPlusThree < maxWordLength) {
                                                            if (wordLengthUsed[conjugatedLengthPlusTwo]) {
                                                                switch (i) {
                                                                    case 2:
                                                                        lists[conjugatedLengthPlusTwo].add("ele" + conjugated);
                                                                        lists[conjugatedLengthPlusTwo].add("ela" + conjugated);
                                                                    case 3:
                                                                        lists[conjugatedLengthPlusTwo].add("nós" + conjugated);
                                                                        continue conjugateLoop;
                                                                    case 4:
                                                                        lists[conjugatedLengthPlusTwo].add("vós" + conjugated);
                                                                }
                                                            }
                                                            int conjugatedLengthPlusFour = conjugatedLength + 4;
                                                            if (conjugatedLengthPlusFour < maxWordLength) {
                                                                if (wordLengthUsed[conjugatedLengthPlusThree]) {
                                                                    switch (i) {
                                                                        case 2:
                                                                            lists[conjugatedLengthPlusThree].add("você" + conjugated);
                                                                            continue conjugateLoop;
                                                                        case 5:
                                                                            lists[conjugatedLengthPlusThree].add("eles" + conjugated);
                                                                            lists[conjugatedLengthPlusThree].add("elas" + conjugated);
                                                                            continue conjugateLoop;
                                                                    }
                                                                }
                                                                int conjugatedLengthPlusFive = conjugatedLength + 5;
                                                                if (conjugatedLengthPlusFive < maxWordLength && wordLengthUsed[conjugatedLengthPlusFour]) {
                                                                    lists[conjugatedLengthPlusFour].add("vocês" + conjugated);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.v("myapp", "error reading file");
        }
        for (int i = 0; i < lists.length; i++) {
            if (wordLengthUsed[i]) {
                prioritizedWords[i] = new String[lists[i].size()];
                for (int j = 0; j < lists[i].size(); j++) {
                    if (j == 0) {
                        Log.v("myapp", "hey, " + (i + 1) + ": " + lists[i].get(j));
                    }
                    prioritizedWords[i][j] = (String) lists[i].get(j);
                }
            }
        }
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
        convertNodesToCharArray();
    }

    private void convertNodesToCharArray() {
        int gridSize = crosswordGrid.length;
        char[] letters = new char[gridSize * gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (crosswordGrid[i][j] == null) {
                    letters[i * gridSize + j] = ' ';
                } else {
                    letters[i * gridSize + j] = crosswordGrid[i][j].getSolutionLetter();
                    Log.v("myapp", "hii" + (int) crosswordGrid[i][j].getSolutionLetter());
                }
            }
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        crosswordFragment = new CrosswordFragment();
        Bundle bundle = new Bundle();
        bundle.putCharArray(CrosswordFragment.CROSSWORD_SOLUTION, letters);
        crosswordFragment.setArguments(bundle);
        transaction.replace(R.id.container, crosswordFragment);
        transaction.commit();
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

    private boolean isCharacterAccent(char character) {
        if (character == '´' || character == '`' || character == '^' || character == '~' || character == '¨') {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
