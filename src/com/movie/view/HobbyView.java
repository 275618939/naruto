package com.movie.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.movie.R;

public class HobbyView extends ScrollView implements OnTouchListener {

	Map<Integer,String> hobbies;

	List<Integer> userHobbies;

	/**
	 * 每页要加载的喜好数量
	 */
	public static final int PAGE_SIZE = 50;

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
	 * 当前第三列的高度
	 */
	private int thirdColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;
	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;
	/**
	 * 第三列的布局
	 */
	private LinearLayout thirdColumn;

	/**
	 * HobbyView 下的直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * HobbyView 布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 在Handler中进行喜好可见性检查的判断，以及加载更多喜好的操作。
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			HobbyView myScrollView = (HobbyView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部 开始加载下一页的喜好
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()) {
					myScrollView.loadMoreHobby();
				}

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
	public HobbyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	/**
	 * 进行一些关键性的初始化操作，获取HobbyView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的喜好。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	public void init() {
		scrollViewHeight = getHeight();
		scrollLayout = getChildAt(0);
		firstColumn = (LinearLayout) findViewById(R.id.first_column);
		secondColumn = (LinearLayout) findViewById(R.id.second_column);
		thirdColumn = (LinearLayout) findViewById(R.id.third_column);
		columnWidth = firstColumn.getWidth();

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
	 * 开始加载喜好信息
	 */
	public void loadMoreHobby() {
		Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
		int startIndex = page * PAGE_SIZE;
		int endIndex = page * PAGE_SIZE + PAGE_SIZE;
		int len = hobbies.size();
		if (startIndex < len) {
			if (endIndex > len) {
				endIndex = len;
			}
			for(Entry<Integer, String> value:hobbies.entrySet()){
				addHobby(value.getKey(),value.getValue());
			}
			/*for (int i = startIndex; i < endIndex; i++) {
				
			}*/
			page++;
		} else {
			Toast.makeText(getContext(), "已没有更多喜好", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 向Layout中添加喜好
	 * 
	 * @param bitmap
	 *            待添加的喜好
	 */
	private void addHobby(Integer id,String text) {

		View hobbyView = LayoutInflater.from(getContext()).inflate(R.layout.hobby_ment, null);
		final Button unselectBtn = (Button) hobbyView.findViewById(R.id.user_unselect);
		final Button selectBtn = (Button) hobbyView.findViewById(R.id.user_select);
		unselectBtn.setTag(String.valueOf(id));
		selectBtn.setTag(String.valueOf(id));
		unselectBtn.setText(text);
		selectBtn.setText(text);
		if (userHobbies.contains(id)) {
			selectBtn.setVisibility(View.VISIBLE);
			selectBtn.setText(text);
			unselectBtn.setVisibility(View.GONE);
		}
		unselectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				selectBtn.setVisibility(View.VISIBLE);
				if (!userHobbies.contains(Integer.parseInt(v.getTag().toString()))) {
					userHobbies.add(Integer.parseInt(v.getTag().toString()));
				}
			}
		});
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				unselectBtn.setVisibility(View.VISIBLE);
				removeSelect(Integer.parseInt(v.getTag().toString()));
			}
		});

		LinearLayout linearLayout = findColumnToAdd(hobbyView, 60);
		if (null != linearLayout) {
			linearLayout.addView(hobbyView);
		}
	}

	/**
	 * 找到此时应该添加喜好的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
	 * 
	 * @param imageView
	 * @param imageHeight
	 * @return 应该添加喜好的一列
	 */
	private LinearLayout findColumnToAdd(View view, int height) {

		if (firstColumnHeight <= secondColumnHeight) {
			if (firstColumnHeight <= thirdColumnHeight) {
				view.setTag(R.string.border_top, firstColumnHeight);
				firstColumnHeight += height;
				view.setTag(R.string.border_bottom, firstColumnHeight);
				return firstColumn;
			}
			view.setTag(R.string.border_top, thirdColumnHeight);
			thirdColumnHeight += height;
			view.setTag(R.string.border_bottom, thirdColumnHeight);
			return thirdColumn;
		} else {
			if (secondColumnHeight <= thirdColumnHeight) {
				view.setTag(R.string.border_top, secondColumnHeight);
				secondColumnHeight += height;
				view.setTag(R.string.border_bottom, secondColumnHeight);
				return secondColumn;
			}
			view.setTag(R.string.border_top, thirdColumnHeight);
			thirdColumnHeight += height;
			view.setTag(R.string.border_bottom, thirdColumnHeight);
			return thirdColumn;
		}
	}

	protected void removeSelect(Integer value) {
		Iterator<Integer> iter = userHobbies.iterator();
		while (iter.hasNext()) {
			Integer b = iter.next();
			if (b.equals(value)) {
				iter.remove();
			}
		}
	}


	public List<Integer> getUserHobbies() {
		return userHobbies;
	}

	public void setUserHobbies(List<Integer> userHobbies) {
		this.userHobbies = userHobbies;
	}

	public void setHobbies(Map<Integer,String> hobbies) {
		this.hobbies = hobbies;
	}

}
