package cn.guanmai.open.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetCommonParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.bean.stock.OpenStockInSheetBean;
import cn.guanmai.open.bean.stock.OpenStockInSheetDetailBean;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockInCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockInSheetFilterParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.purchase.OpenPurcahseServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockInServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.interfaces.stock.OpenStockInService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年10月23日
 * @time 下午4:35:46
 * @des TODO
 */

public class OpenStockInTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockInTest.class);
	private OpenStockInService openStockInService;
	private OpenCategoryService openCategoryService;
	private OpenSupplierService openSupplierService;
	private OpenPurcahseService openPurcahseService;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockInService = new OpenStockInServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
	}

	@Test
	public void openStockInTestCase01() {
		ReporterCSS.title("测试点: 新建采购入库单");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam openStockInCommonParam = new OpenStockInCommonParam();
			openStockInCommonParam.setSupplier_id(supplier_id);
			openStockInCommonParam.setSubmit_date(todayStr);
			openStockInCommonParam.setDiscounts(new ArrayList<>());

			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = openStockInCommonParam.new Detail();
				detail.setIn_stock_count(NumberUtil.getRandomNumber(10, 20, 2).toString());
				detail.setIn_stock_unit_price(NumberUtil.getRandomNumber(5, 10, 2).toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				details.add(detail);
				if (details.size() >= 20) {
					break;
				}
			}
			openStockInCommonParam.setDetails(details);

			String stockInSheetId = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!stockInSheetDetail.getSupplier_id().equals(supplier_id)) {
				msg = String.format("采购入库单%s的供应商ID与预期的不一致,预期:%s,实际:%s", stockInSheetId, supplier_id,
						stockInSheetDetail.getSupplier_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!stockInSheetDetail.getCreate_date().equals(todayStr)) {
				msg = String.format("采购入库单%s的提交时间与预期的不一致,预期:%s,实际:%s", stockInSheetId, todayStr,
						stockInSheetDetail.getCreate_date());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockInSheetDetail.getStatus() != 1) {
				msg = String.format("采购入库单%s的状态值与预期的不一致,预期:%s,实际:%s", stockInSheetId, 1,
						stockInSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OpenStockInSheetDetailBean.Detail> resultDetails = stockInSheetDetail.getDetails();
			for (OpenStockInCommonParam.Detail paramDetail : details) {
				OpenStockInSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(paramDetail.getSpec_id())).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("采购入库单%s缺少入库商品%s", stockInSheetId, paramDetail.getSpec_id());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (resultDetail.getIn_stock_count().compareTo(new BigDecimal(paramDetail.getIn_stock_count())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库数与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_count(), resultDetail.getIn_stock_count());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDetail.getIn_stock_unit_price()
						.compareTo(new BigDecimal(paramDetail.getIn_stock_unit_price())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库单价与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_unit_price(), resultDetail.getIn_stock_unit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "采购入库单 " + stockInSheetId + " 详细信息与提交填写的参数不一致");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase02() {
		ReporterCSS.title("测试点: 新建采购入库单,并添加折扣");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam openStockInCommonParam = new OpenStockInCommonParam();
			openStockInCommonParam.setSupplier_id(supplier_id);
			openStockInCommonParam.setSubmit_date(todayStr);

			BigDecimal expected_total_amount = BigDecimal.ZERO;
			BigDecimal expected_sku_amount = BigDecimal.ZERO;

			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = openStockInCommonParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				detail.setIn_stock_count(in_stock_count.toString());
				detail.setIn_stock_unit_price(in_stock_unit_price.toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				details.add(detail);
				expected_sku_amount = expected_sku_amount.add(in_stock_count.multiply(in_stock_unit_price));
				if (details.size() >= 20) {
					break;
				}
			}
			openStockInCommonParam.setDetails(details);

			OpenStockInCommonParam.Discount discount_1 = openStockInCommonParam.new Discount();
			BigDecimal discount_amount1 = new BigDecimal("2");
			discount_1.setDiscount_action("1");
			discount_1.setDiscount_reason("1");
			discount_1.setDiscount_amount(discount_amount1.toString());
			discount_1.setRemark(StringUtil.getRandomNumber(6));

			OpenStockInCommonParam.Discount discount_2 = openStockInCommonParam.new Discount();
			BigDecimal discount_amount2 = new BigDecimal("3");
			discount_2.setDiscount_action("1");
			discount_2.setDiscount_reason("1");
			discount_2.setDiscount_amount(discount_amount2.toString());
			discount_2.setRemark(StringUtil.getRandomNumber(6));

			List<OpenStockInCommonParam.Discount> paramDiscounts = new ArrayList<OpenStockInCommonParam.Discount>();
			paramDiscounts.add(discount_1);
			paramDiscounts.add(discount_2);

			openStockInCommonParam.setDiscounts(paramDiscounts);

			String stockInSheetId = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!stockInSheetDetail.getSupplier_id().equals(supplier_id)) {
				msg = String.format("采购入库单%s的供应商ID与预期的不一致,预期:%s,实际:%s", stockInSheetId, supplier_id,
						stockInSheetDetail.getSupplier_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!stockInSheetDetail.getCreate_date().equals(todayStr)) {
				msg = String.format("采购入库单%s的提交时间与预期的不一致,预期:%s,实际:%s", stockInSheetId, todayStr,
						stockInSheetDetail.getCreate_date());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OpenStockInSheetDetailBean.Detail> resultDetails = stockInSheetDetail.getDetails();
			for (OpenStockInCommonParam.Detail paramDetail : details) {
				OpenStockInSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(paramDetail.getSpec_id())).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("采购入库单%s缺少入库商品%s", stockInSheetId, paramDetail.getSpec_id());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (resultDetail.getIn_stock_count().compareTo(new BigDecimal(paramDetail.getIn_stock_count())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库数与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_count(), resultDetail.getIn_stock_count());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDetail.getIn_stock_unit_price()
						.compareTo(new BigDecimal(paramDetail.getIn_stock_unit_price())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库单价与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_unit_price(), resultDetail.getIn_stock_unit_price());
					result = false;
					logger.warn(msg);
					ReporterCSS.warn(msg);
				}
			}
			expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (stockInSheetDetail.getSku_amount().compareTo(expected_sku_amount) != 0) {
				msg = String.format("采购入库单%s的商品总金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, expected_sku_amount,
						stockInSheetDetail.getSku_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			expected_total_amount = expected_sku_amount.add(discount_amount1).add(discount_amount2);
			if (stockInSheetDetail.getTotal_amount().compareTo(expected_total_amount) != 0) {
				msg = String.format("采购入库单%s的入库金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, expected_total_amount,
						stockInSheetDetail.getTotal_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			BigDecimal discount_amount = discount_amount1.add(discount_amount2);
			if (discount_amount.compareTo(stockInSheetDetail.getDiscount_amount()) != 0) {
				msg = String.format("采购入库单%s的折让金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, discount_amount,
						stockInSheetDetail.getDiscount_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OpenStockInSheetDetailBean.Discount> resultDiscounts = stockInSheetDetail.getDiscounts();
			for (OpenStockInCommonParam.Discount paramDiscount : paramDiscounts) {
				String remark = paramDiscount.getRemark();
				OpenStockInSheetDetailBean.Discount resultDiscount = resultDiscounts.parallelStream()
						.filter(d -> d.getRemark().equals(remark)).findAny().orElse(null);
				if (resultDiscount == null) {
					msg = String.format("采购入库单%s少了一条备注记录为%s的折让金额", stockInSheetId, remark);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (resultDiscount.getDiscount_action() != Integer.valueOf(paramDiscount.getDiscount_action())) {
					msg = String.format("采购入库单%s中备注为%s的折让条目的折让类型与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_action(), resultDiscount.getDiscount_action());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDiscount.getDiscount_reason() != Integer.valueOf(paramDiscount.getDiscount_reason())) {
					msg = String.format("采购入库单%s中备注为%s折让条目的折让原因与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_reason(), resultDiscount.getDiscount_reason());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDiscount.getDiscount_amount()
						.compareTo(new BigDecimal(paramDiscount.getDiscount_amount())) != 0) {
					msg = String.format("采购入库单%s中备注为%s折让条目的折让原因与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_amount(), resultDiscount.getDiscount_amount());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "采购入库单 " + stockInSheetId + " 详细信息与提交填写的参数不一致");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase03() {
		ReporterCSS.title("测试点: 修改采购入库单,添加折扣");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam openStockInCommonParam = new OpenStockInCommonParam();
			openStockInCommonParam.setSupplier_id(supplier_id);
			openStockInCommonParam.setSubmit_date(todayStr);

			BigDecimal expected_total_amount = BigDecimal.ZERO;
			BigDecimal expected_sku_amount = BigDecimal.ZERO;

			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = openStockInCommonParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				detail.setIn_stock_count(in_stock_count.toString());
				detail.setIn_stock_unit_price(in_stock_unit_price.toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				details.add(detail);
				expected_sku_amount = expected_sku_amount.add(in_stock_count.multiply(in_stock_unit_price));
				if (details.size() >= 20) {
					break;
				}
			}

			openStockInCommonParam.setDetails(details);
			String stockInSheetId = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam.Discount discount_1 = openStockInCommonParam.new Discount();
			BigDecimal discount_amount1 = new BigDecimal("2");
			discount_1.setDiscount_action("1");
			discount_1.setDiscount_reason("1");
			discount_1.setDiscount_amount(discount_amount1.toString());
			discount_1.setRemark(StringUtil.getRandomNumber(6));

			OpenStockInCommonParam.Discount discount_2 = openStockInCommonParam.new Discount();
			BigDecimal discount_amount2 = new BigDecimal("3");
			discount_2.setDiscount_action("1");
			discount_2.setDiscount_reason("1");
			discount_2.setDiscount_amount(discount_amount2.toString());
			discount_2.setRemark(StringUtil.getRandomNumber(6));

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			discounts.add(discount_1);
			discounts.add(discount_2);

			OpenStockInCommonParam stockInUpdateParam = new OpenStockInCommonParam();
			stockInUpdateParam.setIn_stock_sheet_id(stockInSheetId);
			stockInUpdateParam.setSubmit_date(TimeUtil.calculateTime("yyyy-MM-dd", todayStr, 1, Calendar.DATE));
			stockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(stockInUpdateParam);
			Assert.assertEquals(result, true, "修改采购入库单失败");

			expected_total_amount = expected_sku_amount.add(discount_amount1).add(discount_amount2);

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			String msg = null;
			if (!stockInSheetDetail.getIn_stock_date().equals(stockInUpdateParam.getSubmit_date())) {
				msg = String.format("采购入库单%s的提交时间与预期的不一致,预期:%s,实际:%s", stockInSheetId,
						stockInUpdateParam.getSubmit_date(), stockInSheetDetail.getCreate_date());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<OpenStockInSheetDetailBean.Detail> resultDetails = stockInSheetDetail.getDetails();
			for (OpenStockInCommonParam.Detail paramDetail : details) {
				OpenStockInSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(paramDetail.getSpec_id())).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("采购入库单%s缺少入库商品%s", stockInSheetId, paramDetail.getSpec_id());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (resultDetail.getIn_stock_count().compareTo(new BigDecimal(paramDetail.getIn_stock_count())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库数与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_count(), resultDetail.getIn_stock_count());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDetail.getIn_stock_unit_price()
						.compareTo(new BigDecimal(paramDetail.getIn_stock_unit_price())) != 0) {
					msg = String.format("采购入库单%s里库商品%s的入库单价与预期不符,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_unit_price(), resultDetail.getIn_stock_unit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}
			expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (stockInSheetDetail.getSku_amount().compareTo(expected_sku_amount) != 0) {
				msg = String.format("采购入库单%s的商品总金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, expected_sku_amount,
						stockInSheetDetail.getSku_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			expected_total_amount = expected_sku_amount.add(discount_amount1).add(discount_amount2);
			if (stockInSheetDetail.getTotal_amount().compareTo(expected_total_amount) != 0) {
				msg = String.format("采购入库单%s的入库金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, expected_total_amount,
						stockInSheetDetail.getTotal_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			BigDecimal discount_amount = discount_amount1.add(discount_amount2);
			if (discount_amount.compareTo(stockInSheetDetail.getDiscount_amount()) != 0) {
				msg = String.format("采购入库单%s的折让金额与预期的不一致,预期:%s,实际:%s", stockInSheetId, discount_amount,
						stockInSheetDetail.getDiscount_amount());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<OpenStockInSheetDetailBean.Discount> resultDiscounts = stockInSheetDetail.getDiscounts();
			for (OpenStockInCommonParam.Discount paramDiscount : discounts) {
				String remark = paramDiscount.getRemark();
				OpenStockInSheetDetailBean.Discount resultDiscount = resultDiscounts.parallelStream()
						.filter(d -> d.getRemark().equals(remark)).findAny().orElse(null);
				if (resultDiscount == null) {
					msg = String.format("采购入库单%s少了一条备注记录为%s的折让金额", stockInSheetId, remark);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (resultDiscount.getDiscount_action() != Integer.valueOf(paramDiscount.getDiscount_action())) {
					msg = String.format("采购入库单%s中备注为%s的折让条目的折让类型与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_action(), resultDiscount.getDiscount_action());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDiscount.getDiscount_reason() != Integer.valueOf(paramDiscount.getDiscount_reason())) {
					msg = String.format("采购入库单%s中备注为%s折让条目的折让原因与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_reason(), resultDiscount.getDiscount_reason());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (resultDiscount.getDiscount_amount()
						.compareTo(new BigDecimal(paramDiscount.getDiscount_amount())) != 0) {
					msg = String.format("采购入库单%s中备注为%s折让条目的折让原因与预期不一致,预期:%s,实际:%s", stockInSheetId, remark,
							paramDiscount.getDiscount_amount(), resultDiscount.getDiscount_amount());
					result = false;
					logger.warn(msg);
					ReporterCSS.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "采购入库单 " + stockInSheetId + " 查询到的信息与修改填写的信息不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase04() {
		ReporterCSS.title("测试点: 修改采购入库单,添加条目数");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);

			BigDecimal expected_total_amount = BigDecimal.ZERO;
			BigDecimal expected_sku_amount = BigDecimal.ZERO;

			List<OpenStockInCommonParam.Detail> lastDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			List<OpenStockInCommonParam.Detail> addDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				detail.setIn_stock_count(in_stock_count.toString());
				detail.setIn_stock_unit_price(in_stock_unit_price.toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				expected_sku_amount = expected_sku_amount.add(in_stock_count.multiply(in_stock_unit_price));
				if (createDetails.size() <= 2) {
					createDetails.add(detail);
				} else {
					addDetails.add(detail);
				}

				if (addDetails.size() >= 5) {
					break;
				}
			}
			Assert.assertEquals(addDetails.size() > 0, true, "无可用添加采购条目商品,无法进行添加采购条目操作");

			stockInCreateParam.setDetails(createDetails);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = stockInCreateParam.new Discount();
			BigDecimal discount_amount = new BigDecimal("2");
			discount.setDiscount_action("1");
			discount.setDiscount_reason("1");
			discount.setDiscount_amount(discount_amount.toString());
			discount.setRemark(StringUtil.getRandomNumber(6));
			discounts.add(discount);
			stockInCreateParam.setDiscounts(discounts);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam addDetailsParam = new OpenStockInCommonParam();
			addDetailsParam.setIn_stock_sheet_id(stockInSheetId);
			addDetailsParam.setDetails(addDetails);
			boolean result = openStockInService.addStockInDetail(addDetailsParam);
			Assert.assertEquals(result, true, "采购入库单 " + stockInSheetId + "新增条目信息失败");

			lastDetails.addAll(createDetails);
			lastDetails.addAll(addDetails);

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			String msg = null;
			if (stockInSheetDetail.getSku_amount().compareTo(expected_sku_amount) != 0) {
				msg = String.format("采购入库单%s添加了入库条目后,商品总金额与预期不一致,预期:%s,实际:%s", stockInSheetId, expected_sku_amount,
						stockInSheetDetail.getSku_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			expected_total_amount = expected_sku_amount.add(discount_amount);
			if (stockInSheetDetail.getTotal_amount().compareTo(expected_total_amount) != 0) {
				msg = String.format("采购入库单%s添加了入库条目后,入库单总金额与预期不一致,预期:%s,实际:%s", stockInSheetId, expected_total_amount,
						stockInSheetDetail.getTotal_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OpenStockInSheetDetailBean.Detail> resultDetails = stockInSheetDetail.getDetails();

			for (OpenStockInCommonParam.Detail paramDetail : lastDetails) {
				OpenStockInSheetDetailBean.Detail resultDetail = resultDetails.parallelStream()
						.filter(d -> d.getSpec_id().equals(paramDetail.getSpec_id())).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("采购入库单%s中少了入库商品%s", stockInSheetId, paramDetail.getSpec_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!paramDetail.getIn_stock_count().equals(resultDetail.getIn_stock_count().toString())) {
					msg = String.format("采购入库单%s中商品%s的入库数与预期不一致,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_count(), resultDetail.getIn_stock_count());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!paramDetail.getIn_stock_unit_price().equals(resultDetail.getIn_stock_unit_price().toString())) {
					msg = String.format("采购入库单%s中商品%s的入库单价与预期不一致,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
							paramDetail.getIn_stock_unit_price(), resultDetail.getIn_stock_unit_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			List<OpenStockInSheetDetailBean.Discount> resultDiscounts = stockInSheetDetail.getDiscounts();
			if (resultDiscounts.size() != discounts.size()) {
				msg = String.format("采购入库单%s中添加的折扣条目数与预期不一致,预期:%s,实际:%s", stockInSheetId, discounts.size(),
						resultDiscounts.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase05() {
		ReporterCSS.title("测试点: 修改采购入库单,添加已存在的采购条目,断言失败");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				detail.setIn_stock_count(in_stock_count.toString());
				detail.setIn_stock_unit_price(in_stock_unit_price.toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(detail);
				if (createDetails.size() > 10) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam stockInUpdateParam = new OpenStockInCommonParam();
			stockInUpdateParam.setIn_stock_sheet_id(stockInSheetId);
			stockInUpdateParam.setDetails(createDetails);
			stockInUpdateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			boolean result = openStockInService.addStockInDetail(stockInUpdateParam);
			Assert.assertEquals(result, false, "采购入库单新增重复的采购条目,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase06() {
		ReporterCSS.title("测试点: 修改采购入库单,修改已存在的采购条目(多条)");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			List<OpenStockInCommonParam.Detail> updateDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail createDetail = null;
			OpenStockInCommonParam.Detail updateDetail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			BigDecimal expected_sku_amount = BigDecimal.ZERO;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);

				updateDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				updateDetail.setIn_stock_count(in_stock_count.toString());
				updateDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				updateDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				updateDetail.setRemark(StringUtil.getRandomString(6));
				updateDetails.add(updateDetail);

				expected_sku_amount = expected_sku_amount.add(in_stock_count.multiply(in_stock_unit_price));

				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam stockInUpdateParam = new OpenStockInCommonParam();
			stockInUpdateParam.setDetails(updateDetails);
			stockInUpdateParam.setIn_stock_sheet_id(stockInSheetId);

			boolean result = openStockInService.updateStockInDetail(stockInUpdateParam);
			Assert.assertEquals(result, true, "采购入库单,修改采购条目失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			String msg = null;
			if (stockInSheetDetail.getSku_amount().compareTo(expected_sku_amount) != 0) {
				msg = String.format("采购入库单%s修改采购条目后,商品总金额与预期不一致,预期:%s,实际:%s", stockInSheetId, expected_sku_amount,
						stockInSheetDetail.getSku_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			result = compareDetail(stockInSheetId, updateDetails, stockInSheetDetail.getDetails());
			Assert.assertEquals(result, true, "采购入库单,修改的后的采购条目信息与预期的不一致");

		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase07() {
		ReporterCSS.title("测试点: 修改采购入库单,修改已存在的采购条目(小于新建条目数)");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			List<OpenStockInCommonParam.Detail> updateDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail createDetail = null;
			OpenStockInCommonParam.Detail updateDetail = null;
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			BigDecimal expected_sku_amount = BigDecimal.ZERO;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);

				updateDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				updateDetail.setIn_stock_count(in_stock_count.toString());
				updateDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				updateDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				updateDetail.setRemark(StringUtil.getRandomString(6));

				if (updateDetails.size() == 0) {
					updateDetails.add(createDetail);
				} else {
					updateDetails.add(updateDetail);
				}

				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam stockInUpdateParam = new OpenStockInCommonParam();
			stockInUpdateParam.setDetails(updateDetails);
			stockInUpdateParam.setIn_stock_sheet_id(stockInSheetId);

			boolean result = openStockInService.updateStockInDetail(stockInUpdateParam);
			Assert.assertEquals(result, true, "采购入库单,修改采购条目失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			String msg = null;
			for (OpenStockInCommonParam.Detail detail : updateDetails) {
				expected_sku_amount = expected_sku_amount.add(new BigDecimal(detail.getIn_stock_count())
						.multiply(new BigDecimal(detail.getIn_stock_unit_price())));

			}
			expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			if (stockInSheetDetail.getSku_amount().compareTo(expected_sku_amount) != 0) {
				msg = String.format("采购入库单%s修改采购条目后,商品总金额与预期不一致,预期:%s,实际:%s", stockInSheetId, expected_sku_amount,
						stockInSheetDetail.getSku_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			result = compareDetail(stockInSheetId, updateDetails, stockInSheetDetail.getDetails());
			Assert.assertEquals(result, true, "采购入库单,修改的后的采购条目信息与预期的不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase08() {
		ReporterCSS.title("测试点: 修改采购入库单,删除采购条目数");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			Assert.assertEquals(createDetails.size() >= 2, true, "采购条目数不足2条,无法进行删除操作");

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			OpenStockInCommonParam.Detail removeDetail = NumberUtil.roundNumberInList(createDetails);
			String spec_id = removeDetail.getSpec_id();

			OpenStockInCommonParam stockInRemoveParam = new OpenStockInCommonParam();
			stockInRemoveParam.setIn_stock_sheet_id(stockInSheetId);
			stockInRemoveParam.setDetails(Arrays.asList(removeDetail));
			boolean result = openStockInService.deleteStockInDetail(stockInRemoveParam);
			Assert.assertEquals(result, true, "采购入库单,删除采购条目操作失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			List<OpenStockInSheetDetailBean.Detail> tempDetails = stockInSheetDetail.getDetails().parallelStream()
					.filter(d -> d.getSpec_id().equals(spec_id)).collect(Collectors.toList());
			Assert.assertEquals(tempDetails.size(), 0, "采购入库单" + stockInSheetId + ",删除的采购条目实际没有删除");

		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase09() {
		ReporterCSS.title("测试点: 修改采购入库单,删除采购条目数(删除多条)");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			Assert.assertEquals(createDetails.size() >= 3, true, "采购条目数不足3条,无法进行多条删除操作");

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			List<OpenStockInCommonParam.Detail> revomeDetails = new ArrayList<OpenStockInCommonParam.Detail>();
			for (int i = 0; i < createDetails.size(); i += 2) {
				revomeDetails.add(createDetails.get(i));
			}

			OpenStockInCommonParam stockInRemoveParam = new OpenStockInCommonParam();
			stockInRemoveParam.setIn_stock_sheet_id(stockInSheetId);
			stockInRemoveParam.setDetails(revomeDetails);
			boolean result = openStockInService.deleteStockInDetail(stockInRemoveParam);
			Assert.assertEquals(result, true, "采购入库单,删除采购条目操作失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详细信息失败");

			String msg = null;
			List<OpenStockInSheetDetailBean.Detail> resultDetails = stockInSheetDetail.getDetails();
			for (OpenStockInCommonParam.Detail revomeDetail : revomeDetails) {
				String spec_id = revomeDetail.getSpec_id();
				List<OpenStockInSheetDetailBean.Detail> tempDetails = resultDetails.parallelStream()
						.filter(d -> d.getSpec_id().contentEquals(spec_id)).collect(Collectors.toList());
				if (tempDetails.size() != 0) {
					msg = String.format("采购入库单%s删除的入库条目%s实际没有删除成功", stockInSheetId, spec_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购入库单" + stockInSheetId + " 存在没有实际删除的采购条目");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase10() {
		ReporterCSS.title("测试点: 提交采购单据");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			boolean result = openStockInService.submitStockInSheet(stockInSheetId);
			Assert.assertEquals(result, true, "提交采购单据失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详情失败");

			Assert.assertEquals(stockInSheetDetail.getStatus(), 2, "采购入库单" + stockInSheetId + "的状态值与预期的不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase11() {
		ReporterCSS.title("测试点: 冲销未提交的采购单据");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			boolean result = openStockInService.revertStockInsheet(stockInSheetId);
			Assert.assertEquals(result, true, "冲销采购单据失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详情失败");

			Assert.assertEquals(stockInSheetDetail.getStatus(), -1, "采购入库单" + stockInSheetId + "的状态值与预期的不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase12() {
		ReporterCSS.title("测试点: 冲销已提交的采购单据");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			boolean result = openStockInService.submitStockInSheet(stockInSheetId);
			Assert.assertEquals(result, true, "提交采购单据失败");

			result = openStockInService.revertStockInsheet(stockInSheetId);
			Assert.assertEquals(result, true, "冲销已提交的采购单据失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详情失败");

			Assert.assertEquals(stockInSheetDetail.getStatus(), -1, "采购入库单" + stockInSheetId + "的状态值与预期的不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase13() {
		ReporterCSS.title("测试点: 审核不通过已经提交的采购单据");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);
			stockInCreateParam.setSubmit_date(todayStr);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			boolean result = openStockInService.submitStockInSheet(stockInSheetId);
			Assert.assertEquals(result, true, "提交采购单据失败");

			result = openStockInService.rejectStockInSheet(stockInSheetId);
			Assert.assertEquals(result, true, "审核不通过已提交的采购单据失败");

			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(stockInSheetId);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单 " + stockInSheetId + " 详情失败");

			Assert.assertEquals(stockInSheetDetail.getStatus(), 0, "采购入库单" + stockInSheetId + "的状态值与预期的不一致");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase14() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单");
		try {
			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("2");
			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);

			List<OpenStockInSheetBean> stockInSheets = openStockInService.queryStockInSheet(filterParam);
			Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase15() {
		ReporterCSS.title("测试点: 按状态值搜索过滤采购入库单");
		try {
			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("2");

			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);

			List<OpenStockInSheetBean> stockInSheets = null;
			for (int i = -1; i < 4; i++) {
				filterParam.setStatus("" + i);
				stockInSheets = openStockInService.queryStockInSheet(filterParam);
				Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
				List<OpenStockInSheetBean> tempStockInSheets = stockInSheets.parallelStream()
						.filter(s -> s.getStatus() != Integer.valueOf(filterParam.getStatus()))
						.collect(Collectors.toList());
				Assert.assertEquals(tempStockInSheets.size(), 0, "按状态值搜索过滤采购入库单,搜索出了不符合状态值得采购入库单");
			}
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase16() {
		ReporterCSS.title("测试点: 按供应商搜索过滤采购入库单");
		try {
			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("2");

			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);

			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			List<OpenStockInSheetBean> stockInSheets = null;
			for (OpenSupplierBean supplier : suppliers) {
				String supplier_id = supplier.getSupplier_id();
				filterParam.setSupplier_id(supplier_id);
				for (int i = -1; i < 4; i++) {
					filterParam.setStatus("" + i);
					stockInSheets = openStockInService.queryStockInSheet(filterParam);
					Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
					List<OpenStockInSheetBean> tempStockInSheets = stockInSheets.parallelStream()
							.filter(s -> s.getStatus() != Integer.valueOf(filterParam.getStatus())
									|| !s.getSupplier_id().equals(supplier_id))
							.collect(Collectors.toList());
					Assert.assertEquals(tempStockInSheets.size(), 0, "按状态值搜索过滤采购入库单,搜索出了不符合状态值得采购入库单");
				}
			}
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase17() {
		ReporterCSS.title("测试点: 按供应商+状态搜索过滤采购入库单");
		try {
			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("1");

			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);

			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			List<OpenStockInSheetBean> stockInSheets = null;
			for (OpenSupplierBean supplier : suppliers.subList(0, suppliers.size() > 5 ? 5 : suppliers.size())) {
				String supplier_id = supplier.getSupplier_id();
				filterParam.setSupplier_id(supplier_id);
				stockInSheets = openStockInService.queryStockInSheet(filterParam);
				Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
				List<OpenStockInSheetBean> tempStockInSheets = stockInSheets.parallelStream()
						.filter(s -> !s.getSupplier_id().equals(supplier_id)).collect(Collectors.toList());
				Assert.assertEquals(tempStockInSheets.size(), 0, "按供应商搜索过滤采购入库单,搜索出了不符合状态值得采购入库单");
			}
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase18() {
		ReporterCSS.title("测试点: 按入库时间过滤采购入库单");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
			Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenStockInCommonParam stockInCreateParam = new OpenStockInCommonParam();
			stockInCreateParam.setSupplier_id(supplier_id);

			String in_stock_date = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, 7, Calendar.DATE);
			stockInCreateParam.setSubmit_date(in_stock_date);
			stockInCreateParam.setDiscounts(new ArrayList<OpenStockInCommonParam.Discount>());

			List<OpenStockInCommonParam.Detail> createDetails = new ArrayList<OpenStockInCommonParam.Detail>();

			OpenStockInCommonParam.Detail createDetail = null;

			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				createDetail = stockInCreateParam.new Detail();
				in_stock_count = NumberUtil.getRandomNumber(10, 20, 2);
				in_stock_unit_price = NumberUtil.getRandomNumber(5, 10, 2);
				createDetail.setIn_stock_count(in_stock_count.toString());
				createDetail.setIn_stock_unit_price(in_stock_unit_price.toString());
				createDetail.setSpec_id(openPurchaseSpec.getSpec_id());
				createDetail.setRemark(StringUtil.getRandomString(6));
				createDetails.add(createDetail);
				if (createDetails.size() > 8) {
					break;
				}
			}

			stockInCreateParam.setDetails(createDetails);

			String stockInSheetId = openStockInService.createStockInSheet(stockInCreateParam);
			Assert.assertNotEquals(stockInSheetId, null, "新建采购入库单失败");

			boolean result = openStockInService.submitStockInSheet(stockInSheetId);
			Assert.assertEquals(result, true, "提交采购入库单据失败");

			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("1");

			filterParam.setStart_date(in_stock_date);
			filterParam.setEnd_date(in_stock_date);

			List<OpenStockInSheetBean> stockInSheets = openStockInService.queryStockInSheet(filterParam);
			Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");

			List<OpenStockInSheetBean> tempStockInSheets = stockInSheets.parallelStream()
					.filter(s -> !s.getIn_stock_date().substring(0, 10).equals(in_stock_date))
					.collect(Collectors.toList());

			Assert.assertEquals(tempStockInSheets.size(), 0, "按入库时间过滤采购入库单,过滤出了不符合条件的数据");

			OpenStockInSheetBean tempStockInSheet = stockInSheets.parallelStream()
					.filter(s -> s.getIn_stock_sheet_id().equals(stockInSheetId)).findAny().orElse(null);
			Assert.assertNotEquals(tempStockInSheet, null, "按入库时间过滤采购入库单,没有找到目标入库单 " + stockInSheetId);

			filterParam.setStatus("2");
			stockInSheets = openStockInService.queryStockInSheet(filterParam);
			Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
			tempStockInSheets = stockInSheets.parallelStream()
					.filter(s -> !s.getIn_stock_date().substring(0, 10).equals(in_stock_date)
							|| s.getStatus() != Integer.valueOf(filterParam.getStatus()))
					.collect(Collectors.toList());

			Assert.assertEquals(tempStockInSheets.size(), 0, "按入库时间过滤采购入库单,过滤出了不符合条件的数据");
			tempStockInSheet = stockInSheets.parallelStream()
					.filter(s -> s.getIn_stock_sheet_id().equals(stockInSheetId)).findAny().orElse(null);
			Assert.assertNotEquals(tempStockInSheet, null, "按入库时间过滤采购入库单,没有找到目标入库单 " + stockInSheetId);

			filterParam.setStatus("1");
			stockInSheets = openStockInService.queryStockInSheet(filterParam);
			Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");
			tempStockInSheet = stockInSheets.parallelStream()
					.filter(s -> s.getIn_stock_sheet_id().equals(stockInSheetId)).findAny().orElse(null);
			Assert.assertEquals(tempStockInSheet, null, "搜索过滤待提交的入库单,把待审核的采购入库单" + stockInSheetId + " 搜索过滤出来了");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void openStockInTestCase19() {
		ReporterCSS.title("测试点: 采购单提交生成采购入库单,验证关联的采购单ID");
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建采购单据");

			OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

			String supplier_id = supplier.getSupplier_id();

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			openPurchaserFilterParam.setSupplier_id(supplier_id);
			List<OpenPurcahserBean> purcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
			Assert.assertNotEquals(purcahserList, null, "根据供应商查询采购员失败");

			Assert.assertEquals(purcahserList.size() > 0, true, "供应商 " + supplier_id + " 没有绑定任何采购员");
			OpenPurcahserBean openPurcahser = NumberUtil.roundNumberInList(purcahserList);

			OpenPurchaseSheetCommonParam purchaseSheetCreateParam = new OpenPurchaseSheetCommonParam();
			purchaseSheetCreateParam.setSupplier_id(supplier_id);
			purchaseSheetCreateParam.setRemark(StringUtil.getRandomString(6));
			purchaseSheetCreateParam.setPurchaser_id(openPurcahser.getPurchaser_id().toString());
			// purchaseSheetCreateParam.setPurcahser_name(openPurcahser.getPurchaser_name());

			List<OpenPurchaseSheetCommonParam.Detail> details = new ArrayList<OpenPurchaseSheetCommonParam.Detail>();
			OpenPurchaseSheetCommonParam.Detail detail = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = purchaseSheetCreateParam.new Detail();
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setPurchase_price(NumberUtil.getRandomNumber(5, 10, 2).toString());
				detail.setPurchase_count(NumberUtil.getRandomNumber(5, 10, 2).toString());
				detail.setRemark(StringUtil.getRandomString(6));
				details.add(detail);

				if (details.size() >= 12) {
					break;
				}
			}
			purchaseSheetCreateParam.setDetails(details);

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			boolean result = openPurcahseService.submitPurchaseSheet(purchase_sheet_id);
			Assert.assertEquals(result, true, "提交采购单据失败");

			OpenStockInSheetFilterParam filterParam = new OpenStockInSheetFilterParam();
			filterParam.setSearch_type("2");
			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);
			filterParam.setSupplier_id(supplier_id);

			List<OpenStockInSheetBean> stockInSheets = openStockInService.queryStockInSheet(filterParam);
			Assert.assertNotEquals(stockInSheets, null, "搜索过滤采购入库单失败");

			Assert.assertEquals(stockInSheets.size() > 0, true, "按供应商搜索购率采购入库单,结果条目数为0,与预期不符");

			OpenStockInSheetBean stockInSheet = stockInSheets.get(0);
			String sheet_id = stockInSheet.getIn_stock_sheet_id();
			OpenStockInSheetDetailBean stockInSheetDetail = openStockInService.getStockInSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInSheetDetail, null, "获取采购入库单详情信息失败");

			String msg = null;
			if (stockInSheetDetail.getPurchase_sheet_id() == null) {
				msg = String.format("采购单提交生成的采购入库单,对应关联的采购单ID为空,与预期不符");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!stockInSheetDetail.getPurchase_sheet_id().equals(purchase_sheet_id)) {
					msg = String.format("采购单提交生成的采购入库单,对应关联的采购单ID与预期不符,预期:%s,实际:%s,与预期不符",
							stockInSheetDetail.getPurchase_sheet_id(), purchase_sheet_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购入库单提交生成的采购入库单,关联的采购单信息与预期不符");
		} catch (Exception e) {
			logger.error("采购单提交生成采购入库单遇到错误", e);
			Assert.fail("采购单提交生成采购入库单遇到错误", e);
		}
	}

	public boolean compareDetail(String stockInSheetId, List<OpenStockInCommonParam.Detail> paramDetails,
			List<OpenStockInSheetDetailBean.Detail> resultDetails) {
		String msg = null;
		boolean result = true;
		for (OpenStockInCommonParam.Detail paramDetail : paramDetails) {
			OpenStockInSheetDetailBean.Detail resultDetail = resultDetails.parallelStream()
					.filter(d -> d.getSpec_id().equals(paramDetail.getSpec_id())).findAny().orElse(null);
			if (resultDetail == null) {
				msg = String.format("采购入库单%s中少了入库商品%s", stockInSheetId, paramDetail.getSpec_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (new BigDecimal(paramDetail.getIn_stock_count()).compareTo(resultDetail.getIn_stock_count()) != 0) {
				msg = String.format("采购入库单%s中商品%s的入库数与预期不一致,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
						paramDetail.getIn_stock_count(), resultDetail.getIn_stock_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (new BigDecimal(paramDetail.getIn_stock_unit_price())
					.compareTo(resultDetail.getIn_stock_unit_price()) != 0) {
				msg = String.format("采购入库单%s中商品%s的入库单价与预期不一致,预期:%s,实际:%s", stockInSheetId, paramDetail.getSpec_id(),
						paramDetail.getIn_stock_unit_price(), resultDetail.getIn_stock_unit_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}
}
