package cn.guanmai.station.bean.invoicing.param;

import java.util.List;

/**
 * @author liming
 * @date 2020年1月14日
 * @time 下午4:45:53
 * @des 接口 /stock/out_stock_summary_by_spu/list
 *      /stock/in_stock_summary_by_spu/list 对应的参数
 * 
 */

public class StockSummaryFilterParam {
	private String begin;
	private String end;
	private String q;
	private Integer limit = 50;
	private Integer offset = 0;
	private String page_obj;

	private String supplier; // 入库统计才有的参数

	private String restaurant_id; // 出库统计才有的参数

	private List<String> category_id_1;

	private List<String> category_id_2;

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

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * 入库统计才有的参数
	 * 
	 * @return
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * 入库统计才有的参数
	 * 
	 * @param supplier
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * 出库才有的参数,商户ID
	 * 
	 * @return
	 */
	public String getRestaurant_id() {
		return restaurant_id;
	}

	/**
	 * 出库才有的参数,商户ID
	 * 
	 * @param restaurant_id
	 */
	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
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

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
