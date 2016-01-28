package org.undp_iwomen.iwomen.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.Sample;
import org.undp_iwomen.iwomen.ui.activity.RegisterMainActivity;

public class RegisterPwdFragment2 extends Fragment implements View.OnClickListener{
    private static final String EXTRA_SAMPLE = "sample";
    private Button btn_next;
    public static RegisterPwdFragment2 newInstance(Sample sample) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPwdFragment2 fragment = new RegisterPwdFragment2();
        fragment.setArguments(args);
        return fragment;
    }
    public static RegisterPwdFragment2 newInstance( ) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_SAMPLE, sample);
        RegisterPwdFragment2 fragment = new RegisterPwdFragment2();
        //fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_2_pwd, container, false);
        btn_next = (Button)view.findViewById(R.id.Next);


        btn_next.setOnClickListener(this);
        //Sample sample = (Sample) getArguments().getSerializable(EXTRA_SAMPLE);

        //ImageView squareBlue = (ImageView) view.findViewById(R.id.square_blue);
        //DrawableCompat.setTint(squareBlue.getDrawable(), sample.color);

        setEnglishFont();
        return view;
    }

    private void addNextFragment( Button squareBlue, boolean overlap) {
        RegisterProfileFragment3 registerProfileFragment3 = RegisterProfileFragment3.newInstance();

        Slide slideTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));

        }

        ChangeBounds changeBoundsTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        }

        registerProfileFragment3.setEnterTransition(slideTransition);
        registerProfileFragment3.setAllowEnterTransitionOverlap(overlap);
        registerProfileFragment3.setAllowReturnTransitionOverlap(overlap);
        registerProfileFragment3.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, registerProfileFragment3)
                .addToBackStack(null)
                .addSharedElement(squareBlue, getString(R.string.register_next))
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Next:
                addNextFragment(btn_next, false);
                break;
        }
    }

    public void setEnglishFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_pwd);
    }
    public void setMyanmarFont() {

        // Set title bar
        ((RegisterMainActivity) getActivity()).textViewTitle.setText(R.string.register_title_pwd);
    }
}
