package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderPurchaseTaskParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2020年1月13日
 * @time 上午10:29:17
 * @des 订单相关操作测试
 */

public class OrderPurchaseTaskTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderPurchaseTaskTest.class);
	private PurchaseTaskService purchaseTaskService;
	private OrderService orderService;
	private PurchaseTaskTool purchaseTaskTool;
	private ProfileService profileService;
	private AsyncService asyncService;
	private OrderTool orderTool;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaseTaskTool = new PurchaseTaskTool(headers);
		profileService = new ProfileServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(1);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "系统设置-订单设置-设置订单商品手动进入采购任务失败");
		} catch (Exception e) {
			logger.error("系统设置-订单设置-设置订单商品手动进入采购任务遇到错误: ", e);
			Assert.fail("系统设置-订单设置-设置订单商品手动进入采购任务遇到错误: ", e);
		}
	}

	@Test
	public void orderPurchaseTaskTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤订单,操作整个订单手工生成采购任务");
		try {
			ReporterCSS.step("步骤一: 创建订单");
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			ReporterCSS.step("步骤二: 操作订单手工生成采购任务");
			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			String tomorrowStr = TimeUtil.calculateTime("yyyy-MM-dd 00:00", todayStr, 1, Calendar.DATE);
			orderSkuFilterParam.setStart_date_new(todayStr);
			orderSkuFilterParam.setEnd_date_new(tomorrowStr);
			orderSkuFilterParam.setSearch_text(order_id);
			BigDecimal task_id = purchaseTaskService.createPurchaseTaskByOrder(orderSkuFilterParam);
			Assert.assertNotEquals(task_id, null, "操作订单手工生成采购任务失败");
			boolean result = asyncService.getAsyncTaskResult(task_id, "");
			Assert.assertEquals(result, true, "订单手工生成采购任务的异步任务执行失败");

			ReporterCSS.step("步骤三: 验证手工操作采购任务是否生成了?");
			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");
		} catch (Exception e) {
			logger.error("设置订单手工生成采购任务过程中遇到错误: ", e);
			Assert.fail("设置订单手工生成采购任务过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderPurchaseTaskTestCase02() {
		ReporterCSS.title("测试点: 搜索过滤订单,操作整个订单手工生成采购任务");
		try {
			ReporterCSS.step("步骤一: 创建订单");
			String order_id = orderTool.oneStepCreateOrder(10);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<OrderPurchaseTaskParam> orderPurchaseTaskParams = new ArrayList<OrderPurchaseTaskParam>();
			OrderPurchaseTaskParam orderPurchaseTaskParam = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				orderPurchaseTaskParam = new OrderPurchaseTaskParam();
				orderPurchaseTaskParam.setOrder_id(order_id);
				orderPurchaseTaskParam.setPlan_purchase_amount(detail.getQuantity());
				orderPurchaseTaskParam.setSku_id(detail.getSku_id());
				if (detail.getDetail_id() != null) {
					orderPurchaseTaskParam.setDetail_id(detail.getDetail_id());
				}
				orderPurchaseTaskParams.add(orderPurchaseTaskParam);
			}

			BigDecimal task_id = purchaseTaskService.createPurchaseTaskByOrder(orderPurchaseTaskParams);
			Assert.assertNotEquals(task_id, null, "操作订单手工生成采购任务失败");
			boolean result = asyncService.getAsyncTaskResult(task_id, "完成");
			Assert.assertEquals(result, true, "订单手工生成采购任务的异步任务执行失败");

			ReporterCSS.step("步骤三: 验证手工操作采购任务是否生成了?");
			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");
		} catch (Exception e) {
			logger.error("设置订单手工生成采购任务过程中遇到错误: ", e);
			Assert.fail("设置订单手工生成采购任务过程中遇到错误: ", e);
		}
	}

	@AfterClass
	public void afterClass() {
		try {
			ReporterCSS.step("后置处理: 恢复系统设置");

			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);

			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "系统设置-订单设置-设置订单商品自动进入采购任务失败");
		} catch (Exception e) {
			logger.error("系统设置-订单设置-设置订单商品自动进入采购任务遇到错误: ", e);
			Assert.fail("系统设置-订单设置-设置订单商品自动进入采购任务遇到错误: ", e);
		}
	}
}
