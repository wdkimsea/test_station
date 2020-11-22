package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.SettleSheetDetailBean;
import cn.guanmai.station.bean.invoicing.SettleSheetBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailSubmitParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierFinanceServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.SupplierFinanceService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Mar 28, 2019 2:16:22 PM 
* @des 结款审核测试
* @version 1.0 
*/
public class SupplierSettleSheetTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierSettleSheetTest.class);
	private SupplierFinanceService supplierFinanceService;
	private LoginUserInfoService loginUserInfoService;
	private InStockService stockInService;
	private ReturnStockService stockReturnService;
	private InStockTool stockInTool;
	private InitDataBean initData;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String start;
	private String end;
	private String station_id;
	private String settle_supplier_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		supplierFinanceService = new SupplierFinanceServiceImpl(headers);
		stockInTool = new InStockTool(headers);
		stockInService = new InStockServiceImpl(headers);
		stockReturnService = new ReturnStockServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			start = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -3, Calendar.DATE);
			end = todayStr;

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();

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

	@Test
	public void stockSettleSheetTestCase01() {
		ReporterCSS.title("测试点: 待处理单据加入结款单操作");
		try {
			String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
					new String[] { "a", "c" });
			Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

			String last_month = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -1, Calendar.MONTH);

			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setSettle_supplier_id(settle_supplier_id);
			settleSheetDetailFilterParam.setStart(last_month);
			settleSheetDetailFilterParam.setEnd(todayStr);
			settleSheetDetailFilterParam.setReceipt_type(1);
			List<SettleSheetDetailBean> stockSettleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);

			Assert.assertNotEquals(stockSettleSheetDetails, null, "查询近一个月的已有结款单,用作是否加入已有结款单操作");

			List<String> sheet_nos = Arrays.asList(stock_in_sheet_id);
			String sheet_id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);
			Assert.assertNotEquals(sheet_id, null, "待处理单据加入结款单操作失败");

			InStockDetailInfoBean stockInDetailInfo = stockInService.getInStockSheetDetail(stock_in_sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "获取采购入库单" + stock_in_sheet_id + "详细信息失败");

			SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
			settleSheetFilterParam.setType(1);
			settleSheetFilterParam.setStart(start);
			settleSheetFilterParam.setEnd(todayStr);
			settleSheetFilterParam.setReceipt_type(1);
			List<SettleSheetBean> stockSettleSheets = supplierFinanceService.searchSettleSheet(settleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheets, null, "搜索过滤结款单据失败");

			String msg = null;
			boolean result = true;
			if (stockInDetailInfo.getStatus() != 3) {
				msg = String.format("采购入库单%s已经加入结款单据,状态值应该为3,实际为:%s", stock_in_sheet_id, stockInDetailInfo.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			SettleSheetBean stockSettleSheet = stockSettleSheets.stream()
					.filter(s -> s.getId().equals(stock_in_sheet_id)).findAny().orElse(null);
			if (stockSettleSheet != null) {
				msg = String.format("采购入库单%s已经加入结款单据,此单据应该不在待处理页面显示了", stock_in_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购入库单提交后,预期的数据结果与预期不符");
		} catch (Exception e) {
			logger.error("待处理单据加入结款单操作过程中遇到错误: ", e);
			Assert.fail("待处理单据加入结款单操作过程中遇到错误:", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase02() {
		ReporterCSS.title("测试点: 待处理单据加入已有结款单据");
		try {
			String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
					new String[] { "a", "c" });
			Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

			String last_month = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -1, Calendar.MONTH);

			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setSettle_supplier_id(settle_supplier_id);
			settleSheetDetailFilterParam.setStart(last_month);
			settleSheetDetailFilterParam.setEnd(todayStr);
			settleSheetDetailFilterParam.setReceipt_type(1);
			List<SettleSheetDetailBean> stockSettleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);

			Assert.assertNotEquals(stockSettleSheetDetails, null, "查询近一个月的已有结款单,用作是否加入已有结款单操作");

			List<String> sheet_nos = new ArrayList<String>();
			sheet_nos.add(stock_in_sheet_id);
			if (stockSettleSheetDetails.size() > 0) {
				SettleSheetDetailBean stockSettleSheetDetail = NumberUtil.roundNumberInList(stockSettleSheetDetails);
				String id = stockSettleSheetDetail.getId();
				boolean result = supplierFinanceService.addExistedSettleSheet(id, sheet_nos);
				Assert.assertEquals(result, true, "采购入库单" + stock_in_sheet_id + " 加入已有结款单据操作失败");
			} else {
				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);
				Assert.assertNotEquals(id, null, "待处理单据加入结款单操作失败");

				stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
						new String[] { "a", "c" });
				Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				stockSettleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(stockSettleSheetDetails, null, "查询近一个月的已有结款单,用作是否加入已有结款单操作");

				Assert.assertEquals(stockSettleSheetDetails.size() > 0, true, "已有结款单据为空,与预期不符,预期有已结款单 " + id);

				boolean result = supplierFinanceService.addExistedSettleSheet(id, sheet_nos);
				Assert.assertEquals(result, true, "采购入库单" + stock_in_sheet_id + " 加入已有结款单据操作失败");
			}

			InStockDetailInfoBean stockInDetailInfo = stockInService.getInStockSheetDetail(stock_in_sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "获取采购入库单" + stock_in_sheet_id + "详细信息失败");

			SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
			settleSheetFilterParam.setType(1);
			settleSheetFilterParam.setStart(start);
			settleSheetFilterParam.setEnd(todayStr);
			settleSheetFilterParam.setReceipt_type(1);

			List<SettleSheetBean> stockSettleSheets = supplierFinanceService.searchSettleSheet(settleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheets, null, "搜索过滤结款单据失败");

			String msg = null;
			boolean result = true;
			if (stockInDetailInfo.getStatus() != 3) {
				msg = String.format("采购入库单%s已经加入结款单据,状态值应该为3,实际为:%s", stock_in_sheet_id, stockInDetailInfo.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			SettleSheetBean stockSettleSheet = stockSettleSheets.stream()
					.filter(s -> s.getId().equals(stockInDetailInfo.getId())).findAny().orElse(null);
			if (stockSettleSheet != null) {
				msg = String.format("采购入库单%s已经加入结款单据,此单据应该不在待处理页面显示了", stock_in_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购入库单提交后,预期的数据结果与预期不符");
		} catch (Exception e) {
			logger.error("待处理单据加入结款单操作过程中遇到错误: ", e);
			Assert.fail("待处理单据加入结款单操作过程中遇到错误:", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase03() {
		ReporterCSS.title("测试点: 提交结款单据");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
				stockSettleSheetFilterParam.setType(1);
				stockSettleSheetFilterParam.setStart(start);
				stockSettleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> settleSheets = supplierFinanceService
						.searchSettleSheet(stockSettleSheetFilterParam);
				Assert.assertNotEquals(settleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (settleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			SettleSheetDetailSubmitParam submitParam = new SettleSheetDetailSubmitParam();
			submitParam.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
			submitParam.setDelta_money(new BigDecimal("0"));
			submitParam.setDiscount(new ArrayList<SettleSheetDetailSubmitParam.Discount>());
			submitParam.setSettle_supplier_id(settleSheetDetail.getSettle_supplier_id());
			submitParam.setSettle_supplier_name(settleSheetDetail.getSettle_supplier_name());
			submitParam.setSheet_nos(settleSheetDetail.getSheet_nos());
			submitParam.setStatus(0);
			submitParam.setTotal_price(settleSheetDetail.getTotal_price());
			submitParam.setRemark("自动化");
			submitParam.setId(settleSheetDetail.getId());
			submitParam.setStation_id(station_id);

			boolean result = supplierFinanceService.submitSettleSheetDetail(submitParam);
			Assert.assertEquals(result, true, "提交采购单据失败");

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			String msg = null;
			for (String temp_id : sheet_nos) {
				if (temp_id.contains("-JHD-")) {
					InStockDetailInfoBean tempStockInDetailInfo = stockInService.getInStockSheetDetail(temp_id);
					Assert.assertNotEquals(tempStockInDetailInfo, null, "获取采购入库单 " + temp_id + " 详细信息失败");
					if (tempStockInDetailInfo.getStatus() != 3) {
						msg = String.format("采购入库单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockInDetailInfo.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else if (temp_id.contains("-JHTHD-")) {
					ReturnStockDetailBean tempStockReturn = stockReturnService.getRetrunStockDetail(temp_id);
					Assert.assertNotEquals(tempStockReturn, null, "获取采购退货单 " + temp_id + "详细信息失败");
					if (tempStockReturn.getStatus() != 3) {
						msg = String.format("采购退货单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockReturn.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			Assert.assertEquals(settleSheetDetail.getStatus(), 2, "提交的结款单据 " + id + " 状态值与预期不符");

			Assert.assertEquals(result, true, "提交的结款单据里的单据状态值不正确");
		} catch (Exception e) {
			logger.error("提交结款单据过程中遇到错误: ", e);
			Assert.fail("提交结款单据过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase04() {
		ReporterCSS.title("测试点: 对结款单据进行标记结款");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
				stockSettleSheetFilterParam.setType(1);
				stockSettleSheetFilterParam.setStart(start);
				stockSettleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> settleSheets = supplierFinanceService
						.searchSettleSheet(stockSettleSheetFilterParam);
				Assert.assertNotEquals(settleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (settleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			SettleSheetDetailSubmitParam submitParam = new SettleSheetDetailSubmitParam();
			submitParam.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
			submitParam.setDelta_money(new BigDecimal("0"));
			submitParam.setDiscount(new ArrayList<SettleSheetDetailSubmitParam.Discount>());
			submitParam.setSettle_supplier_id(settleSheetDetail.getSettle_supplier_id());
			submitParam.setSettle_supplier_name(settleSheetDetail.getSettle_supplier_name());
			submitParam.setSheet_nos(settleSheetDetail.getSheet_nos());
			submitParam.setStatus(0);
			submitParam.setTotal_price(settleSheetDetail.getTotal_price());
			submitParam.setRemark("自动化");
			submitParam.setId(settleSheetDetail.getId());
			submitParam.setStation_id(station_id);

			boolean result = supplierFinanceService.submitSettleSheetDetail(submitParam);
			Assert.assertEquals(result, true, "提交采购单据失败");

			result = supplierFinanceService.markPayment(settleSheetDetail.getId(),
					StringUtil.getRandomString(8).toUpperCase(), settleSheetDetail.getTotal_price());

			Assert.assertEquals(result, true, "结款单据标记结款失败");

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			Assert.assertEquals(settleSheetDetail.getStatus(), 4, "标记已结款的结款单据 " + id + " 状态值与预期不符");

			String msg = null;
			for (String temp_id : sheet_nos) {
				if (temp_id.contains("-JHD-")) {
					InStockDetailInfoBean tempStockInDetailInfo = stockInService.getInStockSheetDetail(temp_id);
					Assert.assertNotEquals(tempStockInDetailInfo, null, "获取采购入库单 " + temp_id + " 详细信息失败");
					if (tempStockInDetailInfo.getStatus() != 3) {
						msg = String.format("采购入库单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockInDetailInfo.getStatus());
					}
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				} else if (temp_id.contains("-JHTHD-")) {
					ReturnStockDetailBean tempStockReturn = stockReturnService.getRetrunStockDetail(temp_id);
					Assert.assertNotEquals(tempStockReturn, null, "获取采购退货单 " + temp_id + "详细信息失败");
					if (tempStockReturn.getStatus() != 3) {
						msg = String.format("采购退货单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockReturn.getStatus());
					}
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		} catch (Exception e) {
			logger.error("提交结款单据过程中遇到错误: ", e);
			Assert.fail("提交结款单据过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase05() {
		ReporterCSS.title("测试点: 对结款单据进行红冲操作");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
				settleSheetFilterParam.setType(1);
				settleSheetFilterParam.setStart(start);
				settleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> stockSettleSheets = supplierFinanceService
						.searchSettleSheet(settleSheetFilterParam);
				Assert.assertNotEquals(stockSettleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (stockSettleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			boolean result = supplierFinanceService.deleteSettleSheetDetail(id);
			Assert.assertEquals(result, true, "对结款单进行红冲操作失败");

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			Assert.assertEquals(settleSheetDetail.getStatus(), -1, "红冲的结款单据 " + id + " 状态值与预期不符");

			String msg = null;
			for (String temp_id : sheet_nos) {
				if (temp_id.contains("-JHD-")) {
					InStockDetailInfoBean tempStockInDetailInfo = stockInService.getInStockSheetDetail(temp_id);
					Assert.assertNotEquals(tempStockInDetailInfo, null, "获取采购入库单 " + temp_id + " 详细信息失败");
					if (tempStockInDetailInfo.getStatus() != 2) {
						msg = String.format("采购入库单%s的状态值与预期不符,预期:2,实际:%s", temp_id, tempStockInDetailInfo.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

				} else if (temp_id.contains("-JHTHD-")) {
					ReturnStockDetailBean tempStockReturn = stockReturnService.getRetrunStockDetail(temp_id);
					Assert.assertNotEquals(tempStockReturn, null, "获取采购退货单 " + temp_id + "详细信息失败");
					if (tempStockReturn.getStatus() != 2) {
						msg = String.format("采购退货单%s的状态值与预期不符,预期:2,实际:%s", temp_id, tempStockReturn.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "结款单据红冲后,对应的采购入库单/采购退货单状态与预期不符");
		} catch (Exception e) {
			logger.error("对结款单据进行红冲操作过程中遇到错误: ", e);
			Assert.fail("对结款单据进行红冲操作过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase06() {
		ReporterCSS.title("测试点: 对结款单进行审核不通过操作");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam stockSettleSheetFilterParam = new SettleSheetFilterParam();
				stockSettleSheetFilterParam.setType(1);
				stockSettleSheetFilterParam.setStart(start);
				stockSettleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> settleSheets = supplierFinanceService
						.searchSettleSheet(stockSettleSheetFilterParam);
				Assert.assertNotEquals(settleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (settleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			SettleSheetDetailSubmitParam submitParam = new SettleSheetDetailSubmitParam();
			submitParam.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
			submitParam.setDelta_money(new BigDecimal("0"));
			submitParam.setDiscount(new ArrayList<SettleSheetDetailSubmitParam.Discount>());
			submitParam.setSettle_supplier_id(settleSheetDetail.getSettle_supplier_id());
			submitParam.setSettle_supplier_name(settleSheetDetail.getSettle_supplier_name());
			submitParam.setSheet_nos(settleSheetDetail.getSheet_nos());
			submitParam.setStatus(0);
			submitParam.setTotal_price(settleSheetDetail.getTotal_price());
			submitParam.setRemark("自动化");
			submitParam.setId(settleSheetDetail.getId());
			submitParam.setStation_id(station_id);

			boolean result = supplierFinanceService.submitSettleSheetDetail(submitParam);
			Assert.assertEquals(result, true, "提交采购单据失败");
			result = supplierFinanceService.reviewSettleSheetDetail(settleSheetDetail.getId());
			Assert.assertEquals(result, true, "审核不通过结款单据操作失败");

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(id);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据" + id + "详细信息失败");

			Assert.assertEquals(settleSheetDetail.getStatus(), 0, "结款单据" + id + "的审核不通过后,状态值与预期不一致");

			String msg = null;
			for (String temp_id : sheet_nos) {
				if (temp_id.contains("-JHD-")) {
					InStockDetailInfoBean tempStockInDetailInfo = stockInService.getInStockSheetDetail(temp_id);
					Assert.assertNotEquals(tempStockInDetailInfo, null, "获取采购入库单 " + temp_id + " 详细信息失败");
					if (tempStockInDetailInfo.getStatus() != 3) {
						msg = String.format("采购入库单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockInDetailInfo.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else if (temp_id.contains("-JHTHD-")) {
					ReturnStockDetailBean tempStockReturn = stockReturnService.getRetrunStockDetail(temp_id);
					Assert.assertNotEquals(tempStockReturn, null, "获取采购退货单 " + temp_id + "详细信息失败");
					if (tempStockReturn.getStatus() != 3) {
						msg = String.format("采购退货单%s的状态值与预期不符,预期:3,实际:%s", temp_id, tempStockReturn.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "结款单据红冲后,对应的采购入库单/采购退货单状态与预期不符");
		} catch (Exception e) {
			logger.error("进行结款单据进行审核不通过过程中遇到错误: ", e);
			Assert.fail("进行结款单据进行审核不通过过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase07() {
		ReporterCSS.title("测试点: 打印结款单据");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
				settleSheetFilterParam.setType(1);
				settleSheetFilterParam.setStart(start);
				settleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> settleSheets = supplierFinanceService.searchSettleSheet(settleSheetFilterParam);
				Assert.assertNotEquals(settleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (settleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			SettleSheetDetailBean print_payment = supplierFinanceService.printSettleSheetDetail(id);
			Assert.assertNotEquals(print_payment, null, "打印结款单据操作失败");
		} catch (Exception e) {
			logger.error("打印结款单据遇到错误: ", e);
			Assert.fail("打印结款单据遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase08() {
		ReporterCSS.title("测试点: 导出结款单据");
		try {
			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(start);
			settleSheetDetailFilterParam.setEnd(end);
			settleSheetDetailFilterParam.setReceipt_type(1);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = null;
			String stock_in_sheet_id = null;
			List<String> sheet_nos = null;
			if (settleSheetDetails.size() == 0) {
				SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
				settleSheetFilterParam.setType(1);
				settleSheetFilterParam.setStart(start);
				settleSheetFilterParam.setEnd(end);

				List<SettleSheetBean> settleSheets = supplierFinanceService.searchSettleSheet(settleSheetFilterParam);
				Assert.assertNotEquals(settleSheets, null, "搜索过滤待处理结款单据列表失败");
				if (settleSheets.size() == 0) {
					stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(settle_supplier_id,
							new String[] { "a", "c" });
					Assert.assertNotEquals(stock_in_sheet_id, null, "创建采购入库单失败");
				}

				sheet_nos = new ArrayList<String>();
				sheet_nos.add(stock_in_sheet_id);

				String id = supplierFinanceService.addSettleSheet(settle_supplier_id, sheet_nos);

				Assert.assertNotEquals(id, null, "待处理单据加入结款单失败");

				settleSheetDetails = supplierFinanceService.searchSettleSheetDetail(settleSheetDetailFilterParam);
				Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

				settleSheetDetail = settleSheetDetails.stream().filter(s -> s.getId().equals(id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(settleSheetDetail, null, "结款单据 " + id + " 在结款单据中没有找到");
			} else {
				settleSheetDetail = NumberUtil.roundNumberInList(settleSheetDetails);
				sheet_nos = settleSheetDetail.getSheet_nos();

			}

			String id = settleSheetDetail.getId();

			boolean result = supplierFinanceService.exportSettleSheetDetail(id);
			Assert.assertEquals(result, true, "导出结款单据操作失败");
		} catch (Exception e) {
			logger.error("导出结款单据遇到错误: ", e);
			Assert.fail("导出结款单据遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase09() {
		try {
			ReporterCSS.title("测试点: 导出待处理单据列表");

			boolean result = supplierFinanceService.exportSettleSheet(todayStr, todayStr);
			Assert.assertEquals(result, true, "导出待结款单据列表失败");
		} catch (Exception e) {
			logger.error("导出待结款单据列表遇到错误: ", e);
			Assert.fail("导出待结款单据列表遇到错误: ", e);
		}
	}

	@Test
	public void stockSettleSheetTestCase10() {
		ReporterCSS.title("测试点: 导出结款单据列表");
		try {
			boolean result = supplierFinanceService.exportPaymentList(todayStr, todayStr);
			Assert.assertEquals(result, true, "导出待结款单据列表失败");
		} catch (Exception e) {
			logger.error("导出结款单据列表遇到错误: ", e);
			Assert.fail("导出结款单据列表遇到错误: ", e);
		}
	}

}
