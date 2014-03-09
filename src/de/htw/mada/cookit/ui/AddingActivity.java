package de.htw.mada.cookit.ui;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;
import de.htw.mada.cookit.utils.HttpConnectionManager;
import de.htw.mada.cookit.utils.ImageUtil;
import de.htw.mada.cookit.utils.KochBuchResponseHandler;

public class AddingActivity extends SherlockFragmentActivity implements OnClickListener{

	EditText dishName, author, comment, duration, ingredients, directions, notes;
	Spinner type, difficulty;
	Button saveButton, setImage;
	ImageView dishPic;
	String[] input = new String[11];
	String[] temp = new String[9];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adding_activity);
		setViews();

		byte[] byteArray;
		if (savedInstanceState != null) {
			byteArray = savedInstanceState.getByteArray("image");
			if (byteArray != null) {
				Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				dishPic.setImageBitmap(bmp);
			}
		} 
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {

		if (dishPic.getDrawable() != null) {
			Bitmap bitmap = ((BitmapDrawable) dishPic.getDrawable()).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();	
			savedInstanceState.putByteArray("image", byteArray);
		}

		super.onSaveInstanceState(savedInstanceState);
	}

	private void setViews() {
		dishName = (EditText) findViewById(R.id.editText_name);
		author = (EditText) findViewById(R.id.editText_author);
		comment = (EditText) findViewById(R.id.editText_comment);
		duration = (EditText) findViewById(R.id.editText_duration);
		ingredients = (EditText) findViewById(R.id.editText_ingredients);
		directions = (EditText) findViewById(R.id.editText_directions);
		notes = (EditText) findViewById(R.id.editText_notes);

		type = (Spinner) findViewById(R.id.spinner_type);
		difficulty = (Spinner) findViewById(R.id.spinner_difficulty);

		saveButton = (Button) findViewById(R.id.button_save);
		setImage = (Button) findViewById(R.id.button_set_image);

		dishPic = (ImageView) findViewById(R.id.imageView);

		saveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id) {
		case R.id.button_save:
			input[0] = dishName.getText().toString();
			if (input[0].equals("")) {
				Toast toast = Toast.makeText(AddingActivity.this, Constant.FILL_NAME, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, -180);
				toast.show();
				break;
			}
			input[1] = author.getText().toString();
			if (input[1].equals("")) {
				input[1] = Constant.DEFAULT_AUTHOR;
			}
			input[2] = comment.getText().toString();
			input[3] = type.getSelectedItem().toString();
			input[4] = difficulty.getSelectedItem().toString();
			input[5] = duration.getText().toString();
			input[6] = ingredients.getText().toString();
			if (input[6].equals("")) {
				Toast.makeText(AddingActivity.this, Constant.FILL_INGREDIENTS, Toast.LENGTH_SHORT).show();
				break;
			}
			input[7] = directions.getText().toString();
			if (input[7].equals("")) {
				Toast.makeText(AddingActivity.this, Constant.FILL_DIRECTIONS, Toast.LENGTH_SHORT).show();
				break;
			}
			input[8] = notes.getText().toString();
			if (mDishPicBitmap != null) {
				input[9] = ImageUtil.convertBitmapToString(mDishPicBitmap);
				input[10] = System.currentTimeMillis() + ".jpg";
			}
			DBInsert task = new DBInsert();
			task.execute(input);
						
			break;
		}

	}

	public void onClickSetImage(View v) {
		selectImage();
	}

	private class DBInsert extends AsyncTask<String, Long, KochBuchResponseHandler> {

		@Override
		protected KochBuchResponseHandler doInBackground(String... input) {
			HttpConnectionManager cm = HttpConnectionManager.getInstance();
			KochBuchResponseHandler result = null;
			if (mDishPicBitmap != null) {
				cm.request(Constant.UPLOAD_PICTURE, input, Constant.EMPTY_ID);
			}
			try {
				result  = cm.request(Constant.INSERT_RECIPE, input, Constant.EMPTY_ID);	
			} catch (Exception e) {
				// TODO: handle exception
			}

			return result;
		}
		@Override
		protected void onPostExecute(KochBuchResponseHandler result) {
			if (result == null) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(AddingActivity.this);
				builder1.setTitle("Failed !");
				builder1.setMessage("No connection to database");
				builder1.setCancelable(true);
				builder1.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alert11 = builder1.create();
				alert11.show();
			}

			else if(result.getRc() == Constant.SUCCESS)
			{
				Toast toast = Toast.makeText(AddingActivity.this, Constant.UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
				Intent intent = new Intent(AddingActivity.this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			else if(result.getRc() == Constant.MYSQL_CONNECT_ERROR)
			{
				Toast toast = Toast.makeText(AddingActivity.this, "Error", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}

		}
	}

	private void selectImage() {
		final CharSequence[] items = { Constant.TAKE_PHOTO, Constant.FROM_GALLERY,
				Constant.CANCEL };

		AlertDialog.Builder builder = new AlertDialog.Builder(AddingActivity.this);
		builder.setTitle(Constant.ADD_PHOTO);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals(Constant.TAKE_PHOTO)) {
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					startActivityForResult(cameraIntent, Constant.REQUEST_CAMERA); 
				} else if (items[item].equals(Constant.FROM_GALLERY)) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select File"), Constant.SELECT_FILE);
				} else if (items[item].equals(Constant.CANCEL)) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			if (requestCode == Constant.REQUEST_CAMERA) {

				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				this.mDishPicBitmap = bitmap;
				dishPic.setImageBitmap(bitmap);

			} else if (requestCode == Constant.SELECT_FILE) {
				Uri selectedImageUri = data.getData();

				Bitmap bitmap = null;
				try {
					//					bitmap = decodeUri(selectedImageUri);
					bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
					this.mDishPicBitmap = bitmap;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dishPic.setImageBitmap(bitmap);
			}
		}
	}

	private Bitmap mDishPicBitmap = null;

	@SuppressWarnings("unused")
	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE
					|| height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

	}

}
