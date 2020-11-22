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

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.weight.CategoryTree;
import cn.guanmai.station.bean.weight.WeightTag;
import cn.guanmai.station.bean.weight.param.ChecklistParam;
import cn.guanmai.station.bean.weight.param.DiffOrderWeighParam;
import cn.guanmai.station.bean.weight.param.OldSetWeighParam;
import cn.guanmai.station.bean.weight.param.PackWeighDataParam;
import cn.guanmai.station.bean.weight.param.UnionDispatchParam;
import cn.guanmai.station.bean.weight.param.WeighAllDataParam;
import cn.guanmai.station.bean.weight.param.WeighTaskParam;
import cn.guanmai.station.bean.weight.param.OldSetWeighParam.Data;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 10, 2019 10:10:45 AM 
* @des 老版本的称重软件相关测试用例
* @version 1.0 
*/
public class OldWeightSystemTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OldWeightSystemTest.class);
	private String station_id;
	private OrderService orderService;
	private OrderTool orderTool;
	private OrderDetailBean orderDetail;
	private WeightService weightService;
	private LoginUserInfoService loginUserInfoService;
	private String order_id;
	private String time_config_id;
	private String cycle_start_time;
	private String todayStr;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		orderTool = new OrderTool(headers);
		weightService = new WeightServiceImpl(headers);
		todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();
		} catch (Exception e) {
			logger.error("前置操作-获取站点ID遇到错误: ", e);
			Assert.fail("前置操作-获取站点ID遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			time_config_id = orderCycle.getTime_config_id();
			cycle_start_time = orderCycle.getCycle_start_time();
		} catch (Exception e) {
			logger.error("前置操作-创建订单遇到错误: ", e);
			Assert.fail("前置操作-创建订单遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase01() {
		Reporter.log("测试点: 老PC称重-分拣单核查");
		ChecklistParam paramBean = new ChecklistParam();
		paramBean.setStation_id(station_id);
		paramBean.setCycle_start_time(cycle_start_time + ":00");
		paramBean.setSort_by_amiba("False");
		paramBean.setTime_config_id(time_config_id);
		paramBean.setDate(todayStr);
		try {
			boolean result = weightService.getWeighChecklist(paramBean);
			Assert.assertEquals(result, true, "老PC称重-分拣单核查失败");
		} catch (Exception e) {
			logger.error("老PC称重-分拣单核查遇到错误: ", e);
			Assert.fail("老PC称重-分拣单核查遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase02() {
		Reporter.log("老版本的称重软件(PC称重) 统配称重快速打印");
		UnionDispatchParam paramBean = new UnionDispatchParam();
		paramBean.setStation_id(station_id);
		paramBean.setTime_config_id(time_config_id);
		paramBean.setWeigh_filter(0);
		paramBean.setCycle_start_time(cycle_start_time + ":00");
		try {
			boolean result = weightService.unionDispath(paramBean);
			Assert.assertEquals(result, true, "老版本的称重软件(PC称重) 统配称重快速打印失败");
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 统配称重快速打印遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 统配称重快速打印遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase03() {
		Reporter.log("老版本的称重软件(PC称重) 获取所有的称重数据");
		WeighAllDataParam paramBean = new WeighAllDataParam();
		paramBean.setStation_id(station_id);
		paramBean.setTime_config_id(time_config_id);
		paramBean.setUnion_dispatch(true);
		paramBean.setCycle_start_time(cycle_start_time + ":00");
		try {
			boolean result = weightService.getWeighAllData(paramBean);
			Assert.assertEquals(result, true, "老版本的称重软件(PC称重) 获取所有的称重数据失败");
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 获取所有的称重数据遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 获取所有的称重数据遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase04() {
		Reporter.log("测试点:老版本的称重软件(PC称重) 根据订单相关想信息获取称重数据");
		try {
			List<CategoryTree> categoryTrees = weightService.getCategoryTree(station_id, time_config_id,
					cycle_start_time + " 00:00");
			Assert.assertNotEquals(categoryTrees, null, "称重软件里获取站点所有的一、二级分类信息失败");

			Map<String, String> catual_spuMap = new HashMap<>();
			JSONArray category2_ids = new JSONArray();
			for (Detail detail : orderDetail.getDetails()) {
				String category1_name = detail.getCategory_title_1();
				String category2_name = detail.getCategory_title_2();

				CategoryTree categoryTree = categoryTrees.stream().filter(ct -> ct.getName().equals(category1_name))
						.findAny().orElse(null);
				Assert.assertNotEquals(categoryTree, null,
						"订单" + order_id + "里的商品 " + detail.getSku_id() + " 在称重软件里没有找到所属的一级分类");

				CategoryTree.Category2 category2 = categoryTree.getList().stream()
						.filter(ct2 -> ct2.getName().equals(category2_name.trim())).findAny().orElse(null);
				Assert.assertNotEquals(category2, null,
						"订单" + order_id + "里的商品 " + detail.getSku_id() + " 在称重软件里没有找到所属的二级分类");

				if (!category2_ids.contains(category2.getId())) {
					category2_ids.add(category2.getId());
				}
				catual_spuMap.put(detail.getSpu_id(), detail.getSpu_id());

			}

			Map<String, String> spuMap = weightService.getSpuByCategory2(station_id, time_config_id, cycle_start_time,
					category2_ids);

			catual_spuMap.keySet().removeAll(spuMap.keySet());
			Assert.assertEquals(catual_spuMap.size() == 0, true,
					"称重软件拉取到的SPU集合没有包含订单" + order_id + "中的商品所属SPU,比如  " + catual_spuMap.keySet());

		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 获取称重数据遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 获取称重数据遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase05() {
		Reporter.log("老版本的称重软件(PC称重) 差异对比");
		DiffOrderWeighParam paramBean = new DiffOrderWeighParam();
		paramBean.setStation_id(station_id);
		paramBean.setTime_config_id(time_config_id);
		paramBean.setUnion_dispatch(false);
		paramBean.setIs_weight(true);
		paramBean.setCycle_start_time(cycle_start_time + ":00");
		try {
			boolean result = weightService.getDiffOrderWeight(paramBean);
			Assert.assertEquals(result, true, "老版本的称重软件(PC称重) 差异对比失败");
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 差异对比遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 差异对比遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase06() {
		Reporter.log("老版本的称重软件(PC称重) 获取称重任务");
		cycle_start_time = cycle_start_time + ":00";

		JSONArray spu_ids = new JSONArray();

		String spu_id = null;
		List<Detail> details = orderDetail.getDetails().stream().filter(d -> d.getIs_weigh() == 1)
				.collect(Collectors.toList());
		for (Detail detail : details) {
			spu_id = detail.getSpu_id();
			if (!spu_ids.contains(spu_id)) {
				spu_ids.add(spu_id);
			}
		}

		try {
			Reporter.log("步骤一:生成称重数据");
			PackWeighDataParam packWeighDataParam = new PackWeighDataParam(station_id, time_config_id, true,
					cycle_start_time);
			boolean result = weightService.packWeighData(packWeighDataParam);
			Assert.assertEquals(result, true, "生成称重数据失败");

			Reporter.log("步骤二:拉取订单中的下单商品的称重数据");
			WeighTaskParam paramBean = new WeighTaskParam(station_id, true, true, "01", time_config_id,
					cycle_start_time, false, spu_ids);
			List<WeightTag> weightTags = weightService.getWeighTask(paramBean);
			Assert.assertEquals(weightTags != null && weightTags.size() > 0, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			JSONArray product_id_list = new JSONArray();
			for (int i = 0; i < weightTags.size(); i++) {
				WeightTag weightTag = weightTags.get(i);
				product_id_list.add(weightTag.getSku_id());
			}

			paramBean = new WeighTaskParam(station_id, true, time_config_id, cycle_start_time, false, product_id_list);

			weightTags = weightService.getWeighTask(paramBean);
			Assert.assertEquals(weightTags != null, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			// 获取指定SKU所有的订单称重任务
			String product_id = null;
			List<WeightTag> target_weight_tags = new ArrayList<WeightTag>();
			for (Object obj : product_id_list) {
				product_id = String.valueOf(obj);
				paramBean = new WeighTaskParam(station_id, true, "01", time_config_id, cycle_start_time, false,
						product_id);
				weightTags = weightService.getWeighTask(paramBean);

				List<WeightTag> temp_list = weightTags.stream().filter(wt -> wt.getOrder_id().equals(order_id))
						.collect(Collectors.toList());
				target_weight_tags.addAll(temp_list);
			}
			// 验证称重软件拉取的数据(与订单详细信息对比)

			WeightTag weightTag = null;
			String msg = null;
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				weightTag = target_weight_tags.stream().filter(wt -> wt.getSku_id().equals(sku_id)).findAny()
						.orElse(null);

				if (weightTag == null) {
					msg = String.format("订单%s里的商品%s在称重软件中没找到", order_id, sku_id);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (weightTag.getAmount().compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s里的商品%s在称重软件中显示的下单数与预期不符,预期:%s,实际:%s", order_id, sku_id,
							detail.getQuantity(), weightTag.getAmount());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 在老版称重软件显示的商品信息与订单详细中的不一致");
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase07() {
		Reporter.log("老版本的称重软件(PC称重) 进行称重操作");
		cycle_start_time = cycle_start_time + ":00";

		JSONArray spu_ids = new JSONArray();

		String spu_id = null;
		for (Detail detail : orderDetail.getDetails()) {
			spu_id = detail.getSpu_id();
			if (detail.getIs_weigh() == 1) {
				if (!spu_ids.contains(spu_id)) {
					spu_ids.add(spu_id);
				}
			}
		}
		try {
			Reporter.log("步骤一:生成称重数据");
			PackWeighDataParam packWeighDataParam = new PackWeighDataParam(station_id, time_config_id, true,
					cycle_start_time);
			boolean result = weightService.packWeighData(packWeighDataParam);
			Assert.assertEquals(result, true, "生成称重数据失败");

			Reporter.log("步骤二:拉取订单中的下单商品的称重数据");
			WeighTaskParam weighTaskParam = new WeighTaskParam(station_id, true, true, "01", time_config_id,
					cycle_start_time, false, spu_ids);

			List<WeightTag> weightTags = weightService.getWeighTask(weighTaskParam);
			Assert.assertEquals(weightTags != null && weightTags.size() > 0, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			JSONArray product_id_list = new JSONArray();
			for (int i = 0; i < weightTags.size(); i++) {
				WeightTag weightTag = weightTags.get(i);
				product_id_list.add(weightTag.getSku_id());
			}

			weighTaskParam = new WeighTaskParam(station_id, true, time_config_id, cycle_start_time, false,
					product_id_list);

			// 这一步不会返回啥数据
			weightTags = weightService.getWeighTask(weighTaskParam);
			Assert.assertEquals(weightTags != null, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			String product_id = null;
			for (Object obj : product_id_list) {
				product_id = String.valueOf(obj);
				weighTaskParam = new WeighTaskParam(station_id, true, "01", time_config_id, cycle_start_time, false,
						product_id);
				weightTags = weightService.getWeighTask(weighTaskParam);

				List<WeightTag> target_weight_tags = weightTags.stream().filter(wt -> wt.getOrder_id().equals(order_id))
						.collect(Collectors.toList());

				for (int j = 0; j < target_weight_tags.size(); j++) {
					WeightTag weightTag = target_weight_tags.get(j);
					String detail_id = weightTag.getDetail_id();
					BigDecimal real_weight = weightTag.getAmount().multiply(weightTag.getSale_ratio());
					OldSetWeighParam oldSetWeightParam = new OldSetWeighParam();
					Data data = oldSetWeightParam.new Data();
					data.setDetail_id(detail_id);
					data.setOrder_id(order_id);
					data.setReal_weight(real_weight);
					oldSetWeightParam.setData(data);
					oldSetWeightParam.setOperator_id("01");
					oldSetWeightParam.setCycle_start_time(cycle_start_time);
					oldSetWeightParam.setStation_id(station_id);
					oldSetWeightParam.setTime_config_id(time_config_id);
					oldSetWeightParam.setUnion_dispatch(true);
					result = weightService.setWeight(oldSetWeightParam);
					Assert.assertEquals(result, true, "商品 " + detail_id + " 称重失败");
				}
			}
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
		}
	}

	@Test
	public void oldWeightSystemTestCase08() {
		Reporter.log("老版本的称重软件(PC称重) 称重后验证订单中的相关信息是否更新");
		cycle_start_time = cycle_start_time + ":00";

		JSONArray spu_ids = new JSONArray();
		JSONArray sku_ids = new JSONArray();

		String spu_id = null;
		String sku_id = null;
		for (Detail detal : orderDetail.getDetails()) {
			spu_id = detal.getSpu_id();
			sku_id = detal.getSku_id();
			if (detal.getIs_weigh() == 1) {
				if (!spu_ids.contains(spu_id)) {
					spu_ids.add(spu_id);
				}
				// 统计下单列表中称重商品
				sku_ids.add(sku_id);
			}
		}

		try {
			Reporter.log("步骤一:生成称重数据");
			PackWeighDataParam packWeighDataParam = new PackWeighDataParam(station_id, time_config_id, true,
					cycle_start_time);
			boolean result = weightService.packWeighData(packWeighDataParam);
			Assert.assertEquals(result, true, "生成称重数据失败");

			Reporter.log("步骤二:拉取订单中的下单商品的称重数据");
			WeighTaskParam paramBean = new WeighTaskParam(station_id, true, true, "01", time_config_id,
					cycle_start_time, false, spu_ids);
			List<WeightTag> weightTags = weightService.getWeighTask(paramBean);
			Assert.assertEquals(weightTags != null && weightTags.size() > 0, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			JSONArray product_id_list = new JSONArray();
			for (int i = 0; i < weightTags.size(); i++) {
				WeightTag weightTag = weightTags.get(i);
				product_id_list.add(weightTag.getSku_id());
			}

			paramBean = new WeighTaskParam(station_id, true, time_config_id, cycle_start_time, false, product_id_list);

			// 这一步不会返回啥数据
			weightTags = weightService.getWeighTask(paramBean);
			Assert.assertEquals(weightTags != null, true, "老版本的称重软件(PC称重) 获取称重任务失败");

			String product_id = null;
			for (Object obj : product_id_list) {
				product_id = String.valueOf(obj);
				paramBean = new WeighTaskParam(station_id, true, "01", time_config_id, cycle_start_time, false,
						product_id);
				weightTags = weightService.getWeighTask(paramBean);

				List<WeightTag> target_weight_tags = weightTags.stream().filter(wt -> wt.getOrder_id().equals(order_id))
						.collect(Collectors.toList());

				for (int j = 0; j < target_weight_tags.size(); j++) {
					WeightTag weightTag = target_weight_tags.get(j);
					String detail_id = weightTag.getDetail_id();
					BigDecimal real_weight = weightTag.getAmount().multiply(weightTag.getSale_ratio())
							.add(new BigDecimal("0.2"));
					OldSetWeighParam oldSetWeightParam = new OldSetWeighParam();
					Data data = oldSetWeightParam.new Data();
					data.setDetail_id(detail_id);
					data.setOrder_id(order_id);
					data.setReal_weight(real_weight);
					oldSetWeightParam.setData(data);
					oldSetWeightParam.setOperator_id("01");
					oldSetWeightParam.setCycle_start_time(cycle_start_time);
					oldSetWeightParam.setStation_id(station_id);
					oldSetWeightParam.setTime_config_id(time_config_id);
					oldSetWeightParam.setUnion_dispatch(true);
					result = weightService.setWeight(oldSetWeightParam);
					Assert.assertEquals(result, true, "商品 " + detail_id + " 称重失败");
				}
			}
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			for (Object obj : sku_ids) {
				String temp_sku_id = String.valueOf(obj);
				Detail detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(temp_sku_id))
						.findAny().orElse(null);
				BigDecimal actual_weighted = detail.getStd_real_quantity();
				BigDecimal expected_weighted = detail.getStd_unit_quantity().add(new BigDecimal("0.2"));
				if (actual_weighted.compareTo(expected_weighted) != 0) {
					String msg = String.format("订单%s里的商品%s出库数与预期的不一致,预期:%s,实际:%s", order_id, temp_sku_id,
							expected_weighted, actual_weighted);
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");
			BigDecimal expect_real_price = BigDecimal.ZERO;
			BigDecimal std_sale_price_forsale = null;
			BigDecimal expect_std_unit_quantity = null;
			for (Detail detail : orderDetail.getDetails()) {
				std_sale_price_forsale = detail.getStd_sale_price_forsale();
				if (detail.getIs_weigh() == 1) {
					expect_std_unit_quantity = detail.getStd_unit_quantity().add(new BigDecimal("0.2"));
				} else {
					expect_std_unit_quantity = detail.getStd_unit_quantity();
				}
				expect_real_price = expect_real_price.add(std_sale_price_forsale.multiply(expect_std_unit_quantity));
			}
			expect_real_price = expect_real_price.setScale(2, BigDecimal.ROUND_HALF_UP);

			BigDecimal actual_real_money = orderDetail.getReal_price();

			if (expect_real_price.compareTo(actual_real_money) != 0) {
				String msg = String.format("订单%s的出库金额与预期不符,预期:%s,实际:%s", order_id, expect_real_price,
						actual_real_money);
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 称重后,订单详细信息存在数据与预期不符");
		} catch (Exception e) {
			logger.error("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
			Assert.fail("老版本的称重软件(PC称重) 获取称重任务遇到错误: ", e);
		}
	}
}
