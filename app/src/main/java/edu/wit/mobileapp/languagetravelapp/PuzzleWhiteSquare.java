package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class PuzzleWhiteSquare extends AppCompatTextView {

    public CellNode cellNode = null;

    public PuzzleWhiteSquare(Context context) {
        super(context);
    }

    public PuzzleWhiteSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PuzzleWhiteSquare(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
