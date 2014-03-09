package de.htw.mada.cookit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

public class RecipeLoader extends AsyncTaskLoader<Cursor>{

	private String mSearchValue;
	private boolean fetchAll;
	private int id;

	public RecipeLoader(Context context, String searchValue, boolean fetchAll, int id) {
		super(context);
		this.mSearchValue = searchValue;
		this.fetchAll = fetchAll;
		this.id = id;
	}

	@SuppressWarnings("resource")
	@SuppressLint("DefaultLocale")
	@Override
	public Cursor loadInBackground() {
		KochBuchResponseHandler result = null;
		Cursor cursor = null;
		if (fetchAll) {
			// load all recipes
			try {
				result = HttpConnectionManager.getInstance().request(Constant.GET_ALL_RECIPEES, null, 0);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if (result != null) {
				cursor = result.getCursor();
			}
			
			// load recipe according to query
			if (!TextUtils.isEmpty(mSearchValue) && cursor != null) {
				mSearchValue = mSearchValue.toLowerCase();
				MatrixCursor filteredCursor = new MatrixCursor(RecipeQuery.PROJECTION);
				while(cursor.moveToNext()) {
					if (cursor.getString(RecipeLoader.RecipeQuery.DISHNAME).toLowerCase().contains(mSearchValue)) {
						filteredCursor.addRow(new Object[] {
								cursor.getInt(RecipeQuery.ID), cursor.getString(RecipeQuery.DISHNAME),
								cursor.getString(RecipeQuery.AUTHOR), cursor.getString(RecipeQuery.COMMENT),
								cursor.getString(RecipeQuery.TYPE), cursor.getString(RecipeQuery.DIFFICULTY),
								cursor.getString(RecipeQuery.DURATION), cursor.getString(RecipeQuery.INGREDIENTS),
								cursor.getString(RecipeQuery.DIRECTIONS), cursor.getString(RecipeQuery.NOTES),
								cursor.getString(RecipeQuery.IMAGEPATH)
						});
					}
				}

				cursor = filteredCursor;
			}
		}
		else if (!fetchAll) {

			result = HttpConnectionManager.getInstance().request(Constant.GET_A_RECIPE, null, id);

			cursor =  result.getCursor();
		}
		return cursor;
	}

	public interface RecipeQuery {
		String[] PROJECTION = {"_id", "dishName", "author", "comment", "type", "difficulty", "duration",
				"ingredients", "directions", "notes", "imagePath"};

		int ID = 0;
		int DISHNAME = 1;
		int AUTHOR = 2;
		int COMMENT = 3;
		int TYPE = 4;
		int DIFFICULTY = 5;
		int DURATION = 6;
		int INGREDIENTS = 7;
		int DIRECTIONS = 8;
		int NOTES = 9;
		int IMAGEPATH = 10;
	}
}
