package cn.guanmai.station.interfaces.jingcai;

import java.util.List;

import cn.guanmai.station.bean.jingcai.ProcessTaskBean;
import cn.guanmai.station.bean.jingcai.param.ProcessTaskFilterParam;

/**
 * @author: liming
 * @Date: 2020年5月12日 下午7:36:03
 * @description:
 * @version: 1.0
 */

public interface ProcessTaskService {
	/**
	 * 等待净菜的加工计划生成
	 * 
	 * @param processTaskFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ProcessTaskBean> waitingProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception;

	/**
	 * 搜索过滤净菜的加工计划
	 * 
	 * @param processTaskFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ProcessTaskBean> searchProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception;

	/**
	 * 发布净菜加工计划
	 * 
	 * @param processTaskFilterParam
	 * @return
	 * @throws Exception
	 */
	public boolean releaseProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception;

}
