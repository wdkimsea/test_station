package cn.guanmai.station.interfaces.jingcai;

import java.util.List;

import cn.guanmai.station.bean.jingcai.ProcessOrderBean;
import cn.guanmai.station.bean.jingcai.param.ProcessOrderFilterParam;

/**
 * @author: liming
 * @Date: 2020年5月18日 下午2:34:57
 * @description:
 * @version: 1.0
 */

public interface ProcessOrderService {
	/**
	 * 搜索过滤加工单据
	 * 
	 * @param processOrderFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ProcessOrderBean> searchProcessOrder(ProcessOrderFilterParam processOrderFilterParam) throws Exception;
}
