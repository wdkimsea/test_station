package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 上午11:40:46
 * @des TODO
 */

public class PurchaseTaskShareBean {
	private String customer_id;
	private String settle_supplier_id;
	private String settle_supplier_name;

	@JSONField(name="purchaser")
	private List<Purchaser> purchasers;

	public class Purchaser {
		private String purchaser_id;
		private String purchaser_name;

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
	}

	private List<Task> tasks;

	public class Task {
		private String category1_name;
		private String category2_name;
		private String pinlei_name;
		private String sku_name;
		private String std_unit_name;
		private String sale_unit_name;
		private BigDecimal stock;
		private BigDecimal suggest_purchase_num;
		private BigDecimal sale_ratio;
		private String description;
		private BigDecimal price;

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

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
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

		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
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

	public List<Purchaser> getPurchasers() {
		return purchasers;
	}

	public void setPurchasers(List<Purchaser> purchasers) {
		this.purchasers = purchasers;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
