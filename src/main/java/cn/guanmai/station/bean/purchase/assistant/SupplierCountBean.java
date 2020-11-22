package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 24, 2019 10:26:34 AM 
* @des 采购金额分布统计(供应商维度)
* @version 1.0 
*/
public class SupplierCountBean {
	private BigDecimal all_purchase_price;
	private BigDecimal all_purchase_sheet_num;

	@JSONField(name = "supplier_price")
	private List<SupplierPrice> supplier_price_list;

	public class SupplierPrice {
		private BigDecimal purchase_price;
		private String supplier_name;

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

	}

	/**
	 * @return the all_purchase_price
	 */
	public BigDecimal getAll_purchase_price() {
		return all_purchase_price;
	}

	/**
	 * @param all_purchase_price
	 *            the all_purchase_price to set
	 */
	public void setAll_purchase_price(BigDecimal all_purchase_price) {
		this.all_purchase_price = all_purchase_price;
	}

	/**
	 * @return the all_purchase_sheet_num
	 */
	public BigDecimal getAll_purchase_sheet_num() {
		return all_purchase_sheet_num;
	}

	/**
	 * @param all_purchase_sheet_num
	 *            the all_purchase_sheet_num to set
	 */
	public void setAll_purchase_sheet_num(BigDecimal all_purchase_sheet_num) {
		this.all_purchase_sheet_num = all_purchase_sheet_num;
	}

	/**
	 * @return the supplier_price_list
	 */
	public List<SupplierPrice> getSupplier_price_list() {
		return supplier_price_list;
	}

	/**
	 * @param supplier_price_list
	 *            the supplier_price_list to set
	 */
	public void setSupplier_price_list(List<SupplierPrice> supplier_price_list) {
		this.supplier_price_list = supplier_price_list;
	}

}
