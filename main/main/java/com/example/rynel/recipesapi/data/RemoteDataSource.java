package com.example.rynel.recipesapi.data;

/**
 * Created by rynel on 10/16/2017.
 */

import com.example.rynel.recipesapi.model.Example;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rynel on 10/13/2017.
 */

public class RemoteDataSource {

    //Edeman URL
    public static final String BASE_URL = "https://api.edamam.com/";
    //https://api.edamam.com/search?q=chicken&app_id=$30b9cf92&app_key=$
    //ea46a1329e99d7c17e324278fa15bb26&from=0&to=3&calories=gte%20591,%20lte%20722&health=alcohol-free


    //Retrofit construct
    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

    public static Observable<Example> getRecipe(String query, int start, int to ) {
        Retrofit retrofit = create();
        RemoteService remoteService = retrofit.create( RemoteService.class );

        return remoteService.getRecipe( query, start, to );
    }
}

