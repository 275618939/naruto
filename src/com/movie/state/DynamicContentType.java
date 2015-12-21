package com.movie.state;

public enum DynamicContentType {

	Text(1, "文本"), Expression(2, "表情"), Photo(3, "图片"),Audio(4,"音频"),Video(5,"视频");

	private int type;
	private String message;

	private DynamicContentType(int type, String message) {
		this.type = type;
		this.message = message;
	}

	public int getType() { 
		return type;
	}

	public String getMessage() {
		return message;
	}

	public static DynamicContentType getState(int id) {
		for (DynamicContentType m : DynamicContentType.values()) {
			if (m.getType() == id) {
				return m;
			}
		}
		return null;

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"type\":").append(type);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
}
