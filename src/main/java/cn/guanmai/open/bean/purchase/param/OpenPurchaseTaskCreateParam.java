package cn.guanmai.open.bean.purchase.param;

import java.io.Serializable;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午11:02:03
 * @des TODO
 */

public class OpenPurchaseTaskCreateParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5173233992779733659L;
	private String spec_id;
	private String purchase_count;
	private String supplier_id;
	private String purchaser_id;

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getPurchase_count() {
		return purchase_count;
	}

	public void setPurchase_count(String purchase_count) {
		this.purchase_count = purchase_count;
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

}
