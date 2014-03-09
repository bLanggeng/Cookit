package de.htw.mada.cookit.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;

import de.htw.mada.cookit.R;
import de.htw.mada.cookit.utils.Constant;
import de.htw.mada.cookit.utils.RecipeLoader;

public class RecipeListFragment extends SherlockListFragment implements LoaderCallbacks<Cursor>{

	private RecipeListAdapter mListAdapter;
	private AQuery aQuery;
	EditText etSearch;
	private String mCurrentFilter;
	TextView tvEmptyList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mListAdapter = new RecipeListAdapter(getActivity());
		setListAdapter(mListAdapter);

		aQuery = new AQuery(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_list_with_empty_container, null);
		tvEmptyList = (TextView) root.findViewById(R.id.tv_emptyList);
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
		inflater.inflate(R.menu.all_recipe, menu);
		
		mCurrentFilter = "";

		MenuItem item = menu.findItem(R.id.action_search);
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
					Loader<Cursor> loader = getLoaderManager().restartLoader(0, null, RecipeListFragment.this);
					loader.forceLoad();
					return true;
				}
			});
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(getActivity(), AddingActivity.class);
			startActivity(intent);
			return true;

		case R.id.action_refresh:
			Loader<Cursor> loader = getLoaderManager().getLoader(0);
			loader.reset();
			loader.forceLoad();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}



	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new RecipeLoader(getActivity(), mCurrentFilter, true, 0);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor == null) {

			Toast toast = Toast.makeText(getActivity(), Constant.NO_CONNECTION, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();	
		}
		
		if (cursor != null && TextUtils.isEmpty(mCurrentFilter)) {
			tvEmptyList.setText(Constant.EMPTY_LIST);
			tvEmptyList.setVisibility(View.VISIBLE);

		}

		if (cursor != null && !TextUtils.isEmpty(mCurrentFilter)) {
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

	private class RecipeListAdapter extends CursorAdapter {

		public RecipeListAdapter(Context context) {
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

			final int recipeId = cursor.getInt(RecipeLoader.RecipeQuery.ID);
			final String imagePath = cursor.getString(RecipeLoader.RecipeQuery.IMAGEPATH);

			final String dishName = cursor.getString(RecipeLoader.RecipeQuery.DISHNAME);
			tvDishName.setText(dishName);
			tvAuthor.setText("By : " + cursor.getString(RecipeLoader.RecipeQuery.AUTHOR));
			tvComment.setText(cursor.getString(RecipeLoader.RecipeQuery.COMMENT));
			tvType.setText("Type : " + cursor.getString(RecipeLoader.RecipeQuery.TYPE));

			if (!TextUtils.isEmpty(imagePath)) {

				aQuery.id(ivDishPic).image(Constant.IP_ADDRESS +imagePath , true, true);


			} else {
				ivDishPic.setImageResource(R.drawable.ic_cookit_icon);
			}
			clickableView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle dataBundle = new Bundle();
					dataBundle.putInt(Constant.ID, recipeId);
					dataBundle.putBoolean(Constant.FROM_FAVORITE, false);
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		//clean the file cache when root activity exit
		//the resulting total cache size will be less than 3M   
		if(getActivity().isTaskRoot()){
			AQUtility.cleanCacheAsync(getActivity());
		}
	}

	

}
