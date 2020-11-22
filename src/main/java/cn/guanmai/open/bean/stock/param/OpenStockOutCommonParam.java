package cn.guanmai.open.bean.stock.param;

import java.io.Serializable;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:17:06
 * @des TODO
 */

public class OpenStockOutCommonParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2231460292834423197L;
	private String out_stock_sheet_id;
	private String customer_name;

	public List<Detail> details;

	public class Detail implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6066982219694647404L;
		private String sku_id;
		private String out_stock_count;

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getOut_stock_count() {
			return out_stock_count;
		}

		public void setOut_stock_count(String out_stock_count) {
			this.out_stock_count = out_stock_count;
		}

	}

	public String getOut_stock_sheet_id() {
		return out_stock_sheet_id;
	}

	public void setOut_stock_sheet_id(String out_stock_sheet_id) {
		this.out_stock_sheet_id = out_stock_sheet_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
