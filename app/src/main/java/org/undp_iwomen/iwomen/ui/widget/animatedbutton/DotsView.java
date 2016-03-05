package org.undp_iwomen.iwomen.ui.widget.animatedbutton;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import org.undp_iwomen.iwomen.ui.widget.animatedbutton.util.Utils;

/**
 * Created by Toe Lie on 3/3/2016.
 */
public class DotsView extends View {

    private static final int DOTS_COUNT = 7;
    private static final int OUTER_DOTS_POSITION_ANGLE = 360 / DOTS_COUNT;

//    private static final int COLOR_1 = 0xFFFFC107;
//    private static final int COLOR_2 = 0xFFFF9800;
//    private static final int COLOR_3 = 0xFFFF5722;
//    private static final int COLOR_4 = 0xFFF44336;

    private static final int COLOR_1 = Color.parseColor("#ffcdd2");//0xFFFFCDD2;//
    private static final int COLOR_2 = Color.parseColor("#ff80ab");
    private static final int COLOR_3 = Color.parseColor("#ff4081");
    private static final int COLOR_4 = Color.parseColor("#f50057");//0xFFF50057;

    private final Paint[] mCirclePaints = new Paint[4];
    private float mCurrentProgress = 0;

    private float currentRadius1 = 0;
    private float currentDotSize1 = 0;

    private float currentDotSize2 = 0;
    private float currentRadius2 = 0;

    private int centerX;
    private int centerY;

    private float maxOuterDotsRadius;
    private float maxInnerDotsRadius;
    private float maxDotSize;

    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();


    public DotsView(Context context) {
        super(context);
    }

    public DotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupPaints();
    }

    private void setupPaints() {
        for (int i = 0; i < mCirclePaints.length; i++) {
            mCirclePaints[i] = new Paint();
            mCirclePaints[i].setStyle(Paint.Style.FILL);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        maxDotSize = 8; //20
        maxOuterDotsRadius = w / 2 - maxDotSize * 8; //*2
        maxInnerDotsRadius = 0.5f * maxOuterDotsRadius; // 0.8f
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterDotsFrame(canvas);
        drawInnerDotsFrame(canvas);
    }

    private void drawOuterDotsFrame(Canvas canvas) {
        for (int i = 0; i < DOTS_COUNT; i++) {
            int cX = (int) (centerX + currentRadius1 * Math.cos(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
            int cY = (int) (centerY + currentRadius1 * Math.sin(i * OUTER_DOTS_POSITION_ANGLE * Math.PI / 180));
            canvas.drawCircle(cX, cY, currentDotSize1, mCirclePaints[i % mCirclePaints.length]);
        }
    }

    private void drawInnerDotsFrame(Canvas canvas) {
        for (int i = 0; i < DOTS_COUNT; i++) {
            int cX = (int) (centerX + currentRadius2 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180));
            int cY = (int) (centerY + currentRadius2 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180));
            canvas.drawCircle(cX, cY, currentDotSize2, mCirclePaints[(i + 1) % mCirclePaints.length]);
        }
    }


    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;

        updateInnerDotsPosition();
        updateOuterDotsPosition();
        updateDotsPaints();
        updateDotsAlpha();

        postInvalidate();
    }

    public float getCurrentProgress() {
        return mCurrentProgress;
    }


    private void updateInnerDotsPosition() {
        if (mCurrentProgress < 0.3f) {
            this.currentRadius2 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0, 0.3f, 0.f, maxInnerDotsRadius);
        } else {
            this.currentRadius2 = maxInnerDotsRadius;
        }

        if (mCurrentProgress < 0.2) {
            this.currentDotSize2 = maxDotSize;
        } else if (mCurrentProgress < 0.5) {
            this.currentDotSize2 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.2f, 0.5f, maxDotSize, 0.3 * maxDotSize);
        } else {
            this.currentDotSize2 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.5f, 1f, maxDotSize * 0.3f, 0);
        }

    }

    private void updateOuterDotsPosition() {
        if (mCurrentProgress < 0.3f) {
            this.currentRadius1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.0f, 0.3f, 0, maxOuterDotsRadius * 0.4f);
        } else {
            this.currentRadius1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.3f, 1f, 0.8f * maxOuterDotsRadius, maxOuterDotsRadius);
        }

        if (mCurrentProgress < 0.4) {
            this.currentDotSize1 = maxDotSize;
        } else {
            this.currentDotSize1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.4f, 0.7f, maxDotSize, 0);
        }
    }

//    private void updateOuterDotsPosition() {
//        if (mCurrentProgress < 0.3f) {
//            this.currentRadius1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.0f, 0.3f, 0, maxOuterDotsRadius * 0.8f);
//        } else {
//            this.currentRadius1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.3f, 1f, 0.8f * maxOuterDotsRadius, maxOuterDotsRadius);
//        }
//
//        if (mCurrentProgress < 0.7) {
//            this.currentDotSize1 = maxDotSize;
//        } else {
//            this.currentDotSize1 = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.7f, 1f, maxDotSize, 0);
//        }
//    }

    private void updateDotsPaints() {
        if (mCurrentProgress < 0.5f) {
            float progress = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0f, 0.5f, 0, 1f);
            mCirclePaints[0].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
            mCirclePaints[1].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_2, COLOR_3));
            mCirclePaints[2].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_3, COLOR_4));
            mCirclePaints[3].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_4, COLOR_1));
        } else {
            float progress = (float) Utils.mapValueFromRangeToRange(mCurrentProgress, 0.5f, 1f, 0, 1f);
            mCirclePaints[0].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_2, COLOR_3));
            mCirclePaints[1].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_3, COLOR_4));
            mCirclePaints[2].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_4, COLOR_1));
            mCirclePaints[3].setColor((Integer) mArgbEvaluator.evaluate(progress, COLOR_1, COLOR_2));
        }
    }

    private void updateDotsAlpha() {
        float progress = (float) Utils.clamp(mCurrentProgress, 0.6f, 1f);
        int alpha = (int) Utils.mapValueFromRangeToRange(progress, 0.6f, 1f, 255, 0);
        mCirclePaints[0].setAlpha(alpha);
        mCirclePaints[1].setAlpha(alpha);
        mCirclePaints[2].setAlpha(alpha);
        mCirclePaints[3].setAlpha(alpha);
    }

    public static final Property<DotsView, Float> DOTS_PROGRESS = new Property<DotsView, Float>(Float.class, "dotsProgress") {
        @Override
        public Float get(DotsView object) {
            return object.getCurrentProgress();
        }

        @Override
        public void set(DotsView object, Float value) {
            object.setCurrentProgress(value);
        }
    };
}
