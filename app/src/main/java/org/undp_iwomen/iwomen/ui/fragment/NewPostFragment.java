package org.undp_iwomen.iwomen.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.undp_iwomen.iwomen.R;

public class NewPostFragment extends Fragment {

    private Spinner mCategorySpinner;

    public NewPostFragment() {
    }

    public static NewPostFragment newInstance(){
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootViews){
        mCategorySpinner = (Spinner) rootViews.findViewById(R.id.new_post_category_spinner);
        setupCategorySpinner();
    }

    private void setupCategorySpinner(){
        String[] categories = getCategory();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.new_post_category_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);
    }

    private String[] getCategory(){
        return getResources().getStringArray(R.array.new_post_category);
    }
}
