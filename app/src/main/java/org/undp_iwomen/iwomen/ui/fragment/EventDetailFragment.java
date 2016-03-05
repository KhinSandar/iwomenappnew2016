package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    //TODO: replace with your desired data
    public static final String EXTRA_ID = "id";

    private CustomTextView event_title;
    private CustomTextView event_desc;
    private CustomTextView event_location;
    private CustomTextView event_date;
    private CustomTextView event_time;
    private String id;

    private SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;

    //TODO: replace with your desired data
    public static EventDetailFragment newInstance(String id) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, id);
        Log.e("<<event 1 detail>>>", "==>" + id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            id = bundleArgs.getString(EventDetailFragment.EXTRA_ID);

            Log.e("<<event 2 detail>>>", "==>" + id);
            //Toast.makeText(getActivity().getApplicationContext(), "Trip ID>>" + strTripId, Toast.LENGTH_SHORT).show();
        }

        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        event_title = (CustomTextView) rootView.findViewById(R.id.event_detail_title);
        event_desc = (CustomTextView) rootView.findViewById(R.id.event_detail_desc);
        event_location = (CustomTextView) rootView.findViewById(R.id.event_detail_location);
        event_date = (CustomTextView) rootView.findViewById(R.id.event_detail_date);
        event_time = (CustomTextView) rootView.findViewById(R.id.event_detail_time);


        setEventData();
        return rootView;
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

                    }else{
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
