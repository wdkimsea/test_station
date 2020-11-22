package cn.guanmai.open.bean.stock.param;

import java.io.Serializable;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午5:14:16
 * @des TODO
 */

public class OpenStockRefundSheetCommonParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6494465757712273769L;
	/**
	 * 
	 */

	private String supplier_refund_sheet_id;
	private String supplier_id;
	private String refund_date;
	private List<Detail> details;

	public class Detail implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3731558065389797256L;
		/**
		 * 
		 */

		private String spec_id;
		private String refund_amount;
		private String refund_unit_price;
		private String refund_count;
		private String batch_number;

		public String getSpec_id() {
			return spec_id;
		}

		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		public String getRefund_amount() {
			return refund_amount;
		}

		public void setRefund_amount(String refund_amount) {
			this.refund_amount = refund_amount;
		}

		public String getRefund_unit_price() {
			return refund_unit_price;
		}

		public void setRefund_unit_price(String refund_unit_price) {
			this.refund_unit_price = refund_unit_price;
		}

		public String getRefund_count() {
			return refund_count;
		}

		public void setRefund_count(String refund_count) {
			this.refund_count = refund_count;
		}

		public String getBatch_number() {
			return batch_number;
		}

		public void setBatch_number(String batch_number) {
			this.batch_number = batch_number;
		}

	}

	public List<Discount> discounts;

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

	public String getSupplier_refund_sheet_id() {
		return supplier_refund_sheet_id;
	}

	/**
	 * 修改采购退货单填的参数
	 * 
	 * @param supplier_refund_sheet_id
	 */
	public void setSupplier_refund_sheet_id(String supplier_refund_sheet_id) {
		this.supplier_refund_sheet_id = supplier_refund_sheet_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSubmit_date() {
		return refund_date;
	}

	public void setSubmit_date(String submit_date) {
		this.refund_date = submit_date;
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
