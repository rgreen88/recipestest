package com.example.rynel.recipesapi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rynel.recipesapi.data.RemoteDataSource;
import com.example.rynel.recipesapi.model.Example;
import com.example.rynel.recipesapi.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //declaring objects as per RecyclerView (layout/item animator), DatabaseHelper, and adapter
    RecyclerView recyclerViewQuery;
    List<Recipe> recipeItems = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;
    private RecipeListAdapter recipeListAdapter;
    Button searchBtn;
    EditText queryView;
    TextView state;

    //starting position for search
    int start = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind/initialize the views, button, and query
        recyclerViewQuery = findViewById(R.id.rvRecipeItemList);
        queryView = findViewById(R.id.etQuery);
        searchBtn = findViewById(R.id.btnSearch);
        layoutManager = new GridLayoutManager(this, 3);
        itemAnimator = new DefaultItemAnimator();
        recyclerViewQuery.setLayoutManager(layoutManager);
        recyclerViewQuery.setItemAnimator(itemAnimator);
        state = findViewById(R.id.tvState);

        //
        recyclerViewQuery.addOnScrollListener( new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(MainActivity.this, "onLoadMore", Toast.LENGTH_SHORT).show();
//                start+=10;
//                nextPage("Next page");
            }
        });



    }




    public void search(View view) {

        closeKeyboard();

        String query = queryView.getText().toString();

        //reset everything
        recipeItems = new ArrayList<>();
        recyclerViewQuery.setAdapter(null);
        start = 1;


        //populating screen by calling api
        RemoteDataSource.getRecipe(query, start, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((new Observer<Example>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                                Log.d(TAG, "onSubscribe: ");
                                state.setText("Searching...");

                            }

                            @Override
                            public void onNext(Example example) {

                                int r = 0;
                            Log.d(TAG, "onNext: ");
                            Log.d(TAG, "onNext: query: " + example.getHits());
                            Log.d(TAG, "onNext: total count: " + example.getCount());
    //                        Log.d(TAG, "onNext: get start: " + recipeLookup.getBookmarked());

                            }

                            @Override
                            public void onError(Throwable e) {

                                Log.d(TAG, "onError: " + e.toString());
                                state.setText("No Results.");
                                e.printStackTrace();

                            }

                            @Override
                            public void onComplete() {

                                Log.d(TAG, "onComplete: ");
//
                                //set adapter on complete
                                recipeListAdapter = new RecipeListAdapter(recipeItems);
                                recyclerViewQuery.setAdapter(recipeListAdapter);
//
//                        String s = (recipeItems.size() > 0) ? "" : "No Results";
//                        state.setText(s);

                            }
                        }));
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//   //                     Log.d(TAG, "onSubscribe: ");
//                        state.setText("Searching...");
//                    }
//
//                    @Override
//                    public void onNext(Recipe recipe) {
//
//                        int r = 0;
//                        Log.d(TAG, "onNext: ");
//                        Log.d(TAG, "onNext: query: " + recipe.getSource());
//                        Log.d(TAG, "onNext: total yield: " + recipe.getYield());
////                        Log.d(TAG, "onNext: get start: " + recipeLookup.getBookmarked());
//
//                    }
//
//
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.d(TAG, "onError: " + e.toString());
//                        state.setText("No Results.");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//
//                        //set adapter on complete
//                        recipeListAdapter = new RecipeListAdapter(recipeItems);
//                        recyclerViewQuery.setAdapter(recipeListAdapter);
//
//                        String s = (recipeItems.size() > 0) ? "" : "No Results";
//                        state.setText(s);
//                    }
//                }));

    }


    public void closeKeyboard() {
        View v = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


//    public void nextPage( String query ) {
//        RemoteDataSource.getRecipe( query, start )
//                .observeOn( AndroidSchedulers.mainThread() )
//                .subscribeOn(  Schedulers.io() )
//                .subscribe(new Observer<Recipe>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        Log.d(TAG, "onSubscribe: ");
//                        state.setText( "Searching...");
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Recipe recipeLookup) {
//                        for( Ingredient r : recipeLookup.getIngredients() )
//                            recipeLookup.getLabel( r );
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Log.d(TAG, "onError: " + e.toString());
//                        state.setText( "No Results.");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//
//                        //setting adapter
//                        recipeListAdapter = new RecipeListAdapter(recipeItems);
//                        recyclerViewQuery.setAdapter(recipeListAdapter);
//
//                        String s = (recipeItems.size() > 0) ? "" : "No Results";
//                        state.setText(s);
//                    }
//                });
//    }
}
