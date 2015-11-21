package com.movie.app;

public enum MissStage {

	Apply(1, "申请"), BeInvited(2, "被邀请"), At(3, "应约"), End(3, "已结束");

	private int state;
	private String message;

	private MissStage(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public static MissStage getState(int id) {

		try {
			return MissStage.values()[id];
		} catch (Throwable e) {
			throw new RuntimeException("not exist MissStage: " + id);
		}
//		switch (state) {
//		case 1:
//
//			return Apply;
//		case 2:
//
//			return BeInvited;
//		case 3:
//
//			return At;
//		case 4:
//
//			return End;
//
//		default:
//			return End;
//		}

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
}
