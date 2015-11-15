package com.movie.client.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：类型信息</br>
 */
public class Dictionary extends BaseBean implements Serializable {
	
	public final static List<Dictionary> dictionaries = new ArrayList(
			Arrays.asList(new Dictionary(1, "腹黑"), new Dictionary(2, "小萝莉"),
					new Dictionary(3, "我的女神"), new Dictionary(4, "猫妹"), new Dictionary(
							5, "声音好听"), new Dictionary(6, "女汉子"), new Dictionary(7,
							"可爱"), new Dictionary(8,
									"泽塔琼斯")));
	
	public final static List<Dictionary> hobbies  =new ArrayList(Arrays.asList(new Dictionary(1,"美食"),
			 new Dictionary(2,"旅行"),
			 new Dictionary(3,"趣味"),
			 new Dictionary(4,"摄影"),
			 new Dictionary(5,"电影"),
			 new Dictionary(6,"人文"),
			 new Dictionary(7,"艺术"),
			 new Dictionary(8,"娱乐"),
			 new Dictionary(9,"读书"),
			 new Dictionary(10,"居家"),
			 new Dictionary(11,"历史")));

	int id;
	String name;

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

}
