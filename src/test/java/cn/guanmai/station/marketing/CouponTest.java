package cn.guanmai.station.marketing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.marketing.CouponAddressBean;
import cn.guanmai.station.bean.marketing.CouponBean;
import cn.guanmai.station.bean.marketing.CouponDetailBean;
import cn.guanmai.station.bean.marketing.CouponPageBean;
import cn.guanmai.station.bean.marketing.CouponUsageBean;
import cn.guanmai.station.bean.marketing.param.CouponFilterParam;
import cn.guanmai.station.bean.marketing.param.CouponParam;
import cn.guanmai.station.bean.marketing.param.CouponUsageFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.marketing.CouponServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.marketing.CouponService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午7:33:02
 * @description:
 * @version: 1.0
 */

public class CouponTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(CouponTest.class);
	private CouponService couponService;
	private AsyncService asyncService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		couponService = new CouponServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@Test
	public void couponTestCase01() {
		ReporterCSS.title("测试点: 新建优惠券,面向全部商户");
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
			couponParam.setDescription(StringUtil.getRandomNumber(12));

			String coupon_id = couponService.createCoupon(couponParam);
			Assert.assertNotEquals(coupon_id, null, "新建优惠券失败");

			CouponFilterParam couponFilterParam = new CouponFilterParam();
			List<CouponBean> coupons = couponService.searchCoupon(couponFilterParam);

			Assert.assertNotEquals(coupons, null, "搜索过滤优惠券失败");

			CouponBean coupon = coupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny().orElse(null);
			Assert.assertNotEquals(coupon, null, "新建的优惠券没有搜索到");

			String msg = null;
			boolean result = true;
			if (!couponParam.getName().equals(coupon.getName())) {
				msg = String.format("新建的优惠券名称和查询到的不一致,预期:%s,实际:%s", couponParam.getName(), coupon.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getPrice_value().compareTo(coupon.getPrice_value()) != 0) {
				msg = String.format("新建的优惠券面值和查询到的不一致,预期:%s,实际:%s", couponParam.getPrice_value(),
						coupon.getPrice_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getType() != coupon.getType()) {
				msg = String.format("新建的优惠券类型和查询到的不一致,预期:%s,实际:%s", couponParam.getType(), coupon.getType());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getMin_total_price().compareTo(coupon.getMin_total_price()) != 0) {
				msg = String.format("新建的优惠券满减使用额度和查询到的不一致,预期:%s,实际:%s", couponParam.getMin_total_price(),
						coupon.getMin_total_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getAudience_type() != coupon.getAudience_type()) {
				msg = String.format("新建的优惠券满audience_type对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getAudience_type(),
						coupon.getAudience_type());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getValidity_day() != coupon.getValidity_day()) {
				msg = String.format("新建的优惠券有效天数对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getValidity_day(),
						coupon.getValidity_day());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getIs_active() != coupon.getIs_active()) {
				msg = String.format("新建的优惠券状态值对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getIs_active(),
						coupon.getIs_active());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getCollect_limit() != coupon.getCollect_limit()) {
				msg = String.format("新建的优惠券限领数对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getCollect_limit(),
						coupon.getCollect_limit());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			couponFilterParam = new CouponFilterParam();
			couponFilterParam.setQ(name);
			coupons = couponService.searchCoupon(couponFilterParam);
			if (coupons == null) {
				msg = String.format("按优惠券名称搜索过滤优惠券失败");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				coupon = coupons.stream().filter(s -> s.getId().equals(coupon_id)).findAny().orElse(null);
				if (coupon == null) {
					msg = String.format("按优惠券名称搜索过滤优惠券,没有找到新建的优惠券" + name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<String> names = coupons.stream().filter(c -> !c.getName().contains(name)).map(c -> c.getName())
						.collect(Collectors.toList());

				if (names.size() > 0) {
					msg = String.format("按优惠券名称搜索过滤优惠券,过滤出了不符合过滤条件的优惠券 :" + names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			CouponDetailBean couponDetail = couponService.getCouponDetail(coupon_id);
			Assert.assertNotEquals(couponDetail, null, "获取优惠券详细信息失败");

			if (!couponDetail.getDescription().equals(couponParam.getDescription())) {
				msg = String.format("新建的优惠券使用说明对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getDescription(),
						couponDetail.getDescription());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的优惠券信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建优惠券出现错误: ", e);
			Assert.fail("新建优惠券出现错误: ", e);
		}
	}

	@Test
	public void couponTestCase02() {
		ReporterCSS.title("测试点: 新建优惠券,面向指定商户");
		try {
			List<CouponAddressBean> couponAddressList = couponService.searchCouponAddress();
			Assert.assertNotEquals(couponAddressList, null, "拉取商户列表失败");
			Assert.assertEquals(couponAddressList.size() > 0, true, "商户列表为空,无法建立面向指定商户的优惠券");

			couponAddressList = NumberUtil.roundNumberInList(couponAddressList, 3);
			List<BigDecimal> kids = couponAddressList.stream().map(c -> c.getAddress_id()).collect(Collectors.toList());

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

			CouponDetailBean couponDetail = couponService.getCouponDetail(coupon_id);
			Assert.assertNotEquals(couponDetail, null, "新建的优惠券没有搜索到");

			String msg = null;
			boolean result = true;

			if (!couponParam.getName().equals(couponDetail.getName())) {
				msg = String.format("新建的优惠券名称和查询到的不一致,预期:%s,实际:%s", couponParam.getName(), couponDetail.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getPrice_value().compareTo(couponDetail.getPrice_value()) != 0) {
				msg = String.format("新建的优惠券面值和查询到的不一致,预期:%s,实际:%s", couponParam.getPrice_value(),
						couponDetail.getPrice_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getType() != couponDetail.getType()) {
				msg = String.format("新建的优惠券类型和查询到的不一致,预期:%s,实际:%s", couponParam.getType(), couponDetail.getType());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getMin_total_price().compareTo(couponDetail.getMin_total_price()) != 0) {
				msg = String.format("新建的优惠券满减使用额度和查询到的不一致,预期:%s,实际:%s", couponParam.getMin_total_price(),
						couponDetail.getMin_total_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getAudience_type() != couponDetail.getAudience_type()) {
				msg = String.format("新建的优惠券满audience_type对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getAudience_type(),
						couponDetail.getAudience_type());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getValidity_day() != couponDetail.getValidity_day()) {
				msg = String.format("新建的优惠券有效天数对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getValidity_day(),
						couponDetail.getValidity_day());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getIs_active() != couponDetail.getIs_active()) {
				msg = String.format("新建的优惠券状态值与对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getIs_active(),
						couponDetail.getIs_active());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponParam.getCollect_limit() != couponDetail.getCollect_limit()) {
				msg = String.format("新建的优惠券限领数与对应的值和查询到的不一致,预期:%s,实际:%s", couponParam.getCollect_limit(),
						couponDetail.getCollect_limit());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (couponDetail.getKids() != null) {
				if (!couponParam.getKids().equals(couponDetail.getKids())) {
					msg = String.format("新建的优惠券绑定的商户列表与预期不一致,预期:%s,实际:%s", couponParam.getKids(),
							couponDetail.getKids());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("新建的优惠券绑定的商户列表为空,与预期结果不一致");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			CouponFilterParam couponFilterParam = new CouponFilterParam();
			couponFilterParam.setAudience_type(4);
			List<CouponBean> coupons = couponService.searchCoupon(couponFilterParam);
			if (coupons == null) {
				msg = String.format("按优惠券的可用范围搜索过滤失败");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				CouponBean coupon = coupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny().orElse(null);
				if (coupon == null) {
					msg = String.format("按优惠券的使用范围搜索过滤,没有找到新建的优惠券");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<Integer> audience_types = coupons.stream().filter(c -> c.getAudience_type() != 4)
						.map(c -> c.getAudience_type()).collect(Collectors.toList());
				if (audience_types.size() > 0) {
					msg = String.format("按优惠券的使用范围(4:指定商户)搜索过滤,搜索出了其他范围值 %s 的优惠券", audience_types);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "新建的优惠券信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建优惠券出现错误: ", e);
			Assert.fail("新建优惠券出现错误: ", e);
		}
	}

	@Test
	public void couponTestCase03() {
		ReporterCSS.title("测试点: 修改优惠券状态");
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

			boolean result = couponService.editCouponStatus(coupon_id, 0);
			Assert.assertEquals(result, true, "修改订单状态值失败");

			CouponDetailBean couponDetail = couponService.getCouponDetail(coupon_id);
			Assert.assertNotEquals(couponDetail, null, "新建的优惠券没有搜索到");

			String msg = null;
			if (couponDetail.getIs_active() != 0) {
				msg = String.format("优惠券状态值修改后,状态值与预期不符,预期:%s,实际:%s", 0, couponDetail.getIs_active());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			CouponFilterParam couponFilterParam = new CouponFilterParam();
			couponFilterParam.setIs_active(0);

			List<CouponBean> coupons = couponService.searchCoupon(couponFilterParam);
			if (coupons == null) {
				msg = String.format("按优惠券的状态值搜索过滤失败");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				CouponBean coupon = coupons.stream().filter(c -> c.getId().equals(coupon_id)).findAny().orElse(null);
				if (coupon == null) {
					msg = String.format("按优惠券的状态值搜索过滤,没有过滤出目标优惠券");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<Integer> audience_types = coupons.stream().filter(c -> c.getIs_active() != 0)
						.map(c -> c.getIs_active()).collect(Collectors.toList());
				if (audience_types.size() > 0) {
					msg = String.format("按优惠券的状态值(0:无效)搜索过滤,搜索出了其他状态值 %s 的优惠券", audience_types);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "优惠券状态修改后,状态值与预期不同");
		} catch (Exception e) {
			logger.error("修改优惠券状态出现错误: ", e);
			Assert.fail("修改优惠券状态出现错误: ", e);
		}
	}

	@Test
	public void couponTestCase04() {
		ReporterCSS.title("测试点: 优惠券列表翻页");
		try {
			CouponFilterParam couponFilterParam = new CouponFilterParam();
			couponFilterParam.setLimit(2);
			couponFilterParam.setOffset(0);

			CouponPageBean couponPage = couponService.searchCouponPage(couponFilterParam);
			Assert.assertNotEquals(couponPage, null, "优惠券列表搜索过滤失败");

			List<String> no1_data = couponPage.getCoupons().stream().map(c -> c.getId()).collect(Collectors.toList());

			CouponPageBean.Pagination pagination = couponPage.getPagination();
			boolean more = pagination.isMore();
			if (more) {
				couponFilterParam.setPage_obj(pagination.getPage_obj());
				couponPage = couponService.searchCouponPage(couponFilterParam);
				Assert.assertNotEquals(couponPage, null, "优惠券列表搜索过滤失败");

				List<String> no2_data = couponPage.getCoupons().stream().map(c -> c.getId())
						.collect(Collectors.toList());

				List<String> repeat_data = no1_data.stream().filter(n -> no2_data.contains(n))
						.collect(Collectors.toList());
				boolean result = true;
				if (repeat_data.size() > 0) {
					String msg = String.format("优惠券列表翻页,第二页的数据出现了第一页的数据:%s", repeat_data);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "优惠券列表翻页,显示的数据不正确");
			}
		} catch (Exception e) {
			logger.error("优惠券列表翻页出现错误: ", e);
			Assert.fail("优惠券列表翻页出现错误: ", e);
		}
	}

	@Test
	public void couponTestCase05() {
		ReporterCSS.title("测试点: 搜索过滤优惠券使用概况");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

			CouponUsageFilterParam couponUsageFilterParam = new CouponUsageFilterParam();
			couponUsageFilterParam.setCollect_begin_time(todayStr);
			couponUsageFilterParam.setCollect_end_time(todayStr);
			couponUsageFilterParam.setSearch_type(1);

			List<CouponUsageBean> couponUsageList = couponService.searchCouponUsage(couponUsageFilterParam);
			Assert.assertNotEquals(couponUsageList, null, "搜索过滤优惠券使用概况列表失败");

		} catch (Exception e) {
			logger.error("优惠券使用概况出现错误: ", e);
			Assert.fail("优惠券使用概况出现错误: ", e);
		}
	}

	@Test
	public void couponTestCase06() {
		ReporterCSS.title("测试点: 导出滤优惠券使用概况");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

			CouponUsageFilterParam couponUsageFilterParam = new CouponUsageFilterParam();
			couponUsageFilterParam.setCollect_begin_time(todayStr);
			couponUsageFilterParam.setCollect_end_time(todayStr);
			couponUsageFilterParam.setSearch_type(1);
			couponUsageFilterParam.setAsync(1);

			BigDecimal task_id = couponService.exportCouponUsage(couponUsageFilterParam);
			Assert.assertNotEquals(task_id, null, "导出滤优惠券使用概况失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出滤优惠券使用概况异步任务执行失败");
		} catch (Exception e) {
			logger.error("优惠券使用概况出现错误: ", e);
			Assert.fail("优惠券使用概况出现错误: ", e);
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

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

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
