package com.main.tapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.main.tapp.metadata.Job;
import com.main.tapp.R;

import java.util.ArrayList;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {

    public static class JobViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private OnJobClickListener mJobClickListener;
        private TextView mTitle;
        private TextView mCreatedDate;
        private TextView mSalary;
        private TextView mEstTime;
        private View layoutView;


        public JobViewHolder(View v, TextView salary, TextView estTime, TextView createdDate,
                             TextView title, OnJobClickListener jobClickListener) {
            super(v);
            layoutView = v;
            mSalary = salary;
            mCreatedDate = createdDate;
            mTitle = title;
            mEstTime = estTime;
            mJobClickListener = jobClickListener;

            layoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mJobClickListener.onJobClick(getAdapterPosition());
        }
    }

    public interface OnJobClickListener{
        void onJobClick(int position);
    }

    private static final String TAG = "JobListAdapter";
    private ArrayList<Job> mData;
    private OnJobClickListener mClickListener;

    public JobListAdapter(ArrayList<Job> objects, OnJobClickListener clickListener) {
        mData = objects;
        mClickListener = clickListener;
    }

    @Override
    public JobListAdapter.JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_adapter_browse_item, parent, false);

       TextView title = layoutView.findViewById(R.id.browse_job_title);
       TextView salary = layoutView.findViewById(R.id.browse_job_salary);
       TextView estTime = layoutView.findViewById(R.id.browse_job_est_time);
       TextView createdDate = layoutView.findViewById(R.id.browse_job_created_date);

       return new JobViewHolder(layoutView, salary, estTime, createdDate, title, mClickListener);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        holder.mTitle.setText(mData.get(position).getTitle());
        holder.mCreatedDate.setText(mData.get(position).getCreatedDate());
        holder.mSalary.setText(mData.get(position).getSalary() + "kr");
        holder.mEstTime.setText(mData.get(position).getEstTime() + "h");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
