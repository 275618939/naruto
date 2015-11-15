package com.movie.test;

import com.movie.app.Constant;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://101.200.176.217/api/captcha/13800138000";
		String Captcha_API_URL = Constant.SERVER_URL+ "/captcha/";
		System.out.println(url.indexOf(Captcha_API_URL));
		
		/*int [] dd= strChangeInt("2012-02-34");
		System.out.print(dd[1]);*/
	
		
	}
	 public static int[] strChangeInt(String date){
			
			int [] temp=new int[3];
			temp[0]=Integer.parseInt(date.substring(0, 4));
			temp[1]=Integer.parseInt(date.substring(6, 7));
			temp[2]=Integer.parseInt(date.substring(8, 9));
			return temp;
		}

}
