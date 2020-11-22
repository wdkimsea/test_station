package cn.guanmai.station.bean.purchase.param;

import java.util.List;

import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Apr 19, 2019 10:16:23 AM 
* @des 建议采购过滤参数
* @version 1.0 
*/
public class SupplyLimitFilterParam {
	private int q_type;
	private String begin_time;
	private String end_time;
	private Object supplier_spec;

	public class SupplierSpec {
		private String supplier_id;
		private String sku_id;

		/**
		 * @return the supplier_id
		 */
		public String getSupplier_id() {
			return supplier_id;
		}

		/**
		 * @param supplier_id
		 *            the supplier_id to set
		 */
		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id
		 *            the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}
	}

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * @param q_type
	 *            the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * @param begin_time
	 *            the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time
	 *            the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the supplier_spec
	 */
	public Object getSupplier_spec() {
		return supplier_spec;
	}

	/**
	 * @param supplier_spec
	 *            the supplier_spec to set
	 */
	public void setSupplier_spec(List<SupplierSpec> supplier_spec) {
		this.supplier_spec = JsonUtil.objectToStr(supplier_spec);
	}

}
