package com.movie.client.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.movie.app.SexState;

/**
 * 描述：类型信息</br>
 */
public class Dictionary extends BaseBean implements Serializable {


	
	public final static Map<Integer,String> womenDictionaries = new HashMap<Integer,String>();
	public final static Map<Integer,String> menDictionaries = new HashMap<Integer,String>();

	public final static Map<Integer, Map<Integer,String>> commentsMap = new HashMap<Integer, Map<Integer,String>>();

	public final static List<Dictionary> hobbies = new ArrayList(Arrays.asList(
			new Dictionary(1, "美食"), new Dictionary(2, "旅行"), new Dictionary(3,
					"趣味"), new Dictionary(4, "摄影"), new Dictionary(5, "电影"),
			new Dictionary(6, "人文"), new Dictionary(7, "艺术"), new Dictionary(8,
					"娱乐"), new Dictionary(9, "读书"), new Dictionary(10, "居家"),
			new Dictionary(11, "历史")));

	int id;
	String name;
	static {
		womenDictionaries.put(1, "腹黑");
		womenDictionaries.put(2, "小萝莉");
		womenDictionaries.put(3, "我的女神");
		womenDictionaries.put(4, "猫妹");
		womenDictionaries.put(5, "声音好听");
		womenDictionaries.put(6, "女汉子");
		womenDictionaries.put(7, "可爱");
		womenDictionaries.put(8, "泽塔琼斯");
		menDictionaries.put(1, "腹黑");
		menDictionaries.put(2, "小正太");
		menDictionaries.put(3, "我的男神");
		menDictionaries.put(4, "猫王");
		menDictionaries.put(5, "声音性感");
		menDictionaries.put(6, "猛男");
		menDictionaries.put(7, "萌");
		menDictionaries.put(8, "磁性声音");
		commentsMap.put(SexState.MAN.getState(), menDictionaries);
		commentsMap.put(SexState.WOMAN.getState(),womenDictionaries);
	}

	public Dictionary() {
		super();

	}

	public Dictionary(int id, String name) {
		super();
		this.id = id;
		this.name = name;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Map<Integer, Integer>> getTempComments(int sex) {

		Map<Integer, Integer> maps = null;
		Random random = new Random();
		Map<Integer,String> dictionaries= commentsMap.get(sex);
		List<Map<Integer, Integer>> comments = new ArrayList<Map<Integer, Integer>>();
		int size=dictionaries.size();
		for (int i = 0; i < size; i++) {
			maps = new HashMap<Integer, Integer>();
			maps.put(i+1, random.nextInt(100));
			comments.add(maps);
		}
		return comments;

	}

}
