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
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.InStockRecordBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.InStockRecordFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年12月31日
 * @time 上午10:41:43
 * @des 采购入库记录查询
 */

public class InStockRecordTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(InStockRecordTest.class);
	private StockService stockService;
	private InStockService inStockService;
	private StockRecordService stockRecordService;
	private LoginUserInfoService loginUserInfoService;
	private InitDataBean initData;
	private String station_store_id;
	private String sheet_id;
	private InStockDetailInfoBean inStockDetailInfo;
	private List<InStockDetailInfoBean.Detail> details;
	private String submit_time = TimeUtil.getCurrentTime("yyyy-MM-dd"); // 提交时间
	private String in_stock_time;
	private InStockRecordFilterParam inStockRecordFilterParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockService = new StockServiceImpl(headers);
		inStockService = new InStockServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);

		try {
			initData = getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_store_id = loginUserInfo.getStation_id();

			stockService = new StockServiceImpl(headers);

			// 新建入库单据
			SupplierDetailBean supplier = initData.getSupplier();
			String settle_supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			sheet_id = inStockService.createInStockSheet(settle_supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			inStockDetailInfo = new InStockDetailInfoBean();
			inStockDetailInfo.setId(sheet_id);
			inStockDetailInfo.setCreator("自动化");
			inStockDetailInfo.setRemark("自动化创建");
			inStockDetailInfo.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
			in_stock_time = TimeUtil.calculateTime("yyyy-MM-dd", submit_time, -1, Calendar.DATE);
			inStockDetailInfo.setSubmit_time(in_stock_time);
			inStockDetailInfo.setSettle_supplier_id(settle_supplier_id);
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

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("h", sheet_id);
			Assert.assertEquals(supplySkus != null && supplySkus.size() >= 1, true, "入库单搜索入库商品无结果,与预期不符");

			details = new ArrayList<InStockDetailInfoBean.Detail>();
			NumberFormat nf = new DecimalFormat("00000");
			int batch_number = 1;
			for (SupplySkuBean supplySku : supplySkus) {
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();
				InStockDetailInfoBean.Detail detail = inStockDetailInfo.new Detail();

				detail.setBatch_number(sheet_id + "-" + nf.format(batch_number));
				batch_number++;
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
				BigDecimal quantity = NumberUtil.getRandomNumber(5, 10, 1);
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.multiply(purchase_ratio));
				BigDecimal unit_price = NumberUtil.getRandomNumber(2, 5, 0);
				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity));
				detail.setOperator("自动化");
				details.add(detail);
				if (details.size() >= 2) {
					break;
				}
			}
			inStockDetailInfo.setDetails(details);

			boolean result = inStockService.modifyInStockSheet(inStockDetailInfo);
			Assert.assertEquals(result, true, "提交采购入库单失败");
			Thread.sleep(3000);
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase01() {
		ReporterCSS.title("测试点: 按默认时间[等于按入库时间]查询入库记录,(不带time_type参数)");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase02() {
		ReporterCSS.title("测试点: 按提交时间查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(1);
			inStockRecordFilterParam.setBegin(submit_time);
			inStockRecordFilterParam.setEnd(submit_time);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> submit_times = targetInStockRecordArray.stream()
					.filter(s -> !s.getCommit_time().equals(submit_time)).map(r -> r.getSubmit_time())
					.collect(Collectors.toList());

			Assert.assertEquals(submit_times.size(), 0, "按提交时间查询入库记录,过滤出了以下提交时间 " + submit_times + " 的入库记录,与过滤条件不符");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase03() {
		ReporterCSS.title("测试点: 按入库时间查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(2);
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> in_stock_times = targetInStockRecordArray.stream()
					.filter(s -> !s.getSubmit_time().equals(in_stock_time)).map(r -> r.getSubmit_time())
					.collect(Collectors.toList());

			Assert.assertEquals(in_stock_times.size(), 0,
					"按入库时间查询入库记录,过滤出了以下提交时间 " + in_stock_times + " 的入库记录,与过滤条件不符");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase04() {
		ReporterCSS.title("测试点: 按[提交时间+商品ID]查询入库记录");
		try {
			InStockDetailInfoBean.Detail detail = details.get(0);
			String sku_id = detail.getId();
			List<InStockDetailInfoBean.Detail> tempDetail = new ArrayList<InStockDetailInfoBean.Detail>();
			tempDetail.add(detail);

			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(1);
			inStockRecordFilterParam.setBegin(submit_time);
			inStockRecordFilterParam.setEnd(submit_time);
			inStockRecordFilterParam.setText(sku_id);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(tempDetail, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> sku_ids = targetInStockRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());
			Assert.assertEquals(sku_ids.size(), 0, "按[提交时间+商品ID]查询入库记录,查询出了如下商品ID " + sku_ids + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase05() {
		ReporterCSS.title("测试点: 按[入库时间+商品ID]查询入库记录");
		try {
			InStockDetailInfoBean.Detail detail = details.get(0);
			String sku_id = detail.getId();
			List<InStockDetailInfoBean.Detail> tempDetail = new ArrayList<InStockDetailInfoBean.Detail>();
			tempDetail.add(detail);

			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(2);
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);
			inStockRecordFilterParam.setText(sku_id);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(tempDetail, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> sku_ids = targetInStockRecordArray.stream().filter(r -> !r.getSku_id().equals(sku_id))
					.map(r -> r.getSku_id()).collect(Collectors.toList());
			Assert.assertEquals(sku_ids.size(), 0, "按[入库时间+商品ID]查询入库记录,查询出了如下商品ID " + sku_ids + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase06() {
		ReporterCSS.title("测试点: 按[提交时间+商品名称]查询入库记录");
		try {
			InStockDetailInfoBean.Detail detail = details.get(0);
			String sku_name = detail.getName();
			List<InStockDetailInfoBean.Detail> tempDetail = new ArrayList<InStockDetailInfoBean.Detail>();
			tempDetail.add(detail);

			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(1);
			inStockRecordFilterParam.setBegin(submit_time);
			inStockRecordFilterParam.setEnd(submit_time);
			inStockRecordFilterParam.setText(sku_name);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(tempDetail, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> sku_names = targetInStockRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getName()).collect(Collectors.toList());
			Assert.assertEquals(sku_names.size(), 0, "按[提交时间+商品名称]查询入库记录,查询出了如下商品名称 " + sku_names + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase07() {
		ReporterCSS.title("测试点: 按[入库时间+商品名称]查询入库记录");
		try {
			InStockDetailInfoBean.Detail detail = details.get(0);
			String sku_name = detail.getName();
			List<InStockDetailInfoBean.Detail> tempDetail = new ArrayList<InStockDetailInfoBean.Detail>();
			tempDetail.add(detail);

			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(2);
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);
			inStockRecordFilterParam.setText(sku_name);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(tempDetail, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> sku_names = targetInStockRecordArray.stream().filter(r -> !r.getName().contains(sku_name))
					.map(r -> r.getName()).collect(Collectors.toList());
			Assert.assertEquals(sku_names.size(), 0, "按[入库时间+商品名称]查询入库记录,查询出了如下商品名称 " + sku_names + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase08() {
		ReporterCSS.title("测试点: 按[提交时间+供应商]查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(1);
			inStockRecordFilterParam.setBegin(submit_time);
			inStockRecordFilterParam.setEnd(submit_time);
			inStockRecordFilterParam.setSettle_supplier_id(inStockDetailInfo.getSettle_supplier_id());
			String supplier_name = inStockDetailInfo.getSupplier_name();

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> supplier_names = targetInStockRecordArray.stream()
					.filter(r -> !r.getSupplier_name().contains(supplier_name)).map(r -> r.getName())
					.collect(Collectors.toList());
			Assert.assertEquals(supplier_names.size(), 0,
					"按[提交时间+供应商]查询入库记录,查询出了如下供应商 " + supplier_names + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase09() {
		ReporterCSS.title("测试点: 按[入库时间+供应商]查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(2);
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);
			inStockRecordFilterParam.setSettle_supplier_id(inStockDetailInfo.getSettle_supplier_id());
			String supplier_name = inStockDetailInfo.getSupplier_name();

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> supplier_names = targetInStockRecordArray.stream()
					.filter(r -> !r.getSupplier_name().contains(supplier_name)).map(r -> r.getName())
					.collect(Collectors.toList());
			Assert.assertEquals(supplier_names.size(), 0,
					"按[入库时间+供应商]查询入库记录,查询出了如下供应商 " + supplier_names + " 的商品入库记录,不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase10() {
		ReporterCSS.title("测试点: 按[提交时间+商品分类]查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(1);
			inStockRecordFilterParam.setBegin(submit_time);
			inStockRecordFilterParam.setEnd(submit_time);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> category_id_1 = new ArrayList<String>();
			List<String> category_id_2 = new ArrayList<String>();
			for (InStockRecordBean inStockRecord : targetInStockRecordArray) {
				if (!category_id_1.contains(inStockRecord.getCategory_id_1())) {
					category_id_1.add(inStockRecord.getCategory_id_1());
				}

				if (!category_id_2.contains(inStockRecord.getCategory_id_2())) {
					category_id_2.add(inStockRecord.getCategory_id_2());
				}
			}

			offset = 0;
			inStockRecordFilterParam.setCategory_id_1(category_id_1);
			inStockRecordFilterParam.setCategory_id_2(category_id_2);

			inStockRecordArray = new ArrayList<InStockRecordBean>();
			while (true) {
				inStockRecordFilterParam.setOffset(offset);
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
			}

			// 把之前创建的入库单的入库记录过滤出来
			targetInStockRecordArray = inStockRecordArray.stream().filter(s -> sheet_id.equals(s.getSheet_no()))
					.collect(Collectors.toList());

			result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> category_ids = targetInStockRecordArray.stream().filter(
					r -> !category_id_1.contains(r.getCategory_id_1()) || !category_id_2.contains(r.getCategory_id_2()))
					.map(r -> r.getCategory_id_1() + "-" + r.getCategory_id_2()).collect(Collectors.toList());
			Assert.assertEquals(category_ids.size(), 0,
					"按[提交时间+商品分类]查询入库记录,过滤出了如下分类 " + category_ids + ",不符合过滤条件的入库记录");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	@Test
	public void inStockRecordTestCase11() {
		ReporterCSS.title("测试点: 按[入库时间+商品分类]查询入库记录");
		try {
			inStockRecordFilterParam = new InStockRecordFilterParam();
			inStockRecordFilterParam.setTime_type(2);
			inStockRecordFilterParam.setBegin(in_stock_time);
			inStockRecordFilterParam.setEnd(in_stock_time);

			int offset = 0;
			int limit = 50;
			inStockRecordFilterParam.setOffset(offset);
			inStockRecordFilterParam.setLimit(limit);

			List<InStockRecordBean> inStockRecordArray = new ArrayList<InStockRecordBean>();
			List<InStockRecordBean> tmpArry = null;
			while (true) {
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
				inStockRecordFilterParam.setOffset(offset);
			}

			// 把之前创建的入库单的入库记录过滤出来
			List<InStockRecordBean> targetInStockRecordArray = inStockRecordArray.stream()
					.filter(s -> sheet_id.equals(s.getSheet_no())).collect(Collectors.toList());

			boolean result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> category_id_1 = new ArrayList<String>();
			List<String> category_id_2 = new ArrayList<String>();
			for (InStockRecordBean inStockRecord : targetInStockRecordArray) {
				if (!category_id_1.contains(inStockRecord.getCategory_id_1())) {
					category_id_1.add(inStockRecord.getCategory_id_1());

				}
				if (!category_id_2.contains(inStockRecord.getCategory_id_2())) {
					category_id_2.add(inStockRecord.getCategory_id_2());
				}
			}
			offset = 0;
			inStockRecordFilterParam.setCategory_id_1(category_id_1);
			inStockRecordFilterParam.setCategory_id_2(category_id_2);

			inStockRecordArray = new ArrayList<InStockRecordBean>();
			while (true) {
				inStockRecordFilterParam.setOffset(offset);
				tmpArry = stockRecordService.inStockRecords(inStockRecordFilterParam);
				Assert.assertNotEquals(tmpArry, null, "获取成品入库日志失败");
				inStockRecordArray.addAll(tmpArry);
				if (tmpArry.size() < limit) {
					break;
				}
				offset += limit;
			}

			// 把之前创建的入库单的入库记录过滤出来
			targetInStockRecordArray = inStockRecordArray.stream().filter(s -> sheet_id.equals(s.getSheet_no()))
					.collect(Collectors.toList());

			result = compareData(details, targetInStockRecordArray);
			Assert.assertEquals(result, true, "入库单 " + sheet_id + " 的入库日志存在问题");

			List<String> category_ids = targetInStockRecordArray.stream().filter(
					r -> !category_id_1.contains(r.getCategory_id_1()) || !category_id_2.contains(r.getCategory_id_2()))
					.map(r -> r.getCategory_id_1() + "-" + r.getCategory_id_2()).collect(Collectors.toList());
			Assert.assertEquals(category_ids.size(), 0,
					"按[入库时间+商品分类]查询入库记录,过滤出了如下分类 " + category_ids + ",不符合过滤条件的入库记录");
		} catch (Exception e) {
			logger.error("搜索查询采购入库记录遇到错误: ", e);
			Assert.fail("搜索查询采购入库记录遇到错误: ", e);
		}
	}

	public boolean compareData(List<InStockDetailInfoBean.Detail> details,
			List<InStockRecordBean> targetInStockRecordArray) {
		boolean result = true;
		String msg = null;
		// 验证入库单里的入库商品,是不是每个商品都记录了入库记录,验证入库数和入库金额是否正确
		for (InStockDetailInfoBean.Detail det : details) {
			InStockRecordBean inStockRecord = targetInStockRecordArray.stream()
					.filter(r -> r.getSku_id().equals(det.getId())).findAny().orElse(null);
			if (inStockRecord == null) {
				msg = String.format("入库单%s中的商品 %s 没有记录入库日志", sheet_id, det.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (inStockRecord.getIn_stock_amount().compareTo(det.getQuantity()) != 0) {
				msg = String.format("入库单%s中的商品 %s 没有记录的入库数与预期不一致,预期:%s,实际:%s", sheet_id, det.getName(),
						det.getQuantity(), inStockRecord.getIn_stock_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (inStockRecord.getAll_price().compareTo(det.getMoney()) != 0) {
				msg = String.format("入库单%s中的商品 %s 的入库金额与预期不一致,预期:%s,实际:%s", sheet_id, det.getName(), det.getMoney(),
						inStockRecord.getAll_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

}
