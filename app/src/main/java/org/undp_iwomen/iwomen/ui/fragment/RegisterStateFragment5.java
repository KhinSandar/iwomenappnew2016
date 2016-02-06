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
import android.widget.Button;
import android.widget.Spinner;

import com.parse.model.CityForShow;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
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


        CityForShow ygnCity = new CityForShow("Klsa0sQrMw", "Yangon", getResources().getString(R.string.ygn));
        CityForShow mdyCity = new CityForShow("A471gH1HNh", "Mandalay", getResources().getString(R.string.mdy));
        CityForShow nptCity = new CityForShow("EGDFFZrIM7", "Nay Pyi Taw", getResources().getString(R.string.npt));

        cities.add(ygnCity);
        cities.add(mdyCity);
        cities.add(nptCity);

        String[] str_item_type = new String[3];
        str_item_type[0] = "Type of Item";
        str_item_type[1] = "Non-Refrigerated Goods";
        str_item_type[2] = "Refrigerated Goods (+ S$2.00)";
        StateSpinnerAdapter adapter = new StateSpinnerAdapter((AppCompatActivity)getActivity(), cities);
        spinner.setAdapter(adapter);
    }

    private void addNextFragment( Button squareBlue, boolean overlap) {
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        Log.e("<<Spn data>>", "====>" + spn_state.getSelectedItem().toString() + "/" + spn_state.getSelectedItem().toString());
        mEditorUserInfo.putString(CommonConfig.USER_STATE, spn_state.getSelectedItem().toString());
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
