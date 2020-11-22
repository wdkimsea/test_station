package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年2月4日 上午10:54:41
 * @description: 应付总账 对应结果
 * @version: 1.0
 */

public class SettlementCollectBean {
	// 期初未支付
	private BigDecimal early_unpay_sum;

	// 本期已付
	private BigDecimal cur_pay_sum;

	// 期末未支付
	private BigDecimal total_unpay_sum;

	// 本期应付金额
	private BigDecimal cur_should_pay_sum;

	public BigDecimal getEarly_unpay_sum() {
		return early_unpay_sum;
	}

	public void setEarly_unpay_sum(BigDecimal early_unpay_sum) {
		this.early_unpay_sum = early_unpay_sum;
	}

	public BigDecimal getCur_pay_sum() {
		return cur_pay_sum;
	}

	public void setCur_pay_sum(BigDecimal cur_pay_sum) {
		this.cur_pay_sum = cur_pay_sum;
	}

	public BigDecimal getTotal_unpay_sum() {
		return total_unpay_sum;
	}

	public void setTotal_unpay_sum(BigDecimal total_unpay_sum) {
		this.total_unpay_sum = total_unpay_sum;
	}

	public BigDecimal getCur_should_pay_sum() {
		return cur_should_pay_sum;
	}

	public void setCur_should_pay_sum(BigDecimal cur_should_pay_sum) {
		this.cur_should_pay_sum = cur_should_pay_sum;
	}

}
