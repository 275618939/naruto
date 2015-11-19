package com.movie.client.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：电影信息</br>
 */
public class Movie extends BaseBean implements Serializable {
	private static final long serialVersionUID = -2524097215473990057L;

	public final static Movie[] movies = new Movie[] {
			new Movie(1,"星际穿越","http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",20, 4, 90),
			new Movie(2,
					"移动迷宫",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					21, 3, 90),
			new Movie(
					3,
					"单身男女",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					22,2, 90),
			new Movie(
					4,
					"超体",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					23, 1.5f, 90),
			new Movie(
					5,
					"红颜祸水",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					24,5, 90),
			new Movie(
					6,
					"求爱大作战",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					25,  3, 90),
			new Movie(
					7,
					"狂野非洲",
					"http://img4.douban.com/view/movie_poster_cover/spst/public/p2230105606.jpg",
					26, 2.5f, 90), };

	int id;
	String name;
	String icon;
	String briefing;
	int miss;
	int interval;
	List<String> directors;
	List<String> stars;
	List<Integer> types;
	List<String> scenarists;
	int type;
	int tryst;
	float start;
	long score;
	int scoreCnt;
    String playTime;
	public Movie() {
		super();
	}
	

	public Movie(int id, String name, String icon, int miss, float start,
			long score) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.miss = miss;
		this.start = start;
		this.score = score;
	}


	public Movie(int id, String name, String icon, int miss, int interval,
			List<String> directors, List<String> stars, int type, float start,
			long score) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.miss = miss;
		this.interval = interval;
		this.directors = directors;
		this.stars = stars;
		this.type = type;
		this.start = start;
		this.score = score;
	}
	

	public String getBriefing() {
		return briefing;
	}


	public void setBriefing(String briefing) {
		this.briefing = briefing;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getMiss() {
		return miss;
	}

	public void setMiss(int miss) {
		this.miss = miss;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<String> getDirectors() {
		return directors;
	}

	public void setDirectors(List<String> directors) {
		this.directors = directors;
	}

	public List<String> getStars() {
		return stars;
	}

	public void setStars(List<String> stars) {
		this.stars = stars;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getStart() {
		return start;
	}

	public void setStart(float start) {
		this.start = start;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public static Movie[] getMovies() {
		return movies;
	}


	public String getPlayTime() {
		return playTime;
	}


	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}


	public List<Integer> getTypes() {
		return types;
	}


	public void setTypes(List<Integer> types) {
		this.types = types;
	}


	public List<String> getScenarists() {
		return scenarists;
	}


	public void setScenarists(List<String> scenarists) {
		this.scenarists = scenarists;
	}


	public int getTryst() {
		return tryst;
	}


	public void setTryst(int tryst) {
		this.tryst = tryst;
	}


	public int getScoreCnt() {
		return scoreCnt;
	}
	


	public void setScoreCnt(int scoreCnt) {
		this.scoreCnt = scoreCnt;
	}


	


	
	
	
	
	
	
	

}
