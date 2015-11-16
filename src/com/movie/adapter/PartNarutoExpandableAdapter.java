package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.SelfPartNarutoBtn;
import com.movie.app.SexState;
import com.movie.client.bean.Dictionary;
import com.movie.client.bean.Miss;
import com.movie.client.bean.User;
import com.movie.ui.UserDetailActivity;
import com.movie.util.ImageLoaderCache;
import com.movie.util.StringUtil;
import com.movie.view.CommentsGridView;
import com.movie.view.MessageDialog;

public class PartNarutoExpandableAdapter extends BaseExpandableListAdapter {

	List<Dictionary> parents;
	List<List<User>> childs;
	Miss miss;
	int status;
	private LayoutInflater inflater;
	ImageLoaderCache imageLoaderCache;
	Context context;
	PopupWindow popupWindow;
	View popView;
	/** popWindow 关闭按钮 */
	ImageView btn_pop_close;
	CommentsGridView commentsGridView;
	EvaluationAdapter evaluationAdapter;
	Handler handler;
	public PartNarutoExpandableAdapter(Context context,List<Dictionary> parents, List<List<User>> childs) {
		inflater = LayoutInflater.from(context);
		imageLoaderCache = new ImageLoaderCache(context);
		this.parents = parents;
		this.childs = childs;
		this.context = context;
		initPopWindow();
	}
	public PartNarutoExpandableAdapter(Context context,Handler handler,List<Dictionary> parents, List<List<User>> childs) {
		this.handler=handler;
		inflater = LayoutInflater.from(context);
		imageLoaderCache = new ImageLoaderCache(context);
		this.parents = parents;
		this.childs = childs;
		this.context = context;
		initPopWindow();
	}

	@Override
	public int getGroupCount() {
		return parents == null ? 0 : parents.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (childs == null)
			return 0;
		if (childs.get(groupPosition) == null || groupPosition >= childs.size()) {
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		TextView title = null;
		TextView content = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.dictionary_item, parent,
					false);
		}
		title = (TextView) convertView.findViewById(R.id.title);
		content = (TextView) convertView.findViewById(R.id.part_info);
		if (isExpanded) {
			content.setText("点击收缩");
		} else {
			content.setText("点击展开");
		}
		title.setText(parents.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.part_self_naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.part_user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.part_user_icon);
			mHolder.missUserName = (TextView) view.findViewById(R.id.user_name);
			mHolder.missUserSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.missUserCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.missUserLove = (TextView) view.findViewById(R.id.user_love);
			mHolder.partNarutoBtn = (TextView) view.findViewById(R.id.part_naruto_btn);
			mHolder.userStatus = (TextView) view.findViewById(R.id.user_status);
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
		mHolder.userIcon.setOnClickListener(new UserSelectAction(groupPosition,childPosition));
		if(miss!=null){
			int result=StringUtil.dateCompareByCurrent(miss.getRunTime());
			if(result==-1){
				result=SelfPartNarutoBtn.Evaluation.getState();
				mHolder.partNarutoBtn.setBackgroundResource(R.drawable.tag2_btn_ment);
			}
			SelfPartNarutoBtn btn =SelfPartNarutoBtn.getState(result);
			status=btn.getState();
			if(result>0){
				mHolder.partNarutoBtn.setVisibility(View.VISIBLE);
				mHolder.partNarutoBtn.setText(btn.getMessage());	
				mHolder.partNarutoBtn.setOnClickListener(new UserSelectAction(groupPosition,childPosition));
			  
			}else{
				mHolder.userStatus.setText(btn.getMessage());
				mHolder.userStatus.setVisibility(View.VISIBLE);
			}
		}
		
		return view;

	}
	private void initPopWindow() {
		popView = inflater.inflate(R.layout.evluation_list_item_pop, null);
		popView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popupWindow = new PopupWindow(popView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		//设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		commentsGridView = (CommentsGridView) popView.findViewById(R.id.comments);
		evaluationAdapter = new EvaluationAdapter(popView.getContext(),handler,null);
		commentsGridView.setAdapter(evaluationAdapter);
		btn_pop_close = (ImageView) popView.findViewById(R.id.btn_pop_close);
	}
	public void showPop(View parent,int x,int y,User user) {
		
		evaluationAdapter.setUser(user);
		evaluationAdapter.updateData(false,user.getSex(),Dictionary.getTempComments(user.getSex()));
	
		//设置popwindow显示位置
		popupWindow.showAtLocation(parent,Gravity.CENTER, 0,0);
		//获取popwindow焦点
		popupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		if (popupWindow.isShowing()) {
			
		}
		btn_pop_close.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
			}
		});
		
	}
	

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateData(List<Dictionary> parents, List<List<User>> childs) {
		this.parents = parents;
		this.childs = childs;
		this.notifyDataSetChanged();

	}
	

	public void setMiss(Miss miss) {
		this.miss = miss;
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
			case R.id.part_user_icon:
				Intent intent = new Intent(context, UserDetailActivity.class);
				intent.putExtra("user", user);
				context.startActivity(intent);			
				break;
			case R.id.part_naruto_btn:
				if(status==SelfPartNarutoBtn.Evaluation.getState()){
					int[] arrayOfInt = new int[2];
					//获取点击按钮的坐标
					v.getLocationOnScreen(arrayOfInt);
			    	int popupWidth = popView.getMeasuredWidth();
					int popupHeight =  popView.getMeasuredHeight();
					int x = (arrayOfInt[0]+v.getWidth()/2)-popupWidth/2;
					int y =  arrayOfInt[1]-popupHeight;
					showPop(v,x,y, user);
				}else if(status==SelfPartNarutoBtn.KickedOut.getState()){
					final MessageDialog.Builder builder = new MessageDialog.Builder(v.getContext());
					builder.setTitle(R.string.kicked_out);
					builder.setMessage("您确定要踢出么?");
					builder.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("确定",
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
									Message message = new Message();
									message.what = Miss.AGREE_MISS;
									Bundle bundle = new Bundle();
									bundle.putSerializable("user", user);
									message.setData(bundle);
									handler.sendMessage(message);
								}
							});

					builder.create().show();
				}else{
					Intent btnIntent = new Intent(context, UserDetailActivity.class);
					btnIntent.putExtra("user", user);
					context.startActivity(btnIntent);	
				}
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
		//  用户操作
		TextView partNarutoBtn;
		// 用户状态
		TextView userStatus;

	}

}
