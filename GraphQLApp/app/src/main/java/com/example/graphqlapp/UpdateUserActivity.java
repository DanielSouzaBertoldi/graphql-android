package com.example.graphqlapp;

import android.content.Intent;
import android.os.Bundle;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphqlapp.util.AplClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.graphqlapp.R;

import org.jetbrains.annotations.NotNull;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {
    private String userID;

    private EditText name;
    private EditText age;
    private EditText profession;
    private EditText postComment;
    private EditText hobbyTitle;
    private EditText hobbyDesc;

    private LinearLayout bottomLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //EditTexts
        name = findViewById(R.id.update_user_name);
        age = findViewById(R.id.update_user_age);
        profession = findViewById(R.id.update_user_profession);
        postComment = findViewById(R.id.update_user_post_comment);
        hobbyTitle = findViewById(R.id.update_user_hobby_title);
        hobbyDesc = findViewById(R.id.update_user_hobby_desc);

        //Buttons
        Button updateUser = findViewById(R.id.update_user_btn);
        Button addPostAndHobby = findViewById(R.id.update_user_post_and_hobby_btn);

        bottomLL = findViewById(R.id.update_user_ll_bottom);

        updateUser.setOnClickListener(this);
        addPostAndHobby.setOnClickListener(this);

        Intent intent = getIntent();

        if(intent != null) {
            userID = intent.getStringExtra("id");
            name.setText(intent.getStringExtra("name"));
            age.setText(String.valueOf(intent.getIntExtra("age", 0)));
            profession.setText(intent.getStringExtra("profession"));
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.update_user_btn:
                updateUser(userID);
                break;
            case R.id.update_user_post_and_hobby_btn:
                addHobby(userID);
                addPost(userID);
                break;
        }
    }

    private void updateUser(final String userID) {
        AplClient.getmApolloClient()
                .mutate(UpdateUserMutation
                        .builder()
                        .iD(userID)
                        .name(name.getText().toString().trim())
                        .age(Integer.parseInt(age.getText().toString()))
                        .profession(profession.getText().toString().trim())
                        .build())
                .enqueue(new ApolloCall.Callback<UpdateUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                        assert response.getData() != null;
                        assert response.getData().UpdateUser != null;
                        final String name = response.getData().UpdateUser.name;

                        UpdateUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (name != null) {
                                    bottomLL.setVisibility(View.VISIBLE);
                                    Toast.makeText(UpdateUserActivity.this,
                                            "Name: " + name, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.d("WHAT", e.toString());
                    }
                });
    }

    private void addPost(String userID) {
        AplClient.getmApolloClient()
                .mutate(AddPostMutation
                        .builder()
                        .id(userID)
                        .comment(postComment.getText().toString().trim())
                        .build())
                .enqueue(new ApolloCall.Callback<AddPostMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<AddPostMutation.Data> response) {
                        assert response.getData() != null;
                        assert response.getData().CreatePost != null;

                        UpdateUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateUserActivity.this,
                                        "Post Comment: " + response.getData().CreatePost.comment,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(new Intent(UpdateUserActivity.this,
                                MainActivity.class));
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    private void addHobby(String userID) {
        AplClient.getmApolloClient()
                .mutate(AddHobbyMutation
                        .builder()
                        .id(userID)
                        .title(hobbyTitle.getText().toString().trim())
                        .desc(hobbyDesc.getText().toString().trim())
                        .build())
                .enqueue(new ApolloCall.Callback<AddHobbyMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<AddHobbyMutation.Data> response) {
                        assert response.getData() != null;
                        assert response.getData().CreateHobby != null;

                        UpdateUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateUserActivity.this,
                                        "Hobby Title: " + response.getData().CreateHobby.title,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(new Intent(UpdateUserActivity.this,
                                MainActivity.class));
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    // Resets the MainActivity view
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(UpdateUserActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, keyEvent);
    }
}