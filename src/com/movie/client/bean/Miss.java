package com.movie.client.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.movie.util.Images;

public class Miss extends BaseBean implements Serializable {


	private static final long serialVersionUID = -2476412515600400457L;
	public static final int AGREE_MISS = 0X111;   		 // 同意约会
	public static final int CANCLE_MISS = 0X112;  		 // 取消约会
	public static final int KICKED_OUT = 0X113;    		 // 踢出约会
	public static final int EVLATOIN_USER = 0X114;		 // 评价用户
	public static final int APPLY_MISS = 0X115;   		 // 报名约会
	public static final int COIN_MISS = 0X116;     		 // 分影币
	public static final int INVITE_MISS = 0X117;   		 // 邀请约会
	public static final int AGREE_INVITE_MISS = 0X118;   // 同意邀请
	//我发起的约会
	public static final int MY_MISS = 0X210;
	//我参与的约会
	public static final int MY_APPLY = 0X211;
	//我应邀的约会
	public static final int MY_INVITATION = 0X212;
	//应约的约会
	public static final int ATTEDD_MISS = 0X215;
	//用户参与的约会
	public static final int USER_INVITATION = 0X213;
	//电影下的约会
	public static final int MOVIE_INVITATION = 0X214;

	//约会类型
	public static final String MISS_KEY = "miss_type";
	//电影下的约会
	public static final String CONDITION_KEY = "miss_query_key";
	private String trystId;
	private String memberId;
	private Integer filmId;
	private String filmName;
	private String runTime;
	private Integer coin;
	private String nickName;
	private String cinemaId;
	private String cinameName;
	private String cinameAddress;
	private List<User> attend;
	private Integer status;
	private Integer stage;
	private String icon;
	private String movieName;
	private String createUserName;
	private String cinameCity;
	private String cinemaName;
	private String cinemaAddress;
	private String cinemaPhone;

	public Miss() {
	}

	public Miss(String trystId, String memberId, Integer filmId,
			String runTime, Integer coin, String cinemaId, List<User> attend,
			Integer status) {
		super();
		this.trystId = trystId;
		this.memberId = memberId;
		this.filmId = filmId;
		this.runTime = runTime;
		this.coin = coin;
		this.cinemaId = cinemaId;
		this.attend = attend;
		this.status = status;
	}

	public Miss(String trystId, String memberId, Integer filmId,
			String runTime, Integer coin, String cinemaId, Integer status) {
		super();
		this.trystId = trystId;
		this.memberId = memberId;
		this.filmId = filmId;
		this.runTime = runTime;
		this.coin = coin;
		this.cinemaId = cinemaId;
		this.status = status;
	}

	public String getTrystId() {
		return trystId;
	}

	public void setTrystId(String trystId) {
		this.trystId = trystId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getFilmId() {
		return filmId;
	}

	public void setFilmId(Integer filmId) {
		this.filmId = filmId;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public List<User> getAttend() {
		return attend;
	}

	public void setAttend(List<User> attend) {
		this.attend = attend;
	}

	public Integer getStatus() {

		return status;
		/*
		 * MissState missState = MissState.getState(status); return
		 * missState.getMessage();
		 */
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCinameName() {
		return cinameName;
	}

	public void setCinameName(String cinameName) {
		this.cinameName = cinameName;
	}

	public String getCinameAddress() {
		return cinameAddress;
	}

	public void setCinameAddress(String cinameAddress) {
		this.cinameAddress = cinameAddress;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getCinemaAddress() {
		return cinemaAddress;
	}

	public void setCinemaAddress(String cinemaAddress) {
		this.cinemaAddress = cinemaAddress;
	}

	public String getCinemaPhone() {
		return cinemaPhone;
	}

	public void setCinemaPhone(String cinemaPhone) {
		this.cinemaPhone = cinemaPhone;
	}

	public static List<Miss> getTempData() {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Miss> misses = new ArrayList<Miss>();
		Random random = new Random();
		int stage = random.nextInt(4) + 1;
		Miss miss = new Miss();
		miss.setCinemaId("016fc79c22d5b3fc");
		miss.setStatus(random.nextInt(3)+1);
		miss.setCoin(0);
		miss.setTrystId("016fc7cd5300bb03");
		miss.setMemberId("016f9266086f4fab");
		miss.setRunTime(dateFormat.format(new Date()));
		miss.setFilmId(2015103001);
		miss.setStage(stage);
		miss.setIcon("http://101.200.176.217/test.jpg");
		miss.setCinameCity("北京");
		miss.setCinameName("博纳国际影城通州店");
		miss.setCinameAddress("北京市通州区杨庄北里天时名苑14号楼F4-01");
		User user = null;
		List<User> users = new ArrayList<User>();
		List<Integer> hobbies = new ArrayList<Integer>();
		hobbies.add(1);
		hobbies.add(2);
		hobbies.add(3);
		for (int i = 0; i < 10; i++) {
			user = new User();
			user.setNickname("鸣人");
			user.setSignature("伙影，用了还想用!");
			user.setPortrait(Images.imageUrls[i]);
			user.setSex(1);
			user.setCharm(200);
			user.setLove(6000);
			user.setPortrait("http://d.3987.com/sqmy_131219/001.jpg");
			user.setMemberId("016f8b8161a12f2e");
			user.setHobbies(hobbies);
			users.add(user);
		}
		miss.setAttend(users);
		misses.add(miss);
		miss = new Miss();
		miss.setCinemaId("016fc79c22d5b3fc");
		miss.setStatus(random.nextInt(3)+1);
		miss.setCoin(0);
		miss.setTrystId("016fc7cd5300bb03");
		miss.setMemberId("016f9266086f4fab");
		miss.setRunTime("2015-11-18 12:34:00");
		miss.setFilmId(2015103001);
		miss.setStage(stage);
		miss.setIcon("http://101.200.176.217/test.jpg");
		miss.setCinameCity("北京");
		miss.setCinameName("博纳国际影城通州店");
		miss.setCinameAddress("北京市通州区杨庄北里天时名苑14号楼F4-01");
		miss.setAttend(users);
		misses.add(miss);
		miss = new Miss();
		miss.setCinemaId("016fc79c22d5b3fc");
		miss.setStatus(random.nextInt(3)+1);
		miss.setCoin(0);
		miss.setTrystId("016fc7cd5300bb03");
		miss.setMemberId("016f9266086f4fab");
		miss.setRunTime("2015-11-17 8:34:00");
		miss.setFilmId(2015103001);
		miss.setStage(stage);
		miss.setIcon("http://101.200.176.217/test.jpg");
		miss.setCinameCity("北京");
		miss.setCinameName("博纳国际影城通州店");
		miss.setCinameAddress("北京市通州区杨庄北里天时名苑14号楼F4-01");
		miss.setAttend(users);
		misses.add(miss);
		
		return misses;

	}

	public String getCinameCity() {
		return cinameCity;
	}

	public void setCinameCity(String cinameCity) {
		this.cinameCity = cinameCity;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}
	
	
	

}
