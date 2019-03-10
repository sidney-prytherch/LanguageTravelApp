package edu.wit.mobileapp.languagetravelapp;

import android.content.res.Resources;

public class CellNode {

    private CellNode up = null;
    private CellNode down = null;
    private CellNode left = null;
    private CellNode right = null;
    private RootNode acrossRoot = null;
    private RootNode downRoot = null;
    private char solutionLetter;
    private char currentLetter;
    private PuzzleWhiteSquare square;

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

    public PuzzleWhiteSquare getSquare() {
        return square;
    }

    public void uncolorWord(Resources resources) {
        this.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
        CellNode left = this.left;
        while (left != null) {
            left.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
            left = left.left;
        }
        CellNode right = this.right;
        while (right != null) {
            right.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
            right = right.right;
        }
        CellNode up = this.up;
        while (up != null) {
            up.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
            up = up.up;
        }
        CellNode down = this.down;
        while (down != null) {
            down.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareDefault));
            down = down.down;
        }
    }

    public void colorWord(WordOrientation wordOrientation, Resources resources) {
        if (wordOrientation == WordOrientation.ACROSS) {
            this.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareSelectedAcross));
            CellNode left = this.left;
            while (left != null) {
                left.setColor(resources.getColor(R.color.colorCrosswordAcrossWordSelected));
                left = left.left;
            }
            CellNode right = this.right;
            while (right != null) {
                right.setColor(resources.getColor(R.color.colorCrosswordAcrossWordSelected));
                right = right.right;
            }
        } else {
            this.setColor(resources.getColor(R.color.colorPuzzleWhiteSquareSelectedDown));
            CellNode up = this.up;
            while (up != null) {
                up.setColor(resources.getColor(R.color.colorCrosswordDownWordSelected));
                up = up.up;
            }
            CellNode down = this.down;
            while (down != null) {
                down.setColor(resources.getColor(R.color.colorCrosswordDownWordSelected));
                down = down.down;
            }
        }
    }

    private void setColor(int color) {
        this.square.setBackgroundColor(color);
    }

}
