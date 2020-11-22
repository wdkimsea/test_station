package cn.guanmai.station.interfaces.async;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.async.AsyncTaskResultBean;

/* 
* @author liming 
* @date Jan 2, 2019 4:37:16 PM 
* @des 异步任务相关接口
* @version 1.0 
*/
public interface AsyncService {
	/**
	 * 异步任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AsyncTaskResultBean> getAsyncTaskResultList() throws Exception;

	/**
	 * 获取指定异步任务信息
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public AsyncTaskResultBean getAsyncTaskResult(BigDecimal task_id) throws Exception;

	/**
	 * 获取异步任务执行结果
	 * 
	 * @param task_id
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public boolean getAsyncTaskResult(BigDecimal task_id, String msg) throws Exception;

	/**
	 * 
	 * @param user_task_id
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public boolean getAsyncTaskResult(String user_task_id, String msg) throws Exception;

}
