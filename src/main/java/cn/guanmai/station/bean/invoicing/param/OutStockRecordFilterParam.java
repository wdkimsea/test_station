package cn.guanmai.station.bean.invoicing.param;

import java.util.List;

/**
 * @author liming
 * @date 2020年1月2日
 * @time 上午10:22:17
 * @des 接口 /stock/out_stock_sku 对应的参数
 */

public class OutStockRecordFilterParam {
	/**
	 * 1=按出库日期,2=按建单日期,3=按运营周期,4=按收货日期
	 */
	private Integer time_type;
	private String begin;
	private String end;
	private String text;
	private List<String> category_id_1;
	private List<String> category_id_2;
	/**
	 * 当time_type=3时,才传的参数
	 */
	private String time_config_id;
	private int offset;
	private int limit = 20;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
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

}
