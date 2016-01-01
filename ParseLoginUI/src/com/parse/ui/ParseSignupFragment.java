/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.parse.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.makeramen.RoundedImageView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.adapter.CitySpinnerAdapter;
import com.parse.model.CityForShow;
import com.parse.model.MyTypeFace;
import com.parse.retrofit_api.TlgProfileAPI;
import com.parse.utils.Connection;
import com.parse.utils.Utils;
import com.parse.utils.ValidatorUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment for the user signup screen.
 */
public class ParseSignupFragment extends ParseLoginFragmentBase implements OnClickListener, ImageChooserListener {

    /**
     * *******My Update Code***********
     */
    private static final String TAG = "ParseSignupFragment";

    public static final String USERNAME = "com.parse.ui.ParseSignupFragment.USERNAME";
    public static final String PASSWORD = "com.parse.ui.ParseSignupFragment.PASSWORD";

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    //private EditText emailField;

    //private EditText addressField;
    private EditText mobileNoForNrcField;
    /*private EditText mobileNoForPassportField;
    private EditText nrcNoField;
    private EditText passportFiled;*/
    private Button createAccountButton;
    private Button editAccountButton;
    private ParseOnLoginSuccessListener onLoginSuccessListener;

    private TextInputLayout mUserNameTextInputLayout;
    //private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;
    /*private TextInputLayout mNrcTextInputLayout;
    private TextInputLayout mPassportTextInputLayout;*/
    private TextInputLayout mMobileNoForNrcTextInputLayout;
    //private TextInputLayout mMobileNoForPassportTextInputLayout;

    private CheckBox mIAgreeCheckBox;
    private CheckBox mIamTLGCheckBox;

    private static boolean isTlgmember= false;
    private TextView mTosTextView;
    private TextView mTlgmemberText;
    private TextView mTownshipLbl;

    private ParseLoginConfig config;
    private int minPasswordLength;

    private static final String LOG_TAG = "ParseSignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private static final String USER_OBJECT_NAME_FIELD = "name";
    /**
     * *******My Update Code***********
     */
    private LoginButton btnLoginWithFacebook;
    private RoundedImageView profile_rounded;
    private ProgressBar register_profilePic_progressBar;
    private Button btnContinue;
    private CallbackManager callbackManager;
    private JSONObject user;

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";

    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE});

    private String user_fb_id, user_fb_email;
    private Context mContext;

    private static final float MAX_TEXTURE_SIZE = 1024f;

    // variable for control doc Type
    //private RadioGroup mDocTypeRadioGroup;

    //private enum DocType {NRC, PASSPORT}

    ;
    //private DocType mDocType = DocType.NRC;
    private EditText mCountryCodeEditText;

    private TextView img_upload_textview;
    SharedPreferences sharePref;

    String lang;

    private Uri photoUri;
    private ScaleAndSetImageTask runningImageTask;
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final String PHOTO_URI_KEY = "photo_uri";
    private static final String TEMP_URI_KEY = "temp_uri";
    private static final String FILE_PREFIX = "scrumptious_img_";
    private static final String FILE_SUFFIX = ".jpg";

    private Uri tempUri = null;
    //private File imageFile;
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

    String mFromCityName;
    private Spinner spnFrom;
    String FromCityID;
    private ProgressDialog mProgressDialog;
    /**
     * *********************************
     */

    public static ParseSignupFragment newInstance(Bundle configOptions, String username, String password) {
        ParseSignupFragment signupFragment = new ParseSignupFragment();
        Bundle args = new Bundle(configOptions);
        args.putString(ParseSignupFragment.USERNAME, username);
        args.putString(ParseSignupFragment.PASSWORD, password);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        /**********My Update Code************/
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        /**********My Update Code************/
        Bundle args = getArguments();
        config = ParseLoginConfig.fromBundle(args, getActivity());

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        if (config.getParseSignupMinPasswordLength() != null) {
            minPasswordLength = config.getParseSignupMinPasswordLength();
        }

        //et_user_name = (EditText)v.findViewById(R.id.signup_username_input);//et_user_name_login


        mContext = getActivity().getApplicationContext();
        //addressField = (EditText) v.findViewById(R.id.signup_address_input);
        //ImageView appLogo = (ImageView) v.findViewById(R.id.app_logo);
        String username = "KHin";//(String) args.getString(USERNAME);
        String password = "12345";//(String) args.getString(PASSWORD);

        View v = inflater.inflate(R.layout.com_parse_ui_parse_signup_fragment,
                parent, false);

        usernameField = (EditText) v.findViewById(R.id.signup_username_input);

        passwordField = (EditText) v.findViewById(R.id.signup_password_input);
        confirmPasswordField = (EditText) v.findViewById(R.id.signup_confirm_password_input);
        mobileNoForNrcField = (EditText) v.findViewById(R.id.signup_mobile_number_for_nrc_input);

        spnFrom = (Spinner) v.findViewById(R.id.signup_spn_township);

        /*emailField = (EditText) v.findViewById(R.id.signup_email_input);
        mobileNoForPassportField = (EditText) v.findViewById(R.id.signup_mobile_number_for_passport_input);
        nrcNoField = (EditText) v.findViewById(R.id.signup_nrc_input);
        passportFiled = (EditText) v.findViewById(R.id.signup_passport_input);*/

        mCountryCodeEditText = (EditText) v.findViewById(R.id.signup_confirm_password_input);

        /*mDocTypeRadioGroup = (RadioGroup) v.findViewById(R.id.signup_doc_type_radio_group);
        mDocTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onDocTypeChanged(checkedId);
            }
        });*/

        mUserNameTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_user_name);
        mPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_password);
        mConfirmPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_confirm_password);

        mMobileNoForNrcTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_mobile_number_for_nrc_input);

        mTosTextView = (TextView) v.findViewById(R.id.terms_and_conditions_textview);
        mTosTextView.setClickable(true);
        mTosTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mTlgmemberText = (TextView)v.findViewById(R.id.singup_tlg_checkbox_textview);
        mTownshipLbl = (TextView)v.findViewById(R.id.signup_lbl_township);


        /*mEmailTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_email_address);
        mMobileNoForPassportTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_mobile_number_for_passport_input);
        mNrcTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_nrc_input);
        mPassportTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_passport_input);*/

        // mNrcTextInputLayout.setError(getResources().getString(R.string.nrc_number_helper));
        createAccountButton = (Button) v.findViewById(R.id.create_account);
        editAccountButton = (Button) v.findViewById(R.id.register_edit_account);

        if (config.getParseSignupSubmitButtonText() != null) {
            createAccountButton.setText(config.getParseSignupSubmitButtonText());
        }
        createAccountButton.setOnClickListener(this);

        mIAgreeCheckBox = (CheckBox) v.findViewById(R.id.i_agree_checkbox);
        mIAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createAccountButton.setEnabled(true);
                } else {
                    createAccountButton.setEnabled(false);
                }
            }
        });

        spnFrom.setEnabled(false);
        mIamTLGCheckBox = (CheckBox)v.findViewById(R.id.singup_tlg_checkbox);
        mIamTLGCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spnFrom.setEnabled(true);
                    isTlgmember = true;
                } else {
                    spnFrom.setEnabled(false);
                    isTlgmember = false;
                }
            }
        });


        btnLoginWithFacebook = (LoginButton) v.findViewById(R.id.facebook_login);

        profile_rounded = (RoundedImageView) v.findViewById(R.id.register_profilePic_rounded);
        register_profilePic_progressBar = (ProgressBar) v.findViewById(R.id.register_profilePic_progressBar);
        register_profilePic_progressBar.setVisibility(View.GONE);
        profile_rounded.setAdjustViewBounds(true);
        profile_rounded.setScaleType(ImageView.ScaleType.CENTER_CROP);

        profile_rounded.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.doToastEng(getActivity().getApplicationContext(),"Coming soon pls !");
                takePicture();

            }
        });

        img_upload_textview = (TextView) v.findViewById(R.id.register_img_upload_textview);

        img_upload_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        //btnLoginWithFacebook.setText("Connect with facebook");
        btnLoginWithFacebook.setReadPermissions("public_profile", "email", "user_friends");
        // If using in a fragment
        btnLoginWithFacebook.setFragment(this);
        btnLoginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login successful Facebook");
                btnLoginWithFacebook.setVisibility(View.GONE);

                updateUI();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login error");
            }
        });

        /******Font Setting*********/
        sharePref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        lang = sharePref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if (lang.equals(Utils.ENG_LANG)) {

            setEnglishFont();
        } else if (lang.equals(Utils.MM_LANG)) {

            setMyanmarFont();
        }
        /******Font Setting*********/

        setFromAdapter();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ParseOnLoginSuccessListener) {
            onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginSuccessListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }


    private void setFromAdapter() {

        final List<String> listName = new ArrayList<String>();
        listName.add("Yangon            " + getResources().getString(R.string.ygn));//Klsa0sQrMw
        listName.add("Mandalay          "+getResources().getString(R.string.mdy));//EGDFFZrIM7
        listName.add("Nay Pyi Taw       "+ getResources().getString(R.string.npt));//A471gH1HNh

        final List<String> listEngName = new ArrayList<String>();
        listEngName.add("Yangon");//Klsa0sQrMw
        listEngName.add("Mandalay");//EGDFFZrIM7
        listEngName.add("Nay Pyi Taw");//A471gH1HNh


        final List<String> list_id = new ArrayList<String>();
        list_id.add("Klsa0sQrMw");//Klsa0sQrMw
        list_id.add("A471gH1HNh");//EGDFFZrIM7
        list_id.add("EGDFFZrIM7");//A471gH1HNh

        final ArrayList<CityForShow> cities = new ArrayList<CityForShow>();


        /*CityForShow ygnCity = new CityForShow("Klsa0sQrMw", "Yangon", getResources().getString(R.string.ygn));
        CityForShow mdyCity = new CityForShow("A471gH1HNh", "Mandalay", getResources().getString(R.string.mdy));
        CityForShow nptCity = new CityForShow("EGDFFZrIM7", "Nay Pyi Taw", getResources().getString(R.string.npt));

        cities.add(ygnCity);
        cities.add(mdyCity);
        cities.add(nptCity);*/


        if(Connection.isOnline(getActivity().getApplicationContext())) {

            mProgressDialog.show();
            TlgProfileAPI.getInstance().getService().getTlgProfileList(new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try{
                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);


                            String _objectId;
                            String _tlg_group_name;
                            String _tlg_group_address;
                            String _tlg_group_address_mm;
                            String _tlg_group_lat_address;
                            String _tlg_group_lng_address;
                            if (each_object.isNull("objectId")) {
                                _objectId = "null";
                            } else {
                                _objectId = each_object.getString("objectId");
                            }

                            if (each_object.isNull("tlg_group_name")) {
                                _tlg_group_name = "null";
                            } else {
                                _tlg_group_name = each_object.getString("tlg_group_name");
                            }

                            if (each_object.isNull("tlg_group_address")) {
                                _tlg_group_address = "null";
                            } else {
                                _tlg_group_address = each_object.getString("tlg_group_address");
                            }
                            if (each_object.isNull("tlg_group_address_mm")) {
                                _tlg_group_address_mm = "null";
                            } else {
                                _tlg_group_address_mm = each_object.getString("tlg_group_address_mm");
                            }

                            if (each_object.isNull("tlg_group_lat_address")) {
                                _tlg_group_lat_address = "null";
                            } else {
                                _tlg_group_lat_address = each_object.getString("tlg_group_lat_address");
                            }

                            if (each_object.isNull("tlg_group_lng_address")) {
                                _tlg_group_lng_address = "null";
                            } else {
                                _tlg_group_lng_address = each_object.getString("tlg_group_lng_address");
                            }

                            cities.add( new CityForShow(_objectId, _tlg_group_address, _tlg_group_address_mm));
                        }
                        mProgressDialog.dismiss();

                        CitySpinnerAdapter adapter = new CitySpinnerAdapter((AppCompatActivity)getActivity(), cities);
                        spnFrom.setAdapter(adapter);

                        /*
                        ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, listName);
                        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnFrom.setAdapter(adapterFrom);
                        */
                        spnFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                FromCityID = cities.get(position).getId();
                                mFromCityName = cities.get(position).getNameInEnglish();

                                //TODO for query in local db
                                //Log.e("Search From", "==" + list_id.get(position));
                                //queryToCity(FromCityID);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressDialog.dismiss();
                }
            });
        }else {
            mProgressDialog.dismiss();

            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            } else {


            }
        }



    }


    /*private void onDocTypeChanged(int checkId) {
        if (checkId == R.id.signup_doc_type_nrc_radio_button) {

            nrcNoField.setVisibility(View.VISIBLE);
            passportFiled.setVisibility(View.GONE);

            mNrcTextInputLayout.setErrorEnabled(false);
            mPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);

            mobileNoForNrcField.setVisibility(View.VISIBLE);
            mobileNoForPassportField.setVisibility(View.GONE);

            mDocType = DocType.NRC;

        } else if (checkId == R.id.signup_doc_type_other_radio_button) {

            nrcNoField.setVisibility(View.GONE);
            passportFiled.setVisibility(View.VISIBLE);

            mobileNoForNrcField.setVisibility(View.GONE);
            mobileNoForPassportField.setVisibility(View.VISIBLE);

            mNrcTextInputLayout.setErrorEnabled(false);
            mPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
            mMobileNoForPassportTextInputLayout.setErrorEnabled(false);

            mDocType = DocType.PASSPORT;
        }
    }*/

    @Override
    public void onClick(View v) {

        //clearError();

        //showToast(et_ph.getText().toString() + et_nrc.getText().toString()+et_address.getText().toString());
        final String username = usernameField.getText().toString().trim();

        final String password = passwordField.getText().toString();
        final String passwordAgain = confirmPasswordField.getText().toString();
        final String mobileNoForNrc = mobileNoForNrcField.getText().toString().trim();

        /*String email = emailField.getText().toString().trim();
        String mobileNoForPassport = mobileNoForPassportField.getText().toString().trim();
        String nrcNo = nrcNoField.getText().toString().trim();
        String passportNo = passportFiled.getText().toString().trim();*/

        if (TextUtils.isEmpty(username)) {
            mUserNameTextInputLayout.setError(getResources().getString(R.string.your_name_error));

            if (lang.equals(Utils.ENG_LANG)) {
                doToast(getResources().getString(R.string.your_name_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.your_name_error_mm));
            }

            return;
        } else {
            mUserNameTextInputLayout.setErrorEnabled(false);
        }

        /*if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout.setError(getResources().getString(R.string.email_address_error));
            doToast(getResources().getString(R.string.email_address_error));
            return;
        } else {
            mEmailTextInputLayout.setErrorEnabled(false);
        }*/

        /*if (TextUtils.isEmpty(password)) {
            mPasswordTextInputLayout.setError(getResources().getString(R.string.password_error));
            doToast(getResources().getString(R.string.passport_error));
            return;
        } else {
            mPasswordTextInputLayout.setErrorEnabled(false);
        }*/

        if (TextUtils.isEmpty(passwordAgain)) {
            mConfirmPasswordTextInputLayout.setError(getResources().getString(R.string.confirm_password_error));

            if (lang.equals(Utils.ENG_LANG)) {
                doToast(getResources().getString(R.string.confirm_password_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.confirm_password_error_mm));
            }
            return;
        } else {
            mConfirmPasswordTextInputLayout.setErrorEnabled(false);
        }

        if (password.length() < minPasswordLength) {

            if (lang.equals(Utils.ENG_LANG)) {

                showToast(getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast,
                        minPasswordLength, minPasswordLength));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast_mm,
                        minPasswordLength, minPasswordLength));
            }
            return;
        }

        if (!password.equals(passwordAgain)) {
            //showToast(R.string.com_parse_ui_mismatch_confirm_password_toast);
            if (lang.equals(Utils.ENG_LANG)) {
                doToast(getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast_mm));
            }
            confirmPasswordField.selectAll();
            confirmPasswordField.requestFocus();
            return;
        }
        boolean inputMobileOk = true;

        if (TextUtils.isEmpty(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.mobile_number_error));
            //doToast(getResources().getString(R.string.mobile_number_error));
            if (lang.equals(Utils.ENG_LANG)) {
                doToast(getResources().getString(R.string.mobile_number_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.mobile_number_error_mm));
            }
            inputMobileOk = false;
            return;
        } else if (!ValidatorUtils.isValidMobileNo(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.invalid_mobile_number));
            //doToast(getResources().getString(R.string.invalid_mobile_number));
            if (lang.equals(Utils.ENG_LANG)) {
                doToast(getResources().getString(R.string.invalid_mobile_number));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.invalid_mobile_number_mm));
            }
            inputMobileOk = false;
            return;
        }
        if (inputMobileOk) {
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
        }


        // prepare mobile number
        /*if (mobileNoForNrc.startsWith("09")) {
            mobileNoForNrc = mobileNoForNrc.substring(2, mobileNoForNrc.length());
        }*/
        //mobileNoForNrc = "+959" + mobileNoForNrc;


        if (crop_file_path != null) {

            File photo = new File(crop_file_path);

            FileInputStream fileInputStream = null;


            byte[] bFile = new byte[(int) photo.length()];

            try {
                //convert file into array of bytes
                fileInputStream = new FileInputStream(photo);
                fileInputStream.read(bFile);
                fileInputStream.close();

                for (int i = 0; i < bFile.length; i++) {
                    //System.out.print((char)bFile[i]);
                }


                //System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Image Upload Sing up", "==>" + e.toString());
            }


            final ParseFile ParsephotoFile = new ParseFile("photo.jpg", bFile);

            //System.out.println("PARSE FILE NAME : " + picturePath);
            ParsephotoFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        //btSignup.setText(e.getMessage());
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {


                    // input validation ok -- do saving
                    final ParseUser user = new ParseUser();

                    // Set standard fields
                    //TODO username and email replace bcoz of avoiding duplicate
                    user.setUsername(username);//username
                    user.setPassword(password);

                    //user.setEmail(email);

                    user.put("username", username);
                    user.put("phoneNo", mobileNoForNrc);
                    //TODO:


                    user.put("user_profile_img", ParsephotoFile);

                    user.put("searchName", username.toLowerCase());

                    //user.put("Address", "");//addressField.getText().toString());

                    if (user_fb_id != null) {
                        user.put("facebookId", user_fb_id);
                    }
                    if (user_fb_email != null) {
                        user.put("email", user_fb_email);
                    }

                    user.put("isTlgTownshipExit",isTlgmember);

                    if(isTlgmember){

                        if(mFromCityName != null) {
                            user.put("tlg_city_address", mFromCityName);
                        }

                    }

                    loadingStart();
                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (isActivityDestroyed()) {
                                return;
                            }

                            if (e == null) {
                                loadingFinish();
                                signupSuccess(user);
                            } else {
                                loadingFinish();
                                if (e != null) {
                                    debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                            e.toString());
                                    switch (e.getCode()) {
                                        case ParseException.INVALID_EMAIL_ADDRESS:
                                            showToast(R.string.com_parse_ui_invalid_email_toast);
                                            break;
                                        case ParseException.USERNAME_TAKEN:
                                            if (lang.equals(Utils.ENG_LANG)) {
                                                showToast(R.string.com_parse_ui_username_taken_toast);
                                            } else if (lang.equals(Utils.MM_LANG)) {

                                                Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_username_taken_toast_mm));
                                            }


                                            break;
                                        case ParseException.EMAIL_TAKEN:
                                            showToast(R.string.com_parse_ui_email_taken_toast);
                                            break;
                                        default:
                                            if (lang.equals(Utils.ENG_LANG)) {
                                                showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                                            } else if (lang.equals(Utils.MM_LANG)) {

                                                Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_signup_failed_unknown_toast_mm));
                                            }

                                    }
                                }
                            }
                        }
                    });
                }
            });
        } else { //TODO End of Cross file path if null case

            // input validation ok -- do saving
            final ParseUser user = new ParseUser();

            // Set standard fields
            //TODO username and email replace bcoz of avoiding duplicate
            user.setUsername(username);//username
            user.setPassword(password);

            //user.setEmail(email);

            user.put("username", username);
            user.put("phoneNo", mobileNoForNrc);



            //mage No selected
            //user.put("user_profile_img", ParsephotoFile);

            user.put("searchName", username.toLowerCase());

            //user.put("Address", "");//addressField.getText().toString());

            if (user_fb_id != null) {
                user.put("facebookId", user_fb_id);
            }
            if (user_fb_email != null && user_fb_email != "") {
                user.put("email", user_fb_email);
            }

            user.put("isTlgTownshipExit",isTlgmember);
            if(isTlgmember){
                if(mFromCityName != null) {
                    user.put("tlg_city_address", mFromCityName);
                }

            }

            loadingStart();
            user.signUpInBackground(new SignUpCallback() {

                @Override
                public void done(ParseException e) {
                    if (isActivityDestroyed()) {
                        return;
                    }

                    if (e == null) {
                        loadingFinish();
                        signupSuccess(user);
                    } else {
                        loadingFinish();
                        if (e != null) {
                            debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                    e.toString());
                            switch (e.getCode()) {
                                case ParseException.INVALID_EMAIL_ADDRESS:
                                    showToast(R.string.com_parse_ui_invalid_email_toast);
                                    break;
                                case ParseException.USERNAME_TAKEN:
                                    if (lang.equals(Utils.ENG_LANG)) {
                                        showToast(R.string.com_parse_ui_username_taken_toast);
                                    } else if (lang.equals(Utils.MM_LANG)) {

                                        Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_username_taken_toast_mm));
                                    }


                                    break;
                                case ParseException.EMAIL_TAKEN:
                                    showToast(R.string.com_parse_ui_email_taken_toast);
                                    break;
                                default:
                                    if (lang.equals(Utils.ENG_LANG)) {
                                        showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                                    } else if (lang.equals(Utils.MM_LANG)) {

                                        Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_signup_failed_unknown_toast_mm));
                                    }

                            }
                        }
                    }
                }
            });


        }


    }

    private File getTempPhotoStagingDirectory() {
        File photoDir = new File(getActivity().getCacheDir(), "photoFiles");
        photoDir.mkdirs();

        return photoDir;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void signupSuccess(ParseUser user) {
        onLoginSuccessListener.onLoginSuccess(user);
    }


    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        //postStatusUpdateButton.setEnabled(enableButtons || canPresentShareDialog);
        //postPhotoButton.setEnabled(enableButtons || canPresentShareDialogWithPhotos);
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            user = me;
                            //updateUI();


                            //Toast.makeText(getApplicationContext(), "ID" + user.optString("id") + "Name" + user.optString("name") + "Email " +user.optString("email"), Toast.LENGTH_LONG).show();

                            if (user.optString("id") != null) {
                                Toast.makeText(getActivity().getApplicationContext(), "Log In Successful." + user.optString("name") + user.optString("email").toString() + user.optString("id"), Toast.LENGTH_LONG).show();

                                usernameField.setText(user.optString("name"));
                                //emailField.setText(user.optString("email").toString());

                                user_fb_email = user.optString("email").toString();
                                user_fb_id = user.optString("id");
                                String fb_path = "https://graph.facebook.com/" + user_fb_id + "/picture";

                                /*Uri uri =  Uri.parse(fb_path  );
                                profile_rounded.setImageURI(uri);*/

                                try {
                                    register_profilePic_progressBar.setVisibility(View.VISIBLE);
                                    Picasso.with(mContext)
                                            .load(fb_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                            .placeholder(R.drawable.camera_icon)
                                            .error(R.drawable.camera_icon)
                                            .into(profile_rounded, new ImageLoadedCallback(register_profilePic_progressBar) {
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
                                Toast.makeText(getActivity().getApplicationContext(), "Please Log In", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            //parameters.putString(FIELDS, REQUEST_FIELDS);
            parameters.putString("fields", "id,name,email,gender, birthday");

            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "accessToken Null Case ", Toast.LENGTH_LONG).show();

            user = null;
        }
    }

    public static boolean isNrcNoValid(String input) {
        Pattern pattern;
        Matcher matcher;
        final String NRC_PATTERN = "[0-9]{1,2}[/]([a-zA-Z]{3}){1,2}\\(N\\)[0-9]{6}"; //"([0-9]{1,2}[/][a-zA-Z]{3}\\(N\\))"; //"([\\u1000-\\u1021]{1,2})([0-9]{6})";
        pattern = Pattern.compile(NRC_PATTERN);
        matcher = pattern.matcher(input.toUpperCase());
        return matcher.matches();
    }

    public static String toCorrectFormat(String input) {
        String stateId = input.substring(input.indexOf('/') + 1, input.indexOf('('));
        String formattedStateId = "";

        if (stateId.length() == 3) {
            return input.toUpperCase();
        } else if (stateId.length() == 6) {
            char first = stateId.charAt(0);
            char second = stateId.charAt(2);
            char third = stateId.charAt(4);

            for (int i = 0; i < stateId.length(); i++) {
                char c = stateId.charAt(i);
                if (i == 0 || i == 2 || i == 4) {
                    formattedStateId += String.valueOf(c).toUpperCase();
                } else {
                    formattedStateId += String.valueOf(c).toLowerCase();
                }
            }
        }

        String formattedNrcNo = input.substring(0, input.indexOf('/') + 1) + formattedStateId + input.substring(input.indexOf('('), input.length());

        return formattedNrcNo;
    }

    private void doToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    public void setEnglishFont() {
        img_upload_textview.setText(getResources().getString(R.string.com_parse_ui_parse_signup_img_upload_label_eng));
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_name_input_hint));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint));
        mMobileNoForNrcTextInputLayout.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint));
        confirmPasswordField.setHint(getResources().getString(R.string.com_parse_ui_confirm_password_input_hint));
        mTosTextView.setText(getResources().getString(R.string.i_agree));
        createAccountButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label_eng));
        mobileNoForNrcField.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint));
        editAccountButton.setText(getResources().getString(R.string.com_parse_ui_edit_account_button_label_eng));

        mTlgmemberText.setText(getResources().getString(R.string.signup_tlg_member_exit_eng));
        mTownshipLbl.setText(getResources().getString(R.string.signup_tlg_township_choose_eng));
        //Set Type Face
        img_upload_textview.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        confirmPasswordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mTosTextView.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        createAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        editAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mTlgmemberText.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mTownshipLbl.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    }

    public void setMyanmarFont() {

        img_upload_textview.setText(getResources().getString(R.string.com_parse_ui_parse_signup_img_upload_label_mm));
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
        mMobileNoForNrcTextInputLayout.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint_mm));

        confirmPasswordField.setHint(getResources().getString(R.string.com_parse_ui_confirm_password_input_hint_mm));
        mobileNoForNrcField.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint_mm));
        createAccountButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label_mm));
        editAccountButton.setText(getResources().getString(R.string.com_parse_ui_edit_account_button_label_mm));
        mTosTextView.setText(getResources().getString(R.string.i_agree_mm));
        mTlgmemberText.setText(getResources().getString(R.string.signup_tlg_member_exit_mm));

        mTownshipLbl.setText(getResources().getString(R.string.signup_tlg_township_choose_mm));
        //Set Type Face
        img_upload_textview.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        confirmPasswordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mTosTextView.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        createAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        editAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mTlgmemberText.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mTownshipLbl.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));



    }

    /*private void showPhotoChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence camera = getResources().getString(R.string.action_photo_camera);
        CharSequence gallery = getResources().getString(R.string.action_photo_gallery);
        builder.setCancelable(true).
                setItems(new CharSequence[]{camera, gallery},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CAMERA) {
                                    startCameraActivity();
                                } else if (i == GALLERY) {
                                    startGalleryActivity();
                                }
                            }
                        });
        builder.show();
    }
    private void setPhotoThumbnail() {
        // The selected image may be too big so scale here
        if (runningImageTask != null &&
                runningImageTask.getStatus() != AsyncTask.Status.FINISHED) {
            runningImageTask.cancel(true);
        }

        runningImageTask = new ScaleAndSetImageTask(photoUri);
        runningImageTask.execute();
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempUri = getTempUri();
        if (tempUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        }
        startActivityForResult(intent, 0);
    }


    private void startGalleryActivity() {
        tempUri = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");
        String selectPicture = getResources().getString(R.string.select_picture);
        startActivityForResult(Intent.createChooser(intent, selectPicture), 0);
    }
    private Uri getTempUri() {
        String imgFileName = FILE_PREFIX + System.currentTimeMillis() + FILE_SUFFIX;

        // Note: on an emulator, you might need to create the "Pictures" directory in
        //         /mnt/sdcard first
        //       % adb shell
        //       % mkdir /mnt/sdcard/Pictures
        File image = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                imgFileName);
        return Uri.fromFile(image);
    }*/


    /* private void setPhotoText() {
         if (photoUri == null) {
             setText2(getResources().getString(R.string.action_photo_default));
         } else {
             setText2(getResources().getString(R.string.action_photo_ready));
         }
     }*/
    //Image Set Up
    private class ScaleAndSetImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri uri;

        public ScaleAndSetImageTask(Uri uri) {
            this.uri = uri;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        FacebookSdk.getApplicationContext().getContentResolver(), uri);
                if (bitmap.getHeight() > MAX_TEXTURE_SIZE || bitmap.getWidth() > MAX_TEXTURE_SIZE) {
                    // We need to scale the image
                    float scale = Math.min(
                            MAX_TEXTURE_SIZE / bitmap.getHeight(),
                            MAX_TEXTURE_SIZE / bitmap.getWidth());
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);
                    bitmap = Bitmap.createBitmap(
                            bitmap,
                            0,
                            0,
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            matrix,
                            false);
                }
                return bitmap;
            } catch (Exception ex) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                profile_rounded.setImageBitmap(result);
            } else {
                // If we fail just try to set the image from the uri
                profile_rounded.setImageURI(photoUri);
            }
        }
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

    //Get File Path
    /*private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }*/
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);


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


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode >= 0 ) {
            //listElements.get(requestCode).onActivityResult(data);
            if (tempUri != null) {
                photoUri = tempUri;
            } else if (data != null) {
                photoUri = data.getData();
            }
            setPhotoThumbnail();



        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        //callbackManager.onActivityResult(requestCode, resultCode, data);

    }*/

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

            //Toast.makeText(getActivity().getApplicationContext(), "File Name & PATH are:" + crop_file_name + "\n" + crop_file_path, Toast.LENGTH_LONG).show();


        }
        //Toast.makeText(getActivity().getApplicationContext(), "Image"+crop_file_name +"Path \n"+ crop_file_path, Toast.LENGTH_SHORT).show();


        callbackManager.onActivityResult(requestCode, resultCode, data);


        /*if(crop_file_path != null ){

            Intent intent = new Intent(MainReportActivity.this, MainPhotoReportActivity.class);
            intent.putExtra("ImageName" ,crop_file_name);
            intent.putExtra("ImagePath",crop_file_path);
            intent.putExtra("ImageAbsolutePath" ,croppedImageFile.getAbsolutePath());


            startActivity(intent);*//*

            *//*MainReportFragment mainReportFragment = new MainReportFragment();
            Bundle b = new Bundle();

            b.putString("ImageName",crop_file_name);
            b.putString("ImagePath",crop_file_path);
            b.putString("ImageAbsolutePath",croppedImageFile.getAbsolutePath());

            mainReportFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainReportFragment)
                    .commit();
        }*/
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


    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


}
