package cn.guanmai.station.bean.invoicing.param;

/* 
* @author liming 
* @date Feb 28, 2019 10:32:35 AM 
* @des 商品盘点过滤参数封装类
* @version 1.0 
*/
public class StockCheckFilterParam {
	private String category_id_1;
	private String category_id_2;
	private String text;
	private int offset;
	private int limit;
	private Integer remain_status; // 库存状态值
	private Integer export; // 商品盘点导出时带的参数

	/**
	 * @return the category_id_1
	 */
	public String getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * 商品盘点导出才带的参数 值为 1
	 * 
	 * @param export the export to set
	 */
	public void setExport(Integer export) {
		this.export = export;
	}

	/**
	 * @return the remain_status
	 */
	public Integer getRemain_status() {
		return remain_status;
	}

	/**
	 * 1 库存大于0;2 库存等于0;3库存小于0;
	 * 
	 * @param remain_status the remain_status to set
	 */
	public void setRemain_status(Integer remain_status) {
		this.remain_status = remain_status;
	}

}
