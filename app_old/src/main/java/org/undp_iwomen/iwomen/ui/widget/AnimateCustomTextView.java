package org.undp_iwomen.iwomen.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.smk.application.StoreUtil;

import org.undp_iwomen.iwomen.model.FontConverter;

public class AnimateCustomTextView extends ShimmerTextView{
	private Shimmer shimmer;

	public AnimateCustomTextView(Context context){
		super(context);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			if(selected_font != null){
				if(selected_font.equals("default")){

				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( selected_font.equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/mm3-multi-os.ttf"));
				}else if( selected_font.equals("english")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
			}
			initShimmerTextView();
		}
	}
	public AnimateCustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			if(selected_font != null){
				if(selected_font.equals("default")){

				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( selected_font.equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/mm3-multi-os.ttf"));
				}else if( selected_font.equals("english")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/roboto-medium.ttf"));
			}
			initShimmerTextView();
		}
	}
	
	public void initShimmerTextView() {
        shimmer = new Shimmer();
        shimmer.setRepeatCount(0)
        		.setRepeatCount(10)
                .setDuration(3000)
                .setStartDelay(300)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
        shimmer.start(this);
    }
	
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		String selected_font = StoreUtil.getInstance().selectFrom("fonts");
		if(selected_font != null){
			if(selected_font.equals("myanmar3")){
				if(text != null){
					text = FontConverter.zg12uni51(text.toString());
				}
			}
		}
		super.setText(text, type);
	}
	
}
