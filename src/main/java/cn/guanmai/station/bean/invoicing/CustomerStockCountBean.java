package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/22.
 */
public class CustomerStockCountBean {
	private int address_count;
	private BigDecimal total_stock_value;

	public int getAddress_count() {
		return address_count;
	}

	public void setAddress_count(int address_count) {
		this.address_count = address_count;
	}

	public BigDecimal getTotal_stock_value() {
		return total_stock_value;
	}

	public void setTotal_stock_value(BigDecimal total_stock_value) {
		this.total_stock_value = total_stock_value;
	}

}
