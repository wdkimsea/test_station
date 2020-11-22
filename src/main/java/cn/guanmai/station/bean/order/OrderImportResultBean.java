package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月7日 下午9:02:14
 * @description: 订单导入商品结果
 * @version: 1.0
 */

public class OrderImportResultBean {
	private String id;
	private BigDecimal std_sale_price_forsale;
	private BigDecimal sale_price;
	private BigDecimal quantity;
	private String spu_remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getStd_sale_price_forsale() {
		return std_sale_price_forsale;
	}

	public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
		this.std_sale_price_forsale = std_sale_price_forsale;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getSpu_remark() {
		return spu_remark;
	}

	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
	}

}
