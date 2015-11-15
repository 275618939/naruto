package com.movie.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil {
	private static DBUtil mInstance;
	// private Context mContext;
	private SQLHelper mSQLHelp;
	private SQLiteDatabase mSQLiteDatabase;

	private DBUtil(Context context) {
		// mContext = context;
		mSQLHelp = new SQLHelper(context);
		mSQLiteDatabase = mSQLHelp.getWritableDatabase();
	}

	/**
	 * 初始化数据库操作DBUtil类
	 */
	public static DBUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBUtil(context);
		}
		return mInstance;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (null != mSQLHelp) {
			mSQLHelp.close();
			mSQLHelp = null;
		}
		if (null != mSQLiteDatabase) {
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}

		mInstance = null;
	}

	/**
	 * 添加数据
	 */
	public void insertData(String tableName, ContentValues values) {
		mSQLiteDatabase.insert(tableName, null, values);
	}

	/**
	 * 更新数据
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void updateData(String tableName, ContentValues values,
			String whereClause, String[] whereArgs) {
		mSQLiteDatabase.update(tableName, values, whereClause, whereArgs);
	}

	/**
	 * 删除数据
	 * 
	 * @param whereClause
	 * @param whereArgs
	 */
	public void deleteData(String tableName, String whereClause,
			String[] whereArgs) {
		mSQLiteDatabase.delete(tableName, whereClause, whereArgs);
	}

	/**
	 * 查询数据
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor selectData(String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor cursor = mSQLiteDatabase.query(tableName, columns, selection,selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	public int getDataCount(String tableName,String[] selectionArgs) {
		String sql="select count(*) from "+tableName;
		Cursor cursor =  mSQLiteDatabase.rawQuery(sql, selectionArgs);
		cursor.moveToFirst();  
        return cursor.getInt(0);  
	}
	

	public void clearFeedTable(String tableName) {
		String sql = "DELETE FROM " + tableName + ";";
		mSQLiteDatabase.execSQL(sql);
		revertSeq(tableName);
	}

	private void revertSeq(String tableName) {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ tableName + "'";
		mSQLiteDatabase.execSQL(sql);
	}

}