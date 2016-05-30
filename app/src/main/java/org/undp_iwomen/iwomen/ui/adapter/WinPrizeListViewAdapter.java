package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.PrizePointsItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

import java.util.List;

/**
 * Created by khinsandar on 7/29/15.
 */
public class WinPrizeListViewAdapter extends BaseAdapter {
    private String[] listName = null;
    private int[] listicon = null;
    private List<PrizePointsItem> datalist;

    //private Activity activity;

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;

    public WinPrizeListViewAdapter(Context context, List<PrizePointsItem> listdata, String typefaceName) { //
        super();
        this.datalist = listdata;
        mContext = context;

        inflater = LayoutInflater.from(mContext);
        //Log.e("BrowseGridviewAdapter Constructor", "" + listCountry.size() +listCountry.toString());
        sharePrefLanguageUtil = mContext.getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mstr_lang = typefaceName;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return datalist.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datalist.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public  class ViewHolder {

        public CustomTextView txtPrize;
        public CustomTextView txtPoint;


    }


    public View getView(final int i, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        //final ViewHolder holder;
        ViewHolder vh;
        //LayoutInflater inflator = activity.getLayoutInflater();

        if (view == null) {

            view= LayoutInflater.from(mContext).inflate(R.layout.win_prize_list_item, parent, false);
            vh = new ViewHolder();

            vh.txtPrize = (CustomTextView) view.findViewById(R.id.txt_item_prize);
            vh.txtPoint = (CustomTextView) view.findViewById(R.id.txt_item_points);


            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        //Log.e("Data==","==>" +datalist.get(i).getPoint());
       /* if (datalist.get(i).getPoint() != null ) {
            vh.txtPoint.setText(datalist.get(i).getPoint());
        }
        if (datalist.get(i).getPrice() != null && !datalist.get(i).getPrice().isEmpty()) {
            vh.txtPrize.setText(datalist.get(i).getPrice()) ;
        }
*/



        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            vh.txtPrize.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            vh.txtPrize.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

        }


        //holder.imgIcon.setImageResource(listicon[position]);


        return view;
    }

}
