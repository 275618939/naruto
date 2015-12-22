package com.movie.client.bean;

public class FeedComment extends BaseBean {

	public static final String NAME = "name";
	public static final String AVATAR = "avatar";
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	private String commentId;
	private String memberId;
	private String portrait;
	private String name;
	private String avatar;
	private String content;
	private String time;
	public FeedComment(){}
	
	public FeedComment(String name, String avatar, String content, String time) {
		super();
		
		this.name = name;
		this.avatar = avatar;
		this.content = content;
		this.time = time;
	}

	public FeedComment(String commentId,String memberId,String portrait,String name, String avatar, String content, String time) {
		super();
		this.commentId=commentId;
		this.memberId=memberId;
		this.portrait=portrait;
		this.name = name;
		this.avatar = avatar;
		this.content = content;
		this.time = time;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
