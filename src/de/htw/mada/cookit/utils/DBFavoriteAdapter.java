package de.htw.mada.cookit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBFavoriteAdapter {

	private static final String DATABASE_TABLE = "favorite";
	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_TRUEID = "true_id";
	public static final String KEY_DISHNAME = "dish_name";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_COMMENT = "comment";
	public static final String KEY_TYPE = "type";
	public static final String KEY_DIFFICULTY = "difficulty";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_INGREDIENTS = "ingredients";
	public static final String KEY_DIRECTIONS = "directions";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_IMAGE = "image";

	SQLiteDatabase mDb;
	Context mCtx;
	DBFavoriteHelper mDbHelper;

	public DBFavoriteAdapter(Context context) {

		this.mCtx = context;
	}

	/**
	 * mDbHelper extends SQLiteOpenHelper. getWritableDatabase() gibt SQLiteDatabse zurueck.
	 * Erstelle und/oder oeffnen ein Datenbank (hier mDbHelper),
	 * das genutzt wird, zum lesen und schreiben von Daten im Datenbank.
	 * 
	 * @return diese Klasse.
	 * @throws SQLException	falls der Datenbank zum Schreiben nicht geoeffnet werden kann.
	 */
	public DBFavoriteAdapter open() throws SQLException {

		mDbHelper = new DBFavoriteHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Diese Methode muss aufgerufen werden wenn der Datenbank (hier mDbHelper)
	 * nicht mehr gebraucht wird.
	 */
	public void close() 
	{
		mDbHelper.close();
	}

	/**
	 * erstelle neue Zeile im Datenbank.
	 */
	public long insert(int trueId, String dishName,String author, String comment, String type,
			String difficulty, String duration, String ingredients, String directions, String notes, String imagePath) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TRUEID, trueId);
		initialValues.put(KEY_DISHNAME, dishName);
		initialValues.put(KEY_AUTHOR, author);
		initialValues.put(KEY_COMMENT, comment);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_DIFFICULTY, difficulty);
		initialValues.put(KEY_DURATION, duration);
		initialValues.put(KEY_INGREDIENTS, ingredients);
		initialValues.put(KEY_DIRECTIONS, directions);
		initialValues.put(KEY_NOTES, notes);
		initialValues.put(KEY_IMAGE, imagePath);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * entferne eine Zeile im Datenbank von Todoliste.
	 * 
	 * @param id	ID der zuentfernende Zeile.
	 * @return		Methode delete von SQLiteDatabase Klasse gibt ein int zurueck.
	 * 				Wenn die Zeile mit dem genannten id existiert, delete Methode gibt zurueck,
	 * 				wie viele Zeile ist entfernt. Dritte Parameter von delete Methode ist das whereClause.
	 * 				Hier wird kein "?" genutzt, dann ist das whereArgs (vierte Parameter) null.
	 */
	public boolean delete(long id) {

		return mDb.delete(DATABASE_TABLE, KEY_TRUEID + " = " + id, null) > 0;
	}

	public Cursor fetchAll() {

		return mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_TRUEID,KEY_DISHNAME,KEY_AUTHOR, KEY_COMMENT,
				KEY_TYPE, KEY_DIFFICULTY, KEY_DURATION, KEY_INGREDIENTS, KEY_DIRECTIONS, KEY_NOTES, KEY_IMAGE},
				null, null, null, null, null);
	}

	public Cursor fetch (long trueId) {
		Cursor c = mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_TRUEID,KEY_DISHNAME,KEY_AUTHOR, KEY_COMMENT,
				KEY_TYPE, KEY_DIFFICULTY, KEY_DURATION, KEY_INGREDIENTS, KEY_DIRECTIONS, KEY_NOTES, KEY_IMAGE}, KEY_TRUEID + " = " + trueId, null, null, null, null);

		return c;
	}
}