package com.example.rynel.recipesapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rynel.recipesapi.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rynel on 11/1/2017.
 */

public class RecipeActivity extends AppCompatActivity {


    private static final String TAG = "RecipeActivity";
    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.tvLabel)
    TextView tvLabel;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.tvURL)
    TextView tvURL;
    @BindView(R.id.lvIngredients)
    ListView lvIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        Recipe r = (Recipe) getIntent().getSerializableExtra("item");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                r.getIngredientLines());
        lvIngredients.setAdapter(adapter);

        Glide.with(this)
                .load(r.getImage())
                .into(ivThumbnail);

        tvLabel.setText( r.getLabel());
        tvSource.setText( r.getSource());
        tvURL.setText( r.getUrl());
    }
}