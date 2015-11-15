package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Dictionary;
import com.movie.client.db.SQLHelper;

public class FilmTypeDaoImple extends BaseDao {

	public FilmTypeDaoImple() {
		table = SQLHelper.TABLE_FILMTYPE;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {
		if (null != baseBean) {
			Dictionary comment = (Dictionary) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.ID, comment.getId());
			contentValues.put(SQLHelper.NAME, comment.getName());
		}

	}

}
