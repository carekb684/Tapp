package com.main.tapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.tapp.Adapter.UserListAdapter;
import com.main.tapp.metadata.Chat;
import com.main.tapp.metadata.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class MessagesFragment extends Fragment implements UserListAdapter.OnUserClickListener{

    private static final String TAG = "MessagesFragment";

    private ArrayList<User> mUsers;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mFireAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore chatRef;
    private UserListAdapter mAdapter;

    private int userUpdateCount = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        mFireAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFireAuth.getCurrentUser();

        mRecyclerView = rootView.findViewById(R.id.browse_users_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getUsersList();

        return rootView;
    }

    private void getUsersList() {

        mUsers = new ArrayList<>();

        chatRef = FirebaseFirestore.getInstance();
        chatRef.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                mUsers.clear();
                for (Chat chat : documentSnapshot.toObjects(Chat.class)) {
                    if (chat.getReceiver().equals(mCurrentUser.getUid())) {
                        mUsers.add(new User().uid(chat.getSender()));
                    }
                    if (chat.getSender().equals(mCurrentUser.getUid())) {
                        mUsers.add(new User().uid(chat.getReceiver()));
                    }
                }

                //remove duplicates of uid
                HashSet<Object> seen = new HashSet<>();
                mUsers.removeIf(u->!seen.add(u.getUid()));

                setRecyclerView();
                retrieveUserInfo();
            }
        });

    }

    private void retrieveUserInfo() {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        CollectionReference userRef = FirebaseFirestore.getInstance().collection(RegisterActivity.USER_COLLECTION);

        final long ONE_MEGABYTE = 1024 * 1024;
        userUpdateCount = 0;

        for (User user: mUsers) {
            userRef.document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    Map<String, Object> data = documentSnapshot.getData();
                    user.setFullname(String.valueOf(data.get(RegisterActivity.USER_FULLNAME)));
                }
            });

            StorageReference userImageRef = ref.child("images/users/" + user.getUid() + "/image.png");
            userImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bTemp, 150, 150, false);
                    RoundedBitmapDrawable userBImage = RoundedBitmapDrawableFactory.create(getResources(), scaledBitmap);
                    userBImage.setCircular(true);
                    user.setImage(userBImage);
                    userUpdateCount++;
                    if (mUsers.size() == userUpdateCount) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    userUpdateCount++;
                    if (mUsers.size() == userUpdateCount) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    private void setRecyclerView() {
        mAdapter = new UserListAdapter(mUsers, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onUserClick(int position) {
        User user = mUsers.get(position);
        Intent intent = new Intent(getContext(), ConversationActivity.class);
        intent.putExtra(ViewJobFragment.USER_RECEIVE, user.getUid());
        intent.putExtra(ViewJobFragment.USERNAME_RECEIVE, user.getFullname());
        startActivity(intent);
    }
}
