package com.movie.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.movie.R;
import com.movie.fragment.SelfFragment;
import com.movie.util.Bimp;

public class UserPhotoGridAdapter extends BaseAdapter implements OnItemClickListener{
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private Handler mhandler;
	private View parentView;
	private LinearLayout ll_popup;
	private PopupWindow pop;
	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public UserPhotoGridAdapter(Context context,Handler handler,View parentView) {
		this.context=context;
		this.mhandler=handler;
		this.parentView=parentView;
		inflater = LayoutInflater.from(context);
		initPopWindow();
	}
    protected void initPopWindow(){
    	View view = inflater.inflate(R.layout.item_user_photo_popupwindows, null);
		pop = new PopupWindow(view);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable(0));
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
    }
	
	public void update() {
		loading();
	}

	public int getCount() {
		if (Bimp.tempSelectBitmap.size() == SelfFragment.MAX_SHOW_USER_PHOTO) {
			return SelfFragment.MAX_SHOW_USER_PHOTO;
		}
		return (Bimp.tempSelectBitmap.size() + 1);
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == SelfFragment.MAX_SHOW_USER_PHOTO) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	
	protected void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = SelfFragment.PTHOTO_UPDATE;
						mhandler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = SelfFragment.PTHOTO_UPDATE;
						mhandler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (position == Bimp.tempSelectBitmap.size()) {
			ll_popup.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.activity_translate_in));
			pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		} else {
			/*Intent intent = new Intent(this.context,GalleryActivity.class);
			intent.putExtra("position", "1");
			intent.putExtra("ID", position);
			startActivity(intent);*/
		}
		
	}

	
}
