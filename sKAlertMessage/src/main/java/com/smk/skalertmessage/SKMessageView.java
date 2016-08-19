package com.smk.skalertmessage;


import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SKMessageView{
    private static SKMessageView instance;
    private String Message;
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    private int MessageType;
    private View SuccessView;
    private View ErrorView;
    private View InfoView;
    private View WarningView;
    private LinearLayout ContentView;
    private Activity activity;
    private TextView txt_message;
    private View btn_delete;
    private LayoutParams params;
    public SKMessageView(Activity activity) {
        this.activity = activity;

        // TODO Auto-generated constructor stub
    }

    public static SKMessageView getInstance(Activity activity) {
        if(instance == null){
            instance = new SKMessageView(activity);
        }
        instance.init();
        return instance;
    }

    public void showMessage(LinearLayout contentView,String message, int messageType){
        setContentView(contentView);
        instance.init();
        instance.setMessage(message);
        instance.setMessageType(messageType);
        instance.showDialog();
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
    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public void showDialog(){
        switch (getMessageType()) {
            case SUCCESS:
                showSuccess();
                break;
            case ERROR:
                showError();
                break;
            case INFO:
                showInfo();
                break;
            case WARNING:
                showWarning();
                break;
            default:
                showSuccess();
                break;
        }
        if(getMessage() == null){
            throw new RuntimeException("Message may not empty, Please use setMessage(...).");
        }

    }

    private void init(){

        SuccessView = View.inflate(activity, R.layout.sk_message_success, null);
        ErrorView = View.inflate(activity, R.layout.sk_message_error, null);
        InfoView = View.inflate(activity, R.layout.sk_message_info, null);
        WarningView = View.inflate(activity, R.layout.sk_message_warning, null);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    }

    private void showSuccess(){

        getContentView().addView(SuccessView, params);

        txt_message = (TextView) getSuccessView().findViewById(R.id.sk_txt_message);
        txt_message.setText(Html.fromHtml(getMessage()));

        btn_delete = getSuccessView().findViewById(R.id.sk_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Log.v("SKMessageDialog","SKMessageDialog is close.");
                getSuccessView().postDelayed(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        getContentView().removeAllViews();
                    }
                }, 1000);
            }
        });
    }

    private void showError(){

        getContentView().addView(ErrorView, params);

        txt_message = (TextView) getErrorView().findViewById(R.id.sk_txt_message);
        txt_message.setText(Html.fromHtml(getMessage()));

        btn_delete = getErrorView().findViewById(R.id.sk_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Log.v("SKMessageDialog","SKMessageDialog is close.");
                getErrorView().postDelayed(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        getContentView().removeAllViews();
                    }
                }, 1000);
            }
        });
    }

    private void showInfo(){

        activity.addContentView(InfoView, params);

        txt_message = (TextView) getInfoView().findViewById(R.id.sk_txt_message);
        txt_message.setText(Html.fromHtml(getMessage()));

        btn_delete = getInfoView().findViewById(R.id.sk_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Log.v("SKMessageDialog","SKMessageDialog is close.");
                getInfoView().postDelayed(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        getContentView().removeAllViews();
                    }
                }, 1000);
            }
        });
    }

    private void showWarning(){

        getContentView().addView(WarningView, params);

        txt_message = (TextView) getWarningView().findViewById(R.id.sk_txt_message);
        txt_message.setText(Html.fromHtml(getMessage()));

        btn_delete = getWarningView().findViewById(R.id.sk_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                // TODO Auto-generated method stub
                Log.v("SKMessageDialog","SKMessageDialog is close.");
                getContentView().removeAllViews();

            }
        });
    }

    public View getSuccessView() {
        return SuccessView;
    }

    public void setSuccessView(View successView) {
        SuccessView = successView;
    }

    public View getErrorView() {
        return ErrorView;
    }

    public void setErrorView(View errorView) {
        ErrorView = errorView;
    }

    public View getInfoView() {
        return InfoView;
    }

    public void setInfoView(View infoView) {
        InfoView = infoView;
    }

    public View getWarningView() {
        return WarningView;
    }

    public void setWarningView(View warningView) {
        WarningView = warningView;
    }

    public LinearLayout getContentView() {
        return ContentView;
    }

    public void setContentView(LinearLayout contentView) {
        ContentView = contentView;
    }
}
