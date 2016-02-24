package com.smk.skalertmessage;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SKToastMessage extends Toast {
	private static SKToastMessage instance;
	private String Message;
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int WARNING = 3;
	private int MessageType;
	private View mView;
	private Context context;
	private TextView txt_message;
	private Typeface typeface;
	private static int CustomDuration = 0;
	public SKToastMessage(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public static SKToastMessage getInstance(Context context) {
    	if(instance == null){
    		instance = new SKToastMessage(context);
		}
		return instance;
	}
	
	public static void showMessage(Context context, String message, int messageType){
		if(instance == null){
			instance = new SKToastMessage(context);
		}
		instance.setMessage(message);
		instance.setMessageType(messageType);
		if(getCustomDuration() != 0){
			instance.setDuration(getCustomDuration());
		}else{
			instance.setDuration(LENGTH_LONG);
		}
		instance.show();
	}
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public int getMessageType() {
		return MessageType;
	}
	
	public Typeface getTypeface() {
		return typeface;
	}

	public void setTypeface(Typeface typeface) {
		this.typeface = typeface;
	}
	
	public static int getCustomDuration() {
		return CustomDuration;
	}

	public static void setCustomDuration(int customDuration) {
		CustomDuration = customDuration;
	}

	public void setMessageType(int messageType) {
		MessageType = messageType;
		
		switch (getMessageType()) {
			case SUCCESS:
				mView = View.inflate(context, R.layout.sk_toast_message_success, null);
				break;
			case ERROR:
				mView = View.inflate(context, R.layout.sk_toast_message_error, null);
				break;
			case INFO:
				mView = View.inflate(context, R.layout.sk_toast_message_info, null);
				break;
			case WARNING:
				mView = View.inflate(context, R.layout.sk_toast_message_warning, null);
				break;
			default:
				mView = View.inflate(context, R.layout.sk_toast_message_success, null);
				break;
		}
		if(getMessage() == null){
			throw new RuntimeException("Message may not empty, Please use setMessage(...).");
		}
		
		txt_message = (TextView) mView.findViewById(R.id.sk_txt_message);
		txt_message.setText(getMessage());
		
		if(getTypeface() != null)
			txt_message.setTypeface(getTypeface());
		
		setView(mView);

	}
	
}
