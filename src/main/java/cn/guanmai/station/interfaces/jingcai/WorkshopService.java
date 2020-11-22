package cn.guanmai.station.interfaces.jingcai;

import java.util.List;

import cn.guanmai.station.bean.jingcai.WorkshopBean;
import cn.guanmai.station.bean.jingcai.param.WorkshopFilterParam;
import cn.guanmai.station.bean.jingcai.param.WorkshopParam;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:30:45
 * @description:
 * @version: 1.0
 */

public interface WorkshopService {

	/**
	 * 新建车间
	 * 
	 * @param workshopCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createWorkshop(WorkshopParam workshopCreateParam) throws Exception;

	/**
	 * 修改车间
	 * 
	 * @param workshopUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateWorkshop(WorkshopParam workshopUpdateParam) throws Exception;

	/**
	 * 删除车间
	 * 
	 * @param workshop_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteWorkshop(String workshop_id) throws Exception;

	/**
	 * 搜索过滤车间
	 * 
	 * @param workshopFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<WorkshopBean> searchWorkshop(WorkshopFilterParam workshopFilterParam) throws Exception;

	/**
	 * 获取车间信息
	 * 
	 * @param workshop_id
	 * @return
	 * @throws Exception
	 */
	public WorkshopBean getWorkshop(String workshop_id) throws Exception;

}
