package com.main.tapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;

import com.main.tapp.R;
import com.main.tapp.metadata.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private OnUserClickListener mUserClickListener;
        private TextView mFullName;
        private TextView mLastMsgDate;
        private ImageView mImage;

        private View layoutView;


        public UserViewHolder(View v, ImageView image, TextView lastMsgDate,
                              TextView fullName, OnUserClickListener userClickListener) {
            super(v);
            layoutView = v;
            mImage = image;
            mLastMsgDate = lastMsgDate;
            mFullName = fullName;
            mUserClickListener = userClickListener;

            layoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mUserClickListener.onUserClick(getAdapterPosition());
        }
    }

    public interface OnUserClickListener {
        void onUserClick(int position);
    }

    private static final String TAG = "UserListAdapter";
    private ArrayList<User> mData;
    private OnUserClickListener mClickListener;

    public UserListAdapter(ArrayList<User> objects, OnUserClickListener clickListener) {
        mData = objects;
        mClickListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_adapter_browse_user, parent, false);

       TextView name = layoutView.findViewById(R.id.browse_user_name);
       TextView lastMsg = layoutView.findViewById(R.id.browse_user_last_msg);
       ImageView image = layoutView.findViewById(R.id.browse_users_image);

       return new UserViewHolder(layoutView, image, lastMsg, name, mClickListener);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.mFullName.setText(mData.get(position).getFullname());
        holder.mLastMsgDate.setText(mData.get(position).getLastMsgDate());

        RoundedBitmapDrawable image = mData.get(position).getImage();
        if(image != null) {
            holder.mImage.setImageDrawable(image);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
