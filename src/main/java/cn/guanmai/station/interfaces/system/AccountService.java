package cn.guanmai.station.interfaces.system;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.account.RoleBean;
import cn.guanmai.station.bean.account.RoleDetailBean;
import cn.guanmai.station.bean.account.UserBean;
import cn.guanmai.station.bean.account.UserDetailBean;
import cn.guanmai.station.bean.account.param.UserAddParam;
import cn.guanmai.station.bean.account.param.UserFilterParam;
import cn.guanmai.station.bean.account.param.UserUpdataParam;

/* 
* @author liming 
* @date Apr 23, 2019 11:05:40 AM 
* @des 用户管理
* @version 1.0 
*/
public interface AccountService {
	/**
	 * 获取全部权限列表
	 * 
	 * @param station_id
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getAllPermissions(String station_id) throws Exception;

	/**
	 * 查询角色
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<RoleBean> searchRole(String station_id, String search_text) throws Exception;

	/**
	 * 新增角色
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BigDecimal addRole(RoleDetailBean param) throws Exception;

	/**
	 * 获取指定ID角色的详细信息
	 * 
	 * @param id
	 * @returnRoleDetailBean
	 * @throws Exception
	 */
	public RoleDetailBean getRoleDetail(BigDecimal id) throws Exception;

	/**
	 * 修改角色详细信息
	 * 
	 * @param roleDetail
	 * @return
	 * @throws Exception
	 */
	public boolean updateRole(RoleDetailBean roleDetail) throws Exception;

	/**
	 * 删除角色信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRole(BigDecimal id) throws Exception;

	/**
	 * 搜索过滤用户信息
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<UserBean> searchUser(UserFilterParam filterParam) throws Exception;

	/**
	 * 获取用户详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public UserDetailBean getUserDetail(Integer id) throws Exception;

	/**
	 * 新建用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer addUser(UserAddParam param) throws Exception;

	/**
	 * 更新用户详细信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean updateUser(UserUpdataParam param) throws Exception;

	/**
	 * 修改 
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean updateUserPwd(int id, String password) throws Exception;

	/**
	 * 删除用户信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUser(Integer id) throws Exception;

}
