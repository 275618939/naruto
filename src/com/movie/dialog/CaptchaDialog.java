package com.movie.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.R;
import com.movie.app.BaseDialog;

public class CaptchaDialog extends BaseDialog {

	private TextView change;
	private EditText realCode;
	private ImageView codeView;

	public CaptchaDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_captcha);
		realCode = ((EditText)findViewById(R.id.captcha_input));
		codeView = (ImageView)findViewById(R.id.captcha);
		change = (TextView)findViewById(R.id.change);
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

	public TextView getChange() {
		return change;
	}

	public EditText getRealCode() {
		return realCode;
	}

	public ImageView getCodeView() {
		return codeView;
	}
	



}
