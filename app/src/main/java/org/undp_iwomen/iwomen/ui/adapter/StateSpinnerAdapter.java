package org.undp_iwomen.iwomen.ui.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CityForShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KSD on 7/1/2015.
 */

public class StateSpinnerAdapter extends ArrayAdapter<CityForShow> {
    private final String TAG = StateSpinnerAdapter.class.getName();
    private List<CityForShow> mCityList;
    private AppCompatActivity mContext;


    public static class ViewHolder {
        public final TextView enTextView;
        public final TextView mmTextView;

        public ViewHolder(View view) {
            enTextView = (TextView) view.findViewById(R.id.city_in_en_textview);
            mmTextView = (TextView) view.findViewById(R.id.city_in_mm_textview);
        }
    }

    public StateSpinnerAdapter(AppCompatActivity context, ArrayList<CityForShow> cityList) {
        super(context, R.layout.custom_city_spinner_item, cityList);
        mCityList = cityList;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent,true);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent, boolean isDropDown) {
        View view;

        if(!isDropDown) {
            view = mContext.getLayoutInflater().inflate(R.layout.custom_city_spinner_item, parent,
                    false);
        }else{
            view = mContext.getLayoutInflater().inflate(R.layout.custom_city_spinner_dropdown_item, parent,
                    false);
        }

        TextView cityInEnTextView = (TextView) view.findViewById(R.id.city_in_en_textview);
        TextView cityInMmTextView = (TextView) view.findViewById(R.id.city_in_mm_textview);

        CityForShow cityForShow = mCityList.get(position);

        cityInEnTextView.setText(cityForShow.getNameInEnglish());
        cityInMmTextView.setText(cityForShow.getNameInMyanmar());

        return view;
    }

}
