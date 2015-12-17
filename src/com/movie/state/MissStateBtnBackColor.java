package com.movie.state;

import com.movie.R;

public enum MissStateBtnBackColor {

	HaveInHand(1, R.color.btn_add), Expired(2, R.color.tag6), Completed(3,R.color.tag6);
	private int state;
	private int sourceId;

	private MissStateBtnBackColor(int state, int sourceId) {
		this.state = state;
		this.sourceId = sourceId;
	}

	public int getState() {
		return state;
	}

	public int getSourceId() {
		return sourceId;
	}

	public static MissStateBtnBackColor getState(int id) {
		for (MissStateBtnBackColor m : MissStateBtnBackColor.values()) {
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
