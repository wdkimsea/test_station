package cn.guanmai.open.bean.purchase;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 下午7:51:25
 * @des TODO
 */

public class OpenPurcahserBean {
	private BigDecimal purchaser_id;
	private String purchaser_name;

	public BigDecimal getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(BigDecimal purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	public String getPurchaser_name() {
		return purchaser_name;
	}

	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

}
