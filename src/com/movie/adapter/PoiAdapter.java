package com.movie.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.movie.R;
import com.movie.ui.MissConfirmActivity;

public class PoiAdapter extends BaseAdapter {
	

	List<PoiInfo> poiInfos=new ArrayList<PoiInfo>();
	Context context;
	LayoutInflater inflater;
	String dateTime;
	String cointInfo;
	
	
	public PoiAdapter(Context context,List<PoiInfo> poiInfos) {
		
		this.context = context;
		this.poiInfos = poiInfos;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return poiInfos == null ? 0 : poiInfos.size();
	}
	@Override
	public PoiInfo getItem(int position) {
		// TODO Auto-generated method stub
		if (poiInfos != null && poiInfos.size() != 0) {
			return poiInfos.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.poi_item, null);
			mHolder = new ViewHolder();
			mHolder.poiInfoView=(RelativeLayout)view.findViewById(R.id.poi_view);
			mHolder.titleText = (TextView)view.findViewById(R.id.address_title);
			mHolder.contentText = (TextView)view.findViewById(R.id.address_content);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		PoiInfo poiInfo = getItem(position);
		if(poiInfo!=null){
			mHolder.titleText.setText(poiInfo.name);
			mHolder.contentText.setText(poiInfo.address);
		}
		mHolder.poiInfoView.setOnClickListener(new UserSelectAction(position));
		return view;
	}
	class ViewHolder {
		
		RelativeLayout poiInfoView;
		TextView contentText;
		TextView titleText;	
		
	}
	public void updateData(List<PoiInfo> poiInfos) {
		this.poiInfos=poiInfos;
		this.notifyDataSetChanged();
	}
	public void clearData() {
		poiInfos.clear();
	}
	protected class UserSelectAction implements OnClickListener{

		int position;
		
		public UserSelectAction(int position){
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			
			PoiInfo poiInfo=poiInfos.get(position);
			Intent intent=new Intent(context,MissConfirmActivity.class);
			intent.putExtra("dateTime", dateTime);
			intent.putExtra("cointInfo", cointInfo);
			intent.putExtra("cinemaName", poiInfo.name);
			intent.putExtra("cinameAddress", poiInfo.address);
			intent.putExtra("cinamePhoneNum", poiInfo.phoneNum);
			intent.putExtra("cinameUid", poiInfo.uid);
			intent.putExtra("latitude", poiInfo.location.latitude);
			intent.putExtra("longitude", poiInfo.location.longitude);
			context.startActivity(intent);
			
		}
		
	}
	public void setPoiInfos(List<PoiInfo> poiInfos) {
		this.poiInfos = poiInfos;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public void setCointInfo(String cointInfo) {
		this.cointInfo = cointInfo;
	}
	
	
	
	
	
	
	

	
	

	




	



	
}
