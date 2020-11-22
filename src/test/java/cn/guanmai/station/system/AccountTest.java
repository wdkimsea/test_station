package cn.guanmai.station.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.account.RoleBean;
import cn.guanmai.station.bean.account.RoleDetailBean;
import cn.guanmai.station.bean.account.UserBean;
import cn.guanmai.station.bean.account.UserDetailBean;
import cn.guanmai.station.bean.account.UserDetailBean.Role;
import cn.guanmai.station.bean.account.param.UserAddParam;
import cn.guanmai.station.bean.account.param.UserFilterParam;
import cn.guanmai.station.bean.account.param.UserUpdataParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.system.AccountServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Apr 23, 2019 11:45:27 AM 
* @des 用户管理测试
* @version 1.0 
*/
public class AccountTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(AreaTest.class);
	private AccountService accountService;
	private LoginUserInfoService loginUserInfoService;
	private String station_id;
	private List<Integer> station_permissions;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		accountService = new AccountServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();

			boolean is_admin_user = loginUserInfo.isIs_staff();
			Assert.assertEquals(is_admin_user, true, "登录账户非管理员账户,无法进行用户管理操作");

			station_permissions = accountService.getAllPermissions(station_id);
			Assert.assertNotEquals(station_permissions, null, "获取站点全部权限失败");

			station_permissions = station_permissions.stream().sorted().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase01() {
		Reporter.log("测试点: 获取角色列表,并获取角色详细信息");
		try {
			List<RoleBean> roles = accountService.searchRole(null, null);
			Assert.assertNotEquals(roles, "获取角色列表失败");

			BigDecimal id = roles.get(0).getId();
			RoleDetailBean roleDetail = accountService.getRoleDetail(id);
			Assert.assertNotEquals(roleDetail, null, "获取权限详细信息失败");

		} catch (Exception e) {
			logger.error("查询角色信息遇到错误: ", e);
			Assert.fail("查询角色信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase02() {
		Reporter.log("测试点: 新建角色");
		BigDecimal id = null;
		try {
			String name = StringUtil.getRandomString(6).toUpperCase();
			RoleDetailBean roleDetail = new RoleDetailBean(name, "自动化新建的角色", station_id, station_permissions);
			id = accountService.addRole(roleDetail);
			Assert.assertNotEquals(id, null, "新建角色失败");

			List<RoleBean> roles = accountService.searchRole(null, name);
			Assert.assertNotEquals(roles, null, "角色列表搜索查询失败");

			RoleBean role = roles.stream().filter(r -> r.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(role, null, "新建角色 " + name + " 在角色列表没有搜索到");

			roleDetail = accountService.getRoleDetail(id);
			Assert.assertNotEquals(role, null, "获取指定ID " + id + "的角色相信信息失败");
			List<Integer> actual_permissions = roleDetail.getPermission_ids().stream().sorted()
					.collect(Collectors.toList());

			Assert.assertEquals(actual_permissions, station_permissions, "新建的角色名称绑定的角色列表和新建传入的角色列表不一致");

		} catch (Exception e) {
			logger.error("新建角色遇到错误: ", e);
			Assert.fail("新建角色遇到错误: ", e);
		} finally {
			if (id != null) {
				try {
					boolean result = accountService.deleteRole(id);
					Assert.assertEquals(result, true, "删除角色失败");
				} catch (Exception e) {
					logger.error("删除角色遇到错误: ", e);
					Assert.fail("删除角色遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void accountTestCase03() {
		Reporter.log("测试点: 修改角色信息");
		try {
			List<RoleBean> roles = accountService.searchRole(null, null);
			Assert.assertNotEquals(roles, "获取角色列表失败");

			BigDecimal id = roles.get(0).getId();
			RoleDetailBean roleDetail = accountService.getRoleDetail(id);
			Assert.assertNotEquals(roleDetail, null, "获取权限详细信息失败");

			boolean result = accountService.updateRole(roleDetail);
			Assert.assertEquals(result, true, "修改角色信息失败");
		} catch (Exception e) {
			logger.error("修改角色信息遇到错误: ", e);
			Assert.fail("修改角色信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase04() {
		Reporter.log("测试点: 搜索过滤角色");
		try {
			List<RoleBean> roles = accountService.searchRole(station_id, null);
			Assert.assertNotEquals(roles, "获取角色列表失败");
			Assert.assertEquals(roles.size() > 0, true, "角色列表存在角色");
		} catch (Exception e) {
			logger.error("搜索角色信息遇到错误: ", e);
			Assert.fail("搜索角色信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase05() {
		Reporter.log("测试点: 获取用户列表并查看用户详细信息");
		try {
			List<UserBean> users = accountService.searchUser(new UserFilterParam());
			Assert.assertNotEquals(users, null, "搜索过滤用户操作失败");

			Assert.assertEquals(users.size() > 0, true, "用户列表大小应该大于0");

			Integer id = users.get(0).getId();
			UserDetailBean userDetail = accountService.getUserDetail(id);
			Assert.assertNotEquals(userDetail, null, "获取用户详细信息失败");
		} catch (Exception e) {
			logger.error("搜索用户信息遇到错误: ", e);
			Assert.fail("搜索用户信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase06() {
		Reporter.log("测试点: 新建用户");
		Integer user_id = null;
		try {
			List<RoleBean> roles = accountService.searchRole(null, null);
			Assert.assertNotEquals(roles, null, "获取角色列表信息失败");

			Assert.assertEquals(roles.size() > 0, true, "角色列表大小应该大于0");

			BigDecimal role_id = roles.get(0).getId();

			UserAddParam param = new UserAddParam();
			String username = StringUtil.getRandomString(8).toUpperCase();
			String password = "Test1234_";
			param.setUsername(username);
			param.setPassword(password);
			param.setStation_id(station_id);
			param.setIs_admin(false);
			param.setIs_valid(1);
			param.setType_id(0);

			List<BigDecimal> role_ids = Arrays.asList(role_id);
			param.setRole_ids(role_ids);

			user_id = accountService.addUser(param);
			Assert.assertNotEquals(user_id, null, "新增用户失败");

			UserFilterParam filterParam = new UserFilterParam();
			filterParam.setSearch_text(username);
			List<UserBean> users = accountService.searchUser(filterParam);
			Assert.assertNotEquals(users, null, "用户列表过滤失败");

			UserBean user = users.stream().filter(u -> u.getUsername().equals(username)).findAny().orElse(null);
			Assert.assertNotEquals(user, null, "新建的用户在用户列表没有查询到");

			UserDetailBean userDetail = accountService.getUserDetail(user_id);
			Assert.assertNotEquals(userDetail, null, "获取用户详细信息失败");

			boolean result = true;
			List<BigDecimal> actual_role_ids = new ArrayList<>();
			for (Role role : userDetail.getRoles()) {
				actual_role_ids.add(role.getId());
			}

			if (!actual_role_ids.equals(role_ids)) {
				Reporter.log("新建用户绑定的角色和新建完成查询到绑定的的角色不一致,预期," + role_ids + ",实际:" + actual_role_ids);
				result = false;
			}

			Assert.assertEquals(result, true, "新建用户传的信息和新建完成查询到的信息不一致");

			Map<String, String> cookie = LoginUtil.loginStation(username, password);
			Assert.assertNotEquals(cookie, null, "新建的用户 " + username + "登录Station失败");
		} catch (Exception e) {
			logger.error("新建用户信息遇到错误: ", e);
			Assert.fail("新建用户信息遇到错误: ", e);
		} finally {
			if (user_id != null) {
				try {
					boolean result = accountService.deleteUser(user_id);
					Assert.assertEquals(result, true, "删除用户失败");
				} catch (Exception e) {
					logger.error("删除用户信息遇到错误: ", e);
					Assert.fail("删除用户信息遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void accountTestCase07() {
		Reporter.log("测试点: 修改用户信息");
		try {
			List<UserBean> users = accountService.searchUser(new UserFilterParam());
			Assert.assertNotEquals(users, null, "用户列表过滤失败");

			Assert.assertEquals(users.size() > 0, true, "用户列表应该存在至少一个用户");

			Integer user_id = users.get(0).getId();

			UserDetailBean userDetail = accountService.getUserDetail(user_id);
			Assert.assertNotEquals(userDetail, null, "获取商户详细信息失败");

			UserUpdataParam updateParam = new UserUpdataParam();
			updateParam.setId(userDetail.getId());
			updateParam.setUsername(userDetail.getUsername());
			updateParam.setType_id(userDetail.getType_id());
			updateParam.setStation_id(userDetail.getStation_id());
			updateParam.setIs_valid(userDetail.isIs_valid() == true ? 1 : 0);
			updateParam.setIs_admin(userDetail.isIs_admin());
			updateParam.setName(userDetail.getName());
			updateParam.setEmail("station@guanmai.cn");

			List<BigDecimal> role_ids = userDetail.getRoles().stream().map(Role::getId).collect(Collectors.toList());
			updateParam.setRole_ids(role_ids);

			boolean result = accountService.updateUser(updateParam);
			Assert.assertEquals(result, true, "修改用户信息失败");
		} catch (Exception e) {
			logger.error("修改用户信息遇到错误: ", e);
			Assert.fail("修改用户信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase08() {
		Reporter.log("测试点: 用户搜索过滤,过滤状态为有效的用户");
		try {
			UserFilterParam filterParam = new UserFilterParam();
			filterParam.setIs_valid(1);

			List<UserBean> users = accountService.searchUser(filterParam);
			Assert.assertNotEquals(users, null, "搜索过滤用户失败");

			List<UserBean> otherUsers = users.stream().filter(u -> u.isIs_valid() == false)
					.collect(Collectors.toList());
			Assert.assertEquals(otherUsers.size(), 0, "过滤搜索状态为有效的用户,把无效的用户也给过滤出来了");
		} catch (Exception e) {
			logger.error("搜索用户信息遇到错误: ", e);
			Assert.fail("搜索用户信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase09() {
		Reporter.log("测试点: 用户搜索过滤,按角色过滤");
		try {

			List<RoleBean> roles = accountService.searchRole(null, null);
			Assert.assertNotEquals(roles, null, "获取角色列表失败");

			String msg = null;
			boolean result = true;
			UserFilterParam filterParam = null;
			for (RoleBean role : roles) {
				BigDecimal role_id = role.getId();
				filterParam = new UserFilterParam();
				filterParam.setRole_id(role_id);

				List<UserBean> users = accountService.searchUser(filterParam);
				Assert.assertNotEquals(users, null, "搜索过滤用户失败");

				for (UserBean user : users) {
					List<UserBean.Role> temp_roles = user.getRoles();
					List<BigDecimal> temp_role_ids = temp_roles.stream().map(r -> r.getId())
							.collect(Collectors.toList());
					if (!temp_role_ids.contains(role_id)) {
						msg = String.format("过滤角色 %s 的用户,把非此角色的用户 %s 也给过滤出来了", role.getName(), user.getUsername());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "过滤角色,搜索结果与预期不符");
		} catch (Exception e) {
			logger.error("搜索用户信息遇到错误: ", e);
			Assert.fail("搜索用户信息遇到错误: ", e);
		}
	}

	@Test
	public void accountTestCase10() {
		Reporter.log("测试点: 按用户类型过滤用户");
		try {
			List<Integer> type_ids = Arrays.asList(0, 1, 999);
			UserFilterParam filterParam = null;
			for (Integer type_id : type_ids) {
				filterParam = new UserFilterParam();
				filterParam.setType_id(type_id);

				List<UserBean> users = accountService.searchUser(filterParam);
				Assert.assertNotEquals(users, null, "搜索过滤用户失败");

				List<UserBean> otherUsers = users.stream().filter(u -> u.getType_id() - type_id != 0)
						.collect(Collectors.toList());
				Assert.assertEquals(otherUsers.size(), 0, "过滤搜索用户类型 " + type_id + " 的用户,把非此类型的用户也给过滤出来了");
			}
		} catch (Exception e) {
			logger.error("搜索用户信息遇到错误: ", e);
			Assert.fail("搜索用户信息遇到错误: ", e);
		}
	}
}
