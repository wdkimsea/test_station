package cn.guanmai.bshop.marketing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.account.BsRegisterAreaBean;
import cn.guanmai.bshop.bean.account.param.BsAddressParam;
import cn.guanmai.bshop.bean.account.param.BsRegisterParam;
import cn.guanmai.bshop.bean.marketing.BsCouponAvailBean;
import cn.guanmai.bshop.bean.marketing.BsCouponVisibleBean;
import cn.guanmai.bshop.bean.order.BsOrderDetailBean;
import cn.guanmai.bshop.bean.order.BsOrderResultBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.impl.BsAccountServiceImpl;
import cn.guanmai.bshop.impl.BsCouponServiceImpl;
import cn.guanmai.bshop.impl.BsOrderServiceImpl;
import cn.guanmai.bshop.service.BsAccountService;
import cn.guanmai.bshop.service.BsCouponService;
import cn.guanmai.bshop.service.BsOrderService;
import cn.guanmai.bshop.tools.LoginBshop;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.marketing.CouponAddressBean;
import cn.guanmai.station.bean.marketing.CouponBean;
import cn.guanmai.station.bean.marketing.param.CouponFilterParam;
import cn.guanmai.station.bean.marketing.param.CouponParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.weight.param.OutOfStockParam;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.marketing.CouponServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.interfaces.marketing.CouponService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午3:33:51
 * @description: 优惠券使用相关测试用例
 * @version: 1.0
 */

public class BsCouponUsageTest extends LoginBshop {
	private Logger logger = LoggerFactory.getLogger(BsCouponUsageTest.class);
	private BsAccountService bsAccountService;
	private BsCouponService bsCouponService;
	private BsOrderService bsOrderService;

	// Station业务相关接口
	private CouponService couponService;
	private OrderService orderService;
	private WeightService weightService;
	private RefundStockService refundService;
	private LoginUserInfoService loginUserInfoService;

	// Manage业务相关接口
	private MgFinanceService financeService;
	private MgCustomerService mgCustomerService;

	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String address_id;

	@BeforeClass
	public void initData() {
		Map<String, String> bs_headers = getBshopCookie();
		bsAccountService = new BsAccountServiceImpl(bs_headers);
		bsCouponService = new BsCouponServiceImpl(bs_headers);
		bsOrderService = new BsOrderServiceImpl(bs_headers);
		try {
			BsAccountBean bsAccount = bsAccountService.getAccountInfo();
			Assert.assertNotEquals(bsAccount, null, "获取BSHOP登录账号信息失败");

			address_id = getAddressId();

			Map<String, String> st_headers = LoginUtil.loginStation();
			Assert.assertNotEquals(st_headers, null, "Station站点登录报错");

			couponService = new CouponServiceImpl(st_headers);
			orderService = new OrderServiceImpl(st_headers);
			weightService = new WeightServiceImpl(st_headers);
			refundService = new RefundStockServiceImpl(st_headers);
			loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);

			Map<String, String> ma_headers = LoginUtil.loginManage();
			Assert.assertNotEquals(ma_headers, null, "信息平台登录失败");

			financeService = new MgFinanceServiceImpl(ma_headers);
			mgCustomerService = new MgCustomerServiceImpl(ma_headers);

		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		// 查询是否有部分支付的订单
		try {
			List<String> order_ids = bsOrderService.getPartPayOrders();
			Assert.assertNotEquals(order_ids, null, "查询差额订单列表失败");
			if (order_ids.size() > 0) {
				boolean result = bsOrderService.payOrder(order_ids);
				Assert.assertEquals(result, true, "订单支付失败");
			}
		} catch (Exception e) {
			logger.error("查询差额订单列表遇到错误: ", e);
			Assert.fail("查询差额订单列表遇到错误: ", e);
		}
	}

	public String createCoupon() throws Exception {
		CouponParam couponParam = new CouponParam();
		String name = "AT-" + StringUtil.getRandomNumber(4);
		couponParam.setName(name);
		couponParam.setPrice_value(new BigDecimal("1"));
		couponParam.setType(1);
		couponParam.setMin_total_price(new BigDecimal("8"));
		couponParam.setMax_discount_percent(new BigDecimal("75"));
		couponParam.setAudience_type(1);
		couponParam.setValidity_day(3);
		couponParam.setIs_active(1);
		couponParam.setCollect_limit(2);

		String coupon_id = couponService.createCoupon(couponParam);
		Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

		return coupon_id;
	}

	@Test
	public void couponUsageTestCase01() {
		ReporterCSS.title("测试点: 新建优惠券,检测商城商户是否可见");
		try {
			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("1"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("10"));
			couponParam.setMax_discount_percent(new BigDecimal("90"));
			couponParam.setAudience_type(1);
			couponParam.setValidity_day(10);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(10);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

			List<BsCouponVisibleBean> bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			BsCouponVisibleBean bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(bsCoupon, null, "新建的优惠券,BSHOP登录账号没有找到");

			boolean result = true;
			String msg = null;
			if (!bsCoupon.getName().equals(couponParam.getName())) {
				msg = String.format("新建优惠券填写的名称与商城看到的名称不一致,预期:%s,实际:%s", couponParam.getName(), bsCoupon.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (bsCoupon.getCollect_limit() != couponParam.getCollect_limit()) {
				msg = String.format("新建优惠券填写的限领数与商城看到的名称不一致,预期:%s,实际:%s", couponParam.getCollect_limit(),
						bsCoupon.getCollect_limit());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (bsCoupon.getMin_total_price().compareTo(couponParam.getMin_total_price()) != 0) {
				msg = String.format("新建优惠券填写的使用条件与商城看到的名称不一致,预期:%s,实际:%s", couponParam.getMin_total_price(),
						bsCoupon.getMin_total_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (bsCoupon.getPrice_value().compareTo(couponParam.getPrice_value()) != 0) {
				msg = String.format("新建优惠券填写的单张面值与商城看到的名称不一致,预期:%s,实际:%s", couponParam.getPrice_value(),
						bsCoupon.getPrice_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的优惠券和在商城看到信息不一致");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase02() {
		ReporterCSS.title("测试点: 修改优惠券状态,查看商户是否可见");
		try {
			String coupon_id = createCoupon();
			boolean result = couponService.editCouponStatus(coupon_id, 0);
			Assert.assertEquals(result, true, "修改优惠券状态失败");

			List<BsCouponVisibleBean> bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			BsCouponVisibleBean bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny()
					.orElse(null);
			Assert.assertEquals(bsCoupon, null, "优惠券状态已修改为无效,BSHOP登录账号应该看不到此优惠券");

			ReporterCSS.title("测试点: 把优惠券状态修改为有效,查看商户是否可见");
			boolean resutl = couponService.editCouponStatus(coupon_id, 1);
			Assert.assertEquals(resutl, true, "修改优惠券状态为有效失败");

			bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny().orElse(null);
			Assert.assertNotEquals(bsCoupon, null, "优惠券状态已修改为有效,BSHOP登录账号应该能看到此优惠券");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase03() {
		ReporterCSS.title("测试点: 新建优惠券,面向指定商户");
		try {
			List<BigDecimal> kids = new ArrayList<BigDecimal>();
			kids.add(new BigDecimal(address_id));

			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("1"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("10"));
			couponParam.setMax_discount_percent(new BigDecimal("90"));
			couponParam.setAudience_type(4);
			couponParam.setValidity_day(10);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(10);
			couponParam.setKids(kids);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

			List<BsCouponVisibleBean> bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			BsCouponVisibleBean bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny()
					.orElse(null);
			Assert.assertEquals(bsCoupon, null, "新建的部分商户可见优惠券,目标商户没有看到此优惠券");

			List<CouponAddressBean> couponAddressList = couponService.searchCouponAddress();
			Assert.assertNotEquals(couponAddressList, null, "拉取商户列表失败");
			Assert.assertEquals(couponAddressList.size() > 0, true, "商户列表为空,无法建立面向指定商户的优惠券");

			couponAddressList = NumberUtil.roundNumberInList(couponAddressList, 6);
			kids = couponAddressList.stream().map(c -> c.getAddress_id()).filter(c -> !c.toString().equals(address_id))
					.collect(Collectors.toList());

			name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setKids(kids);
			String other_coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

			bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(other_coupon_id)).findAny().orElse(null);
			Assert.assertEquals(bsCoupon, null, "新建的部分商户可见优惠券(目标商户不在此范围中),目标商户应该看不到此优惠券");
		} catch (Exception e) {
			logger.error("新建优惠券出现错误: ", e);
			Assert.fail("新建优惠券出现错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase04() {
		ReporterCSS.title("测试点: 优惠券领取后查看可用优惠券列表");
		try {
			String coupon_id = createCoupon();
			String id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(id, null, "优惠券领取失败");

			List<BsCouponAvailBean> bsCoupons = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可用优惠券列表失败");

			BsCouponAvailBean bsCouponAvail = bsCoupons.stream().filter(c -> c.getId().equals(id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(bsCouponAvail, null, "可用优惠券列表,没有找到刚刚领取的优惠券");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase05() {
		ReporterCSS.title("测试点: 优惠券置为无效后再领取");
		try {
			String coupon_id = createCoupon();
			boolean result = couponService.editCouponStatus(coupon_id, 0);
			Assert.assertEquals(result, true, "修改优惠券状态失败");

			String id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertEquals(id, null, "优惠券置为无效后再领取,领取预期失败");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase06() {
		ReporterCSS.title("测试点: 优惠券领取超出限领数");
		try {
			String coupon_id = createCoupon();
			List<BsCouponVisibleBean> bsCoupons = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(bsCoupons, null, "获取可见优惠券列表失败");

			BsCouponVisibleBean bsCoupon = bsCoupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(bsCoupon, null, "新建的优惠券没有在商城显示");

			int pending_collect_num = bsCoupon.getPending_collect_num();
			String id = null;
			for (int i = 0; i <= pending_collect_num; i++) {
				id = bsCouponService.collectCoupon(coupon_id);
				if (i >= pending_collect_num) {
					Assert.assertEquals(id, null, "商户领取的优惠券已经超出此优惠券的限领数,预期领取失败");
					break;
				} else {
					Assert.assertNotEquals(id, null, "商户领取的优惠券在此优惠券的限领数范围内,预期领取成功");
				}
			}
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase07() {
		ReporterCSS.title("测试点: 下单使用未满足使用条件的优惠券");
		try {
			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("1"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("100000"));
			couponParam.setMax_discount_percent(new BigDecimal("100"));
			couponParam.setAudience_type(1);
			couponParam.setValidity_day(10);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(2);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券信息失败");

			String avail_coupon_id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(avail_coupon_id, null, "领取优惠券失败");

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 6);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertEquals(bsOrderResult.getMsg(), "订单不满足满减条件，请重新选择", "订单不满足优惠券的使用条件,强行使用,实际结果与预期不一致");

			Assert.assertEquals(bsOrderResult.getExtender().getOrder_id(), null, "订单不满足优惠券的使用条件,强行使用,订单不应该创建出来");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase08() {
		ReporterCSS.title("测试点: 同一优惠券尝试多次使用");
		try {
			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("1"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("2"));
			couponParam.setMax_discount_percent(new BigDecimal("50"));
			couponParam.setAudience_type(1);
			couponParam.setValidity_day(10);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(1);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券信息失败");

			String avail_coupon_id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(avail_coupon_id, null, "领取优惠券失败");

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 6);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertEquals(bsOrderResult.getCode(), 0, "使用优惠券订单提交失败");

			products = bsOrderService.searchProducts("b");
			selectedProducts = NumberUtil.roundNumberInList(products, 6);
			result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertEquals(bsOrderResult.getMsg(), "优惠券被使用，请重新选择", "同一优惠券尝试多次使用,实际结果与预期不一致");

			Assert.assertEquals(bsOrderResult.getExtender().getOrder_id(), null, "同一优惠券尝试多次使用,订单不应该创建出来");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase09() {
		ReporterCSS.title("测试点: 提交订单的时候使用别人的优惠券");
		try {

			String avail_coupon_id = "1333454";

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 6);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertEquals(bsOrderResult.getMsg(), "错误的优惠券", "提交订单的时候使用别人的优惠券,实际结果与预期不一致");

			Assert.assertEquals(bsOrderResult.getExtender().getOrder_id(), null, "同一优惠券尝试多次使用,订单不应该创建出来");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase10() {
		ReporterCSS.title("测试点: 下单使用优惠券");
		try {
			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("2"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("4"));
			couponParam.setMax_discount_percent(new BigDecimal("50"));
			couponParam.setAudience_type(1);
			couponParam.setValidity_day(10);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(1);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券信息失败");

			String avail_coupon_id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(avail_coupon_id, null, "领取优惠券失败");

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 6);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			BsOrderDetailBean bsOrderDetail = bsOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(bsOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			String msg = null;
			if (bsOrderDetail.getCoupon_amount().compareTo(couponParam.getPrice_value()) != 0) {
				msg = String.format("使用优惠券创建订单后,订单详情里的优惠券金额与预期不一致,预期:%s,实际:%s", couponParam.getPrice_value(),
						bsOrderDetail.getCoupon_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			BigDecimal expeted_total_price = bsOrderDetail.getCoupon_amount().add(bsOrderDetail.getTotal_pay());
			if (expeted_total_price.compareTo(bsOrderDetail.getTotal_price()) != 0) {
				msg = String.format("使用优惠券创建订单后,(应付金额+优惠金额)与订单总额不一致,预期:%s,实际:%s", expeted_total_price,
						bsOrderDetail.getTotal_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "使用优惠券创建订单后,相关金额与预期不一致");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	/**
	 * 获取下单可用优惠券ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAvailCoupon() throws Exception {
		List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
		Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

		BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getStatus() == 1
				&& c.getDays_remaining() > 0 && c.getMin_total_price().compareTo(new BigDecimal("10")) <= 0).findFirst()
				.orElse(null);

		String avail_coupon_id = null;
		if (couponAvail == null) {
			List<BsCouponVisibleBean> couponVisibles = bsCouponService.getVisibleCoupons();
			Assert.assertNotEquals(couponVisibles, null, "获取可领优惠券列表失败");
			BsCouponVisibleBean couponVisible = couponVisibles.stream().filter(c -> c.getAudience_type() == 1
					&& c.getPending_collect_num() > 0 && c.getMin_total_price().compareTo(new BigDecimal("10")) <= 0)
					.findFirst().orElse(null);
			String coupon_id = null;
			if (couponVisible == null) {
				coupon_id = createCoupon();
			} else {
				coupon_id = couponVisible.getId();
			}
			avail_coupon_id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(avail_coupon_id, null, "优惠券领取失败");
		} else {
			avail_coupon_id = couponAvail.getId();
		}
		return avail_coupon_id;
	}

	@Test
	public void couponUsageTestCase11() {
		ReporterCSS.title("测试点: 使用优惠券下单后,订单称重设置缺货,查看优惠券退券");
		try {
			String avail_coupon_id = getAvailCoupon();

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 2);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			result = bsOrderService.payOrder(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "订单支付失败");

			result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + " 修改状态为分拣中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详细" + order_id + "信息失败");

			List<OrderDetailBean.Detail> details = orderDetail.getDetails();
			List<OutOfStockParam> outOfStockParams = new ArrayList<OutOfStockParam>();
			OutOfStockParam outOfStockParam = null;
			for (OrderDetailBean.Detail detail : details) {
				outOfStockParam = new OutOfStockParam(order_id, detail.getSku_id(), new BigDecimal("0"), 0);
				outOfStockParams.add(outOfStockParam);
			}
			result = weightService.outOfStock(outOfStockParams);
			Assert.assertEquals(result, true, "分拣称重失败");

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus(), 1,
					"使用了优惠券的订单称重设置缺货后,优惠券[" + couponAvail.getName() + "]的状态值没有恢复");

		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase12() {
		ReporterCSS.title("测试点: 使用优惠券下单后,进行整单退款,查看优惠券退券");
		try {
			String avail_coupon_id = getAvailCoupon();

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 2);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			result = bsOrderService.payOrder(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "订单支付失败");

			result = financeService.updateOrderLockStatus(Arrays.asList(order_id), 0);
			Assert.assertEquals(result, true, "订单 " + order_id + " 解锁失败");

			result = financeService.refundOrder(order_id, 2);
			Assert.assertEquals(result, true, "订单 " + order_id + " 整单退款失败");

			Thread.sleep(2000);

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus(), 1,
					"使用了优惠券的订单整单退款后,优惠券[" + couponAvail.getName() + "]的状态值没有恢复");

		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase13() {
		ReporterCSS.title("测试点: 使用优惠券下单后,进行订单修改操作,查看优惠券退券");
		try {
			String avail_coupon_id = getAvailCoupon();

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);

			BigDecimal min_total_price = couponAvail.getMin_total_price();

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 2);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			result = bsOrderService.payOrder(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "订单支付失败");

			result = financeService.updateOrderLockStatus(Arrays.asList(order_id), 0);
			Assert.assertEquals(result, true, "订单 " + order_id + " 解锁失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			OrderEditParam orderEditParam = new OrderEditParam();
			orderEditParam.setOrder_id(order_id);

			OrderEditParam.OrderData orderData = orderEditParam.new OrderData();
			orderData.setReceive_begin_time(orderDetail.getCustomer().getReceive_begin_time());
			orderData.setReceive_end_time(orderDetail.getCustomer().getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			orderEditParam.setOrder_data(orderData);

			OrderDetailBean.Detail detail = orderDetail.getDetails().get(0);

			List<OrderCreateParam.OrderSku> details = new ArrayList<OrderCreateParam.OrderSku>();
			OrderCreateParam.OrderSku orderSku = new OrderCreateParam().new OrderSku();

			orderSku.setSku_id(detail.getSku_id());
			orderSku.setSpu_id(detail.getSpu_id());
			orderSku.setUnit_price(min_total_price.subtract(new BigDecimal("0.01")));
			orderSku.setIs_price_timing(0);
			orderSku.setSalemenu_id(detail.getSalemenu_id());
			orderSku.setAmount(new BigDecimal("1"));
			orderSku.setSpu_remark("");
			details.add(orderSku);

			orderEditParam.setDetails(details);

			result = orderService.editOrder(orderEditParam);
			Assert.assertEquals(result, true, "修改订单失败");

			Thread.sleep(2000);

			couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id)).findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus(), 1,
					"使用了优惠券的订单整单退款后,优惠券[" + couponAvail.getName() + "]的状态值没有恢复");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase14() {
		ReporterCSS.title("测试点: 使用优惠券下单后,进行订单商品异常编辑,查看优惠券退券");
		try {
			String avail_coupon_id = getAvailCoupon();

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 2);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			result = bsOrderService.payOrder(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "订单支付失败");

			result = financeService.updateOrderLockStatus(Arrays.asList(order_id), 0);
			Assert.assertEquals(result, true, "订单 " + order_id + " 解锁失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<OrderExceptionParam> orderExceptions = new ArrayList<OrderExceptionParam>();
			OrderExceptionParam orderExceptionParam = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				orderExceptionParam = new OrderExceptionParam(new BigDecimal("0"), detail.getSku_id(), 1, 1, 2);
				orderExceptions.add(orderExceptionParam);
			}

			result = orderService.addOrderException(order_id, orderExceptions, new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加异常条目信息失败");

			Thread.sleep(2000);

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus(), 1,
					"使用了优惠券的订单商户异常后,优惠券[" + couponAvail.getName() + "]的状态值没有恢复");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase15() {
		ReporterCSS.title("测试点: 使用优惠券下单后,进行订单商品退货,查看优惠券退券");
		try {
			String avail_coupon_id = getAvailCoupon();

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 2);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();

			result = bsOrderService.payOrder(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "订单支付失败");

			result = financeService.updateOrderLockStatus(Arrays.asList(order_id), 0);
			Assert.assertEquals(result, true, "订单 " + order_id + " 解锁失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号的信息失败");
			String station_id = loginUserInfo.getStation_id();

			List<OrderRefundParam> orderRefundParams = new ArrayList<OrderRefundParam>();
			OrderRefundParam orderRefundParam = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				orderRefundParam = new OrderRefundParam(detail.getStd_unit_quantity(), detail.getSku_id(), station_id,
						1, 1);
				orderRefundParams.add(orderRefundParam);
			}

			result = orderService.addOrderException(order_id, new ArrayList<>(), orderRefundParams);
			Assert.assertEquals(result, true, "订单添加退货信息失败");

			RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
			refundFilterParam.setDate_from(todayStr);
			refundFilterParam.setDate_end(todayStr);
			refundFilterParam.setOrder_id(order_id);

			List<RefundStockResultBean> refundResults = refundService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundResults, null, "获取商户退货条目信息失败");

			List<RefundStockParam> refundList = new ArrayList<RefundStockParam>();
			RefundStockParam stockRefundParam = null;
			for (RefundStockResultBean refundResult : refundResults) {
				stockRefundParam = new RefundStockParam();
				stockRefundParam.setRefund_id(refundResult.getRefund_id());
				stockRefundParam.setSolution(157);
				stockRefundParam.setSku_name(refundResult.getSku_name());
				stockRefundParam.setSku_id(refundResult.getSku_id());
				stockRefundParam.setDriver_id(0);
				stockRefundParam.setIn_stock_price(new BigDecimal("0"));
				stockRefundParam.setDisabled_in_stock_price(true);
				stockRefundParam.setReal_amount(refundResult.getRequest_amount());
				stockRefundParam.setStore_amount(new BigDecimal("0"));
				stockRefundParam.setRequest_amount(new BigDecimal("0"));
				stockRefundParam.setDescription("自动化测试");
				stockRefundParam.setShelf_name(null);
				stockRefundParam.setShelf_id(null);
				stockRefundParam.setSupplier_id(null);
				stockRefundParam.setSupplier_name(null);
				stockRefundParam.setSale_ratio(refundResult.getSale_ratio());
				stockRefundParam.setPurchase_sku_id(refundResult.getPurchase_sku_id());

				refundList.add(stockRefundParam);
			}

			result = refundService.editRefundStock(refundList);
			Assert.assertEquals(result, true, "商户售后处理,放弃取货失败");

			Thread.sleep(2000);

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus(), 1,
					"使用了优惠券的订单进行商户退货后,优惠券[" + couponAvail.getName() + "]的状态值没有恢复");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase16() {
		ReporterCSS.title("测试点: 先款后货商户,新建订单使用优惠券不支付,自动关闭后查看退券");
		try {
			CouponParam couponParam = new CouponParam();
			String name = "AT-" + StringUtil.getRandomNumber(4);
			couponParam.setName(name);
			couponParam.setPrice_value(new BigDecimal("2"));
			couponParam.setType(1);
			couponParam.setMin_total_price(new BigDecimal("12"));
			couponParam.setMax_discount_percent(new BigDecimal("83.33"));
			couponParam.setAudience_type(1);
			couponParam.setValidity_day(3);
			couponParam.setIs_active(1);
			couponParam.setCollect_limit(1);

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

			String avail_coupon_id = bsCouponService.collectCoupon(coupon_id);
			Assert.assertNotEquals(avail_coupon_id, null, "领取优惠券失败");

			List<BsProductBean> products = bsOrderService.searchProducts("a");
			List<BsProductBean> selectedProducts = NumberUtil.roundNumberInList(products, 6);
			boolean result = bsOrderService.updateCart(selectedProducts);
			Assert.assertEquals(result, true, "更新购物车失败");

			result = bsOrderService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, result, "设置付款方式失败");

			result = bsOrderService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			BsOrderResultBean bsOrderResult = bsOrderService.submitCart(avail_coupon_id);
			Assert.assertNotEquals(bsOrderResult, null, "提交订单失败");

			Assert.assertNotEquals(bsOrderResult.getExtender().getOrder_id(), null, "使用优惠券提交订单,订单未生成");

			String order_id = bsOrderResult.getExtender().getOrder_id();
			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单:" + order_id + "失败");

			List<BsCouponAvailBean> couponAvails = bsCouponService.getAvailCoupons();
			Assert.assertNotEquals(couponAvails, null, "获取可用优惠券列表失败");

			BsCouponAvailBean couponAvail = couponAvails.stream().filter(c -> c.getId().equals(avail_coupon_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(couponAvail, null, "订单下单使用过的优惠券,在可用优惠券列表没有找到");

			Assert.assertEquals(couponAvail.getStatus() == 1, true,
					"订单已经删除,请登录BSHOP查看优惠券[" + couponAvail.getName() + "]是否已经退券");
		} catch (Exception e) {
			logger.error("优惠券测试遇到错误: ", e);
			Assert.fail("优惠券测试遇到错误: ", e);
		}
	}

	@Test
	public void couponUsageTestCase99() {
		ReporterCSS.title("测试点: 新建平台自动发放优惠券");
		String address_id = null;
		try {
			CouponFilterParam couponFilterParam = new CouponFilterParam();
			couponFilterParam.setLimit(50);
			couponFilterParam.setOffset(0);
			couponFilterParam.setIs_active(1);
			couponFilterParam.setAudience_type(2);

			List<CouponBean> coupons = couponService.searchCoupon(couponFilterParam);
			Assert.assertNotEquals(coupons, null, "搜索过滤优惠券失败");

			if (coupons.size() == 0) {
				CouponParam couponParam = new CouponParam();
				String name = "New-" + StringUtil.getRandomNumber(4);
				couponParam.setName(name);
				couponParam.setPrice_value(new BigDecimal("2"));
				couponParam.setType(1);
				couponParam.setMin_total_price(new BigDecimal("10"));
				couponParam.setMax_discount_percent(new BigDecimal("80"));
				couponParam.setAudience_type(2);
				couponParam.setValidity_day(365);
				couponParam.setIs_active(1);
				couponParam.setCollect_limit(2);
				couponParam.setDescription(StringUtil.getRandomNumber(12));
				couponParam.setRegister_after(todayStr);

				String coupon_id = couponService.createCoupon(couponParam);
				Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

				coupons = couponService.searchCoupon(couponFilterParam);
				Assert.assertNotEquals(coupons, null, "搜索过滤优惠券失败");

				CouponBean coupon = coupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny().orElse(null);
				Assert.assertNotEquals(coupon, null, "新建的优惠券在列表中没有找到");

			}

			String username = "TMP" + TimeUtil.getLongTime();
			BsRegisterParam bsRegisterParam = new BsRegisterParam();
			bsRegisterParam.setUsername(username);
			bsRegisterParam.setPassword("1qaz2wsx");
			bsRegisterParam.setCustomer_type(1);
			bsRegisterParam.setIs_bind_wechat(0);
			bsRegisterParam.setType(1);

			boolean result = bsAccountService.register(bsRegisterParam);
			Assert.assertEquals(result, true, "注册BSHOP账户失败");

			Map<String, String> bs_headers = LoginUtil.loginBshop(username, "1qaz2wsx");
			Assert.assertNotEquals(bs_headers, null, "新注册的账号登录失败");

			BsAccountService newBsAccountService = new BsAccountServiceImpl(bs_headers);
			List<BsRegisterAreaBean> bsRegisterAreaList = newBsAccountService.getRegisterArea();
			Assert.assertNotEquals(bsRegisterAreaList, null, "获取地理标签信息失败");

			String area = null;
			OK: for (BsRegisterAreaBean bsRegisterArea : bsRegisterAreaList) {
				if (bsRegisterArea.getArea_level1_list().size() > 0) {
					for (BsRegisterAreaBean.AreaLevel1 areaLevel1 : bsRegisterArea.getArea_level1_list()) {
						if (areaLevel1.getArea_level2_list().size() > 0) {
							area = areaLevel1.getArea_level2_list().get(0).getArea2_id();
							break OK;
						}
					}
				}
			}
			Assert.assertNotEquals(area, null, "没有可用的地理标签");

			BsAddressParam bsAddressParam = new BsAddressParam();
			bsAddressParam.setResname(username);
			bsAddressParam.setName(username);
			bsAddressParam.setArea(area);
			bsAddressParam.setTelephone("110" + StringUtil.getRandomNumber(8));

			result = newBsAccountService.addAddress(bsAddressParam);
			Assert.assertEquals(result, true, "注册BSHOP账号新增店铺失败");

			BsAccountBean bsAccount = newBsAccountService.getAccountInfo();
			Assert.assertNotEquals(bsAccount, null, "获取登录账号信息失败");

			address_id = bsAccount.getAddresses().get(0).getSid();

			Thread.sleep(1000);
			BsCouponService newBsCouponService = new BsCouponServiceImpl(bs_headers);
			List<BsCouponAvailBean> bsCouponAvails = newBsCouponService.getAvailCoupons();
			Assert.assertNotEquals(bsCouponAvails, null, "获取可用优惠券列表失败");

			String msg = null;
			for (CouponBean coupon : coupons) {
				String coupon_name = coupon.getName();
				int collect_limit = coupon.getCollect_limit();

				long coupon_count = bsCouponAvails.stream().filter(s -> s.getName().equals(coupon_name)).count();

				if (coupon_count < collect_limit) {
					msg = String.format("新用户专享券:%s,没有发给新用户", coupon_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "新用户专享券,没有自动发给新商户");
		} catch (Exception e) {
			logger.error("优惠券新用户专享券测试遇到错误: ", e);
			Assert.fail("优惠券新用户专享券测试遇到错误: ", e);
		} finally {
			if (address_id != null) {
				try {
					address_id = address_id.replaceAll("S0*", "");
					boolean result = mgCustomerService.deleteCustomter(address_id);
					Assert.assertEquals(result, true, "删除商户失败");
				} catch (Exception e) {
					logger.error("删除商户遇到错误: ", e);
					Assert.fail("删除商户遇到错误: ", e);
				}
			}
		}
	}

	@AfterClass
	public void afterClass() {
		logger.info("后置处理: 把新建的优惠券置于无效");
		try {
			CouponFilterParam couponFilterParam = new CouponFilterParam();
			couponFilterParam.setQ("AT-");
			couponFilterParam.setIs_active(1);
			couponFilterParam.setOffset(0);
			couponFilterParam.setLimit(50);
			List<CouponBean> coupons = couponService.searchCoupon(couponFilterParam);
			Assert.assertNotEquals(coupons, null, "搜索过滤优惠券列表失败");

			boolean result = true;
			for (CouponBean coupon : coupons) {
				if (coupon.getCreate_time().startsWith(todayStr)) {
					result = couponService.editCouponStatus(coupon.getId(), 0);
					Assert.assertEquals(result, true, "修改优惠券: " + coupon.getName() + " 状态失败");
				}
			}
		} catch (Exception e) {
			logger.error("修改优惠券遇到错误: ", e);
			Assert.fail("修改优惠券遇到错误: ", e);
		}
	}

}
