package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月24日 下午12:02:18
 * @des 接口 /stock/avg_price/update 对应的参数,加权平均修复库存均价
 * @version 1.0
 */
public class StockAvgPriceUpdateParam {
	private String spu_id;
	private BigDecimal price;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
