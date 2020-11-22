package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Feb 19, 2019 5:34:10 PM 
* @des 锁价规则属性封装类
* @version 1.0 
*/
public class PriceRuleBean {
	@JSONField(name="_id")
	private String id;

	private String name;

	private String begin;

	private String end;

	private String salemenu_id;

	private String salemenu_name;

	private String type;

	private JSONArray addresses;

	private List<Category2> category_2_list;

	public class Category2 {
		private String category_1_id;
		private String category_1_name;
		private String category_2_id;
		private String category_2_name;
		private boolean check_status;
		private int rule_type;
		private List<String> search_text;
		private BigDecimal yx_price;

		public String getCategory_1_id() {
			return category_1_id;
		}

		public void setCategory_1_id(String category_1_id) {
			this.category_1_id = category_1_id;
		}

		public String getCategory_1_name() {
			return category_1_name;
		}

		public void setCategory_1_name(String category_1_name) {
			this.category_1_name = category_1_name;
		}

		public String getCategory_2_id() {
			return category_2_id;
		}

		public void setCategory_2_id(String category_2_id) {
			this.category_2_id = category_2_id;
		}

		public String getCategory_2_name() {
			return category_2_name;
		}

		public void setCategory_2_name(String category_2_name) {
			this.category_2_name = category_2_name;
		}

		public boolean isCheck_status() {
			return check_status;
		}

		public void setCheck_status(boolean check_status) {
			this.check_status = check_status;
		}

		public int getRule_type() {
			return rule_type;
		}

		public void setRule_type(int rule_type) {
			this.rule_type = rule_type;
		}

		public List<String> getSearch_text() {
			return search_text;
		}

		public void setSearch_text(List<String> search_text) {
			this.search_text = search_text;
		}

		public BigDecimal getYx_price() {
			return yx_price;
		}

		public void setYx_price(BigDecimal yx_price) {
			this.yx_price = yx_price;
		}

	}

	private List<Sku> skus;

	public class Sku {
		private String id;
		private String name;
		private String sale_price;
		private int rule_type;
		private BigDecimal yx_price;

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
		 * @return the sale_price
		 */
		public String getSale_price() {
			return sale_price;
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(String sale_price) {
			this.sale_price = sale_price;
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

		/**
		 * @return the yx_price
		 */
		public BigDecimal getYx_price() {
			return yx_price;
		}

		/**
		 * @param yx_price the yx_price to set
		 */
		public void setYx_price(BigDecimal yx_price) {
			this.yx_price = yx_price;
		}
	}

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
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * @param salemenu_name the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
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
	 * @return the addresses
	 */
	public JSONArray getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(JSONArray addresses) {
		this.addresses = addresses;
	}

	public List<Category2> getCategory_2_list() {
		return category_2_list;
	}

	public void setCategory_2_list(List<Category2> category_2_list) {
		this.category_2_list = category_2_list;
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
}
