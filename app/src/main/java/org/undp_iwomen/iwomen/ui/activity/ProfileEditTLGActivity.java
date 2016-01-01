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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.adapter.CitySpinnerAdapter;
import com.parse.model.CityForShow;
import com.parse.retrofit_api.TlgProfileAPI;
import com.smk.iwomen.BaseActionBarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

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
    private Spinner spnFrom;
    String FromCityID;


    //After imagchose
    private ParseUser currentUser;
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
        spnFrom = (Spinner) findViewById(R.id.edit_profile_tlg_spn_township);

        spnFrom.setEnabled(false);
        mIamTLGCheckBox = (CheckBox)findViewById(R.id.edit_profile_tlg_checkbox);
        mIamTLGCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnFrom.setEnabled(true);
                    isTlgmember = true;
                } else {
                    spnFrom.setEnabled(false);
                    isTlgmember = false;
                }
            }
        });


        btn_cancel.setOnClickListener(clickListener);
        btn_edit.setOnClickListener(clickListener);
        //txt_edit_next.setOnClickListener(clickListener);



        strLang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);

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
            TlgProfileAPI.getInstance().getService().getTlgProfileList(new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try{
                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);


                            String _objectId;
                            String _tlg_group_name;
                            String _tlg_group_address;
                            String _tlg_group_address_mm;
                            String _tlg_group_lat_address;
                            String _tlg_group_lng_address;
                            if (each_object.isNull("objectId")) {
                                _objectId = "null";
                            } else {
                                _objectId = each_object.getString("objectId");
                            }

                            if (each_object.isNull("tlg_group_name")) {
                                _tlg_group_name = "null";
                            } else {
                                _tlg_group_name = each_object.getString("tlg_group_name");
                            }

                            if (each_object.isNull("tlg_group_address")) {
                                _tlg_group_address = "null";
                            } else {
                                _tlg_group_address = each_object.getString("tlg_group_address");
                            }
                            if (each_object.isNull("tlg_group_address_mm")) {
                                _tlg_group_address_mm = "null";
                            } else {
                                _tlg_group_address_mm = each_object.getString("tlg_group_address_mm");
                            }

                            if (each_object.isNull("tlg_group_lat_address")) {
                                _tlg_group_lat_address = "null";
                            } else {
                                _tlg_group_lat_address = each_object.getString("tlg_group_lat_address");
                            }

                            if (each_object.isNull("tlg_group_lng_address")) {
                                _tlg_group_lng_address = "null";
                            } else {
                                _tlg_group_lng_address = each_object.getString("tlg_group_lng_address");
                            }

                            cities.add( new CityForShow(_objectId, _tlg_group_address, _tlg_group_address_mm));
                        }
                        mProgressDialog.dismiss();

                        CitySpinnerAdapter adapter = new CitySpinnerAdapter(ProfileEditTLGActivity.this, cities);
                        spnFrom.setAdapter(adapter);

                        /*
                        ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, listName);
                        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnFrom.setAdapter(adapterFrom);
                        */
                        spnFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                FromCityID = cities.get(position).getId();
                                mFromCityName = cities.get(position).getNameInEnglish();

                                //TODO for query in local db
                                //Log.e("Search From", "==" + list_id.get(position));
                                //queryToCity(FromCityID);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressDialog.dismiss();
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

                    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                    userQuery.getInBackground(mstrUserId, new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                //parseUser.put("userImgPath", userprofile_Image_path);
                                parseUser.put("isTlgTownshipExit",isTlgmember);

                                if(isTlgmember){
                                    parseUser.put("tlg_city_address",mFromCityName);

                                }
                                parseUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            mProgressDialog.dismiss();


                                            startDrawerMainActivity();
                                        }
                                    }
                                });
                            }
                        }
                    });


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
