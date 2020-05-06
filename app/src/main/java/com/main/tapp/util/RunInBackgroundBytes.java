package com.main.tapp.util;

import android.os.AsyncTask;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.ExecutionException;

public class RunInBackgroundBytes extends AsyncTask<Task<byte[]>, Void, byte[]> {


    public RunInBackgroundBytes() {}

    @Override
    protected byte[] doInBackground(Task<byte[]>... tasks) {
        byte[] snap = null;
        try {
            snap = Tasks.await(tasks[0]);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return snap;
    }


}
