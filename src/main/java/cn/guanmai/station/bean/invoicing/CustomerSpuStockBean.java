package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年9月27日
 * @time 下午5:13:04
 * @des 接口 /stock/address/spu_stock/list 对应的结果
 */

public class CustomerSpuStockBean {
	private BigDecimal avg_price;
	private String spu_id;
	private String spu_name;
	private String std_unit_name;
	private BigDecimal stock;
	private BigDecimal stock_value;

	public BigDecimal getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(BigDecimal avg_price) {
		this.avg_price = avg_price;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public BigDecimal getStock_value() {
		return stock_value;
	}

	public void setStock_value(BigDecimal stock_value) {
		this.stock_value = stock_value;
	}

}
