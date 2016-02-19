package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.utils.Utils;

/**
 * Created by lgvalle on 05/09/15.
 */
public class RegisterLoginFragment1 extends Fragment implements View.OnClickListener {

    private static final String EXTRA_SAMPLE = "sample";

    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;

    private Button btn_next;
    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mMobileNoForNrcTextInputLayout;

    private EditText usernameField;
    private EditText mobileNoForNrcField;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private JSONObject user;
    private String user_fb_id, user_fb_email;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;



    public static RegisterLoginFragment1 newInstance(Sample sample) {

        Bundle args = new Bundle();

        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterLoginFragment1 fragment = new RegisterLoginFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterLoginFragment1 newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterLoginFragment1 fragment = new RegisterLoginFragment1();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_fblogin, container, false);
        //final Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);
        callbackManager = CallbackManager.Factory.create();

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);


        btn_next = (Button) view.findViewById(R.id.Next);
        mUserNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_fb_user_name);
        mMobileNoForNrcTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_fb_phone_number);
        usernameField = (EditText) view.findViewById(R.id.register_fb_username_input);
        mobileNoForNrcField = (EditText) view.findViewById(R.id.register_fb_phone_number_input);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                updateUI();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });

        btn_next.setOnClickListener(this);


        setEnglishFont();
        /*if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            setEnglishFont();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            setMyanmarFont();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            setMyanmarFontUni();
        } else if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            setMyanmarFontDefault();
        }
*/

        return view;
    }


    private void addNextFragment(Button squareBlue, boolean overlap) {


        final String username = usernameField.getText().toString().trim();
        final String mobileNoForNrc = mobileNoForNrcField.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mUserNameTextInputLayout.setError(getResources().getString(R.string.your_name_error));

            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.your_name_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.your_name_error_mm));
            }

            return;
        } else {
            mUserNameTextInputLayout.setErrorEnabled(false);
        }

        boolean inputMobileOk = true;

        if (TextUtils.isEmpty(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.mobile_number_error));
            //doToast(getResources().getString(R.string.mobile_number_error));
            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.mobile_number_error));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.mobile_number_error_mm));
            }
            inputMobileOk = false;
            return;
        }
        /*else if (!ValidatorUtils.isValidMobileNo(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.invalid_mobile_number));
            //doToast(getResources().getString(R.string.invalid_mobile_number));
            if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.invalid_mobile_number));
            } else if (lang.equals(Utils.MM_LANG)) {

                Utils.doToastMM(mContext, getResources().getString(R.string.invalid_mobile_number_mm));
            }
            inputMobileOk = false;
            return;
        }*/
        if (inputMobileOk) {
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
        }


        mEditorUserInfo = mSharedPreferencesUserInfo.edit();

        mEditorUserInfo.putString(CommonConfig.USER_NAME, username);
        mEditorUserInfo.putString(CommonConfig.USER_PH, mobileNoForNrc);

        if (user_fb_email != null && user_fb_email != "") {
            mEditorUserInfo.putString(CommonConfig.USER_EMAIL, user_fb_email);
        }
        if (user_fb_id != null && user_fb_id != "") {
            mEditorUserInfo.putString(CommonConfig.USER_FBID, user_fb_id);
        }
        mEditorUserInfo.commit();


        RegisterPwdFragment2 registerPwdFragment2 = RegisterPwdFragment2.newInstance();

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

        registerPwdFragment2.setEnterTransition(slideTransition);
        registerPwdFragment2.setAllowEnterTransitionOverlap(overlap);
        registerPwdFragment2.setAllowReturnTransitionOverlap(overlap);
        registerPwdFragment2.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, registerPwdFragment2)
                .addToBackStack(null)
                .addSharedElement(squareBlue, getString(R.string.register_next))
                .commit();
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
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);

        //((RegisterMainActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        /*usernameField.setHint(getResources().getString(R.string.register_name_hint));
        mobileNoForNrcField.setHint(getResources().getString(R.string.register_ph_hint));


        //Set Type Face

        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));*/


    }

    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_mm);
        ((RegisterMainActivity) getActivity()).textViewTitle.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));

       /* usernameField.setHint(getResources().getString(R.string.register_name_hint));
        mobileNoForNrcField.setHint(getResources().getString(R.string.register_ph_hint));

        //Set Type Face
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mobileNoForNrcField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));*/


    }

    public void setMyanmarFontUni() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);
    }

    public void setMyanmarFontDefault() {
        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

                                usernameField.setText(user.optString("name"));
                                //emailField.setText(user.optString("email").toString());

                                user_fb_email = user.optString("email").toString();
                                user_fb_id = user.optString("id");

                                //Toast.makeText(getActivity().getApplicationContext(), "Log In Successful." + user.optString("name") + user.optString("email").toString() + user.optString("id"), Toast.LENGTH_LONG).show();

                                /*Uri uri =  Uri.parse(fb_path  );
                                profile_rounded.setImageURI(uri);*/

                                /*
                                String fb_path = "https://graph.facebook.com/" + user_fb_id + "/picture";
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
                                }*/


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


}
