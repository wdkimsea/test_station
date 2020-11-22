package cn.guanmai.station.system;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.system.OperationLogBean;
import cn.guanmai.station.bean.system.OperationLogDetailBean;
import cn.guanmai.station.bean.system.param.OperationLogFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.system.OperationLogServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.system.OperationLogService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午3:11:14
 * @description:
 * @version: 1.0
 */

public class OperationLogTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OperationLogTest.class);
	private OperationLogService operationLogService;
	private AsyncService asyncService;
	private OperationLogFilterParam operationLogFilterParam;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		operationLogService = new OperationLogServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		operationLogFilterParam = new OperationLogFilterParam();
		operationLogFilterParam.setOp_start_date(today);
		operationLogFilterParam.setOp_end_date(today);
	}

	@Test
	public void operationLogTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤订单日志");
		try {
			operationLogFilterParam.setLog_type(1);
			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤订单日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String order_id = operationLog.getOp_id();
				operationLogFilterParam.setSearch_text(order_id);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤订单日志失败");

				List<String> other_order_ids = operationLogs.stream().filter(o -> !o.getOp_id().equals(order_id))
						.map(o -> o.getOp_id()).collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				if (other_order_ids.size() > 0) {
					msg = String.format("订单日志输入订单号%s搜索过滤,过滤出了其他订单%s的操作日志", order_id, other_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}

				String customer_name = operationLog.getCustomer_name();
				operationLogFilterParam.setSearch_text(customer_name);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤订单日志失败");

				List<String> other_customer_names = operationLogs.stream()
						.filter(o -> !o.getCustomer_name().equals(customer_name)).map(o -> o.getCustomer_name())
						.collect(Collectors.toList());

				if (other_customer_names.size() > 0) {
					msg = String.format("订单日志输入商户名%s搜索过滤,过滤出了其他商户%s的操作日志", customer_name, other_customer_names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}
				Assert.assertEquals(result, true, "操作日志输入关键词过滤,过滤出了不符合过滤条件的条目");
			}

		} catch (Exception e) {
			logger.error("搜索过滤订单日志遇到错误: ", e);
			Assert.fail("搜索过滤订单日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase02() {
		ReporterCSS.title("测试点: 搜索过滤订单日志");
		try {
			operationLogFilterParam.setLog_type(1);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤订单日志失败");

			List<Integer> other_op_types = operationLogs.stream().filter(o -> o.getOp_type() != 1)
					.map(o -> o.getOp_type()).collect(Collectors.toList());

			Assert.assertEquals(other_op_types.size(), 0, "订单操作按操作类型:新增 过滤,过滤出了其他操作类型的条目信息");
		} catch (Exception e) {
			logger.error("搜索过滤订单日志遇到错误: ", e);
			Assert.fail("搜索过滤订单日志遇到错误: ", e);
		}
	}

	public void operationLogTestCase03() {
		ReporterCSS.title("测试点: 获取订单操作日志详情");
		try {
			operationLogFilterParam.setLog_type(1);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤订单日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String id = operationLog.getId();

				OperationLogDetailBean operationLogDetail = operationLogService.getOperationLogDetail(id);
				Assert.assertNotEquals(operationLogDetail, null, "获取订单操作日志详情失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤订单日志遇到错误: ", e);
			Assert.fail("搜索过滤订单日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase04() {
		ReporterCSS.title("测试点: 搜索过滤商品日志");
		try {
			operationLogFilterParam.setLog_type(2);
			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤商品日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String merchandise_name = operationLog.getMerchandise_name();
				operationLogFilterParam.setSearch_text(merchandise_name);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤商品日志失败");

				List<String> other_merchandise_names = operationLogs.stream()
						.filter(o -> !o.getMerchandise_name().equals(merchandise_name)).map(o -> o.getOp_id())
						.collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				if (other_merchandise_names.size() > 0) {
					msg = String.format("商品日志输入商品名%s搜索过滤,过滤出了其他商品%s的操作日志", merchandise_name, other_merchandise_names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}

				String sku_id = operationLog.getOp_id();
				operationLogFilterParam.setSearch_text(sku_id);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤商品日志失败");

				List<String> other_sku_ids = operationLogs.stream().filter(o -> !o.getOp_id().equals(sku_id))
						.map(o -> o.getCustomer_name()).collect(Collectors.toList());

				if (other_sku_ids.size() > 0) {
					msg = String.format("商品日志输入商品ID%s搜索过滤,过滤出了其他商品%s的操作日志", sku_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}
				Assert.assertEquals(result, true, "操作日志输入关键词过滤,过滤出了不符合过滤条件的条目");
			}

		} catch (Exception e) {
			logger.error("搜索过滤订单日志遇到错误: ", e);
			Assert.fail("搜索过滤订单日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase05() {
		ReporterCSS.title("测试点: 搜索过滤商品日志");
		try {
			operationLogFilterParam.setLog_type(2);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤商品日志失败");

			List<Integer> other_op_types = operationLogs.stream().filter(o -> o.getOp_type() != 1)
					.map(o -> o.getOp_type()).collect(Collectors.toList());

			Assert.assertEquals(other_op_types.size(), 0, "商品操作按操作类型:新增 过滤,过滤出了其他操作类型的条目信息");
		} catch (Exception e) {
			logger.error("搜索过滤商品日志遇到错误: ", e);
			Assert.fail("搜索过滤商品日志遇到错误: ", e);
		}
	}

	public void operationLogTestCase06() {
		ReporterCSS.title("测试点: 获取商品操作日志详情");
		try {
			operationLogFilterParam.setLog_type(2);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤商品日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String id = operationLog.getId();

				OperationLogDetailBean operationLogDetail = operationLogService.getOperationLogDetail(id);
				Assert.assertNotEquals(operationLogDetail, null, "获取商品操作日志详情失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤商品日志遇到错误: ", e);
			Assert.fail("搜索过滤商品日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase07() {
		ReporterCSS.title("测试点: 搜索过滤分拣日志");
		try {
			operationLogFilterParam.setLog_type(4);
			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤分拣日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String merchandise_name = operationLog.getMerchandise_name();
				operationLogFilterParam.setSearch_text(merchandise_name);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤分拣日志失败");

				List<String> other_merchandise_names = operationLogs.stream()
						.filter(o -> !o.getMerchandise_name().equals(merchandise_name)).map(o -> o.getOp_id())
						.collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				if (other_merchandise_names.size() > 0) {
					msg = String.format("分拣日志输入商品名%s搜索过滤,过滤出了其他商品%s的操作日志", merchandise_name, other_merchandise_names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String order_id = operationLog.getOp_id().split("_")[0];
				operationLogFilterParam.setSearch_text(order_id);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤分拣日志失败");

				List<String> other_orders_ids = operationLogs.stream().filter(o -> !o.getOp_id().contains(order_id))
						.map(o -> o.getOp_id().split("_")[0]).collect(Collectors.toList());

				if (other_orders_ids.size() > 0) {
					msg = String.format("分拣日志输入订单ID%s搜索过滤,过滤出了其他订单%s的操作日志", order_id, other_orders_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;

				}
				Assert.assertEquals(result, true, "操作日志输入关键词过滤,过滤出了不符合过滤条件的条目");
			}

		} catch (Exception e) {
			logger.error("搜索过滤订单日志遇到错误: ", e);
			Assert.fail("搜索过滤订单日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase08() {
		ReporterCSS.title("测试点: 按操作类型搜索过滤分拣日志");
		try {
			operationLogFilterParam.setLog_type(4);
			operationLogFilterParam.setOp_type(4);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤分拣日志失败");

			List<Integer> other_op_types = operationLogs.stream().filter(o -> o.getOp_type() != 4)
					.map(o -> o.getOp_type()).collect(Collectors.toList());

			Assert.assertEquals(other_op_types.size(), 0, "分拣操作按操作类型:分拣 过滤,过滤出了其他操作类型的条目信息");
		} catch (Exception e) {
			logger.error("搜索过滤商品日志遇到错误: ", e);
			Assert.fail("搜索过滤商品日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase09() {
		ReporterCSS.title("测试点: 搜索过滤锁价日志");
		try {
			operationLogFilterParam.setLog_type(5);
			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤锁价日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String op_id = operationLog.getOp_id();
				operationLogFilterParam.setSearch_text(op_id);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤锁价日志失败");

				List<String> other_op_ids = operationLogs.stream().filter(o -> !o.getOp_id().equals(op_id))
						.map(o -> o.getOp_id()).collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				if (other_op_ids.size() > 0) {
					msg = String.format("锁价日志输入锁价ID%s搜索过滤,过滤出了其他锁价%s的操作日志", op_id, other_op_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "锁价日志输入关键词过滤,过滤出了不符合过滤条件的条目");
			}

		} catch (Exception e) {
			logger.error("搜索过滤锁价日志遇到错误: ", e);
			Assert.fail("搜索过滤锁价日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase10() {
		ReporterCSS.title("测试点: 按操作类型搜索过滤锁价日志");
		try {
			operationLogFilterParam.setLog_type(5);
			operationLogFilterParam.setOp_type(2);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤锁价日志失败");

			List<Integer> other_op_types = operationLogs.stream().filter(o -> o.getOp_type() != 2)
					.map(o -> o.getOp_type()).collect(Collectors.toList());

			Assert.assertEquals(other_op_types.size(), 0, "锁价操作按操作类型:编辑 过滤,过滤出了其他操作类型的条目信息");
		} catch (Exception e) {
			logger.error("搜索过滤锁价日志遇到错误: ", e);
			Assert.fail("搜索过滤锁价日志遇到错误: ", e);
		}
	}

	public void operationLogTestCase11() {
		ReporterCSS.title("测试点: 获取锁价操作日志详情");
		try {
			operationLogFilterParam.setLog_type(5);
			operationLogFilterParam.setOp_type(2);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤锁价日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String id = operationLog.getId();

				OperationLogDetailBean operationLogDetail = operationLogService.getOperationLogDetail(id);
				Assert.assertNotEquals(operationLogDetail, null, "获取锁价操作日志详情失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤锁价日志遇到错误: ", e);
			Assert.fail("搜索过滤锁价日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase12() {
		ReporterCSS.title("测试点: 搜索过滤采购日志");
		try {
			operationLogFilterParam.setLog_type(7);
			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤采购日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String sku_name = operationLog.getModify().getJSONObject("sku_name").getString("after");
				operationLogFilterParam.setSearch_text(sku_name);
				operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
				Assert.assertNotEquals(operationLogs, null, "搜索过滤锁价日志失败");

				List<String> other_sku_names = operationLogs.stream()
						.filter(o -> !o.getModify().getJSONObject("sku_name").toString().contains(sku_name))
						.map(o -> o.getModify().getJSONObject("sku_name").toString()).collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				if (other_sku_names.size() > 0) {
					msg = String.format("采购日志输入商品名称[%s]搜索过滤,过滤出了其他采购商品%s的操作日志", sku_name, other_sku_names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "采购日志输入关键词过滤,过滤出了不符合过滤条件的条目");
			}

		} catch (Exception e) {
			logger.error("搜索过滤采购日志遇到错误: ", e);
			Assert.fail("搜索过滤采购日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase13() {
		ReporterCSS.title("测试点: 按操作类型搜索过滤采购日志");
		try {
			operationLogFilterParam.setLog_type(7);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤采购日志失败");

			List<Integer> other_op_types = operationLogs.stream().filter(o -> o.getOp_type() != 1)
					.map(o -> o.getOp_type()).collect(Collectors.toList());

			Assert.assertEquals(other_op_types.size(), 0, "采购日志按操作类型:新增 过滤,过滤出了其他操作类型的条目信息");
		} catch (Exception e) {
			logger.error("搜索过滤采购日志遇到错误: ", e);
			Assert.fail("搜索过滤采购日志遇到错误: ", e);
		}
	}

	public void operationLogTestCase14() {
		ReporterCSS.title("测试点: 获取采购日志详情");
		try {
			operationLogFilterParam.setLog_type(7);
			operationLogFilterParam.setOp_type(1);

			List<OperationLogBean> operationLogs = operationLogService.searchOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(operationLogs, null, "搜索过滤采购日志失败");

			if (operationLogs.size() > 0) {
				OperationLogBean operationLog = NumberUtil.roundNumberInList(operationLogs);
				String id = operationLog.getId();

				OperationLogDetailBean operationLogDetail = operationLogService.getOperationLogDetail(id);
				Assert.assertNotEquals(operationLogDetail, null, "获取采购操作日志详情失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤采购日志遇到错误: ", e);
			Assert.fail("搜索过滤采购日志遇到错误: ", e);
		}
	}

	@Test
	public void operationLogTestCase15() {
		ReporterCSS.title("测试点: 导出操作日志");
		try {
			operationLogFilterParam.setLog_type(1);

			BigDecimal task_id = operationLogService.exportOperationLog(operationLogFilterParam);
			Assert.assertNotEquals(task_id, null, "导出操作日志异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出操作日志的异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出操作日志遇到错误: ", e);
			Assert.fail("导出操作日志遇到错误: ", e);
		}
	}

}
