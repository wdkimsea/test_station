package cn.guanmai.manage.bean.async;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 上午10:09:48
 * @des TODO
 */

public class MgAsyncTaskBean {
	private int status;
	private Integer progress;
	private int type;
	private Resutl result;
	private BigDecimal task_id;
	private String task_name;
	private String user_task_id;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the progress
	 */
	public Integer getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the result
	 */
	public Resutl getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Resutl result) {
		this.result = result;
	}

	/**
	 * @return the task_id
	 */
	public BigDecimal getTask_id() {
		return task_id;
	}

	/**
	 * @param task_id the task_id to set
	 */
	public void setTask_id(BigDecimal task_id) {
		this.task_id = task_id;
	}

	/**
	 * @return the task_name
	 */
	public String getTask_name() {
		return task_name;
	}

	/**
	 * @param task_name the task_name to set
	 */
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	/**
	 * @return the user_task_id
	 */
	public String getUser_task_id() {
		return user_task_id;
	}

	/**
	 * @param user_task_id the user_task_id to set
	 */
	public void setUser_task_id(String user_task_id) {
		this.user_task_id = user_task_id;
	}

	public MgAsyncTaskBean(int status, Integer progress, int type, Resutl result, BigDecimal task_id, String task_name,
			String user_task_id) {
		super();
		this.status = status;
		this.progress = progress;
		this.type = type;
		this.result = result;
		this.task_id = task_id;
		this.task_name = task_name;
		this.user_task_id = user_task_id;
	}

	public class Resutl {
		private String link;
		private String msg;
		private String target;

		/**
		 * @return the link
		 */
		public String getLink() {
			return link;
		}

		/**
		 * @param link the link to set
		 */
		public void setLink(String link) {
			this.link = link;
		}

		/**
		 * @return the msg
		 */
		public String getMsg() {
			return msg;
		}

		/**
		 * @param msg the msg to set
		 */
		public void setMsg(String msg) {
			this.msg = msg;
		}

		/**
		 * @return the target
		 */
		public String getTarget() {
			return target;
		}

		/**
		 * @param target the target to set
		 */
		public void setTarget(String target) {
			this.target = target;
		}

	}
}
