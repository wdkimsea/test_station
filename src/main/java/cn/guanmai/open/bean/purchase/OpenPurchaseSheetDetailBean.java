package cn.guanmai.open.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午10:03:05
 * @des TODO
 */

public class OpenPurchaseSheetDetailBean {
	private String create_time;
	private String purchase_sheet_id;
	private String station_id;
	private String station_name;
	private int status;
	private String submit_time;
	private String supplier_id;
	private String supplier_name;
	private String operator;
	private String purchaser_id;
	private String purchaser_name;
	private String remark;

	private List<Detail> details;

	public class Detail {
		private String detail_id;
		private String spec_id;
		private String spec_name;
		private String category1_name;
		private String category2_name;
		private String pinlei_name;
		private String purchase_unit_name;
		private String purchase_std_unit_name;
		private BigDecimal purchase_price;
		private BigDecimal plan_count;
		private BigDecimal purchase_count;
		private BigDecimal already_purchased_count;
		private BigDecimal sale_ratio;
		private String remark;

		public String getDetail_id() {
			return detail_id;
		}

		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
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

		public String getPurchase_unit_name() {
			return purchase_unit_name;
		}

		public void setPurchase_unit_name(String purchase_unit_name) {
			this.purchase_unit_name = purchase_unit_name;
		}

		public String getPurchase_std_unit_name() {
			return purchase_std_unit_name;
		}

		public void setPurchase_std_unit_name(String purchase_std_unit_name) {
			this.purchase_std_unit_name = purchase_std_unit_name;
		}

		public BigDecimal getPurchase_price() {
			return purchase_price;
		}

		public void setPurchase_price(BigDecimal purchase_price) {
			this.purchase_price = purchase_price;
		}

		public BigDecimal getPlan_count() {
			return plan_count;
		}

		public void setPlan_count(BigDecimal plan_count) {
			this.plan_count = plan_count;
		}

		public BigDecimal getPurchase_count() {
			return purchase_count;
		}

		public void setPurchase_count(BigDecimal purchase_count) {
			this.purchase_count = purchase_count;
		}

		public BigDecimal getAlready_purchased_count() {
			return already_purchased_count;
		}

		public void setAlready_purchased_count(BigDecimal already_purchased_count) {
			this.already_purchased_count = already_purchased_count;
		}

		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getPurchase_sheet_id() {
		return purchase_sheet_id;
	}

	public void setPurchase_sheet_id(String purchase_sheet_id) {
		this.purchase_sheet_id = purchase_sheet_id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
