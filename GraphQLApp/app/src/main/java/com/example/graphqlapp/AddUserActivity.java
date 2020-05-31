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

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private String userID;

    private EditText name;
    private EditText age;
    private EditText profession;
    private EditText postComment;
    private EditText hobbyTitle;
    private EditText hobbyDesc;

    private Button addUser;
    private Button addPostAndHobby;

    private LinearLayout bottomLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.add_user_name);
        age = findViewById(R.id.add_user_age);
        profession = findViewById(R.id.add_user_profession);
        postComment = findViewById(R.id.add_user_post_comment);
        hobbyTitle = findViewById(R.id.add_user_hobby_title);
        hobbyDesc = findViewById(R.id.add_user_hobby_desc);

        addUser = findViewById(R.id.add_user_btn);
        addPostAndHobby = findViewById(R.id.add_user_post_and_hobby_btn);

        bottomLL = findViewById(R.id.add_user_ll_bottom);

        // since our class implements on click,
        // we just pass the context, which will run the onClick below
        // it's a good ideia when you have 2 or more buttons
        addUser.setOnClickListener(this);
        addPostAndHobby.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_user_btn:
                addUser();
                break;
            case R.id.add_user_post_and_hobby_btn:
                addPost(userID);
                addHobby(userID);
                break;
        }
    }

    private void addUser() {
        AplClient.getmApolloClient()
                .mutate(AddUserMutation
                        .builder()
                        .name(name.getText().toString().trim())
                        .age(Integer.parseInt(age.getText().toString()))
                        .profession(profession.getText().toString().trim())
                        .build())
                .enqueue(new ApolloCall.Callback<AddUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AddUserMutation.Data> response) {
                        assert response.getData() != null;
                        assert response.getData().CreateUser != null;
                        final String id = response.getData().CreateUser.id;

                        AddUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(id != null) {
                                    userID = id;
                                    bottomLL.setVisibility(View.VISIBLE);
                                    Toast.makeText(AddUserActivity.this,
                                            "id: "+userID, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

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

                        AddUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddUserActivity.this,
                                        "Post Comment: " + response.getData().CreatePost.comment,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(new Intent(AddUserActivity.this,
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

                        AddUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddUserActivity.this,
                                        "Hobby Title: " + response.getData().CreateHobby.title,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        startActivity(new Intent(AddUserActivity.this,
                                MainActivity.class));
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(AddUserActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, keyEvent);
    }
}