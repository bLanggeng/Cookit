package de.htw.mada.cookit.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {

	public static String convertBitmapToString(Bitmap input) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		input.compress(Bitmap.CompressFormat.PNG, 0, stream); //compress to which format you want.
		byte [] byteArray = stream.toByteArray();
		String imageString = Base64.encodeBytes(byteArray);
		return imageString;
	}

	public static Bitmap getBitmapFromURL(String imageUrl) {
		try {
			String fullUrl = Constant.IP_ADDRESS + imageUrl;
			URL url = new URL(fullUrl);
			HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

			InputStream is = connection.getInputStream();
			Bitmap img = BitmapFactory.decodeStream(is);

			is.close();

			return img;
		} catch (Exception e) {
			Log.d("error", e.toString());
		}
		return null;

	}

	public static Bitmap getImage(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
	}

	public static String saveImageToFile(Bitmap bitmap, int id) {

		File sdCard = Environment.getExternalStorageDirectory();
		String fullPath = sdCard.getAbsolutePath() + "/cookit/";
		File dir = new File (fullPath);
		dir.mkdirs();
		String fname = "Image-"+ id +".png";
		fullPath += fname;
		File file = new File (dir, fname);
		if (file.exists ()) file.delete (); 
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fullPath;
	}
}
