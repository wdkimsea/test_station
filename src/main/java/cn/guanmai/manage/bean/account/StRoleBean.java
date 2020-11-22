package cn.guanmai.manage.bean.account;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年8月22日
 * @time 下午7:44:26
 * @des TODO
 */

public class StRoleBean {
	private String station_name;
	private String name;
	private BigDecimal id;
	private String description;
	private String create_date;

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

}
