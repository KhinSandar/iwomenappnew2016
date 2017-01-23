package org.undp_iwomen.iwomen.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.NewIWomenPostFragment;
import org.undp_iwomen.iwomen.ui.fragment.NewPostPostFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

public class NewPostActivity extends BaseActionBarActivity {

    private String categoryId, categoryName;
    private CustomTextView textViewTitle;
    SharedPreferences sharePrefLanguageUtil;
    String strLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        strLang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            categoryId = bundle.getString("categoryId");
            categoryName = bundle.getString("categoryName");
        }

        if (strLang.equals(Utils.ENG_LANG)) {
            textViewTitle.setText(R.string.title_activity_new_post);

        } else
        {
            textViewTitle.setText(R.string.title_activity_new_post);

        }
        if(null == savedInstanceState){
            if(bundle != null && categoryId != null)
                initFragment(NewPostPostFragment.newInstance(categoryId , categoryName));
            else
                initFragment(NewIWomenPostFragment.newInstance());

        }
    }

    private void initFragment(Fragment newPostFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, newPostFragment);
        transaction.commit();

    }

    /*@Override
    public boolean onSupportNavigateUp() {
        finish();
        super.onBackPressed();
        return true;
    }*/
    /*@Override
    public void onBackPressed() {
        NewPostPostFragment fragment = (NewPostPostFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(fragment != null){
            fragment.onHomePressed();
        }
    }*/

}
