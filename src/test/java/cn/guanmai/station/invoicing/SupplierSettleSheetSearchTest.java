package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
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

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.SettleSheetBean;
import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetFilterParam;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierFinanceServiceImpl;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.SupplierFinanceService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Mar 28, 2019 2:16:22 PM 
* @des 结款审核测试
* @version 1.0 
*/
public class SupplierSettleSheetSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierSettleSheetSearchTest.class);
	private SupplierFinanceService stockSettleSheetService;
	private InStockService stockInService;
	private ReturnStockService stockReturnService;
	private InStockTool stockInTool;
	private InitDataBean initData;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String start;
	private String end;
	private String settle_supplier_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockSettleSheetService = new SupplierFinanceServiceImpl(headers);
		stockInTool = new InStockTool(headers);
		stockInService = new InStockServiceImpl(headers);
		stockReturnService = new ReturnStockServiceImpl(headers);
		try {
			start = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -3, Calendar.DATE);
			end = todayStr;
			initData = getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");
			settle_supplier_id = initData.getSupplier().getId();

			String[] texts = new String[] { "a", "c" };
			String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id, texts);
			Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	private List<InStockSheetBean> stockInSheeType1List; // 按入库日期的入库单
	private List<InStockSheetBean> stockInSheeType2List; // 按建单日期的入库单

	private List<ReturnStockSheetBean> stockReturnSheetType1List; // 按退货日期的退货单
	private List<ReturnStockSheetBean> stockReturnSheetType2List; // 按建单日期的退货单

	@Test(timeOut = 60000, priority = 0)
	public void stockSettleSheetTestCase00() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单、采购退货单,用作后面的数据对比");
		try {
			int limit = 50;
			int offset = 0;
			InStockSheetFilterParam stockInSheetFilterParam = new InStockSheetFilterParam();
			stockInSheetFilterParam.setType(1);
			stockInSheetFilterParam.setStart(start);
			stockInSheetFilterParam.setEnd(end);
			stockInSheetFilterParam.setStatus(2);
			stockInSheetFilterParam.setLimit(limit);
			List<InStockSheetBean> tempStockInSheeList = null;
			stockInSheeType1List = new ArrayList<InStockSheetBean>();
			while (true) {
				stockInSheetFilterParam.setOffset(offset);
				tempStockInSheeList = stockInService.searchInStockSheet(stockInSheetFilterParam);
				Assert.assertNotEquals(tempStockInSheeList, null, "按入库日期搜索过滤采购入库单失败");
				stockInSheeType1List.addAll(tempStockInSheeList);
				if (tempStockInSheeList.size() < limit) {
					break;
				}
				offset += limit;
			}

			offset = 0;
			stockInSheetFilterParam.setType(2);
			stockInSheeType2List = new ArrayList<InStockSheetBean>();
			while (true) {
				stockInSheetFilterParam.setOffset(offset);
				tempStockInSheeList = stockInService.searchInStockSheet(stockInSheetFilterParam);
				Assert.assertNotEquals(tempStockInSheeList, null, "按建单日期搜索过滤采购入库单失败");
				stockInSheeType2List.addAll(tempStockInSheeList);
				if (tempStockInSheeList.size() < limit) {
					break;
				}
				offset += limit;
			}

			ReturnStockSheetFilterParam stockReturnSheetFilterParam = new ReturnStockSheetFilterParam();
			stockReturnSheetFilterParam.setType(1);
			stockReturnSheetFilterParam.setStart(start);
			stockReturnSheetFilterParam.setEnd(end);
			stockReturnSheetFilterParam.setStatus(2);
			stockReturnSheetFilterParam.setLimit(limit);
			offset = 0;
			stockReturnSheetType1List = new ArrayList<ReturnStockSheetBean>();
			List<ReturnStockSheetBean> tempStockReturnSheetList = null;
			while (true) {
				stockReturnSheetFilterParam.setOffset(offset);
				tempStockReturnSheetList = stockReturnService.searchReturnStockSheet(stockReturnSheetFilterParam);
				Assert.assertNotEquals(tempStockReturnSheetList, null, "按退货日期搜索过滤退货单失败");
				stockReturnSheetType1List.addAll(tempStockReturnSheetList);
				if (tempStockReturnSheetList.size() < limit) {
					break;
				}
				offset += limit;
			}

			offset = 0;
			stockReturnSheetFilterParam.setType(2);
			stockReturnSheetType2List = new ArrayList<ReturnStockSheetBean>();
			while (true) {
				stockReturnSheetFilterParam.setOffset(offset);
				tempStockReturnSheetList = stockReturnService.searchReturnStockSheet(stockReturnSheetFilterParam);
				Assert.assertNotEquals(tempStockReturnSheetList, null, "按退货日期搜索过滤退货单失败");
				stockReturnSheetType2List.addAll(tempStockReturnSheetList);
				if (tempStockReturnSheetList.size() < limit) {
					break;
				}
				offset += limit;
			}

		} catch (Exception e) {
			logger.error("搜索过滤采购入库单、采购退货单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单、采购退货单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 1)
	public void stockSettleSheetTestCase01() {
		ReporterCSS.title("测试点: 按入库/退货日期 搜索过滤供应商结款待处理结款单据列表");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(1);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setReceipt_type(5);

			List<SettleSheetBean> stockSettleSheetList = stockSettleSheetService
					.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheetList, null, "按入库/退货日期 搜索过滤供应商结款待处理结款单据列表失败");

			boolean result = compareStockSettleSheetData(stockInSheeType1List, stockReturnSheetType1List,
					stockSettleSheetList);

			Assert.assertEquals(result, true, "按入库/退货 日期搜索供应商待处理单据数据,搜索到的数据与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤待处理结款单据列表遇到错误: ", e);
			Assert.fail("搜索过滤待处理结款单据列表遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 2)
	public void stockSettleSheetTestCase02() {
		ReporterCSS.title("测试点: 按建单日期 搜索过滤供应商结款待处理结款单据列表");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(2);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setReceipt_type(5);

			List<SettleSheetBean> stockSettleSheetList = stockSettleSheetService
					.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheetList, null, "按建单日期 搜索过滤供应商结款待处理结款单据列表失败");

			boolean result = compareStockSettleSheetData(stockInSheeType2List, stockReturnSheetType2List,
					stockSettleSheetList);

			Assert.assertEquals(result, true, "按建单日期搜索供应商待处理单据数据,搜索到的数据与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤待处理结款单据列表遇到错误: ", e);
			Assert.fail("搜索过滤待处理结款单据列表遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 3)
	public void stockSettleSheetTestCase03() {
		ReporterCSS.title("测试点: 按入库/退货日期 + 供应商过滤搜索待处理单据");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(1);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setSettle_supplier_id(settle_supplier_id);
			stockSettleSheetFilterParam.setReceipt_type(5);
			List<SettleSheetBean> stockSettleSheets = stockSettleSheetService
					.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheets, null, "搜索过滤结款单据失败");

			List<InStockSheetBean> expectedStockInSheets = stockInSheeType1List.stream()
					.filter(s -> s.getSettle_supplier_id().equals(settle_supplier_id)).collect(Collectors.toList());

			List<ReturnStockSheetBean> expectedStockReturnSheets = stockReturnSheetType1List.stream()
					.filter(s -> s.getSettle_supplier_id().equals(settle_supplier_id)).collect(Collectors.toList());

			boolean result = compareStockSettleSheetData(expectedStockInSheets, expectedStockReturnSheets,
					stockSettleSheets);
			Assert.assertEquals(result, true, "按入库/退货日期 + 供应商过滤搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按入库/退货日期 + 供应商过滤搜索待处理单据遇到错误: ", e);
			Assert.fail("按入库/退货日期 + 供应商过滤搜索待处理单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 4)
	public void stockSettleSheetTestCase04() {
		ReporterCSS.title("测试点: 按建单日期 + 供应商过滤搜索待处理单据");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(2);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setSettle_supplier_id(settle_supplier_id);
			stockSettleSheetFilterParam.setReceipt_type(5);
			List<SettleSheetBean> settleSheets = stockSettleSheetService.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(settleSheets, null, "搜索过滤结款单据失败");

			List<InStockSheetBean> expectedStockInSheets = stockInSheeType2List.stream()
					.filter(s -> s.getSettle_supplier_id().equals(settle_supplier_id)).collect(Collectors.toList());

			List<ReturnStockSheetBean> expectedStockReturnSheets = stockReturnSheetType2List.stream()
					.filter(s -> s.getSettle_supplier_id().equals(settle_supplier_id)).collect(Collectors.toList());

			boolean result = compareStockSettleSheetData(expectedStockInSheets, expectedStockReturnSheets,
					settleSheets);
			Assert.assertEquals(result, true, "按建单日期 + 供应商过滤搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按建单日期 + 供应商过滤搜索待处理单据遇到错误: ", e);
			Assert.fail("按建单日期 + 供应商过滤搜索待处理单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 5)
	public void stockSettleSheetTestCase05() {
		ReporterCSS.title("测试点: 按入库/退货日期 + 单据类型[采购入库单] 搜索待处理单据");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(1);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setReceipt_type(1);
			List<SettleSheetBean> settleSheets = stockSettleSheetService.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(settleSheets, null, "搜索过滤结款单据失败");

			boolean result = compareStockSettleSheetData(stockInSheeType1List, new ArrayList<ReturnStockSheetBean>(),
					settleSheets);
			Assert.assertEquals(result, true, "按入库/退货日期 + 单据类型[采购入库单] 搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按入库/退货日期 + 单据类型[采购入库单] 搜索待处理单据遇到错误: ", e);
			Assert.fail("按入库/退货日期 + 单据类型[采购入库单] 搜索待处理单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 6)
	public void stockSettleSheetTestCase06() {
		ReporterCSS.title("测试点: 按建单日期 + 单据类型[采购入库单]  过滤搜索待处理单据");
		try {
			SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
			stockSettleSheetFilterParam.setType(2);
			stockSettleSheetFilterParam.setStart(start);
			stockSettleSheetFilterParam.setEnd(end);
			stockSettleSheetFilterParam.setReceipt_type(1);
			List<SettleSheetBean> settleSheets = stockSettleSheetService.searchSettleSheet(stockSettleSheetFilterParam);
			Assert.assertNotEquals(settleSheets, null, "搜索过滤结款单据失败");

			boolean result = compareStockSettleSheetData(stockInSheeType2List, new ArrayList<ReturnStockSheetBean>(),
					settleSheets);
			Assert.assertEquals(result, true, "按建单日期 + 单据类型[采购入库单]  过滤搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按建单日期 + 单据类型[采购入库单]  过滤搜索待处理单据遇到错误: ", e);
			Assert.fail("按建单日期 + 单据类型[采购入库单]  过滤搜索待处理单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 7)
	public void stockSettleSheetTestCase07() {
		ReporterCSS.title("测试点: 按入库/退货日期 + 单据类型[采购退货单] 搜索待处理单据");
		try {
			SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
			settleSheetFilterParam.setType(1);
			settleSheetFilterParam.setStart(start);
			settleSheetFilterParam.setEnd(end);
			settleSheetFilterParam.setReceipt_type(2);
			List<SettleSheetBean> settleSheets = stockSettleSheetService.searchSettleSheet(settleSheetFilterParam);
			Assert.assertNotEquals(settleSheets, null, "搜索过滤结款单据失败");

			boolean result = compareStockSettleSheetData(new ArrayList<InStockSheetBean>(), stockReturnSheetType1List,
					settleSheets);
			Assert.assertEquals(result, true, "按入库/退货日期 + 单据类型[采购退货单] 搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按入库/退货日期 + 单据类型[采购退货单] 搜索待处理单据遇到错误: ", e);
			Assert.fail("按入库/退货日期 + 单据类型[采购退货单] 搜索待处理单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockSettleSheetTestCase00" }, priority = 8)
	public void stockSettleSheetTestCase08() {
		ReporterCSS.title("测试点: 按建单日期 + 单据类型[采购退货单]  过滤搜索待处理单据");
		try {
			SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
			settleSheetFilterParam.setType(2);
			settleSheetFilterParam.setStart(start);
			settleSheetFilterParam.setEnd(end);
			settleSheetFilterParam.setReceipt_type(2);
			List<SettleSheetBean> settleSheets = stockSettleSheetService.searchSettleSheet(settleSheetFilterParam);
			Assert.assertNotEquals(settleSheets, null, "搜索过滤结款单据失败");

			boolean result = compareStockSettleSheetData(new ArrayList<InStockSheetBean>(), stockReturnSheetType2List,
					settleSheets);
			Assert.assertEquals(result, true, "按建单日期 + 单据类型[采购退货单]  过滤搜索待处理单据,过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按建单日期 + 单据类型[采购退货单]  过滤搜索待处理单据遇到错误: ", e);
			Assert.fail("按建单日期 + 单据类型[采购退货单]  过滤搜索待处理单据遇到错误: ", e);
		}
	}

	public boolean compareStockSettleSheetData(List<InStockSheetBean> stockInSheets,
			List<ReturnStockSheetBean> stockReturnSheets, List<SettleSheetBean> stockSettleSheets) {
		String msg = null;
		boolean result = true;
		List<String> stock_in_sheet_ids = new ArrayList<String>();
		for (InStockSheetBean stockInSheet : stockInSheets) {
			String sheet_id = stockInSheet.getId();
			stock_in_sheet_ids.add(sheet_id);
			SettleSheetBean stockSettleSheet = stockSettleSheets.stream().filter(s -> s.getId().equals(sheet_id))
					.findAny().orElse(null);
			if (stockSettleSheet == null) {
				msg = String.format("采购入库单%s没有出现在供应商结算待处理单据页面", sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (stockInSheet.getSku_money().setScale(2, BigDecimal.ROUND_HALF_UP)
					.compareTo(stockSettleSheet.getSku_money().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				msg = String.format("待处理单据页面显示的采购入库单%s显示的单据总金额与预期不符,预期:%s,实际:%s", sheet_id, stockInSheet.getSku_money(),
						stockSettleSheet.getSku_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		List<String> stock_return_sheet_ids = new ArrayList<String>();
		for (ReturnStockSheetBean stockReturnSheet : stockReturnSheets) {
			String sheet_id = stockReturnSheet.getId();
			stock_return_sheet_ids.add(sheet_id);
			SettleSheetBean stockSettleSheet = stockSettleSheets.stream().filter(s -> s.getId().equals(sheet_id))
					.findAny().orElse(null);
			if (stockSettleSheet == null) {
				msg = String.format("采购退货单%s没有出现在供应商结算待处理单据页面", sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (stockReturnSheet.getSku_money().setScale(2, BigDecimal.ROUND_HALF_UP)
					.compareTo(stockSettleSheet.getSku_money().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				msg = String.format("待处理单据页面显示的采购退货单%s显示的单据总金额与预期不符,预期:%s,实际:%s", sheet_id,
						stockReturnSheet.getSku_money(), stockSettleSheet.getSku_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		List<String> actual_sheet_ids = stockSettleSheets.stream().map(s -> s.getId()).collect(Collectors.toList());

		List<String> expected_sheet_ids = new ArrayList<String>();
		expected_sheet_ids.addAll(stock_in_sheet_ids);
		expected_sheet_ids.addAll(stock_return_sheet_ids);

		actual_sheet_ids.removeAll(expected_sheet_ids);
		if (actual_sheet_ids.size() > 0) {
			msg = String.format("如下单据号%s不应该出现在供应商结算待处理单据页面", actual_sheet_ids);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;
	}
}
