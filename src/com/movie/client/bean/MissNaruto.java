package com.movie.client.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MissNaruto extends BaseBean implements Parcelable {

	private String memberId;
	private String nickname;
	private String portrait;
	private int birthday;
	private int sex;
	private int loveCnt;
	private long faceTtl;
	private int faceCnt;
	private int trystCnt;
	private int filmCnt;
	private int stage;
	
	public MissNaruto() {
		super();
	}

	public MissNaruto(String memberId, String nickname, String portrait,
			int birthday, int sex, int loveCnt, long faceTtl, int faceCnt,
			int trystCnt, int filmCnt) {
		super();
		this.memberId = memberId;
		this.nickname = nickname;
		this.portrait = portrait;
		this.birthday = birthday;
		this.sex = sex;
		this.loveCnt = loveCnt;
		this.faceTtl = faceTtl;
		this.faceCnt = faceCnt;
		this.trystCnt = trystCnt;
		this.filmCnt = filmCnt;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getLoveCnt() {
		return loveCnt;
	}

	public void setLoveCnt(int loveCnt) {
		this.loveCnt = loveCnt;
	}

	public long getFaceTtl() {
		return faceTtl;
	}

	public void setFaceTtl(long faceTtl) {
		this.faceTtl = faceTtl;
	}

	public int getFaceCnt() {
		return faceCnt;
	}

	public void setFaceCnt(int faceCnt) {
		this.faceCnt = faceCnt;
	}

	public int getTrystCnt() {
		return trystCnt;
	}

	public void setTrystCnt(int trystCnt) {
		this.trystCnt = trystCnt;
	}
	public int getFilmCnt() {
		return filmCnt;
	}

	public void setFilmCnt(int filmCnt) {
		this.filmCnt = filmCnt;
	}
	

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(memberId);
		dest.writeString(nickname);
		dest.writeString(portrait);
		dest.writeInt(birthday);
		dest.writeInt(sex);
		dest.writeInt(loveCnt);
		dest.writeLong(faceTtl);
		dest.writeInt(faceCnt);
		dest.writeInt(trystCnt);
		dest.writeInt(filmCnt);
		dest.writeInt(stage);
	}

	public static final Parcelable.Creator<MissNaruto> CREATOR = new Parcelable.Creator<MissNaruto>() {

		@Override
		public MissNaruto createFromParcel(Parcel source) {
			MissNaruto near = new MissNaruto();
			near.setMemberId(source.readString());
			near.setNickname(source.readString());
			near.setPortrait(source.readString());
			near.setBirthday(source.readInt());
			near.setSex(source.readInt());
			near.setLoveCnt(source.readInt());
			near.setFaceTtl(source.readLong());
			near.setFaceCnt(source.readInt());
			near.setTrystCnt(source.readInt());
			near.setFilmCnt(source.readInt());
			near.setStage(source.readInt());
			return near;
		}

		@Override
		public MissNaruto[] newArray(int size) {
			return new MissNaruto[size];
		}
	};
}
