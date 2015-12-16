package com.movie.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.movie.R;
import com.movie.app.BaseDialog;

public class UserDateDialog extends BaseDialog {

	DatePicker userDate;

	public UserDateDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_date);
		userDate = ((DatePicker) findViewById(R.id.user_date));
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

	public DatePicker getUserDate() {
		return userDate;
	}

	

}
