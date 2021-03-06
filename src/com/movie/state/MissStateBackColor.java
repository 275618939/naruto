package com.movie.state;

import com.movie.R;

public enum MissStateBackColor {

	HaveInHand(1, R.drawable.miss_background_in), Expired(2,
			R.drawable.miss_background_expire), Completed(3,
			R.drawable.miss_background_end);
	private int state;
	private int sourceId;

	private MissStateBackColor(int state, int sourceId) {
		this.state = state;
		this.sourceId = sourceId;
	}

	public int getState() {
		return state;
	}

	public int getSourceId() {
		return sourceId;
	}

	public static MissStateBackColor getState(int id) {
		for (MissStateBackColor m : MissStateBackColor.values()) {
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
