package com.movie.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

@SuppressLint("SimpleDateFormat")
public class StringUtil {

	public static boolean isEmpty(Object obj) {

		return null == obj || "".equals(obj.toString().trim());
	}

	public static boolean isNotEmpty(Object obj) {

		return !isEmpty(obj);
	}

	public static String getSequenceId() {
		String mark = String.valueOf(System.currentTimeMillis());
		return mark;
	}

	public static String getCurrentlyDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	public static String transformDateTime(long t) {
		Date date = new Date(t);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String getCurrentlyDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(new Date());
	}

	public static String getIMEI(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

	}

	public static int StrToInt(String pattern, String value)
			throws ParseException {
		// 声明一个DecimalFormat对象
		DecimalFormat df = new DecimalFormat(pattern);
		int result = (int) (df.parse(value).doubleValue() * 100);
		return result;
	}

	public static String dateChangeStr(int y, int m, int d) {

		StringBuilder builder = new StringBuilder();
		builder.append(y);
		builder.append((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
		builder.append((d < 10) ? "0" + d : d);
		return builder.toString();
	}

	public static String strChangeDate(String date) {

		if (null == date)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(date.substring(0, 4)).append("-");
		builder.append(date.substring(4, 6)).append("-");
		builder.append(date.substring(6, 8));
		return builder.toString();
	}

	public static String strChangeLocalDate(String date) {

		if (null == date)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(date.substring(0, 4)).append("年");
		builder.append(date.substring(4, 6)).append("月");
		builder.append(date.substring(6, 8)).append("日");
		return builder.toString();
	}
	public static int dateCompareByCurrent(String time) {
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int result=-1;
		try {
			Date date = format.parse(time);
			Date currentDate = new Date();
			result = date.compareTo(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
			result=-2;
		}
	
		return result;
	
	}
    public static int dateCompareByCurrent(String time,int hour) {
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int result=-1;
		try {
			Date date = format.parse(time);
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, hour);
			Date currentDate = new Date();
			result = calendar.getTime().compareTo(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
			result=-2;
		}
	
		return result;
	
	}

	public static String getShortStr(String data, int len) {

		if (null == data)
			return "";
		if (data.length() <= len)
			return data;
		data = data.substring(0, len);

		return data;
	}
	

	public static String getShortStrBySym(String data, String sym) {

		if (null == data)
			return "";
		int index = data.lastIndexOf(sym);
		data = data.substring(0, index);
		return data;
	}
	
	

	public static int[] strChangeInt(String date) {

		int[] temp = new int[3];
		temp[0] = Integer.parseInt(date.substring(0, 4));
		temp[1] = Integer.parseInt(date.substring(5, 7));
		temp[2] = Integer.parseInt(date.substring(8, 10));
		return temp;
	}

	public static String listToString(List<String> stringList, String sp) {
		if (stringList == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (String string : stringList) {
			if (flag) {
				result.append(sp);
			} else {
				flag = true;
			}
			result.append(string);
		}
		return result.toString();
	}

	public static String listToStringByMap(List<Integer> stringList,
			Map<Integer, String> values, String sp) {
		if (stringList == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (Integer value : stringList) {
			if (flag) {
				result.append(sp);
			} else {
				flag = true;
			}

			result.append(values.get(value));
		}
		return result.toString();
	}

}
