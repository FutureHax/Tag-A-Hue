package com.t3hh4xx0r.tag_a_hue.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;

import com.t3hh4xx0r.openhuesdk.sdk.PreferencesManager;
import com.t3hh4xx0r.openhuesdk.sdk.bulb.BulbManager;
import com.t3hh4xx0r.openhuesdk.sdk.bulb.IBulbManager.onBulbStateFetchedListener;
import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;
import com.t3hh4xx0r.openhuesdk.sdk.objects.BulbState;
import com.t3hh4xx0r.tag_a_hue.DBAdapter;
import com.t3hh4xx0r.tag_a_hue.R;

public class SwitchActivatedActivity extends Activity {
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		try {
			resolveIntent(getIntent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		try {
			resolveIntent(intent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
	}

	private void resolveIntent(final Intent intent) throws IOException {
		final byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		if (tagId != null) {
			handleKey(byteArrayToHexString(tagId));
			finish();
		}
	}

	private void handleKey(String id) {
		DBAdapter db = new DBAdapter(this);
		db.open();
		String bulb = db.getBulbForSwitch(id);
		if (bulb != null) {
			PreferencesManager pMan = new PreferencesManager(this);
			BulbManager bulbMan = new BulbManager(this, pMan.getBridge());
			for (int i = 0; i < pMan.getBulbs().size(); i++) {
				if (pMan.getBulbs().get(i).getNumber().equals(bulb)) {
					toggleBulb(pMan.getBulbs().get(i), bulbMan);
					return;
				}
			}
			
			for (Bulb b : pMan.getGroup(bulb)) {
				toggleBulb(b, bulbMan);
				Log.d("TOGGLE BULB", b.toString());
			}
		}
	}

	private void toggleBulb(final Bulb bulb, final BulbManager bulbMan) {
		bulbMan.getLightState(bulb, new onBulbStateFetchedListener() {
			@Override
			public void onWifiNotAvailable() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStateUnableToBeFetched(final String error) {
				Intent intent = new Intent(SwitchActivatedActivity.this,
						LoadingSplashActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(
						SwitchActivatedActivity.this, 0, intent, 0);

				Notification noti = new Notification.Builder(
						SwitchActivatedActivity.this).setContentTitle("ERROR")
						.setContentText(error)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentIntent(pIntent).build();

				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

				noti.flags |= Notification.FLAG_AUTO_CANCEL;

				notificationManager.notify(0, noti);
				Log.d("OMFGBWTFBBQ", error);
			}

			@Override
			public void onStateFetched(BulbState state) {
				if (state.getState().isOn()) {
					bulbMan.turnOff(bulb, null);
				} else {
					bulbMan.turnOn(bulb, null);
				}
			}
		});
	}

	static String byteArrayToHexString(byte[] inarray) {
		int i, j, in;
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F" };
		String out = "";

		for (j = 0; j < inarray.length; ++j) {
			in = (int) inarray[j] & 0xff;
			i = (in >> 4) & 0x0f;
			out += hex[i];
			i = in & 0x0f;
			out += hex[i];
		}
		return out;
	}
}
