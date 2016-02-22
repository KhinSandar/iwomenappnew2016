package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.makeramen.RoundedImageView;
import org.smk.iwomen.BaseActionBarActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.AuthorItem;
import org.undp_iwomen.iwomen.model.retrofit_api.UserPostAPI;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AuthorDetailActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;
    private CustomTextView txtName;
    private CustomTextView txtBody;
    private RoundedImageView profileImg;
    private ProgressBar profileProgressbar;
    private CustomTextView txtAuthorTitle;

    private ArrayList<AuthorItem> authorItemArrayList;
    private StorageUtil storageUtil;

    private Context mContext;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    private ProgressDialog mProgressDialog;

    String mstrAuthorId, mstrTitleMm, mstrContentEng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail_main);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(AuthorDetailActivity.this);
        mProgressDialog.setCancelable(false);

        authorItemArrayList = new ArrayList<AuthorItem>();
        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        storageUtil = StorageUtil.getInstance(mContext);

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();

        mstrAuthorId = i.getStringExtra("AuthorId");


        txtName = (CustomTextView) findViewById(R.id.authordetail_author_name);
        txtAuthorTitle = (CustomTextView) findViewById(R.id.authordetail_author_title);
        txtBody = (CustomTextView) findViewById(R.id.authordetail_about_author);


        profileImg = (RoundedImageView) findViewById(R.id.authordetail_profilePic_rounded);
        profileProgressbar = (ProgressBar) findViewById(R.id.authordetail_progressBar_profile_item);


        authorItemArrayList = (ArrayList<AuthorItem>) storageUtil.ReadArrayListFromSD("AuthorArrayList" + mstrAuthorId);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);


        if (strLang.equals(Utils.ENG_LANG)) {

            textViewTitle.setText(R.string.author_title_eng);

        } else //FOR ALl MM FONT
        {
            textViewTitle.setText(R.string.author_title_mm);

        }

        clearData();

        if (authorItemArrayList.size() > 0) {

            setAuthorItem(authorItemArrayList);
        } else {
            //FOR OFFLINE Work
            getAuthorDetailByIdFromSever();
        }


        profileProgressbar.setVisibility(View.GONE);


        profileImg.setAdjustViewBounds(true);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);


    }
    private void clearData(){
        txtAuthorTitle.setText("");
        txtBody.setText("");
        txtName.setText("");
    }

    private void getAuthorDetailByIdFromSever() {
        if (Connection.isOnline(mContext)) {

            mProgressDialog.show();
            UserPostAPI.getInstance().getService().getAuhtorDetailById(mstrAuthorId, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {

                        JSONObject whole_object = new JSONObject(s);
                        AuthorItem item = new AuthorItem();

                        authorItemArrayList.clear();
                        if (!whole_object.isNull("authorInfoEng")) {

                            item.setAuthorInfoEng(whole_object.getString("authorInfoEng"));

                        } else {

                            item.setAuthorInfoEng("");
                        }

                        if (!whole_object.isNull("authorInfoMM")) {

                            item.setAuthorInfoMM(whole_object.getString("authorInfoMM"));

                        } else {

                            item.setAuthorInfoMM("");
                        }

                        if (!whole_object.isNull("authorName")) {

                            item.setAuthorName(whole_object.getString("authorName"));

                        } else {

                            item.setAuthorName("");
                        }

                        if (!whole_object.isNull("authorTitleEng")) {

                            item.setAuthorTitleEng(whole_object.getString("authorTitleEng"));

                        } else {

                            item.setAuthorTitleEng("");
                        }

                        if (!whole_object.isNull("authorTitleMM")) {

                            item.setAuthorTitleMM(whole_object.getString("authorTitleMM"));

                        } else {

                            item.setAuthorTitleMM("");
                        }


                        if (!whole_object.isNull("updatedAt")) {

                            item.setUpdatedAt(whole_object.getString("updatedAt"));

                        } else {

                            item.setUpdatedAt("");
                        }

                        //TlgLeaderImg
                        if (!whole_object.isNull("authorImg")) {
                            JSONObject groupLogoObject = whole_object.getJSONObject("authorImg");
                            if (!groupLogoObject.isNull("url")) {
                                item.setAuthorImg(groupLogoObject.getString("url"));

                            } else {
                                item.setAuthorImg("");
                            }


                        } else {
                            item.setAuthorImg("");
                        }

                        if (!whole_object.isNull("createdAt")) {

                            item.setCreatedAt(whole_object.getString("createdAt"));

                        } else {

                            item.setCreatedAt("");
                        }


                        authorItemArrayList.add(item);

                        storageUtil.SaveArrayListToSD("AuthorArrayList" + mstrAuthorId, authorItemArrayList);
                        setAuthorItem(authorItemArrayList);
                        mProgressDialog.dismiss();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressDialog.dismiss();
                }
            });


        } else {

            if (strLang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }

    private void setAuthorItem(ArrayList<AuthorItem> arrayList) {

        AuthorItem item = arrayList.get(0);
        if (strLang.equals(Utils.ENG_LANG)) {

            txtAuthorTitle.setText(item.getAuthorTitleEng());
            txtBody.setText(item.getAuthorInfoEng());

        } else {
            txtAuthorTitle.setText(item.getAuthorTitleMM());
            txtBody.setText(item.getAuthorInfoMM());
        }

        //Common
        txtName.setText(item.getAuthorName());
        if (item.getAuthorImg() != null && item.getAuthorImg() != "") {

            try {

                Picasso.with(mContext)
                        .load(item.getAuthorImg()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            getAuthorDetailByIdFromSever();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
