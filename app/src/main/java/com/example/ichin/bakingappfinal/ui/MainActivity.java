package com.example.ichin.bakingappfinal.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ichin.bakingappfinal.R;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Refresh recipe list in case database was updated
        RecipeFragment fragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipe);
        fragment.refreshRecipeList();
    }

}
