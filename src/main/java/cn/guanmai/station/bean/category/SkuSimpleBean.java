package cn.guanmai.station.bean.category;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年2月18日 上午11:51:23
 * @description:
 * @version: 1.0
 */

public class SkuSimpleBean {
	private String id;
	private String name;
	private String std_unit_name;
	private String sale_unit_name;
	private boolean is_price_timing;
	private BigDecimal sale_price;

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

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public boolean isIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(boolean is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

}
