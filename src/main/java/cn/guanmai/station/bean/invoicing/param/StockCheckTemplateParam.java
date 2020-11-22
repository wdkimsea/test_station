package cn.guanmai.station.bean.invoicing.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年12月30日
 * @time 上午10:06:11
 * @des 先进先出站点 /stock/check/template 接口对应参数
 */

public class StockCheckTemplateParam {
	private String begin_time;
	private String end_time;
	private List<String> category_id_1;
	private List<String> category_id_2;
	private List<String> pinlei_ids;

	private Integer export_type;

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public List<String> getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(List<String> category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public List<String> getCategory_id_2() {
		return category_id_2;
	}

	public void setCategory_id_2(List<String> category_id_2) {
		this.category_id_2 = category_id_2;
	}

	public List<String> getPinlei_ids() {
		return pinlei_ids;
	}

	public void setPinlei_ids(List<String> pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

	public Integer getExport_type() {
		return export_type;
	}

	public void setExport_type(Integer export_type) {
		this.export_type = export_type;
	}

}
