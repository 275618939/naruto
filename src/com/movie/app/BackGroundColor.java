package com.movie.app;

import com.movie.R;

public enum BackGroundColor {

	Tag1(0, R.color.tag1), Tag2(1, R.color.tag2), Tag3(2, R.color.tag3), Tag4(
			3, R.color.tag4), Tag5(4, R.color.tag5), Tag6(5, R.color.tag6), Tag7(
			6, R.color.tag7), Tag8(7, R.color.tag8);
	private int state;
	private int sourceId;

	private BackGroundColor(int state, int sourceId) {
		this.state = state;
		this.sourceId = sourceId;
	}

	public int getState() {
		return state;
	}

	public int getSourceId() {
		return sourceId;
	}

	public static BackGroundColor getState(int id) {
		try {
			return BackGroundColor.values()[id];
		} catch (Throwable e) {
			throw new RuntimeException("not exist BackGroundColor: " + id);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"sourceId\":\"").append(sourceId);
		builder.append("\"}");
		return builder.toString();
	}
}
