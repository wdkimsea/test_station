package cn.guanmai.open.bean.stock;

import java.util.List;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午4:52:30
 * @des TODO
 */

public class OpenStockRefundSheetDetailBean {
	private String supplier_refund_sheet_id;
	private String supplier_id;
	private String supplier_name;
	private String status;
	private String sku_amount;
	private String discount_amount;
	private String total_amount;
	private String station_id;
	private String create_date;
	private String refund_date;
	private String creator;
	private List<Detail> details;
	private List<Discount> discounts;

	public class Detail {
		private String spu_id;
		private String spec_id;
		private String spec_name;
		private String category_name;
		private String refund_amount;
		private String refund_count;
		private String refund_unit_price;
		private String std_unit_name;
		private String batch_number;

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

		public String getRefund_amount() {
			return refund_amount;
		}

		public void setRefund_amount(String refund_amount) {
			this.refund_amount = refund_amount;
		}

		public String getRefund_count() {
			return refund_count;
		}

		public void setRefund_count(String refund_count) {
			this.refund_count = refund_count;
		}

		public String getRefund_unit_price() {
			return refund_unit_price;
		}

		public void setRefund_unit_price(String refund_unit_price) {
			this.refund_unit_price = refund_unit_price;
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

	}

	public class Discount {
		private String discount_reason;
		private String discount_action;
		private String discount_money;
		private String create_date;
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
			return discount_money;
		}

		public void setDiscount_amount(String discount_money) {
			this.discount_money = discount_money;
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

	}

	public String getSupplier_refund_sheet_id() {
		return supplier_refund_sheet_id;
	}

	public void setSupplier_refund_sheet_id(String supplier_refund_sheet_id) {
		this.supplier_refund_sheet_id = supplier_refund_sheet_id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSku_amount() {
		return sku_amount;
	}

	public void setSku_amount(String sku_amount) {
		this.sku_amount = sku_amount;
	}

	public String getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(String discount_amount) {
		this.discount_amount = discount_amount;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
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

	public String getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
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

}
