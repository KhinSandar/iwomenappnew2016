package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.SisterAppItem;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

import java.util.List;

/**
 * Created by khinsandar on 8/13/15.
 */
public class SisterAppListAdapter extends BaseAdapter {

    List<SisterAppItem> datalist;
    private Context mContext;

    public SisterAppListAdapter(Context context, List<SisterAppItem> dl){
        this.datalist = dl;
        mContext = context;
    }

    @Override
    public int getCount() {
        return this.datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return this.datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if(view == null){
            view= LayoutInflater.from(mContext).inflate(R.layout.sister_apps_list_item, viewGroup, false);

            //LayoutInflater inflater = mActivity.getLayoutInflater();
            //view = inflater.inflate(R.layout.sister_apps_list_item, viewGroup, false);

            vh = new ViewHolder();
            vh.imgApp = (ResizableImageView) view.findViewById(R.id.sister_app_logo_img);
            vh.tv_name = (TextView) view.findViewById(R.id.sister_app_txt_app_name);
            vh.tv_download = (TextView) view.findViewById(R.id.sister_app_txt_download);
            vh.progressBar = (ProgressBar)view.findViewById(R.id.sister_app_logo_img_progress);


            view.setTag(vh);

        }else{
            vh = (ViewHolder) view.getTag();
        }

        vh.tv_name.setText(datalist.get(i).get_app_name());//


        // Feed image
        if (datalist.get(i).get_app_img() != null && !datalist.get(i).get_app_img().isEmpty()) {
            try {

                Picasso.with(mContext)
                        .load(datalist.get(i).get_app_img()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(vh.imgApp, new ImageLoadedCallback(vh.progressBar) {
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
            vh.progressBar.setVisibility(View.GONE);
        }



        vh.tv_download.setClickable(true);
        vh.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utils.doToastEng(mContext,"OnCLickDownload");
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + datalist.get(i).get_app_down_link())));

            }
        });

        return view;
    }

    public class ViewHolder{
        ResizableImageView imgApp;
        TextView tv_name , tv_download;
        ProgressBar progressBar;

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
