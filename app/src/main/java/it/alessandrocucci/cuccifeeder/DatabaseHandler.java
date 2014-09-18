/*
 * DatabaseHandler.java
 * 
 * Developer:
 * 		Alessandro Cucci
 * 		alessandro.cucci@gmail.com
 * 		www.alessandrocucci.it
 * 
 * Disclaimer: When I wrote this, only God and I understood what I was doing
 */

package it.alessandrocucci.cuccifeeder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;


	private static final String DATABASE_NAME = "Feeds";


	private static final String TABLE_FEEDS = "feeds";

	


	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_URL = "url";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FEEDS_TABLE = "CREATE TABLE " + TABLE_FEEDS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," +
                KEY_URL + " TEXT)";

		

		db.execSQL(CREATE_FEEDS_TABLE);

        // Facciamoci un pò di pubblicità
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, "AlessandroCucci.it");
        values.put(KEY_URL, "http://www.alessandrocucci.it/blog/feed");
        db.insert(TABLE_FEEDS, null, values);
        values.clear();



	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);



		onCreate(db);
	}


	void addFeed(Feed feed) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, feed.getName());
        values.put(KEY_URL, feed.getUrl());


		db.insert(TABLE_FEEDS, null, values);
		db.close(); // Closing database connection
	}






		Feed getFeed(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_FEEDS, new String[] { KEY_ID,
					KEY_NAME, KEY_URL }, KEY_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();

			Feed feed = new Feed(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1));

			return feed;
		}




	public List<Feed> getAllFeeds() {
		List<Feed> feedList = new ArrayList<Feed>();

		String selectQuery = "SELECT  * FROM " + TABLE_FEEDS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);


		if (cursor.moveToFirst()) {
			do {
				Feed feed = new Feed();
				feed.setID(Integer.parseInt(cursor.getString(0)));
				feed.setName(cursor.getString(1));
                feed.setUrl(cursor.getString(2));

				feedList.add(feed);
			} while (cursor.moveToNext());
		}
		

		return feedList;
	}
	




	public void deleteFeed(Feed feed) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FEEDS, KEY_ID + " = ?",
				new String[] { String.valueOf(feed.getID()) });
		db.close();
	}
	


	public int getFeedsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FEEDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();


		return cursor.getCount();
	}
	



}
