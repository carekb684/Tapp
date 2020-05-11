package com.main.tapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.tapp.Adapter.MessageListAdapter;
import com.main.tapp.metadata.Chat;
import com.main.tapp.metadata.Job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Nullable;

public class ConversationActivity extends AppCompatActivity {

    private static final String TAG = "ConversationActivity";

    private FirebaseUser mCurrentUser;
    private RoundedBitmapDrawable targetBImage;
    private RoundedBitmapDrawable userBImage;

    private TextView mTargetUserNameTV;
    private ImageView mTargetImageView;
    private EditText mSendText;
    private ImageButton mSendBtn;

    private ArrayList<Chat> mChat;
    private MessageListAdapter msgAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore chatRef;

    private DatabaseHelper mDb;

    private View mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initViews();

        Toolbar toolbar = findViewById(R.id.conversation_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???
                finish();
            }
        });


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String targetUid = getIntent().getStringExtra(ViewJobFragment.USER_RECEIVE);
        String targetName = getIntent().getStringExtra(ViewJobFragment.USERNAME_RECEIVE);

        initUserImage(targetUid, mCurrentUser.getUid());
        mTargetUserNameTV.setText(targetName);


        readMessages(mCurrentUser.getUid(), targetUid);


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mSendText.getText().toString();
                if (!msg.isEmpty()) {
                    sendMessage(mCurrentUser.getUid(), targetUid, msg);
                }
                mSendText.setText("");
            }
        });

    }

    private void initUserImage(String targetUid, String currentUserUid) {

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference targetImageRef = ref.child("images/users/" + targetUid + "/image.png");
        StorageReference userImageRef = ref.child("images/users/" + currentUserUid + "/image.png");

        final long ONE_MEGABYTE = 1024 * 1024;
        userImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bTemp, 150, 150, false);
                userBImage = RoundedBitmapDrawableFactory.create(getResources(), scaledBitmap);
                userBImage.setCircular(true);

                if (msgAdapter != null) {
                    msgAdapter.setUserImage(userBImage);
                    msgAdapter.notifyDataSetChanged();
                }
            }
        });

        targetImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bTemp, 150, 150, false);
                targetBImage = RoundedBitmapDrawableFactory.create(getResources(), scaledBitmap);
                targetBImage.setCircular(true);
                mTargetImageView.setImageDrawable(targetBImage);

                if (msgAdapter != null) {
                    msgAdapter.setTargetImage(targetBImage);
                    msgAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {
        FirebaseFirestore reference = FirebaseFirestore.getInstance();

        HashMap<String, Object> chatData = new HashMap<>();
        chatData.put("sender", sender);
        chatData.put("receiver", receiver);
        chatData.put("message", message);
        chatData.put("timestamp", new Date());

        reference.collection("chats").document().set(chatData);
    }

    private void readMessages(String userId, String targetId) {
        mChat = new ArrayList<>();

        chatRef = FirebaseFirestore.getInstance();
        chatRef.collection("chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mChat.clear();
                for (Chat chat : documentSnapshot.toObjects(Chat.class)) {
                    if (chat.getReceiver().equals(userId) && chat.getSender().equals(targetId) ||
                            chat.getReceiver().equals(targetId) && chat.getSender().equals(userId)) {
                        mChat.add(chat);
                    }

                }
                if (!mChat.isEmpty()) {
                    Collections.sort(mChat, new Comparator<Chat>() {
                        @Override
                        public int compare(Chat chat1, Chat chat2) {
                            return (chat1.getTimestamp().compareTo(chat2.getTimestamp()));
                        }
                    });
                }
                msgAdapter = new MessageListAdapter(mChat, targetBImage, userBImage);
                mRecyclerView.setAdapter(msgAdapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.conversation_menu_give_job:
                showJobPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showJobPopup() {

        ArrayList<Job> jobs = mDb.getJobsBy(mCurrentUser.getUid());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.conversation_give_job_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //set shadow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.conversation_dot_menu, menu);
        return true;
    }

    private void initViews() {
        mTargetUserNameTV = findViewById(R.id.conversation_username);
        mTargetImageView = findViewById(R.id.conversation_profile_img);
        mSendText = findViewById(R.id.conversation_edit_text);
        mSendBtn = findViewById(R.id.conversation_btn_send);

        mRecyclerView = findViewById(R.id.conversation_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mDb = new DatabaseHelper(getApplicationContext());
        mRootView = getLayoutInflater().inflate(R.layout.activity_conversation, null);
    }

}
