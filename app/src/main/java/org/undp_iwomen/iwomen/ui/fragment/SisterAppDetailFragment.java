package org.undp_iwomen.iwomen.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.undp_iwomen.iwomen.R;

public class SisterAppDetailFragment extends Fragment {

    public SisterAppDetailFragment() {
    }

    //TODO: replace with your desired data
    public static final String EXTRA_ID = "id";

    //TODO: replace with your desired data
    public static SisterAppDetailFragment newInstance(String id){
        SisterAppDetailFragment fragment = new SisterAppDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sister_app_detail, container, false);
    }
}
