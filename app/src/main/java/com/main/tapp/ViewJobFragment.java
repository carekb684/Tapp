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

    public final static String JOB_RECEIVE = "job_receive";
    private TextView mTitleTV;
    private TextView mDescriptionTV;
    private TextView mEstTimeTV;
    private TextView mSalaryTV;
    private TextView mSelectDateTV;
    private TextView mSelectTimeTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_job, container, false);


        mTitleTV = rootView.findViewById(R.id.view_job_title_TV);
        mDescriptionTV = rootView.findViewById(R.id.view_job_description_TV);
        mEstTimeTV = rootView.findViewById(R.id.view_job_est_time_TV);
        mSalaryTV = rootView.findViewById(R.id.view_job_salary_TV);
        mSelectDateTV = rootView.findViewById(R.id.view_job_date_TV);
        mSelectTimeTV = rootView.findViewById(R.id.view_job_date_time_TV);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            Job job = (Job)args.getSerializable(JOB_RECEIVE);
            mTitleTV.setText(job.getTitle());
            mDescriptionTV.setText(job.getDescription());
            mEstTimeTV.setText(String.valueOf(job.getEstTime()));
            mSalaryTV.setText(String.valueOf(job.getSalary()));
            mSelectDateTV.setText(job.getDate());
            mSelectTimeTV.setText(job.getDateTime());
        }
    }

}
