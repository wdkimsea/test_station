package cn.guanmai.bshop.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 22, 2019 3:05:13 PM 
* @des 订单详细信息类
* @version 1.0 
*/
public class OrderDetailBean {
	private BigDecimal total_pay;
	private BigDecimal total_price;
	@JSONField(name="id")
	private String order_id;
	private String receive_way;

	private List<Detail> details;

	public class Detail {
		@JSONField(name="id")
		private String sku_id;
		private BigDecimal real_quantity;
		private BigDecimal sale_ratio;
		private BigDecimal std_sale_price;
		private BigDecimal real_item_price;
		private String sale_unit_name;
		private String std_unit_name;
		private String remark;

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
		 * @return the real_quantity
		 */
		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		/**
		 * @param real_quantity
		 *            the real_quantity to set
		 */
		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio
		 *            the sale_ratio to set
		 */
		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		/**
		 * @return the std_sale_price
		 */
		public BigDecimal getStd_sale_price() {
			return std_sale_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param std_sale_price
		 *            the std_sale_price to set
		 */
		public void setStd_sale_price(BigDecimal std_sale_price) {
			this.std_sale_price = std_sale_price;
		}

		/**
		 * @return the real_item_price
		 */
		public BigDecimal getReal_item_price() {
			return real_item_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param real_item_price
		 *            the real_item_price to set
		 */
		public void setReal_item_price(BigDecimal real_item_price) {
			this.real_item_price = real_item_price;
		}

		/**
		 * @return the sale_unit_name
		 */
		public String getSale_unit_name() {
			return sale_unit_name;
		}

		/**
		 * @param sale_unit_name
		 *            the sale_unit_name to set
		 */
		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		/**
		 * @return the std_unit_name
		 */
		public String getStd_unit_name() {
			return std_unit_name;
		}

		/**
		 * @param std_unit_name
		 *            the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}

		/**
		 * @param remark
		 *            the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay.divide(new BigDecimal("100"));
	}

	/**
	 * @param total_pay
	 *            the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param total_price
	 *            the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

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

	public String getReceive_way() {
		return receive_way;
	}

	public void setReceive_way(String receive_way) {
		this.receive_way = receive_way;
	}

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
