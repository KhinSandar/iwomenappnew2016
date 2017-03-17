package org.undp_iwomen.iwomen.ui.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import org.undp_iwomen.iwomen.model.FontConverter;
import org.undp_iwomen.iwomen.utils.StoreUtil;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView{
	public CustomTextView(Context context){
		super(context);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			//Log.e("<<CustomTextView>>","1font-"+ selected_font);
			if(selected_font != null){
				if(selected_font.equals("default")){

				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( selected_font.equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/mm3-multi-os.ttf"));
				}else if( selected_font.equals("english")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotomedium.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotomedium.ttf"));
			}
			this.setSelected(true);
		}
			
	}
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			String selected_font = StoreUtil.getInstance().selectFrom("fonts");
			//Log.e("<<CustomTextView>>","2font-"+ selected_font);

			if(selected_font != null){
				if(selected_font.equals("default")){

				}else if( selected_font.equals("zawgyione")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/zawgyi.ttf"));
				}else if( selected_font.equals("myanmar3")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/mm3-multi-os.ttf"));
				}else if( selected_font.equals("english")){
					setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotomedium.ttf"));
				}
			}else{
				setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/robotomedium.ttf"));
			}
			this.setSelected(true);
		}
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		String selected_font = StoreUtil.getInstance().selectFrom("fonts");
		if(selected_font != null){
			if(selected_font.equals("myanmar3")){
				if(text != null){
					text = FontConverter.Correction(FontConverter.zg12uni51(text.toString()));
				}
			}
		}
		super.setText(text, type);
	}
	
	

}
