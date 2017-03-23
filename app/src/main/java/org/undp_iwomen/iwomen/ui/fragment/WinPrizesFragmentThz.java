package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.model.User;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.PrizePointsItem;
import org.undp_iwomen.iwomen.model.retrofit_api.SMKserverAPI;
import org.undp_iwomen.iwomen.ui.adapter.WinPrizeListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WinPrizesFragmentThz extends Fragment {

    private static final String POINT = "point";
    private static final String PAGE = "page";

    //private int mPoint;
    private String mShareStatus;
    private Context mContext;


    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences.Editor mEditorUserInfo;
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private String user_name, user_obj_id, user_id, user_role, user_ph, register_msg, user_img_path;
    private ZProgressHUD zPDialog;

    private CustomTextView txt_points, txt_prize_thz_link,txt_prize_thz_link_fb;
    private ListView lv_prize_points;
    private WinPrizeListViewAdapter adapter;
    private CustomButton btn_share;



    public static WinPrizesFragmentThz newInstance(int point, String status) {
        WinPrizesFragmentThz frag = new WinPrizesFragmentThz();
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

        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);
        user_obj_id= mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);


        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_win_prize_thz, container, false);
        initViewsThz(view);
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


    private void initViewsThz(View rootView) {

        mContext = getActivity().getApplicationContext();
        txt_points = (CustomTextView) rootView.findViewById(R.id.win_prize_points);
        txt_prize_thz_link = (CustomTextView) rootView.findViewById(R.id.win_prize_thz_link);
        txt_prize_thz_link_fb = (CustomTextView) rootView.findViewById(R.id.win_prize_share_link);
        btn_share = (CustomButton)rootView.findViewById(R.id.win_prize_thz_share_btn);
        lv_prize_points = (ListView)rootView.findViewById(R.id.win_prize_lv);


        txt_prize_thz_link_fb.setText("https://www.facebook.com/iwomenApp");
        Linkify.addLinks(txt_prize_thz_link_fb, Linkify.WEB_URLS);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });
        getUserPointsCount();

    }


    private void getUserPointsCount() {
        if (Connection.isOnline(mContext)) {
            Activity activity = getActivity();
            if(activity != null) {
                SMKserverAPI.getInstance().getService().getUserPoinsByID(user_id, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        Activity activity = getActivity();
                        if (activity != null) {

                            // etc ...
                            if (user.getPoints() != null) {
                                txt_points.setText(user.getPoints().toString() + getResources().getString(R.string.points_prize));

                                mEditorUserInfo = mSharedPreferencesUserInfo.edit();
                                mEditorUserInfo.putString(CommonConfig.USER_POINTS, user.getPoints().toString());
                                mEditorUserInfo.commit();
                            } else {
                                //txt_points.setText("");
                            }

                            getPrizePointsCount();

                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        } else {

            /*if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {
                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }*/
        }
    }

    //Share URL
    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        /*share.putExtra(Intent.EXTRA_SUBJECT, mPostTile.getText().toString());//Title Of The Post
        if (post_content.getText().length() > 20) {
            share_data = post_content.getText().toString().substring(0, 12) + " ...";
        } else {
            share_data = post_content.getText().toString();
        }*/

        share.putExtra(Intent.EXTRA_TEXT, user_obj_id);
        share.putExtra(Intent.EXTRA_HTML_TEXT, user_obj_id);
        startActivity(Intent.createChooser(share, "Your Code Share link!"));
    }

    private void getPrizePointsCount() {
        if (Connection.isOnline(mContext)) {

            SMKserverAPI.getInstance().getService().getPrizePoints(new Callback<List<PrizePointsItem>>() {
                @Override
                public void success(List<PrizePointsItem> prizePointsItems, Response response) {
                    adapter = new WinPrizeListViewAdapter(mContext, prizePointsItems, lang);
                    adapter.notifyDataSetChanged();


                    View padding = new View(getActivity().getApplicationContext());
                    padding.setMinimumHeight(20);
                    lv_prize_points.addFooterView(padding);
                    lv_prize_points.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(lv_prize_points);
                    //Helper.getListViewSize(lv_prize_points);

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else {

            /*if (lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }*/
        }
    }

    /**
     * **************** ListView WIthin ScrollView Step1 ********************
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



}
