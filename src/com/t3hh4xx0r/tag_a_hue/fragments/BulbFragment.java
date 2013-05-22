package com.t3hh4xx0r.tag_a_hue.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3hh4xx0r.openhuesdk.sdk.PreferencesManager;
import com.t3hh4xx0r.openhuesdk.sdk.bulb.BulbManager;
import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;
import com.t3hh4xx0r.tag_a_hue.R;

public class BulbFragment extends Fragment {
	BulbManager bulbMan;
	Bulb b;

	public BulbFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bulb_manager,
				container, false);
		bulbMan = new BulbManager(getActivity(), new PreferencesManager(getActivity()).getBridge());
		b = (Bulb) getArguments().getSerializable("bulb");
		
		return rootView;
	}
}
