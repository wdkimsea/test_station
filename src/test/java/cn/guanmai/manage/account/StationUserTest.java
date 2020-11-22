package cn.guanmai.manage.account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.account.StRoleBean;
import cn.guanmai.manage.bean.account.param.StRoleCreateParam;
import cn.guanmai.manage.bean.account.param.StUserCreateParam;
import cn.guanmai.manage.impl.account.MgAccountServiceImpl;
import cn.guanmai.manage.interfaces.account.MgAccountService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年8月23日
 * @time 上午10:57:48
 * @des TODO
 */

public class StationUserTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(StationUserTest.class);
	private MgAccountService manageAccountService;

	@BeforeClass
	public void initData() {
		Map<String, String> ma_cookie = getManageCookie();
		manageAccountService = new MgAccountServiceImpl(ma_cookie);
	}

	@Test
	public void StationUserTestCase01() {
		ReporterCSS.title("测试点: MA用户管理,获取业务平台角色列表");
		try {
			List<StRoleBean> stationRoles = manageAccountService.searchStationRoles(null, null);
			Assert.assertNotEquals(stationRoles, null, "MA用户管理,获取业务平台角色列表失败");
		} catch (Exception e) {
			logger.error("MA用户管理,获取业务平台角色列表遇到错误: ", e);
			Assert.fail("MA用户管理,获取业务平台角色列表遇到错误: ", e);
		}
	}

	@Test
	public void StationUserTestCase02() {
		ReporterCSS.title("测试点: MA用户管理,新建业务平台角色");
		BigDecimal role_id = null;
		try {
			List<String> station_ids = manageAccountService.getAllStationIds();
			Assert.assertNotEquals(station_ids, null, "MA用户管理,获取全部站点信息失败");

			String station_id = NumberUtil.roundNumberInList(station_ids);

			List<Integer> permission_ids = manageAccountService.getStationPermissions(station_id);
			Assert.assertNotEquals(permission_ids, null, "MA用户管理,获取站点 " + station_id + " 所有权限失败");

			StRoleCreateParam stationRoleCreateParam = new StRoleCreateParam();
			String role_name = StringUtil.getRandomString(8).toUpperCase();
			stationRoleCreateParam.setName(role_name);
			stationRoleCreateParam.setStation_id(station_id);
			stationRoleCreateParam.setPermission_ids(permission_ids);

			role_id = manageAccountService.createStationRole(stationRoleCreateParam);
			Assert.assertNotEquals(role_id, null, "MA用户管理,新建业务平台角色失败");

			List<StRoleBean> stationRoles = manageAccountService.searchStationRoles(station_id, role_name);
			Assert.assertNotEquals(stationRoles, null, "MA用户管理,获取业务平台角色列表失败");
		} catch (Exception e) {
			logger.error("MA用户管理,新建业务平台角色遇到错误: ", e);
			Assert.fail("MA用户管理,新建业务平台角色遇到错误: ", e);
		} finally {
			if (role_id != null) {
				try {
					boolean result = manageAccountService.deleteStationRole(role_id);
					Assert.assertEquals(result, true, "MA用户管理,删除业务平台角色失败");
				} catch (Exception e) {
					logger.error("MA用户管理,删除业务平台角色遇到错误: ", e);
					Assert.fail("MA用户管理,删除业务平台角色遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void StationUserTestCase03() {
		ReporterCSS.title("测试点: MA用户管理,新建业务平台用户");
		BigDecimal user_id = null;
		try {
			List<String> station_ids = manageAccountService.getAllStationIds();
			Assert.assertNotEquals(station_ids, null, "MA用户管理,获取全部站点信息失败");
			String station_id = NumberUtil.roundNumberInList(station_ids);

			List<StRoleBean> stationRoles = manageAccountService.searchStationRoles(station_id, null);
			Assert.assertNotEquals(stationRoles, null, "MA用户管理,获取业务平台角色列表失败");

			BigDecimal role_id = null;
			if (stationRoles.size() == 0) {
				List<Integer> permission_ids = manageAccountService.getStationPermissions(station_id);
				Assert.assertNotEquals(permission_ids, null, "MA用户管理,获取站点 " + station_id + " 所有权限失败");

				StRoleCreateParam stationRoleCreateParam = new StRoleCreateParam();
				String role_name = "站点管理员";
				stationRoleCreateParam.setName(role_name);
				stationRoleCreateParam.setStation_id(station_id);
				stationRoleCreateParam.setPermission_ids(permission_ids);

				role_id = manageAccountService.createStationRole(stationRoleCreateParam);
				Assert.assertNotEquals(role_id, null, "MA用户管理,新建业务平台角色失败");
			} else {
				role_id = NumberUtil.roundNumberInList(stationRoles).getId();
			}

			StUserCreateParam stationUserCreateParam = new StUserCreateParam();
			stationUserCreateParam.setIs_admin(false);
			stationUserCreateParam.setIs_valid(1);
			String user_name = StringUtil.getRandomString(8).toUpperCase();
			stationUserCreateParam.setName(user_name);
			stationUserCreateParam.setUsername(user_name);
			stationUserCreateParam.setPassword("4rfv5tgb");
			stationUserCreateParam.setRole_ids(Arrays.asList(role_id));
			stationUserCreateParam.setStation_id(station_id);
			stationUserCreateParam.setType_id(0);

			user_id = manageAccountService.createStationUser(stationUserCreateParam);
			Assert.assertNotEquals(user_id, null, "新建业务平台用户失败");
		} catch (Exception e) {
			logger.error("MA用户管理,新建业务平台用户遇到错误: ", e);
			Assert.fail("MA用户管理,新建业务平台用户遇到错误: ", e);
		} finally {
			if (user_id != null) {
				try {
					boolean result = manageAccountService.deleteStationUser(user_id);
					Assert.assertEquals(result, true, "MA用户管理,删除业务平台用户失败");
				} catch (Exception e) {
					logger.error("MA用户管理,删除业务平台用户遇到错误: ", e);
					Assert.fail("MA用户管理,删除业务平台用户遇到错误: ", e);
				}
			}
		}
	}

}
