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

import com.parse.utils.Utils;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterLoginFragment1 extends Fragment implements View.OnClickListener {

    private static final String EXTRA_SAMPLE = "sample";

    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;

    private Button btn_next;
    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mMobileNoForNrcTextInputLayout;

    private EditText usernameField;
    private EditText mobileNoForNrcField;


    public static RegisterLoginFragment1 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterLoginFragment1 fragment = new RegisterLoginFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterLoginFragment1 newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterLoginFragment1 fragment = new RegisterLoginFragment1();
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
        View view = inflater.inflate(R.layout.activity_register_fblogin, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);


        btn_next = (Button) view.findViewById(R.id.Next);
        mUserNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_fb_user_name);
        mMobileNoForNrcTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_fb_phone_number);
        usernameField = (EditText) view.findViewById(R.id.register_fb_username_input);
        mobileNoForNrcField = (EditText) view.findViewById(R.id.register_fb_phone_number_input);


        btn_next.setOnClickListener(this);

        if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            setEnglishFont();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            setMyanmarFont();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            setMyanmarFontUni();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            setMyanmarFontDefault();
        }


        return view;
    }


    private void addNextFragment(Button squareBlue, boolean overlap) {


        final String username = usernameField.getText().toString().trim();
        final String mobileNoForNrc = mobileNoForNrcField.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mUserNameTextInputLayout.setError(getResources().getString(com.parse.ui.R.string.your_name_error));

            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(com.parse.ui.R.string.your_name_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(com.parse.ui.R.string.your_name_error_mm));
            }

            return;
        } else {
            mUserNameTextInputLayout.setErrorEnabled(false);
        }

        boolean inputMobileOk = true;

        if (TextUtils.isEmpty(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(com.parse.ui.R.string.mobile_number_error));
            //doToast(getResources().getString(R.string.mobile_number_error));
            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(com.parse.ui.R.string.mobile_number_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(com.parse.ui.R.string.mobile_number_error_mm));
            }
            inputMobileOk = false;
            return;
        }
        /*else if (!ValidatorUtils.isValidMobileNo(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(com.parse.ui.R.string.invalid_mobile_number));
            //doToast(getResources().getString(R.string.invalid_mobile_number));
            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(com.parse.ui.R.string.invalid_mobile_number));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(com.parse.ui.R.string.invalid_mobile_number_mm));
            }
            inputMobileOk = false;
            return;
        }*/
        if (inputMobileOk) {
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
        }


        RegisterPwdFragment2 registerPwdFragment2 = RegisterPwdFragment2.newInstance();

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

        registerPwdFragment2.setEnterTransition(slideTransition);
        registerPwdFragment2.setAllowEnterTransitionOverlap(overlap);
        registerPwdFragment2.setAllowReturnTransitionOverlap(overlap);
        registerPwdFragment2.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, registerPwdFragment2)
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
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);

        ((RegisterMainActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        /*usernameField.setHint(getResources().getString(com.parse.ui.R.string.register_name_hint));
        mobileNoForNrcField.setHint(getResources().getString(com.parse.ui.R.string.register_ph_hint));


        //Set Type Face

        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));*/


    }

    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_mm);
        ((RegisterMainActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));

       /* usernameField.setHint(getResources().getString(com.parse.ui.R.string.register_name_hint));
        mobileNoForNrcField.setHint(getResources().getString(com.parse.ui.R.string.register_ph_hint));

        //Set Type Face
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));*/


    }

    public void setMyanmarFontUni() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);
    }

    public void setMyanmarFontDefault() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);
    }


}
