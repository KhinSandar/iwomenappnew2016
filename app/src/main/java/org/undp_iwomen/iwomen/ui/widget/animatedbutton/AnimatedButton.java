package org.undp_iwomen.iwomen.ui.widget.animatedbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;


/**
 * Created by Toe Lie on 3/2/2016.
 */
public class AnimatedButton extends FrameLayout implements View.OnClickListener {

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private AnimatorSet mAnimatorSet;

    private boolean mChecked;

    private ImageView mIcon;
    private CircleView mCircle;
    private DotsView mDots;
    private TextView mTextView;

    private Drawable mNormalDrawable;
    private Drawable mCheckedDrawable;
    private Callbacks listener;

    private AnimatedButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AnimatedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AnimatedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        LayoutInflater.from(getContext()).inflate(R.layout.animated_button, this, true);
        mIcon = (ImageView) findViewById(R.id.icon);
        mCircle = (CircleView) findViewById(R.id.circle);
        mDots = (DotsView) findViewById(R.id.dots);
        mTextView = (TextView) findViewById(R.id.text);
        setOnClickListener(this);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.AnimatedButton, defStyleAttr, defStyleRes);

        mNormalDrawable = a.getDrawable(R.styleable.AnimatedButton_src);
        mCheckedDrawable = a.getDrawable(R.styleable.AnimatedButton_srcChecked);
        String text = a.getString(R.styleable.AnimatedButton_text);

        if (mNormalDrawable != null) {
            mIcon.setImageDrawable(mChecked ? mCheckedDrawable : mNormalDrawable);
        }

        if (TextUtils.isEmpty(text)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setText(text);
            mTextView.setVisibility(View.VISIBLE);
        }

        a.recycle();
    }

    public void setCallbackListener(Callbacks callbackListener){
        this.listener = callbackListener;
    }

    public interface Callbacks{
        void onClick();
    }

    @Override
    public void onClick(View v) {
        if(this.listener != null){
            this.listener.onClick();
        }
        mChecked = !mChecked;
        mIcon.setImageDrawable(mChecked ? mCheckedDrawable : mNormalDrawable);

        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }

        if (mChecked) {
            mIcon.animate().cancel();
            mIcon.setScaleX(0);
            mIcon.setScaleY(0);
            mCircle.setInnerCircleRadiusProgress(0);
            mCircle.setOuterCircleRadiusProgress(0);
            mDots.setCurrentProgress(0);

            mAnimatorSet = new AnimatorSet();

            ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(mCircle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
            outerCircleAnimator.setDuration(250);
            outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(mCircle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
            innerCircleAnimator.setDuration(200);
            innerCircleAnimator.setStartDelay(200);
            innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator starScaleYAnimator = ObjectAnimator.ofFloat(mIcon, ImageView.SCALE_Y, 0.2f, 1f);
            starScaleYAnimator.setDuration(350);
            starScaleYAnimator.setStartDelay(250);
            starScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator starScaleXAnimator = ObjectAnimator.ofFloat(mIcon, ImageView.SCALE_X, 0.2f, 1f);
            starScaleXAnimator.setDuration(350);
            starScaleXAnimator.setStartDelay(250);
            starScaleXAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(mDots, DotsView.DOTS_PROGRESS, 0, 1f);
            dotsAnimator.setDuration(900);
            dotsAnimator.setStartDelay(50);
            dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);

            mAnimatorSet.playTogether(
                    outerCircleAnimator,
                    innerCircleAnimator,
                    starScaleYAnimator,
                    starScaleXAnimator,
                    dotsAnimator);

            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    mCircle.setInnerCircleRadiusProgress(0);
                    mCircle.setOuterCircleRadiusProgress(0);
                    mDots.setCurrentProgress(0);
                    mIcon.setScaleX(1);
                    mIcon.setScaleY(1);
                }
            });

            mAnimatorSet.start();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIcon.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).setInterpolator(DECCELERATE_INTERPOLATOR);
                setPressed(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
                if (isPressed() != isInside) {
                    setPressed(isInside);
                }
                break;

            case MotionEvent.ACTION_UP:
                mIcon.animate().scaleX(1).scaleY(1).setInterpolator(DECCELERATE_INTERPOLATOR);
                if (isPressed()) {
                    performClick();
                    setPressed(false);
                }
                break;
        }
        return true;
    }

    public void setText(String text){
        mTextView.setText(text);
    }
    public void setmCheckedDrawable(){
        mIcon.setImageDrawable(mCheckedDrawable);
        setPressed(false);
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }


    }
}
