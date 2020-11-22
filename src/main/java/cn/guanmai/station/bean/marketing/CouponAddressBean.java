package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年6月2日 上午11:55:21
 * @description:
 * @version: 1.0
 */

public class CouponAddressBean {
	private BigDecimal id;
	private BigDecimal address_id;
	private String username;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getAddress_id() {
		return address_id;
	}

	public void setAddress_id(BigDecimal address_id) {
		this.address_id = address_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
