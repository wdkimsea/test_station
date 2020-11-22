package cn.guanmai.station.bean.order;

/**
 * @author: liming
 * @Date: 2020年8月13日 下午4:30:46
 * @description:
 * @version: 1.0
 */

public class OrderProcessBean {
	private String id;
	private String name;

	private int group_id;
	private String station_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

}
