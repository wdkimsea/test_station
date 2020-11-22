package cn.guanmai.station.bean.invoicing.param;

/* 
* @author liming 
* @date Feb 25, 2019 7:12:51 PM 
* @des 成品入库单搜索过滤参数封装类
* @version 1.0 
*/
public class InStockSheetFilterParam {
	private int type;
	private Integer search_type;
	private int status;
	private String start;
	private String start_date_new; // 新版UI
	private String end;
	private String end_date_new;// 新版UI
	private String search_text;
	private int offset = 0;
	private int limit = 50;
	// 导出成品入库单的时候才需要添加的参数
	private Integer export;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public Integer getSearch_type() {
		return search_type;
	}

	public void setSearch_type(Integer search_type) {
		this.search_type = search_type;
	}

	/**
	 * 1: 按入库日期,2: 按建单日期
	 * 
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 5: 全部单据, 0:审核不通过,1:待提交,2:已提交待审核,3:审核通过待结款,4:已结款,-1: 已删除
	 * 
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart_date_new() {
		return start_date_new;
	}

	public void setStart_date_new(String start_date_new) {
		this.start_date_new = start_date_new;
	}

	public String getEnd_date_new() {
		return end_date_new;
	}

	public void setEnd_date_new(String end_date_new) {
		this.end_date_new = end_date_new;
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

	/**
	 * @return the export
	 */
	public Integer getExport() {
		return export;
	}

	/**
	 * 导出成品入库单才需要加的参数
	 * 
	 * @param export the export to set
	 */
	public void setExport() {
		this.export = 1;
	}

}
