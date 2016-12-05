package org.undp_iwomen.iwomen.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.TLGTownship;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.TlgProfileItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.TlgProfileAPI;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TlgProfileActivity extends BaseActionBarActivity implements View.OnClickListener {

    public CustomTextView textViewTitle;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;

    private CustomTextView txt_tlg_group_name;
    private CustomTextView txt_tlg_group_address;

    private CustomTextView txt_tlg_leader_name;
    private CustomTextView txt_tlg_leader_role_lbl;

    private CustomTextView txt_tlg_info_who_we_are_lbl;
    private CustomTextView txt_tlg_member_lbl;
    private CustomTextView txt_tlg_member_txt;

    private CustomTextView txt_tlg_srg_member_lbl;
    private CustomTextView txt_tlg_srg_member_txt;

    private CustomTextView txt_tlg_key_activity_lbl;
    private CustomTextView txt_tlg_key_activity_txt;

    private CustomTextView txt_tlg_key_skill_lbl;
    private CustomTextView txt_tlg_key_skill_txt;

    private CustomTextView txt_bod_name;

    private ImageView img_tlg_ph_no;
    private ImageView img_tlg_viber_no;
    private ImageView img_tlg_fb_link;
    private RoundedImageView leaderProfileImg;
    private ProgressBar profile_item_progressBar;
    private RoundedImageView tlgLogoImg;
    private ProgressBar logo_progressBar;
    private CustomTextView txtBody;


    String tlgName;
    String tlgAddress;
    String tlgId;
    private Context mContext;

    String tlgLeaderPhno, tlgLeaderViberNo;
    String tlgLeaderFbLink;
    private TLGTownship tlgTownship;


    private final String call_phone_READ_PERMISSION = "android.permission.CALL_PHONE";
    boolean call_phone_PermissionAccepted = false;


    //Try request code between 1 to 255
    private static final int INITIAL_REQUEST = 1;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 2;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST + 1;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    private static final int CALL_REQUEST = INITIAL_REQUEST + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tlg_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tlgTownship = new Gson().fromJson(bundle.getString("tlgObj"), TLGTownship.class);
        }
        tlgName = tlgTownship.getTlgGroupName();
        //tlgId = tlgTownship.getId();
        tlgAddress = tlgTownship.getTlgGroupAddress();
        mContext = getApplicationContext();
        Log.e("tlgId", "===>" + tlgId + "///" + tlgName);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if (strLang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            textViewTitle.setText(tlgName);
        } else {
            textViewTitle.setText(tlgName);
        }
        textViewTitle.setText(R.string.app_name);
        //textViewTitle.setTypeface();


        txt_tlg_group_name = (CustomTextView) findViewById(R.id.tlg_group_name);
        txt_tlg_group_address = (CustomTextView) findViewById(R.id.tlg_group_address);

        txt_tlg_leader_name = (CustomTextView) findViewById(R.id.tlg_leader_profile_username);
        txt_tlg_leader_role_lbl = (CustomTextView) findViewById(R.id.tlg_leader_lbl);

        txt_tlg_info_who_we_are_lbl = (CustomTextView) findViewById(R.id.tlg_who_are_u);
        txt_tlg_member_lbl = (CustomTextView) findViewById(R.id.tlg_member_txt_lbl);
        txt_tlg_member_txt = (CustomTextView) findViewById(R.id.tlg_member_txt);

        txt_tlg_srg_member_lbl = (CustomTextView) findViewById(R.id.tlg_srg_lbl);
        txt_tlg_srg_member_txt = (CustomTextView) findViewById(R.id.tlg_srg_txt);

        txt_tlg_key_activity_lbl = (CustomTextView) findViewById(R.id.tgl_key_activity_lbl);
        txt_tlg_key_activity_txt = (CustomTextView) findViewById(R.id.tlg_key_acitvity_txt);

        txt_tlg_key_skill_lbl = (CustomTextView) findViewById(R.id.tlg_key_kill_lbl);
        txt_tlg_key_skill_txt = (CustomTextView) findViewById(R.id.tlg_key_skill_txt);

        img_tlg_ph_no = (ImageView) findViewById(R.id.tlg_ph_img);
        img_tlg_viber_no = (ImageView) findViewById(R.id.tlg_viber_img);
        img_tlg_fb_link = (ImageView) findViewById(R.id.tlg_fb_img);

        leaderProfileImg = (RoundedImageView) findViewById(R.id.tlg_leader_profilePic_rounded);
        tlgLogoImg = (RoundedImageView) findViewById(R.id.tlg_group_logo_img);
        profile_item_progressBar = (ProgressBar) findViewById(R.id.tlg_leader_profile_progressBar_profile_item);
        logo_progressBar = (ProgressBar) findViewById(R.id.tlg_group_logo_progressBar_profile_item);

        txt_bod_name = (CustomTextView) findViewById(R.id.tlg_leader_profile_bod_username);
        txtBody = (CustomTextView) findViewById(R.id.tlg_about_mdk);


        img_tlg_ph_no.setOnClickListener(this);
        img_tlg_viber_no.setOnClickListener(this);
        img_tlg_viber_no.setOnClickListener(this);


        clearData();


        setTLgDetailItem(tlgTownship);
        //getTLGDetailByIdFromSever();


    }


    private void clearData() {
        txt_tlg_group_name.setText("");
        txt_tlg_group_address.setText("");
        txt_tlg_leader_name.setText("");
        txt_tlg_leader_role_lbl.setText("");
        txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

        txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);//TLG Member
        txt_tlg_member_txt.setText("");

        txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
        txt_tlg_srg_member_txt.setText("");

        txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
        txt_tlg_key_activity_txt.setText("..");

        txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
        txt_tlg_key_skill_txt.setText("...");

        tlgLogoImg.setImageResource(R.drawable.place_holder);
        leaderProfileImg.setImageResource(R.drawable.blank_profile);
    }

    public void setEnglishFont() {


        if (tlgName.equalsIgnoreCase("May Tot Ah Man")) {
            textViewTitle.setText(R.string.tlg_group_name_eng);

            txt_tlg_group_name.setText(R.string.tlg_group_name_eng);
            txt_tlg_group_address.setText(R.string.tlg_group_address_eng);
            txt_tlg_leader_name.setText(R.string.tlg_leader_name_eng);
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_eng);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);
            txt_tlg_member_txt.setText(R.string.tlg_member_txt_eng);

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
            txt_tlg_srg_member_txt.setText(R.string.tlg_srg_member_txt_eng);

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
            txt_tlg_key_activity_txt.setText(R.string.tlg_key_activity_txt_eng);

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
            txt_tlg_key_skill_txt.setText(R.string.tlg_key_skill_txt_eng);

            tlgLogoImg.setImageResource(R.drawable.maydoearrman);
            leaderProfileImg.setImageResource(R.drawable.tlg_leader);
        } else {

            textViewTitle.setText(tlgName);

            txt_tlg_group_name.setText(tlgName);


            txt_tlg_group_address.setText(tlgAddress);


            tlgLogoImg.setImageResource(R.drawable.place_holder);
            leaderProfileImg.setImageResource(R.drawable.blank_profile);

            txt_tlg_leader_name.setText("");
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_eng);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);
            txt_tlg_member_txt.setText("");

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
            txt_tlg_srg_member_txt.setText("....");

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
            txt_tlg_key_activity_txt.setText("...");

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
            txt_tlg_key_skill_txt.setText("....");

        }
        textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_group_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_group_address.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_leader_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_leader_role_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_info_who_we_are_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_srg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_srg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_activity_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_activity_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_skill_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
        txt_tlg_key_skill_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));


    }

    public void setMyanmarFont() {


        if (tlgName.equalsIgnoreCase("May Tot Ah Man")) {
            textViewTitle.setText(R.string.tlg_group_name_mm);

            txt_tlg_group_name.setText(R.string.tlg_group_name_mm);
            txt_tlg_group_address.setText(R.string.tlg_group_address_mm);
            txt_tlg_leader_name.setText(R.string.tlg_leader_name_mm);
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_mm);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_mm);
            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_mm);
            txt_tlg_member_txt.setText(R.string.tlg_member_txt_mm);
            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_mm);
            txt_tlg_srg_member_txt.setText(R.string.tlg_srg_member_txt_mm);
            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_mm);
            txt_tlg_key_activity_txt.setText(R.string.tlg_key_activity_txt_mm);
            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_mm);
            txt_tlg_key_skill_txt.setText(R.string.tlg_key_skill_txt_mm);
        } else {

            textViewTitle.setText(tlgName);

            txt_tlg_group_name.setText(tlgName);


            txt_tlg_group_address.setText(tlgAddress);

            tlgLogoImg.setImageResource(R.drawable.place_holder);
            leaderProfileImg.setImageResource(R.drawable.blank_profile);

            txt_tlg_leader_name.setText("");
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role_mm);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_mm);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_mm);
            txt_tlg_member_txt.setText("");

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_mm);
            txt_tlg_srg_member_txt.setText("....");

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_mm);
            txt_tlg_key_activity_txt.setText("...");

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_mm);
            txt_tlg_key_skill_txt.setText("....");

        }


        //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_group_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_group_address.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_leader_name.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_leader_role_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_info_who_we_are_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_srg_member_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_srg_member_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_activity_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_activity_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_skill_lbl.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        txt_tlg_key_skill_txt.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));


    }

    private void getTLGDetailByIdFromSever() {
        if (Connection.isOnline(mContext)) {
            TlgProfileAPI.getInstance().getService().getTlgProfileDetailById(tlgId, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {

                        JSONObject whole_object = new JSONObject(s);
                        TlgProfileItem item = new TlgProfileItem();
                        if (!whole_object.isNull("createdAt")) {

                            item.setCreated_at(whole_object.getString("createdAt"));

                        } else {

                            item.setCreated_at("");
                        }
                        //tlgLogoImg
                        if (!whole_object.isNull("tlg_group_logo")) {
                            JSONObject groupLogoObject = whole_object.getJSONObject("tlg_group_logo");
                            if (!groupLogoObject.isNull("url")) {
                                item.setTlgLogoImg(groupLogoObject.getString("url"));

                            } else {
                                item.setTlgLogoImg("");
                            }


                        } else {
                            item.setTlgLogoImg("");
                        }
                        //TlgLeaderImg
                        if (!whole_object.isNull("tlg_leader_img")) {
                            JSONObject groupLogoObject = whole_object.getJSONObject("tlg_leader_img");
                            if (!groupLogoObject.isNull("url")) {
                                item.setTlgLeaderImg(groupLogoObject.getString("url"));

                            } else {
                                item.setTlgLeaderImg("");
                            }


                        } else {
                            item.setTlgLeaderImg("");
                        }
                        //tlg_group_name
                        if (!whole_object.isNull("tlg_group_name")) {

                            item.set_tlg_group_name(whole_object.getString("tlg_group_name"));

                        } else {
                            item.set_tlg_group_name("");
                        }
                        //
                        if (!whole_object.isNull("tlg_group_name_mm")) {
                            item.set_tlg_group_name_mm(whole_object.getString("tlg_group_name_mm"));
                        } else {
                            item.set_tlg_group_name_mm("");

                        }
                        //tlg_group_address
                        if (!whole_object.isNull("tlg_group_address")) {
                            item.set_tlg_group_address(whole_object.getString("tlg_group_address"));


                        } else {
                            item.set_tlg_group_address("");
                        }
                        //
                        if (!whole_object.isNull("tlg_group_address_mm")) {
                            item.set_tlg_group_address_mm(whole_object.getString("tlg_group_address_mm"));

                        } else {
                            item.set_tlg_group_address_mm("");

                        }
                        //tlg_leader_name
                        if (!whole_object.isNull("tlg_leader_name")) {
                            item.setTlgLeaderName(whole_object.getString("tlg_leader_name"));


                        } else {
                            item.setTlgLeaderName("");
                        }
                        //
                        if (!whole_object.isNull("tlg_leader_name_mm")) {
                            item.setTlgLeaderNameMm(whole_object.getString("tlg_leader_name_mm"));


                        } else {
                            item.setTlgLeaderNameMm("");
                        }
                        //tlg_leader_ph
                        if (!whole_object.isNull("tlg_leader_ph")) {
                            item.setTlgLeaderPh(whole_object.getString("tlg_leader_ph"));


                        } else {
                            item.setTlgLeaderPh("");
                        }
                        //tlg_leader_fb_link
                        if (!whole_object.isNull("tlg_leader_fb_link")) {
                            item.setTlgLeaderFbLink(whole_object.getString("tlg_leader_fb_link"));


                        } else {
                            item.setTlgLeaderFbLink("");
                        }

                        //tlg_group_member_no
                        if (!whole_object.isNull("tlg_group_member_no")) {
                            item.setTlgGroupMemberNo(whole_object.getString("tlg_group_member_no"));


                        } else {
                            item.setTlgGroupMemberNo("");
                        }
                        //tlg_group_other_member_no
                        if (!whole_object.isNull("tlg_group_other_member_no")) {
                            item.setTlgGroupOtherMemberNo(whole_object.getString("tlg_group_other_member_no"));


                        } else {
                            item.setTlgGroupOtherMemberNo("");
                        }
                        //tlg_group_key_activity
                        if (!whole_object.isNull("tlg_group_key_activity")) {
                            item.setTlgGroupKeyActivity(whole_object.getString("tlg_group_key_activity"));


                        } else {
                            item.setTlgGroupKeyActivity("");
                        }
                        //
                        if (!whole_object.isNull("tlg_group_key_activity_mm")) {
                            item.setTlgGroupKeyActivityMm(whole_object.getString("tlg_group_key_activity_mm"));


                        } else {
                            item.setTlgGroupKeyActivityMm("");
                        }
                        //tlg_group_key_skills
                        if (!whole_object.isNull("tlg_group_key_skills")) {
                            item.setTlgGroupKeySkills(whole_object.getString("tlg_group_key_skills"));


                        } else {
                            item.setTlgGroupKeySkills("");
                        }
                        //tlg_group_key_skills_mm
                        if (!whole_object.isNull("tlg_group_key_skills_mm")) {
                            item.setTlgGroupKeySkillsMm(whole_object.getString("tlg_group_key_skills_mm"));


                        } else {
                            item.setTlgGroupKeySkillsMm("");
                        }

                        //setTLgDetailItem(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSON err", "==>" + e.toString());
                    }


                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else {

            if (strLang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    private void setTLgDetailItem(TLGTownship item) {
        if (strLang.equals(Utils.ENG_LANG)) {
            textViewTitle.setText(item.getTlgGroupName());
            txt_tlg_group_name.setText(item.getTlgGroupName());

            txt_tlg_group_address.setText(item.getTlgGroupAddress());


            txt_tlg_leader_name.setText(item.getTlgLeaderName());
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_eng);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_eng);
            txt_tlg_member_txt.setText(item.getTlgGroupMemberNo());

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_eng);
            txt_tlg_srg_member_txt.setText(item.getTlgGroupOtherMemberNo());

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_eng);
            txt_tlg_key_activity_txt.setText(item.getTlgGroupKeyActivity());

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_eng);
            txt_tlg_key_skill_txt.setText(item.getTlgGroupKeySkills());

        } else {

            textViewTitle.setText(item.getTlgGroupNameMm());

            txt_tlg_group_name.setText(item.getTlgGroupNameMm());


            txt_tlg_group_address.setText(item.getTlgGroupAddressMm());


            txt_tlg_leader_name.setText(item.getTlgLeaderNameMm());
            txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role);
            txt_tlg_info_who_we_are_lbl.setText(R.string.tlg_who_we_r_mm);

            txt_tlg_member_lbl.setText(R.string.tlg_member_lbl_mm);
            txt_tlg_member_txt.setText(item.getTlgGroupMemberNo());

            txt_tlg_srg_member_lbl.setText(R.string.tlg_srg_member_lbl_mm);
            txt_tlg_srg_member_txt.setText(item.getTlgGroupOtherMemberNo());

            txt_tlg_key_activity_lbl.setText(R.string.tlg_key_activity_lbl_mm);
            txt_tlg_key_activity_txt.setText(item.getTlgGroupKeyActivityMm());

            txt_tlg_key_skill_lbl.setText(R.string.tlg_key_skill_lbl_mm);
            txt_tlg_key_skill_txt.setText(item.getTlgGroupKeySkillsMm());
        }

        if (item.getIsLeader() != null) {
            if (item.getIsLeader()) {//item.getIsLeader()

                txt_bod_name.setText(R.string.tlg_mdk_leader_role);
            } else {
                txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role);
                txt_bod_name.setText(R.string.tlg_normal_leader_role);
            }

        } else {
            txt_bod_name.setText(R.string.tlg_normal_leader_role);
        }
        txt_tlg_leader_role_lbl.setText(R.string.tlg_leader_role);

        //Phone No
        tlgLeaderPhno = item.getTlgLeaderPh();
        tlgLeaderFbLink = item.getTlgLeaderFbLink();
        tlgLeaderViberNo = item.getTlgMemberPhNo().trim();//TLGMemberPhNo is use for Viber Number

        //TODO Picaso calling
        if (item.getTlgGroupLogo() != null && !item.getTlgGroupLogo().isEmpty()) {
            try {

                logo_progressBar.setVisibility(View.VISIBLE);

                Picasso.with(mContext)
                        .load(item.getTlgGroupLogo()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(tlgLogoImg, new ImageLoadedCallback(logo_progressBar) {
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

            tlgLogoImg.setImageResource(R.drawable.place_holder);
            logo_progressBar.setVisibility(View.GONE);
        }
        if (item.getTlgLeaderImg() != null && !item.getTlgLeaderImg().isEmpty()) {
            try {
                leaderProfileImg.setAdjustViewBounds(true);
                leaderProfileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                profile_item_progressBar.setVisibility(View.VISIBLE);


                Picasso.with(mContext)
                        .load(item.getTlgLeaderImg()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(leaderProfileImg, new ImageLoadedCallback(profile_item_progressBar) {
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

            leaderProfileImg.setImageResource(R.drawable.place_holder);
            profile_item_progressBar.setVisibility(View.GONE);
        }
        tlgLogoImg.setImageResource(R.drawable.place_holder);
        leaderProfileImg.setImageResource(R.drawable.blank_profile);
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
        if (id == android.R.id.home) {

            finish();
            return true;


        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tlg_ph_img:


                // TODO: Consider calling
                //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //Log.e("<<<tlgLeaderPhno above if >>>", "===>" + tlgLeaderPhno);
                if (!hasPermission(call_phone_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {call_phone_READ_PERMISSION};
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, CALL_REQUEST);
                    } else {
                        //Log.e("<<<tlgLeaderPhno if else >>>", "===>" + tlgLeaderPhno);

                        if (tlgLeaderPhno != null) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            String ph = tlgLeaderPhno;
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));

                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                //Log.e("<<<tlgLeaderPhno if Consider calling >>>", "===>" + tlgLeaderPhno);

                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                startActivity(callIntent);

                                //return;
                            }
                            startActivity(callIntent);
                            //return;

                        }
                    }


                } else {
                    // Check if we were successful in obtaining the map.
                    //Log.e("<<<tlgLeaderPhno else >>>", "===>" + tlgLeaderPhno);
                    if (tlgLeaderPhno != null) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String ph = tlgLeaderPhno;
                        callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                        startActivity(callIntent);
                        return;
                        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            String ph = tlgLeaderPhno;
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                            startActivity(callIntent);
                            return;
                        }
                        */
                    }
                }

            case R.id.tlg_viber_img:

                try {
                    String sphone;
                    Log.e("try tlgLeaderViberNog ","==>"+tlgLeaderViberNo);

                    if (tlgLeaderViberNo != null && tlgLeaderViberNo != "") {
                        sphone = tlgLeaderViberNo;
                    } else {
                        sphone = tlgLeaderPhno;
                    }
                    Log.e("try ","==>"+tlgLeaderPhno);

                    Uri uri = Uri.parse("tel:" + Uri.encode(sphone));
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //FLAG_ACTIVITY_NEW_TASK
                    intent.setData(uri);
                    //intent.putExtra(Intent.EXTRA_SUBJECT, post_content.getText().toString().substring(0, 12) + "...");//Title Of The Post
                    //intent.putExtra(Intent.EXTRA_TEXT, CommonConfig.SHARE_URL);
                    //intent.setType("text/plain");
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {

                    Log.e("Replye","==>"+ex.toString());
                }
                break;
            case R.id.tlg_fb_img:

                startActivity(getOpenFacebookIntent(getApplicationContext()));


                break;
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/testingmobileapp"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/1457723087870355"));
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case CALL_REQUEST:

                call_phone_PermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (call_phone_PermissionAccepted) {

                    //img_tlg_ph_no.performClick();
                    if (tlgLeaderPhno != null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            String ph = tlgLeaderPhno;
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                            startActivity(callIntent);
                            return;
                        }

                    }

                }

                break;

        }
    }


    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }
}
