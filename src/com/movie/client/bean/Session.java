package com.movie.client.bean;

import java.io.Serializable;

public class Session extends BaseBean implements Serializable {
	private static final long serialVersionUID = 2659447608299292924L;
	String sid;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
