package com.main.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.main.tapp.metadata.Job;

public class ViewJobFragment extends Fragment {

    public final static String JOB_RECEIVE = "job_receive";
    public final static String USER_RECEIVE = "user_receive";
    public final static String USERNAME_RECEIVE = "username_receive";

    private TextView mTitleTV;
    private TextView mDescriptionTV;
    private TextView mEstTimeTV;
    private TextView mSalaryTV;
    private TextView mSelectDateTV;
    private TextView mSelectTimeTV;
    private Button mContactBtn;

    private String mCreatedByName;

    private String mCreatedByUid;
    private FirebaseAuth mFireAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_job, container, false);

        mFireAuth = FirebaseAuth.getInstance();

        mTitleTV = rootView.findViewById(R.id.view_job_title_TV);
        mDescriptionTV = rootView.findViewById(R.id.view_job_description_TV);
        mEstTimeTV = rootView.findViewById(R.id.view_job_est_time_TV);
        mSalaryTV = rootView.findViewById(R.id.view_job_salary_TV);
        mSelectDateTV = rootView.findViewById(R.id.view_job_date_TV);
        mSelectTimeTV = rootView.findViewById(R.id.view_job_date_time_TV);
        mContactBtn = rootView.findViewById(R.id.contact_button);

        mContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCreatedByUid.equals(mFireAuth.getCurrentUser().getUid())) {
                    Intent intent = new Intent(getContext(), ConversationActivity.class);
                    intent.putExtra(USER_RECEIVE, mCreatedByUid);
                    intent.putExtra(USERNAME_RECEIVE, mCreatedByName);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Can not contact your own job",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            Job job = (Job)args.getSerializable(JOB_RECEIVE);
            mCreatedByUid = job.getCreatedByUID();

            mTitleTV.setText(job.getTitle());
            mDescriptionTV.setText(job.getDescription());
            mEstTimeTV.setText(String.valueOf(job.getEstTime()));
            mSalaryTV.setText(String.valueOf(job.getSalary()));
            mSelectDateTV.setText(job.getDate());
            mSelectTimeTV.setText(job.getDateTime());

            mCreatedByName = job.getCreatedByName();
        }
    }

}
