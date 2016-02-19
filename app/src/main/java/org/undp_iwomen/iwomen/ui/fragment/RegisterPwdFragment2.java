package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.utils.Utils;

public class RegisterPwdFragment2 extends Fragment implements View.OnClickListener{
    private static final String EXTRA_SAMPLE = "sample";
    private Button btn_next;

    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;

    private EditText passwordField;
    private EditText confirmPasswordField;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;
    private int minPasswordLength;
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;


    public static RegisterPwdFragment2 newInstance(Sample sample) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPwdFragment2 fragment = new RegisterPwdFragment2();
        fragment.setArguments(args);
        return fragment;
    }
    public static RegisterPwdFragment2 newInstance( ) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPwdFragment2 fragment = new RegisterPwdFragment2();
        //fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_2_pwd, container, false);


        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        //String user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        //String  phone = mSharedPreferencesUserInfo.getString(CommonConfig.USER_PH, null);
        //Log.e("<<<<Register>>>>","===>" + user_name + phone);

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        btn_next = (Button)view.findViewById(R.id.Next);
        passwordField = (EditText) view.findViewById(R.id.register_pwd_pwd_input);
        confirmPasswordField = (EditText) view.findViewById(R.id.register_pwd_confrim_pwd_input);

        mPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_pwd_input_ly);
        mConfirmPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_pwd_con_input_ly);

        btn_next.setOnClickListener(this);
        //Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);

        //ImageView squareBlue = (ImageView) view.findViewById(R.id.square_blue);
        //DrawableCompat.setTint(squareBlue.getDrawable(), sample.color);

        setEnglishFont();
        return view;
    }

    private void addNextFragment( Button squareBlue, boolean overlap) {


        final String password = passwordField.getText().toString();
        final String passwordAgain = confirmPasswordField.getText().toString();


        if (TextUtils.isEmpty(password)) {
            mPasswordTextInputLayout.setError(getResources().getString(R.string.password_error));

            if (lang.equals(Utils.ENG_LANG)) {
                //doToast(getResources().getString(R.string.confirm_password_error));
                Utils.doToastEng(mContext, getResources().getString(R.string.password_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.password_error));
            }
            return;
        } else {
            mPasswordTextInputLayout.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(passwordAgain)) {
            mConfirmPasswordTextInputLayout.setError(getResources().getString(R.string.confirm_password_error));

            if (lang.equals(Utils.ENG_LANG)) {
                //doToast(getResources().getString(R.string.confirm_password_error));
                Utils.doToastEng(mContext, getResources().getString(R.string.confirm_password_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.confirm_password_error_mm));
            }
            return;
        } else {
            mConfirmPasswordTextInputLayout.setErrorEnabled(false);
        }

        if (password.length() < minPasswordLength) {

            if (lang.equals(Utils.ENG_LANG)) {


                Utils.doToastEng(mContext, getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast,
                        minPasswordLength, minPasswordLength));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast_mm,
                        minPasswordLength, minPasswordLength));
            }
            return;
        }

        if (!password.equals(passwordAgain)) {
            //showToast(R.string.com_parse_ui_mismatch_confirm_password_toast);
            if (lang.equals(Utils.ENG_LANG)) {
                //doToast(getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast));
                Utils.doToastEng(mContext, getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast_mm));
            }
            confirmPasswordField.selectAll();
            confirmPasswordField.requestFocus();
            return;
        }

        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        mEditorUserInfo.putString(CommonConfig.USER_PWD, password);
        mEditorUserInfo.putString(CommonConfig.USER_CON_PWD, passwordAgain);


        mEditorUserInfo.commit();

        RegisterProfileFragment3 registerProfileFragment3 = RegisterProfileFragment3.newInstance();

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

        registerProfileFragment3.setEnterTransition(slideTransition);
        registerProfileFragment3.setAllowEnterTransitionOverlap(overlap);
        registerProfileFragment3.setAllowReturnTransitionOverlap(overlap);
        registerProfileFragment3.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, registerProfileFragment3)
                .addToBackStack(null)
                .addSharedElement(squareBlue, getString(R.string.register_next))
                .commit();
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
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_pwd);
    }
    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_pwd);
    }
}
