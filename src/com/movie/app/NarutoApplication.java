package com.movie.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.movie.R;
import com.movie.client.db.SQLHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class NarutoApplication extends Application {

	private static final String PHOTO_CACSHE_DIR = "naruto/cache";
	public static int longitude; 								     //经度
	public static int latitude; 								     //纬度
	public static String city;										 //所在城市
	public static String address;									 //详细地址
	private static NarutoApplication mAppApplication;
	private SQLHelper sqlHelper;
	public static DisplayImageOptions imageOptions;
	public static List<String> mEmoticons = new ArrayList<String>();
	public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
	public static List<String> mEmoticons_Zem = new ArrayList<String>();
	public static List<String> mEmoticons_Zemoji = new ArrayList<String>();
	/**
	 * 屏幕的宽度、高度、密度
	 */
	public int mScreenWidth;
	public int mScreenHeight;
	public float mDensity;
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		mAppApplication = this;
		showMetrices();
		initZEM();
		
	}

	/** 获取Application */
	public static NarutoApplication getApp() {
		return mAppApplication;
	}

	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mAppApplication);
		return sqlHelper;
	}
	private void showMetrices(){
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		Log.i("屏幕宽度","------------>>>>>"+mScreenWidth);
		Log.i("屏幕高度","------------>>>>>"+mScreenHeight);
		Log.i("屏幕密度","------------>>>>>"+ mDensity+"");
		
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
		// 整体摧毁的时候调用这个方法
	}
    /**初始化表情包*/
	public void initZEM(){
		for (int i = 1; i < 64; i++) {
			String emoticonsName = "[zem" + i + "]";
			int emoticonsId = getResources().getIdentifier("zem" + i,"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zem.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		for (int i = 1; i < 59; i++) {
			String emoticonsName = "[zemoji" + i + "]";
			int emoticonsId = getResources().getIdentifier("zemoji_e" + i,"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zemoji.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
	}

	/** 初始化ImageLoader */
	public  void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,PHOTO_CACSHE_DIR);// 获取到缓存的目录地址
		Log.d("cacheDir", cacheDir.getPath());
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPoolSize(2);// 线程池内加载的数量
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		//config.denyCacheImageMultipleSizesInMemory();
		//config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5加密	
		//config.diskCacheSize(30 * 1024 * 1024); // 50 MiB 50 * 1024 * 1024 (int)Runtime.getRuntime().maxMemory() / 4
		config.diskCache(new UnlimitedDiskCache(cacheDir));
		config.memoryCache(new WeakMemoryCache());
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		//config.writeDebugLogs(); // Remove for release app
		ImageLoader.getInstance().init(config.build());// 全局初始化此配置
		/*初始化显示配置*/
		imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.empty_photo) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.empty_photo) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.empty_photo) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565) 
		.build(); // 构建完成
	}

	
	

}