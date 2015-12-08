package com.movie.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.movie.R;
import com.movie.adapter.ImageBrowserAdapter;
import com.movie.app.BaseActivity;
import com.movie.util.Bimp;
import com.movie.view.PhotoTextView;
import com.movie.view.ScrollViewPager;

public class ImageBrowserActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {

	private ScrollViewPager mSvpPager;
	private PhotoTextView mPtvPage;
	private ImageBrowserAdapter mAdapter;
	private String mType;
	private int mPosition;
	private int mTotal;
	public static final String IMAGE_TYPE = "image_type";
	public static final String TYPE_ALBUM = "image_album";
	public static final String TYPE_PHOTO = "image_photo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagebrowser);
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mSvpPager = (ScrollViewPager) findViewById(R.id.imagebrowser_svp_pager);
		mPtvPage = (PhotoTextView) findViewById(R.id.imagebrowser_ptv_page);
	}

	@Override
	protected void initEvents() {
		mSvpPager.addOnPageChangeListener(this);
	}

	private void init() {
		mPosition = getIntent().getIntExtra("position", 0);
		mTotal = Bimp.tempSelectBitmap.size();
		if(mTotal==1){
			mPtvPage.setText("1/1");
			mAdapter = new ImageBrowserAdapter(Bimp.tempSelectBitmap,TYPE_PHOTO);
			mSvpPager.setAdapter(mAdapter);
		}else{
			if (mPosition > mTotal) {
				mPosition = mTotal - 1;
			}
			if (mTotal > 1) {
				mPosition += 1000 * mTotal;
				mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
				mAdapter = new ImageBrowserAdapter(Bimp.tempSelectBitmap,TYPE_ALBUM);
				mSvpPager.setAdapter(mAdapter);
				mSvpPager.setCurrentItem(mPosition, false);
			} 
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mPosition = arg0;
		mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
	}
	@Override
	public void onBackPressed() {
		defaultFinish();
		overridePendingTransition(0, R.anim.zoom_exit);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdapter=null;
		System.gc();
	}

}
