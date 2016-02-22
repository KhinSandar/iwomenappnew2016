/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smk.sklistview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class SKListView extends ListView {
    private Callbacks mCallbacks;
	private boolean isScrollTop = false;
	private int scrollState;
	private boolean isScrollingDown = false;
	private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();
	private int scrolledValue;
	private int minDataScroll = 2;
	private boolean nextPage = true;
	private View footerView;
    private Context mContext;

	public SKListView(Context context){
		super(context);
        mContext = context;
		setOnScrollListener(scrollListener);
        footerView = View.inflate(context, R.layout.loading_bottom, null);
        if(isConnectingToInternet())
		    addFooterView(footerView);
	}
	
    public SKListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnScrollListener(scrollListener);
        footerView = View.inflate(context, R.layout.loading_bottom, null);
        if(isConnectingToInternet())
            addFooterView(footerView);
    }
    
    public SKListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, l, t, r, b);
    }

	public boolean isConnectingToInternet(){
		boolean status = false;
		ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						status = true;
					}

		}
		return status;
	}

    private OnScrollListener scrollListener = new OnScrollListener() {
		
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(mCallbacks != null){
				mCallbacks.onScrollState(scrollState);
			}
			setScrollState(scrollState);
		}
		
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if(totalItemCount > 0){
				if (mCallbacks != null) {
					trackScrolling(getScroll());
		            mCallbacks.onScrollChanged(getScroll());
		            if(getScroll() == 0){
		            	isScrollTop = true;
		            }else{
		            	isScrollTop = false;	
		            }
					if (totalItemCount - (visibleItemCount + firstVisibleItem) <= getMinDataScroll() && getScroll() > 0 && isNextPage()) {
						mCallbacks.onNextPageRequest();
						if(getFooterViewsCount() == 0 && isConnectingToInternet()){
							addFooterView(footerView);
						}	
						
					} else {
						try{
							if(!nextPage && isConnectingToInternet())
								removeFooterView(footerView);
						}catch(Exception e){
							
						}
					}
					
		        }
			} 
		}
	};
	
	private int getScroll() {
	    View c = getChildAt(0); //this is the first visible row
	    int scrollY = -c.getTop();
	    listViewItemHeights.put(getFirstVisiblePosition(), c.getHeight());
	    for (int i = 0; i < getFirstVisiblePosition(); ++i) {
	        if (listViewItemHeights.get(i) != null) //(this is a sanity check)
	            scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
	    }
	    return scrollY;
	}

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }
    
    public int getScrollState() {
		return scrollState;
	}
    
	public void setScrollState(int scrollState) {
		this.scrollState = scrollState;
	}
	
	public void trackScrolling(int scrollY){
		if(scrolledValue != scrollY){
			if(scrolledValue > scrollY){
				setScrollingDown(false);
			}else{
				setScrollingDown(true);
			}
		}
		
		scrolledValue = scrollY;
	}
	
	public boolean isScrollingDown() {
		return isScrollingDown;
	}
	
	public void setScrollingDown(boolean isScrollingDown) {
		this.isScrollingDown = isScrollingDown;
	}
	
	public int getScrolledValue() {
		return scrolledValue;
	}

	public void setScrolledValue(int scrolledValue) {
		this.scrolledValue = scrolledValue;
	}

	public boolean isScrollTop(){
		return isScrollTop;
	}
	
	public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }
	
    public boolean isNextPage() {
		return nextPage;
	}
    
	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public int getMinDataScroll() {
		return minDataScroll;
	}
	
	public void setMinDataScroll(int minDataScroll) {
		this.minDataScroll = minDataScroll;
	}

	public static interface Callbacks {
		public void onScrollState(int scrollSate);
        public void onScrollChanged(int scrollY);
        public void onNextPageRequest();
    }
}
