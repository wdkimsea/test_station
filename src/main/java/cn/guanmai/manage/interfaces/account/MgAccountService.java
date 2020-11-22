package cn.guanmai.manage.interfaces.account;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.manage.bean.account.MgPermissionBean;
import cn.guanmai.manage.bean.account.MgRoleBean;
import cn.guanmai.manage.bean.account.MgUserBean;
import cn.guanmai.manage.bean.account.StRoleBean;
import cn.guanmai.manage.bean.account.StUserBean;
import cn.guanmai.manage.bean.account.StationInfoBean;
import cn.guanmai.manage.bean.account.param.MgRoleCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserFilterParam;
import cn.guanmai.manage.bean.account.param.StRoleCreateParam;
import cn.guanmai.manage.bean.account.param.StUserCreateParam;
import cn.guanmai.manage.bean.account.param.StUserFilterParam;

/**
 * @author liming
 * @date 2019年8月22日
 * @time 下午7:42:51
 * @des MA 用户管理相关业务
 */

public interface MgAccountService {

	/**
	 * 获取此Group下所有的ST站点ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllStationIds() throws Exception;

	/**
	 * 获取此Group下的所有站点
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<StationInfoBean> getAllStations() throws Exception;

	/**
	 * 查询ST平台的角色
	 * 
	 * @param station_id
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<StRoleBean> searchStationRoles(String station_id, String search_text) throws Exception;

	/**
	 * 获取ST平台的所有的权限ID
	 * 
	 * @param station_id
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getStationPermissions(String station_id) throws Exception;

	/**
	 * 新建ST角色
	 * 
	 * @param stationRoleCreateParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createStationRole(StRoleCreateParam stationRoleCreateParam) throws Exception;

	/**
	 * 搜索ST用户
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<StUserBean> searchStationUser(StUserFilterParam filterParam) throws Exception;

	/**
	 * 创建ST用户
	 * 
	 * @param createParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createStationUser(StUserCreateParam createParam) throws Exception;

	/**
	 * 删除ST角色
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteStationRole(BigDecimal id) throws Exception;

	/**
	 * 删除ST用户
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteStationUser(BigDecimal id) throws Exception;

	/**
	 * 修改ST账号密码
	 * 
	 * @param user_id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean changeStationUserPassword(BigDecimal user_id, String password) throws Exception;

	/**
	 * 搜索过滤manage的角色
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<MgRoleBean> searchManageRole(String search_text) throws Exception;

	/**
	 * 获取manage相关权限
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<MgPermissionBean> getManagePermissions() throws Exception;

	/**
	 * 新建manage角色
	 * 
	 * @param mgRoleCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createManageRole(MgRoleCreateParam mgRoleCreateParam) throws Exception;

	/**
	 * 搜索过滤MA用户
	 * 
	 * @param mgUserFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<MgUserBean> searchManagerUser(MgUserFilterParam mgUserFilterParam) throws Exception;

	/**
	 * 创建manage用户
	 * 
	 * @param mgUserCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createManageUser(MgUserCreateParam mgUserCreateParam) throws Exception;

}
