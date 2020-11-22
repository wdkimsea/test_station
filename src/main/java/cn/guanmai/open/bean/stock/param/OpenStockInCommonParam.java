package cn.guanmai.open.bean.stock.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午5:53:29
 * @des TODO
 */

public class OpenStockInCommonParam {
	private String in_stock_sheet_id;
	private String supplier_id;
	private String in_stock_date;

	private List<Detail> details;

	public class Detail {
		private String spec_id;
		private String in_stock_count;
		private String in_stock_unit_price;
		private String in_stock_amount;
		private String batch_number;
		private String remark;

		public String getSpec_id() {
			return spec_id;
		}

		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		public String getIn_stock_count() {
			return in_stock_count;
		}

		public void setIn_stock_count(String in_stock_count) {
			this.in_stock_count = in_stock_count;
		}

		public String getIn_stock_unit_price() {
			return in_stock_unit_price;
		}

		public void setIn_stock_unit_price(String in_stock_unit_price) {
			this.in_stock_unit_price = in_stock_unit_price;
		}

		public String getIn_stock_amount() {
			return in_stock_amount;
		}

		public void setIn_stock_amount(String in_stock_amount) {
			this.in_stock_amount = in_stock_amount;
		}

		public String getBatch_number() {
			return batch_number;
		}

		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	private List<Discount> discounts;

	public class Discount {
		private String discount_reason;
		private String discount_action;
		private String discount_amount;
		private String remark;

		public String getDiscount_reason() {
			return discount_reason;
		}

		public void setDiscount_reason(String discount_reason) {
			this.discount_reason = discount_reason;
		}

		public String getDiscount_action() {
			return discount_action;
		}

		public void setDiscount_action(String discount_action) {
			this.discount_action = discount_action;
		}

		public String getDiscount_amount() {
			return discount_amount;
		}

		public void setDiscount_amount(String discount_amount) {
			this.discount_amount = discount_amount;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	public String getIn_stock_sheet_id() {
		return in_stock_sheet_id;
	}

	public void setIn_stock_sheet_id(String in_stock_sheet_id) {
		this.in_stock_sheet_id = in_stock_sheet_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSubmit_date() {
		return in_stock_date;
	}

	public void setSubmit_date(String submit_date) {
		this.in_stock_date = submit_date;
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

}
