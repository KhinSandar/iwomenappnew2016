package org.undp_iwomen.iwomen.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.smk.clientapi.NetworkEngine;
import com.smk.model.User;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.utils.Connection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterTermsFragment8 extends Fragment implements View.OnClickListener {

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private ProgressDialog mProgressDialog;

    private Button btn_next;

    public static RegisterTermsFragment8 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterTermsFragment8 fragment = new RegisterTermsFragment8();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterTermsFragment8 newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterTermsFragment8 fragment = new RegisterTermsFragment8();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_8_terms, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button) view.findViewById(R.id.Next);

        btn_next.setOnClickListener(this);

        setEnglishFont();

        return view;
    }

    /*private void addNextFragment(Sample sample, ImageView squareBlue, boolean overlap) {
        SharedElementFragment2 sharedElementFragment2 = SharedElementFragment2.newInstance(sample);

        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        sharedElementFragment2.setEnterTransition(slideTransition);
        sharedElementFragment2.setAllowEnterTransitionOverlap(overlap);
        sharedElementFragment2.setAllowReturnTransitionOverlap(overlap);
        sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, sharedElementFragment2)
                .addToBackStack(null)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .commit();
    }*/

    private void addNextFragment(final Button squareBlue, final boolean overlap) {


        if (Connection.isOnline(mContext)) {
            String user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
            String phone = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);

            String email = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);
            String pwd = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);

            String address_tlg_township_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);

            //TODO city field
            //String address_sate_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);
            //TODO country
            //String address_country_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);


            String user_photo = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);

            String user_role = "User";
            //TODO remark group_id
            //String groud_id = "1";

            //TODO tlg user (istlgexit true and tlg address0
            String isTlgExit = "true";
            String tlgName = "Kanpetlet, Chin State";


            NetworkEngine.getInstance().postCreateUser(user_name, pwd, phone, user_photo, isTlgExit, tlgName, new Callback<User>() {
                        @Override
                        public void success(User user, Response response) {
                            mEditorUserInfo = mSharedPreferencesUserInfo.edit();

                            mEditorUserInfo.putString(CommonConfig.USER_ROLE, user.getRole());
                            mEditorUserInfo.putString(CommonConfig.USER_NAME, user.getUsername());
                            mEditorUserInfo.putString(CommonConfig.USER_PH, user.getPhone());

                            if(user.getEmail()!= null || user.getEmail() != ""){
                                mEditorUserInfo.putString(CommonConfig.USER_EMAIL, user.getEmail());
                            }



                            mEditorUserInfo.commit();

                            Register_Congrat_Fragment register_congrat_fragment = Register_Congrat_Fragment.newInstance();

                            Slide slideTransition = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                slideTransition = new Slide(Gravity.LEFT);
                                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

                            }

                            ChangeBounds changeBoundsTransition = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                changeBoundsTransition = new ChangeBounds();
                                changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

                            }

                            register_congrat_fragment.setEnterTransition(slideTransition);
                            register_congrat_fragment.setAllowEnterTransitionOverlap(overlap);
                            register_congrat_fragment.setAllowReturnTransitionOverlap(overlap);
                            register_congrat_fragment.setSharedElementEnterTransition(changeBoundsTransition);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, register_congrat_fragment)
                                    .addToBackStack(null)
                                    .addSharedElement(squareBlue, getString(R.string.register_next))
                                    .commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                                org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
                            } else {

                                org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
                            }
                        }
                    }
            );




        } else {

            if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Next:
                addNextFragment(btn_next, false);
                break;
        }

    }

    public void setEnglishFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_terms_title);
    }

    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_terms_title);
    }
}
