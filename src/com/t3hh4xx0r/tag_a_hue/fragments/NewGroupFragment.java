package com.t3hh4xx0r.tag_a_hue.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.t3hh4xx0r.openhuesdk.sdk.PreferencesManager;
import com.t3hh4xx0r.openhuesdk.sdk.bulb.BulbManager;
import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;
import com.t3hh4xx0r.tag_a_hue.R;
import com.t3hh4xx0r.tag_a_hue.activities.MainActivity;

public class NewGroupFragment extends Fragment {
	public class BulbAdapter extends BaseAdapter {
		ArrayList<Bulb> bulbs;
		Context c;
		LayoutInflater inf;
		
		public BulbAdapter(FragmentActivity activity, ArrayList<Bulb> bulbs) {
			c = activity;
			this.bulbs = bulbs;
			inf = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return bulbs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return bulbs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View root = inf.inflate(android.R.layout.simple_list_item_activated_1, arg2, false);
			TextView text = (TextView) root.findViewById(android.R.id.text1);
			text.setText(bulbs.get(arg0).getName());
			return root;
		}
	}

	BulbManager bulbMan;
	ArrayList<Bulb> bulbs;
	PreferencesManager pMan;
	ListView list;
	EditText name;
	
	public NewGroupFragment() {
		setHasOptionsMenu(true);
	}	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.new_group, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_new_group,
				container, false);
		pMan = new PreferencesManager(getActivity());
		bulbMan = new BulbManager(getActivity(), pMan.getBridge());
		bulbs = pMan.getBulbs();
		BulbAdapter listAdapter = new BulbAdapter(getActivity(), bulbs);
		list = (ListView) rootView.findViewById(R.id.list);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				v.setActivated(!v.isActivated());
			}
		});
		name = (EditText) rootView.findViewById(R.id.groupName);		
		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_save) {
			saveGroup();
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveGroup() {
		ArrayList<Bulb> bulbGroup = new ArrayList<Bulb>();
		for (int i=0;i<list.getChildCount();i++) {
			if (list.getChildAt(i).isActivated()) {
				bulbGroup.add(bulbs.get(i));
			}
		}
		pMan.storeGroup(name.getText().toString(), bulbGroup);
		
		((MainActivity) getActivity()).setNavItems(((MainActivity) getActivity()).getNavItems().size() -2);
	}
}
