package cn.guanmai.open.bean.stock;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午10:43:48
 * @des TODO
 */

public class OpenStockSaleRefundBean {
	private String order_id; // "PL10689462", 订单id
	private String address_id;// "S369085", 商户id
	private String resname; // minmin123",商户名称
	private String spu_id; // C3986595",
	private String sku_id; // D22189593",
	private BigDecimal request_count; // 请求退货数
	private BigDecimal request_refund_amount; // 15.59,请求退货金额
	private BigDecimal real_count; // 1.9,实际退货数
	private BigDecimal real_refund_amount;// 10,实际退货金额
	private String std_unit_name; // 斤,销售基本单位
	private int state;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getResname() {
		return resname;
	}

	public void setResname(String resname) {
		this.resname = resname;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public BigDecimal getRequest_count() {
		return request_count;
	}

	public void setRequest_count(BigDecimal request_count) {
		this.request_count = request_count;
	}

	public BigDecimal getRequest_refund_amount() {
		return request_refund_amount;
	}

	public void setRequest_refund_amount(BigDecimal request_refund_amount) {
		this.request_refund_amount = request_refund_amount;
	}

	public BigDecimal getReal_count() {
		return real_count;
	}

	public void setReal_count(BigDecimal real_count) {
		this.real_count = real_count;
	}

	public BigDecimal getReal_refund_amount() {
		return real_refund_amount;
	}

	public void setReal_refund_amount(BigDecimal real_refund_amount) {
		this.real_refund_amount = real_refund_amount;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
