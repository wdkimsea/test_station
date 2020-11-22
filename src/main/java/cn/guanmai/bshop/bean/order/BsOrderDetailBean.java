package cn.guanmai.bshop.bean.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月17日 上午11:14:09
 * @description:
 * @version: 1.0
 */

public class BsOrderDetailBean {
	private BigDecimal total_price;
	private BigDecimal total_pay;
	private BigDecimal to_pay_amount;
	private BigDecimal coupon_amount;

	private List<Detail> details;

	public class Detail {
		private String id;
		private String spu_id;
		private BigDecimal std_sale_price_forsale;
		private BigDecimal purchase_quantity;
		private BigDecimal real_quantity;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getStd_sale_price_forsale() {
			return std_sale_price_forsale;
		}

		public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
			this.std_sale_price_forsale = std_sale_price_forsale;
		}

		public BigDecimal getPurchase_quantity() {
			return purchase_quantity;
		}

		public void setPurchase_quantity(BigDecimal purchase_quantity) {
			this.purchase_quantity = purchase_quantity;
		}

		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}
	}

	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP, 2);
	}

	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	public BigDecimal getTotal_pay() {
		return total_pay.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP, 2);
	}

	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	public BigDecimal getTo_pay_amount() {
		return to_pay_amount.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP, 2);
	}

	public void setTo_pay_amount(BigDecimal to_pay_amount) {
		this.to_pay_amount = to_pay_amount;
	}

	public BigDecimal getCoupon_amount() {
		return coupon_amount.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP, 2);
	}

	public void setCoupon_amount(BigDecimal coupon_amount) {
		this.coupon_amount = coupon_amount;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
