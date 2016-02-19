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

import com.smk.iwomen.BaseActionBarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.TlgProfileItem;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.model.retrofit_api.TlgProfileAPI;
import org.undp_iwomen.iwomen.ui.adapter.TLGListViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TLGListActivity extends BaseActionBarActivity {


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
    private ArrayList<TlgProfileItem> tlgArraylist;

    TLGListViewAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    //private String mResourceId;

    private String mTitleEng, mTitleMM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_resource_list);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(TLGListActivity.this);
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
        //mResourceId = i.getStringExtra("ResourceId");
        mTitleEng = i.getStringExtra("TitleEng");
        mTitleMM = i.getStringExtra("TitleMM");


        lv = (ListView) findViewById(R.id.sub_resource_list);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if (mstr_lang.equals(Utils.ENG_LANG)) {
            textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            textViewTitle.setText(R.string.betogether_title_eng);

        } else {
            //textViewTitle.setTypeface(MyTypeFace.get(mContext, MyTypeFace.ZAWGYI));
            textViewTitle.setText(R.string.betogether_title_mm);

        }
        tlgArraylist = new ArrayList<TlgProfileItem>();
        tlgArraylist = (ArrayList<TlgProfileItem>)storageUtil.ReadArrayListFromSD("TlgArrayList");
        //SubResourceItems = (ArrayList<SubResourceItem>) storageUtil.ReadArrayListFromSD("SubResourceArrayList" + mResourceId);

        Log.e("Sub ResourceItems size", "===>" + tlgArraylist.size());

        if (tlgArraylist.size() > 0) {
            mAdapter = new TLGListViewAdapter(getApplicationContext(), tlgArraylist, mstr_lang);
            lv.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {

            getTLGListDataFromSever();
        }





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext.getApplicationContext(), TlgProfileActivity.class);////DetailActivity
                // Pass all data rank
                intent.putExtra("TLGName", tlgArraylist.get(i).get_tlg_group_name());//(shopInfolist.get(position).getsShopName())
                // Pass all data country
                intent.putExtra("TLGAddress", tlgArraylist.get(i).get_tlg_group_address());//(shopInfolist.get(position).getsShopID())
                // Pass all data population
                intent.putExtra("TLGID", tlgArraylist.get(i).get_objectId());


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        });


    }

    private void getTLGListDataFromSever() {
        if (Connection.isOnline(mContext)) {
           /* Double[][] Nearby = {{16.785231, 96.153374}, {16.785344, 96.15391},
                    {16.779602, 96.151679}, {16.780424, 96.155562},
                    {16.783177, 96.158373}, {16.782417, 96.158953}
                    , {16.783988, 96.157011}, {16.783238, 96.155638}
                    , {16.78292, 96.153095}};*/

            //tlgList = new ArrayList<TlgProfileItem>();
            TlgProfileAPI.getInstance().getService().getTlgProfileList(new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    try {


                        JSONObject whole_body = new JSONObject(s);
                        JSONArray result = whole_body.getJSONArray("results");
                        tlgArraylist.clear();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject each_object = result.getJSONObject(i);


                            String _objectId;
                            String _tlg_group_name;
                            String _tlg_group_address;
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

                            tlgArraylist.add(new TlgProfileItem(_objectId, _tlg_group_name, _tlg_group_address, _tlg_group_lat_address, _tlg_group_lng_address));
                        }

                        Log.e("tlgList", "==>" + tlgArraylist.size());
                        storageUtil.SaveArrayListToSD("TlgArrayList", tlgArraylist);

                        mAdapter = new TLGListViewAdapter(mContext, tlgArraylist, mstr_lang);

                        lv.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "==>" + e.toString());

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                        Log.e("NullPointerException", "==>" + ex.toString());

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RetrofitError", "==>" + error);
                }
            });


        } else {

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, "Internet Connection need!");
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
        switch (id) {

            case R.id.action_refresh:
                getTLGListDataFromSever();
                return true;
            case android.R.id.home:

                finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
