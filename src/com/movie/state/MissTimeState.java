package com.movie.state;

public enum MissTimeState {

	HaveInHand(1, "正在进行"), Expired(0, "已到期"), Completed(-1, "已结束"), Un(-2, "未知");

	private int state;
	private String message;

	private MissTimeState(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public static MissTimeState getState(int id) {
		for (MissTimeState m : MissTimeState.values()) {
			if (m.getState() == id) {
				return m;
			}
		}
		return null;

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
}
