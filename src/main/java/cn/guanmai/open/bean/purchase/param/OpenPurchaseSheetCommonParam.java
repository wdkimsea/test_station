package cn.guanmai.open.bean.purchase.param;

import java.io.Serializable;
import java.util.List;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午10:17:18
 * @des TODO
 */

public class OpenPurchaseSheetCommonParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7732556210039245106L;
	private String purchase_sheet_id;
	private String supplier_id;
	private String purchaser_id;
	private String purcahser_name;
	private String remark;
	private List<Detail> details;

	public class Detail implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6729046501388439793L;
		private String detail_id;
		private String spec_id;
		private String purchase_price;
		private String purchase_count;
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

		public String getPurchase_price() {
			return purchase_price;
		}

		public void setPurchase_price(String purchase_price) {
			this.purchase_price = purchase_price;
		}

		public String getPurchase_count() {
			return purchase_count;
		}

		public void setPurchase_count(String purchase_count) {
			this.purchase_count = purchase_count;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

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

	public String getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	public String getPurcahser_name() {
		return purcahser_name;
	}

	/**
	 * 非接口参数,只是用来方便验证结果的
	 * 
	 * @param purcahser_name
	 */
	public void setPurcahser_name(String purcahser_name) {
		this.purcahser_name = purcahser_name;
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
