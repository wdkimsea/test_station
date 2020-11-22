package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2020年1月6日
 * @time 下午5:50:21
 * @des 接口 /stock/abandon_goods/log/list 对应的结果
 */

public class StockAbandonGoodsRecordBean {
	private String category_id_1;
	private String category_id_2;
	private String create_time;
	private String order_id;
	private BigDecimal request_amount;
	private BigDecimal request_refund_money;
	private String spu_id;
	private String spu_name;
	private String std_unit;

	public String getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public String getCategory_id_2() {
		return category_id_2;
	}

	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public BigDecimal getRequest_amount() {
		return request_amount;
	}

	public void setRequest_amount(BigDecimal request_amount) {
		this.request_amount = request_amount;
	}

	public BigDecimal getRequest_refund_money() {
		return request_refund_money;
	}

	public void setRequest_refund_money(BigDecimal request_refund_money) {
		this.request_refund_money = request_refund_money;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public String getStd_unit() {
		return std_unit;
	}

	public void setStd_unit(String std_unit) {
		this.std_unit = std_unit;
	}

}
