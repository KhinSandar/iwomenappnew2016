package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.adapter.AudioListAdapter;
import org.undp_iwomen.iwomen.ui.fragment.AudioVisualizerFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by User on 09-Mar-17.
 */
public class AudioListActivity extends BaseActionBarActivity{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String iWomenPostId,lang;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_recycler);

        mRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent i = getIntent();
        iWomenPostId = i.getStringExtra("postId");
        lang = i.getStringExtra("language");

        ArrayList<String> strArrlist = new ArrayList<String>();
        String url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3";
        String audio = "https://testing Audio";

        strArrlist.add(url);
        strArrlist.add(audio);
        mAdapter = new AudioListAdapter(strArrlist,lang);
        mRecyclerView.setAdapter(mAdapter);


    }

}
