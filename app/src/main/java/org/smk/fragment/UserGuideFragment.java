package org.smk.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.smk.model.UserIntro;
import org.undp_iwomen.iwomen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserGuideFragment extends Fragment {


    private TextView txt_number_indicator;
    private ImageView img_user_guide;
    private TextView txt_user_guide;
    private int mPosition;
    private UserIntro mUserIntro;

    public UserGuideFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserGuideFragment newInstance(int position, Object obj) {
        UserGuideFragment fragment = new UserGuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("_position", position);
        bundle.putString("_object", new Gson().toJson(obj));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getArguments() != null ? getArguments().getInt("_position") : 1;
        mUserIntro = getArguments() != null ? new Gson().fromJson(getArguments().getString("_object"), UserIntro.class) : null;
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_user_guide, container, false);
        img_user_guide = (ImageView) convertView.findViewById(R.id.img_user_guide);
        txt_user_guide = (TextView) convertView.findViewById(R.id.txt_user_guide_desc);
        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        img_user_guide.setImageResource(mUserIntro.getDrawable());
        txt_user_guide.setText(mUserIntro.getDescription());
    }
}
