package cn.guanmai.open.bean.stock;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月28日
 * @time 下午4:32:48
 * @des TODO
 */

public class OpenStockInSheetDetailBean {
	private String in_stock_sheet_id;
	private String purchase_sheet_id;
	private String supplier_id;
	private String supplier_name;
	private int status;
	private BigDecimal sku_amount;
	private BigDecimal discount_amount;
	private BigDecimal total_amount;
	private String station_id;
	private String create_date;
	private String in_stock_date;
	private String creator;
	private List<Detail> details;

	public class Detail {
		private String spu_id;
		private String spec_id;
		private String spec_name;
		private String category_name;
		private BigDecimal in_stock_count;
		private BigDecimal in_stock_amount;
		private BigDecimal in_stock_unit_price;
		private String std_unit_name;
		private String batch_number;
		private String operator;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getSpec_id() {
			return spec_id;
		}

		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		public String getSpec_name() {
			return spec_name;
		}

		public void setSpec_name(String spec_name) {
			this.spec_name = spec_name;
		}

		public String getCategory_name() {
			return category_name;
		}

		public void setCategory_name(String category_name) {
			this.category_name = category_name;
		}

		public BigDecimal getIn_stock_count() {
			return in_stock_count;
		}

		public void setIn_stock_count(BigDecimal in_stock_count) {
			this.in_stock_count = in_stock_count;
		}

		public BigDecimal getIn_stock_amount() {
			return in_stock_amount;
		}

		public void setIn_stock_amount(BigDecimal in_stock_amount) {
			this.in_stock_amount = in_stock_amount;
		}

		public BigDecimal getIn_stock_unit_price() {
			return in_stock_unit_price;
		}

		public void setIn_stock_unit_price(BigDecimal in_stock_unit_price) {
			this.in_stock_unit_price = in_stock_unit_price;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
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
	}

	private List<Discount> discounts;

	public class Discount {
		private int discount_reason;
		private int discount_action;
		private String date;
		private BigDecimal discount_amount;
		private String remark;
		private String operator;

		public int getDiscount_reason() {
			return discount_reason;
		}

		public void setDiscount_reason(int discount_reason) {
			this.discount_reason = discount_reason;
		}

		public int getDiscount_action() {
			return discount_action;
		}

		public void setDiscount_action(int discount_action) {
			this.discount_action = discount_action;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public BigDecimal getDiscount_amount() {
			return discount_amount;
		}

		public void setDiscount_amount(BigDecimal discount_amount) {
			this.discount_amount = discount_amount;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

	}

	private List<Share> shares;

	public class Share {
		private int share_reason;
		private int share_action;
		private int share_method;
		private List<String> share_specs;
		private BigDecimal share_amount;
		private String create_date;
		private String remark;
		private String operator;

		public int getShare_reason() {
			return share_reason;
		}

		public void setShare_reason(int share_reason) {
			this.share_reason = share_reason;
		}

		public int getShare_action() {
			return share_action;
		}

		public void setShare_action(int share_action) {
			this.share_action = share_action;
		}

		public int getShare_method() {
			return share_method;
		}

		public void setShare_method(int share_method) {
			this.share_method = share_method;
		}

		public List<String> getShare_specs() {
			return share_specs;
		}

		public void setShare_specs(List<String> share_specs) {
			this.share_specs = share_specs;
		}

		public BigDecimal getShare_amount() {
			return share_amount;
		}

		public void setShare_amount(BigDecimal share_amount) {
			this.share_amount = share_amount;
		}

		public String getCreate_date() {
			return create_date;
		}

		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}
	}

	public String getIn_stock_sheet_id() {
		return in_stock_sheet_id;
	}

	public void setIn_stock_sheet_id(String in_stock_sheet_id) {
		this.in_stock_sheet_id = in_stock_sheet_id;
	}

	public String getPurchase_sheet_id() {
		return purchase_sheet_id;
	}

	public void setPurchase_sheet_id(String purchase_sheet_id) {
		this.purchase_sheet_id = purchase_sheet_id;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigDecimal getSku_amount() {
		return sku_amount;
	}

	public void setSku_amount(BigDecimal sku_amount) {
		this.sku_amount = sku_amount;
	}

	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}

	public BigDecimal getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIn_stock_date() {
		return in_stock_date;
	}

	public void setIn_stock_date(String in_stock_date) {
		this.in_stock_date = in_stock_date;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	public List<Share> getShares() {
		return shares;
	}

	public void setShares(List<Share> shares) {
		this.shares = shares;
	}

}
