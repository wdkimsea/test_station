package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年4月23日 下午6:55:50
 * @description:
 * @version: 1.0
 */

public class OutStockPriceUpdateParam {
	private String sheet_id;
	private String sku_id;
	private BigDecimal price;

	public String getSheet_id() {
		return sheet_id;
	}

	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
