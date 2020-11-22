package cn.guanmai.open.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午10:43:57
 * @des TODO
 */

public class OpenPurchaseTaskBean {
	private String category1_name;
	private String category2_name;
	private boolean generated_purchase_sheet;
	private String spec_name;
	private String pinlei_name;
	private BigDecimal purchased_count;
	private BigDecimal plan_purchase_count;
	private String purchaser_id;
	private String purchaser_name;
	private BigDecimal sale_ratio;
	private String sale_unit_name;
	private String supplier_id;
	private String supplier_name;
	private String spec_id;
	private int status;
	private String std_unit_name;
	private BigDecimal stock;
	private String stock_avg_price;
	private List<PurchaseTask> purchase_tasks;

	public class PurchaseTask {
		private String task_id;
		private String order_id;
		private String order_status;
		private BigDecimal plan_purchase_count;
		private String purchaser_name;
		private String remark;
		private String res_name;
		private String route_name;
		private String sale_ratio;
		private String sale_unit_name;
		private String supplier_id;
		private String supplier_name;
		private String std_unit_name;

		public String getTask_id() {
			return task_id;
		}

		public void setTask_id(String task_id) {
			this.task_id = task_id;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public String getOrder_status() {
			return order_status;
		}

		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}

		public BigDecimal getPlan_purchase_count() {
			return plan_purchase_count;
		}

		public void setPlan_purchase_count(BigDecimal plan_purchase_count) {
			this.plan_purchase_count = plan_purchase_count;
		}

		public String getPurchaser_name() {
			return purchaser_name;
		}

		public void setPurchaser_name(String purchaser_name) {
			this.purchaser_name = purchaser_name;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getRes_name() {
			return res_name;
		}

		public void setRes_name(String res_name) {
			this.res_name = res_name;
		}

		public String getRoute_name() {
			return route_name;
		}

		public void setRoute_name(String route_name) {
			this.route_name = route_name;
		}

		public String getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(String sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		public String getSupplier_id() {
			return supplier_id;
		}

		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}

		public String getSupplier_name() {
			return supplier_name;
		}

		public void setSupplier_name(String supplier_name) {
			this.supplier_name = supplier_name;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}
	}

	public String getCategory1_name() {
		return category1_name;
	}

	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	public String getCategory2_name() {
		return category2_name;
	}

	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}

	public boolean isGenerated_purchase_sheet() {
		return generated_purchase_sheet;
	}

	public void setGenerated_purchase_sheet(boolean generated_purchase_sheet) {
		this.generated_purchase_sheet = generated_purchase_sheet;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	public String getPinlei_name() {
		return pinlei_name;
	}

	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	public BigDecimal getPurchased_count() {
		return purchased_count;
	}

	public void setPurchased_count(BigDecimal purchased_count) {
		this.purchased_count = purchased_count;
	}

	public BigDecimal getPlan_purchase_count() {
		return plan_purchase_count;
	}

	public void setPlan_purchase_count(BigDecimal plan_purchase_count) {
		this.plan_purchase_count = plan_purchase_count;
	}

	public String getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	public String getPurchaser_name() {
		return purchaser_name;
	}

	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public String getStock_avg_price() {
		return stock_avg_price;
	}

	public void setStock_avg_price(String stock_avg_price) {
		this.stock_avg_price = stock_avg_price;
	}

	public List<PurchaseTask> getPurchase_tasks() {
		return purchase_tasks;
	}

	public void setPurchase_tasks(List<PurchaseTask> purchase_tasks) {
		this.purchase_tasks = purchase_tasks;
	}

}
