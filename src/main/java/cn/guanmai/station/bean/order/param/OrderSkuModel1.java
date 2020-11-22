package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author: liming
 * @Date: 2020年7月7日 下午8:10:41
 * @description: 订单单个导入对应的Excel
 * @version: 1.0
 */

public class OrderSkuModel1 {
	@ExcelProperty(value = "商品ID")
	private String id;

	@ExcelProperty(value = "分类一")
	private String category1_name;

	@ExcelProperty(value = "分类二")
	private String category2_name;

	@ExcelProperty(value = "报价单简称（对外）")
	private String salemenu_name;

	@ExcelProperty(value = "商品名")
	private String sku_name;

	@ExcelProperty(value = "描述")
	private String des;

	@ExcelProperty(value = "基础单位")
	private String std_unit_name;

	@ExcelProperty(value = "规格")
	private String specification;

	@ExcelProperty(value = "单价（销售单位）")
	private BigDecimal sale_price;

	@ExcelProperty(value = "下单数")
	private BigDecimal quantity;

	@ExcelProperty(value = "下单单位")
	private String std_unit_name_forsale;

	@ExcelProperty(value = "备注")
	private String remark;

	@ExcelProperty(value = "自定义编码")
	private String outer_id;

	@ExcelProperty(value = "预下单数")
	private BigDecimal fake_quantity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory1_name() {
		return category1_name;
	}

	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	public String getCategory2_name() {
		return category2_name;
	}

	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	public String getStd_unit_name_forsale() {
		return std_unit_name_forsale;
	}

	public void setStd_unit_name_forsale(String std_unit_name_forsale) {
		this.std_unit_name_forsale = std_unit_name_forsale;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getFake_quantity() {
		return fake_quantity;
	}

	public void setFake_quantity(BigDecimal fake_quantity) {
		this.fake_quantity = fake_quantity;
	}

}
