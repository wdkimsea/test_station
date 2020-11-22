package cn.guanmai.station.delivery;

import cn.guanmai.station.bean.delivery.PickUpStationBean;
import cn.guanmai.station.bean.delivery.param.PickUpStationFilterParam;
import cn.guanmai.station.bean.system.AreaBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.delivery.PickUpStationServiceImpl;
import cn.guanmai.station.impl.system.AreaServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.PickUpStationService;
import cn.guanmai.station.interfaces.system.AreaService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by yangjinhai on 2019/8/26.
 */
public class PickUpStationTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PickUpStationTest.class);
	private PickUpStationService pickUpStationService;
	private AreaService areaService;

	private PickUpStationBean pickUpStation;
	private String id, name;
	private String city_name, district_name, area_name;// 可配送城市,城市名,二级市及地区
	private String city_id, district_id, area_id;// 可配送城市,城市名,二级市及地区 ID
	private String principal, phone, address;// 创建字体点默认元素

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		pickUpStationService = new PickUpStationServiceImpl(headers);
		areaService = new AreaServiceImpl(headers);
		try {
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			JSONArray permissions = loginUserInfo.getUser_permission();

			Assert.assertNotEquals(permissions, null, "获取站点权限信息失败");
			Assert.assertEquals(permissions.contains("get_pick_up_station"), true, "登录用户没有自提点管理权限");
		} catch (Exception e) {
			logger.error("获取登录用户信息遇到错误", e);
			Assert.fail("获取登录用户信息遇到错误", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		id = null;
		name = "自动化创建自提点" + StringUtil.getRandomString(2);
		try {
			List<AreaBean> citys = areaService.getAreaDict();
			List<AreaBean.District> districts = null;
			List<AreaBean.District.Area> areas = null;
			OK: for (AreaBean city : citys) {
				districts = city.getDistricts();
				if (districts == null || districts.size() == 0) {
					continue;
				}
				for (AreaBean.District district : districts) {
					areas = district.getAreas();
					if (areas == null || areas.size() == 0) {
						continue;
					}
					city_name = city.getCity_name();
					city_id = city.getCity_id();
					district_name = district.getDistrict_name();
					district_id = district.getDistrict_id();

					AreaBean.District.Area area = areas.get(0);
					area_name = area.getArea_name();
					area_id = area.getArea_id();
					break OK;
				}

			}
		} catch (Exception e) {
			logger.error("获取地区相关信息遇到错误", e);
			Assert.fail("获取地区相关信息遇到错误", e);
		}
		principal = "自动化";
		phone = "12" + StringUtil.getRandomNumber(9);
		address = "东街" + NumberUtil.getRandomNumber(1, 20, 0) + "号";

		pickUpStation = new PickUpStationBean();
		pickUpStation.setName(name);
		pickUpStation.setDistrict_code(city_id);
		pickUpStation.setArea_l1(district_id);
		pickUpStation.setArea_l2(area_id);
		pickUpStation.setPhone(phone);
		pickUpStation.setPrincipal(principal);
		pickUpStation.setAddress(address);
		pickUpStation.setBusiness_status("1");

	}

	@AfterMethod
	public void destroy() {
		try {
			if (id != null) {
				boolean result = pickUpStationService.deletePickUpStation(id);
				Assert.assertTrue(result, "删除新建的自提点失败");
			}
		} catch (Exception e) {
			logger.error("后置处理,删除新建的自提点遇到错误", e);
			Assert.fail("后置处理,删除新建的自提点遇到错误", e);
		}

	}

	@Test
	public void createPickUpStationTestcase01() throws Exception {
		try {
			ReporterCSS.title("测试点:创建自提点,地理标签只选城市名");
			pickUpStation.setArea_l1(null);
			pickUpStation.setArea_l2(null);

			boolean result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertTrue(result, "创建自提点失败");

			List<PickUpStationBean> pickUpStations = pickUpStationService.queryPickUpStations("", name);
			Assert.assertNotEquals(pickUpStations, null, "查询自提点列表失败");

			PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
					.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);

			Assert.assertNotEquals(tempPickUpStation, null, "没有查询到名称为" + name + " 的自提点");

			id = tempPickUpStation.getId();

			String msg = null;
			if (!tempPickUpStation.getName().equals(pickUpStation.getName())) {
				msg = String.format("新建自提点填写的名称和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getName(),
						tempPickUpStation.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPhone().equals(pickUpStation.getPhone())) {
				msg = String.format("新建自提点填写的电话号码和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getPhone(),
						tempPickUpStation.getPhone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPrincipal().equals(pickUpStation.getPrincipal())) {
				msg = String.format("新建自提点填写的负责人和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getPrincipal(),
						tempPickUpStation.getPrincipal());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getGeographic_label().equals(city_name)) {
				msg = String.format("新建自提点填写的地理标签和新建后查询到的不一致,预期:%s,实际:%s", city_name,
						tempPickUpStation.getGeographic_label());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getAddress().equals(pickUpStation.getAddress())) {
				msg = String.format("新建自提点填写的地理位置和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getAddress(),
						tempPickUpStation.getAddress());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getBusiness_status().equals(pickUpStation.getBusiness_status())) {
				msg = String.format("新建自提点填写的营业状态和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getBusiness_status(),
						tempPickUpStation.getBusiness_status());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建自提点填写的相关信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("创建自提点发生错误", e);
			Assert.fail("创建自提点发生错误", e);
		}
	}

	/**
	 * 创建包含三级城市地理标签的自提点
	 */
	@Test
	public void createPickUpStationTestCase02() throws Exception {
		try {
			ReporterCSS.title("测试点:创建自提点,传入三级城市地理标签");

			boolean result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertTrue(result, "传入三级地理标签创建自提点失败");

			PickUpStationFilterParam filterParam = new PickUpStationFilterParam();
			filterParam.setSearch_text(name);
			List<PickUpStationBean> pickUpStations = pickUpStationService.queryPickUpStations(filterParam);
			PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
					.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);

			Assert.assertNotEquals(tempPickUpStation, null, "没有查询到名称为" + name + " 的自提点");

			id = tempPickUpStation.getId();

			String msg = null;
			if (!tempPickUpStation.getName().equals(pickUpStation.getName())) {
				msg = String.format("新建自提点填写的名称和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getName(),
						tempPickUpStation.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPhone().equals(pickUpStation.getPhone())) {
				msg = String.format("新建自提点填写的电话号码和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getPhone(),
						tempPickUpStation.getPhone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPrincipal().equals(pickUpStation.getPrincipal())) {
				msg = String.format("新建自提点填写的负责人和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getPrincipal(),
						tempPickUpStation.getPrincipal());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_geographic_label = city_name + "-" + district_name + "-" + area_name;
			if (!tempPickUpStation.getGeographic_label().equals(expected_geographic_label)) {
				msg = String.format("新建自提点填写的地理标签和新建后查询到的不一致,预期:%s,实际:%s", expected_geographic_label,
						tempPickUpStation.getGeographic_label());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getAddress().equals(pickUpStation.getAddress())) {
				msg = String.format("新建自提点填写的地理位置和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getAddress(),
						tempPickUpStation.getAddress());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getBusiness_status().equals(pickUpStation.getBusiness_status())) {
				msg = String.format("新建自提点填写的营业状态和新建后查询到的不一致,预期:%s,实际:%s", pickUpStation.getBusiness_status(),
						tempPickUpStation.getBusiness_status());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建自提点填写的相关信息与查询到的信息不一致");

		} catch (Exception e) {
			logger.error("创建自提点(地理标签选择到地区)发生错误", e);
			Assert.fail("创建自提点(地理标签选择到地区)发生错误", e);
		}
	}

	@Test
	public void createPickUpStationTestCase03() {
		try {
			ReporterCSS.log("测试点:新建自提点信息,使用已经存在的名称,断言创建失败");
			boolean result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertTrue(result, "新建自提点失败");

			PickUpStationFilterParam filterParam = new PickUpStationFilterParam();
			filterParam.setSearch_text(name);

			List<PickUpStationBean> pickUpStations = pickUpStationService.queryPickUpStations(filterParam);
			PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
					.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);

			Assert.assertNotEquals(tempPickUpStation, null, "没有查询到名称为" + name + " 的自提点");

			id = tempPickUpStation.getId();

			result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertFalse(result, "新建自提点信息,使用已经存在的名称,断言创建失败");
		} catch (Exception e) {
			logger.error("新建自提点发生错误", e);
			Assert.fail("新建自提点发生错误", e);
		}
	}

	@Test
	public void updatePickUpStationTestCase01() {
		ReporterCSS.title("测试点: 修改自提点信息");
		try {
			boolean result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertTrue(result, "新建自提点失败");

			PickUpStationFilterParam filterParam = new PickUpStationFilterParam();
			filterParam.setSearch_text(name);

			List<PickUpStationBean> pickUpStations = pickUpStationService.queryPickUpStations(filterParam);
			PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
					.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);

			Assert.assertNotEquals(tempPickUpStation, null, "没有查询到名称为" + name + " 的自提点");

			id = tempPickUpStation.getId();

			tempPickUpStation.setName(StringUtil.getRandomString(6));
			tempPickUpStation.setAddress("西街6好");
			tempPickUpStation.setDistrict_code(city_id);
			tempPickUpStation.setArea_l1(null);
			tempPickUpStation.setArea_l2(null);
			tempPickUpStation.setPrincipal("kirs");
			tempPickUpStation.setBusiness_status("2");
			tempPickUpStation.setPhone("12" + StringUtil.getRandomNumber(9));
			result = pickUpStationService.updatePickUpStation(tempPickUpStation);
			Assert.assertEquals(result, true, "修改自提点信息失败");

			PickUpStationBean afterPickUpStation = pickUpStationService.getPickUpStationDetailInfo(id);
			Assert.assertNotEquals(afterPickUpStation, null, "获取ID为 " + id + " 自提点详细信息失败");

			String msg = null;
			if (!tempPickUpStation.getName().equals(afterPickUpStation.getName())) {
				msg = String.format("修改自提点填写的名称和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getName(),
						afterPickUpStation.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPhone().equals(afterPickUpStation.getPhone())) {
				msg = String.format("修改自提点填写的电话号码和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getPhone(),
						afterPickUpStation.getPhone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getPrincipal().equals(afterPickUpStation.getPrincipal())) {
				msg = String.format("修改自提点填写的负责人和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getPrincipal(),
						afterPickUpStation.getPrincipal());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getDistrict_code().equals(afterPickUpStation.getDistrict_code())) {
				msg = String.format("修改自提点填写的地理标签和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getDistrict_code(),
						afterPickUpStation.getDistrict_code());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getAddress().equals(afterPickUpStation.getAddress())) {
				msg = String.format("修改自提点填写的地理位置和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getAddress(),
						afterPickUpStation.getAddress());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPickUpStation.getBusiness_status().equals(afterPickUpStation.getBusiness_status())) {
				msg = String.format("修改自提点填写的营业状态和新建后查询到的不一致,预期:%s,实际:%s", tempPickUpStation.getBusiness_status(),
						afterPickUpStation.getBusiness_status());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "修改自提点填写的相关信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("修改自提点发生错误", e);
			Assert.fail("修改自提点发生错误", e);
		}
	}

	@Test
	public void deletePickUpStationTestCase01() throws Exception {
		try {
			ReporterCSS.log("测试点:删除自提点");
			boolean result = pickUpStationService.createPickUpStation(pickUpStation);
			Assert.assertTrue(result, "新建自提点失败");

			PickUpStationFilterParam filterParam = new PickUpStationFilterParam();
			filterParam.setSearch_text(name);

			List<PickUpStationBean> pickUpStations = pickUpStationService.queryPickUpStations(filterParam);
			PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
					.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);

			Assert.assertNotEquals(tempPickUpStation, null, "没有查询到名称为" + name + " 的自提点");

			String id = tempPickUpStation.getId();
			result = pickUpStationService.deletePickUpStation(id);
			Assert.assertEquals(result, true, "删除自提点失败");

			pickUpStations = pickUpStationService.queryPickUpStations(filterParam);
			tempPickUpStation = pickUpStations.parallelStream().filter(p -> p.getName().equals(pickUpStation.getName()))
					.findAny().orElse(null);
			Assert.assertEquals(tempPickUpStation, null, "名称为" + name + " 的自提点没有真正删除");
		} catch (Exception e) {
			logger.error("删除自提点发生错误", e);
			Assert.fail("删除自提点发生错误", e);
		}
	}
}
