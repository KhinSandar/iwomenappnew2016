package org.undp_iwomen.iwomen.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.undp_iwomen.iwomen.R;

import java.util.ArrayList;


/**
 * Created by User on 09-Mar-17.
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {
   private ArrayList<String> audioList;
   private String mstr_lang;

    public AudioListAdapter(ArrayList<String> strArrlist, String lang) {
        this.audioList = strArrlist;
        this.mstr_lang = lang;
    }


    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.activity_audio_list_cardview, viewGroup, false);

        return new AudioListAdapter.ViewHolder(itemView);
    }


    @Override

    public void onBindViewHolder(AudioListAdapter.ViewHolder holder, int position) {
        for(int i=0; i<audioList.size();i++){
            holder.txtView.setText("Audio Testing" + audioList.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView imgView;
        protected TextView txtView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.txtImageView);
            txtView = (TextView) itemView.findViewById(R.id.txtTextView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.i("AudioListAdapter","OnClick on RecyclerView ");
            for (int i=0; i<audioList.size();i++){
                Log.i("onClick","Audio ListSize " + audioList.size());
            }

        }
    }




}
