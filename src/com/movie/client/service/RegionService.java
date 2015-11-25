package com.movie.client.service;

import java.util.Map;

import android.content.Context;

import com.movie.client.bean.Dictionary;
import com.movie.client.dao.BaseDao;
import com.movie.client.dao.RegionDaoImple;
import com.movie.client.db.SQLHelper;

public class RegionService {

	protected BaseDao regionDao;
	protected Context context;

	public RegionService() {
		regionDao = new RegionDaoImple();
	}
	public void addRegion(Dictionary region) {
		regionDao.deleteData(null, null);
		regionDao.setContentValues(region);
		regionDao.addData();
	}
	public int getRegionId() {

		Object regionMap = regionDao.viewData(null, null);
	    int regionId=0;
		if (regionMap != null&&!((Map<String, String>) regionMap).isEmpty()) {
			Map<String, String> maplist = (Map<String, String>) regionMap;
			regionId=Integer.parseInt(maplist.get(SQLHelper.ID));
		}
		return regionId;
	}

	

}
