package com.movie.client.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NearNaruto extends BaseBean implements Parcelable {

	private String memberId;
	private String nickname;
	private String portrait;
	private int birthday;
	private int sex;
	private int loveCnt;
	private long faceTtl;
	private int faceCnt;
	private int trystCnt;
	private int filmId;
	private String filmName;
	private int filmCnt;
	private int longitude;
	private int latitude;

	public NearNaruto() {
		super();
	}

	public NearNaruto(String memberId, String nickname, String portrait,
			int birthday, int sex, int loveCnt, long faceTtl, int faceCnt,
			int trystCnt, int filmId, String filmName, int filmCnt,
			int longitude, int latitude) {
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
		this.filmId = filmId;
		this.filmName = filmName;
		this.filmCnt = filmCnt;
		this.longitude = longitude;
		this.latitude = latitude;
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

	public int getFilmId() {
		return filmId;
	}

	public void setFilmId(int filmId) {
		this.filmId = filmId;
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

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
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
		dest.writeInt(filmId);
		dest.writeString(filmName);
		dest.writeInt(filmCnt);
		dest.writeInt(longitude);
		dest.writeInt(latitude);
	}

	public static final Parcelable.Creator<NearNaruto> CREATOR = new Parcelable.Creator<NearNaruto>() {

		@Override
		public NearNaruto createFromParcel(Parcel source) {
			NearNaruto near = new NearNaruto();
			near.setMemberId(source.readString());
			near.setNickname(source.readString());
			near.setPortrait(source.readString());
			near.setBirthday(source.readInt());
			near.setSex(source.readInt());
			near.setLoveCnt(source.readInt());
			near.setFaceTtl(source.readLong());
			near.setFaceCnt(source.readInt());
			near.setTrystCnt(source.readInt());
			near.setFilmId(source.readInt());
			near.setFilmName(source.readString());
			near.setFilmCnt(source.readInt());
			near.setLongitude(source.readInt());
			near.setLatitude(source.readInt());
			return near;
		}

		@Override
		public NearNaruto[] newArray(int size) {
			return new NearNaruto[size];
		}
	};
}
