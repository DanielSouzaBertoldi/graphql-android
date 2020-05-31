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

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteUserActivity extends AppCompatActivity implements View.OnClickListener {
    private String userID;
    private String name;

    private TextView userName;

    private Button yesBtn;
    private Button noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = findViewById(R.id.delete_user_name);
        yesBtn = findViewById(R.id.delete_user_yes);
        noBtn = findViewById(R.id.delete_user_no);

        Intent intent = getIntent();
        if(intent != null) {
            userID = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
        }

        userName.setText(name);

        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);

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
        switch(v.getId()) {
            case R.id.delete_user_yes:
                deleteUser(userID);
                break;
            case R.id.delete_user_no:
                startActivity(new Intent(DeleteUserActivity.this,
                        MainActivity.class));
                break;
        }
    }

    private void deleteUser(String userID) {
        AplClient.getmApolloClient()
                .mutate(DeleteUserMutation
                        .builder()
                        .id(userID)
                        .build())
                .enqueue(new ApolloCall.Callback<DeleteUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<DeleteUserMutation.Data> response) {
                        name = Objects.requireNonNull(
                                Objects.requireNonNull(response.getData()).DeleteUser).name();

                        DeleteUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DeleteUserActivity.this,
                                        "Deleted user " + name + ".",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(DeleteUserActivity.this,
                                        MainActivity.class));
                                DeleteUserActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }
}