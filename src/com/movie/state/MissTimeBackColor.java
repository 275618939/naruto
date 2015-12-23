package com.movie.state;

import com.movie.R;

public enum MissTimeBackColor {

	HaveInHand(1, R.drawable.miss_background_in), Expired(0,R.drawable.miss_background_expire), Completed(-1,R.drawable.miss_background_expire), Un(-2,R.drawable.miss_background_expire);
	private int state;
	private int sourceId;

	private MissTimeBackColor(int state, int sourceId) {
		this.state = state;
		this.sourceId = sourceId;
	}

	public int getState() {
		return state;
	}

	public int getSourceId() {
		return sourceId;
	}

	public static MissTimeBackColor getState(int id) {
		for (MissTimeBackColor m : MissTimeBackColor.values()) {
			if (m.getState() == id) {
				return m;
			}
		}
		return null;

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"sourceId\":\"").append(sourceId);
		builder.append("\"}");
		return builder.toString();
	}
}
