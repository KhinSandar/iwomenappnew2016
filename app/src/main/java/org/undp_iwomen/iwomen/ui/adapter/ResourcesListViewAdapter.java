package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.makeramen.RoundedImageView;
import com.smk.model.ResourceItem;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesListViewAdapter extends BaseAdapter
{


    private List<ResourceItem> ResourceItems;
    Context mContext;
    LayoutInflater inflater;
    String mstr_lang;
    public ResourcesListViewAdapter(Context context, List<ResourceItem> resourceItems , String typeFaceName) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.ResourceItems = resourceItems;
        mstr_lang = typeFaceName;
        //Log.e("ResourcesListViewAdapter ", "" + resourceItems.size() + typeFaceName);

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return ResourceItems.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ResourceItems.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {

        public CustomTextView txtName;
        //public TextView txtBodyText;
        public RoundedImageView imgIcon;
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
            view = inflater.inflate(R.layout.resources_list_item_view, null);//gridview_row //fra_browse_gridview_item
            holder.txtName= (CustomTextView)view.findViewById(R.id.resource_item_name);
            //holder.txtBodyText= (TextView)view.findViewById(R.id.resource_item_text);
            holder.imgIcon = (RoundedImageView) view.findViewById(R.id.resouce_list_item_img);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.resouce_list_item_progressBar);


            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.txtName.setText(ResourceItems.get(position).getResourceTitleEng());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else{//FOR ALL MM FONT
            holder.txtName.setText(ResourceItems.get(position).getResourceTitleMm());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

            //holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }

        holder.imgIcon.setAdjustViewBounds(true);
        holder.imgIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(ResourceItems.get(position).getAuthorImgPath() != null && !ResourceItems.get(position).getAuthorImgPath().isEmpty()) {

            try {

                Picasso.with(mContext)
                        .load(ResourceItems.get(position).getAuthorImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
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




        //holder.txtBodyText.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        //holder.txtName.setTypeface(DrawerMainActivity.faceNormal);

        //holder.imgIcon.setImageResource(ResourceItems.get(position).getResourceImg());



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
