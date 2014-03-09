package de.htw.mada.cookit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;

public class HomeActivity extends BaseActivity implements TabListener, OnPageChangeListener{

	private ViewPager mViewPager;
	private boolean toFavTab = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_home);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		if (mViewPager != null) {
			mViewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
			mViewPager.setOnPageChangeListener(this);

			final ActionBar actionBar = getSupportActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			actionBar.addTab(actionBar.newTab()
					.setIcon(R.drawable.ic_home)
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab()
					.setIcon(R.drawable.ic_favorite)
					.setTabListener(this));

		}
		Bundle recdData = getIntent().getExtras();
		if (recdData != null) {
			toFavTab = recdData.getBoolean(Constant.TO_FAVOURITE_LIST);
		}
		if (toFavTab) {
			getSupportActionBar().setSelectedNavigationItem(1);
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		getSupportActionBar().setSelectedNavigationItem(position);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	private class HomePagerAdapter extends FragmentPagerAdapter {

		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new RecipeListFragment();
			case 1:
				return new FavoriteRecipeListFragment();

			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}
}
