package cn.guanmai.station.bean.purchase;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年2月14日 下午5:42:39
 * @description:
 * @version: 1.0
 */

public class PurchaseTaskCanChangeSupplierBean {
	@JSONField(name="id")
	private String settle_supplier_id;
	@JSONField(name="name")
	private String settle_supplier_name;

	private List<Purchaser> purchasers;

	public class Purchaser {
		@JSONField(name="id")
		private String purchaser_id;
		@JSONField(name="name")
		private String purchaser_name;

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

	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getSettle_supplier_name() {
		return settle_supplier_name;
	}

	public void setSettle_supplier_name(String settle_supplier_name) {
		this.settle_supplier_name = settle_supplier_name;
	}

	public List<Purchaser> getPurchasers() {
		return purchasers;
	}

	public void setPurchasers(List<Purchaser> purchasers) {
		this.purchasers = purchasers;
	}

}
