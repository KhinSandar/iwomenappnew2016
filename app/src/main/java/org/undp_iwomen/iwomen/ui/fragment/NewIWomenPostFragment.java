package org.undp_iwomen.iwomen.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.system.ErrnoException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.alexbbb.uploadservice.UploadNotificationConfig;
import com.alexbbb.uploadservice.UploadServiceBroadcastReceiver;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
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
import org.undp_iwomen.iwomen.ui.activity.ImagePickerActivity;
import org.undp_iwomen.iwomen.ui.activity.NewPostActivity;
import org.undp_iwomen.iwomen.ui.activity.PhotoIntentActivity;
import org.undp_iwomen.iwomen.ui.activity.SplashActivity;
import org.undp_iwomen.iwomen.ui.adapter.MediaResultsAdapter;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

public class NewIWomenPostFragment extends Fragment implements View.OnClickListener, ImageChooserListener, ImagePickerCallback {

    public static final String TAG = "New Post";
    private static final int PERMISSION_REQUEST = 10002;

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


    private final String CAMERA_PERMISSION = "android.permission.CAMERA";
    private SharedPreferences mSharedPreferencesUserInfo;
    private String uploadPhoto;
    private Context mContext;


    private final String AUDIO_PERMISSION = "android.permission.RECORD_AUDIO";
    private final String WRITE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String READ_PERMISSIOIN = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PREPARE_AUDIO_PERMISSION = "android.permission.MODIFY_AUDIO_SETTINGS";
    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";

    private static final String CAMERA = Manifest.permission.CAMERA;


    private static final int REQUEST_EXTERNAL_STORAGE_AND_CAMERA_RESULT = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;
    private static final int REQUEST_CAMERA_RESULT = 1;

    Uri fileUri;

    private static final String IMAGE_DIRECTORY_NAME = "Camera Images";

    //New Code for Image
    private String pickerPath;
    private ListView lvResults;
    private List<? extends ChosenFile> files;
    private String choseImageOriginalPath, outPutfileUri, takePhotoUriPath;

    private ZProgressHUD zPDialog;


    public NewIWomenPostFragment() {
    }

    public static NewIWomenPostFragment newInstance() {
        NewIWomenPostFragment fragment = new NewIWomenPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);

        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

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

        //Image
        lvResults = (ListView) rootView.findViewById(R.id.lvResults);

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

        if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG)) {
            et_postDesc.setHint(getResources().getString(R.string.new_iwomen_post_hint_title_mm));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));

            upload_photo_btn.setText(getResources().getString(R.string.audio_upload_photo_mm));
            take_photo_btn.setText(getResources().getString(R.string.aduio_take_photo_mm));
            audio_upload_btn.setText(getResources().getString(R.string.audio_record_btn_txt_mm));
            video_upload_btn.setText(getResources().getString(R.string.audio_vid_file_mm));
            mPostBtn.setText(getResources().getString(R.string.audio_upload_post_mm));


        } else if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG_UNI)) {
            et_postDesc.setHint(getResources().getString(R.string.new_iwomen_post_hint_title_mm));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.UNI));

            upload_photo_btn.setText(getResources().getString(R.string.audio_upload_photo_mm));
            take_photo_btn.setText(getResources().getString(R.string.aduio_take_photo_mm));
            audio_upload_btn.setText(getResources().getString(R.string.audio_record_btn_txt_mm));
            video_upload_btn.setText(getResources().getString(R.string.audio_vid_file_mm));
            mPostBtn.setText(getResources().getString(R.string.audio_upload_post_mm));


        } else if (mstr_lang != null && mstr_lang.equals(Utils.MM_LANG_DEFAULT)) {
            et_postDesc.setHint(getResources().getString(R.string.new_iwomen_post_hint_title_mm));

            upload_photo_btn.setText(getResources().getString(R.string.audio_upload_photo_mm));
            take_photo_btn.setText(getResources().getString(R.string.aduio_take_photo_mm));
            audio_upload_btn.setText(getResources().getString(R.string.audio_record_btn_txt_mm));
            video_upload_btn.setText(getResources().getString(R.string.audio_vid_file_mm));
            mPostBtn.setText(getResources().getString(R.string.audio_upload_post_mm));


        } else {
            et_postDesc.setHint(getResources().getString(R.string.new_iwomen_post_hint_title_eng));
            et_postDesc.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

            upload_photo_btn.setText(getResources().getString(R.string.audio_upload_photo));
            take_photo_btn.setText(getResources().getString(R.string.aduio_take_photo));
            audio_upload_btn.setText(getResources().getString(R.string.audio_record_btn_txt));
            video_upload_btn.setText(getResources().getString(R.string.audio_vid_file));
            mPostBtn.setText(getResources().getString(R.string.audio_upload_post));



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
                //Log.e("iWOmen Post Btn Click", "===>");

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


                if (!hasPermission(CAMERA)) {

                    requestPermissions(new String[]{STORAGE_READ_PERMISSION, CAMERA}, PERMISSION_REQUEST);
                    return;
                }

                /*Intent intent = new Intent(mContext, ImagePickerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);*/
                takePictureNew();

                //takePicture();

                /*if (!hasPermission(CAMERA_PERMISSION)) {

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
                }*/
                break;

            case R.id.new_post_photo_upload_btn:
                //check whether there is permission for READ_STORAGE_PERMISSION
                if (!hasPermission(CAMERA_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {CAMERA_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {
                    pickImageMultiple();

                    //chooseImage();
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

        Log.e("iWOmen Post Btn Click", "checkProcessWhattoDo===>" + choseImageOriginalPath +"/**/"+ takePhotoUriPath);
        //null/**/file:///data/user/0/org.undp_iwomen.iwomen/files/pictures/480469e9-76b8-48ea-a52f-2b5564cb4821.jpeg

        ///storage/emulated/0/DCIM/Camera/IMG_20171002_133333.jpg/**/content://com.android.providers.media.documents/document/image%3A17784
        //Before code is crop_file_path


        if (choseImageOriginalPath != null  ) {

            uploadImage(choseImageOriginalPath);
        } else if(takePhotoUriPath != null ){
            uploadImage(takePhotoUriPath);
        }
        else if (mAudioFilePath != null) {
            String uploadUrl = "http://api.iwomenapp.org/api/v1/file/audioUpload";
            uploadingAudioFile(uploadUrl, mAudioFilePath);
        } else {

            if (et_postDesc.getText().toString().isEmpty() && choseImageOriginalPath == null && takePhotoUriPath == null && mAudioFilePath == null) {//this mean user doesn't choose nothing
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


        Log.e("iWOmen Post Btn Click", "===> take Picture4 " + crop_file_path);

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
        Log.e("iWOmen Post Btn Click", "===> Choose  Image" + crop_file_path);

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


        NetworkEngine.getInstance().postCreateWomenPost(
                0, content,
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
                createPostcallback);

    }

    Callback<IWomenPost> createPostcallback = new Callback<IWomenPost>() {
        @Override
        public void success(IWomenPost iWomenPost, Response response) {
            SKToastMessage.getInstance(getActivity()).showMessage(getActivity(), getResources().getString(R.string.audio_post_success), SKToastMessage.SUCCESS);
            getActivity().finish();

            progress_wheel.setVisibility(View.GONE);

        }

        @Override
        public void failure(RetrofitError error) {
            SKToastMessage.getInstance(getActivity()).showMessage(getActivity(), getResources().getString(R.string.audio_post_error), SKToastMessage.ERROR);

            progress_wheel.setVisibility(View.GONE);

        }

    };


    void performImageCropAfterChose(ChosenImage image) {
        progress_wheel.setVisibility(View.GONE);
        if (image != null) {
            //textViewFile.setText(image.getFilePathOriginal());
            croppedImageFile = new File(image.getFilePathOriginal());
            //Uri croppedImage = Uri.fromFile(croppedImageFile);
            Uri croppedImage = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".org.undp_iwomen.iwomen.provider", croppedImageFile);

            /*CropImageIntentBuilder cropImage = new CropImageIntentBuilder(650, 650, croppedImage);
            cropImage.setSourceImage(croppedImage);
            startActivityForResult(cropImage.getIntent(getActivity().getApplicationContext()), REQUEST_CROP_PICTURE);
*/

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
                Toast.makeText(getActivity().getApplicationContext(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(ChosenImages chosenImages) {

    }

    //@Override
    /*public void onImagesChosen(ChosenImages images) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(images, getActivity());
        lvResults.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(lvResults);

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //doFetching();
                    SKToastMessage.showMessage(getActivity().getApplicationContext(), " permission accepted", SKToastMessage.ERROR);

                } else {
                    SKToastMessage.showMessage(getActivity().getApplicationContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", SKToastMessage.ERROR);
                }
            }
        }
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
        /*Old code crop image after pick photo and camera
        if (resultCode == getActivity().RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
            //startActivityForResult(MediaStoreUtils.getPickImageIntent(getActivity().getApplicationContext()),REQUEST_PICTURE );
        } else {
            progress_wheel.setVisibility(View.GONE);
        }*/
        //Old code crop image after
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
                /*selectedImage.setVisibility(View.VISIBLE);
                selectedImage.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
                selectedImage.setMaxHeight(650);
                crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
                crop_file_path = Uri.fromFile(croppedImageFile).getPath();*/
            }


        }

        if (resultCode == Activity.RESULT_OK) {

            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&

                    getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(Uri.fromFile(croppedImageFile))) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                //mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
                if (!requirePermissions) {

                    //crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
                    //Log.e("iWOmen Post Btn Click", "11111===> Permission" + choseImageOriginalPath + "/" + data.getData());
                    choseImageOriginalPath = getImagePath(data.getData());//Uri.fromFile(croppedImageFile).getPath();
                }

            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);

                }
                cameraPicker.submit(data);

                if (!requirePermissions) {

                    //Log.e("iWOmen Post Btn Click", "11112===> Permission" +"/"+takePhotoUriPath );
                    //choseImageOriginalPath = "/data/user/0/org.undp_iwomen.iwomen/files/pictures/f0fa0a60-2e41-4988-a7ac-1b0f454bd7bd.jpeg";

                    // permission has been granted, continue as usual
                    /*Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                    Uri takeImageUri = Uri.fromFile(file);

                    //crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
                    Log.e("iWOmen Post Btn Click", "11112===> Permission" + file +"//" + takeImageUri.getPath());
                    //storage/emulated/0/MyPhoto.jpg/
                    choseImageOriginalPath = takeImageUri.getPath();//getImagePath(takeImageUri.getPath().toString());//Uri.fromFile(croppedImageFile).getPath();
                    */




                }
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //For Upload Photo
    public String getImagePath(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void uploadImage(String filename) {
        Log.e("iWOmen Post Btn Click", "2222===> upload Image" + filename);
        //content://com.android.providers.media.documents/document/image%3A17530
        progress_wheel.setVisibility(View.VISIBLE);


        File photo = new File(filename);
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("image", new TypedFile("image/png", photo));
        NetworkEngine.getInstance().postUserPhoto(multipartTypedOutput, new Callback<PhotoUpload>() {
            @Override
            public void success(PhotoUpload photo, Response response) {

                if (photo.getResizeUrl().size() > 0) {
                    uploadPhoto = photo.getResizeUrl().get(0);
                    if(choseImageOriginalPath != null){
                        File file = new File(choseImageOriginalPath);
                        if(file.exists())
                            file.delete();
                    }
                    if(takePhotoUriPath != null){
                        File file = new File(takePhotoUriPath);
                        if(file.exists())
                            file.delete();
                    }
                    choseImageOriginalPath = null;
                    takePhotoUriPath = null;
                }

                checkProcessWhattoDo();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("<<<<Fail>>>", "===>" + error.toString());
                progress_wheel.setVisibility(View.INVISIBLE);


                return;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    /**
     * For Image use new Lib for  new version N
     */
    @Override
    public void onImagesChosen(List<com.kbeanie.multipicker.api.entity.ChosenImage> images) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(images, getActivity());
        lvResults.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(lvResults);
        this.files = images;

        takePhotoUriPath = files.get(0).getOriginalPath();

        //Log.e("Edit ImageChoose", "0000===> Permission" + takePhotoUriPath +"/"+ files.get(0).getQueryUri());
        //0000===> Permission/storage/emulated/0/Random/Random Pictures/38b71e97-5f8e-4b88-a5b5-45fed0a9ff43.jpeg/file:///data/user/0/org.undp_iwomen.iwomen/files/pictures/38b71e97-5f8e-4b88-a5b5-45fed0a9ff43.jpeg

    }

    private ImagePicker imagePicker;

    public void pickImageSingle() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.pickImage();
    }

    public void pickImageMultiple() {
        imagePicker = new ImagePicker(this);
        imagePicker.allowMultiple();
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.pickImage();
    }

    private CameraImagePicker cameraPicker;

    public void takePictureNew() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }

    /*@Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }

    //New Code for Image Uri to Upload Uri


}
