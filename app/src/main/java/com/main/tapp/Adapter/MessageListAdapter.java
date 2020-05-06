package com.main.tapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.main.tapp.R;
import com.main.tapp.metadata.Chat;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mShow_message;
        private ImageView mImage;
        private View layoutView;


        public MessageViewHolder(View v) {
            super(v);
            layoutView = v;
            mImage = v.findViewById(R.id.chat_item_image);
            mShow_message = v.findViewById(R.id.chat_item_msg);
        }
    }

    private static final String TAG = "MessageListAdapter";
    private ArrayList<Chat> mData;
    private RoundedBitmapDrawable mTargetBImage;
    private RoundedBitmapDrawable mUserBImage;

    private FirebaseUser mCurrentUser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageListAdapter(ArrayList<Chat> objects, RoundedBitmapDrawable targetImage,
                              RoundedBitmapDrawable userBImage) {
        mData = objects;
        mTargetBImage = targetImage;
        mUserBImage = userBImage;
    }

    public void setTargetImage(RoundedBitmapDrawable img){
        mTargetBImage = img;
    }

    public void setUserImage(RoundedBitmapDrawable img){
        mUserBImage = img;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
            return new MessageViewHolder(layoutView);
        } else {
            View layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);
            return new MessageViewHolder(layoutView);
        }

    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Chat chat = mData.get(position);

        holder.mShow_message.setText(chat.getMessage());
        if (getItemViewType(position) == MSG_TYPE_LEFT && mTargetBImage !=null ) {
            holder.mImage.setImageDrawable(mTargetBImage);
        } else if (mUserBImage != null){
            holder.mImage.setImageDrawable(mUserBImage);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mData.get(position).getSender().equals(mCurrentUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
