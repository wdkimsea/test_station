package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Nov 8, 2018 3:08:13 PM 
* @des 报价单测试
* @version 1.0 
*/
public class SalemenuTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SalemenuTest.class);
	private JSONArray permissions;

	private SalemenuService salemenuService;
	private ServiceTimeService serviceTimeService;
	private LoginUserInfoService loginUserInfoService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		salemenuService = new SalemenuServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			permissions = loginUserInfo.getUser_permission();
			Assert.assertNotEquals(permissions, null, "获取站点权限信息失败");
		} catch (Exception e) {
			logger.error("获取站点权限信息失败: ", e);
			Assert.fail("获取站点权限信息失败: ", e);
		}
	}

	/**
	 * 搜索过滤并随机获取具体某个报价单的详细信息
	 * 
	 */
	@Test
	public void salemenuTestCase01() {
		try {
			ReporterCSS.title("测试点: 搜索过滤报价达并随机获取具体某个报价单的详细信息");
			SalemenuFilterParam param = new SalemenuFilterParam("", null, null, "");
			List<SalemenuBean> salemenuArray = salemenuService.searchSalemenu(param);
			if (salemenuArray.size() > 0) {
				String id = NumberUtil.roundNumberInList(salemenuArray).getId();
				SalemenuBean salemenu = salemenuService.getSalemenuById(id);
				Assert.assertNotEquals(salemenu, null, "获取报价单详细信息失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	/**
	 * 创建报价单
	 * 
	 */
	@Test
	public void salemenuTestCase02() {
		try {
			ReporterCSS.title("测试点: 创建名称为<[自动化] 无商户绑定>的报价单,如果存在则进行修改操作");
			SalemenuFilterParam param = new SalemenuFilterParam("", null, null, "");
			List<SalemenuBean> salemenuArray = salemenuService.searchSalemenu(param);
			String name = "[自动化] 无商户绑定";
			String id = null;
			for (SalemenuBean salemenu : salemenuArray) {
				if (salemenu.getName().equals(name)) {
					id = salemenu.getId();
					break;
				}
			}
			// 存在则修改,不存在则新建
			if (id == null) {
				List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
				Assert.assertEquals(serviceTimeList != null && serviceTimeList.size() > 0, true,
						"此站点无运营时间,无法进行创建报价单操作");
				String time_config_id = serviceTimeList.get(0).getId();

				String supplier_name = "自动化";
				String about = "这个是自动化创建的";
				SalemenuBean salemenu = new SalemenuBean(name, time_config_id, 1, new JSONArray(), supplier_name,
						about);
				id = salemenuService.createSalemenu(salemenu);
				Assert.assertEquals(id != null, true, "新建报价单,断言成功");

				SalemenuBean tmp_salemenu = salemenuService.getSalemenuById(id);
				boolean result = tmp_salemenu.getName().equals(salemenu.getName())
						&& tmp_salemenu.getServiceTime().getId().equals(time_config_id)
						&& tmp_salemenu.getIs_active() == 1 && tmp_salemenu.getTargets().size() == 0
						&& tmp_salemenu.getSupplier_name().equals(supplier_name)
						&& tmp_salemenu.getAbout().equals(about);
				Assert.assertEquals(result, true, "创建成功报价单后,验证其基本信息,验证失败");

			} else {
				// 获取报价单信息之后不能直接作为参数传递进行修改,因为状态值和销售属性类型有变化
				SalemenuBean salemenu = salemenuService.getSalemenuById(id);

				// 报价单状态参数的转化
				salemenu.setIs_active(salemenu.getIs_active());

				// 销售对象参数的转化
				List<SalemenuBean.Target> tagets = salemenu.getTargets();
				JSONArray targetArray = new JSONArray();
				for (SalemenuBean.Target taget : tagets) {
					targetArray.add(taget.getId());
				}
				salemenu.setTargets(targetArray);

				String new_about = "新的about";
				salemenu.setAbout(new_about);

				boolean result = salemenuService.updateSalemenu(salemenu);
				Assert.assertEquals(result, true, "修改报价单,断言成功");

				SalemenuBean tmp_salemenu = salemenuService.getSalemenuById(id);
				result = tmp_salemenu.getAbout().equals(new_about);
				Assert.assertEquals(result, true, "创建成功报价单后,验证其基本信息,验证失败");
			}
		} catch (Exception e) {
			logger.error("新建或修改报价单遇到错误: ", e);
			Assert.fail("新建或修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuTestCase03() {
		ReporterCSS.title("测试点: 删除报价单测试");
		Assert.assertEquals(permissions.contains("delete_salemenu"), true, "登录用户没有删除报价单权限");
		try {
			SalemenuFilterParam param = new SalemenuFilterParam("", null, null, "");
			List<SalemenuBean> salemenuArray = salemenuService.searchSalemenu(param);
			String name = "[自动化] 无商户绑定";
			String id = null;
			for (SalemenuBean salemenu : salemenuArray) {
				if (salemenu.getName().equals(name)) {
					id = salemenu.getId();
					break;
				}
			}
			// 存在则修改,不存在则新建
			if (id == null) {
				List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
				Assert.assertEquals(serviceTimeList != null && serviceTimeList.size() > 0, true,
						"此站点无运营时间,无法进行创建报价单操作");
				String time_config_id = serviceTimeList.get(0).getId();

				String supplier_name = "自动化";
				String about = "这个是自动化创建的";
				SalemenuBean salemenu = new SalemenuBean(name, time_config_id, 1, new JSONArray(), supplier_name,
						about);
				id = salemenuService.createSalemenu(salemenu);
				Assert.assertEquals(id != null, true, "新建报价单,断言成功");
			}

			boolean result = salemenuService.deleteSalemenu(id);
			Assert.assertEquals(result, true, "删除报价单失败");
		} catch (Exception e) {
			logger.error("删除报价单操作过程中遇到错误: ", e);
			Assert.fail("删除报价单操作过程中遇到错误: ", e);
		}
	}

	@Test
	public void salemenuTestCase04() {
		ReporterCSS.title("测试点: 报价单分享测试");
		Assert.assertEquals(permissions.contains("get_share_salemenu"), true, "登录用户没有分享报价单权限");
		try {
			SalemenuFilterParam param = new SalemenuFilterParam("", null, null, "");
			param.setWith_sku_num(1);
			List<SalemenuBean> salemenuArray = salemenuService.searchSalemenu(param);
			String salemenu_id = null;
			for (SalemenuBean salemenu : salemenuArray) {
				if (salemenu.getSku_num() > 0 && salemenu.getIs_active() == 1) {
					salemenu_id = salemenu.getId();
					break;
				}
			}
			Assert.assertNotEquals(salemenu_id, null, "没有找到合适的报价单来进行分享操作");

			BigDecimal share_id = salemenuService.createShareSalemenu(salemenu_id);
			Assert.assertNotEquals(share_id, null, "分享报价单" + salemenu_id + "失败");

			Map<String, SkuBean> skuMap = salemenuService.getShareSalemenu(share_id);
			Assert.assertNotEquals(skuMap, null, "获取分享报价单详情信息失败");

		} catch (Exception e) {
			logger.error("分享报价单过程中遇到错误: ", e);
			Assert.fail("分享报价单过程中遇到错误: ", e);
		}
	}

	@Test
	public void salemenuFilterTestCase01() {
		ReporterCSS.title("测试点: 报价单按运营时间搜索过滤");
		try {
			List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
			Assert.assertNotEquals(serviceTimeList, null, "获取站点运营时间列表失败");

			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setType(-1);
			filterParam.setWith_sku_num(1);
			List<SalemenuBean> salemenus = null;
			for (ServiceTimeBean serviceTime : serviceTimeList) {
				String time_config_id = serviceTime.getId();
				String time_config_name = serviceTime.getName();
				filterParam.setTime_config_id(time_config_id);
				salemenus = salemenuService.searchSalemenu(filterParam);
				Assert.assertNotEquals(salemenus, null, "搜索查询报价单失败");

				List<String> time_config_names = salemenus.stream()
						.filter(s -> !s.getTime_config_name().equals(time_config_name))
						.map(s -> s.getTime_config_name()).collect(Collectors.toList());
				Assert.assertEquals(time_config_names.size() == 0, true,
						"按运营时间 " + time_config_name + " 搜索顾虑报价单,过滤出了绑定运营时间 " + time_config_names + " 的报价单");
			}
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuFilterTestCase02() {
		ReporterCSS.title("测试点: 按报价单ID或报价单名称搜索过滤");
		try {

			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setType(-1);
			filterParam.setWith_sku_num(1);
			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			SalemenuBean salemenu = NumberUtil.roundNumberInList(salemenus);
			String id = salemenu.getId();
			String name = salemenu.getName();
			filterParam.setQ(id);

			salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			List<String> salemenu_names = salemenus.stream()
					.filter(s -> !s.getId().equals(id) && !s.getName().contains(id)).map(s -> s.getName())
					.collect(Collectors.toList());
			Assert.assertEquals(salemenu_names.size() == 0, true, "按报价单ID过滤报价单,搜索出了不符合过滤条件的报价单 " + salemenu_names);

			filterParam.setQ(name);
			salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			salemenu_names = salemenus.stream().filter(s -> !s.getId().equals(name) && !s.getName().contains(name))
					.map(s -> s.getName()).collect(Collectors.toList());
			Assert.assertEquals(salemenu_names.size() == 0, true, "按报价单名称过滤报价单,搜索出了不符合过滤条件的报价单 " + salemenu_names);

		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuFilterTestCase03() {
		ReporterCSS.title("测试点: 按报价单类型搜索过滤");
		try {

			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setWith_sku_num(1);
			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			// 2:代售单, 4: 自售单
			List<Integer> types = Arrays.asList(2, 4);
			for (Integer type : types) {
				filterParam.setType(type);
				salemenus = salemenuService.searchSalemenu(filterParam);
				Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");
				List<String> salemenu_names = salemenus.stream().filter(s -> s.getType() != type).map(s -> s.getName())
						.collect(Collectors.toList());
				Assert.assertEquals(salemenu_names.size() == 0, true,
						"按报价单类型 " + type + "过滤报价单,过滤出了不符合过滤条件的报价单" + salemenu_names);
			}
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuFilterTestCase04() {
		ReporterCSS.title("测试点: 按报价单状态搜索过滤");
		try {
			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setWith_sku_num(1);
			filterParam.setType(-1);
			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			// 0:未激活, 1: 已激活
			List<Integer> actives = Arrays.asList(0, 1);
			for (Integer active : actives) {
				filterParam.setIs_active(active);
				salemenus = salemenuService.searchSalemenu(filterParam);
				Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");
				List<String> salemenu_names = salemenus.stream().filter(s -> active == 0).map(s -> s.getName())
						.collect(Collectors.toList());
				Assert.assertEquals(salemenu_names.size() == 0, true,
						"按报价单状态 " + active + "过滤报价单,过滤出了不符合过滤条件的报价单" + salemenu_names);
			}
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}
}
