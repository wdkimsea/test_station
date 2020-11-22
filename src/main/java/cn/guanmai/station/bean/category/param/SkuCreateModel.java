package cn.guanmai.station.bean.category.param;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author: liming
 * @Date: 2020年3月17日 上午10:51:29
 * @description: SKU 批量新建对应的参数
 * @version: 1.0
 */

public class SkuCreateModel {
	@ExcelProperty(index = 0, value = "SPUID")
	private String spu_id;

	@ExcelProperty(index = 1, value = "自定义编码")
	private String outer_id;

	@ExcelProperty(index = 2, value = "商品名(可修改)")
	private String sku_name;

	@ExcelProperty(index = 3, value = "商品描述(可修改)")
	private String desc;

	@ExcelProperty(index = 4, value = "单价(可修改)")
	private double unit_price;

	@ExcelProperty(index = 5, value = "是否时价(可修改，1时价，0非时价)")
	private int is_price_timing;

	@ExcelProperty(index = 6, value = "销售单位")
	private String sale_unit_name;

	@ExcelProperty(index = 7, value = "销售规格(数字)")
	private double sale_ratio;

	@ExcelProperty(index = 8, value = "损耗率(数字)")
	private double attrition_rate;

	@ExcelProperty(index = 9, value = "最小下单数(数字)")
	private double sale_num_least;

	@ExcelProperty(index = 10, value = "销售状态(可修改，1上架，0下架)")
	private int state;

	@ExcelProperty(index = 11, value = "是否称重(可修改，1称重，0不称)")
	private int is_weigh;

	@ExcelProperty(index = 12, value = "默认供应商编码")
	private String supplier_customer_id;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	public int getIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public double getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(double sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public double getAttrition_rate() {
		return attrition_rate;
	}

	public void setAttrition_rate(double attrition_rate) {
		this.attrition_rate = attrition_rate;
	}

	public double getSale_num_least() {
		return sale_num_least;
	}

	public void setSale_num_least(double sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIs_weigh() {
		return is_weigh;
	}

	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	public String getSupplier_customer_id() {
		return supplier_customer_id;
	}

	public void setSupplier_customer_id(String supplier_customer_id) {
		this.supplier_customer_id = supplier_customer_id;
	}

}
