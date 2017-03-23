package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.felipecsl.gifimageview.library.GifImageView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.iwomen.TakeAndTourActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by khinsandar on 8/28/15.
 */
public class Register_Congrat_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private Context mContext;
    WebView webviewActionView;
    Button btnEnjoyApp;

    private GifImageView gifImageView;
    private ZProgressHUD zPDialog;
    private Button btnTakeATour;

    private CustomTextView txtlbl1, txtlbl2, txtlbl3;


    public static Register_Congrat_Fragment newInstance() {

        Bundle args = new Bundle();

        //args.putSerializable(EXTRA_SAMPLE, sample);
        Register_Congrat_Fragment fragment = new Register_Congrat_Fragment();
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

        View view = inflater.inflate(R.layout.activity_register_congrat, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();

        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        gifImageView = (GifImageView) rootView.findViewById(R.id.register_gifImageView);

        btnEnjoyApp = (Button) rootView.findViewById(R.id.Next);
        btnTakeATour = (Button) rootView.findViewById(R.id.register_take_tour);

        txtlbl1 = (CustomTextView) rootView.findViewById(R.id.register_congrat_head_txt);
        txtlbl2 = (CustomTextView) rootView.findViewById(R.id.register_congrat_head_txt1);
        txtlbl3 = (CustomTextView) rootView.findViewById(R.id.register_congrat_head_txt2);


        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            setEnglishFont();
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG)) {
            setMyanmarFont();
        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_UNI)) {
            setMyanmarFont();

        } else if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.MM_LANG_DEFAULT)) {
            setMyanmarFont();

        }

        btnTakeATour.setOnClickListener(this);
        btnEnjoyApp.setOnClickListener(this);


        try {
            //animation.gif is just an example, use the name of your file
            //that is inside the assets folder.

            InputStream is = getActivity().getAssets().open("congratulations.gif");
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();


            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Next:
                zPDialog = new ZProgressHUD(getActivity());
                zPDialog.show();

                zPDialog.dismissWithSuccess();
                Intent i = new Intent(getActivity(), DrawerMainActivity.class);//DrawerMainActivity
                startActivity(i);
                getActivity().finish();
                break;
            case R.id.register_take_tour:
                startActivity(new Intent(getActivity(), TakeAndTourActivity.class));
                break;

        }
    }

    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.menu_post_news, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {


            return false;

        }

        return super.onOptionsItemSelected(item);
    }

    public void setEnglishFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_congrat_title);

        txtlbl1.setText(getResources().getString(R.string.register_congrat_eng1));
        txtlbl2.setText(getResources().getString(R.string.register_congrat_eng2));
        txtlbl3.setText(getResources().getString(R.string.register_congrat_eng3));

        btnEnjoyApp.setText(getResources().getString(R.string.register_enjoy));
        btnTakeATour.setText(getResources().getString(R.string.register_tour));
    }

    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_congrat_eng1_mm);

        txtlbl1.setText(getResources().getString(R.string.register_congrat_eng1_mm));
        txtlbl2.setText(getResources().getString(R.string.register_congrat_eng2_mm));
        txtlbl3.setText(getResources().getString(R.string.register_congrat_eng3_mm));

        btnEnjoyApp.setText(getResources().getString(R.string.register_enjoy_mm));
        btnTakeATour.setText(getResources().getString(R.string.register_tour_mm));
    }
}
