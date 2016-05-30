package org.undp_iwomen.iwomen.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.fragment.CalendarViewPagerFragment;

public class CalendarViewPagerFragmentAdapter extends FragmentPagerAdapter { //implements IconPagerAdapter FragmentPagerAdapter
    protected static final String[] CONTENT = new String[] { "1", "2", "3",
    "4","5", "6", "7"
    ,"8", "9", "10"
    ,"11", "12"};
    int[] randomImg = new int[]{R.drawable.jan ,R.drawable.feb, R.drawable.mar
            ,R.drawable.april,R.drawable.may ,R.drawable.june, R.drawable.july
            ,R.drawable.aug ,R.drawable.sep, R.drawable.oct
            ,R.drawable.nov ,R.drawable.dec
                                };/*For View Pager*/
	protected  static String[] imgurl ;
    LayoutInflater inflater;


    private int mCount = randomImg.length;

    public CalendarViewPagerFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

		
    @Override
	public Fragment getItem(int position) {

        return CalendarViewPagerFragment.newInstance(randomImg[position % randomImg.length]);
    }

    
    @Override
	public int getCount() {
        return mCount;
    }

    
    @Override
	public CharSequence getPageTitle(int position) {
        return CalendarViewPagerFragmentAdapter.CONTENT[position % CONTENT.length];
    }

   /* 
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }*/

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    /************My Adapter Codes**************/
  

}