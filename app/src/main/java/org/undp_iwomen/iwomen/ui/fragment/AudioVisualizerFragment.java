package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.VisualizerView;

import java.io.IOException;

/**
 * Created by Hein Htet on 15-07-16.
 */

public class AudioVisualizerFragment extends DialogFragment {

    public static final String AUDIO_URL_KEY = "audio_streaming_url";
    public static final String AUDIO_LANG = "audio_lang";

    MediaPlayer mPlayer;
    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;
    private SeekBar mSeekbar;
    private CustomTextView mAudioControl;

    String mstrPlay, mstrPlayMM, mstrLoading, mstrLoadingMM, mstrPause, mstrPauseMM;

    String mAudioUrl, mstr_lang;

    public static DialogFragment newInstance(String audioUrl, String lang) {
        AudioVisualizerFragment audioVisualizerFragment = new AudioVisualizerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AUDIO_URL_KEY, audioUrl);
        bundle.putString(AUDIO_LANG, lang);
        audioVisualizerFragment.setArguments(bundle);

        return audioVisualizerFragment;
    }

    public AudioVisualizerFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        if(bundle == null){
            dismiss();
        }

        mAudioUrl = bundle.getString(AUDIO_URL_KEY);
        mstr_lang = bundle.getString(AUDIO_LANG);
        if(mAudioUrl == null){
            dismiss();
        }

        View view = inflater.inflate(R.layout.audio_visualizer_fragment, container);

        mVisualizerView = (VisualizerView) view.findViewById(R.id.audio_visualizer);
        mSeekbar = (SeekBar) view.findViewById(R.id.audio_seekbar);
        mAudioControl = (CustomTextView) view.findViewById(R.id.audio_control);


        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            mstrPlay = getResources().getString(R.string.audio_play);
            mstrPlayMM = getResources().getString(R.string.audio_play_mm);
            mstrLoading = getResources().getString(R.string.audio_loading);
            mstrLoadingMM = getResources().getString(R.string.audio_loading_mm);
            mstrPause = getResources().getString(R.string.audio_pause);
            mstrPauseMM = getResources().getString(R.string.audio_pause_mm);
        }



        mAudioControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mAudioControl.getText().equals(mstrPlay) || mAudioControl.getText().equals(mstrPlayMM)){
                    if (mPlayer != null) {
                        mPlayer.start();
                        //setupVisualizerFxAndUI();
                        mVisualizer.setEnabled(true);
                    }else{
                        initMediaPlayer();
                    }
                    //mAudioControl.setText("Pause");
                    if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                        mAudioControl.setText(getResources().getString(R.string.audio_pause));//Play
                    }else{
                        mAudioControl.setText(getResources().getString(R.string.audio_pause_mm));//
                    }

                }
                else if(mAudioControl.getText().equals(mstrLoading) ||mAudioControl.getText().equals(mstrLoadingMM)){

                }else{//Pause State
                    if (mPlayer != null) {
                        mPlayer.pause();
                        mVisualizer.setEnabled(false);
                    }
                    if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                        mAudioControl.setText(getResources().getString(R.string.audio_play));//Play
                    }else{
                        mAudioControl.setText(getResources().getString(R.string.audio_play_mm));//
                    }
                }

            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser && mPlayer != null){
                    mPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        initMediaPlayer();

        return view;
    }

    public void initMediaPlayer(){

        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            mAudioControl.setText(mstrLoading);//"Loading..."

        }else{
            mAudioControl.setText(mstrLoadingMM);
        }

        mPlayer = new MediaPlayer();
        // Set type to streaming
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // Listen for if the audio file can't be prepared
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // ... react appropriately ...
                // The MediaPlayer has moved to the Error state, must be reset!
                return false;
            }
        });
        // Attach to when audio file is prepared for playing
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();

                mSeekbar.setMax(mPlayer.getDuration() / 1000);
                mSeekbar.setProgress(0);

                //mAudioControl.setText("Pause");
                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                    mAudioControl.setText(mstrPause);//"Loading..."

                }else{
                    mAudioControl.setText(mstrPauseMM);
                }


                final Handler mHandler = new Handler();

                AudioVisualizerFragment.this.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(mPlayer != null){
                            int mCurrentPosition = mPlayer.getCurrentPosition() / 1000;
                            mSeekbar.setProgress(mCurrentPosition);
                        }

                        mHandler.postDelayed(this, 1000);
                    }
                });

            }
        });


        // Set the data source to the remote URL
        try {
            mPlayer.setDataSource(mAudioUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupVisualizerFxAndUI();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVisualizer.setEnabled(false);
                //mAudioControl.setText("Play");
                if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                    mAudioControl.setText(mstrPlay);//Play
                }else{
                    mAudioControl.setText(mstrPlayMM);//
                }
                if (mPlayer != null) {
                    mVisualizer.release();
                    mPlayer.release();
                    mPlayer = null;
                }
            }
        });

        mVisualizer.setEnabled(true);
        // Trigger an async preparation which will file listener when completed
        mPlayer.prepareAsync();
    }


    private void setupVisualizerFxAndUI() {
        // Create a VisualizerView (defined below), which will render the simplified audio
        // wave form to a Canvas.

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mVisualizer.release();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mPlayer != null) {
            mVisualizer.release();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
