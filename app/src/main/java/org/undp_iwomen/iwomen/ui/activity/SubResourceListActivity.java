package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.SubResourceItem;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.ui.adapter.SubResourceListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SubResourceListActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;
    private SKListView lv;
    private int paginater = 1;
    private ArrayList<org.smk.model.SubResourceItem> SubResourceItems;
    private Context mContext;

    private String[] ListName;
    private String[] ListAuthorName;
    private String[] ListDate;
    private String[] ListDataText;
    private int[] ListIcon;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private StorageUtil storageUtil;
    private ZProgressHUD zPDialog;

    SubResourceListViewAdapter mAdapter;
    //private ProgressDialog mProgressDialog;
    private String mResourceId;

    private String mTitleEng, mTitleMM;

    String storagelistname ;
    List<org.smk.model.SubResourceItem> StoragesubResourceItems;

    private boolean isFirstLoading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_resource_list);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);


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
        mResourceId = i.getStringExtra("ResourceId");
        mTitleEng = i.getStringExtra("TitleEng");
        mTitleMM = i.getStringExtra("TitleMM");

        //Log.e("<<<Resesource ID>>","==>"+ mResourceId);

        //mResourceId= "SRBlN1Kow5";

        storagelistname = "SubResourcesList"+mResourceId;
        lv = (SKListView) findViewById(R.id.sub_resource_list);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);


        SubResourceItems = new ArrayList<>();
        mAdapter = new SubResourceListViewAdapter(mContext, SubResourceItems, mstr_lang);
        lv.setAdapter(mAdapter);
        lv.setCallbacks(skCallbacks);
        lv.setNextPage(true);
        mAdapter.notifyDataSetChanged();



        //StoragesubResourceItems = StoreUtil.getInstance().selectFrom(storagelistname);
        StoragesubResourceItems = (ArrayList<org.smk.model.SubResourceItem>) storageUtil.ReadArrayListFromSD(storagelistname);
        if (Connection.isOnline(mContext)){
            // Showing local data while loading from internet
            if(StoragesubResourceItems != null && StoragesubResourceItems.size() > 0){
                SubResourceItems.addAll(StoragesubResourceItems);
                mAdapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(this);
                zPDialog.show();
            }
            getSubResourceDataFromSever(mResourceId);
        }else{
            //SKConnectionDetector.getInstance(this).showErrorMessage();

            if(StoragesubResourceItems != null){
                SubResourceItems.clear();
                SubResourceItems.addAll(StoragesubResourceItems);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (mstr_lang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText(mTitleEng);
        } else {//FOR Default and Custom
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText(mTitleMM);
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ResourceDetailActivity.class);
                intent.putExtra("TitleEng",mTitleEng);//SubResourceItems.get(i).getSub_resource_title_mm()
                intent.putExtra("TitleMM", mTitleMM);//mCatNames.get((Integer)view.getTag()).toString()


                intent.putExtra("SubResourceDetailTitleEng",SubResourceItems.get(i).getSubResourceTitleEng());//SubResourceItems.get(i).getSub_resource_title_mm()
                intent.putExtra("SubResourceDetailTitleMM", SubResourceItems.get(i).getSubResourceTitleMm());

                intent.putExtra("ContentEng", SubResourceItems.get(i).getSubResouceContentEng());
                intent.putExtra("ContentMM", SubResourceItems.get(i).getSubResouceContentMm());
                intent.putExtra("AuthorName", SubResourceItems.get(i).getAuthorName());
                intent.putExtra("AuthorTitleEng", SubResourceItems.get(i).getAuthor().getAuthorTitleEng());
                intent.putExtra("AuthorTitleMM", SubResourceItems.get(i).getAuthor().getAuthorTitleMM());


                intent.putExtra("AuthorId", SubResourceItems.get(i).getAuthorId());
                intent.putExtra("AuthorImgPath", SubResourceItems.get(i).getAuthorImgUrl());
                intent.putExtra("PostDate", SubResourceItems.get(i).getPostedDate());
                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("likeCount",SubResourceItems.get(i).getLikes());

                intent.putExtra("post_type", "subResourcePost");
                intent.putExtra("postObj", new Gson().toJson(adapterView.getAdapter().getItem(i)));
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
                getSubResourceDataFromSever(mResourceId);
            }
        }
    };
    private void getSubResourceDataFromSever(final String id) {
        if (Connection.isOnline(mContext)) {
            isLoading = true;

            SMKserverAPI.getInstance().getService().getSubResourceByResourceIDByPagination(paginater, id,1, new Callback<List<SubResourceItem>>() {
                @Override
                public void success(List<SubResourceItem> subResourceItems, Response response) {
                    // Only first REQUEST that visible

                    if(isFirstLoading){
                        isFirstLoading = false;
                        SubResourceItems.clear();
                        if(zPDialog != null && zPDialog.isShowing())
                            zPDialog.dismissWithSuccess();
                    }
                    /* Old code ( it leading wrong when dismiss dialog and infite loading list
                    if(zPDialog != null && zPDialog.isShowing()){
                        SubResourceItems.clear();
                        zPDialog.dismissWithSuccess();
                    }*/
                  /* for(int i=0; i < subResourceItems.size(); i++){
                        Log.e("SubResource---->>>"," "+subResourceItems.get(i).toString());
                    }*/


                    SubResourceItems.addAll(subResourceItems);
                    mAdapter.notifyDataSetChanged();
                    isLoading = false;
                    //StoreUtil.getInstance().saveTo(storagelistname, SubResourceItems);
                    final ArrayList<SubResourceItem> storagelist = new ArrayList<SubResourceItem>();
                    storagelist.addAll(SubResourceItems);
                    storageUtil.SaveArrayListToSD(storagelistname, storagelist);

                    if(SubResourceItems.size() == 12){
                        lv.setNextPage(true);
                        paginater++;
                    }else{
                        // If no more item
                        lv.setNextPage(false);
                    }
                    //Log.e("<<<SubResource List size>>>","==>"+SubResourceItems.size());
                    if( SubResourceItems.size() == 0){
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
                    zPDialog.dismissWithSuccess();
                }
            });

        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();
            List<org.smk.model.SubResourceItem> subResourceItems1 = (ArrayList<org.smk.model.SubResourceItem>) storageUtil.ReadArrayListFromSD(storagelistname);
            if(subResourceItems1 != null){
                SubResourceItems.clear();
                SubResourceItems.addAll(subResourceItems1);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getSubResourceDataFromSever(mResourceId);
            return true;
        }
        if (id == android.R.id.home) {

            //Log.e("Home click on Resouce Lit", "==" + id);


            finish();
            return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
