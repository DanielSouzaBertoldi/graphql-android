package com.example.graphqlapp.ui;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graphqlapp.DeleteUserActivity;
import com.example.graphqlapp.DetailsActivity;
import com.example.graphqlapp.R;
import com.example.graphqlapp.UpdateUserActivity;
import com.example.graphqlapp.UsersQuery;
import com.example.graphqlapp.util.AplClient;

import java.util.Collections;
import java.util.List;

public class UserRecyclerViewAdapter
        extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {
    private Context context;
    private List<UsersQuery.User> users = Collections.emptyList();

    public UserRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(List<UsersQuery.User> users) {
        this.users = users;
        this.notifyDataSetChanged();
        Log.d(AplClient.TAG, "updating users in adapter..." + users.size());
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.row, parent, false);

        return new UserViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.UserViewHolder holder, int position) {
        final UsersQuery.User user = this.users.get(position);
        holder.setUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTV;
        TextView age;
        TextView profession;
        CardView container;
        String userId;
        Context context;
        ImageButton deleteButton;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            userTV       = itemView.findViewById(R.id.user_name);
            age          = itemView.findViewById(R.id.user_age);
            profession   = itemView.findViewById(R.id.user_profession);
            container    = itemView.findViewById(R.id.cardview);
            deleteButton = itemView.findViewById(R.id.imageButton);

            this.context = context;
        }

        void setUser(final UsersQuery.User user) {
            userTV.setText(context.getString(R.string.user_name, user.name()));
            age.setText(context.getString(R.string.user_age, user.age()));
            profession.setText(context.getString(R.string.user_profession, user.profession()));
            userId = user.id();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DeleteUserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", userId);
                    intent.putExtra("name", user.name());
                    context.startActivity(intent);
                }
            });

            // Update
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, UpdateUserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", userId);
                    intent.putExtra("name", user.name());
                    intent.putExtra("age", user.age());
                    intent.putExtra("profession", user.profession());

                    context.startActivity(intent);
                    return false;
                }
            });

            // Show details
            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(AplClient.TAG, " - UserID: " + userId);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userID", userId);
                    intent.putExtra("userName", user.name());
                    intent.putExtra("userAge", user.age());
                    intent.putExtra("userProfession", user.profession());

                    context.startActivity(intent); //This file is not an activity. We need the context to start another activity.
                }
            });
        }
    }
}
