package com.example.rynel.recipesapi.data;

/**
 * Created by rynel on 10/16/2017.
 */

import com.example.rynel.recipesapi.model.Example;
import com.example.rynel.recipesapi.util.Constants;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.rynel.recipesapi.util.Constants.VALUES.APP_ID;

/**
 * Created by rynel on 10/13/2017.
 */

public interface RemoteService {

    @GET("search?app_key="  + Constants.VALUES.APP_KEY + "&app_id=" + APP_ID)
    Observable<Example> getRecipe(@Query("q") String query, @Query("from") int start, @Query("to") int to);

}
