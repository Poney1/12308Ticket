package com.wxws.myticket.bus.entity;

/**
 * 地位信息
 */
public class BaocheLocalInfo {
	//省份名称
	private String province;
	// 城市名称
	private String city;
	// 纬度
	private double latitude;
	// 经度
	private double longitude;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
