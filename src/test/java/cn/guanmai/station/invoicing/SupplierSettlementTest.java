package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.StockSettleSupplierBean;
import cn.guanmai.station.bean.invoicing.SettlementCollectBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailPageBean;
import cn.guanmai.station.bean.invoicing.SettleSheetDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementBean;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettlementFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierFinanceServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.invoicing.SupplierFinanceService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年2月4日 下午2:29:04
 * @description: 应付总账 & 应付明细账
 * @version: 1.0
 */

public class SupplierSettlementTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierSettlementTest.class);
	private String begin;
	private String end;

	private InStockService stockInService;
	private ReturnStockService stockReturnService;
	private SupplierFinanceService supplierFinanceService;
	private StockService stockService;
	private AsyncService asyncService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockInService = new InStockServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		stockReturnService = new ReturnStockServiceImpl(headers);
		supplierFinanceService = new SupplierFinanceServiceImpl(headers);

		String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
		try {
			begin = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -1, Calendar.DATE);
			end = todayStr;
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	private List<InStockSheetBean> stockInSheetList;
	private List<ReturnStockSheetBean> stockReturnSheetList;

	@Test(timeOut = 60000)
	public void supplierSettlementTestCase00() {
		ReporterCSS.title("测试点: 统计" + begin + "-" + end + "的采购入库和采购退回金额信息");
		try {
			int limit = 50;
			int offset = 0;
			InStockSheetFilterParam stockInSheetFilterParam = new InStockSheetFilterParam();
			stockInSheetFilterParam.setStatus(5);
			stockInSheetFilterParam.setStart(begin);
			stockInSheetFilterParam.setEnd(end);
			stockInSheetFilterParam.setLimit(limit);
			stockInSheetFilterParam.setType(1);

			stockInSheetList = new ArrayList<InStockSheetBean>();
			List<InStockSheetBean> tempStockInSheets = null;
			while (true) {
				stockInSheetFilterParam.setOffset(offset);
				tempStockInSheets = stockInService.searchInStockSheet(stockInSheetFilterParam);
				Assert.assertNotEquals(tempStockInSheets, null, "搜索过滤采购入库单列表失败");
				stockInSheetList.addAll(tempStockInSheets);
				if (tempStockInSheets.size() < limit) {
					break;
				}
				offset += limit;
			}

			offset = 0;
			ReturnStockSheetFilterParam stockReturnSheetFilterParam = new ReturnStockSheetFilterParam();
			stockReturnSheetFilterParam.setStatus(5);
			stockReturnSheetFilterParam.setType(1);
			stockReturnSheetFilterParam.setStart(begin);
			stockReturnSheetFilterParam.setEnd(end);
			stockReturnSheetFilterParam.setLimit(limit);
			stockReturnSheetFilterParam.setOffset(offset);

			stockReturnSheetList = new ArrayList<ReturnStockSheetBean>();
			List<ReturnStockSheetBean> tempStockReturnSheets = null;
			while (true) {
				stockReturnSheetFilterParam.setOffset(offset);
				tempStockReturnSheets = stockReturnService.searchReturnStockSheet(stockReturnSheetFilterParam);
				Assert.assertNotEquals(tempStockReturnSheets, null, "搜索过滤采购退回单失败");
				stockReturnSheetList.addAll(tempStockReturnSheets);
				if (tempStockReturnSheets.size() < limit) {
					break;
				}
				offset += limit;
			}
		} catch (Exception e) {
			logger.error("统计采购入库采购退回信息遇到错误: ", e);
			Assert.fail("统计采购入库采购退回信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementTestCase01() {
		ReporterCSS.title("测试点: 获取应付总账统计信息");
		try {
			SettlementCollectBean settlementCollect = supplierFinanceService.getSettlementCollect(begin, end, "");
			Assert.assertNotEquals(settlementCollect, null, "获取应付总账统计信息失败");
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementTestCase02() {
		ReporterCSS.title("测试点: 应付总账列表信息");
		try {
			SettlementFilterParam filterParam = new SettlementFilterParam();
			filterParam.setBegin(begin);
			filterParam.setEnd(end);

			List<SettlementBean> settlements = supplierFinanceService.searchSettlement(filterParam);
			Assert.assertNotEquals(settlements, null, "获取应付总账列表信息失败");
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierSettlementTestCase00" })
	public void supplierSettlementTestCase03() {
		ReporterCSS.title("测试点: 获取应付总账统计信息");
		try {
			BigDecimal expected_cur_should_pay_sum = new BigDecimal("0");
			int status = 0;
			for (InStockSheetBean stockInSheet : stockInSheetList) {
				status = stockInSheet.getStatus();
				if (status == 4 || status == 3 || status == 2) {
					expected_cur_should_pay_sum = expected_cur_should_pay_sum.add(stockInSheet.getTotal_money());
				}
			}

			for (ReturnStockSheetBean stockReturnSheet : stockReturnSheetList) {
				status = stockReturnSheet.getStatus();
				if (status == 4 || status == 3 || status == 2) {
					expected_cur_should_pay_sum = expected_cur_should_pay_sum
							.subtract(stockReturnSheet.getTotal_money());
				}
			}

			SettlementCollectBean stockSettlementCollect = supplierFinanceService.getSettlementCollect(begin, end, "");
			Assert.assertNotEquals(stockSettlementCollect, null, "获取应付总账统计信息失败");

			String msg = null;
			boolean result = true;
			if (expected_cur_should_pay_sum.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(
					stockSettlementCollect.getCur_should_pay_sum().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				msg = String.format("应付总账统计的应付金额与预期不一致,预期:%s,实际:%s", expected_cur_should_pay_sum,
						stockSettlementCollect.getCur_should_pay_sum());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(begin);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(4);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤供应商结款单据失败");

			// 注:本期结款金额无法验证,这里统计的是这段时间内结款的金额,但是结款单据的搜索维度只有一个 按建单日期 搜索过滤

			Assert.assertEquals(result, true, "应付总账统计的总计数据与预期不一致");
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierSettlementTestCase00" }, timeOut = 60000)
	public void supplierSettlementTestCase04() {
		ReporterCSS.title("测试点: 应付总账列表信息校验");
		try {
			int limit = 50;
			int offset = 0;
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);
			settlementFilterParam.setLimit(limit);

			List<SettlementBean> settlementList = new ArrayList<SettlementBean>();
			List<SettlementBean> tempSettlements = null;
			while (true) {
				settlementFilterParam.setOffset(offset);
				tempSettlements = supplierFinanceService.searchSettlement(settlementFilterParam);
				Assert.assertNotEquals(tempSettlements, null, "应用总账列表信息拉取失败");
				settlementList.addAll(tempSettlements);
				if (tempSettlements.size() < limit) {
					break;
				}
				offset += limit;
			}

			Map<String, BigDecimal> cur_should_pay_map = new HashMap<String, BigDecimal>();

			Map<String, List<InStockSheetBean>> stockInSheetMap = stockInSheetList.stream()
					.collect(Collectors.groupingBy(InStockSheetBean::getSettle_supplier_id));

			int status = 0;
			for (String settle_supplier_id : stockInSheetMap.keySet()) {
				List<InStockSheetBean> stockInSheets = stockInSheetMap.get(settle_supplier_id);
				BigDecimal cur_should_pay = new BigDecimal("0");
				BigDecimal cur_pay = new BigDecimal("0");
				for (InStockSheetBean stockInSheet : stockInSheets) {
					status = stockInSheet.getStatus();
					if (status == 4) {
						cur_should_pay = cur_should_pay.add(stockInSheet.getTotal_money());
						cur_pay = cur_pay.add(stockInSheet.getTotal_money());
						continue;
					} else if (status == 2 || status == 3) {
						cur_should_pay = cur_should_pay.add(stockInSheet.getTotal_money());
					}
				}
				cur_should_pay_map.put(settle_supplier_id, cur_should_pay);
			}

			// 采购退货单
			Map<String, List<ReturnStockSheetBean>> stockReturnSheetMap = stockReturnSheetList.stream()
					.collect(Collectors.groupingBy(ReturnStockSheetBean::getSettle_supplier_id));
			for (String settle_supplier_id : stockReturnSheetMap.keySet()) {
				List<ReturnStockSheetBean> stockReturnSheets = stockReturnSheetMap.get(settle_supplier_id);
				BigDecimal cur_should_pay = new BigDecimal("0");
				BigDecimal cur_pay = new BigDecimal("0");
				for (ReturnStockSheetBean stockReturnSheet : stockReturnSheets) {
					status = stockReturnSheet.getStatus();
					if (status == 4) {
						cur_should_pay = cur_should_pay.add(stockReturnSheet.getTotal_money());
						cur_pay = cur_pay.add(stockReturnSheet.getTotal_money());
						continue;
					} else if (status == 2 || status == 3) {
						cur_should_pay = cur_should_pay.add(stockReturnSheet.getTotal_money());
					}
				}

				BigDecimal temp_cur_should_pay = cur_should_pay_map.getOrDefault(settle_supplier_id,
						new BigDecimal("0"));

				cur_should_pay_map.put(settle_supplier_id, temp_cur_should_pay.subtract(cur_should_pay));

			}

			String msg = null;
			boolean result = true;
			BigDecimal expected_cur_should_pay = null;
			String settle_supplier_name = null;
			for (String settle_supplier_id : cur_should_pay_map.keySet()) {
				SettlementBean stockSettlement = settlementList.stream()
						.filter(s -> s.getSettle_supplier_id().equals(settle_supplier_id)).findAny().orElse(null);
				if (stockSettlement == null) {
					msg = String.format("应付总账里没有供应商%s的条目信息", settle_supplier_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				settle_supplier_name = stockSettlement.getName();

				expected_cur_should_pay = cur_should_pay_map.get(settle_supplier_id);

				if (expected_cur_should_pay.setScale(2, BigDecimal.ROUND_HALF_UP)
						.compareTo(stockSettlement.getCur_should_pay().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
					msg = String.format("应付总账里供应商[%s-%s]的条目信息,本期应付金额与预期不一致,预期:%s,实际:%s", settle_supplier_id,
							settle_supplier_name, expected_cur_should_pay, stockSettlement.getCur_should_pay());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "应付总账统计的数据与预期不一致");
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementTestCase05() {
		ReporterCSS.title("测试点: 应付总账使用供应商过滤信息");
		try {
			List<StockSettleSupplierBean> stocksSettleSuppliers = stockService.getStockSettleSuppliers();
			Assert.assertNotEquals(stocksSettleSuppliers, null, "获取进销存供应商列表失败");

			int limit = 50;
			int offset = 0;
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);
			settlementFilterParam.setLimit(limit);
			settlementFilterParam.setOffset(offset);

			StockSettleSupplierBean stockSettleSupplier = NumberUtil.roundNumberInList(stocksSettleSuppliers);
			String settle_supplier_id = stockSettleSupplier.getSettle_supplier_id();

			settlementFilterParam.setSettle_supplier_id(settle_supplier_id);

			List<SettlementBean> stockSettlements = supplierFinanceService.searchSettlement(settlementFilterParam);
			Assert.assertNotEquals(stockSettlements, null, "应付总账使用供应商过滤信息失败");

			List<String> settle_supplier_ids = stockSettlements.stream()
					.filter(s -> !s.getSettle_supplier_id().equals(settle_supplier_id))
					.map(s -> s.getSettle_supplier_id()).collect(Collectors.toList());

			Assert.assertEquals(settle_supplier_ids.size(), 0, "使用供应商搜索过滤应付总账信息,过滤出了不合符过滤条件的信息" + settle_supplier_ids);
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementTestCase06() {
		ReporterCSS.title("测试点: 导出应付总账信息");
		try {
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);

			boolean result = supplierFinanceService.exportSettlement(settlementFilterParam);
			Assert.assertEquals(result, true, "导出应付总账信息失败");
		} catch (Exception e) {
			logger.error("获取应付总账统计信息遇到错误: ", e);
			Assert.fail("获取应付总账统计信息遇到错误: ", e);
		}
	}

	/***************************************************************
	 * *****************************应付明细账**************************
	 ***************************************************************/

	@Test(timeOut = 20000)
	public void supplierSettlementDetailTestCase01() {
		ReporterCSS.title("测试点: 应付明细账列表");
		try {
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);
			settlementFilterParam.setLimit(50);
			settlementFilterParam.setOffset(0);

			SettlementDetailPageBean settlementDetailPage = null;
			SettlementDetailPageBean.Pagination pagination = null;
			boolean more = true;
			while (more) {
				settlementDetailPage = supplierFinanceService.searchSettlementDetailPage(settlementFilterParam);
				Assert.assertNotEquals(settlementDetailPage, null, "应付明细账搜索过滤失败");
				pagination = settlementDetailPage.getPagination();
				more = pagination.isMore();
				if (more) {
					settlementFilterParam.setPage_obj(pagination.getPage_obj());
				}
			}
		} catch (Exception e) {
			logger.error("获取应付明细账信息遇到错误: ", e);
			Assert.fail("获取应付明细账信息遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000, dependsOnMethods = { "supplierSettlementTestCase00" })
	public void supplierSettlementDetailTestCase02() {
		ReporterCSS.title("测试点: 应付明细账列表数据验证");
		try {
			int limit = 50;
			int offset = 0;
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);
			settlementFilterParam.setLimit(limit);
			settlementFilterParam.setOffset(offset);

			List<SettlementDetailBean> settlementDetailList = new ArrayList<SettlementDetailBean>();

			SettlementDetailPageBean settlementDetailPage = null;

			boolean more = true;
			while (more) {
				settlementDetailPage = supplierFinanceService.searchSettlementDetailPage(settlementFilterParam);
				Assert.assertNotEquals(settlementDetailPage, null, "应付明细账搜索过滤失败");
				SettlementDetailPageBean.Pagination pagination = settlementDetailPage.getPagination();
				more = pagination.isMore();
				settlementDetailList.addAll(settlementDetailPage.getSettlementDetails());
				if (more) {
					settlementFilterParam.setPage_obj(pagination.getPage_obj());
				}
			}

			int status = 0;
			List<String> expected_sheet_ids = new ArrayList<String>();
			SettlementDetailBean settlementDetail = null;
			boolean result = true;
			String msg = null;
			for (InStockSheetBean stockInSheet : stockInSheetList) {
				status = stockInSheet.getStatus();
				if (status == 4 || status == 3 || status == 2) {
					String sheet_id = stockInSheet.getId();
					expected_sheet_ids.add(sheet_id);

					settlementDetail = settlementDetailList.stream()
							.filter(s -> s.getSheet_number() != null && s.getSheet_number().equals(sheet_id)).findAny()
							.orElse(null);
					if (settlementDetail == null) {
						msg = String.format("采购入库单%s没有出现在应付明细账页面中", sheet_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (stockInSheet.getTotal_money().compareTo(settlementDetail.getShould_pay()) != 0) {
						msg = String.format("采购入库单%s的应付金额与应付明细账页面中的应付金额不一致,预期:%s,实际:%s", sheet_id,
								stockInSheet.getTotal_money(), settlementDetail.getShould_pay());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			for (ReturnStockSheetBean stockReturnSheet : stockReturnSheetList) {
				status = stockReturnSheet.getStatus();
				if (status == 4 || status == 3 || status == 2) {
					String sheet_id = stockReturnSheet.getId();
					expected_sheet_ids.add(sheet_id);

					settlementDetail = settlementDetailList.stream()
							.filter(s -> s.getSheet_number() != null && s.getSheet_number().equals(sheet_id)).findAny()
							.orElse(null);
					if (settlementDetail == null) {
						msg = String.format("采购退货单%s没有出现在应付明细账页面中", sheet_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (stockReturnSheet.getTotal_money().negate().setScale(2, BigDecimal.ROUND_HALF_UP)
							.compareTo(settlementDetail.getShould_pay().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
						msg = String.format("采购退货单%s的应付金额与应付明细账页面中的应付金额不一致,预期:%s,实际:%s", sheet_id,
								stockReturnSheet.getTotal_money().negate(), settlementDetail.getShould_pay());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			List<String> actual_sheet_ids = settlementDetailList.stream()
					.filter(s -> s.getSheet_number() != null && s.getSheet_number().contains("-JH"))
					.map(s -> s.getSheet_number()).collect(Collectors.toList());

			List<String> temp_ids = actual_sheet_ids.stream().filter(s -> !expected_sheet_ids.contains(s))
					.collect(Collectors.toList());

			if (temp_ids.size() > 0) {
				msg = String.format("应付明细账页面中多拉取显示了如下采购入库单\\采购退回单%s", temp_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "应付明细页面中显示的数据与预期不一致");
		} catch (Exception e) {
			logger.error("获取应付明细账信息遇到错误: ", e);
			Assert.fail("获取应付明细账信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementDetailTestCase03() {
		ReporterCSS.title("测试点: 应付明细账使用供应商搜索过滤");
		try {
			int limit = 50;
			int offset = 0;
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);
			settlementFilterParam.setLimit(limit);
			settlementFilterParam.setOffset(offset);

			List<StockSettleSupplierBean> stocksSettleSuppliers = stockService.getStockSettleSuppliers();
			Assert.assertNotEquals(stocksSettleSuppliers, null, "获取进销存供应商列表失败");

			StockSettleSupplierBean stockSettleSupplier = NumberUtil.roundNumberInList(stocksSettleSuppliers);
			String settle_supplier_id = stockSettleSupplier.getSettle_supplier_id();
			settlementFilterParam.setSettle_supplier_id(settle_supplier_id);

			List<SettlementDetailBean> settlementDetails = supplierFinanceService
					.searchSettlementDetail(settlementFilterParam);
			Assert.assertNotEquals(settlementDetails, null, "应付明细账搜索过滤失败");

			List<SettlementDetailBean> tempSettlementDetails = settlementDetails.stream()
					.filter(s -> !s.getSupplier_id().equals(stockSettleSupplier.getCustomer_id()))
					.collect(Collectors.toList());
			Assert.assertEquals(tempSettlementDetails.size(), 0, "应付明细账使用供应商搜索过滤,过滤出了不符合过滤条件的数据");
		} catch (Exception e) {
			logger.error("获取应付明细账信息遇到错误: ", e);
			Assert.fail("获取应付明细账信息遇到错误: ", e);
		}
	}

	@Test
	public void supplierSettlementDetailTestCase04() {
		ReporterCSS.title("测试点: 应付明细账导出");
		try {
			SettlementFilterParam settlementFilterParam = new SettlementFilterParam();
			settlementFilterParam.setBegin(begin);
			settlementFilterParam.setEnd(end);

			BigDecimal task_id = supplierFinanceService.exportSettlementDetail(settlementFilterParam);
			Assert.assertNotEquals(task_id, null, "应付明细导出异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "");
			Assert.assertEquals(result, true, "应付明细导出异步任务执行失败");
		} catch (Exception e) {
			logger.error("应付明细账导出遇到错误: ", e);
			Assert.fail("应付明细账导出遇到错误: ", e);
		}
	}

}
