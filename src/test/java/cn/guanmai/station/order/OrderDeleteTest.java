package cn.guanmai.station.order;

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

import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 12, 2018 11:08:55 AM 
* @des 创建订单测试
* @version 1.0 
*/
public class OrderDeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderDeleteTest.class);
	private OrderService orderService;
	private OrderTool orderTool;
	private OutStockService stockOutService;
	private PurchaseTaskTool purchaseTaskTool;
	private String order_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OrderFilterParam paramBean;
	private PurchaseTaskService purchaseTaskService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
		stockOutService = new OutStockServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(5);
			Assert.assertNotEquals(order_id, null, "创建订单失败");
			paramBean = new OrderFilterParam();
			paramBean.setStart_date(todayStr);
			paramBean.setEnd_date(todayStr);
			paramBean.setQuery_type(1);
			paramBean.setSearch_text(order_id);
			paramBean.setOffset(0);
			paramBean.setLimit(20);
		} catch (Exception e) {
			logger.error("创建订单过程中遇到错误: ", e);
			Assert.fail("创建订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase01() {
		try {
			Reporter.log("测试点: 删除状态为待分拣的订单");

			boolean result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除状态为待分拣的订单失败");

			List<OrderBean> orderDetailArray = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orderDetailArray, null, "订单列表接口调用失败");

			Assert.assertEquals(orderDetailArray.size(), 0, "订单 " + order_id + " 没有删除成功,还可以搜索拉取到");
		} catch (Exception e) {
			logger.error("删除订单过程中遇到错误: ", e);
			Assert.fail("删除订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase02() {
		try {
			Reporter.log("测试点: 删除状态为分拣的订单");

			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单状态由待分拣改为分拣中失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除状态为分拣中的订单失败");

			List<OrderBean> orderDetailArray = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orderDetailArray, null, "订单列表接口调用失败");

			Assert.assertEquals(orderDetailArray.size(), 0, "订单 " + order_id + " 没有删除成功,还可以搜索拉取到");
		} catch (Exception e) {
			logger.error("删除订单过程中遇到错误: ", e);
			Assert.fail("删除订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase03() {
		try {
			Reporter.log("测试点: 删除状态为配送中的订单");
			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "订单状态由待分拣改为配送中失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除状态为配送中的订单失败");

			List<OrderBean> orderDetailArray = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orderDetailArray, null, "订单列表接口调用失败");

			Assert.assertEquals(orderDetailArray.size(), 0, "订单 " + order_id + " 没有删除成功,还可以搜索拉取到");
		} catch (Exception e) {
			logger.error("删除订单过程中遇到错误: ", e);
			Assert.fail("删除订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase04() {
		try {
			Reporter.log("测试点: 删除状态为已签收的订单");
			boolean result = orderService.updateOrderState(order_id, 15);
			Assert.assertEquals(result, true, "订单状态由待分拣改为已签收失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除状态为已签收的订单失败");

			List<OrderBean> orderDetailArray = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orderDetailArray, null, "订单列表接口调用失败");

			Assert.assertEquals(orderDetailArray.size(), 0, "订单 " + order_id + " 没有删除成功,还可以搜索拉取到");
		} catch (Exception e) {
			logger.error("删除订单过程中遇到错误: ", e);
			Assert.fail("删除订单过程中遇到错误: ", e);
		}
	}

	/**
	 * 
	 * 删除状态为待分拣的订单
	 * 
	 */
	@Test
	public void orderDeleteTestCase05() {
		try {
			Reporter.log("测试点: 删除订单,并验证出库单是否同步修改状态");

			// 验证出库单是否同步删除
			OutStockSheetFilterParam outStockSheetFilterParam = new OutStockSheetFilterParam();
			outStockSheetFilterParam.setType(2);
			outStockSheetFilterParam.setStatus(0);
			outStockSheetFilterParam.setSearch_text(order_id);
			outStockSheetFilterParam.setOffset(0);
			outStockSheetFilterParam.setLimit(10);
			outStockSheetFilterParam.setStart(todayStr);
			outStockSheetFilterParam.setEnd(todayStr);

			List<OutStockSheetBean> outStockSheetList = null;
			int loopCount = 15;
			boolean exist = false;
			while (loopCount > 0) {
				outStockSheetList = stockOutService.searchOutStockSheet(outStockSheetFilterParam);
				if (outStockSheetList.size() == 0) {
					Thread.sleep(2000);
					loopCount--;
					continue;
				} else {
					exist = true;
					break;
				}
			}
			Assert.assertEquals(exist, true, "订单 " + order_id + " 在30秒钟内没有生成对应的出库单");

			boolean result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单操作失败");

			List<OrderBean> orderDetailArray = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orderDetailArray, null, "订单列表接口调用失败");

			Assert.assertEquals(orderDetailArray.size(), 0, "订单 " + order_id + " 没有删除成功,还可以搜索拉取到");

			loopCount = 15;
			result = false;
			while (loopCount > 0) {
				outStockSheetList = stockOutService.searchOutStockSheet(outStockSheetFilterParam);
				OutStockSheetBean stockOutSheet = outStockSheetList.stream().filter(s -> s.getId().equals(order_id))
						.findAny().orElse(null);
				if (stockOutSheet.getStatus() == 3) {
					result = true;
					break;
				} else {
					Thread.sleep(2000);
				}
				loopCount -= 1;
			}
			Assert.assertEquals(result, true, "30秒后,出库单 " + order_id + " 没有随着订单删除而同步改变状态");

		} catch (Exception e) {
			logger.error("删除订单,并验证出库单是否同步修改状态过程中遇到错误: ", e);
			Assert.fail("删除订单,并验证出库单是否同步修改状态过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase06() {
		try {
			Reporter.log("测试点: 验证订单删除后,采购任务是否同步删除");
			// 采购任务过滤对象参数
			PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd") + " 00:00:00";
			param.setBegin_time(today);
			param.setEnd_time(today);
			param.setQ(order_id);
			param.setQ_type(1);
			param.setLimit(10);

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");

			boolean result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单失败");

			Thread.sleep(2000);

			PurchaseTaskBean purcahseTask = purchaseTaskService.searchPurchaseTask(param);
			Assert.assertEquals(purcahseTask != null && purcahseTask.getPurchaseTaskDataArray() == null, true,
					"订单 " + order_id + " 对应的采购任务没有同步删除");
		} catch (Exception e) {
			logger.error("删除订单,验证采购任务是否同步删除遇到错误: ", e);
			Assert.fail("删除订单,验证采购任务是否同步删除遇到错误: ", e);
		}
	}

	/**
	 * 用来删除订单列表,这个用例不在线上跑
	 */
	public void deleteAllOrders() {
		OrderFilterParam param = new OrderFilterParam();
		int limit = 50;
		param.setQuery_type(1);
		param.setStart_date(todayStr);
		param.setEnd_date(todayStr);
		param.setLimit(50);

		int offset = 0;
		try {
			while (true) {
				param.setOffset(offset * limit);
				List<OrderBean> tempArray = orderService.searchOrder(param);

				List<String> order_ids = tempArray.stream().map(o -> o.getId()).collect(Collectors.toList());

				for (String id : order_ids) {
					orderService.deleteOrder(id);
				}

				if (tempArray.size() < limit) {
					break;
				}
				offset++;
			}
			System.out.println("完");
		} catch (Exception e) {
			logger.error("删除订单遇到错误: ", e);
			Assert.fail("删除订单遇到错误: ", e);
		}
	}
}
