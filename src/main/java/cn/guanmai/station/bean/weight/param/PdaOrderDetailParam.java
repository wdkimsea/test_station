package cn.guanmai.station.bean.weight.param;

/**
 * @author liming
 * @date 2019年7月31日 下午6:24:24
 * @des
 * @version 1.0
 */
public class PdaOrderDetailParam {
	private String order_id;
	private Integer real_is_weight;
	private Integer sort_status;
	private Integer out_of_stock;
	private Integer is_print;
	private String search_text;

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the real_is_weight
	 */
	public Integer getReal_is_weight() {
		return real_is_weight;
	}

	/**
	 * @param real_is_weight
	 *            the real_is_weight to set
	 */
	public void setReal_is_weight(Integer real_is_weight) {
		this.real_is_weight = real_is_weight;
	}

	/**
	 * @return the sort_status
	 */
	public Integer getSort_status() {
		return sort_status;
	}

	/**
	 * @param sort_status
	 *            the sort_status to set
	 */
	public void setSort_status(Integer sort_status) {
		this.sort_status = sort_status;
	}

	/**
	 * @return the out_of_stock
	 */
	public Integer getOut_of_stock() {
		return out_of_stock;
	}

	/**
	 * @param out_of_stock
	 *            the out_of_stock to set
	 */
	public void setOut_of_stock(Integer out_of_stock) {
		this.out_of_stock = out_of_stock;
	}

	/**
	 * @return the is_print
	 */
	public Integer getIs_print() {
		return is_print;
	}

	/**
	 * @param is_print
	 *            the is_print to set
	 */
	public void setIs_print(Integer is_print) {
		this.is_print = is_print;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text
	 *            the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

}
