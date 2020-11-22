package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SkuMeasurementBean;
import cn.guanmai.station.bean.invoicing.BatchOutStockBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.OutStockRemindBean;
import cn.guanmai.station.bean.invoicing.StockSaleSkuBean;
import cn.guanmai.station.bean.invoicing.param.NegativeStockRemindParam;
import cn.guanmai.station.bean.invoicing.param.OutStockModifyParam;
import cn.guanmai.station.bean.invoicing.param.OutStockPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 26, 2019 3:41:45 PM 
* @des 销售出库测试
* @version 1.0 
*/
public class OutStockTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OutStockTest.class);
	private OutStockService outStockService;
	private InStockTool inStockTool;
	private AsyncService asyncService;
	private OrderService orderService;
	private CategoryService categoryService;
	private StockRecordService stockRecordService;
	private ServiceTimeService serviceTimeService;
	private WeightService weightService;
	private LoginUserInfoService loginUserInfoService;

	private JSONArray user_permissions;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String current_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");

	private String yestoday;
	private String tommrow;

	// 系统转化生成的自定义站点ID
	private String customize_station_id;
	private int stock_type;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		outStockService = new OutStockServiceImpl(headers);
		inStockTool = new InStockTool(headers);
		orderService = new OrderServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		weightService = new WeightServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			yestoday = TimeUtil.calculateTime("yyyy-MM-dd", today, -1, Calendar.DATE);
			tommrow = TimeUtil.calculateTime("yyyy-MM-dd", today, 1, Calendar.DATE);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号的相关信息失败");
			stock_type = loginUserInfo.getStock_method();

			String station_id = loginUserInfo.getStation_id();
			customize_station_id = StringUtil.getCustomizedStationId(station_id);

		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test(priority = 0, timeOut = 30000)
	public void outStockTestCase01() {
		ReporterCSS.title("测试点: 查看订单是否正常生成出库单");
		// 按建单日期搜索
		try {
			// 查询订单
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date(today);
			orderFilterParam.setEnd_date(today);
			int offset = 0;
			int limit = 50;
			orderFilterParam.setLimit(limit);

			List<OrderBean> orderList = new ArrayList<>();
			List<OrderBean> tempList = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempList, null, "搜索过滤订单失败");
				orderList.addAll(tempList);
				if (tempList.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderList.stream().sorted(Comparator.comparing(OrderBean::getId))
					.map(o -> o.getId()).collect(Collectors.toList());

			// 查询出库单
			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setType(2);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setStart(today);
			outStockSheetFilterParam.setEnd(today);
			offset = 0;
			outStockSheetFilterParam.setLimit(limit);
			List<OutStockSheetBean> outStockSheetList = new ArrayList<>();
			List<OutStockSheetBean> tempOutStockSheetList = null;
			while (true) {
				outStockSheetFilterParam.setOffset(offset);
				tempOutStockSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
				Assert.assertNotEquals(tempOutStockSheetList, null, "搜索过滤产品出库单失败");
				outStockSheetList.addAll(tempOutStockSheetList);
				if (tempOutStockSheetList.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> stock_out_ids = outStockSheetList.stream()
					.sorted(Comparator.comparing(OutStockSheetBean::getId)).map(s -> s.getId())
					.collect(Collectors.toList());

			if (order_ids.size() >= stock_out_ids.size()) {
				order_ids.removeAll(stock_out_ids);
				Assert.assertEquals(order_ids.size(), 0, "如下订单没有生成对应的出库单 " + order_ids);
			} else {
				stock_out_ids.removeAll(order_ids);
				if (stock_out_ids.size() > 0) {
					boolean result = true;
					List<String> tempIDList = new ArrayList<String>();
					for (String id : stock_out_ids) {
						if (id.startsWith("PL") || id.startsWith("LK") || id.startsWith(customize_station_id)) {
							OutStockSheetBean stockOutSheet = outStockSheetList.stream()
									.filter(s -> s.getId().equals(id)).findAny().orElse(null);
							if (stockOutSheet.getStatus() != 3) {
								tempIDList.add(id);
								result = false;
							}
						}
					}
					Assert.assertEquals(result, true, "如下出库单没有对应的订单 " + tempIDList + ",怀疑是订单删除了,出库单没有同步删除");
				}
			}
		} catch (Exception e) {
			logger.error("搜索查询出库单遇到错误: ", e);
			Assert.fail("搜索查询出库单遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase02() {
		ReporterCSS.title("测试点: 查看销售出库单详细信息");
		// 按建单日期搜索
		try {
			OutStockSheetFilterParam fiterParam = new OutStockSheetFilterParam();
			fiterParam.setType(2);
			fiterParam.setStatus(0);
			fiterParam.setStart(today);
			fiterParam.setEnd(today);
			fiterParam.setOffset(0);
			fiterParam.setLimit(10);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(fiterParam);
			Assert.assertNotEquals(outStockSheetList, null, "销售出库列表搜索过滤失败");

			if (outStockSheetList.size() > 0) {
				String out_stock_sheet_id = outStockSheetList.get(0).getId();
				OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(out_stock_sheet_id);
				Assert.assertNotEquals(outStockDetail, null, "获取销售出库单" + out_stock_sheet_id + "详细信息失败");
			}
		} catch (Exception e) {
			logger.error("搜索查询销售出库单遇到错误: ", e);
			Assert.fail("搜索查询销售出库单遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void outStockTestCase03() {
		ReporterCSS.title("测试点: 订单分拣称重后,销售出库单手工出库");
		// 按建单日期搜索
		try {
			String order_id = null;
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			String address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			String time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			OrderCreateParam orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts,
					4);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			OrderCreateParam.OrderSku orderSku = orderCreateParam.getDetails().get(0);
			String sku_id = orderSku.getSku_id();
			String spu_id = orderSku.getSpu_id();
			SkuBean sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU" + sku_id + " 详细信息失败");

			// 弄一个上商品成非基本单位售卖规格的
			sku.setSale_ratio(new BigDecimal("5"));
			if (sku.getStd_unit_name().equals("包")) {
				sku.setSale_unit_name("件");
			} else {
				sku.setSale_unit_name("包");
			}
			sku.setStock_type(1);
			sku.setStocks("-99999");
			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改销售规格" + sku_id + "失败");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String temp_sku_id = skuObj.getSku_id();

						// 首先找出这个SKU有没有在组合商品里
						List<String> combine_goods_ids = orderSkus.stream()
								.filter(s -> s.getSku_id().equals(temp_sku_id) && s.isIs_combine_goods())
								.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

						// 先把这个商品过滤掉
						orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(temp_sku_id))
								.collect(Collectors.toList());

						// 过滤掉组合商品里的其他商品
						for (String combine_goods_id : combine_goods_ids) {
							combine_goods_map.remove(combine_goods_id);
							orderSkus = orderSkus.stream().filter(
									s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
									.collect(Collectors.toList());
						}
					}
					Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
					orderCreateParam.setDetails(orderSkus);
					orderResponse = orderService.createOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
				order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			// 订单状态改为配送中
			result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			result = weightService.oneStepWeightOrder(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + "称重失败");

			result = outStockService.asyncOrderToOutStockSheet(order_id);
			Assert.assertEquals(result, true, "订单在20S内没有生成对应的出库单");

			// 为订单新建采购入库单
			inStockTool.createInStockSheetForOrder(order_id);

			OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(outStockDetail, null, "获取销售出库单详细信息失败");

			NegativeStockRemindParam negativeStockRemindParam = new NegativeStockRemindParam();
			List<NegativeStockRemindParam.Detail> nsrp_details = new ArrayList<>();
			NegativeStockRemindParam.Detail nsrp_detail = null;
			negativeStockRemindParam.setId(order_id);
			negativeStockRemindParam.setIs_submit(2);
			for (OutStockDetailBean.Detail detail : outStockDetail.getDetails()) {
				nsrp_detail = negativeStockRemindParam.new Detail();
				nsrp_detail.setSpu_id(detail.getSpu_id());
				nsrp_detail.setSku_id(detail.getId());
				nsrp_detail.setQuantity(detail.getQuantity());
				if (stock_type == 2) {
					List<NegativeStockRemindParam.Detail.FIFO> fifos = new ArrayList<>();
					List<BatchOutStockBean> outStockBatchList = outStockService.searchOutStockBatch(detail.getId(), "");
					Assert.assertNotEquals(outStockBatchList, null, "拉取商品 " + detail.getId() + "对应的出库批次失败");

					BigDecimal tempCount = BigDecimal.ZERO;
					BigDecimal count = BigDecimal.ZERO;

					// 出库数
					BigDecimal std_unit_quantity = detail.getQuantity().multiply(detail.getSale_ratio()).setScale(2,
							BigDecimal.ROUND_UP);

					NegativeStockRemindParam.Detail.FIFO fifo = null;
					for (int i = 0; i < outStockBatchList.size(); i++) {
						BatchOutStockBean stockOutBatch = outStockBatchList.get(i);
						BigDecimal batchQuantity = stockOutBatch.getRemain();

						fifo = nsrp_detail.new FIFO();

						tempCount = tempCount.add(batchQuantity);

						fifo.setBatch_number(stockOutBatch.getBatch_number());
						if (tempCount.compareTo(std_unit_quantity) == 0) {
							fifo.setOut_stock_base(stockOutBatch.getRemain());
							fifos.add(fifo);
							break;
						} else if (tempCount.compareTo(std_unit_quantity) == 1) {
							fifo.setOut_stock_base(std_unit_quantity.subtract(count));
							fifos.add(fifo);
							break;
						} else {
							fifo.setOut_stock_base(batchQuantity);
							count = count.add(batchQuantity);
							fifos.add(fifo);
						}
					}
					nsrp_detail.setFIFO(fifos);
				}
				nsrp_details.add(nsrp_detail);
			}

			negativeStockRemindParam.setDetails(nsrp_details);

			List<OutStockRemindBean> stockRemindList = outStockService.singleOutStockRemind(negativeStockRemindParam);
			Assert.assertNotEquals(stockRemindList, null, "手工出库负库存提醒功能存在问题");

			OutStockModifyParam outStockModifyParam = new OutStockModifyParam();
			outStockModifyParam.setId(outStockDetail.getId());
			outStockModifyParam.setCreator(outStockDetail.getCreator());
			outStockModifyParam.setIs_bind_order(true);
			outStockModifyParam.setIs_submit(2);
			outStockModifyParam.setOut_stock_customer_id(outStockDetail.getOut_stock_customer_id());
			outStockModifyParam.setOut_stock_time(current_time);
			outStockModifyParam.setStatus(1);
			List<OutStockModifyParam.Detail> param_details = new ArrayList<OutStockModifyParam.Detail>();
			OutStockModifyParam.Detail param_detail = null;
			for (OutStockDetailBean.Detail detail : outStockDetail.getDetails()) {
				param_detail = outStockModifyParam.new Detail();
				sku_id = detail.getId();
				param_detail.setCategory(detail.getCategory());
				param_detail.setClean_food(false);
				param_detail.setId(sku_id);
				param_detail.setName(detail.getName());
				param_detail.setOut_of_stock(false);
				param_detail.setQuantity(detail.getQuantity());
				param_detail.setReal_std_count(detail.getReal_std_count());
				param_detail.setSale_ratio(detail.getSale_ratio());
				param_detail.setSale_unit_name(detail.getSale_unit_name());
				param_detail.setSpu_id(detail.getSpu_id());
				param_detail.setStd_unit_name(detail.getStd_unit_name());
				param_detail.setStd_unit_name_forsale(detail.getStd_unit_name_forsale());

				if (stock_type == 2) {
					List<OutStockModifyParam.Detail.BatchDetail> batchDetails = new ArrayList<>();
					spu_id = detail.getSpu_id();
					List<BatchOutStockBean> outStockBatchList = outStockService.searchOutStockBatch(sku_id, "");
					Assert.assertNotEquals(outStockBatchList, null, "拉取商品 " + sku_id + "对应的退货批次失败");

					// 出库数
					BigDecimal quantity = detail.getQuantity().multiply(detail.getSale_ratio()).setScale(2,
							BigDecimal.ROUND_UP);
					BigDecimal tempCount = BigDecimal.ZERO;
					BigDecimal count = BigDecimal.ZERO;
					OutStockModifyParam.Detail.BatchDetail batchDetail = null;
					for (int i = 0; i < outStockBatchList.size(); i++) {
						BatchOutStockBean stockOutBatch = outStockBatchList.get(i);
						batchDetail = param_detail.new BatchDetail();

						BigDecimal batchQuantity = stockOutBatch.getRemain();
						tempCount = tempCount.add(batchQuantity);

						batchDetail.setBatch_number(stockOutBatch.getBatch_number());

						if (tempCount.compareTo(quantity) == 0) {
							batchDetail.setOut_stock_base(batchQuantity);
							batchDetails.add(batchDetail);
							break;
						} else if (tempCount.compareTo(quantity) == 1) {
							batchDetail.setOut_stock_base(quantity.subtract(count));
							batchDetails.add(batchDetail);
							break;
						} else {
							count = count.add(batchQuantity);
							batchDetail.setOut_stock_base(batchQuantity);
							batchDetails.add(batchDetail);
						}
						if (i == outStockBatchList.size() - 1) {
							Assert.assertEquals(tempCount.compareTo(quantity) == -1, false,
									"商品" + spu_id + "库存数不足,无法进行手工出库");
						}
					}
					param_detail.setBatch_details(batchDetails);

				}
				param_details.add(param_detail);
			}
			outStockModifyParam.setDetails(param_details);

			result = outStockService.modifyOutStockSheet(outStockModifyParam);
			Assert.assertEquals(result, true, "出库单手工出库失败");

			outStockDetail = outStockService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(outStockDetail, null, "获取销售出库单详细信息失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OrderDetailBean.Detail> o_details = orderDetail.getDetails();
			List<OutStockDetailBean.Detail> s_details = outStockDetail.getDetails();

			String msg = null;
			for (OrderDetailBean.Detail o_detail : o_details) {
				String temp_sku_id = o_detail.getSku_id();
				OutStockDetailBean.Detail s_detail = s_details.stream().filter(s -> s.getId().equals(temp_sku_id))
						.findAny().orElse(null);
				if (s_detail == null) {
					msg = String.format("订单 %s 中的商品 %s 没有出现在对应的出库单中", order_id, temp_sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (o_detail.getStd_real_quantity().compareTo(s_detail.getReal_std_count()) != 0) {
					msg = String.format("订单%s中的商品 %s的出库数与订单中的分拣称重数不一致,订单:%s,出库单:%s", order_id, temp_sku_id,
							o_detail.getStd_real_quantity(), s_detail.getReal_std_count());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "出库单" + order_id + "里的信息与预期不一致");
		} catch (

		Exception e) {
			logger.error("销售出库单手工出库遇到错误: ", e);
			Assert.fail("销售出库单手工出库遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "outStockTestCase03" })
	public void outStockTestCase04() {
		ReporterCSS.title("测试点: 已经出库的销售出库单冲销");
		try {
			OutStockSheetFilterParam fiterParam = new OutStockSheetFilterParam();
			fiterParam.setType(2);
			fiterParam.setStatus(2);
			fiterParam.setStart_date_new(yestoday + " 00:00");
			fiterParam.setEnd_date_new(tommrow + " 00:00");
			fiterParam.setOffset(0);
			fiterParam.setLimit(10);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(fiterParam);
			Assert.assertNotEquals(outStockSheetList, null, "销售出库列表搜索过滤失败");

			Assert.assertEquals(outStockSheetList.size() > 0, true, "销售出库单列表没有已经出库的出库单,与预期不符");

			OutStockSheetBean stockOutSheet = outStockSheetList.stream()
					.filter(s -> s.getId().startsWith("PL") || s.getId().startsWith(customize_station_id)).findFirst()
					.orElse(null);
			String out_stock_sheet_id = stockOutSheet.getId();
			boolean result = outStockService.cancelOutStockSheet(out_stock_sheet_id);
			Assert.assertEquals(result, true, "销售出库单冲销失败");

			OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(out_stock_sheet_id);
			Assert.assertNotEquals(outStockDetail, null, "获取出库单" + out_stock_sheet_id + "详情失败");

			Assert.assertEquals(outStockDetail.getStatus(), 3, "出库单" + out_stock_sheet_id + "冲销后,状态值与预期不一致");
		} catch (Exception e) {
			logger.error("销售出库单冲销遇到错误: ", e);
			Assert.fail("销售出库单冲销遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase05() {
		ReporterCSS.title("测试点: 待出库状态销售出库单冲销");
		// 按建单日期搜索
		try {
			OutStockSheetFilterParam fiterParam = new OutStockSheetFilterParam();
			fiterParam.setType(2);
			fiterParam.setStatus(1);
			fiterParam.setStart(yestoday);
			fiterParam.setEnd(today);
			fiterParam.setOffset(0);
			fiterParam.setLimit(50);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(fiterParam);
			Assert.assertNotEquals(outStockSheetList, null, "销售出库列表搜索过滤失败");

			Assert.assertEquals(outStockSheetList.size() > 0, true, "销售出库单列表没有待出库单数据,无法进行后续操作");
			OutStockSheetBean stockOutSheet = outStockSheetList.stream()
					.filter(s -> s.getId().startsWith("PL") || s.getId().startsWith(customize_station_id)).findFirst()
					.orElse(null);

			Assert.assertNotEquals(stockOutSheet, null, "没有找到目标PL单");

			String out_stock_sheet_id = stockOutSheet.getId();
			boolean result = outStockService.cancelOutStockSheet(out_stock_sheet_id);
			Assert.assertEquals(result, true, "销售出库单冲销失败");

			OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(out_stock_sheet_id);
			Assert.assertNotEquals(outStockDetail, null, "获取出库单" + out_stock_sheet_id + "详情失败");

			Assert.assertEquals(outStockDetail.getStatus(), 3, "出库单" + out_stock_sheet_id + "冲销后,状态值与预期不一致");

		} catch (Exception e) {
			logger.error("销售出库单冲销遇到错误: ", e);
			Assert.fail("销售出库单冲销遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase06() {
		ReporterCSS.title("测试点: 批量出库负库存提醒");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
		outStockSheetFilterParam.setType(2);
		outStockSheetFilterParam.setStatus(0);
		outStockSheetFilterParam.setStart(today);
		outStockSheetFilterParam.setEnd(today);

		try {
			List<OutStockRemindBean> stockRemindList = outStockService.batchOutStockRemind(outStockSheetFilterParam);
			Assert.assertNotEquals(stockRemindList, null, "批量出库负库存提醒失败");
		} catch (Exception e) {
			logger.error("批量出库负库存提醒遇到错误: ", e);
			Assert.fail("批量出库负库存提醒遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase07() {
		ReporterCSS.title("测试点: 批量出库负库存提醒(选择个别订单)");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
		outStockSheetFilterParam.setType(2);
		outStockSheetFilterParam.setStatus(0);
		outStockSheetFilterParam.setStart(today);
		outStockSheetFilterParam.setEnd(today);
		outStockSheetFilterParam.setLimit(10);
		outStockSheetFilterParam.setOffset(0);

		try {
			List<OutStockSheetBean> outStockSheets = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheets, null, "搜索过滤销售出库单失败");

			List<String> out_stock_sheet_ids = outStockSheets.stream().map(s -> s.getId()).collect(Collectors.toList());

			List<OutStockRemindBean> stockRemindList = outStockService.newBatchOutStockRemind(out_stock_sheet_ids);
			Assert.assertNotEquals(stockRemindList, null, "批量出库负库存提醒失败");
		} catch (Exception e) {
			logger.error("批量出库负库存提醒遇到错误: ", e);
			Assert.fail("批量出库负库存提醒遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase08() {
		ReporterCSS.title("测试点: 批量出库负库存提醒");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
		outStockSheetFilterParam.setType(2);
		outStockSheetFilterParam.setStatus(0);
		outStockSheetFilterParam.setStart(today);
		outStockSheetFilterParam.setEnd(today);

		try {
			List<OutStockRemindBean> stockRemindList = outStockService.newBatchOutStockRemind(outStockSheetFilterParam);
			Assert.assertNotEquals(stockRemindList, null, "批量出库负库存提醒失败");
		} catch (Exception e) {
			logger.error("批量出库负库存提醒遇到错误: ", e);
			Assert.fail("批量出库负库存提醒遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase09() {
		ReporterCSS.title("测试点: 批量出库负库存提醒(选择个别订单)");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
		outStockSheetFilterParam.setType(2);
		outStockSheetFilterParam.setStatus(0);
		outStockSheetFilterParam.setStart(today);
		outStockSheetFilterParam.setEnd(today);
		outStockSheetFilterParam.setLimit(10);
		outStockSheetFilterParam.setOffset(0);

		try {
			List<OutStockSheetBean> outStockSheets = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheets, null, "搜索过滤销售出库单失败");

			List<String> out_stock_sheet_ids = outStockSheets.stream().map(s -> s.getId()).collect(Collectors.toList());

			List<OutStockRemindBean> stockRemindList = outStockService.batchOutStockRemind(out_stock_sheet_ids);
			Assert.assertNotEquals(stockRemindList, null, "批量出库负库存提醒失败");
		} catch (Exception e) {
			logger.error("批量出库负库存提醒遇到错误: ", e);
			Assert.fail("批量出库负库存提醒遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void outStockTestCase10() {
		ReporterCSS.title("测试点: 销售出库批量出库");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
		outStockSheetFilterParam.setType(2);
		outStockSheetFilterParam.setStatus(0);
		outStockSheetFilterParam.setStart(today);
		outStockSheetFilterParam.setEnd(today);

		try {
			String order_id = null;
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			String address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			String time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			OrderCreateParam orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts,
					10);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			OrderCreateParam.OrderSku orderSku = orderCreateParam.getDetails().get(0);
			String sku_id = orderSku.getSku_id();
			String spu_id = orderSku.getSpu_id();
			SkuBean sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU" + sku_id + " 详细信息失败");

			// 弄一个上商品成非基本单位售卖规格的
			sku.setSale_ratio(new BigDecimal("5"));
			if (sku.getStd_unit_name().equals("包")) {
				sku.setSale_unit_name("件");
			} else {
				sku.setSale_unit_name("包");
			}
			sku.setStock_type(1);
			sku.setStocks("-99999");
			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改销售规格" + sku_id + "失败");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String temp_sku_id = skuObj.getSku_id();

						// 首先找出这个SKU有没有在组合商品里
						List<String> combine_goods_ids = orderSkus.stream()
								.filter(s -> s.getSku_id().equals(temp_sku_id) && s.isIs_combine_goods())
								.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

						// 先把这个商品过滤掉
						orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(temp_sku_id))
								.collect(Collectors.toList());

						// 过滤掉组合商品里的其他商品
						for (String combine_goods_id : combine_goods_ids) {
							combine_goods_map.remove(combine_goods_id);
							orderSkus = orderSkus.stream().filter(
									s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
									.collect(Collectors.toList());
						}
					}
					Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
					orderCreateParam.setDetails(orderSkus);
					orderResponse = orderService.createOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
				order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			// 订单状态改为配送中
			result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			result = weightService.oneStepWeightOrder(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + "称重失败");

			result = outStockService.asyncOrderToOutStockSheet(order_id);
			Assert.assertEquals(result, true, "订单在20S内没有生成对应的出库单");

			inStockTool.createInStockSheetForOrder(order_id);

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date(today);
			orderFilterParam.setEnd_date(today);
			orderFilterParam.setStatus(10);
			orderFilterParam.setStock_type(1);
			int limit = 50;
			int offset = 0;
			orderFilterParam.setOffset(offset);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			List<OrderBean> tempOrderList = null;
			while (true) {
				tempOrderList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrderList, null, "搜索过滤订单失败");
				orderList.addAll(tempOrderList);
				if (tempOrderList.size() < 50) {
					break;
				}
				offset = offset + limit;
				orderFilterParam.setOffset(offset);
			}

			List<String> order_ids = orderList.stream().filter(o -> o.getStatus() == 10).map(o -> o.getId())
					.collect(Collectors.toList());
			ReporterCSS.step("待出库的订单有 " + order_ids.size() + "个 " + order_ids);

			String task_url = outStockService.batchOutStock(outStockSheetFilterParam);
			Assert.assertNotEquals(task_url, null, "销售出库批量出库失败");

			String task_id = task_url.split("task_id=")[1];

			result = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "成功");

			Assert.assertEquals(result, true, "销售出库批量出库异步任务执行失败");

			OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(outStockDetail, null, "获取销售出库单详细信息失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");
			String msg = null;

			if (outStockDetail.getStatus() != 2) {
				msg = String.format("销售出库单 %s 的状态没有变成 已出库,实际状态值:%s", order_id, outStockDetail.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				Map<String, List<SkuMeasurementBean>> skuMeasurementMap = categoryService.getSkuMeasurementMap();
				Assert.assertNotEquals(skuMeasurementMap, null, "获取计量单位的比例列表失败");

				List<OrderDetailBean.Detail> o_details = orderDetail.getDetails();
				List<OutStockDetailBean.Detail> s_details = outStockDetail.getDetails();
				for (OrderDetailBean.Detail o_detail : o_details) {
					String temp_sku_id = o_detail.getSku_id();
					OutStockDetailBean.Detail s_detail = s_details.stream().filter(s -> s.getId().equals(temp_sku_id))
							.findAny().orElse(null);
					if (s_detail == null) {
						msg = String.format("订单 %s 中的商品 %s 没有出现在对应的出库单中", order_id, temp_sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					BigDecimal o_std_real_quantity = o_detail.getStd_real_quantity();
					if (!s_detail.getStd_unit_name_forsale().equals(o_detail.getStd_unit_name_forsale())) {
						List<SkuMeasurementBean> skuMeasurements = skuMeasurementMap
								.get(o_detail.getStd_unit_name_forsale());
						SkuMeasurementBean skuMeasurement = skuMeasurements.stream()
								.filter(s -> s.getStd_unit_name_forsale().equals(s_detail.getStd_unit_name_forsale()))
								.findAny().orElse(null);
						BigDecimal std_ratio = skuMeasurement.getStd_ratio();
						o_std_real_quantity = o_std_real_quantity.divide(std_ratio, 2, BigDecimal.ROUND_HALF_UP);
					}

					if (o_std_real_quantity.compareTo(s_detail.getReal_std_count()) != 0) {
						msg = String.format("订单%s中的商品 %s的出库数与订单中的分拣称重数不一致,订单:%s,出库单:%s", order_id, temp_sku_id,
								o_std_real_quantity, s_detail.getReal_std_count());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			// 过滤出库单
			OutStockSheetFilterParam fiterParam = new OutStockSheetFilterParam();
			fiterParam.setType(2);
			fiterParam.setStatus(2);
			fiterParam.setStart(today);
			fiterParam.setEnd(today);
			offset = 0;
			fiterParam.setOffset(offset);
			fiterParam.setLimit(limit);
			List<OutStockSheetBean> outStockSheetList = new ArrayList<OutStockSheetBean>();
			List<OutStockSheetBean> tempStockOutSheetList = null;
			while (true) {
				tempStockOutSheetList = outStockService.searchOutStockSheet(fiterParam);
				Assert.assertNotEquals(tempStockOutSheetList, null, "搜索过滤销售出库单失败");
				outStockSheetList.addAll(tempStockOutSheetList);
				offset = offset + limit;
				fiterParam.setOffset(offset);
				if (tempStockOutSheetList.size() < limit) {
					break;
				}
			}

			List<String> out_stock_sheet_ids = outStockSheetList.stream().map(s -> s.getId())
					.collect(Collectors.toList());
			out_stock_sheet_ids.retainAll(order_ids);

			List<OutStockSheetBean> targetStockOutSheets = outStockSheetList.stream()
					.filter(s -> out_stock_sheet_ids.contains(s.getId())).collect(Collectors.toList());
			List<String> target_out_stock_sheet_ids = targetStockOutSheets.stream().filter(s -> s.getStatus() != 2)
					.map(s -> s.getId()).collect(Collectors.toList());

			if (target_out_stock_sheet_ids.size() > 0) {
				msg = String.format("如下订单%s对应的出库单没有出库成功.", target_out_stock_sheet_ids.toString());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "批量出库后,存在信息与预期不符");
		} catch (Exception e) {
			logger.error("销售出库批量出库遇到错误: ", e);
			Assert.fail("销售出库批量出库遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase11() {
		ReporterCSS.title("测试点: 手工新建销售出库单");

		String sheet_id = "AT" + TimeUtil.getLongTime();
		try {
			boolean result = outStockService.createOutStockSheet("AT", sheet_id);
			Assert.assertEquals(result, true, "手工新建销售出库单失败");
		} catch (Exception e) {
			logger.error("手工新建销售出库单遇到错误: ", e);
			Assert.fail("手工新建销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase12() {
		ReporterCSS.title("测试点: 手工新建销售出库单并进行出库操作,操作对象为自定义商户");

		String sheet_id = "AT" + TimeUtil.getLongTime();
		try {
			String out_stock_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");

			OutStockSheetCreateParam outStockSheetCreateParam = new OutStockSheetCreateParam();
			outStockSheetCreateParam.setId(sheet_id);
			outStockSheetCreateParam.setOut_stock_time(out_stock_time);
			outStockSheetCreateParam.setOut_stock_target("AT");
			outStockSheetCreateParam.setOut_stock_remark("123123");
			outStockSheetCreateParam.setIs_submit(2);

			List<StockSaleSkuBean> stockSaleSkuList = outStockService.searchStockSaleSku("C", "");
			Assert.assertNotEquals(stockSaleSkuList, null, "搜索出库商品遇到失败");

			Assert.assertEquals(stockSaleSkuList.size() > 0, true, "没有搜索到出库商品,无法进行后续操作");

			stockSaleSkuList = NumberUtil.roundNumberInList(stockSaleSkuList, 5);

			OutStockSheetCreateParam.Detail detail = null;
			List<OutStockSheetCreateParam.Detail> details = new ArrayList<OutStockSheetCreateParam.Detail>();
			BigDecimal quantity = null;

			for (StockSaleSkuBean stockSaleSku : stockSaleSkuList) {
				String sku_id = stockSaleSku.getSku_id();
				detail = outStockSheetCreateParam.new Detail();
				quantity = NumberUtil.getRandomNumber(5, 10, 1);
				if (stock_type == 2) {
					List<BatchOutStockBean> outStockBatchList = outStockService.searchOutStockBatch(sku_id, "");
					Assert.assertNotEquals(outStockBatchList, null, "拉取商品 " + sku_id + "对应的出库批次失败");

					if (outStockBatchList.size() == 0) {
						continue;
					}
					outStockBatchList = NumberUtil.roundNumberInList(outStockBatchList, 2);

					BigDecimal std_unit_quantity = new BigDecimal("0");
					List<OutStockSheetCreateParam.Detail.BatchDetail> batchDetails = new ArrayList<>();
					OutStockSheetCreateParam.Detail.BatchDetail batchDetail = null;
					for (BatchOutStockBean outStockBatch : outStockBatchList) {
						batchDetail = detail.new BatchDetail();
						batchDetail.setBatch_number(outStockBatch.getBatch_number());
						batchDetail.setOut_stock_base(outStockBatch.getRemain());
						std_unit_quantity = std_unit_quantity
								.add(outStockBatch.getRemain().setScale(3, BigDecimal.ROUND_HALF_DOWN));
						batchDetails.add(batchDetail);
					}
					quantity = std_unit_quantity.divide(stockSaleSku.getSale_ratio(), 3, BigDecimal.ROUND_HALF_DOWN);
					detail.setBatch_details(batchDetails);
				}
				detail.setCategory(stockSaleSku.getCategory_id_2_name());
				detail.setName(stockSaleSku.getSku_name());
				detail.setId(sku_id);
				detail.setSale_unit_name(stockSaleSku.getSale_unit_name());
				detail.setSpu_id(stockSaleSku.getSpu_id());
				detail.setStd_unit_name(stockSaleSku.getStd_unit_name());
				detail.setSale_ratio(stockSaleSku.getSale_ratio());
				detail.setQuantity(quantity);
				details.add(detail);
			}
			outStockSheetCreateParam.setDetails(details);
			boolean result = outStockService.createOutStockSheet(outStockSheetCreateParam);
			Assert.assertEquals(result, true, "手工新建销售出库单并进行出库失败");
		} catch (Exception e) {
			logger.error("手工新建销售出库单并进行出库操作遇到错误: ", e);
			Assert.fail("手工新建销售出库单并进行出库操作遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase13() {
		ReporterCSS.title("测试点: 手工新建销售出库单并进行出库操作,出库对象为站点商户");
		String sheet_id = "AT" + TimeUtil.getLongTime();
		try {
			List<CustomerBean> customerList = orderService.getCustomers();
			Assert.assertNotEquals(customerList, null, "获取站点商户列表失败");

			CustomerBean customer = NumberUtil.roundNumberInList(customerList);
			String address_id = customer.getAddress_id();

			String out_stock_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");

			OutStockSheetCreateParam outStockSheetCreateParam = new OutStockSheetCreateParam();
			outStockSheetCreateParam.setId(sheet_id);
			outStockSheetCreateParam.setOut_stock_time(out_stock_time);
			outStockSheetCreateParam.setOut_stock_customer_id(address_id);
			outStockSheetCreateParam.setIs_submit(2);

			List<StockSaleSkuBean> stockSaleSkuList = outStockService.searchStockSaleSku("C", address_id);
			Assert.assertNotEquals(stockSaleSkuList, null, "搜索出库商品遇到失败");

			Assert.assertEquals(stockSaleSkuList.size() > 0, true, "没有搜索到出库商品,无法进行后续操作");

			stockSaleSkuList = NumberUtil.roundNumberInList(stockSaleSkuList, 5);

			OutStockSheetCreateParam.Detail detail = null;
			List<OutStockSheetCreateParam.Detail> details = new ArrayList<OutStockSheetCreateParam.Detail>();
			BigDecimal quantity = null;

			for (StockSaleSkuBean stockSaleSku : stockSaleSkuList) {
				String sku_id = stockSaleSku.getSku_id();
				detail = outStockSheetCreateParam.new Detail();
				quantity = NumberUtil.getRandomNumber(5, 10, 1);
				if (stock_type == 2) {
					List<BatchOutStockBean> outStockBatchList = outStockService.searchOutStockBatch(sku_id, "");
					Assert.assertNotEquals(outStockBatchList, null, "拉取商品 " + sku_id + "对应的出库批次失败");

					if (outStockBatchList.size() == 0) {
						continue;
					}
					outStockBatchList = NumberUtil.roundNumberInList(outStockBatchList, 2);

					BigDecimal std_unit_quantity = new BigDecimal("0");
					List<OutStockSheetCreateParam.Detail.BatchDetail> batchDetails = new ArrayList<>();
					OutStockSheetCreateParam.Detail.BatchDetail batchDetail = null;
					for (BatchOutStockBean outStockBatch : outStockBatchList) {
						batchDetail = detail.new BatchDetail();
						batchDetail.setBatch_number(outStockBatch.getBatch_number());
						batchDetail.setOut_stock_base(outStockBatch.getRemain());
						std_unit_quantity = std_unit_quantity
								.add(outStockBatch.getRemain().setScale(3, BigDecimal.ROUND_HALF_DOWN));
						batchDetails.add(batchDetail);
					}
					quantity = std_unit_quantity.divide(stockSaleSku.getSale_ratio(), 3, BigDecimal.ROUND_HALF_UP);
					detail.setBatch_details(batchDetails);
				}
				detail.setCategory(stockSaleSku.getCategory_id_2_name());
				detail.setName(stockSaleSku.getSku_name());
				detail.setId(sku_id);
				detail.setSale_unit_name(stockSaleSku.getSale_unit_name());
				detail.setSpu_id(stockSaleSku.getSpu_id());
				detail.setStd_unit_name(stockSaleSku.getStd_unit_name());
				detail.setSale_ratio(stockSaleSku.getSale_ratio());
				detail.setQuantity(quantity);
				details.add(detail);
			}
			outStockSheetCreateParam.setDetails(details);
			boolean result = outStockService.createOutStockSheet(outStockSheetCreateParam);
			Assert.assertEquals(result, true, "手工新建销售出库单并进行出库失败");
		} catch (Exception e) {
			logger.error("手工新建销售出库单并进行出库操作遇到错误: ", e);
			Assert.fail("手工新建销售出库单并进行出库操作遇到错误: ", e);
		}
	}

	@Test
	public void outStockTestCase14() {
		ReporterCSS.title("测试点: 修复出库数据");
		try {
			user_permissions = loginUserInfoService.getLoginUserInfo().getUser_permission();
			Assert.assertNotEquals(user_permissions, null, "获取登录用户的权限信息失败");

			Assert.assertEquals(user_permissions.contains("import_out_stock_log_price_repair"), true, "登录用户无修复出库数据权限");

			OutStockRecordFilterParam stockOutRecordFilterParam = new OutStockRecordFilterParam();
			stockOutRecordFilterParam.setBegin(today);
			stockOutRecordFilterParam.setEnd(today);
			stockOutRecordFilterParam.setTime_type(1);
			stockOutRecordFilterParam.setLimit(20);
			stockOutRecordFilterParam.setOffset(0);

			List<OutStockRecordBean> outStockRecords = stockRecordService.outStockRecords(stockOutRecordFilterParam);
			Assert.assertNotEquals(outStockRecords, null, "搜索过滤出库记录失败");

			boolean result = true;
			if (outStockRecords.size() > 0) {
				OutStockRecordBean stockOutRecord = outStockRecords.stream()
						.filter(s -> s.getOrder_id().startsWith("PL")).findFirst().orElse(outStockRecords.get(0));
				String order_id = stockOutRecord.getOrder_id();
				String sku_id = stockOutRecord.getSku_id();
				BigDecimal price = stockOutRecord.getPrice().add(new BigDecimal("0.1"));

				OutStockPriceUpdateParam outStockPriceUpdateParam = new OutStockPriceUpdateParam();
				outStockPriceUpdateParam.setSheet_id(order_id);
				outStockPriceUpdateParam.setSku_id(sku_id);
				outStockPriceUpdateParam.setPrice(price);

				List<OutStockPriceUpdateParam> outStockPriceUpdateParams = Arrays.asList(outStockPriceUpdateParam);

				result = outStockService.updateOutStockPrice(outStockPriceUpdateParams);
				Assert.assertEquals(result, true, "修改出库均价失败");

				Thread.sleep(2000);

				outStockRecords = stockRecordService.outStockRecords(stockOutRecordFilterParam);
				Assert.assertNotEquals(outStockRecords, null, "搜索过滤出库记录失败");

				stockOutRecord = outStockRecords.stream()
						.filter(s -> s.getOrder_id().equals(order_id) && s.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(stockOutRecord, null, "修复出库均价后,出库记录没有找到");

				String msg = null;
				if (stockOutRecord.getPrice().compareTo(price) != 0) {
					msg = String.format("订单%s里的商品%s修复库存均价后,出库记录没有更新,预期:%s,实际:%s", order_id, sku_id, price,
							stockOutRecord.getPrice());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				OutStockDetailBean outStockDetail = outStockService.getOutStockDetailInfo(order_id);
				Assert.assertNotEquals(outStockDetail, null, "获取出库单 " + order_id + "的详细信息失败");

				OutStockDetailBean.Detail detail = outStockDetail.getDetails().stream()
						.filter(d -> d.getId().equals(sku_id)).findAny().orElse(null);

				Assert.assertNotEquals(detail, null, "出库单" + order_id + "里没有找到商品 " + sku_id);

				if (detail.getSale_price().compareTo(price) != 0) {
					msg = String.format("出库单%s里的商品[%s,%s]修复库存均价后,出库成本价没有更新,预期:%s,实际:%s", order_id, sku_id,
							detail.getName(), price, detail.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "修复出库成本价后,信息与预期不符");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test
	public void filterStockOutSheetTestCase01() {
		String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
		// 按建单日期搜索
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam(2, 0, null, 0, 10, today,
				today);
		try {
			ReporterCSS.title("测试点: 搜索过滤成品出库单");
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索成品出库单失败");
		} catch (Exception e) {
			logger.error("搜索成品出库单遇到错误: ", e);
			Assert.fail("搜索成品出库单遇到错误: ", e);
		}
	}

	@Test
	public void filterStockOutSheetTestCase02() {
		String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
		// 按出库日期搜索
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam(1, 0, null, 0, 10, today,
				today);
		try {
			ReporterCSS.title("测试点: 搜索过滤成品出库单");
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索成品出库单失败");
		} catch (Exception e) {
			logger.error("搜索成品出库单遇到错误: ", e);
			Assert.fail("搜索成品出库单遇到错误: ", e);
		}
	}

	@Test
	public void filterStockOutSheetTestCase03() {
		try {
			ReporterCSS.title("测试点: 按运营时间搜索过滤出库单");
			List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
			Assert.assertNotEquals(serviceTimeList, null, "获取站点运营时间失败");
			Assert.assertEquals(serviceTimeList.size() >= 1, true, "站点无运营时间,无法进行后续操作");

			ServiceTimeBean serviceTime = serviceTimeList.get(0);
			String time_config_id = serviceTime.getId();

			String today = LocalDate.now().toString();
			String receive_time_start = today + " " + serviceTime.getReceive_time_limit().getStart();

			String receive_time_end = TimeUtil.calculateTime("yyyy-MM-dd", today,
					serviceTime.getReceive_time_limit().getE_span_time(), Calendar.DAY_OF_MONTH) + " "
					+ serviceTime.getReceive_time_limit().getEnd();

			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam(0, null, 0, 10,
					time_config_id, receive_time_start, receive_time_end);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索成品出库单失败");
		} catch (Exception e) {
			logger.error("按运营时间搜索出库单遇到错误: ", e);
			Assert.fail("按运营时间搜索出库单遇到错误: ", e);
		}
	}

	@Test
	public void filterStockOutSheetTestCase04() {
		try {
			ReporterCSS.title("测试点: 按收货时间过滤出库单");

			String end = TimeUtil.calculateTime("yyyy-MM-dd", today, 1, Calendar.MONTH);
			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setType(4);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setStart(today);
			outStockSheetFilterParam.setEnd(end);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索成品出库单失败");
		} catch (Exception e) {
			logger.error("按运营时间搜索出库单遇到错误: ", e);
			Assert.fail("按运营时间搜索出库单遇到错误: ", e);
		}
	}

	@Test
	public void exportStockOutSheetTestCase01() {
		ReporterCSS.title("测试点: 导出成品出库单");
		String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
		OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam(2, 0, null, 0, 10, today,
				today);
		outStockSheetFilterParam.setExport();
		try {
			boolean result = outStockService.exportOutStockSheet(outStockSheetFilterParam);
			Assert.assertEquals(result, true, "导出成品出库单失败");
		} catch (Exception e) {
			logger.error("导出成品出库单遇到错误: ", e);
			Assert.fail("导出成品出库单遇到错误: ", e);
		}
	}

	@Test
	public void printStockOutSheet() {
		ReporterCSS.title("测试点: 打印销售出库单");
		try {
			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setStart_date_new(yestoday + " 00:00");
			outStockSheetFilterParam.setEnd_date_new(tommrow + " 00:00");
			outStockSheetFilterParam.setOffset(0);
			outStockSheetFilterParam.setLimit(10);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setType(2);

			List<OutStockSheetBean> outStockSheets = outStockService.searchOutStockSheet(outStockSheetFilterParam);
			Assert.assertNotEquals(outStockSheets, null, "销售出库单搜索过滤失败");

			Assert.assertEquals(outStockSheets.size() > 0, true, "销售出库单列表为空,无法进行打印操作");

			List<String> out_stock_sheet_ids = outStockSheets.stream().map(s -> s.getId()).collect(Collectors.toList());

			List<OutStockDetailBean> outStockDetailList = outStockService.printStockOutSheet(out_stock_sheet_ids);
			Assert.assertNotEquals(outStockDetailList, null, "销售出库单打印失败");
		} catch (Exception e) {
			logger.error("打印销售出库单遇到错误: ", e);
			Assert.fail("打印销售出库单遇到错误: ", e);
		}
	}
}
