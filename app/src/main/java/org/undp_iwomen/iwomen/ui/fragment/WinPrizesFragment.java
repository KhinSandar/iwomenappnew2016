package org.undp_iwomen.iwomen.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.undp_iwomen.iwomen.R;

public class WinPrizesFragment extends Fragment {

    private static final String POINT = "point";
    private static final String PAGE = "page";

    private int mPoint, mPage;

    public static WinPrizesFragment newInstance(int backgroundColor, int page) {
        WinPrizesFragment frag = new WinPrizesFragment();
        Bundle b = new Bundle();
        b.putInt(POINT, backgroundColor);
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(POINT))
            throw new RuntimeException("Fragment must contain a \"" + POINT + "\" argument!");
        mPoint = getArguments().getInt(POINT);

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        mPage = getArguments().getInt(PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Select a layout based on the current page
        int layoutResId;
        switch (mPage) {
            case 0:
                layoutResId = R.layout.fragment_win_prize;
                break;
            default:
                layoutResId = R.layout.fragment_win_prize_thz;
        }

        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);

        // Set the current page index as the View's tag (useful in the PageTransformer)
        view.setTag(mPage);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the background color of the root view to the color specified in newInstance()
        /*View background = view.findViewById(R.id.intro_background);
        background.setBackgroundColor(mBackgroundColor);*/
    }
    
}
