package org.undp_iwomen.iwomen.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.smk.model.Categories;

import org.undp_iwomen.iwomen.ui.adapter.TalkTogetherGridViewAdapter;

import java.util.List;

/**
 * Use this class when you want a gridview that doesn't scroll and automatically
 * wraps to the height of its contents
 */
public class WrappedGridView extends GridView implements AbsListView.OnScrollListener {
    private EndlessListener listener;
    private boolean isLoading;
    private View footer;
    TalkTogetherGridViewAdapter adapter;

    boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public WrappedGridView(Context context) {
        super(context);
    }

    public WrappedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrappedGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // HACK! TAKE THAT ANDROID!
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

    }

    public EndlessListener setListener() {
        return listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null)
            return;
        if (getAdapter().getCount() == 0)
            return;
        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount && !isLoading) {
            // It is time to add new data. We call the listener
            this.addFooterView(footer);
            isLoading = true;
            listener.loadData();
        }

    }

    public void setAdapter(TalkTogetherGridViewAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(footer);
    }

    public void addNewData(List<Categories> data) {
        this.removeFooterView(footer);
        Log.i("Data", data.toString());
        adapter.Addall(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public void setLoadingView(View v) {
        /*LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = (View) inflater.inflate(resId, null);*/
        footer = v;

        this.addFooterView(footer);


    }

    public void addFooterView(View v) {
        v.setVisibility(View.VISIBLE);
        Log.i("loading", "add");
    }

    public void removeFooterView(View v) {
        v.setVisibility(View.GONE);
        Log.i("loading", "remove");
    }


    public static interface EndlessListener {
        public void loadData();
    }
}