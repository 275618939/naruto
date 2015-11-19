package com.movie.test;

import com.movie.util.MovieScore;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(MovieScore.GetScore(600,10));
	
		
	}
	 public static int[] strChangeInt(String date){
			
			int [] temp=new int[3];
			temp[0]=Integer.parseInt(date.substring(0, 4));
			temp[1]=Integer.parseInt(date.substring(6, 7));
			temp[2]=Integer.parseInt(date.substring(8, 9));
			return temp;
		}

}
