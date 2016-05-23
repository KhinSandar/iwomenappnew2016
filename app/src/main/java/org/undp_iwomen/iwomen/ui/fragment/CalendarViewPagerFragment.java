package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

public final class CalendarViewPagerFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    // Declare Variables
    public Context mContext;
    static LayoutInflater inflater;


    public class ViewHolder {

        TextView shopAdd;
        ResizableImageView shopImg;
        Button[] btn;
    }

    public static CalendarViewPagerFragment newInstance(int content) {

        CalendarViewPagerFragment fragment = new CalendarViewPagerFragment();
        fragment.mContent = content;
        return fragment;
    }

    private int mContent;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            //mContent = savedInstanceState.getString(KEY_CONTENT);
            savedInstanceState.getInt(KEY_CONTENT);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Context context = getActivity();
        View view = inflater.inflate(R.layout.atx_calendar_viewpager_item, null);


        ViewHolder holder = new ViewHolder();
        holder.shopImg = (ResizableImageView) view.findViewById(R.id.img_pager);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        view.setTag(holder);
        /**********important img showing***********/
        holder.shopImg.setImageResource(mContent);


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }
}
