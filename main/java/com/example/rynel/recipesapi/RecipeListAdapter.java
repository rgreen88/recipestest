package com.example.rynel.recipesapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rynel.recipesapi.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rynel on 10/15/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{

    //create an array list walmartItemList
    List<Recipe> recipeItems = new ArrayList<>();
    Context context;

    public RecipeListAdapter(List<Recipe> recipeItemList){

        this.recipeItems = recipeItemList;

    }


    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Recipe recipe = recipeItems.get(position);

        Glide.with( context )
                .load( recipe.getImage() )
                .into( holder.itemImage );



        holder.recipe.setText((CharSequence) recipe.getDietLabels());
    }

    @Override
    public int getItemCount() {

        return recipeItems.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView recipe, recipeDescription, recipeBought;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.ivThumbnail);
            recipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            recipe = itemView.findViewById(R.id.tvRecipe);
            recipeBought = itemView.findViewById(R.id.tvBought);

        }
    }
}
