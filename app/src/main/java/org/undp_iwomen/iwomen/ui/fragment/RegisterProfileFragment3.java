package org.undp_iwomen.iwomen.ui.fragment;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomRadioButton;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterProfileFragment3 extends Fragment implements  View.OnClickListener{

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private Button btn_next;
    private RadioGroup radioGroupMM, radioGroupTlg;
    private CustomRadioButton rd_mm_yes, rd_mm_no, rd_tlg_yes, rd_tlg_no , rd_worth_yes, rd_worth_no;
    private LinearLayout ly_pact;

    public static RegisterProfileFragment3 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterProfileFragment3 fragment = new RegisterProfileFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterProfileFragment3 newInstance( ) {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterProfileFragment3 fragment = new RegisterProfileFragment3();
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
        View view = inflater.inflate(R.layout.activity_register_3_profile, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button)view.findViewById(R.id.Next);

        btn_next.setOnClickListener(this);
        radioGroupMM = (RadioGroup) view.findViewById(R.id.register_pro_country_rdioGroup);
        radioGroupTlg = (RadioGroup) view.findViewById(R.id.register_pro_tlg_rdioGroup);

        rd_mm_yes = (CustomRadioButton) view.findViewById(R.id.register_pro_country_yes);
        rd_mm_no = (CustomRadioButton) view.findViewById(R.id.register_pro_country_no);
        rd_tlg_yes = (CustomRadioButton) view.findViewById(R.id.register_pro_tlg_yes);
        rd_tlg_no = (CustomRadioButton) view.findViewById(R.id.register_pro_tlg_no);
        rd_worth_yes = (CustomRadioButton)view.findViewById(R.id.register_pro_worth_yes) ;
        rd_worth_no = (CustomRadioButton)view.findViewById(R.id.register_pro_worth_no) ;
        ly_pact = (LinearLayout) view.findViewById(R.id.pact_layout);

        if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            setEnglishFont();
        }else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            setMyanmarFont();
        }else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            setMyanmarFont();

        }else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            setMyanmarFont();

        }




        rd_mm_yes.setOnClickListener(this);
        rd_mm_no.setOnClickListener(this);
        rd_tlg_yes.setOnClickListener(this);
        rd_tlg_no.setOnClickListener(this);
        ly_pact.setVisibility(View.INVISIBLE);



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

        //Log.e("<<<<isCheck>>>>","---->" + rd_mm_yes.isChecked() +rd_mm_no.isChecked()+rd_tlg_yes.isChecked()+ rd_tlg_no.isChecked() );



        RegisterTlgFragment4 registerTlgFragment4 = RegisterTlgFragment4.newInstance();
        RegisterStateFragment5 registerStateFragment5 = RegisterStateFragment5.newInstance();
        RegisterCountryFragment6 registerCountryFragment6 = RegisterCountryFragment6.newInstance();


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

        registerTlgFragment4.setEnterTransition(slideTransition);
        registerTlgFragment4.setAllowEnterTransitionOverlap(overlap);
        registerTlgFragment4.setAllowReturnTransitionOverlap(overlap);
        registerTlgFragment4.setSharedElementEnterTransition(changeBoundsTransition);


        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        if(rd_worth_yes.isChecked()){
            mEditorUserInfo.putString(CommonConfig.USER_ISWORTH, "true");

        }else{
            mEditorUserInfo.putString(CommonConfig.USER_ISWORTH, "false");

        }
        mEditorUserInfo.commit();

        if(rd_mm_yes.isChecked()){

            if(rd_tlg_yes.isChecked()){
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, registerTlgFragment4)
                        .addToBackStack(null)
                        .addSharedElement(squareBlue, getString(R.string.register_next))
                        .commit();
            }else{
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, registerStateFragment5)
                        .addToBackStack(null)
                        .addSharedElement(squareBlue, getString(R.string.register_next))
                        .commit();
            }

        }else{

            //Outside of country  can't be tlg

            /*if(rd_tlg_yes.isChecked()){
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, registerTlgFragment4)
                        .addToBackStack(null)
                        .addSharedElement(squareBlue, getString(R.string.register_next))
                        .commit();
            }*/
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, registerCountryFragment6)
                        .addToBackStack(null)
                        .addSharedElement(squareBlue, getString(R.string.register_next))
                        .commit();


        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Next:
                addNextFragment(btn_next, false);
                break;
            case R.id.register_pro_tlg_yes:
                if(rd_tlg_yes.isChecked()){
                    ly_pact.setVisibility(View.INVISIBLE);

                    rd_worth_no.isChecked();//For again press when appear view

                }else{
                    ly_pact.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.register_pro_tlg_no:
                if(rd_tlg_yes.isChecked()){
                    ly_pact.setVisibility(View.INVISIBLE);

                    rd_worth_no.isChecked();//For again press when appear view

                }else{
                    ly_pact.setVisibility(View.VISIBLE);
                }
                break;

        }

    }
    public void setEnglishFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_profile);
    }
    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_profile_mm);
    }
}
