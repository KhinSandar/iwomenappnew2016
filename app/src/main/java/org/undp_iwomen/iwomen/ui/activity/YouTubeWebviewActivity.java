package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.smk.iwomen.BaseActionBarActivity;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.VideoEnabledWebChromeClient;
import org.undp_iwomen.iwomen.ui.widget.VideoEnabledWebView;


public class YouTubeWebviewActivity extends BaseActionBarActivity {
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    private ProgressBar Pbar;
    private String videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Adds Progrss bar Support
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_webview);


        // Makes Progress bar Visible
        this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        Intent i = getIntent();
        videoID = i.getStringExtra("video_id");
        // Save the web view
        webView = (VideoEnabledWebView) findViewById(R.id.webView);
        Pbar = (ProgressBar) findViewById(R.id.wv_progressBar);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments

        // Sets the Chrome Client, and defines the onProgressChanged
        // This makes the Progress bar be updated.
        final Activity MyActivity = this;

        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                MyActivity.setTitle("Loading...");
                MyActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded

                /*// Return the app name after finish loading
                if (progress == 100) {
                    MyActivity.setTitle(R.string.app_name);
                }*/
                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                setValue(progress);

                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                    //txtview.setVisibility(View.VISIBLE);
                }
                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                    MyActivity.setTitle(R.string.app_name);
                    //txtview.setVisibility(View.GONE);
                }


            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);

        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
        //https://youtu.be/YOAu82xu8VY
        webView.loadUrl("http://m.youtube.com/watch?v="+videoID);//https://www.youtube.com/watch?v=lqplGpum-E4

    }

    public void setValue(int progress) {
        Pbar.setProgress(progress);
    }

    @Override
    public void onBackPressed() {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it

        webView.loadUrl("");
        webView.stopLoading();

        finish();
        super.onBackPressed();
        /*if (!webChromeClient.onBackPressed()) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                // Standard back button implementation (for example this could close the app)
                super.onBackPressed();
            }
        }*/
    }
    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

}
