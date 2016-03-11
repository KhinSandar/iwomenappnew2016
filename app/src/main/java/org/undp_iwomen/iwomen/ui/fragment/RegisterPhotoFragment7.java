package org.undp_iwomen.iwomen.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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

import org.smk.clientapi.NetworkEngine;
import org.smk.model.PhotoUpload;
import org.smk.model.Sticker;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.ui.adapter.StickerGridViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterPhotoFragment7 extends Fragment implements View.OnClickListener, ImageChooserListener {

    private static final String EXTRA_SAMPLE = "sample";
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    private ProgressDialog mProgressDialog;

    private Button btn_next;
    private ProgressBar register_profilePic_progressBar;
    private RoundedImageView profile_rounded;


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

    private final String STORAGE_READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    boolean storagePermissionAccepted = false;

    //TODO For Avator
    private List<Sticker> stickerArrayList;
    private StickerGridViewAdapter mAdapter;
    private com.pnikosis.materialishprogress.ProgressWheel progress_wheel;
    private GridView gridView;
    private com.pnikosis.materialishprogress.ProgressWheel progress_wheel_gv;
    String sticker_img_path;





    public static RegisterPhotoFragment7 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPhotoFragment7 fragment = new RegisterPhotoFragment7();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterPhotoFragment7 newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPhotoFragment7 fragment = new RegisterPhotoFragment7();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_7_photo, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        btn_next = (Button) view.findViewById(R.id.Next);
        register_profilePic_progressBar = (ProgressBar) view.findViewById(R.id.register_photo_profilePic_pgbar);
        register_profilePic_progressBar.setVisibility(View.GONE);

        profile_rounded = (RoundedImageView) view.findViewById(R.id.register_photo_profilePic);
        profile_rounded.setAdjustViewBounds(true);
        profile_rounded.setScaleType(ImageView.ScaleType.CENTER_CROP);

        profile_rounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.doToastEng(getActivity().getApplicationContext(),"Coming soon pls !");
                //takePicture();

                //check whether there is permission for READ_STORAGE_PERMISSION
                if (!hasPermission(STORAGE_READ_PERMISSION)) {

                    //if no permission, request permission
                    String[] perms = {STORAGE_READ_PERMISSION};

                    int permsRequestCode = 200;

                    requestPermissions(perms, permsRequestCode);

                } else {

                    chooseImage();
                }

            }
        });
        btn_next.setOnClickListener(this);

        gridView = (GridView)view.findViewById(R.id.edit_profile_gv);
        progress_wheel_gv = (com.pnikosis.materialishprogress.ProgressWheel)view.findViewById(R.id.edit_profile_progress_wheel);

        progress_wheel_gv.setVisibility(View.GONE);
        LoadStickerData();

        setEnglishFont();


        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

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
            return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }


    private void addNextFragment(final Button squareBlue, final boolean overlap) {

        Log.e("<<ImagePath>>","==>" + crop_file_path+"/"+ sticker_img_path);
        if (crop_file_path != null) {


            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();//{"isAllow": true}

            /*final File photo = new File(crop_file_path);
            TypedFile typedFile = new TypedFile("multipart/form-data", photo);*///croppedImageFile
            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            multipartTypedOutput.addPart("image", new TypedFile("image/png", new File(crop_file_path)));


            NetworkEngine.getInstance().postUserPhoto(multipartTypedOutput, new Callback<PhotoUpload>() {
                @Override
                public void success(PhotoUpload photoUpload, Response response) {
                    Log.e("<<<<Success>>>", "===>" + photoUpload.getResizeUrl().get(2).toString());

                    //photo_20160205070904_7780379861454656029697.jpg

                    mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                    mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_NAME, photoUpload.getName());

                    mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_URL, photoUpload.getResizeUrl().get(2).toString());
                    mEditorUserInfo.commit();
                    RegisterTermsFragment8 registerTermsFragment8 = RegisterTermsFragment8.newInstance();

                    Slide slideTransition = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        slideTransition = new Slide(Gravity.LEFT);
                        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

                    }

                    ChangeBounds changeBoundsTransition = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        changeBoundsTransition = new ChangeBounds();
                        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

                    }

                    mProgressDialog.dismiss();


                    registerTermsFragment8.setEnterTransition(slideTransition);
                    registerTermsFragment8.setAllowEnterTransitionOverlap(overlap);
                    registerTermsFragment8.setAllowReturnTransitionOverlap(overlap);
                    registerTermsFragment8.setSharedElementEnterTransition(changeBoundsTransition);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, registerTermsFragment8)
                            .addToBackStack(null)
                            .addSharedElement(squareBlue, getString(R.string.register_next))
                            .commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("<<<<Fail>>>", "===>" + error.toString());

                    mProgressDialog.dismiss();
                    return;
                }
            });

        } else if(sticker_img_path != null){

            //TODO showtoast please upload image or choose image
            mEditorUserInfo = mSharedPreferencesUserInfo.edit();
            //mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_NAME, photoUpload.getName());

            mEditorUserInfo.putString(CommonConfig.USER_UPLOAD_IMG_URL, sticker_img_path);
            mEditorUserInfo.commit();
            RegisterTermsFragment8 registerTermsFragment8 = RegisterTermsFragment8.newInstance();

            Slide slideTransition = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                slideTransition = new Slide(Gravity.LEFT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

            }

            ChangeBounds changeBoundsTransition = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                changeBoundsTransition = new ChangeBounds();
                changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

            }

            mProgressDialog.dismiss();


            registerTermsFragment8.setEnterTransition(slideTransition);
            registerTermsFragment8.setAllowEnterTransitionOverlap(overlap);
            registerTermsFragment8.setAllowReturnTransitionOverlap(overlap);
            registerTermsFragment8.setSharedElementEnterTransition(changeBoundsTransition);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, registerTermsFragment8)
                    .addToBackStack(null)
                    .addSharedElement(squareBlue, getString(R.string.register_next))
                    .commit();

        }else{

        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Next:
                addNextFragment(btn_next, false);
                break;
        }

    }

    public void setEnglishFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_photo_title);
    }

    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_photo_title);
    }

    /**
     * *****************Image Chooser***************
     */
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
            //startActivityForResult(MediaStoreUtils.getPickImageIntent(getActivity().getApplicationContext()),REQUEST_PICTURE );
        } else {
            register_profilePic_progressBar.setVisibility(View.GONE);
        }
        if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == getActivity().RESULT_OK)) {
            // When we are done cropping, display it in the ImageView.

            profile_rounded.setVisibility(View.VISIBLE);
            profile_rounded.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
            //img_job.setMaxWidth(300);
            profile_rounded.setMaxHeight(400);
            crop_file_name = Uri.fromFile(croppedImageFile).getLastPathSegment().toString();
            crop_file_path = Uri.fromFile(croppedImageFile).getPath();

            sticker_img_path = null;
            //Toast.makeText(getActivity().getApplicationContext(), "File Name & PATH are:" + crop_file_name + "\n" + crop_file_path, Toast.LENGTH_LONG).show();


        }

    }


    @Override
    public void onImageChosen(final ChosenImage image) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                register_profilePic_progressBar.setVisibility(View.GONE);
                if (image != null) {
                    //textViewFile.setText(image.getFilePathOriginal());
                    croppedImageFile = new File(image.getFilePathOriginal());

                    // When the user is done picking a picture, let's start the CropImage Activity,
                    // setting the output image file and size to 200x200 pixels square.

                    Uri croppedImage = Uri.fromFile(croppedImageFile);
                    CropImageIntentBuilder cropImage = new CropImageIntentBuilder(512, 512, croppedImage);
                    cropImage.setSourceImage(croppedImage);
                    startActivityForResult(cropImage.getIntent(getActivity().getApplicationContext()), REQUEST_CROP_PICTURE);


                    chosenImage = image;


                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                register_profilePic_progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), reason,
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


    public void LoadStickerData() {

        stickerArrayList = new ArrayList<Sticker>();

        Sticker st1 = new Sticker("1","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310055354_2991641011.png\t");
        Sticker st2 = new Sticker("2","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310055524_14832384922.png\t");
        Sticker st3 = new Sticker("3","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310055642_15890413383.png\t");
        Sticker st4 = new Sticker("4","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310055651_16156241574.png\t");
        Sticker st5 = new Sticker("5","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310060004_16379109165.png\t");
        Sticker st6 = new Sticker("6","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310060019_7731226226.png\t");
        Sticker st7 = new Sticker("7","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310060035_1632834377.png\t");
        Sticker st8 = new Sticker("8","http://api.iwomenapp.org/stickers_photo/x400/photo_20160310060053_18858192448.png\t");



        stickerArrayList.add(st1);
        stickerArrayList.add(st2);
        stickerArrayList.add(st3);
        stickerArrayList.add(st4);
        stickerArrayList.add(st5);
        stickerArrayList.add(st6);
        stickerArrayList.add(st7);
        stickerArrayList.add(st8);


        if (mAdapter == null) {
            mAdapter = new StickerGridViewAdapter(getActivity(), mContext, stickerArrayList);

        }

        gridView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {


                sticker_img_path = stickerArrayList.get(position).getStickerImgPath();
                profile_rounded.setVisibility(View.VISIBLE);

                profile_rounded.setMaxHeight(400);
                if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("1")){
                    profile_rounded.setImageResource(R.drawable.ava_1);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("2")){
                    profile_rounded.setImageResource(R.drawable.ava_2);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("3")){
                    profile_rounded.setImageResource(R.drawable.ava_3);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("4")){
                    profile_rounded.setImageResource(R.drawable.ava_4);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("5")){
                    profile_rounded.setImageResource(R.drawable.ava_5);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("6")){
                    profile_rounded.setImageResource(R.drawable.ava_6);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("7")){
                    profile_rounded.setImageResource(R.drawable.ava_7);
                }else if(stickerArrayList.get(position).getStickerName().equalsIgnoreCase("8")){
                    profile_rounded.setImageResource(R.drawable.ava_8);
                }
                crop_file_path = null;
            }
        });

    }


}