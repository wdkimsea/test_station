package cn.guanmai.station.bean.async;

/* 
* @author liming 
* @date Jan 2, 2019 2:51:46 PM 
* @des 异步任务类
* @version 1.0 
*/
public class AsyncTaskBean {
	private double async;
	private String link;
	private String task_url;

	/**
	 * @return the async
	 */
	public double getAsync() {
		return async;
	}

	/**
	 * @param async
	 *            the async to set
	 */
	public void setAsync(double async) {
		this.async = async;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the task_url
	 */
	public String getTask_url() {
		return task_url;
	}

	/**
	 * @param task_url
	 *            the task_url to set
	 */
	public void setTask_url(String task_url) {
		this.task_url = task_url;
	}

	public AsyncTaskBean(double async, String link, String task_url) {
		super();
		this.async = async;
		this.link = link;
		this.task_url = task_url;
	}

}
