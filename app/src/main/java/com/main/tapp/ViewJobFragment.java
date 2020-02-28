package com.main.tapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewJobFragment extends Fragment {

    final static String DATA_RECEIVE = "data_receive";
    private TextView mTitleTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_job, container, false);

        mTitleTV = rootView.findViewById(R.id.view_job_title_TV);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            mTitleTV.setText(args.getString(DATA_RECEIVE));
        }
    }

}
