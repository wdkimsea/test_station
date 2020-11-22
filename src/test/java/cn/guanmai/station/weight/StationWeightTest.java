package cn.guanmai.station.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderProcessBean;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.param.SortingProfileParam;
import cn.guanmai.station.bean.weight.WeightCollectInfoBean;
import cn.guanmai.station.bean.weight.WeightCollectOrderBean;
import cn.guanmai.station.bean.weight.WeightCollectSkuBean;
import cn.guanmai.station.bean.weight.WeightSkuBean;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.weight.param.WeightCollectOrderFilterParam;
import cn.guanmai.station.bean.weight.param.WeightCollectSkuFilterParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Apr 2, 2019 11:45:40 AM 
* @des ST分拣
* @version 1.0 
*/
public class StationWeightTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StationWeightTest.class);
	private WeightService weightService;
	private CategoryService categoryService;
	private OrderTool orderTool;
	private DistributeService distributeService;
	private LoginUserInfoService loginUserInfoService;
	private RouteService routeService;
	private OrderService orderService;
	private String order_id;
	private OrderCycle orderCycle;
	private OrderDetailBean orderDetail;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		weightService = new WeightServiceImpl(headers);
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		routeService = new RouteServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			ProfileService profileService = new ProfileServiceImpl(headers);
			boolean result = profileService.updateSortingProfile(new SortingProfileParam());
			Assert.assertEquals(result, true, "系统-分拣设置失败");
			order_id = orderTool.oneStepCreateOrder(10);
			result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + " 状态改为分拣中失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单" + order_id + "所处运营周期时间失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");
		} catch (Exception e) {
			logger.error("新建订单过程中遇到错误: ", e);
			Assert.fail("新建订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase01() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣任务-获取分拣进度");
		try {
			WeightCollectInfoBean weightCollectInfo = weightService.getWeightCollectInfo(orderCycle.getTime_config_id(),
					orderCycle.getCycle_start_time());
			Assert.assertNotEquals(weightCollectInfo, null, "供应链-分拣-分拣任务-获取分拣进度失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣任务-获取分拣进度遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣任务-获取分拣进度遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase02() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣进度-订单分拣进度");
		try {
			boolean result = weightService.getWeightCollectRandomOrderInfo(orderCycle.getTime_config_id(),
					orderCycle.getCycle_start_time(), 7);
			Assert.assertEquals(result, true, "供应链-分拣-分拣进度-订单分拣进度 获取失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣进度-订单分拣进度 获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣进度-订单分拣进度 获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase03() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣进度-商品分拣进度");
		try {
			boolean result = weightService.getgetWeightCollectRandomSkuInfo(orderCycle.getTime_config_id(),
					orderCycle.getCycle_start_time(), 15);
			Assert.assertEquals(result, true, "供应链-分拣-分拣进度-商品分拣进度 获取失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣进度-商品分拣进度 获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣进度-商品分拣进度 获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase04() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣搜索过滤");
		try {
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣 数据获取失败");

		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase05() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣,输入商品名搜索过滤");
		try {

			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_name = detail.getSku_name();
			weightCollectSkuFilterParam.setSearch(sku_name);

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣,输入商品名搜索过滤失败");

			List<String> other_sku_names = weightCollectSkus.stream().filter(s -> !s.getSku_name().contains(sku_name))
					.map(s -> s.getSku_name()).collect(Collectors.toList());

			Assert.assertEquals(other_sku_names.size(), 0,
					"供应链-分拣-分拣明细-按商品分拣,输入商品名[" + sku_name + "]搜索过滤,搜索出了其他名称的商品信息" + other_sku_names);
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase06() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣,使用商品分类搜索过滤");
		try {
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

			String spu_id = detail.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU商品" + spu_id + "详情失败");
			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();
			String category2_id = spu.getCategory_id_2();
			String category2_name = spu.getCategory_name_2();
			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			JSONArray category1Array = new JSONArray();
			category1Array.add(category1_id);

			JSONArray category2Array = new JSONArray();
			category2Array.add(category2_id);

			JSONArray pinleiArray = new JSONArray();
			pinleiArray.add(pinlei_id);

			weightCollectSkuFilterParam.setCategory_id_1(category1Array);
			weightCollectSkuFilterParam.setCategory_id_2(category2Array);
			weightCollectSkuFilterParam.setPinlei_id(pinleiArray);

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣,使用商品分类搜索过滤失败");

			String msg = null;
			boolean result = true;
			for (WeightCollectSkuBean weightCollectSku : weightCollectSkus) {
				if (!weightCollectSku.getCategory1_name().equals(category1_name)) {
					msg = String.format("供应链-分拣-分拣明细-按商品分拣,输入一级分类[%s]搜索过滤,过滤出了一级分类[%s]的商品[%s]", category1_name,
							weightCollectSku.getCategory1_name(), weightCollectSku.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!weightCollectSku.getCategory2_name().equals(category2_name)) {
					msg = String.format("供应链-分拣-分拣明细-按商品分拣,输入二级分类[%s]搜索过滤,过滤出了二级分类[%s]的商品[%s]", category2_name,
							weightCollectSku.getCategory2_name(), weightCollectSku.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!weightCollectSku.getPinlei_name().equals(pinlei_name)) {
					msg = String.format("供应链-分拣-分拣明细-按商品分拣,输入品类分类[%s]搜索过滤,过滤出了品类分类[%s]的商品[%s]", pinlei_name,
							weightCollectSku.getPinlei_name(), weightCollectSku.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			String category_name = String.format("%s-%s-%s", category1_name, category2_name, pinlei_name);
			Assert.assertEquals(result, true, "供应链-分拣-分拣明细-按商品分拣,使用分类" + category_name + "过滤,过滤的数据与预期不符");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase07() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣,选择报价单搜索过滤");
		try {
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String salemenu_id = detail.getSalemenu_id();
			weightCollectSkuFilterParam.setSalemenu_id(salemenu_id);

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣,使用报价单搜索过滤失败");

			List<String> other_salemenu_ids = weightCollectSkus.stream()
					.filter(s -> !s.getSalemenu_id().equals(salemenu_id)).map(s -> s.getSku_name())
					.collect(Collectors.toList());

			Assert.assertEquals(other_salemenu_ids.size(), 0,
					"供应链-分拣-分拣明细-按商品分拣,输入报价单ID[" + salemenu_id + "]搜索过滤,搜索出了其他报价单的商品信息" + other_salemenu_ids);
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase08() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣,选择验货状态搜索过滤");
		try {
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);
			weightCollectSkuFilterParam.setInspect_status(2);

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣,选择验货状态搜索过滤失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase09() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按商品分拣,选择订单类型搜索过滤");
		try {
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setLimit(10);
			weightCollectSkuFilterParam.setOffset(0);

			List<OrderProcessBean> orderProcessList = orderService.getOrderProcessList();
			Assert.assertNotEquals(orderProcessList, null, "获取订单流信息失败");

			OrderProcessBean orderProcess = NumberUtil.roundNumberInList(orderProcessList);

			weightCollectSkuFilterParam.setOrder_process_type_id(orderProcess.getId());

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣,选择验货状态搜索过滤失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按商品分拣 数据获取遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase10() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,搜索过滤数据失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase11() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,输入订单ID搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setSearch(order_id);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,输入订单ID搜索过滤数据失败");

			List<String> other_order_ids = weightCollectOrders.stream().filter(o -> !o.getOrder_id().equals(order_id))
					.map(o -> o.getOrder_id()).collect(Collectors.toList());
			Assert.assertEquals(other_order_ids.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,输入订单ID[" + order_id + "]搜索过滤数据,过滤出了其他订单" + other_order_ids + "的数据");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase12() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,输入商户名搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			String resname = orderDetail.getCustomer().getExtender().getResname();
			weightCollectOrderFilterParam.setSearch(resname);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,输入商户名搜索过滤数据失败");

			List<String> other_resnames = weightCollectOrders.stream()
					.filter(o -> !o.getAddress_name().contains(resname)).map(o -> o.getAddress_name())
					.collect(Collectors.toList());
			Assert.assertEquals(other_resnames.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,输商户名称[" + resname + "]搜索过滤数据,过滤出了其他商户名" + other_resnames + "的数据");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase13() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,输入商户ID搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			String address_id = orderDetail.getCustomer().getAddress_id();
			String resname = orderDetail.getCustomer().getExtender().getResname();
			String sid = "S" + String.format("%06d", Integer.valueOf(address_id));
			weightCollectOrderFilterParam.setSearch(sid);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,输入商户ID搜索过滤数据失败");

			List<String> other_resnames = weightCollectOrders.stream()
					.filter(o -> !o.getAddress_name().contains(resname)).map(o -> o.getAddress_name())
					.collect(Collectors.toList());
			Assert.assertEquals(other_resnames.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,输商户ID[" + address_id + "]搜索过滤数据,过滤出了其他商户名" + other_resnames + "的数据");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase14() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,选择线路搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			BigDecimal route_id = routeService.initRouteData();
			String route_name = "人民路";
			weightCollectOrderFilterParam.setRoute_id(route_id);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,选择线路搜索过滤数据失败");

			List<String> other_route_names = weightCollectOrders.stream().filter(o -> !o.getRoute().equals(route_name))
					.map(o -> o.getRoute()).collect(Collectors.toList());
			Assert.assertEquals(other_route_names.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,选择线路[" + route_name + "]搜索过滤数据,过滤出了其他线路" + other_route_names + "的数据");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase15() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用订单状态搜索过滤数据");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			weightCollectOrderFilterParam.setStatus(5);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用订单状态搜索过滤数据失败");

			List<String> other_order_ids = weightCollectOrders.stream().filter(o -> o.getOrder_status() != 5)
					.map(o -> o.getOrder_id()).collect(Collectors.toList());
			Assert.assertEquals(other_order_ids.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,使用订单状态[" + 5 + "]搜索过滤数据,过滤出了其他状态的订单" + other_order_ids);
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase16() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用分拣状态搜索过滤");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			weightCollectOrderFilterParam.setSort_status(2);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用分拣状态搜索过滤失败");

			List<String> other_order_ids = weightCollectOrders.stream().filter(o -> o.getTotal() != o.getFinished())
					.map(o -> o.getOrder_id()).collect(Collectors.toList());
			Assert.assertEquals(other_order_ids.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,使用分拣状态[已完成分拣]搜索过滤数据,过滤出了未完成分拣的订单" + other_order_ids);
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase17() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用司机筛选过滤");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			LoginStationInfoBean loginStationInfo = loginUserInfoService.getLoginStationInfo();
			Assert.assertNotEquals(loginStationInfo, null, "获取登录站点信息失败");
			String station_id = loginStationInfo.getStation_id();

			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机信息失败");
			BigDecimal driver_id = driver.getId();
			String driver_name = driver.getName();

			weightCollectOrderFilterParam.setDriver_id(driver_id);
			weightCollectOrderFilterParam.setSort_status(2);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用分拣状态搜索过滤失败");

			List<String> other_driver_names = weightCollectOrders.stream()
					.filter(o -> !o.getDriver_name().equals(driver_name)).map(o -> o.getOrder_id())
					.collect(Collectors.toList());
			Assert.assertEquals(other_driver_names.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,使用司机[" + driver_name + "]搜索过滤数据,过滤出了其他司机" + other_driver_names + "的数据");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase18() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用验货状态过滤");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			weightCollectOrderFilterParam.setInspect_status(2);
			weightCollectOrderFilterParam.setSort_status(2);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用验货状态过滤失败");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase19() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用打印状态过滤");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());

			weightCollectOrderFilterParam.setPrint_status(1);
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用验货状态过滤失败");

			List<String> other_order_ids = weightCollectOrders.stream().filter(o -> o.getPrint_times() == 0)
					.map(o -> o.getOrder_id()).collect(Collectors.toList());
			Assert.assertEquals(other_order_ids.size(), 0,
					"供应链-分拣-分拣明细-按订单分拣,使用打印状态[已打印]搜索过滤数据,过滤出了未打印的订单" + other_order_ids);
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase20() {
		ReporterCSS.title("测试点: 供应链-分拣-分拣明细-按订单分拣,使用订单类型过滤");
		try {
			WeightCollectOrderFilterParam weightCollectOrderFilterParam = new WeightCollectOrderFilterParam();
			weightCollectOrderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectOrderFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setEnd_date(orderCycle.getCycle_start_time());
			weightCollectOrderFilterParam.setLimit(10);
			weightCollectOrderFilterParam.setOffset(0);

			List<OrderProcessBean> orderProcessList = orderService.getOrderProcessList();
			Assert.assertNotEquals(orderProcessList, null, "获取订单流信息失败");

			OrderProcessBean orderProcess = NumberUtil.roundNumberInList(orderProcessList);

			weightCollectOrderFilterParam.setOrder_process_type_id(orderProcess.getId());

			List<WeightCollectOrderBean> weightCollectOrders = weightService
					.getWeightCollectOrderInfo(weightCollectOrderFilterParam);
			Assert.assertNotEquals(weightCollectOrders, null, "供应链-分拣-分拣明细-按订单分拣,使用订单类型搜索过滤失败");

		} catch (Exception e) {
			logger.error("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣明细-按订单分拣,搜索过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase21() {
		try {
			ReporterCSS.title("测试点: 供应链-分拣-分拣任务-分拣明细-批量修改缺货");

			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

			String sku_id = detail.getSku_id();
			OrderAndSkuBean orderAndSku = new OrderAndSkuBean(order_id, sku_id);

			List<OrderAndSkuBean> orderSkuList = Arrays.asList(orderAndSku);

			boolean result = weightService.stationBatchOutOfStock(orderSkuList);
			Assert.assertEquals(result, true, "供应链-分拣-分拣任务-分拣明细-批量修改缺货失败");

		} catch (Exception e) {
			logger.error("供应链-分拣-分拣任务-分拣明细-批量修改缺货遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣任务-分拣明细-批量修改缺货遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase22() {
		try {
			ReporterCSS.title("测试点: 供应链-分拣-分拣任务-分拣明细-修改出库数");
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			BigDecimal set_weight = detail.getStd_unit_quantity().add(new BigDecimal("0.1"));

			SetWeightParam setWeightParam = new SetWeightParam();
			setWeightParam.setNeed_res(1);
			List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
			SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weight,
					false, null);
			weights.add(weight);

			setWeightParam.setWeights(weights);

			boolean result = weightService.stationSetWeight(setWeightParam);
			Assert.assertEquals(result, true, "供应链-分拣-分拣任务-分拣明细-修改出库数失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 的详细信息失败");

			detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

			String msg = null;
			if (detail.getStd_real_quantity().compareTo(set_weight) != 0) {
				msg = String.format("订单%s里的商品%s没有同步称重数", order_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String time_config_id = orderCycle.getTime_config_id();
			String date = orderCycle.getCycle_start_time().split(" ")[0];
			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(time_config_id, date);
			Assert.assertNotNull(weightSkuList, "新版称重软件,获取称重数据失败");

			WeightSkuBean weightSku = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotNull(weightSku, "新版称重软件,没有找到订单" + order_id + "中的称重商品" + sku_id);

			if (weightSku.getWeighting_quantity().compareTo(set_weight) != 0) {
				msg = String.format("订单%s里的商品%s的称重数没有同步到称重软件", order_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "供应链-分拣-分拣任务-分拣明细-修改出库数后,称重数据没有进行相关同步");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣任务-分拣明细-修改出库数遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣任务-分拣明细-修改出库数遇到错误: ", e);
		}
	}

	@Test
	public void stationWeightTestCase23() {
		try {
			ReporterCSS.title("测试点: 供应链-分拣-分拣任务-分拣明细-过滤全选批量修改缺货");
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			WeightCollectSkuFilterParam weightCollectSkuFilterParam = new WeightCollectSkuFilterParam();
			weightCollectSkuFilterParam.setTime_config_id(orderCycle.getTime_config_id());
			weightCollectSkuFilterParam.setStart_date(orderCycle.getCycle_start_time());
			weightCollectSkuFilterParam.setEnd_date(orderCycle.getCycle_end_time());
			weightCollectSkuFilterParam.setSearch(sku_id);
			weightCollectSkuFilterParam.setOp_way(1);

			boolean result = weightService.batchOutOfStock(weightCollectSkuFilterParam);
			Assert.assertEquals(result, true, "供应链-分拣-分拣任务-分拣明细-过滤全选批量修改缺货失败");

			List<WeightCollectSkuBean> weightCollectSkus = weightService
					.getWeightCollectSkuInfo(weightCollectSkuFilterParam);
			Assert.assertNotEquals(weightCollectSkus, null, "供应链-分拣-分拣明细-按商品分拣搜索过滤数据失败");

			String msg = null;
			for (WeightCollectSkuBean weightCollectSku : weightCollectSkus) {
				if (!weightCollectSku.getSku_id().equals(sku_id)) {
					msg = String.format("供应链-分拣-分拣明细-按商品分拣,输入商品ID[%s]过滤,过滤出了其他商品[%s]的商品信息", sku_id,
							weightCollectSku.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				for (WeightCollectSkuBean.Order order : weightCollectSku.getOrders()) {
					if (order.getOut_of_stock() != 1) {
						msg = String.format("供应链-分拣-分拣明细-按商品分拣,全选批量设置缺货后,订单%s下的商品%s没有标志成缺货", order.getOrder_id(),
								sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (order.getReal_quantity().compareTo(new BigDecimal("0")) != 0) {
						msg = String.format("供应链-分拣-分拣明细-按商品分拣,全选批量设置缺货后,订单%s下的商品%s出库数没有变为0", order.getOrder_id(),
								sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "供应链-分拣-分拣任务-分拣明细-过滤全选批量修改缺货,结果与预期不符");
		} catch (Exception e) {
			logger.error("供应链-分拣-分拣任务-分拣明细-过滤全选批量修改缺货遇到错误: ", e);
			Assert.fail("供应链-分拣-分拣任务-分拣明细-过滤全选批量修改缺货遇到错误: ", e);
		}
	}
}
