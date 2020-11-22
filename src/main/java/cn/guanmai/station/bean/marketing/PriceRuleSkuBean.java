package cn.guanmai.station.bean.marketing;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Feb 20, 2019 4:45:08 PM 
* @des 锁价规则,按商户商品查看的结果封装类
* @version 1.0 
*/
public class PriceRuleSkuBean {
	private String addresses;
	@JSONField(name = "_id")
	private String price_rule_id;

	private Sku sku;

	public class Sku {
		private String id;
		private String yx_price;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the yx_price
		 */
		public String getYx_price() {
			return yx_price;
		}

		/**
		 * @param yx_price the yx_price to set
		 */
		public void setYx_price(String yx_price) {
			this.yx_price = yx_price;
		}
	}

	/**
	 * @return the addresses
	 */
	public String getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the price_rule_id
	 */
	public String getPrice_rule_id() {
		return price_rule_id;
	}

	/**
	 * @param price_rule_id the price_rule_id to set
	 */
	public void setPrice_rule_id(String price_rule_id) {
		this.price_rule_id = price_rule_id;
	}

	/**
	 * @return the sku
	 */
	public Sku getSku() {
		return sku;
	}

	/**
	 * @param sku the sku to set
	 */
	public void setSku(Sku sku) {
		this.sku = sku;
	}

}
