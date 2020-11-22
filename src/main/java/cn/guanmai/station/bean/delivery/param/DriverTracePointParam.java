package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年12月30日
 * @time 下午6:15:00
 * @des TODO
 */

public class DriverTracePointParam {
	private BigDecimal driver_id;
	private String token;
	private double latitude;
	private double longitude;
	private String locatetime;
	private double direction;
	private double speed;
	private int locate_type;

	public BigDecimal getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(BigDecimal driver_id) {
		this.driver_id = driver_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getLocatetime() {
		return locatetime;
	}

	public void setLocatetime(String locatetime) {
		this.locatetime = locatetime;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getLocate_type() {
		return locate_type;
	}

	public void setLocate_type(int locate_type) {
		this.locate_type = locate_type;
	}

}
