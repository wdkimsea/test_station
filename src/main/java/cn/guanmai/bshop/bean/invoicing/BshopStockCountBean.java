package cn.guanmai.bshop.bean.invoicing;

import java.math.BigDecimal;

/**
 * 
 * @author Administrator
 * @des 商城 接口 /stock/spu/count 对应的结果
 *
 */
public class BshopStockCountBean {
	private BigDecimal total_stock_value;
	private int spu_count;

	public BigDecimal getTotal_stock_value() {
		return total_stock_value;
	}

	public void setTotal_stock_value(BigDecimal total_stock_value) {
		this.total_stock_value = total_stock_value;
	}

	public int getSpu_count() {
		return spu_count;
	}

	public void setSpu_count(int spu_count) {
		this.spu_count = spu_count;
	}

}
