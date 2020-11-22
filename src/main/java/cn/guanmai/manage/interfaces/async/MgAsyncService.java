package cn.guanmai.manage.interfaces.async;

import java.math.BigDecimal;

import cn.guanmai.manage.bean.async.MgAsyncTaskBean;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 上午9:56:54
 * @des MA的异步任务
 */

public interface MgAsyncService {
	/**
	 * 获取异步任务结果
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public MgAsyncTaskBean getAsyncTask(BigDecimal task_id) throws Exception;

	/**
	 * 获取异步任务结果
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public boolean getAsyncTaskResult(BigDecimal task_id,String msg) throws Exception;

}
