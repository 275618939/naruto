package com.movie.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.movie.client.bean.Feed;
import com.movie.client.bean.FeedComment;

/**
 * @fileName JsonResolveUtils.java
 * @package com.immomo.momo.android.util
 * @description Json解析工具类
 * @author 任东卫
 * @email 86930007@qq.com
 * @version 1.0
 */
public class JsonResolveUtils {
	// 附近个人的json文件名称
	private static final String NEARBY_PEOPLE = "nearby_people.json";
	// 附近个人的json文件名称
	private static final String NEARBY_GROUP = "nearby_group.json";
	// 用户资料文件夹
	private static final String PROFILE = "profile/";
	// 用户状态文件夹
	private static final String STATUS = "status/";
	// 后缀名
	private static final String SUFFIX = ".json";
	// 状态评论
	private static final String FEEDCOMMENT = "feedcomment.json";

	/**
	 * 解析附近个人状态(临时数据)
	 * 
	 * @param context
	 * @param feeds
	 * @param uid
	 * @return
	 */
	public static boolean resolveNearbyStatus(Context context,List<Feed> feeds, String uid) {
		if (!android.text.TextUtils.isEmpty(uid)) {
			String json = TextUtils.getJson(context, STATUS + uid + SUFFIX);
			if (json != null) {
				try {
					JSONArray array = new JSONArray(json);
					Feed feed = null;
					List<String> contentImage=null;
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String time = object.getString(Feed.TIME);
						String content = object.getString(Feed.CONTENT);
						contentImage = new ArrayList<String>();
						if (object.has(Feed.CONTENT_IMAGE)) {
							JSONArray aJsonArray = object.getJSONArray(Feed.CONTENT_IMAGE);
							for (int j = 0; j < aJsonArray.length(); j++) {
								contentImage.add(aJsonArray.getString(j));
							}
						}
						String site = object.getString(Feed.SITE);
						int commentCount = object.getInt(Feed.COMMENT_COUNT);
						feed = new Feed(time, content,contentImage,site,commentCount);
						if (object.has(Feed.PORTRAIT)) {
							feed.setPortrait(object.getString(Feed.PORTRAIT));
						}
						if (object.has(Feed.NAME)) {
							feed.setName(object.getString(Feed.NAME));
						}
						feeds.add(feed);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					feeds = null;
					return false;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析状态评论
	 * 
	 * @param context
	 * @param comments
	 * @return
	 */
	public static boolean resoleFeedComment(Context context,
			List<FeedComment> comments) {
		String json = TextUtils.getJson(context, FEEDCOMMENT);
		if (json != null) {
			try {
				JSONArray array = new JSONArray(json);
				FeedComment comment = null;
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String name = object.getString(FeedComment.NAME);
					String avatar = object.getString(FeedComment.AVATAR);
					String content = object.getString(FeedComment.CONTENT);
					String time = object.getString(FeedComment.TIME);
					comment = new FeedComment(name, avatar, content, time);
					comments.add(comment);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				comments = null;
				return false;
			}
			return true;
		}
		return false;
	}

}
