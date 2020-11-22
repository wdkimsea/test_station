package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年5月14日 下午4:40:32
 * @description:
 * @version: 1.0
 */

public class AddressLabelBean {
	private BigDecimal id;
	private String name;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
