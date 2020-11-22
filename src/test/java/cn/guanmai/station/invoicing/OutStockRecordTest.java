package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.OutStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年12月31日
 * @time 下午5:26:04
 * @des 销售出库记录
 */

public class OutStockRecordTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OutStockRecordTest.class);
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OutStockService outStockService;
	private StockRecordService stockRecordService;
	private OrderService orderService;
	private AsyncService asyncService;
	private OrderTool orderTool;
	private OutStockDetailBean outStockDetail;
	private OrderDetailBean orderDetail;
	private OrderCycle orderCycle;
	private String order_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		outStockService = new OutStockServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		orderTool = new OrderTool(headers);

		try {
			order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotEquals(order_id, null, "创建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + orderDetail + "详细信息失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单 " + order_id + " 所处运营时间相关信息失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单" + order_id + "状态为配送中,修改失败");

			InStockTool stockInTool = new InStockTool(headers);
			result = stockInTool.createInStockSheetForOrder(order_id);
			Assert.assertEquals(result, true, "为订单 " + order_id + " 新建采购入库单失败");

			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setType(2);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setStart(todayStr);
			outStockSheetFilterParam.setEnd(todayStr);
			outStockSheetFilterParam.setSearch_text(order_id);
			outStockSheetFilterParam.setOffset(0);
			outStockSheetFilterParam.setLimit(10);
			List<OutStockSheetBean> stockOutSheetList = null;
			OutStockSheetBean stockOutSheet = null;
			int times = 20;
			while (times-- >= 0) {
				stockOutSheetList = outStockService.searchOutStockSheet(outStockSheetFilterParam);
				Assert.assertNotEquals(stockOutSheetList, null, "搜索过滤销售出库单失败");

				if (stockOutSheetList.size() > 0) {
					stockOutSheet = stockOutSheetList.stream().filter(s -> s.getId().equals(order_id)).findAny()
							.orElse(null);
					break;
				}
				Thread.sleep(3000);

			}
			Assert.assertNotEquals(stockOutSheet, null, "订单在生成60秒后,对应的销售出库单" + order_id + "还没有生成");
			outStockDetail = outStockService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(outStockDetail, null, "获取成品出库单详情失败");

			String task_url = outStockService.batchOutStock(outStockSheetFilterParam);
			Assert.assertNotEquals(task_url, null, "销售出库批量出库创建异步任务失败");

			BigDecimal task_id = new BigDecimal(task_url.split("=")[1]);

			result = asyncService.getAsyncTaskResult(task_id, "批量出库(成功1)");
			Assert.assertEquals(result, true, "销售出库单批量出库异步任务执行失败");

			// 再次请求,获取出库均价信息
			outStockDetail = outStockService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(outStockDetail, null, "获取成品出库单详情失败");
		} catch (Exception e) {
			logger.error("新建出库单后再出库过程中遇到错误: ", e);
			Assert.fail("新建出库单后再出库过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase01() {
		ReporterCSS.title("测试点: 按出库日期查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase02() {
		ReporterCSS.title("测试点: 按建单日期查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase03() {
		ReporterCSS.title("测试点: 按运营时间查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(3);
			stockRecordParam.setTime_config_id(orderCycle.getTime_config_id());
			stockRecordParam.setBegin(orderCycle.getCycle_start_time().substring(0, 10));
			stockRecordParam.setEnd(orderCycle.getCycle_end_time().substring(0, 10));

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase04() {
		ReporterCSS.title("测试点: 按收货日期查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(4);
			stockRecordParam.setBegin(orderDetail.getCustomer().getReceive_begin_time().substring(0, 10));
			stockRecordParam.setEnd(orderDetail.getCustomer().getReceive_end_time().substring(0, 10));

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase05() {
		ReporterCSS.title("测试点: 按[出库日期+商品ID]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_id = detail.getId();
			stockRecordParam.setText(sku_id);
			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_ids = targetStockOutRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "按[出库日期+商品ID]查询出库记录,过滤出了不符合搜索输入ID " + sku_id + "的出库记录结果 " + sku_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase06() {
		ReporterCSS.title("测试点: 按[建单日期+商品ID]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_id = detail.getId();
			stockRecordParam.setText(sku_id);
			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_ids = targetStockOutRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "按[建单日期+商品ID]查询出库记录,过滤出了不符合搜索输入ID " + sku_id + "的出库记录结果 " + sku_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase07() {
		ReporterCSS.title("测试点: 按[运营时间+商品ID]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(3);
			stockRecordParam.setTime_config_id(orderCycle.getTime_config_id());
			stockRecordParam.setBegin(orderCycle.getCycle_start_time().substring(0, 10));
			stockRecordParam.setEnd(orderCycle.getCycle_end_time().substring(0, 10));

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_id = detail.getId();
			stockRecordParam.setText(sku_id);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_ids = targetStockOutRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "按[运用时间+商品ID]查询出库记录,过滤出了不符合搜索输入ID " + sku_id + "的出库记录结果 " + sku_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase08() {
		ReporterCSS.title("测试点: 按[收货日期+商品ID]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(4);
			stockRecordParam.setBegin(orderDetail.getCustomer().getReceive_begin_time().substring(0, 10));
			stockRecordParam.setEnd(orderDetail.getCustomer().getReceive_end_time().substring(0, 10));

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_id = detail.getId();
			stockRecordParam.setText(sku_id);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_ids = targetStockOutRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "按[收货时间+商品ID]查询出库记录,过滤出了不符合搜索输入ID " + sku_id + "的出库记录结果 " + sku_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase09() {
		ReporterCSS.title("测试点: 按[出库日期+商品名称]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_name = detail.getName();
			stockRecordParam.setText(sku_name);
			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_names = targetStockOutRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_names.size(), 0,
					"按[出库日期+商品名称]查询出库记录,过滤出了不符合搜索输入商品名称 " + sku_name + "的出库记录结果 " + sku_names);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase10() {
		ReporterCSS.title("测试点: 按[建单日期+商品名称]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_name = detail.getName();
			stockRecordParam.setText(sku_name);
			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_names = targetStockOutRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_names.size(), 0,
					"按[建单日期+商品名称]查询出库记录,过滤出了不符合搜索输入商品名称 " + sku_name + "的出库记录结果 " + sku_names);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase11() {
		ReporterCSS.title("测试点: 按[运营时间+商品名称]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(3);
			stockRecordParam.setTime_config_id(orderCycle.getTime_config_id());
			stockRecordParam.setBegin(orderCycle.getCycle_start_time().substring(0, 10));
			stockRecordParam.setEnd(orderCycle.getCycle_end_time().substring(0, 10));

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_name = detail.getName();
			stockRecordParam.setText(sku_name);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_names = targetStockOutRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_names.size(), 0,
					"按[运用时间+商品名称]查询出库记录,过滤出了不符合搜索输入商品名称 " + sku_name + "的出库记录结果 " + sku_names);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockOutRecordTestCase12() {
		ReporterCSS.title("测试点: 按[收货日期+商品名称]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(4);
			stockRecordParam.setBegin(orderDetail.getCustomer().getReceive_begin_time().substring(0, 10));
			stockRecordParam.setEnd(orderDetail.getCustomer().getReceive_end_time().substring(0, 10));

			List<OutStockDetailBean.Detail> details = outStockDetail.getDetails();
			OutStockDetailBean.Detail detail = NumberUtil.roundNumberInList(details);
			List<OutStockDetailBean.Detail> temp_details = new ArrayList<OutStockDetailBean.Detail>();
			temp_details.add(detail);

			String sku_name = detail.getName();
			stockRecordParam.setText(sku_name);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> sku_names = targetStockOutRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_names.size(), 0,
					"按[收货时间+商品名称]查询出库记录,过滤出了不符合搜索输入商品名称 " + sku_name + "的出库记录结果 " + sku_names);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	private List<String> category_id_1;
	private List<String> category_id_2;

	@Test
	public void stockOutRecordTestCase13() {
		ReporterCSS.title("测试点: 按[出库日期]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			category_id_1 = new ArrayList<String>();
			category_id_2 = new ArrayList<String>();
			for (OutStockRecordBean stockOutRecord : targetStockOutRecordArray) {
				if (!category_id_1.contains(stockOutRecord.getCategory_id_1())) {
					category_id_1.add(stockOutRecord.getCategory_id_1());
				}
				if (!category_id_2.contains(stockOutRecord.getCategory_id_2())) {
					category_id_2.add(stockOutRecord.getCategory_id_2());
				}
			}
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockOutRecordTestCase13" })
	public void stockOutRecordTestCase14() {
		ReporterCSS.title("测试点: 按[出库日期+商品分类]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> temp_category2_ids = targetStockOutRecordArray.stream()
					.filter(r -> !category_id_2.contains(r.getCategory_id_2())).map(r -> r.getCategory_id_2())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_category2_ids.size(), 0,
					"按[出库日期+商品分类]查询出库记录,过滤出了不符合搜索商品分类 " + category_id_2 + "的出库记录结果 " + temp_category2_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockOutRecordTestCase13" })
	public void stockOutRecordTestCase15() {
		ReporterCSS.title("测试点: 按[建单日期+商品分类]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> temp_category2_ids = targetStockOutRecordArray.stream()
					.filter(r -> !category_id_2.contains(r.getCategory_id_2())).map(r -> r.getCategory_id_2())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_category2_ids.size(), 0,
					"按[出库日期+商品分类]查询出库记录,过滤出了不符合搜索商品分类 " + category_id_2 + "的出库记录结果 " + temp_category2_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockOutRecordTestCase13" })
	public void stockOutRecordTestCase16() {
		ReporterCSS.title("测试点: 按[运营时间+商品分类]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(3);
			stockRecordParam.setTime_config_id(orderCycle.getTime_config_id());
			stockRecordParam.setBegin(orderCycle.getCycle_start_time().substring(0, 10));
			stockRecordParam.setEnd(orderCycle.getCycle_end_time().substring(0, 10));

			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> temp_category2_ids = targetStockOutRecordArray.stream()
					.filter(r -> !category_id_2.contains(r.getCategory_id_2())).map(r -> r.getCategory_id_2())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_category2_ids.size(), 0,
					"按[出库日期+商品分类]查询出库记录,过滤出了不符合搜索商品分类 " + category_id_2 + "的出库记录结果 " + temp_category2_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockOutRecordTestCase13" })
	public void stockOutRecordTestCase17() {
		ReporterCSS.title("测试点: 按[收货日期+商品名称]查询出库记录");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(4);
			stockRecordParam.setBegin(orderDetail.getCustomer().getReceive_begin_time().substring(0, 10));
			stockRecordParam.setEnd(orderDetail.getCustomer().getReceive_end_time().substring(0, 10));

			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			// 成品出库记录是倒序排列的,下单只有最多6个商品,所以出库日志查询一次就行了
			List<OutStockRecordBean> stockOutRecordArray = stockRecordService.outStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockOutRecordArray, null, "获取成品出库日志失败");

			// 把目标成品退货单的退货记录给过滤出来
			List<OutStockRecordBean> targetStockOutRecordArray = stockOutRecordArray.stream()
					.filter(record -> record.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = compareData(outStockDetail.getDetails(), targetStockOutRecordArray);
			Assert.assertEquals(result, true, "出库单 " + order_id + " 出库记录与预期不符");

			List<String> temp_category2_ids = targetStockOutRecordArray.stream()
					.filter(r -> !category_id_2.contains(r.getCategory_id_2())).map(r -> r.getCategory_id_2())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_category2_ids.size(), 0,
					"按[出库日期+商品分类]查询出库记录,过滤出了不符合搜索商品分类 " + category_id_2 + "的出库记录结果 " + temp_category2_ids);
		} catch (Exception e) {
			logger.error("查询出库记录过程中遇到错误: ", e);
			Assert.fail("查询出库记录过程中遇到错误: ", e);
		}
	}

	public boolean compareData(List<OutStockDetailBean.Detail> details,
			List<OutStockRecordBean> targetStockOutRecordArray) {
		// 循环判断是否每个商品都记录了日志
		boolean result = true;
		String msg = null;
		for (OutStockDetailBean.Detail det : details) {
			String sku_id = det.getId(); // 出库SKU ID
			OutStockRecordBean targetRecord = targetStockOutRecordArray.stream()
					.filter(record -> record.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (targetRecord == null) {
				msg = String.format("出库单%s中的出库商品,sku_id:%s,sku_name:%s没有记录出库日志", order_id, sku_id, det.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			} else {
				BigDecimal expected_stock_out = det.getQuantity();
				BigDecimal actual_stock_out = targetRecord.getOut_stock_sale();
				if (expected_stock_out.compareTo(actual_stock_out) != 0) {
					result = false;
					msg = String.format("出库单%s中的出库商品%s,实际出库数:%s,出库日志记录的出库数数:%s", order_id, expected_stock_out,
							actual_stock_out);
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				BigDecimal expected_sale_price = det.getSale_price();
				BigDecimal actual_sale_price = targetRecord.getPrice();
				if (expected_sale_price.compareTo(actual_sale_price) != 0) {
					result = false;
					msg = String.format("出库单%s中的出库商品%s,实际成本单价:%s,出库日志记录的成本单价:%s", order_id, sku_id, expected_sale_price,
							actual_sale_price);
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

			}
		}
		return result;
	}

}
