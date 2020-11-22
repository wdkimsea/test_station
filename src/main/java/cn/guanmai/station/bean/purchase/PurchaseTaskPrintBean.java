package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年8月14日
 * @time 下午7:46:05
 * @des 接口 /purchase/task/print 对应的结果
 */

public class PurchaseTaskPrintBean {
	private String customer_id;
	private String settle_supplier_id;
	private String settle_supplier_name;
	private List<Purchaser> purchaser;

	public class Purchaser {
		private BigDecimal purchaser_id;
		private String purchaser_name;

		public BigDecimal getPurchaser_id() {
			return purchaser_id;
		}

		public void setPurchaser_id(BigDecimal purchaser_id) {
			this.purchaser_id = purchaser_id;
		}

		public String getPurchaser_name() {
			return purchaser_name;
		}

		public void setPurchaser_name(String purchaser_name) {
			this.purchaser_name = purchaser_name;
		}

	}

	private List<Task> tasks;

	public class Task {
		private int address_count;
		private String category1_name;
		private String category2_name;
		private String pinlei_name;
		private BigDecimal price;
		private BigDecimal sale_ratio;
		private String sale_unit_name;
		private String sku_name;
		private String std_unit_name;
		private BigDecimal stock;
		private BigDecimal suggest_purchase_num;

		private List<Address> addresses;

		public class Address {
			private String address_id;
			private BigDecimal plan_purchase_amount;
			private String remark;
			private String res_name;
			private BigDecimal sale_ratio;
			private String sale_unit_name;
			private String sort_id;

			public String getAddress_id() {
				return address_id;
			}

			public void setAddress_id(String address_id) {
				this.address_id = address_id;
			}

			public BigDecimal getPlan_purchase_amount() {
				return plan_purchase_amount;
			}

			public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
				this.plan_purchase_amount = plan_purchase_amount;
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

			public String getSort_id() {
				return sort_id;
			}

			public void setSort_id(String sort_id) {
				this.sort_id = sort_id;
			}
		}

		public int getAddress_count() {
			return address_count;
		}

		public void setAddress_count(int address_count) {
			this.address_count = address_count;
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

		public String getPinlei_name() {
			return pinlei_name;
		}

		public void setPinlei_name(String pinlei_name) {
			this.pinlei_name = pinlei_name;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
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

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
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

		public BigDecimal getSuggest_purchase_num() {
			return suggest_purchase_num;
		}

		public void setSuggest_purchase_num(BigDecimal suggest_purchase_num) {
			this.suggest_purchase_num = suggest_purchase_num;
		}

		public List<Address> getAddresses() {
			return addresses;
		}

		public void setAddresses(List<Address> addresses) {
			this.addresses = addresses;
		}

	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getSettle_supplier_name() {
		return settle_supplier_name;
	}

	public void setSettle_supplier_name(String settle_supplier_name) {
		this.settle_supplier_name = settle_supplier_name;
	}

	public List<Purchaser> getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(List<Purchaser> purchaser) {
		this.purchaser = purchaser;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
