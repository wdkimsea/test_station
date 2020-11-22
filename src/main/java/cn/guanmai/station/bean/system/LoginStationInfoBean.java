package cn.guanmai.station.bean.system;

/**
 * @author: liming
 * @Date: 2020年5月20日 上午11:53:00
 * @description:
 * @version: 1.0
 */

public class LoginStationInfoBean {
	private boolean clean_food;
	private boolean is_superman;
	private String station_id;

	public boolean isClean_food() {
		return clean_food;
	}

	public void setClean_food(boolean clean_food) {
		this.clean_food = clean_food;
	}

	public boolean isIs_superman() {
		return is_superman;
	}

	public void setIs_superman(boolean is_superman) {
		this.is_superman = is_superman;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

}
