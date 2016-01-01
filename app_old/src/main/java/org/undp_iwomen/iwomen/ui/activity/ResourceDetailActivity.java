package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.smk.iwomen.BaseActionBarActivity;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;


public class ResourceDetailActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;
    private CustomTextView txtName;
    private CustomTextView txtBody;
    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;
    private TextView profileName;


    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;


    String mstrTitleEng, mstrTitleMm, mstrContentEng, mstrContentMm, mstrAuthorName , mstrAuthorId, mstrAuthorImgPath, mstrPostDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sub_resource);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");


        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i  =getIntent();

        mstrTitleEng = i.getStringExtra("TitleEng");
        mstrTitleMm = i.getStringExtra("TitleMM");
        mstrContentEng = i.getStringExtra("ContentEng");
        mstrContentMm = i.getStringExtra("ContentMM");
        mstrAuthorName = i.getStringExtra("AuthorName");
        mstrAuthorId = i.getStringExtra("AuthorId");
        mstrAuthorImgPath = i.getStringExtra("AuthorImgPath");
        mstrPostDate = i.getStringExtra("PostDate");




        txtName = (CustomTextView)findViewById(R.id.tipdetail_title_name);
        txtBody = (CustomTextView)findViewById(R.id.tipdetail_body);
        profileImg = (RoundedImageView)findViewById(R.id.tipdetail_profilePic_rounded);
        profileName = (TextView)findViewById(R.id.tipdetail_content_username);
        profileProgressbar = (ProgressBar)findViewById(R.id.tipdetail_progressBar_profile_item);




        strLang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);
        if(strLang.equals(Utils.ENG_LANG)){

            textViewTitle.setText(R.string.leadership_eng);
            txtBody.setText(mstrContentEng);
            txtName.setText(mstrTitleEng);

            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));


            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.leadership_mm);
            txtBody.setText(mstrContentMm);
            txtName.setText(mstrTitleEng);

            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));


            //txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            //txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }



        profileProgressbar.setVisibility(View.GONE);



        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(mstrAuthorImgPath != null && mstrAuthorImgPath != "") {

            try {

                Picasso.with(mContext)
                        .load(mstrAuthorImgPath) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(profileImg, new ImageLoadedCallback(profileProgressbar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        }else{
            profileProgressbar.setVisibility(View.GONE);
        }

        //profileImg.setImageResource(R.drawable.astrid);
        profileName.setText(mstrAuthorName);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }
}
