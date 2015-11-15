package com.movie.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

public class ImageLoader {
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private static LruCache<String, Bitmap> mMemoryCache;

	/**
	 * ImageLoader的实例。
	 */
	private static ImageLoader mImageLoader;

	private ImageLoader() {
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
	}

	/**
	 * 获取ImageLoader的实例。
	 * 
	 * @return ImageLoader的实例。
	 */
	public static ImageLoader getInstance() {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader();
		}
		return mImageLoader;
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public  void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}
	/** 
     * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。 
     *  
     * @param imageUrl 
     *            图片的URL地址 
     * @return 加载到内存的图片。 
     */  
    public  Bitmap loadImage(String imageUrl,int width) {  
        File imageFile = new File(getImagePath(imageUrl));  
        if (!imageFile.exists()) {  
            downloadImage(imageUrl,width);  
        }  
        if (imageUrl != null) {  
            Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), width);  
            if (bitmap != null) {  
            	addBitmapToMemoryCache(imageUrl, bitmap);  
                return bitmap;  
            }  
        }  
        return null;  
    }  
    /** 
     * 将图片下载到SD卡缓存起来。 
     *  
     * @param imageUrl 
     *            图片的URL地址。 
     */  
    public  void downloadImage(String imageUrl,int width) {  
        HttpURLConnection con = null;  
        FileOutputStream fos = null;  
        BufferedOutputStream bos = null;  
        BufferedInputStream bis = null;  
        File imageFile = null;  
        try {  
            URL url = new URL(imageUrl);  
            con = (HttpURLConnection) url.openConnection();  
            con.setConnectTimeout(5 * 1000);  
            con.setReadTimeout(15 * 1000);  
            con.setDoInput(true);  
            //con.setDoOutput(true);  
            bis = new BufferedInputStream(con.getInputStream());  
            imageFile = new File(getImagePath(imageUrl));  
            fos = new FileOutputStream(imageFile);  
            bos = new BufferedOutputStream(fos);  
            byte[] b = new byte[1024];  
            int length;  
            while ((length = bis.read(b)) != -1) {  
                bos.write(b, 0, length);  
                bos.flush();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bis != null) {  
                    bis.close();  
                }  
                if (bos != null) {  
                    bos.close();  
                }  
                if (con != null) {  
                    con.disconnect();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        if (imageFile != null) {  
            Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), width);  
            if (bitmap != null) {  
               addBitmapToMemoryCache(imageUrl, bitmap);  
            }  
        }  
    }  
    /** 
     * 获取图片的本地存储路径。 
     *  
     * @param imageUrl 
     *            图片的URL地址。 
     * @return 图片的本地存储路径。 
     */  
    public String getImagePath(String imageUrl) {  
        int lastSlashIndex = imageUrl.lastIndexOf("/");  
        String imageName = imageUrl.substring(lastSlashIndex + 1);  
        String imageDir = Environment.getExternalStorageDirectory().getPath() + "/naruto/";  
        File file = new File(imageDir);  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        String imagePath = imageDir + imageName;  
        return imagePath;  
    }  
	 /**
     * 获取网落图片资源 
     * @param url
     * @return
     */
    public Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
         
        return bitmap;
         
    }
    
}
