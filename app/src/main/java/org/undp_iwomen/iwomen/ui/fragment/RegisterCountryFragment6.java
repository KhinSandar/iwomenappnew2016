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

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterCountryFragment6 extends Fragment implements  View.OnClickListener{

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private Button btn_next;
    private TextInputLayout mCountryTextInputLayout;

    private EditText countryField;

    public static RegisterCountryFragment6 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterCountryFragment6 fragment = new RegisterCountryFragment6();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterCountryFragment6 newInstance( ) {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterCountryFragment6 fragment = new RegisterCountryFragment6();
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
        View view = inflater.inflate(R.layout.activity_register_6_country, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button)view.findViewById(R.id.Next);
        mCountryTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_country_type_input_ly);
        countryField = (EditText) view.findViewById(R.id.register_country_input);
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

    private void addNextFragment( Button squareBlue, boolean overlap) {

        final String countryName = countryField.getText().toString().trim();
        if (TextUtils.isEmpty(countryName)) {
            countryField.setError(getResources().getString(R.string.register_country_error));

            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.register_country_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.register_country_error));
            }

            return;
        } else {
            mCountryTextInputLayout.setErrorEnabled(false);
        }
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        mEditorUserInfo.putString(CommonConfig.USER_COUNTRY, countryName);
        mEditorUserInfo.commit();

        RegisterPhotoFragment7 registerPhotoFragment7 = RegisterPhotoFragment7.newInstance();

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

        registerPhotoFragment7.setEnterTransition(slideTransition);
        registerPhotoFragment7.setAllowEnterTransitionOverlap(overlap);
        registerPhotoFragment7.setAllowReturnTransitionOverlap(overlap);
        registerPhotoFragment7.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, registerPhotoFragment7)
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
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_country_title);
    }
    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_country_title);
    }
}
