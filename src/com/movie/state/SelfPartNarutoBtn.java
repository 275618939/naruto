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

	public static SelfPartNarutoBtn getState(int id) {

		for (SelfPartNarutoBtn m : SelfPartNarutoBtn.values()) {
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
