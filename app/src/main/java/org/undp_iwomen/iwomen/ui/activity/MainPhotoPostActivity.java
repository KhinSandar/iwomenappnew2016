package org.undp_iwomen.iwomen.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.fragment.MainPhotoIWomenPostFragment;
import org.undp_iwomen.iwomen.ui.fragment.MainPhotoPostFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;


public class MainPhotoPostActivity extends ActionBarActivity {

    String mstrPostType;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    private CustomTextView textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        Intent i = getIntent();
        mstrPostType = i.getStringExtra("PostType");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if(strLang.equals(Utils.ENG_LANG)) {


            if(mstrPostType.equalsIgnoreCase("BeInspiredIwomenPost")){
                textViewTitle.setText(R.string.title_activity_beinspired_new_post_eng);
            }else{
                textViewTitle.setText(R.string.title_activity_new_post_eng);
            }

            textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.NORMAL));
            //setTitle(R.string.loading_eng);


        }
        else if(strLang.equals(Utils.MM_LANG)){
            textViewTitle.setText(R.string.title_activity_new_post_mm);
            //textViewTitle.setTypeface(MyTypeFace.get(getApplicationContext(), MyTypeFace.ZAWGYI));



        }else if(strLang.equals(Utils.MM_LANG_UNI)){
            textViewTitle.setText(R.string.title_activity_new_post_mm);




        }else if(strLang.equals(Utils.MM_LANG_DEFAULT)){
            textViewTitle.setText(R.string.title_activity_new_post_mm);


        }



        if (savedInstanceState == null) {


            MainPhotoPostFragment mainReportFragment = new MainPhotoPostFragment();

            MainPhotoIWomenPostFragment  mainPhotoIWomenPostFragment = new MainPhotoIWomenPostFragment();
            /*Bundle b = new Bundle();

            b.putString("ReportType",mstrReportType);


            mainReportFragment.setArguments(b);*/
            if(mstrPostType.equalsIgnoreCase("BeInspiredIwomenPost")){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mainPhotoIWomenPostFragment)
                        .commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mainReportFragment)
                        .commit();
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
       /* final MenuItem item = menu.add(0, 16, 0, "Setting");
        //menu.removeItem(12);
        item.setIcon(R.drawable.lf200b);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER
                | MenuItem.SHOW_AS_ACTION_NEVER);

        return true;*/
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
