package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月9日 上午11:55:12
 * @description: 订单批量删除商品异步任务失败结果 /station/order/batch_delete_skus_result
 * @version: 1.0
 */

public class OrderDeleteSkuResultBean {
	private String address_id;
	private String err_msg;
	private String order_id;
	private String resname;
	private String salemenu_id;
	private String salemenu_name;
	private String sku_id;
	private String sku_name;
	private BigDecimal std_sale_price;
	private BigDecimal std_sale_price_forsale;
	private String std_unit_name;
	private String std_unit_name_forsale;

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getResname() {
		return resname;
	}

	public void setResname(String resname) {
		this.resname = resname;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public BigDecimal getStd_sale_price() {
		return std_sale_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	public BigDecimal getStd_sale_price_forsale() {
		return std_sale_price_forsale.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
		this.std_sale_price_forsale = std_sale_price_forsale;
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

}
