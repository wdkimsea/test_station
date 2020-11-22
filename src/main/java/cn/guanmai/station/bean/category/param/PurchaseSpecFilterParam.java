package cn.guanmai.station.bean.category.param;

import java.util.List;

/* 
* @author liming 
* @date Nov 7, 2018 11:21:12 AM 
* @des 采购规格过滤条件类
* @version 1.0 
*/
public class PurchaseSpecFilterParam {
	private List<String> category_id_1;
	private List<String> category_id_2;
	private List<String> pinlei_id;
	private String search_text;
	private int offset;
	private int limit;
	private Integer export;

	public PurchaseSpecFilterParam() {
		super();
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

	public List<String> getPinlei_id() {
		return pinlei_id;
	}

	public void setPinlei_id(List<String> pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

}
