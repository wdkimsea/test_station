package cn.guanmai.open.delivery.abnormal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 14, 2019 10:00:53 AM 
* @des 开放平台司机异常测试
* @version 1.0 
*/
public class DeliveryDriverAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(DeliveryDriverAbnormalTest.class);
	private OpenDeliveryService openDeliveryService;
	private DistributeService distributeService;
	private DrivcerCreateParam drivcerCreateParam;
	private String carrier_id;
	private String car_model_id;
	private OpenDriverBean openDriver;
	private DrivcerUpdateParam drivcerUpdateParam;

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
			DriverBean dirver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(dirver, null, "ST初始化配送司机数据失败");

			carrier_id = dirver.getCarrier_id().toString();

			car_model_id = dirver.getCar_model_id().toString();

			List<OpenDriverBean> openDriverList = openDeliveryService.searchDriver(null, "0", "20");
			Assert.assertNotEquals(openDriverList, null, "查询司机列表失败");

			openDriver = openDriverList.get(0);
		} catch (Exception e) {
			logger.error("ST初始化配送司机数据遇到错误: ", e);
			Assert.fail("ST初始化配送司机数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		drivcerCreateParam = new DrivcerCreateParam();
		drivcerCreateParam.setAccount(StringUtil.getRandomString(6).toUpperCase());
		drivcerCreateParam.setPassword("Test1234_");
		drivcerCreateParam.setCarrier_id(carrier_id);
		drivcerCreateParam.setCar_model_id(car_model_id);
		String name = StringUtil.getRandomString(6).toString();
		drivcerCreateParam.setName(name);
		String phone = "12" + StringUtil.getRandomNumber(9);
		drivcerCreateParam.setPhone(phone);
		String plate_number = "粤B." + StringUtil.getRandomString(5).toUpperCase();
		drivcerCreateParam.setPlate_number(plate_number);

		drivcerUpdateParam = new DrivcerUpdateParam();
		drivcerUpdateParam.setDriver_id(openDriver.getDriver_id());
		drivcerUpdateParam.setCarrier_id(openDriver.getCarrier_id());
		drivcerUpdateParam.setCar_model_id(openDriver.getCar_model_id());
		drivcerUpdateParam.setName(openDriver.getDriver_name());
		drivcerUpdateParam.setPhone(openDriver.getPhone());
		drivcerUpdateParam.setPlate_number(openDriver.getPlate_number());
		drivcerUpdateParam.setState(String.valueOf(openDriver.getState()));
	}

	@Test
	public void deliveryDriverAbnormalTestCase01() {
		Reporter.log("测试点: 搜索查询配送司机异常测试,分页offset值填入非数字,断言失败");
		try {
			List<OpenDriverBean> openDriverList = openDeliveryService.searchDriver(null, "A", "20");
			Assert.assertEquals(openDriverList, null, "搜索查询配送司机异常测试,分页offset值填入非数字,断言失败");
		} catch (Exception e) {
			logger.error("搜索查询承运商遇到错误: ", e);
			Assert.fail("搜索查询承运商遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase02() {
		Reporter.log("测试点: 新建配送司机异常测试,name输入为空,断言失败");
		try {
			drivcerCreateParam.setName("");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	// @Test
	// public void deliveryDriverAbnormalTestCase03() {
	// Reporter.log("测试点: 新建配送司机异常测试,name输入过长字符串(司机名格式限制为5个汉字或10个英文),断言失败");
	// try {
	// drivcerCreateParam.setName(StringUtil.getRandomString(11).toUpperCase());
	// String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
	// Assert.assertEquals(driver_id, null,
	// "新建配送司机异常测试,name输入过长字符串(司机名格式限制为5个汉字或10个英文),断言失败");
	// } catch (Exception e) {
	// logger.error("新建配送司机遇到错误: ", e);
	// Assert.fail("新建配送司机遇到错误: ", e);
	// }
	// }
	//
	// @Test
	// public void deliveryDriverAbnormalTestCase04() {
	// Reporter.log("测试点: 新建配送司机异常测试,name输入过长字符串(司机名格式限制为5个汉字或10个英文),断言失败");
	// try {
	// drivcerCreateParam.setName("一二三四五六");
	// String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
	// Assert.assertEquals(driver_id, null,
	// "新建配送司机异常测试,name输入过长字符串(司机名格式限制为5个汉字或10个英文),断言失败");
	// } catch (Exception e) {
	// logger.error("新建配送司机遇到错误: ", e);
	// Assert.fail("新建配送司机遇到错误: ", e);
	// }
	// }

	@Test
	public void deliveryDriverAbnormalTestCase05() {
		Reporter.log("测试点: 新建配送司机异常测试,承运商ID输入为空,断言失败");
		try {
			drivcerCreateParam.setCarrier_id("");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,承运商ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase06() {
		Reporter.log("测试点: 新建配送司机异常测试,承运商ID输入为非本站的承运商ID,断言失败");
		try {
			String temp_carrier_id = new BigDecimal(carrier_id).subtract(new BigDecimal("1")).toString();
			drivcerCreateParam.setCarrier_id(temp_carrier_id);
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,承运商ID输入为非本站的承运商ID,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase07() {
		Reporter.log("测试点: 新建配送司机异常测试,车型ID输入为空,断言失败");
		try {
			drivcerCreateParam.setCar_model_id("");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,车型ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase08() {
		Reporter.log("测试点: 新建配送司机异常测试,车型ID输入为错误值,断言失败");
		try {
			drivcerCreateParam.setCar_model_id("A");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,车型ID输入为错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase09() {
		Reporter.log("测试点: 新建配送司机异常测试,车型ID输入为非本站的车型ID,断言失败");
		try {
			String temp_car_model_id = new BigDecimal(car_model_id).subtract(new BigDecimal("1")).toString();
			drivcerCreateParam.setCar_model_id(temp_car_model_id);
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,车型ID输入为非本站的车型ID,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase10() {
		Reporter.log("测试点: 新建配送司机异常测试,司机账号输入为空,断言失败");
		try {
			drivcerCreateParam.setAccount("");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,司机账号输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	// @Test
	// public void deliveryDriverAbnormalTestCase11() {
	// Reporter.log("测试点: 新建配送司机异常测试,司机账号输入为空,断言失败");
	// try {
	// drivcerCreateParam.setAccount("");
	// String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
	// Assert.assertEquals(driver_id, null, "新建配送司机异常测试,司机账号输入为空,断言失败");
	// } catch (Exception e) {
	// logger.error("新建配送司机遇到错误: ", e);
	// Assert.fail("新建配送司机遇到错误: ", e);
	// }
	// }

	@Test
	public void deliveryDriverAbnormalTestCase12() {
		Reporter.log("测试点: 新建配送司机异常测试,司机账号输入为空格,断言失败");
		try {
			drivcerCreateParam.setAccount("  ");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,司机账号输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase13() {
		Reporter.log("测试点: 新建配送司机异常测试,密码输入为空,断言失败");
		try {
			drivcerCreateParam.setPassword("");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,密码输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase14() {
		Reporter.log("测试点: 新建配送司机异常测试,密码输入为空格,断言失败");
		try {
			drivcerCreateParam.setPassword("       ");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,密码输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase15() {
		Reporter.log("测试点: 新建配送司机异常测试,密码长度输入不足6位,断言失败");
		try {
			drivcerCreateParam.setPassword("12345");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,密码输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase16() {
		Reporter.log("测试点: 新建配送司机异常测试,密码长度输中文,断言失败");
		try {
			drivcerCreateParam.setPassword("一二三四五六");
			String driver_id = openDeliveryService.createDriver(drivcerCreateParam);
			Assert.assertEquals(driver_id, null, "新建配送司机异常测试,密码长度输中文,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	// @Test
	// public void deliveryDriverAbnormalTestCase17() {
	// Reporter.log("测试点: 修改配送司机异常测试,司机账号输入为不合法字符,断言失败");
	// try {
	// drivcerUpdateParam.setName("VNB-JK");
	// boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
	// Assert.assertEquals(result, false, "修改配送司机异常测试,名称输入为不合法字符,断言失败");
	// } catch (Exception e) {
	// logger.error("新建配送司机遇到错误: ", e);
	// Assert.fail("新建配送司机遇到错误: ", e);
	// }
	// }

	@Test
	public void deliveryDriverAbnormalTestCase18() {
		Reporter.log("测试点: 修改配送司机异常测试,司机账号输入超过最大字符限制(32个字符),断言失败");
		try {
			drivcerUpdateParam.setName(StringUtil.getRandomString(33));
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,名称输入为不合法字符,断言失败");
		} catch (Exception e) {
			logger.error("新建配送司机遇到错误: ", e);
			Assert.fail("新建配送司机遇到错误: ", e);
		}
	}

	// @Test
	// public void deliveryDriverAbnormalTestCase19() {
	// Reporter.log("测试点: 修改配送司机异常测试,名称输入为超过限制长度字符(5个汉字或10个英文),断言失败");
	// try {
	// drivcerUpdateParam.setName(StringUtil.getRandomString(11).toUpperCase());
	// boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
	// Assert.assertEquals(result, false,
	// "修改配送司机异常测试,名称输入为超过限制长度字符(5个汉字或10个英文),断言失败");
	// } catch (Exception e) {
	// logger.error("修改配送司机遇到错误: ", e);
	// Assert.fail("修改配送司机遇到错误: ", e);
	// }
	// }
	//
	// @Test
	// public void deliveryDriverAbnormalTestCase20() {
	// Reporter.log("测试点: 修改配送司机异常测试,名称输入为超过限制长度字符(5个汉字或10个英文),断言失败");
	// try {
	// drivcerUpdateParam.setName("这个是超过了");
	// boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
	// Assert.assertEquals(result, false,
	// "修改配送司机异常测试,名称输入为超过限制长度字符(5个汉字或10个英文),断言失败");
	// } catch (Exception e) {
	// logger.error("修改配送司机遇到错误: ", e);
	// Assert.fail("修改配送司机遇到错误: ", e);
	// }
	// }

	@Test
	public void deliveryDriverAbnormalTestCase21() {
		Reporter.log("测试点: 修改配送司机异常测试,承运商ID输入为空,断言失败");
		try {
			drivcerUpdateParam.setCarrier_id("");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,承运商ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase22() {
		Reporter.log("测试点: 修改配送司机异常测试,承运商ID输入为非本站的承运商ID,断言失败");
		try {
			String temp_carrier_id = new BigDecimal(carrier_id).subtract(new BigDecimal("1")).toString();
			drivcerUpdateParam.setCarrier_id(temp_carrier_id);
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,承运商ID输入为非本站的承运商ID,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase23() {
		Reporter.log("测试点: 修改配送司机异常测试,车型ID输入为空,断言失败");
		try {
			drivcerUpdateParam.setCar_model_id("");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,车型ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase24() {
		Reporter.log("测试点: 修改配送司机异常测试,车型ID输入为错误值,断言失败");
		try {
			drivcerUpdateParam.setCar_model_id("A");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,车型ID输入为错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase25() {
		Reporter.log("测试点: 修改配送司机异常测试,车型ID输入为非本站的车型ID,断言失败");
		try {
			String temp_car_model_id = new BigDecimal(car_model_id).subtract(new BigDecimal("1")).toString();
			drivcerUpdateParam.setCar_model_id(temp_car_model_id);
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,车型ID输入为非本站的车型ID,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase26() {
		Reporter.log("测试点: 修改配送司机异常测试,司机ID输入为空,断言失败");
		try {
			drivcerUpdateParam.setDriver_id("");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,司机ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase27() {
		Reporter.log("测试点: 修改配送司机异常测试,密码输入为空,断言失败");
		try {
			drivcerUpdateParam.setPassword("");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,密码输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase28() {
		Reporter.log("测试点: 修改配送司机异常测试,密码输入为空格,断言失败");
		try {
			drivcerUpdateParam.setPassword("        ");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,密码输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase29() {
		Reporter.log("测试点: 修改配送司机异常测试,密码长度输入不足6位,断言失败");
		try {
			drivcerUpdateParam.setPassword("12345");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,密码长度输入不足6位,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

	@Test
	public void deliveryDriverAbnormalTestCase30() {
		Reporter.log("测试点: 修改配送司机异常测试,密码输入为中文,断言失败");
		try {
			drivcerUpdateParam.setPassword("一二345678");
			boolean result = openDeliveryService.updateDriver(drivcerUpdateParam);
			Assert.assertEquals(result, false, "修改配送司机异常测试,密码输入为中文,断言失败");
		} catch (Exception e) {
			logger.error("修改配送司机遇到错误: ", e);
			Assert.fail("修改配送司机遇到错误: ", e);
		}
	}

}
