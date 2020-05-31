package com.example.graphqlapp.ui;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graphqlapp.GetUserQuery;
import com.example.graphqlapp.R;

import java.util.Collections;
import java.util.List;

public class GetUserRecyclerViewAdapter
        extends RecyclerView.Adapter<GetUserRecyclerViewAdapter.GetUserViewHolder>  {
    private boolean isHobby;
    private Context context;
    private List<GetUserQuery.Post> posts    = Collections.emptyList();
    private List<GetUserQuery.Hobby> hobbies = Collections.emptyList();

    public GetUserRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setUserData(List<GetUserQuery.Post> posts, List<GetUserQuery.Hobby> hobbies, boolean isHobby) {
        this.posts   = posts;
        this.hobbies = hobbies;
        this.isHobby = isHobby;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GetUserRecyclerViewAdapter.GetUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.details_row, parent, false);
        return new GetUserViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GetUserViewHolder holder, int position) {
        if(isHobby) {
            final GetUserQuery.Hobby hobby = this.hobbies.get(position);
            holder.setHobby(hobby);
        } else {
            final GetUserQuery.Post post = this.posts.get(position);
            holder.setPost(post);
        }
    }

    @Override
    public int getItemCount() {
        return isHobby ? hobbies.size() : posts.size();
    }

    public class GetUserViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        Context context;

        public GetUserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            title = itemView.findViewById(R.id.firstInfo);
            description = itemView.findViewById(R.id.descriptionInfo);
            this.context = context;
        }

        public void setHobby(GetUserQuery.Hobby hobby) {
            title.setText(hobby.title());
            description.setText(hobby.description());
        }

        public void setPost(GetUserQuery.Post post) {
            title.setText("Comments");
            description.setText(post.comment());
        }
    }
}
