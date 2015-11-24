package com.movie.util;

import java.text.DecimalFormat;

/**
 * IMDB 计算公式 贝叶斯统计的算法得出的加权分(Weighted Rank-WR) weighted rank (WR) = (v ÷ (v+m)) ×
 * R + (m ÷ (v+m)) × C where:
 * 
 * R = average for the movie (mean) = (Rating) -单部电影的得分 v = number of votes for
 * the movie = (votes) -单部电影的有效评分人数 m = minimum votes required to be listed in
 * the Top 250 (currently 25000) -入选top250榜单所需最低的有效评分人数 C = the mean vote across
 * the whole report (currently 7.0) -所有影片的平均分
 * 
 * @author liu
 * 
 */
public class UserCharm {

	public final static DecimalFormat df = new DecimalFormat("0.0");
	public final static float currently = 7.0f;
	public final static int minimum = 3000;

	/**
	 * 
	 * @param rating
	 *            电影的得
	 * @param votes
	 *            电影的有效评分人
	 * @return
	 */
	public static String GetScore(float rating, float votes) {

		float avg = rating / votes;
		float a = (votes / (votes + minimum)) * avg;
		float b = (minimum / (votes + minimum)) * currently;
		float rank = a + b;
		return df.format(rank);
	}

	public static void main(String[] args) {

		System.out.println(GetScore(0, 120));

	}

}
