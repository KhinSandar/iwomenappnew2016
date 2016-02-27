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
public class LeadershipTipsAdapter extends RecyclerView.Adapter<LeadershipTipsAdapter.LeadershipTipsAdapterViewHolder> {

    private Context mContext;
    final private LeadershipTipsAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;
    final private ItemChoiceManager mICM;
    private List<String> mTips;

    public LeadershipTipsAdapter(Context context, LeadershipTipsAdapterOnClickHandler dh, View emptyView, int choiceMode, List<String> tips) {
        mContext = context;
        mTips = tips;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public LeadershipTipsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_leadership_tips, viewGroup, false);
            view.setFocusable(true);
            return new LeadershipTipsAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(LeadershipTipsAdapterViewHolder leadershipTipsAdapterViewHolder, int position) {
        //TODO: bind with object
        leadershipTipsAdapterViewHolder.mTipTitle.setText(mTips.get(0));

        mICM.onBindViewHolder(leadershipTipsAdapterViewHolder, position);
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
        if (null == mTips) return 0;
        return mTips.size();
    }

    public class LeadershipTipsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTipTitle;
        public final TextView mTipAuthor;

        public LeadershipTipsAdapterViewHolder(View itemView) {
            super(itemView);
            mTipTitle = (TextView) itemView.findViewById(R.id.item_tip_title);
            mTipAuthor = (TextView) itemView.findViewById(R.id.item_tip_author);
            itemView.setOnClickListener(this);
            mICM.onClick(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition, this);
        }
    }

    public interface LeadershipTipsAdapterOnClickHandler {
        void onClick(int position, LeadershipTipsAdapterViewHolder vh);
    }

    public void swapCursor(List<String> newTips) {
        mTips = newTips;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof LeadershipTipsAdapterViewHolder) {
            LeadershipTipsAdapterViewHolder vfh = (LeadershipTipsAdapterViewHolder) viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    public void delete(int position) {
        mTips.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int from, int to) {
        String prev = mTips.remove(from);
        mTips.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }


}
