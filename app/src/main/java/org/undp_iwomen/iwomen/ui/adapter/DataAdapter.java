package org.undp_iwomen.iwomen.ui.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.smk.model.IWomenPostAudios;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends BaseAdapter {
    private List<IWomenPostAudios> audioList;
    private final LayoutInflater mInflater;
    private Context mContext;
    private String mstr_lang;
    private ArrayList<IWomenPostAudios> arraylist;




    public DataAdapter(Context applicationContext, List<IWomenPostAudios> audioList, String language) {
        mInflater = LayoutInflater.from(applicationContext);
        this.mstr_lang = language;
        this.mContext= applicationContext;
        this.audioList = audioList;
        this.arraylist = new ArrayList<IWomenPostAudios>();
        this.arraylist.addAll(arraylist);
    }


    @Override
    public int getCount() {
        return audioList.size();
    }

    @Override
    public Object getItem(int position) {
        return audioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new DataAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_audio_list_cardview, null);

            holder.imgAudoImage = (ImageView) convertView.findViewById(R.id.social_audio);
            holder.txtAudioName = (TextView) convertView.findViewById(R.id.tv_aduiotext);

            convertView.setTag(holder);
        } else {
            holder = (DataAdapter.ViewHolder) convertView.getTag();
        }

        if (mstr_lang.equals(Utils.ENG_LANG)) {
            holder.txtAudioName.setText(audioList.get(position).getName());
            holder.txtAudioName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));


        }else if (mstr_lang.equals(Utils.MM_LANG)) {
            holder.txtAudioName.setText(audioList.get(position).getNameMm());
            holder.txtAudioName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));



        }else if (mstr_lang.equals(Utils.MM_LANG_UNI)) {
            holder.txtAudioName.setText(audioList.get(position).getNameMm());

            holder.txtAudioName.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));



        }else {//FOR Default and Custom
            holder.txtAudioName.setText(audioList.get(position).getNameMm());

        }
        return convertView;
    }

    public static class ViewHolder
    {
        public TextView txtAudioName;
        public ImageView imgAudoImage;


    }

}