package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Dictionary;
import com.movie.client.db.SQLHelper;

public class CommentDaoImple extends BaseDao {

	public CommentDaoImple() {
		table = SQLHelper.TABLE_COMMENT;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {
		if (null != baseBean) {
			Dictionary comment = (Dictionary) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.ID, comment.getId());
			contentValues.put(SQLHelper.TYPE, comment.getType());
			contentValues.put(SQLHelper.NAME, comment.getName());
		}

	}

}
