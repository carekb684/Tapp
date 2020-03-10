package com.main.tapp;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.ExecutionException;

public class RunInBackground extends AsyncTask<Task<DocumentSnapshot>, Void, DocumentSnapshot> {

    private ProgressBar mBar;

    public RunInBackground(FragmentActivity activity) {
        mBar = activity.findViewById(R.id.create_job_progressBar);
    }

    @Override
    protected DocumentSnapshot doInBackground(Task<DocumentSnapshot>... tasks) {
        DocumentSnapshot snap = null;
        try {
            snap = Tasks.await(tasks[0]);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return snap;
    }

    @Override
    protected void onPreExecute() {
        mBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(DocumentSnapshot result) {
        mBar.setVisibility(View.INVISIBLE);
    }

}
