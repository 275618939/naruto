package com.movie.state;

public enum ErrorState {
	
	Success(1,"成功"),
	Failure(2,"失败"),
	SessionInvalid(3,"会话不存在或者已过期"),
	MissParameter(4,"缺少参数"),
	ParamTypeError(5,"参数类型错误"),
	LoginIllegal(6,"登录名非法"),
	CaptchaIllegal(7,"验证码非法"),
	CaptchaSendLimit(8,"当日验证码发送次数太多"),
	LoginLimit(9,"当日登录次数太多"),
	ParamOutRange(10,"参数越界"),
	DatabaseExcetpion(11,"数据库操作异常"),
	CaptchaError(12,"验证码错误"),
	CheckPwdError(13,"用户名或者密码错误"),
	ObjectNotExist(14,"对象不存在"),
	ObjectConflict(15,"对象已存在"),

	CallBackNotExist(95,"回调对象不存在"),
	RuntimeException(96,"运行时异常"),
	ConvertJsonFasle(97,"结果转换为JSON失败"),
	InvalidResource(98,"请求的资源不存在"),
	UNDefinedUrl(99,"无效的URI");
	
	private int state;
	private String message;
	
	private ErrorState(int state,String message){
		this.state = state;
		this.message = message;
	}

	public int getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public String toString(){
		StringBuilder builder=new StringBuilder();
		builder.append("{\"state\":").append(state);
		builder.append(",\"message\":\"").append(message);
		builder.append("\"}");
		return builder.toString();
	}
	
}
