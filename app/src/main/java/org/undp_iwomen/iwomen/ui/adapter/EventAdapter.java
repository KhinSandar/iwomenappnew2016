package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.smk.model.CalendarEvent;
import org.undp_iwomen.iwomen.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater mInflater;
    private String mstr_lang;
    private List<CalendarEvent> mEvents;
    private ArrayList<CalendarEvent> mEventsArraylist;

    public EventAdapter(Context context, List<CalendarEvent> events, String typeFaceName) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mEvents = events;
        mstr_lang = typeFaceName;
        this.mEventsArraylist = new ArrayList<>();
        this.mEventsArraylist.addAll(mEvents);

    }


    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public CalendarEvent getItem(int i) {
        return mEvents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_event, null);
            holder.mEventTitle = (TextView) convertView.findViewById(R.id.item_event_title);
            holder.mEventPlace = (TextView) convertView.findViewById(R.id.item_event_place);
            holder.mEventTime = (TextView) convertView.findViewById(R.id.item_event_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CalendarEvent calendar = getItem(i);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        //2015-09-16T13:46:34.875Z
        //Sun Jun 22 18:32:00 GMT+06:30 2014
        //Log.e("Stories Post Adapter==","Date===>" + item.getCreated_at());
        /*try {
            Date timedate = format.parse(calendar.getStartDate());
            holder.mEventTime.setText(sdf.format(timedate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
       */
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.mEventTitle.setText(calendar.getTitle());
        }else{
            holder.mEventTitle.setText(calendar.getTitleMm());
        }
        //Log.e("<<Adapter Data>>","===>" + calendar.getCreatedAt() + calendar.getLocation() +"/"+ calendar.getStartTime());

        holder.mEventPlace.setText(calendar.getLocation());
        holder.mEventTime.setText(calendar.getStartTime());
        return convertView;
    }




    static class ViewHolder {
        public TextView mEventTitle;
        public TextView mEventPlace;
        public TextView mEventTime;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mEvents.clear();
        if (charText.length() == 0) {
            mEvents.addAll(mEventsArraylist);
        } else {
            for (CalendarEvent fi : mEventsArraylist) {
                if (fi.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mEvents.add(fi);
                }
            }
        }
        notifyDataSetChanged();
    }


}
