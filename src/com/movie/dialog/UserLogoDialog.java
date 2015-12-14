package com.movie.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.movie.R;
import com.movie.app.BaseDialog;

public class UserLogoDialog extends BaseDialog {

	DatePicker missDate;
	TimePicker missTime;

	public UserLogoDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_missdate);
		missDate = ((DatePicker)findViewById(R.id.miss_date));
		missTime = ((TimePicker)findViewById(R.id.miss_time));
	}

	@Override
	public void setTitle(CharSequence text) {
		super.setTitle(text);
	}

	public void setButton(CharSequence text,
			DialogInterface.OnClickListener listener) {
		super.setButton1(text, listener);
	}

	public void setButton(CharSequence text1,
			DialogInterface.OnClickListener listener1, CharSequence text2,
			DialogInterface.OnClickListener listener2) {
		super.setButton1(text1, listener1);
		super.setButton2(text2, listener2);
	}

	public DatePicker getMissDate() {
		return missDate;
	}
	public TimePicker getMissTime() {
		return missTime;
	}
	public void setMissDate(int y,int m,int d){
		if(null!=missDate){
			missDate.updateDate(y, m, d);
		}
	}



	
}
