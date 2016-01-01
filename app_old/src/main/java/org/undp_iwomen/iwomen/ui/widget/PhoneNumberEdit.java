package org.undp_iwomen.iwomen.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * source: http://mdev-android.blogspot.com/2014/05/not-editable-prefix-in-edittext.html
 */
public class PhoneNumberEdit extends AppCompatEditText {

    //private String mPrefix = "+95 9"; // can be hardcoded for demo purposes
    private String mPrefix = "";
    private Rect mPrefixRect = new Rect(); // actual prefix size

    public PhoneNumberEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(mPrefix, 0, mPrefix.length(), mPrefixRect);
        mPrefixRect.right += getPaint().measureText(" "); // add some offset

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mPrefix, super.getCompoundPaddingLeft(), getBaseline(), getPaint());
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + mPrefixRect.width();
    }
}
