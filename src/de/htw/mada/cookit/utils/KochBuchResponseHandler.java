package de.htw.mada.cookit.utils;

import java.util.StringTokenizer;

import de.htw.mada.cookit.utils.RecipeLoader.RecipeQuery;

import android.database.Cursor;
import android.database.MatrixCursor;

public class KochBuchResponseHandler {

	private short rc;
	private String data;
	private MatrixCursor cursor;

	public short getRc() {
		return rc;
	}
	public void setRc(short rc) {
		this.rc = rc;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Cursor getCursor() {
		return cursor;
	}
	public KochBuchResponseHandler(short rc, String data) {
		this.rc = rc;
		this.data = data;
	}

	public KochBuchResponseHandler(short rc, MatrixCursor cursor) {
		this.rc = rc;
		this.cursor = cursor;
	}

	public static KochBuchResponseHandler parseResponse(String response) {
		StringTokenizer st = new StringTokenizer(response, Constant.DELIMITER_RECIPE);
		String	rcTemp = st.nextToken();
		StringTokenizer rcToken = new StringTokenizer(rcTemp, Constant.DELIMITER_RC);
		rcToken.nextToken();

		String temp, dishName, author, comment = "", type, difficulty,
				duration = "", ingredients, directions, notes = "", imagePath = "";
		int id;
		MatrixCursor cursor = new MatrixCursor(RecipeQuery.PROJECTION);
		while(st.hasMoreTokens()) {
			temp = st.nextToken();
			StringTokenizer data = new StringTokenizer(temp, Constant.DELIMITER_DATA);
			id = Integer.parseInt(data.nextToken());
			dishName = data.nextToken();
			author = data.nextToken();
			comment = data.nextToken();
			if (comment.length() == 1) {
				comment = Constant.NO_COMMENT;
			}
			else {
				comment = comment.substring(1);
			}
			type = data.nextToken();
			difficulty = data.nextToken();
			duration = data.nextToken();
			if (duration.length() == 1) {
				duration = "";
			}
			else {
				duration = duration.substring(1);
			}
			ingredients = data.nextToken();
			directions = data.nextToken();
			notes = data.nextToken();
			if (notes.length() == 1) {
				notes = "";
			}
			else {
				notes = notes.substring(1);
			}
			imagePath = data.nextToken();
			if (imagePath.length() == 0) {
				imagePath = "";
			}

			cursor.addRow(new Object[] {id, dishName, author, comment, type, difficulty, duration, ingredients, directions, notes, imagePath.substring(1)});
		}
		KochBuchResponseHandler krh = new KochBuchResponseHandler(Short.parseShort(rcToken.nextToken()), cursor);
		return krh;
	}

}
