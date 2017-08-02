package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.smk.skalertmessage.SKToastMessage;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.SubResourceDetailAudios;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.ui.adapter.DataAdapter;
import org.undp_iwomen.iwomen.ui.adapter.SubResourceDetailDataAdapter;
import org.undp_iwomen.iwomen.ui.fragment.AudioVisualizerFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mac on 7/20/17.
 */

public class SubResourceDetailAuidoListActivity extends BaseActionBarActivity{
    private SubResourceDetailAudios audioList = new SubResourceDetailAudios();

    private List<SubResourceDetailAudios> subResourceDetailAudioLists;
    private SubResourceDetailDataAdapter mAdapter;
    private String mstrPostID;
    private Context mContext;
    private SKListView lv_recyclerView;
    private SharedPreferences sharePrefLanguageUtil;
    private String mstr_lang;
    private List<SubResourceDetailAudios> storageSubResourceDetailList;
    private StorageUtil storageUtil;
    private ZProgressHUD zPDialog;
    private String storagelistname;

    private boolean isFirstLoading = true;
    private int paginater = 1;
    private CustomTextView textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_recycler);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mContext = getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);
        //For action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
    }

    private void initViews() {
        Intent i = getIntent();
        mstrPostID = i.getStringExtra("postId");

        Log.e("SubResource Post ID","return==>" + mstrPostID);


        mstr_lang = i.getStringExtra("language");
        lv_recyclerView = (SKListView) findViewById(R.id.audio_list);

        if (mstr_lang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText("Audio List");
        } else {//FOR Default and Custom
            textViewTitle.setText("အသံဖိုင္မ်ား");
        }
        subResourceDetailAudioLists = new ArrayList<>();

        mAdapter = new SubResourceDetailDataAdapter(mContext, subResourceDetailAudioLists, mstr_lang);
        lv_recyclerView.setAdapter(mAdapter);
        lv_recyclerView.setCallbacks(skCallbacks);
        lv_recyclerView.setNextPage(true);
        mAdapter.notifyDataSetChanged();


        storagelistname = "AudioList" + mstrPostID;
        //StoragesubResourceItems = StoreUtil.getInstance().selectFrom(storagelistname);
        storageSubResourceDetailList = (ArrayList<SubResourceDetailAudios>) storageUtil.ReadArrayListFromSD(storagelistname);
        if (Connection.isOnline(mContext)) {
            // Showing local data while loading from internet
            if (storageSubResourceDetailList != null && storageSubResourceDetailList.size() > 0) {
                subResourceDetailAudioLists.addAll(storageSubResourceDetailList);
                mAdapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(this);
                zPDialog.show();
            }
            getAudioListDataFromSever(mstrPostID);
        } else {
            //SKConnectionDetector.getInstance(this).showErrorMessage();

            if (storageSubResourceDetailList != null) {
                subResourceDetailAudioLists.clear();
                subResourceDetailAudioLists.addAll(storageSubResourceDetailList);
                mAdapter.notifyDataSetChanged();
            }
        }

        lv_recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "OnClick On Audio List", Toast.LENGTH_LONG).show();

                if (subResourceDetailAudioLists.size() >= 0) {
                    DialogFragment visualizerFragment = AudioVisualizerFragment.newInstance(subResourceDetailAudioLists.get(position).getLinksPath(), mstr_lang);
                    visualizerFragment.show(getSupportFragmentManager(), "AudioVisualizer");
                } else {
                    /*if (mstr_lang.equals(Utils.ENG_LANG)) {
                        SKToastMessage.showMessage(AudioListActivity.this, getResources().getString(R.string.audio_not_availabe_msg), SKToastMessage.INFO);

                    } else {
                        SKToastMessage.showMessage(AudioListActivity.this, getResources().getString(R.string.audio_not_availabe_msg_mm), SKToastMessage.INFO);

                    }*/
                }
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
            if (!isLoading) {
                getAudioListDataFromSever(mstrPostID);
            }
        }
    };

    private void getAudioListDataFromSever(final String id) {
        if (Connection.isOnline(mContext)) {
            isLoading = true;
            Log.e("PostID: Server=> ",mstrPostID);
            SMKserverAPI.getInstance().getService().getSubResourceAudioListByPagination(mstrPostID, new Callback<List<SubResourceDetailAudios>>() {
                @Override
                public void success(List<SubResourceDetailAudios> subResourceDetailAudios, Response response) {
                    if (isFirstLoading) {
                        isFirstLoading = false;

                        Log.e("AudioPost","return==>" + subResourceDetailAudios.toString());
                        if(subResourceDetailAudios == null || subResourceDetailAudios.toString() == "[]"){
                            if (mstr_lang.equals(Utils.ENG_LANG)) {
                                SKToastMessage.showMessage(SubResourceDetailAuidoListActivity.this, getResources().getString(R.string.audio_not_availabe_msg), SKToastMessage.INFO);

                            } else {
                                SKToastMessage.showMessage(SubResourceDetailAuidoListActivity.this, getResources().getString(R.string.audio_not_availabe_msg_mm), SKToastMessage.INFO);

                            }
                            return;
                        }
                        subResourceDetailAudioLists.clear();
                        if (zPDialog != null && zPDialog.isShowing())
                            zPDialog.dismissWithSuccess();

                        subResourceDetailAudioLists.addAll(subResourceDetailAudios);
                        mAdapter.notifyDataSetChanged();
                        final ArrayList<SubResourceDetailAudios> storagelist = new ArrayList<SubResourceDetailAudios>();
                        storagelist.addAll(subResourceDetailAudios);
                        storageUtil.SaveArrayListToSD("AudioList" + mstrPostID, storagelist);
                        isLoading = false;
                        if (subResourceDetailAudios.size() == 12) {
                            lv_recyclerView.setNextPage(true);
                            paginater++;
                        } else {
                            // If no more item
                            lv_recyclerView.setNextPage(false);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (zPDialog != null && zPDialog.isShowing())
                        zPDialog.dismissWithSuccess();
                }
            });

        } else {
            //No coonection , not reach here
            //SKConnectionDetector.getInstance(this).showErrorMessage();
            /*List<AudioListActivity> subResourceItems1 = (ArrayList<org.smk.model.SubResourceItem>) storageUtil.ReadArrayListFromSD(storagelistname);
            if(subResourceItems1 != null){
                SubResourceItems.clear();
                SubResourceItems.addAll(subResourceItems1);
                mAdapter.notifyDataSetChanged();
            }*/
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

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;


        }


        return super.onOptionsItemSelected(item);
    }
}
