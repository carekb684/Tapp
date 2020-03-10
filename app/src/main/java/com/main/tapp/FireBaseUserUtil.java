package com.main.tapp;

import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.Nullable;

public class FireBaseUserUtil {

    private FirebaseAuth mFireAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mCurrentUser;



    public FireBaseUserUtil() {
        mFireAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mCurrentUser = mFireAuth.getCurrentUser();
    }


    /**
     *  Retrieves the logged in users full name synchronously
     * @param activity
     * @return
     * @throws FirebaseNoSignedInUserException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String getUserNameSynch(FragmentActivity activity)
            throws FirebaseNoSignedInUserException, ExecutionException, InterruptedException {
        mCurrentUser = mFireAuth.getCurrentUser();
        if (mCurrentUser != null) {
            DocumentReference ref = mFirestore.collection(
                    RegisterActivity.USER_COLLECTION).document(mCurrentUser.getUid());

            //creates task in background
            AsyncTask<Task<DocumentSnapshot>, Void, DocumentSnapshot> execute =
                    new RunInBackground(activity).execute(ref.get());
            // .get() waits(blocks) until data is ready
            DocumentSnapshot snap = execute.get();

            return String.valueOf(snap.get(RegisterActivity.USER_FULLNAME));
        }
        throw new FirebaseNoSignedInUserException("User not signed in, should be.");
    }
}
