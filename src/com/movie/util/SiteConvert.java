package com.movie.util;

import java.text.DecimalFormat;



public class SiteConvert {


	public final static float PI = 3.14f;
	public final static DecimalFormat df = new DecimalFormat("0.0");

	/**
	 * 
	 * @param longitude
	 *           经度
	 * @param latitude
	 *           维度
	 * @return
	 */
	public static String GetSite(double longitude, double latitude) {

		double lg=(longitude-latitude)*PI/28125.0;
		double lt=(longitude-latitude)*PI/28125.0;
		lt=lt*Math.cos((longitude+longitude)/2*PI/180000000.0);
		double dist=Math.sqrt(lg*lg+lt*lt);
		return df.format((dist/1000));
	}

	public static void main(String[] args) {

		System.out.println(GetSite(0, 120));

	}

}
