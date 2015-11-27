package com.movie.state;

public enum SelfPartNarutoBtn {

	KickedOut(1, "踢出"), InProcess(0, "进行中"), Evaluation(2, "评价");

	private int state;
	private String message;

	private SelfPartNarutoBtn(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public static SelfPartNarutoBtn getState(int state) {

		switch (state) {
		case 0:
			return InProcess;
		case 1:
			return KickedOut;
		case 2:
			return Evaluation;
		default:
			return InProcess;
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
