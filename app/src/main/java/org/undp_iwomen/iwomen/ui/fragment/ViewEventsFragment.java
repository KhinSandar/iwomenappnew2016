package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.EventDetailActivity;
import org.undp_iwomen.iwomen.ui.activity.NewEventActivity;
import org.undp_iwomen.iwomen.ui.adapter.EventAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class ViewEventsFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private List<String> mEvents;
    private int mChoiceMode;

    private EventAdapter mEventAdapter;

    public ItemTouchHelper mItemTouchHelper;

    public ItemTouchHelper.Callback mCallback;

    private Button mCreateEventButton;

    public ViewEventsFragment(){
        // Required empty public constructor
    }

    public static ViewEventsFragment newInstance(){
        return new ViewEventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: load event data from server
        mEvents = generateSampleEvents();
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EventsRecycler,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.EventsRecycler_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        a.recycle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_events, container, false);
        initViews(rootView);
        initViewsListeners();
        return rootView;
    }

    private void initViews(View rootView){

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.view_events_list);
        mEmptyView = rootView.findViewById(R.id.empty_events_view);
        setupEventRecyclerView();
        mCreateEventButton = (Button) rootView.findViewById(R.id.view_events_create_event_button);
    }

    private void initViewsListeners(){
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupEventRecyclerView(){

        mEventAdapter = new EventAdapter(getActivity(), new EventAdapter.EventAdapterOnClickHandler() {
            @Override
            public void onClick(int position, EventAdapter.EventAdapterViewHolder vh) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class)
                        //TODO: replace with your desired data
                        .putExtra(EventDetailFragment.EXTRA_ID, mEvents.get(position));
                startActivity(intent);
            }

        }, mEmptyView, mChoiceMode, mEvents);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mEventAdapter);

        //it's ok to use with cursorLoader,
        // but now just use with list
        mEventAdapter.swapCursor(mEvents);
    }


    private List<String> generateSampleEvents(){
        List<String> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            events.add("Meeting " + i+1);
        }
        return events;
    }

}
