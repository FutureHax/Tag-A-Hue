package com.t3hh4xx0r.tag_a_hue;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

import com.t3hh4xx0r.openhuesdk.sdk.PreferencesManager;
import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;
import com.t3hh4xx0r.tag_a_hue.fragments.BulbFragment;

public class MainActivity extends FragmentActivity implements OnNavigationListener {
	ArrayList<Bulb> bulbs;
	PreferencesManager pMan;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		pMan = new PreferencesManager(this);
		bulbs = pMan.getBulbs();

		String[] bulbNames = new String[bulbs.size()];
		for (int i = 0; i < bulbs.size(); i++) {
			bulbNames[i] = bulbs.get(i).getName();
		}
		setNavItems(bulbNames, -1);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long arg1) {
		Fragment fragment = new BulbFragment();
		Bundle args = new Bundle();
		args.putSerializable("bulb", bulbs.get(position));
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	private void setNavItems(final String[] bulbNames, final int pos) {
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getActionBar().setListNavigationCallbacks(
						new ArrayAdapter<String>(
								MainActivity.this.getActionBar().getThemedContext(),
								android.R.layout.simple_list_item_1,
								android.R.id.text1, bulbNames),
								MainActivity.this);
				if (pos != -1) {
					getActionBar().setSelectedNavigationItem(pos);
				}
			}
		});
	}

	
}
