package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.smk.skconnectiondetector.SKConnectionDetector;
import com.smk.sklistview.SKListView;

import org.smk.clientapi.NetworkEngine;
import org.smk.model.CalendarEvent;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.EventDetailActivity;
import org.undp_iwomen.iwomen.ui.activity.NewEventActivity;
import org.undp_iwomen.iwomen.ui.adapter.EventAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class ViewEventsFragment extends Fragment {

    //KSD code
    private Context mContext;
    private SKListView lvCalendar;
    private int paginater = 1;
    private ArrayList<CalendarEvent> calendarEventArrayList;


    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    //KSD code
    //private RecyclerView mRecyclerView;
    private View mEmptyView;
    private List<String> mEvents;
    private int mChoiceMode;

    private EventAdapter mEventAdapter;

    public ItemTouchHelper mItemTouchHelper;

    public ItemTouchHelper.Callback mCallback;

    private Button mCreateEventButton;

    private CustomTextView txt_women_remember_day;
    private CustomTextView txt_date_title;

    private static final String STR_DATE = "Date";
    private static final String STR_MONTH = "Month";
    String str_date;
    int imonth;


    public ViewEventsFragment() {
        // Required empty public constructor
    }

    public static ViewEventsFragment newInstance() {

        return new ViewEventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: load event data from server
        //mEvents = generateSampleEvents();
    }

    /*@Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EventsRecycler,
                0, 0);
        mChoiceMode = a.getInt(R.styleable.EventsRecycler_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        a.recycle();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_events, container, false);
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            str_date = bundleArgs.getString("Date");
            imonth = bundleArgs.getInt("Month");
            //Toast.makeText(getActivity().getApplicationContext(), "Trip ID>>" + strTripId, Toast.LENGTH_SHORT).show();
        }
        initViews(rootView);
        initViewsListeners();
        return rootView;
    }

    private boolean isLoading = true;
    private SKListView.Callbacks skCallbacks = new SKListView.Callbacks() {
        @Override
        public void onScrollState(int scrollSate) {

        }

        @Override
        public void onScrollChanged(int scrollY) {

        }

        @Override
        public void onNextPageRequest() {
            if (!isLoading) {
                getCalendarListByPagination();
            }
        }
    };

    private void initViews(View rootView) {
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        lvCalendar = (SKListView) rootView.findViewById(R.id.view_events_list);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        calendarEventArrayList = new ArrayList<>();
        mEventAdapter = new EventAdapter(getActivity().getApplicationContext(), calendarEventArrayList, mstr_lang);
        lvCalendar.setAdapter(mEventAdapter);
        lvCalendar.setCallbacks(skCallbacks);
        lvCalendar.setNextPage(true);
        mEventAdapter.notifyDataSetChanged();


        mEmptyView = rootView.findViewById(R.id.empty_events_view);

        getCalendarListByPagination();

        mCreateEventButton = (Button) rootView.findViewById(R.id.view_events_create_event_button);
        txt_women_remember_day = (CustomTextView) rootView.findViewById(R.id.view_events_women_day_txt);
        txt_date_title = (CustomTextView) rootView.findViewById(R.id.view_events_date_title);

        Bundle bundle = getArguments();
        str_date = bundle.getString(STR_DATE);
        imonth = bundle.getInt(STR_MONTH);
        txt_women_remember_day.setText(getResources().getStringArray(R.array.women_remember_day_array)[imonth]);
        txt_date_title.setText(str_date);
        txt_women_remember_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(EventDetailFragment.EXTRA_EVENT_TYPE, EventDetailFragment.TYPE_IMPLICIT);
                intent.putExtra(EventDetailFragment.EXTRA_MONTH, imonth);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        lvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(EventDetailFragment.EXTRA_EVENT_TYPE, EventDetailFragment.TYPE_USER_DEFINED);
                intent.putExtra(EventDetailFragment.EXTRA_ID, calendarEventArrayList.get(position).getId());
                Log.e("<<View event detail>>>", "==>" + calendarEventArrayList.get(position).getId() + "/" + calendarEventArrayList.size());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });

        //TODO
        /*Intent intent = new Intent(getActivity(), EventDetailActivity.class)
                //TODO: replace with your desired data
                .putExtra(EventDetailFragment.EXTRA_ID, mEvents.get(position));
        startActivity(intent);*/

    }

    private void initViewsListeners() {
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCalendarListByPagination() {
        if (Connection.isOnline(getActivity())) {
            isLoading = true;
            NetworkEngine.getInstance().getCalendarEvent(paginater, new Callback<List<CalendarEvent>>() {
                @Override
                public void success(List<CalendarEvent> calendarEvents, Response response) {
                    calendarEventArrayList.addAll(calendarEvents);
                    mEventAdapter.notifyDataSetChanged();
                    isLoading = false;
                    if (calendarEventArrayList.size() == 12) {
                        lvCalendar.setNextPage(true);
                        paginater++;
                    } else {
                        // If no more item
                        lvCalendar.setNextPage(false);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    isLoading = false;
                }
            });

        } else {

            SKConnectionDetector.getInstance(getActivity()).showErrorMessage();

        }
    }

/*
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
*/


    private List<String> generateSampleEvents() {
        List<String> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            events.add("Meeting " + i + 1);
        }
        return events;
    }

}
