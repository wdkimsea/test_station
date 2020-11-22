package cn.guanmai.open.delivery;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.delivery.OpenCarModelBean;
import cn.guanmai.open.bean.delivery.OpenCarrierBean;
import cn.guanmai.open.bean.delivery.OpenDriverBean;
import cn.guanmai.open.bean.delivery.param.DrivcerCreateParam;
import cn.guanmai.open.bean.delivery.param.DrivcerUpdateParam;
import cn.guanmai.open.impl.delivery.OpenDeliveryServiceImpl;
import cn.guanmai.open.interfaces.delivery.OpenDeliveryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 13, 2019 3:16:52 PM 
* @des 开放平台司机测试
* @version 1.0 
*/
public class DeliveryDriverTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(DeliveryDriverTest.class);
	private OpenDeliveryService openDeliveryService;
	private DistributeService distributeService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		Map<String, String> st_headers = getSt_headers();

		openDeliveryService = new OpenDeliveryServiceImpl(access_token);
		distributeService = new DistributeServiceImpl(st_headers);

		try {
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取ST的登录信息失败");

			String station_id = loginUserInfo.getStation_id();
			Assert.assertNotEquals(station_id, null, "获取站点ID失败");
			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "ST初始化配送司机数据失败");
		} catch (Exception e) {
			logger.error("ST初始化配送司机数据遇到错误: ", e);
			Assert.fail("ST初始化配送司机数据遇到错误: ", e);
		}

	}

	@Test
	public void driverTestCase01() {
		ReporterCSS.title("测试点: 查询承运商列表");
		try {
			List<OpenCarrierBean> openCarrierList = openDeliveryService.searchCarrier(null, "0", "20");
			Assert.assertNotEquals(openCarrierList, null, "查询承运商列表失败");

			Assert.assertEquals(openCarrierList.size() >= 1, true, "开放平台查询无承运商,与预期不符");

			OpenCarrierBean openCarrier = openCarrierList.get(0);
			String carrier_name = openCarrier.getCarrier_name();
			String carrier_id = openCarrier.getCarrier_id();

			openCarrierList = openDeliveryService.searchCarrier(carrier_name, "0", "20");
			Assert.assertNotEquals(openCarrierList, null, "查询承运商列表失败");

			openCarrier = openCarrierList.stream().filter(c -> c.getCarrier_id().equals(carrier_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(openCarrier, null, "输入承运商名称进行搜索查询,没有找到预期的承运商");

		} catch (Exception e) {
			logger.error("查询承运商列表遇到错误: ", e);
			Assert.fail("查询承运商列表遇到错误: ", e);
		}
	}

	@Test
	public void driverTestCase02() {
		ReporterCSS.title("测试点: 查询车型列表");
		try {
			List<OpenCarModelBean> openCarModelList = openDeliveryService.searchCarModel(null, "0", "20");
			Assert.assertNotEquals(openCarModelList, null, "查询车型列表失败");

			Assert.assertEquals(openCarModelList.size() >= 1, true, "开放平台查询车型,与预期不符");

			OpenCarModelBean openCarModel = openCarModelList.get(0);
			String car_model_name = openCarModel.getCar_model_name();
			String car_model_id = openCarModel.getCar_model_id();

			openCarModelList = openDeliveryService.searchCarModel(car_model_name, "0", "20");
			Assert.assertNotEquals(openCarModelList, null, "查询车型列表失败");

			openCarModel = openCarModelList.stream().filter(c -> c.getCar_model_id().equals(car_model_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(openCarModel, null, "输入车型名称进行搜索,没有搜索到预期的车型");
		} catch (Exception e) {
			logger.error("查询车型列表遇到错误: ", e);
			Assert.fail("查询车型列表遇到错误: ", e);
		}
	}

	@Test
	public void driverTestCase03() {
		ReporterCSS.title("测试点: 查询司机列表");
		try {
			List<OpenDriverBean> openDriverList = openDeliveryService.searchDriver(null, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			Assert.assertEquals(openDriverList.size() >= 1, true, "开放平台查询司机列表,与预期不符");

			OpenDriverBean openDriver = openDriverList.get(0);
			String driver_name = openDriver.getDriver_name();
			String dirver_id = openDriver.getDriver_id();

			openDriverList = openDeliveryService.searchDriver(driver_name, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			openDriver = openDriverList.stream().filter(d -> d.getDriver_id().equals(dirver_id)).findAny().orElse(null);
			Assert.assertNotEquals(openDriver, null, "输入司机名称进行搜索,没有找到预期的司机");
		} catch (Exception e) {
			logger.error("查询司机列表遇到错误: ", e);
			Assert.fail("查询司机列表遇到错误: ", e);
		}
	}

	@Test
	public void driverTestCase04() {
		ReporterCSS.title("测试点: 新建司机");
		try {
			List<OpenCarrierBean> openCarrierList = openDeliveryService.searchCarrier(null, "0", "20");
			Assert.assertNotEquals(openCarrierList, null, "查询承运商列表失败");
			Assert.assertEquals(openCarrierList.size() >= 1, true, "开放平台查询无承运商,与预期不符");

			String carrier_id = openCarrierList.get(0).getCarrier_id();

			List<OpenCarModelBean> openCarModelList = openDeliveryService.searchCarModel(null, "0", "20");
			Assert.assertNotEquals(openCarModelList, null, "查询车型列表失败");
			Assert.assertEquals(openCarModelList.size() >= 1, true, "开放平台查询车型,与预期不符");

			String car_model_id = openCarModelList.get(0).getCar_model_id();

			DrivcerCreateParam drivcerCreateParam = new DrivcerCreateParam();
			drivcerCreateParam.setAccount(StringUtil.getRandomString(6).toUpperCase());
			drivcerCreateParam.setPassword("Test1234_");
			drivcerCreateParam.setCarrier_id(carrier_id);
			drivcerCreateParam.setCar_model_id(car_model_id);
			String name = StringUtil.getRandomString(6).toUpperCase();
			drivcerCreateParam.setName(name);
			String phone = "12" + StringUtil.getRandomNumber(9);
			drivcerCreateParam.setPhone(phone);
			String plate_number = "粤B." + StringUtil.getRandomString(5).toUpperCase();
			drivcerCreateParam.setPlate_number(plate_number);

			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertNotEquals(driver_id, null, "新建配送司机失败");

			List<OpenDriverBean> openDriverList = openDeliveryService.searchDriver(name, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			OpenDriverBean openDriver = openDriverList.stream().filter(d -> d.getDriver_id().equals(driver_id))
					.findAny().orElse(null);

			Assert.assertNotEquals(openDriver, null, "新建的司机在司机列表没有找到");

			boolean result = true;
			String msg = null;
			if (!openDriver.getCarrier_id().equals(carrier_id)) {
				msg = String.format("新建司机绑定的承运商与预期的不一致,预期:%s,实际:%s", carrier_id, openDriver.getCarrier_id());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getCar_model_id().equals(car_model_id)) {
				msg = String.format("新建司机绑定的车型与预期的不一致,预期:%s,实际:%s", car_model_id, openDriver.getCar_model_id());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getPlate_number().equals(plate_number)) {
				msg = String.format("新建司机填写的车牌号与预期的不一致,预期:%s,实际:%s", plate_number, openDriver.getPlate_number());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getDriver_name().equals(name)) {
				msg = String.format("新建司机填写的司机名与预期的不一致,预期:%s,实际:%s", name, openDriver.getDriver_name());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getPhone().equals(phone)) {
				msg = String.format("新建司机填写的电话号码与预期的不一致,预期:%s,实际:%s", phone, openDriver.getPhone());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建司机填写的信息与新建完查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建司机遇到错误: ", e);
			Assert.fail("新建司机遇到错误: ", e);
		}
	}

	@Test
	public void driverTestCase05() {
		ReporterCSS.title("测试点: 修改司机信息");
		try {
			List<OpenDriverBean> openDriverList = openDeliveryService.searchDriver(null, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			Assert.assertEquals(openDriverList.size() >= 1, true, "开放平台查询司机列表,与预期不符");

			OpenDriverBean openDriver = openDriverList.get(0);
			String dirver_id = openDriver.getDriver_id();

			String new_name = StringUtil.getRandomString(4).toUpperCase();
			String new_phone = "12" + StringUtil.getRandomNumber(9);
			String new_plate_number = "粤B." + StringUtil.getRandomString(5).toUpperCase();
			String new_state = openDriver.getState() == 0 ? "1" : "0";

			DrivcerUpdateParam drivcerUpdateParam = new DrivcerUpdateParam();
			drivcerUpdateParam.setDriver_id(dirver_id);
			drivcerUpdateParam.setCarrier_id(openDriver.getCarrier_id());
			drivcerUpdateParam.setCar_model_id(openDriver.getCar_model_id());
			drivcerUpdateParam.setName(new_name);
			drivcerUpdateParam.setPhone(new_phone);
			drivcerUpdateParam.setPlate_number(new_plate_number);
			drivcerUpdateParam.setState(new_state);

			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, true, "修改司机信息失败");

			openDriverList = openDeliveryService.searchDriver(new_name, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			Assert.assertEquals(openDriverList.size() >= 1, true, "用新改的司机名称搜索司机,没有找到目标司机");

			openDriver = openDriverList.stream().filter(d -> d.getDriver_id().equals(dirver_id)).findAny().orElse(null);
			Assert.assertNotEquals(openDriver, null, "用新改的司机名称搜索司机,没有找到目标司机");

			String msg = null;
			if (!openDriver.getPlate_number().equals(new_plate_number)) {
				msg = String.format("修改司机填写的车牌号与预期的不一致,预期:%s,实际:%s", new_plate_number, openDriver.getPlate_number());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getDriver_name().equals(new_name)) {
				msg = String.format("修改司机填写的司机名与预期的不一致,预期:%s,实际:%s", new_name, openDriver.getDriver_name());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openDriver.getPhone().equals(new_phone)) {
				msg = String.format("修改司机填写的电话号码与预期的不一致,预期:%s,实际:%s", new_phone, openDriver.getPhone());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!String.valueOf(openDriver.getState()).equals(new_state)) {
				msg = String.format("修改司机状态填的状态值与预期的不一致,预期:%s,实际:%s", new_state, openDriver.getState());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改司机填写的信息与修改完查询到的信息不一致");
		} catch (Exception e) {
			logger.error("修改司机信息遇到错误: ", e);
			Assert.fail("修改司机信息遇到错误: ", e);
		}
	}

}
