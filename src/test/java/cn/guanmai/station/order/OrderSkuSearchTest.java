package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.OrderPriceSyncToSkuResultBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.order.param.WeightRemarkFilterParam;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Apr 4, 2019 11:41:17 AM 
* @des 订单列表-按商品查看
* @version 1.0 
*/
public class OrderSkuSearchTest extends LoginStation {
	private static Logger logger = LoggerFactory.getLogger(OrderSkuSearchTest.class);
	private OrderService orderService;
	private ServiceTimeService serviceTimeService;
	private CategoryService categoryService;
	private RouteService routeService;
	private AsyncService asyncService;
	private OrderTool orderTool;

	private OrderDetailBean orderDetail;
	private OrderSkuFilterParam orderSkuFilterParam;

	private String start_date_new;
	private String end_date_new;
	private String todayStr;
	private String order_id;
	private String address_id;
	private String address_name;

	private int limit = 50;

	@BeforeClass
	public void initData() {
		todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		routeService = new RouteServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		try {
			start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);
			order_id = orderTool.oneStepCreateOrder(10);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			// 获取商户ID
			String address_num = orderDetail.getCustomer().getAddress_id();

			address_id = "S" + String.format("%06d", Integer.valueOf(address_num));

			// 获取商户名称
			address_name = orderDetail.getCustomer().getExtender().getResname();
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(1);
		orderSkuFilterParam.setStart_date_new(start_date_new);
		orderSkuFilterParam.setEnd_date_new(end_date_new);
		orderSkuFilterParam.setLimit(limit);
		orderSkuFilterParam.setOffset(0);
	}

	@Test
	public void searchOrderSkuTestCase01() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-以订单号过滤");
		// 因为下单商品最多10个,所以这里查询就不需要翻页了
		orderSkuFilterParam.setSearch_text(order_id);
		try {
			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-过滤搜索失败");

			OrderSkuFilterResultBean orderSku = null;
			boolean result = true;
			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				orderSku = orderSkus.stream().filter(s -> s.getSku_id().equals(detail.getSku_id())).findAny()
						.orElse(null);
				if (orderSku == null) {
					msg = String.format("订单%s中的商品%s,按商品查看没有搜索到", order_id, detail.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (orderSku.getQuantity().compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s中的商品%s,订单详细中的下单数和商品列表下的单数不一致,订单详细:%s,商品列表:%s", order_id,
							detail.getSku_id(), detail.getQuantity(), orderSku.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}

				BigDecimal detail_sale_money = detail.getSale_money().divide(new BigDecimal("100")).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				BigDecimal sku_sale_money = orderSku.getSale_money().setScale(2, BigDecimal.ROUND_HALF_UP);
				if (detail_sale_money.compareTo(sku_sale_money) != 0) {
					msg = String.format("订单%s中的商品%s,订单详细中的下单金额和商品列表下单金额的不一致,订单详细:%s,商品列表:%s", order_id,
							detail.getSku_id(), detail_sale_money, sku_sale_money);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			}
			Assert.assertEquals(result, true, "订单 " + order_id + "详细信息和按商品查看到的信息不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase02() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按商品信息查询");

		// 随机选取一个商品
		int index = new Random().nextInt(orderDetail.getDetails().size());
		String order_sku_id = orderDetail.getDetails().get(index).getSku_id();
		orderSkuFilterParam.setSearch_text(order_sku_id);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getOrder_id().equals(order_id)).collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 1,
					"订单 " + order_id + " 中的商品 " + order_sku_id + " 在订单列表-按商品查看页面应该只有一个");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase03() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按商品信息查询");

		// 随机选取一个商品
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);

		orderSkuFilterParam.setSearch_text(order_sku.getSku_name());
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();

		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getOrder_id().equals(order_id) && s.getSku_id().equals(order_sku.getSku_id()))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 1,
					"订单 " + order_id + " 中的商品 " + order_sku.getSku_id() + " 在订单列表-按商品查看页面应该只有一个");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase04() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按商户ID信息查询");
		orderSkuFilterParam.setSearch_text(address_id);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			// 从搜索结果中过滤中订单的商品总数
			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getAddress_id().equals(address_id) && s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), orderDetail.getDetails().size(),
					"订单列表-按商品查看-按商户信息过滤,过滤出的订单" + order_id + "商品总数和预期的不一致");

			// 从搜搜结果中过滤不符合条件的商品
			targetOrderSkus = orderSkus.stream().filter(s -> !s.getAddress_id().equals(address_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 0,
					"订单列表-按商品查看-按商户信息过滤,过滤出了不符合条件的商品列表" + JsonUtil.objectToStr(targetOrderSkus));
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase05() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按商户名称信息查询");
		orderSkuFilterParam.setSearch_text(address_name);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();

		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			// 从搜索结果中过滤中订单的商品总数
			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getAddress_id().equals(address_id) && s.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), orderDetail.getDetails().size(),
					"订单列表-按商品查看-按商户信息过滤,过滤出的订单" + order_id + "商品总数和预期的不一致");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase06() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按[商品ID,商户ID]组合方式查询");

		// 随机选取一个商品
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String sku_id = order_sku.getSku_id();

		orderSkuFilterParam.setSearch_text(sku_id + "," + address_id);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();

		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			// 从搜索结果中过滤中订单的商品总数
			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getAddress_id().equals(address_id) && s.getOrder_id().equals(order_id)
							&& s.getSku_id().equals(sku_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 1,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出的订单" + order_id + "商品" + sku_id + "总数和预期的不一致");

			// 从搜索结果中过滤看看有没有不符合过滤条件的
			targetOrderSkus = orderSkus.stream()
					.filter(s -> !s.getAddress_id().equals(address_id) || !s.getSku_id().equals(sku_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 0,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出有不符合条件的数据 " + JsonUtil.objectToStr(targetOrderSkus));
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase07() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按[商品名称,商户ID]组合方式查询");

		// 随机选取一个商品
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String sku_id = order_sku.getSku_id();
		String sku_name = order_sku.getSku_name();

		orderSkuFilterParam.setSearch_text(sku_name + "," + address_id);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();

		try {
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			// 从搜索结果中过滤中订单的商品总数
			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getAddress_id().equals(address_id) && s.getOrder_id().equals(order_id)
							&& s.getSku_id().equals(sku_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 1,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出的订单" + order_id + "商品" + sku_id + "总数和预期的不一致");

			// 从搜索结果中过滤看看有没有不符合过滤条件的
			targetOrderSkus = orderSkus.stream()
					.filter(s -> !s.getAddress_id().equals(address_id) || !s.getSku_name().contains(sku_name))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 0,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出有不符合条件的数据 " + JsonUtil.objectToStr(targetOrderSkus));
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase08() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-按[商品ID,商户名称]组合方式查询");

		// 随机选取一个商品
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String sku_id = order_sku.getSku_id();

		OrderSkuFilterParam param = new OrderSkuFilterParam();
		param.setQuery_type(1);
		param.setStart_date(todayStr);
		param.setEnd_date(todayStr);

		// 商品ID + 商户名称
		param.setSearch_text(sku_id + "," + address_name);
		param.setLimit(limit);
		int offset = 0;
		List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();

		try {
			while (true) {
				param.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(param);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			// 从搜索结果中过滤中订单的商品总数
			List<OrderSkuFilterResultBean> targetOrderSkus = orderSkus.stream()
					.filter(s -> s.getOrder_id().equals(order_id) && s.getSku_id().equals(sku_id))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 1,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出的订单" + order_id + "商品" + sku_id + "总数和预期的不一致");

			// 从搜索结果中过滤看看有没有不符合过滤条件的
			targetOrderSkus = orderSkus.stream()
					.filter(s -> !s.getSku_id().equals(sku_id) || !s.getResname().contains(address_name))
					.collect(Collectors.toList());
			Assert.assertEquals(targetOrderSkus.size(), 0,
					"订单列表-按商品查看-按按[商品,商户信息]组合过滤,过滤出有不符合条件的数据 " + JsonUtil.objectToStr(targetOrderSkus));
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase09() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-以商品一级分类进行过滤");
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String spu_id = order_sku.getSpu_id();
		try {
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU详细信息失败");
			String category_id_1 = spu.getCategory_id_1();
			String category_name_1 = spu.getCategory_name_1();

			long expectedCount = orderDetail.getDetails().stream()
					.filter(d -> d.getCategory_title_1().equals(category_name_1)).count();

			orderSkuFilterParam.setSearch_text("");
			// 以一级分类ID过滤
			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category_id_1);
			orderSkuFilterParam.setCategory1_ids(category1_ids);
			orderSkuFilterParam.setCategory2_ids(new JSONArray());
			orderSkuFilterParam.setPinlei_ids(new JSONArray());
			orderSkuFilterParam.setSearch_text(order_id);

			orderSkuFilterParam.setLimit(limit);
			int offset = 0;
			List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			long actualOrderSkusCount = orderSkus.stream().filter(s -> s.getOrder_id().equals(order_id)).count();
			Assert.assertEquals(actualOrderSkusCount, expectedCount,
					"订单列表-按商品查看-按下单日期-以商品一级分类进行过滤,过滤出订单 " + order_id + " 中的一级分类 " + category_name_1 + "商品数量和与预期的数量不一致");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase10() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-以商品二级分类进行过滤");
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String spu_id = order_sku.getSpu_id();
		try {
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU详细信息失败");
			String category_id_1 = spu.getCategory_id_1();
			String category_name_1 = spu.getCategory_name_1();
			String category_id_2 = spu.getCategory_id_2();
			String category_name_2 = spu.getCategory_name_2();

			long expectedCount = orderDetail.getDetails().stream()
					.filter(d -> d.getCategory_title_1().equals(category_name_1)
							&& d.getCategory_title_2().equals(category_name_2))
					.count();

			orderSkuFilterParam.setSearch_text("");
			// 以一级分类ID过滤
			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category_id_1);
			orderSkuFilterParam.setCategory1_ids(category1_ids);

			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category_id_2);

			orderSkuFilterParam.setCategory2_ids(category2_ids);
			orderSkuFilterParam.setPinlei_ids(new JSONArray());

			orderSkuFilterParam.setLimit(limit);
			int offset = 0;
			List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			long actualOrderSkusCount = orderSkus.stream().filter(s -> s.getOrder_id().equals(order_id)).count();
			Assert.assertEquals(actualOrderSkusCount, expectedCount,
					"订单列表-按商品查看-按下单日期-以商品二级分类进行过滤,过滤出订单 " + order_id + " 中的二级分类 " + category_name_2 + "商品数量和与预期的数量不一致");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}

	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase11() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-按下单日期-以商品品类分类进行过滤");
		int index = new Random().nextInt(orderDetail.getDetails().size());
		Detail order_sku = orderDetail.getDetails().get(index);
		String spu_id = order_sku.getSpu_id();
		try {
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU详细信息失败");
			String category_id_1 = spu.getCategory_id_1();
			String category_name_1 = spu.getCategory_name_1();
			String category_id_2 = spu.getCategory_id_2();
			String category_name_2 = spu.getCategory_name_2();
			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			// 先把符合添加的直接过滤出来,减少要访问接口查询的次数
			List<Detail> temp_details = orderDetail.getDetails().stream()
					.filter(d -> d.getCategory_title_1().equals(category_name_1)
							&& d.getCategory_title_2().equals(category_name_2))
					.collect(Collectors.toList());

			String temp_spu_id = null;
			SpuBean temp_spu = null;

			long expectedCount = 0;
			for (Detail detail : temp_details) {
				temp_spu_id = detail.getSpu_id();
				temp_spu = categoryService.getSpuById(temp_spu_id);
				Assert.assertNotEquals(temp_spu, null, "获取SPU详细信息失败");
				if (temp_spu.getPinlei_id().equals(pinlei_id)) {
					expectedCount += 1;
				}
			}

			orderSkuFilterParam.setSearch_text("");
			// 一级分类ID参数
			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category_id_1);
			orderSkuFilterParam.setCategory1_ids(category1_ids);

			// 二级分类ID参数
			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category_id_2);
			orderSkuFilterParam.setCategory2_ids(category2_ids);

			// 品类分类ID参数
			JSONArray pinlei_ids = new JSONArray();
			pinlei_ids.add(pinlei_id);
			orderSkuFilterParam.setPinlei_ids(pinlei_ids);

			orderSkuFilterParam.setLimit(limit);
			int offset = 0;
			List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
				} else {
					break;
				}
			}

			long actualOrderSkusCount = orderSkus.stream().filter(s -> s.getOrder_id().equals(order_id)).count();
			Assert.assertEquals(actualOrderSkusCount, expectedCount,
					"订单列表-按商品查看-按下单日期-以商品品类分类进行过滤,过滤出订单 " + order_id + " 中的品类分类 " + pinlei_name + "商品数量和与预期的数量不一致");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase12() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-获取订单的分拣备注(按下单日期)");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);

			String weight_remark = StringUtil.getRandomString(6).toUpperCase();

			List<String> order_ids = Arrays.asList(order_id);
			boolean result = orderService.updateOrderState(order_ids, 5, weight_remark);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为分拣中失败");

			WeightRemarkFilterParam weightRemarkFilterParam = new WeightRemarkFilterParam();
			weightRemarkFilterParam.setQuery_type(1);
			weightRemarkFilterParam.setStart_date(todayStr);
			weightRemarkFilterParam.setEnd_date(todayStr);
			List<String> weight_remarks = orderService.getWeightRemarks(weightRemarkFilterParam);
			Assert.assertNotEquals(weight_remarks, null, "订单列表-按商品查看-获取订单的分拣备注失败(按下单时间)");

			String msg = null;
			if (!weight_remarks.contains(weight_remark)) {
				msg = String.format("按下单日期拉取,获取到的分拣备注中没有目标分拣备注%s", weight_remark);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time();
			String cycle_end_time = orderCycle.getCycle_end_time();
			weightRemarkFilterParam = new WeightRemarkFilterParam();
			weightRemarkFilterParam.setQuery_type(2);
			weightRemarkFilterParam.setTime_config_id(time_config_id);
			weightRemarkFilterParam.setCycle_start_time(cycle_start_time);
			weightRemarkFilterParam.setCycle_end_time(cycle_end_time);

			weight_remarks = orderService.getWeightRemarks(weightRemarkFilterParam);
			Assert.assertNotEquals(weight_remarks, null, "订单列表-按商品查看-获取订单的分拣备注失败(按运营周期)");

			if (!weight_remarks.contains(weight_remark)) {
				msg = String.format("按运营周期拉取,获取到的分拣备注中没有目标分拣备注%s", weight_remark);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String receive_start_date = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10);
			weightRemarkFilterParam = new WeightRemarkFilterParam();
			weightRemarkFilterParam.setQuery_type(3);
			weightRemarkFilterParam.setReceive_start_date(receive_start_date);
			weightRemarkFilterParam.setReceive_end_date(receive_start_date);

			weight_remarks = orderService.getWeightRemarks(weightRemarkFilterParam);
			Assert.assertNotEquals(weight_remarks, null, "订单列表-按商品查看-获取订单的分拣备注失败(按收货时间)");
			if (!weight_remarks.contains(weight_remark)) {
				msg = String.format("按收货时间拉取,获取到的分拣备注中没有目标分拣备注%s", weight_remark);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "分拣备注拉取,拉取的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-获取订单的分拣备注信息遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-获取订单的分拣备注信息遇到错误: ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase13() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以分拣备注搜索商品");
		try {
			String sorting_remark = StringUtil.getRandomString(6).toUpperCase();

			List<String> order_ids = Arrays.asList(order_id);
			boolean result = orderService.updateOrderState(order_ids, 5, sorting_remark);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为分拣中失败");

			WeightRemarkFilterParam weightRemarkFilterParam = new WeightRemarkFilterParam();
			weightRemarkFilterParam.setQuery_type(1);
			weightRemarkFilterParam.setStart_date(todayStr);
			weightRemarkFilterParam.setEnd_date(todayStr);
			List<String> sorting_remarks = orderService.getWeightRemarks(weightRemarkFilterParam);
			Assert.assertEquals(sorting_remarks.contains(sorting_remark), true, "获取到的分拣备注中没有目标分拣备注 " + sorting_remark);

			OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
			orderSkuFilterParam.setQuery_type(1);
			orderSkuFilterParam.setStart_date(todayStr);
			orderSkuFilterParam.setEnd_date(todayStr);
			orderSkuFilterParam.setSearch_text("");
			orderSkuFilterParam.setBatch_remark(sorting_remark);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);

			long expectedCount = orderSkus.stream().filter(s -> s.getOrder_id().equals(order_id)).count();
			Assert.assertEquals(orderDetail.getDetails().size(), expectedCount, "订单列表-按商品查看-以分拣备注搜索商品搜索到的商品结果数目和预期不一致");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以分拣备注搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以分拣备注搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase14() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以分拣备注(无分拣备注)搜索商品");
		try {
			OrderSkuFilterParam param = new OrderSkuFilterParam();
			param.setQuery_type(1);
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setSearch_text("");
			param.setBatch_remark_is_null(1);
			param.setOffset(0);
			param.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(param);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以分拣备注(无分拣备注)搜索商品失败");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以分拣备注搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以分拣备注搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase15() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以是否计重(计重)搜索商品");
		try {
			OrderSkuFilterParam param = new OrderSkuFilterParam();
			param.setQuery_type(1);
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setSearch_text("");
			param.setIs_weigh(1);
			param.setOffset(0);
			param.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(param);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以是否计重(计重)搜索商品失败");

			List<OrderSkuFilterResultBean> fiterOrderSkus = orderSkus.stream().filter(s -> s.getIs_weigh() != 1)
					.collect(Collectors.toList());
			Assert.assertEquals(fiterOrderSkus.size(), 0, "订单列表-按商品查看-以是否计重(计重)搜索商品,过滤出了不计重商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以是否计重商品搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以是否计重商品搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase16() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以是否计重(不计重)搜索商品");
		try {
			OrderSkuFilterParam param = new OrderSkuFilterParam();
			param.setQuery_type(1);
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setSearch_text("");
			param.setIs_weigh(0);
			param.setOffset(0);
			param.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(param);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以是否计重(计重)搜索商品失败");

			List<OrderSkuFilterResultBean> fiterOrderSkus = orderSkus.stream().filter(s -> s.getIs_weigh() != 0)
					.collect(Collectors.toList());
			Assert.assertEquals(fiterOrderSkus.size(), 0, "订单列表-按商品查看-以是否计重(计重)搜索商品,过滤出了不计重商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以是否计重商品搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以是否计重商品搜索商品遇到错误 ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase17() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以是否已经称重(未称重)搜索商品");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 的详细信息失败");

			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + " 状态修改为分拣中失败");

			Detail detail = orderDetail.getDetails().get(new Random().nextInt(orderDetail.getDetails().size()));

			String sku_id = detail.getSku_id();
			BigDecimal quantity = detail.getStd_unit_quantity();
			BigDecimal std_real_quantity = quantity.add(new BigDecimal("0.1"));

			result = orderService.orderRealQuantityUpdate(order_id, sku_id, std_real_quantity);
			Assert.assertEquals(result, true, "订单列表-按商品查看,对商品进行修改出库数失败");

			orderSkuFilterParam.setSearch_text(order_id);
			orderSkuFilterParam.setWeighted(0);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以是否已称重(未称重)搜索商品失败");

			List<OrderSkuFilterResultBean> fiterOrderSkus = orderSkus.stream().filter(s -> s.getWeighted() != 0)
					.collect(Collectors.toList());
			Assert.assertEquals(fiterOrderSkus.size(), 0, "订单列表-按商品查看-以是否称重(未称重)搜索商品,过滤出了已经称重商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以是否已经称重搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以是否已经称重搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase18() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以是否已经称重(已称重)搜索商品");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 的详细信息失败");

			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + " 状态修改为分拣中失败");

			Detail detail = orderDetail.getDetails().get(new Random().nextInt(orderDetail.getDetails().size()));

			String sku_id = detail.getSku_id();
			BigDecimal quantity = detail.getStd_unit_quantity();
			BigDecimal std_real_quantity = quantity.add(new BigDecimal("0.1"));

			result = orderService.orderRealQuantityUpdate(order_id, sku_id, std_real_quantity);
			Assert.assertEquals(result, true, "订单列表-按商品查看,对商品进行修改出库数失败");

			orderSkuFilterParam.setSearch_text(order_id);
			orderSkuFilterParam.setWeighted(1);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以是否已称重(已称重)搜索商品失败");
			Assert.assertEquals(orderSkus.size(), 1, "订单列表-按商品查看-订单 " + order_id + " 应该只有一个商品进行了称重");

			List<OrderSkuFilterResultBean> fiterOrderSkus = orderSkus.stream().filter(s -> s.getWeighted() != 1)
					.collect(Collectors.toList());
			Assert.assertEquals(fiterOrderSkus.size(), 0, "订单列表-按商品查看-以是否称重(已称重)搜索商品,过滤出了未称重商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以是否已经称重搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以是否已经称重搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase19() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以订单状态过滤商品");
		try {
			orderSkuFilterParam.setStatus(5);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以订单状态(分拣中)搜索商品失败");

			List<OrderSkuFilterResultBean> temp_order_skus = orderSkus.stream().filter(s -> s.getStatus() != 5)
					.collect(Collectors.toList());
			Assert.assertEquals(temp_order_skus.size(), 0, "订单列表-按商品查看-以订单状态(分拣中)搜索商品,搜索出了非分拣中状态的商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以订单状态搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以订单状态搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase20() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以订单状态过滤商品");
		try {
			orderSkuFilterParam.setStatus(10);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以订单状态(配送中)搜索商品失败");

			List<OrderSkuFilterResultBean> temp_order_skus = orderSkus.stream().filter(s -> s.getStatus() != 10)
					.collect(Collectors.toList());
			Assert.assertEquals(temp_order_skus.size(), 0, "订单列表-按商品查看-以订单状态(配送中)搜索商品,搜索出了非配送中状态的商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以订单状态搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以订单状态搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase21() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以订单状态过滤商品");
		try {
			orderSkuFilterParam.setStatus(15);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以订单状态(已签收)搜索商品失败");

			List<OrderSkuFilterResultBean> temp_order_skus = orderSkus.stream().filter(s -> s.getStatus() != 15)
					.collect(Collectors.toList());
			Assert.assertEquals(temp_order_skus.size(), 0, "订单列表-按商品查看-以订单状态(已签收)搜索商品,搜索出了非已签收状态的商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以订单状态搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以订单状态搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase22() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以支付状态过滤商品");
		try {
			orderSkuFilterParam.setPay_status(1);
			orderSkuFilterParam.setOffset(0);
			orderSkuFilterParam.setLimit(limit);

			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以支付状态(未支付)搜索商品失败");

			orderSkuFilterParam.setPay_status(5);
			orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以支付状态(部分支付)搜索商品失败");

			orderSkuFilterParam.setPay_status(10);
			orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-以支付状态(已支付)搜索商品失败");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以支付状态搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以支付状态搜索商品遇到错误 ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase23() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以报价单过滤商品");
		try {
			Detail detail = orderDetail.getDetails().get(0);
			String salemenu_id = detail.getSalemenu_id();

			orderSkuFilterParam.setSalemenu_id(salemenu_id);
			orderSkuFilterParam.setLimit(limit);

			int offset = 0;
			List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索商品失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					if (offset >= 100) {
						break;
					}
				} else {
					break;
				}
			}

			long count = orderSkus.stream().filter(s -> !s.getSalemenu_id().equals(salemenu_id)).count();
			Assert.assertEquals(count, 0, "订单列表-按商品查看-以报价单过滤搜索商品,搜索出了非 " + salemenu_id + "报价单里的商品");

		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以支付状态搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以支付状态搜索商品遇到错误 ", e);
		}
	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase24() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以线路过滤搜索商品");
		orderSkuFilterParam.setRoute_id(new BigDecimal("-1"));
		orderSkuFilterParam.setLimit(limit);
		try {
			int offset = 0;
			List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
			while (true) {
				orderSkuFilterParam.setOffset(offset);
				List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
				Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索商品失败");
				orderSkus.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					if (offset >= 100) {
						break;
					}
				} else {
					break;
				}
			}
			long count = orderSkus.stream().filter(s -> !s.getRoute_name().equals("无线路")).count();
			Assert.assertEquals(count, 0, "订单列表-按商品查看-以线路搜索过滤商品,过滤出了不符合过滤路线的商品");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以线路搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以线路搜索商品遇到错误 ", e);
		}

	}

	@Test(timeOut = 60000)
	public void searchOrderSkuTestCase25() {
		try {
			ReporterCSS.title("测试点: 订单列表-按商品查看-以线路过滤搜索商品");

			orderSkuFilterParam.setLimit(limit);

			routeService.initRouteData();
			List<RouteBean> routes = routeService.getAllRoutes();
			Assert.assertNotEquals(routes, null, "获取全部线路信息失败");

			BigDecimal route_id = null;
			for (RouteBean route : routes) {
				List<OrderSkuFilterResultBean> orderSkus = new ArrayList<>();
				route_id = route.getId();
				String route_name = route.getName();
				orderSkuFilterParam.setRoute_id(route_id);
				int offset = 0;
				orderSkuFilterParam.setOffset(0);
				while (true) {
					orderSkuFilterParam.setOffset(offset);
					List<OrderSkuFilterResultBean> tempArray = orderService.searchOrderSku(orderSkuFilterParam);
					Assert.assertNotEquals(tempArray, null, "订单列表-按商品查看-过滤搜索商品失败");
					orderSkus.addAll(tempArray);
					if (tempArray.size() >= limit) {
						offset += limit;
						if (offset >= 100) {
							break;
						}
					} else {
						break;
					}
				}
				long count = orderSkus.stream().filter(s -> !s.getRoute_name().equals(route_name)).count();
				Assert.assertEquals(count, 0, "订单列表-按商品查看-以线路搜索过滤商品,过滤出了不符合过滤路线的商品");
			}
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以线路搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以线路搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase26() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以收货时间搜索商品");
		String start_date = orderDetail.getCustomer().getReceive_begin_time();
		String end_date = orderDetail.getCustomer().getReceive_end_time();
		orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(3);
		orderSkuFilterParam.setStart_date_new(start_date);
		orderSkuFilterParam.setEnd_date_new(end_date);
		orderSkuFilterParam.setSearch_text(order_id);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setLimit(limit);

		try {
			List<OrderSkuFilterResultBean> order_skus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(order_skus, null, "订单列表-按商品查看-以收货时间搜索商品失败");

			Assert.assertEquals(order_skus.size(), orderDetail.getDetails().size(), "订单列表-按商品查看-搜索到的商品总数和预期的不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以收货时间搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以收货时间搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase27() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-以运营时间搜索商品");
		OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(2);
		orderSkuFilterParam.setSearch_text(order_id);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setLimit(limit);
		try {
			String time_config_id = orderDetail.getTime_config_id();
			ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间 " + time_config_id + " 的详细信息失败");

			OrderCycle orderCyle = orderTool.getOrderOperationCycle(order_id);
			orderSkuFilterParam.setTime_config_id(orderCyle.getTime_config_id());
			orderSkuFilterParam.setStart_date_new(orderCyle.getCycle_start_time());
			orderSkuFilterParam.setEnd_date_new(orderCyle.getCycle_end_time());

			List<OrderSkuFilterResultBean> order_skus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(order_skus, null, "订单列表-按商品查看-以运营时间搜索商品失败");

			Assert.assertEquals(order_skus.size(), orderDetail.getDetails().size(), "订单列表-按商品查看-搜索到的商品总数和预期的不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以运营时间搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以运营时间搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase28() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU");
		OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(1);
		orderSkuFilterParam.setStart_date_new(start_date_new);
		orderSkuFilterParam.setEnd_date_new(end_date_new);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setLimit(limit);
		try {
			List<OrderAndSkuBean> skuArray = new ArrayList<OrderAndSkuBean>();
			OrderAndSkuBean orderAndSku = null;

			List<OrderSkuFilterResultBean> order_skus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(order_skus, null, "订单列表-按商品查看-以下单时间搜索商品失败");

			List<String> order_ids = new ArrayList<String>();
			List<String> sku_ids = new ArrayList<String>();
			String sku_id = null;
			String spu_id = null;
			SkuBean sku = null;
			String order_id = null;
			OrderDetailBean orderDetail = null;
			BigDecimal sale_price = null;
			boolean result = true;
			Map<String, BigDecimal> expected_sale_unit_price_map = new HashMap<String, BigDecimal>();
			for (OrderSkuFilterResultBean orderSkuFilterResult : order_skus) {
				sku_id = orderSkuFilterResult.getSku_id();
				order_id = orderSkuFilterResult.getOrder_id();
				if (!order_ids.contains(order_id) && !sku_ids.contains(sku_id)) {
					orderAndSku = new OrderAndSkuBean(order_id, sku_id);
					skuArray.add(orderAndSku);
					order_ids.add(order_id);
					sku_ids.add(sku_id);

					orderDetail = orderService.getOrderDetailById(order_id);
					Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

					for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
						if (detail.getSku_id().equals(sku_id)) {
							spu_id = detail.getSpu_id();
							// 避免商品单价过大,当销售基本单价大于50时,就减0.1
							if (detail.getStd_sale_price_forsale().compareTo(new BigDecimal("50")) > 0) {
								sale_price = (detail.getStd_sale_price_forsale().subtract(new BigDecimal("0.1"))
										.multiply(detail.getSale_ratio()));
							} else {
								sale_price = (detail.getStd_sale_price_forsale().add(new BigDecimal("0.1"))
										.multiply(detail.getSale_ratio()));
							}

							detail.setSale_price(sale_price);
							expected_sale_unit_price_map.put(order_id + "-" + spu_id + "-" + sku_id, sale_price);
							break;
						}
					}
					result = orderTool.updateOrder(orderDetail);
					Assert.assertEquals(result, true, "修改订单" + order_id + "失败");
					if (skuArray.size() >= 2) {
						break;
					}
				}
			}

			BigDecimal task_id = orderService.batchOrderPriceSyncToSku(skuArray);
			Assert.assertNotEquals(task_id, null, "订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU创建异步任务失败");

			result = asyncService.getAsyncTaskResult(task_id, "失败0");

			String msg = null;
			if (!result) {
				List<OrderPriceSyncToSkuResultBean> orderPriceSyncToSkuResults = orderService
						.batchOrderPriceSyncToSkuResult(task_id);
				Assert.assertNotEquals(orderPriceSyncToSkuResults, null, "获取异步任务执行结果详细信息失败");

				int reason_type = 0;
				result = true;
				List<String> remove_keys = new ArrayList<String>();
				for (OrderPriceSyncToSkuResultBean orderPriceSyncToSkuResult : orderPriceSyncToSkuResults) {
					reason_type = orderPriceSyncToSkuResult.getReason_type();
					// 删除的商品不需要同步
					if (reason_type == 1) {
						String temp_sku_id = orderPriceSyncToSkuResult.getId();
						String temp_salemenu_id = orderPriceSyncToSkuResult.getSalemenu_id();
						SalemenuSkuBean skuSalemenu = categoryService.getSkuInSalemenu(temp_salemenu_id, temp_sku_id);
						Assert.assertNotEquals(skuSalemenu, null, "报价单 " + temp_salemenu_id + " 里搜索过滤商品失败");
						if (skuSalemenu.getSku_id() != null) {
							msg = String.format("订单商品%s价格同步销售SKU失败原因显示为:商品已被删除,其实商品没删除", temp_sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						// 删不需要验证的商品
						for (String key : expected_sale_unit_price_map.keySet()) {
							if (key.contains(temp_sku_id)) {
								remove_keys.add(key);
							}
						}
					}

					// 时间商品不需要同步价格
					if (reason_type == 2) {
						String temp_sku_id = orderPriceSyncToSkuResult.getId();
						String temp_salemenu_id = orderPriceSyncToSkuResult.getSalemenu_id();
						SalemenuSkuBean skuSalemenu = categoryService.getSkuInSalemenu(temp_salemenu_id, temp_sku_id);
						Assert.assertNotEquals(skuSalemenu, null, "报价单 " + temp_salemenu_id + " 里搜索过滤商品失败");
						if (!skuSalemenu.isIs_price_timing()) {
							msg = String.format("订单商品%s价格同步销售SKU失败原因显示为:商品为时价商品,其实商品不是时价商品", temp_sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						for (String key : expected_sale_unit_price_map.keySet()) {
							if (key.contains(temp_sku_id)) {
								remove_keys.add(key);
							}
						}
					}
				}

				for (String key : remove_keys) {
					expected_sale_unit_price_map.remove(key);
				}
			}

			boolean check_result = true;
			for (String key : expected_sale_unit_price_map.keySet()) {
				order_id = key.split("-")[0];
				spu_id = key.split("-")[1];
				sku_id = key.split("-")[2];
				sale_price = expected_sale_unit_price_map.get(key);
				sku = categoryService.getSaleSkuById(spu_id, sku_id);
				Assert.assertNotEquals(sku, null, "获取销售SKU" + sku_id + "详细信息失败");
				if (sale_price.compareTo(sku.getSale_price()) != 0) {
					msg = String.format("订单%s里的商品%s价格没有同步到销售SKU,预期%s,实际:%s", order_id, sku_id, sale_price,
							sku.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					check_result = false;
				}
			}
			Assert.assertEquals(result && check_result, true, "订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU没有成功");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase29() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-搜索过滤商品后进行销售价格同步至销售SKU");
		orderSkuFilterParam.setSearch_text(order_id);
		try {
			String sku_id = null;
			String spu_id = null;
			BigDecimal sale_ratio = null;
			OrderDetailBean.Detail detail = null;
			BigDecimal sale_price = null;
			Map<String, BigDecimal> expected_sale_price_map = new HashMap<String, BigDecimal>();
			for (int i = 0; i < orderDetail.getDetails().size(); i += 2) {
				detail = orderDetail.getDetails().get(i);
				sku_id = detail.getSku_id();
				spu_id = detail.getSpu_id();
				sale_ratio = detail.getSale_ratio();
				// 基本单位 加 0.1
				sale_price = (detail.getStd_sale_price_forsale().add(new BigDecimal("0.1")).multiply(sale_ratio));
				detail.setSale_price(sale_price);
				expected_sale_price_map.put(spu_id + "-" + sku_id, sale_price);
			}

			boolean result = orderTool.updateOrder(orderDetail);
			Assert.assertEquals(result, true, "修改订单" + order_id + "失败");

			BigDecimal task_id = orderService.batchOrderPriceSyncToSku(orderSkuFilterParam);
			Assert.assertNotEquals(task_id, null, "订单列表-按商品查看-搜索过滤商品后进行销售价格同步至销售SKU异步任务创建失败");
			String msg = null;

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			if (result) {
				List<OrderPriceSyncToSkuResultBean> orderPriceSyncToSkuResults = orderService
						.batchOrderPriceSyncToSkuResult(task_id);
				Assert.assertNotEquals(orderPriceSyncToSkuResults, null, "获取异步任务执行结果详细信息失败");

				int reason_type = 0;
				result = true;
				List<String> remove_keys = new ArrayList<String>();
				for (OrderPriceSyncToSkuResultBean orderPriceSyncToSkuResult : orderPriceSyncToSkuResults) {
					reason_type = orderPriceSyncToSkuResult.getReason_type();
					// 删除的商品不需要同步
					if (reason_type == 1) {
						String temp_sku_id = orderPriceSyncToSkuResult.getId();
						String temp_salemenu_id = orderPriceSyncToSkuResult.getSalemenu_id();
						SalemenuSkuBean skuSalemenu = categoryService.getSkuInSalemenu(temp_salemenu_id, temp_sku_id);
						Assert.assertNotEquals(skuSalemenu, null, "报价单 " + temp_salemenu_id + " 里搜索过滤商品失败");
						if (skuSalemenu.getSku_id() != null) {
							msg = String.format("订单商品%s价格同步销售SKU失败原因显示为:商品已被删除,其实商品没删除", temp_sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						for (String key : expected_sale_price_map.keySet()) {
							if (key.contains(temp_sku_id)) {
								remove_keys.add(key);
							}
						}
					}

					// 时价商品不需要同步
					if (reason_type == 2) {
						String temp_sku_id = orderPriceSyncToSkuResult.getId();
						String temp_salemenu_id = orderPriceSyncToSkuResult.getSalemenu_id();
						SalemenuSkuBean skuSalemenu = categoryService.getSkuInSalemenu(temp_salemenu_id, temp_sku_id);
						Assert.assertNotEquals(skuSalemenu, null, "报价单 " + temp_salemenu_id + " 里搜索过滤商品失败");
						if (!skuSalemenu.isIs_price_timing()) {
							msg = String.format("订单商品%s价格同步销售SKU失败原因显示为:商品为时价商品,其实商品不是时价商品", temp_sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						for (String key : expected_sale_price_map.keySet()) {
							if (key.contains(temp_sku_id)) {
								remove_keys.add(key);
							}
						}
					}
				}

				for (String key : remove_keys) {
					expected_sale_price_map.remove(key);
				}
			}

			SkuBean sku = null;

			for (String key : expected_sale_price_map.keySet()) {
				spu_id = key.split("-")[0];
				sku_id = key.split("-")[1];
				sale_price = expected_sale_price_map.get(key).setScale(2, BigDecimal.ROUND_HALF_UP);
				sku = categoryService.getSaleSkuById(spu_id, sku_id);
				Assert.assertNotEquals(sku, null, "获取销售SKU" + sku_id + "详细信息失败");
				if (sale_price.compareTo(sku.getSale_price()) != 0) {
					msg = String.format("订单%s里的商品%s价格没有同步到销售SKU,预期%s,实际:%s", order_id, sku_id, sale_price,
							sku.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU没有成功");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-选择个别商品进行销售价格同步至销售SKU遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase30() {
		ReporterCSS.title("测试点: 旧版UI,订单列表-按商品查看-按下单日期-以订单号过滤");
		// 因为下单商品最多10个,所以这里查询就不需要翻页了
		orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(1);
		orderSkuFilterParam.setStart_date(todayStr);
		orderSkuFilterParam.setEnd_date(todayStr);
		orderSkuFilterParam.setSearch_text(order_id);
		orderSkuFilterParam.setLimit(limit);
		orderSkuFilterParam.setOffset(0);
		try {
			List<OrderSkuFilterResultBean> orderSkus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(orderSkus, null, "订单列表-按商品查看-过滤搜索失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + " 详细信息失败");

			OrderSkuFilterResultBean orderSku = null;
			boolean result = true;
			String msg = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				orderSku = orderSkus.stream().filter(s -> s.getSku_id().equals(detail.getSku_id())).findAny()
						.orElse(null);
				if (orderSku == null) {
					msg = String.format("订单%s中的商品%s,按商品查看没有搜索到", order_id, detail.getSku_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (orderSku.getQuantity().compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s中的商品%s,订单详细中的下单数和商品列表下的单数不一致,订单详细:%s,商品列表:%s", order_id,
							detail.getSku_id(), detail.getQuantity(), orderSku.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}

				BigDecimal detail_sale_money = detail.getSale_money().divide(new BigDecimal("100")).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				BigDecimal sku_sale_money = orderSku.getSale_money().setScale(2, BigDecimal.ROUND_HALF_UP);
				if (detail_sale_money.compareTo(sku_sale_money) != 0) {
					msg = String.format("订单%s中的商品%s,订单详细中的下单金额和商品列表下单金额的不一致,订单详细:%s,商品列表:%s", order_id,
							detail.getSku_id(), detail_sale_money, sku_sale_money);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			}
			Assert.assertEquals(result, true, "订单 " + order_id + "详细信息和按商品查看到的信息不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-搜索过滤遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase31() {
		ReporterCSS.title("测试点: 旧版UI,订单列表-按商品查看-以运营时间搜索商品");
		OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(2);
		orderSkuFilterParam.setSearch_text(order_id);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setLimit(limit);
		try {
			String time_config_id = orderDetail.getTime_config_id();
			ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间 " + time_config_id + " 的详细信息失败");

			OrderCycle orderCyle = orderTool.getOrderOperationCycle(order_id);
			orderSkuFilterParam.setTime_config_id(orderCyle.getTime_config_id());
			orderSkuFilterParam.setStart_date(orderCyle.getCycle_start_time());
			orderSkuFilterParam.setEnd_date(orderCyle.getCycle_end_time());

			List<OrderSkuFilterResultBean> order_skus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(order_skus, null, "订单列表-按商品查看-以运营时间搜索商品失败");

			Assert.assertEquals(order_skus.size(), orderDetail.getDetails().size(), "订单列表-按商品查看-搜索到的商品总数和预期的不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以运营时间搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以运营时间搜索商品遇到错误 ", e);
		}
	}

	@Test
	public void searchOrderSkuTestCase32() {
		ReporterCSS.title("测试点: 旧版UI,订单列表-按商品查看-以收货时间搜索商品");
		String start_date = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10);
		orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setQuery_type(3);
		orderSkuFilterParam.setStart_date(start_date);
		orderSkuFilterParam.setEnd_date(start_date);
		orderSkuFilterParam.setSearch_text(order_id);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setLimit(limit);

		try {
			List<OrderSkuFilterResultBean> order_skus = orderService.searchOrderSku(orderSkuFilterParam);
			Assert.assertNotEquals(order_skus, null, "订单列表-按商品查看-以收货时间搜索商品失败");

			Assert.assertEquals(order_skus.size(), orderDetail.getDetails().size(), "订单列表-按商品查看-搜索到的商品总数和预期的不一致");
		} catch (Exception e) {
			logger.error("订单列表-按商品查看-以收货时间搜索商品遇到错误: ", e);
			Assert.fail("订单列表-按商品查看-以收货时间搜索商品遇到错误 ", e);
		}
	}

}
