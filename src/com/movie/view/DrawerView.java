package com.movie.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.movie.R;
import com.movie.ui.LoginActivity;
import com.movie.ui.MySelfDetailActivity;
import com.movie.ui.UserActivity;
/** 
 * 自定义SlidingMenu 测拉菜单类
 * */
public class DrawerView implements OnClickListener{
	private final Activity activity;
	SlidingMenu localSlidingMenu;
	private LinearLayout logout_layout;
	private LinearLayout userLogoBtn;
	private LinearLayout userEdit_layout;
	public DrawerView(Activity activity) {
		this.activity = activity;
	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);//设置左滑菜单
		//localSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);//设置左右滑菜单
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
//		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);//SlidingMenu滑动时的渐变程度
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//使SlidingMenu附加在Activity右边
//		localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
		localSlidingMenu.setMenu(R.layout.left_drawer_fragment);//设置menu的布局文件
//		localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {
						
					}
				});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
			
				
			}
		});
		initView();
		return localSlidingMenu;
	}
	
	private void initView() {
		userEdit_layout =(LinearLayout)localSlidingMenu.findViewById(R.id.user_edit);
		logout_layout=(LinearLayout)localSlidingMenu.findViewById(R.id.logout_layout);
		userLogoBtn=(LinearLayout)localSlidingMenu.findViewById(R.id.user_logo_btn);
		userEdit_layout.setOnClickListener(this);
		logout_layout.setOnClickListener(this);
		userLogoBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.logout_layout:
				activity.startActivity(new Intent(activity,LoginActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				activity.finish();
				break;
			case R.id.user_edit:
				activity.startActivity(new Intent(activity,UserActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				activity.finish();
				break;
			case R.id.user_logo_btn:
				activity.startActivity(new Intent(activity,MySelfDetailActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				activity.finish();
				break;
			
			default:
				break;
		}
	}
}
