package org.undp_iwomen.iwomen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CategoriesDataModel;
import org.undp_iwomen.iwomen.ui.activity.TalkTogetherMainActivity;
import org.undp_iwomen.iwomen.ui.adapter.TalkTogetherGridViewAdapter;
import org.undp_iwomen.iwomen.ui.widget.WrappedGridView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.SharePrefUtils;
import org.undp_iwomen.iwomen.utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

/*
import com.etsy.android.sample.CategoryGridViewAdapter;
import com.etsy.android.sample.DetailActivity;
import com.etsy.android.sample.R;
*/

/**
 * Created by dharmaone on 29/05/2014.
 */
public class TalkTogetherCategoryFragment extends android.support.v4.app.Fragment implements WrappedGridView.EndlessListener{

    private android.support.v4.app.Fragment mFragment;

    private static int ITEM_PER_REQUEST = 10;

    private View v;
    private Context ctx;
    private TalkTogetherGridViewAdapter mAdapter;

    private WrappedGridView gridView;
    private String[] mTestCategoriesNames;
    private String[] mTestCategoriesID;
    private ArrayList<CategoriesDataModel> CategoriesModelList;

    private StorageUtil storageUtil;
    private SharePrefUtils sharePrefUtils;


    ProgressWheel progress_wheel;
    JSONObject json_each_Object;
    JSONArray json_whole_Array;
    JSONObject detailed;
    String cat_img_path;
    JSONObject main_pair;
    int offset = 0;

    private  boolean gridViewResized = false;



    //public static final String ARG_PLANET_NUMBER = "planet_number";

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
        storageUtil =StorageUtil.getInstance(getActivity().getApplicationContext());


        /**********Ajust Layout Image size depend on screen at Explore ************/
        //prepareList();
        // prepared arraylist and passed it to the Adapter class
        //mAdapter = new CategoryGridViewAdapter(ctx, listShopName, listShopImg);
        // Set custom adapter to gridview
        //progressBar =(ProgressBar)v.findViewById(R.id.ProgressLoading_category);
        progress_wheel = (ProgressWheel)v.findViewById(R.id.progress_wheel);
        gridView = (WrappedGridView) v.findViewById(R.id.grid_view_cate); // Implement On Item click listener

        //gridView.setLoadingView(progressBar);
        progress_wheel.spin();
        progress_wheel.setRimColor(Color.LTGRAY);
        gridView.setLoadingView(progress_wheel);


        LoadData();

        return v;
        //return inflater.inflate(R.layout.activity_sgv, container, false);
    }

    public void LoadData(){
        if(Connection.isOnline(getActivity().getApplicationContext())){

            //Parameter


            CategoriesModelList = new ArrayList<CategoriesDataModel>();


            CategoriesDataModel cat_model0 = new CategoriesDataModel("0","Calendar","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-06f18fed-199e-4aa8-8e56-74475674cf84-applause-to-the-woman.png");
            CategoriesModelList.add(cat_model0);


            CategoriesDataModel cat_model = new CategoriesDataModel("1","Activities","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-b06f0901-87c3-4f89-b52e-39eecf7f4fa5-unity-is-strength.png");
            CategoriesModelList.add(cat_model);
            CategoriesDataModel cat_model2 = new CategoriesDataModel("2","Livelihood","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-b0069850-941d-460c-85f6-5c8631abca4f-lets-go.png");
            CategoriesModelList.add(cat_model2);

            CategoriesDataModel cat_model3 = new CategoriesDataModel("3","Q&A","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-760c37e9-f739-4222-992c-d0f2232a61fa-dun-believe-the-rumour.png");
            CategoriesModelList.add(cat_model3);

            CategoriesDataModel cat_model4 = new CategoriesDataModel("4","Sample","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-06f18fed-199e-4aa8-8e56-74475674cf84-applause-to-the-woman.png");
            CategoriesModelList.add(cat_model4);

            CategoriesDataModel cat_model5 = new CategoriesDataModel("4","Sample","http://files.parsetfss.com/a7e7daa5-3bd6-46a6-b715-5c9ac02237ee/tfss-06f18fed-199e-4aa8-8e56-74475674cf84-applause-to-the-woman.png");
            CategoriesModelList.add(cat_model5);



            if(mAdapter == null){
                mAdapter = new TalkTogetherGridViewAdapter(getActivity(),ctx, CategoriesModelList);

            }
            storageUtil.SaveArrayListToSD("Categories",CategoriesModelList);

            //mAdapter.notifyDataSetChanged();
            //progressBar.setVisibility(View.GONE);
            progress_wheel.setVisibility(View.GONE);
            gridView.setAdapter(mAdapter);




            }else{

                    //Log.e("Categories API data", "Network failure case case");
                    Toast.makeText(ctx, "Check your network connection", Toast.LENGTH_SHORT).show();

                    CategoriesModelList = (ArrayList<CategoriesDataModel>)storageUtil.ReadArrayListFromSD("Categories");

                    if(CategoriesModelList.size() > 0){
                        mAdapter = new TalkTogetherGridViewAdapter(getActivity(),ctx, CategoriesModelList);
                        //mAdapter = new CategoryGridViewAdapter(getActivity(),ctx, (ArrayList<CategoriesDataModel>) createItems(0) );//CategoriesModelList
                        gridView.setAdapter(mAdapter);
                     }

                //mAdapter.notifyDataSetChanged();
             }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(ctx, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                /*TLGUserStoriesRecentFragment tlgUserStoriesRecentFragment = new TLGUserStoriesRecentFragment();
                android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();// getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, tlgUserStoriesRecentFragment).commit();
                */

                Intent i = new Intent(ctx, TalkTogetherMainActivity.class);



                i.putExtra("CategoryName", CategoriesModelList.get(position).category);//CategoryName

                startActivity(i);

            }
        });

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
        if(menu!=null){
            menu.close();
            menu.removeItem(12);
            /*SearchView searchView = (SearchView) menu.findItem(12).getActionView();
            searchView.setIconified(false);
            searchView.setVisibility(View.INVISIBLE);*/
        }
    }

    private List<CategoriesDataModel> createItems(int os) {
        List<CategoriesDataModel> result = new ArrayList<CategoriesDataModel>();

        if(os+10 > CategoriesModelList.size()){ //10 > 85 90    //
            ITEM_PER_REQUEST = CategoriesModelList.size() - os; //5
        }

        if(ITEM_PER_REQUEST > 0) { //10 20
            for (int j = os; j < ITEM_PER_REQUEST + os; j++) {
                result.add(CategoriesModelList.get(j));
            }
        }
        return result;
    }


    @Override
    public void loadData() {

        offset +=10;
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

        *//**
         * This method can be invoked from doInBackground(Params...) to publish
         * updates on the UI thread while the background computation is still
         * running. Each call to this method will trigger the execution of
         * onProgressUpdate(Progress...) on the UI thread.
         *
         * onProgressUpdate(Progress...) will not be called if the task has been
         * canceled.
         *
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


    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {

        //int myProgressCount;


        Activity mContext;

        public BackgroundAsyncTask(Activity atx){
            this.mContext = atx;
        }
        @Override
        protected void onPreExecute() {
            /*Toast.makeText(DrawerMainActivity.this,
                    "onPreExecute Start Progress Bar", Toast.LENGTH_LONG)
                    .show();*/
            //main_spinner.setProgress(0);
            //myProgressCount = 0;
            /*pDialog = new ProgressDialog(this.mContext);
            pDialog.setMessage("Loading  information. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            /*while (myProgressCount < 50) {
                myProgressCount++;
                *//**
             * Runs on the UI thread after publishProgress(Progress...) is
             * invoked. The specified values are the values passed to
             * publishProgress(Progress...).
             *
             * Parameters values The values indicating progress.
             *//*

                publishProgress(myProgressCount);
                SystemClock.sleep(50);
            }*/
            try {

                LoadData();
            } catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pDialog.dismiss();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            //main_spinner.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            /*Toast.makeText(DrawerMainActivity.this, "onPostExecute End Progress Bar",
                    Toast.LENGTH_LONG).show();*/
            //main_spinner.setVisibility(View.GONE);
           // pDialog.dismiss();

        }


    }



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
