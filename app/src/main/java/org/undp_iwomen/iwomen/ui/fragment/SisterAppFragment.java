package org.undp_iwomen.iwomen.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.smk.model.SisterAppItem;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smk.clientapi.NetworkEngine;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.Helper;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.ui.activity.AboutUsWebActivity;
import org.undp_iwomen.iwomen.ui.adapter.SisterAppListAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 8/28/15.
 */
public class SisterAppFragment extends Fragment {

    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private Context mContext;

    private SKListView lv_sister;
    private ProgressWheel progress;
    private int paginater = 1;
    private ArrayList<SisterAppItem> sisterAppItemList;

    private SisterAppListAdapter sisterAppListAdapter;
    private TextView txt_gen_link;
    private TextView txt_undp_link;
    private StorageUtil storageUtil;
    private ZProgressHUD zPDialog;
    List<SisterAppItem> StoragesisterAppItems;

    //private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        /*mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_sister, container, false);
        init(view);
        return view;
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
                getSisterListPaginationFromSever();
            }
        }
    };

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        lv_sister = (SKListView) rootView.findViewById(R.id.sister_app_listview);
        progress = (ProgressWheel) rootView.findViewById(R.id.sister_progress_wheel);
        sisterAppItemList = new ArrayList<>();

        sisterAppListAdapter = new SisterAppListAdapter(getActivity(), sisterAppItemList);

        lv_sister.setAdapter(sisterAppListAdapter);
        lv_sister.setCallbacks(skCallbacks);
        lv_sister.setNextPage(true);
        sisterAppListAdapter.notifyDataSetChanged();


        //StoragesisterAppItems = StoreUtil.getInstance().selectFrom("SisterAppList");
        StoragesisterAppItems = (ArrayList<SisterAppItem>) storageUtil.ReadArrayListFromSD("SisterAppList");
        if (Connection.isOnline(mContext)){
            // Showing local data while loading from internet
            if(StoragesisterAppItems != null && StoragesisterAppItems.size() > 0){
                sisterAppItemList.addAll(StoragesisterAppItems);
                sisterAppListAdapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(getActivity());
                zPDialog.show();
            }
            getSisterListPaginationFromSever();
        }else{
            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            if(StoragesisterAppItems != null){
                sisterAppItemList.clear();
                sisterAppItemList.addAll(StoragesisterAppItems);
                sisterAppListAdapter.notifyDataSetChanged();
            }
        }


        txt_gen_link = (TextView) rootView.findViewById(R.id.sister_app_gen_txt);
        txt_undp_link = (TextView) rootView.findViewById(R.id.sister_app_undp_link_txt);


        txt_gen_link.setText("www.iwomenapp.org");
        Linkify.addLinks(txt_gen_link, Linkify.WEB_URLS);

        txt_undp_link.setText("www.undpmyanmar.org");
        Linkify.addLinks(txt_undp_link, Linkify.WEB_URLS);

        /*sisterAppItemList = (ArrayList<SisterAppItem>) storageUtil.ReadArrayListFromSD("SisterAppArrayList");
        Log.e("sisterAppItemList size", "===>" + sisterAppItemList.size());
        if (sisterAppItemList.size() > 0) {
            sisterAppListAdapter = new SisterAppListAdapter(mContext, sisterAppItemList);
            lv_sister.setAdapter(sisterAppListAdapter);
            View padding = new View(getActivity().getApplicationContext());
            padding.setMinimumHeight(20);
            lv_sister.addFooterView(padding);
            Helper.getListViewSize(lv_sister);
        } else {
            getSisterAppListFromServer();
        }*/
        lv_sister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    if (sisterAppItemList.get(i).getAppPackageName() != null && !sisterAppItemList.get(i).getAppPackageName().isEmpty()) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + sisterAppItemList.get(i).getAppPackageName())));
                    } else {

                        Intent intent2 = new Intent(getActivity().getApplicationContext(), AboutUsWebActivity.class);
                        intent2.putExtra("ActivityName", "PostDetailActivity");
                        intent2.putExtra("URL", sisterAppItemList.get(i).getAppLink());
                        startActivity(intent2);

                    }
                } catch (ActivityNotFoundException e) {

                }
            }
        });


    }

    private void getSisterListPaginationFromSever() {
        if (Connection.isOnline(mContext)) {
            progress.setVisibility(View.VISIBLE);
            isLoading = true;
            NetworkEngine.getInstance().getSisterAppByPagination(paginater, new Callback<List<SisterAppItem>>() {
                @Override
                public void success(List<com.smk.model.SisterAppItem> sisterAppItems, Response response) {
                    // Only first REQUEST that visible
                    if(zPDialog != null && zPDialog.isShowing()){
                        sisterAppItemList.clear();
                        zPDialog.dismissWithSuccess();
                    }
                    sisterAppItemList.addAll(sisterAppItems);
                    sisterAppListAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.INVISIBLE);
                    isLoading = false;
                    //StoreUtil.getInstance().saveTo("SisterAppList", sisterAppItemList);
                    final ArrayList<SisterAppItem> storagelist = new ArrayList<SisterAppItem>();
                    storagelist.addAll(sisterAppItemList);
                    storageUtil.SaveArrayListToSD("SisterAppList", storagelist);
                    if (sisterAppItems.size() == 12) {
                        lv_sister.setNextPage(true);
                        paginater++;

                    } else {
                        // If no more item
                        lv_sister.setNextPage(false);
                    }

                    if (sisterAppItems.size() > 0) {

                        View padding = new View(getActivity().getApplicationContext());
                        padding.setMinimumHeight(20);
                        lv_sister.addFooterView(padding);
                        Helper.getListViewSize(lv_sister);
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    progress.setVisibility(View.INVISIBLE);
                }
            });

        }else {
            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            List<SisterAppItem> storagelist = (ArrayList < SisterAppItem >) storageUtil.ReadArrayListFromSD("SisterAppList");
            if(storagelist != null){
                sisterAppItemList.clear();
                sisterAppItemList.addAll(storagelist);
                sisterAppListAdapter.notifyDataSetChanged();
            }
        }
    }


    private void getSisterAppListFromServer() {
        if (Connection.isOnline(mContext)) {
            String sCondition = "{\"isAllow\": true}";
            //mProgressDialog.setMessage("Loading...");
            //mProgressDialog.show();
            UserPostAPI.getInstance().getService().getSisterAppList("createdAt", sCondition, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {

                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");
                        String _object_id;
                        String _app_name;
                        String _app_package_name;
                        String _app_img;
                        String _app_down_link;


                        sisterAppItemList.clear();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);
                            final ContentValues cv = new ContentValues();
                            if (!each_object.isNull("objectId")) {

                                _object_id = each_object.getString("objectId");


                            } else {
                                _object_id = "";
                            }

                            if (!each_object.isNull("app_name")) {

                                _app_name = each_object.getString("app_name");

                            } else {
                                _app_name = "";
                            }
                            if (!each_object.isNull("app_package_name")) {

                                _app_package_name = each_object.getString("app_package_name");

                            } else {
                                _app_package_name = "";
                            }

                            if (!each_object.isNull("app_img")) {

                                JSONObject imgjsonObject = each_object.getJSONObject("app_img");
                                if (!imgjsonObject.isNull("url")) {

                                    _app_img = imgjsonObject.getString("url");
                                } else {
                                    _app_img = "";
                                }


                            } else {
                                _app_img = "";
                            }
                            if (!each_object.isNull("app_link")) {

                                _app_down_link = each_object.getString("app_link");

                            } else {
                                _app_down_link = "";
                            }


                            //TODO
                            //sisterAppItemList.add(new SisterAppItem(_object_id, _app_name, _app_package_name, _app_down_link, _app_img));

                        }


                        if (sisterAppItemList.size() == 0) {
                            if (mstr_lang.equals(Utils.ENG_LANG)) {
                                Utils.doToastEng(mContext, getResources().getString(R.string.resource_coming_soon_eng));
                            } else {

                                Utils.doToastMM(mContext, getResources().getString(R.string.resource_coming_soon_mm));
                            }
                        } else {
                            storageUtil.SaveArrayListToSD("SisterAppArrayList", sisterAppItemList);
                            sisterAppListAdapter = new SisterAppListAdapter(mContext, sisterAppItemList);
                            lv_sister.setAdapter(sisterAppListAdapter);
                            View padding = new View(getActivity().getApplicationContext());
                            padding.setMinimumHeight(20);
                            lv_sister.addFooterView(padding);
                            Helper.getListViewSize(lv_sister);
                        }
                        //mProgressDialog.dismiss();


                    } catch (JSONException e) {
                        //mProgressDialog.dismiss();

                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    //mProgressDialog.dismiss();
                }
            });

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.refresh_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {


            //getSisterAppListFromServer();
            getSisterListPaginationFromSever();
            return false;

        }

        if (id == android.R.id.home) {


            return false;

        }

        return super.onOptionsItemSelected(item);
    }
}
