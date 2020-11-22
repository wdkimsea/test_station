package cn.guanmai.station.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.weight.WeightBasketBean;
import cn.guanmai.station.bean.weight.WeightCategoryTreeBean;
import cn.guanmai.station.bean.weight.WeightGroupBean;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Apr 11, 2019 5:05:41 PM 
* @des 称重
* @version 1.0 
*/
public class NewWeightSystemInfoTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StationWeightTest.class);
	private WeightService weightService;
	private LoginUserInfoService infoService;
	private ServiceTimeService serviceTimeService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		weightService = new WeightServiceImpl(headers);
		infoService = new LoginUserInfoServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
	}

	@Test
	public void weightBasketTestCase01() {
		try {
			Reporter.log("测试点: 获取称重框列表");
			List<WeightBasketBean> seightBaskets = weightService.getWeightBaskets();
			Assert.assertNotEquals(seightBaskets, null, "获取称重框失败");
		} catch (Exception e) {
			logger.error("获取称重框遇到错误: ", e);
			Assert.fail("获取称重框遇到错误: ", e);
		}
	}

	@Test
	public void weightBasketTestCase02() {
		ReporterCSS.title("测试点: 新建称重框");
		boolean result = false;
		String id = null;
		try {
			String name = StringUtil.getRandomString(6);
			WeightBasketBean weightBasket = new WeightBasketBean(name, new BigDecimal("1"));

			result = weightService.createWeightBasket(weightBasket);
			Assert.assertEquals(result, true, "新建称重框失败");

			List<WeightBasketBean> seightBaskets = weightService.getWeightBaskets();
			weightBasket = seightBaskets.stream().filter(b -> b.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(weightBasket, null, "新建的称重框没有查询到");
			id = weightBasket.getId();

		} catch (Exception e) {
			logger.error("新建称重框遇到错误: ", e);
			Assert.fail("新建称重框遇到错误: ", e);
		} finally {
			if (result) {
				try {
					result = weightService.deleteWeightBasket(id);
					Assert.assertEquals(result, true, "删除称重框失败");
				} catch (Exception e) {
					logger.error("删除称重框遇到错误: ", e);
					Assert.fail("删除称重框遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void weightBasketTestCase03() {
		ReporterCSS.title("测试点: 修改称重框");
		boolean result = false;
		String id = null;
		try {
			String name = StringUtil.getRandomString(6);
			WeightBasketBean weightBasket = new WeightBasketBean(name, new BigDecimal("1"));

			result = weightService.createWeightBasket(weightBasket);
			Assert.assertEquals(result, true, "新建称重框失败");

			List<WeightBasketBean> seightBaskets = weightService.getWeightBaskets();
			weightBasket = seightBaskets.stream().filter(b -> b.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(weightBasket, null, "新建的称重框没有查询到");
			id = weightBasket.getId();

			String new_name = StringUtil.getRandomString(6);
			weightBasket.setName(new_name);

			boolean success = weightService.updateWeightBasket(weightBasket);
			Assert.assertEquals(success, true, "修改称重框失败");
		} catch (Exception e) {
			logger.error("新建称重框遇到错误: ", e);
			Assert.fail("新建称重框遇到错误: ", e);
		} finally {
			if (result) {
				try {
					result = weightService.deleteWeightBasket(id);
					Assert.assertEquals(result, true, "删除称重框失败");
				} catch (Exception e) {
					logger.error("删除称重框遇到错误: ", e);
					Assert.fail("删除称重框遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void loginUserInfo01() {
		ReporterCSS.title("测试点: 获取登录的操作员账号相关信息");
		try {
			LoginUserInfoBean info = infoService.getLoginUserInfo();
			Assert.assertNotEquals(info, null, "获取登录的操作员账号相关信息失败");
		} catch (Exception e) {
			logger.error("获取登录的操作员账号相关信息遇到错误: ", e);
			Assert.fail("获取登录的操作员账号相关信息遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase01() {
		ReporterCSS.title("测试点: 获取运营时间相关信息");
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.allTimeConfig();
			Assert.assertNotEquals(serviceTimes, null, "获取运营时间列表报错");

			for (ServiceTimeBean serviceTime : serviceTimes) {
				String id = serviceTime.getId();
				List<String> dateDetails = serviceTimeService.getServiceTimeDateDetails(id);
				Assert.assertNotEquals(dateDetails, null, "获取指定运营时间 " + id + " 的运营时间列表失败");
			}
		} catch (Exception e) {
			logger.error("获取运营时间相关信息遇到错误: ", e);
			Assert.fail("获取运营时间相关信息遇到错误: ", e);
		}
	}

	@Test
	public void employeeInfoTestCase01() {
		ReporterCSS.title("测试点: 新版称重系统-设置当前称重操作员工工号");
		try {
			boolean result = weightService.setEmployee("");
			Assert.assertEquals(result, true, "员工账号设置为空,设置失败");
			result = weightService.setEmployee("001");
			Assert.assertEquals(result, true, "设置当前称重操作员工工号失败");
		} catch (Exception e) {
			logger.error("获取登录的操作员账号相关信息遇到错误: ", e);
			Assert.fail("获取登录的操作员账号相关信息遇到错误: ", e);
		}
	}

	@Test
	public void weightGroupTestCase01() {
		ReporterCSS.title("测试点: 新版称重软件-按计重商品分拣-获取商品分组列表");
		try {
			List<WeightGroupBean> weightGroups = weightService.getWeightGroupList();
			Assert.assertNotNull(weightGroups, "新版称重软件-按计重商品分拣-获取商品分组列表失败");
		} catch (Exception e) {
			logger.error("新版称重软件-按计重商品分拣-获取商品分组列表遇到错误: ", e);
			Assert.fail("新版称重软件-按计重商品分拣-获取商品分组列表遇到错误: ", e);
		}
	}

	@Test
	public void weightGroupTestCase02() {
		ReporterCSS.title("测试点: 新版称重软件-新建称重分组");
		String id = null;
		try {
			List<WeightCategoryTreeBean> categoryTree = weightService.getWeightCategoryUngroupTree();
			Assert.assertNotEquals(categoryTree, null, "拉取未分组称重的商品树失败");

			List<String> spu_ids = new ArrayList<String>();
			OK: for (WeightCategoryTreeBean category : categoryTree) {
				for (WeightCategoryTreeBean.Category2 category2 : category.getCategory2s()) {
					for (WeightCategoryTreeBean.Category2.Spu spu : category2.getSpus()) {
						spu_ids.add(spu.getSpu_id());
						if (spu_ids.size() > 10) {
							break OK;
						}
					}
				}
			}

			String name = StringUtil.getRandomString(6).toUpperCase();
			id = weightService.createWeightGroup(name, spu_ids);
			Assert.assertNotEquals(id, null, "新版称重软件-新建称重分组失败");

			List<WeightGroupBean> weightGroups = weightService.getWeightGroupList();
			Assert.assertNotNull(weightGroups, "新版称重软件-按计重商品分拣-获取商品分组列表失败");

			WeightGroupBean weightGroup = weightGroups.stream().filter(w -> w.getName().equals(name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(weightGroup, null, "新版称重软件-新建的称重分组在称重分组列表没有找到");

			weightGroup = weightService.getWeightGroupDetail(id);
			Assert.assertNotEquals(weightGroup, null, "新版称重软件-获取称重分组的详细信息失败");

			boolean result = weightService.updateWeightGroup(id, spu_ids);
			Assert.assertEquals(result, true, "新版称重软件-修改称重分组失败");
		} catch (Exception e) {
			logger.error("新版称重软件-称重分组相关操作遇到错误: ", e);
			Assert.fail("新版称重软件-称重分组相关操作遇到错误: ", e);
		} finally {
			if (id != null) {
				try {
					boolean result = weightService.deleteWeightGroup(id);
					Assert.assertEquals(result, true, "新版称重软件-删除称重分组失败");
				} catch (Exception e) {
					logger.error("新版称重软件-删除称重分组操作遇到错误: ", e);
					Assert.fail("新版称重软件-删除称重分组操作遇到错误: ", e);
				}
			}
		}
	}
}
