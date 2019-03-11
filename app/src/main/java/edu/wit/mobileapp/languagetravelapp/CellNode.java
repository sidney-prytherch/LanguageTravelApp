package edu.wit.mobileapp.languagetravelapp;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class CellNode {

    private CellNode up = null;
    private CellNode down = null;
    private CellNode left = null;
    private CellNode right = null;
    private RootNode acrossRoot = null;
    private RootNode downRoot = null;
    private char solutionLetter;
    private char currentLetter = ' ';
    private PuzzleWhiteSquare square;
    private Background background = Background.NONE;
    private SelectionStatus selectionStatus = SelectionStatus.UNSELECTED;

    public CellNode(CellNode left, CellNode up, PuzzleWhiteSquare square) {
        if (left != null) {
            this.left = left;
            this.left.right = this;
        }
        if (up != null) {
            this.up = up;
            this.up.down = this;
        }
        this.square = square;
    }

    public CellNode(CellNode cellNode) {
        if (cellNode.up != null) {
            this.up = cellNode.up;
            this.up.down = this;
        }
        if (cellNode.down != null) {
            this.down = cellNode.down;
            this.down.up = this;
        }
        if (cellNode.left != null) {
            this.left = cellNode.left;
            this.left.right = this;
        }
        if (cellNode.right != null) {
            this.right = cellNode.right;
            this.right.left = this;
        }
        this.acrossRoot = cellNode.acrossRoot;
        this.downRoot = cellNode.downRoot;
        this.square = cellNode.square;
    }

    public RootNode getRoot(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case DOWN:
                return downRoot;
            case ACROSS:
                return acrossRoot;
            default:
                return null;
        }
    }

    public CellNode getPrev(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case DOWN:
                return this.up;
            case ACROSS:
                return this.left;
            default:
                return null;
        }
    }

    public CellNode getNext(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case DOWN:
                return this.down;
            case ACROSS:
                return this.right;
            default:
                return null;
        }
    }

    public String getSolutionWord(WordOrientation wordOrientation) {
        switch (wordOrientation) {
            case DOWN:
                return this.downRoot.getSolutionWord(wordOrientation);
            case ACROSS:
                return this.acrossRoot.getSolutionWord(wordOrientation);
            default:
                return null;
        }
    }

    public void setRoot(WordOrientation wordOrientation, RootNode root) {
        switch (wordOrientation) {
            case DOWN:
                this.downRoot = root;
                break;
            case ACROSS:
                this.acrossRoot = root;
        }
    }

    public void setPrev(CellNode left, CellNode up) {
        if (left != null) {
            this.left = left;
            this.left.right = this;
        }
        if (up != null) {
            this.up = up;
            this.up.down = this;
        }
    }

    public char getSolutionLetter() {
        return solutionLetter;
    }

    protected void setSolutionLetter(char solutionLetter) {
        this.solutionLetter = solutionLetter;
    }

    public char getCurrentLetter() {
        return currentLetter;
    }

    public void setCurrentLetter(char currentLetter) {
        this.currentLetter = currentLetter;
    }

    public PuzzleWhiteSquare getSquare() {
        return square;
    }

    public void uncolorWord(WordOrientation wordOrientation, Resources resources) {
        CellNode currentCellNode = this.getRoot(wordOrientation);
        while (currentCellNode != null) {
            currentCellNode.selectionStatus = SelectionStatus.UNSELECTED;
            currentCellNode.setColor(resources);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
    }

    public void colorWord(WordOrientation wordOrientation, Resources resources) {
        CellNode currentCellNode = this.getRoot(wordOrientation);
        while (currentCellNode != null) {
            currentCellNode.selectionStatus = SelectionStatus.WORD_SELECTED;
            currentCellNode.setColor(resources);
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
        this.selectionStatus = SelectionStatus.LETTER_SELECTED;
        this.setColor(resources);
    }

    private void setColor(int color) {
        this.square.setBackgroundColor(color);
    }

    public void setBackground(Background background, Resources resources) {
        this.background = background;
        this.setColor(resources);
    }

    public void unsetBackground(Resources resources) {
        this.background = Background.NONE;
        this.setColor(resources);
    }

    private void setBackground(Drawable drawable) {
        this.square.setBackground(drawable);
    }

    public void setColor(Resources resources) {
        switch (this.background) {
            case NONE:
                switch (this.selectionStatus) {
                    case UNSELECTED:
                        this.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
                        break;
                    case WORD_SELECTED:
                        this.setColor(resources.getColor(R.color.colorCrosswordWordSelected));
                        break;
                    default:
                        this.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareSelected));
                }
                break;
            case CORRECT:
                switch (this.selectionStatus) {
                    case UNSELECTED:
                        this.setBackground(resources.getDrawable(R.drawable.correct_letter_default));
                        break;
                    case WORD_SELECTED:
                        this.setBackground(resources.getDrawable(R.drawable.correct_letter_word_selected));
                        break;
                    default:
                        this.setBackground(resources.getDrawable(R.drawable.correct_letter_selected));
                }
                break;
            default:
                switch (this.selectionStatus) {
                    case UNSELECTED:
                        this.setBackground(resources.getDrawable(R.drawable.incorrect_letter_default));
                        break;
                    case WORD_SELECTED:
                        this.setBackground(resources.getDrawable(R.drawable.incorrect_letter_word_selected));
                        break;
                    default:
                        this.setBackground(resources.getDrawable(R.drawable.incorrect_letter_selected));
                }
        }
    }
}
