package com.movie.client.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Feed extends BaseBean implements Parcelable {

	public static final String TIME = "time";
	public static final String NAME = "name";
	public static final String PORTRAIT = "avatar";
	public static final String CONTENT = "content";
	public static final String CONTENT_IMAGE = "content_image";
	public static final String SITE = "site";
	public static final String COMMENT_COUNT = "comment_count";
	
	private String name;
	private String portrait;
	private String time;
	private String content;
	private List<String> contentImage;
	private String site;
	private int commentCount;

	public Feed() {
		super();
	}

	public Feed(String time, String content, List<String> contentImage, String site,
			int commentCount) {
		super();
		this.time = time;
		this.content = content;
		this.contentImage = contentImage;
		this.site = site;
		this.commentCount = commentCount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getContentImage() {
		return contentImage;
	}

	public void setContentImage(List<String> contentImage) {
		this.contentImage = contentImage;
	}
	

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(time);
		dest.writeString(content);
		dest.writeList(contentImage);
		dest.writeString(site);
		dest.writeInt(commentCount);
		dest.writeString(portrait);
		dest.writeString(name);
	}

	public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {

		@SuppressWarnings("unchecked")
		@Override
		public Feed createFromParcel(Parcel source) {
			Feed feed = new Feed();
			feed.setTime(source.readString());
			feed.setContent(source.readString());
			feed.setContentImage(source.readArrayList(ArrayList.class.getClassLoader()));
			feed.setSite(source.readString());
			feed.setCommentCount(source.readInt());
			feed.setPortrait(source.readString());
			feed.setName(source.readString());
			return feed;
		}

		@Override
		public Feed[] newArray(int size) {
			return new Feed[size];
		}
	};
}
