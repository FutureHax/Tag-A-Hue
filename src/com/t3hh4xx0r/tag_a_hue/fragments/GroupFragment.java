package com.t3hh4xx0r.tag_a_hue.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.t3hh4xx0r.openhuesdk.sdk.PreferencesManager;
import com.t3hh4xx0r.openhuesdk.sdk.bulb.BulbManager;
import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;
import com.t3hh4xx0r.tag_a_hue.R;

public class GroupFragment extends Fragment {
	BulbManager bulbMan;
	String groupName;
	ArrayList<Bulb> bulbList;
	PreferencesManager pMan;

	public GroupFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_group, container,
				false);
		pMan = new PreferencesManager(getActivity());
		bulbMan = new BulbManager(getActivity(), pMan.getBridge());

		groupName = (String) getArguments().getString("group");
		bulbList = pMan.getGroup(groupName);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.group, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_edit) {
			NewGroupFragment f = new NewGroupFragment();
			Bundle b = new Bundle();
			b.putString("group", groupName);
			f.setArguments(b);
			getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), f).addToBackStack("").commit();
		}
		return super.onOptionsItemSelected(item);
	}
}
