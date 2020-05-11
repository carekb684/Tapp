package com.main.tapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tapp.Adapter.JobListAdapter;
import com.main.tapp.metadata.Job;

import java.util.ArrayList;

public class BrowseFragment extends Fragment implements JobListAdapter.OnJobClickListener {

    private static final String TAG = "BrowseFragment";

    private ArrayList<Job> mJobs;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseHelper mDatabaseHelper;
    private DataPassListener mCallback;

    public interface DataPassListener{
        public void passViewJob(Job job);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (DataPassListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement DataPassListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        mDatabaseHelper = new DatabaseHelper(getActivity());

        mRecyclerView = rootView.findViewById(R.id.browse_job_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobs = mDatabaseHelper.getJobs();
        JobListAdapter adapter = new JobListAdapter(mJobs, this);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onJobClick(int position) {
        Job job = mJobs.get(position);
        mCallback.passViewJob(job);
    }
}
