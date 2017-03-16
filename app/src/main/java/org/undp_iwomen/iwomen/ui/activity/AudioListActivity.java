package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.smk.sklistview.SKListView;

import org.smk.model.IWomenPost;
import org.smk.model.IWomenPostAudios;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.adapter.DataAdapter;
import org.undp_iwomen.iwomen.ui.adapter.StoriesRecentListAdapter;
import org.undp_iwomen.iwomen.ui.fragment.AudioVisualizerFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 09-Mar-17.
 */
public class AudioListActivity extends AppCompatActivity {

    private IWomenPostAudios audioList = new IWomenPostAudios();

    private List<IWomenPostAudios> iWomenPostList;
    private DataAdapter adaAudio;
    private CustomTextView textViewTitle;
    private String language;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list_recycler);
        initViews();
    }

    private void initViews(){
        Intent i = getIntent();
       String id=  i.getStringExtra("postId");
       language =  i.getStringExtra("language");

        SKListView recyclerView = (SKListView)findViewById(R.id.audio_list);

        iWomenPostList = new ArrayList<>();
            for (int count=1; count<=5; count++){
                audioList.setName("TestingAudio");
                audioList.setNameMm("TestingAudioMM");
                audioList.setLinksPath("https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3");
                iWomenPostList.add(audioList);

            }

        adaAudio = new DataAdapter(getApplicationContext(),iWomenPostList,language);
        recyclerView.setAdapter(adaAudio);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(getApplicationContext(),"OnClikc On Audio List",Toast.LENGTH_LONG).show();

                DialogFragment visualizerFragment = AudioVisualizerFragment.newInstance(iWomenPostList.get(position).getLinksPath(), language);
                visualizerFragment.show(getSupportFragmentManager(), "AudioVisualizer");
            }
        });
        if (language.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText("Audio Lists");
        } else {//FOR Default and Custom
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText("အသံဖိုင္မ်ား");
        }

    }
}