package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Dictionary;
import com.movie.client.db.SQLHelper;

public class HobbyDaoImple extends BaseDao {

	public HobbyDaoImple() {
		table = SQLHelper.TABLE_HOBBY;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {
		if (null != baseBean) {
			Dictionary hobby = (Dictionary) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.ID, hobby.getId());
			contentValues.put(SQLHelper.NAME, hobby.getName());
		}

	}

}
