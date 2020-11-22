package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2020年1月9日
 * @time 上午10:21:16
 * @des 接口 /station/order/batch_price_sync_to_sku/result 对应的结果
 */

public class OrderPriceSyncToSkuResultBean {
	private String address_id;
	private String resname;
	private String id;
	private String name;
	private String order_id;
	private String reason;
	private int reason_type;
	private BigDecimal sale_price;
	private String sale_unit_name;
	private String salemenu_id;
	private String std_unit_name;
	private String std_unit_name_forsale;
	private BigDecimal std_sale_price;
	private BigDecimal std_sale_price_forsale;

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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getReason_type() {
		return reason_type;
	}

	public void setReason_type(int reason_type) {
		this.reason_type = reason_type;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getStd_unit_name_forsale() {
		return std_unit_name_forsale;
	}

	public void setStd_unit_name_forsale(String std_unit_name_forsale) {
		this.std_unit_name_forsale = std_unit_name_forsale;
	}

	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	public BigDecimal getStd_sale_price_forsale() {
		return std_sale_price_forsale;
	}

	public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
		this.std_sale_price_forsale = std_sale_price_forsale;
	}

}
