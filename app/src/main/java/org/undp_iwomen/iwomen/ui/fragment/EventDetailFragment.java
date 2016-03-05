package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smk.skconnectiondetector.SKConnectionDetector;

import org.smk.model.CalendarEvent;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class EventDetailFragment extends Fragment {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_EVENT_TYPE = "event_type";
    public static final String EXTRA_MONTH = "month";

    private CustomTextView event_title;
    private CustomTextView event_desc;
    private CustomTextView event_location;
    private CustomTextView event_date;
    private CustomTextView event_time;

    private SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;

    @IntDef({TYPE_IMPLICIT, TYPE_USER_DEFINED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    public static final int TYPE_IMPLICIT = 0;
    public static final int TYPE_USER_DEFINED = 1;

    private String mId;
    private int mMonthResIndex;
    private int mEventType;

    public static EventDetailFragment newInstance(String id, @EventType int eventType, int month) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_EVENT_TYPE, eventType);
        args.putString(EXTRA_ID, id);
        args.putInt(EXTRA_MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mId = args.getString(EXTRA_ID);
        mEventType = args.getInt(EXTRA_EVENT_TYPE);
        mMonthResIndex = args.getInt(EXTRA_MONTH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        event_title = (CustomTextView) rootView.findViewById(R.id.event_detail_title);
        event_desc = (CustomTextView) rootView.findViewById(R.id.event_detail_desc);
        event_location = (CustomTextView) rootView.findViewById(R.id.event_detail_location);
        event_date = (CustomTextView) rootView.findViewById(R.id.event_detail_date);
        event_time = (CustomTextView) rootView.findViewById(R.id.event_detail_time);

        setEventData();
        updateUi();
        return rootView;
    }

    private void updateUi() {
        if (mEventType == TYPE_IMPLICIT) {
            String title = getResources().getStringArray(R.array.women_remember_day_array)[mMonthResIndex];
            event_title.setText(title);

            String description = getResources().getStringArray(R.array.women_remember_day_desc_array)[mMonthResIndex];
            event_desc.setText(description);

            event_location.setVisibility(View.INVISIBLE);
            event_date.setVisibility(View.INVISIBLE);
            event_time.setVisibility(View.INVISIBLE);
        }
    }

    private void setEventData() {
        if (Connection.isOnline(mContext)) {

            SMKserverAPI.getInstance().getService().getCalendarEventDetailByID("6", new Callback<CalendarEvent>() {
                @Override
                public void success(CalendarEvent calendarEvent, Response response) {

                    if (lang.equals(Utils.ENG_LANG)) {
                        event_title.setText(calendarEvent.getTitle());
                        event_desc.setText(calendarEvent.getDescription());
                        event_location.setText(calendarEvent.getLocation());

                    } else {
                        event_title.setText(calendarEvent.getTitleMm());
                        event_desc.setText(calendarEvent.getDescriptionMm());
                        event_location.setText(calendarEvent.getLocation());


                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

                    try {
                        Date start_date = format.parse(calendarEvent.getStartDate());
                        Date end_date = format.parse(calendarEvent.getEndDate());

                        event_date.setText(sdf.format(start_date) + " To " + sdf.format(end_date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    event_time.setText(calendarEvent.getStartTime() + " - " + calendarEvent.getEndTime());
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            event_title.setText("Title");
        } else {
            SKConnectionDetector.getInstance(getActivity()).showErrorMessage();

        }
    }

    private String getUserFriendlyDateString(Date date) {

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private String getUserFriendlyTimeString(Date date) {
        String format = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
