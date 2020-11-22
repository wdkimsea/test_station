package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:17:09
 * @description:
 * @version: 1.0
 */

public class CouponBean {
	private String id;
	private String name;
	private String category_id_1;
	private String create_time;

	private int audience_type;
	private int collect_limit;
	private int give_out_num;
	private int is_active;
	private int type;
	private int used_num;
	private int validity_day;

	private BigDecimal min_total_price;
	private BigDecimal price_value;

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

	public String getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public int getGive_out_num() {
		return give_out_num;
	}

	public void setGive_out_num(int give_out_num) {
		this.give_out_num = give_out_num;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUsed_num() {
		return used_num;
	}

	public void setUsed_num(int used_num) {
		this.used_num = used_num;
	}

	public int getValidity_day() {
		return validity_day;
	}

	public void setValidity_day(int validity_day) {
		this.validity_day = validity_day;
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
