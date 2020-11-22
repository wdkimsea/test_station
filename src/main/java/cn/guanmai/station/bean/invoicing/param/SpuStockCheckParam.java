package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月24日 上午11:32:17
 * @des 加权平均站点批量盘点对应的参数 /station/stock/check/batch
 * @version 1.0
 */
public class SpuStockCheckParam {
	private String spu_id;
	private BigDecimal new_stock;
	private String remark;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the new_stock
	 */
	public BigDecimal getNew_stock() {
		return new_stock;
	}

	/**
	 * @param new_stock the new_stock to set
	 */
	public void setNew_stock(BigDecimal new_stock) {
		this.new_stock = new_stock;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
