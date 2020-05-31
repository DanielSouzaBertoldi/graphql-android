package com.example.graphqlapp;

import android.content.Intent;
import android.os.Bundle;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.graphqlapp.ui.UserRecyclerViewAdapter;
import com.example.graphqlapp.util.AplClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private UserRecyclerViewAdapter userRecyclerViewAdapter;
    private ViewGroup content;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        content = findViewById(R.id.content_holder);
        progressBar = findViewById(R.id.progressBar);

        //Recycler instantiations
        RecyclerView recyclerView = findViewById(R.id.recycler);
        userRecyclerViewAdapter = new UserRecyclerViewAdapter(this.getApplicationContext());
        recyclerView.setAdapter(userRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddUserActivity.class));
            }
        });

        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Apollo Client stuff
     */
    private void getUsers() {
        AplClient.getmApolloClient()
        .query(UsersQuery.builder().build())
        .enqueue(new ApolloCall.Callback<UsersQuery.Data>() {
            @Override
            public void onResponse(@NotNull final Response<UsersQuery.Data> response) {
                final String name = response.getData().users().get(0).name;

                for(int i = 0; i < response.getData().users().size(); i++) {
                    Log.d("ID", response.getData().users().get(i).id);
                    Log.d("User", response.getData().users().get(i).name);
                    Log.d("Age", response.getData().users().get(i).age.toString());
                }

                progressBar.setVisibility(View.VISIBLE);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userRecyclerViewAdapter.setUsers(Objects.requireNonNull(
                                response.getData().users));
                        progressBar.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull final ApolloException e) {
                Log.d("Apollo On Failure", e.toString());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //textView.setText(getString(R.string.no_results_found));
                        Log.d(AplClient.TAG, e.getStackTrace().toString());
                    }
                });
            }
        });
    }
}
