package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Feb 27, 2019 11:23:09 AM 
* @des 成品退货单详细
* @version 1.0 
*/
public class ReturnStockDetailBean {
	private BigDecimal sku_money;
	private String station_id;
	private String creator;
	private Integer status;
	private BigDecimal delta_money;
	private Integer supplier_status;
	private Integer type;
	private String settle_supplier_id;
	private String date_time;
	private String submit_time;
	private String supplier_name;
	private String id;
	private String return_sheet_remark;

	@JSONField(name="discount")
	private List<Discount> discounts;

	public class Discount {
		private Integer action;
		private String remark;
		private Integer reason;
		private BigDecimal money;
		private String operate_time;
		private String creator;

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

		/**
		 * @return the operate_time
		 */
		public String getOperate_time() {
			return operate_time;
		}

		/**
		 * @param operate_time the operate_time to set
		 */
		public void setOperate_time(String operate_time) {
			this.operate_time = operate_time;
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
	}

	public List<Detail> details;

	public class Detail {
		private String name;
		private String id;
		private String spu_id;
		private String category;
		private String std_unit;
		private String spu_remark;
		private BigDecimal unit_price;
		private BigDecimal quantity;
		private BigDecimal money;
		private String operator;
		// 先进先出退货特有参数
		private String batch_number;
		// 先进先出退货特有参数
		private BigDecimal remain;

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

		public String getSpu_remark() {
			return spu_remark;
		}

		public void setSpu_remark(String spu_remark) {
			this.spu_remark = spu_remark;
		}

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
		 * @return the batch_number
		 */
		public String getBatch_number() {
			return batch_number;
		}

		/**
		 * // 先进先出退货特有参数 退货批次
		 * 
		 * @param batch_number the batch_number to set
		 */
		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

		/**
		 * @return the remain
		 */
		public BigDecimal getRemain() {
			return remain;
		}

		/**
		 * 先进先出退货特有参数 退货批次库存
		 * 
		 * @param remain the remain to set
		 */
		public void setRemain(BigDecimal remain) {
			this.remain = remain;
		}

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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the delta_money
	 */
	public BigDecimal getDelta_money() {
		return delta_money;
	}

	public Integer getSupplier_status() {
		return supplier_status;
	}

	public void setSupplier_status(Integer supplier_status) {
		this.supplier_status = supplier_status;
	}

	/**
	 * @param delta_money the delta_money to set
	 */
	public void setDelta_money(BigDecimal delta_money) {
		this.delta_money = delta_money;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
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

	public String getReturn_sheet_remark() {
		return return_sheet_remark;
	}

	public void setReturn_sheet_remark(String return_sheet_remark) {
		this.return_sheet_remark = return_sheet_remark;
	}

}
