package org.undp_iwomen.iwomen.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.LeadershipTipDetailActivity;
import org.undp_iwomen.iwomen.ui.adapter.LeadershipTipsAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeadershipTipsFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private List<String> mTips;
    private int mChoiceMode;
    private LeadershipTipsAdapter mLeadershipTipsAdapter;

    public LeadershipTipsFragment() {
        // Required empty public constructor
    }

    public static LeadershipTipsFragment newInstance() {
        return new LeadershipTipsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTips = generateSampleTips();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leadership_tips, container, false);
        initViews(rootView);

        return rootView;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EventsRecycler,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.EventsRecycler_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        a.recycle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_leadership_tips, menu);
    }

    private void initViews(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.leadership_tips_list);
        mEmptyView = rootView.findViewById(R.id.empty_tips_view);
        setupLeadershipTipsRecyclerView();
    }

    private void setupLeadershipTipsRecyclerView(){

        mLeadershipTipsAdapter = new LeadershipTipsAdapter(getActivity(), new LeadershipTipsAdapter.LeadershipTipsAdapterOnClickHandler() {
            @Override
            public void onClick(int position, LeadershipTipsAdapter.LeadershipTipsAdapterViewHolder vh) {
                Intent intent = new Intent(getActivity(), LeadershipTipDetailActivity.class)
                        //TODO: replace with your desired data
                        .putExtra(LeadershipTipDetailFragment.EXTRA_ID, mTips.get(position));
                startActivity(intent);
            }

        }, mEmptyView, mChoiceMode, mTips);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mLeadershipTipsAdapter);

        //it's ok to use with cursorLoader,
        // but now just use with list
        mLeadershipTipsAdapter.swapCursor(mTips);
    }


    private List<String> generateSampleTips(){
        List<String> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            events.add("Meeting " + i+1);
        }
        return events;
    }

}
