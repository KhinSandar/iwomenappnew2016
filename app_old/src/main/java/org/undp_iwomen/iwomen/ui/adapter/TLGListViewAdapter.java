package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.TlgProfileItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;

import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class TLGListViewAdapter extends BaseAdapter
{
    /*private String[] listName = null;
    private String[] listAuthorName = null;
    private String[] listDate = null;
    private String[] listDataText = null;
    private int[] listicon = null;
*/
    //private Activity activity;

    // Declare Variables
    private List<TlgProfileItem> SubResourceItems;
    Context mContext;
    LayoutInflater inflater;
    String mstr_lang;
    public TLGListViewAdapter(Context context, List<TlgProfileItem> resourceItems, String typeFaceName) { //
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
        public TextView txtAddress;
        public TextView txtName;
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


            holder.txtAddress = (TextView)view.findViewById(R.id.sub_resouce_list_item_author_name);
            holder.txtName= (TextView)view.findViewById(R.id.sub_resouce_list_item_title);
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

        holder.txtAddress.setVisibility(View.VISIBLE);
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.txtName.setText(SubResourceItems.get(position).get_tlg_group_name());

            holder.txtAddress.setText(SubResourceItems.get(position).get_tlg_group_address());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());

            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }else{
            holder.txtName.setText(SubResourceItems.get(position).get_tlg_group_name());
            //holder.txtBodyText.setText(ResourceItems.get(position).getResourceText());
            holder.txtAddress.setText(SubResourceItems.get(position).get_tlg_group_address());

            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }
        holder.imgIcon.setImageResource(R.drawable.place_holder);
        holder.progressBar.setVisibility(View.GONE);



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
