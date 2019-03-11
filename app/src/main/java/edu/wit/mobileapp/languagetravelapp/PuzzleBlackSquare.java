package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class PuzzleBlackSquare extends View {
    public PuzzleBlackSquare(Context context) {
        super(context);
    }

    public PuzzleBlackSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PuzzleBlackSquare(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
