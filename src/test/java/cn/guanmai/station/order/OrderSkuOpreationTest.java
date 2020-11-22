package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.order.OrderChangeSkuResultBean;
import cn.guanmai.station.bean.order.OrderDeleteSkuResultBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderChangeSkuParam;
import cn.guanmai.station.bean.order.param.OrderDeleteSkuParam;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年7月8日 下午5:01:00
 * @description: // 订单-按商品列表-商品列表的相关操作
 * @version: 1.0
 */

public class OrderSkuOpreationTest extends LoginStation {
	class OrderChangeSkuResult {
		private String sku_id;
		private BigDecimal quantity;
		private BigDecimal sale_price;
		private String remark;

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getSale_price() {
			return sale_price;
		}

		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	private Logger logger = LoggerFactory.getLogger(OrderSkuOpreationTest.class);
	private OrderTool orderTool;
	private OrderService orderService;
	private AsyncService asyncService;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
	private String order_id;
	private String abnormal_sku_id;
	private BigDecimal detail_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		try {
			order_id = orderTool.oneStepCreateOrder(10);
			Assert.assertNotEquals(order_id, null, "新建订单失败");
		} catch (Exception e) {
			logger.error("新建订单遇到错误: ", e);
			Assert.fail("新建订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSkuOpreationTestCase01() {
		ReporterCSS.title("测试点: 批量替换订单商品(并且更新数量和备注)");
		try {
			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			orderSkuFilterParam.setStart_date_new(today);
			String torrrow = TimeUtil.calculateTime("yyyy-MM-dd 00:00", today, 1, Calendar.DATE);
			orderSkuFilterParam.setEnd_date_new(torrrow);
			orderSkuFilterParam.setSearch_text(order_id);

			List<OrderSkuFilterResultBean> orderSkuFilterResults = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkuFilterResults, null, "订单商品列表搜索过滤失败");

			List<OrderChangeSkuParam> orderChangeSkuParams = new ArrayList<OrderChangeSkuParam>();

			OrderChangeSkuParam orderChangeSkuParam = null;
			String salemenu_id = null;

			List<OrderChangeSkuResult> orderChangeSkuResults = new ArrayList<OrderChangeSkuResult>();

			// 报价单对应其商品
			Map<String, List<String>> salemenu_sku_map = new HashMap<String, List<String>>();

			// 订单里的商品
			List<String> order_sku_ids = orderSkuFilterResults.stream().map(s -> s.getSku_id())
					.collect(Collectors.toList());

			for (OrderSkuFilterResultBean orderSkuFilterResult : orderSkuFilterResults) {
				salemenu_id = orderSkuFilterResult.getSalemenu_id();
				List<String> temp_sku_ids = null;
				if (salemenu_sku_map.containsKey(salemenu_id)) {
					temp_sku_ids = salemenu_sku_map.get(salemenu_id);
				} else {
					temp_sku_ids = orderService.searchOrderChangeSku(salemenu_id, "a");
					Assert.assertNotEquals(temp_sku_ids, null, "订单替换商品,搜索过滤替换商品失败");
					salemenu_sku_map.put(salemenu_id, temp_sku_ids);
				}
				OrderChangeSkuResult orderChangeSkuResult = new OrderChangeSkuResult();

				// 如果订单里的商品已经全部包含了报价单的商品,则此商品没有必要替换了
				if (order_sku_ids.containsAll(temp_sku_ids)) {
					orderChangeSkuResult.setSku_id(orderSkuFilterResult.getSku_id());
					orderChangeSkuResult.setSale_price(orderSkuFilterResult.getSale_price());
					orderChangeSkuResult.setQuantity(orderSkuFilterResult.getQuantity());
					orderChangeSkuResult.setRemark(orderSkuFilterResult.getSpu_remark());
					orderChangeSkuResults.add(orderChangeSkuResult);
					continue;
				}

				// 选出一个还未下单的商品
				String sku_id = null;
				while (true) {
					sku_id = NumberUtil.roundNumberInList(temp_sku_ids);
					if (order_sku_ids.contains(sku_id)) {
						continue;
					}
					break;
				}

				// 更新订单下单商品列表
				Collections.replaceAll(order_sku_ids, orderSkuFilterResult.getSku_id(), sku_id);

				BigDecimal quantity = NumberUtil.getRandomNumber(5, 10, 1);
				String remark = StringUtil.getRandomString(6);
				orderChangeSkuResult.setQuantity(quantity);
				orderChangeSkuResult.setSku_id(sku_id);
				orderChangeSkuResult.setRemark(remark);
				orderChangeSkuResults.add(orderChangeSkuResult);

				orderChangeSkuParam = new OrderChangeSkuParam();
				orderChangeSkuParam.setChange_quantity(NumberUtil.getRandomNumber(4, 8, 1));
				orderChangeSkuParam.setNew_sku_id(sku_id);
				orderChangeSkuParam.setChange_quantity(quantity);
				orderChangeSkuParam.setChange_type(1);
				orderChangeSkuParam.setSpu_remark(remark);
				orderChangeSkuParam.setSku_id(orderSkuFilterResult.getSku_id());
				orderChangeSkuParam.setSalemenu_name(orderSkuFilterResult.getSalemenu_name());

				List<OrderChangeSkuParam.OrderInfo> orderInfos = new ArrayList<OrderChangeSkuParam.OrderInfo>();
				OrderChangeSkuParam.OrderInfo orderInfo = orderChangeSkuParam.new OrderInfo();
				orderInfo.setOrder_id(order_id);
				orderInfo.setQuantity(orderSkuFilterResult.getQuantity());
				orderInfo.setResname(orderSkuFilterResult.getResname());

				orderInfos.add(orderInfo);
				orderChangeSkuParam.setOrder_infos(orderInfos);
				orderChangeSkuParams.add(orderChangeSkuParam);
			}

			BigDecimal task_id = orderService.orderChangeSkus(orderChangeSkuParams);
			Assert.assertNotEquals(task_id, null, "订单替换商品,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "异步任务执行失败");

			// 结果判断
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详情失败");
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			List<String> actul_order_sku_ids = details.stream().map(s -> s.getSku_id()).collect(Collectors.toList());

			String msg = null;
			if (actul_order_sku_ids.size() == order_sku_ids.size()) {
				for (OrderDetailBean.Detail detail : details) {
					String sku_id = detail.getSku_id();
					OrderChangeSkuResult orderChangeSkuResult = orderChangeSkuResults.stream()
							.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
					if (orderChangeSkuResult == null) {
						msg = String.format("订单%s商品%s在预期结果列表中没有找到", order_id, sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (detail.getQuantity().compareTo(orderChangeSkuResult.getQuantity()) != 0) {
						msg = String.format("订单%s的商品%s下单数与预期不符,预期:%s,实际:%s", order_id, sku_id,
								orderChangeSkuResult.getQuantity(), detail.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (!detail.getSpu_remark().equals(orderChangeSkuResult.getRemark())) {
						msg = String.format("订单%s的商品%s备注信息与预期不符,预期:%s,实际:%s", order_id, sku_id,
								orderChangeSkuResult.getRemark(), detail.getSpu_remark());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			} else {
				msg = String.format("订单里的商品替换后,实际商品列表与预期不一致,预期:%s,实际:%s", order_sku_ids, actul_order_sku_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "订单" + order_id + "商品替换后,结果信息与预期不一致");
		} catch (Exception e) {
			logger.error("批量替换订单商品遇到错误: ", e);
			Assert.fail("批量替换订单商品遇到错误: ", e);
		}
	}

	@Test
	public void orderSkuOpreationTestCase02() {
		ReporterCSS.title("测试点: 批量删除订单里的商品(选中删除)");
		try {
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			List<String> order_sku_ids = details.stream().map(d -> d.getSku_id()).collect(Collectors.toList());

			List<String> delete_sku_ids = NumberUtil.roundNumberInList(order_sku_ids, 2).stream()
					.collect(Collectors.toList());

			List<OrderDeleteSkuParam> orderDeleteSkuParams = new ArrayList<OrderDeleteSkuParam>();
			OrderDeleteSkuParam orderDeleteSkuParam = null;
			for (String sku_id : delete_sku_ids) {
				orderDeleteSkuParam = new OrderDeleteSkuParam();
				orderDeleteSkuParam.setOrder_ids(Arrays.asList(order_id));
				orderDeleteSkuParam.setSku_id(sku_id);
				orderDeleteSkuParams.add(orderDeleteSkuParam);
			}

			BigDecimal task_id = orderService.orderDeleteSkus(orderDeleteSkuParams);
			Assert.assertNotEquals(task_id, null, "批量删除订单商品,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除订单商品的异步任务执行失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			List<String> after_deleted_sku_ids = orderDetail.getDetails().stream().map(d -> d.getSku_id())
					.collect(Collectors.toList());

			order_sku_ids.removeAll(delete_sku_ids);

			ReporterCSS.step("预期商品列表:" + order_sku_ids);
			ReporterCSS.step("实际商品列表:" + after_deleted_sku_ids);

			logger.info("预期商品列表:" + order_sku_ids);
			logger.info("实际商品列表:" + after_deleted_sku_ids);

			Assert.assertEquals(after_deleted_sku_ids, order_sku_ids, "批量删除订单商品后,订单" + order_id + "剩下的商品列表与预期不符");

		} catch (Exception e) {
			logger.error("批量删除订单里的商品遇到错误: ", e);
			Assert.fail("批量删除订单里的商品遇到错误: ", e);
		}
	}

	@Test
	public void orderSkuOpreationTestCase03() {
		ReporterCSS.title("测试点: 批量删除订单里的商品(过滤删除)");
		try {
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			List<String> order_sku_ids = details.stream().map(d -> d.getSku_id()).collect(Collectors.toList());

			String delete_sku_id = NumberUtil.roundNumberInList(order_sku_ids);

			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			orderSkuFilterParam.setStart_date_new(today);
			String torrrow = TimeUtil.calculateTime("yyyy-MM-dd 00:00", today, 1, Calendar.DATE);
			orderSkuFilterParam.setEnd_date_new(torrrow);
			orderSkuFilterParam.setSearch_text(delete_sku_id);

			BigDecimal task_id = orderService.orderDeleteSkus(orderSkuFilterParam);
			Assert.assertNotEquals(task_id, null, "批量删除订单商品,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			if (!result) {
				List<OrderDeleteSkuResultBean> orderDeleteSkuResults = orderService.getOrderDeleteSkuResults(task_id);
				Assert.assertNotEquals(orderDeleteSkuResults, null, "获取批量删除订商品结果失败");
			}

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			List<String> after_deleted_sku_ids = orderDetail.getDetails().stream().map(d -> d.getSku_id())
					.collect(Collectors.toList());

			order_sku_ids.remove(delete_sku_id);

			ReporterCSS.step("预期商品列表:" + order_sku_ids);
			ReporterCSS.step("实际商品列表:" + after_deleted_sku_ids);

			logger.info("预期商品列表:" + order_sku_ids);
			logger.info("实际商品列表:" + after_deleted_sku_ids);

			Assert.assertEquals(after_deleted_sku_ids, order_sku_ids, "批量删除订单商品后,订单" + order_id + "剩下的商品列表与预期不符");

		} catch (Exception e) {
			logger.error("批量删除订单里的商品遇到错误: ", e);
			Assert.fail("批量删除订单里的商品遇到错误: ", e);
		}
	}

	@Test
	public void orderSkuOpreationTestCase04() {
		ReporterCSS.title("测试点: 为订单做添加异常,为下面异常操作提供数据");
		try {
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			// 选取一个商品,添加商品异常
			Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

			abnormal_sku_id = detail.getSku_id();
			detail_id = detail.getDetail_id();

			BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
			int max = std_unit_quantity.intValue() + 10;
			// 最终值
			BigDecimal final_amount = NumberUtil.getRandomNumber(1, max, 2);

			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			orderExceptionArray.add(new OrderExceptionParam(final_amount, abnormal_sku_id, 1, 1, 2));

			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");
		} catch (Exception e) {
			logger.error("提单添加异常遇到错误: ", e);
			Assert.fail("提单添加异常遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderSkuOpreationTestCase04" })
	public void orderSkuOpreationTestCase05() {
		ReporterCSS.title("测试点: 批量替换商品,选择做了异常的商品,断言替换失败");
		try {
			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			orderSkuFilterParam.setStart_date_new(today);
			String torrrow = TimeUtil.calculateTime("yyyy-MM-dd 00:00", today, 1, Calendar.DATE);
			orderSkuFilterParam.setEnd_date_new(torrrow);
			orderSkuFilterParam.setSearch_text(order_id);

			List<OrderSkuFilterResultBean> orderSkuFilterResults = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkuFilterResults, null, "订单商品列表搜索过滤失败");

			List<String> sku_ids = new ArrayList<String>();
			String salemenu_id = null;
			OrderSkuFilterResultBean orderSkuFilterResult = null;
			for (OrderSkuFilterResultBean sku : orderSkuFilterResults) {
				sku_ids.add(sku.getSku_id());
				if (sku.getSku_id().equals(abnormal_sku_id)) {
					orderSkuFilterResult = sku;
				}
			}

			salemenu_id = orderSkuFilterResult.getSalemenu_id();

			List<String> salemenu_sku_ids = orderService.searchOrderChangeSku(salemenu_id, "a");
			Assert.assertNotEquals(salemenu_sku_ids, null, "订单替换商品,搜索过滤替换商品失败");

			Assert.assertEquals(sku_ids.containsAll(salemenu_sku_ids), false, "报价单里无可用商品替换");

			String change_sku_id = null;
			for (String sku_id : salemenu_sku_ids) {
				if (!sku_ids.contains(sku_id)) {
					change_sku_id = sku_id;
					break;
				}
			}

			List<OrderChangeSkuParam> orderChangeSkuParams = new ArrayList<OrderChangeSkuParam>();

			OrderChangeSkuParam orderChangeSkuParam = new OrderChangeSkuParam();
			orderChangeSkuParam.setChange_quantity(NumberUtil.getRandomNumber(4, 8, 1));
			orderChangeSkuParam.setNew_sku_id(change_sku_id);
			orderChangeSkuParam.setChange_type(0);
			orderChangeSkuParam.setSku_id(abnormal_sku_id);
			orderChangeSkuParam.setSalemenu_name(salemenu_id);

			List<OrderChangeSkuParam.OrderInfo> orderInfos = new ArrayList<OrderChangeSkuParam.OrderInfo>();
			OrderChangeSkuParam.OrderInfo orderInfo = orderChangeSkuParam.new OrderInfo();
			orderInfo.setOrder_id(order_id);
			orderInfo.setQuantity(orderSkuFilterResult.getQuantity());
			orderInfo.setResname(orderSkuFilterResult.getResname());

			orderInfos.add(orderInfo);
			orderChangeSkuParam.setOrder_infos(orderInfos);
			orderChangeSkuParams.add(orderChangeSkuParam);

			BigDecimal task_id = orderService.orderChangeSkus(orderChangeSkuParams);
			Assert.assertNotEquals(task_id, null, "订单替换商品,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败1");
			Assert.assertEquals(result, true, "批量替换商品,选择做了异常的商品,断言替换失败");

			List<OrderChangeSkuResultBean> orderChangeSkuResults = orderService.getOrderChangeSkuResults(task_id);
			Assert.assertNotEquals(orderChangeSkuResults, null, "获取订单切换商品的结果失败");

			OrderChangeSkuResultBean orderChangeSkuResult = orderChangeSkuResults.get(0);

			String expected_err_msg = "商品" + abnormal_sku_id + "在客服系统中操作了退货或异常，请在客服系统中进行解除操作，再修改商品信息";
			if (!expected_err_msg.equals(orderChangeSkuResult.getErr_msg())) {
				String msg = String.format("订单商品替换失败原因与预期不符,预期:%s,实际:%s", expected_err_msg,
						orderChangeSkuResult.getErr_msg());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true);
		} catch (Exception e) {
			logger.error("批量替换订单商品遇到错误: ", e);
			Assert.fail("批量替换订单商品遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderSkuOpreationTestCase04" })
	public void orderSkuOpreationTestCase06() {
		ReporterCSS.title("测试点: 批量删除商品,选择做了异常的商品,断言替换失败");
		try {
			List<OrderDeleteSkuParam> orderDeleteSkuParams = new ArrayList<OrderDeleteSkuParam>();
			OrderDeleteSkuParam orderDeleteSkuParam = null;

			orderDeleteSkuParam = new OrderDeleteSkuParam();
			orderDeleteSkuParam.setOrder_ids(Arrays.asList(order_id));
			orderDeleteSkuParam.setSku_id(abnormal_sku_id);
			orderDeleteSkuParams.add(orderDeleteSkuParam);

			BigDecimal task_id = orderService.orderDeleteSkus(orderDeleteSkuParams);
			Assert.assertNotEquals(task_id, null, "批量删除订单商品,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败1");
			Assert.assertEquals(result, true, "批量删除商品,选择做了异常的商品,断言替换失败");

			List<OrderDeleteSkuResultBean> orderDeleteSkuResults = orderService.getOrderDeleteSkuResults(task_id);
			Assert.assertNotEquals(orderDeleteSkuResults, null, "获取批量删除商品的结果失败");

			OrderDeleteSkuResultBean orderDeleteSkuResult = orderDeleteSkuResults.get(0);

			String expected_err_msg = String.format("商品('%s', %s)在客服系统中操作了退货或异常，请在客服系统中进行解除操作，再删除商品信息", abnormal_sku_id,
					detail_id);
			if (!expected_err_msg.equals(orderDeleteSkuResult.getErr_msg())) {
				ReporterCSS.warn("订单商品替换失败原因与预期不符");
				ReporterCSS.warn("预期:" + expected_err_msg);
				logger.warn(expected_err_msg);
				ReporterCSS.warn("实际:" + orderDeleteSkuResult.getErr_msg());
				logger.warn(orderDeleteSkuResult.getErr_msg());
				result = false;
			}
			Assert.assertEquals(result, true);

		} catch (Exception e) {
			logger.error("批量删除订单商品遇到错误: ", e);
			Assert.fail("批量删除订单商品遇到错误: ", e);
		}
	}

}
