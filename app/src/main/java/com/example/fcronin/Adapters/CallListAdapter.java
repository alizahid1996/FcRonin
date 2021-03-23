package com.example.fcronin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fcronin.MainActivity;
import com.example.fcronin.Models.User;
import com.example.fcronin.R;
import com.example.fcronin.activities.CallListActivity;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.holder> implements Filterable {
    private Context context;
    private ArrayList<User> myUsers;
    private ArrayList<User> itemsFiltered;
    private User userMe;

    public CallListAdapter(Context context, ArrayList<User> myUsers, ArrayList<User> itemsFiltered, User userMe) {
        this.context = context;
        this.myUsers = myUsers;
        this.itemsFiltered = itemsFiltered;
        this.userMe = userMe;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public holder onCreateholder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.adapter_list_item_log_call,
                parent, false));

    }

    @Override
    public void onBindholder(@NonNull holder holder, int position) {
        final User user = itemsFiltered.get(position);
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            holder.myProgressBar.setVisibility(View.VISIBLE);
            if (user.getBlockedUsersIds() != null && !user.getBlockedUsersIds().contains(MainActivity.userId))
                Picasso.get()
                        .load(user.getImage())
                        .tag(this)
                        .placeholder(R.drawable.ic_avatar)
                        .error(R.drawable.ic_avatar)
                        .into(holder.userImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.myProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                holder.myProgressBar.setVisibility(View.GONE);
                            }
                        });
            else {
                Picasso.get()
                        .load(R.drawable.ic_avatar)
                        .tag(this)
                        .error(R.drawable.ic_avatar)
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.userImage);
                holder.myProgressBar.setVisibility(View.GONE);
            }
        } else {
            Picasso.get()
                    .load(R.drawable.ic_avatar)
                    .tag(this)
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .into(holder.userImage);
            holder.myProgressBar.setVisibility(View.GONE);
        }
        holder.userName.setText(user.getNameInPhone());
        holder.status.setText(user.getStatus());

        holder.audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CallListActivity) context).makeCall(false, user);
            }
        });

        holder.videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CallListActivity) context).makeCall(true, user);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class holder extends RecyclerView.holder{
        private ImageView userImage;
        private ImageView audioCall;
        private ImageView videoCall;
        private TextView userName;
        private TextView status;
        private ProgressBar myProgressBar;

        public holder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            status = itemView.findViewById(R.id.status);
            audioCall = itemView.findViewById(R.id.audioCall);
            videoCall = itemView.findViewById(R.id.videoCall);
            myProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
