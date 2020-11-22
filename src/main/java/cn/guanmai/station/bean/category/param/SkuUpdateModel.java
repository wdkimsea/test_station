package cn.guanmai.station.bean.category.param;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author: liming
 * @Date: 2020年3月17日 上午10:51:29
 * @description: SKU 批量修改对应的参数
 * @version: 1.0
 */

public class SkuUpdateModel {
	@ExcelProperty(index = 0, value = "SPUID")
	private String spu_id;

	@ExcelProperty(index = 1, value = "商品ID(SKUID)")
	private String sku_id;

	@ExcelProperty(index = 2, value = "商品名(可修改)")
	private String sku_name;

	@ExcelProperty(index = 3, value = "商品描述(可修改)")
	private String desc;

	@ExcelProperty(index = 4, value = "一级分类")
	private String category1_name;

	@ExcelProperty(index = 5, value = "二级分类")
	private String category2_name;

	@ExcelProperty(index = 6, value = "单价(可修改)")
	private double unit_price;

	@ExcelProperty(index = 7, value = "是否时价(可修改，1时价，0非时价)")
	private int is_price_timing;

	@ExcelProperty(index = 8, value = "基础单位")
	private String std_unit_name;

	@ExcelProperty(index = 9, value = "销售规格")
	private String sale_unit_name;

	@ExcelProperty(index = 10, value = "损耗率(直接输入损耗比例，自动转化为百分数，如输入11.23表示损耗比例为11.23%)")
	private double attrition_rate;

	@ExcelProperty(index = 11, value = "销售价")
	private String sale_price;

	@ExcelProperty(index = 12, value = "最小下单数(可修改)")
	private double sale_num_least;

	@ExcelProperty(index = 13, value = "销售状态(可修改，1上架，0下架)")
	private int state;

	@ExcelProperty(index = 14, value = "是否称重(可修改，1称重，0不称)")
	private int is_weigh;

	@ExcelProperty(index = 15, value = "供应商编码(可修改)")
	private String supplier_customer_id;

	@ExcelProperty(index = 16, value = "供应商")
	private String supplier_name;

	@ExcelProperty(index = 17, value = "参考价")
	private double reference_price;

	@ExcelProperty(index = 18, value = "排序")
	private Integer sort;

	@ExcelProperty(index = 19, value = "自定义编码")
	private String outer_id;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public double getAttrition_rate() {
		return attrition_rate;
	}

	public void setAttrition_rate(double attrition_rate) {
		this.attrition_rate = attrition_rate;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
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

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public double getReference_price() {
		return reference_price;
	}

	public void setReference_price(double reference_price) {
		this.reference_price = reference_price;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

}
