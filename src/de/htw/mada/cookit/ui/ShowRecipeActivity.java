package de.htw.mada.cookit.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;

import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;
import de.htw.mada.cookit.utils.FavoriteHandler;
import de.htw.mada.cookit.utils.FavoriteRecipeLoader;
import de.htw.mada.cookit.utils.ImageUtil;
import de.htw.mada.cookit.utils.RecipeLoader;

public class ShowRecipeActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor>{

	private static final int DIALOG_FAVOURITE = 1;
	private static final int DIALOG_UNFAVOURITE = 2;

	TextView dishName, author, comment, duration, ingredients, directions, notes, type, difficulty;
	ImageView dishPic;
	Bitmap bitmap;
	int id;
	boolean fromFav;
	MenuItem item;
	String sDishName;
	String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_activity);
		Bundle recdData = getIntent().getExtras();
		this.id = recdData.getInt(Constant.ID);
		this.fromFav = recdData.getBoolean(Constant.FROM_FAVORITE);

		setViews();

		Loader<Cursor> loader = getSupportLoaderManager().initLoader(0, null, this);
		loader.forceLoad();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.detail_recipe, menu);
		FavoriteHandler fh = new FavoriteHandler(this);

		if (fh.checkFavorite(id)) {
			MenuItem mi = menu.getItem(0);
			mi.setIcon(R.drawable.ic_favourited);
			mi.setTitle(R.string.favourited);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Handle item selection
		FavoriteHandler fh = new FavoriteHandler(this);
		boolean inFav = fh.checkFavorite(id);
		switch (item.getItemId()) {
		case R.id.action_favourite:
			this.item = item;
			if (inFav && !fromFav) {

			}
			else if (inFav && fromFav) {
				showDialog(DIALOG_UNFAVOURITE);
			}
			else if (!inFav) {
				showDialog(DIALOG_FAVOURITE);
			}

			return true;			

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_FAVOURITE:
			// Create Dialog for adding to favourite
			builder.setMessage("Add this recipe to favourite?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes", new OkOnClickListenerFav());
			builder.setNegativeButton("Cancel", new CancelOnClickListener());
			AlertDialog dialog1 = builder.create();
			dialog1.show();
			break;

		case DIALOG_UNFAVOURITE:
			// Create Dialog for removing from favourite
			builder.setMessage("remove this recipe from favourite?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes", new OkOnClickListenerUnfav());
			builder.setNegativeButton("Cancel", new CancelOnClickListener());
			AlertDialog dialog2 = builder.create();
			dialog2.show();
			break;
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
	DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
		}
	}

	private final class OkOnClickListenerFav implements
	DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String imagePath = "";
			bitmap = ((BitmapDrawable)dishPic.getDrawable()).getBitmap();
			if (bitmap != null) {
				imagePath = ImageUtil.saveImageToFile(bitmap, id);
			}

			FavoriteHandler fh = new FavoriteHandler(ShowRecipeActivity.this);
			fh.insertFavorite(id, dishName.getText().toString(), author.getText().toString(), comment.getText().toString(),
					type.getText().toString(), difficulty.getText().toString(), "" + duration.getText().toString(),
					ingredients.getText().toString(), directions.getText().toString(), "" + notes.getText().toString(),
					imagePath);
			Toast toast = Toast.makeText(ShowRecipeActivity.this, Constant.MESSAGE_ADD_FAVORITE, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();

			item.setIcon(R.drawable.ic_favourited);
			item.setTitle(R.string.favourited);
		}
	} 

	private final class OkOnClickListenerUnfav implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			FavoriteHandler fh = new FavoriteHandler(ShowRecipeActivity.this);
			fh.removeFavorite(id);

			Toast toast = Toast.makeText(ShowRecipeActivity.this, Constant.MESSAGE_REMOVE_FAVORITE, Toast.LENGTH_SHORT);
			TextView v = (TextView)toast.getView().findViewById(android.R.id.message);
			v.setGravity(Gravity.CENTER);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			Bundle dataBundle = new Bundle();
			dataBundle.putBoolean(Constant.TO_FAVOURITE_LIST, true);
			Intent intent = new Intent(ShowRecipeActivity.this, HomeActivity.class);
			intent.putExtras(dataBundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	private void setViews() {
		dishName = (TextView) findViewById(R.id.editText_name);
		author = (TextView) findViewById(R.id.editText_author);
		comment = (TextView) findViewById(R.id.editText_comment);
		duration = (TextView) findViewById(R.id.editText_duration);
		ingredients = (TextView) findViewById(R.id.editText_ingredients);
		directions = (TextView) findViewById(R.id.editText_directions);
		notes = (TextView) findViewById(R.id.editText_notes);
		type = (TextView) findViewById(R.id.textView_type2);
		difficulty = (TextView) findViewById(R.id.textView_difficulty2);

		dishPic = (ImageView) findViewById(R.id.imageView);
	}

	private void setText(TextView et, String text) {
		et.setText(text);
	}

	public void fillViews(String dishName, String author, String comment, String type, String difficulty,
			String duration, String ingredients, String directions, String notes, String imagePath) {

		setText(this.dishName,dishName);
		setText(this.author, author);
		setText(this.comment, comment);
		setText(this.type, type);
		setText(this.difficulty, difficulty);
		setText(this.duration, duration);
		setText(this.ingredients, ingredients);
		setText(this.directions, directions);
		setText(this.notes, notes);

		AQuery aQuery = new AQuery(ShowRecipeActivity.this);
		if (!fromFav) {	
			if (!imagePath.equals(Constant.IP_ADDRESS)) {
				//				imageLoader.displayImage(imagePath, dishPic);
				aQuery.id(dishPic).image(imagePath , true, true);
			}
			else {
				//				dishPic.setImageResource(R.drawable.ic_launcher);
				aQuery.id(dishPic).image(R.drawable.ic_cookit_icon);
			}
		}
		else {
			bitmap = BitmapFactory.decodeFile(imagePath);
			dishPic.setImageBitmap(bitmap);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Loader<Cursor> loader = null;
		if (fromFav) {
			loader = new FavoriteRecipeLoader(this, "", false, id);
		}
		else if (!fromFav){
			loader = new RecipeLoader(this, "", false, id);	
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();

			if (!fromFav) {
				sDishName = cursor.getString(RecipeLoader.RecipeQuery.DISHNAME);
				imagePath = Constant.IP_ADDRESS + cursor.getString(RecipeLoader.RecipeQuery.IMAGEPATH);
				fillViews(sDishName, 
						cursor.getString(RecipeLoader.RecipeQuery.AUTHOR),
						cursor.getString(RecipeLoader.RecipeQuery.COMMENT),
						cursor.getString(RecipeLoader.RecipeQuery.TYPE),
						cursor.getString(RecipeLoader.RecipeQuery.DIFFICULTY), 
						cursor.getString(RecipeLoader.RecipeQuery.DURATION),
						cursor.getString(RecipeLoader.RecipeQuery.INGREDIENTS), 
						cursor.getString(RecipeLoader.RecipeQuery.DIRECTIONS),
						cursor.getString(RecipeLoader.RecipeQuery.NOTES),
						imagePath);

			}
			else if (fromFav) {
				sDishName = cursor.getString(FavoriteRecipeLoader.RecipeQuery.DISHNAME);
				imagePath = cursor.getString(FavoriteRecipeLoader.RecipeQuery.IMAGEPATH);
				fillViews(sDishName, 
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.AUTHOR),
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.COMMENT),
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.TYPE),
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.DIFFICULTY), 
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.DURATION),
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.INGREDIENTS), 
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.DIRECTIONS),
						cursor.getString(FavoriteRecipeLoader.RecipeQuery.NOTES), 
						imagePath);
			}

			dishPic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle dataBundle = new Bundle();
					dataBundle.putString(Constant.DISH_NAME, sDishName);
					dataBundle.putString(Constant.IMAGE_PATH, imagePath);
					dataBundle.putBoolean(Constant.FROM_FAVORITE, fromFav);
					Intent i = new Intent(ShowRecipeActivity.this,ViewImageActivity.class);
					i.putExtras(dataBundle);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			});
		} 
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}
}
