package de.htw.mada.cookit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author bawonolanggeng
 *
 *	Diese Klasse ist Extendklasse von SQLiteOpenHelper.
 *	SQLiteOpenHelper ist eine Hilfeklasse zur Erstellung der Datenbank für Todoliste.
 */
public class DBFavoriteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "bawonski";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "CREATE TABLE favorite" +
			"(_id integer primary key autoincrement, true_id integer, dish_name text, author text, comment text," +
			"type text, difficulty text, duration text, ingredients text, directions text, notes text, image text);";
	
	
	public DBFavoriteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS favorite");
		onCreate(db);
	}

}