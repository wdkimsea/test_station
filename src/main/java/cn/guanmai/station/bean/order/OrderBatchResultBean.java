package cn.guanmai.station.bean.order;

import java.util.List;

import cn.guanmai.station.bean.system.ServiceTimeBean;

/**
 * @author liming
 * @date 2019年8月13日
 * @time 下午5:29:19
 * @des 接口 /station/order/batch/result 对应的结果
 */

public class OrderBatchResultBean {
	private ServiceTimeBean time_config;

	private List<OrderDetailBean> details;

	private String task_id;

	private String file_name;

	public ServiceTimeBean getTime_config() {
		return time_config;
	}

	public void setTime_config(ServiceTimeBean time_config) {
		this.time_config = time_config;
	}

	public List<OrderDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailBean> details) {
		this.details = details;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

}
