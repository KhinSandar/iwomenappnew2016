package com.smk.iwomen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.alexbbb.uploadservice.UploadNotificationConfig;
import com.alexbbb.uploadservice.UploadServiceBroadcastReceiver;
import com.skd.androidrecording.audio.AudioRecordingHandler;
import com.skd.androidrecording.audio.AudioRecordingThread;
import com.skd.androidrecording.visualizer.VisualizerView;
import com.skd.androidrecording.visualizer.renderer.BarGraphRenderer;

import org.undp_iwomen.iwomen.BuildConfig;
import org.undp_iwomen.iwomen.R;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.UUID;

public class AudioRecordingActivity extends AppCompatActivity {

    private VisualizerView visualizerView;
    private String fileName = null;
    private AudioRecordingThread recordingThread;
    private Button btn_recording;
    private boolean startRecording = true;

    private String  TAG = "Iwomen App";
    private final UploadServiceBroadcastReceiver uploadReceiver =
            new UploadServiceBroadcastReceiver() {

                @Override
                public void onProgress(String uploadId, int progress) {
                    if(dialog != null)
                        dialog.setProgress(progress);
                    Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
                }
                @Override
                public void onError(String uploadId, Exception exception) {
                    if(dialog != null)
                        dialog.dismiss();
                    Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
                            + exception.getLocalizedMessage(), exception);
                }

                @Override
                public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
                    if(dialog != null)
                        dialog.dismiss();
                    Log.i(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                            + serverResponseMessage);
                    addForActivityResultData(serverResponseMessage);

                }
            };
    private String USER_AGENT = "UploadService/" + BuildConfig.VERSION_NAME;;
    private ProgressBar progressBar;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fileName = getFileName();

        btn_recording = (Button) findViewById(R.id.btn_audio_recording);
        btn_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

        visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        setupVisualizer();

    }

    private void addForActivityResultData(String data){
        Intent intent = new Intent();
        intent.putExtra("audio_file_name", data);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    public static String getFileName() {
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        return String.format("%s/%s", storageDir, "iwomen_audio_recording.wav");
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
        recordStop();
    }

    @Override
    protected void onDestroy() {
        recordStop();
        releaseVisualizer();

        super.onDestroy();
    }

    private UploadNotificationConfig getNotificationConfig() {
       return new UploadNotificationConfig()
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setInProgressMessage("Your recording audio is uploading.")
                .setCompletedMessage("Your recording audio was successfully uploaded.")
                .setErrorMessage("Your recording audio is error.")
                .setAutoClearOnSuccess(false)
                .setClickIntent(new Intent(this, AudioRecordingActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private void uploadingAudioFile(String url, String filePath){
        final String serverUrlString = url;
        final String fileToUploadPath = filePath;
        final String uploadID = UUID.randomUUID().toString();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Your recording file is uploading");
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        try {
            new MultipartUploadRequest(this, uploadID, serverUrlString)
                    .addFileToUpload(fileToUploadPath, "uploaded_file")
                    .setNotificationConfig(getNotificationConfig())
                    .setCustomUserAgent(USER_AGENT)
                    .setMaxRetries(2)
                    .startUpload();

            // these are the different exceptions that may be thrown
        } catch (FileNotFoundException exc) {

        } catch (IllegalArgumentException exc) {

        } catch (MalformedURLException exc) {

        }
    }

    private void setupVisualizer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(2, paint, false);
        visualizerView.addRenderer(barGraphRendererBottom);
    }

    private void releaseVisualizer() {
        visualizerView.release();
        visualizerView = null;
    }

    private void record() {
        if (startRecording) {
            recordStart();
        }
        else {
            recordStop();
            uploadingAudioFile("http://api.shopyface.com/api-v1/post/audio", fileName);
        }
    }

    private void recordStart() {
        startRecording();
        startRecording = false;
        btn_recording.setText("Stop Recording");
    }

    private void recordStop() {
        stopRecording();
        startRecording = true;
        btn_recording.setText("Ready to Record");
        btn_recording.setEnabled(true);

    }

    private void startRecording() {
        recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() {
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes);
                        }
                    }
                });
            }

            @Override
            public void onRecordSuccess() {}

            @Override
            public void onRecordingError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        recordStop();
                    }
                });
            }

            @Override
            public void onRecordSaveError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        recordStop();
                    }
                });
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread = null;
        }
    }

}
