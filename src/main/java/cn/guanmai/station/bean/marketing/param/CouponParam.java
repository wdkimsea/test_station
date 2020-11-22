package cn.guanmai.station.bean.marketing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:59:01
 * @description:
 * @version: 1.0
 */

public class CouponParam {
	private String name;
	private String description;
	private int type;
	private BigDecimal min_total_price;
	private BigDecimal max_discount_percent;
	private BigDecimal price_value;
	private int audience_type;
	private int validity_day;
	private int is_active;
	private int collect_limit;
	private List<BigDecimal> address_label_ids;
	private List<BigDecimal> kids;

	private String register_after;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getMin_total_price() {
		return min_total_price;
	}

	public void setMin_total_price(BigDecimal min_total_price) {
		this.min_total_price = min_total_price;
	}

	public BigDecimal getMax_discount_percent() {
		return max_discount_percent;
	}

	public void setMax_discount_percent(BigDecimal max_discount_percent) {
		this.max_discount_percent = max_discount_percent;
	}

	public BigDecimal getPrice_value() {
		return price_value;
	}

	public void setPrice_value(BigDecimal price_value) {
		this.price_value = price_value;
	}

	public int getAudience_type() {
		return audience_type;
	}

	public void setAudience_type(int audience_type) {
		this.audience_type = audience_type;
	}

	public int getValidity_day() {
		return validity_day;
	}

	public void setValidity_day(int validity_day) {
		this.validity_day = validity_day;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public int getCollect_limit() {
		return collect_limit;
	}

	public void setCollect_limit(int collect_limit) {
		this.collect_limit = collect_limit;
	}

	public List<BigDecimal> getAddress_label_ids() {
		return address_label_ids;
	}

	public void setAddress_label_ids(List<BigDecimal> address_label_ids) {
		this.address_label_ids = address_label_ids;
	}

	public List<BigDecimal> getKids() {
		return kids;
	}

	public void setKids(List<BigDecimal> kids) {
		this.kids = kids;
	}

	public String getRegister_after() {
		return register_after;
	}

	public void setRegister_after(String register_after) {
		this.register_after = register_after;
	}

}
