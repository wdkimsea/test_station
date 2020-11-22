package cn.guanmai.station.bean.jingcai.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年5月12日 下午7:40:51
 * @description:
 * @version: 1.0
 */

public class ProcessTaskFilterParam {
	private String time_config_id;
	private String begin_time;
	private String end_time;
	private String q;
	private String page_obj;

	private Integer status;
	private int q_type;
	private int limit = 20;
	private int offset;

	private Integer all;
	private Integer is_submit;

	private BigDecimal route_id;
	private List<BigDecimal> driver_ids;
	private List<BigDecimal> address_labels;
	private List<BigDecimal> process_labels;

	// 发布净菜加工任务对应的参数
	private List<Release> release_list;

	public class Release {
		private List<BigDecimal> ids;
		private BigDecimal amount;

		public List<BigDecimal> getIds() {
			return ids;
		}

		public void setIds(List<BigDecimal> ids) {
			this.ids = ids;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getQ_type() {
		return q_type;
	}

	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(Integer is_submit) {
		this.is_submit = is_submit;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

	public BigDecimal getRoute_id() {
		return route_id;
	}

	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	public List<BigDecimal> getDriver_ids() {
		return driver_ids;
	}

	public void setDriver_ids(List<BigDecimal> driver_ids) {
		this.driver_ids = driver_ids;
	}

	public List<BigDecimal> getAddress_labels() {
		return address_labels;
	}

	public void setAddress_labels(List<BigDecimal> address_labels) {
		this.address_labels = address_labels;
	}

	public List<BigDecimal> getProcess_labels() {
		return process_labels;
	}

	public void setProcess_labels(List<BigDecimal> process_labels) {
		this.process_labels = process_labels;
	}

	public List<Release> getRelease_list() {
		return release_list;
	}

	public void setRelease_list(List<Release> release_list) {
		this.release_list = release_list;
	}

}
