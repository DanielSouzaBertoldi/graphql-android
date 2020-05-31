package com.example.graphqlapp.util;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;

public class AplClient {
    public static final String BASE_URL = "https://graphql-udemy-android.herokuapp.com/graphql";
    public static final String TAG = "CLIENT Android App";
    private static ApolloClient mApolloClient;

    public static ApolloClient getmApolloClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        mApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return mApolloClient;
    }
}
