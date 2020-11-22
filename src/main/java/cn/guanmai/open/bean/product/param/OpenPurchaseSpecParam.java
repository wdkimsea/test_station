package cn.guanmai.open.bean.product.param;

/**
 * @author: liming
 * @Date: 2020年2月7日 下午3:48:20
 * @description:
 * @version: 1.0
 */

public class OpenPurchaseSpecParam {
	private String spec_id;
	private String spu_id;
	private String purchase_unit_name;
	private String purchase_ratio;
	private String spec_name;

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getPurchase_unit_name() {
		return purchase_unit_name;
	}

	public void setPurchase_unit_name(String purchase_unit_name) {
		this.purchase_unit_name = purchase_unit_name;
	}

	public String getPurchase_ratio() {
		return purchase_ratio;
	}

	public void setPurchase_ratio(String purchase_ratio) {
		this.purchase_ratio = purchase_ratio;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

}
