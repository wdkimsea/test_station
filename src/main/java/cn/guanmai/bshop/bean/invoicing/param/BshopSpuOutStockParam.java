package cn.guanmai.bshop.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月8日
 * @time 上午11:19:44
 * @des 接口 /stock/spu/out_stock/create 对应的参数
 */

public class BshopSpuOutStockParam {
	private String address_id;
	private List<OutStockDetail> batch_list;

	public class OutStockDetail {
		private String spu_id;
		private BigDecimal std_quantity;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getStd_quantity() {
			return std_quantity;
		}

		public void setStd_quantity(BigDecimal std_quantity) {
			this.std_quantity = std_quantity;
		}
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public List<OutStockDetail> getBatch_list() {
		return batch_list;
	}

	public void setBatch_list(List<OutStockDetail> batch_list) {
		this.batch_list = batch_list;
	}

}
