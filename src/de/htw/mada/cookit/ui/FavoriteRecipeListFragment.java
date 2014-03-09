package de.htw.mada.cookit.ui;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.androidquery.AQuery;

import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;
import de.htw.mada.cookit.utils.FavoriteRecipeLoader;

public class FavoriteRecipeListFragment extends SherlockListFragment implements LoaderCallbacks<Cursor>{

	private FavoriteRecipeListAdapter mListAdapter;
	private AQuery aQuery;
	private String mCurrentFilter = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		mListAdapter = new FavoriteRecipeListAdapter(getActivity());
		setListAdapter(mListAdapter);
		aQuery = new AQuery(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_list_with_empty_container, null);
		Loader<Cursor> loader = getLoaderManager().initLoader(0, null, this);
		loader.forceLoad();
		return root;
	}

	@Override
	public void onStart() {
		super.onStart();
		Loader<Cursor> loader = getLoaderManager().initLoader(0, null, this);
		loader.forceLoad();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fav_recipe, menu);

		MenuItem item = menu.findItem(R.id.action_searchFav);
		SearchView searchView = (SearchView) item.getActionView();

		if (searchView != null) {
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String query) {
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					mCurrentFilter = !TextUtils.isEmpty(newText) ? newText : "";
					Loader<Cursor> loader = getLoaderManager().restartLoader(0, null, FavoriteRecipeListFragment.this);
					loader.forceLoad();
					return true;
				}
			});
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new FavoriteRecipeLoader(getActivity(), mCurrentFilter, true, 0);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		TextView tvEmptyList = (TextView)getView().findViewById(R.id.tv_emptyList);
		if (cursor == null) {

			tvEmptyList.setText(Constant.EMPTY_LIST);
			tvEmptyList.setVisibility(View.VISIBLE);

		}

		if (cursor != null && cursor.getCount() == 0) {
			tvEmptyList.setText(Constant.SEARCH_NOT_FOUND);
			tvEmptyList.setVisibility(View.VISIBLE);
		}
		
		if (cursor != null && cursor.getCount() != 0) {
			if (tvEmptyList != null) {
				tvEmptyList.setVisibility(View.GONE);
			}
		}

		mListAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	private class FavoriteRecipeListAdapter extends CursorAdapter {

		public FavoriteRecipeListAdapter(Context context) {
			super(context, null, false);
		}

		@Override
		public void bindView(View view, Context arg1, final Cursor cursor) {
			TextView tvDishName = (TextView) view.findViewById(R.id.listitem_dishname);
			TextView tvAuthor = (TextView) view.findViewById(R.id.listitem_author);
			TextView tvComment = (TextView) view.findViewById(R.id.listitem_comment);
			TextView tvType = (TextView) view.findViewById(R.id.listitem_type);
			ImageView ivDishPic = (ImageView) view.findViewById(R.id.listitem_image);
			View clickableView = view.findViewById(R.id.list_item_clickable_view);

			final int favRecipeId = cursor.getInt(FavoriteRecipeLoader.RecipeQuery.TRUE_ID);
			final String imagePath = cursor.getString(FavoriteRecipeLoader.RecipeQuery.IMAGEPATH);

			tvDishName.setText(cursor.getString(FavoriteRecipeLoader.RecipeQuery.DISHNAME));
			tvAuthor.setText("By : " + cursor.getString(FavoriteRecipeLoader.RecipeQuery.AUTHOR));
			tvComment.setText(cursor.getString(FavoriteRecipeLoader.RecipeQuery.COMMENT));
			tvType.setText("Type : " + cursor.getString(FavoriteRecipeLoader.RecipeQuery.TYPE));

			if (!TextUtils.isEmpty(imagePath)) {
				
				File file = new File(imagePath);        
				aQuery.id(ivDishPic).image(file, 300);
				
			} else {
				ivDishPic.setImageResource(R.drawable.ic_cookit_icon);
			}
			
			clickableView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Bundle dataBundle = new Bundle();
					dataBundle.putInt(Constant.ID, favRecipeId);
					dataBundle.putBoolean(Constant.FROM_FAVORITE, true);
					Intent i = new Intent(getActivity(),ShowRecipeActivity.class);
					i.putExtras(dataBundle);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			});
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return getActivity().getLayoutInflater().inflate(R.layout.list_item, parent, false); 
		}

	}
}
