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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.smk.clientapi.NetworkEngine;
import com.smk.iwomen.BaseActionBarActivity;
import com.smk.model.TLGTownship;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CityForShow;
import org.undp_iwomen.iwomen.ui.adapter.TLGTownshipSpinnerAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileEditTLGActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;


    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    private ProgressDialog mProgressDialog;

    String mstrUserId, mstrTitleMm, mstrContentEng;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;




    private CheckBox mIamTLGCheckBox;

    private static boolean isTlgmember= false;
    String mFromCityName;
    private Spinner spnTLG;
    String FromCityID;
    private String tlgCityID, tlgCityName;



    //After imagchose
    //private TextView txt_edit_next;
    private Button btn_edit;
    private Button btn_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_tlg);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(ProfileEditTLGActivity.this);
        mProgressDialog.setCancelable(false);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();

        mstrUserId = i.getStringExtra("UserId");




        //txt_edit_next = (TextView) findViewById(R.id.edit_profile_txt_edit_next);
        btn_edit = (Button) findViewById(R.id.edit_profile_tlg_btn_save);
        btn_cancel = (Button) findViewById(R.id.edit_profile_tlg_btn_cancel);
        spnTLG = (Spinner) findViewById(R.id.edit_profile_tlg_spn_township);

        spnTLG.setEnabled(false);
        mIamTLGCheckBox = (CheckBox)findViewById(R.id.edit_profile_tlg_checkbox);
        mIamTLGCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnTLG.setEnabled(true);
                    isTlgmember = true;
                } else {
                    spnTLG.setEnabled(false);
                    isTlgmember = false;
                }
            }
        });


        btn_cancel.setOnClickListener(clickListener);
        btn_edit.setOnClickListener(clickListener);
        //txt_edit_next.setOnClickListener(clickListener);



        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        getTLGList();

        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.edit_profile_eng);


        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.edit_profile_mm);


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

    private void startDrawerMainActivity() {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void getTLGList() {

        if (Connection.isOnline(mContext)) {
            final ArrayList<CityForShow> cities = new ArrayList<CityForShow>();
            mProgressDialog.show();
            NetworkEngine.getInstance().getTLGTownship(new Callback<List<TLGTownship>>() {
                @Override
                public void success(List<TLGTownship> tlgTownships, Response response) {

                    mProgressDialog.dismiss();

                    final ArrayList<TLGTownship> tlgTownshipArrayList = new ArrayList<TLGTownship>();
                    tlgTownshipArrayList.addAll(tlgTownships);

                    TLGTownshipSpinnerAdapter adapter = new TLGTownshipSpinnerAdapter(ProfileEditTLGActivity.this, tlgTownshipArrayList);
                    spnTLG.setAdapter(adapter);

                    spnTLG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tlgCityID = tlgTownshipArrayList.get(position).getId().toString();
                            tlgCityName = tlgTownshipArrayList.get(position).getTlgGroupAddress();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }else {
            mProgressDialog.dismiss();
            //Utils.doToast(mContext, "Internet Connection need!");

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }


    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (arg0 == btn_edit) {
                if (Connection.isOnline(mContext)) {

                    mProgressDialog.show();
                    //TODO Profile Update with TLG exit and township address




                } else { //TODO When user choose sticker case

                    //Utils.doToast(mContext, "Internet Connection need!");

                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
                    } else {

                        Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
                    }


                }


            }
            if (arg0 == btn_cancel) {

                startDrawerMainActivity();
            }
        }
    };


}
