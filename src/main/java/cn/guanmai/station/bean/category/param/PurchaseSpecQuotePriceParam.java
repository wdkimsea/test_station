package cn.guanmai.station.bean.category.param;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月3日 上午11:58:07
 * @description:
 * @version: 1.0
 */

public class PurchaseSpecQuotePriceParam {
	private String spec_id;
	private String customer_id;
	private BigDecimal std_unit_price;
	private String origin_place;
	private String remark;

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public BigDecimal getStd_unit_price() {
		return std_unit_price;
	}

	public void setStd_unit_price(BigDecimal std_unit_price) {
		this.std_unit_price = std_unit_price.multiply(new BigDecimal("100"));
	}

	public String getOrigin_place() {
		return origin_place;
	}

	public void setOrigin_place(String origin_place) {
		this.origin_place = origin_place;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
