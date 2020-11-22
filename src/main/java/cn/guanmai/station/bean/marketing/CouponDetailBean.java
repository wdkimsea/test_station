package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:17:09
 * @description:
 * @version: 1.0
 */

public class CouponDetailBean {
	private String id;
	private String name;
	private String category_id_1;
	private String description;

	private int audience_type;
	private int collect_limit;
	private int is_active;
	private int type;
	private int validity_day;

	private BigDecimal max_discount_percent;
	private BigDecimal min_total_price;
	private BigDecimal price_value;

	private List<BigDecimal> kids;
	private List<BigDecimal> toc_user_label_dict;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getValidity_day() {
		return validity_day;
	}

	public void setValidity_day(int validity_day) {
		this.validity_day = validity_day;
	}

	public BigDecimal getMax_discount_percent() {
		return max_discount_percent;
	}

	public void setMax_discount_percent(BigDecimal max_discount_percent) {
		this.max_discount_percent = max_discount_percent;
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

	public List<BigDecimal> getKids() {
		return kids;
	}

	public void setKids(List<BigDecimal> kids) {
		this.kids = kids;
	}

	public List<BigDecimal> getToc_user_label_dict() {
		return toc_user_label_dict;
	}

	public void setToc_user_label_dict(List<BigDecimal> toc_user_label_dict) {
		this.toc_user_label_dict = toc_user_label_dict;
	}
}
