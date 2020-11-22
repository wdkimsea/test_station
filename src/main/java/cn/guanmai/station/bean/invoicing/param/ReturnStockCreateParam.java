package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月16日 下午2:21:49
 * @description: 接口 /stock/return_stock_sheet/new_create 对应的参数
 * @version: 1.0
 */

public class ReturnStockCreateParam {
	private String supplier_name;
	private String settle_supplier_id;
	private String submit_time;
	private String return_sheet_remark;
	private BigDecimal sku_money;
	private BigDecimal delta_money;
	private int is_submit;
	private List<Detail> details;
	private List<Discount> discount;

	public class Detail {
		private String id;
		private String name;
		private String batch_number;
		private String operator;
		private String category;
		private String spu_id;
		private String std_unit;
		private String spu_remark;
		private BigDecimal tax_rate;
		private BigDecimal tax_money;
		private BigDecimal return_money_no_tax;
		private BigDecimal quantity;
		private BigDecimal unit_price;
		private BigDecimal money;
		private BigDecimal remain;
		private BigDecimal different_price;

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

		public String getBatch_number() {
			return batch_number;
		}

		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getStd_unit() {
			return std_unit;
		}

		public void setStd_unit(String std_unit) {
			this.std_unit = std_unit;
		}

		public String getSpu_remark() {
			return spu_remark;
		}

		public void setSpu_remark(String spu_remark) {
			this.spu_remark = spu_remark;
		}

		public BigDecimal getTax_rate() {
			return tax_rate != null ? tax_rate.divide(new BigDecimal("100")) : null;
		}

		public void setTax_rate(BigDecimal tax_rate) {
			this.tax_rate = tax_rate.multiply(new BigDecimal("100"));
		}

		public BigDecimal getTax_money() {
			return tax_money;
		}

		public void setTax_money(BigDecimal tax_money) {
			this.tax_money = tax_money;
		}

		public BigDecimal getReturn_money_no_tax() {
			return return_money_no_tax;
		}

		public void setReturn_money_no_tax(BigDecimal return_money_no_tax) {
			this.return_money_no_tax = return_money_no_tax;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getUnit_price() {
			return unit_price;
		}

		public void setUnit_price(BigDecimal unit_price) {
			this.unit_price = unit_price;
		}

		public BigDecimal getMoney() {
			return money;
		}

		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		public BigDecimal getRemain() {
			return remain;
		}

		public void setRemain(BigDecimal remain) {
			this.remain = remain;
		}

		public BigDecimal getDifferent_price() {
			return different_price;
		}

		public void setDifferent_price(BigDecimal different_price) {
			this.different_price = different_price;
		}
	}

	public class Discount {
		private int reason;
		private int action;
		private String operate_time;
		private String operator;
		private String create_time;
		private BigDecimal money;
		private String remark;

		public int getReason() {
			return reason;
		}

		public void setReason(int reason) {
			this.reason = reason;
		}

		public int getAction() {
			return action;
		}

		public void setAction(int action) {
			this.action = action;
		}

		public String getOperate_time() {
			return operate_time;
		}

		public void setOperate_time(String operate_time) {
			this.operate_time = operate_time;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public BigDecimal getMoney() {
			return money;
		}

		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param submit_time
	 */
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}

	public String getReturn_sheet_remark() {
		return return_sheet_remark;
	}

	public void setReturn_sheet_remark(String return_sheet_remark) {
		this.return_sheet_remark = return_sheet_remark;
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

	public int getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(int is_submit) {
		this.is_submit = is_submit;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public List<Discount> getDiscount() {
		return discount;
	}

	public void setDiscount(List<Discount> discount) {
		this.discount = discount;
	}

}
