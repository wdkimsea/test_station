package cn.guanmai.station.bean.purchase;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 28, 2018 5:56:22 PM 
* @des 采购单据详情类
* @version 1.0 
*/
public class PurchaseSheetDetailBean {
	private String create_time;
	private String settle_supplier_id;
	private String status;
	@JSONField(name="tasks")
	private List<Task> tasks;

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the settle_supplier_id
	 */
	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	/**
	 * @param settle_supplier_id
	 *            the settle_supplier_id to set
	 */
	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public class Task {
		private String release_id;
		private String spec_id;
		private String spec_name;

		/**
		 * @return the release_id
		 */
		public String getRelease_id() {
			return release_id;
		}

		/**
		 * @param release_id
		 *            the release_id to set
		 */
		public void setRelease_id(String release_id) {
			this.release_id = release_id;
		}

		/**
		 * @return the spec_id
		 */
		public String getSpec_id() {
			return spec_id;
		}

		/**
		 * @param spec_id
		 *            the spec_id to set
		 */
		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		/**
		 * @return the spec_name
		 */
		public String getSpec_name() {
			return spec_name;
		}

		/**
		 * @param spec_name
		 *            the spec_name to set
		 */
		public void setSpec_name(String spec_name) {
			this.spec_name = spec_name;
		}

	}

}
