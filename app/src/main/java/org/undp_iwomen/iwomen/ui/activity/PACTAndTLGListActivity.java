package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.smk.sklistview.SKListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.TLGTownship;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.adapter.PACTAndTLGListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 20-Sep-16.
 */
public class PACTAndTLGListActivity extends BaseActionBarActivity {
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
    /*private ArrayList<TLGTownship> tlgArraylist;
    private List<TLGTownship> StoragetlgArraylist;*/
    private ArrayList<String> sampleArrList = new ArrayList<String>();

    PACTAndTLGListViewAdapter mAdapter;
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

            mProgressDialog = new ProgressDialog(PACTAndTLGListActivity.this);
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

            sampleArrList.add("PACT Group");
            sampleArrList.add("TLG");
            sampleArrList.add("Other");
            mAdapter = new PACTAndTLGListViewAdapter(getApplicationContext(), sampleArrList, mstr_lang);
            lv.setAdapter(mAdapter);

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
                textViewTitle.setText(R.string.betogether_title_eng);

            } else {
                //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
                textViewTitle.setText(R.string.betogether_title_mm);

            }


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(mContext.getApplicationContext(), TLGListActivity.class);
                    Log.e("PACT&TLG onClick>>", " "+i);
                    intent.putExtra("TitleEng", R.string.betogether_title_eng);
                    intent.putExtra("TitleMM", R.string.betogether_title_mm);
                    //intent.putExtra("post_id", feedItems.get(position).getPost_obj_id());
                    //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    mContext.startActivity(intent);

                }
            });




    }
}
