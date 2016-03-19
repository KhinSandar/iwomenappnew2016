package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.smk.skconnectiondetector.SKConnectionDetector;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.IWomenPost;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.adapter.StoriesRecentListAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class IWomenPostSearchActivity extends BaseActionBarActivity {

    private Context mContext;
    private SKListView skListView;
    private List<IWomenPost> iWomenPostList;
    private StoriesRecentListAdapter stories;
    private int paginater = 1;
    private ZProgressHUD zPDialog;
    private SharedPreferences sharePrefLanguageUtil;
    private String mstr_lang;
    private CustomTextView textViewTitle;
<<<<<<< HEAD
    private String keywords;
=======
    private String mstrKeyWords;
>>>>>>> e73d298cfdfedcf1b133b7fa0c4b8617090b0aff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewTitle.setText(R.string.app_name);
        /*Intent i = getIntent();
        mstrKeyWords = i.getStringExtra("KeyWords");*/

        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

<<<<<<< HEAD
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            keywords = bundle.getString("keywords");
        }

=======
>>>>>>> e73d298cfdfedcf1b133b7fa0c4b8617090b0aff
        skListView = (SKListView) findViewById(R.id.lst_search_stories);
        zPDialog = new ZProgressHUD(IWomenPostSearchActivity.this);
        zPDialog.show();
        getIWomenPostBySearch();
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
                getIWomenPostBySearch();
            }
        }
    };

    //TODO SMK API
    private void getIWomenPostBySearch() {
        if (Connection.isOnline(mContext)) {

            isLoading = true;
            iWomenPostList = new ArrayList<>();
            NetworkEngine.getInstance().getIWomenPostBySearch(paginater, 1, keywords, new Callback<List<IWomenPost>>() {
                @Override
                public void success(List<IWomenPost> iWomenPosts, Response response) {
                    // Only first REQUEST that visible

                    if(zPDialog != null && zPDialog.isShowing()) {
                        zPDialog.dismissWithSuccess();
                    }

                    iWomenPostList.addAll(iWomenPosts);
                    stories = new StoriesRecentListAdapter(IWomenPostSearchActivity.this, iWomenPostList, mstr_lang);

                    skListView.setAdapter(stories);
                    skListView.setCallbacks(skCallbacks);
                    skListView.setNextPage(true);
                    stories.notifyDataSetChanged();;
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
                    if(zPDialog != null && zPDialog.isShowing())
                        zPDialog.dismissWithSuccess();
                }
            });

        } else {
            SKConnectionDetector.getInstance(IWomenPostSearchActivity.this).showErrorMessage();
            if(zPDialog != null && zPDialog.isShowing()) {
                zPDialog.dismissWithSuccess();
            }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
