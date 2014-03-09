package de.htw.mada.cookit;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class CookitApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 1) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .discCacheSize(50 * 1024 * 1024)
        .discCacheFileCount(100)
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
        .build();
		ImageLoader.getInstance().init(config);
	}
	
}
