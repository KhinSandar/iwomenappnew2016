package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.smk.model.Categories;
import com.smk.skconnectiondetector.SKConnectionDetector;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smk.clientapi.NetworkEngine;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.CalendarActivity;
import org.undp_iwomen.iwomen.ui.activity.TalkTogetherMainActivity;
import org.undp_iwomen.iwomen.ui.adapter.TalkTogetherGridViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.WrappedGridView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
import com.etsy.android.sample.CategoryGridViewAdapter;
import com.etsy.android.sample.DetailActivity;
import com.etsy.android.sample.R;
*/

/**
 * Created by dharmaone on 29/05/2014.
 */
public class TalkTogetherCategoryFragment extends android.support.v4.app.Fragment implements WrappedGridView.EndlessListener {

    private android.support.v4.app.Fragment mFragment;


    private static int ITEM_PER_REQUEST = 10;

    private View v;
    private Context ctx;
    private TalkTogetherGridViewAdapter mAdapter;

    private WrappedGridView gridView;
    private String[] mTestCategoriesNames;
    private String[] mTestCategoriesID;
    private ArrayList<Categories> CategoriesModelList;

    private StorageUtil storageUtil;
    private SharePrefUtils sharePrefUtils;


    ProgressWheel progress_wheel;
    private boolean isLoading = true;
    JSONObject json_each_Object;
    JSONArray json_whole_Array;
    JSONObject detailed;
    String cat_img_path;
    JSONObject main_pair;
    int offset = 0;

    private boolean gridViewResized = false;
    private ZProgressHUD zPDialog;

    private SharedPreferences mSharedPreferencesUserInfo;
    private String user_name, user_obj_id, user_id, user_role, user_ph, register_msg, user_img_path;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;

    public TalkTogetherCategoryFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        ctx = getActivity().getApplicationContext();
        v = inflater.inflate(R.layout.fragment_talk_together_main, container, false);
        storageUtil = StorageUtil.getInstance(getActivity().getApplicationContext());
        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        user_role = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ROLE, null);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        /**********Ajust Layout Image size depend on screen at Explore ************/
        //prepareList();
        // prepared arraylist and passed it to the Adapter class
        //mAdapter = new CategoryGridViewAdapter(ctx, listShopName, listShopImg);
        // Set custom adapter to gridview
        //progressBar =(ProgressBar)v.findViewById(R.id.ProgressLoading_category);
        progress_wheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        gridView = (WrappedGridView) v.findViewById(R.id.grid_view_cate); // Implement On Item click listener

        //gridView.setLoadingView(progressBar);
        progress_wheel.spin();
        progress_wheel.setRimColor(Color.LTGRAY);
        gridView.setLoadingView(progress_wheel);

        CategoriesModelList = new ArrayList<>();
        mAdapter = new TalkTogetherGridViewAdapter(getActivity(), ctx, CategoriesModelList , mstr_lang);
        gridView.setAdapter(mAdapter);


        List<Categories> categories = (ArrayList<Categories>) storageUtil.ReadArrayListFromSD("Categories");
        if (Connection.isOnline(ctx)) {
            if (categories != null && categories.size() > 0) {
                CategoriesModelList.addAll(categories);
                mAdapter.notifyDataSetChanged();
                zPDialog = new ZProgressHUD(getActivity());
                zPDialog.show();
            }
            LoadData();
        } else {
            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            if (categories != null) {
                CategoriesModelList.clear();
                CategoriesModelList.addAll(categories);
                mAdapter.notifyDataSetChanged();
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {


                if (user_role.equalsIgnoreCase("User")) {
                    if (CategoriesModelList.get(position).getName().equalsIgnoreCase("Archive")) {//position == 0 ||
                        Utils.showPermissionDialog(getActivity());
                    } else if (CategoriesModelList.get(position).getName().equalsIgnoreCase("Calendar")) {//position == 0 ||
                        Intent i = new Intent(ctx, CalendarActivity.class);
                        i.putExtra("CategoryName", CategoriesModelList.get(position).getName());//CategoryName
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ctx, TalkTogetherMainActivity.class);
                        i.putExtra("CategoryName", CategoriesModelList.get(position).getName());//CategoryName
                        i.putExtra("CategoryID", CategoriesModelList.get(position).getObjectId());//CategoryName
                        startActivity(i);
                    }
                } else {
                    if (CategoriesModelList.get(position).getName().equalsIgnoreCase("Calendar")) {//position == 0 ||
                        Intent i = new Intent(ctx, CalendarActivity.class);
                        i.putExtra("CategoryName", CategoriesModelList.get(position).getName());//CategoryName
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ctx, TalkTogetherMainActivity.class);
                        i.putExtra("CategoryName", CategoriesModelList.get(position).getName());//CategoryName
                        i.putExtra("CategoryID", CategoriesModelList.get(position).getObjectId());//CategoryName
                        startActivity(i);
                    }
                }


            }
        });


        return v;
        //return inflater.inflate(R.layout.activity_sgv, container, false);
    }

    public void LoadData() {
        if (Connection.isOnline(getActivity().getApplicationContext())) {

            progress_wheel.setVisibility(View.VISIBLE);
            isLoading = true;

            NetworkEngine.getInstance().getCategoriesByPagination(1, new Callback<List<Categories>>() {
                @Override
                public void success(List<Categories> categories, Response response) {
                    //CategoriesModelList = new ArrayList<Categories>();
                    // Only first REQUEST that visible
                    if (zPDialog != null && zPDialog.isShowing()) {
                        CategoriesModelList.clear();
                        zPDialog.dismissWithSuccess();
                    }
                    CategoriesModelList.addAll(categories);


                    if (mAdapter == null) {
                        mAdapter = new TalkTogetherGridViewAdapter(getActivity(), ctx, CategoriesModelList, mstr_lang);

                    }
                    storageUtil.SaveArrayListToSD("Categories", CategoriesModelList);

                    progress_wheel.setVisibility(View.GONE);
                    gridView.setAdapter(mAdapter);


                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else {


            //SKConnectionDetector.getInstance(getActivity()).showErrorMessage();
            CategoriesModelList = (ArrayList<Categories>) storageUtil.ReadArrayListFromSD("Categories");

            if (CategoriesModelList.size() > 0) {
                mAdapter = new TalkTogetherGridViewAdapter(getActivity(), ctx, CategoriesModelList);
                gridView.setAdapter(mAdapter);
            }


        }

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public void onResume() {
        super.onResume();

        //Tracking.startUsage(getActivity());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.close();
            menu.removeItem(12);

        }
    }

    private List<Categories> createItems(int os) {
        List<Categories> result = new ArrayList<Categories>();

        if (os + 10 > CategoriesModelList.size()) { //10 > 85 90    //
            ITEM_PER_REQUEST = CategoriesModelList.size() - os; //5
        }

        if (ITEM_PER_REQUEST > 0) { //10 20
            for (int j = os; j < ITEM_PER_REQUEST + os; j++) {
                result.add(CategoriesModelList.get(j));
            }
        }
        return result;
    }


    @Override
    public void loadData() {

        offset += 10;
        gridView.addNewData(createItems(offset));
    }


    /*************************************************************************
     * Background  Class extend Async Class
     ***************************************************************************/

    /*************************************************************************
     * Background Suggestion Post Reply Control Class extend Async Class
     ***************************************************************************//*
    private class AsyncControlClass extends BackgroundAsyncTask {




        public AsyncControlClass(Activity atx){
            super(atx);

            // TODO Auto-generated constructor stub
        }

        */

    /**
     * This method can be invoked from doInBackground(Params...) to publish
     * updates on the UI thread while the background computation is still
     * running. Each call to this method will trigger the execution of
     * onProgressUpdate(Progress...) on the UI thread.
     * <p/>
     * onProgressUpdate(Progress...) will not be called if the task has been
     * canceled.
     * <p/>
     * Parameters values The progress values to update the UI with.
     *//*
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            //main_spinner.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            *//*Toast.makeText(DrawerMainActivity.this, "onPostExecute End Progress Bar",
                    Toast.LENGTH_LONG).show();*//*
            //main_spinner.setVisibility(View.GONE);
            pDialog.dismiss();

        }


    }*/
    @Override
    public void onPause() {
        super.onPause();
        //Tracking.stopUsage(getActivity());
    }


    @Override
    public void onStop() {
        super.onStop();


    }

    private void resizeGridView(GridView gridView, int items, int columns) {
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        int oneRowHeight = gridView.getHeight();
        int rows = (int) (items / columns);
        params.height = oneRowHeight * rows;
        gridView.setLayoutParams(params);
    }


}
