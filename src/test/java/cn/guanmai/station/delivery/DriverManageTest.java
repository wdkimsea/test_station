package cn.guanmai.station.delivery;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.delivery.CarModelBean;
import cn.guanmai.station.bean.delivery.CarrierBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.param.DriverCreateParam;
import cn.guanmai.station.bean.delivery.param.DriverUpdateParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 19, 2019 3:23:54 PM 
* @des 司机管理测试用例
* @version 1.0 
*/
public class DriverManageTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(DriverManageTest.class);
	private DistributeService distributeService;
	private LoginUserInfoService loginUserInfoService;
	private DriverBean driver;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		distributeService = new DistributeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			String station_id = loginUserInfo.getStation_id();
			Assert.assertNotNull(station_id, "获取站点ID失败");

			driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机管理数据失败");
		} catch (Exception e) {
			logger.error("初始化司机管理数据遇到错误: ", e);
			Assert.fail("初始化司机管理数据遇到错误: ", e);
		}
	}

	@Test
	public void driverManageTestCase01() {
		Reporter.log("测试点: 司机管理,新建承运商");
		BigDecimal carrier_id = null;
		try {
			String carrier_name = StringUtil.getRandomString(6).toUpperCase();
			carrier_id = distributeService.addCarrier(carrier_name);
			Assert.assertNotEquals(carrier_id, null, "新建承运商失败");

			List<CarrierBean> carrierList = distributeService.sarchCarrier(carrier_name);
			Assert.assertNotEquals(carrierList, null, "搜索过滤承运商失败");

			CarrierBean carrier = carrierList.stream().filter(c -> c.getCompany_name().equals(carrier_name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(carrier, null, "新建的承运商在承运商列表没有找到");
		} catch (Exception e) {
			logger.error("新建承运商遇到错误: ", e);
			Assert.fail("新建承运商遇到错误: ", e);
		} finally {
			try {
				Reporter.log("后置处理,删除新建的承运商");
				boolean result = distributeService.deleteCarrier(carrier_id);
				Assert.assertEquals(result, true, "后置处理,删除新建的承运商失败");
			} catch (Exception e) {
				logger.error("删除承运商遇到错误: ", e);
				Assert.fail("删除承运商遇到错误: ", e);
			}
		}
	}

	@Test
	public void driverManageTestCase02() {
		Reporter.log("测试点: 司机管理,修改承运商");
		BigDecimal carrier_id = null;
		try {
			String carrier_name = StringUtil.getRandomString(6).toUpperCase();
			carrier_id = distributeService.addCarrier(carrier_name);
			Assert.assertNotEquals(carrier_id, null, "新建承运商失败");

			String new_carrier_name = StringUtil.getRandomString(6).toUpperCase();
			boolean result = distributeService.updateCarrier(carrier_id, new_carrier_name);
			Assert.assertEquals(result, true, "修改承运商失败");

			List<CarrierBean> carrierList = distributeService.sarchCarrier(new_carrier_name);
			Assert.assertNotEquals(carrierList, null, "以承运商新名称搜索过滤承运商失败");

			CarrierBean carrier = carrierList.stream().filter(c -> c.getCompany_name().equals(new_carrier_name))
					.findAny().orElse(null);
			Assert.assertNotEquals(carrier, null, "以承运商新名称搜索过滤承运商没有找到目标承运商");
		} catch (Exception e) {
			logger.error("修改承运商遇到错误: ", e);
			Assert.fail("修改承运商遇到错误: ", e);
		} finally {
			try {
				Reporter.log("后置处理,删除新建的承运商");
				boolean result = distributeService.deleteCarrier(carrier_id);
				Assert.assertEquals(result, true, "后置处理,删除新建的承运商失败");
			} catch (Exception e) {
				logger.error("删除承运商遇到错误: ", e);
				Assert.fail("删除承运商遇到错误: ", e);
			}
		}
	}

	@Test
	public void driverManageTestCase03() {
		Reporter.log("测试点: 司机管理,删除承运商");
		BigDecimal carrier_id = null;
		try {
			String carrier_name = StringUtil.getRandomString(6).toUpperCase();
			carrier_id = distributeService.addCarrier(carrier_name);
			Assert.assertNotEquals(carrier_id, null, "新建承运商失败");

			boolean result = distributeService.deleteCarrier(carrier_id);
			Assert.assertEquals(result, true, "后置处理,删除新建的承运商失败");

			List<CarrierBean> carrierList = distributeService.sarchCarrier(carrier_name);
			Assert.assertNotEquals(carrierList, null, "搜索过滤承运商失败");

			CarrierBean carrier = carrierList.stream().filter(c -> c.getCompany_name().equals(carrier_name)).findAny()
					.orElse(null);
			Assert.assertEquals(carrier, null, "删除的承运商实际没有删除,还在承运商列表显示");
		} catch (Exception e) {
			logger.error("删除承运商遇到错误: ", e);
			Assert.fail("删除承运商遇到错误: ", e);
		}
	}

	@Test
	public void driverManageTestCase04() {
		Reporter.log("测试点: 司机管理,新建车型");
		BigDecimal car_model_id = null;
		try {
			String car_model_name = StringUtil.getRandomString(6).toUpperCase();
			CarModelBean carModel = new CarModelBean(car_model_name, 8);

			car_model_id = distributeService.addCarModel(carModel);
			Assert.assertNotEquals(car_model_id, null, "新建车型失败");

			List<CarModelBean> carModelList = distributeService.searchCarModel(car_model_name);
			Assert.assertNotEquals(carModelList, null, "搜索过滤车型失败");

			carModel = carModelList.stream().filter(c -> c.getCar_model_name().equals(car_model_name)).findAny()
					.orElse(null);
			Assert.assertNotNull(carModel, "新建的车型在车型列表页面没找到");
		} catch (Exception e) {
			logger.error("新建车型遇到错误: ", e);
			Assert.fail("新建车型遇到错误: ", e);
		} finally {
			if (car_model_id != null) {
				try {
					Reporter.log("后置处理,删除新建的车型");
					boolean result = distributeService.deleteCarModel(car_model_id);
					Assert.assertEquals(result, true, "后置处理,删除新建的车型失败");
				} catch (Exception e) {
					logger.error("删除车型遇到错误: ", e);
					Assert.fail("删除车型遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void driverManageTestCase05() {
		Reporter.log("测试点: 司机管理,修改车型");
		BigDecimal car_model_id = null;
		try {
			String car_model_name = StringUtil.getRandomString(6).toUpperCase();
			CarModelBean carModel = new CarModelBean(car_model_name, 8);

			car_model_id = distributeService.addCarModel(carModel);
			Assert.assertNotEquals(car_model_id, null, "新建车型失败");

			String new_car_model_name = StringUtil.getRandomString(6).toUpperCase();
			carModel.setId(car_model_id);
			carModel.setCar_model_name(new_car_model_name);
			carModel.setMax_load(4);

			boolean result = distributeService.updateCarModel(carModel);
			Assert.assertEquals(result, true, "修改车型信息失败");

			List<CarModelBean> carModelList = distributeService.searchCarModel(new_car_model_name);
			Assert.assertNotEquals(carModelList, null, "搜索过滤车型失败");

			CarModelBean temp_car_model = carModelList.stream()
					.filter(c -> c.getCar_model_name().equals(new_car_model_name)).findAny().orElse(null);
			Assert.assertNotNull(temp_car_model, "使用新的车型名称搜索车型在车型列表页面没找到");

			Assert.assertEquals(temp_car_model.getMax_load(), carModel.getMax_load(), "修改车型的满载数没有生效");
		} catch (Exception e) {
			logger.error("修改车型遇到错误: ", e);
			Assert.fail("修改车型遇到错误: ", e);
		} finally {
			if (car_model_id != null) {
				try {
					Reporter.log("后置处理,删除新建的车型");
					boolean result = distributeService.deleteCarModel(car_model_id);
					Assert.assertEquals(result, true, "后置处理,删除新建的车型失败");
				} catch (Exception e) {
					logger.error("删除车型遇到错误: ", e);
					Assert.fail("删除车型遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void driverManageTestCase06() {
		Reporter.log("测试点: 司机管理,删除车型");
		BigDecimal car_model_id = null;
		try {
			String car_model_name = StringUtil.getRandomString(6).toUpperCase();
			CarModelBean carModel = new CarModelBean(car_model_name, 8);

			car_model_id = distributeService.addCarModel(carModel);
			Assert.assertNotEquals(car_model_id, null, "新建车型失败");

			boolean result = distributeService.deleteCarModel(car_model_id);
			Assert.assertEquals(result, true, "删除新建的车型失败");

			List<CarModelBean> carModelList = distributeService.searchCarModel(car_model_name);
			Assert.assertNotEquals(carModelList, null, "搜索过滤车型失败");

			CarModelBean temp_car_model = carModelList.stream()
					.filter(c -> c.getCar_model_name().equals(car_model_name)).findAny().orElse(null);
			Assert.assertEquals(temp_car_model, null, "删除的车型实际没有删除成功,还可以在车型列表找到");
		} catch (Exception e) {
			logger.error("删除车型遇到错误: ", e);
			Assert.fail("删除车型遇到错误: ", e);
		}
	}

	@Test
	public void driverManageTestCase07() {
		Reporter.log("测试点: 司机管理,新建司机");
		BigDecimal driver_id = null;
		try {
			String account = "ZDH" + StringUtil.getRandomNumber(6);
			String name = StringUtil.getRandomString(6).toUpperCase();
			DriverCreateParam driverCreateParam = new DriverCreateParam();
			driverCreateParam.setAccount(account);
			driverCreateParam.setAllow_login(1);
			driverCreateParam.setCar_model_id(driver.getCar_model_id());
			driverCreateParam.setCarrier_id(driver.getCarrier_id());
			driverCreateParam.setMax_load(driver.getMax_load());
			driverCreateParam.setName(name);
			driverCreateParam.setPassword("Test1234_");
			driverCreateParam.setPassword_check("Test1234_");
			driverCreateParam.setShare(1);
			driverCreateParam.setState(1);

			driver_id = distributeService.addDriver(driverCreateParam);
			Assert.assertNotEquals(driver_id, null, "新建司机失败");

			List<DriverBean> driverList = distributeService.searchDriver(name, 0, 10);
			Assert.assertNotEquals(driverList, null, "搜索过滤司机列表失败");

			DriverBean temp_driver = driverList.stream().filter(d -> d.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(temp_driver, null, "新建的司机在司机列表没有找到");
		} catch (Exception e) {
			logger.error("新建司机遇到错误: ", e);
			Assert.fail("新建司机遇到错误: ", e);
		} finally {
			if (driver_id != null) {
				try {
					Reporter.log("后置处理,删除新建的司机");
					boolean result = distributeService.deleteDriver(driver_id);
					Assert.assertEquals(result, true, "后置处理,删除新建的司机失败");
				} catch (Exception e) {
					logger.error("后置处理-删除司机遇到错误: ", e);
					Assert.fail("后置处理-删除司机遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void driverManageTestCase08() {
		Reporter.log("测试点: 司机管理,新建司机");
		BigDecimal driver_id = null;
		try {
			String account = "ZDH" + StringUtil.getRandomNumber(6);
			String name = StringUtil.getRandomString(6).toUpperCase();
			DriverUpdateParam driverUpdateParam = new DriverUpdateParam();

			driverUpdateParam.setAccount(account);
			driverUpdateParam.setAllow_login(1);
			driverUpdateParam.setCar_model_id(driver.getCar_model_id());
			driverUpdateParam.setCarrier_id(driver.getCarrier_id());
			driverUpdateParam.setMax_load(driver.getMax_load());
			driverUpdateParam.setName(name);
			driverUpdateParam.setPassword("Test1234_");
			driverUpdateParam.setPassword_check("Test1234_");
			driverUpdateParam.setShare(1);
			driverUpdateParam.setState(1);

			driver_id = distributeService.addDriver(driverUpdateParam);
			Assert.assertNotEquals(driver_id, null, "新建司机失败");

			driverUpdateParam.setDriver_id(driver_id);
			String new_name = StringUtil.getRandomString(6).toUpperCase();
			driverUpdateParam.setName(new_name);

			boolean result = distributeService.updateDriver(driverUpdateParam);
			Assert.assertEquals(result, true, "修改司机失败");

		} catch (Exception e) {
			logger.error("新建司机遇到错误: ", e);
			Assert.fail("新建司机遇到错误: ", e);
		} finally {
			if (driver_id != null) {
				try {
					Reporter.log("后置处理,删除新建的司机");
					boolean result = distributeService.deleteDriver(driver_id);
					Assert.assertEquals(result, true, "后置处理,删除新建的司机失败");
				} catch (Exception e) {
					logger.error("后置处理-删除司机遇到错误: ", e);
					Assert.fail("后置处理-删除司机遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void driverManageTestCase09() {
		Reporter.log("测试点: 司机管理,新建司机");
		BigDecimal driver_id = null;
		try {
			String account = "ZDH" + StringUtil.getRandomNumber(6);
			String name = StringUtil.getRandomString(6).toUpperCase();
			DriverCreateParam driverCreateParam = new DriverCreateParam();
			driverCreateParam.setAccount(account);
			driverCreateParam.setAllow_login(1);
			driverCreateParam.setCar_model_id(driver.getCar_model_id());
			driverCreateParam.setCarrier_id(driver.getCarrier_id());
			driverCreateParam.setMax_load(driver.getMax_load());
			driverCreateParam.setName(name);
			driverCreateParam.setPassword("Test1234_");
			driverCreateParam.setPassword_check("Test1234_");
			driverCreateParam.setShare(1);
			driverCreateParam.setState(1);

			driver_id = distributeService.addDriver(driverCreateParam);
			Assert.assertNotEquals(driver_id, null, "新建司机失败");

			boolean result = distributeService.deleteDriver(driver_id);
			Assert.assertEquals(result, true, "删除新建的司机失败");

			List<DriverBean> driverList = distributeService.searchDriver(name, 0, 10);
			Assert.assertNotEquals(driverList, null, "搜索过滤司机列表失败");

			DriverBean temp_driver = driverList.stream().filter(d -> d.getName().equals(name)).findAny().orElse(null);
			Assert.assertEquals(temp_driver, null, "删除的司机实际没有删除,在司机列表还可以找到");
		} catch (Exception e) {
			logger.error("删除司机遇到错误: ", e);
			Assert.fail("删除司机遇到错误: ", e);
		}
	}

}
