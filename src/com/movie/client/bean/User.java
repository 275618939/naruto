package com.movie.client.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.movie.util.Images;

public class User extends BaseBean implements Serializable {

	public static final int USER_LOVE = 0X111; // 会员心动
	private static final long serialVersionUID = 6477157496175784788L;
	private String memberId;
	private String portrait;
	private String nickname;
	private String mobile;
	private String email;
	private String birthday;
	private Integer sex;
	private List<Integer> hobbies;
	private String signature;
	private List<Integer> filmType;
	private Integer charm;
	private Integer love;
	private Integer stage;
	private Integer tryst;
	private Integer loveMovie;
	private String loveMovieName;
	private String sid;
	private String name;
	private String constell;
	private int filmId;
	private String filmName;
	private int filmCnt;
	private long face;
	private int faceCnt; 

	public User() {
	}

	public User(String sid, String name) {
		this.sid = sid;
		this.name = name;
	}

	public User(String memberId, String portrait, String nickname,
			String mobile, String email, String birthday, Integer sex,
			List<Integer> hobbies, String signature, List<Integer> filmType,
			Integer charm, Integer love, String sid, String name) {
		super();
		this.memberId = memberId;
		this.portrait = portrait;
		this.nickname = nickname;
		this.mobile = mobile;
		this.email = email;
		this.birthday = birthday;
		this.sex = sex;
		this.hobbies = hobbies;
		this.signature = signature;
		this.filmType = filmType;
		this.charm = charm;
		this.love = love;
		this.sid = sid;
		this.name = name;
	}

	public static List<User> getTempData() {
		List<User> users = new ArrayList<User>();

		int size = 10;
		User user = null;
		List<Integer> hobbies = new ArrayList<Integer>();
		hobbies.add(1);
		hobbies.add(2);
		hobbies.add(3);
		int sexs[]=new int[]{0,1};
		String[] nickName = new String[] { "鸣人", "佐助", "大蛇丸", "小樱", "纲手", "三代","小李", "天天", "夏洛特", "烦恼" };
		for (int i = 0; i < size; i++) {
			user = new User();
			user.setNickname(nickName[i]);
			user.setSignature("伙影，用了还想用!");
			user.setPortrait(Images.imageUrls[i]);
			user.setCharm(200);
			user.setLove(6000);
			user.setTryst(1);
			user.setLoveMovie(1);
			user.setSex(sexs[i%1]);
			user.setLoveMovieName("魔兽世界");
			user.setConstell("双子座");
			user.setPortrait(Images.imageUrls[i]);
			user.setMemberId("016f8b8161a12f2e");
			user.setHobbies(hobbies);
			users.add(user);
		}
		return users;

	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public List<Integer> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<Integer> hobbies) {
		this.hobbies = hobbies;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<Integer> getFilmType() {
		return filmType;
	}

	public void setFilmType(List<Integer> filmType) {
		this.filmType = filmType;
	}

	public Integer getCharm() {
		return charm;
	}

	public void setCharm(Integer charm) {
		this.charm = charm;
	}

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getTryst() {
		return tryst;
	}

	public void setTryst(Integer tryst) {
		this.tryst = tryst;
	}

	public String getConstell() {
		return constell;
	}

	public void setConstell(String constell) {
		this.constell = constell;
	}

	public Integer getLoveMovie() {
		return loveMovie;
	}

	public void setLoveMovie(Integer loveMovie) {
		this.loveMovie = loveMovie;
	}

	public String getLoveMovieName() {
		return loveMovieName;
	}

	public void setLoveMovieName(String loveMovieName) {
		this.loveMovieName = loveMovieName;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public int getFilmCnt() {
		return filmCnt;
	}

	public void setFilmCnt(int filmCnt) {
		this.filmCnt = filmCnt;
	}

	public long getFace() {
		return face;
	}

	public void setFace(long face) {
		this.face = face;
	}

	public int getFaceCnt() {
		return faceCnt;
	}

	public void setFaceCnt(int faceCnt) {
		this.faceCnt = faceCnt;
	}

	public int getFilmId() {
		return filmId;
	}

	public void setFilmId(int filmId) {
		this.filmId = filmId;
	}
	
	
	
	

}
