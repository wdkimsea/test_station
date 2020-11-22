package cn.guanmai.station.interfaces.category;

import java.util.List;

import cn.guanmai.station.bean.category.SmartMenuBean;
import cn.guanmai.station.bean.category.SmartMenuDetailBean;
import cn.guanmai.station.bean.category.param.SmartMenuParam;

/**
 * @author: liming
 * @Date: 2020年3月2日 下午6:08:54
 * @description: 智能菜单相关功能
 * @version: 1.0
 */

public interface SmartMenuService {
	/**
	 * 新建智能菜单
	 * 
	 * @param smartMenuParam
	 * @return
	 * @throws Exception
	 */
	public String createSmartMenu(SmartMenuParam smartMenuParam) throws Exception;

	/**
	 * 修改智能菜单
	 * 
	 * @param smartMenuParam
	 * @return
	 * @throws Exception
	 */
	public boolean editSmartMenu(SmartMenuParam smartMenuParam) throws Exception;

	/**
	 * 获取智能菜单详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SmartMenuDetailBean getSmartMenuDetail(String id) throws Exception;

	/**
	 * 搜索过滤智能菜单
	 * 
	 * @param search_text
	 * @param limit
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public List<SmartMenuBean> searchSmartMenu(String search_text, int limit, int offset) throws Exception;

	/**
	 * 删除智能菜单
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSmartMenu(String id) throws Exception;

	/**
	 * 打印智能菜单
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SmartMenuDetailBean printSmartMenu(String id) throws Exception;
}
