package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Feb 22, 2019 7:06:41 PM 
* @des 入库单参数封装类
* @version 1.0 
*/
public class InStockDetailInfoBean {
	private String settle_supplier_id;
	private BigDecimal sku_money;
	private String creator;
	private String supplier_customer_id;
	private int type;
	private BigDecimal delta_money;
	private String id;
	private String station_id;
	private int status;
	private Integer supplier_status;
	private String supplier_name;
	private String submit_time;
	private String remark;
	private String date_time;
	private int is_submit;

	private List<Detail> details;

	public class Detail {
		private BigDecimal unit_price;
		private String remark;
		private String desc;
		private String category;
		private String std_unit;
		private String batch_number;
		private BigDecimal money;
		private BigDecimal quantity;
		private String purchase_unit;
		private String life_time;
		private String id;
		private String supplier_stock_avg_price;
		private BigDecimal shelf_id;
		private String operator;
		private String name;
		private BigDecimal ratio;
		private String shelf_name;
		private String spu_id;
		private String displayName;
		private BigDecimal purchase_unit_quantity;
		private BigDecimal purchase_unit_price;
		private int is_arrival; // 到货状态
		private BigDecimal different_price; // 补差

		/**
		 * @return the unit_price
		 */
		public BigDecimal getUnit_price() {
			return unit_price;
		}

		/**
		 * @param unit_price the unit_price to set
		 */
		public void setUnit_price(BigDecimal unit_price) {
			this.unit_price = unit_price;
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
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}

		/**
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}

		/**
		 * @return the category
		 */
		public String getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 */
		public void setCategory(String category) {
			this.category = category;
		}

		/**
		 * @return the std_unit
		 */
		public String getStd_unit() {
			return std_unit;
		}

		/**
		 * @param std_unit the std_unit to set
		 */
		public void setStd_unit(String std_unit) {
			this.std_unit = std_unit;
		}

		/**
		 * @return the batch_number
		 */
		public String getBatch_number() {
			return batch_number;
		}

		/**
		 * @param batch_number the batch_number to set
		 */
		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

		/**
		 * @return the money
		 */
		public BigDecimal getMoney() {
			return money.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		/**
		 * @param money the money to set
		 */
		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		/**
		 * @return the quantity
		 */
		public BigDecimal getQuantity() {
			return quantity;
		}

		/**
		 * @param quantity the quantity to set
		 */
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		/**
		 * @return the purchase_unit
		 */
		public String getPurchase_unit() {
			return purchase_unit;
		}

		/**
		 * @param purchase_unit the purchase_unit to set
		 */
		public void setPurchase_unit(String purchase_unit) {
			this.purchase_unit = purchase_unit;
		}

		/**
		 * @return the life_time
		 */
		public String getLife_time() {
			return life_time;
		}

		/**
		 * @param life_time the life_time to set
		 */
		public void setLife_time(String life_time) {
			this.life_time = life_time;
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
		 * @return the supplier_stock_avg_price
		 */
		public String getSupplier_stock_avg_price() {
			return supplier_stock_avg_price;
		}

		/**
		 * @param supplier_stock_avg_price the supplier_stock_avg_price to set
		 */
		public void setSupplier_stock_avg_price(String supplier_stock_avg_price) {
			this.supplier_stock_avg_price = supplier_stock_avg_price;
		}

		public BigDecimal getShelf_id() {
			return shelf_id;
		}

		public void setShelf_id(BigDecimal shelf_id) {
			this.shelf_id = shelf_id;
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
		 * @return the shelf_name
		 */
		public String getShelf_name() {
			return shelf_name;
		}

		/**
		 * @param shelf_name the shelf_name to set
		 */
		public void setShelf_name(String shelf_name) {
			this.shelf_name = shelf_name;
		}

		/**
		 * @return the spu_id
		 */
		public String getSpu_id() {
			return spu_id;
		}

		/**
		 * @param spu_id the spu_id to set
		 */
		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		/**
		 * @return the displayName
		 */
		public String getDisplayName() {
			return displayName;
		}

		/**
		 * @param displayName the displayName to set
		 */
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * @return the purchase_unit_quantity
		 */
		public BigDecimal getPurchase_unit_quantity() {
			return purchase_unit_quantity;
		}

		/**
		 * @param purchase_unit_quantity the purchase_unit_quantity to set
		 */
		public void setPurchase_unit_quantity(BigDecimal purchase_unit_quantity) {
			this.purchase_unit_quantity = purchase_unit_quantity;
		}

		/**
		 * @return the purchase_unit_price
		 */
		public BigDecimal getPurchase_unit_price() {
			return purchase_unit_price;
		}

		/**
		 * @param purchase_unit_price the purchase_unit_price to set
		 */
		public void setPurchase_unit_price(BigDecimal purchase_unit_price) {
			this.purchase_unit_price = purchase_unit_price;
		}

		/**
		 * @return the is_arrival
		 */
		public int getIs_arrival() {
			return is_arrival;
		}

		/**
		 * @param is_arrival the is_arrival to set
		 */
		public void setIs_arrival(int is_arrival) {
			this.is_arrival = is_arrival;
		}

		/**
		 * @return the different_price
		 */
		public BigDecimal getDifferent_price() {
			return different_price;
		}

		/**
		 * @param different_price the different_price to set
		 */
		public void setDifferent_price(BigDecimal different_price) {
			this.different_price = different_price;
		}

	}

	@JSONField(name = "share")
	private List<Share> shares;

	public class Share {
		private Integer action;
		private String create_time;
		private BigDecimal money;
		private JSONArray in_sku_logs;
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

		/**
		 * @return the in_sku_logs
		 */
		public JSONArray getIn_sku_logs() {
			return in_sku_logs;
		}

		/**
		 * @param in_sku_logs the in_sku_logs to set
		 */
		public void setIn_sku_logs(JSONArray in_sku_logs) {
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

	@JSONField(name = "discount")
	public List<Discount> discounts;

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
	 * @return the sku_money
	 */
	public BigDecimal getSku_money() {
		return sku_money;
	}

	/**
	 * @param sku_money the sku_money to set
	 */
	public void setSku_money(BigDecimal sku_money) {
		this.sku_money = sku_money;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the supplier_customer_id
	 */
	public String getSupplier_customer_id() {
		return supplier_customer_id;
	}

	/**
	 * @param supplier_customer_id the supplier_customer_id to set
	 */
	public void setSupplier_customer_id(String supplier_customer_id) {
		this.supplier_customer_id = supplier_customer_id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the delta_money
	 */
	public BigDecimal getDelta_money() {
		return delta_money;
	}

	/**
	 * @param delta_money the delta_money to set
	 */
	public void setDelta_money(BigDecimal delta_money) {
		this.delta_money = delta_money;
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
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
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

	public Integer getSupplier_status() {
		return supplier_status;
	}

	public void setSupplier_status(Integer supplier_status) {
		this.supplier_status = supplier_status;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	/**
	 * @return the submit_time
	 */
	public String getSubmit_time() {
		return submit_time;
	}

	/**
	 * @param submit_time the submit_time to set
	 */
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
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
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time the date_time to set
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	/**
	 * @return the is_submit
	 */
	public int getIs_submit() {
		return is_submit;
	}

	/**
	 * @param is_submit the is_submit to set
	 */
	public void setIs_submit(int is_submit) {
		this.is_submit = is_submit;
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
	 * @return the shares
	 */
	public List<Share> getShares() {
		return shares;
	}

	/**
	 * @param shares the shares to set
	 */
	public void setShares(List<Share> shares) {
		this.shares = shares;
	}

	/**
	 * @return the discounts
	 */
	public List<Discount> getDiscounts() {
		return discounts;
	}

	/**
	 * @param discounts the discounts to set
	 */
	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

}
