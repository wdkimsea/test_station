package cn.guanmai.station.bean.marketing.param;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Feb 20, 2019 11:55:44 AM 
* @des 创建锁价规则的参数封装类
* @version 1.0 
*/
public class PriceRuleCreateParam {
	private String begin;
	private String end;
	private String name;
	private String salemenu_id;
	private JSONArray address_ids;
	private List<Sku> skus;
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

	private String type;

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
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
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

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
