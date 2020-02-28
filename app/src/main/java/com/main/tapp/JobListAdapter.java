package com.main.tapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class JobListAdapter extends ArrayAdapter<Job> {
    private static final String TAG = "JobListAdapter";

    private Context mContext;
    private int mResource;

    public JobListAdapter(@NonNull Context context, int resource, ArrayList<Job> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Job job = new Job().
                title(getItem(position).getTitle()).
                createdDate(getItem(position).getCreatedDate()).
                estTime(getItem(position).getEstTime()).
                salary(getItem(position).getSalary());

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView titleTV = convertView.findViewById(R.id.browse_job_title);
        TextView createdTV = convertView.findViewById(R.id.browse_job_text1);
        TextView salary2TV = convertView.findViewById(R.id.browse_job_text2);
        TextView estTime2TV = convertView.findViewById(R.id.browse_job_text3);

        titleTV.setText(job.getTitle());
        createdTV.setText(job.getCreatedDate());
        salary2TV.setText(String.valueOf(job.getSalary()) + "kr");
        estTime2TV.setText(String.valueOf(job.getEstTime()) + "h");

        return convertView;
    }
}
