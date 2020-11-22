package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 24, 2019 5:48:13 PM 
* @des 采购助手APP过滤后的采购任务结果
* @version 1.0 
*/
public class PurchaseAssistantTaskBean {
	private BigDecimal last_in_stock_price;
	private BigDecimal last_purchase_price;
	private BigDecimal last_quote_price;
	private BigDecimal plan_amount;
	private BigDecimal purchase_amount;
	private BigDecimal purchase_price;
	private String purchase_unit_name;
	private BigDecimal ratio;
	private String release_id;
	private String remark;
	private String spec_id;
	private String spec_name;
	private int status;
	private String std_unit_name;
	private BigDecimal stock;
	private BigDecimal stock_avg_price;
	private BigDecimal suggest_purchase_num;
	private String supplier_id;
	private String supplier_name;

	@JSONField(name = "detail")
	private List<Detail> details;

	public class Detail {
		private String order_id;
		private BigDecimal plan_purchase_amount;
		private String remark;
		private String res_name;

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
		 * @return the plan_purchase_amount
		 */
		public BigDecimal getPlan_purchase_amount() {
			return plan_purchase_amount;
		}

		/**
		 * @param plan_purchase_amount
		 *            the plan_purchase_amount to set
		 */
		public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
			this.plan_purchase_amount = plan_purchase_amount;
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

		/**
		 * @return the res_name
		 */
		public String getRes_name() {
			return res_name;
		}

		/**
		 * @param res_name
		 *            the res_name to set
		 */
		public void setRes_name(String res_name) {
			this.res_name = res_name;
		}

	}

	/**
	 * @return the last_in_stock_price
	 */
	public BigDecimal getLast_in_stock_price() {
		return last_in_stock_price;
	}

	/**
	 * @param last_in_stock_price
	 *            the last_in_stock_price to set
	 */
	public void setLast_in_stock_price(BigDecimal last_in_stock_price) {
		this.last_in_stock_price = last_in_stock_price;
	}

	/**
	 * @return the last_purchase_price
	 */
	public BigDecimal getLast_purchase_price() {
		return last_purchase_price;
	}

	/**
	 * @param last_purchase_price
	 *            the last_purchase_price to set
	 */
	public void setLast_purchase_price(BigDecimal last_purchase_price) {
		this.last_purchase_price = last_purchase_price;
	}

	/**
	 * @return the last_quote_price
	 */
	public BigDecimal getLast_quote_price() {
		return last_quote_price;
	}

	/**
	 * @param last_quote_price
	 *            the last_quote_price to set
	 */
	public void setLast_quote_price(BigDecimal last_quote_price) {
		this.last_quote_price = last_quote_price;
	}

	/**
	 * @return the plan_amount
	 */
	public BigDecimal getPlan_amount() {
		return plan_amount;
	}

	/**
	 * @param plan_amount
	 *            the plan_amount to set
	 */
	public void setPlan_amount(BigDecimal plan_amount) {
		this.plan_amount = plan_amount;
	}

	/**
	 * @return the purchase_amount
	 */
	public BigDecimal getPurchase_amount() {
		return purchase_amount;
	}

	/**
	 * @param purchase_amount
	 *            the purchase_amount to set
	 */
	public void setPurchase_amount(BigDecimal purchase_amount) {
		this.purchase_amount = purchase_amount;
	}

	/**
	 * @return the purchase_price
	 */
	public BigDecimal getPurchase_price() {
		return purchase_price;
	}

	/**
	 * @param purchase_price
	 *            the purchase_price to set
	 */
	public void setPurchase_price(BigDecimal purchase_price) {
		this.purchase_price = purchase_price;
	}

	/**
	 * @return the purchase_unit_name
	 */
	public String getPurchase_unit_name() {
		return purchase_unit_name;
	}

	/**
	 * @param purchase_unit_name
	 *            the purchase_unit_name to set
	 */
	public void setPurchase_unit_name(String purchase_unit_name) {
		this.purchase_unit_name = purchase_unit_name;
	}

	/**
	 * @return the ratio
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param ratio
	 *            the ratio to set
	 */
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the release_id
	 */
	public String getRelease_id() {
		return release_id;
	}

	/**
	 * @param release_id
	 *            the release_id to set
	 */
	public void setRelease_id(String release_id) {
		this.release_id = release_id;
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

	/**
	 * @return the spec_id
	 */
	public String getSpec_id() {
		return spec_id;
	}

	/**
	 * @param spec_id
	 *            the spec_id to set
	 */
	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	/**
	 * @return the spec_name
	 */
	public String getSpec_name() {
		return spec_name;
	}

	/**
	 * @param spec_name
	 *            the spec_name to set
	 */
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * @return the stock_avg_price
	 */
	public BigDecimal getStock_avg_price() {
		return stock_avg_price;
	}

	/**
	 * @param stock_avg_price
	 *            the stock_avg_price to set
	 */
	public void setStock_avg_price(BigDecimal stock_avg_price) {
		this.stock_avg_price = stock_avg_price;
	}

	/**
	 * @return the suggest_purchase_num
	 */
	public BigDecimal getSuggest_purchase_num() {
		return suggest_purchase_num;
	}

	/**
	 * @param suggest_purchase_num
	 *            the suggest_purchase_num to set
	 */
	public void setSuggest_purchase_num(BigDecimal suggest_purchase_num) {
		this.suggest_purchase_num = suggest_purchase_num;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id
	 *            the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name
	 *            the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
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
