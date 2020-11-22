package cn.guanmai.station.bean.order.param;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 订单批量导入对应的Excel
 * 
 * @author lm
 *
 */
public class OrderSkuModel2 {
	@ExcelProperty(value = "下单商户ID", index = 0)
	private String address_id;

	@ExcelProperty(value = "下单商户名", index = 1)
	private String address_name;

	@ExcelProperty(value = "下单商品ID", index = 2)
	private String sku_id;

	@ExcelProperty(value = "下单商品名", index = 3)
	private String sku_name;

	@ExcelProperty(value = "下单商品单价", index = 4)
	private double sale_price;

	@ExcelProperty(value = "下单商品数量", index = 5)

	private double quantity;

	@ExcelProperty(value = "下单商品备注", index = 6)
	private String remark;

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
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

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
