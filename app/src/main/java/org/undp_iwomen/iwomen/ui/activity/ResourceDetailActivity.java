package org.undp_iwomen.iwomen.ui.activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algo.hha.emojiicon.EmojiconEditText;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.smk.model.CommentItem;
import com.smk.skalertmessage.SKToastMessage;
import com.squareup.picasso.Picasso;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.SubResourceItem;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;
import org.undp_iwomen.iwomen.model.Helper;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverStringConverterAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.adapter.CommentAdapter;
import org.undp_iwomen.iwomen.ui.fragment.AudioVisualizerFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ProgressWheel;
import org.undp_iwomen.iwomen.ui.widget.animatedbutton.AnimatedButton;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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
    String mstrAudioFilePath ;


    String mstrTitleEng, mstrTitleMm, mstrContentEng, mstrContentMm, mstrAuthorName ,mstrAuthorNameMM, mstrAuthorId, mstrAuthorImgPath, mstrPostDate , mstrAuthorRoleMM, mstrAuthorRoleEng;
    String mstrSubResourceTitleEng, mstrSubResourceTitleMm;

    private CustomTextView txtSocialShare;
    private ShareButton shareButton;
    private ImageView img_social_no_ear_share;


    String share_data;
    String share_img_url_data;
    private AnimatedButton mSocialNoEarLikeAnimatedButton;
    private SubResourceItem subResouceItemObj;
    private String postId, postObjId, like_status, postType;
    private String user_name, user_obj_id, user_ph, user_id,userprofile_Image_path;
    private SharedPreferences mSharedPreferencesUserInfo;
    private TextView txt_social_no_ear_like_counts;
    private ImageView img_viber_share,img__social_audio;

    private Cursor cursorMain;
    public ImageView emojiIconToggle;
    //Sticker
    public RoundedImageView stickerImg;
    private LinearLayout ly_sticker_holder;
    private EmojiconEditText et_comment;
    private ImageView img_comment_submit;
    private boolean alreadySticker = false;

    //TODO Comment
    ListView listView_Comment;
    private int paginater = 1;
    private List<com.smk.model.CommentItem> listComment;
    private CommentAdapter adapter;
    private ProgressWheel progressWheel_comment;
    private final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    private final String WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PREPARE_AUDIO_PERMISSION = "android.permission.MODIFY_AUDIO_SETTINGS";

    private ProgressBar feed_item_progressBar;
    private ProgressBar profile_item_progressBar;
    private RoundedImageView profile;
    CustomTextView post_content_user_name;
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
        mstrAuthorNameMM = i.getStringExtra("AuthorNameMM");
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

        // Google Analytics
        MainApplication application = (MainApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        //Update Tracking name in Oct 25 after version 1.7 release
        mTracker.setScreenName("Resource Detail ~ "+  mstrTitleEng + ">" +mstrSubResourceTitleEng);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);
        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);


        txtName.setVisibility(View.GONE);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        if(strLang.equals(Utils.ENG_LANG)){

            textViewTitle.setText(mstrTitleEng);
            txtBody.setText(mstrContentEng);
            Linkify.addLinks(txtBody, Linkify.WEB_URLS);
            txtName.setText(mstrSubResourceTitleEng);

            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

            txtAuthorRole.setText(mstrAuthorRoleEng);

            profileName.setText(mstrAuthorName);



            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else if (strLang.equals(Utils.MM_LANG)) {
            profileName.setText(mstrAuthorNameMM);

            textViewTitle.setText(mstrTitleMm);
            txtBody.setText(mstrContentMm);
            txtName.setText(mstrSubResourceTitleMm);
            txtAuthorRole.setText(mstrAuthorRoleMM);

            profileName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            txtBody.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            txtAuthorRole.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        } else {//FOR ALl MM FONT

            profileName.setText(mstrAuthorNameMM);

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
        /*lysocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strLang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.resource_coming_soon_eng));
                } else {

                    Utils.doToastMM(mContext, getResources().getString(R.string.resource_coming_soon_mm));
                }
            }
        });*/
        //Linn Wah
        listView_Comment = (ListView) findViewById(R.id.postdetail_comment_listview);
        img_viber_share = (ImageView) findViewById(R.id.social_no_ear_viber_img);
        img_viber_share.setOnClickListener(this);

        txtSocialShare =(CustomTextView)findViewById(R.id.social_no_ear_share_txt);
        txtSocialShare.setOnClickListener(this);

        shareButton = (ShareButton) findViewById(R.id.social_no_ear_fb_share_button);
        shareButton.setShareContent(getLinkContent());

        img_social_no_ear_share = (ImageView) findViewById(R.id.social_no_ear_share_img);
        img_social_no_ear_share.setOnClickListener(this);

        mSocialNoEarLikeAnimatedButton = (AnimatedButton) findViewById(R.id.social_no_ear_like_animated_button);
        mSocialNoEarLikeAnimatedButton.setEnabled(true);

        /*profile_item_progressBar = (ProgressBar) findViewById(R.id.postdetail_progressBar_profile_item);
        profile = (RoundedImageView) findViewById(R.id.postdetail_profilePic_rounded);
        post_content_user_name = (CustomTextView) findViewById(R.id.postdetail_content_username);*/ //Linn Wah After UI Out

       // progressWheel_comment = (ProgressWheel) findViewById(R.id.postdetail_progress_wheel_comment);

        img__social_audio = (ImageView) findViewById(R.id.social_no_ear_earphone_img);
        img__social_audio.setEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            subResouceItemObj = new Gson().fromJson(bundle.getString("postObj"), SubResourceItem.class);
        }
        Log.e("SubResource---->>>"," "+subResouceItemObj.getLikes().toString());
        postType = i.getStringExtra("post_type");
        postId = subResouceItemObj.getId().toString();// i.getStringExtra("post_id");
        postObjId = subResouceItemObj.getObjectId();
        mstrAudioFilePath = subResouceItemObj.getAudioFile();
        //strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

         //temp for SQLite
        /* cursorMain = getContentResolver().query(IwomenProviderData.SubResourceDetailProvider.CONTETNT_URI, null, "sub_resource_id = ? AND user_id = ?", new String[]{ postId,user_id },null );

         Log.e("Cursor Count >>>>"," " + cursorMain.getCount());
        if(cursorMain.getCount() > 0){
            Toast.makeText(ResourceDetailActivity.this, "Already Like", Toast.LENGTH_SHORT).show();
            mSocialNoEarLikeAnimatedButton.setEnabled(false);
            String sqLiteLikeCount = cursorMain.getString(cursorMain.getColumnIndex("like_count"));
            Log.e("Like Count >>>>"," " + sqLiteLikeCount);
        }*/
        if(subResouceItemObj.getLikes() != null){
            mSocialNoEarLikeAnimatedButton.setText(subResouceItemObj.getLikes().toString());
        }else{
            mSocialNoEarLikeAnimatedButton.setText("0");
        }

        mSocialNoEarLikeAnimatedButton.setCallbackListener(new AnimatedButton.Callbacks() {
            @Override
            public void onClick() {
                {

                        SMKserverStringConverterAPI.getInstance().getService().postiWomenSubResourceDetailLikes(postId, user_id, new Callback<String>() {
                            @Override
                            public void success(String item, Response response) {
                                Log.e("ResourceLike>>",item);
                                mSocialNoEarLikeAnimatedButton.setEnabled(false);
                                mSocialNoEarLikeAnimatedButton.setText(item);
                                mSocialNoEarLikeAnimatedButton.setEnabled(false);
                                mSocialNoEarLikeAnimatedButton.setOnClickListener(ResourceDetailActivity.this);
                                saveLikeStatusToSQLite(postId,user_id,item);


                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("ResourceLike Fail>>",error.toString());
                            }
                        });
                    }

                }
        });

        img__social_audio.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // for Audio Streaming

               if (mstrAudioFilePath.length() == 10) {
                    SKToastMessage.showMessage(ResourceDetailActivity.this, getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);
                }

                if (!hasPermission(RECORD_AUDIO) || !hasPermission(WRITE_STORAGE) || !hasPermission(STORAGE_READ_PERMISSION) || !hasPermission(PREPARE_AUDIO_PERMISSION)) {
                    //if no permission, request permission
                    String[] perms = {RECORD_AUDIO, WRITE_STORAGE, STORAGE_READ_PERMISSION, PREPARE_AUDIO_PERMISSION};
                    int permsRequestCode = 200;
                    requestPermissions(perms, permsRequestCode);

                }

                if (Connection.isOnline(getApplicationContext())) {

                    //String url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3"; //Default
                    if (mstrAudioFilePath != null && mstrAudioFilePath.length() > 20) {

                        Log.e("<<ResourceDetail>>", "<<isPlay>>" + "show audio visualizer dialog" + mstrAudioFilePath);
                        DialogFragment visualizerFragment = AudioVisualizerFragment.newInstance(mstrAudioFilePath, strLang);
                        visualizerFragment.show(getSupportFragmentManager(), "AudioVisualizer");


                    } else {
                        //TODO Audio File Null case
                        // TODO Auto-generated method stub
                        SKToastMessage.showMessage(ResourceDetailActivity.this, getResources().getString(R.string.audio_not_availabe_msg), SKToastMessage.INFO);


                    }


                } else {
                    //SKConnectionDetector.getInstance(this).showErrorMessage();
                }

            }

        });

        //getCommentByPagination();

        //LinnWah AfterUI out

       /* post_content_user_name.setText(subResouceItemObj.getAuthorName());
        if (mstrAuthorImgPath != null && !mstrAuthorImgPath.isEmpty()) {
            try {
                //profilePictureView.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(subResouceItemObj.getAuthorImgUrl()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(profile, new ImageLoadedCallback(profile_item_progressBar) {
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
        } else {

            profile.setImageResource(R.drawable.blank_profile);
            profile_item_progressBar.setVisibility(View.GONE);
        }

*/
        // Feed image
      /*  if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                postIMg.setVisibility(View.VISIBLE);
                feed_item_progressBar.setVisibility(View.VISIBLE);
                share_img_url_data = item.getImage();
                Picasso.with(this)
                        .load(item.getImage()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(postIMg, new PostDetailActivity.ImageLoadedCallback(feed_item_progressBar) {
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
        } else {
            postIMg.setVisibility(View.GONE);
            feed_item_progressBar.setVisibility(View.GONE);
        }*/


        /*final List<com.smk.model.CommentItem> comment = StoreUtil.getInstance().selectFrom("commentlist");
        if (Connection.isOnline(mContext)) {
            // Showing local data while loading from internet
            if (comment != null && comment.size() > 0) {
                listComment.addAll(comment);
                adapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(this);
                zPDialog.show();
            }

            getCommentByPagination();
        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
            if (comment != null) {
                listComment.clear();
                listComment.addAll(comment);
                adapter.notifyDataSetChanged();
            }
        }*/
    }



    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }
    public void getCommentByPagination() {
        if (Connection.isOnline(mContext)) {
            NetworkEngine.getInstance().getCommentlistByPostIDByPagination(paginater, "tJG4qymeU8", new Callback<List<com.smk.model.CommentItem>>() {
                @Override
                public void success(List<com.smk.model.CommentItem> commentItems, Response response) {

                    listComment = new ArrayList<>();
                    listComment.addAll(commentItems);
                    adapter = new CommentAdapter(ResourceDetailActivity.this, listComment);
                    listView_Comment.setAdapter(adapter);

                    progressWheel_comment.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listView_Comment.setNestedScrollingEnabled(true);
                    } else {

                    }
                    Helper.getListViewSize(listView_Comment);

                    StoreUtil.getInstance().saveTo("commentlist", listComment);
                    //TODO get sticker for comment
                   /* if (!alreadySticker)
                       LoadStickerData();*/
                }

                @Override
                public void failure(RetrofitError error) {
                    progressWheel_comment.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
        }

    }

    private void saveLikeStatusToSQLite(String postId, String user_id, String item) {
        ContentValues cv = new ContentValues();
        cv.put("sub_resource_id",postId);
        cv.put("user_id",user_id);
        cv.put("like_count",item);
        cv.put("status","1");
        getContentResolver().insert(IwomenProviderData.SubResourceDetailProvider.CONTETNT_URI, cv);
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
            case R.id.social_no_ear_like_animated_button:
                if (Connection.isOnline(getApplicationContext())) {


                } else {
                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.no_connection));
                    } else {

                        Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.no_connection_mm));
                    }
                }

                break;
            case R.id.social_no_ear_share_txt:
                shareTextUrl();
                break;
            case R.id.social_no_ear_share_img:
                shareButton.performClick();
                break;
            case R.id.social_no_ear_viber_img:

                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //FLAG_ACTIVITY_NEW_TASK
                    //intent.setData(uri);
                    if (txtBody.getText().length() > 20) {
                        share_data = txtBody.getText().toString().substring(0, 12) + " ...";
                    } else {
                        share_data = txtBody.getText().toString();
                    }
                    intent.putExtra(Intent.EXTRA_SUBJECT, share_data + "...");//Title Of The Post
                    intent.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);
                    intent.setType("text/plain");
                    getApplicationContext().startActivity(intent);
                    Log.i("Resource Detail","Viber Complete");
                } catch (ActivityNotFoundException ex) {
                    Log.i("Social Viber",""+ex);
                }
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
