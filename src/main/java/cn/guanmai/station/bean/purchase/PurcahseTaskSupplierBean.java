package cn.guanmai.station.bean.purchase;

/**
 * @author liming
 * @date 2019年10月12日
 * @time 上午11:43:07
 * @des 接口 /purchase/task/suppliers 对应的参数
 */

public class PurcahseTaskSupplierBean {
	private String default_purchaser_id;
	private String default_purchaser_name;
	private String settle_supplier_id;
	private String supplier_name;

	public String getDefault_purchaser_id() {
		return default_purchaser_id;
	}

	public void setDefault_purchaser_id(String default_purchaser_id) {
		this.default_purchaser_id = default_purchaser_id;
	}

	public String getDefault_purchaser_name() {
		return default_purchaser_name;
	}

	public void setDefault_purchaser_name(String default_purchaser_name) {
		this.default_purchaser_name = default_purchaser_name;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
