package edu.wit.mobileapp.languagetravelapp;

public class Tuple {
    public final WordOrientation wordOrientation;
    public final RootNode rootNode;

    public Tuple(WordOrientation wordOrientation, RootNode rootNode) {
        this.wordOrientation = wordOrientation;
        this.rootNode = rootNode;
    }
}