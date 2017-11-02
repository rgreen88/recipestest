package com.example.rynel.recipesapi;

import android.content.Context;
import android.content.res.Configuration;
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
import com.example.rynel.recipesapi.model.Hit;
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
    ArrayList<Recipe> recipeItems = new ArrayList<Recipe>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;
    private RecipeListAdapter recipeListAdapter;
    Button searchBtn;
    EditText queryView;
    TextView state;

    private String query;
    private int from, to;

    //starting position for search
    int start = 1;
    int cols = 3;

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
        state = findViewById(R.id.tvState);

        query = getIntent().getStringExtra(getString(R.string.search_for_recipes));
        setTitle(query);

        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
            cols = 5;


        //layout in rv
        GridLayoutManager manager = new GridLayoutManager(this, cols);
        recyclerViewQuery.setLayoutManager(manager);
        recyclerViewQuery.setItemAnimator(new DefaultItemAnimator());

        recyclerViewQuery.addOnScrollListener( new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(MainActivity.this, "onLoadMore", Toast.LENGTH_SHORT).show();
//                start+=10;
//                nextPage("Next page");
            }
        });

        search(query, 0, 20);

    }



    public void search(String q, int from, int to) {
        state.setText("Searching...");
    }

    public void search(View view) {

        closeKeyboard();

        String query = queryView.getText().toString();

        //reset everything
        recipeItems = new ArrayList<Recipe>();
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

                                Log.d(TAG, "onNext: ");
                                Log.d(TAG, "onNext: query: " + example.getHits());
                                Log.d(TAG, "onNext: total count: " + example.getCount());


                                if (example.getCount() > 0) {
                                    List<Hit> hitList = example.getHits();

                                    for (Hit h : hitList) {
                                        recipeItems.add(h.getRecipe());

                                    }
                                }
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

                                //set adapter on complete
                                recipeListAdapter = new RecipeListAdapter(recipeItems);
                                recyclerViewQuery.setAdapter(recipeListAdapter);

                                String s = (recipeItems.size() > 0) ? "" : "No Results";
                                state.setText(s);


                                //fixme: missing display for recyclerview ---fixed
                                //fixme: add ls orientation and add 2 columns to expand view -- fixed
                                //fixme: add save config//make it work after landscape change
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


//    public void nextPage( String query, int to ) {
//        RemoteDataSource.getRecipe( query, start, to )
//                .observeOn( AndroidSchedulers.mainThread() )
//                .subscribeOn(  Schedulers.io() )
//                .subscribe(new Observer<Example>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        Log.d(TAG, "onSubscribe: ");
//                        state.setText( "Searching...");
//                    }
//
//                    @Override
//                    public void onNext(Example example) {
//
//                        for( Hit hit : example.getHits() )
//                            example.getFrom( hit );
//
//                    }
//
////                    @Override
////                    public void onNext(@NonNull Recipe recipeLookup) {
////                        for( Ingredient r : recipeLookup.getIngredients() )
////                            recipeLookup.getLabel( r );
////                    }
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
