package com.movie.state;

public enum MissState {

	HaveInHand(1, "正在进行"), Expired(2, "已到期"), Completed(3, "已完成");

	private int state;
	private String message;

	private MissState(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public static MissState getState(int state) {

		switch (state) {
		case 1:

			return HaveInHand;
		case 2:

			return Expired;
		case 3:

			return Completed;

		default:
			return Completed;
		}

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
}
