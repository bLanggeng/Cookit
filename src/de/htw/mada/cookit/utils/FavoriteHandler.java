package de.htw.mada.cookit.utils;

import android.content.Context;
import android.database.Cursor;

public class FavoriteHandler {

	DBFavoriteAdapter db;

	public FavoriteHandler(Context c) {
		this.db = new DBFavoriteAdapter(c);
	}

	public long insertFavorite(int trueId, String dishName,String author, String comment, String type,
			String difficulty, String duration, String ingredients, String directions, String notes, String imagePath) {
		db.open();
		long result = db.insert(trueId, dishName, author, comment, type, difficulty, duration, ingredients, directions, notes, imagePath);
		db.close();
		return result;
	}

	public Cursor getFavRecipe(long trueId) {
		db.open();
		Cursor cursor = db.fetch(trueId);
		//db.close();

		return cursor;
	}

	public Cursor getAllFavorite() {

		db.open();
		Cursor cursor = db.fetchAll();
		//		db.close();
		return cursor;
	}

	public boolean checkFavorite(long trueId) {
		db.open();
		Cursor c = db.fetch(trueId);
		boolean result = c.moveToFirst();
		db.close();
		return result;
	}

	public boolean removeFavorite(long trueId) {
		db.open();
		boolean result = db.delete(trueId);
		db.close();

		return result;
	}
}
