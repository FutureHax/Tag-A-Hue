package com.t3hh4xx0r.tag_a_hue;

import java.util.ArrayList;

import com.t3hh4xx0r.openhuesdk.sdk.objects.Bulb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_SWITCHES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SWICTHES);
			onCreate(db);
		}
	}

	private static final int DB_VERSION = 1;
	public static final String DB_NAME = "switches.db";
	public static final String SWICTHES = "switches";
	public static final String VALUE = "value";
	public static final String BULB = "bulb";

	private static final String CREATE_SWITCHES = "create table "
			+ SWICTHES + "(_id integer primary key autoincrement, "
			+ VALUE + " text not null, " 
			+ BULB + " text not null, " 
			+ "unique(" + VALUE + ") on conflict replace);";

	private final Context context;
	private static DatabaseHelper DBHelper;
	public SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public void close() {
		DBHelper.close();
	}

	public void insertSwitch(String id, String bulb) {
		ContentValues v = new ContentValues();
		v.put(VALUE, id);
		v.put(BULB, bulb);

		db.insert(SWICTHES, null, v);
	}

	public boolean isASwitch(String v) {
		Cursor mCursor = db.query(SWICTHES, null, VALUE + " = ? ",
				new String[] { v }, null, null, null, null);
		boolean b = mCursor.getCount() > 0;
		return b;
	}
	
	public String getBulbForSwitch(String id) {
		Cursor mCursor = db.query(SWICTHES, new String[] {BULB, VALUE}, VALUE + " = ? ",
				new String[] { id }, null, null, null, null);
		if (mCursor.moveToFirst()) {
			return mCursor.getString(0);
		} else {
			return null;
		}
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public ArrayList<String> getSwitches() {
		ArrayList<String> res = new ArrayList<String>();
		Cursor mCursor = db.query(SWICTHES, new String[] {BULB, VALUE}, null,
				null, null, null, null, null);
		while (mCursor.moveToNext()) {
			res.add(mCursor.getString(1));
		}
		return res;
	}

	public ArrayList<String> getSwitchesForBulb(Bulb bulb) {
		ArrayList<String> res = new ArrayList<String>();
		Cursor mCursor = db.query(SWICTHES, new String[] {BULB, VALUE}, BULB + " = ? ",
				new String[] { bulb.getNumber() }, null, null, null, null);
		while (mCursor.moveToNext()) {
			res.add(mCursor.getString(1));
		}
		return res;
	}

}
