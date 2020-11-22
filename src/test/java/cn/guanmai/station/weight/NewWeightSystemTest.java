package cn.guanmai.station.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.SkuMeasurementBean;
import cn.guanmai.station.bean.delivery.DistributeOrderDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderStatusPreconfigParam;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.param.SortingProfileParam;
import cn.guanmai.station.bean.weight.WeightCategoryTreeBean;
import cn.guanmai.station.bean.weight.WeightSkuBean;
import cn.guanmai.station.bean.weight.param.OutOfStockParam;
import cn.guanmai.station.bean.weight.param.PrintInfoBean;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.weight.param.WeightDataFilterParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 8, 2019 12:14:25 PM 
* @des 新版称重相关测试用例
* @version 1.0 
*/
public class NewWeightSystemTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(NewWeightSystemTest.class);
	private OrderService orderService;
	private OrderTool orderTool;
	private OrderDetailBean orderDetail;
	private WeightService weightService;

	private OutStockService stockOutService;
	private DistributeService distributeService;
	private CategoryService categoryService;
	private String order_id;
	private int limit = 100; // 每页显示的商品、订单数,默认取值100

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		weightService = new WeightServiceImpl(headers);
		stockOutService = new OutStockServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		ProfileService profileService = new ProfileServiceImpl(headers);
		try {
			boolean result = profileService.updateSortingProfile(new SortingProfileParam());
			Assert.assertEquals(result, true, "系统-分拣设置失败");
		} catch (Exception e) {
			logger.error("前置操作-分拣设置遇到错误: ", e);
			Assert.fail("前置操作-分拣设置遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void createOrder() {
		try {
			order_id = orderTool.oneStepCreateOrder(8);
			Thread.sleep(1000);
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + " 状态改为分拣中,更新失败");
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

		} catch (Exception e) {
			logger.error("前置操作-创建订单遇到错误: ", e);
			Assert.fail("前置操作-创建订单遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase01() {
		ReporterCSS.title("测试点: 验证订单生成的称重数据(按订单分拣页面)");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");

			boolean result = true;
			String msg = null;
			for (Detail detail : orderDetail.getDetails()) {
				WeightSkuBean weightSku = targetWeightSkus.stream()
						.filter(s -> s.getSku_id().equals(detail.getSku_id())).findAny().orElse(null);
				if (weightSku == null) {
					Reporter.log("订单 " + order_id + " 的商品 " + detail.getSku_id() + " 没有出现在称重数据中");
					result = false;
				} else {
					if (weightSku.getQuantity().compareTo(detail.getQuantity()) != 0) {
						msg = String.format("订单%s里的商品%s下单数在称重软件显示的与预期不一致,预期:%s,实际:%s", order_id, detail.getSku_id(),
								detail.getQuantity(), weightSku.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					String weight_remark = weightSku.getRemark() == null ? "" : weightSku.getRemark();
					String detail_remark = detail.getSpu_remark() == null ? "" : detail.getSpu_remark();

					if (!weight_remark.equals(detail_remark)) {
						msg = String.format("订单%s里的商品%s备注在称重软件显示的与预期不一致,预期:%s,实际:%s", order_id, detail.getSku_id(),
								detail_remark, weight_remark);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 里的商品在称重页面展示的不准确");
		} catch (

		Exception e) {
			logger.error("获取称重数据遇到错误: ", e);
			Assert.fail("获取称重数据遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase02() {
		ReporterCSS.title("测试点: 按订单分拣-批量缺货上报");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<OutOfStockParam> outOfStocList = new ArrayList<OutOfStockParam>();
			OutOfStockParam outOfStock = null;
			for (Detail detail : orderDetail.getDetails()) {
				outOfStock = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"),
						detail.getSort_way());
				outOfStocList.add(outOfStock);
			}
			boolean result = weightService.outOfStock(outOfStocList);
			Assert.assertEquals(result, true, "按订单分拣-批量缺货上报失败");

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");

			for (WeightSkuBean weightSku : targetWeightSkus) {
				if (!weightSku.isOut_of_stock()) {
					result = false;
					ReporterCSS.warn("订单 " + order_id + " 的商品 " + weightSku.getSku_id() + " 设置了缺货,实际显示没缺货");
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 商品全部设置缺货,称重软件中实际存在商品显示没缺货");
		} catch (Exception e) {
			logger.error("分拣任务-分拣明细-批量缺货上报操作遇到错误: ", e);
			Assert.fail("分拣任务-分拣明细-批量缺货上报操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase03() {
		ReporterCSS.title("测试点: 按订单分拣-批量缺货上报-查看订单详细是否同步了数据");
		try {
			List<OutOfStockParam> outOfStocList = new ArrayList<OutOfStockParam>();
			OutOfStockParam outOfStock = null;
			for (Detail detail : orderDetail.getDetails()) {
				outOfStock = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"),
						detail.getSort_way());
				outOfStocList.add(outOfStock);
			}
			boolean result = weightService.outOfStock(outOfStocList);
			Assert.assertEquals(result, true, "按订单分拣-批量缺货上报失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				if (detail.getReal_quantity().compareTo(BigDecimal.ZERO) != 0 || detail.isOut_of_stock() == false) {
					msg = String.format("订单%s详细中的商品%s没有显示对应缺货状态", order_id, detail.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 商品全部设置缺货,订单详细没有同步数据");

		} catch (Exception e) {
			logger.error("分拣任务-分拣明细-批量缺货上报操作遇到错误: ", e);
			Assert.fail("分拣任务-分拣明细-批量缺货上报操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase04() {
		ReporterCSS.title("测试点: 按订单分拣-取消缺货状态-查看订单是否同步");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<OutOfStockParam> outOfStocList = new ArrayList<OutOfStockParam>();
			List<OutOfStockParam> unOutOfStocList = new ArrayList<OutOfStockParam>();
			OutOfStockParam outOfStock = null;
			OutOfStockParam unOutOfStock = null;
			for (Detail detail : orderDetail.getDetails()) {
				outOfStock = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"), 0);
				unOutOfStock = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"), 1);
				outOfStocList.add(outOfStock);
				unOutOfStocList.add(unOutOfStock);
			}
			boolean result = weightService.outOfStock(outOfStocList);
			Assert.assertEquals(result, true, "按订单分拣-批量缺货上报失败");

			result = weightService.unOutOfStock(unOutOfStocList);
			Assert.assertEquals(result, true, "按订单分拣-取消批量缺货上报失败");

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");

			for (WeightSkuBean weightSku : targetWeightSkus) {
				if (weightSku.isOut_of_stock()) {
					result = false;
					ReporterCSS.warn("订单 " + order_id + " 的商品 " + weightSku.getSku_id() + " 已经取消了缺货状态,实际显示还是缺货状态");
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 商品全部设置取消了缺货状态,实际还有商品显示还是缺货状态");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				if (detail.getReal_quantity().compareTo(detail.getQuantity()) != 0 || detail.isOut_of_stock() == true) {
					msg = String.format("订单%s详细中的商品%s仍然显示缺货状态", order_id, detail.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 商品全部取消缺货状态,订单详细没有同步数据");
		} catch (Exception e) {
			logger.error("批量取消缺货上报操作遇到错误: ", e);
			Assert.fail("批量取消缺货上报操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase05() {
		ReporterCSS.title("测试点: 新版称重软件-进行商品称重操作");
		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
		SetWeightParam.Weight weight = null;
		String sku_id = null;
		BigDecimal set_weigh = null;
		// 用来存储SKU和对应的称重数,用于后面的验证
		Map<String, BigDecimal> skuWeightMap = new HashMap<>();
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity();

			weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh, false,
					detail.getSort_way());
			weights.add(weight);

			skuWeightMap.put(sku_id, set_weigh);
		}

		setWeightParam.setWeights(weights);

		try {
			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "新版称重软件-进行商品称重操作失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");

			BigDecimal expect_weight = null;
			BigDecimal actul_weigth = null;
			for (WeightSkuBean weightSku : targetWeightSkus) {
				expect_weight = skuWeightMap.get(weightSku.getSku_id());
				actul_weigth = weightSku.getWeighting_quantity();
				if (expect_weight.compareTo(actul_weigth) != 0) {
					result = false;
					String msg = String.format("订单 %s 的商品 %s 存储的称重数和输入的不一致,输入: %s,存储: %s", order_id,
							weightSku.getSku_id(), expect_weight, actul_weigth);
					logger.info(msg);
					ReporterCSS.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 中的商品存在输入的称重数和存储的称重数不一致的情况");
		} catch (Exception e) {
			logger.error("新版称重软件-进行商品称重操作遇到错误: ", e);
			Assert.fail("新版称重软件-进行商品称重操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase06() {
		ReporterCSS.title("测试点: 新版称重软件-取消称重操作");
		ReporterCSS.step("步骤一: 先对商品进行称重操作");
		SetWeightParam setWeithtParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
		SetWeightParam unSetWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> unWeights = new ArrayList<SetWeightParam.Weight>();
		String sku_id = null;
		BigDecimal set_weigh = null;

		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity();

			SetWeightParam.Weight setWeitht = setWeithtParam.new Weight(order_id, sku_id, new BigDecimal("0"),
					set_weigh, false, 0);
			weights.add(setWeitht);

			SetWeightParam.Weight unSetWeight = setWeithtParam.new Weight(order_id, sku_id, set_weigh,
					new BigDecimal("0"), false, 1);
			unWeights.add(unSetWeight);
		}

		setWeithtParam.setWeights(weights);
		unSetWeightParam.setWeights(unWeights);

		try {
			boolean result = weightService.setWeight(setWeithtParam);
			Assert.assertEquals(result, true, "新版称重软件-进行称重操作失败");

			ReporterCSS.step("步骤二: 再对商品进行取消称重操作");
			result = weightService.setWeight(unSetWeightParam);
			Assert.assertEquals(result, true, "新版称重软件-进行取消称重操作失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");
			for (WeightSkuBean weightSku : targetWeightSkus) {
				if (weightSku.isHas_weighted()) {
					String msg = String.format("订单 %s 中的商品 %s 取消称重后,在称重页面,是否已经称重的值 has_weighted 没有变为 false", order_id,
							weightSku.getSku_id());
					logger.info(msg);
					ReporterCSS.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "取消称重后,存在商品的 has_weighted 字段值没有变回 false");

			orderDetail = orderService.getOrderDetailById(order_id);
			for (Detail detail : orderDetail.getDetails()) {
				if (detail.getWeighted() == 1) {
					String msg = String.format("订单 %s 中的商品 %s 取消称重后,在订单详细页面,是否已经称重的值 weighted 没有变为 0", order_id,
							detail.getSku_id());
					logger.info(msg);
					ReporterCSS.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "取消称重后,在订单详细页面,存在商品的 weighted 字段值没有变回 0");
		} catch (Exception e) {
			logger.error("新版称重软件-取消称重操作遇到错误: ", e);
			Assert.fail("新版称重软件-取消称重操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase07() {
		ReporterCSS.title("测试点: 新版称重软件-加入已称重数");
		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();

		SetWeightParam addWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> addWeights = new ArrayList<SetWeightParam.Weight>();

		BigDecimal weight = null;
		BigDecimal add_weight = null;
		Map<String, BigDecimal> weightMap = new HashMap<>();
		for (Detail detail : orderDetail.getDetails()) {
			weight = detail.getStd_unit_quantity().add(new BigDecimal("0.1"));
			add_weight = weight.add(new BigDecimal("0.2"));
			// 第一次称重数参数
			SetWeightParam.Weight setWeight = setWeightParam.new Weight(order_id, detail.getSku_id(),
					new BigDecimal("0"), weight, false, 0);
			weights.add(setWeight);

			// 加入称重数参数,原有称重数加入0.2
			SetWeightParam.Weight addWeight = setWeightParam.new Weight(order_id, detail.getSku_id(), weight,
					add_weight, true, 1);
			addWeights.add(addWeight);

			weightMap.put(detail.getSku_id(), add_weight);
		}

		setWeightParam.setWeights(weights);
		addWeightParam.setWeights(addWeights);
		try {
			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "新版称重软件进行称重操作失败");

			result = weightService.setWeight(addWeightParam);
			Assert.assertEquals(result, true, "新版称重软件进行加入称重数操作失败");

			// 验证称重后的数据
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time().substring(0, 10);

			List<WeightSkuBean> weightSkus = weightService.getWeightSkus(time_config_id, cycle_start_time);
			Assert.assertNotEquals(weightSkus, null, "获取称重数据失败");

			List<WeightSkuBean> targetWeightSkus = weightSkus.stream().filter(s -> s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetWeightSkus.size(), orderDetail.getDetails().size(),
					"获取到的订单 " + order_id + " 称重商品数量与预期不符");

			BigDecimal expected_weight = null;
			BigDecimal actual_weight = null;
			for (WeightSkuBean weightSku : targetWeightSkus) {
				expected_weight = weightMap.get(weightSku.getSku_id());
				actual_weight = weightSku.getWeighting_quantity();
				if (actual_weight.compareTo(expected_weight) != 0) {
					String msg = String.format("订单 %s 里的商品 %s 称重数与预期的不一致,预期:%s,实际:%s", order_id, weightSku.getSku_id(),
							expected_weight, actual_weight);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "称重软件里订单 " + order_id + " 存在商品预期称重数和实际称重数不一致");

			// 验证订单里的称重数
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			for (Detail detail : orderDetail.getDetails()) {
				expected_weight = weightMap.get(detail.getSku_id());
				actual_weight = detail.getStd_real_quantity();
				if (actual_weight.compareTo(expected_weight) != 0) {
					String msg = String.format("订单 %s 详细页面显示的商品 %s 称重数与预期的不一致,预期:%s,实际:%s", order_id,
							detail.getSku_id(), expected_weight, actual_weight);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "加入称重数后,订单 " + order_id + " 详细页面里存在商品预期称重数和实际称重数不一致");

			Reporter.log("验证称重数是否同步了配送单");
			List<String> order_ids = new ArrayList<>();
			order_ids.add(order_id);
			result = distributeService.createPrintLog(order_ids);
			Assert.assertEquals(result, true, "创建配送单打印日志失败");
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到目标配送单 " + order_id + " 的详细信息");

			String msg = null;
			for (DistributeOrderDetailBean.Detail detail : distributeOrderDetail.getDetails()) {
				String sku_id = detail.getSku_id();
				if (detail.getReal_weight().compareTo(weightMap.get(sku_id)) != 0) {
					msg = String.format("配送单 %s 中的商品 %s 记录的出库数与预期的不一致,预期: %s,实际 %s", order_id, sku_id,
							weightMap.get(sku_id), detail.getReal_weight());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "加入已称重数后,配送单 " + order_id + "中的出库数据没有更新");
		} catch (Exception e) {
			logger.error("新版称重软件-加入已称重数操作遇到错误: ", e);
			Assert.fail("新版称重软件-加入已称重数操作遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase08() {
		ReporterCSS.title("测试点: 新版称重软件,获取打印标签需要的数据");
		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
		String sku_id = null;
		BigDecimal set_weigh = null;
		// 用来存储SKU和对应的称重数,用于后面的验证
		Map<String, BigDecimal> skuWeightMap = new HashMap<>();

		// 用于打印标签的参数
		List<OrderAndSkuBean> printInfoParams = new ArrayList<>();
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity().add(new BigDecimal("0.25"));

			SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh,
					false, detail.getSort_way());
			weights.add(weight);

			skuWeightMap.put(sku_id, set_weigh);

			printInfoParams.add(new OrderAndSkuBean(order_id, sku_id));
		}
		setWeightParam.setWeights(weights);
		try {
			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "新版称重软件-进行商品称重操作失败");

			List<PrintInfoBean> printInfos = weightService.getWeightSkuPrintInfo(printInfoParams);
			Assert.assertNotEquals(printInfos, null, "获取标签打印数据失败");

			Assert.assertEquals(printInfos.size(), skuWeightMap.size(), "获取标签打印数据总数与预期不一致");

			// 验证标签上的称重数
			BigDecimal expected_weight = null;
			BigDecimal actual_weight = null;
			for (PrintInfoBean printInfo : printInfos) {
				sku_id = printInfo.getSku_id();
				expected_weight = skuWeightMap.get(sku_id);
				actual_weight = printInfo.getWeighting_quantity();
				if (expected_weight.compareTo(actual_weight) != 0) {
					String msg = String.format("订单%s中的商品%s称重后打印的标签记录的称重数与预期不符,预期:%s,实际:%s", order_id, sku_id,
							expected_weight, actual_weight);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}

			// 验证其他基本信息
			String address_name = orderDetail.getCustomer().getExtender().getResname();
			String address_id = orderDetail.getCustomer().getAddress_id();
			String actual_address_name = null; // 商户名称
			String actual_address_id = null; // 商户ID
			String actual_sku_name = null; // 商品名称
			String actual_remark = null; // 订单商品备注
			BigDecimal actual_quantity = null; // 下单数
			String actual_sale_unit_name = null; // 销售单位
			String actual_std_unit_name = null; // 基本单位
			BigDecimal actual_sale_ratio = null; // 销售单位与基本单位的比率
			PrintInfoBean printInfo = null;
			String msg = null;
			for (Detail detail : orderDetail.getDetails()) {
				printInfo = printInfos.stream().filter(i -> i.getSku_id().equals(detail.getSku_id())).findAny()
						.orElse(null);
				actual_address_name = printInfo.getOrder().getAddress_name();
				if (!actual_address_name.equals(address_name)) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商户名称不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							address_name, actual_address_name);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
				actual_address_id = printInfo.getOrder().getAddress_id();
				if (!actual_address_id.equals(address_id)) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商户ID不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							address_id, actual_address_id);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				actual_sku_name = printInfo.getSku_name();
				if (!actual_sku_name.equals(detail.getSku_name())) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商品名称不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							detail.getSku_name(), actual_sku_name);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				actual_remark = printInfo.getRemark();
				if (!actual_remark.equals(detail.getSpu_remark())) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商品备注不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							detail.getSpu_remark(), actual_remark);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				actual_quantity = printInfo.getQuantity();
				if (actual_quantity.compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商品下单数不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							detail.getQuantity(), actual_quantity);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				actual_sale_unit_name = printInfo.getSale_unit_name();
				actual_std_unit_name = printInfo.getStd_unit_name();
				actual_sale_ratio = printInfo.getSale_ratio();
				String sales_specification = actual_sale_ratio + actual_std_unit_name + "/" + actual_sale_unit_name;
				String actual_sales_specification = detail.getSale_ratio() + detail.getStd_unit_name() + "/"
						+ detail.getSale_unit_name();
				if (!sales_specification.equals(actual_sales_specification)) {
					msg = String.format("订单%s中的商品%s称重后的打印的标签商品销售单位不对 ,预期 :%s,实际:%s", order_id, detail.getSku_id(),
							sales_specification, actual_sales_specification);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "中的商品称重后打印的标签记录信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新版称重软件-获取打印标签数据遇到错误: ", e);
			Assert.fail("新版称重软件-获取打印标签数据遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase09() {
		ReporterCSS.title("测试点: 打印商品称重标签");
		// 用于打印标签的参数
		String sku_id = null;
		List<OrderAndSkuBean> printParam = new ArrayList<>();
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			printParam.add(new OrderAndSkuBean(order_id, sku_id));
		}
		try {
			boolean result = weightService.printSkuWeight(printParam);
			Assert.assertEquals(result, true, "打印商品称重标签失败");
		} catch (Exception e) {
			logger.error("新版称重软件-打印标签遇到错误: ", e);
			Assert.fail("新版称重软件-打印标签遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase10() {
		ReporterCSS.title("测试点: 新版称重软件-进行商品称重操作,称重后验证订单详细数据是否更新");
		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
		String sku_id = null;
		BigDecimal set_weigh = null;
		// 用来存储SKU和对应的称重数,用于后面的验证
		Map<String, BigDecimal> skuWeightMap = new HashMap<>();
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity().add(new BigDecimal("0.25"));

			SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh,
					false, detail.getSort_way());
			weights.add(weight);

			skuWeightMap.put(sku_id, set_weigh);
		}
		setWeightParam.setWeights(weights);
		try {
			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "新版称重软件-进行商品称重操作失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			BigDecimal expected_money = BigDecimal.ZERO; // 预期称重后的订单金额
			BigDecimal expected_std_real_quantity = null;
			BigDecimal actual_std_real_quantity = null;
			for (Detail detail : orderDetail.getDetails()) {
				sku_id = detail.getSku_id();
				expected_std_real_quantity = skuWeightMap.get(sku_id);
				actual_std_real_quantity = detail.getStd_real_quantity();
				expected_money = expected_money.add(expected_std_real_quantity
						.divide(detail.getSale_ratio(), 4, BigDecimal.ROUND_HALF_UP).multiply(detail.getSale_price()));
				if (expected_std_real_quantity.compareTo(actual_std_real_quantity) != 0) {
					String msg = String.format("订单 %s 称重后,订单详细页面显示的的商品 %s 存储的称重数和输入的不一致,输入: %s,存储: %s", order_id,
							sku_id, expected_std_real_quantity, actual_std_real_quantity);
					logger.info(msg);
					ReporterCSS.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 称重后,订单详细页面显示的称重数和输入的称重数不一致");

			expected_money = expected_money.setScale(2, BigDecimal.ROUND_HALF_UP); // 总金额四舍五入

			BigDecimal actual_money = orderDetail.getReal_price();

			String msg = String.format("订单 %s 称重后预期的总额和实际的订单总额不一致,预期:%s,实际:%s", order_id, expected_money, actual_money);
			Assert.assertEquals(expected_money.compareTo(actual_money) == 0, true, msg);
		} catch (Exception e) {
			logger.error("新版称重软件-进行商品称重操作遇到错误: ", e);
			Assert.fail("新版称重软件-进行商品称重操作遇到错误: ", e);
		}
	}

	@Test(timeOut = 40000)
	public void newWeightSystemTestCase11() {
		ReporterCSS.title("测试点: 称重分拣设置商品缺货,查看出库单是否同步数据");
		List<OutOfStockParam> outOfStockParam = new ArrayList<OutOfStockParam>();
		String sku_id = null;

		// 用来存储SKU和对应的称重数,用于后面的验证
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			outOfStockParam.add(new OutOfStockParam(order_id, sku_id, new BigDecimal("0"), 0));
		}

		try {
			boolean result = weightService.outOfStock(outOfStockParam);
			Assert.assertEquals(result, true, "称重分拣设置商品缺货操作失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			int loop_count = 10;
			List<OutStockSheetBean> stockOutSheetList = null;
			OutStockSheetBean stockOutSheet = null;

			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setType(1);
			outStockSheetFilterParam.setStart(todayStr);
			outStockSheetFilterParam.setEnd(todayStr);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setSearch_text(order_id);

			while (loop_count > 0) {
				stockOutSheetList = stockOutService.searchOutStockSheet(outStockSheetFilterParam);
				Assert.assertNotEquals(stockOutSheetList, null, "出库单列表搜索过滤查询失败");

				if (stockOutSheetList.size() > 0) {
					stockOutSheet = stockOutSheetList.stream().filter(s -> s.getId().equals(order_id)).findAny()
							.orElse(null);
					break;
				} else {
					loop_count -= 1;
					Thread.sleep(2000);
				}
			}
			Assert.assertNotEquals(stockOutSheet, null, "订单创建后20秒,没有查询到对应的出库单" + order_id);

			OutStockDetailBean stockOutDetail = stockOutService.getOutStockDetailInfo(stockOutSheet.getId());
			Assert.assertNotEquals(stockOutDetail, null, "获取出库单详情信息失败");

			BigDecimal actual_quantity = null;
			for (OutStockDetailBean.Detail detail : stockOutDetail.getDetails()) {
				actual_quantity = detail.getQuantity().multiply(detail.getSale_ratio());
				if (detail.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
					result = false;
					String msg = String.format("出库单 %s 中的商品 %s 实际出库数与预期出库数不一致,预期:%s,实际:%s", order_id, sku_id, 0,
							actual_quantity);
					ReporterCSS.warn(msg);
					logger.info(msg);
				}
			}

			ReporterCSS.title("测试点: 称重后,再次查看称重数是否同步到出库单");
			List<OutOfStockParam> un_out_of_stock_list = new ArrayList<OutOfStockParam>();
			SetWeightParam setWeightParam = new SetWeightParam();
			List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
			OutOfStockParam un_out_of_stock = null;
			BigDecimal set_weigh = null;
			// 用来存储SKU和对应的称重数,用于后面的验证
			Map<String, List<SkuMeasurementBean>> skuMeasurementMap = categoryService.getSkuMeasurementMap();
			Assert.assertNotEquals(skuMeasurementMap, null, "获取基本单位的映射关系列表信息失败");

			Map<String, BigDecimal> skuWeightMap = new HashMap<>();
			BigDecimal std_retio = null;
			for (Detail detail : orderDetail.getDetails()) {
				String std_unit_name = detail.getStd_unit_name();
				String std_unit_name_forsale = detail.getStd_unit_name_forsale();

				std_retio = new BigDecimal("1");
				if (skuMeasurementMap.containsKey(std_unit_name)) {
					List<SkuMeasurementBean> skuMeasurementList = skuMeasurementMap.get(std_unit_name);
					SkuMeasurementBean skuMeasurement = skuMeasurementList.stream()
							.filter(s -> s.getStd_unit_name_forsale().equals(std_unit_name_forsale)).findAny()
							.orElse(null);
					std_retio = skuMeasurement.getStd_ratio();

				}
				sku_id = detail.getSku_id();
				set_weigh = detail.getStd_unit_quantity().add(new BigDecimal("0.25"));

				SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"),
						set_weigh, false, detail.getSort_way());
				weights.add(weight);

				un_out_of_stock = new OutOfStockParam(order_id, sku_id, new BigDecimal("0"), 1);
				un_out_of_stock_list.add(un_out_of_stock);
				skuWeightMap.put(sku_id, set_weigh.multiply(std_retio));
			}
			result = weightService.unOutOfStock(un_out_of_stock_list);
			Assert.assertEquals(result, true, "取消称重操作失败");

			setWeightParam.setWeights(weights);
			result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "进行称重操作失败");

			stockOutDetail = stockOutService.getOutStockDetailInfo(stockOutSheet.getId());
			Assert.assertNotEquals(stockOutDetail, null, "获取出库单详情信息失败");

			for (OutStockDetailBean.Detail detail : stockOutDetail.getDetails()) {
				sku_id = detail.getId();
				actual_quantity = detail.getQuantity().multiply(detail.getSale_ratio()).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				if (actual_quantity.compareTo(skuWeightMap.get(sku_id)) != 0) {
					result = false;
					String msg = String.format("出库单 %s 中的商品 %s 实际出库数与预期出库数不一致,预期:%s,实际:%s", order_id, sku_id,
							skuWeightMap.get(sku_id), actual_quantity);
					ReporterCSS.warn(msg);
					logger.info(msg);
				}
			}
			Assert.assertEquals(result, true, "出库单 " + order_id + " 中存在实际出库数与预期出库数不一致的商品");
		} catch (Exception e) {
			logger.error("验证称重数据同步出库单遇到错误: ", e);
			Assert.fail("验证称重数据同步出库单遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase12() {
		ReporterCSS.title("测试点: 订单称重后,查询配送单里的数据是否同步");

		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
		String sku_id = null;
		BigDecimal set_weigh = null;
		// 用来存储SKU和对应的称重数,用于后面的验证
		Map<String, BigDecimal> skuWeightMap = new HashMap<>();
		for (Detail detail : orderDetail.getDetails()) {
			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity().add(new BigDecimal("0.25"));

			SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh,
					false, detail.getSort_way());
			weights.add(weight);
			skuWeightMap.put(sku_id, set_weigh);
		}
		setWeightParam.setWeights(weights);
		try {
			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "进行称重分拣操作失败");

			List<String> order_ids = new ArrayList<>();
			order_ids.add(order_id);
			result = distributeService.createPrintLog(order_ids);
			Assert.assertEquals(result, true, "创建配送单打印日志失败");
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到目标配送单 " + order_id + " 的详细信息");

			Assert.assertEquals(distributeOrderDetail.getDetails().size(), skuWeightMap.size(), " 配送单中记录的商品总数与预期的不一致");

			String msg = null;
			for (DistributeOrderDetailBean.Detail detail : distributeOrderDetail.getDetails()) {
				sku_id = detail.getSku_id();
				if (detail.getReal_weight().compareTo(skuWeightMap.get(sku_id)) != 0) {
					msg = String.format("配送单 %s 中的商品 %s 记录的出库数与预期的不一致,预期: %s,实际 %s", order_id, sku_id,
							skuWeightMap.get(sku_id), detail.getReal_weight());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "称重分拣后,配送单 " + order_id + "中的出库数据没有更新");
		} catch (Exception e) {
			logger.error("验证称重数据同步出库单遇到错误: ", e);
			Assert.fail("验证称重数据同步出库单遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase13() {
		ReporterCSS.title("测试点: 称重设置商品缺货,查询配送单里的数据是否同步");
		List<OutOfStockParam> outOfStocList = new ArrayList<OutOfStockParam>();
		OutOfStockParam outOfStock = null;

		for (Detail detail : orderDetail.getDetails()) {
			outOfStock = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"), detail.getSort_way());
			outOfStocList.add(outOfStock);
		}

		try {
			boolean result = weightService.outOfStock(outOfStocList);
			Assert.assertEquals(result, true, "按订单分拣-批量缺货上报失败");
			List<String> order_ids = new ArrayList<>();
			order_ids.add(order_id);
			result = distributeService.createPrintLog(order_ids);
			Assert.assertEquals(result, true, "创建配送单打印日志失败");
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到目标配送单 " + order_id + " 的详细信息");

			Assert.assertEquals(distributeOrderDetail.getDetails().size(), outOfStocList.size(), " 配送单中记录的商品总数与预期的不一致");

			String msg = null;
			for (OutOfStockParam outOfStockParam : outOfStocList) {
				DistributeOrderDetailBean.Detail detail = distributeOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(outOfStockParam.getSku_id())).findAny().orElse(null);
				if (detail.getReal_weight().compareTo(BigDecimal.ZERO) != 0) {
					msg = String.format("配送单 %s 中的商品 %s 没有同步缺货状态", order_id, outOfStockParam.getSku_id());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单商品缺货上报后,配送单 " + order_id + "中的出库数据没有更新");
		} catch (Exception e) {
			logger.error("验证称重数据同步出库单遇到错误: ", e);
			Assert.fail("验证称重数据同步出库单遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase14() {
		ReporterCSS.title("测试点: 新版称重软件-按计重商品分拣-获取称重商品结果树");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);

			String time_config_id = orderCycle.getTime_config_id();
			String start_time = orderCycle.getCycle_start_time().split(" ")[0];

			List<WeightCategoryTreeBean> weightCategoryTrees = weightService.getWeightCategoryTree(time_config_id,
					start_time, true);

			Assert.assertNotNull(weightCategoryTrees, "新版称重软件-按计重商品分拣-获取称重商品结果树失败");

			List<String> actual_spu_ids = new ArrayList<>();
			for (WeightCategoryTreeBean weightCategoryTree : weightCategoryTrees) {
				for (WeightCategoryTreeBean.Category2 category2 : weightCategoryTree.getCategory2s()) {
					for (WeightCategoryTreeBean.Category2.Spu spu : category2.getSpus()) {
						actual_spu_ids.add(spu.getSpu_id());
					}
				}
			}

			List<String> spu_ids = orderDetail.getDetails().stream().filter(d -> d.getIs_weigh() == 1)
					.map(d -> d.getSpu_id()).collect(Collectors.toList());

			boolean reuslt = actual_spu_ids.containsAll(spu_ids);
			if (!reuslt) {
				spu_ids.removeAll(actual_spu_ids);
				Assert.assertEquals(reuslt, true,
						"新版称重软件-按计重商品分拣-获取的称重商品结果树-应该包含订单" + order_id + "中的所有SPU ID,但订中的商品 " + spu_ids + " 没有出现");
			}

			WeightDataFilterParam outStockSheetFilterParam = new WeightDataFilterParam();
			outStockSheetFilterParam.setTime_config_id(time_config_id);
			outStockSheetFilterParam.setDate(start_time);
			outStockSheetFilterParam.setSpu_ids(spu_ids);

			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(outStockSheetFilterParam);
			Assert.assertNotNull(weightSkuList, "新版称重软件-按计重商品分拣-获取称重数据失败");

			List<WeightSkuBean> targetWeightSkuList = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id)).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				if (detail.getIs_weigh() == 1) {
					WeightSkuBean weightSku = targetWeightSkuList.stream()
							.filter(w -> w.getSku_id().equals(detail.getSku_id())).findAny().orElse(null);
					if (weightSku == null) {
						msg = String.format("订单%s中的商品%s没有出现在按计重商品分拣的页面中", order_id, detail.getSpu_id());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
						continue;
					}
					if (weightSku.getQuantity().compareTo(detail.getQuantity()) != 0) {
						msg = String.format("订单%s中的商品%s在按计重商品分拣的页面中显示的称重数与预期不一致,预期:%s,实际:%s", order_id,
								detail.getSpu_id(), detail.getQuantity(), weightSku.getQuantity());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "新版称重软件-按计重商品分拣-查询到的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("新版称重软件-按计重商品分拣-获取称重商品结果树遇到错误: ", e);
			Assert.fail("新版称重软件-按计重商品分拣-获取称重商品结果树遇到错误: ", e);
		}
	}

	@Test
	public void newWeightSystemTestCase15() {
		ReporterCSS.title("测试点: 查看指定运营时间下的所有可称重订单");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time();
			String cycle_end_time = orderCycle.getCycle_end_time();

			// 这里参数很奇怪,要格式化成 2020-07-07-06-00-00
			String time_format = cycle_start_time.replace(":", "-").replace(" ", "-") + "-00";

			OrderStatusPreconfigParam orderStatusPreconfigParam = new OrderStatusPreconfigParam();
			orderStatusPreconfigParam.setTime_config_id(time_config_id);
			orderStatusPreconfigParam.setStart_cycle_time(time_format);
			orderStatusPreconfigParam.setEnd_cycle_time(time_format);
			orderStatusPreconfigParam.setBatch_remark("批量修改订单状态");
			orderStatusPreconfigParam.setQuery_type(2);
			orderStatusPreconfigParam.setCount("all");
			orderStatusPreconfigParam.setTo_status(5);

			boolean result = orderService.preconfigUpdateOrderStatus(orderStatusPreconfigParam);
			Assert.assertEquals(result, true, "按预设数修改订单状态失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setCycle_start_time(cycle_start_time);
			orderFilterParam.setCycle_end_time(cycle_end_time);
			int offset = 0;
			orderFilterParam.setLimit(limit);

			List<OrderBean> orderList = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> temp_order_list = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(temp_order_list, null, "搜索过滤订单失败");
				orderList.addAll(temp_order_list);
				if (temp_order_list.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderList.stream().filter(o -> o.getStatus() > 1).map(o -> o.getId())
					.collect(Collectors.toList());

			List<String> weight_order_ids = weightService.getWeightOrders(time_config_id,
					cycle_start_time.substring(0, 10));

			Reporter.log("期望的订单列表:" + order_ids);
			Reporter.log("实际的订单列表:" + weight_order_ids);
			if (order_ids.size() > weight_order_ids.size()) {
				order_ids.removeAll(weight_order_ids);
				Reporter.log("订单列表中的如下订单,没有出现在称重软件-按订单分拣列表中 " + order_ids);
				Assert.assertEquals(order_ids.size(), 0, "存在部分订单没有出现在称重软件中," + order_ids);
			}
		} catch (Exception e) {
			logger.error("新版称重软件-按订单分拣-获取数据遇到错误: ", e);
			Assert.fail("新版称重软件-按订单分拣-获取数据遇到错误: ", e);
		}
	}

}
