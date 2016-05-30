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

import com.smk.model.Categories;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmaone on 29/05/2014.
 */
public class TalkTogetherGridViewAdapter extends BaseAdapter {
    /*private ArrayList<String> listName = null;
    private ArrayList<String> listImg = null ;
    private ArrayList<String>  listID = null;
*/
    ArrayList<Categories> cat_data_list = null;

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
    String mstr_lang;


    public TalkTogetherGridViewAdapter(Activity atx, Context context, ArrayList<Categories> catlist , String typeFaceName) { //
        super();
        mActivity = atx;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
        mstr_lang = typeFaceName;

        this.cat_data_list = catlist;

        //Log.e("BrowseGridviewAdapter Constructor", "" + listShopName.size() +listShopName.toString());
        //this.activity = activity;
    }

    public void setServerListSize(int serverListSize) {
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

    public void Addall(List<Categories> list) {
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
        return cat_data_list.size();
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
        return cat_data_list.get(position).getId().toString();
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



    /*public int getCount() {
        // TODO Auto-generated method stub
        return listName.size();
    }*/


    /*public String getItem(int position) {
        // TODO Auto-generated method stub
        return listID.get(position);
    }*/


/*
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
*/

    public static class ViewHolder {
        public ImageView imgViewCate;
        public TextView txtCateName;
        public ProgressBar progressBar;
        //public TextView txtViewTitle;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        //LayoutInflater inflator = activity.getLayoutInflater();

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.fragment_talk_together_grid_item, null);//gridview_row //fra_browse_gridview_item

            //holder.txtViewTitle = (TextView) view.findViewById(R.id.textView1);
            holder.imgViewCate = (ImageView) view.findViewById(R.id.img_cate);//imageView1
            holder.txtCateName = (TextView) view.findViewById(R.id.txt_cate_name);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar_grid);


            view.setTag(holder);
        } else {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }


        if (mstr_lang.equals(Utils.ENG_LANG)) {
            holder.txtCateName.setText(cat_data_list.get(position).getName());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());
            holder.txtCateName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else if (mstr_lang.equals(Utils.MM_LANG)) {
            holder.txtCateName.setText(cat_data_list.get(position).getNameMm());

            holder.txtCateName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }else {//FOR Default and Custom
            holder.txtCateName.setText(cat_data_list.get(position).getNameMm());
        }


        if (cat_data_list.get(position).getImage() != null && !cat_data_list.get(position).getImage().isEmpty()) {
            try {
                Picasso.with(mContext)
                        .load(cat_data_list.get(position).getImage())//"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
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
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }


        /*if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, view, parent);
        }*/

        return view;
    }

    /**
     * returns a View to be displayed in the last row.
     *
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

