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

	public static SexState getState(int id) {

		for (SexState s : SexState.values()) {
			if (s.getState() == id) {
				return s;
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
