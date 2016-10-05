package org.undp_iwomen.iwomen.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import org.smk.clientapi.NetworkEngine;
import org.smk.model.TLGTownship;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.ui.adapter.TLGTownshipSpinnerAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterTlgFragment4 extends Fragment implements View.OnClickListener {

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private Button btn_next;
    private Button btn_tlg_back;
    private ProgressDialog mProgressDialog;
    private Spinner spnTLG;
    private String tlgCityID, tlgCityName;

    public static RegisterTlgFragment4 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterTlgFragment4 fragment = new RegisterTlgFragment4();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterTlgFragment4 newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterTlgFragment4 fragment = new RegisterTlgFragment4();
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
        View view = inflater.inflate(R.layout.activity_register_4_tlg, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button) view.findViewById(R.id.Next);
        btn_tlg_back = (Button)view.findViewById(R.id.register_tlg_back_btn);
        spnTLG = (Spinner) view.findViewById(R.id.register_tlg_spn_township);
        btn_next.setOnClickListener(this);
        btn_tlg_back.setOnClickListener(this);

        setEnglishFont();

        getTlgTownship();

        return view;
    }


    private void getTlgTownship() {
        if (Connection.isOnline(getActivity().getApplicationContext())) {
            mProgressDialog.show();
            NetworkEngine.getInstance().getTLGTownship(new Callback<List<TLGTownship>>() {
                @Override
                public void success(List<TLGTownship> tlgTownships, Response response) {

                    mProgressDialog.dismiss();

                    final ArrayList<TLGTownship> tlgTownshipArrayList = new ArrayList<TLGTownship>();
                    tlgTownshipArrayList.addAll(tlgTownships);

                    TLGTownshipSpinnerAdapter adapter = new TLGTownshipSpinnerAdapter((AppCompatActivity) getActivity(), tlgTownshipArrayList);
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

        } else {
            mProgressDialog.dismiss();

            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.no_connection));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.no_connection_mm));
            } else {


            }
        }
    }

    private void addNextFragment(Button squareBlue, boolean overlap) {


        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        //mEditorUserInfo.putString(CommonConfig.USER_PWD, password);
        //mEditorUserInfo.commit();

        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        mEditorUserInfo.putString(CommonConfig.USER_TLG_ID, tlgCityID);
        mEditorUserInfo.putString(CommonConfig.USER_TLG_NAME, tlgCityName);

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
            case R.id.register_tlg_back_btn:
                getActivity().onBackPressed();
                break;
        }

    }

    public void setEnglishFont() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_township_title);
    }

    public void setMyanmarFont() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_township_title);
    }
}
