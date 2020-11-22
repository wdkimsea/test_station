package cn.guanmai.station.bean.invoicing.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年12月31日
 * @time 下午4:02:52
 * @des TODO
 */

public class InStockRecordFilterParam {
	private Integer time_type;
	private String begin;
	private String end;
	private List<String> category_id_1;
	private List<String> category_id_2;
	private String text;
	private int offset;
	private int limit;
	private String settle_supplier_id;

	public Integer getTime_type() {
		return time_type;
	}

	public void setTime_type(Integer time_type) {
		this.time_type = time_type;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

}
