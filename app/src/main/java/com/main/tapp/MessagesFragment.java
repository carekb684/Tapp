package com.main.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.main.tapp.Adapter.UserListAdapter;
import com.main.tapp.metadata.User;

import java.util.ArrayList;

public class MessagesFragment extends Fragment implements UserListAdapter.OnUserClickListener{

    private static final String TAG = "MessagesFragment";

    private ArrayList<User> mUsers;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mFireAuth;
    private FirebaseUser mCurrentUser;



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
        mUsers = getUsersList();
        UserListAdapter adapter = new UserListAdapter(mUsers, this);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<User> getUsersList() {

        ArrayList<User> users = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setFullname("Test 1");
        user2.setFullname("Test 2");
        user3.setFullname("Test 3");

        user1.setLastMsgDate("02/5 12:00");
        user2.setLastMsgDate("02/5 12:02");
        user3.setLastMsgDate("02/5 12:03");

        user1.setUid("G0BNfl0wIOeT2uyXgGKbb3lqmcf1");
        user2.setUid("HmJg67u5jTNzz9gFMZnjNlPDEL62");
        user3.setUid("TARMHbyxrfUm0bYYwV8AOhY192Z2");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        return users;
    }

    @Override
    public void onUserClick(int position) {
        User user = mUsers.get(position);
        Intent intent = new Intent(getContext(), ConversationActivity.class);

    }
}
