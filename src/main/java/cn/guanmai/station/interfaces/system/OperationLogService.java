package cn.guanmai.station.interfaces.system;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.system.OperationLogBean;
import cn.guanmai.station.bean.system.OperationLogDetailBean;
import cn.guanmai.station.bean.system.param.OperationLogFilterParam;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午2:38:17
 * @description:
 * @version: 1.0
 */

public interface OperationLogService {
	/**
	 * 搜索过滤操作日志
	 * 
	 * @param operationLogFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OperationLogBean> searchOperationLog(OperationLogFilterParam operationLogFilterParam) throws Exception;

	/**
	 * 获取操作日志详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OperationLogDetailBean getOperationLogDetail(String id) throws Exception;

	/**
	 * 操作日志导出
	 * 
	 * @param operationLogFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportOperationLog(OperationLogFilterParam operationLogFilterParam) throws Exception;
}
