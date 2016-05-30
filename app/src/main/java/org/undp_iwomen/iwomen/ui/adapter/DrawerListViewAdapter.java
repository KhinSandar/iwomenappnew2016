package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

/**
 * Created by khinsandar on 7/29/15.
 */
public class DrawerListViewAdapter extends BaseAdapter {
    private String[] listName = null;
    private int[] listicon = null;


    Context mContext;
    LayoutInflater inflater;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;

    public DrawerListViewAdapter(Context context, String[] listName, int[] listic, String typefaceName) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
        this.listName = listName;
        this.listicon = listic;
        sharePrefLanguageUtil = mContext.getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mstr_lang = typefaceName;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return listName.length;
    }


    public String getItem(int position) {
        // TODO Auto-generated method stub
        return listName[position];
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder {

        public CustomTextView txtName;
        public ImageView imgIcon;
        //public TextView txtViewTitle;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        //LayoutInflater inflator = activity.getLayoutInflater();

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.drawer_list_item, null);//gridview_row //fra_browse_gridview_item
            holder.txtName = (CustomTextView) view.findViewById(R.id.txt_item_name);
            holder.imgIcon = (ImageView) view.findViewById(R.id.icon);


            view.setTag(holder);
        } else {
            //holder = (ViewHolder) view.getTag();
            holder = (ViewHolder) view.getTag();
        }

        holder.txtName.setText(listName[position]);


        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            holder.txtName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }


        holder.imgIcon.setImageResource(listicon[position]);

        return view;
    }

}
