package cn.guanmai.station.bean.category.param;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author: liming
 * @Date: 2020年6月23日 下午7:34:41
 * @description:
 * @version: 1.0
 */

public class IndexSkuCreateModel {
	@ExcelProperty(index = 0, value = "一级分类名称")
	private String category1_name;

	@ExcelProperty(index = 1, value = "二级分类名称")
	private String category2_name;

	@ExcelProperty(index = 2, value = "SPU名称")
	private String spu_name;

	@ExcelProperty(index = 3, value = "单位")
	private String unit_name;

	@ExcelProperty(index = 4, value = "描述")
	private String desc;

	@ExcelProperty(index = 5, value = "自定义编码")
	private String outer_id;

	@ExcelProperty(index = 6, value = "图片")
	private String pic_name;

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

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	public String getPic_name() {
		return pic_name;
	}

	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}

}
