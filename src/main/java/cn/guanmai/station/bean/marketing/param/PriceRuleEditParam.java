package cn.guanmai.station.bean.marketing.param;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Feb 20, 2019 3:58:11 PM 
* @des 锁价规则修改参数封装类
* @version 1.0 
*/
public class PriceRuleEditParam {
	private String price_rule_id;
	private String begin;
	private String end;
	private String name;
	private int status;
	private JSONArray address_ids;
	private List<Sku> skus;
	private List<Category2> category_2_list;
	private int rule_object_type;

	public class Sku {
		private String sku_id;
		private String yx_price;
		private int rule_type;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
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

		/**
		 * @return the rule_type
		 */
		public int getRule_type() {
			return rule_type;
		}

		/**
		 * @param rule_type the rule_type to set
		 */
		public void setRule_type(int rule_type) {
			this.rule_type = rule_type;
		}
	}

	public class Category2 {
		private String category_2_id;
		private int rule_type;
		private BigDecimal yx_price;

		public String getCategory_2_id() {
			return category_2_id;
		}

		public void setCategory_2_id(String category_2_id) {
			this.category_2_id = category_2_id;
		}

		public int getRule_type() {
			return rule_type;
		}

		public void setRule_type(int rule_type) {
			this.rule_type = rule_type;
		}

		public BigDecimal getYx_price() {
			return yx_price;
		}

		public void setYx_price(BigDecimal yx_price) {
			this.yx_price = yx_price;
		}

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
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the address_ids
	 */
	public JSONArray getAddress_ids() {
		return address_ids;
	}

	/**
	 * @param address_ids the address_ids to set
	 */
	public void setAddress_ids(JSONArray address_ids) {
		this.address_ids = address_ids;
	}

	/**
	 * @return the skus
	 */
	public List<Sku> getSkus() {
		return skus;
	}

	/**
	 * @param skus the skus to set
	 */
	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<Category2> getCategory_2_list() {
		return category_2_list;
	}

	public void setCategory_2_list(List<Category2> category_2_list) {
		this.category_2_list = category_2_list;
	}

	/**
	 * @return the rule_object_type
	 */
	public int getRule_object_type() {
		return rule_object_type;
	}

	/**
	 * @param rule_object_type the rule_object_type to set
	 */
	public void setRule_object_type(int rule_object_type) {
		this.rule_object_type = rule_object_type;
	}

}
