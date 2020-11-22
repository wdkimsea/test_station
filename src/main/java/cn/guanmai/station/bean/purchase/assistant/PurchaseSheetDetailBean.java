package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date May 27, 2019 3:37:29 PM 
* @des 接口 /purchase_assistant/purchase_sheet/details 对应的值
*      采购助手APP-采购单据详情
* @version 1.0 
*/
public class PurchaseSheetDetailBean {
	private PurchaseSheet purchase_sheet;

	public PurchaseSheet getPurchase_sheet() {
		return purchase_sheet;
	}

	public void setPurchase_sheet(PurchaseSheet purchase_sheet) {
		this.purchase_sheet = purchase_sheet;
	}

	public class PurchaseSheet {
		private String settle_supplier_id;
		private String supplier_name;
		private int status;
		private int sku_num;
		private BigDecimal purchase_sheet_money;

		public String getSettle_supplier_id() {
			return settle_supplier_id;
		}

		public void setSettle_supplier_id(String settle_supplier_id) {
			this.settle_supplier_id = settle_supplier_id;
		}

		public String getSupplier_name() {
			return supplier_name;
		}

		public void setSupplier_name(String supplier_name) {
			this.supplier_name = supplier_name;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getSku_num() {
			return sku_num;
		}

		public void setSku_num(int sku_num) {
			this.sku_num = sku_num;
		}

		public BigDecimal getPurchase_sheet_money() {
			return purchase_sheet_money.divide(new BigDecimal("100"));
		}

		public void setPurchase_sheet_money(BigDecimal purchase_sheet_money) {
			this.purchase_sheet_money = purchase_sheet_money;
		}

	}

	private List<Task> tasks;

	public class Task {
		private BigDecimal already_purchased_amount;
		private String category_name_1;
		private String category_name_2;
		private String id;
		private BigDecimal last_in_stock_price;
		private BigDecimal last_purchase_price;
		private BigDecimal last_quote_price;
		private String name;
		private BigDecimal plan_amount;
		private BigDecimal purchase_amount;
		private BigDecimal purchase_price;
		private String purchase_unit_name;
		private BigDecimal ratio;
		private BigDecimal ref_price;
		private BigDecimal release_id;
		private String release_time;
		private String spec_id;
		private String spec_name;
		private int status;
		private String std_unit_name;
		private BigDecimal stock;
		private BigDecimal stock_avg_price;
		private BigDecimal suggest_purchase_num;
		private List<String> remarks;
		private List<Detail> details;
		private List<Address> address;

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
			 * @param order_id the order_id to set
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
			 * @param plan_purchase_amount the plan_purchase_amount to set
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
			 * @param remark the remark to set
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
			 * @param res_name the res_name to set
			 */
			public void setRes_name(String res_name) {
				this.res_name = res_name;
			}
		}

		public class Address {
			private BigDecimal plan_amount;
			private String remark;
			private String res_name;
			private BigDecimal suggest_purchase_num;

			/**
			 * @return the plan_amount
			 */
			public BigDecimal getPlan_amount() {
				return plan_amount;
			}

			/**
			 * @param plan_amount the plan_amount to set
			 */
			public void setPlan_amount(BigDecimal plan_amount) {
				this.plan_amount = plan_amount;
			}

			/**
			 * @return the remark
			 */
			public String getRemark() {
				return remark;
			}

			/**
			 * @param remark the remark to set
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
			 * @param res_name the res_name to set
			 */
			public void setRes_name(String res_name) {
				this.res_name = res_name;
			}

			/**
			 * @return the suggest_purchase_num
			 */
			public BigDecimal getSuggest_purchase_num() {
				return suggest_purchase_num;
			}

			/**
			 * @param suggest_purchase_num the suggest_purchase_num to set
			 */
			public void setSuggest_purchase_num(BigDecimal suggest_purchase_num) {
				this.suggest_purchase_num = suggest_purchase_num;
			}

		}

		/**
		 * @return the already_purchased_amount
		 */
		public BigDecimal getAlready_purchased_amount() {
			return already_purchased_amount;
		}

		/**
		 * @param already_purchased_amount the already_purchased_amount to set
		 */
		public void setAlready_purchased_amount(BigDecimal already_purchased_amount) {
			this.already_purchased_amount = already_purchased_amount;
		}

		/**
		 * @return the category_name_1
		 */
		public String getCategory_name_1() {
			return category_name_1;
		}

		/**
		 * @param category_name_1 the category_name_1 to set
		 */
		public void setCategory_name_1(String category_name_1) {
			this.category_name_1 = category_name_1;
		}

		/**
		 * @return the category_name_2
		 */
		public String getCategory_name_2() {
			return category_name_2;
		}

		/**
		 * @param category_name_2 the category_name_2 to set
		 */
		public void setCategory_name_2(String category_name_2) {
			this.category_name_2 = category_name_2;
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
		 * @return the last_in_stock_price
		 */
		public BigDecimal getLast_in_stock_price() {
			return last_in_stock_price;
		}

		/**
		 * @param last_in_stock_price the last_in_stock_price to set
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
		 * @param last_purchase_price the last_purchase_price to set
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
		 * @param last_quote_price the last_quote_price to set
		 */
		public void setLast_quote_price(BigDecimal last_quote_price) {
			this.last_quote_price = last_quote_price;
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
		 * @return the plan_amount
		 */
		public BigDecimal getPlan_amount() {
			return plan_amount;
		}

		/**
		 * @param plan_amount the plan_amount to set
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
		 * @param purchase_amount the purchase_amount to set
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
		 * @param purchase_price the purchase_price to set
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
		 * @param purchase_unit_name the purchase_unit_name to set
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
		 * @param ratio the ratio to set
		 */
		public void setRatio(BigDecimal ratio) {
			this.ratio = ratio;
		}

		/**
		 * @return the ref_price
		 */
		public BigDecimal getRef_price() {
			return ref_price;
		}

		/**
		 * @param ref_price the ref_price to set
		 */
		public void setRef_price(BigDecimal ref_price) {
			this.ref_price = ref_price;
		}

		public BigDecimal getRelease_id() {
			return release_id;
		}

		public void setRelease_id(BigDecimal release_id) {
			this.release_id = release_id;
		}

		/**
		 * @return the release_time
		 */
		public String getRelease_time() {
			return release_time;
		}

		/**
		 * @param release_time the release_time to set
		 */
		public void setRelease_time(String release_time) {
			this.release_time = release_time;
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

		/**
		 * @return the spec_name
		 */
		public String getSpec_name() {
			return spec_name;
		}

		/**
		 * @param spec_name the spec_name to set
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
		 * @param status the status to set
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
		 * @param std_unit_name the std_unit_name to set
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
		 * @param stock the stock to set
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
		 * @param stock_avg_price the stock_avg_price to set
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
		 * @param suggest_purchase_num the suggest_purchase_num to set
		 */
		public void setSuggest_purchase_num(BigDecimal suggest_purchase_num) {
			this.suggest_purchase_num = suggest_purchase_num;
		}

		/**
		 * @return the remarks
		 */
		public List<String> getRemarks() {
			return remarks;
		}

		/**
		 * @param remarks the remarks to set
		 */
		public void setRemarks(List<String> remarks) {
			this.remarks = remarks;
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
		 * @return the address
		 */
		public List<Address> getAddress() {
			return address;
		}

		/**
		 * @param address the address to set
		 */
		public void setAddress(List<Address> address) {
			this.address = address;
		}

	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
