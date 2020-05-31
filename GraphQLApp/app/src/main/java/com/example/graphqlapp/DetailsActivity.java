package com.example.graphqlapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphqlapp.ui.GetUserRecyclerViewAdapter;
import com.example.graphqlapp.util.AplClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    private String id;
    private GetUserRecyclerViewAdapter getUserRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<GetUserQuery.Post> posts = Collections.emptyList();
    private List<GetUserQuery.Hobby> hobbies = Collections.emptyList();
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nameTV = findViewById(R.id.details_name);
        TextView ageTV = findViewById(R.id.details_age);
        TextView professionTV = findViewById(R.id.details_profession);
        Button hobbiesBtn = findViewById(R.id.details_btn_hobbies);
        Button postsBtn = findViewById(R.id.details_btn_posts);
        relativeLayout = findViewById(R.id.details_recycler_view);

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("userID");
            String name = intent.getStringExtra("userName");
            int age = intent.getIntExtra("userAge", 0);
            String profession = intent.getStringExtra("userProfession");

            nameTV.setText(getString(R.string.user_name, name));
            ageTV.setText(getString(R.string.user_age, age));
            professionTV.setText(getString(R.string.user_profession, profession));

            //RecyclerView setup
            recyclerView = findViewById(R.id.details_recycler);
            getUserRecyclerViewAdapter = new GetUserRecyclerViewAdapter(this.getApplicationContext());
            recyclerView.setAdapter(getUserRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setVisibility(View.INVISIBLE);

            hobbiesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.removeAllViews();
                    getInfo(true);
                }
            });

            postsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.removeAllViews();
                    getInfo(false);
                }
            });
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void getInfo(final boolean isHobby) {
        AplClient.getmApolloClient()
                .query(GetUserQuery.builder()
                        .userID(id)
                        .build())
                .enqueue(new ApolloCall.Callback<GetUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<GetUserQuery.Data> response) {
                        /*for(int i = 0; i < posts.size(); i++) { Making sure we're getting all the data
                            Log.d("POSTS", posts.get(i).comment);
                            Log.d("HOBBIES", hobbies.get(i).title);
                        }*/

                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                assert response.getData() != null;
                                hobbies = Objects.requireNonNull(response.getData().user).hobbies;
                                posts = Objects.requireNonNull(response.getData().user).posts;

                                if(isHobby && hobbies.isEmpty())
                                        showMessage(getString(R.string.no_hobbies_found));
                                else if(!isHobby && posts.isEmpty())
                                        showMessage(getString(R.string.no_posts_found));

                                getUserRecyclerViewAdapter.setUserData(posts, hobbies, isHobby);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull final ApolloException e) {
                        Log.d("Apollo On Failure", e.toString());

                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //textView.setText(getString(R.string.no_results_found));
                                Log.d(AplClient.TAG, e.getStackTrace().toString());
                            }
                        });
                    }
                });
    }

    public void showMessage(String message) {
        relativeLayout.removeAllViews();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        TextView msg = new TextView(DetailsActivity.this);
        msg.setText(message);
        msg.setTextColor(Color.GRAY);
        msg.setTextSize((float) 18.9);
        msg.setLayoutParams(layoutParams);

        relativeLayout.addView(msg);
    }
}
