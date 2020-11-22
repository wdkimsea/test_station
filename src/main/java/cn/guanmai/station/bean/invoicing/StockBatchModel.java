package cn.guanmai.station.bean.invoicing;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author liming
 * @date 2019年12月30日
 * @time 上午11:34:27
 * @des TODO
 */

public class StockBatchModel {
	@ExcelProperty(value = "批次号", index = 0)
	private String batch_number;

	@ExcelProperty(value = "SPUID", index = 1)
	private String spu_id;

	@ExcelProperty(value = "商品名", index = 2)
	private String spu_name;

	@ExcelProperty(value = "一级分类", index = 3)
	private String category1_name;

	@ExcelProperty(value = "二级分类", index = 4)
	private String category2_name;

	@ExcelProperty(value = "基本单位", index = 5)
	private String std_unit_name;

	@ExcelProperty(value = "抄盘数", index = 6)
	private double old_stock;

	@ExcelProperty(value = "实盘数", index = 7)
	private double new_stock;

	@ExcelProperty(value = "入库单价 (不可修改)", index = 8)
	private String std_unit_price;

	@ExcelProperty(value = "备注", index = 9)
	private String remark;

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
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

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public double getOld_stock() {
		return old_stock;
	}

	public void setOld_stock(double old_stock) {
		this.old_stock = old_stock;
	}

	public double getNew_stock() {
		return new_stock;
	}

	public void setNew_stock(double new_stock) {
		this.new_stock = new_stock;
	}

	public String getStd_unit_price() {
		return std_unit_price;
	}

	public void setStd_unit_price(String std_unit_price) {
		this.std_unit_price = std_unit_price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
