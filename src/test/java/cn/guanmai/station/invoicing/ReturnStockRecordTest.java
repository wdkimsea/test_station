package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.ReturnStockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockRecordBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2020年1月6日
 * @time 上午11:42:49
 * @des 采购退货记录
 */

public class ReturnStockRecordTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ReturnStockRecordTest.class);
	private ReturnStockService returnStockService;
	private InStockService inStockService;
	private LoginUserInfoService loginUserInfoService;
	private StockService stockService;
	private StockRecordService stockRecordService;
	private StockCheckService stockCheckService;
	private InitDataBean initData;
	private ReturnStockDetailBean returnStockDetail;
	private String submit_time = TimeUtil.getCurrentTime("yyyy-MM-dd"); // 提交时间
	private String return_stock_time;
	private String return_sheet_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		returnStockService = new ReturnStockServiceImpl(headers);
		inStockService = new InStockServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		try {
			initData = getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");
			String station_store_id = loginUserInfo.getStation_id();

			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String in_stock_sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(in_stock_sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean inStockDetailInfo = new InStockDetailInfoBean();
			inStockDetailInfo.setId(in_stock_sheet_id);
			inStockDetailInfo.setCreator("自动化");
			inStockDetailInfo.setRemark("自动化创建");
			inStockDetailInfo.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
			String in_stock_time = TimeUtil.calculateTime("yyyy-MM-dd", submit_time, -1, Calendar.DATE);
			inStockDetailInfo.setSubmit_time(in_stock_time);
			inStockDetailInfo.setSettle_supplier_id(supplier_id);
			inStockDetailInfo.setSupplier_name(supplier_name);
			inStockDetailInfo.setSupplier_customer_id(supplier.getCustomer_id());
			inStockDetailInfo.setType(1);
			inStockDetailInfo.setStatus(1);
			inStockDetailInfo.setIs_submit(2);
			inStockDetailInfo.setStation_id(station_store_id);
			inStockDetailInfo.setSku_money(new BigDecimal("0"));
			inStockDetailInfo.setDelta_money(new BigDecimal("0"));

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			inStockDetailInfo.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			inStockDetailInfo.setShares(shares);

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("h", in_stock_sheet_id);
			Assert.assertEquals(supplySkus != null && supplySkus.size() >= 1, true, "入库单搜索入库商品无结果,与预期不符");

			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			NumberFormat nf = new DecimalFormat("00000");
			int batch_index = 1;
			for (SupplySkuBean supplySku : supplySkus) {
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();
				InStockDetailInfoBean.Detail detail = inStockDetailInfo.new Detail();

				detail.setBatch_number(in_stock_sheet_id + "-" + nf.format(batch_index));
				batch_index++;
				detail.setName(supplySku.getSku_name());
				String displayName = supplySku.getSku_name() + "(" + purchase_ratio + parchase_std_unit + "/"
						+ purchase_unit + ")";
				detail.setDisplayName(displayName);
				detail.setId(supplySku.getSku_id());
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setPurchase_unit(purchase_unit);
				detail.setStd_unit(parchase_std_unit);
				detail.setRatio(purchase_ratio);
				BigDecimal quantity = new BigDecimal("10");
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.multiply(purchase_ratio));
				BigDecimal unit_price = NumberUtil.getRandomNumber(2, 8, 1);
				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity));
				detail.setOperator("自动化");
				details.add(detail);
				if (details.size() >= 6) {
					break;
				}
			}
			inStockDetailInfo.setDetails(details);

			boolean result = inStockService.modifyInStockSheet(inStockDetailInfo);
			Assert.assertEquals(result, true, "提交采购入库单" + in_stock_sheet_id + "失败");

			int stock_method = loginUserInfo.getStock_method();

			// 新建采购退货单
			return_sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(return_sheet_id, null, "新建采购退货单失败");

			// 加权平均和先进先出退货方式不同
			returnStockDetail = returnStockService.getRetrunStockDetail(return_sheet_id);
			List<ReturnStockDetailBean.Detail> returnStockDetail_Details = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail returnStockDetail_Detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			if (stock_method == 1) {
				for (SupplySkuBean supplySku : supplySkus) {
					returnStockDetail_Detail = returnStockDetail.new Detail();
					returnStockDetail_Detail.setCategory(supplySku.getCategory_id_2_name());
					returnStockDetail_Detail.setId(supplySku.getSku_id());
					returnStockDetail_Detail.setName(supplySku.getSku_name());
					returnStockDetail_Detail.setSpu_id(supplySku.getSpu_id());
					returnStockDetail_Detail.setStd_unit(supplySku.getStd_unit_name());
					unit_price = NumberUtil.getRandomNumber(4, 8, 1);
					returnStockDetail_Detail.setUnit_price(unit_price);
					quantity = NumberUtil.getRandomNumber(4, 8, 0);
					returnStockDetail_Detail.setQuantity(quantity);
					money = unit_price.multiply(quantity);
					returnStockDetail_Detail.setMoney(money);
					sku_money = sku_money.add(money);
					returnStockDetail_Details.add(returnStockDetail_Detail);
					if (returnStockDetail_Details.size() >= 6) {
						break;
					}
				}
			} else {
				List<ReturnStockBatchBean> returnStockDetailBatchList = null;
				String purchase_spec__id = null;
				for (SupplySkuBean supplySku : supplySkus) {
					returnStockDetail_Detail = returnStockDetail.new Detail();
					purchase_spec__id = supplySku.getSku_id();
					returnStockDetailBatchList = returnStockService.searchReturnStockBatch(purchase_spec__id,
							supplier_id);

					Assert.assertNotEquals(returnStockDetailBatchList, null, "先进先出,搜索过滤成品退货商品批次信息失败");

					Assert.assertEquals(returnStockDetailBatchList.size() > 0, true,
							"采购规格" + purchase_spec__id + "无对应批次,与预期结果不符");

					// 有商户退货的批次就使用商户退货的批次进行退货
					ReturnStockBatchBean returnStockDetailBatch = returnStockDetailBatchList.stream()
							.filter(s -> s.getBatch_number().startsWith("THRKP")).findFirst().orElse(null);
					if (returnStockDetailBatch == null) {
						returnStockDetailBatch = NumberUtil.roundNumberInList(returnStockDetailBatchList);
					}

					String batch_number = returnStockDetailBatch.getBatch_number();
					returnStockDetail_Detail.setName(supplySku.getSku_name());
					returnStockDetail_Detail.setId(purchase_spec__id);
					returnStockDetail_Detail.setSpu_id(supplySku.getSpu_id());
					returnStockDetail_Detail.setCategory(supplySku.getCategory_id_2_name());

					StockBatchBean stockBatch = stockCheckService.getStockBatch(batch_number);
					Assert.assertNotEquals(stockBatch, null, "获取批次  " + batch_number + " 详细信息失败");
					unit_price = stockBatch.getPrice();
					returnStockDetail_Detail.setUnit_price(unit_price);
					returnStockDetail_Detail.setStd_unit(supplySku.getStd_unit_name());
					BigDecimal remain = returnStockDetailBatch.getRemain();
					returnStockDetail_Detail.setQuantity(remain);
					returnStockDetail_Detail.setMoney(unit_price.multiply(remain));
					returnStockDetail_Detail.setBatch_number(batch_number);
					returnStockDetail_Detail.setOperator("自动化");
					returnStockDetail_Details.add(returnStockDetail_Detail);
					money = unit_price.multiply(remain);
					sku_money = sku_money.add(money);
					if (returnStockDetail_Details.size() >= 6) {
						break;
					}
				}
			}

			returnStockDetail.setSku_money(sku_money);
			return_stock_time = TimeUtil.calculateTime("yyyy-MM-dd", submit_time, -1, Calendar.DATE);
			returnStockDetail.setSubmit_time(return_stock_time);
			returnStockDetail.setDetails(returnStockDetail_Details);
			returnStockDetail.setDiscounts(new ArrayList<ReturnStockDetailBean.Discount>());

			result = returnStockService.modifyReturnStockSheet(returnStockDetail);
			Assert.assertEquals(result, true, "提交成品退货单" + return_sheet_id + "失败");

			returnStockDetail = returnStockService.getRetrunStockDetail(return_sheet_id);
			Assert.assertNotEquals(returnStockDetail, null, "获取采购退货单 " + return_sheet_id + " 失败");

			Thread.sleep(2000);
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	private List<String> category_id_1 = new ArrayList<String>();
	private List<String> category_id_2 = new ArrayList<String>();

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase01() {
		ReporterCSS.title("测试点: 按提交时间查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(submit_time);
			stockRecordParam.setEnd(submit_time);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			String category1_id = null;
			String category2_id = null;
			for (ReturnStockRecordBean returnStockDetailRecord : targetStockReturnRecordArray) {
				category1_id = returnStockDetailRecord.getCategory_id_1();
				category2_id = returnStockDetailRecord.getCategory_id_2();
				if (!category_id_1.contains(category1_id)) {
					category_id_1.add(category1_id);
				}

				if (!category_id_2.contains(category2_id)) {
					category_id_2.add(category2_id);
				}
			}
			boolean result = compareData(returnStockDetail.details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按提交时间查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase02() {
		ReporterCSS.title("测试点: 按退货出库时间查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(return_stock_time);
			stockRecordParam.setEnd(return_stock_time);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(returnStockDetail.details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按退货出库时间查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase03() {
		ReporterCSS.title("测试点: 按[提交时间+商品ID]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(submit_time);
			stockRecordParam.setEnd(submit_time);

			List<ReturnStockDetailBean.Detail> returnStockDetail_Details = returnStockDetail.getDetails();
			ReturnStockDetailBean.Detail temp_detail = NumberUtil.roundNumberInList(returnStockDetail_Details);
			String sku_id = temp_detail.getId();

			List<ReturnStockDetailBean.Detail> temp_details = new ArrayList<ReturnStockDetailBean.Detail>();
			temp_details.add(temp_detail);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);
			stockRecordParam.setText(sku_id);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[提交时间+商品ID]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase04() {
		ReporterCSS.title("测试点: 按[退货出库时间+商品ID]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(return_stock_time);
			stockRecordParam.setEnd(return_stock_time);

			List<ReturnStockDetailBean.Detail> returnStockDetail_Details = returnStockDetail.getDetails();
			ReturnStockDetailBean.Detail temp_detail = NumberUtil.roundNumberInList(returnStockDetail_Details);
			String sku_id = temp_detail.getId();

			List<ReturnStockDetailBean.Detail> temp_details = new ArrayList<ReturnStockDetailBean.Detail>();
			temp_details.add(temp_detail);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);
			stockRecordParam.setText(sku_id);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[退货出库时间+商品ID]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase05() {
		ReporterCSS.title("测试点: 按[提交时间+商品名称]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(submit_time);
			stockRecordParam.setEnd(submit_time);

			List<ReturnStockDetailBean.Detail> returnStockDetail_Details = returnStockDetail.getDetails();
			ReturnStockDetailBean.Detail temp_detail = NumberUtil.roundNumberInList(returnStockDetail_Details);
			String sku_name = temp_detail.getName();

			List<ReturnStockDetailBean.Detail> temp_details = new ArrayList<ReturnStockDetailBean.Detail>();
			temp_details.add(temp_detail);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);
			stockRecordParam.setText(sku_name);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[提交时间+商品名称]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void returnStockDetailRecordTestCase06() {
		ReporterCSS.title("测试点: 按[退货出库时间+商品ID]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(return_stock_time);
			stockRecordParam.setEnd(return_stock_time);

			List<ReturnStockDetailBean.Detail> returnStockDetail_Details = returnStockDetail.getDetails();
			ReturnStockDetailBean.Detail temp_detail = NumberUtil.roundNumberInList(returnStockDetail_Details);
			String sku_name = temp_detail.getName();

			List<ReturnStockDetailBean.Detail> temp_details = new ArrayList<ReturnStockDetailBean.Detail>();
			temp_details.add(temp_detail);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);
			stockRecordParam.setText(sku_name);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(temp_details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[退货出库时间+商品名称]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "returnStockDetailRecordTestCase01" }, timeOut = 30000)
	public void returnStockDetailRecordTestCase07() {
		ReporterCSS.title("测试点: 按[提交时间+商品分类]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(submit_time);
			stockRecordParam.setEnd(submit_time);
			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(returnStockDetail.details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[提交时间+商品分类]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "returnStockDetailRecordTestCase01" }, timeOut = 30000)
	public void returnStockDetailRecordTestCase08() {
		ReporterCSS.title("测试点: 按[退货出库时间+商品分类]查询采购退货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setTime_type(2);
			stockRecordParam.setBegin(return_stock_time);
			stockRecordParam.setEnd(return_stock_time);
			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<ReturnStockRecordBean> returnStockDetailRecordArray = new ArrayList<ReturnStockRecordBean>();

			// 获取今天所有的退货出库记录
			while (true) {
				List<ReturnStockRecordBean> tempArrray = stockRecordService.returnStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempArrray, null, "获取成品退货出库日志失败");
				returnStockDetailRecordArray.addAll(tempArrray);
				if (tempArrray.size() < limit) {
					break;
				}
				offset += limit;
				stockRecordParam.setOffset(offset);
			}

			// 把目标成品退货单的退货记录给过滤出来
			List<ReturnStockRecordBean> targetStockReturnRecordArray = returnStockDetailRecordArray.stream()
					.filter(record -> record.getSheet_no().equals(return_sheet_id)).collect(Collectors.toList());

			boolean result = compareData(returnStockDetail.details, targetStockReturnRecordArray);
			Assert.assertEquals(result, true, " 按[退货出库时间+商品分类]查询采购退货记录,查询到的采购退货记录与预期不符");
		} catch (Exception e) {
			logger.error("查询采购退货记录遇到错误: ", e);
			Assert.fail("查询采购退货记录遇到错误: ", e);
		}
	}

	public boolean compareData(List<ReturnStockDetailBean.Detail> returnDetails,
			List<ReturnStockRecordBean> targetStockReturnRecordArray) {
		boolean result = true;
		String msg = null;
		ReturnStockRecordBean returnStockDetailRecord = null;
		for (ReturnStockDetailBean.Detail detail : returnDetails) {
			String sku_id = detail.getId();
			returnStockDetailRecord = targetStockReturnRecordArray.stream().filter(r -> r.getSku_id().equals(sku_id))
					.findAny().orElse(null);
			if (returnStockDetailRecord == null) {
				msg = String.format("采购退货单%s中的商品%s没有记录退货出库日志", return_sheet_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (returnStockDetailRecord.getReturn_amount().compareTo(detail.getQuantity()) != 0) {
				msg = String.format("采购退货单%s中的商品%s记录的退货数与预期不符,预期:%s,实际:%s", return_sheet_id, sku_id,
						detail.getQuantity(), returnStockDetailRecord.getReturn_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (returnStockDetailRecord.getAll_price().setScale(2, BigDecimal.ROUND_HALF_UP)
					.compareTo(detail.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				msg = String.format("采购退货单%s中的商品%s记录的退货记录与预期不符,预期:%s,实际:%s", return_sheet_id, sku_id, detail.getMoney(),
						returnStockDetailRecord.getAll_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

}
