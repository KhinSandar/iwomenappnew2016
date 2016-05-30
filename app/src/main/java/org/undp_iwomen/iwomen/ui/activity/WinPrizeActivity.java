package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.WinPrizesFragment;
import org.undp_iwomen.iwomen.ui.fragment.WinPrizesFragmentThz;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;


public class WinPrizeActivity extends BaseActionBarActivity {
    private Toolbar mActionBarToolbar;
    private Toolbar toolbar;
    public CustomTextView textViewTitle;
    SharedPreferences sharePrefLanguageUtil;

    private static final String POINT = "point";
    private static final String PAGE = "page";
    //private String mPoint;
    private String mShareStatus;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_fragment);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        String lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        //setActionBarToolbar();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);
        //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));
        //textViewTitle.setText("Myanma\u0020Deals");//"MYANMARDEALS"
        if(lang.equals(Utils.ENG_LANG)){
            textViewTitle.setText(R.string.menu5);
        }
        else if(lang.equals(Utils.MM_LANG)){
            textViewTitle.setText(R.string.menu5);
        }else if(lang.equals(Utils.MM_LANG_UNI)){
            textViewTitle.setText(R.string.menu5);
        }else if(lang.equals(Utils.MM_LANG_DEFAULT)){
            textViewTitle.setText(R.string.menu5);
        }

        Intent i = getIntent();
        //mPoint = i.getStringExtra(POINT);
        mShareStatus = i.getStringExtra(PAGE);

        bundle = new Bundle();
        //bundle.putString(POINT, mPoint);
        bundle.putString(PAGE, mShareStatus);

        if (savedInstanceState == null) {

            WinPrizesFragment winPrizesFragment = new WinPrizesFragment();
            winPrizesFragment.setArguments(bundle);

            WinPrizesFragmentThz winPrizesFragmentThz = new WinPrizesFragmentThz();
            winPrizesFragmentThz.setArguments(bundle);


            if(mShareStatus.equalsIgnoreCase("0")){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, winPrizesFragment)
                        .commit();
            }else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, winPrizesFragmentThz)
                        .commit();
            }

        }
        overridePendingTransition(0,0);
    }

    private void initFragment(Fragment winPrizeFragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, winPrizeFragment);
        transaction.commit();
    }

    protected Toolbar setActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        return mActionBarToolbar;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            startDrawerMainActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        startDrawerMainActivity();
    }

    private void startDrawerMainActivity(){
        Intent intent = new Intent(this, DrawerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
