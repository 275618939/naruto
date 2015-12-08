package com.movie.app;

import java.nio.charset.Charset;

/**
 * 
 * @version 1.0
 */
public interface Constant {

	// 服务端web地址
	// public static final String SERVER_URL =
	// "http://192.168.22.25:8081/cms-web";
	public static final String SERVER_ADRESS = "http://101.200.176.217";
	public static final String SERVER_URL = "http://101.200.176.217/api";
	// 服务端IP地址
	// public static final String CIM_SERVER_HOST = "192.168.22.25";
	public static final String CIM_SERVER_HOST = "192.168.1.103";
	// 上报自己位置
	public final static String Member_Location_API_URL = SERVER_URL+ "/member/location";
	// 获取附近的会员
	public final static String Member_Near_API_URL = SERVER_URL+ "/member/near";
	// Sid api
	public final static String Session_API_URL = SERVER_URL + "/anonymous";
	// 用户修改api
	public final static String User_Modify_API_URL = SERVER_URL+ "/member/modify";
	// 用户修改签名 API
	public final static String Sign_Modify_API_URL = SERVER_URL+ "/member/sign";
	// 登陆 api
	public final static String Login_API_URL = SERVER_URL+ "/account/sigin";
	// 登出 api
	public final static String Logout_API_URL = SERVER_URL+ "/account/sigout";
	// 忘记密码
	public final static String Account_Forget_API_URL = SERVER_URL+ "/account/forget";
	// 手机认证
	public final static String Verify_API_URL = SERVER_URL+ "/account/verify";
	// 用户修改密码api
	public final static String SetPwd_API_URL = SERVER_URL+ "/account/setpwd";
	// 查询用户个人信息
	public final static String User_API_URL = SERVER_URL+ "/member/query";
	// 查询其他用户个人信息
	public final static String User_Query_API_URL = SERVER_URL+ "/member/query";
	// 查询喜好信息
	public final static String Hobby_API_URL = SERVER_URL+ "/dictionary/hobby";
	// 修改喜好信息
	public final static String Hobby_Modify_API_URL = SERVER_URL+ "/member/hobby";
	// 用户心动
	public final static String User_Love_API_URL = SERVER_URL+ "/member/love";
	// 查询所有评价信息
	public final static String Dic_Comment_API_URL = SERVER_URL+ "/dictionary/effect";
	// 查询所有影片类型信息
	public final static String Dic_FilmType_API_URL = SERVER_URL+ "/dictionary/filmType";
	// 查询会员评价信息
	public final static String Comment_API_URL = SERVER_URL+ "/member/comment";
	// 查询会员喜好电影信息
	public final static String User_FilmType_API_URL = SERVER_URL+ "/member/film";
	// 上传头像
	public final static String Portrait_Modify_API_URL = SERVER_URL+ "/member/portrait";
	public final static String Regsiter_API_URL = SERVER_URL+ "/account/register";
	public final static String Captcha_API_URL = SERVER_URL+ "/captcha/";
	public final static String Captcha_API_MOBILE_URL = SERVER_URL+ "/captcha";
	// 即将上映电影列表
	public final static String Upcoming_API_URL = SERVER_URL+ "/film/upcoming";
	// 正在上映电影列表
	public final static String Playing_API_URL = SERVER_URL+ "/film/playing";
	// 查询电影详细信息
	public final static String FileDetail_API_URL = SERVER_URL+ "/film/detail";
	// 修改或查询想看电影会员列表
	public final static String FilmLove_API_URL = SERVER_URL+ "/film/love";
	// 获取想看的电影列表
	public final static String Member_FileLove_API_URL = SERVER_URL+ "/member/film";
	// 创建约会
	public final static String Miss_Create_API_URL = SERVER_URL+ "/tryst/create";
	// 创建电影评论
	public final static String Movie_Comment_Create_API_URL = SERVER_URL+ "/film/comment";
	// 查询电影评论
	public final static String Movie_Comment_Query_API_URL = SERVER_URL+ "/film/comment";
	// 查询电影下的约会
	public final static String Miss_Film_Query_API_URL = SERVER_URL+ "/film/tryst";
	// 查询会员发起的约会
	public final static String Miss_Query_API_URL = SERVER_URL+ "/tryst/query";
	// 查询会员参与的约会
	public final static String Miss_Touch_Query_API_URL = SERVER_URL+ "/tryst/touch";
	// 查询会员应约的约会
	public final static String Miss_Attend_Query_API_URL = SERVER_URL+ "/tryst/attend";
	// 查询约会参与的会员
	public final static String Miss_Hope_Query_API_URL = SERVER_URL+ "/tryst/hope";
	// 撤销约会
	public final static String Miss_Cancel_API_URL = SERVER_URL+ "/tryst/cancel";
	// 同意约会
	public final static String Miss_Agree_API_URL = SERVER_URL+ "/tryst/agree";
	// 报名约会
	public final static String Miss_Apply_API_URL = SERVER_URL+ "/tryst/apply";
	// 同意约会
	public final static String Agree_Apply_API_URL = SERVER_URL+ "/tryst/agree";
	
	// 查询会员列表By颜值
	public final static String Member_ByFace_Query_API_URL = SERVER_URL+ "/member/byFace";
	// 查询会员列表By心动
	public final static String Member_Love_Query_API_URL = SERVER_URL+ "/member/byLove";
	// 查询会员列表By最近
	public final static String Member_Near_Query_API_URL = SERVER_URL+ "/member/near";
	// 查询城市id
	public final static String Region_API_URL = SERVER_URL+ "/region";

	public static final int CIM_SERVER_PORT = 23456;
	
    public static class MissBtnStatus{
    	
    	//约会时间跟当前时间的最大允许撤销时间差
    	public final static int MAX_MISS_CANCEL_HOUR=12;
    	
    }
    public static final int UPLOAD_LOCATION_TIME=1*60*1000;
    public static final int UPLOAD_SCANSPAN=1000;

	public static class Page {
		// 首页
		public static final int FIRST_PAGE = 0;
		// 下一页
		public static final int NEXT_PAGE = 1;
		// 默认显示数据大小
		public static final int DEFAULT_SIZE = 20;
		// 想看电影显示大小
		public static int WANT_SEE_MOIVE_SIZE = 5;
		// 喜好显示最大项
		public final static int MAXHOBBIES = 3;
		// 评价最大显示项
		public final static int COMMENTS_MAX_SHOW = 4;
		// 电影评论最大显示项
		public final static int MOVIES_COMMENTS_MAX_SHOW = 3;
		//附近的会员或动态，与本人的最小距离
		public final static int MIN_DISTANCE=2;
		// 获取附近的会员或动态，与本人的最远距离
		public final static int MAX_DISTANCE=10;
		// 动态图片最大显示数
		public final static int MAX_DYNAMIC=4;
		// 用户最大上传个人照片
		public final static int MAX_SHOW_USER_PHOTO=4;
	}

	public static class Sex {
		// 女
		public static final int WOMEN = 0;
		// 男
		public static final int MEN = 1;
		// 保密
		public static final int SECRECY = 2;
	}

	public static class ImageSize {

		public static final int HEADWIDTH = 550;
		public static final int HEADHEIGTH = 550;
		
		public static final int DYNAMIC_HEIGHT=80;
		public static final int DYNAMIC_WIDTH=80;
		
	}
	public static class NameShow{
		public static final int MOVIENAME_MAX=8;
	}

	public static interface MessageType {

		// 用户之间的普通消息
		public static final String TYPE_0 = "0";

		// 下线类型
		String TYPE_999 = "999";
	}

	public static interface MessageStatus {

		// 消息未读
		public static final String STATUS_0 = "0";
		// 消息已经读取
		public static final String STATUS_1 = "1";
	}

	public static class ReturnCode {

		public static String RETURN_TAG = "tag";
		public static String RETURN_STATE = "state";
		public static String RETURN_MESSAGE = "desc";
		public static String RETURN_DATA = "date";
		public static String RETURN_VALUE = "value";
		public static String CODE_404 = "404";

		public static String CODE_403 = "403";

		public static String CODE_405 = "405";

		public static String CODE_200 = "200";

		public static String CODE_206 = "206";

		public static String CODE_500 = "500";

		public static int STATE_SUCCESS = 0;

		public static String STATE_1 = "1";

		public static String STATE_2 = "2";

		public static String STATE_3 = "3";

		public static String STATE_12 = "12";

		public static String STATE_101 = "101";
		
		public static String STATE_999 = "999";

	}

	public static final Charset ENCODE_UTF8 = Charset.forName("UTF-8");

	public static byte MESSAGE_SEPARATE = '\b';

	public static byte FLEX_DATA_SEPARATE = '\0';

	public static int CIM_DEFAULT_MESSAGE_ORDER = 1;

	public static final String SESSION_SID = "sid";

	public static final String SESSION_KEY = "account";

	public static final String HEARTBEAT_KEY = "heartbeat";

	/**
	 * FLEX 客户端socket请求发的安全策略请求，需要特殊处理，返回安全验证报文
	 */
	public static final String FLEX_POLICY_REQUEST = "<policy-file-request/>";

	public static final String FLEX_POLICY_RESPONSE = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";

	/**
	 * 对应ichat 中 spring-cim.xml > bean:mainIoHandler >handlers 为
	 * 服务端处理对应的handlers，应该继承与com.cms.nio.handler.AbstractHandler
	 * 
	 * @author xiajun
	 * 
	 */
	public static class RequestKey {

		public static String CLIENT_BIND = "client_bind";

		public static String CLIENT_HEARTBEAT = "client_heartbeat";

		public static String CLIENT_LOGOUT = "client_logout";

		public static String CLIENT_LOGIN = "client_login";

		public static String CLIENT_PASSWORD = "client_password";

		public static String CLIENT_OFFLINE_MESSAGE = "client_get_offline_message";

	}

	public static class ResponseKey {

		public static String SERVER_ALLBUSINESSTYPE = "server_allbusinesetype";
		public static String SERVER_RESULT = "server_RESULT";

	}

	public static class SessionStatus {

		public static int STATUS_OK = 0;

		public static int STATUS_CLOSED = 1;

	}

}
