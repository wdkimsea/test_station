package cn.guanmai.open.bean.product;

import java.util.List;
import java.util.Map;

/**
 * @author liming
 * @date 2019年7月2日 下午3:23:42
 * @des 接口 /product/receive_time/get/1.0 对应的结果
 * @version 1.0
 */
public class OpenReceiveTimeBean {
	private String time_config_id;
	private String time_config_name;
	private Map<String, List<String>> receive_time;

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id
	 *            the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the time_config_name
	 */
	public String getTime_config_name() {
		return time_config_name;
	}

	/**
	 * @param time_config_name
	 *            the time_config_name to set
	 */
	public void setTime_config_name(String time_config_name) {
		this.time_config_name = time_config_name;
	}

	/**
	 * @return the receive_time
	 */
	public Map<String, List<String>> getReceive_time() {
		return receive_time;
	}

	/**
	 * @param receive_time
	 *            the receive_time to set
	 */
	public void setReceive_time(Map<String, List<String>> receive_time) {
		this.receive_time = receive_time;
	}

}
