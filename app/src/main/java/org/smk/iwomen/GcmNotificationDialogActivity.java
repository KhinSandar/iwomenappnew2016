package org.smk.iwomen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smk.model.GcmMessage;
import org.undp_iwomen.iwomen.R;

public class GcmNotificationDialogActivity extends AppCompatActivity {

    private GcmMessage gcmMessage;
    private TextView txt_title;
    private TextView txt_message;
    private Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm_notification_dialog);

        txt_title = (TextView) findViewById(R.id.txt_gcm_title);
        txt_message = (TextView) findViewById(R.id.txt_gcm_message);
        btn_close = (Button) findViewById(R.id.btn_gcm_close);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            gcmMessage = new Gson().fromJson(bundle.getString("gcm_message"), GcmMessage.class);

            txt_title.setText(Html.fromHtml(gcmMessage.getTitle()));
            txt_message.setText(Html.fromHtml(gcmMessage.getMessage()));
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
