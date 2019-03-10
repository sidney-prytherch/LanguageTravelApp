package edu.wit.mobileapp.languagetravelapp;

public class RootNode extends CellNode {
    private String solutionAcrossWord = null;
    private String solutionDownWord = null;
    private int solutionAcrossWordLength = 0;
    private int solutionDownWordLength = 0;
    private int acrossIndex;
    private int downIndex;


    public RootNode(CellNode left, CellNode up, PuzzleWhiteSquare square, WordOrientation wordOrientation, int index) {
        super(left, up, square);
        this.setIndex(wordOrientation, index);
    }

    public RootNode(CellNode cellNode, WordOrientation wordOrientation, int index) {
        super(cellNode);
        this.setIndex(wordOrientation, index);
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

    public void setWordSolution(WordOrientation wordOrientation, String solution) {
        switch (wordOrientation) {
            case ACROSS:
                this.solutionAcrossWord = solution.toUpperCase();
                break;
            case DOWN:
                this.solutionDownWord = solution.toUpperCase();
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

    public void finalizeSolution() {
        CellNode node = this;
        for (int i = 0; i < this.solutionAcrossWordLength; i++) {
            node.setSolutionLetter(solutionAcrossWord.charAt(i));
            node = node.getNext(WordOrientation.ACROSS);
        }
        node = this;
        for (int i = 0; i < this.solutionDownWordLength; i++) {
            node.setSolutionLetter(solutionDownWord.charAt(i));
            node = node.getNext(WordOrientation.DOWN);
        }
    }
}
