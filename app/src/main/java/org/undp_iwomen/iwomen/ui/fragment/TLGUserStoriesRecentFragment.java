package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.smk.clientapi.NetworkEngine;
import com.smk.iwomen.BaseActionBarActivity;
import com.smk.model.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.parse.Post;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.activity.MainPhotoPostActivity;
import org.undp_iwomen.iwomen.ui.activity.TLGUserPostDetailActivity;
import org.undp_iwomen.iwomen.ui.adapter.TLGUserPostListRecyclerViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.RecyclerOnItemClickListener;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 7/29/15.
 */
public class TLGUserStoriesRecentFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TLGUserPostListRecyclerViewAdapter mPostListRecyclerViewAdapter;
    ProgressWheel progress;
    private List<FeedItem> feedItems;

    private ProgressDialog mProgressDialog;

    FloatingActionButton fab;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;

    private int offsetlimit = 10;
    private int skipLimit = 0;
    private Menu menu;
    private Rating avgRatings;

    public TLGUserStoriesRecentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories, container, false);
        mContext = getActivity().getApplicationContext();

        //int index = getArguments().getInt(ARG_MENU_INDEX);
        //SetUserData();
        //SetPostData();
        init(rootView);
        getReview();

        return rootView;
    }

    private void init(View rootView) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        feedItems = new ArrayList<FeedItem>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.activity_main_recyclerview);
        final Activity parentActivity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.setHasFixedSize(false);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //mRecyclerView.setLayoutManager(layoutManager);
        progress = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        fab = (FloatingActionButton) rootView.findViewById(R.id.post_news);
        fab.setOnClickListener(this);

        progress.setVisibility(View.VISIBLE);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        //When very start this fragment open , need to check db data
        Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.UserPostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

        if (cursorMain.getCount() > 0) {
            setupAdapter();


        } else {

            if (Connection.isOnline(getActivity())) {
                //getPostDataOrderByLikesDate();
                getTLGUserPostByLimit();
            } else {
                progress.setVisibility(View.INVISIBLE);

                if (mstr_lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, "Internet Connection need!");
                } else {

                    Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
                }


            }
        }


        //setupAdapter();


        mRecyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(getActivity(), new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, TLGUserPostDetailActivity.class);

                intent.putExtra("post_id", feedItems.get(position).getPost_obj_id());

                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }));

        //Avoid SwipeReresh Loading when it is not at the top item
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ?
                                0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(dx == 0 && topRowVerticalPosition >= 0);


            }
        });


        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupAdapter();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    private void setupAdapter() {

        if (getActivity().getApplicationContext() != null) {


            //TODO ADMIN ACCOUNT POST FILTER
            String selections = TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID + "!=?";
            String[] selectionargs = {"P8Q6mhIfOG"};
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.UserPostProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC");//DESC


            ArrayList<FeedItem> feedItemArrayList = new ArrayList<>();
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
            feedItems.clear();
            try {

                if (cursor != null && cursor.moveToFirst()) {
                    int i = 0;
                    do {

                        post_obj_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_OBJ_ID));
                        post_title = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_TITLE));
                        post_content = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT));
                        post_like = cursor.getInt(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_LIKES));
                        post_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_IMG_PATH));
                        post_content_type = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_TYPES));
                        post_content_user_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID));
                        post_content_user_name = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_NAME));
                        post_content_user_img_path = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH));

                        video_id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID));
                        post_content_suggest_text = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT));
                        post_content_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM));
                        post_content_title_mm = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM));


                        like_status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.LIKE_STATUS));
                        status = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.STATUS));
                        created_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.CREATED_DATE));
                        updated_at = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.UPDATED_DATE));


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

                        item.setPost_like_status(like_status);
                        item.setStatus(status);
                        item.setCreated_at(created_at);
                        item.setUpdated_at(updated_at);



                    /*String image = feedObj.isNull("image") ? null : feedObj
                            .getString("image");*/

                        feedItems.add(item);

                        i++;


                    } while (cursor.moveToNext());
                }

                cursor.close();


                mPostListRecyclerViewAdapter = new TLGUserPostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems, mstr_lang);
                mRecyclerView.setAdapter(mPostListRecyclerViewAdapter);
                mProgressDialog.dismiss();
                progress.setVisibility(View.INVISIBLE);



            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }


        } else {
            Log.e("TLGuserFragment", "Activity Null Case");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);

        this.menu = menu;
        this.menu.findItem(R.id.action_rating).setVisible(false);

        final MenuItem item = menu.add(0, 12, 0, "Search");
        //menu.removeItem(12);
        item.setIcon(R.drawable.ic_action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                | MenuItem.SHOW_AS_ACTION_ALWAYS);


        final SearchView sv = new SearchView(getActivity());//.getActionBar().getThemedContext()

        item.setActionView(sv);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        //int searchPlateId = sv.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        // Getting the 'search_plate' LinearLayout.
        View searchPlate = sv.findViewById(android.support.v7.appcompat.R.id.search_plate);
        // Setting background of 'search_plate' to earlier defined drawable.
        searchPlate.setBackgroundResource(R.drawable.searchviewbackground);


        sv.setSubmitButtonEnabled(false);
        sv.setOnQueryTextListener(this);
        /*EditText et_search = (EditText) sv.findViewById(R.id.search_src_text);
        et_search.setTextColor(Color.WHITE);*/

        /*ImageView clearBtn = (ImageView) sv.findViewById(R.id.search_close_btn);
        clearBtn.setImageResource(R.drawable.ic_action_cancel);*/

        ImageView close = (ImageView) sv.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        close.setImageResource(R.drawable.ic_action_cancel);
        close.setAlpha(192);

        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ImageView search_btn = (ImageView) sv.findViewById(R.id.search_button);
        search_btn.setBackgroundResource(R.drawable.ic_action_search);

        SpannableStringBuilder ssb = new SpannableStringBuilder("   ");
        ssb.append("Search");
        Drawable searchIcon = getActivity().getResources().getDrawable(R.drawable.ic_action_search);
        int textSize = (int) (searchAutoComplete.getTextSize() * 1.25);
        searchIcon.setBounds(0, 0, textSize, textSize);
        ssb.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchAutoComplete.setHint(ssb);

        /*sv.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_hint) + "</font>"));*/


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //index = 1;
                setupAdapter();
                sv.setIconified(true);
                sv.setQuery("", false);
            }
        });

        sv.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    item.collapseActionView();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                //Utils.doToast(getActivity(), "do refresh");
                //mProgressDialog.show();

                //SetPostData();
                //getPostDataOrderByLikesDate();
                getTLGUserPostByLimit();
                return true;
            case R.id.action_rating:
                showReviewDetailDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReview(){
        NetworkEngine.getInstance().getReview("Talk Together", new Callback<Rating>() {


            @Override
            public void success(Rating arg0, Response response) {
                menu.findItem(R.id.action_rating).setVisible(true);
                menu.findItem(R.id.action_rating).setIcon(BaseActionBarActivity.getRatingIcon(arg0.getTotalRatings()));
                avgRatings = arg0;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void showReviewDetailDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = View.inflate(getActivity(),R.layout.dialog_reviews_detial,null);
        TextView txt_avg_title = (TextView ) convertView.findViewById(R.id.txt_avg_title);
        TextView txt_total_rating = (TextView ) convertView.findViewById(R.id.txt_total_rating);
        RatingBar avg_ratings = (RatingBar) convertView.findViewById(R.id.avg_ratings);
        TextView txt_avg_ratings = (TextView) convertView.findViewById(R.id.txt_avg_ratings);
        TextView txt_rating_desc = (TextView) convertView.findViewById(R.id.txt_rating_desc);
        Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
        alertDialog.setView(convertView);

        txt_avg_title.setText("Talk Together");
        txt_total_rating.setText(avgRatings.getTotalRatings()+"");
        avg_ratings.setRating(avgRatings.getTotalRatings().floatValue());
        txt_rating_desc.setText(BaseActionBarActivity.getRatingDesc(avgRatings.getTotalRatings()));
        txt_avg_ratings.setText(avgRatings.getTotalUsers() + " Total");

        final AlertDialog ad = alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }


    private void getTLGUserPostByLimit() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.UserPostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            if (cursorMain.getCount() > 0) {


                Log.e("Row Count", "==>" + cursorMain.getCount());

                //skipLimit = skipLimit + 10; // OLd way
                skipLimit = cursorMain.getCount();// my way


                Log.e("Offset Range Count", "==>" + offsetlimit + "/" + skipLimit);
                mProgressDialog.show();//{"isAllow": true}
                String sCondition = "{\"isAllow\": true}";
                UserPostAPI.getInstance().getService().getUserPost("createdAt",offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.e("success", "==" + s);
                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);

                                /*if (each_object.isNull("comment_contents")) {
                                    comment = "null";
                                } else {
                                    comment = each_object.getString("comment_contents");
                                }*/

                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, each_object.getString("objectId"));


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, "");


                                }
                                if (!each_object.isNull("title")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, each_object.getString("title"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, "");

                                }

                                if (!each_object.isNull("content")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, each_object.getString("content"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_LIKES, each_object.getInt("likes"));

                                if (!each_object.isNull("image")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("image");
                                    if (!imgjsonObject.isNull("url")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, imgjsonObject.getString("url"));
                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                    }


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TYPES, each_object.getString("contentType"));//

                                if (!each_object.isNull("userId")) {

                                    JSONObject userjsonObject = each_object.getJSONObject("userId");
                                    if (!userjsonObject.isNull("objectId")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, userjsonObject.getString("objectId"));
                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");

                                    }


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");

                                }


                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_NAME, each_object.getString("postUploadName"));


                                if (!each_object.isNull("postUploadPersonImg")) {

                                    JSONObject postimgjsonObject = each_object.getJSONObject("postUploadPersonImg");
                                    if (!postimgjsonObject.isNull("url")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, postimgjsonObject.getString("url"));
                                    } else {
                                        //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        if (!each_object.isNull("postUploadUserImgPath")) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        }

                                    }


                                } else {
                                    //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");


                                    if (!each_object.isNull("postUploadUserImgPath")) {

                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                    }

                                }

                                if (!each_object.isNull("videoId")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, each_object.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, "");

                                }


                                if (!each_object.isNull("suggest_section")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, each_object.getString("suggest_section"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                }

                                if (!each_object.isNull("titleMm")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, each_object.getString("titleMm"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, "");

                                }

                                if (!each_object.isNull("content_mm")) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, each_object.getString("content_mm"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, "");

                                }

                                cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.UserPostUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.UserPostUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.UserPostUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                //Log.e("saveUserPostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.UserPostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                        mProgressDialog.dismiss();
                    }
                });


            } else {
                mProgressDialog.show();

                String sCondition = "{\"isAllow\": true}";

                UserPostAPI.getInstance().getService().getUserPost("createdAt",offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.e("success", "==" + s);


                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);

                                /*if (each_object.isNull("comment_contents")) {
                                    comment = "null";
                                } else {
                                    comment = each_object.getString("comment_contents");
                                }*/

                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, each_object.getString("objectId"));


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, "");


                                }
                                if (!each_object.isNull("title")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, each_object.getString("title"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, "");

                                }

                                if (!each_object.isNull("content")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, each_object.getString("content"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_LIKES, each_object.getInt("likes"));

                                if (!each_object.isNull("image")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("image");
                                    if (!imgjsonObject.isNull("url")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, imgjsonObject.getString("url"));
                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                    }


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TYPES, each_object.getString("contentType"));//

                                if (!each_object.isNull("userId")) {

                                    JSONObject userjsonObject = each_object.getJSONObject("userId");
                                    if (!userjsonObject.isNull("objectId")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, userjsonObject.getString("objectId"));
                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");

                                    }


                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");

                                }


                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_NAME, each_object.getString("postUploadName"));


                                if (!each_object.isNull("postUploadPersonImg")) {

                                    JSONObject postimgjsonObject = each_object.getJSONObject("postUploadPersonImg");
                                    if (!postimgjsonObject.isNull("url")) {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, postimgjsonObject.getString("url"));
                                    } else {
                                        //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        if (!each_object.isNull("postUploadUserImgPath")) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        }

                                    }


                                } else {
                                    //cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");


                                    if (!each_object.isNull("postUploadUserImgPath")) {

                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, each_object.getString("postUploadUserImgPath"));

                                    } else {
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                    }

                                }

                                if (!each_object.isNull("videoId")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, each_object.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, "");

                                }


                                if (!each_object.isNull("suggest_section")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, each_object.getString("suggest_section"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                }

                                if (!each_object.isNull("titleMm")) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, each_object.getString("titleMm"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, "");

                                }

                                if (!each_object.isNull("content_mm")) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, each_object.getString("content_mm"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, "");

                                }

                                cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.UserPostUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.UserPostUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.UserPostUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                //Log.e("saveUserPostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.UserPostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                        mProgressDialog.dismiss();
                    }
                });


            }

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext,getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    private void getPostDataOrderByLikesDate() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.UserPostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            if (cursorMain.getCount() > 0) {


                cursorMain.close();


                Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.UserPostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC LIMIT 1");


                if (cursor.getCount() > 0) {


                    Date date = null;
                    if (cursor != null && cursor.moveToFirst()) {

                        date = new Date(cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.CREATED_DATE)));
                    /*First Row Id﹕ ==>5
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/Date﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/First Row Data﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015*/
                        Log.e("First Row Id", "==>" + cursor.getString(cursor.getColumnIndex("_id")));
                        Log.e("Date", "==>" + date.toString());
                        Log.e("First Row Data", "==>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.UserPostUtil.CREATED_DATE)));


                        mProgressDialog.show();

                        ParseQuery<Post> query = Post.getQuery();


                        query.whereGreaterThan("postUploadedDate", date);
                        //query.whereLessThan("likes",)
                        query.whereEqualTo("isAllow", true);
                        query.setLimit(10);

                        query.orderByDescending("postUploadedDate"); //Latest date is first
                        //query.orderByDescending("likes");
                        query.findInBackground(new FindCallback<Post>() {
                            @Override
                            public void done(List<Post> postList, ParseException e) {
                                if (e == null) {

                                    for (Post post : postList) {
                                        Log.e("Second time Data", "==>" + post.getObjectId());
                                        final ContentValues cv = new ContentValues();
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, post.getObjectId());

                                        if (post.get("title") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, post.getString("title"));
                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, "");

                                        }

                                        if (post.get("content") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, post.getString("content"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, "");

                                        }

                                        cv.put(TableAndColumnsName.UserPostUtil.POST_LIKES, post.getNumber("likes").intValue());


                                        if (post.get("image") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                        }
                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//


                                        /*if (!post.isN("userId")) {

                                            JSONObject userjsonObject = post.getJSONObject("userId");
                                            if (!userjsonObject.isNull("objectId")) {
                                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, post.getString("objectId"));
                                            } else {
                                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");

                                            }


                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");

                                        }*/

                                        /*if (post.get("image") != null) {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");
                                        }*/

                                        cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                        if (post.get("postUploadPersonImg") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        }
                                        if (post.get("videoId") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, "");

                                        }
                                        if (post.get("suggest_section") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                        }
                                        if (post.get("titleMm") != null) {

                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, "");

                                        }
                                        if (post.get("content_mm") != null) {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                        } else {
                                            cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, "");

                                        }

                                        cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "0");

                                        cv.put(TableAndColumnsName.UserPostUtil.STATUS, "0");
                                        cv.put(TableAndColumnsName.UserPostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                        cv.put(TableAndColumnsName.UserPostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());

                                        //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

                                        getActivity().getContentResolver().insert(IwomenProviderData.UserPostProvider.CONTETN_URI, cv);

                                    }
                                    setupAdapter();

                                } else {
                                    Log.e("Post Get Err", "===>" + e.toString());

                                }
                            }
                        });


                    }
                }


            } else {
                mProgressDialog.show();


                ParseQuery<Post> query = Post.getQuery();
                //query.orderByDescending("createdAt"); //Latest date is first
                //query.orderByAscending("postUploadedDate");//Very first row Date is first
                //query.orderByDescending("postUploadedDate");


                //query.orderByDescending("createdAt");// Retrieve the most recent ones
                //query.orderByAscending("createdAt");// Retrieve the most recent ones
                query.whereLessThan("postUploadedDate", "Sun Jun 22 18:07:00 GMT+06:30 2014");
                //query.orderByDescending("likes");//Most likes is first
                query.whereEqualTo("isAllow", true);
                query.setLimit(10);
                query.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> postList, ParseException e) {

                        if (e == null) {
                            Log.e("Post sizes", "==>" + postList.size() + "/" + postList.get(0).toString());

                            for (Post post : postList) {

                                final ContentValues cv = new ContentValues();
                                cv.put(TableAndColumnsName.UserPostUtil.POST_OBJ_ID, post.getObjectId());
                                if (post.get("title") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, post.getString("title"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_TITLE, "");

                                }

                                if (post.get("content") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, post.getString("content"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_LIKES, post.getNumber("likes").intValue());

                                if (post.get("image") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_IMG_PATH, "");

                                }
                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                if (post.get("image") != null) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_ID, "");
                                }

                                cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                if (post.get("postUploadPersonImg") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                }

                                if (post.get("videoId") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, "");

                                }
                                if (post.get("videoId") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_VIDEO_ID, "");

                                }
                                if (post.get("suggest_section") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                }
                                if (post.get("titleMm") != null) {

                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_TITLE_MM, "");

                                }
                                if (post.get("content_mm") != null) {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                } else {
                                    cv.put(TableAndColumnsName.UserPostUtil.POST_CONTENT_MM, "");

                                }

                                cv.put(TableAndColumnsName.UserPostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.UserPostUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.UserPostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.UserPostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());


                                //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.UserPostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();


                        } else {
                            Log.e("Post Get Err", "===>" + e.toString());
                        }
                    }
                });
            }

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext,getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_news:

                Intent intent = new Intent(mContext, MainPhotoPostActivity.class);

                intent.putExtra("PostType", "TalkTogetherPost");
                startActivity(intent);
                //startActivity(new Intent(getActivity(), MainPhotoPostActivity.class));
                //Utils.doToastEng(mContext, "Coming Soon!");

                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        mPostListRecyclerViewAdapter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.equals("")) {
            //index = 1;
            setupAdapter();

        }
        return false;
    }
}

