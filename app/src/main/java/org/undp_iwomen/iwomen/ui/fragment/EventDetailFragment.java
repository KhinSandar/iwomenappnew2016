package org.undp_iwomen.iwomen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.undp_iwomen.iwomen.R;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class EventDetailFragment extends Fragment {

    //TODO: replace with your desired data
    public static final String EXTRA_ID = "id";

    //TODO: replace with your desired data
    public static EventDetailFragment newInstance(String id){
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail,  container, false);

        return rootView;
    }
}
