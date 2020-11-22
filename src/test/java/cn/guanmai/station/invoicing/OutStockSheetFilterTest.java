package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年3月10日 下午5:34:48
 * @description: 销售出库单搜索过滤
 * @version: 1.0
 */

public class OutStockSheetFilterTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OutStockTest.class);
	private OutStockService outStockService;
	private OrderTool orderTool;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String start_date_new;
	private String end_date_new;
	private ServiceTimeService serviceTimeService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		outStockService = new OutStockServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase01() {
		// 按建单日期搜索
		OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam(2, 0, null, 0, 50, today, today);
		try {
			ReporterCSS.title("测试点: 按建单日期搜索过滤销售出库单");
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream().filter(s -> !s.getDate_time().contains(today))
					.map(s -> s.getId()).collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"按建单日期过滤销售出库单,搜索出了如下不符合过滤条件[建单日期]的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase02() {
		// 按出库日期搜索
		OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam(1, 0, null, 0, 50, today, today);
		try {
			ReporterCSS.title("测试点: 按出库日期搜索过滤销售出库单");
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream()
					.filter(s -> !s.getOut_stock_time().contains(today)).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"按出库日期过滤销售出库单,搜索出了如下不符合过滤条件[出库日期]的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void stockOutSheetFilterTestCase03() {
		try {
			ReporterCSS.title("测试点: 按运营时间搜索过滤出库单");
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam(2, 0, null, 0, 50, today, today);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			if (outStockSheetList.size() > 0) {
				OutStockSheetBean stockOutSheet = outStockSheetList.stream().filter(s -> s.getId().startsWith("PL"))
						.findFirst().orElse(null);
				if (stockOutSheet != null) {
					String id = stockOutSheet.getId();
					OrderCycle orderCycle = orderTool.getOrderOperationCycle(id);
					String time_config_id = orderCycle.getTime_config_id();
					String cycle_start_time = orderCycle.getCycle_start_time();
					String cycle_end_time = orderCycle.getCycle_end_time();
					int limit = 50;
					int offset = 0;

					filterParam = new OutStockSheetFilterParam();
					filterParam.setType(3);
					filterParam.setTime_config_id(time_config_id);
					filterParam.setCycle_start_time(cycle_start_time);
					filterParam.setCycle_end_time(cycle_end_time);
					filterParam.setLimit(limit);

					outStockSheetList = new ArrayList<OutStockSheetBean>();
					List<OutStockSheetBean> stockOutSheets = null;
					while (true) {
						filterParam.setOffset(offset);
						stockOutSheets = outStockService.searchOutStockSheet(filterParam);
						Assert.assertNotEquals(stockOutSheets, null, "按运营时间搜索销售出库单失败");
						outStockSheetList.addAll(stockOutSheets);
						if (stockOutSheets.size() < limit) {
							break;
						} else {
							offset += limit;
						}

					}
					stockOutSheet = outStockSheetList.stream().filter(s -> s.getId().equals(id)).findAny().orElse(null);
					Assert.assertNotEquals(stockOutSheet, null, "按运营时间搜索过滤出库单,没有过滤出目标销售出库单 " + id);
				}
			} else {
				List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
				Assert.assertNotEquals(serviceTimeList, null, "获取站点运营时间失败");
				Assert.assertEquals(serviceTimeList.size() >= 1, true, "站点无运营时间,无法进行后续操作");

				ServiceTimeBean serviceTime = serviceTimeList.get(0);
				String time_config_id = serviceTime.getId();

				String today = LocalDate.now().toString();
				String receive_time_start = today + " " + serviceTime.getReceive_time_limit().getStart();

				String receive_time_end = TimeUtil.calculateTime("yyyy-MM-dd", today,
						serviceTime.getReceive_time_limit().getE_span_time(), Calendar.DAY_OF_MONTH) + " "
						+ serviceTime.getReceive_time_limit().getEnd();

				filterParam = new OutStockSheetFilterParam(0, null, 0, 10, time_config_id, receive_time_start,
						receive_time_end);
				outStockSheetList = outStockService.searchOutStockSheet(filterParam);
				Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");
			}
		} catch (Exception e) {
			logger.error("按运营时间搜索出库单遇到错误: ", e);
			Assert.fail("按运营时间搜索出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase04() {
		try {
			ReporterCSS.title("测试点: 按收货时间过滤出库单");

			String end = TimeUtil.calculateTime("yyyy-MM-dd", today, 2, Calendar.DAY_OF_WEEK);
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(4);
			filterParam.setStatus(0);
			filterParam.setStart(today);
			filterParam.setEnd(end);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			String msg = null;
			boolean result = true;
			String receive_begin_time = null;
			for (OutStockSheetBean stockOutSheet : outStockSheetList) {
				receive_begin_time = stockOutSheet.getReceive_begin_time().substring(0, 10);
				if (TimeUtil.compareDate("yyyy-MM-dd", today, receive_begin_time) == 1) {
					msg = String.format("按收货日期搜索开始时间%s,销售出库单%s收货开始时间", today, stockOutSheet.getId(),
							receive_begin_time);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
				if (TimeUtil.compareDate("yyyy-MM-dd", end, receive_begin_time) == -1) {
					msg = String.format("按收货日期搜索结束时间%s,销售出库单%s收货开始时间", end, stockOutSheet.getId(), receive_begin_time);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "按收货日期过滤销售出库单,过滤出了不符合条件的销售出库单");
		} catch (Exception e) {
			logger.error("按运营时间搜索出库单遇到错误: ", e);
			Assert.fail("按运营时间搜索出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase05() {
		ReporterCSS.title("测试点: 导出销售出库单");
		OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam(2, 0, null, 0, 10, today, today);
		filterParam.setExport();
		try {
			boolean result = outStockService.exportOutStockSheet(filterParam);
			Assert.assertEquals(result, true, "导出销售出库单失败");
		} catch (Exception e) {
			logger.error("导出销售出库单遇到错误: ", e);
			Assert.fail("导出销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase06() {
		ReporterCSS.title("测试点: 新建UI,按建单日期过滤销售出库单");
		// 按建单日期搜索
		OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
		filterParam.setType(2);
		filterParam.setStart_date_new(start_date_new);
		filterParam.setEnd_date_new(end_date_new);
		try {
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "新建UI,按建单日期过滤销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream()
					.filter(s -> !s.getDate_time().contains(start_date_new.substring(0, 10))).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"新建UI,按建单日期过滤销售出库单,搜索出了如下不符合过滤条件[建单日期]的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase07() {
		ReporterCSS.title("测试点: 新版UI,按出库时间搜索过滤销售出库单");
		OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
		filterParam.setType(1);
		filterParam.setStart_date_new(start_date_new);
		filterParam.setEnd_date_new(end_date_new);
		try {
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "新版UI,按出库时间搜索过滤销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream()
					.filter(s -> !s.getOut_stock_time().contains(start_date_new.substring(0, 10))).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"新建UI,按建单日期过滤销售出库单,搜索出了如下不符合过滤条件[出库日期]的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase08() {
		try {
			ReporterCSS.title("测试点: 新版UI,按收货时间过滤出库单");
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(4);
			filterParam.setStart_date_new(start_date_new);
			String end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 2, Calendar.DAY_OF_WEEK);
			filterParam.setEnd_date_new(end_date_new);

			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "新版UI,按收货时间过滤出库单失败");

			String msg = null;
			boolean result = true;
			String receive_begin_time = null;
			for (OutStockSheetBean stockOutSheet : outStockSheetList) {
				receive_begin_time = stockOutSheet.getReceive_begin_time();
				if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_date_new, receive_begin_time) == 1) {
					msg = String.format("按收货日期搜索开始时间%s,销售出库单%s收货开始时间", today, stockOutSheet.getId(),
							receive_begin_time);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
				if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", end_date_new, receive_begin_time) == -1) {
					msg = String.format("按收货日期搜索结束时间%s,销售出库单%s收货开始时间", end_date_new, stockOutSheet.getId(),
							receive_begin_time);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "按收货日期过滤销售出库单,过滤出了不符合条件的销售出库单");
		} catch (Exception e) {
			logger.error("按运营时间搜索出库单遇到错误: ", e);
			Assert.fail("按运营时间搜索出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase09() {
		ReporterCSS.title("测试点: 新版UI,导出销售出库单");
		try {
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			filterParam.setExport();
			boolean result = outStockService.exportOutStockSheet(filterParam);
			Assert.assertEquals(result, true, "导出销售出库单失败");
		} catch (Exception e) {
			logger.error("导出销售出库单遇到错误: ", e);
			Assert.fail("导出销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase10() {
		ReporterCSS.title("测试点: 新版UI,按建单日期+出库单据状态过滤销售出库单");
		try {
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			filterParam.setStatus(1);

			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream()
					.filter(s -> s.getStatus() != filterParam.getStatus()).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"新版UI,按建单日期+出库单据状态[待出库]过滤销售出库单,过滤出了如下不符合过滤条件的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase12() {
		ReporterCSS.title("测试点: 新版UI,按建单日期+单据备注过滤销售出库单");
		try {
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			filterParam.setHas_remark(1);

			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");

			List<String> stockOutSheetIds = outStockSheetList.stream().filter(s -> s.getOut_stock_remark() == null)
					.map(s -> s.getId()).collect(Collectors.toList());
			Assert.assertEquals(stockOutSheetIds.size() == 0, true,
					"新版UI,按建单日期+单据备注[有憋住]滤销售出库单,过滤出了如下不符合过滤条件的销售出库单" + stockOutSheetIds);
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase13() {
		ReporterCSS.title("测试点: 新版UI,按建单日期+商户标签过滤销售出库单");
		try {
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			filterParam.setAddress_label_id("2566");

			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}

	@Test
	public void stockOutSheetFilterTestCase14() {
		ReporterCSS.title("测试点: 新版UI,按建单日期+线路过滤销售出库单");
		try {
			OutStockSheetFilterParam filterParam = new OutStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			BigDecimal route_id = new BigDecimal("-1");
			filterParam.setRoute_id(route_id);
			List<OutStockSheetBean> outStockSheetList = outStockService.searchOutStockSheet(filterParam);
			Assert.assertNotEquals(outStockSheetList, null, "搜索销售出库单失败");
		} catch (Exception e) {
			logger.error("搜索销售出库单遇到错误: ", e);
			Assert.fail("搜索销售出库单遇到错误: ", e);
		}
	}
}
