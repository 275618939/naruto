package com.movie.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.R;
import com.movie.client.bean.Movie;
import com.movie.ui.MovieDetailActivity;
import com.movie.util.ImageLoader;

public class MovieView extends ScrollView implements OnTouchListener {

	
	
	private Movie[] movies=Movie.movies;
	
	 /** 
     * 每页要加载的图片数量 
     */  
    public static final int PAGE_SIZE = 6;  
  
    /** 
     * 记录当前已加载到第几页 
     */  
    private int page;  
  
    /** 
     * 每一列的宽度 
     */  
    private int columnWidth;  
  
    /** 
     * 当前第一列的高度 
     */  
    private int firstColumnHeight;  
  
    /** 
     * 当前第二列的高度 
     */  
    private int secondColumnHeight;  
  
  
    /** 
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次 
     */  
    private boolean loadOnce;  
  
    /** 
     * 对图片进行管理的工具类 
     */  
    private ImageLoader imageLoader;  
  
    /** 
     * 第一列的布局 
     */  
    private LinearLayout firstColumn;  
  
    /** 
     * 第二列的布局 
     */  
    private LinearLayout secondColumn;  
  
   
    /** 
     * 记录所有正在下载或等待下载的任务。 
     */  
    private static Set<LoadImageTask> taskCollection;  
  
    /** 
     * ContractView 下的直接子布局。 
     */  
    private static View scrollLayout;  
  
    /** 
     * ContractView  布局的高度。 
     */  
    private static int scrollViewHeight;  
  
    /** 
     * 记录上垂直方向的滚动距离。 
     */  
    private static int lastScrollY = -1;  
  
    /** 
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。 
     */  
    private List<ImageView> imageViewList = new ArrayList<ImageView>();  
  
    /** 
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。 
     */  
    private static Handler handler = new Handler() {  
  
        public void handleMessage(Message msg) {  
        	MovieView myScrollView = (MovieView) msg.obj;  
            int scrollY = myScrollView.getScrollY();  
            // 如果当前的滚动位置和上次相同，表示已停止滚动  
            if (scrollY == lastScrollY) {  
                // 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片  
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight()  
                        && taskCollection.isEmpty()) {  
                    myScrollView.loadMoreMovie();  
                }  
                myScrollView.checkVisibility();  
            } else {  
                lastScrollY = scrollY;  
                Message message = new Message();  
                message.obj = myScrollView;  
                // 5毫秒后再次对滚动位置进行判断  
                handler.sendMessageDelayed(message, 5);  
            }  
        };  
  
    };  
  
    /** 
     * ContractView 的构造函数。 
     *  
     * @param context 
     * @param attrs 
     */  
    public MovieView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        imageLoader = ImageLoader.getInstance();  
        taskCollection = new HashSet<LoadImageTask>();  
        setOnTouchListener(this);  
    }  
  
    /** 
     * 进行一些关键性的初始化操作，获取ContractView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。 
     */  
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
        super.onLayout(changed, l, t, r, b);  
        if (changed && !loadOnce) {  
            scrollViewHeight = getHeight();  
            scrollLayout = getChildAt(0);  
            firstColumn = (LinearLayout) findViewById(R.id.first_column);  
            secondColumn = (LinearLayout) findViewById(R.id.second_column);  
            columnWidth = firstColumn.getWidth();  
            loadOnce = true;  
            loadMoreMovie();  
            
        }  
    }  
  
    /** 
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。 
     */  
    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            Message message = new Message();  
            message.obj = this;  
            handler.sendMessageDelayed(message, 5);  
        }  
        return false;  
    }  
    

 
    /** 
     * 开始加载电影信息，每张图片都会开启一个异步线程去下载。 
     */  
    public void loadMoreMovie() {  
        if (hasSDCard()) {  
            int startIndex = page * PAGE_SIZE;  
            int endIndex = page * PAGE_SIZE + PAGE_SIZE;  
   
            if (startIndex < Movie.movies.length) {  
                Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT)  
                        .show();  
                if (endIndex > Movie.movies.length) {  
                    endIndex = Movie.movies.length;  
                }  
                for (int i = startIndex; i < endIndex; i++) {  
                    LoadImageTask task = new LoadImageTask();  
                    taskCollection.add(task);  
                    task.execute(Movie.movies[i]);  
                }  
                page++;  
            } else {  
                Toast.makeText(getContext(), "已没有更多影片", Toast.LENGTH_SHORT)  
                        .show();  
            }  
        } else {  
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();  
        }  
    }  
  
    /** 
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。 
     */  
    public void checkVisibility() {  
        for (int i = 0; i < imageViewList.size(); i++) {  
            ImageView imageView = imageViewList.get(i);  
            int borderTop = (Integer) imageView.getTag(R.string.border_top);  
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);  
            if (borderBottom > getScrollY()  && borderTop < getScrollY() + scrollViewHeight) {  
                String imageUrl = (String) imageView.getTag(R.string.image_url);  
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);  
                if (bitmap != null) {  
                    imageView.setImageBitmap(bitmap);  
                } else {  
                	Movie movie=new Movie();
                	movie.setIcon(imageUrl);
                    LoadImageTask task = new LoadImageTask(imageView);  
                    task.execute(movie);  
                }  
            } else {  
                imageView.setImageResource(R.drawable.empty_photo);  
            }  
        }  
    }  
  
    /** 
     * 判断手机是否有SD卡。 
     *  
     * @return 有SD卡返回true，没有返回false。 
     */  
    private boolean hasSDCard() {  
        return Environment.MEDIA_MOUNTED.equals(Environment  
                .getExternalStorageState());  
    }  
    /** 
     * 异步下载头像图片的任务。 
     *  
     * @author guolin 
     */  
    class LoadHeadImageTask extends AsyncTask<String, Void, Bitmap> {  
  
        /** 
         * 图片的URL地址 
         */  
        private String mImageUrl;  
        
        private int width;  
        
  
        /** 
         * 可重复使用的ImageView 
         */  
        private ImageView mImageView;  
  
        public LoadHeadImageTask() {  
        }  
  
        /** 
         * 将可重复使用的ImageView传入 
         *  
         * @param imageView 
         */  
        public LoadHeadImageTask(ImageView imageView) {  
            mImageView = imageView;  
        }  
  
        @Override  
        protected Bitmap doInBackground(String... params) {  
            mImageUrl = params[0];  
            width=Integer.parseInt(params[1]) ;  
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);  
            if (imageBitmap == null) {  
            	imageBitmap=imageLoader.loadImage(mImageUrl, width);
            }  
            return imageBitmap;  
        }  
 
    }  
  
    /** 
     * 异步下载图片的任务。 
     *  
     * @author guolin 
     */  
    class LoadImageTask extends AsyncTask<Movie, Void, Bitmap> {  
  
        /** 
         * 图片的URL地址 
         */  
        private Movie movie;  
  
        /** 
         * 可重复使用的ImageView 
         */  
        private ImageView mImageView;  
  
        public LoadImageTask() {  
        }  
  
        /** 
         * 将可重复使用的ImageView传入 
         *  
         * @param imageView 
         */  
        public LoadImageTask(ImageView imageView) {  
            mImageView = imageView;  
        }  
  
        @Override  
        protected Bitmap doInBackground(Movie... params) {  
        	movie = params[0];  
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(movie.getIcon());  
            if (imageBitmap == null) {  
            	imageBitmap=imageLoader.loadImage(movie.getIcon(), columnWidth);
                //imageBitmap = loadImage(mImageUrl);  
            }  
            return imageBitmap;  
        }  
  
        @Override  
        protected void onPostExecute(Bitmap bitmap) {  
            if (bitmap != null) {  
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);  
                int scaledHeight = (int) (bitmap.getHeight() / ratio);  
                addMovie(bitmap, columnWidth, scaledHeight);  
            }  
            taskCollection.remove(this);  
        }  
  
  
        /** 
         * 向ImageView中添加一张图片 
         *  
         * @param bitmap 
         *            待添加的图片 
         * @param imageWidth 
         *            图片的宽度 
         * @param imageHeight 
         *            图片的高度 
         */  
        private void addMovie(Bitmap bitmap, int imageWidth, int imageHeight) {  
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  
                    imageWidth, imageHeight);  
            if (mImageView != null) {  
                mImageView.setImageBitmap(bitmap);  
            } else {  
                ImageView imageView = new ImageView(getContext());  
                imageView.setLayoutParams(params);  
                imageView.setImageBitmap(bitmap);  
                imageView.setScaleType(ScaleType.FIT_XY);  
                imageView.setPadding(0, 0, 8, 0);
                imageView.setTag(R.string.image_url, movie.getIcon());
                
                FrameLayout imageLayout=new FrameLayout(getContext());
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(imageWidth, LayoutParams.WRAP_CONTENT);  
                
                
                
                imageLayout.setLayoutParams(frameParams);
                
                View mentView = LayoutInflater.from(getContext()).inflate(R.layout.movie_ment, null);
                LinearLayout.LayoutParams mentParams = new LinearLayout.LayoutParams(imageWidth-8, 80);  
               
                
                mentView.setLayoutParams(mentParams);
                
                TextView ment=(TextView)mentView.findViewById(R.id.ment);
                ment.setText("11");
                imageLayout.addView(imageView);
                imageLayout.addView(mentView);
                
               
         
                LinearLayout linearLayout=findColumnToAdd(imageView, imageHeight);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                
             
                //设置底部标签
                View tagView = LayoutInflater.from(getContext()).inflate(R.layout.movie_tag, null);
              
                
                RelativeLayout relativeLayout=(RelativeLayout)tagView.findViewById(R.id.movie_column_tag);
                LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(imageWidth-8, LayoutParams.WRAP_CONTENT);  
                tagParams.setMargins(0, 0, 0, 25);
                relativeLayout.setLayoutParams(tagParams);
                
                TextView title=(TextView)tagView.findViewById(R.id.movie_title);
                title.setText(movie.getName());
                
                RatingBar starBar=(RatingBar)tagView.findViewById(R.id.movie_star);
                starBar.setRating(movie.getStart());
                
   
                TextView score=(TextView)tagView.findViewById(R.id.movie_score);
                score.setText("9.5");
                
                 
                linearLayout.addView(imageLayout);  
                linearLayout.addView(tagView);
                
                linearLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getContext(),MovieDetailActivity.class);
						intent.putExtra("movie", movie);
						getContext().startActivity(intent);
					}
				});
              
                
                imageViewList.add(imageView);  
            }  
        }  
  
        /** 
         * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。 
         *  
         * @param imageView 
         * @param imageHeight 
         * @return 应该添加图片的一列 
         */  
        private LinearLayout findColumnToAdd(ImageView imageView,  
                int imageHeight) {  
            if (firstColumnHeight <= secondColumnHeight) {  
                 imageView.setTag(R.string.border_top, firstColumnHeight);  
                 firstColumnHeight += imageHeight;  
                 imageView.setTag(R.string.border_bottom, firstColumnHeight);  
                
                 return firstColumn;  
                
            } else {  
                imageView.setTag(R.string.border_top, secondColumnHeight);  
                secondColumnHeight += imageHeight;  
                imageView.setTag(R.string.border_bottom, secondColumnHeight);  
                return secondColumn;  
            }  
        }  
  
    }
	

	
  

}
