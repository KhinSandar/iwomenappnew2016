package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.TLGListActivity;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;
import org.undp_iwomen.iwomen.utils.Utils;

/**
 * Created by khinsandar on 8/28/15.
 */
public class BeTogetherFragment extends Fragment {

    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    private Context mContext;

    ResizableImageView imgMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_be_togther, container, false);
        init(view);
        return view;
    }

    private void init(View rootView) {


        mContext = getActivity().getApplicationContext();
        imgMap = (ResizableImageView)rootView.findViewById(R.id.betogether_map_img);
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TLGListActivity.class);
                intent.putExtra("TitleEng", R.string.betogether_title_eng);
                intent.putExtra("TitleMM", R.string.betogether_title_mm);
                //intent.putExtra("post_id", feedItems.get(position).getPost_obj_id());

                //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }
    public void setEnglishFont(){

    }
    public void setMyanmarFont(){

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.menu_post_news, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {


            return false;

        }

        return super.onOptionsItemSelected(item);
    }
}
