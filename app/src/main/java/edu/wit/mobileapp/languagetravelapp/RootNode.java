package edu.wit.mobileapp.languagetravelapp;

import android.util.Log;

public class RootNode extends CellNode {
    private String solutionAcrossWord = null;
    private String solutionDownWord = null;
    private String hintAcrossWord = null;
    private String hintDownWord = null;
    private int solutionAcrossWordLength = 0;
    private int solutionDownWordLength = 0;
    private int acrossIndex;
    private int downIndex;
    private int number;
    private int filledInCountAcross = 0;
    private int filledInCountDown = 0;

    public int getNumber() {
        return number;
    }

    public RootNode(CellNode left, CellNode up, PuzzleWhiteSquare square, WordOrientation wordOrientation, int index, int number) {
        super(left, up, square);
        this.setIndex(wordOrientation, index);
        this.number = number;
    }

    public RootNode(CellNode cellNode, WordOrientation wordOrientation, int index, int number) {
        super(cellNode);
        this.setIndex(wordOrientation, index);
        this.number = number;
    }

    public void setIndex(WordOrientation wordOrientation, int index) {
        switch (wordOrientation) {
            case ACROSS:
                this.acrossIndex = index;
            case DOWN:
                this.downIndex = index;
        }
    }

    public void setWordLength(WordOrientation wordOrientation, int length) {
        switch (wordOrientation) {
            case ACROSS:
                this.solutionAcrossWordLength = length;
                break;
            case DOWN:
                this.solutionDownWordLength = length;
        }
    }

    public void setWordSolution(WordOrientation wordOrientation, String solution, String hint) {
        switch (wordOrientation) {
            case ACROSS:
                this.solutionAcrossWord = solution.toUpperCase();
                this.hintAcrossWord = hint;
                break;
            case DOWN:
                this.solutionDownWord = solution.toUpperCase();
                this.hintDownWord = hint;
        }
    }

    public String getSolutionWord(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case ACROSS:
                return this.solutionAcrossWord;
            case DOWN:
                return this.solutionDownWord;
            default:
                return null;
        }
    }

    public String getHint(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case ACROSS:
                return this.hintAcrossWord;
            case DOWN:
                return this.hintDownWord;
            default:
                return null;
        }
    }

    public int getWordLength(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case ACROSS:
                return this.solutionAcrossWordLength;
            case DOWN:
                return this.solutionDownWordLength;
            default:
                return 0;
        }
    }

    public int getIndex(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case ACROSS:
                return this.acrossIndex;
            case DOWN:
                return this.downIndex;
            default:
                return -1;
        }
    }

    public void finalizeSolution(WordOrientation wordOrientation) {
        if (wordOrientation == WordOrientation.ACROSS) {
            CellNode node = this;
            while (node != null) {
                for (int i = 0; i < this.solutionAcrossWordLength; i++) {
                    if (node.getSolutionLetter() == ' ') {
                        node.setSolutionLetter(solutionAcrossWord.charAt(i));
                        node.getRoot(WordOrientation.ACROSS).increaseFilledInCount(WordOrientation.ACROSS);
                        RootNode downRoot = node.getRoot(WordOrientation.DOWN);
                        if (downRoot != null) {
                            downRoot.increaseFilledInCount(WordOrientation.DOWN);
                        }
                    }
                    node = node.getNext(WordOrientation.ACROSS);
                }
            }
        } else {
            CellNode node = this;
            while (node != null) {
                for (int i = 0; i < this.solutionDownWordLength; i++) {
                    if (node.getSolutionLetter() == ' ') {
                        node.setSolutionLetter(solutionDownWord.charAt(i));
                        node.getRoot(WordOrientation.DOWN).increaseFilledInCount(WordOrientation.DOWN);
                        RootNode acrossRoot = node.getRoot(WordOrientation.ACROSS);
                        if (acrossRoot != null) {
                            acrossRoot.increaseFilledInCount(WordOrientation.ACROSS);
                        }
                    }
                    node = node.getNext(WordOrientation.DOWN);
                }
            }
        }
    }

    public void finalizeSolution() {
        CellNode node = this;
        Log.v("printwords", "" + this.solutionAcrossWord);
        Log.v("printwords", "" + this.solutionDownWord);
        for (int i = 0; i < this.solutionAcrossWordLength; i++) {
            node.setSolutionLetter(solutionAcrossWord.charAt(i));
            node = node.getNext(WordOrientation.ACROSS);
        }
        node = this;
        for (int i = 0; i < this.solutionDownWordLength; i++) {
            node.setSolutionLetter(solutionDownWord.charAt(i));
            node = node.getNext(WordOrientation.DOWN);
        }
        Log.v("printwords", "" + this.getSolutionLetter());
    }

    public String getPatternString(WordOrientation wordOrientation) {
        CellNode node = this;
        double random = Math.random();
        char[] patternCharArray = new char[this.getWordLength(wordOrientation)];
        if (this.getWordLength(wordOrientation) == 0) {
            return "";
        }
        int i = 0;
        while (node != null) {
            Log.v("currenttest2", "" + random + " - " + node.getSolutionLetter());
            patternCharArray[i] = node.getSolutionLetter();
            i++;
            node = node.getNext(wordOrientation);
        }
        String pattern = String.copyValueOf(patternCharArray);
        Log.v("currenttest2", "" + random + " - " + pattern);
        return pattern;
    }

    private void increaseFilledInCount(WordOrientation wordOrientation) {
        if (wordOrientation == WordOrientation.DOWN) {
            this.filledInCountDown++;
        } else {
            this.filledInCountAcross++;
        }

    }

    public int getFilledInCount(WordOrientation wordOrientation) {
        if (wordOrientation == WordOrientation.DOWN) {
            return this.filledInCountDown;
        } else {
            return this.filledInCountAcross;
        }
    }
}
