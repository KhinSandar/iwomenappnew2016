package org.undp_iwomen.iwomen.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.makeramen.RoundedImageView;
import com.smk.skalertmessage.SKToastMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smk.clientapi.NetworkEngine;
import org.smk.iwomen.BaseActionBarActivity;
import org.smk.model.PhotoUpload;
import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverStringConverterAPI;
import org.undp_iwomen.iwomen.ui.adapter.EditProfileGridviewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.WrappedGridView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;


public class ProfileEditActivity extends BaseActionBarActivity implements ImageChooserListener {


    private CustomTextView textViewTitle;
    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;
    private EditProfileGridviewAdapter mAdapter;
    private ArrayList<String> listShopName;
    private ArrayList<String> listShopImg;
    private WrappedGridView gridView;
    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    private ProgressDialog mProgressDialog;
    String mstrUserId, mstrTitleMm, mstrContentEng;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;

    private String userprofile_Image_path;

    String[] cameraList;

    //For Image Chooser
    private ImageChooserManager imageChooserManager;
    String crop_file_name, crop_file_path;
    private int chooserType;
    private String filePath;
    private String capture_filePath;
    File croppedImageFile = null;
    private ChosenImage chosenImage;

    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;

    //After imagchose
    private CustomButton btn_edit_next;
    private Button btn_edit;
    private Button btn_cancel;
    private ImageView img_camera;

    private final String CAMERA_PERMISSION = "android.permission.CAMERA";
    boolean cameraPermissionAccepted = false;

    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    boolean storagePermissionAccepted = false;


    //Try request code between 1 to 255
    private static final int INITIAL_REQUEST = 1;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 2;
    private static final int READ_EXTERNAL_STORAGE = INITIAL_REQUEST + 3;

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    private com.pnikosis.materialishprogress.ProgressWheel progress_wheel_gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_main);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(ProfileEditActivity.this);
        mProgressDialog.setCancelable(true);

        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        mstrUserId = i.getStringExtra("UserId");

        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);

        profileImg = (RoundedImageView) findViewById(R.id.edit_profile_profilePic_rounded);
        profileProgressbar = (ProgressBar) findViewById(R.id.edit_profile_progressBar_profile_item);
        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        btn_edit_next = (CustomButton) findViewById(R.id.edit_profile_txt_edit_next);
        btn_edit = (Button) findViewById(R.id.edit_profile_btn_save);
        btn_cancel = (Button) findViewById(R.id.edit_profile_btn_cancel);
        img_camera = (ImageView) findViewById(R.id.edit_profile_camera_icon);

        progress_wheel_gv = (com.pnikosis.materialishprogress.ProgressWheel) findViewById(R.id.edit_profile_progress_wheel);

        progress_wheel_gv.setVisibility(View.GONE);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        //TODO invisible in Profile Register
        btn_edit_next.setVisibility(View.VISIBLE);
        btn_cancel.setOnClickListener(clickListener);
        btn_edit.setOnClickListener(clickListener);
        btn_edit_next.setOnClickListener(clickListener);
        img_camera.setOnClickListener(clickListener);
        profileImg.setOnClickListener(clickListener);


        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.edit_profile_eng);
            cameraList = new String[]{getResources().getString(R.string.new_post_take_photo_eng), getResources().getString(R.string.new_post_upload_photo_eng)};


        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.edit_profile_mm);
            cameraList = new String[]{getResources().getString(R.string.new_post_take_photo_mm), getResources().getString(R.string.new_post_upload_photo_mm)};


        }

        userprofile_Image_path = mSharedPreferencesUserInfo.getString(CommonConfig.USER_UPLOAD_IMG_URL, null);
        if (userprofile_Image_path != null && userprofile_Image_path.length() != 0 && userprofile_Image_path.length() < 6) {

            try {

                Picasso.with(getApplicationContext())
                        .load(userprofile_Image_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(profileImg, new ImageLoadedCallback(profileProgressbar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        } else {
            profileProgressbar.setVisibility(View.GONE);
        }

        gridView = (WrappedGridView) findViewById(R.id.edit_profile_gv);
        /**********Ajust Layout Image size depend on screen at Explore ************/
        prepareList();

        View padding = new View(ProfileEditActivity.this);
        padding.setMinimumHeight(20);
        gridView.setExpanded(true);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(mContext, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                //profileImg.setImageResource(listShopImg.get(position));
                profileProgressbar.setVisibility(View.GONE);


                if (listShopImg.get(position) != null && listShopImg.get(position) != "") {
                    try {

                        Picasso.with(getApplicationContext())
                                .load(listShopImg.get(position)) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                .placeholder(R.drawable.blank_profile)
                                .error(R.drawable.blank_profile)
                                .into(profileImg, new ImageLoadedCallback(profileProgressbar) {
                                    @Override
                                    public void onSuccess() {
                                        if (this.progressBar != null) {
                                            this.progressBar.setVisibility(View.GONE);
                                        } else {
                                            this.progressBar.setVisibility(View.VISIBLE);
                                        }
                                    }

                                });
                    } catch (OutOfMemoryError outOfMemoryError) {
                        outOfMemoryError.printStackTrace();
                    }

                }

                //TODO selected Image path in main
                //Log.e("==","");
                userprofile_Image_path = listShopImg.get(position);


            }
        });


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

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;


        }


        return super.onOptionsItemSelected(item);
    }

    private void startDrawerMainActivity() {
        Intent intent = new Intent(this, DrawerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void prepareList() {

        if (Connection.isOnline(mContext)) {
            listShopName = new ArrayList<String>();
            listShopImg = new ArrayList<String>();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();


            SMKserverStringConverterAPI.getInstance().getService().getAllAvator(new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {
                        listShopName.clear();
                        listShopImg.clear();
                        JSONArray whole_body_json_arr = new JSONArray(s);
                        for (int i = 0; i < whole_body_json_arr.length(); i++) {
                            JSONObject each_object = whole_body_json_arr.getJSONObject(i);
                            if (!each_object.isNull("objectId")) {

                                listShopName.add(each_object.getString("objectId"));

                            } else {
                                listShopName.add("");
                            }

                            if (!each_object.isNull("avatorImg")) {

                                listShopImg.add(each_object.getString("avatorImg"));

                            } else {
                                listShopImg.add("");
                            }

                        }
                        mProgressDialog.dismiss();
                        mAdapter = new EditProfileGridviewAdapter(mContext, listShopName, listShopImg);
                        // Set custom adapter to gridview
                        gridView.setExpanded(true);
                        gridView.setAdapter(mAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressDialog.dismiss();
                }
            });

        } else {
            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning));
            } else {
                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }


    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (arg0 == btn_edit) {

                if (Connection.isOnline(mContext)) {


                    if (crop_file_path != null) {
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();
                        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
                        multipartTypedOutput.addPart("image", new TypedFile("image/png", new File(crop_file_path)));


                        NetworkEngine.getInstance().postUserPhoto(multipartTypedOutput, new Callback<PhotoUpload>() {
                            @Override
                            public void success(PhotoUpload photoUpload, Response response) {
                                Log.e("<<<<Success>>>", "===>" + photoUpload.getResizeUrl().get(2).toString());

                                mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                                mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_NAME, photoUpload.getName());

                                mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_URL, photoUpload.getResizeUrl().get(2).toString());
                                mEditorUserInfo.commit();
                                updateUserInfo(photoUpload.getResizeUrl().get(2).toString());

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("<<<<Fail>>>", "===>" + error.toString());

                                mProgressDialog.dismiss();


                                return;
                            }
                        });

                    } else if (userprofile_Image_path != null && !userprofile_Image_path.isEmpty()) {
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();
                        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

                        mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_URL, userprofile_Image_path);
                        mEditorUserInfo.commit();

                        updateUserInfo(userprofile_Image_path);

                    } else {
                        SKToastMessage.showMessage(ProfileEditActivity.this, getResources().getString(R.string.choose_photo_eng), SKToastMessage.ERROR);
                        //startDrawerMainActivity();
                    }


                } else {

                    if (strLang.equals(Utils.ENG_LANG)) {
                        Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
                    } else {

                        Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
                    }
                }
            }
            if (arg0 == btn_cancel) {

                startDrawerMainActivity();
            }
            if (arg0 == btn_edit_next) {
                Intent intent = new Intent(getApplicationContext(), ProfileEditTLGActivity.class);
                intent.putExtra("UserId", mstrUserId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //SKToastMessage.showMessage(ProfileEditActivity.this, getResources().getString(R.string.resource_coming_soon_eng), SKToastMessage.ERROR);

            }

            if (arg0 == img_camera || arg0 == profileImg) {

                showPhotoChoice();

                //TODO Material Dialog
                /*if (strLang.equals(Utils.ENG_LANG)) {
                    new MaterialDialog.Builder(ProfileEditActivity.this)
                            .title(R.string.choose_photo_eng)
                            .items(cameraList)
                            .typeface("roboto-medium.ttf", "roboto-medium.ttf")
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {


                                    if (which == 0) {
                                        takePicture();
                                    } else {
                                        chooseImage();
                                    }
                                    //Utils.doToastEng(mContext, "Choose Comming Soon" + which + text.toString());

                                *//*String phno = "tel:" + text.toString();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phno));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);*//*

                                }
                            })
                            .show();
                } else {
                    new MaterialDialog.Builder(ProfileEditActivity.this)
                            .title(R.string.choose_photo_mm)
                            .items(cameraList)
                            .typeface("zawgyi.ttf", "zawgyi.ttf")

                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                    //Utils.doToastEng(mContext, "Choose Comming Soon" + which + text.toString());

                                    if (which == 0) {
                                        takePicture();
                                    } else {
                                        chooseImage();
                                    }
                                *//*String phno = "tel:" + text.toString();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phno));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);*//*

                                }
                            })
                            .show();

                }*/
            }
        }
    };

    /**
     * *****************Image Chooser***************
     */
    private void updateUserInfo(final String user_img_path){
        if (Connection.isOnline(mContext)) {
            //Log.e("<<UserID,img>>","==>"+ mstrUserId + user_img_path);
            NetworkEngine.getInstance().postUpdateUser(Integer.parseInt(mstrUserId), user_img_path, new Callback<User>() {
                @Override
                public void success(User user, Response response) {

                    if(mProgressDialog != null){
                        mProgressDialog.dismiss();
                    }
                    startDrawerMainActivity();
                    //Log.e("<<updateUserInfo S>>","==>"+ mstrUserId + user_img_path);
                }

                @Override
                public void failure(RetrofitError error) {
                    if(mProgressDialog != null){
                        mProgressDialog.dismiss();
                    }
                    //Log.e("<<updateUserInfo Fail>>","==>"+ mstrUserId + user_img_path);

                }
            });


        }else {

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {
                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void takePicture() {

        try {
            //progress_wheel.setVisibility(View.VISIBLE);
            if (!hasPermission(STORAGE_READ_PERMISSION)) {

                //if no permission, request permission
                String[] perms = {STORAGE_READ_PERMISSION};
                int permsRequestCode = 200;
                requestPermissions(perms, permsRequestCode);

            } else {

                chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
                imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
                imageChooserManager.setImageChooserListener(this);
                capture_filePath = imageChooserManager.choose();
                //chooseImage();
            }


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPhotoChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
        CharSequence camera;
        CharSequence gallery;

        if (strLang.equals(Utils.ENG_LANG)) {
            camera = getResources().getString(R.string.action_photo_camera);
            gallery = getResources().getString(R.string.action_photo_gallery);
        } else {
            camera = getResources().getString(R.string.action_photo_camera_mm);
            gallery = getResources().getString(R.string.action_photo_gallery_mm);
        }
        builder.setCancelable(true).
                setItems(new CharSequence[]{camera, gallery},
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CAMERA) {
                                    if (!hasPermission(CAMERA_PERMISSION)) {
                                        //if no permission, request permission
                                        String[] perms = {CAMERA_PERMISSION};
                                        requestPermissions(perms, CAMERA_REQUEST);

                                    } else {
                                        takePicture();
                                    }

                                } else if (i == GALLERY) {
                                    if (!hasPermission(STORAGE_READ_PERMISSION)) {
                                        //if no permission, request permission
                                        String[] perms = {STORAGE_READ_PERMISSION};
                                        int permsRequestCode = 200;
                                        requestPermissions(perms, permsRequestCode);

                                    } else {
                                        chooseImage();
                                    }
                                }
                            }
                        });
        builder.show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
            //startActivityForResult(MediaStoreUtils.getPickImageIntent(getActivity().getApplicationContext()),REQUEST_PICTURE );
        } else {
            profileProgressbar.setVisibility(View.GONE);
        }
        if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == RESULT_OK)) {

            boolean requirePermissions = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&

                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(Uri.fromFile(croppedImageFile))) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;

                //mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
            if (!requirePermissions) {
                // When we are done cropping, display it in the ImageView.

                profileImg.setVisibility(View.VISIBLE);
                profileImg.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
                //img_job.setMaxWidth(300);
                profileImg.setMaxHeight(400);
                crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
                crop_file_path = Uri.fromFile(croppedImageFile).getPath();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onImageChosen(final ChosenImage image) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                profileProgressbar.setVisibility(View.GONE);
                if (image != null) {
                    //textViewFile.setText(image.getFilePathOriginal());
                    croppedImageFile = new File(image.getFilePathOriginal());

                    // When the user is done picking a picture, let's start the CropImage Activity,
                    // setting the output image file and size to 200x200 pixels square.

                    Uri croppedImage = Uri.fromFile(croppedImageFile);
                    CropImageIntentBuilder cropImage = new CropImageIntentBuilder(512, 512, croppedImage);
                    cropImage.setSourceImage(croppedImage);
                    startActivityForResult(cropImage.getIntent(getApplicationContext()), REQUEST_CROP_PICTURE);


                    chosenImage = image;


                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                profileProgressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(ChosenImages images) {

    }


    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case CAMERA_REQUEST:
                cameraPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraPermissionAccepted) {
                    //TODO camera function
                    takePicture();
                }
                break;

            case 200:
                storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storagePermissionAccepted) {
                    chooseImage();
                }
                break;

        }
    }


    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
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
}
