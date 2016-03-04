package org.undp_iwomen.iwomen.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.smk.model.Sticker;
import org.undp_iwomen.iwomen.R;

import java.util.List;

/**
 * Created by dharmaone on 29/05/2014.
 */
public class StickerGridViewAdapter extends BaseAdapter
{

    List<Sticker> cat_data_list ;

    protected Activity mActivity;

    // the serverListSize is the total number of items on the server side,
    // which should be returned from the web request results
    protected int serverListSize = -1;

    // Two view types which will be used to determine whether a row should be displaying
    // data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;


    //private Activity activity;

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    public StickerGridViewAdapter(Activity atx, Context context, List<Sticker> catlist) { //
        super();
        mActivity = atx;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.cat_data_list = catlist;


    }
    public void setServerListSize(int serverListSize){
        this.serverListSize = serverListSize;
    }
    /**
     * disable click events on indicating rows
     */
    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    /**
     * Custom Add all
     */

    public void Addall(List<Sticker> list){
     this.cat_data_list.addAll(list);
    }

    /**
     * One type is normal data row, the other type is Progressbar
     */
/*    @Override
    public int getViewTypeCount() {
        return 2;
    }*/


    /**
     * the size of the List plus one, the one is the last row, which displays a Progressbar
     */
    @Override
    public int getCount() {
        return cat_data_list.size() ;
    }

    /**
     * return the type of the row,
     * the last row indicates the user that the ListView is loading more data
     */
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return (position >= cat_data_list.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return  cat_data_list.get(position).getObjectId();
         /*return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? listID
                .get(position) : null;*/
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
       return 0;
        /*return  (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position
                : -1;*/
    }



    public static class ViewHolder
    {
        public ImageView imgViewCate;
        //public TextView txtCateName;
        public ProgressBar progressBar;
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
            holder.imgViewCate = (ImageView) view.findViewById(R.id.edit_profile_img_item);//imageView1
            //holder.txtCateName= (TextView)view.findViewById(R.id.txt_cate_name);
            holder.progressBar = (ProgressBar)view.findViewById(R.id.edit_profile_progressBar_profile_item);


            view.setTag(holder);
        }
        else
        {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        //holder.txtCateName.setText(cat_data_list.get(position).category);
        //holder.txtCateName.setTypeface(DrawerMainActivity.faceNormal);


        try {
            Picasso.with(mContext)
                    .load(cat_data_list.get(position).getStickerImgPath() )//"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(holder.imgViewCate, new ImageLoadedCallback(holder.progressBar) {
                        @Override
                        public void onSuccess() {
                            if (this.progressBar != null) {
                                this.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }

        /*if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, view, parent);
        }*/

        return view;
    }
    /**
     * returns a View to be displayed in the last row.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView,
                              ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(mActivity);
            tvLastRow.setHint("Reached the last row.");
            tvLastRow.setGravity(Gravity.CENTER);
            return tvLastRow;
        }

        View row = convertView;
        if (row == null) {
            row = mActivity.getLayoutInflater().inflate(
                    R.layout.fragment_progress, parent, false);
        }

        return row;
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

