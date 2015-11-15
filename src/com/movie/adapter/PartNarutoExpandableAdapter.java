package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.SexState;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;

public class PartNarutoExpandableAdapter extends BaseExpandableListAdapter {

	List<Dictionary> parents;
	List<List<User>> childs;
	private LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;
	Context context;

	public PartNarutoExpandableAdapter(Context context,List<Dictionary> parents, List<List<User>> childs) {
		inflater = LayoutInflater.from(context);
		imageLoaderCache = new ImageLoaderCache(context);
		this.parents = parents;
		this.childs = childs;
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		return parents == null ? 0 : parents.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(childs==null)
			return 0;
		if(childs.get(groupPosition)==null||groupPosition>=childs.size()){
			return 0;
		}
		
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parents.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {


		TextView title=null;
		TextView content=null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.dictionary_item, parent,false);
		}
		title = (TextView) convertView.findViewById(R.id.title);
		content = (TextView) convertView.findViewById(R.id.part_info);
		if(isExpanded){
			content.setText("点击收缩");
		}else{
			content.setText("点击展开");
		}
		title.setText(parents.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.part_naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.part_user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.part_user_icon);
			mHolder.missUserName = (TextView) view.findViewById(R.id.user_name);
			mHolder.missUserSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.missUserCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.missUserLove = (TextView) view.findViewById(R.id.user_love);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		User user = childs.get(groupPosition).get(childPosition);
		imageLoaderCache.DisplayImage(user.getPortrait(), mHolder.userIcon);
		mHolder.missUserName.setText(user.getNickname());
		mHolder.missUserSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.missUserCharm.setText(user.getCharm().toString());
		mHolder.userConstell.setText(user.getConstell());
		mHolder.missUserLove.setText(String.format(context.getResources().getString(R.string.user_love_count), user.getLove().toString()));
		mHolder.userItemView.setOnClickListener(new UserSelectAction(groupPosition,childPosition));

		return view;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	public void updateData(List<Dictionary> parents,List<List<User>> childs) {
		this.parents = parents;
		this.childs = childs;
		this.notifyDataSetChanged();

	}

	protected class UserSelectAction implements OnClickListener {

		int groupPosition;
		int childPosition;

		public UserSelectAction(int groupPosition, int childPosition) {
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

		@Override
		public void onClick(final View v) {
			final User user = childs.get(groupPosition).get(childPosition);
			switch (v.getId()) {
			case R.id.part_user_item_view:
				Intent intent = new Intent(context, UserDetailActivity.class);
				intent.putExtra("user", user);
				context.startActivity(intent);
				break;

			default:
				break;
			}
		}

	}

	static class ViewHolder {

		LinearLayout userItemView;
		// 用户LOGO
		ImageView userIcon;
		// 用户
		TextView missUserName;
		// 用户性别
		TextView missUserSex;
		// 用户星座
		TextView userConstell;
		// 用户颜值
		TextView missUserCharm;
		// 用户心动数
		TextView missUserLove;

	}

}
