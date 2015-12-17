package com.movie.state;

public enum MissStage {

	Apply(1, "申请"), BeInvited(2, "被邀请"), At(3, "应约"), End(4, "已结束");

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
		for (MissStage m : MissStage.values()) {
			if (m.getState() == id) {
				return m;
			}
		}
		return null;
		/*
		 * try { return MissStage.values()[id]; } catch (Throwable e) { throw
		 * new RuntimeException("not exist MissStage: " + id); }
		 */

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
}
