package org.undp_iwomen.iwomen.ui.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smk.iwomen.BaseActionBarActivity;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.utils.Utils;

public class PostNewsActivity extends BaseActionBarActivity implements View.OnClickListener {

    EditText et_postid, et_post_title, et_post_content,
            et_post_imgpath, et_post_contenttype, et_userid,
            et_username, et_user_imgpath, et_userstatus,
            et_createdate, et_updateddate;

    TextView post_news_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_news);

        init();

    }

    public void init(){
        et_postid = (EditText) findViewById(R.id.post_news_obj_id);
        et_post_title = (EditText) findViewById(R.id.post_news_title);
        et_post_content = (EditText) findViewById(R.id.post_news_content);
        et_post_imgpath = (EditText) findViewById(R.id.post_news_img_path);
        et_post_contenttype = (EditText) findViewById(R.id.post_news_content_type);
        et_username = (EditText) findViewById(R.id.post_news_user_name);
        et_user_imgpath = (EditText) findViewById(R.id.post_news_user_img_path);
        et_userstatus = (EditText) findViewById(R.id.post_news_user_status);
        et_createdate = (EditText) findViewById(R.id.post_news_user_created_date);
        et_updateddate = (EditText) findViewById(R.id.post_news_user_updated_date);
        et_userid = (EditText) findViewById(R.id.post_news_user_id);

        post_news_button = (TextView) findViewById(R.id.post_news_button);
        post_news_button.setOnClickListener(this);
    }


    private void SetPostData() {

        Utils.doToastEng(PostNewsActivity.this, "Post Save");

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, et_postid.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_TITLE, et_post_title.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, et_post_content.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, "1");
        cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, et_post_imgpath.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, et_post_contenttype.getText().toString());//

        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, et_userid.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, et_username.getText().toString());
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, et_user_imgpath.getText().toString());

        cv.put(TableAndColumnsName.UserUtil.STATUS, et_userstatus.getText().toString());
        cv.put(TableAndColumnsName.UserUtil.CREATED_DATE, et_createdate.getText().toString());
        cv.put(TableAndColumnsName.UserUtil.UPDATED_DATE, et_updateddate.getText().toString());

        //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

        getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);
        finish();

    }

    @Override
    public void onClick(View v) {
        SetPostData();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
