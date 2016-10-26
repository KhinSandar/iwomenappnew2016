package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.undp_iwomen.iwomen.R;
import java.util.ArrayList;

/**
 * Created by Linn on 20-Sep-16.
 */
public class PACTAndTLGListViewAdapter extends BaseAdapter {

    // Declare Variables
    private ArrayList<String> SubResourceItems;
    Context mContext;
    LayoutInflater inflater;
    String mstr_lang;
    public PACTAndTLGListViewAdapter(Context context, ArrayList<String> resourceItems, String typeFaceName) { //
        super();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mstr_lang = typeFaceName;
        this.SubResourceItems = resourceItems;
    }
    @Override
    public int getCount() {
        return SubResourceItems.size();
    }

    @Override
    public Object getItem(int position) {
        return SubResourceItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0 ;
    }

    public static class ViewHolder
    {
        public TextView txtName;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        {
            // TODO Auto-generated method stub
            final ViewHolder holder;

            if(view ==null)
            {holder = new ViewHolder();
                view = inflater.inflate(R.layout.pact_tlg_list, null);
                holder.txtName= (TextView)view.findViewById(R.id.sub_resouce_list_item_title);
                holder.txtName.setText(SubResourceItems.get(position).toString());
                view.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) view.getTag();
            }

            return view;
        }
    }
}
