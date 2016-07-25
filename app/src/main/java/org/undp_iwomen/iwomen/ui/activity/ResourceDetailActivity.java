package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.plus.model.people.Person;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;
import org.w3c.dom.Text;


public class ResourceDetailActivity extends BaseActionBarActivity implements View.OnClickListener {


    private CustomTextView textViewTitle;
    private CustomTextView txtName;
    private CustomTextView txtBody;
    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;
    private TextView profileName;
    private TextView txtMore;
    private TextView txtAuthorRole;
    private LinearLayout lysocial;


    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;


    String mstrTitleEng, mstrTitleMm, mstrContentEng, mstrContentMm, mstrAuthorName , mstrAuthorId, mstrAuthorImgPath, mstrPostDate , mstrAuthorRoleMM, mstrAuthorRoleEng;
    String mstrSubResourceTitleEng, mstrSubResourceTitleMm;

    private CustomTextView txtSocialShare;
    private ShareButton shareButton;
    private ImageView img_social_no_ear_share;


    String share_data;
    String share_img_url_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sub_resource);//activity_detail_sub_resource
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

        mstrSubResourceTitleEng= i.getStringExtra("SubResourceDetailTitleEng");
        mstrSubResourceTitleMm = i.getStringExtra("SubResourceDetailTitleMM");


        mstrContentEng = i.getStringExtra("ContentEng");
        mstrContentMm = i.getStringExtra("ContentMM");
        mstrAuthorName = i.getStringExtra("AuthorName");
        mstrAuthorRoleEng= i.getStringExtra("AuthorTitleEng");
        mstrAuthorRoleMM= i.getStringExtra("AuthorTitleMM");
        mstrAuthorId = i.getStringExtra("AuthorId");
        mstrAuthorImgPath = i.getStringExtra("AuthorImgPath");
        mstrPostDate = i.getStringExtra("PostDate");




        txtName = (CustomTextView)findViewById(R.id.tipdetail_title_name);
        txtBody = (CustomTextView)findViewById(R.id.tipdetail_body);
        profileImg = (RoundedImageView)findViewById(R.id.tipdetail_profilePic_rounded);
        profileName = (TextView)findViewById(R.id.tipdetail_content_username);
        profileProgressbar = (ProgressBar)findViewById(R.id.tipdetail_progressBar_profile_item);
        txtMore = (TextView)findViewById(R.id.tipdetail_content_user_role_more);
        txtAuthorRole = (TextView)findViewById(R.id.tipdetail_content_user_role);


        lysocial = (LinearLayout)findViewById(R.id.tipdetail_ly_social);

        txtBody.setMovementMethod(LinkMovementMethod.getInstance());

        // Google Analytics
        MainApplication application = (MainApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Resource Detail ~ "+ mstrTitleEng);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        txtName.setVisibility(View.GONE);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        if(strLang.equals(Utils.ENG_LANG)){

            textViewTitle.setText(mstrTitleEng);
            txtBody.setText(mstrContentEng);
            txtName.setText(mstrSubResourceTitleEng);

            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

            txtAuthorRole.setText(mstrAuthorRoleEng);

            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else //FOR ALl MM FONT
        {
            textViewTitle.setText(mstrTitleMm);
            txtBody.setText(mstrContentMm);
            txtName.setText(mstrSubResourceTitleMm);
            txtAuthorRole.setText(mstrAuthorRoleMM);
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));


            //txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            //txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }



        profileProgressbar.setVisibility(View.GONE);

        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(mstrAuthorImgPath != null && mstrAuthorImgPath != "" && !mstrAuthorImgPath.isEmpty() && mstrAuthorImgPath != "null") {

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

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AuthorDetailActivity.class);

                intent.putExtra("AuthorId", mstrAuthorId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        lysocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strLang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.resource_coming_soon_eng));
                } else {

                    Utils.doToastMM(mContext, getResources().getString(R.string.resource_coming_soon_mm));
                }
            }
        });


        txtSocialShare =(CustomTextView)findViewById(R.id.social_no_ear_share_txt);
        txtSocialShare.setOnClickListener(this);

        shareButton = (ShareButton) findViewById(R.id.social_no_ear_fb_share_button);

        img_social_no_ear_share = (ImageView) findViewById(R.id.social_no_ear_share_img);
        img_social_no_ear_share.setOnClickListener(this);

        shareButton.setShareContent(getLinkContent());


    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


        // Add data to the intent, the receiving app will decide
        // what to do with it.
        String share_data;
        share.putExtra(Intent.EXTRA_SUBJECT, txtBody.getText().toString());//Title Of The Post
        if (txtBody.getText().length() > 20) {
            share_data = txtBody.getText().toString().substring(0, 12) + " ...";
        } else {
            share_data = txtBody.getText().toString();
        }

        share.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);

        share.putExtra(Intent.EXTRA_HTML_TEXT, share_data);


        startActivity(Intent.createChooser(share, "I Women Share link!"));
    }

    private ShareLinkContent getLinkContent() {

        if (txtBody.getText().length() > 20) {
            share_data = txtBody.getText().toString().substring(0, 20) + " ...";
        } else {
            share_data = txtBody.getText().toString();
        }

        if (share_img_url_data != null) {
            return new ShareLinkContent.Builder()

                    .setContentTitle(txtName.getText().toString())
                    .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                    .setImageUrl(Uri.parse(share_img_url_data))
                    .setContentDescription(share_data)
                    .build();
        } else {
            return new ShareLinkContent.Builder()

                    .setContentTitle(txtName.getText().toString())
                    .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                    .setContentDescription(share_data)
                    .build();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.social_no_ear_share_txt:
                shareTextUrl();
                break;
            case R.id.social_no_ear_share_img:

                shareButton.performClick();
                break;
        }



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
