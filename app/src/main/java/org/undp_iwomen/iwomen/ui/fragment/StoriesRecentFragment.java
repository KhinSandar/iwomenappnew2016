package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.skconnectiondetector.SKConnectionDetector;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.IWomenPost;
import org.smk.model.Rating;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.activity.NewPostActivity;
import org.undp_iwomen.iwomen.ui.activity.PostDetailActivity;
import org.undp_iwomen.iwomen.ui.adapter.IWomenPostListByDateRecyclerViewAdapter;
import org.undp_iwomen.iwomen.ui.adapter.StoriesRecentListAdapter;
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
public class StoriesRecentFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private SKListView skListView;
    //private PostListRecyclerViewAdapter mPostListRecyclerViewAdapter;
    private IWomenPostListByDateRecyclerViewAdapter mIWomonePostListAdapter;
    private List<IWomenPost> iWomenPostList;
    private List<FeedItem> feedItems;
    private FloatingActionButton fab;
    private SharedPreferences sharePrefLanguageUtil;
    private String mstr_lang;
    private int offsetlimit = 10;
    private int skipLimit = 0;
    private Menu menu;
    public static Rating avgRatings;
    private ZProgressHUD zPDialog;
    private LinearLayoutManager linearLayoutManager;
    private int paginater = 1;
    private StoriesRecentListAdapter stories;

    public StoriesRecentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories_list, container, false);
        mContext = getActivity().getApplicationContext();

        init(rootView);
        getReview();
        return rootView;
    }

    private void init(View rootView) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        feedItems = new ArrayList<FeedItem>();
        skListView = (SKListView) rootView.findViewById(R.id.lst_stories);

        iWomenPostList = new ArrayList<>();
        stories = new StoriesRecentListAdapter(getActivity(), iWomenPostList, mstr_lang);
        skListView.setAdapter(stories);
        skListView.setCallbacks(skCallbacks);
        skListView.setNextPage(true);
        final Activity parentActivity = getActivity();

        fab = (FloatingActionButton) rootView.findViewById(R.id.post_news);
        fab.setOnClickListener(this);

        //When very start this fragment open , need to check db data
        /*Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

        if (cursorMain.getCount() > 0) {
            setupAdapter();


        } else {

            if (Connection.isOnline(getActivity())) {
                //getPostDataOrderByLikesDate();
                getIWomenPostByLimit();
            } else {
                //scrollview.setVisibility(View.INVISIBLE);
                //connectionerrorview.setVisibility(View.VISIBLE);
                //            product_arrayList = (ArrayList<ProductsModel>) storageUtil.ReadArrayListFromSD("HomeProductsList");

                progress.setVisibility(View.INVISIBLE);
                    *//* Toast.makeText(getActivity().getApplicationContext(),
                    "Please Open Internet Connection!",
                    Toast.LENGTH_LONG).show();*//*
                if (mstr_lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, "Internet Connection need!");
                } else {

                    Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
                }


            }
        }*/

        /*
         * This is load data from local or server when connection is connected or not connected
         */
        //Load data from local storage for no connection
        final List<IWomenPost> iWomenPosts = StoreUtil.getInstance().selectFrom("stories_recent");
        if (Connection.isOnline(mContext)){
            // Showing local data while loading from internet
            if(iWomenPosts != null && iWomenPosts.size() > 0){
                iWomenPostList.addAll(iWomenPosts);
                stories.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(getActivity());
                zPDialog.show();
            }
            // Clear local data when connected to internet
            // iWomenPostList.clear();
            getIWomenPostByPagination();
        }else{
            SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            if(iWomenPosts != null){
                iWomenPostList.clear();
                iWomenPostList.addAll(iWomenPosts);
                stories.notifyDataSetChanged();
            }
        }


        //setupAdapter();


        skListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);

                    //intent.putExtra("post_id", feedItems.get(position).getPost_obj_id());
                    intent.putExtra("post_type", "iWomenPost");
                    intent.putExtra("postObj", new Gson().toJson(parent.getAdapter().getItem(position)));

                    //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (ArrayIndexOutOfBoundsException e){}

            }
        });

    }

    private void setupAdapter() {

        if (getActivity().getApplicationContext() != null) {


            //TODO ADMIN ACCOUNT POST FILTER
            /*String selections = TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID + "=?";
            String[] selectionargs = {"P8Q6mhIfOG"};*/
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


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


            String like_status = "";
            String status = "";
            String created_at = "";
            String updated_at = "";
            feedItems.clear();
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

                        feedItems.add(item);

                        i++;


                    } while (cursor.moveToNext());
                }

                cursor.close();

                //lost_data_list, lost_data_id_list, lost_data_obj_id_list ,lost_data_img_url_list
                //storageUtil.SaveArrayListToSD("lost_data_list", lost_data_list);


                if (mstr_lang.equals(Utils.ENG_LANG)) {
                    /*mPostListRecyclerViewAdapter = new PostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems, mstr_lang);
                    skListView.setAdapter(mPostListRecyclerViewAdapter);
                    mProgressDialog.dismiss();*
                } else {
                    /*mPostListRecyclerViewAdapter = new PostListRecyclerViewAdapter(getActivity().getApplicationContext(), feedItems, mstr_lang);
                    skListView.setAdapter(mPostListRecyclerViewAdapter);
                    mProgressDialog.dismiss();*/
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }


            //Utils.doToast(getActivity(), String.valueOf(feedItems.size()));
        } else {
            Log.e("LostListFragment", "Activity Null Case");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);

        this.menu = menu;

        if (StoriesRecentFragment.avgRatings != null) {
            this.menu.findItem(R.id.action_rating).setVisible(true);
            this.menu.findItem(R.id.action_rating).setIcon(BaseActionBarActivity.getRatingIcon(StoriesRecentFragment.avgRatings.getTotalRatings()));

        }

        final MenuItem item = menu.add(0, 12, 0, "Search");
        //menu.removeItem(12);
        item.setIcon(R.drawable.ic_action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                | MenuItem.SHOW_AS_ACTION_ALWAYS);


        final SearchView sv = new SearchView(getActivity());//.getActionBar().getThemedContext()

        //item.setActionView(sv);

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

                getIWomenPostByLimit();
                return true;
            case R.id.action_rating:
                showReviewDetailDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReview() {
        NetworkEngine.getInstance().getReview("Be Inspired", new Callback<Rating>() {


            @Override
            public void success(Rating arg0, Response response) {
                if (menu != null && arg0.getTotalRatings() > 0) {
                    menu.findItem(R.id.action_rating).setVisible(true);
                    menu.findItem(R.id.action_rating).setIcon(BaseActionBarActivity.getRatingIcon(arg0.getTotalRatings()));
                    avgRatings = arg0;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void showReviewDetailDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = View.inflate(getActivity(), R.layout.dialog_reviews_detial, null);
        TextView txt_avg_title = (TextView) convertView.findViewById(R.id.txt_avg_title);
        TextView txt_total_rating = (TextView) convertView.findViewById(R.id.txt_total_rating);
        RatingBar avg_ratings = (RatingBar) convertView.findViewById(R.id.avg_ratings);
        TextView txt_avg_ratings = (TextView) convertView.findViewById(R.id.txt_avg_ratings);
        TextView txt_rating_desc = (TextView) convertView.findViewById(R.id.txt_rating_desc);
        Button btn_ok = (Button) convertView.findViewById(R.id.btn_ok);
        alertDialog.setView(convertView);

        txt_avg_title.setText(getResources().getString(R.string.str_overall_rating_be_inspired));
        txt_total_rating.setText(avgRatings.getTotalRatings() + "");
        avg_ratings.setRating(avgRatings.getTotalRatings().floatValue());
        txt_rating_desc.setText(getRatingDesc(avgRatings.getTotalRatings()));
        txt_avg_ratings.setText(avgRatings.getTotalUsers() +" "+ getResources().getString(R.string.str_total));

        final AlertDialog ad = alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public String getRatingDesc(Double ratings){
        String ratingOfDesc = "";
        if(ratings >0 && ratings <= 1.5){
            ratingOfDesc = getResources().getString(R.string.str_poor);
        }
        if(ratings >1.5 && ratings <= 2.5){
            ratingOfDesc = getResources().getString(R.string.str_fair);
        }
        if(ratings >2.5 && ratings <= 3.5){
            ratingOfDesc = getResources().getString(R.string.str_good);
        }
        if(ratings >3.5 && ratings <= 4.5){
            ratingOfDesc = getString(R.string.str_very_good);
        }
        if(ratings >4.5) {
            ratingOfDesc = getResources().getString(R.string.str_excellent);
        }
        return ratingOfDesc;
    }


    private void SetUserData() {

        Utils.doToastEng(mContext, "User Save");

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.UserUtil.USER_OBJ_ID, "U001");
        cv.put(TableAndColumnsName.UserUtil.USER_ROLE, "2");
        cv.put(TableAndColumnsName.UserUtil.USER_NAME, "Khin");
        cv.put(TableAndColumnsName.UserUtil.USER_PH, "123456789");

        cv.put(TableAndColumnsName.UserUtil.STATUS, "0");
        cv.put(TableAndColumnsName.UserUtil.CREATED_DATE, "01-06-2015");
        cv.put(TableAndColumnsName.UserUtil.UPDATED_DATE, "01-06-2015");
        //Log.e("saveUserLocal : ", "= = = = = = = : " + cv.toString());

        getActivity().getContentResolver().insert(IwomenProviderData.UserProvider.CONTENT_URI, cv);

    }

    private void SetPostData() {

        Utils.doToastEng(mContext, "Post Save");

        ContentValues cv = new ContentValues();
        cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, "POO4");
        cv.put(TableAndColumnsName.PostUtil.POST_TITLE, "TITLE1");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, "POST CONTENT");
        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, "1");
        cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "http://i.imgur.com/ad7uSGY.png");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, "Letter");//

        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "U001");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, "Khin");
        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "http://www.windowsegis.com/wp-content/uploads/2013/08/100-superb-windows-phone-wallpapers-to-mark-you-niftier-01.jpg");

        cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
        cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");
        cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, "Sun Aug 02 18:07:00 GMT+06:30 2015");

        //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

        getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);

    }

    private void getIWomenPostByLimit() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            if (cursorMain.getCount() > 0) {


                Log.e("Row Count", "==>" + cursorMain.getCount());

                //skipLimit = skipLimit + 10;
                skipLimit = cursorMain.getCount();
                Log.e("Offset Range Count", "==>" + offsetlimit + "/" + skipLimit);
                String sCondition = "{\"isAllow\": true}";
                UserPostAPI.getInstance().getService().getIWomenPost("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
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
                                    cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, each_object.getString("objectId"));


                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, "");


                                }
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

                                //TODO
                                if (!each_object.isNull("likes")) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_LIKES, each_object.getInt("likes"));
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_LIKES, 0);

                                }


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


                                cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
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
                                //TODO TableColumnUpdate 2

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


                                //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                    }
                });


            } else {
                
                //Log.e("First Time Offset Range Count", "==>" + offsetlimit + "/" + skipLimit);//where={"isAllow": true}

                String sCondition = "{\"isAllow\": true}";

                UserPostAPI.getInstance().getService().getIWomenPost("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
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
                                    cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, each_object.getString("objectId"));


                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, "");


                                }
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
                                //TODO
                                if (!each_object.isNull("likes")) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_LIKES, each_object.getInt("likes"));
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_LIKES, 0);

                                }

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

                                cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.PostUtil.STATUS, "0");

                                //TODO TableColumnUpdate 3
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


                                //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }

                        
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                        
                    }
                });


            }

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, "Internet Connection need!");
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }


    private void getPostDataOrderByLikesDate() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");


            if (cursorMain.getCount() > 0) {


                cursorMain.close();


                Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.PostProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC LIMIT 1");


                if (cursor.getCount() > 0) {


                    Date date = null;
                    if (cursor != null && cursor.moveToFirst()) {

                        date = new Date(cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));
                    /*First Row Id﹕ ==>5
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/Date﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015
                    08-02 18:11:11.175  10088-10088/org.undp_iwomen.iwomen E/First Row Data﹕ ==>Sun Aug 02 18:07:00 GMT+06:30 2015*/
                        Log.e("First Row Id", "==>" + cursor.getString(cursor.getColumnIndex("_id")));
                        Log.e("Date", "==>" + date.toString());
                        Log.e("First Row Data", "==>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.PostUtil.CREATED_DATE)));

                        //TODO GET POST And Save in DB

                        /*ParseQuery<Post> query = Post.getQuery();


                        query.whereGreaterThan("postUploadedDate", date);
                        //query.whereLessThan("likes",)
                        query.whereEqualTo("isAllow", true);
                        query.setLimit(3);

                        query.orderByDescending("postUploadedDate"); //Latest date is first
                        //query.orderByDescending("likes");
                        query.findInBackground(new FindCallback<Post>() {
                            @Override
                            public void done(List<Post> postList, ParseException e) {
                                if (e == null) {

                                    for (Post post : postList) {
                                        Log.e("Second time Data", "==>" + post.getObjectId());
                                        final ContentValues cv = new ContentValues();
                                        cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, post.getObjectId());

                                        if (post.get("title") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_TITLE, post.getString("title"));
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_TITLE, "");

                                        }

                                        if (post.get("content") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, post.getString("content"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, "");

                                        }

                                        cv.put(TableAndColumnsName.PostUtil.POST_LIKES, post.getNumber("likes").intValue());


                                        if (post.get("image") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                                        }
                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                        if (post.get("image") != null) {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");
                                        }

                                        cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                        if (post.get("postUploadPersonImg") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                        }
                                        if (post.get("videoId") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                                        }
                                        if (post.get("suggest_section") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                        }
                                        if (post.get("titleMm") != null) {

                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, "");

                                        }
                                        if (post.get("content_mm") != null) {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                        } else {
                                            cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, "");

                                        }

                                        cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                        cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
                                        cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                        cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());

                                        //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());

                                        getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);

                                    }
                                    setupAdapter();

                                } else {
                                    Log.e("Post Get Err", "===>" + e.toString());

                                }
                            }
                        });*/


                    }
                }


            } else {

                //GET POST and save  AFTER INITIAL SAVE TIME
                /*ParseQuery<Post> query = Post.getQuery();
                //query.orderByDescending("createdAt"); //Latest date is first
                query.orderByAscending("postUploadedDate");//Very first row Date is first
                //query.orderByDescending("likes");//Most likes is first
                query.whereEqualTo("isAllow", true);
                query.setLimit(3);
                query.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> postList, ParseException e) {

                        if (e == null) {
                            Log.e("Post sizes", "==>" + postList.size() + "/" + postList.get(0).toString());

                            for (Post post : postList) {

                                final ContentValues cv = new ContentValues();
                                cv.put(TableAndColumnsName.PostUtil.POST_OBJ_ID, post.getObjectId());
                                if (post.get("title") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_TITLE, post.getString("title"));
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_TITLE, "");

                                }

                                if (post.get("content") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, post.getString("content"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT, "");

                                }
                                cv.put(TableAndColumnsName.PostUtil.POST_LIKES, post.getNumber("likes").intValue());

                                if (post.get("image") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, post.getParseFile("image").getUrl());
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_IMG_PATH, "");

                                }
                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TYPES, post.getString("contentType"));//

                                if (post.get("userId") != null) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, post.getParseObject("userId").getObjectId());
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_ID, "");
                                }

                                cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_NAME, post.getString("postUploadName"));

                                if (post.get("postUploadPersonImg") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, post.getParseFile("postUploadPersonImg").getUrl());

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_USER_IMG_PATH, "");

                                }


                                if (post.get("videoId") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, post.getString("videoId"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_VIDEO_ID, "");

                                }
                                if (post.get("suggest_section") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, post.getString("suggest_section"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_SUGGEST_TEXT, "");

                                }
                                if (post.get("titleMm") != null) {

                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, post.getString("titleMm"));

                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_TITLE_MM, "");

                                }
                                if (post.get("content_mm") != null) {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, post.getString("content_mm"));
                                } else {
                                    cv.put(TableAndColumnsName.PostUtil.POST_CONTENT_MM, "");

                                }

                                cv.put(TableAndColumnsName.PostUtil.LIKE_STATUS, "0");

                                cv.put(TableAndColumnsName.PostUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.PostUtil.CREATED_DATE, post.get("postUploadedDate").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.PostUtil.UPDATED_DATE, post.get("postUploadedDate").toString());


                                //Log.e("savePostLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.PostProvider.CONTETN_URI, cv);


                            }
                            setupAdapter();


                        } else {
                            Log.e("Post Get Err", "===>" + e.toString());
                        }
                    }
                });*/
            }

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, "Internet Connection need!");
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    //TODO SMK API
    private void getIWomenPostByPagination() {
        if (Connection.isOnline(mContext)) {
            isLoading = true;
            NetworkEngine.getInstance().getIWomenPostByDateByPagination(paginater,"Recent", 1, new Callback<List<IWomenPost>>() {
                @Override
                public void success(List<IWomenPost> iWomenPosts, Response response) {
                    if(zPDialog != null && zPDialog.isShowing()){
                        iWomenPostList.clear();
                        zPDialog.dismissWithSuccess();
                    }
                    iWomenPostList.addAll(iWomenPosts);
                    stories.notifyDataSetChanged();
                    StoreUtil.getInstance().saveTo("stories_recent", iWomenPostList);
                    isLoading = false;
                    if(iWomenPosts.size() == 12){
                        skListView.setNextPage(true);
                        paginater++;
                    }else{
                        // If no more item
                        skListView.setNextPage(false);
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        } else {
            SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            List<IWomenPost> iWomenPosts = StoreUtil.getInstance().selectFrom("stories_recent");
            if(iWomenPosts != null){
                iWomenPostList.clear();
                iWomenPostList.addAll(iWomenPosts);
                stories.notifyDataSetChanged();
            }
        }
    }

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
            if(!isLoading){
                getIWomenPostByPagination();
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_news:

                //Intent intent = new Intent(mContext, MainPhotoPostActivity.class);
                Intent intent = new Intent(mContext, NewPostActivity.class);

                intent.putExtra("PostType", "BeInspiredIwomenPost");
                startActivity(intent);
                //Utils.doToastEng(mContext, "Coming Soon!");

                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("iWomen", query);
        stories.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        /*if (s.equals("")) {
            //index = 1;
            setupAdapter();

        }*/
        Log.d("iWomen", s);
        if(s != null && s.length() > 0)
            stories.filter(s);
        else
            stories.notifyDataSetChanged();
        return false;
    }
}

