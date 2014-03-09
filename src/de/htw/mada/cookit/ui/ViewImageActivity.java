package de.htw.mada.cookit.ui;

import java.io.File;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;

public class ViewImageActivity extends SherlockFragmentActivity {

	private String imagePath;
	private String dishName;
	private boolean fromFav;
	ImageView ivDishPic;
	TextView tvDishName;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.image_activity);
		
		Bundle bundle = getIntent().getExtras();
		dishName = bundle.getString(Constant.DISH_NAME);
		imagePath = bundle.getString(Constant.IMAGE_PATH);
		fromFav = bundle.getBoolean(Constant.FROM_FAVORITE);
		
		tvDishName = (TextView) findViewById(R.id.tv_image_name);
		tvDishName.setText(dishName);		
		
		ivDishPic = (ImageView) findViewById(R.id.iv_largeImageView);
		if (!fromFav) {
			ImageLoader mImageLoader = ImageLoader.getInstance();
			mImageLoader.displayImage(imagePath, ivDishPic);
			if (imagePath.equals(Constant.IP_ADDRESS)) {
				AQuery aQuery = new AQuery(ViewImageActivity.this);
				aQuery.id(ivDishPic).image(R.drawable.ic_cookit_icon);
			}
		}
		if (fromFav) {
			AQuery aQuery = new AQuery(ViewImageActivity.this);
			File file = new File(imagePath);        
			aQuery.id(ivDishPic).image(file, 300);
		}
		Button closeButton = (Button) findViewById(R.id.close_button); 
		closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
