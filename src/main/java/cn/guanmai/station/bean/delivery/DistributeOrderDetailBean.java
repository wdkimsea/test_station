package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 12, 2019 7:24:09 PM 
* @des 配送单详细信息类
* @version 1.0 
*/
public class DistributeOrderDetailBean {
	private String id;
	private BigDecimal real_price; // 出库金额
	private BigDecimal total_price;
	private BigDecimal total_pay;
	private List<Detail> details;
	private List<Refund> refunds;
	private List<Abnormal> abnormals;

	public class Detail {
		private String category_title_1;
		private String category_title_2;
		private String pinlei_title;
		private String spu_name;

		@JSONField(name = "id")
		private String sku_id;
		@JSONField(name = "name")
		private String sku_name;
		private String std_unit_name;
		private String sale_unit_name;
		private BigDecimal std_sale_price;
		private BigDecimal std_sale_price_forsale;
		private BigDecimal sale_price;
		private BigDecimal sale_ratio;
		private BigDecimal quantity;
		private BigDecimal real_weight;
		private BigDecimal real_item_price;

		/**
		 * @return the category_title_1
		 */
		public String getCategory_title_1() {
			return category_title_1;
		}

		/**
		 * @param category_title_1 the category_title_1 to set
		 */
		public void setCategory_title_1(String category_title_1) {
			this.category_title_1 = category_title_1;
		}

		/**
		 * @return the category_title_2
		 */
		public String getCategory_title_2() {
			return category_title_2;
		}

		/**
		 * @param category_title_2 the category_title_2 to set
		 */
		public void setCategory_title_2(String category_title_2) {
			this.category_title_2 = category_title_2;
		}

		/**
		 * @return the pinlei_title
		 */
		public String getPinlei_title() {
			return pinlei_title;
		}

		/**
		 * @param pinlei_title the pinlei_title to set
		 */
		public void setPinlei_title(String pinlei_title) {
			this.pinlei_title = pinlei_title;
		}

		/**
		 * @return the spu_name
		 */
		public String getSpu_name() {
			return spu_name;
		}

		/**
		 * @param spu_name the spu_name to set
		 */
		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

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
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		/**
		 * @return the std_unit_name
		 */
		public String getStd_unit_name() {
			return std_unit_name;
		}

		/**
		 * @param std_unit_name the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		/**
		 * @return the sale_unit_name
		 */
		public String getSale_unit_name() {
			return sale_unit_name;
		}

		/**
		 * @param sale_unit_name the sale_unit_name to set
		 */
		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		/**
		 * @return the std_sale_price
		 */
		public BigDecimal getStd_sale_price() {
			return std_sale_price;
		}

		/**
		 * @param std_sale_price the std_sale_price to set
		 */
		public void setStd_sale_price(BigDecimal std_sale_price) {
			this.std_sale_price = std_sale_price;
		}

		public BigDecimal getStd_sale_price_forsale() {
			return std_sale_price_forsale;
		}

		public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
			this.std_sale_price_forsale = std_sale_price_forsale;
		}

		/**
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price;
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio the sale_ratio to set
		 */
		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		/**
		 * @return the quantity
		 */
		public BigDecimal getQuantity() {
			return quantity;
		}

		/**
		 * @param quantity the quantity to set
		 */
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		/**
		 * @return the real_weight
		 */
		public BigDecimal getReal_weight() {
			return real_weight;
		}

		/**
		 * @param real_weight the real_weight to set
		 */
		public void setReal_weight(BigDecimal real_weight) {
			this.real_weight = real_weight;
		}

		/**
		 * @return the real_item_price
		 */
		public BigDecimal getReal_item_price() {
			return real_item_price;
		}

		/**
		 * @param real_item_price the real_item_price to set
		 */
		public void setReal_item_price(BigDecimal real_item_price) {
			this.real_item_price = real_item_price;
		}

	}

	public class Refund {
		private BigDecimal amount_delta;
		private String detail_id;
		private BigDecimal money_delta;
		private String text;
		private String type_text;

		/**
		 * @return the amount_delta
		 */
		public BigDecimal getAmount_delta() {
			return amount_delta;
		}

		/**
		 * @param amount_delta the amount_delta to set
		 */
		public void setAmount_delta(BigDecimal amount_delta) {
			this.amount_delta = amount_delta;
		}

		/**
		 * @return the detail_id
		 */
		public String getDetail_id() {
			return detail_id;
		}

		/**
		 * @param detail_id the detail_id to set
		 */
		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
		}

		/**
		 * @return the money_delta
		 */
		public BigDecimal getMoney_delta() {
			return money_delta;
		}

		/**
		 * @param money_delta the money_delta to set
		 */
		public void setMoney_delta(BigDecimal money_delta) {
			this.money_delta = money_delta;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * @return the type_text
		 */
		public String getType_text() {
			return type_text;
		}

		/**
		 * @param type_text the type_text to set
		 */
		public void setType_text(String type_text) {
			this.type_text = type_text;
		}

	}

	public class Abnormal {
		private BigDecimal amount_delta;
		private String detail_id;
		private BigDecimal money_delta;
		private String text;
		private String type_text;

		/**
		 * @return the amount_delta
		 */
		public BigDecimal getAmount_delta() {
			return amount_delta;
		}

		/**
		 * @param amount_delta the amount_delta to set
		 */
		public void setAmount_delta(BigDecimal amount_delta) {
			this.amount_delta = amount_delta;
		}

		/**
		 * @return the detail_id
		 */
		public String getDetail_id() {
			return detail_id;
		}

		/**
		 * @param detail_id the detail_id to set
		 */
		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
		}

		/**
		 * @return the money_delta
		 */
		public BigDecimal getMoney_delta() {
			return money_delta;
		}

		/**
		 * @param money_delta the money_delta to set
		 */
		public void setMoney_delta(BigDecimal money_delta) {
			this.money_delta = money_delta;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * @return the type_text
		 */
		public String getType_text() {
			return type_text;
		}

		/**
		 * @param type_text the type_text to set
		 */
		public void setType_text(String type_text) {
			this.type_text = type_text;
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
	 * @return the real_price
	 */
	public BigDecimal getReal_price() {
		return real_price;
	}

	/**
	 * @param real_price the real_price to set
	 */
	public void setReal_price(BigDecimal real_price) {
		this.real_price = real_price;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay;
	}

	/**
	 * @param total_pay the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	/**
	 * @return the refunds
	 */
	public List<Refund> getRefunds() {
		return refunds;
	}

	/**
	 * @param refunds the refunds to set
	 */
	public void setRefunds(List<Refund> refunds) {
		this.refunds = refunds;
	}

	/**
	 * @return the abnormals
	 */
	public List<Abnormal> getAbnormals() {
		return abnormals;
	}

	/**
	 * @param abnormals the abnormals to set
	 */
	public void setAbnormals(List<Abnormal> abnormals) {
		this.abnormals = abnormals;
	}

}
