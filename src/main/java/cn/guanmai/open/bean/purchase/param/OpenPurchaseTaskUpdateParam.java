package cn.guanmai.open.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午11:04:39
 * @des TODO
 */

public class OpenPurchaseTaskUpdateParam {
	private String task_id;
	private String supplier_id;
	private String purchaser_id;

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
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
