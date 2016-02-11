package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.ItemChoiceManager;

import java.util.List;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private Context mContext;
    final private EventAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    final private ItemChoiceManager mICM;
    private List<String> mEvents;

    public EventAdapter(Context context, EventAdapterOnClickHandler dh, View emptyView, int choiceMode, List<String> events) {
        mContext = context;
        mEvents = events;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
            view.setFocusable(true);
            return new EventAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(EventAdapterViewHolder eventAdapterViewHolder, int position) {
        //TODO: bind with object
        eventAdapterViewHolder.mEventTitle.setText(mEvents.get(0));

        mICM.onBindViewHolder(eventAdapterViewHolder, position);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mICM.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    @Override
    public int getItemCount() {
        if (null == mEvents) return 0;
        return mEvents.size();
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mEventTitle;
        public final TextView mEventPlace;
        public final TextView mEventTime;


        public EventAdapterViewHolder(View itemView) {
            super(itemView);
            mEventTitle = (TextView) itemView.findViewById(R.id.item_event_title);
            mEventPlace = (TextView) itemView.findViewById(R.id.item_event_place);
            mEventTime = (TextView) itemView.findViewById(R.id.item_event_time);
            itemView.setOnClickListener(this);
            mICM.onClick(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition, this);
        }
    }

    public interface EventAdapterOnClickHandler {
        void onClick(int position, EventAdapterViewHolder vh);
    }

    public void swapCursor(List<String> newEvents) {
        mEvents = newEvents;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof EventAdapterViewHolder) {
            EventAdapterViewHolder vfh = (EventAdapterViewHolder) viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    public void delete(int position) {
        mEvents.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int from, int to) {
        String prev = mEvents.remove(from);
        mEvents.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }


}
