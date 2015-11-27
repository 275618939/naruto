package com.movie.state;

public enum SexState {

	MAN(1, "♂男"), WOMAN(0, "♀女");

	private int state;
	private String message;

	private SexState(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public static SexState getState(int state) {

		switch (state) {
		case 0:

			return WOMAN;
		case 1:

			return MAN;
	

		default:
			return WOMAN;
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
