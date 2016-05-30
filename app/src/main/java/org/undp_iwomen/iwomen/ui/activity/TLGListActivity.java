package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.TLGTownship;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.ui.adapter.TLGListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TLGListActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;
    private SKListView lv;
    private Context mContext;

    private String[] ListName;
    private String[] ListAuthorName;
    private String[] ListDate;
    private String[] ListDataText;
    private int[] ListIcon;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private StorageUtil storageUtil;
    private ArrayList<TLGTownship> tlgArraylist;
    private List<TLGTownship> StoragetlgArraylist;

    TLGListViewAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    //private String mResourceId;

    private String mTitleEng, mTitleMM;
    private ZProgressHUD zPDialog;
    private boolean isFirstLoading = true;
    private int paginater = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_resource_list);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(TLGListActivity.this);
        mProgressDialog.setCancelable(false);
        mContext = getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        //mResourceId = i.getStringExtra("ResourceId");
        mTitleEng = i.getStringExtra("TitleEng");
        mTitleMM = i.getStringExtra("TitleMM");


        lv = (SKListView) findViewById(R.id.sub_resource_list);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        tlgArraylist = new ArrayList<TLGTownship>();
        mAdapter = new TLGListViewAdapter(getApplicationContext(), tlgArraylist, mstr_lang);
        lv.setAdapter(mAdapter);
        lv.setCallbacks(skCallbacks);
        lv.setNextPage(true);
        mAdapter.notifyDataSetChanged();

        StoragetlgArraylist = (ArrayList<TLGTownship>)storageUtil.ReadArrayListFromSD("TlgArrayList");
        if (Connection.isOnline(mContext)){
            // Showing local data while loading from internet
            if(StoragetlgArraylist != null && StoragetlgArraylist.size() > 0){
                tlgArraylist.addAll(StoragetlgArraylist);
                mAdapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(this);
                zPDialog.show();
            }
            getTLGListDataFromSever();
        }else{
            //SKConnectionDetector.getInstance(this).showErrorMessage();

            if(StoragetlgArraylist != null){
                tlgArraylist.clear();
                tlgArraylist.addAll(StoragetlgArraylist);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (mstr_lang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText(R.string.betogether_title_eng);

        } else {
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText(R.string.betogether_title_mm);

        }

        //SubResourceItems = (ArrayList<SubResourceItem>) storageUtil.ReadArrayListFromSD("SubResourceArrayList" + mResourceId);

        /*if (tlgArraylist.size() > 0) {
            mAdapter = new TLGListViewAdapter(getApplicationContext(), tlgArraylist, mstr_lang);
            lv.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {

            getTLGListDataFromSever();
        }*/




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(mContext.getApplicationContext(), TlgProfileActivity.class);////DetailActivity

                intent.putExtra("tlgObj", new Gson().toJson(adapterView.getAdapter().getItem(i)));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


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
                getTLGListDataFromSever();
            }
        }
    };

    private void getTLGListDataFromSever() {
        if (Connection.isOnline(mContext)) {
            isLoading = true;

            SMKserverAPI.getInstance().getService().getTLGTownshipByPagination(paginater, new Callback<List<TLGTownship>>() {
                @Override
                public void success(List<TLGTownship> tlgTownships, Response response) {

                    if (isFirstLoading) {
                        isFirstLoading = false;
                        tlgArraylist.clear();
                        if (zPDialog != null && zPDialog.isShowing())
                            zPDialog.dismissWithSuccess();
                    }
                    tlgArraylist.addAll(tlgTownships);
                    mAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //StoreUtil.getInstance().saveTo(storagelistname, SubResourceItems);
                    final ArrayList<TLGTownship> storagelist = new ArrayList<TLGTownship>();
                    storagelist.addAll(tlgArraylist);
                    storageUtil.SaveArrayListToSD("TlgArrayList", storagelist);

                    if (tlgArraylist.size() == 31) {
                        lv.setNextPage(true);
                        paginater++;
                    } else {
                        // If no more item
                        lv.setNextPage(false);
                    }
                    //Log.e("<<<SubResource List size>>>","==>"+SubResourceItems.size());
                    if (tlgArraylist.size() == 0) {
                        // If no more item
                        lv.setNextPage(false);
                        if (mstr_lang.equals(Utils.ENG_LANG)) {
                            Utils.doToastEng(mContext, getResources().getString(R.string.resource_coming_soon_eng));
                        } else {
                            Utils.doToastMM(mContext, getResources().getString(R.string.resource_coming_soon_mm));
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }else{
            List<TLGTownship> tlgTownships = (ArrayList<TLGTownship>) storageUtil.ReadArrayListFromSD("TlgArrayList");
            if(tlgTownships != null){
                tlgArraylist.clear();
                tlgArraylist.addAll(tlgTownships);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                getTLGListDataFromSever();
                return true;
            case android.R.id.home:
                finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
