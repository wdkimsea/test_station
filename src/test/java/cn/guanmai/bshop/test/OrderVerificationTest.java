package cn.guanmai.bshop.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.bshop.bean.CartUpdateResult;
import cn.guanmai.bshop.bean.OrderDetailBean;
import cn.guanmai.bshop.bean.OrderDetailBean.Detail;
import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.bean.product.BsProductBean.Sku;
import cn.guanmai.bshop.impl.BshopServiceImpl;
import cn.guanmai.bshop.service.BshopService;
import cn.guanmai.bshop.tools.LoginBshop;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Apr 22, 2019 5:45:17 PM 
* @des 订单相关验证 
* @version 1.0 
*/
public class OrderVerificationTest extends LoginBshop {
	private static Logger logger = LoggerFactory.getLogger(OrderVerificationTest.class);
	private OrderService orderService;
	private BshopService bshopService;
	private WeightService weighService;
	private List<String> order_ids;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void beforeTest() {
		try {
			Map<String, String> st_cookie = LoginUtil.loginStation();
			Assert.assertNotEquals(st_cookie, null, "登录Station失败");
			orderService = new OrderServiceImpl(st_cookie);
			weighService = new WeightServiceImpl(st_cookie);

			Map<String, String> bshop_cookie = getBshopCookie();
			bshopService = new BshopServiceImpl(bshop_cookie);

			BsAccountBean account = bshopService.getAccountInfo();
			Assert.assertNotEquals(account, null, "获取账户信息失败");

			String sid = NumberUtil.roundNumberInList(account.getAddresses()).getSid();

			boolean result = bshopService.setAddress(sid);
			Assert.assertEquals(result, true, "选择分店操作失败");
		} catch (Exception e) {
			logger.error("初始化操作失败: ", e);
			Assert.fail("初始化操作失败: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<String> serch_texts = Arrays.asList("A", "E", "I", "O", "U");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			BigDecimal expected_money = BigDecimal.ZERO;
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					List<Sku> skus = product.getSkus();
					Sku sku = NumberUtil.roundNumberInList(skus);
					BigDecimal quantity = NumberUtil.getRandomNumber(5, 15, 0);
					if (!skuMap.containsKey(sku.getSku_id())) {
						if (!sku.isIs_price_timing()) {
							expected_money = expected_money.add(sku.getSale_price().multiply(quantity));
						}
						skuMap.put(sku.getSku_id(), quantity);
						if (skuMap.size() >= 20) {
							break OK;
						}
					}
				}
			}

			Assert.assertEquals(skuMap.size() > 0, true, "没有搜索到下单商品,下单集合为空,无法进行下单");

			CartUpdateResult cartUpdateResult = bshopService.updateCart(skuMap);
			Assert.assertNotEquals(cartUpdateResult, null, "商品加入购物车失败");

			boolean result = bshopService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, true, "设置付款方式失败");

			result = bshopService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			order_ids = bshopService.submitCart(false);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");
			logger.info("订单号: " + order_ids);
		} catch (Exception e) {
			logger.error("BSHOP下单遇到错误 ", e);
			Assert.fail("BSHOP下单遇到错误 ", e);
		}

	}

	@Test
	public void orderVerificationTestCase01() {
		Reporter.log("测试点: BSHOP下单后,在Station订单列表查询此订单");
		try {
			OrderFilterParam filterParam = new OrderFilterParam();
			filterParam.setQuery_type(1);
			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);
			filterParam.setOffset(0);
			filterParam.setLimit(20);

			List<OrderBean> orderlist = null;
			for (String order_id : order_ids) {
				filterParam.setSearch_text(order_id);
				orderlist = orderService.searchOrder(filterParam);
				Assert.assertNotEquals(orderlist, null, "BSHOP下单后,在Station订单列表查询此订单失败");

				OrderBean order = orderlist.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
				Assert.assertNotEquals(order, null, "BSHOP下单后,在Station订单列表查询此订单,没有查询到");
			}
		} catch (Exception e) {
			logger.error("Station订单列表查询遇到错误", e);
			Assert.fail("Station订单列表查询遇到错误", e);
		}
	}

	@Test
	public void orderVerificationTestCase02() {
		try {
			Reporter.log("测试点: 下单后与ST的订单详细做对比");
			boolean result = true;
			for (String order_id : order_ids) {
				OrderDetailBean orderDetailInBshop = bshopService.orderDetail(order_id);
				Assert.assertNotEquals(orderDetailInBshop, null, "在BSHOP获取订单详细信息失败");

				cn.guanmai.station.bean.order.OrderDetailBean orderDetailInSt = orderService
						.getOrderDetailById(order_id);
				Assert.assertNotEquals(orderDetailInSt, null, "在ST获取订单详细信息失败");

				if (orderDetailInBshop.getTotal_price().compareTo(orderDetailInSt.getTotal_price()) != 0) {
					String msg = String.format("订单%s在bshop中显示的的总额与station显示的订单总额不一致,bshop:%s,station:%s", order_id,
							orderDetailInBshop.getTotal_price(), orderDetailInSt.getTotal_price());
					Reporter.log(msg);
					result = false;
				}

				String msg = null;
				for (Detail detailInbshop : orderDetailInBshop.getDetails()) {
					String sku_id = detailInbshop.getSku_id();
					BigDecimal quantity = detailInbshop.getReal_quantity();
					BigDecimal std_sale_price = detailInbshop.getStd_sale_price();
					cn.guanmai.station.bean.order.OrderDetailBean.Detail detailInSt = orderDetailInSt.getDetails()
							.stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
					if (quantity.compareTo(detailInSt.getReal_quantity()) != 0) {
						msg = String.format("订单%s里的下单商品%s,下单数bshop商城记录的为%s,station记录的下单数为%s", order_id, sku_id,
								quantity, detailInSt.getReal_quantity());
						result = false;
						Reporter.log(msg);
					}

					if (std_sale_price.compareTo(detailInSt.getStd_sale_price_forsale()) != 0) {
						msg = String.format("订单%s里的下单商品%s,商品基本单价bshop商城记录的为,station记录的下单数为", order_id, sku_id,
								std_sale_price, detailInSt.getStd_sale_price_forsale());
						result = false;
						Reporter.log(msg);
					}
				}
			}
			Assert.assertEquals(result, true, "bshop订单数据和station订单数据做对比,存在数据不一致的信息");
		} catch (Exception e) {
			logger.error("BSHOP下单后对数据进行验证遇到错误 ", e);
			Assert.fail("BSHOP下单后对数据进行验证遇到错误 ", e);
		}
	}

	@Test
	public void orderVerificationTestCase03() {
		Reporter.log("测试点: ST修改订单后,查看BSHOP中订单是否更新");
		try {
			String order_id = order_ids.get(0);
			cn.guanmai.station.bean.order.OrderDetailBean orderDetailInSt = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInSt, null, "ST获取订单详情失败");

			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);

			List<OrderCreateParam.OrderSku> orderSkus = new ArrayList<>();
			OrderCreateParam.OrderSku orderSku = null;
			for (cn.guanmai.station.bean.order.OrderDetailBean.Detail detail : orderDetailInSt.getDetails()) {
				orderSku = new OrderCreateParam().new OrderSku();
				orderSku.setSku_id(detail.getSku_id());
				orderSku.setSpu_id(detail.getSpu_id());
				orderSku.setAmount(detail.getQuantity().add(new BigDecimal("1")));
				orderSku.setUnit_price(NumberUtil.getRandomNumber(6, 15, 2));
				orderSku.setIs_price_timing(0);
				orderSku.setSpu_remark("--");
				orderSkus.add(orderSku);
			}
			editOrder.setDetails(orderSkus);

			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderDetailInSt.getCustomer().getReceive_begin_time());
			orderData.setReceive_end_time(orderDetailInSt.getCustomer().getReceive_end_time());
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "ST修改订单失败");

			orderDetailInSt = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInSt, null, "ST获取订单详情失败");

			OrderDetailBean orderDetailInBshop = bshopService.orderDetail(order_id);
			Assert.assertNotEquals(orderDetailInBshop, null, "BSHOP获取订单详情失败");
			String msg = null;
			if (orderDetailInBshop.getTotal_price().compareTo(orderDetailInSt.getTotal_price()) != 0) {
				msg = String.format("订单%s在bshop中显示的的总额与station显示的订单总额不一致,bshop:%s,station:%s", order_id,
						orderDetailInBshop.getTotal_price(), orderDetailInSt.getTotal_price());
				Reporter.log(msg);
				result = false;
			}

			for (cn.guanmai.station.bean.order.OrderDetailBean.Detail detail : orderDetailInSt.getDetails()) {
				String sku_id = detail.getSku_id();
				Detail detailInBshop = orderDetailInBshop.getDetails().stream()
						.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (detailInBshop == null) {
					msg = String.format("订单%s里的商品%s,在bshop的订单详细里没有查询到", order_id, sku_id);
					Reporter.log(msg);
					result = false;
					continue;
				}

				if (detailInBshop.getReal_quantity().compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s里的商品%s,station记录的下单数和bshop记录的下单数不一致,station:%s,bshop:%s", order_id, sku_id,
							detail.getQuantity(), detailInBshop.getReal_quantity());
					Reporter.log(msg);
					result = false;
				}

				if (detailInBshop.getStd_sale_price().compareTo(detail.getStd_sale_price_forsale()) != 0) {
					msg = String.format("订单%s里的商品%s,station记录的下单单价和bshop记录的下单单价不一致,station:%s,bshop:%s", order_id,
							sku_id, detail.getStd_sale_price_forsale(), detailInBshop.getStd_sale_price());
					Reporter.log(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "Station修改订单后,bshop订单详细中存在信息不一致的信息");
		} catch (Exception e) {
			logger.error("BSHOP下单后对数据进行验证遇到错误 ", e);
			Assert.fail("BSHOP下单后对数据进行验证遇到错误 ", e);
		}
	}

	@Test
	public void orderVerificationTestCase04() {
		Reporter.log("测试点: 订单称重后,查看BHSOP里的订单详细是否更新出库数");
		try {
			String order_id = order_ids.get(0);
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "修改订单状态失败");

			Map<String, BigDecimal> expected_map = new HashMap<>();

			OrderDetailBean orderDetailInBshop = bshopService.orderDetail(order_id);
			Assert.assertNotEquals(orderDetailInBshop, null, "BSHOP获取订单详情失败");
			String sku_id = null;
			BigDecimal set_weigh = null;

			SetWeightParam setWeightParam = new SetWeightParam();
			List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
			for (Detail detail : orderDetailInBshop.getDetails()) {
				sku_id = detail.getSku_id();
				set_weigh = detail.getReal_quantity().multiply(detail.getSale_ratio())
						.setScale(2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(1));

				SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"),
						set_weigh, false, 0);
				weights.add(weight);

				expected_map.put(sku_id, set_weigh);
			}
			setWeightParam.setWeights(weights);

			result = weighService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "新版称重软件-进行商品称重操作失败");

			orderDetailInBshop = bshopService.orderDetail(order_id);
			String msg = null;
			for (String id : expected_map.keySet()) {
				Detail detail = orderDetailInBshop.getDetails().stream().filter(d -> d.getSku_id().equals(id)).findAny()
						.orElse(null);
				if (detail == null) {
					msg = String.format("商品%s在bshop的订单%s详细中没有找到", id, order_id);
					Reporter.log(msg);
					result = false;
					continue;
				}
				BigDecimal actual_quantity = detail.getReal_quantity().multiply(detail.getSale_ratio()).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				if (actual_quantity.compareTo(expected_map.get(id)) != 0) {
					msg = String.format("商品%s称重后在bshop订单%s详细中没有更新,称重后:%s,实际:%s", id, order_id, actual_quantity,
							expected_map.get(id));
					Reporter.log(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "称重后,bshop里的出库数没有更新");
		} catch (Exception e) {
			logger.error("BSHOP下单后对数据进行验证遇到错误 ", e);
			Assert.fail("BSHOP下单后对数据进行验证遇到错误 ", e);
		}
	}
}
