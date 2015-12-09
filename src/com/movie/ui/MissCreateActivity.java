package com.movie.ui;

import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.movie.R;
import com.movie.app.BaseActivity;
import com.movie.app.Constant;
import com.movie.client.service.CallBackService;
import com.movie.dialog.MissDateDialog;
import com.movie.util.StringUtil;

public class MissCreateActivity extends BaseActivity implements OnClickListener,CallBackService {

	TextView title;
	TextView right;
	EditText runTime;
	EditText coin;
	ImageView clear_runTime;
	ImageView clear_coin;
	MissDateDialog missDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miss_create);
		initViews();
		initEvents();
		initData();
	}
	@Override
	protected void initViews() {
		missDialog =new MissDateDialog(this);
		title = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right_text);
		runTime = (EditText) findViewById(R.id.runTime);
		coin = (EditText) findViewById(R.id.coin);
		coin.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		clear_runTime = (ImageView) findViewById(R.id.clear_runTime);
		clear_coin = (ImageView) findViewById(R.id.clear_coin);
		right.setVisibility(View.VISIBLE);
		

	}

	@Override
	protected void initEvents() {
		
		right.setOnClickListener(this);
		clear_runTime.setOnClickListener(this);
		clear_coin.setOnClickListener(this);
		runTime.setOnClickListener(this);
		runTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDateDiog(v);
					clear_runTime.setVisibility(View.VISIBLE);
				}else{
					clear_runTime.setVisibility(View.GONE);
				}
			}
		});
		coin.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					
					clear_coin.setVisibility(View.VISIBLE);
				}else{
					clear_coin.setVisibility(View.GONE);
				}

			}
		});
		
	}
	@Override
	protected void initData() {
		title.setText("创建约会");
		right.setText("下一步");
		coin.setText("0");
		missDialog.setTitle("选择约会时间");
	}
	private void showDateDiog(View view){
		missDialog.setButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						missDialog.cancel();
					}
				}, "确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						missDialog.dismiss();
						DatePicker datePicker=missDialog.getMissDate();
						TimePicker timePicker=missDialog.getMissTime();
						int year= datePicker.getYear();
						int month=datePicker.getMonth();
						int data=datePicker.getDayOfMonth();
						int h= timePicker.getCurrentHour();
						int m= timePicker.getCurrentMinute();
						String value=StringUtil.dateChangeStr(year,month,data);
						StringBuilder currentDateTime=new StringBuilder(StringUtil.strChangeDate(value));
						currentDateTime.append(" ").append(h<10?"0"+h:h).append(":").append(m<10?"0"+m:m).append(":").append("00");
						runTime.setText(currentDateTime.toString());
					}
				});
		missDialog.show();
	}
		

	

	private void nextActivity() {

		String dateTime = runTime.getText().toString();
		String cointInfo = coin.getText().toString();
		if (dateTime == null || dateTime.isEmpty()) {
			showToask("约会时间不能为空");
			return;
		}
		if(cointInfo.isEmpty()){
			cointInfo="0";
		}
		
		Intent cinemaPoi = new Intent(this, CinemaSearchActivity.class);
		cinemaPoi.putExtra("dateTime", dateTime);
		cinemaPoi.putExtra("cointInfo", cointInfo);
		startActivity(cinemaPoi);
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.runTime:
			showDateDiog(v);
			break;
		case R.id.right_text:
			nextActivity();
			break;
		
		case R.id.clear_runTime:
			runTime.setText("");
			break;
		case R.id.clear_coin:
			coin.setText("");
			break;
			
		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void SuccessCallBack(Map<String, Object> map) {

		hideProgressDialog();
		String code = map.get(Constant.ReturnCode.RETURN_STATE).toString();
		if (Constant.ReturnCode.STATE_1.equals(code)) {
			String tag=map.get(Constant.ReturnCode.RETURN_TAG).toString();
			
		} else {
			String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
			showToask(message);
		}

	}

	@Override
	public void ErrorCallBack(Map<String, Object> map) {
		hideProgressDialog();
		String message = map.get(Constant.ReturnCode.RETURN_MESSAGE).toString();
		showToask(message);
	}

	@Override
	public void OnRequest() {
		showProgressDialog("提示", "请稍后......");

	}

	

}
