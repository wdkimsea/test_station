package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Apr 12, 2019 10:42:36 AM 
* @des 新版称重软件 { /weight/sku/out_of_stock , /weight/sku/un_out_of_stock } 接口对应的参数类
* @version 1.0 
*/
public class OutOfStockParam {
	private String order_id;
	private String sku_id;
	private BigDecimal weight;
	private int sort_way;

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id
	 *            the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	/**
	 * @return the weight
	 */
	public BigDecimal getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	/**
	 * @return the sort_way
	 */
	public int getSort_way() {
		return sort_way;
	}

	/**
	 * @param sort_way
	 *            the sort_way to set
	 */
	public void setSort_way(int sort_way) {
		this.sort_way = sort_way;
	}

	public OutOfStockParam(String order_id, String sku_id, BigDecimal weight, int sort_way) {
		super();
		this.order_id = order_id;
		this.sku_id = sku_id;
		this.weight = weight;
		this.sort_way = sort_way;
	}

}
