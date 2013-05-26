package com.t3hh4xx0r.tag_a_hue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class ChangeLogDialog {

	private static String changelog_PREFIX = "changelog_";
	private static final String CHANGELOG = "-Initial Release";

	private static PackageInfo getPackageInfo(Activity a) {
		PackageInfo pi = null;
		try {
			pi = a.getPackageManager().getPackageInfo(a.getPackageName(),
					PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi;
	}

	public static void show(final Activity a) {
		PackageInfo versionInfo = getPackageInfo(a);

		final String changelogKey = changelog_PREFIX + versionInfo.versionCode;
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(a);
		final Editor e = prefs.edit();
		boolean hasBeenShown = prefs.getBoolean(changelogKey, false);
		// if (true) {
		if (hasBeenShown == false) {
			e.putBoolean(changelogKey, true).commit();
			String title = a.getString(R.string.app_name) + " v"
					+ versionInfo.versionName;
			AlertDialog.Builder builder = new AlertDialog.Builder(a)
					.setTitle(title).setMessage(CHANGELOG).setCancelable(true);
			builder.create().show();
		}
	}
}
