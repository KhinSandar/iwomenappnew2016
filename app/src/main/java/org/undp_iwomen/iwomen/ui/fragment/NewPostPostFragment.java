package org.undp_iwomen.iwomen.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.system.ErrnoException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.alexbbb.uploadservice.UploadNotificationConfig;
import com.alexbbb.uploadservice.UploadServiceBroadcastReceiver;
import com.android.camera.CropImageIntentBuilder;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.smk.skalertmessage.SKToastMessage;
import com.smk.skconnectiondetector.SKConnectionDetector;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.clientapi.NetworkEngine;
import org.smk.model.IWomenPost;
import org.smk.model.PhotoUpload;
import org.undp_iwomen.iwomen.BuildConfig;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;
import org.undp_iwomen.iwomen.ui.activity.TalkTogetherMainActivity;
import org.undp_iwomen.iwomen.ui.widget.CircleProgressBar;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

public class NewPostPostFragment extends Fragment implements View.OnClickListener, ImageChooserListener {

    public static final String TAG = "New Post";
    private static String cateId, cateName;
    public Button mPostBtn;
    public EditText et_postDesc;
    public CustomTextView take_photo_btn, upload_photo_btn, audio_upload_btn, video_upload_btn;
    public ProgressWheel progress_wheel;

    public CustomTextView audio_record_progress_btn;
    public View audio_record_progress_view;
    public CircleProgressBar audio_record_progress_bar;
    public View background_audio_record_process;
    public CustomTextView audio_record_done_btn, audio_record_dismiss_btn;
    public ResizableImageView selectedImage;
    public ProgressWheel selectedImg_progress_wheel;

    public String audio_file_id = null;
    public String postuploadImagePath = null;

    SharedPreferences sharePrefLanguageUtil;
    private String mstr_lang;

    String login_user_name, login_user_id;

    Uri videoUri;

    //For Image Chooser
    private ImageChooserManager imageChooserManager;
    String crop_file_name, crop_file_path = null;
    private int chooserType;
    private String filePath;
    private String capture_filePath;
    File croppedImageFile = null;
    private ChosenImage chosenImage;
    private static int REQUEST_CROP_PICTURE = 2;

    //for audio recording process
    private static String mAudioFilePath = null;
    private MediaPlayer mPlayer = null;
    private int timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    private MediaRecorder mRecorder = null;
    boolean mStartRecording = true;
    boolean isRecording = false;
    CountDownTimer countDownTimer = null;

    //for audio uploading processs
    private ProgressDialog pgDialog;
    private boolean wasAudioRecord = false;


    private SharedPreferences mSharedPreferencesUserInfo;
    private String uploadPhoto;
    private final String CAMERA_PERMISSION = "android.permission.CAMERA";
    private final String AUDIO_PERMISSION = "android.permission.RECORD_AUDIO";
    private final String WRITE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String READ_PERMISSIOIN = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PREPARE_AUDIO_PERMISSION = "android.permission.MODIFY_AUDIO_SETTINGS";
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private ZProgressHUD zPDialog;
    private Context mContext;

    public NewPostPostFragment() {
    }

    public static NewPostPostFragment newInstance(String categoryId, String catName) {
        NewPostPostFragment fragment = new NewPostPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        cateId = categoryId;
        cateName = catName;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);

        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        init(rootView);


        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        login_user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        login_user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);
        postuploadImagePath = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);

        //TODO to update
        //login_user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);


        return rootView;
    }

    public void init(View rootView) {
        mContext = getActivity().getApplicationContext();

        take_photo_btn = (CustomTextView) rootView.findViewById(R.id.new_post_photo_take_btn);
        upload_photo_btn = (CustomTextView) rootView.findViewById(R.id.new_post_photo_upload_btn);
        audio_upload_btn = (CustomTextView) rootView.findViewById(R.id.new_post_audio_upload_btn);
        video_upload_btn = (CustomTextView) rootView.findViewById(R.id.new_post_video_upload_btn);
        et_postDesc = (EditText) rootView.findViewById(R.id.new_post_description_text);
        mPostBtn = (Button) rootView.findViewById(R.id.new_post_upload_btn);
        progress_wheel = (ProgressWheel) rootView.findViewById(R.id.new_post_progress_wheel);
        selectedImage = (ResizableImageView) rootView.findViewById(R.id.new_post_selected_img);
        selectedImg_progress_wheel = (ProgressWheel) rootView.findViewById(R.id.new_post_photo_progress_wheel);

        audio_record_done_btn = (CustomTextView) rootView.findViewById(R.id.audio_record_done_btn);
        audio_record_dismiss_btn = (CustomTextView) rootView.findViewById(R.id.audio_record_dismiss_btn);
        audio_record_progress_btn = (CustomTextView) rootView.findViewById(R.id.audio_record_progress_btn);
        audio_record_progress_view = rootView.findViewById(R.id.audio_record_progress_view);
        audio_record_progress_bar = (CircleProgressBar) rootView.findViewById(R.id.audio_record_progressBar);
        background_audio_record_process = rootView.findViewById(R.id.background_audio_record);

        mPostBtn.setOnClickListener(this);
        take_photo_btn.setOnClickListener(this);
        upload_photo_btn.setOnClickListener(this);
        audio_upload_btn.setOnClickListener(this);
        video_upload_btn.setOnClickListener(this);
        audio_record_done_btn.setOnClickListener(this);
        audio_record_dismiss_btn.setOnClickListener(this);

        audio_record_progress_btn.setOnClickListener(clicker);
        audio_record_progress_view.setOnClickListener(this);
        background_audio_record_process.setOnClickListener(this);

        audio_record_progress_view.setVisibility(View.INVISIBLE);

        /*if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG)) {
            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_mm));
        } else {

            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_eng));
        }*/
        if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG)) {
            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_mm));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
        } else if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG_UNI)) {
            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_mm));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));
        } else if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG_DEFAULT)) {
            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_mm));
        } else {
            et_postDesc.setHint(getResources().getString(R.string.post_body_hint_eng));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
        }


        //setup for progress wheel
        progress_wheel.spin();
        progress_wheel.setRimColor(Color.LTGRAY);
        progress_wheel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_post_upload_btn:

                if (SKConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {

                    zPDialog = new ZProgressHUD(getActivity());
                    zPDialog.setCancelable(false);
                    zPDialog.show();

                    checkProcessWhattoDo();
                } else {
                    SKToastMessage.getInstance(getActivity()).showMessage(getActivity(), "No Internet!", SKToastMessage.ERROR);
                }


                break;

            case R.id.new_post_photo_take_btn:
                if (!hasPermission(CAMERA_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {CAMERA_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {

                    if (!hasPermission(STORAGE_READ_PERMISSION)) {

                        //if no permission, request permission
                        String[] perms = {STORAGE_READ_PERMISSION};

                        int permsRequestCode = 200;


                        requestPermissions(perms, permsRequestCode);
                    } else {
                        takePicture();
                    }
                }
                break;

            case R.id.new_post_photo_upload_btn:
                //check whether there is permission for READ_STORAGE_PERMISSION
                if (!hasPermission(CAMERA_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {CAMERA_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {

                    chooseImage();
                }
                break;

            case R.id.new_post_audio_upload_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!hasPermission(AUDIO_PERMISSION) || !hasPermission(WRITE_PERMISSION) || !hasPermission(READ_PERMISSIOIN) || !hasPermission(PREPARE_AUDIO_PERMISSION)) {
                        String[] perms = {AUDIO_PERMISSION, WRITE_PERMISSION, READ_PERMISSIOIN, PREPARE_AUDIO_PERMISSION};

                        int permsRequestCode = 200;

                        requestPermissions(perms, permsRequestCode);
                    } else {
                        performAudioRecord();
                    }

                    //SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);
                } else {
                    performAudioRecord();
                }
                break;

            case R.id.new_post_video_upload_btn:
                SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);

                break;

            case R.id.audio_record_done_btn:

                if (mAudioFilePath == null || !new File(mAudioFilePath).exists()) {
                    SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.audio_record_warning_1), SKToastMessage.WARNING);
                } else if (isRecording) {
                    SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.audio_record_warning_2), SKToastMessage.WARNING);
                } else {

                    onAudioRecordDoneClick();

                    if (mAudioFilePath == null || !new File(mAudioFilePath).exists()) {
                        audio_upload_btn.setText(R.string.audio_record_btn_txt);
                        audio_upload_btn.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        audio_upload_btn.setText(R.string.audio_record_done_btn_text);
                        audio_upload_btn.setBackgroundColor(getResources().getColor(R.color.primary_light));
                    }
                }

                break;

            case R.id.audio_record_dismiss_btn:

                onAudioRecordDismissClick();

                audio_upload_btn.setText(R.string.audio_record_btn_txt);
                audio_upload_btn.setBackgroundColor(getResources().getColor(R.color.white));

                break;

            case R.id.background_audio_record:

                break;

            default:
                break;
        }
    }

    private void checkProcessWhattoDo() {


        if (crop_file_path != null) {
            uploadImage();
        } else if (mAudioFilePath != null) {
            String uploadUrl = "http://api.iwomenapp.org/api/v1/file/audioUpload";
            uploadingAudioFile(uploadUrl, mAudioFilePath);
        } else {

            if (et_postDesc.getText().toString().isEmpty() && crop_file_path == null && mAudioFilePath == null) {//this mean user doesn't choose nothing
                //progress_wheel.setVisibility(View.GONE);
                if (zPDialog != null && zPDialog.isShowing()) {
                    zPDialog.dismissWithSuccess();
                }
                SKToastMessage.showMessage(getActivity(), getResources().getString(R.string.audio_record_warning_post_something), SKToastMessage.WARNING);
            } else {


                //we will post all data to server, if it's here, we assume all data is ready
                performPostUpload();
            }
        }

    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //progress_wheel.setVisibility(View.VISIBLE);
            capture_filePath = imageChooserManager.choose();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //progress_wheel.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * prepare data to upload and post data to server
     */
    void performPostUpload() {

        progress_wheel.setVisibility(View.VISIBLE);
        /*zPDialog = new ZProgressHUD(getActivity());
        zPDialog.setCancelable(false);
        zPDialog.show();*/


        String content = null, content_type = null, content_mm = null, postUploadName = null;

        if (mstr_lang != null && mstr_lang.equals(Utils.ENG_LANG)) {
            content = et_postDesc.getText().toString();
        } else {
            content_mm = et_postDesc.getText().toString();
        }

        if (login_user_name != null) {
            postUploadName = login_user_name;
        } else {
            postUploadName = "";
        }

        if (audio_file_id != null) { //to check if user upload audio
            content_type = "Audio";
        } else { // user doesn't choose audio nor video
            content_type = "Letter";
        }

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sTodayDate = formatter.format(today);


        NetworkEngine.getInstance().postCreateNewPost(
                1, content,
                content_type,
                content_mm,
                postUploadName,
                null,
                null,
                uploadPhoto,
                sTodayDate,
                login_user_id,
                null,
                audio_file_id,
                postuploadImagePath,
                cateId,
                createPostcallback);

    }

    Callback<IWomenPost> createPostcallback = new Callback<IWomenPost>() {
        @Override
        public void success(IWomenPost iWomenPost, Response response) {
            SKToastMessage.getInstance(getActivity()).showMessage(getActivity(), getResources().getString(R.string.audio_post_success), SKToastMessage.SUCCESS);


            Intent i = new Intent(getActivity().getApplicationContext(), TalkTogetherMainActivity.class);
            i.putExtra("CategoryName", cateName);//CategoryName
            i.putExtra("CategoryID", cateId);//CategoryName
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //Kill this activity after back from TalkTogetherMain
            getActivity().finish();




            progress_wheel.setVisibility(View.GONE);
            if (zPDialog != null && zPDialog.isShowing()) {
                zPDialog.dismissWithSuccess();
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("New Post Post", "===>p" + error.getMessage());
            SKToastMessage.getInstance(getActivity()).showMessage(getActivity(), getResources().getString(R.string.audio_post_error), SKToastMessage.ERROR);
            progress_wheel.setVisibility(View.GONE);
            if (zPDialog != null && zPDialog.isShowing()) {
                zPDialog.dismissWithSuccess();
            }
        }
    };


    void performImageCropAfterChose(ChosenImage image) {
        progress_wheel.setVisibility(View.GONE);
        if (image != null) {
            //textViewFile.setText(image.getFilePathOriginal());
            croppedImageFile = new File(image.getFilePathOriginal());
            Uri croppedImage = Uri.fromFile(croppedImageFile);
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(650, 650, croppedImage);
            cropImage.setSourceImage(croppedImage);
            startActivityForResult(cropImage.getIntent(getActivity().getApplicationContext()), REQUEST_CROP_PICTURE);


            chosenImage = image;


        }
    }

    public void performAudioRecord() {

        if (mAudioFilePath == null || !new File(mAudioFilePath).exists()) {
            mStartRecording = true;
            audio_record_progress_btn.setText(R.string.audio_tap_talk);
            audio_record_progress_view.setVisibility(View.VISIBLE);
        } else {
            mStartRecording = false;
            audio_record_progress_btn.setText(R.string.audio_replay);
            audio_record_progress_view.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onImageChosen(final ChosenImage image) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                performImageCropAfterChose(image);
            }
        });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progress_wheel.setVisibility(View.GONE);
                //Toast.makeText(getActivity().getApplicationContext(), reason,
                //Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(ChosenImages images) {

    }

    /***
     * to check where given permission is already permitted or not
     *
     * @param permission
     * @return
     */
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {

            mPlayer.setDataSource(mAudioFilePath);
            mPlayer.prepare();
            mPlayer.start();

            audio_record_progress_bar.setMax(20);
            timeElapsed = mPlayer.getCurrentPosition();
            audio_record_progress_bar.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateProgressTime, 100);


        } catch (IOException e) {
            Log.e("Audio Record", "prepare() failed");
            e.printStackTrace();
        }
    }

    //handler to change progress bar's time
    private Runnable updateProgressTime = new Runnable() {
        public void run() {

            if (mPlayer != null) {
                //get current position
                timeElapsed = mPlayer.getCurrentPosition();

                //set time remaing
                double timeRemaining = mPlayer.getDuration() - timeElapsed;
                //audio_record_progress_btn.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

                long leftSec = TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining);
                if (leftSec == 0) {
                    audio_record_progress_btn.setText(R.string.audio_replay);
                    onPlay(false);
                } else {
                    audio_record_progress_btn.setText(String.format("%d sec left", leftSec));
                    //repeat yourself that again in 100 miliseconds
                    durationHandler.postDelayed(this, 100);
                }
            }

        }
    };

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mAudioFilePath);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e("Audio Record", "prepare() failed");
            e.printStackTrace();
        }

    }

    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        uploadReceiver.unregister(getActivity());

        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    View.OnClickListener clicker = new View.OnClickListener() {
        public void onClick(View v) {

            //initialize file name
            mAudioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioFilePath += "/iwomenaudiorecord.3gp";

            onRecord(mStartRecording);
            if (mStartRecording) {
                audio_record_progress_btn.setText(R.string.audio_stop);

                countDownTimer = getCountDown();
                if (countDownTimer != null) {
                    countDownTimer.start();
                }

                mStartRecording = !mStartRecording;
                isRecording = true;

            } else {


                if (audio_record_progress_btn.getText().equals("Replay") || audio_record_progress_btn.getText().equals("အသံ\u107Fပန္နားေထာင္မည္")) {
                    audio_record_progress_btn.setText(R.string.audio_stop);
                    onPlay(true);
                } else {
                    audio_record_progress_btn.setText(R.string.audio_replay);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    onPlay(false);
                    stopRecording();
                }
                isRecording = false;
            }
        }
    };

    public CountDownTimer getCountDown() {
        return new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //audio_record_progress_btn.setText("Recording : " + millisUntilFinished / 1000);
                audio_record_progress_bar.setProgress(20 - (int) millisUntilFinished / 1000);
            }

            public void onFinish() {
                audio_record_progress_btn.setText(R.string.audio_replay);
                audio_record_progress_bar.setProgress(audio_record_progress_bar.getProgress() + 1);
                onRecord(false);
            }
        };
    }

    public void onAudioRecordDoneClick() {
        audio_record_progress_view.setVisibility(View.INVISIBLE);
        onPlay(false);
        onRecord(false);


        if (mAudioFilePath != null)
            if (new File(mAudioFilePath).exists()) {
                wasAudioRecord = true;
            }

    }

    public void onAudioRecordDismissClick() {
        audio_record_progress_view.setVisibility(View.INVISIBLE);
        onPlay(false);
        onRecord(false);

        if (mAudioFilePath != null) {
            File file = new File(mAudioFilePath);
            file.delete();
        }
    }

    private void uploadingAudioFile(String url, String filePath) {
        final String serverUrlString = url;
        final String fileToUploadPath = filePath;
        final String uploadID = UUID.randomUUID().toString();
        pgDialog = new ProgressDialog(getActivity());
        pgDialog.setTitle(R.string.audio_file_uploading);
        pgDialog.setCancelable(false);
        pgDialog.setProgress(0);
        pgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pgDialog.show();
        try {
            new MultipartUploadRequest(getActivity(), uploadID, serverUrlString)
                    .addFileToUpload(fileToUploadPath, "uploaded_file")
                    .setNotificationConfig(getNotificationConfig())
                    .setCustomUserAgent("UploadService/" + BuildConfig.VERSION_NAME)
                    .setMaxRetries(2)
                    .startUpload();

            // these are the different exceptions that may be thrown
        } catch (FileNotFoundException exc) {

        } catch (IllegalArgumentException exc) {

        } catch (MalformedURLException exc) {

        }
    }

    private UploadNotificationConfig getNotificationConfig() {
        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setInProgressMessage(getResources().getString(R.string.audio_file_uploading))
                .setCompletedMessage(getResources().getString(R.string.audio_file_success_uploaded))
                .setErrorMessage(getResources().getString(R.string.audio_file_upload_err))
                .setAutoClearOnSuccess(false)
                .setClickIntent(new Intent(getActivity(), DrawerMainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private final UploadServiceBroadcastReceiver uploadReceiver =
            new UploadServiceBroadcastReceiver() {

                @Override
                public void onProgress(String uploadId, int progress) {
                    if (pgDialog != null)
                        pgDialog.setProgress(progress);
                    Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
                }

                @Override
                public void onError(String uploadId, Exception exception) {
                    if (pgDialog != null)
                        pgDialog.dismiss();
                    Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
                            + exception.getLocalizedMessage(), exception);
                }

                @Override
                public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
                    if (pgDialog != null)
                        pgDialog.dismiss();
                    Log.i(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                            + serverResponseMessage);
                    //Toast.makeText(getActivity(), serverResponseMessage, Toast.LENGTH_LONG).show();

                    //audio_file_id = uploadId;
                    audio_file_id = serverResponseMessage.replace("\"", "").replace("\\", "");

                    mAudioFilePath = null;
                    checkProcessWhattoDo();

                }
            };

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
            //startActivityForResult(MediaStoreUtils.getPickImageIntent(getActivity().getApplicationContext()),REQUEST_PICTURE );
        } else {
            progress_wheel.setVisibility(View.GONE);
        }
        if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == getActivity().RESULT_OK)) {
            // When we are done cropping, display it in the ImageView.


            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&

                    getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(Uri.fromFile(croppedImageFile))) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                //mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                //mCropImageView.setImageUriAsync(imageUri);
                selectedImage.setVisibility(View.VISIBLE);
                selectedImage.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
                //img_job.setMaxWidth(300);
                selectedImage.setMaxHeight(650);
                crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
                crop_file_path = Uri.fromFile(croppedImageFile).getPath();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void uploadImage() {
        //progress_wheel.setVisibility(View.VISIBLE);
        File photo = new File(crop_file_path);
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("image", new TypedFile("image/png", photo));
        NetworkEngine.getInstance().postUserPhoto(multipartTypedOutput, new Callback<PhotoUpload>() {
            @Override
            public void success(PhotoUpload photo, Response response) {

                if (photo.getResizeUrl().size() > 0) {
                    uploadPhoto = photo.getResizeUrl().get(0);
                    crop_file_path = null;
                }

                checkProcessWhattoDo();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("<<<<Fail>>>", "===>" + error.toString());

                return;
            }
        });
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getActivity().getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onHomePressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public void onHomePressed() {

            getActivity().finish();

    }*/
}
