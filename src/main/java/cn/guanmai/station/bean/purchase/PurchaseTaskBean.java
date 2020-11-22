package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 16, 2018 9:56:39 AM 
* @des 采购任务类
* @version 1.0 
*/
public class PurchaseTaskBean {
	private int code;
	private String msg;
	@JSONField(name = "data")
	private List<PurchaseTaskData> purchaseTaskDataArray;
	private Pagination pagination;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the purchaseTaskDataArray
	 */
	public List<PurchaseTaskData> getPurchaseTaskDataArray() {
		return purchaseTaskDataArray;
	}

	/**
	 * @param purchaseTaskDataArray the purchaseTaskDataArray to set
	 */
	public void setPurchaseTaskDataArray(List<PurchaseTaskData> purchaseTaskDataArray) {
		this.purchaseTaskDataArray = purchaseTaskDataArray;
	}

	/**
	 * @return the pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * @param pagination the pagination to set
	 */
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public static class PurchaseTaskData {
		private String category1_name;
		private String category2_name;
		private String pinlei_name;
		private String spec_id;
		private String name;
		private BigDecimal plan_purchase_amount;
		private String settle_supplier_name;
		private String settle_supplier_id;
		private String supplier_status;
		private BigDecimal sale_ratio;
		private String sale_unit_name;
		private BigDecimal release_id;
		private String purchaser_id;
		private String purchaser_name;
		private int status;

		private List<Task> tasks;

		/**
		 * @return the category1_name
		 */
		public String getCategory1_name() {
			return category1_name;
		}

		/**
		 * @param category1_name the category1_name to set
		 */
		public void setCategory1_name(String category1_name) {
			this.category1_name = category1_name;
		}

		/**
		 * @return the category2_name
		 */
		public String getCategory2_name() {
			return category2_name;
		}

		/**
		 * @param category2_name the category2_name to set
		 */
		public void setCategory2_name(String category2_name) {
			this.category2_name = category2_name;
		}

		/**
		 * @return the pinlei_name
		 */
		public String getPinlei_name() {
			return pinlei_name;
		}

		/**
		 * @param pinlei_name the pinlei_name to set
		 */
		public void setPinlei_name(String pinlei_name) {
			this.pinlei_name = pinlei_name;
		}

		/**
		 * @return the spec_id
		 */
		public String getSpec_id() {
			return spec_id;
		}

		/**
		 * @param spec_id the spec_id to set
		 */
		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the plan_purchase_amount
		 */
		public BigDecimal getPlan_purchase_amount() {
			return plan_purchase_amount;
		}

		/**
		 * @param plan_purchase_amount the plan_purchase_amount to set
		 */
		public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
			this.plan_purchase_amount = plan_purchase_amount;
		}

		public String getSettle_supplier_name() {
			return settle_supplier_name;
		}

		public void setSettle_supplier_name(String settle_supplier_name) {
			this.settle_supplier_name = settle_supplier_name;
		}

		/**
		 * @return the settle_supplier_id
		 */
		public String getSettle_supplier_id() {
			return settle_supplier_id;
		}

		/**
		 * @param settle_supplier_id the settle_supplier_id to set
		 */
		public void setSettle_supplier_id(String settle_supplier_id) {
			this.settle_supplier_id = settle_supplier_id;
		}

		/**
		 * 0 代表供应商已删除
		 * 
		 * @return
		 */
		public String getSupplier_status() {
			return supplier_status;
		}

		public void setSupplier_status(String supplier_status) {
			this.supplier_status = supplier_status;
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
		 * @return the release_id
		 */
		public BigDecimal getRelease_id() {
			return release_id;
		}

		/**
		 * @param release_id the release_id to set
		 */
		public void setRelease_id(BigDecimal release_id) {
			this.release_id = release_id;
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

		public List<Task> getTasks() {
			return tasks;
		}

		public void setTasks(List<Task> tasks) {
			this.tasks = tasks;
		}

		public PurchaseTaskData() {
			super();
		}

		public class Task {
			private String id;
			private String order_id;
			private int order_status;
			private String route_name;
			private BigDecimal plan_purchase_amount;

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
			 * @return the order_id
			 */
			public String getOrder_id() {
				return order_id;
			}

			/**
			 * @param order_id the order_id to set
			 */
			public void setOrder_id(String order_id) {
				this.order_id = order_id;
			}

			/**
			 * @return the order_status
			 */
			public int getOrder_status() {
				return order_status;
			}

			/**
			 * @param order_status the order_status to set
			 */
			public void setOrder_status(int order_status) {
				this.order_status = order_status;
			}

			/**
			 * @return the route_name
			 */
			public String getRoute_name() {
				return route_name;
			}

			/**
			 * @param route_name the route_name to set
			 */
			public void setRoute_name(String route_name) {
				this.route_name = route_name;
			}

			/**
			 * @return the plan_purchase_amount
			 */
			public BigDecimal getPlan_purchase_amount() {
				return plan_purchase_amount;
			}

			/**
			 * @param plan_purchase_amount the plan_purchase_amount to set
			 */
			public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
				this.plan_purchase_amount = plan_purchase_amount;
			}

			public Task() {
				super();
			}

		}

	}

	public class Pagination {
		private boolean more;
		private String page_obj;

		/**
		 * @return the more
		 */
		public boolean isMore() {
			return more;
		}

		/**
		 * @param more the more to set
		 */
		public void setMore(boolean more) {
			this.more = more;
		}

		/**
		 * @return the page_obj
		 */
		public String getPage_obj() {
			return page_obj;
		}

		/**
		 * @param page_obj the page_obj to set
		 */
		public void setPage_obj(String page_obj) {
			this.page_obj = page_obj;
		}

		public Pagination() {
			super();
		}
	}

	public PurchaseTaskBean() {
		super();
	}

}
