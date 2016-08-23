package org.undp_iwomen.iwomen.ui.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.smk.sklistview.SKListView;
import com.squareup.picasso.Picasso;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.Rating;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.ResourceItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.retrofit_api.ResourceAPI;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.activity.SubResourceListActivity;
import org.undp_iwomen.iwomen.ui.adapter.ResourcesListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesFragment extends Fragment {
    public static final String ARG_MENU_INDEX = "index";
    private Context mContext;
    private SKListView lvResouces;
    private int paginater = 1;
    private ArrayList<com.smk.model.ResourceItem> ResourceItems;
    private ResourcesListViewAdapter adapter;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    public Rating avgRatings;
    private StorageUtil storageUtil;
    private int offsetlimit = 3;
    private int skipLimit = 0;
    private Menu menu;
    private ZProgressHUD zPDialog;
    List<com.smk.model.ResourceItem> StorageresourceItems;
    public CustomTextView sp_txtName, sp_title_txt;
    public RoundedImageView sp_imgIcon;
    public ProgressBar sp_progressBar;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;

    private String Resourcce_json;
    private com.smk.model.ResourceItem StorageResourcePostObj;


    public ResourcesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //storageUtil = StorageUtil.getInstance(getActivity().getApplicationContext());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resources, container, false);
        mContext = getActivity().getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);


        init(rootView);

        getReview();
        return rootView;
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
            if (!isLoading) {
                getResourceDataPaginationFromSever();
            }
        }
    };

    private void init(View rootView) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        ResourceItems = new ArrayList<>();
        lvResouces = (SKListView) rootView.findViewById(R.id.resource_lv);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        adapter = new ResourcesListViewAdapter(getActivity(), ResourceItems, mstr_lang);
        lvResouces.setAdapter(adapter);
        lvResouces.setCallbacks(skCallbacks);
        lvResouces.setNextPage(true);
        adapter.notifyDataSetChanged();


        //Header
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View header = layoutInflater.inflate(R.layout.special_content_resources_list_item_view, null);
        sp_txtName = (CustomTextView) header.findViewById(R.id.sp_resource_item_name);
        sp_title_txt = (CustomTextView) header.findViewById(R.id.sp_topic_title);
        sp_imgIcon = (RoundedImageView) header.findViewById(R.id.sp_resouce_list_item_img);
        sp_progressBar = (ProgressBar) header.findViewById(R.id.sp_resouce_list_item_progressBar);
        lvResouces.addHeaderView(header);

        getWeeklyResourcePost();

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SubResourceListActivity.class);

                Gson gson = new Gson();
                Resourcce_json = mSharedPreferencesUserInfo.getString(CommonConfig.RESOURCE_ID, null);
                StorageResourcePostObj = gson.fromJson(Resourcce_json, com.smk.model.ResourceItem.class);
                intent.putExtra("ResourceId", StorageResourcePostObj.getObjectId());
                intent.putExtra("TitleEng", StorageResourcePostObj.getResourceTitleEng());
                intent.putExtra("TitleMM", StorageResourcePostObj.getResourceTitleMm());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        //StorageresourceItems = StoreUtil.getInstance().selectFrom("ResourcesList");
        StorageresourceItems = (ArrayList<com.smk.model.ResourceItem>) storageUtil.ReadArrayListFromSD("ResourcesList");
        if (Connection.isOnline(mContext)) {
            // Showing local data while loading from internet
            if (StorageresourceItems != null && StorageresourceItems.size() > 0) {
                ResourceItems.addAll(StorageresourceItems);
                adapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(getActivity());
                zPDialog.show();
            }
            getResourceDataPaginationFromSever();
        } else {
            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            if (StorageresourceItems != null) {
                ResourceItems.clear();
                ResourceItems.addAll(StorageresourceItems);
                adapter.notifyDataSetChanged();
            }
        }

        //When very start this fragment open , need to check db data
        /*String selections = TableAndColumnsName.ResourceUtil.STATUS + "=?";
        String[] selectionargs = {"0"};
        Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC");//DESC
        if (cursor.getCount() > 0) {
            Log.e("cursor.getCount()", "===>" + cursor.getCount());
            setupAdapter();

        } else {
            if (Connection.isOnline(getActivity())) {
                //getPostDataOrderByLikesDate();
                getResourceDataFromSever();
            } else {
                SKConnectionDetector.getInstance(getActivity()).showErrorMessage();

            }
        }*/


        lvResouces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(mContext, SubResourceListActivity.class);
                intent.putExtra("ResourceId", ResourceItems.get(i - 1).getObjectId());  //Linn Edited
                intent.putExtra("TitleEng", ResourceItems.get(i - 1).getResourceTitleEng());
                intent.putExtra("TitleMM", ResourceItems.get(i - 1).getResourceTitleMm());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });


    }

    private void getWeeklyResourcePost() {
        if (Connection.isOnline(getApplicationContext())) {

            SMKserverAPI.getInstance().getService().getWeeklyContentResource(new Callback<com.smk.model.ResourceItem>() {
                @Override
                public void success(com.smk.model.ResourceItem item, Response response) {

                    SetWeeklySpecialResource(item);
                    mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                    // convert User object user to JSON format
                    Gson gson = new Gson();
                    Resourcce_json = gson.toJson(item);
                    // store in SharedPreferences
                    //String id =  "" +  item.getId(); // get storage key
                    mEditorUserInfo.putString(CommonConfig.RESOURCE_ID, Resourcce_json);
                    mEditorUserInfo.commit();


                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            // do the reverse operation
            Gson gson = new Gson();
            Resourcce_json = mSharedPreferencesUserInfo.getString(CommonConfig.RESOURCE_ID, null);
            StorageResourcePostObj = gson.fromJson(Resourcce_json, com.smk.model.ResourceItem.class);

            SetWeeklySpecialResource(StorageResourcePostObj);


        }
    }

    private void SetWeeklySpecialResource(com.smk.model.ResourceItem item) {
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            sp_txtName.setText(item.getResourceTitleEng());
            sp_title_txt.setText(getResources().getString(R.string.weekly_special_be_inspired));
        } else {//FOR ALL MM FONT
            sp_txtName.setText(item.getResourceTitleMm());
            sp_title_txt.setText(getResources().getString(R.string.weekly_special_be_inspired_mm));


        }

        sp_imgIcon.setAdjustViewBounds(true);
        sp_imgIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Connection.isOnline(mContext)) {

            if (item.getAuthorImgPath() != null && !item.getAuthorImgPath().isEmpty()) {

                try {

                    Picasso.with(mContext)
                            .load(item.getAuthorImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                            .placeholder(R.drawable.blank_profile)
                            .error(R.drawable.blank_profile)
                            .into(sp_imgIcon, new ImageLoadedCallback(sp_progressBar) {
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
                sp_progressBar.setVisibility(View.GONE);
            }
        } else {
            sp_progressBar.setVisibility(View.GONE);
        }

    }

    private void getResourceDataPaginationFromSever() {
        if (Connection.isOnline(mContext)) {

            isLoading = true;
            NetworkEngine.getInstance().getResourceByPagination(paginater, 1, new Callback<List<com.smk.model.ResourceItem>>() {
                @Override
                public void success(List<com.smk.model.ResourceItem> resourceItems, Response response) {

                    // Only first REQUEST that visible
                    if (zPDialog != null && zPDialog.isShowing()) {
                        ResourceItems.clear();
                        zPDialog.dismissWithSuccess();
                    }
                    ResourceItems.addAll(resourceItems);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    //StoreUtil.getInstance().saveTo("ResourcesList", ResourceItems);
                    final ArrayList<com.smk.model.ResourceItem> storagelist = new ArrayList<com.smk.model.ResourceItem>();
                    storagelist.addAll(ResourceItems);
                    storageUtil.SaveArrayListToSD("ResourcesList", storagelist);
                    if (ResourceItems.size() == 12) {
                        lvResouces.setNextPage(true);
                        paginater++;
                    } else {
                        // If no more item
                        lvResouces.setNextPage(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        } else {
            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            //List<com.smk.model.ResourceItem> iWomenPosts = StoreUtil.getInstance().selectFrom("ResourcesList");
            List<com.smk.model.ResourceItem> iWomenPosts = (ArrayList<com.smk.model.ResourceItem>) storageUtil.ReadArrayListFromSD("ResourcesList");

            if (iWomenPosts != null) {
                ResourceItems.clear();
                ResourceItems.addAll(iWomenPosts);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setupAdapter() {

        if (getActivity().getApplicationContext() != null) {


            //TODO ADMIN ACCOUNT POST FILTER
            String selections = TableAndColumnsName.ResourceUtil.STATUS + "=?";
            String[] selectionargs = {"0"};
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC");//DESC

            //Log.e("Set Up adapter cursor.getCount()", "===>" + cursor.getCount());
            String id;
            String resouceEng;
            String resouceMM;
            String iconPath;
            ResourceItems.clear();
            try {

                if (cursor != null && cursor.moveToFirst()) {
                    int i = 0;
                    do {
                        //Log.e("Set Up adapter cursor.moveToFirst()", "===>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID)));

                        id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID));

                        resouceEng = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG));
                        resouceMM = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM));
                        iconPath = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH));


                        ResourceItem rI = new ResourceItem(id, resouceEng, resouceMM, iconPath);
                        //ResourceItems.add(rI);
                        i++;
                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (ResourceItems.size() > 0) {
                    try {
                        //Log.e("Set Up adapter ResourceItems", "===>" + ResourceItems.size());

                        //storageUtil.SaveArrayListToSD("ResourceArrayList", ResourceItems);
                        //adapter = new ResourcesListViewAdapter(getActivity().getApplicationContext(), ResourceItems, mstr_lang);
                        lvResouces.setAdapter(adapter);


                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                } else {

                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        } else {
            Log.e("ResourceFragment", "Activity Null Case");
        }
    }

    private void getResourceDataFromSever() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

            if (cursorMain.getCount() > 0) {

                Log.e("Resource Row Count", "==>" + cursorMain.getCount());

                //skipLimit = skipLimit + 10; // OLd way
                skipLimit = cursorMain.getCount();// my way

                Log.e("Resource Offset  Count", "==>" + offsetlimit + "/" + skipLimit);
                String sCondition = "{\"isAllow\": true}";
                ResourceAPI.getInstance().getService().getResourceList("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");

                            String id;
                            String resouceEng;
                            String resouceMM;
                            String iconPath;
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);
                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {

                                    id = each_object.getString("objectId");


                                } else {
                                    id = "";
                                }

                                if (!each_object.isNull("resource_title_eng")) {

                                    resouceEng = each_object.getString("resource_title_eng");

                                } else {
                                    resouceEng = "";
                                }
                                if (!each_object.isNull("resource_title_mm")) {

                                    resouceMM = each_object.getString("resource_title_mm");

                                } else {
                                    resouceMM = "";
                                }

                                if (!each_object.isNull("resource_icon_img")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("resource_icon_img");
                                    if (!imgjsonObject.isNull("url")) {

                                        iconPath = imgjsonObject.getString("url");
                                    } else {
                                        iconPath = "";
                                    }


                                } else {
                                    iconPath = "";
                                }

                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID, id);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG, resouceEng);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM, resouceMM);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH, iconPath);


                                cv.put(TableAndColumnsName.ResourceUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.ResourceUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.ResourceUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                Log.e("saveResourceLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.ResourceProvider.CONTETN_URI, cv);

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
                ResourceAPI.getInstance().getService().getResourceList("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");

                            String id;
                            String resouceEng;
                            String resouceMM;
                            String iconPath;


                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);

                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {

                                    id = each_object.getString("objectId");

                                } else {
                                    id = "";
                                }

                                if (!each_object.isNull("resource_title_eng")) {

                                    resouceEng = each_object.getString("resource_title_eng");

                                } else {
                                    resouceEng = "";
                                }
                                if (!each_object.isNull("resource_title_mm")) {

                                    resouceMM = each_object.getString("resource_title_mm");

                                } else {
                                    resouceMM = "";
                                }

                                if (!each_object.isNull("resource_icon_img")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("resource_icon_img");
                                    if (!imgjsonObject.isNull("url")) {

                                        iconPath = imgjsonObject.getString("url");
                                    } else {
                                        iconPath = "";
                                    }


                                } else {
                                    iconPath = "";
                                }


                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID, id);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG, resouceEng);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM, resouceMM);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH, iconPath);


                                cv.put(TableAndColumnsName.ResourceUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.ResourceUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.ResourceUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                //Log.e("SaveResourceLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.ResourceProvider.CONTETN_URI, cv);

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
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {
                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.refresh_menu, menu);
        this.menu = menu;
        this.menu.findItem(R.id.action_rating).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                getResourceDataFromSever();
                return true;
            case R.id.action_rating:
                showReviewDetailDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReview() {
        NetworkEngine.getInstance().getReview("Be Knowledgeable", new Callback<Rating>() {


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


        txt_avg_title.setText(getResources().getString(R.string.str_overall_rating_be_knowledgeable));
        txt_total_rating.setText(avgRatings.getTotalRatings() + "");
        avg_ratings.setRating(avgRatings.getTotalRatings().floatValue());
        txt_rating_desc.setText(getRatingDesc(avgRatings.getTotalRatings()));
        txt_avg_ratings.setText(avgRatings.getTotalUsers() + " " + getResources().getString(R.string.str_total));

        final AlertDialog ad = alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public String getRatingDesc(Double ratings) {
        String ratingOfDesc = "";
        if (ratings > 0 && ratings <= 1.5) {
            ratingOfDesc = getResources().getString(R.string.str_poor);
        }
        if (ratings > 1.5 && ratings <= 2.5) {
            ratingOfDesc = getResources().getString(R.string.str_fair);
        }
        if (ratings > 2.5 && ratings <= 3.5) {
            ratingOfDesc = getResources().getString(R.string.str_good);
        }
        if (ratings > 3.5 && ratings <= 4.5) {
            ratingOfDesc = getString(R.string.str_very_good);
        }
        if (ratings > 4.5) {
            ratingOfDesc = getResources().getString(R.string.str_excellent);
        }
        return ratingOfDesc;
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

