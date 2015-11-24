package com.movie.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "database.db";// 数据库名称
	public static final int VERSION = 1;
	public static final String TABLE_USER = "user";// 用户表
	public static final String TABLE_SESSION = "session";// 回话表
	public static final String TABLE_LOGIN = "login";// 登陆表
	public static final String TABLE_HOBBY = "hobby";// 喜好表
	public static final String TABLE_COMMENT = "comment";// 评价表
	public static final String TABLE_FILMTYPE = "filmType";// 电影类型表
	public static final String ID = "id";//
	public static final String TYPE = "type";//
	public static final String NAME = "name";
	public static final String ORDERID = "orderId";
	public static final String SELECTED = "selected";
	public static final String SID = "sid"; // 登陆唯一标示用户ID
	public static final String MEMBERID = "memberId"; // 登陆唯一标示用户ID
	public static final String LOGIN = "login"; // 用户名称（为手机号）
	public static final String ACCOUNT = "account"; // 用户名称（为手机号）
	public static final String PASS = "pass"; // 用户名称（为手机号）

	private Context context;

	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {
			// TODO 创建数据库后，对数据库的操作
			/*
			 * String sql = "create table if not exists " + TABLE_CHANNEL +
			 * "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER , " +
			 * NAME + " TEXT , " + ORDERID + " INTEGER , " + SELECTED +
			 * " SELECTED)";
			 * 
			 * sql = "create table if not exists " + TABLE_USER +
			 * "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + SID + " TEXT , " +
			 * LOGIN + " TEXT )";
			 */

			db.execSQL("create table if not exists " + TABLE_USER
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + MEMBERID
					+ " TEXT )");

			db.execSQL("create table if not exists " + TABLE_SESSION
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + SID
					+ " TEXT  )");

			db.execSQL("create table if not exists " + TABLE_LOGIN
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT
					+ " TEXT , " + PASS + " TEXT )");
			
			db.execSQL("create table if not exists " + TABLE_HOBBY
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
					+ " INTEGER , " + NAME + " TEXT )");
			
			db.execSQL("create table if not exists " + TABLE_COMMENT
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
					+ " INTEGER , " + TYPE+ " INTEGER ," + NAME + " TEXT )");
			
			db.execSQL("create table if not exists " + TABLE_FILMTYPE
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
					+ " INTEGER , " + NAME + " TEXT )");


			// createUserTable();

		} catch (Exception e) {
			Log.e("---->create table", e.getMessage());
		}

	}

	public void createUserTable() {

		String sql = "create table if not exists " + TABLE_USER
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + SID + " TEXT , "
				+ LOGIN + " TEXT )";
		this.getWritableDatabase().execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
		onCreate(db);
	}

}
