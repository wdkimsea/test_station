package cn.guanmai.bshop.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/19.
 */
public class BshopSpuStockBean {
	private BigDecimal avg_price;
	private String img_url;
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

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
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
