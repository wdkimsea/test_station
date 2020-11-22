package cn.guanmai.bshop.test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.bshop.bean.CartUpdateResult;
import cn.guanmai.bshop.bean.CouponBean;
import cn.guanmai.bshop.bean.OrderDetailBean;
import cn.guanmai.bshop.bean.OrderDetailBean.Detail;
import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.bean.product.BsProductBean.Sku;
import cn.guanmai.bshop.impl.BshopServiceImpl;
import cn.guanmai.bshop.service.BshopService;
import cn.guanmai.bshop.tools.LoginBshop;
import cn.guanmai.util.NumberUtil;

/* 
* @author liming 
* @date Jan 12, 2019 2:15:20 PM 
* @des bshop 商城相关测试用例
* @version 1.0 
*/
public class BshopBusinessTest extends LoginBshop {
	private Logger logger = LoggerFactory.getLogger(BshopBusinessTest.class);

	private Map<String, String> cookie;
	private BshopService bshopService;

	@BeforeClass
	public void initData() {
		cookie = getBshopCookie();
		bshopService = new BshopServiceImpl(cookie);
		try {
			BsAccountBean account = bshopService.getAccountInfo();
			Assert.assertNotEquals(account, null, "获取账户信息失败");

			Random random = new Random();

			int index = random.nextInt(account.getAddresses().size());
			String sid = account.getAddresses().get(index).getSid();
			boolean result = bshopService.setAddress(sid);
			Assert.assertEquals(result, true, "选择分店操作失败");

		} catch (Exception e) {
			logger.error("选择分店遇到错误 ", e);
			Assert.fail("选择分店遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase01() {
		try {
			Reporter.log("测试点: 进行下单操作");
			List<String> serch_texts = Arrays.asList("A", "B", "C", "E");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					skuMap.put(product.getSkus().get(0).getSku_id(), NumberUtil.getRandomNumber(5, 10, 0));
					if (skuMap.size() >= 8) {
						break OK;
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

			List<String> order_ids = bshopService.submitCart(false);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");
			logger.info("订单号: " + order_ids);

		} catch (Exception e) {
			logger.error("商品加入购物车遇到错误 ", e);
			Assert.fail("商品加入购物车遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase02() {
		try {
			Reporter.log("测试点: 进行合单操作");
			List<String> serch_texts = Arrays.asList("A", "B", "C", "E");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					skuMap.put(product.getSkus().get(0).getSku_id(), NumberUtil.getRandomNumber(5, 10, 0));
					if (skuMap.size() >= 8) {
						break OK;
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

			List<String> order_ids = bshopService.submitCart(true);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");
			logger.info("订单号: " + order_ids);
		} catch (Exception e) {
			logger.error("进行合单操作遇到错误 ", e);
			Assert.fail("进行合单操作遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase03() {
		try {
			Reporter.log("测试点: 下单后验证订单总额与预期金额是否一致");
			List<String> serch_texts = Arrays.asList("A", "B", "C", "E");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			BigDecimal expected_money = BigDecimal.ZERO;
			Random random = new Random();
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					List<Sku> skus = product.getSkus();

					Sku sku = skus.get(random.nextInt(skus.size()));
					BigDecimal quantity = NumberUtil.getRandomNumber(5, 10, 0);
					if (!skuMap.containsKey(sku.getSku_id())) {
						if (!sku.isIs_price_timing()) {
							expected_money = expected_money.add(sku.getSale_price().multiply(quantity));
						}
						skuMap.put(sku.getSku_id(), quantity);
						if (skuMap.size() >= 10) {
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

			List<String> order_ids = bshopService.submitCart(false);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");
			logger.info("订单号: " + order_ids);

			BigDecimal actual_money = BigDecimal.ZERO;
			for (String order_id : order_ids) {
				OrderDetailBean orderDetail = bshopService.orderDetail(order_id);
				Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");
				actual_money = actual_money.add(orderDetail.getTotal_price());
			}

			Assert.assertEquals(actual_money.compareTo(expected_money) == 0, true,
					"预期下单总额为: " + expected_money + ",实际下单总额为: " + actual_money);
		} catch (Exception e) {
			logger.error("下单后对数据进行验证遇到错误 ", e);
			Assert.fail("下单后对数据进行验证遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase04() {
		try {
			Reporter.log("测试点: 下单后验证下单商品是否和添加购物车商品一致");
			List<String> serch_texts = Arrays.asList("A", "E", "I", "O", "U");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			BigDecimal expected_money = BigDecimal.ZERO;
			Random random = new Random();
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					List<Sku> skus = product.getSkus();
					Sku sku = skus.get(random.nextInt(skus.size()));
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

			List<String> order_ids = bshopService.submitCart(false);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");
			logger.info("订单号: " + order_ids);

			BigDecimal actual_money = BigDecimal.ZERO;
			Map<String, BigDecimal> actual_skuMap = new HashMap<>();
			for (String order_id : order_ids) {
				OrderDetailBean orderDetail = bshopService.orderDetail(order_id);
				Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");
				actual_money = actual_money.add(orderDetail.getTotal_price());

				for (Detail detail : orderDetail.getDetails()) {
					actual_skuMap.put(detail.getSku_id(), detail.getReal_quantity());
				}
			}
			Assert.assertEquals(actual_skuMap, skuMap, "下单后订单详细中展示的商品与预期的不一致");
		} catch (Exception e) {
			logger.error("下单后对数据进行验证遇到错误 ", e);
			Assert.fail("下单后对数据进行验证遇到错误 ", e);
		}
	}

	/**
	 * 商品加入常用
	 * 
	 */
	@Test
	public void bshopTestCase06() {
		try {
			String spu_id = bshopService.getRandomSpu();
			logger.info("随机获取到的SPU为 " + spu_id);
			JSONArray list_ids = new JSONArray();
			list_ids.add(1);
			boolean result = bshopService.addFavoriteSpu(list_ids, spu_id);
			Assert.assertEquals(result, true, "添加常用商品失败");

			list_ids = new JSONArray();
			result = bshopService.addFavoriteSpu(list_ids, spu_id);
			Assert.assertEquals(result, true, "取消常用商品失败");
		} catch (Exception e) {
			logger.error("设置常用商品遇到错误 ", e);
			Assert.fail("设置常用商品遇到错误 ", e);
		}
	}

	/**
	 * 修改商户信息
	 * 
	 */
	@Test
	public void bshopTestCase07() {
		try {
			boolean result = bshopService.editUserInfo();
			Assert.assertEquals(result, true, "修改用户信息失败");
		} catch (Exception e) {
			logger.error("修改用户信息遇到错误 ", e);
			Assert.fail("修改用户信息遇到错误 ", e);
		}
	}

	/**
	 * 添加删除子账号
	 * 
	 */
	@Test
	public void bshopTestCase08() {
		boolean result = true;
		try {
			result = bshopService.addSubaccount();
			Assert.assertEquals(result, true, "添加子账号失败");
		} catch (Exception e) {
			logger.error("添加子账号用户信息遇到错误", e);
			Assert.fail("添加子账号用户信息遇到错误 ", e);
		} finally {
			if (result) {
				try {
					result = bshopService.deleteSubaccount();
					Assert.assertEquals(result, true, "删除子账号失败");
				} catch (Exception e) {
					logger.error("删除子账号用户信息遇到错误 ", e);
					Assert.fail("删除子账号用户信息遇到错误 ", e);
				}
			}
		}
	}

	/**
	 * 修改账号密码
	 * 
	 */
	@Test
	public void bshopTestCase09() {
		try {
			int code = bshopService.changePassword();
			Assert.assertEquals(code, 1, "修改账号密码失败");
		} catch (Exception e) {
			logger.error("修改账号密码遇到错误 ", e);
			Assert.fail("修改账号密码遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase10() {
		try {
			Reporter.log("查看常用商品列表");
			boolean result = bshopService.favoriteList();
			Assert.assertEquals(result, true, "常用商品接口调用失败");
		} catch (Exception e) {
			logger.error("查看常用商品列表遇到错误 ", e);
			Assert.fail("查看常用商品列表遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase11() {
		try {
			Reporter.log("查看搜索热词");
			boolean result = bshopService.hotSearch();
			Assert.assertEquals(result, true, "热门搜索词接口调用失败");
		} catch (Exception e) {
			logger.error("热门搜索词接口调用遇到错误 ", e);
			Assert.fail("热门搜索词接口调用遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase12() {
		try {
			Reporter.log("查看营销活动列表");
			boolean result = bshopService.promotionList();
			Assert.assertEquals(result, true, "查看营销活动列表接口调用失败");
		} catch (Exception e) {
			logger.error("查看营销活动列表遇到错误 ", e);
			Assert.fail("查看营销活动列表遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase13() {
		try {
			Reporter.log("查看用户信息");
			boolean result = bshopService.userAccount();
			Assert.assertEquals(result, true, "用户信息接口调用失败");
		} catch (Exception e) {
			logger.error("用户信息接口调用遇到错误 ", e);
			Assert.fail("用户信息接口调用遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase14() {
		try {
			Reporter.log("检测报价单列表接口调用");
			JSONArray salemenuArray = bshopService.getSalemenuArray();
			Assert.assertEquals(salemenuArray.size() >= 0, true, "检测报价单列表接口调用失败");
		} catch (Exception e) {
			logger.error("检测报价单列表接口调用遇到错误 ", e);
			Assert.fail("检测报价单列表接口调用遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase15() {
		try {
			Reporter.log("查看未支付订单列表");
			boolean result = bshopService.unpayOrderList();
			Assert.assertEquals(result, true, "订单列表接口调用失败");
		} catch (Exception e) {
			logger.error("订单列表接口调用遇到错误 ", e);
			Assert.fail("订单列表接口调用遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase16() {
		try {
			Reporter.log("查看已支付订单列表");
			boolean result = bshopService.payOrderList();
			Assert.assertEquals(result, true, "订单列表调用失败");
		} catch (Exception e) {
			logger.error("订单列表接口调用遇到错误 ", e);
			Assert.fail("订单列表接口调用遇到错误 ", e);
		}

	}

	@Test
	public void bshopTestCase17() {
		try {
			Reporter.log("查看订单总数");
			boolean result = bshopService.orderCount();
			Assert.assertEquals(result, true, "查看订单总数接口调用失败");
		} catch (Exception e) {
			logger.error("查看订单总数遇到错误 ", e);
			Assert.fail("查看订单总数遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase18() {
		try {
			Reporter.log("测试点: 获取商品自定义配置信息");
			boolean result = bshopService.getHomepageCustomized();
			Assert.assertEquals(result, true, "获取商品自定义配置信息失败");
		} catch (Exception e) {
			logger.error("获取商品自定义配置信息遇到错误 ", e);
			Assert.fail("获取商品自定义配置信息遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase19() {
		try {
			Reporter.log("测试点: 获取营销活动商品");
			boolean result = bshopService.getPromotionSku();
			Assert.assertEquals(result, true, "获取营销活动商品失败");
		} catch (Exception e) {
			logger.error("获取营销活动商品遇到错误 ", e);
			Assert.fail("获取营销活动商品遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase20() {
		try {
			Reporter.log("测试点: 获取已领优惠券列表");
			List<CouponBean> couponList = bshopService.getAvailCouponList();
			Assert.assertNotEquals(couponList, null, "获取已领优惠券列表失败");
		} catch (Exception e) {
			logger.error("获取已领优惠券列表遇到错误 ", e);
			Assert.fail("获取已领优惠券列表遇到错误 ", e);
		}
	}

	@Test
	public void bshopTestCase21() {
		try {
			Reporter.log("测试点: 获取可见优惠券列表");
			List<CouponBean> couponList = bshopService.getVisibleCouponList();
			Assert.assertNotEquals(couponList, null, "获取可见优惠券列表列表失败");
		} catch (Exception e) {
			logger.error("获取可见优惠券列表列表遇到错误 ", e);
			Assert.fail("获取可见优惠券列表列表遇到错误 ", e);
		}
	}

}
