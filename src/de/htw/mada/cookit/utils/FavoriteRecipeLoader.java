package de.htw.mada.cookit.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

public class FavoriteRecipeLoader extends AsyncTaskLoader<Cursor>{

	private String mSearchValue;
	private boolean fetchAll;
	private Context context;
	private int id;

	public FavoriteRecipeLoader(Context context, String searchValue, boolean fetchAll, int id) {
		super(context);
		this.context = context;
		this.mSearchValue = searchValue;
		this.fetchAll = fetchAll;
		this.id = id;
	}

	@SuppressWarnings("resource")
	@Override
	public Cursor loadInBackground() {
		FavoriteHandler fh = new FavoriteHandler(context);
		Cursor result = null;
		if (fetchAll) {
			// load all recipes
			result = fh.getAllFavorite();

			if (result.getCount() == 0) {
				result = null;
			}

			if (!TextUtils.isEmpty(mSearchValue) && result != null) {
				mSearchValue = mSearchValue.toLowerCase();
				MatrixCursor filteredCursor = new MatrixCursor(FavoriteRecipeLoader.RecipeQuery.PROJECTION);
				while(result.moveToNext()) {
					if (result.getString(FavoriteRecipeLoader.RecipeQuery.DISHNAME).toLowerCase().contains(mSearchValue)) {
						filteredCursor.addRow(new Object[] {
								result.getInt(FavoriteRecipeLoader.RecipeQuery.ID), result.getInt(FavoriteRecipeLoader.RecipeQuery.TRUE_ID),
								result.getString(FavoriteRecipeLoader.RecipeQuery.DISHNAME), result.getString(FavoriteRecipeLoader.RecipeQuery.AUTHOR),
								result.getString(FavoriteRecipeLoader.RecipeQuery.COMMENT), result.getString(FavoriteRecipeLoader.RecipeQuery.TYPE),
								result.getString(FavoriteRecipeLoader.RecipeQuery.DIFFICULTY), result.getString(FavoriteRecipeLoader.RecipeQuery.DURATION),
								result.getString(FavoriteRecipeLoader.RecipeQuery.INGREDIENTS), result.getString(FavoriteRecipeLoader.RecipeQuery.DIRECTIONS),
								result.getString(FavoriteRecipeLoader.RecipeQuery.NOTES), result.getString(FavoriteRecipeLoader.RecipeQuery.IMAGEPATH)
						});
					}
				}

				result = filteredCursor;
			}
		}
		else if (!fetchAll) {
			Cursor cursor = fh.getFavRecipe(id);
			if (cursor.getCount() == 0) {
				cursor = null;
			}
			result = cursor;
		}

		return result;
	}

	public interface RecipeQuery {
		String[] PROJECTION = {"_id", "trueId", "dishName", "author", "comment", "type", "difficulty", "duration",
				"ingredients", "directions", "notes", "imagePath"};

		int ID = 0;
		int TRUE_ID = 1;
		int DISHNAME = 2;
		int AUTHOR = 3;
		int COMMENT = 4;
		int TYPE = 5;
		int DIFFICULTY = 6;
		int DURATION = 7;
		int INGREDIENTS = 8;
		int DIRECTIONS = 9;
		int NOTES = 10;
		int IMAGEPATH = 11;
	}
}
