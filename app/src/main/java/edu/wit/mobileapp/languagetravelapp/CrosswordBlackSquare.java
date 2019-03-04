package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class CrosswordBlackSquare extends View {
  public CrosswordBlackSquare(Context context) {
    super(context);
  }

  public CrosswordBlackSquare(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CrosswordBlackSquare(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
  }
}
