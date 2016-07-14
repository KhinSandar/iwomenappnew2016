package org.undp_iwomen.iwomen.ui.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thuongnh.zprogresshud.ZProgressHUD;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverStringConverterAPI;
import org.undp_iwomen.iwomen.ui.activity.WinPrizeActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomEditText;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WinPrizesFragment extends Fragment {

    private static final String POINT = "point";
    private static final String PAGE = "page";
    //private int mPoint;
    private String mShareStatus;
    private Context mContext;
    private CustomButton btnSubmit;
    private TextView txt_Prize1;
    private CustomEditText et_code;
    private TextInputLayout mCodeTextInputLayout;

    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private String user_name, user_obj_id, user_id, user_role, user_ph, register_msg, user_img_path;
    private ZProgressHUD zPDialog;

    private CustomTextView txt_points, txt_prize_thz_link;


    public static WinPrizesFragment newInstance(int point, String status) {
        WinPrizesFragment frag = new WinPrizesFragment();
        Bundle b = new Bundle();
        b.putInt(POINT, point);
        b.putString(PAGE, status);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!getArguments().containsKey(POINT))
            throw new RuntimeException("Fragment must contain a \"" + POINT + "\" argument!");
        mPoint = getArguments().getInt(POINT);*/

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        mShareStatus = getArguments().getString(PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);

        // Select a layout based on the current page
        /*int layoutResId;
        View rootView;
        switch (mShareStatus) {
            case "0":
                layoutResId = R.layout.fragment_win_prize;
                 rootView = inflater.inflate(layoutResId, container, false);
                initViews(rootView);
                break;
            default:
                layoutResId = R.layout.fragment_win_prize_thz;
                rootView = inflater.inflate(layoutResId, container, false);
                initViewsThz(rootView);
                break;

        }*/

        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_win_prize, container, false);
        initViews(view);
        // Set the current page index as the View's tag (useful in the PageTransformer)
        // view.setTag(Integer.parseInt(mShareStatus));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set the background color of the root view to the color specified in newInstance()
        /*View background = view.findViewById(R.id.intro_background);
        background.setBackgroundColor(mBackgroundColor);*/
    }
    private void initViews(View rootView) {

        mContext = getActivity().getApplicationContext();
        btnSubmit = (CustomButton)rootView.findViewById(R.id.win_prize_submit_btn);
        et_code = (CustomEditText) rootView.findViewById(R.id.win_prize_code_input);
        mCodeTextInputLayout = (TextInputLayout)rootView.findViewById(R.id.win_prize_code_input_ly);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,"On Click " + mPoint ,Toast.LENGTH_LONG).show();
                setUserShareCode();


            }
        });



    }


    private void setUserShareCode() {
        if (Connection.isOnline(mContext)) {
            zPDialog = new ZProgressHUD(getActivity());
            zPDialog.show();

            final String mstrCode = et_code.getText().toString().trim();
            if (TextUtils.isEmpty(mstrCode)) {
                mCodeTextInputLayout.setError(getResources().getString(R.string.win_prize_enter_code));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.win_prize_enter_code));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.win_prize_enter_code));
                }
                zPDialog.dismissWithSuccess();


                return;
            } else {
                mCodeTextInputLayout.setErrorEnabled(false);
            }

            SMKserverStringConverterAPI.getInstance().getService().postShareUserObject(Integer.parseInt(user_id), mstrCode, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    zPDialog.dismissWithSuccess();
                    //Log.e("<<<WinPrize Success>>>","===>" + s);

                    if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                        org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.code_share_sucess));
                    } else {

                        org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.code_share_sucess));
                    }

                    mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                    mEditorUserInfo.putString(CommonConfig.USER_SHARE_STATUS, "1");
                    mEditorUserInfo.commit();
                    zPDialog.dismissWithSuccess();

                    Intent i = new Intent(mContext, WinPrizeActivity.class);
                    //i.putExtra("point", "10");
                    i.putExtra("page","1" );
                    startActivity(i);


                }

                @Override
                public void failure(RetrofitError error) {
                    //Utils.doToastEng(mContext, error.toString());

                    //"The share object id has already been taken."
                    //"The share object id has already been taken."
                    Log.e("<<<WinPrize Err>>>","===>" + error.toString());
                    zPDialog.dismissWithSuccess();

                    // TODO Auto-generated method stub
                    if (error.getResponse() != null) {
                        switch (error.getResponse().getStatus()) {
                            case 403:
                                //"The share object id has already been taken."
                                if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                                    org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.code_share_error));
                                } else {

                                    org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.code_share_error));
                                }
                                mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                                mEditorUserInfo.putString(CommonConfig.USER_SHARE_STATUS, "1");
                                mEditorUserInfo.commit();
                                zPDialog.dismissWithSuccess();

                                Intent i = new Intent(mContext, WinPrizeActivity.class);
                                //i.putExtra("point", "10");
                                i.putExtra("page","1" );
                                startActivity(i);
                                break;
                            case 400:
                                //"The selected share object id is invalid."
                                mCodeTextInputLayout.setError(getResources().getString(R.string.win_prize_enter_wrong_code));

                                if (lang.equals(Utils.ENG_LANG)) {
                                    Utils.doToastEng(mContext, getResources().getString(R.string.win_prize_enter_wrong_code));
                                } else if (lang.equals(Utils.MM_LANG)) {
                                    Utils.doToastMM(mContext, getResources().getString(R.string.win_prize_enter_wrong_code));
                                }
                                zPDialog.dismissWithSuccess();


                                break;
                            default:
                                if (error.getCause() != null)
                                    Log.e("Win Prize: ", "Error:" + error.getCause().toString());

                                /*mCodeTextInputLayout.setError(getResources().getString(R.string.win_prize_enter_code));

                                if (lang.equals(Utils.ENG_LANG)) {
                                    Utils.doToastEng(mContext, getResources().getString(R.string.win_prize_enter_code));
                                } else if (lang.equals(Utils.MM_LANG)) {
                                    Utils.doToastMM(mContext, getResources().getString(R.string.win_prize_enter_code));
                                }
                                zPDialog.dismissWithSuccess();*/


                                break;
                        }
                    }




                }
            });


        }
        else {

            if (lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
                org.undp_iwomen.iwomen.utils.Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {
                org.undp_iwomen.iwomen.utils.Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }
    
}
