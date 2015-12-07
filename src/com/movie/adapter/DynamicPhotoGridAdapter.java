package com.movie.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.movie.R;
import com.movie.app.BaseObjectListAdapter;
import com.movie.client.bean.BaseBean;
import com.movie.util.Bimp;
import com.movie.util.ImageItem;

public class DynamicPhotoGridAdapter extends BaseObjectListAdapter implements OnItemClickListener{
	
	
	public DynamicPhotoGridAdapter(Context context, Handler mHandler,List<? extends BaseBean> datas) {
		super(context, mHandler, datas);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_published_grida,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ImageItem imageItem= (ImageItem)getItem(position);	
		if(null!=imageItem){
			holder.image.setImageBitmap(imageItem.getBitmap());
		}
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (position == Bimp.tempSelectBitmap.size()) {
			
		} else {
			/*Intent intent = new Intent(this.context,GalleryActivity.class);
			intent.putExtra("position", "1");
			intent.putExtra("ID", position);
			startActivity(intent);*/
		}
		
	}

	
	


	

	
}
