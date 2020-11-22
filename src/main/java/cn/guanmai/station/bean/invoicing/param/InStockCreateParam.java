package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年3月12日 下午4:40:46
 * @description:
 * @version: 1.0
 */

public class InStockCreateParam {
	private String settle_supplier_id;
	private String supplier_name;
	private BigDecimal sku_money;
	private BigDecimal delta_money;
	// 提交入库时间,格式yyyy-MM-dd HH:mm
	private String submit_time_new;
	private List<Detail> details;
	private int is_submit;
	private String remark;

	public class Detail {
		private String batch_number;
		private String category;
		private String category_name_1;
		private String category_name_2;
		private String spu_id;
		private String id;
		private String name;
		private String displayName;
		private String purchase_unit;
		private String std_unit;
		private String life_time;
		private String shelf_id;
		private String shelf_name;

		private Integer is_arrival;
		private BigDecimal money;
		private BigDecimal unit_price;
		private BigDecimal purchase_unit_price;
		private BigDecimal quantity;
		private BigDecimal purchase_unit_quantity;
		private BigDecimal ratio;

		public String getBatch_number() {
			return batch_number;
		}

		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getCategory_name_1() {
			return category_name_1;
		}

		public void setCategory_name_1(String category_name_1) {
			this.category_name_1 = category_name_1;
		}

		public String getCategory_name_2() {
			return category_name_2;
		}

		public void setCategory_name_2(String category_name_2) {
			this.category_name_2 = category_name_2;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getPurchase_unit() {
			return purchase_unit;
		}

		public void setPurchase_unit(String purchase_unit) {
			this.purchase_unit = purchase_unit;
		}

		public String getStd_unit() {
			return std_unit;
		}

		public void setStd_unit(String std_unit) {
			this.std_unit = std_unit;
		}

		public String getLife_time() {
			return life_time;
		}

		public void setLife_time(String life_time) {
			this.life_time = life_time;
		}

		public String getShelf_id() {
			return shelf_id;
		}

		public void setShelf_id(String shelf_id) {
			this.shelf_id = shelf_id;
		}

		public String getShelf_name() {
			return shelf_name;
		}

		public void setShelf_name(String shelf_name) {
			this.shelf_name = shelf_name;
		}

		public Integer getIs_arrival() {
			return is_arrival;
		}

		public void setIs_arrival(Integer is_arrival) {
			this.is_arrival = is_arrival;
		}

		public BigDecimal getMoney() {
			return money;
		}

		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		public BigDecimal getUnit_price() {
			return unit_price;
		}

		public void setUnit_price(BigDecimal unit_price) {
			this.unit_price = unit_price;
		}

		public BigDecimal getPurchase_unit_price() {
			return purchase_unit_price;
		}

		public void setPurchase_unit_price(BigDecimal purchase_unit_price) {
			this.purchase_unit_price = purchase_unit_price;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getPurchase_unit_quantity() {
			return purchase_unit_quantity;
		}

		public void setPurchase_unit_quantity(BigDecimal purchase_unit_quantity) {
			this.purchase_unit_quantity = purchase_unit_quantity;
		}

		public BigDecimal getRatio() {
			return ratio;
		}

		public void setRatio(BigDecimal ratio) {
			this.ratio = ratio;
		}
	}

	private List<Share> share;

	public class Share {
		private Integer action;
		private String create_time;
		private BigDecimal money;
		private List<String> in_sku_logs;
		private String remark;
		private Integer method;
		private String operator;
		private Integer reason;

		/**
		 * @return the action
		 */
		public Integer getAction() {
			return action;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(Integer action) {
			this.action = action;
		}

		/**
		 * @return the create_time
		 */
		public String getCreate_time() {
			return create_time;
		}

		/**
		 * @param create_time the create_time to set
		 */
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		/**
		 * @return the money
		 */
		public BigDecimal getMoney() {
			return money;
		}

		/**
		 * @param money the money to set
		 */
		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		public List<String> getIn_sku_logs() {
			return in_sku_logs;
		}

		public void setIn_sku_logs(List<String> in_sku_logs) {
			this.in_sku_logs = in_sku_logs;
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
		 * @return the method
		 */
		public Integer getMethod() {
			return method;
		}

		/**
		 * @param method the method to set
		 */
		public void setMethod(Integer method) {
			this.method = method;
		}

		/**
		 * @return the operator
		 */
		public String getOperator() {
			return operator;
		}

		/**
		 * @param operator the operator to set
		 */
		public void setOperator(String operator) {
			this.operator = operator;
		}

		/**
		 * @return the reason
		 */
		public Integer getReason() {
			return reason;
		}

		/**
		 * @param reason the reason to set
		 */
		public void setReason(Integer reason) {
			this.reason = reason;
		}

	}

	private List<Discount> discount;

	public class Discount {
		private Integer action;
		private String remark;
		private Integer reason;
		private BigDecimal money;

		/**
		 * @return the action
		 */
		public Integer getAction() {
			return action;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(Integer action) {
			this.action = action;
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
		 * @return the reason
		 */
		public Integer getReason() {
			return reason;
		}

		/**
		 * @param reason the reason to set
		 */
		public void setReason(Integer reason) {
			this.reason = reason;
		}

		/**
		 * @return the money
		 */
		public BigDecimal getMoney() {
			return money;
		}

		/**
		 * @param money the money to set
		 */
		public void setMoney(BigDecimal money) {
			this.money = money;
		}
	}

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

	public BigDecimal getSku_money() {
		return sku_money;
	}

	public void setSku_money(BigDecimal sku_money) {
		this.sku_money = sku_money;
	}

	public BigDecimal getDelta_money() {
		return delta_money;
	}

	public void setDelta_money(BigDecimal delta_money) {
		this.delta_money = delta_money;
	}

	public String getSubmit_time_new() {
		return submit_time_new;
	}

	public void setSubmit_time_new(String submit_time_new) {
		this.submit_time_new = submit_time_new;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public List<Share> getShare() {
		return share;
	}

	public void setShare(List<Share> share) {
		this.share = share;
	}

	public List<Discount> getDiscount() {
		return discount;
	}

	public void setDiscount(List<Discount> discount) {
		this.discount = discount;
	}

	public int getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(int is_submit) {
		this.is_submit = is_submit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
