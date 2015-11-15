package com.movie.client.bean;

import java.io.Serializable;

public class Cinema extends BaseBean implements Serializable {


	private static final long serialVersionUID = -3211492162506799869L;
	private String name;
	private String city;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	

}
