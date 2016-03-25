package org.undp_iwomen.iwomen.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;


public class MainActivity extends BaseActionBarActivity {

    private MediaPlayer myMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*setVolumeControlStream(AudioManager.STREAM_ALARM);
        if ( myMediaPlayer == null)
        {
            myMediaPlayer= MediaPlayer.create(MainActivity.this, R.raw.wai_wai_audio);
            myMediaPlayer.setVolume(1.0f, 1.0f);
            myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer arg0) {
                    Log.i("PLAYER", "Prepare completed");
                    myMediaPlayer.start();
                }
            });
            return;
        }
        // Or just play
        if (myMediaPlayer.isPlaying()) {
            myMediaPlayer.pause();
        } else {
            myMediaPlayer.seekTo(0);
            ;
            myMediaPlayer.start();
        }*/
        /***Second coding***/
        /*setVolumeControlStream(AudioManager.STREAM_ALARM);

        MediaPlayer mediaPlayer;
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.setOnCompletionListener(this);
        } catch(Exception ex){
            throw new RuntimeException("Couldn't load music");
        }

        public void pause() {
            mediaPlayer.pause();
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void dispose() {
            if(mediaPlayer.isPlaying()){
                stop();
            }
            mediaPlayer.release();
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
