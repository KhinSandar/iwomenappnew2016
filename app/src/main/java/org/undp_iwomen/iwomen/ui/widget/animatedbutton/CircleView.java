package org.undp_iwomen.iwomen.ui.widget.animatedbutton;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import org.undp_iwomen.iwomen.ui.widget.animatedbutton.util.Utils;


/**
 * Created by Toe Lie on 3/3/2016.
 */
public class CircleView extends View {

    private static final int START_COLOR = Color.parseColor("#ff80ab");// 0xFFFF80ab;
    private static final int END_COLOR = Color.parseColor("#ff4081");

//    private static final int START_COLOR = 0xFFFF5722;
//    private static final int END_COLOR = 0xFFFFC107;

    private Paint mCirclePaint;
    private Paint mMaskPaint;

    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    private int mMaxCircleSize;

    private float mInnerCircleRadiusProgress = 0f;
    private float mOuterCircleRadiusProgress = 0f;

    private ArgbEvaluator mArgbEvaluator;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mArgbEvaluator = new ArgbEvaluator();
        setupPaints();
    }

    private void setupPaints() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);

        mMaskPaint = new Paint();
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        mMaxCircleSize = w / 2;
        tempBitmap = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(tempBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        tempCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, mOuterCircleRadiusProgress * mMaxCircleSize, mCirclePaint);
        tempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, mInnerCircleRadiusProgress * mMaxCircleSize, mMaskPaint);
        canvas.drawBitmap(tempBitmap, 0, 0, null);
    }

    public void setInnerCircleRadiusProgress(float innerCircleRadiusProgress) {
        mInnerCircleRadiusProgress = innerCircleRadiusProgress;
        postInvalidate();
    }

    public float getInnerCircleRadiusProgress() {
        return mInnerCircleRadiusProgress;
    }


    public void setOuterCircleRadiusProgress(float outerCircleRadiusProgress) {
        mOuterCircleRadiusProgress = outerCircleRadiusProgress;
        updateCircleColor();
        postInvalidate();
    }

    private void updateCircleColor() {
        float colorProgress = (float) Utils.clamp(mOuterCircleRadiusProgress, 0.5, 1);
        colorProgress = (float) Utils.mapValueFromRangeToRange(colorProgress, 0.5f, 1f, 0f, 1f);
        mCirclePaint.setColor((Integer) mArgbEvaluator.evaluate(colorProgress, START_COLOR, END_COLOR));
    }

    public float getOuterCircleRadiusProgress() {
        return mOuterCircleRadiusProgress;
    }


    public static final Property<CircleView, Float> INNER_CIRCLE_RADIUS_PROGRESS =
            new Property<CircleView, Float>(Float.class, "innerCircleRadiusProgress") {
                @Override
                public Float get(CircleView object) {
                    return object.getInnerCircleRadiusProgress();
                }

                @Override
                public void set(CircleView object, Float value) {
                    object.setInnerCircleRadiusProgress(value);
                }
            };

    public static final Property<CircleView, Float> OUTER_CIRCLE_RADIUS_PROGRESS =
            new Property<CircleView, Float>(Float.class, "outerCircleRadiusProgress") {
                @Override
                public Float get(CircleView object) {
                    return object.getOuterCircleRadiusProgress();
                }

                @Override
                public void set(CircleView object, Float value) {
                    object.setOuterCircleRadiusProgress(value);
                }
            };
}
