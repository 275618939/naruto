package com.movie.client.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.movie.app.NarutoApplication;
import com.movie.client.bean.BaseBean;
import com.movie.client.db.DBUtil;
import com.movie.client.db.SQLHelper;

public abstract class BaseDao {
	
	protected String table;
	DBUtil dbUtil;
	protected ContentValues contentValues;
	public BaseDao(){
		dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
	}
	public abstract void setContentValues(BaseBean baseBean);
	public  void addData(){
		try {
			
			dbUtil.insertData(table, contentValues);
		} catch (SQLException e) {
			throw new SQLException("insert :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
	}
	
	public  void deleteData(String whereClause, String[] whereArgs){
		try {
			//dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
			dbUtil.deleteData(table, whereClause, whereArgs);
		} catch (SQLException e) {
			throw new SQLException("delete :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
	}
	public void updateData(String whereClause,String[] whereArgs) {

		try {
			//dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
			dbUtil.updateData(SQLHelper.TABLE_USER,contentValues, whereClause, whereArgs);
		} catch (Exception e) {
			throw new SQLException("updateData :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
	}


	public  List<Map<String, String>> listData(String selection,String[] selectionArgs){
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor cursor = null;
		try {
			//dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
			cursor=	dbUtil.selectData(table, null, selection,selectionArgs, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
				list.add(map);
			}
			cursor.close();

		} catch (Exception e) {
			throw new SQLException("listData :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
		return list;
	}
	public Map<String, String> viewData(String selection,String[] selectionArgs){

		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			//dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
			cursor=	dbUtil.selectData(table, null, selection,selectionArgs, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
			}
			cursor.close();
		} catch (Exception e) {
			throw new SQLException("viewData :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
		return map;
	
		
	}
	public int countData(String[] selectionArgs){

		int  count = 0;
		try {
			//dbUtil=DBUtil.getInstance(NarutoApplication.getApp().getSQLHelper().getContext());
			count=	dbUtil.getDataCount(table, null);
		    return count;
		} catch (Exception e) {
			Log.i("Dao", "countData :"+table+",error:"+e.getMessage());
			throw new SQLException("countData :"+table+",error:"+e.getMessage());
		} finally {
			//dbUtil.close();
		}	
	}
	public void close(){
		
	}
	
	
	

}
