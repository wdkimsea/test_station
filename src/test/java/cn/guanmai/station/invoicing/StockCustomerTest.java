package cn.guanmai.station.invoicing;

import cn.guanmai.bshop.bean.invoicing.BshopSpuStockBean;
import cn.guanmai.bshop.bean.invoicing.BshopStockCountBean;
import cn.guanmai.bshop.bean.invoicing.BshopWaitInStockSkuBean;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuInStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuOutStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuStockFilterParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopWatiInStockFilterParam;
import cn.guanmai.bshop.impl.BsInvoicingServiceImpl;
import cn.guanmai.bshop.service.BsInvoicingService;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockBean;
import cn.guanmai.station.bean.invoicing.CustomerStockCountBean;
import cn.guanmai.station.bean.invoicing.param.CustomerSpuStockLogFilterParam;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockLogBean;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.system.CustomizedBean;
import cn.guanmai.station.impl.invoicing.CustomerStockValueServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.CustomizedServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.invoicing.CustomerStockValueService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.CustomizedService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ConfigureUtil;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商户货值查询商城进销存
 * 
 * @author Administrator
 *
 */
public class StockCustomerTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockCustomerTest.class);
	private CustomizedService customizedService;
	private CustomerStockValueService customerStockService;
	private BsInvoicingService bshopInvoicingService;
	private OrderService orderService;
	private OrderTool orderTool;
	private WeightService weightService;
	private String bshop_user_name;
	private String address_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private BshopWatiInStockFilterParam filterParam;

	@BeforeClass
	public void initData() {
		try {
			String station_url = ConfigureUtil.getValueByKey("stationUrl");
			String bshop_url = ConfigureUtil.getValueByKey("bshopUrl");

			if (!station_url.split("station.")[1].split("guanmai.cn")[0]
					.equals(bshop_url.split("bshop.")[1].split("guanmai.cn")[0])) {
				Assert.fail("item.properties配置中的station链接和bshop链接不是同一环境,请检查配置");
			}

			Map<String, String> headers = getStationCookie();
			customizedService = new CustomizedServiceImpl(headers);
			CustomizedBean customized = customizedService.getCustomized();
			Assert.assertNotEquals(customized, null, "获取店铺自定义设置相关信息失败");

			boolean need_edit = false;
			if (customized.getTitle() == null || customized.getTitle().trim().equals("")) {
				customized.setTitle("自动化修改");
				need_edit = true;
			}

			if (customized.getPhone() == null || customized.getPhone().trim().equals("")) {
				customized.setPhone("12345");
				need_edit = true;
			}

			if (customized.getOrder_edit_time_limit() != null && customized.getOrder_edit_time_limit() == 0) {
				customized.setOrder_edit_time_limit(null);
			}

			if (customized.getIs_open_manage_stock() == 0) {
				customized.setIs_open_manage_stock(1);
				need_edit = true;
			}

			if (need_edit) {
				boolean result = customizedService.updateCustomized(customized);
				Assert.assertEquals(result, true, "店铺运营设置自定义修改失败");
			}

			customerStockService = new CustomerStockValueServiceImpl(headers);
			orderService = new OrderServiceImpl(headers);
			orderTool = new OrderTool(headers);
			weightService = new WeightServiceImpl(headers);

			String item_bshop_name = ConfigureUtil.getValueByKey("bshopUserName");

			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			CustomerBean customer = customerArray.stream().filter(c -> c.getUsername().equals(item_bshop_name))
					.findAny().orElse(null);
			Assert.assertNotEquals(customer, null, "item.properties配置文件中配置的bshop账号不属于配置的station站点客户");

			address_id = customer.getAddress_id();
			bshop_user_name = item_bshop_name;
			// 初始化数
			Map<String, String> bhsop_cookies = LoginUtil.loginBshop();
			Assert.assertNotEquals(bhsop_cookies, null, "Bshop登录失败");

			bshopInvoicingService = new BsInvoicingServiceImpl(bhsop_cookies);

		} catch (Exception e) {
			logger.error("初始化数据操作失败", e);
			Assert.fail("初始化数据操作失败", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		filterParam = new BshopWatiInStockFilterParam();
		filterParam.setAddress_id(address_id);
		try {
			filterParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			filterParam.setEnd_time(todayStr);
		} catch (ParseException e) {
			logger.error("日期计算出现错误", e);
			Assert.fail("日期计算出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest01() {
		try {
			ReporterCSS.log("测试点: 新建订单后查询商城进销存待入库商品");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 12);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
			Assert.assertEquals(orderDetail.getStatus(), status,
					"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			List<BshopWaitInStockSkuBean> orderWaitInStockSkuList = waitInStockSkuList.parallelStream()
					.filter(w -> w.getOrder_id().equals(order_id)).collect(Collectors.toList());

			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				String sku_id = detail.getSku_id();
				BshopWaitInStockSkuBean waitInStockSku = orderWaitInStockSkuList.parallelStream()
						.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (waitInStockSku == null) {
					msg = String.format("订单%s中的商品%s在商城的进销存待入库商品列表中没有找到", order_id, sku_id);
					result = false;
					logger.warn(msg);
					ReporterCSS.warn(msg);
				} else {
					if (detail.getStd_real_quantity().compareTo(waitInStockSku.getStd_quantity()) != 0) {
						msg = String.format("订单%s中的商品%s在商城的进销存待入库商品列表中显示的入库数量与预期不一致,预期:%s,实际:%s", order_id, sku_id,
								detail.getStd_real_quantity(), waitInStockSku.getStd_quantity());
						result = false;
						logger.warn(msg);
						ReporterCSS.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "商城进销存7天待入库查询到的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest02() {
		try {
			ReporterCSS.log("测试点: 订单称重后,查询商城进销存待入库商品");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 4);

			// 编辑订单为配送中或者已签收
			Integer status = 5;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			// 称重
			result = weightService.oneStepWeightOrder(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + "称重失败");

			status = 15;
			result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
			Assert.assertEquals(orderDetail.getStatus(), status,
					"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());

			filterParam.setSearch(order_id);

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			List<BshopWaitInStockSkuBean> orderWaitInStockSkuList = waitInStockSkuList.parallelStream()
					.filter(w -> w.getOrder_id().equals(order_id)).collect(Collectors.toList());

			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				String sku_id = detail.getSku_id();
				BshopWaitInStockSkuBean waitInStockSku = orderWaitInStockSkuList.parallelStream()
						.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (waitInStockSku == null) {
					msg = String.format("订单%s中的商品%s在商城的进销存待入库商品列表中没有找到", order_id, sku_id);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				} else {
					if (detail.getStd_real_quantity().compareTo(waitInStockSku.getStd_quantity()) != 0) {
						msg = String.format("订单%s中的商品%s在商城的进销存待入库商品列表中显示的入库数量与预期不一致,预期:%s,实际:%s", order_id, sku_id,
								detail.getStd_real_quantity(), waitInStockSku.getStd_quantity());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "商城进销存7天待入库查询到的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest03() {
		try {
			ReporterCSS.log("测试点: 商城进销存待入库商品以订单状态(配送)过滤");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			Integer status = 10;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setStart_date(todayStr);
			orderFilterParam.setEnd_date(todayStr);
			orderFilterParam.setStatus(status);
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_text(address_id);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(20);

			List<OrderBean> orderList = new ArrayList<OrderBean>();
			boolean more = true;
			while (more) {
				List<OrderBean> tempList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempList, null, "订单列表搜索过滤失败");
				orderList.addAll(tempList);
				if (tempList.size() < 20) {
					more = false;
				}
				orderFilterParam.setOffset(orderFilterParam.getOffset() + orderFilterParam.getLimit());
			}

			filterParam.setOrder_status(status);
			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			String msg = null;
			for (OrderBean order : orderList) {
				List<OrderBean.Detail> details = order.getDetail();
				for (OrderBean.Detail detail : details) {
					BshopWaitInStockSkuBean waitInStockSku = waitInStockSkuList.parallelStream().filter(
							s -> s.getOrder_id().equals(order.getId()) && detail.getSku_id().equals(s.getSku_id()))
							.findAny().orElse(null);
					if (waitInStockSku == null) {
						msg = String.format("商城进销存以订单状态过滤,订单%s中的商品%s在显示列表中没有找到", order_id, detail.getSku_id());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "商城进销存以订单状态过滤,搜索结果与预期不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest04() {
		try {
			ReporterCSS.log("测试点: 商城进销存待入库商品以订单状态(签收)过滤");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setStart_date(todayStr);
			orderFilterParam.setEnd_date(todayStr);
			orderFilterParam.setStatus(status);
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_text(address_id);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(20);

			List<OrderBean> orderList = new ArrayList<OrderBean>();
			boolean more = true;
			while (more) {
				List<OrderBean> tempList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempList, null, "订单列表搜索过滤失败");
				orderList.addAll(tempList);
				if (tempList.size() < 20) {
					more = false;
				}
				orderFilterParam.setOffset(orderFilterParam.getOffset() + orderFilterParam.getLimit());
			}

			filterParam.setOrder_status(status);
			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			String msg = null;
			for (OrderBean order : orderList) {
				List<OrderBean.Detail> details = order.getDetail();
				for (OrderBean.Detail detail : details) {
					BshopWaitInStockSkuBean waitInStockSku = waitInStockSkuList.parallelStream().filter(
							s -> s.getOrder_id().equals(order.getId()) && detail.getSku_id().equals(s.getSku_id()))
							.findAny().orElse(null);
					if (waitInStockSku == null) {
						msg = String.format("商城进销存以订单状态过滤,订单%s中的商品%s在显示列表中没有找到", order_id, detail.getSku_id());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "商城进销存以订单状态过滤,搜索结果与预期不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest05() {
		try {
			ReporterCSS.log("测试点: 商城进销存待入库商品以收货时间过滤");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String start_time = orderCycle.getCycle_start_time().substring(0, 10);
			String end_time = orderCycle.getCycle_end_time().substring(0, 10);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setReceive_start_date(start_time);
			orderFilterParam.setReceive_end_date(end_time);
			orderFilterParam.setStatus(status);
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setSearch_text(address_id);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(20);

			List<OrderBean> orderList = new ArrayList<OrderBean>();
			boolean more = true;
			while (more) {
				List<OrderBean> tempList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempList, null, "订单列表搜索过滤失败");
				orderList.addAll(tempList);
				if (tempList.size() < 20) {
					more = false;
				}
				orderFilterParam.setOffset(orderFilterParam.getOffset() + orderFilterParam.getLimit());
			}

			filterParam.setStart_time(start_time);
			filterParam.setEnd_time(end_time);
			filterParam.setQuery_type(2);
			filterParam.setOrder_status(status);
			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			String msg = null;
			for (OrderBean order : orderList) {
				List<OrderBean.Detail> details = order.getDetail();
				for (OrderBean.Detail detail : details) {
					BshopWaitInStockSkuBean waitInStockSku = waitInStockSkuList.parallelStream().filter(
							s -> s.getOrder_id().equals(order.getId()) && detail.getSku_id().equals(s.getSku_id()))
							.findAny().orElse(null);
					if (waitInStockSku == null) {
						msg = String.format("商城进销存以收货时间过滤待入库商品,订单%s中的商品%s在显示列表中没有找到", order_id, detail.getSku_id());
						result = false;
						ReporterCSS.warn(msg);
						logger.warn(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "商城进销存以收货时间过滤待入库商品,搜索结果与预期不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest06() {
		try {
			ReporterCSS.log("测试点: 商城进销存待入库商品以商品名称搜索过滤");
			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			OrderDetailBean.Detail detail = orderDetail.getDetails().get(0);
			String sku_name = detail.getSku_name();
			String sku_id = detail.getSku_id();

			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			orderSkuFilterParam.setStart_date(todayStr);
			orderSkuFilterParam.setEnd_date(todayStr);
			orderSkuFilterParam.setSearch_text(sku_id);
			orderSkuFilterParam.setStatus(status);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(20);

			List<OrderSkuFilterResultBean> orderSkuFilterResultList = new ArrayList<OrderSkuFilterResultBean>();

			boolean more = true;
			while (more) {
				List<OrderSkuFilterResultBean> tempList = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempList, null, "订单列表按商品查看过滤失败");
				orderSkuFilterResultList.addAll(tempList);
				if (tempList.size() < 20) {
					more = false;
				}
				orderSkuFilterParam.setOffset(orderSkuFilterParam.getOffset() + orderSkuFilterParam.getLimit());
			}

			List<OrderSkuFilterResultBean> customer_order_skus = orderSkuFilterResultList.parallelStream()
					.filter(s -> s.getAddress_id().equals(address_id)).collect(Collectors.toList());

			filterParam.setOrder_status(status);
			filterParam.setSearch(sku_name);

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			String msg = null;
			for (OrderSkuFilterResultBean orderSku : customer_order_skus) {
				BshopWaitInStockSkuBean waitInStockSku = waitInStockSkuList.parallelStream()
						.filter(s -> s.getOrder_id().equals(orderSku.getOrder_id())
								&& s.getSku_id().equals(orderSku.getSku_id()))
						.findAny().orElse(null);
				if (waitInStockSku == null) {
					msg = String.format("商城进销存以商品名称过滤待入库商品,订单%s中的商品%s在显示列表中没有找到", orderSku.getOrder_id(),
							orderSku.getSku_id());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

			}
			Assert.assertEquals(result, true, "商城进销存待入库商品以商品名称过滤,搜索结果与预期不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest07() {
		try {
			ReporterCSS.log("测试点: 商城进销存,待入库商品进行入库操作");

			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);

			List<BshopSpuStockBean> beforeSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(beforeSpuStockList, null, "商城进销存,获取商品库存列表信息失败");

			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			BshopSpuInStockParam spuInStockParam = new BshopSpuInStockParam();
			spuInStockParam.setAddress_id(address_id);
			spuInStockParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			spuInStockParam.setEnd_time(todayStr);
			spuInStockParam.setAll(1);

			List<BshopSpuInStockParam.BatchInStock> batchInStockList = new ArrayList<>();
			BshopSpuInStockParam.BatchInStock batchInStock = null;
			BigDecimal std_quantity = null;
			BigDecimal std_unit_price = null;
			for (BshopWaitInStockSkuBean waitInStockSku : waitInStockSkuList) {
				batchInStock = spuInStockParam.new BatchInStock();
				batchInStock.setOrder_id(waitInStockSku.getOrder_id());
				batchInStock.setSku_id(waitInStockSku.getSku_id());
				std_quantity = waitInStockSku.getStd_quantity().add(new BigDecimal("0.5"));
				batchInStock.setStd_quantity(std_quantity);
				std_unit_price = waitInStockSku.getStd_unit_price().add(new BigDecimal("0.5"));
				batchInStock.setStd_unit_price(std_unit_price);
				batchInStockList.add(batchInStock);
			}

			spuInStockParam.setBatch_list(batchInStockList);

			result = bshopInvoicingService.createSpuInStock(spuInStockParam);
			Assert.assertEquals(result, true, "商城进销存,商品入库操作失败");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest08() {
		try {
			ReporterCSS.log("测试点: 商城进销存,待入库商品进行入库后检测库存");

			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);

			List<BshopSpuStockBean> beforeSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(beforeSpuStockList, null, "商城进销存,获取商品库存列表信息失败");

			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			BshopSpuInStockParam spuInStockParam = new BshopSpuInStockParam();
			spuInStockParam.setAddress_id(address_id);
			spuInStockParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			spuInStockParam.setEnd_time(todayStr);
			spuInStockParam.setAll(1);

			List<BshopSpuInStockParam.BatchInStock> batchInStockList = new ArrayList<>();
			BshopSpuInStockParam.BatchInStock batchInStock = null;
			BigDecimal std_quantity = null;
			BigDecimal std_unit_price = null;
			List<BshopSpuStockBean> expectedSpuStockList = new ArrayList<BshopSpuStockBean>();
			for (BshopWaitInStockSkuBean waitInStockSku : waitInStockSkuList) {
				String spu_id = waitInStockSku.getSpu_id();
				batchInStock = spuInStockParam.new BatchInStock();
				batchInStock.setOrder_id(waitInStockSku.getOrder_id());
				batchInStock.setSku_id(waitInStockSku.getSku_id());
				std_quantity = waitInStockSku.getStd_quantity().add(new BigDecimal("0.5"));
				batchInStock.setStd_quantity(std_quantity);
				std_unit_price = waitInStockSku.getStd_unit_price().add(new BigDecimal("0.5"));
				batchInStock.setStd_unit_price(std_unit_price);
				batchInStockList.add(batchInStock);

				BshopSpuStockBean tempSpuStock = beforeSpuStockList.parallelStream()
						.filter(s -> s.getSpu_id().contentEquals(spu_id)).findAny().orElse(null);
				if (tempSpuStock == null) {
					tempSpuStock = new BshopSpuStockBean();
					tempSpuStock.setSpu_id(spu_id);
					tempSpuStock.setStock(std_quantity);
					tempSpuStock.setStock_value(std_quantity.multiply(std_unit_price));
				} else {
					BigDecimal new_std_quantity = tempSpuStock.getStock().add(std_quantity);
					BigDecimal new_stock_value = tempSpuStock.getStock_value()
							.add(std_quantity.multiply(std_unit_price)).setScale(2, BigDecimal.ROUND_HALF_UP);
					tempSpuStock.setStock(new_std_quantity);
					tempSpuStock.setStock_value(new_stock_value);
				}
				expectedSpuStockList.add(tempSpuStock);
			}

			spuInStockParam.setBatch_list(batchInStockList);

			result = bshopInvoicingService.createSpuInStock(spuInStockParam);
			Assert.assertEquals(result, true, "商城进销存,商品入库操作失败");

			// 开始验证信息
			bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			List<BshopSpuStockBean> actualSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(actualSpuStockList, null, "商城进销存,获取商品库存列表信息失败");

			String msg = null;
			for (BshopSpuStockBean spuStock : expectedSpuStockList) {
				BshopSpuStockBean actualSpuStock = actualSpuStockList.parallelStream()
						.filter(s -> s.getSpu_id().equals(spuStock.getSpu_id())).findAny().orElse(null);
				if (actualSpuStock == null) {
					msg = String.format("商城进销存,预期存在商品%s的库存,实际不存在", spuStock.getSpu_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (spuStock.getStock().compareTo(actualSpuStock.getStock()) != 0) {
					msg = String.format("商城进销存,预期商品%s的库存为%s,实际为%s", spuStock.getSpu_id(), spuStock.getStock(),
							spuStock.getStock());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!NumberUtil.roundCompare(spuStock.getStock_value(), actualSpuStock.getStock_value(), 0.01)) {
					msg = String.format("商城进销存,预期商品%s的货值为%s,实际为%s", spuStock.getSpu_id(), spuStock.getStock_value(),
							actualSpuStock.getStock_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商城进销存,商品提交入库后,预期库存信息与实际不一致");
		} catch (Exception e) {
			logger.error("查询商城进销存待入库商品出现错误", e);
			Assert.fail("查询商城进销存待入库商品出现错误", e);
		}
	}

	@Test
	public void bshopSpuStockTest09() {
		ReporterCSS.title("测试点: 商城进销存,库存列表搜索过滤");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);

			List<BshopSpuStockBean> spuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(spuStockList, null, "商城进销存,获取商品库存列表信息失败");

			if (spuStockList.size() > 0) {
				String spu_name = spuStockList.get(0).getSpu_name();
				bshopSpuStockFilterParam.setSearch(spu_name);
				spuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
				Assert.assertNotEquals(spuStockList, null, "商城进销存,获取商品库存列表信息失败");

				List<BshopSpuStockBean> tempSpuStockList = spuStockList.parallelStream()
						.filter(s -> !s.getSpu_name().contentEquals(spu_name)).collect(Collectors.toList());

				Assert.assertEquals(tempSpuStockList.size() == 0, true, "商城进销存,以SPU名称过滤列表信息,搜索过滤出来的信息和搜索过滤条件不匹配");
			}
		} catch (Exception e) {
			logger.error("商城进销存,库存列表搜索过滤出现错误", e);
			Assert.fail("商城进销存,库存列表搜索过滤出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void bshopSpuStockTest10() {
		ReporterCSS.title("测试点: 商城进销存,验证SPU按照名称排序");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(2);

			List<BshopSpuStockBean> positiveSpuStockList = bshopInvoicingService
					.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(positiveSpuStockList, null, "商城进销存,按照SPU名称正序获取商品库存列表信息失败");

			bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(3);

			List<BshopSpuStockBean> reverseSpuStockList = bshopInvoicingService
					.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(reverseSpuStockList, null, "商城进销存,按照SPU名称反序获取商品库存列表信息失败");

			Assert.assertEquals(positiveSpuStockList.size(), reverseSpuStockList.size(),
					"商城进销存,按照商品名正序查询和反序查询获取到的SPU数量不一致");

			int list_size = positiveSpuStockList.size();
			String positive_spu_name = null;
			String reverse_spu_name = null;
			String msg = null;
			boolean result = true;
			for (int i = 0; i < list_size; i++) {
				positive_spu_name = positiveSpuStockList.get(i).getSpu_name();
				reverse_spu_name = reverseSpuStockList.get(list_size - i - 1).getSpu_name();
				logger.info("正序SPU名称:  " + positive_spu_name);
				logger.info("反序SPU名称:  " + reverse_spu_name);
				if (!positive_spu_name.equals(reverse_spu_name)) {
					msg = String.format("商城进销存,SPU列表正序第%s的SPU名称和反序的第%s的SPU名称不一致,预期:%s,实际:%s", i, list_size - i - 1,
							positive_spu_name, reverse_spu_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商城进销存,SPU名称正序排列和SPU名称反序排列没有头尾一一对应");
		} catch (Exception e) {
			logger.error("商城进销存,库存列表搜索过滤出现错误", e);
			Assert.fail("商城进销存,库存列表搜索过滤出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void bshopSpuStockTest11() {
		ReporterCSS.title("测试点: 商城进销存,验证SPU按照库存排序");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(4);

			List<BshopSpuStockBean> positiveSpuStockList = bshopInvoicingService
					.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(positiveSpuStockList, null, "商城进销存,按照SPU库存正序获取商品库存列表信息失败");

			bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(5);

			List<BshopSpuStockBean> reverseSpuStockList = bshopInvoicingService
					.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(reverseSpuStockList, null, "商城进销存,按照SPU库存反序获取商品库存列表信息失败");

			Assert.assertEquals(positiveSpuStockList.size(), reverseSpuStockList.size(),
					"商城进销存,按照商品库存正序查询和反序查询获取到的SPU数量不一致");

			int list_size = positiveSpuStockList.size();
			String positive_spu_id = null;
			String reverse_spu_id = null;
			String msg = null;
			boolean result = true;
			for (int i = 0; i < list_size; i++) {
				positive_spu_id = positiveSpuStockList.get(i).getSpu_id();
				reverse_spu_id = reverseSpuStockList.get(list_size - i - 1).getSpu_id();
				logger.info("正序SPU ID:  " + positive_spu_id);
				logger.info("反序SPU ID:  " + reverse_spu_id);
				if (!positive_spu_id.equals(reverse_spu_id)) {
					msg = String.format("商城进销存,SPU库存列表正序第%s的SPU ID和反序的第%s的SPU ID不一致,预期:%s,实际:%s", i, list_size - i - 1,
							positive_spu_id, reverse_spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商城进销存,SPU库存正序排列和SPU库存反序排列没有头尾一一对应");
		} catch (Exception e) {
			logger.error("商城进销存,库存列表搜索过滤出现错误", e);
			Assert.fail("商城进销存,库存列表搜索过滤出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void bshopSpuStockTest12() {
		ReporterCSS.title("测试点: 商城进销存,验证SPU总货值");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(1);

			List<BshopSpuStockBean> spuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(spuStockList, null, "商城进销存,按照SPU库存正序获取商品库存列表信息失败");

			BshopStockCountBean bshopStockCount = bshopInvoicingService.getStockCount(address_id, "");
			Assert.assertNotEquals(bshopStockCount, null, "商城进销存,获取总库存统计失败");

			BigDecimal spuStockValueCount = new BigDecimal("0");
			for (BshopSpuStockBean bshopSpuStock : spuStockList) {
				spuStockValueCount = spuStockValueCount.add(bshopSpuStock.getStock_value());
			}

			String msg = null;
			boolean result = true;
			if (!NumberUtil.roundCompare(spuStockValueCount, bshopStockCount.getTotal_stock_value(), 0.01)) {
				msg = String.format("商城进销存,统计的总货值与预期的不一致,预期:%s,实际:%s", spuStockValueCount,
						bshopStockCount.getTotal_stock_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (bshopStockCount.getSpu_count() != spuStockList.size()) {
				msg = String.format("商城进销存,统计的库存商品数与预期的不一致,预期:%s,实际:%s", spuStockList.size(),
						bshopStockCount.getSpu_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商城进销存,统计的库存总货值信息与预期不一致");
		} catch (Exception e) {
			logger.error("商城进销存,库存列表搜索过滤出现错误", e);
			Assert.fail("商城进销存,库存列表搜索过滤出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void customerStockValueTest01() {
		ReporterCSS.title("测试点: 进销存商户货值统计");
		try {
			CustomerStockCountBean customerStockCount = customerStockService.getCustomerStockValueCount(address_id);
			Assert.assertNotEquals(customerStockCount, null, "进销存商户货值统计失败");

			BshopStockCountBean bshopStockCount = bshopInvoicingService.getStockCount(address_id, "");
			Assert.assertNotEquals(bshopStockCount, null, "商城进销存,获取总库存统计失败");

			Assert.assertEquals(customerStockCount.getTotal_stock_value(), bshopStockCount.getTotal_stock_value(),
					"ST进销存商户货值统计的商品" + address_id + "的总货值与BSHOP统计的商品总货值不一致");
		} catch (Exception e) {
			logger.error("进销存商户货值统计出现错误", e);
			Assert.fail("进销存商户货值统计出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void customerStockValueTest02() {
		ReporterCSS.title("测试点: 进销存商户货值明细");
		try {
			List<CustomerSpuStockBean> customerSpuStockList = customerStockService.getCustomerSpuStockList(address_id);
			Assert.assertNotEquals(customerSpuStockList, null, "进销存-商户货值成本,获取指定商户库存列表信息失败");

			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);
			bshopSpuStockFilterParam.setSort(1);

			List<BshopSpuStockBean> bshopSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(bshopSpuStockList, null, "商城进销存,按照SPU库存正序获取商品库存列表信息失败");

			String msg = null;
			boolean result = true;
			for (BshopSpuStockBean bshopSpuStock : bshopSpuStockList) {
				String spu_id = bshopSpuStock.getSpu_id();
				CustomerSpuStockBean customerSpuStock = customerSpuStockList.parallelStream()
						.filter(s -> s.getSpu_id().equals(spu_id)).findAny().orElse(null);
				if (customerSpuStock == null) {
					msg = String.format("进销存-商户货值,商户%s的货值列表与商城的进销存货值成本列表对比,少了商品%s", address_id, spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (customerSpuStock.getStock_value().compareTo(bshopSpuStock.getStock_value()) != 0) {
					msg = String.format("进销存-商户货值,商户%s的货值列表中的商品%s与商城的进销存货值成本列表对比,货值不一致,商城:%s,ST:%s", address_id, spu_id,
							bshopSpuStock.getStock_value(), customerSpuStock.getStock_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerSpuStock.getStock().compareTo(bshopSpuStock.getStock()) != 0) {
					msg = String.format("进销存-商户货值,商户%s的货值列表中的商品%s与商城的进销存货值成本列表对比,库存数不一致,商城:%s,ST:%s", address_id,
							spu_id, bshopSpuStock.getStock(), customerSpuStock.getStock());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "进销存-商户货值,商户" + address_id + "货值列表和商城进销存列表显示的信息不一致");
		} catch (Exception e) {
			logger.error("进销存商户货值统计出现错误", e);
			Assert.fail("进销存商户货值统计出现错误", e);
		}
	}

	@Test(dependsOnMethods = { "bshopSpuStockTest07" })
	public void customerStockValueTest03() {
		ReporterCSS.title("测试点: 进销存商户货值明细变动记录");
		try {
			List<CustomerSpuStockBean> customerSpuStockList = customerStockService.getCustomerSpuStockList(address_id);
			Assert.assertNotEquals(customerSpuStockList, null, "进销存-商户货值成本,获取指定商户库存列表信息失败");

			String spu_id = customerSpuStockList.get(0).getSpu_id();
			CustomerSpuStockLogFilterParam filterParam = new CustomerSpuStockLogFilterParam();
			filterParam.setStart_time(todayStr);
			filterParam.setEnd_time(todayStr);
			filterParam.setLimit(20);
			filterParam.setSpu_id(spu_id);
			filterParam.setAddress_id(address_id);

			List<CustomerSpuStockLogBean> customerSpuStockLogList = customerStockService
					.searchCustomerSpuStockLog(filterParam);
			Assert.assertNotEquals(customerSpuStockLogList, null, "进销存商户货值明细变动记录获取失败获取失败");
		} catch (Exception e) {
			logger.error("获取进销存商户货值明细变动出现错误", e);
			Assert.fail("获取进销存商户货值明细变动出现错误", e);
		}
	}

	@Test
	public void customerStockValueTest04() {
		ReporterCSS.title("测试点: 进销存商户货值明细记录查询");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);

			List<BshopSpuStockBean> beforeSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(beforeSpuStockList, null, "商城进销存,获取商品库存列表信息失败");

			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			BshopSpuInStockParam spuInStockParam = new BshopSpuInStockParam();
			spuInStockParam.setAddress_id(address_id);
			spuInStockParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			spuInStockParam.setEnd_time(todayStr);
			spuInStockParam.setAll(1);

			List<BshopSpuInStockParam.BatchInStock> batchInStockList = new ArrayList<>();

			BshopSpuOutStockParam bshopSpuOutStockParam = new BshopSpuOutStockParam();
			List<BshopSpuOutStockParam.OutStockDetail> outStockDetails = new ArrayList<>();

			BshopSpuInStockParam.BatchInStock batchInStock = null;
			BshopSpuOutStockParam.OutStockDetail outStockDetail = null;
			BigDecimal std_quantity = null;
			BigDecimal std_unit_price = null;
			String spu_id = null;
			for (BshopWaitInStockSkuBean waitInStockSku : waitInStockSkuList) {
				batchInStock = spuInStockParam.new BatchInStock();
				batchInStock.setOrder_id(waitInStockSku.getOrder_id());
				batchInStock.setSku_id(waitInStockSku.getSku_id());
				std_quantity = waitInStockSku.getStd_quantity();
				batchInStock.setStd_quantity(std_quantity);
				std_unit_price = waitInStockSku.getStd_unit_price();
				batchInStock.setStd_unit_price(std_unit_price);
				batchInStockList.add(batchInStock);

				// 出库参数
				outStockDetail = bshopSpuOutStockParam.new OutStockDetail();
				spu_id = waitInStockSku.getSpu_id();
				outStockDetail.setSpu_id(spu_id);
				outStockDetail.setStd_quantity(std_quantity);
				outStockDetails.add(outStockDetail);
			}

			spuInStockParam.setBatch_list(batchInStockList);

			result = bshopInvoicingService.createSpuInStock(spuInStockParam);
			Assert.assertEquals(result, true, "商城进销存,商品入库操作失败");

			bshopSpuOutStockParam.setAddress_id(address_id);
			bshopSpuOutStockParam.setBatch_list(outStockDetails);
			List<BshopSpuStockBean> addressSpuStockList = bshopInvoicingService.getAddressSpuStockList(address_id);
			Assert.assertNotEquals(addressSpuStockList, null, "商城进销存-出库页面-获取商品库存列表数据失败");

			result = bshopInvoicingService.createSpuStockOutput(bshopSpuOutStockParam);
			Assert.assertEquals(result, true, "商城进销存-进行出库操作失败");

			CustomerSpuStockLogFilterParam filterParam = new CustomerSpuStockLogFilterParam();
			filterParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			filterParam.setEnd_time(todayStr);
			filterParam.setAddress_id(address_id);
			filterParam.setOp_type(1);
			filterParam.setSpu_id(spu_id);
			filterParam.setOffset(0);
			filterParam.setLimit(10);

			List<CustomerSpuStockLogBean> customerSpuStockLogs = customerStockService
					.searchCustomerSpuStockLog(filterParam);
			Assert.assertNotEquals(customerSpuStockLogs, null, "进销存-商户货值查询-查询某个商品的库存变动历史记录失败");

			List<CustomerSpuStockLogBean> targetCustomerSpuStockLogs = customerSpuStockLogs.parallelStream()
					.filter(l -> l.getOp_type() != 1).collect(Collectors.toList());

			Assert.assertEquals(targetCustomerSpuStockLogs.size(), 0, "进销存-商户货值查询-过滤商品的入库变动历史记录,把其他类型的变动历史记录也过滤出来了");

			// 过滤出库记录
			filterParam.setOp_type(2);
			customerSpuStockLogs = customerStockService.searchCustomerSpuStockLog(filterParam);
			Assert.assertNotEquals(customerSpuStockLogs, null, "进销存-商户货值查询-查询某个商品的库存变动历史记录失败");

			targetCustomerSpuStockLogs = customerSpuStockLogs.parallelStream().filter(l -> l.getOp_type() != 2)
					.collect(Collectors.toList());

			Assert.assertEquals(targetCustomerSpuStockLogs.size(), 0, "进销存-商户货值查询-过滤商品的出库变动历史记录,把其他类型的变动历史记录也过滤出来了");
		} catch (Exception e) {
			logger.error("进销存商户货值统计出现错误", e);
			Assert.fail("进销存商户货值统计出现错误", e);
		}
	}

	@Test
	public void exportCustomerStockValueTest01() {
		ReporterCSS.title("测试点: 进销存-商户货值导出");
		try {
			String file_path = customerStockService.exportCustomerSpuStocks("");
			Assert.assertNotEquals(file_path, null, "进销存-商户货值导出失败");
		} catch (Exception e) {
			logger.error("进销存-商户货值导出遇到错误", e);
			Assert.fail("进销存-商户货值导出遇到错误", e);
		}
	}

	@Test
	public void exportCustomerStockLogTest01() {
		ReporterCSS.title("测试点: 进销存-商户货值-导出某个商户货值的库存变动历史记录");
		try {
			BshopSpuStockFilterParam bshopSpuStockFilterParam = new BshopSpuStockFilterParam();
			bshopSpuStockFilterParam.setAddress_id(address_id);

			List<BshopSpuStockBean> beforeSpuStockList = bshopInvoicingService.searchSpuStock(bshopSpuStockFilterParam);
			Assert.assertNotEquals(beforeSpuStockList, null, "商城进销存,获取商品库存列表信息失败");

			String order_id = orderTool.oneStepCreateOrder(bshop_user_name, 6);

			// 编辑订单为配送中或者已签收
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<BshopWaitInStockSkuBean> waitInStockSkuList = bshopInvoicingService.searchWaitInStockSku(filterParam);
			Assert.assertNotEquals(waitInStockSkuList, null, "BShop进销存搜索过滤近七天待入库数据失败");

			BshopSpuInStockParam spuInStockParam = new BshopSpuInStockParam();
			spuInStockParam.setAddress_id(address_id);
			spuInStockParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			spuInStockParam.setEnd_time(todayStr);
			spuInStockParam.setAll(1);

			List<BshopSpuInStockParam.BatchInStock> batchInStockList = new ArrayList<>();

			BshopSpuInStockParam.BatchInStock batchInStock = null;
			BigDecimal std_quantity = null;
			BigDecimal std_unit_price = null;
			String spu_id = null;
			for (BshopWaitInStockSkuBean waitInStockSku : waitInStockSkuList) {
				batchInStock = spuInStockParam.new BatchInStock();
				batchInStock.setOrder_id(waitInStockSku.getOrder_id());
				batchInStock.setSku_id(waitInStockSku.getSku_id());
				std_quantity = waitInStockSku.getStd_quantity();
				batchInStock.setStd_quantity(std_quantity);
				std_unit_price = waitInStockSku.getStd_unit_price();
				batchInStock.setStd_unit_price(std_unit_price);
				batchInStockList.add(batchInStock);

				spu_id = waitInStockSku.getSpu_id();

			}

			spuInStockParam.setBatch_list(batchInStockList);

			result = bshopInvoicingService.createSpuInStock(spuInStockParam);
			Assert.assertEquals(result, true, "商城进销存,商品入库操作失败");

			CustomerSpuStockLogFilterParam filterParam = new CustomerSpuStockLogFilterParam();
			filterParam.setStart_time(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -7, Calendar.DATE));
			filterParam.setEnd_time(todayStr);
			filterParam.setAddress_id(address_id);
			filterParam.setOp_type(-1);
			filterParam.setSpu_id(spu_id);
			filterParam.setExport(1);

			String file_path = customerStockService.exportCustomerSpuStockLog(filterParam);
			Assert.assertNotEquals(file_path, null, "进销存-商户货值-导出某个SPU商品的库存变动历史记录失败");
		} catch (Exception e) {
			logger.error("进销存-商户货值库存历史记录导出遇到错误", e);
			Assert.fail("进销存-商户货值库存历史记录导出遇到错误", e);
		}
	}
}