package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 18, 2019 7:43:38 PM 
* @des 采购任务总览
* @version 1.0 
*/
public class PurcahseTaskSummaryBean {
	@JSONField(name = "purchaser")
	private List<PurchaserSummary> purchaserSummaries;
	
	@JSONField(name = "suppliers")
	private List<SupplierSummary> supplierSummaries;

	public class SupplierSummary {
		private String purchaser_name;
		private String settle_supplier_id;
		private int purchase_amount;
		private BigDecimal purchaser_id;

		/**
		 * @return the purchaser_name
		 */
		public String getPurchaser_name() {
			return purchaser_name;
		}

		/**
		 * @param purchaser_name
		 *            the purchaser_name to set
		 */
		public void setPurchaser_name(String purchaser_name) {
			this.purchaser_name = purchaser_name;
		}

		/**
		 * @return the settle_supplier_id
		 */
		public String getSettle_supplier_id() {
			return settle_supplier_id;
		}

		/**
		 * @param settle_supplier_id
		 *            the settle_supplier_id to set
		 */
		public void setSettle_supplier_id(String settle_supplier_id) {
			this.settle_supplier_id = settle_supplier_id;
		}

		/**
		 * @return the purchase_amount
		 */
		public int getPurchase_amount() {
			return purchase_amount;
		}

		/**
		 * @param purchase_amount
		 *            the purchase_amount to set
		 */
		public void setPurchase_amount(int purchase_amount) {
			this.purchase_amount = purchase_amount;
		}

		/**
		 * @return the purchaser_id
		 */
		public BigDecimal getPurchaser_id() {
			return purchaser_id;
		}

		/**
		 * @param purchaser_id
		 *            the purchaser_id to set
		 */
		public void setPurchaser_id(BigDecimal purchaser_id) {
			this.purchaser_id = purchaser_id;
		}

	}

	public class PurchaserSummary {
		private String settle_supplier_id;
		private int purchase_amount;

		/**
		 * @return the settle_supplier_id
		 */
		public String getSettle_supplier_id() {
			return settle_supplier_id;
		}

		/**
		 * @param settle_supplier_id
		 *            the settle_supplier_id to set
		 */
		public void setSettle_supplier_id(String settle_supplier_id) {
			this.settle_supplier_id = settle_supplier_id;
		}

		/**
		 * @return the purchase_amount
		 */
		public int getPurchase_amount() {
			return purchase_amount;
		}

		/**
		 * @param purchase_amount
		 *            the purchase_amount to set
		 */
		public void setPurchase_amount(int purchase_amount) {
			this.purchase_amount = purchase_amount;
		}

	}

	/**
	 * @return the purchaserSummaries
	 */
	public List<PurchaserSummary> getPurchaserSummaries() {
		return purchaserSummaries;
	}

	/**
	 * @param purchaserSummaries
	 *            the purchaserSummaries to set
	 */
	public void setPurchaserSummaries(List<PurchaserSummary> purchaserSummaries) {
		this.purchaserSummaries = purchaserSummaries;
	}

	/**
	 * @return the supplierSummaries
	 */
	public List<SupplierSummary> getSupplierSummaries() {
		return supplierSummaries;
	}

	/**
	 * @param supplierSummaries
	 *            the supplierSummaries to set
	 */
	public void setSupplierSummaries(List<SupplierSummary> supplierSummaries) {
		this.supplierSummaries = supplierSummaries;
	}
}
