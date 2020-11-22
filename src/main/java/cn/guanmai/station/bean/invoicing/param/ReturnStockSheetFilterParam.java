package cn.guanmai.station.bean.invoicing.param;

/* 
* @author liming 
* @date Feb 27, 2019 4:39:20 PM 
* @des 成品退货搜索过滤参数封装类
* @version 1.0 
*/
public class ReturnStockSheetFilterParam {
	private Integer type;
	private Integer status;
	private String start;
	private String end;
	private String search_text;
	private Integer offset;
	private Integer limit;
	private Integer export;

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 1:按退货日期,2:按建单日期
	 * 
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
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
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return the export
	 */
	public Integer getExport() {
		return export;
	}

	/**
	 * 导出成品退货单才设置的参数
	 * 
	 * @param export the export to set
	 */
	public void setExport() {
		this.export = 1;
	}

}
