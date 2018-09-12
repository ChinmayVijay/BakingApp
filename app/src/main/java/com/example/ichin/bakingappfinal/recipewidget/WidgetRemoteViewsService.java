package com.example.ichin.bakingappfinal.recipewidget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ichin.bakingappfinal.R;
import com.example.ichin.bakingappfinal.data.RecipeDatabase;
import com.example.ichin.bakingappfinal.data.RecipeIngredientsColumns;
import com.example.ichin.bakingappfinal.data.RecipeListColumns;
import com.example.ichin.bakingappfinal.data.RecipeProvider;
import com.example.ichin.bakingappfinal.utils.SharedPrefUtils;


public class WidgetRemoteViewsService extends RemoteViewsService {

    private static final String[] RECIPE_COLUMNS = {
            RecipeListColumns._ID,
            RecipeListColumns.RECIPE_NAME,
            RecipeListColumns.SERVING_SIZE
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_RECIPE_NAME = 1;
    private static final int INDEX_RECIPE_SERVING_SIZE = 2;

    private static final String[] INGREDIENTS_COLUMNS = {
            RecipeIngredientsColumns.ID,
            RecipeIngredientsColumns.INGREDIENT,
            RecipeIngredientsColumns.MEASUREMENT,
            RecipeIngredientsColumns.QUANTITY,
            RecipeDatabase.RECIPE_INGREDIENTS + "." + RecipeIngredientsColumns.RECIPE_LIST_ID
    };

    private static final int INDEX_INGREDIENT_ID = 0;
    private static final int INDEX_INGREDIENT_INGREDIENT = 1;
    private static final int INDEX_INGREDIENT_MEASUREMENT = 2;
    private static final int INDEX_INGREDIENT_QUANTITY = 3;
    private static final int INDEX_INGREDIENT_RECIPE_ID = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        FavoriteRecipeWidgetProvider.setWidgetText(this, SharedPrefUtils.getSavedIngredientName(this));
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if(data != null){
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(RecipeProvider.RecipeIngredients.CONTENT_URI,
                        INGREDIENTS_COLUMNS,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.favorite_recipe_widget_list_item);

                String name = data.getString(INDEX_INGREDIENT_INGREDIENT);
                String measurement = data.getString(INDEX_INGREDIENT_MEASUREMENT);
                double quantity = data.getDouble(INDEX_INGREDIENT_QUANTITY);

                views.setTextViewText(R.id.widget_ingredient_name, name);
                views.setTextViewText(R.id.widget_ingredient_amount, getString(R.string.ingredient_amount,
                        Double.toString(quantity),measurement));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(),R.layout.favorite_recipe_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (data.moveToPosition(i))
                    return data.getLong(INDEX_INGREDIENT_ID);
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
