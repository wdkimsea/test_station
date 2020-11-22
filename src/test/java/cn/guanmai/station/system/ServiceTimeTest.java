package cn.guanmai.station.system;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.param.ServiceTimeParam;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 8, 2018 11:35:00 AM 
* @des 运营时间
* @version 1.0 
*/
/**
 * 
 */
public class ServiceTimeTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ServiceTimeTest.class);

	private ServiceTimeService serviceTimeService;
	private OrderService orderService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
	}

	public void serviceTimeTestCase00() {
		try {
			// 用来测试收货时间取值的用例,不作为测试用例跑
			List<ServiceTimeBean> serviceTimes = serviceTimeService.allTimeConfig();
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");
			for (ServiceTimeBean serviceTime : serviceTimes) {
				if (serviceTime.getId().equals("ST6047")) {
					OrderReceiveTimeBean orderReceiveTime = orderService.getOrderReceiveTime(serviceTime);
					System.out.println(JsonUtil.objectToStr(orderReceiveTime));
					break;
				}
			}
		} catch (Exception e) {
			logger.error("获取服务时间详细信息遇到错误: ", e);
			Assert.fail("获取服务时间详细信息遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase01() {
		// 获取默认服务时间
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.allTimeConfig();
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");
		} catch (Exception e) {
			logger.error("获取服务时间列表遇到错误: ", e);
			Assert.fail("获取服务时间列表遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase02() {
		// 获取默认服务时间
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.allTimeConfig();
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");
			if (serviceTimes.size() > 0) {
				String time_config_id = serviceTimes.get(0).getId();
				ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
				Assert.assertNotEquals(serviceTime, null, "获取ID为" + time_config_id + "服务时间的详细信息失败");
			}
		} catch (Exception e) {
			logger.error("获取服务时间详细信息遇到错误: ", e);
			Assert.fail("获取服务时间详细信息遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase03() {
		ReporterCSS.title("测试点: 拉取运营时间列表(简)");
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(0);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");
		} catch (Exception e) {
			logger.error("拉取运营时间列表遇到错误: ", e);
			Assert.fail("拉取运营时间列表遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase04() {
		ReporterCSS.title("测试点: 拉取运营时间列表(详)");
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(1);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			String msg = null;
			boolean result = true;
			for (ServiceTimeBean serviceTime : serviceTimes) {
				if (serviceTime.getOrder_time_limit() == null) {
					msg = String.format("运营时间%s的下单时间信息为空", serviceTime.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (serviceTime.getReceive_time_limit() == null) {
					msg = String.format("运营时间%s的收货时间信息为空", serviceTime.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "运营时间列表(详细)拉取的信息不正确");
		} catch (Exception e) {
			logger.error("拉取运营时间列表遇到错误: ", e);
			Assert.fail("拉取运营时间列表遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase05() {
		ReporterCSS.title("测试点: 新建运营时间(普通运营时间)");
		try {
			String name = "AT-" + StringUtil.getRandomNumber(6);
			ServiceTimeParam serviceTimeParam = new ServiceTimeParam();
			serviceTimeParam.setName(name);
			serviceTimeParam.setDesc(StringUtil.getRandomString(12));
			serviceTimeParam.setFinal_distribute_time("06:30");
			serviceTimeParam.setFinal_distribute_time_span(1);

			ServiceTimeParam.OrderTimeLimit orderTimeLimit = serviceTimeParam.new OrderTimeLimit();
			orderTimeLimit.setE_span_time(0);
			orderTimeLimit.setStart("07:00");
			orderTimeLimit.setEnd("22:00");
			serviceTimeParam.setOrder_time_limit(orderTimeLimit);

			ServiceTimeParam.ReceiveTimeLimit receiveTimeLimit = serviceTimeParam.new ReceiveTimeLimit();
			receiveTimeLimit.setE_span_time(1);
			receiveTimeLimit.setStart("14:00");
			receiveTimeLimit.setEnd("20:00");
			receiveTimeLimit.setReceiveTimeSpan(30);
			receiveTimeLimit.setS_span_time(1);
			serviceTimeParam.setReceive_time_limit(receiveTimeLimit);

			boolean result = serviceTimeService.createServiceTime(serviceTimeParam);
			Assert.assertEquals(result, true, "新建运营时间(普通)失败");

			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(0);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			ServiceTimeBean serviceTime = serviceTimes.stream().filter(s -> s.getName().equals(name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(serviceTime, null, "新建的运营时间在运营时间列表没有找到");

			String id = serviceTime.getId();

			serviceTime = serviceTimeService.getServiceTime(id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

			String msg = null;
			if (!serviceTime.getDesc().equals(serviceTimeParam.getDesc())) {
				msg = String.format("新建的运营时间描述信息与预期不符,预期:%s,实际:%s", serviceTimeParam.getDesc(), serviceTime.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!serviceTime.getFinal_distribute_time().equals(serviceTimeParam.getFinal_distribute_time())) {
				msg = String.format("新建的运营时间字段(订单变为配送中时间)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time(), serviceTime.getFinal_distribute_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (serviceTime.getFinal_distribute_time_span() != serviceTimeParam.getFinal_distribute_time_span()) {
				msg = String.format("新建的运营时间字段(订单变为配送中时间天数)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time_span(), serviceTime.getFinal_distribute_time_span());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.OrderTimeLimit r_orderTimeLimit = serviceTime.getOrder_time_limit();
			if (!r_orderTimeLimit.getStart().equals(orderTimeLimit.getStart())) {
				msg = String.format("新建的运营时间字段(用户下单时间开始时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getStart(),
						r_orderTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_orderTimeLimit.getEnd().equals(orderTimeLimit.getEnd())) {
				msg = String.format("新建的运营时间字段(用户下单时间结束时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getEnd(),
						r_orderTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_orderTimeLimit.getE_span_time() != orderTimeLimit.getE_span_time()) {
				msg = String.format("新建的运营时间字段(用户下单时间是否跨天)与预期不符,预期:%s,实际:%s", orderTimeLimit.getE_span_time(),
						r_orderTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.ReceiveTimeLimit r_receiveTimeLimit = serviceTime.getReceive_time_limit();
			if (!r_receiveTimeLimit.getStart().equals(receiveTimeLimit.getStart())) {
				msg = String.format("新建的运营时间字段(收货开始时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getStart(),
						r_receiveTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_receiveTimeLimit.getEnd().equals(receiveTimeLimit.getEnd())) {
				msg = String.format("新建的运营时间字段(收货结束时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getEnd(),
						r_receiveTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getE_span_time() != receiveTimeLimit.getE_span_time()) {
				msg = String.format("新建的运营时间字段(收货时间天数是否跨天)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getE_span_time(),
						r_receiveTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getReceiveTimeSpan() != receiveTimeLimit.getReceiveTimeSpan()) {
				msg = String.format("新建的运营时间字段(收货时间间隔)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getReceiveTimeSpan(),
						r_receiveTimeLimit.getReceiveTimeSpan());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的运营时间结果与预期不符");
		} catch (Exception e) {
			logger.error("新建运营时间遇到错误: ", e);
			Assert.fail("新建运营时间遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase06() {
		ReporterCSS.title("测试点: 新建运营时间(预售运营时间)");
		try {
			String name = "AT-" + StringUtil.getRandomNumber(6);
			ServiceTimeParam serviceTimeParam = new ServiceTimeParam();
			serviceTimeParam.setName(name);
			serviceTimeParam.setDesc(StringUtil.getRandomString(12));
			serviceTimeParam.setFinal_distribute_time("06:00");
			serviceTimeParam.setFinal_distribute_time_span(0);
			serviceTimeParam.setType(2);

			ServiceTimeParam.OrderTimeLimit orderTimeLimit = serviceTimeParam.new OrderTimeLimit();
			orderTimeLimit.setE_span_time(0);
			orderTimeLimit.setStart("07:00");
			orderTimeLimit.setEnd("22:00");
			serviceTimeParam.setOrder_time_limit(orderTimeLimit);

			ServiceTimeParam.ReceiveTimeLimit receiveTimeLimit = serviceTimeParam.new ReceiveTimeLimit();
			receiveTimeLimit.setE_span_time(1);
			receiveTimeLimit.setStart("14:00");
			receiveTimeLimit.setEnd("20:00");
			receiveTimeLimit.setReceiveTimeSpan(30);
			receiveTimeLimit.setS_span_time(1);
			receiveTimeLimit.setE_span_time(4);
			receiveTimeLimit.setWeekdays(63);
			serviceTimeParam.setReceive_time_limit(receiveTimeLimit);

			boolean result = serviceTimeService.createServiceTime(serviceTimeParam);
			Assert.assertEquals(result, true, "新建运营时间(普通)失败");

			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(0);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			ServiceTimeBean serviceTime = serviceTimes.stream().filter(s -> s.getName().equals(name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(serviceTime, null, "新建的运营时间在运营时间列表没有找到");

			String id = serviceTime.getId();

			serviceTime = serviceTimeService.getServiceTime(id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

			String msg = null;
			if (!serviceTime.getDesc().equals(serviceTimeParam.getDesc())) {
				msg = String.format("新建的运营时间描述信息与预期不符,预期:%s,实际:%s", serviceTimeParam.getDesc(), serviceTime.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!serviceTime.getFinal_distribute_time().equals(serviceTimeParam.getFinal_distribute_time())) {
				msg = String.format("新建的运营时间字段(订单变为配送中时间)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time(), serviceTime.getFinal_distribute_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (serviceTime.getFinal_distribute_time_span() != serviceTimeParam.getFinal_distribute_time_span()) {
				msg = String.format("新建的运营时间字段(订单变为配送中时间天数)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time_span(), serviceTime.getFinal_distribute_time_span());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.OrderTimeLimit r_orderTimeLimit = serviceTime.getOrder_time_limit();
			if (!r_orderTimeLimit.getStart().equals(orderTimeLimit.getStart())) {
				msg = String.format("新建的运营时间字段(用户下单时间开始时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getStart(),
						r_orderTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_orderTimeLimit.getEnd().equals(orderTimeLimit.getEnd())) {
				msg = String.format("新建的运营时间字段(用户下单时间结束时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getEnd(),
						r_orderTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_orderTimeLimit.getE_span_time() != orderTimeLimit.getE_span_time()) {
				msg = String.format("新建的运营时间字段(用户下单时间是否跨天)与预期不符,预期:%s,实际:%s", orderTimeLimit.getE_span_time(),
						r_orderTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.ReceiveTimeLimit r_receiveTimeLimit = serviceTime.getReceive_time_limit();
			if (!r_receiveTimeLimit.getStart().equals(receiveTimeLimit.getStart())) {
				msg = String.format("新建的运营时间字段(收货开始时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getStart(),
						r_receiveTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_receiveTimeLimit.getEnd().equals(receiveTimeLimit.getEnd())) {
				msg = String.format("新建的运营时间字段(收货结束时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getEnd(),
						r_receiveTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getReceiveTimeSpan() != receiveTimeLimit.getReceiveTimeSpan()) {
				msg = String.format("新建的运营时间字段(收货时间间隔)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getReceiveTimeSpan(),
						r_receiveTimeLimit.getReceiveTimeSpan());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getWeekdays() != receiveTimeLimit.getWeekdays()) {
				msg = String.format("新建的运营时间字段(收货自然日)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getWeekdays(),
						r_receiveTimeLimit.getWeekdays());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getS_span_time() != receiveTimeLimit.getS_span_time()) {
				msg = String.format("新建的运营时间字段(用户可选最早收货天数)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getS_span_time(),
						r_receiveTimeLimit.getS_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getE_span_time() != receiveTimeLimit.getE_span_time()) {
				msg = String.format("新建的运营时间字段(用户可选最晚收货天数)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getE_span_time(),
						r_receiveTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的运营时间结果与预期不符");
		} catch (Exception e) {
			logger.error("新建运营时间遇到错误: ", e);
			Assert.fail("新建运营时间遇到错误: ", e);
		}
	}

	@Test
	public void serviceTimeTestCase07() {
		ReporterCSS.title("测试点: 修改运营时间(预售运营时间)");
		try {
			String name = "AT-" + StringUtil.getRandomNumber(6);
			ServiceTimeParam serviceTimeParam = new ServiceTimeParam();
			serviceTimeParam.setName(name);
			serviceTimeParam.setDesc(StringUtil.getRandomString(12));
			serviceTimeParam.setFinal_distribute_time("06:00");
			serviceTimeParam.setFinal_distribute_time_span(0);
			serviceTimeParam.setType(2);

			ServiceTimeParam.OrderTimeLimit orderTimeLimit = serviceTimeParam.new OrderTimeLimit();
			orderTimeLimit.setE_span_time(0);
			orderTimeLimit.setStart("07:00");
			orderTimeLimit.setEnd("22:00");
			serviceTimeParam.setOrder_time_limit(orderTimeLimit);

			ServiceTimeParam.ReceiveTimeLimit receiveTimeLimit = serviceTimeParam.new ReceiveTimeLimit();
			receiveTimeLimit.setE_span_time(1);
			receiveTimeLimit.setStart("14:00");
			receiveTimeLimit.setEnd("20:00");
			receiveTimeLimit.setReceiveTimeSpan(30);
			receiveTimeLimit.setS_span_time(1);
			receiveTimeLimit.setE_span_time(4);
			receiveTimeLimit.setWeekdays(63);
			serviceTimeParam.setReceive_time_limit(receiveTimeLimit);

			boolean result = serviceTimeService.createServiceTime(serviceTimeParam);
			Assert.assertEquals(result, true, "新建运营时间(普通)失败");

			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(0);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			ServiceTimeBean serviceTime = serviceTimes.stream().filter(s -> s.getName().equals(name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(serviceTime, null, "新建的运营时间在运营时间列表没有找到");

			String id = serviceTime.getId();
			serviceTimeParam.setId(id);
			String new_name = "AT-" + StringUtil.getRandomNumber(6);
			serviceTimeParam.setName(new_name);
			serviceTimeParam.setDesc(StringUtil.getRandomNumber(5));
			serviceTimeParam.setFinal_distribute_time("07:00");
			serviceTimeParam.setFinal_distribute_time_span(0);
			serviceTimeParam.setType(2);

			orderTimeLimit.setE_span_time(0);
			orderTimeLimit.setStart("08:00");
			orderTimeLimit.setEnd("21:00");

			receiveTimeLimit.setE_span_time(1);
			receiveTimeLimit.setStart("13:00");
			receiveTimeLimit.setEnd("21:00");
			receiveTimeLimit.setReceiveTimeSpan(15);
			receiveTimeLimit.setS_span_time(1);
			receiveTimeLimit.setE_span_time(5);
			receiveTimeLimit.setWeekdays(127);

			result = serviceTimeService.updateServiceTime(serviceTimeParam);
			Assert.assertEquals(result, true, "修改运营时间没有生效");

			serviceTime = serviceTimeService.getServiceTime(id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

			String msg = null;
			if (!serviceTime.getName().equals(serviceTimeParam.getName())) {
				msg = String.format("修改的运营时间名称与预期不符,预期:%s,实际:%s", serviceTimeParam.getName(), serviceTime.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!serviceTime.getDesc().equals(serviceTimeParam.getDesc())) {
				msg = String.format("修改的运营时间描述信息与预期不符,预期:%s,实际:%s", serviceTimeParam.getDesc(), serviceTime.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!serviceTime.getFinal_distribute_time().equals(serviceTimeParam.getFinal_distribute_time())) {
				msg = String.format("修改的运营时间字段(订单变为配送中时间)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time(), serviceTime.getFinal_distribute_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (serviceTime.getFinal_distribute_time_span() != serviceTimeParam.getFinal_distribute_time_span()) {
				msg = String.format("修改的运营时间字段(订单变为配送中时间天数)与预期不符,预期:%s,实际:%s",
						serviceTimeParam.getFinal_distribute_time_span(), serviceTime.getFinal_distribute_time_span());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.OrderTimeLimit r_orderTimeLimit = serviceTime.getOrder_time_limit();
			if (!r_orderTimeLimit.getStart().equals(orderTimeLimit.getStart())) {
				msg = String.format("修改的运营时间字段(用户下单时间开始时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getStart(),
						r_orderTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_orderTimeLimit.getEnd().equals(orderTimeLimit.getEnd())) {
				msg = String.format("修改的运营时间字段(用户下单时间结束时间点)与预期不符,预期:%s,实际:%s", orderTimeLimit.getEnd(),
						r_orderTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_orderTimeLimit.getE_span_time() != orderTimeLimit.getE_span_time()) {
				msg = String.format("修改的运营时间字段(用户下单时间是否跨天)与预期不符,预期:%s,实际:%s", orderTimeLimit.getE_span_time(),
						r_orderTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ServiceTimeBean.ReceiveTimeLimit r_receiveTimeLimit = serviceTime.getReceive_time_limit();
			if (!r_receiveTimeLimit.getStart().equals(receiveTimeLimit.getStart())) {
				msg = String.format("修改的运营时间字段(收货开始时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getStart(),
						r_receiveTimeLimit.getStart());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!r_receiveTimeLimit.getEnd().equals(receiveTimeLimit.getEnd())) {
				msg = String.format("修改的运营时间字段(收货结束时间点)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getEnd(),
						r_receiveTimeLimit.getEnd());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getReceiveTimeSpan() != receiveTimeLimit.getReceiveTimeSpan()) {
				msg = String.format("修改的运营时间字段(收货时间间隔)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getReceiveTimeSpan(),
						r_receiveTimeLimit.getReceiveTimeSpan());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getWeekdays() != receiveTimeLimit.getWeekdays()) {
				msg = String.format("修改的运营时间字段(收货自然日)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getWeekdays(),
						r_receiveTimeLimit.getWeekdays());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getS_span_time() != receiveTimeLimit.getS_span_time()) {
				msg = String.format("修改的运营时间字段(用户可选最早收货天数)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getS_span_time(),
						r_receiveTimeLimit.getS_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (r_receiveTimeLimit.getE_span_time() != receiveTimeLimit.getE_span_time()) {
				msg = String.format("修改的运营时间字段(用户可选最晚收货天数)与预期不符,预期:%s,实际:%s", receiveTimeLimit.getE_span_time(),
						r_receiveTimeLimit.getE_span_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的运营时间结果与预期不符");
		} catch (Exception e) {
			logger.error("新建运营时间遇到错误: ", e);
			Assert.fail("新建运营时间遇到错误: ", e);
		}
	}

	@AfterClass
	public void afterClass() {
		ReporterCSS.title("后置处理:删除新建的运营时间");
		try {
			List<ServiceTimeBean> serviceTimes = serviceTimeService.serviceTimeList(1);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			serviceTimes = serviceTimes.stream().filter(s -> s.getName().startsWith("AT-"))
					.collect(Collectors.toList());
			Assert.assertEquals(serviceTimes.size() > 0, true, "没有找到新建的运营时间,与预期不符");

			for (ServiceTimeBean serviceTime : serviceTimes) {
				boolean result = serviceTimeService.deleteServiceTime(serviceTime.getId());
				Assert.assertEquals(result, true, "删除报价单 " + serviceTime.getName() + "失败");
			}
			serviceTimes = serviceTimeService.serviceTimeList(1);
			Assert.assertNotEquals(serviceTimes, null, "获取服务时间列表失败");

			serviceTimes = serviceTimes.stream().filter(s -> s.getName().startsWith("AT-"))
					.collect(Collectors.toList());
			Assert.assertEquals(serviceTimes.size() == 0, true, "新建的运营时间没有真正删除");

		} catch (Exception e) {
			logger.error("删除运营时间遇到错误: ", e);
			Assert.fail("删除运营时间遇到错误: ", e);
		}
	}

}
