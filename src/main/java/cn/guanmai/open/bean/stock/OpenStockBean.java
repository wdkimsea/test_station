package cn.guanmai.open.bean.stock;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午10:54:55
 * @des TODO
 */

public class OpenStockBean {
	private String spu_id;
	private String spu_name;
	private String category1_id;
	private String category1_name;
	private String category2_id;
	private String category2_name;
	private BigDecimal stock;
	private BigDecimal avg_price;
	private BigDecimal frozen_stock;
	private String std_unit_name;
	private BigDecimal available_stock;

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

	public String getCategory1_id() {
		return category1_id;
	}

	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	public String getCategory1_name() {
		return category1_name;
	}

	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	public String getCategory2_id() {
		return category2_id;
	}

	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	public String getCategory2_name() {
		return category2_name;
	}

	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public BigDecimal getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(BigDecimal avg_price) {
		this.avg_price = avg_price;
	}

	public BigDecimal getFrozen_stock() {
		return frozen_stock;
	}

	public void setFrozen_stock(BigDecimal frozen_stock) {
		this.frozen_stock = frozen_stock;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public BigDecimal getAvailable_stock() {
		return available_stock;
	}

	public void setAvailable_stock(BigDecimal available_stock) {
		this.available_stock = available_stock;
	}
}
