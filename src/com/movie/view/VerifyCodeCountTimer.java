package com.movie.view;

import com.movie.R;

import android.os.CountDownTimer;
import android.widget.TextView;

public class VerifyCodeCountTimer extends CountDownTimer {

	public static final int TIME_COUNT = 121000;// 时间防止从119s开始显示（以倒计时120s为例子）
	private TextView btn;
	private int endStrRid;
	private int normalColor, timingColor;// 未计时的文字颜色，计时期间的文字颜色

	/**
	 * 
	 * @param millisInFuture
	 *            倒计时总时间（如60S，120s等）
	 * @param countDownInterval
	 *            渐变时间（每次倒计1s）
	 * @param btn
	 *            点击的按钮
	 * @param endStrRid
	 *            倒计时结束后，按钮对应显示的文字
	 */
	public VerifyCodeCountTimer(long millisInFuture, long countDownInterval,
			TextView btn, int endStrRid) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}

	public VerifyCodeCountTimer(TextView btn, int endStrRid) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}

	public VerifyCodeCountTimer(TextView btn) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = R.string.resend;
	}

	public VerifyCodeCountTimer(TextView tv_varify, int normalColor,
			int timingColor) {
		this(tv_varify);
		this.normalColor = normalColor;
		this.timingColor = timingColor;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (timingColor > 0) {
			btn.setTextColor(timingColor);
		}
		btn.setEnabled(false);
		btn.setText(millisUntilFinished / 1000 + "s");
	}

	@Override
	public void onFinish() {
		if (normalColor > 0) {
			btn.setTextColor(normalColor);
		}
		btn.setText(endStrRid);
		btn.setEnabled(true);

	}

}
