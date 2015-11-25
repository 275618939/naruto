package com.movie.client.dao;

import android.content.ContentValues;

import com.movie.client.bean.BaseBean;
import com.movie.client.bean.Dictionary;
import com.movie.client.db.SQLHelper;

public class RegionDaoImple extends BaseDao {

	public RegionDaoImple() {
		table = SQLHelper.TABLE_REGION;
	}

	@Override
	public void setContentValues(BaseBean baseBean) {
		if (null != baseBean) {
			Dictionary region = (Dictionary) baseBean;
			contentValues = new ContentValues();
			contentValues.put(SQLHelper.ID, region.getId());
		}

	}

}
