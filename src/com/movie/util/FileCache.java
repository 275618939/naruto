package com.movie.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileCache {
	private File cacheDir;
	private final String localPath="naruto";
	public FileCache(Context context) {
		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
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
        String imageDir = Environment.getExternalStorageDirectory().getPath() + "/"+localPath+"/";  
        File file = new File(imageDir);  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        String imagePath = imageDir + imageName;  
        return imagePath;  
    }  

	public File getFile(String url) {
		// 将url的hashCode作为缓存的文件名
		String filename = String.valueOf(url.hashCode());
		// Another possible solution
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}
}
