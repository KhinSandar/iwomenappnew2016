package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.SquareImageView;

import java.util.ArrayList;

public class EditProfileGridviewAdapter extends BaseAdapter
{
	private ArrayList<String> listShopName = null;
	private ArrayList<String> listShopImg = null ;
	//private Activity activity;
	
	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	
	public EditProfileGridviewAdapter(Context context, ArrayList<String> listCountry ,ArrayList<String> listImg) { //
		super();
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		//Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
		this.listShopName = listCountry;
		this.listShopImg = listImg;
		//Log.e("BrowseGridviewAdapter Constructor", "" + listShopName.size() +listShopName.toString());
		//this.activity = activity;
	}
	
	
	public int getCount() {
		// TODO Auto-generated method stub
		return listShopName.size();
	}

	
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listShopName.get(position);
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		public SquareImageView imgViewFlag;
		public ProgressBar imgProgress;
		//public TextView txtViewTitle;
	}
	
	
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		
		//LayoutInflater inflator = activity.getLayoutInflater();
		
		if(view ==null)
		{
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.edit_grid_view_item, null);//gridview_row //fra_browse_gridview_item
			
			//holder.txtViewTitle = (TextView) view.findViewById(R.id.textView1);
			holder.imgViewFlag = (SquareImageView) view.findViewById(R.id.edit_profile_img_item);//imageView1
			holder.imgProgress = (ProgressBar)view.findViewById(R.id.edit_profile_progressBar_profile_item);
			
			view.setTag(holder);
		}
		else
		{
			//holder = (ViewHolder) view.getTag();
			holder = (ViewHolder) view.getTag();
		}

		if (listShopImg.get(position) != null && listShopImg.get(position) != "") {

			try {

				Picasso.with(mContext)
						.load(listShopImg.get(position)) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
						.placeholder(R.drawable.blank_profile)
						.error(R.drawable.blank_profile)
						.into(holder.imgViewFlag, new ImageLoadedCallback(holder.imgProgress) {
							@Override
							public void onSuccess() {
								if (this.progressBar != null) {
									this.progressBar.setVisibility(View.GONE);
								} else {
									this.progressBar.setVisibility(View.VISIBLE);
								}
							}

						});
			} catch (OutOfMemoryError outOfMemoryError) {
				outOfMemoryError.printStackTrace();
			}
		} else {
			holder.imgProgress.setVisibility(View.GONE);
		}
		
		//holder.txtViewTitle.setText(listShopName.get(position));
		//holder.imgViewFlag.setImageResource(listShopImg.get(position));
		
		return view;
	}

	private class ImageLoadedCallback implements com.squareup.picasso.Callback {
		ProgressBar progressBar;

		public ImageLoadedCallback(ProgressBar progBar) {
			progressBar = progBar;
		}

		@Override
		public void onSuccess() {

		}

		@Override
		public void onError() {

		}
	}

}
