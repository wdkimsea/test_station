package cn.guanmai.station.bean.purchase.param;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Nov 27, 2018 7:38:32 PM 
* @des 发布采购任务的参数类
* @version 1.0 
*/
public class ReleasePurchaseTaskParam {
	private JSONArray task_ids;
	private int q_type;
	private String q;
	private String begin_time;
	private String end_time;
	private JSONArray task_suggests;
	private List<String> category1_ids;
	private List<String> category2_ids;
	private List<String> pinlei_ids;

	/**
	 * @return the task_ids
	 */
	public JSONArray getTask_ids() {
		return task_ids;
	}

	/**
	 * 参数样式 [1,2,3]
	 * 
	 * @param task_ids the task_ids to set
	 */
	public void setTask_ids(JSONArray task_ids) {
		this.task_ids = task_ids;
	}

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * 参数 : 1
	 * 
	 * @param q_type the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * 参数样式: 2018-11-27 00:00:00
	 * 
	 * @param begin_time the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * 参数样式: 2018-11-27 00:00:00
	 * 
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the task_suggests
	 */
	public JSONArray getTask_suggests() {
		return task_suggests;
	}

	/**
	 * [{"task_id":1,"suggest_purchase_num":"10.31"}]
	 * 
	 * @param task_suggests the task_suggests to set
	 */
	public void setTask_suggests(JSONArray task_suggests) {
		this.task_suggests = task_suggests;
	}

	public List<String> getCategory1_ids() {
		return category1_ids;
	}

	public void setCategory1_ids(List<String> category1_ids) {
		this.category1_ids = category1_ids;
	}

	public List<String> getCategory2_ids() {
		return category2_ids;
	}

	public void setCategory2_ids(List<String> category2_ids) {
		this.category2_ids = category2_ids;
	}

	public List<String> getPinlei_ids() {
		return pinlei_ids;
	}

	public void setPinlei_ids(List<String> pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

}
