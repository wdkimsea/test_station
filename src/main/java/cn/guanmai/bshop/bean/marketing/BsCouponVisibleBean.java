package cn.guanmai.bshop.bean.marketing;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.IntTypeAdapter;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午4:41:50
 * @description: 接口 /coupon/visible_coupon 对应的结果
 * @version: 1.0
 */

public class BsCouponVisibleBean {
	private String id;
	private String name;
	private String station_id;

	private int audience_type;
	private int collect_limit;
	private int pending_collect_num;

	private BigDecimal min_total_price;
	private BigDecimal price_value;

	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int notify_status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public int getAudience_type() {
		return audience_type;
	}

	public void setAudience_type(int audience_type) {
		this.audience_type = audience_type;
	}

	public int getCollect_limit() {
		return collect_limit;
	}

	public void setCollect_limit(int collect_limit) {
		this.collect_limit = collect_limit;
	}

	public int getPending_collect_num() {
		return pending_collect_num;
	}

	public void setPending_collect_num(int pending_collect_num) {
		this.pending_collect_num = pending_collect_num;
	}

	public int getNotify_status() {
		return notify_status;
	}

	public void setNotify_status(int notify_status) {
		this.notify_status = notify_status;
	}

	public BigDecimal getMin_total_price() {
		return min_total_price;
	}

	public void setMin_total_price(BigDecimal min_total_price) {
		this.min_total_price = min_total_price;
	}

	public BigDecimal getPrice_value() {
		return price_value;
	}

	public void setPrice_value(BigDecimal price_value) {
		this.price_value = price_value;
	}

}
