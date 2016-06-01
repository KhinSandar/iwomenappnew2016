package org.smk.iwomen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.smk.application.StoreUtil;
import org.smk.fragment.UserGuideFragment;
import org.smk.model.UserIntro;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.DrawerMainActivity;
import org.undp_iwomen.iwomen.ui.activity.MainLoginActivity;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class TakeAndTourActivity extends AppCompatActivity {

    private ArrayList<UserIntro> intro_list;
    private Button btn_nxt;
    private Button btn_pre;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_and_tour);

        viewpager = (ViewPager) findViewById(R.id.fragment_quick_start);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.quick_start_indicator);
        btn_pre = (Button) findViewById(R.id.btn_pre_user_guide);
        btn_nxt = (Button) findViewById(R.id.btn_next_user_guide);

        btn_nxt.setOnClickListener(clickListener);
        btn_pre.setOnClickListener(clickListener);


        intro_list = new ArrayList<UserIntro>();
        intro_list.add(new UserIntro(R.drawable.take_a_tour_1,"စြမ္းရည္ျမင့္မာ ေမ့အင္းအား"));
        intro_list.add(new UserIntro(R.drawable.take_a_tour_2,"စြမ္းရည္ျမင့္မာ ေမ့အင္းအား"));
        intro_list.add(new UserIntro(R.drawable.take_a_tour_3,"စြမ္းရည္ျမင့္မာ ေမ့အင္းအား"));
        intro_list.add(new UserIntro(R.drawable.take_a_tour_4,"စြမ္းရည္ျမင့္မာ ေမ့အင္းအား"));
        intro_list.add(new UserIntro(R.drawable.take_a_tour_5,"စြမ္းရည္ျမင့္မာ ေမ့အင္းအား"));
        viewpager.setAdapter(new QuickStartPagerAdapter(getSupportFragmentManager(),intro_list));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    btn_pre.setText(getResources().getString(R.string.str_skip));
                }else{
                    btn_pre.setText(getResources().getString(R.string.str_back));
                }
                if(position == viewpager.getAdapter().getCount() -1){
                    btn_nxt.setText(getResources().getString(R.string.str_finished));
                }else{
                    btn_nxt.setText(getResources().getString(R.string.str_next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicator.setViewPager(viewpager);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btn_nxt){
                if(viewpager.getAdapter().getCount() - 1 == viewpager.getCurrentItem()){
                    StoreUtil.getInstance().saveTo("user_guide", true);
                    Intent i = new Intent(getApplicationContext(), DrawerMainActivity.class);//DrawerMainActivity
                    startActivity(i);
                }else{
                    viewpager.setCurrentItem(getItem(+1), true);
                }
            }
            if(v == btn_pre){
                if(viewpager.getCurrentItem() == 0){
                    StoreUtil.getInstance().saveTo("user_guide", true);
                    startActivity(new Intent(getApplicationContext(), MainLoginActivity.class));
                    finish();
                }else{
                    viewpager.setCurrentItem(getItem(-1), true);
                }

            }
        }
    };

    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }

    public class QuickStartPagerAdapter extends FragmentPagerAdapter {

        private List<UserIntro> list;

        public QuickStartPagerAdapter(FragmentManager fm, List<UserIntro> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return list.get(position).getDescription();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return UserGuideFragment.newInstance(position, list.get(position));
        }

    }
}
