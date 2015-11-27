package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.R;
import com.movie.app.NarutoApplication;
import com.movie.client.bean.User;
import com.movie.client.service.UserService;
import com.movie.state.SexState;
import com.movie.ui.MovieDetailActivity;
import com.movie.ui.UserDetailActivity;
import com.movie.util.Horoscope;
import com.movie.util.StringUtil;
import com.movie.util.UserCharm;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NarutoAdapter extends BaseAdapter {

	List<User> userList;
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoaderCache;
	Handler handler;
	UserService userService;
	User loginUser;
	boolean isLove;

	public NarutoAdapter(Context context, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoader.getInstance();
		userService = new UserService();
		init();
	}

	public NarutoAdapter(Context context, Handler handler, List<User> users) {
		this.context = context;
		this.userList = users;
		inflater = LayoutInflater.from(context);
		imageLoaderCache=ImageLoader.getInstance();
		this.handler = handler;
		userService = new UserService();
		init();
	}
	private void init(){
		loginUser=userService.getUserItem();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList == null ? 0 : userList.size();
	}

	@Override
	public User getItem(int position) {
		// TODO Auto-generated method stub
		if (userList != null && userList.size() != 0) {
			return userList.get(position);
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
			view = inflater.inflate(R.layout.naruto_item, null);
			mHolder = new ViewHolder();
			mHolder.userMovieTag = (LinearLayout) view.findViewById(R.id.user_movie_tag);
			mHolder.movieBreifLayout= (LinearLayout) view.findViewById(R.id.movie_breif_layout);
			mHolder.userBreifLayout= (LinearLayout) view.findViewById(R.id.user_breif_layout);
			mHolder.userItemView = (LinearLayout) view.findViewById(R.id.user_item_view);
			mHolder.userIcon = (ImageView) view.findViewById(R.id.user_icon);
			mHolder.userName = (TextView) view.findViewById(R.id.user_name);
			mHolder.userSex = (TextView) view.findViewById(R.id.user_sex);
			mHolder.userConstell = (TextView) view.findViewById(R.id.user_constell);
			mHolder.userCharm = (TextView) view.findViewById(R.id.user_charm);
			mHolder.userLove = (TextView) view.findViewById(R.id.user_love);
			mHolder.userMovieLove = (TextView) view.findViewById(R.id.user_movie_love);
			mHolder.userCharmBar = (RatingBar) view.findViewById(R.id.user_charm_bar);
			mHolder.userBtnView = (LinearLayout) view.findViewById(R.id.user_btn_view);
			//mHolder.userIngMiss = (TextView) view.findViewById(R.id.user_ing_miss);
			mHolder.userBtnMessage = (TextView) view.findViewById(R.id.user_btn_message);
			mHolder.userBtnLove = (TextView) view.findViewById(R.id.user_btn_love);
			mHolder.missInvite = (TextView) view.findViewById(R.id.miss_invite);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// 获取position对应的数据
		User user = getItem(position);

		imageLoaderCache.displayImage(user.getPortrait(), mHolder.userIcon,NarutoApplication.imageOptions);
		mHolder.userName.setText(user.getNickname());
		mHolder.userSex.setText(SexState.getState(user.getSex()).getMessage());
		mHolder.userLove.setText(String.format(context.getResources().getString(R.string.user_love_count), user.getLove()==null?"0":user.getLove()));
		if(null!=user.getBirthday()&&!user.getBirthday().isEmpty()){
			int [] date=StringUtil.strConvertInts(user.getBirthday());
			mHolder.userConstell.setText(Horoscope.getHoroscope((byte)date[1],(byte)date[2]).getCnName());
		}
		mHolder.userItemView.setOnClickListener(new UserSelectAction(position));
		if(user.getFilmName()!=null&&!user.getFilmName().isEmpty()){
			mHolder.userMovieTag.setVisibility(View.VISIBLE);
		   mHolder.userMovieLove.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.user_love_movie), user.getFilmName(),user.getFilmCnt())));
		}else{
			mHolder.userMovieTag.setVisibility(View.GONE);
		}
		//mHolder.userIngMiss.setText(String.format(context.getResources().getString(R.string.user_ing_miss), user.getTryst()));
		String score=UserCharm.GetScore(user.getFace(), user.getFaceCnt()<=0?1:user.getFaceCnt());
		if(score.equals("NaN")){
			
		}else{
			mHolder.userCharmBar.setRating(Float.valueOf(score)/2f);
			mHolder.userCharm.setText(score);
		}
		if(loginUser!=null){
			if(loginUser.getMemberId().equals(user.getMemberId())){
				mHolder.userBtnView.setVisibility(View.GONE);
			}else{
				mHolder.userBtnView.setVisibility(View.VISIBLE);
			}
		}
		mHolder.userBreifLayout.setOnClickListener(new UserSelectAction(position));
		mHolder.userBtnLove.setOnClickListener(new UserSelectAction(position));
		mHolder.missInvite.setOnClickListener(new UserSelectAction(position));
		mHolder.userBtnMessage.setOnClickListener(new UserSelectAction(position));
		mHolder.movieBreifLayout.setOnClickListener(new UserSelectAction(position));
		return view;
	}

	static class ViewHolder {
		LinearLayout userMovieTag;
		LinearLayout movieBreifLayout;
		LinearLayout userBtnView;
        LinearLayout userBreifLayout;
		LinearLayout userItemView;
		//用户颜值
		RatingBar userCharmBar;
		// 用户LOGO
		ImageView userIcon;
		// 用户
		TextView userName;
		// 用户性别
		TextView userSex;
		// 用户星座
		TextView userConstell;
		// 用户颜值
		TextView userCharm;
		// 用户心动数
		TextView userLove;
		// 用户想看的电影
		TextView userMovieLove;
		// 用户留言
		TextView userBtnMessage;
		// 用户心动
	    TextView userBtnLove;
	    // 用户邀请
	    TextView missInvite;

	}

	public void updateData(List<User> userList) {
		this.userList = userList;
		this.notifyDataSetChanged();

	}

	protected class UserSelectAction implements OnClickListener {

		int position;

		public UserSelectAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(final View v) {
			final User user = userList.get(position);
			switch (v.getId()) {
			case R.id.movie_breif_layout:
				Intent movieDetailIntent = new Intent(context, MovieDetailActivity.class);
				movieDetailIntent.putExtra("filmId", user.getFilmId());
				context.startActivity(movieDetailIntent);
				break;
			case R.id.user_breif_layout:
				Intent userDetailIntent = new Intent(context, UserDetailActivity.class);
				userDetailIntent.putExtra("memberId", user.getMemberId());
				context.startActivity(userDetailIntent);
				break;
			case R.id.user_btn_message:
				break;
			case R.id.user_btn_love:
				if(isLove) {
					Toast toast = Toast.makeText(context, "您已经心动过了哟!", Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				Message message=new Message();
				message.what=User.USER_LOVE;
				Bundle bundle=new Bundle();
				bundle.putString("memberId", user.getMemberId());
				message.setData(bundle);
				handler.sendMessage(message);
				isLove=true;
				break;
			case R.id.miss_invite:
				break;
			
			default:
				break;
			}
		}

	}

}
