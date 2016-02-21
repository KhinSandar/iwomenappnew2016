package org.undp_iwomen.iwomen.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.smk.iwomen.BaseActionBarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.SubResourceItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.SubResourceAPI;
import org.undp_iwomen.iwomen.ui.adapter.SubResourceListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SubResourceListActivity extends BaseActionBarActivity {


    private CustomTextView textViewTitle;
    private ListView lv;
    private Context mContext;

    private String[] ListName;
    private String[] ListAuthorName;
    private String[] ListDate;
    private String[] ListDataText;
    private int[] ListIcon;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private StorageUtil storageUtil;
    private ArrayList<SubResourceItem> SubResourceItems;

    SubResourceListViewAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private String mResourceId;

    private String mTitleEng, mTitleMM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_resource_list);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(SubResourceListActivity.this);
        mProgressDialog.setCancelable(false);
        mContext = getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        mResourceId = i.getStringExtra("ResourceId");
        mTitleEng = i.getStringExtra("TitleEng");
        mTitleMM = i.getStringExtra("TitleMM");


        lv = (ListView) findViewById(R.id.sub_resource_list);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);


        SubResourceItems = (ArrayList<SubResourceItem>) storageUtil.ReadArrayListFromSD("SubResourceArrayList" + mResourceId);

        //Log.e("Sub ResourceItems size", "===>" + SubResourceItems.size());

        if (SubResourceItems.size() > 0) {
            mAdapter = new SubResourceListViewAdapter(getApplicationContext(), SubResourceItems, mstr_lang);
            lv.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            getSubResourceDataFromSever(mResourceId);
        }

        if (mstr_lang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText(mTitleEng);
        } else {//FOR Default and Custom
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText(mTitleMM);
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ResourceDetailActivity.class);
                intent.putExtra("TitleEng",mTitleEng);//SubResourceItems.get(i).getSub_resource_title_mm()
                intent.putExtra("TitleMM", mTitleMM);//mCatNames.get((Integer)view.getTag()).toString()


                intent.putExtra("ContentEng", SubResourceItems.get(i).getSub_resouce_content_eng());
                intent.putExtra("ContentMM", SubResourceItems.get(i).getSub_resouce_content_mm());
                intent.putExtra("AuthorName", SubResourceItems.get(i).getAuthorName());
                intent.putExtra("AuthorId", SubResourceItems.get(i).getAuthor_id());
                intent.putExtra("AuthorImgPath", SubResourceItems.get(i).getAuthor_img_url());
                intent.putExtra("PostDate", SubResourceItems.get(i).getPosted_date());
                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


    }

    private void getSubResourceDataFromSever(String id) {
        if (Connection.isOnline(mContext)) {

            mProgressDialog.show();
            String sCondition = "{\"isAllow\": true}";
            SubResourceAPI.getInstance().getService().getSubResourceByResourceId(sCondition,"{\"resource_id\":{\"__type\":\"Pointer\",\"className\":\"Resources\",\"objectId\":\"" + id + "\"}}", "createdAt", new Callback<String>() {
                @Override
                public void success(String s, Response response) {

                    try {

                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");

                        String authorName;
                        String author_id;
                        String icon_img_url;
                        String author_img_url;

                        String posted_date;
                        String sub_resouce_content_eng;
                        String sub_resouce_content_mm;
                        String sub_resource_title_eng;
                        String sub_resource_title_mm;
                        SubResourceItems.clear();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);

                            if (!each_object.isNull("authorName")) {

                                authorName = each_object.getString("authorName");

                            } else {
                                authorName = "";
                            }
                            if (!each_object.isNull("author_id")) {

                                JSONObject ObjjsonObject = each_object.getJSONObject("author_id");
                                if (!ObjjsonObject.isNull("objectId")) {

                                    author_id = ObjjsonObject.getString("objectId");
                                } else {
                                    author_id = "";
                                }


                            } else {
                                author_id = "";
                            }
                            if (!each_object.isNull("sub_res_icon_img_url")) {

                                icon_img_url = each_object.getString("sub_res_icon_img_url");

                            } else {
                                icon_img_url = "";
                            }
                            if (!each_object.isNull("author_img_url")) {

                                author_img_url = each_object.getString("author_img_url");

                            } else {
                                author_img_url = "";
                            }
                            if (!each_object.isNull("posted_date")) {

                                JSONObject ObjjsonObject = each_object.getJSONObject("posted_date");
                                if (!ObjjsonObject.isNull("iso")) {

                                    posted_date = ObjjsonObject.getString("iso");
                                } else {
                                    posted_date = "";
                                }


                            } else {
                                posted_date = "";
                            }

                            if (!each_object.isNull("sub_resouce_content_eng")) {

                                sub_resouce_content_eng = each_object.getString("sub_resouce_content_eng");

                            } else {
                                sub_resouce_content_eng = "";
                            }

                            if (!each_object.isNull("sub_resouce_content_mm")) {

                                sub_resouce_content_mm = each_object.getString("sub_resouce_content_mm");

                            } else {
                                sub_resouce_content_mm = "";
                            }
                            if (!each_object.isNull("sub_resource_title_eng")) {

                                sub_resource_title_eng = each_object.getString("sub_resource_title_eng");

                            } else {
                                sub_resource_title_eng = "";
                            }
                            if (!each_object.isNull("sub_resource_title_mm")) {

                                sub_resource_title_mm = each_object.getString("sub_resource_title_mm");

                            } else {
                                sub_resource_title_mm = "";
                            }


                            SubResourceItem subResourceItem = new SubResourceItem(sub_resource_title_eng, sub_resource_title_mm, sub_resouce_content_eng, sub_resouce_content_mm, authorName, author_id, author_img_url, icon_img_url, posted_date);


                            SubResourceItems.add(subResourceItem);
                        }

                        //Log.e("SubResourceAdaptersize","==>" +SubResourceItems.size());


                        if (SubResourceItems.size() == 0) {
                            if (mstr_lang.equals(Utils.ENG_LANG)) {
                                Utils.doToastEng(mContext, getResources().getString(R.string.resource_coming_soon_eng));
                            } else {

                                Utils.doToastMM(mContext, getResources().getString(R.string.resource_coming_soon_mm));
                            }
                        }
                        storageUtil.SaveArrayListToSD("SubResourceArrayList" + mResourceId, SubResourceItems);
                        mAdapter = new SubResourceListViewAdapter(mContext, SubResourceItems, mstr_lang);

                        lv.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSON err", "==>" + e.toString());
                    }
                    mProgressDialog.dismiss();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", "==" + error);
                    mProgressDialog.dismiss();
                }
            });

        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getResources().getString(R.string.open_internet_warning_mm));
            }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getSubResourceDataFromSever(mResourceId);
            return true;
        }
        if (id == android.R.id.home) {

            //Log.e("Home click on Resouce Lit", "==" + id);


            finish();
            return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
