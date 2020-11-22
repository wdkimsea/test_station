package cn.guanmai.open.bean.finance.param;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jun 6, 2019 10:52:26 AM 
* @des  /finance/strike 接口对应参数
* @version 1.0 
*/
public class FinanceStrikeParam {
	private String customer_id;
	private String deal_code;
	private String arrival_method_id;
	private String remark;
	private Object order_ids;

	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the deal_code
	 */
	public String getDeal_code() {
		return deal_code;
	}

	/**
	 * @param deal_code the deal_code to set
	 */
	public void setDeal_code(String deal_code) {
		this.deal_code = deal_code;
	}

	/**
	 * @return the arrival_method_id
	 */
	public String getArrival_method_id() {
		return arrival_method_id;
	}

	/**
	 * @param arrival_method_id the arrival_method_id to set
	 */
	public void setArrival_method_id(String arrival_method_id) {
		this.arrival_method_id = arrival_method_id;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the order_ids
	 */
	public Object getOrder_ids() {
		return order_ids;
	}

	/**
	 * @param order_ids the order_ids to set
	 */
	public void setOrder_ids(List<String> order_ids) {
		this.order_ids = JSONArray.parseArray(order_ids.toString());
	}

}
