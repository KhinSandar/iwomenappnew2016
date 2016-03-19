package org.undp_iwomen.iwomen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import org.smk.iwomen.BaseActionBarActivity;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.ViewEventsFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

public class ViewEventsActivity extends BaseActionBarActivity {

    String str_date,calcuate_date;
    int imonth;
    Bundle bundle;
    private CustomTextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        Intent i = getIntent();
        str_date = i.getStringExtra("Date");
        calcuate_date = i.getStringExtra("CalculateDate");
        imonth = i.getIntExtra("Month", 1);


        bundle = new Bundle();
        bundle.putString("Date", str_date);
        bundle.putString("CalculateDate", calcuate_date);
        bundle.putInt("Month", imonth);

        /*// Set up the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);*/
        /**********Set up the ToolBar**************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewTitle.setText(getResources().getString(R.string.calendar_detail));

        /**********Set up the ToolBar**************/


        if(null == savedInstanceState){
            ViewEventsFragment viewEventsFragment = new ViewEventsFragment();
            viewEventsFragment.setArguments(bundle);
            initFragment(viewEventsFragment);
        }
    }

    private void initFragment(Fragment viewEventsFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, viewEventsFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
