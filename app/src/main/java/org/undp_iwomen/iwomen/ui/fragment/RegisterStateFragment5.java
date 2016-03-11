package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CityForShow;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.ui.adapter.StateSpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterStateFragment5 extends Fragment implements  View.OnClickListener{

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private Button btn_next;

    private Spinner spn_state;
    private String stateName;
    public static RegisterStateFragment5 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterStateFragment5 fragment = new RegisterStateFragment5();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterStateFragment5 newInstance( ) {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterStateFragment5 fragment = new RegisterStateFragment5();
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
        View view = inflater.inflate(R.layout.activity_register_5_state, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button)view.findViewById(R.id.Next);
        spn_state = (Spinner)view.findViewById(R.id.register_state_state_spn);
        btn_next.setOnClickListener(this);

        setEnglishFont();
        bindSpinnerAdapter(spn_state);

        return view;
    }

    private void bindSpinnerAdapter(Spinner spinner) {
        //Spinner spn_route, spn_bus_no, spn_bus_class, spn_price;
        //Spinner spn_alternate_location, spn_arrival_location;




        final ArrayList<CityForShow> cities = new ArrayList<CityForShow>();


        CityForShow city1 = new CityForShow("Klsa0sQrMw",  getResources().getString(R.string.state_1), getResources().getString(R.string.state_1m));
        CityForShow city2 = new CityForShow("A471gH1HNh",  getResources().getString(R.string.state_2), getResources().getString(R.string.state_2m));
        CityForShow city3 = new CityForShow("EGDFFZrIM7",  getResources().getString(R.string.state_3), getResources().getString(R.string.state_3m));
        CityForShow city4 = new CityForShow("EGDFFZrIM7",  getResources().getString(R.string.state_4), getResources().getString(R.string.state_4m
        ));

        CityForShow city5 = new CityForShow("A471gH1HNh", getResources().getString(R.string.state_5), getResources().getString(R.string.state_5m));
        CityForShow city6 = new CityForShow("EGDFFZrIM7", getResources().getString(R.string.state_6), getResources().getString(R.string.state_6m));
        CityForShow city7 = new CityForShow("Klsa0sQrMw", getResources().getString(R.string.state_7), getResources().getString(R.string.state_7m));
        CityForShow city8 = new CityForShow("A471gH1HNh", getResources().getString(R.string.state_8), getResources().getString(R.string.state_8m));
        CityForShow city9 = new CityForShow("EGDFFZrIM7", getResources().getString(R.string.state_9), getResources().getString(R.string.state_9m));
        CityForShow city10 = new CityForShow("Klsa0sQrMw", getResources().getString(R.string.state_10), getResources().getString(R.string.state_10m));
        CityForShow city11 = new CityForShow("A471gH1HNh", getResources().getString(R.string.state_11), getResources().getString(R.string.state_11m));
        CityForShow city12 = new CityForShow("EGDFFZrIM7", getResources().getString(R.string.state_12), getResources().getString(R.string.state_12m));
        CityForShow city13 = new CityForShow("Klsa0sQrMw",getResources().getString(R.string.state_13), getResources().getString(R.string.state_13m));
        CityForShow city14 = new CityForShow("A471gH1HNh", getResources().getString(R.string.state_14), getResources().getString(R.string.state_14m));

        CityForShow city15 = new CityForShow("A471gH1HNh",  getResources().getString(R.string.state_15), getResources().getString(R.string.state_15m));

        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        cities.add(city5);
        cities.add(city6);
        cities.add(city7);
        cities.add(city8);
        cities.add(city9);
        cities.add(city10);
        cities.add(city11);
        cities.add(city12);
        cities.add(city13);
        cities.add(city14);
        cities.add(city15);


        StateSpinnerAdapter adapter = new StateSpinnerAdapter((AppCompatActivity)getActivity(), cities);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateName = cities.get(position).getNameInEnglish();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addNextFragment( Button squareBlue, boolean overlap) {
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        Log.e("<<Spn data>>", "====>" +stateName);
        mEditorUserInfo.putString(CommonConfig.USER_STATE, stateName);
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
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_state_title);
    }
    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_state_title);
    }
}
