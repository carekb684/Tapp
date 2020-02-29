package com.main.tapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";

    private ListView mListView;

    private DatabaseHelper mDatabaseHelper;
    private DataPassListener mCallback;

    public interface DataPassListener{
        public void passData(Job job);
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
        mListView = rootView.findViewById(R.id.browse_job_list);

        populateList();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Job job = (Job) mListView.getItemAtPosition(position);
                mCallback.passData(job);
            }
        });

        return rootView;
    }

    private void populateList() {
        Log.d(TAG, "populateList: displaying data in listview.");

        Cursor data = mDatabaseHelper.getJobs();
        ArrayList<Job> jobList = new ArrayList<>();

        while (data.moveToNext()) {
            Job job = new Job().
                    id(Integer.valueOf(data.getString(0))).
                    title(data.getString(1)).
                    description(data.getString(2)).
                    estTime(Double.valueOf(data.getString(3))).
                    salary(data.getInt(4)).
                    date(data.getString(5)).
                    createdDate(data.getString(6)).
                    dateTime(data.getString(7));

            jobList.add(job);
        }

        JobListAdapter adapter = new JobListAdapter(getActivity(), R.layout.list_adapter_browse_item, jobList);
        mListView.setAdapter(adapter);
    }
}
