package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.makeramen.RoundedImageView;
import com.smk.model.SisterAppItem;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.AboutUsWebActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

public class SisterAppDetailFragment extends Fragment {


    private static SisterAppItem sisterAppItemMain;
    SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;
    private CustomButton btnDownload;
    private CustomTextView txtAppName;
    private CustomTextView txtAboutApp;
    private RoundedImageView imgApp;
    private ProgressBar progressImgApp;

    public SisterAppDetailFragment() {
    }

    //TODO: replace with your desired data
    public static final String EXTRA_ID = "id";

    //TODO: replace with your desired data
    public static SisterAppDetailFragment newInstance(SisterAppItem sisterAppItem1) {
        SisterAppDetailFragment fragment = new SisterAppDetailFragment();
        Bundle args = new Bundle();

        args.putString("SisterOjb", sisterAppItem1.getId().toString());
        fragment.setArguments(args);

        sisterAppItemMain = sisterAppItem1;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);

        //return inflater.inflate(R.layout.fragment_sister_app_detail, container, false);
        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sister_app_detail, container, false);
        initViews(view);
        // Set the current page index as the View's tag (useful in the PageTransformer)
        // view.setTag(Integer.parseInt(mShareStatus));

        return view;


    }

    private void initViews(View rootView) {

        mContext = getActivity().getApplicationContext();
        btnDownload = (CustomButton) rootView.findViewById(R.id.sister_app_btn_download);
        txtAppName = (CustomTextView) rootView.findViewById(R.id.sister_app_txt_app_name);
        txtAboutApp = (CustomTextView) rootView.findViewById(R.id.sister_app_txt_app_about);
        imgApp = (RoundedImageView) rootView.findViewById(R.id.sister_app_img);
        progressImgApp = (ProgressBar) rootView.findViewById(R.id.sister_app_img_progress_Bar);


        if (sisterAppItemMain.getAppImg() != null && !sisterAppItemMain.getAppImg().isEmpty()) {

            try {

                Picasso.with(mContext)
                        .load(sisterAppItemMain.getAppImg()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(imgApp, new ImageLoadedCallback(progressImgApp) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError() {
                                super.onError();
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
                progressImgApp.setVisibility(View.GONE);
            }
        } else {

            imgApp.setImageResource(R.drawable.blank_profile);
            progressImgApp.setVisibility(View.GONE);
        }


        if (lang.equals(Utils.ENG_LANG)) {

            txtAppName.setText(sisterAppItemMain.getAppName());
            txtAboutApp.setText(sisterAppItemMain.getAppAbout());
            btnDownload.setText(getResources().getString(R.string.download_app));


        } else {
            txtAppName.setText(sisterAppItemMain.getAppNameMM());
            txtAboutApp.setText(sisterAppItemMain.getAppAboutMM());
            btnDownload.setText(getResources().getString(R.string.download_app_mm));

        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sisterAppItemMain.getAppPackageName() != null && !sisterAppItemMain.getAppPackageName().isEmpty()) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + sisterAppItemMain.getAppPackageName())));
                } else {

                    Intent intent2 = new Intent(getActivity().getApplicationContext(), AboutUsWebActivity.class);
                    intent2.putExtra("ActivityName", "PostDetailActivity");
                    intent2.putExtra("URL", sisterAppItemMain.getAppLink());
                    startActivity(intent2);

                }
            }
        });

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
}
