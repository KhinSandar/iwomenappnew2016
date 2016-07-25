package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algo.hha.emojiicon.EmojiconEditText;
import com.algo.hha.emojiicon.EmojiconGridView;
import com.algo.hha.emojiicon.EmojiconsPopup;
import com.algo.hha.emojiicon.emoji.Emojicon;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.smk.model.CommentItem;
import com.smk.skalertmessage.SKToastMessage;
import com.smk.sklistview.SKListView;
import com.squareup.picasso.Picasso;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.IWomenPost;
import org.smk.model.LikeItem;
import org.smk.model.Sticker;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.manager.MainApplication;
import org.undp_iwomen.iwomen.model.Helper;
import org.undp_iwomen.iwomen.model.ISO8601Utils;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.TextWatcherAdapter;
import org.undp_iwomen.iwomen.model.URLSpanNoUnderline;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.adapter.CommentAdapter;
import org.undp_iwomen.iwomen.ui.adapter.StickerGridViewAdapter;
import org.undp_iwomen.iwomen.ui.fragment.AudioVisualizerFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ProgressWheel;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;
import org.undp_iwomen.iwomen.ui.widget.WrappedGridView;
import org.undp_iwomen.iwomen.ui.widget.animatedbutton.AnimatedButton;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostDetailActivity extends BaseActionBarActivity implements View.OnClickListener {

    CustomTextView mPostTile;

    CustomTextView post_content;
    TextView post_like;
    TextView post_img_path;
    TextView post_content_type;
    TextView post_content_user_id;
    TextView post_content_user_name;
    TextView post_content_user_role;
    CustomTextView post_content_user_more_id;
    TextView post_content_posted_date;
    LinearLayout post_content_user_role_ly;
    TextView post_content_user_img_path;
    TextView post_timestamp;
    TextView postdetail_username;
    TextView post_suggest_text;
    private ProgressBar feed_item_progressBar;
    private ProgressBar profile_item_progressBar;
    //private org.undp_iwomen.iwomen.ui.widget.ProfilePictureView profilePictureView;
    private RoundedImageView profile;
    private ResizableImageView postIMg;
    private SharedPreferences mSharedPreferencesUserInfo;
    private String user_name, user_obj_id, user_ph, user_id;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    //private Toolbar toolbar;
    private LinearLayout ly_likes_button;
    private ImageView img_like;
    private TextView txt_like_count, txt_cmd_count;
    private String postId, postObjId, like_status, postType;
    //private TextView txt_simile_emoji_icon;
    private EmojiconEditText et_comment;
    //FrameLayout et_comment_frame;
    private ImageView img_comment_submit;

    private ImageView img_viber_share;
    private ImageView img_download;
    //private ImageView img_player;
    private TextView txt_player;
    private TextView txt_download;

    private ResizableImageView img_credit_logo;
    private TextView txt_credit_link;
    private ProgressBar progressBar_credit;
    private LinearLayout ly_credit;

    private AnimatedButton mLikeAnimatedButton;

    //private CustomTextView txt_lbl_like_post, txt_lbl_share_post;

    private LinearLayout ly_postdetail_share_button;
    private LinearLayout ly_postdetail_download;
    private LinearLayout ly_postdetail_audio;
    private LinearLayout ly_media_main;

    //TODO Comment
    ListView listView_Comment;
    private int paginater = 1;
    private List<com.smk.model.CommentItem> listComment;
    private CommentAdapter adapter;
    private ProgressWheel progressWheel_comment;


    String share_data;
    String share_img_url_data;

    String str_comment_time_long;
    //FOR FB SHARE
    private ShareButton shareButton;
    private CallbackManager callbackManager;
    private final String PENDING_ACTION_BUNDLE_KEY =
            "org.undp_iwomen.iwomen.app:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private IWomenPost iWomenPost;
    private boolean alreadySticker = false;
    private AnimatedButton mSocialNoEarLikeAnimatedButton;
    private ImageView img_credit_icon_img;

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    //Emoji Keyboard
    public ImageView emojiIconToggle;

    //Sticker
    public RoundedImageView stickerImg;
    //Sticker LinearLayout
    private LinearLayout ly_sticker_holder;

    //TODO for sticker grid show up
    private StickerGridViewAdapter mAdapter;
    private WrappedGridView gridView;
    private List<Sticker> stickerArrayList;
    private int sticker_paginater = 1;
    private com.pnikosis.materialishprogress.ProgressWheel progress_wheel;
    private StorageUtil storageUtil;


    private ShareDialog shareDialog;
    private static final String PERMISSION = "publish_actions";
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;

    public static final int MENU_Search = Menu.FIRST;
    public static final int MENU_Share = Menu.FIRST + 1;
    MediaPlayer mMedia;
    private boolean isPlaying;
    private String mstrPostType, mVideoId, mstrAudioFilePath;
    private Context mContext;

    private ProgressDialog mProgressDialog;
    private String mstr_lang;
    private ImageView video_icon;
    String cmd_count = "0";
    String authorID;
    private String userprofile_Image_path;

    //TODO social share
    private CustomTextView txt_social_share;

    //TODO Social With No earing
    private TextView txt_social_no_ear_like;
    private TextView txt_social_no_ear_comment;
    private TextView txt_social_no_ear_share;
    private ImageView img_social_no_ear_fb;
    private ImageView img_social_no_ear_viber;
    private ZProgressHUD zPDialog;

    private ImageView img_social_audio;

    private final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    private final String WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PREPARE_AUDIO_PERMISSION = "android.permission.MODIFY_AUDIO_SETTINGS";



    //New UI
    ImageView img_social_facebook;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(PostDetailActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    private boolean isLoading = true;
    private SKListView.Callbacks skCallbacks = new SKListView.Callbacks() {
        @Override
        public void onScrollState(int scrollSate) {

        }

        @Override
        public void onScrollChanged(int scrollY) {

        }

        @Override
        public void onNextPageRequest() {
            if (!isLoading) {
                getCommentByPagination();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();


        mProgressDialog = new ProgressDialog(PostDetailActivity.this);
        mProgressDialog.setCancelable(false);
        //FB SHARE
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        //FB SHARE
        setContentView(R.layout.activity_post_detail_v2);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);


        //Log.e("<<<<PostID at Detail>>>>","===>" + postId);

        init();
        initEmojiIcon();

        //Sticker Load
        progress_wheel = (com.pnikosis.materialishprogress.ProgressWheel) findViewById(R.id.progress_wheel);
        gridView = (WrappedGridView) findViewById(R.id.grid_view_cate); // Implement On Item click listener

        //gridView.setLoadingView(progressBar);
        progress_wheel.spin();
        progress_wheel.setRimColor(Color.LTGRAY);
        gridView.setLoadingView(progress_wheel);

        storageUtil = StorageUtil.getInstance(getApplicationContext());


    }

    private void init() {


        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);


        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);
        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);


        profile = (RoundedImageView) findViewById(R.id.postdetail_profilePic_rounded);
        mPostTile = (CustomTextView) findViewById(R.id.postdetail_title);
        post_content = (CustomTextView) findViewById(R.id.postdetail_content);
        post_content_user_name = (TextView) findViewById(R.id.postdetail_content_username);
        post_content_posted_date = (TextView) findViewById(R.id.postdetail_content_posted_date);
        post_content_user_role = (TextView) findViewById(R.id.postdetail_content_user_role);
        post_content_user_more_id = (CustomTextView) findViewById(R.id.postdetail_content_user_role_more);
        post_content_user_role_ly = (LinearLayout) findViewById(R.id.postdetail_content_role_more_ly);


        postdetail_username = (TextView) findViewById(R.id.postdetail_username);
        post_suggest_text = (TextView) findViewById(R.id.postdetail_suggest_title);

        //profilePictureView = (org.undp_iwomen.iwomen.ui.widget.ProfilePictureView) findViewById(R.id.postdetail_profilePic);
        postIMg = (ResizableImageView) findViewById(R.id.postdetail_content_img);
        feed_item_progressBar = (ProgressBar) findViewById(R.id.postdetail_feed_item_progressBar);
        profile_item_progressBar = (ProgressBar) findViewById(R.id.postdetail_progressBar_profile_item);

        listView_Comment = (ListView) findViewById(R.id.postdetail_comment_listview);
        ly_likes_button = (LinearLayout) findViewById(R.id.postdetail_like_button);
        //img_like = (ImageView) findViewById(R.id.postdetail_like_txt);
        txt_like_count = (TextView) findViewById(R.id.postdetail_like_count);
        txt_cmd_count = (TextView) findViewById(R.id.postdetail_cmd_count);

        //txt_simile_emoji_icon = (TextView) findViewById(R.id.postdetail_smile_img_upload);
        et_comment = (EmojiconEditText) findViewById(R.id.postdetail_et_comment_upload);
        //et_comment_frame = (FrameLayout) findViewById(R.id.emojicons);
        img_comment_submit = (ImageView) findViewById(R.id.postdetail_submit_comment);


        ly_postdetail_share_button = (LinearLayout) findViewById(R.id.postdetail_share_button);

        ly_postdetail_download = (LinearLayout) findViewById(R.id.detail_ly_download);
        ly_postdetail_audio = (LinearLayout) findViewById(R.id.detail_ly_listen_now);
        ly_media_main = (LinearLayout) findViewById(R.id.detail_ly_media_main);
        ly_sticker_holder = (LinearLayout) findViewById(R.id.postdetail_ly_sticker_holder);

        //img_player = (ImageView) findViewById(R.id.postdetail_img_player);
        txt_player = (TextView) findViewById(R.id.postdetail_player_text);
        emojiIconToggle = (ImageView) findViewById(R.id.toggleEmojiIcon);
        stickerImg = (RoundedImageView) findViewById(R.id.postdetail_img_sticker);


        shareButton = (ShareButton) findViewById(R.id.postdetail_fb_share_button);
        progressWheel_comment = (ProgressWheel) findViewById(R.id.postdetail_progress_wheel_comment);
        video_icon = (ImageView) findViewById(R.id.postdeail_video_icon);

        /*txt_lbl_like_post = (CustomTextView) findViewById(R.id.postdetail_like_post_lbl);
        txt_lbl_share_post = (CustomTextView) findViewById(R.id.postdetail_share_post_lbl);*/

        img_credit_icon_img = (ImageView) findViewById(R.id.postdetail_credit_pic_rounded);
        img_credit_logo = (ResizableImageView) findViewById(R.id.postdetail_credit_img);
        txt_credit_link = (TextView) findViewById(R.id.postdetail_credit_link);
        progressBar_credit = (ProgressBar) findViewById(R.id.postdetail_credit_progress);
        ly_credit = (LinearLayout) findViewById(R.id.postdetail_ly_credit);
        img_social_facebook = (ImageView) findViewById(R.id.social_facebook);

        txt_social_share = (CustomTextView) findViewById(R.id.social_share);
        img_viber_share = (ImageView) findViewById(R.id.social_viber);

        //TODO Social With No earing
        txt_social_no_ear_like = (TextView) findViewById(R.id.social_no_ear_like_txt);
        mSocialNoEarLikeAnimatedButton = (AnimatedButton) findViewById(R.id.social_no_ear_like_animated_button);
        txt_social_no_ear_comment = (TextView) findViewById(R.id.social_no_ear_comment_txt);
        txt_social_no_ear_share = (TextView) findViewById(R.id.social_no_ear_share_txt);
        img_social_no_ear_fb = (ImageView) findViewById(R.id.social_no_ear_share_img);
        img_social_no_ear_viber = (ImageView) findViewById(R.id.social_no_ear_viber_img);
        img_social_audio = (ImageView) findViewById(R.id.social_audio);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        mLikeAnimatedButton = (AnimatedButton) findViewById(R.id.postdetail_like_animated_button);
        mLikeAnimatedButton.setEnabled(true);
        mSocialNoEarLikeAnimatedButton.setEnabled(true);

        //TODO id
        Intent i = getIntent();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            iWomenPost = new Gson().fromJson(bundle.getString("postObj"), IWomenPost.class);
        }
        postType = i.getStringExtra("post_type");
        postId = iWomenPost.getId().toString();// i.getStringExtra("post_id");
        postObjId = iWomenPost.getObjectId();
        mstrAudioFilePath = iWomenPost.getAudioFile();

        // Google Analytics
        MainApplication application = (MainApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Post Detail ~ "+ iWomenPost.getTitle());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //TODO for Comment


        mLikeAnimatedButton.setCallbackListener(new AnimatedButton.Callbacks() {
            @Override
            public void onClick() {


                if (mLikeAnimatedButton.isEnabled()) {
                    if (postType.equalsIgnoreCase("iWomenPost")) {

                        //Check status


                        //
                        SMKserverAPI.getInstance().getService().postIWomenPostLike(postId, user_id, new Callback<LikeItem>() {
                            @Override
                            public void success(LikeItem item, Response response) {
                                mLikeAnimatedButton.setEnabled(false);
                                mLikeAnimatedButton.setText(String.valueOf(iWomenPost.getLikes() + 1));
                                mLikeAnimatedButton.setOnClickListener(PostDetailActivity.this);

                                txt_social_no_ear_like.setText(String.valueOf(iWomenPost.getLikes() + 1));
                                mSocialNoEarLikeAnimatedButton.setText(String.valueOf(iWomenPost.getLikes() + 1));

                                mSocialNoEarLikeAnimatedButton.setEnabled(false);
                                mSocialNoEarLikeAnimatedButton.setOnClickListener(PostDetailActivity.this);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });

                    } else {
                        SMKserverAPI.getInstance().getService().postPostsLike(postId, user_id, new Callback<LikeItem>() {
                            @Override
                            public void success(LikeItem item, Response response) {

                                mLikeAnimatedButton.setEnabled(false);
                                mLikeAnimatedButton.setText(String.valueOf(iWomenPost.getLikes() + 1));
                                mLikeAnimatedButton.setOnClickListener(PostDetailActivity.this);
                                txt_social_no_ear_like.setText(String.valueOf(iWomenPost.getLikes() + 1));
                                mSocialNoEarLikeAnimatedButton.setText(String.valueOf(iWomenPost.getLikes() + 1));

                                mSocialNoEarLikeAnimatedButton.setEnabled(false);
                                mSocialNoEarLikeAnimatedButton.setOnClickListener(PostDetailActivity.this);

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }
            }
        });

        mSocialNoEarLikeAnimatedButton.setCallbackListener(new AnimatedButton.Callbacks() {
            @Override
            public void onClick() {
                mLikeAnimatedButton.performClick();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView_Comment.setNestedScrollingEnabled(true);
        }


        listComment = new ArrayList<>();
        adapter = new CommentAdapter(PostDetailActivity.this, listComment);
        listView_Comment.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        setPostItem(iWomenPost);
        /*if (postId != null) {

            if (Connection.isOnline(getApplicationContext())) {

                getIWomenPostByPostID();

            } else {
                //TODO  showing Local data , before update from server by Id
                setPostItem(iWomenPost);//
            }

        } else {
            Utils.doToastEng(this, "Nothing to show!");
        }*/

        shareButton.setShareContent(getLinkContent());

        //TODO social_facebook. performCLick mhar ShareButton yae yan
        //ly_likes_button.setOnClickListener(this);
        //txt_simile_emoji_icon.setOnClickListener(this);
        img_comment_submit.setOnClickListener(this);
        img_viber_share.setOnClickListener(this);
        //ly_postdetail_share_button.setOnClickListener(this);
        postIMg.setOnClickListener(this);
        img_social_facebook.setOnClickListener(this);
        txt_social_no_ear_share.setOnClickListener(this);
        img_social_no_ear_fb.setOnClickListener(this);
        img_social_no_ear_viber.setOnClickListener(this);
        img_social_audio.setOnClickListener(this);
        txt_social_share.setOnClickListener(this);
        txt_player.setOnClickListener(this
        );
        //mLikeAnimatedButton.setText(item.getLikes() + "");.setOnClickListener(this);
        /*ly_postdetail_audio.setOnClickListener(this);
        ly_postdetail_download.setOnClickListener(this);
        video_icon.setOnClickListener(this);*/


        if (mstrPostType != null) {


            if (mstrPostType.equalsIgnoreCase("Video")) {
                txt_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, YouTubeWebviewActivity.class);

                        if (mVideoId != null) {
                            intent.putExtra("video_id", mVideoId);
                        } else {
                            intent.putExtra("video_id", "YOAu82xu8VY");
                        }


                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);


                    }
                });
            } else {
                // Utils.doToastEng(mContext, "not video");
            }
        }


        et_comment.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mTxtEmojicon.setText(s);
                //et_comment_frame.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //et_comment_frame.setVisibility(View.GONE);

            }
        });

        progressWheel_comment.bringToFront();
        progressWheel_comment.spin();
        //progress_wheel.setBarColor(Color.RED);
        progressWheel_comment.setRimColor(Color.LTGRAY);
        progressWheel_comment.setVisibility(View.VISIBLE);
        //setEmojiconFragment(false);


        //getTestCommentList();


        //TODO after showing Local data , update from server by Id
        //updatePostDetailServerStatus();

        //getCommentCount(postId);// + " comments";


    }


    private void getIWomenPostByPostID() {
        if (Connection.isOnline(getApplicationContext())) {
            NetworkEngine.getInstance().getIwomenPostByPostID(postId, new Callback<IWomenPost>() {
                @Override
                public void success(IWomenPost iWomenPost, Response response) {

                    setPostItem(iWomenPost);//
                    //TODO comment after finish this Detail Post thread
                    //getCommentByPagination();


                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
        }
    }

    private void setPostItem(final IWomenPost item) {
        profile.setAdjustViewBounds(true);
        profile.setScaleType(ImageView.ScaleType.CENTER_CROP);


        post_content_user_name.setText(item.getPostUploadName());

        post_content_user_role.setText(item.getPostAuthorRole());

        authorID = item.getAuthorId();

        //TODO
        if (authorID != null) {


            post_content_user_more_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(mContext, AuthorDetailActivity.class);

                    intent.putExtra("AuthorId", authorID);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        try {
            Date timedate = format.parse(item.getPostUploadedDate());
            post_content_posted_date.setText("Posted on " + sdf.format(timedate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //TODO
        //txt_lbl_share_post.setText(getResources().getString(R.string.post_detail_share_post));

        mstrPostType = item.getContentType();
        mVideoId = item.getVideoId();
        //TODO hide visible
        if (postType.equalsIgnoreCase("iWomenPost")) {
            postdetail_username.setVisibility(View.VISIBLE);
            img_credit_logo.setVisibility(View.VISIBLE);
            img_credit_icon_img.setVisibility(View.VISIBLE);
            txt_credit_link.setVisibility(View.VISIBLE);
            post_content_posted_date.setVisibility(View.VISIBLE);
        } else {
            postdetail_username.setVisibility(View.INVISIBLE);
            img_credit_logo.setVisibility(View.INVISIBLE);
            img_credit_icon_img.setVisibility(View.INVISIBLE);
            txt_credit_link.setVisibility(View.INVISIBLE);
            post_content_posted_date.setVisibility(View.INVISIBLE);

        }

        //TODO TableColumnUpdate 10 data set show in UI
        if (strLang.equals(Utils.ENG_LANG)) {
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));


            if (item.getContentType().equalsIgnoreCase("Letter")) {
                postdetail_username.setText("Dear " + user_name);

                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
                postIMg.setClickable(false);
            } else if (item.getContentType().equalsIgnoreCase("Audio")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.VISIBLE);
                isPlaying = false;
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_headset_grey600_48dp), null, null, null);
                txt_player.setText(R.string.detail_listen_text_eng);

            } else if (item.getContentType().equalsIgnoreCase("Video")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.VISIBLE);
                ly_media_main.setVisibility(View.VISIBLE);

                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_watch_now), null, null, null);

                txt_player.setText(R.string.detail_play_text_eng);

                isPlaying = true;
            } else if (item.getContentType().equalsIgnoreCase("Story")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
            } else {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
            }


            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getTitle());
            post_content.setText(item.getContent());
            et_comment.setHint(R.string.post_detail_comment_eng);

            txt_like_count.setText(item.getLikes() + "");
            mLikeAnimatedButton.setText(item.getLikes() + "");

            txt_social_no_ear_like.setText(item.getLikes() + "");
            mSocialNoEarLikeAnimatedButton.setText(item.getLikes() + "");


            txt_cmd_count.setText(item.getCommentCount() + "");
            txt_social_no_ear_comment.setText(item.getCommentCount() + "");
            //txt_lbl_share_post.setText(item.getShareCount() + R.string.post_detail_share_post_eng);

            et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            //post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));

            //Log.e("Link===", "==>" + item.getCreditLink());
            if (item.getCreditName() != null && !item.getCreditName().isEmpty()) {


                txt_credit_link.setVisibility(View.VISIBLE);


                txt_credit_link.setText("Credit to " + item.getCreditName());
                if (item.getCreditLink() != null && !item.getCreditLink().isEmpty()) {
                    txt_credit_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent2 = new Intent(getApplicationContext(), AboutUsWebActivity.class);
                            intent2.putExtra("ActivityName", "PostDetailActivity");
                            intent2.putExtra("URL", item.getCreditLink());
                            startActivity(intent2);
                        }
                    });
                } else {
                    //Utils.doToastEng(getApplicationContext(),"there is no link");
                }


            } else {
                txt_credit_link.setVisibility(View.GONE);
            }

        } else {
            // If Language Myanmar case
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

            if (item.getContentType().equalsIgnoreCase("Letter")) {
                postdetail_username.setText(" ခ်စ္လွစြာေသာ " + user_name);
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
                postIMg.setClickable(false);
            } else if (item.getContentType().equalsIgnoreCase("Audio")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.VISIBLE);
                video_icon.setVisibility(View.GONE);
                isPlaying = false;
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_headset_grey600_48dp), null, null, null);
                txt_player.setText(R.string.detail_listen_text_mm);

            } else if (item.getContentType().equalsIgnoreCase("Video")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.VISIBLE);
                video_icon.setVisibility(View.VISIBLE);
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_watch_now), null, null, null);
                txt_player.setText(R.string.detail_play_text_mm);

                isPlaying = true;
            } else if (item.getContentType().equalsIgnoreCase("Story")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
            } else {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
            }

            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getTitleMm());
            post_content.setText(item.getContentMm());
            et_comment.setHint(R.string.post_detail_comment_mm);

            et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));

            //TODO
            /*txt_lbl_like_post.setText(R.string.post_detail_like_post_mm);
            txt_lbl_share_post.setText(item.getShareCount() + R.string.post_detail_share_post_mm);
            */

            if (item.getCreditName() != null && !item.getCreditName().isEmpty()) {
                txt_credit_link.setText("Credit to " + item.getCreditName());

                txt_credit_link.setVisibility(View.VISIBLE);
                /*Pattern pattern = Pattern.compile("[a-zA-Z]+");
                Linkify.addLinks(txt_credit_link,pattern, item.getCredit_link_eng());*/


                /*txt_credit_link.setText(
                        Html.fromHtml(
                                "<a href=" + item.getCredit_link_mm() + ">" + "Credit to " + item.getCredit_name()+" < / a > "));
                txt_credit_link.setMovementMethod(LinkMovementMethod.getInstance());
                stripUnderlines(txt_credit_link);*/
                /*String value = "<html> Credit to " + item.getCredit_name() + "<font color=#9e9e9e><b><a href=\""+ item.getCredit_link_mm() +"\"></a></b></font> </html>";
                Spannable spannedText = (Spannable)
                        Html.fromHtml(value);
                txt_credit_link.setMovementMethod(LinkMovementMethod.getInstance());

                Spannable processedText = removeUnderlines(spannedText);
                txt_credit_link.setText(processedText);*/

                txt_credit_link.setText("Credit to " + item.getCreditName());
                if (item.getCreditLinkMm() != null && !item.getCreditLinkMm().isEmpty()) {
                    txt_credit_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent2 = new Intent(getApplicationContext(), AboutUsWebActivity.class);
                            intent2.putExtra("ActivityName", "PostDetailActivity");
                            intent2.putExtra("URL", item.getCreditLinkMm());
                            startActivity(intent2);

                        }
                    });
                } else {
                    // Utils.doToastEng(getApplicationContext(), "there is no link");
                }
            } else {
                txt_credit_link.setVisibility(View.GONE);
            }
            /*et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));*/
        }


        //TODO check Like Local Value
        /*if (like_status.equalsIgnoreCase("0")) {
            img_like.setImageResource(R.drawable.like_stroke);
        } else {

            img_like.setImageResource(R.drawable.like_fill);
        }*/
        txt_like_count.setText(item.getLikes() + "");
        mLikeAnimatedButton.setText(item.getLikes() + "");

        txt_social_no_ear_like.setText(item.getLikes() + "");
        mSocialNoEarLikeAnimatedButton.setText(item.getLikes() + "");

        txt_cmd_count.setText(item.getCommentCount() + "");
        txt_social_no_ear_comment.setText(item.getCommentCount() + "");
        if (item.getCreditLogoUrl() != null && !item.getCreditLogoUrl().isEmpty()) {
            try {
                img_credit_logo.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(item.getCreditLogoUrl()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(img_credit_logo, new ImageLoadedCallback(progressBar_credit) {
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
            img_credit_logo.setVisibility(View.GONE);
            profile.setImageResource(R.drawable.blank_profile);

            progressBar_credit.setVisibility(View.GONE);
        }

        //viewHolder.mCatNameTextView.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        //viewHolder.profilePictureView.setProfileId(item.get());
        if (item.getPostUploadUserImgPath() != null && !item.getPostUploadUserImgPath().isEmpty()) {
            try {
                //profilePictureView.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(item.getPostUploadUserImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
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

        // Feed image
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                postIMg.setVisibility(View.VISIBLE);
                feed_item_progressBar.setVisibility(View.VISIBLE);
                share_img_url_data = item.getImage();
                Picasso.with(this)
                        .load(item.getImage()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(postIMg, new ImageLoadedCallback(feed_item_progressBar) {
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
        }


        final List<com.smk.model.CommentItem> comment = StoreUtil.getInstance().selectFrom("commentlist");
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
        }
    }

    public void LoadStickerData() {
        if (Connection.isOnline(getApplicationContext())) {

            SMKserverAPI.getInstance().getService().getStickersByPagination(sticker_paginater, new Callback<List<Sticker>>() {
                @Override
                public void success(List<Sticker> stickers, Response response) {
                    alreadySticker = true;
                    stickerArrayList = new ArrayList<Sticker>();
                    stickerArrayList.addAll(stickers);
                    if (mAdapter == null) {
                        mAdapter = new StickerGridViewAdapter(PostDetailActivity.this, mContext, stickerArrayList);

                    }
                    //sticker_paginater++;
                    progress_wheel.setVisibility(View.GONE);
                    gridView.setAdapter(mAdapter);
                    StoreUtil.getInstance().saveTo("StickersList", stickerArrayList);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    progress_wheel.setVisibility(View.GONE);
                }
            });


        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
            List<Sticker> stickers = StoreUtil.getInstance().selectFrom("StickersList");
            if (stickers.size() > 0) {
                mAdapter = new StickerGridViewAdapter(this, mContext, stickers);
                gridView.setAdapter(mAdapter);
            }

            //mAdapter.notifyDataSetChanged();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(mContext, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                //popup.dismiss();
                ly_sticker_holder.setVisibility(View.GONE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                et_comment.setFocusableInTouchMode(false);


                String cmt_text = null;
                if (et_comment.length() != 0) {
                    cmt_text = et_comment.getText().toString();
                }
                //TODO it is not postId , it is postObj id

                if (userprofile_Image_path != null) {

                } else {
                    userprofile_Image_path = "http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-7f0323bc-a862-4184-9e51-d55189fcab18-ic_launcher.png";
                }
                //Log.e("<<Comment>>","==>" +postType,postObjId, user_obj_id, user_name, stickerArrayList.get(position).getStickerImgPath()+""+ userprofile_Image_path+""+ cmt_text );
                SMKserverAPI.getInstance().getService().postCommentByPostID(postType, postObjId, user_obj_id, user_name, stickerArrayList.get(position).getStickerImgPath(), userprofile_Image_path, cmt_text, new Callback<CommentItem>() {
                    @Override
                    public void success(CommentItem calendarEvent, Response response) {

                        txt_cmd_count.setText(calendarEvent.getCommentCount() + "");
                        txt_social_no_ear_comment.setText(calendarEvent.getCommentCount() + "");
                        getCommentByPagination();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgressDialog.dismiss();
                    }
                });
                //TODO COMMENT PSOT


            }
        });

    }


    public void initEmojiIcon() {
        boolean isLargerLollipop = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);

        int reduceheight = 0;
        if (isLargerLollipop) {
            reduceheight = (int) getResources().getDimension(R.dimen.reduceeheight);
        }
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        final EmojiconsPopup popup = new EmojiconsPopup(getWindow().getDecorView().getRootView(), this, reduceheight);


        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();


        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {


            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.smiley);
            }
        });


        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {


            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });


        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {


            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (et_comment == null || emojicon == null) {
                    return;
                }


                int start = et_comment.getSelectionStart();
                int end = et_comment.getSelectionEnd();
                if (start < 0) {
                    et_comment.append(emojicon.getEmoji());
                } else {
                    et_comment.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });


        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {


            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                et_comment.dispatchKeyEvent(event);
            }
        });


        emojiIconToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {
                    ly_sticker_holder.setVisibility(View.GONE);

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.ic_action_keyboard);
                    }

                    //else, open the text keyboard first and immediately after that show the emoji popup
                    else {
                        et_comment.setFocusableInTouchMode(true);
                        et_comment.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(et_comment, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiIconToggle, R.drawable.ic_action_keyboard);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();

                }
            }
        });
        //Sticker icon onClickListern
        stickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup.dismiss();

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                et_comment.setFocusableInTouchMode(false);

                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                et_comment.dispatchKeyEvent(event);

                ly_sticker_holder.setVisibility(View.VISIBLE);

            }
        });
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    public void getCommentByPagination() {
        if (Connection.isOnline(mContext)) {
            progressWheel_comment.setVisibility(View.VISIBLE);
            //TODO BY POST ID

            NetworkEngine.getInstance().getCommentlistByPostIDByPagination(paginater, postObjId, new Callback<List<com.smk.model.CommentItem>>() {
                @Override
                public void success(List<com.smk.model.CommentItem> commentItems, Response response) {

                    listComment = new ArrayList<>();
                    listComment.addAll(commentItems);
                    adapter = new CommentAdapter(PostDetailActivity.this, listComment);
                    listView_Comment.setAdapter(adapter);

                    progressWheel_comment.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listView_Comment.setNestedScrollingEnabled(true);
                    } else {

                    }
                    Helper.getListViewSize(listView_Comment);

                    StoreUtil.getInstance().saveTo("commentlist", listComment);
                    //TODO get sticker for comment
                    if (!alreadySticker)
                        LoadStickerData();
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

    private void getLocalPostDetail(String postId) {
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};

        Cursor cursor = getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC LIMIT 1 ");


        String post_obj_id = "";
        String post_title = "";
        String post_content = "";
        int post_like = 0;
        String post_img_path = "";
        String post_content_type = "";
        String post_content_user_id = "";
        String post_content_user_name = "";
        String post_content_user_img_path = "";

        String video_id = "";
        String post_content_suggest_text = "";
        String post_content_mm = "";
        String post_content_title_mm = "";

        String like_status = "";
        String status = "";
        String created_at = "";
        String updated_at = "";

        //TODO TableColumnUpdate 5
        String author_id;
        String author_role;
        String author_role_mm;
        String credit_name;
        String credit_logo_link;
        String credit_link_mm;
        String credit_link_eng;
        int post_comment_count = 0;
        int post_share_count = 0;

        try {

            if (cursor != null && cursor.moveToFirst()) {
                int i = 0;
                do {

                    post_obj_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_OBJ_ID));
                    post_title = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_TITLE));
                    post_content = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT));
                    post_like = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_LIKES));
                    post_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_IMG_PATH));
                    post_content_type = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES));
                    post_content_user_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID));
                    post_content_user_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME));
                    post_content_user_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH));

                    video_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID));
                    post_content_suggest_text = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT));
                    post_content_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_MM));
                    post_content_title_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM));

                    //TODO TableColumnUpdate 6
                    author_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ID));
                    author_role = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE));

                    author_role_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE_MM));
                    credit_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_NAME));
                    credit_logo_link = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LOGO_URL));
                    credit_link_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LINK_MM));
                    credit_link_eng = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LINK_ENG));
                    post_comment_count = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.COMMENT_COUNT));
                    post_share_count = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.SHARE_COUNT));

                    like_status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.LIKE_STATUS));
                    status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.STATUS));
                    created_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE));
                    updated_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.UPDATED_DATE));


                    FeedItem item = new FeedItem();

                    item.setPost_obj_id(post_obj_id);
                    item.setPost_title(post_title);
                    item.setPost_content(post_content);
                    item.setPost_like(post_like);
                    item.setPost_img_path(post_img_path);
                    item.setPost_content_type(post_content_type);
                    item.setPost_content_user_id(post_content_user_id);
                    item.setPost_content_user_name(post_content_user_name);
                    item.setPost_content_user_img_path(post_content_user_img_path);

                    item.setPost_content_mm(post_content_mm);
                    item.setPost_content_video_id(video_id);
                    item.setPost_content_suggest_text(post_content_suggest_text);
                    item.setPost_title_mm(post_content_title_mm);

                    //TODO TableColumnUpdate 7
                    item.setPost_content_author_id(author_id);
                    item.setPost_content_author_role(author_role);


                    item.setAuthor_role_mm(author_role_mm);//author_role_mm
                    item.setCredit_name(credit_name); //credit_name
                    item.setCredit_logo_link(credit_logo_link); //credit_logo_link
                    item.setCredit_link_mm(credit_link_mm); //credit_link_mm
                    item.setCredit_link_eng(credit_link_eng); //credit_link_eng
                    item.setPost_comment_count(post_comment_count);
                    item.setPost_share_count(post_share_count);

                    item.setPost_like_status(like_status);
                    item.setStatus(status);
                    item.setCreated_at(created_at);
                    item.setUpdated_at(updated_at);



                    /*String image = feedObj.isNull("image") ? null : feedObj
                            .getString("image");*/

                    this.like_status = like_status;
                    Log.e("Db like status", "==>" + like_status);


                    i++;


                } while (cursor.moveToNext());
            }

            cursor.close();


            FeedItem item = getPostById(postId);
            Log.i("Item", item.getPost_content());

            //TODO set Local data
            setItem(item);

            //lost_data_list, lost_data_id_list, lost_data_obj_id_list ,lost_data_img_url_list
            //storageUtil.SaveArrayListToSD("lost_data_list", lost_data_list);


        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

    }

    private void stripUnderlines(TextView tv) {

        Spannable s = (Spannable) tv.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);

        }

        tv.setText(s);

    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);

        }
        return p_Text;
    }


    private void setItem(final FeedItem item) {
        profile.setAdjustViewBounds(true);
        profile.setScaleType(ImageView.ScaleType.CENTER_CROP);


        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);


        if (item.getPost_content_suggest_text() != null && !item.getPost_content_suggest_text().isEmpty()) {
            post_suggest_text.setText(item.getPost_content_suggest_text());

        }
        post_content_user_name.setText(item.getPost_content_user_name());

        post_content_user_role.setText(item.getPost_content_author_role());

        authorID = item.getPost_content_author_id();

        if (authorID != null) {


            post_content_user_more_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(mContext, AuthorDetailActivity.class);

                    intent.putExtra("AuthorId", authorID);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        ParsePosition pp = new ParsePosition(0);
        try {
            Date timedate = ISO8601Utils.parse(item.getCreated_at().toString(), pp);

            post_content_posted_date.setText("Posted on " + sdf.format(timedate));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //txt_lbl_share_post.setText(getResources().getString(R.string.post_detail_share_post));

        mstrPostType = item.getPost_content_type();
        mVideoId = item.getPost_content_video_id();
        //TODO TableColumnUpdate 10 data set show in UI
        if (strLang.equals(Utils.ENG_LANG)) {
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));


            if (item.getPost_content_type().equalsIgnoreCase("Letter")) {
                postdetail_username.setText("Dear " + user_name);

                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
                postIMg.setClickable(false);
            } else if (item.getPost_content_type().equalsIgnoreCase("Audio")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.VISIBLE);
                isPlaying = false;
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_headset_grey600_48dp), null, null, null);

                txt_player.setText(R.string.detail_listen_text_eng);

            } else if (item.getPost_content_type().equalsIgnoreCase("Video")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.VISIBLE);
                ly_media_main.setVisibility(View.VISIBLE);

                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_watch_now), null, null, null);

                txt_player.setText(R.string.detail_play_text_eng);

                isPlaying = true;
            } else if (item.getPost_content_type().equalsIgnoreCase("Story")) {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
            } else {
                postdetail_username.setText("");
                video_icon.setVisibility(View.GONE);
                ly_media_main.setVisibility(View.GONE);
                isPlaying = true;
            }


            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getPost_title());
            post_content.setText(item.getPost_content());
            et_comment.setHint(R.string.post_detail_comment_eng);
            txt_like_count.setText(item.getPost_like() + "");
            mLikeAnimatedButton.setText(item.getPost_like() + "");
            txt_social_no_ear_like.setText(item.getPost_like() + "");
            mSocialNoEarLikeAnimatedButton.setText(item.getPost_like() + "");

            txt_cmd_count.setText(item.getPost_comment_count() + "Comments");
            txt_social_no_ear_comment.setText(item.getPost_comment_count() + "Comments");
            //txt_lbl_share_post.setText(item.getPost_share_count() + R.string.post_detail_share_post_eng);

            et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));
            post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.NORMAL));

            txt_credit_link.setText("Credit to " + item.getCredit_name());
            Log.e("Link===", "==>" + item.getCredit_link_eng());
            if (item.getCredit_name() != null && !item.getCredit_name().isEmpty()) {
                txt_credit_link.setVisibility(View.VISIBLE);


                txt_credit_link.setText("Credit to " + item.getCredit_name());
                if (item.getCredit_link_eng() != null && !item.getCredit_link_eng().isEmpty()) {
                    txt_credit_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent2 = new Intent(getApplicationContext(), AboutUsWebActivity.class);
                            intent2.putExtra("ActivityName", "PostDetailActivity");
                            intent2.putExtra("URL", item.getCredit_link_eng());
                            startActivity(intent2);
                        }
                    });
                } else {
                    //Utils.doToastEng(getApplicationContext(),"there is no link");
                }


            } else {
                txt_credit_link.setVisibility(View.GONE);
            }

        } else {
            postdetail_username.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));

            if (item.getPost_content_type().equalsIgnoreCase("Letter")) {
                postdetail_username.setText(" ခ်စ္လွစြာေသာ " + user_name);
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
                postIMg.setClickable(false);
            } else if (item.getPost_content_type().equalsIgnoreCase("Audio")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.VISIBLE);
                video_icon.setVisibility(View.GONE);
                isPlaying = false;
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_headset_grey600_48dp), null, null, null);

                txt_player.setText(R.string.detail_listen_text_mm);

            } else if (item.getPost_content_type().equalsIgnoreCase("Video")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.VISIBLE);
                video_icon.setVisibility(View.VISIBLE);
                txt_player.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_watch_now), null, null, null);

                txt_player.setText(R.string.detail_play_text_mm);

                isPlaying = true;
            } else if (item.getPost_content_type().equalsIgnoreCase("Story")) {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
            } else {
                postdetail_username.setText("");
                ly_media_main.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                isPlaying = true;
            }

            //post_timestamp.setText(item.getCreated_at());
            mPostTile.setText(item.getPost_title_mm());
            post_content.setText(item.getPost_content_mm());
            et_comment.setHint(R.string.post_detail_comment_mm);

            et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));

            //TODO
            /*txt_lbl_like_post.setText(R.string.post_detail_like_post_mm);
            txt_lbl_share_post.setText(item.getPost_share_count() + R.string.post_detail_share_post_mm);*/

            txt_credit_link.setText("Credit to " + item.getCredit_name());
            if (item.getCredit_name() != null && !item.getCredit_name().isEmpty()) {
                txt_credit_link.setVisibility(View.VISIBLE);
                /*Pattern pattern = Pattern.compile("[a-zA-Z]+");
                Linkify.addLinks(txt_credit_link,pattern, item.getCredit_link_eng());*/


                /*txt_credit_link.setText(
                        Html.fromHtml(
                                "<a href=" + item.getCredit_link_mm() + ">" + "Credit to " + item.getCredit_name()+" < / a > "));
                txt_credit_link.setMovementMethod(LinkMovementMethod.getInstance());
                stripUnderlines(txt_credit_link);*/
                /*String value = "<html> Credit to " + item.getCredit_name() + "<font color=#9e9e9e><b><a href=\""+ item.getCredit_link_mm() +"\"></a></b></font> </html>";
                Spannable spannedText = (Spannable)
                        Html.fromHtml(value);
                txt_credit_link.setMovementMethod(LinkMovementMethod.getInstance());

                Spannable processedText = removeUnderlines(spannedText);
                txt_credit_link.setText(processedText);*/

                txt_credit_link.setText("Credit to " + item.getCredit_name());
                if (item.getCredit_link_mm() != null && !item.getCredit_link_mm().isEmpty()) {
                    txt_credit_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent2 = new Intent(getApplicationContext(), AboutUsWebActivity.class);
                            intent2.putExtra("ActivityName", "PostDetailActivity");
                            intent2.putExtra("URL", item.getCredit_link_mm());
                            startActivity(intent2);

                        }
                    });
                } else {
                    // Utils.doToastEng(getApplicationContext(), "there is no link");
                }
            } else {
                txt_credit_link.setVisibility(View.GONE);
            }
            /*et_comment.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            mPostTile.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_content.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));
            post_suggest_text.setTypeface(MyTypeFace.get(this, MyTypeFace.ZAWGYI));*/
        }


        if (like_status.equalsIgnoreCase("0")) {
            img_like.setImageResource(R.drawable.like_stroke);
        } else {

            img_like.setImageResource(R.drawable.like_fill);
        }
        txt_like_count.setText(item.getPost_like() + " ");
        mLikeAnimatedButton.setText(item.getPost_like() + " ");
        txt_social_no_ear_like.setText(item.getPost_like() + " ");
        mSocialNoEarLikeAnimatedButton.setText(item.getPost_like() + " ");

        txt_cmd_count.setText(item.getPost_comment_count() + "Comments");
        txt_social_no_ear_comment.setText(item.getPost_comment_count() + "Comments");
        if (item.getCredit_logo_link() != null && !item.getCredit_logo_link().isEmpty()) {
            try {
                img_credit_logo.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(item.getCredit_logo_link()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(img_credit_logo, new ImageLoadedCallback(progressBar_credit) {
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
            img_credit_logo.setVisibility(View.GONE);
            profile.setImageResource(R.drawable.blank_profile);

            profile_item_progressBar.setVisibility(View.GONE);
        }

        //viewHolder.mCatNameTextView.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        //viewHolder.profilePictureView.setProfileId(item.get());
        if (item.getPost_content_user_img_path() != null && !item.getPost_content_user_img_path().isEmpty()) {
            try {
                //profilePictureView.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(item.getPost_content_user_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
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

        // Feed image
        if (item.getPost_img_path() != null && !item.getPost_img_path().isEmpty()) {
            try {
                postIMg.setVisibility(View.VISIBLE);
                feed_item_progressBar.setVisibility(View.VISIBLE);
                share_img_url_data = item.getPost_img_path();
                Picasso.with(this)
                        .load(item.getPost_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(postIMg, new ImageLoadedCallback(feed_item_progressBar) {
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
        }
    }

    private FeedItem getPostById(String postId) {


        FeedItem item = new FeedItem();
        if (this != null) {

            /*String selections = TableAndColumnsName.ParseUtil.PARSE_ID + "!=?";
            String[] selectionargs = {cityID};*/
            Cursor cursor = getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?", new String[]{postId}, null);


            String post_obj_id = "";
            String post_title = "";
            String post_content = "";
            int post_like = 0;
            String post_img_path = "";
            String post_content_type = "";
            String post_content_user_id = "";
            String post_content_user_name = "";
            String post_content_user_img_path = "";

            String video_id = "";
            String post_content_suggest_text = "";
            String post_content_mm = "";
            String post_content_title_mm = "";

            //TODO TableColumnUpdate 8
            String author_id;
            String author_role;

            String author_role_mm;
            String credit_name;
            String credit_logo_link;
            String credit_link_mm;
            String credit_link_eng;
            int post_comment_count = 0;
            int post_share_count = 0;


            String like_status = "";
            String status = "";
            String created_at = "";
            String updated_at = "";

            if (cursor != null && cursor.moveToFirst()) {

                do {

                    post_obj_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_OBJ_ID));
                    post_title = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_TITLE));
                    post_content = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT));
                    post_like = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_LIKES));
                    post_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_IMG_PATH));
                    post_content_type = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES));
                    post_content_user_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID));
                    post_content_user_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME));
                    post_content_user_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH));
                    like_status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.LIKE_STATUS));
                    status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.STATUS));
                    created_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE));
                    updated_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.UPDATED_DATE));

                    //TODO TableColumnUpdate 9
                    author_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ID));
                    author_role = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE));


                    author_role_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE_MM));
                    credit_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_NAME));
                    credit_logo_link = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LOGO_URL));
                    credit_link_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LINK_MM));
                    credit_link_eng = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREDIT_LINK_ENG));
                    post_comment_count = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.COMMENT_COUNT));
                    post_share_count = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.PostUtil.SHARE_COUNT));


                    video_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID));
                    post_content_suggest_text = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT));
                    post_content_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_MM));
                    post_content_title_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM));

                    item.setPost_obj_id(post_obj_id);
                    item.setPost_title(post_title);
                    item.setPost_content(post_content);
                    item.setPost_like(post_like);
                    item.setPost_img_path(post_img_path);
                    item.setPost_content_type(post_content_type);
                    item.setPost_content_user_id(post_content_user_id);
                    item.setPost_content_user_name(post_content_user_name);
                    item.setPost_content_user_img_path(post_content_user_img_path);

                    item.setPost_content_video_id(video_id);
                    item.setPost_content_suggest_text(post_content_suggest_text);
                    item.setPost_content_mm(post_content_mm);
                    item.setPost_title_mm(post_content_title_mm);

                    //TODO TableColumnUpdate 10
                    item.setPost_content_author_id(author_id);
                    item.setPost_content_author_role(author_role);

                    item.setAuthor_role_mm(author_role_mm);//author_role_mm
                    item.setCredit_name(credit_name); //credit_name
                    item.setCredit_logo_link(credit_logo_link); //credit_logo_link
                    item.setCredit_link_mm(credit_link_mm); //credit_link_mm
                    item.setCredit_link_eng(credit_link_eng); //credit_link_eng
                    item.setPost_comment_count(post_comment_count);
                    item.setPost_share_count(post_share_count);

                    item.setPost_like_status(like_status);

                    item.setStatus(status);
                    item.setCreated_at(created_at);
                    item.setUpdated_at(updated_at);


                } while (cursor.moveToNext());
            }

        } else {
            Log.e("PostDetail", "Activity Null Case");
        }

        return item;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.postdetail_like_animated_button:
                if (Connection.isOnline(getApplicationContext())) {


                } else {
                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning));
                    } else {

                        Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
                    }
                }

                break;

            case R.id.postdetail_player_text:
                if (mstrPostType.equalsIgnoreCase("Video")) {
                    txt_player.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, YouTubeWebviewActivity.class);

                            if (mVideoId != null) {
                                intent.putExtra("video_id", mVideoId);
                            } else {
                                intent.putExtra("video_id", "YOAu82xu8VY");
                            }


                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);


                        }
                    });
                } else if (mstrPostType.equalsIgnoreCase("Audio")) {
                    img_social_audio.performClick();
                }

                break;
            case R.id.social_viber:

                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //FLAG_ACTIVITY_NEW_TASK
                    //intent.setData(uri);
                    if (post_content.getText().length() > 20) {
                        share_data = post_content.getText().toString().substring(0, 12) + " ...";
                    } else {
                        share_data = post_content.getText().toString();
                    }
                    intent.putExtra(Intent.EXTRA_SUBJECT, share_data + "...");//Title Of The Post
                    intent.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);
                    intent.setType("text/plain");
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {

                }

                break;

            case R.id.postdetail_share_button:
                shareTextUrl();
                if (Connection.isOnline(getApplicationContext())) {
                    HashMap<String, String> _param = new HashMap<>();
                    _param.put("objectId", postId);

                    //TODO Google Analytics

                }

                break;
            case R.id.postdetail_submit_comment:


                if (Connection.isOnline(getApplicationContext())) {


                    if (et_comment.length() == 0) {

                        if (strLang.equals(Utils.ENG_LANG)) {
                            Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.post_detail_comment_eng));
                        } else {

                            Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.post_detail_comment_mm));
                        }
                    } else {

                        mProgressDialog.show();
                        // Utils.doToastEng(getApplicationContext(), "Comment");
                        //TODO Comment Post

                        if (userprofile_Image_path != null) {

                        } else {
                            userprofile_Image_path = "http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-7f0323bc-a862-4184-9e51-d55189fcab18-ic_launcher.png";
                        }

                        SMKserverAPI.getInstance().getService().postCommentTestByPostID(postType, postObjId, user_obj_id, user_name, userprofile_Image_path, et_comment.getText().toString(), new Callback<CommentItem>() {
                            @Override
                            public void success(CommentItem calendarEvent, Response response) {

                                txt_cmd_count.setText(calendarEvent.getCommentCount() + "");
                                txt_social_no_ear_comment.setText(calendarEvent.getCommentCount() + "");

                                //TODO Comment load again
                                if (Connection.isOnline(mContext)) {
                                    progressWheel_comment.setVisibility(View.VISIBLE);
                                    //TODO BY POST ID
                                    NetworkEngine.getInstance().getCommentlistByPostIDByPagination(paginater, postObjId, new Callback<List<com.smk.model.CommentItem>>() {
                                        @Override
                                        public void success(List<com.smk.model.CommentItem> commentItems, Response response) {

                                            listComment = new ArrayList<>();
                                            listComment.addAll(commentItems);
                                            adapter = new CommentAdapter(PostDetailActivity.this, listComment);
                                            listView_Comment.setAdapter(adapter);

                                            progressWheel_comment.setVisibility(View.INVISIBLE);
                                            mProgressDialog.dismiss();

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                listView_Comment.setNestedScrollingEnabled(true);
                                            } else {

                                            }
                                            Helper.getListViewSize(listView_Comment);
                                            //TODO get sticker for comment
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            progressWheel_comment.setVisibility(View.INVISIBLE);
                                            mProgressDialog.dismiss();
                                        }
                                    });
                                } else {
                                    //SKConnectionDetector.getInstance(PostDetailActivity.this).showErrorMessage();
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                mProgressDialog.dismiss();
                            }
                        });


                    }
                } else {

                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning_eng));
                    } else {

                        Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
                    }
                }
                break;
            case R.id.postdetail_content_img:
                if (mstrPostType.equalsIgnoreCase("Video")) {
                    Intent video_intent = new Intent(mContext, YouTubeWebviewActivity.class);

                    if (mVideoId != null) {
                        video_intent.putExtra("video_id", mVideoId);
                    } else {
                        video_intent.putExtra("video_id", "YOAu82xu8VY");
                    }
                    video_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    video_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(video_intent);
                    break;
                }
                 /*else if (mstrPostType.equalsIgnoreCase("Audio")) {
                    if (!isPlaying) {

                        mMedia = MediaPlayer.create(this, R.raw.wai_wai_audio);

                        mMedia.start();

                        isPlaying = true;
                    } else {
                        //Utils.doToastEng(getApplicationContext(), "Is playing ");
                    }
                }*/
                /***********EPP functions **************/
                else if (mstrPostType.equalsIgnoreCase("Audio")) {
                    setVolumeControlStream(AudioManager.STREAM_ALARM);

                    if (iWomenPost.getPostUploadName().equalsIgnoreCase("Wai Wai")) {




                        //MediaPlayer mediaPlayer;
                        if (!isPlaying) {

                            mMedia = MediaPlayer.create(this, R.raw.wai_wai_audio);
                            mMedia.start();
                            if ( mMedia == null)
                            {
                                mMedia= MediaPlayer.create(PostDetailActivity.this, R.raw.wai_wai_audio);
                                mMedia.setVolume(1.0f, 1.0f);
                                mMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    public void onPrepared(MediaPlayer arg0) {
                                        Log.i("PLAYER", "Prepare completed");
                                        mMedia.start();
                                    }
                                });
                                return;
                            }
                            // Or just play
                            if (mMedia.isPlaying()) {
                                mMedia.pause();
                            } else {
                                mMedia.seekTo(0);
                                ;
                                mMedia.start();
                            }

                            isPlaying = true;
                        } else {
                            //Utils.doToastEng(getApplicationContext(), "Is playing ");
                        }
                    } else {

                        img_social_audio.performClick();
                    }


                }


                break;

            case R.id.detail_ly_listen_now:


                if (mstrPostType.equalsIgnoreCase("Video")) {
                    Intent video_intent = new Intent(mContext, YouTubeWebviewActivity.class);

                    if (mVideoId != null) {
                        video_intent.putExtra("video_id", mVideoId);
                    } else {
                        video_intent.putExtra("video_id", "YOAu82xu8VY");
                    }

                    video_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    video_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(video_intent);
                    break;
                } else if (mstrPostType.equalsIgnoreCase("Audio")) {

                    /***********EPP functions **************/
                    if (iWomenPost.getPostUploadName().equalsIgnoreCase("Wai Wai")) {
                        //MediaPlayer mediaPlayer;
                        if (!isPlaying) {

                            /*mMedia = MediaPlayer.create(this, R.raw.wai_wai_audio);
                            mMedia.start();*/
                            if ( mMedia == null)
                            {
                                mMedia= MediaPlayer.create(PostDetailActivity.this, R.raw.wai_wai_audio);
                                mMedia.setVolume(1.0f, 1.0f);
                                mMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    public void onPrepared(MediaPlayer arg0) {
                                        Log.i("PLAYER", "Prepare completed");
                                        mMedia.start();
                                    }
                                });
                                return;
                            }
                            // Or just play
                            if (mMedia.isPlaying()) {
                                mMedia.pause();
                            } else {
                                mMedia.seekTo(0);
                                ;
                                mMedia.start();
                            }

                            isPlaying = true;
                        } else {
                            Utils.doToastEng(getApplicationContext(), "Is playing ");
                        }
                    }

                }
                break;


            case R.id.detail_ly_download:
                break;

            case R.id.postdeail_video_icon:
                if (mstrPostType.equalsIgnoreCase("Video")) {
                    Intent video_intent = new Intent(mContext, YouTubeWebviewActivity.class);
                    if (mVideoId != null) {
                        video_intent.putExtra("video_id", mVideoId);
                    } else {
                        video_intent.putExtra("video_id", "YOAu82xu8VY");
                    }
                    video_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    video_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(video_intent);
                    break;
                }
                break;
            case R.id.social_share:
                shareTextUrl();
                break;
            case R.id.social_facebook:
                shareButton.performClick();
                break;
            case R.id.social_no_ear_share_txt:
                //Normal Share
                shareTextUrl();
                break;
            case R.id.social_no_ear_share_img:
                shareButton.performClick();
                break;
            case R.id.social_no_ear_viber_img:
                img_viber_share.performClick();
                break;

            case R.id.social_audio:
                if (mstrAudioFilePath.length() == 10 ) {
                    SKToastMessage.showMessage(PostDetailActivity.this, getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);
                }

                if(!hasPermission(RECORD_AUDIO) || !hasPermission(WRITE_STORAGE)|| !hasPermission(STORAGE_READ_PERMISSION)|| !hasPermission(PREPARE_AUDIO_PERMISSION)) {
                    //if no permission, request permission
                    String[] perms = {RECORD_AUDIO, WRITE_STORAGE, STORAGE_READ_PERMISSION, PREPARE_AUDIO_PERMISSION};
                    int permsRequestCode = 200;
                    requestPermissions(perms, permsRequestCode);
                    break;
                }

                if (Connection.isOnline(getApplicationContext())) {


                    //String url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3"; //Default
                    if (mstrAudioFilePath != null && mstrAudioFilePath.length() > 20) {


                                Log.e("<<PostDetail>>","<<isPlay>>" + "show audio visualizer dialog"+ mstrAudioFilePath);
                                DialogFragment visualizerFragment = AudioVisualizerFragment.newInstance(mstrAudioFilePath);
                                visualizerFragment.show(getSupportFragmentManager(), "AudioVisualizer");



                    }else{
                        //TODO Audio File Null case
                        // TODO Auto-generated method stub
                        SKToastMessage.showMessage(PostDetailActivity.this, getResources().getString(R.string.audio_not_availabe_msg), SKToastMessage.INFO);


                    }


                } else {
                    //SKConnectionDetector.getInstance(this).showErrorMessage();
                }
                break;

        }

    }

    private void updatePostCommentStatus(String postId, int like_count) {

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.COMMENT_COUNT, like_count);
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};
        getContentResolver().update(IwomenProviderData.PostProvider.CONTETN_URI, cv, selections, selectionargs);


    }

    private void updatePostShareStatus(String postId, int like_count) {

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.SHARE_COUNT, like_count);
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};
        getContentResolver().update(IwomenProviderData.PostProvider.CONTETN_URI, cv, selections, selectionargs);


    }

    private void updatePostLikeStatus(String postId, int like_count) {

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, like_count);
        cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "1");
        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};
        getContentResolver().update(IwomenProviderData.PostProvider.CONTETN_URI, cv, selections, selectionargs);

        //Log.e("Update Post like status", "====>" + postId);

    }

    //TODO this is update everytime for server reply
    private void updatePostDetailServerStatus() {

        if (Connection.isOnline(getApplicationContext())) {

            UserPostAPI.getInstance().getService().getIwomenPostDetailById(postId, new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try {
                        JSONObject each_object = new JSONObject(s);

                                /*if (each_object.isNull("comment_contents")) {
                                    comment = "null";
                                } else {
                                    comment = each_object.getString("comment_contents");
                                }*/

                        final ContentValues cv = new ContentValues();
                        /*if (!each_object.isNull("objectId")) {
                            cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, each_object.getString("objectId"));


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, "");


                        }*/
                        if (!each_object.isNull("title")) {

                            cv.put(TableAndColumnsName.PostUtil.POST_TITLE, each_object.getString("title"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_TITLE, "");

                        }

                        if (!each_object.isNull("content")) {

                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, each_object.getString("content"));

                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, "");

                        }
                        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, each_object.getInt("likes"));

                        if (!each_object.isNull("image")) {

                            JSONObject imgjsonObject = each_object.getJSONObject("image");
                            if (!imgjsonObject.isNull("url")) {
                                cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, imgjsonObject.getString("url"));
                            } else {
                                cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                            }


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                        }
                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, each_object.getString("contentType"));//

                        if (!each_object.isNull("userId")) {

                            JSONObject userjsonObject = each_object.getJSONObject("userId");
                            if (!userjsonObject.isNull("objectId")) {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, userjsonObject.getString("objectId"));
                            } else {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");

                            }


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");

                        }


                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, each_object.getString("postUploadName"));


                        /*if (!each_object.isNull("postUploadPersonImg")) {

                            JSONObject postimgjsonObject = each_object.getJSONObject("postUploadPersonImg");
                            if (!postimgjsonObject.isNull("url")) {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, postimgjsonObject.getString("url"));
                            } else {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                            }


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                        }*/
                        if (!each_object.isNull("postUploadPersonImg")) {

                            JSONObject postimgjsonObject = each_object.getJSONObject("postUploadPersonImg");
                            if (!postimgjsonObject.isNull("url")) {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, postimgjsonObject.getString("url"));
                            } else {
                                //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                if (!each_object.isNull("postUploadUserImgPath")) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                }

                            }


                        } else {
                            //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");


                            if (!each_object.isNull("postUploadUserImgPath")) {

                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                            } else {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                            }

                        }

                        if (!each_object.isNull("videoId")) {

                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, each_object.getString("videoId"));

                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                        }


                        if (!each_object.isNull("suggest_section")) {

                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, each_object.getString("suggest_section"));

                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                        }

                        if (!each_object.isNull("titleMm")) {

                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, each_object.getString("titleMm"));

                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, "");

                        }

                        if (!each_object.isNull("content_mm")) {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, each_object.getString("content_mm"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, "");

                        }

                        //TODO TableColumnUpdate 11
                        if (!each_object.isNull("post_author_role")) {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE, each_object.getString("post_author_role"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE, "");

                        }
                        if (!each_object.isNull("authorId")) {

                            JSONObject userjsonObject = each_object.getJSONObject("authorId");
                            if (!userjsonObject.isNull("objectId")) {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ID, userjsonObject.getString("objectId"));
                            } else {
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ID, "");

                            }


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ID, "");

                        }

                        if (!each_object.isNull("post_author_role_mm")) {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE_MM, each_object.getString("post_author_role_mm"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_AUTHOR_ROLE_MM, "");

                        }
                        if (!each_object.isNull("credit_link")) {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LINK_ENG, each_object.getString("credit_link"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LINK_ENG, "");

                        }
                        if (!each_object.isNull("credit_link_mm")) {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LINK_MM, each_object.getString("credit_link_mm"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LINK_MM, "");

                        }
                        if (!each_object.isNull("credit_logo_url")) {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LOGO_URL, each_object.getString("credit_logo_url"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_LOGO_URL, "");

                        }
                        if (!each_object.isNull("credit_name")) {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_NAME, each_object.getString("credit_name"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.CREDIT_NAME, "");

                        }


                        if (!each_object.isNull("comment_count")) {
                            cv.put(TableAndColumnsName.PostUtil.COMMENT_COUNT, each_object.getInt("comment_count"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.COMMENT_COUNT, 0);

                        }
                        if (!each_object.isNull("share_count")) {
                            cv.put(TableAndColumnsName.PostUtil.SHARE_COUNT, each_object.getInt("share_count"));
                        } else {
                            cv.put(TableAndColumnsName.PostUtil.SHARE_COUNT, 0);

                        }

                        //cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                        //TODO check isAllow status and can hide this post later
                        //cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
                        if (!each_object.isNull("postUploadedDate")) {

                            JSONObject postUploadedDate = each_object.getJSONObject("postUploadedDate");
                            if (!postUploadedDate.isNull("iso")) {
                                cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, postUploadedDate.getString("iso"));
                            } else {
                                cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, "");

                            }


                        } else {
                            cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, "");

                        }
                        //cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                        cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                        Log.e("Update Detail : ", "= = = = = = = : " + cv.toString());

                        //cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "1");
                        String selections = TableAndColumnsName.PostUtil.POST_OBJ_ID + "=?";
                        String[] selectionargs = {postId};
                        getContentResolver().update(IwomenProviderData.PostProvider.CONTETN_URI, cv, selections, selectionargs);


                        getCommentCount(postId);

                        //TODO  showing Local data , after update from server by Id
                        getLocalPostDetail(postId);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        } else {

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
            }
        }

        /*ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.UserPostUtil.POST_LIKES, like_count);
        //cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "1");
        String selections = TableAndColumnsName.UserPostUtil.POST_OBJ_ID + "=?";
        String[] selectionargs = {postId};
        getContentResolver().update(IwomenProviderData.UserPostProvider.CONTETN_URI, cv, selections, selectionargs);*/

        //Log.e("Update Post like status", "====>" + postId);

    }

    //TODO Comment Count API
    private void getCommentCount(String strpostId) {


        if (Connection.isOnline(getApplicationContext())) {

            UserPostAPI.getInstance().getService().getCommentCount(0, 1, "{\"postId\":{\"__type\":\"Pointer\",\"className\":\"Post\",\"objectId\":\"" + postId + "\"}}", new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {
                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        cmd_count = whole_body.getString("count");

                        txt_cmd_count.setText(cmd_count + "Comments");
                        txt_social_no_ear_comment.setText(cmd_count + "Comments");
                        //TODO update local db

                        int c_count = Integer.parseInt(cmd_count);
                        updatePostCommentStatus(postId, c_count);


                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("///Count Error//", "==>" + error.toString());

                }
            });

        } else {

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(getApplicationContext(), getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(getApplicationContext(), getResources().getString(R.string.open_internet_warning_mm));
            }
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


    //Share URL
    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, mPostTile.getText().toString());//Title Of The Post
        if (post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 12) + " ...";
        } else {
            share_data = post_content.getText().toString();
        }

        share.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);

        share.putExtra(Intent.EXTRA_HTML_TEXT, share_data);


        startActivity(Intent.createChooser(share, "iWomen App Share link!"));
    }

    //TODO FB SHARE

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {

        // TODO Auto-generated method stub
        if (mMedia != null && mMedia.isPlaying()) {//If music is playing already
            mMedia.stop();//Stop playing the music
        }
        super.onDestroy();
        //profileTracker.stopTracking();
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                //postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private void showError(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
        builder.setTitle(R.string.error_dialog_title).
                setMessage(messageId).
                setPositiveButton(R.string.ok, null);
        builder.show();
    }

    public void shareUsingNativeDialog() {

        ShareContent content = getLinkContent();

        // share the app
        if (shareDialog.canShow(content, ShareDialog.Mode.NATIVE)) {
            shareDialog.show(content, ShareDialog.Mode.NATIVE);
        } else {
            showError(R.string.native_share_error);
        }
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private ShareLinkContent getLinkContent() {

        if (post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 20) + " ...";
        } else {
            share_data = post_content.getText().toString();
        }


        if (share_img_url_data != null) {
            return new ShareLinkContent.Builder()

                    .setContentTitle(mPostTile.getText().toString())
                    .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                    .setImageUrl(Uri.parse(share_img_url_data))
                    .setContentDescription(share_data)
                    .build();
        } else {
            return new ShareLinkContent.Builder()

                    .setContentTitle(mPostTile.getText().toString())
                    .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                    .setContentDescription(share_data)
                    .build();
        }
    }

    //It is not work well in all device
    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();

        if (post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 12) + " ...";
        } else {
            share_data = post_content.getText().toString();
        }

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Lost And Found")
                .setContentDescription(share_data
                )
                .setContentUrl(Uri.parse(CommonConfig.SHARE_URL))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                LoginManager.getInstance().logInWithPublishPermissions(
                        this,
                        Arrays.asList(PERMISSION));
                return;
            }
        }

        if (allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }


}
