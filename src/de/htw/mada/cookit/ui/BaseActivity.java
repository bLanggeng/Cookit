package de.htw.mada.cookit.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import de.htw.mada.cookit.utils.AccountUtils;

public abstract class BaseActivity extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		if (!AccountUtils.isAuthenticated()) {
			AccountUtils.startRegistrationProcess(this.getIntent());
			finish();
		}
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}
	
}
