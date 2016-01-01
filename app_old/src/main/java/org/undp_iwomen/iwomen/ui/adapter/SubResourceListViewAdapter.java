package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.SubResourceItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class SubResourceListViewAdapter extends BaseAdapter
{
    /*private String[] listName = null;
    private String[] listAuthorName = null;
    private String[] listDate = null;
    private String[] listDataText = null;
    private int[] listicon = null;
*/
    //private Activity activity;

    // Declare Variables
    private List<SubResourceItem> SubResourceItems;
    Context mContext;
    LayoutInflater inflater;
    String mstr_lang;
    public SubResourceListViewAdapter(Context context,List<SubResourceItem> resourceItems , String typeFaceName) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mstr_lang = typeFaceName;
        this.SubResourceItems = resourceItems;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return SubResourceItems.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return SubResourceItems.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {
        public TextView txtAuthour;
        public CustomTextView txtName;
        public TextView txtTime;
        public RoundedImageView imgIcon;
        public ProgressBar progressBar;
        //public TextView txtViewTitle;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        //LayoutInflater inflator = activity.getLayoutInflater();

        if(view ==null)
        {holder = new ViewHolder();
            view = inflater.inflate(R.layout.sub_resource_list_item_new, null);//gridview_row //fra_browse_gridview_item
            /*holder.txtName= (TextView)view.findViewById(R.id.txt_sub_resource_item_name);
            holder.txtTime= (TextView)view.findViewById(R.id.txt_sub_resource_item_time);
            holder.imgIcon = (ImageView) view.findViewById(R.id.sub_resource_icon);*/


            holder.txtAuthour = (TextView)view.findViewById(R.id.sub_resouce_list_item_author_name);
            holder.txtName= (CustomTextView)view.findViewById(R.id.sub_resouce_list_item_title);
            holder.txtTime= (TextView)view.findViewById(R.id.sub_resouce_list_item_time);
            holder.imgIcon = (RoundedImageView) view.findViewById(R.id.sub_resouce_list_item_img);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.sub_resouce_list_item_progressBar);



            view.setTag(holder);
        }
        else
        {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        /*holder.txtAuthour.setText(listAuthorName[position]);

        holder.txtName.setText(listName[position]);
        holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        holder.txtTime.setText(listDate[position]);*/

        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.txtName.setText(SubResourceItems.get(position).getSub_resource_title_eng());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else if (mstr_lang.equals(Utils.MM_LANG)) {
            holder.txtName.setText(SubResourceItems.get(position).getSub_resource_title_mm());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }else {//FOR Default and Custom
            holder.txtName.setText(SubResourceItems.get(position).getSub_resource_title_mm());
        }

        if(SubResourceItems.get(position).getIcon_img_url() != null && !SubResourceItems.get(position).getIcon_img_url().isEmpty()) {

            try {

                Picasso.with(mContext)
                        .load(SubResourceItems.get(position).getIcon_img_url()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(holder.imgIcon, new ImageLoadedCallback(holder.progressBar) {
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
        }else{
            holder.progressBar.setVisibility(View.GONE);
        }


        //holder.imgIcon.setImageResource(listicon[position]);//listicon[position]



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
