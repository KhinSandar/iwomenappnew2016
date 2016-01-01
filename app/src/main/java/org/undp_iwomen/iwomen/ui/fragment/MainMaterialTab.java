package org.undp_iwomen.iwomen.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.astuetz.PagerSlidingTabStrip;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.FontConverter;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by KhinSandar on 24/09/2014.
 */
public class MainMaterialTab extends Fragment {

    // This code up to onDetach() is all to get easy callbacks to the Activity.
    private Callbacks mCallbacks = sDummyCallbacks;

    public interface Callbacks
    {
        public void onTaskFinished();
    }
    private static Callbacks sDummyCallbacks = new Callbacks()
    {
        public void onTaskFinished() { }
    };

    // Save a reference to the fragment manager. This is initialised in onCreate().
    private android.app.FragmentManager mFM;

    // Code to identify the fragment that is calling onActivityResult(). We don't really need
    // this since we only have one fragment to deal with.
    static final int TASK_FRAGMENT = 0;

    // Tag so we can find the task fragment again, in another instance of this fragment after rotation.
    static final String TASK_FRAGMENT_TAG = "task";

    private View v = null;
    private Context ctx;
    private TabHost tabHost;



    String curVersionName;
    public String mainKeyWords;

    /********MaterialTab*******/
    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xBE000000 ;//0xFF666666

    String newversion = null;
    Boolean IsGreater = true;
    String strDownLink = "";


    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;




    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*if (!(activity instanceof Callbacks))
        {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }*/
        //mCallbacks = (Callbacks) activity;
        // mActionBar = activity.getSupportActionBar();
        /*try{
            cListener = (OnContactSelectedListener) activity;
        }catch(Exception e){
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }*/
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setHasOptionsMenu(true);
        //System.out.println("On Activity created");
        /**************Version Check*********************/

        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getApplication().getPackageName(), 0);
            curVersionName = packageInfo.versionName;
            //Toast.makeText(ctx, "Version -" + curVersionName, Toast.LENGTH_SHORT).show();


            //checkPareAppVersion();


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /**************Version Check*********************/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this instance so it isn't destroyed when MainActivity and
        // MainFragment change configuration.
        setRetainInstance(true);



    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected((MenuItem) item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mainbundle = getArguments();




        if (mainbundle != null) {
            mainKeyWords = mainbundle.getString("keywords");
        }

        super.onCreateView(inflater, container, savedInstanceState);

        /*****End Action Bar********/
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        ctx = getActivity().getApplicationContext();
        //Toast.makeText(ctx, "Search Main tab key" + mainKeyWords, 1).show();
        v = inflater.inflate(R.layout.activity_material_tab_main, container, false);


        tabs = (PagerSlidingTabStrip)v.findViewById(R.id.tabs);
        tabs.setTextColor(getResources().getColor(android.R.color.white));
        tabs.setBackgroundColor(getResources().getColor(R.color.line_color));
        tabs.setIndicatorColor(getResources().getColor(android.R.color.white));
        tabs.setIndicatorHeight(2);



        pager = (ViewPager) v.findViewById(R.id.pager);
        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();


        mstr_lang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);
        if (mstr_lang.equals(Utils.ENG_LANG)) {

            adapter = new MyPagerAdapter(getChildFragmentManager(), fragments,mstr_lang);
            tabs.setTypeface(MyTypeFace.get(ctx,MyTypeFace.NORMAL),0);

        } else if (mstr_lang.equals(Utils.MM_LANG)) {

            adapter = new MyPagerAdapter(getChildFragmentManager(), fragments,mstr_lang);
            tabs.setTypeface(MyTypeFace.get(ctx,MyTypeFace.ZAWGYI),0);

        }else if (mstr_lang.equals(Utils.MM_LANG_UNI)) {

            adapter = new MyPagerAdapter(getChildFragmentManager(), fragments,mstr_lang);
            //String text = FontConverter.zg12uni51(tabs..toString());
            tabs.setTypeface(MyTypeFace.get(ctx,MyTypeFace.UNI),0);

        }else if (mstr_lang.equals(Utils.MM_LANG_DEFAULT)) {

            adapter = new MyPagerAdapter(getChildFragmentManager(), fragments,mstr_lang);
            //tabs.setTypeface(MyTypeFace.get(ctx,MyTypeFace.ZAWGYI),0);

        }


        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        /***************ViewPagerControl important****************/
        /* pager.setOffscreenPageLimit([quantity of tabs that you are going to use]);
		with that sentence (pager.setOffscreenPageLimit) you can specify how far away
		 a tab can stand before it get erased by the ViewPager!*/
        //PagerTabStrip strip = (PagerTabStrip)findViewById(R.id.tabstrip);
        //strip.setTabIndicatorColor(Color.parseColor("#3c5a99"));
        pager.setOffscreenPageLimit(2);
        //pager.setCurrentItem(1);
        //pager.setOnPageChangeListener();
        tabs.setTextColor(getResources().getColor(R.color.drawer_list_item_txt));


        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //Toast.makeText(getActivity().getApplicationContext(),"onPageScrolled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                /*(position == 0){
                    pager.setCurrentItem();
                }*/
                //Toast.makeText(getActivity().getApplicationContext(),"onPageSelected" + position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(getActivity().getApplicationContext(),"onPageScrollStateChanged",Toast.LENGTH_SHORT).show();
            }
        });

        //tabs.setIndicatorColor(Color.parseColor("#be0000"));




        return v;

    }
    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        StoriesMostLikesFragment storiesMostLikesFragment = new StoriesMostLikesFragment();
        StoriesRecentFragment storiesRecentFragment = new StoriesRecentFragment();


        fList.add(storiesRecentFragment);

        fList.add(storiesMostLikesFragment);

        return fList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }



    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        // Declare Variables
        Context mContext;
        String mstr_lang;


        /*SharedPreferences sPref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        String language = sPref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        String lang = sPref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);*/





        private final String[] TITLES = {"most recent" ,"most liked"
                 };
        private final String[] TITLES_MM = {"ေနာက္ဆံုုးရစာမ\u103Aား", "လူ\u107Eကိ\u1033က္အမ\u103Aားဆံုုး"
        };

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments , String typefaceName) {
            super(fm);
            this.fragments = fragments;
            mstr_lang = typefaceName;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                return TITLES[position];

            } else if (mstr_lang.equals(Utils.MM_LANG)) {
                return TITLES_MM[position];

            }else if (mstr_lang.equals(Utils.MM_LANG_UNI)) {
                String uni_text = FontConverter.zg12uni51(TITLES_MM[position]);
                return uni_text;

            }else if (mstr_lang.equals(Utils.MM_LANG_DEFAULT)) {
                return TITLES_MM[position];

            }
            return null;


        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            //return TabCardFragment.newInstance(position);
            return this.fragments.get(position);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();


        //Tracking.startUsage(getActivity());
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

    /*//Check Pare Version
    private void checkPareAppVersion(){
        ParseQuery<AppVersion> query = ParseQuery.getQuery("AppVersion");
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<AppVersion>() {
            @Override
            public void done(AppVersion appVersion, ParseException e) {
                if(appVersion == null){
                    IsGreater = false;
                    Toast.makeText(getActivity().getApplicationContext(),
                            "The getFirst request failed." + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }else {



                    if(appVersion.getString("versionName") == null){

                    }else {
                        if (isGreater(appVersion.getString("versionName"), curVersionName)) {


                            IsGreater = true;
                            strDownLink = appVersion.getString("DownloadLinks");
                            Toast.makeText(getActivity(), "New Version Available at " + strDownLink, Toast.LENGTH_SHORT).show();

                            DownAPKTask downAPKTask = new DownAPKTask();
                            downAPKTask.execute();


                        } else {

                            IsGreater = false;
                            //Toast.makeText(getActivity(), "ServerReplyversion-"+appVersion.getString("versionName")  + "IsGreater-" +IsGreater, Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }
        });


    }*/



    public class DownAPKTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            String path = "/sdcard/lostandfound.apk";
            try {
                URL url = new URL(strDownLink);//sUrl[0]//http://goo.gl/4NlNYF"
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("YourApp", "Well that didn't work out so well...");
                Log.e("YourApp", e.getMessage());
            }
            return path;
        }

        // begin the installation by opening the resulting file
        @Override
        protected void onPostExecute(String path) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            Log.d("Lofting", "About to install new .apk");
            getActivity().startActivity(i);
        }

    }
    /*
     * check whether @param one is greater than @param two
     *
     * */
    public boolean isGreater(String server, String device) {



        String s1 = normalisedVersion(server);
        String s2 = normalisedVersion(device);
        int cmp = s1.compareTo(s2);
//        return cmp > 0;
        return cmp < 0 ? false : cmp > 0 ? true : false;

    }

    public String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 4);
    }

    public String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }
}
