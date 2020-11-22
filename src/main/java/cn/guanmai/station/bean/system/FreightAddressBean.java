package cn.guanmai.station.bean.system;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午7:45:29
 * @description:
 * @version: 1.0
 */

public class FreightAddressBean {
	private BigDecimal address_id;
	private String freight_id;

	public BigDecimal getAddress_id() {
		return address_id;
	}

	public void setAddress_id(BigDecimal address_id) {
		this.address_id = address_id;
	}

	public String getFreight_id() {
		return freight_id;
	}

	public void setFreight_id(String freight_id) {
		this.freight_id = freight_id;
	}

}
