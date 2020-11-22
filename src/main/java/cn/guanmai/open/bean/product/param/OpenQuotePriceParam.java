package cn.guanmai.open.bean.product.param;

/**
 * @author: liming
 * @Date: 2020年6月3日 上午11:28:29
 * @description:
 * @version: 1.0
 */

public class OpenQuotePriceParam {
	private String spec_id;
	private String settle_supplier_id;
	private String purchase_price;
	private String remark;
	private String origin_place;

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(String purchase_price) {
		this.purchase_price = purchase_price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrigin_place() {
		return origin_place;
	}

	public void setOrigin_place(String origin_place) {
		this.origin_place = origin_place;
	}

}
