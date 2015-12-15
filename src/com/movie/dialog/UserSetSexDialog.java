package com.movie.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.movie.R;
import com.movie.app.BaseDialog;
import com.movie.state.SexState;

public class UserSetSexDialog extends BaseDialog implements OnCheckedChangeListener{

	RadioGroup sexRadioGroup;
	int sex;

	public UserSetSexDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_sex);
		sexRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		sexRadioGroup.setOnCheckedChangeListener(this);
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


	public int getSex() {
		return sex;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.men) {
			sex = SexState.MAN.getState();
		} else {
			sex = SexState.WOMAN.getState();
		} 
	}



	
}
