package cn.guanmai.open.stock.abnormal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetBean;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetFiterParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockRefundServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.stock.OpenStockRefundService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.stock.OpenStockInTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月4日
 * @time 下午2:57:23
 * @des TODO
 */

public class OpenStockRefundAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockInTest.class);
	private OpenStockRefundService openStockRefundService;
	private OpenCategoryService openCategoryService;
	private OpenSupplierService openSupplierService;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OpenStockRefundSheetCommonParam stockRefundSheetCommonParam;
	private OpenSupplierDetailBean openSupplierDetail;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockRefundService = new OpenStockRefundServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
	}

	@BeforeMethod
	public void beforeMethod() {
		stockRefundSheetCommonParam = new OpenStockRefundSheetCommonParam();
		try {
			String category2_id = null, supplier_id = null;
			if (openSupplierDetail == null) {
				List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
				Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

				Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

				OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

				supplier_id = supplier.getSupplier_id();
				openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
				Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

				List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
				Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

				category2_id = category2List.get(0).getCategory2_id();

			} else {
				supplier_id = openSupplierDetail.getSupplier_id();
				List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
				category2_id = category2List.get(0).getCategory2_id();
			}

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			List<OpenStockRefundSheetCommonParam.Detail> details = new ArrayList<OpenStockRefundSheetCommonParam.Detail>();
			OpenStockRefundSheetCommonParam.Detail detail = null;
			for (OpenPurchaseSpecBean purchaseSpec : openPurchaseSpecList) {
				detail = stockRefundSheetCommonParam.new Detail();
				detail.setRefund_count(NumberUtil.getRandomNumber(5, 20, 2).toString());
				detail.setRefund_unit_price(NumberUtil.getRandomNumber(5, 15, 2).toString());
				detail.setSpec_id(purchaseSpec.getSpec_id());
				details.add(detail);
			}

			Assert.assertEquals(details.size() >= 2, true, "退货商品数不足2,无法进行后续操作");
			stockRefundSheetCommonParam.setSupplier_id(supplier_id);
			stockRefundSheetCommonParam.setSubmit_date(todayStr);
			stockRefundSheetCommonParam.setDetails(details);
		} catch (Exception e) {
			logger.error("初始化创建采购退货单参数遇到错误", e);
			Assert.fail("初始化创建采购退货单参数遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase01() {
		ReporterCSS.title("测试点:  新建采购退货单,供应商ID输入为空,断言失败");
		try {
			stockRefundSheetCommonParam.setSupplier_id("");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,供应商ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase02() {
		ReporterCSS.title("测试点:  新建采购退货单,供应商ID输入异常值,断言失败");
		try {
			stockRefundSheetCommonParam.setSupplier_id("T7442");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,供应商ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase03() {
		ReporterCSS.title("测试点:  新建采购退货单,提交时间传入为空,断言失败");
		try {
			stockRefundSheetCommonParam.setSubmit_date("");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,提交时间传入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase04() {
		ReporterCSS.title("测试点:  新建采购退货单,提交时间传入非指定格式,断言失败");
		try {
			stockRefundSheetCommonParam.setSubmit_date("2019.11.11");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,提交时间传入非指定格式,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase05() {
		ReporterCSS.title("测试点:  新建采购退货单,details内容传入为空,断言失败");
		try {
			stockRefundSheetCommonParam.setDetails(new ArrayList<>());
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,details内容传入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase06() {
		ReporterCSS.title("测试点:  新建采购退货单,采购规格传入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setSpec_id("");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,采购规格传入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase07() {
		ReporterCSS.title("测试点:  新建采购退货单,采购规格传入为非法值,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setSpec_id("D111111");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,采购规格传入为非法值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase08() {
		ReporterCSS.title("测试点:  新建采购退货单,退货数量输入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_count("");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货数量输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase09() {
		ReporterCSS.title("测试点:  新建采购退货单,退货数量输入为非数字,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_count("a");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货数量输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase10() {
		ReporterCSS.title("测试点:  新建采购退货单,退货数量输入为0,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_count("0");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货数量输入为0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase11() {
		ReporterCSS.title("测试点:  新建采购退货单,退货数量输入为负数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_count("-2");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货数量输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase12() {
		ReporterCSS.title("测试点:  新建采购退货单,退货数量输入过长的小数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_count("2.345");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货数量输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase13() {
		ReporterCSS.title("测试点:  新建采购退货单,退货单价输入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_unit_price("");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货单价输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase14() {
		ReporterCSS.title("测试点:  新建采购退货单,退货单价输入为非数字,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_unit_price("a");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货单价输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase15() {
		ReporterCSS.title("测试点:  新建采购退货单,退货单价输入为负数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_unit_price("-2");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货单价输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase16() {
		ReporterCSS.title("测试点:  新建采购退货单,退货单价输入过长的小数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_unit_price("2.345");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货单价输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase17() {
		ReporterCSS.title("测试点:  新建采购退货单,退货金额输入非数字,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_amount("a");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货金额输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase18() {
		ReporterCSS.title("测试点:  新建采购退货单,退货金额输入负数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_amount("-10");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货金额输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase19() {
		ReporterCSS.title("测试点:  新建采购退货单,退货金额输入过长小数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Detail> details = stockRefundSheetCommonParam.getDetails();
			details.get(0).setRefund_amount("6.1234");
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,退货金额输入过长小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase20() {
		ReporterCSS.title("测试点:  新建采购退货单,折让原因输入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("2");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让原因输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase21() {
		ReporterCSS.title("测试点:  新建采购退货单,折让原因输入为非后选值,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("0");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("2");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让原因输入为非后选值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase22() {
		ReporterCSS.title("测试点:  新建采购退货单,折让类型输入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("");
			discount.setDiscount_amount("2");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让类型输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase23() {
		ReporterCSS.title("测试点:  新建采购退货单,折让类型输入为非后选值,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("0");
			discount.setDiscount_amount("2");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让类型输入为非后选值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase24() {
		ReporterCSS.title("测试点:  新建采购退货单,折让金额输入为空,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("0");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让金额输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase25() {
		ReporterCSS.title("测试点:  新建采购退货单,折让金额输入为负数,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("-2");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让金额输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase26() {
		ReporterCSS.title("测试点:  新建采购退货单,折让金额输入为0,断言失败");
		try {
			List<OpenStockRefundSheetCommonParam.Discount> discounts = new ArrayList<OpenStockRefundSheetCommonParam.Discount>();
			OpenStockRefundSheetCommonParam.Discount discount = stockRefundSheetCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("0");
			discounts.add(discount);
			stockRefundSheetCommonParam.setDiscounts(discounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertEquals(stock_refund_sheet_id, null, "新建采购退货单,折让金额输入为0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase27() {
		ReporterCSS.title("测试点:  提交采购退货单据,采购退货单据输入为空,断言失败");
		try {
			boolean result = openStockRefundService.submitStockRefundSheet("");
			Assert.assertEquals(result, false, "提交采购退货单据,采购退货单据输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase28() {
		ReporterCSS.title("测试点:  提交采购退货单据,提交已经冲销的采购退货单据,断言失败");
		try {
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "正常创建采购退货单,断言成功");

			boolean result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "正常冲销采购退货单据,断言成功");

			result = openStockRefundService.submitStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, false, "提交采购退货单据,提交已经冲销的采购退货单据,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase29() {
		ReporterCSS.title("测试点:  冲销采购退货单,退货单号输入为空,断言失败");
		try {
			boolean result = openStockRefundService.revertStockRefundSheet("");
			Assert.assertEquals(result, false, "冲销采购退货单,退货单号输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase30() {
		ReporterCSS.title("测试点:  冲销采购退货单,进行重复冲销,断言失败");
		try {
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "正常创建采购退货单,断言成功");

			boolean result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "正常冲销采购退货单据,断言成功");

			result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, false, "冲销采购退货单,进行重复冲销,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase31() {
		ReporterCSS.title("测试点:  审核不通过采购退货单,冲销采购单据后再审核不通过采购退货单,断言失败");
		try {
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCommonParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "正常创建采购退货单,断言成功");

			boolean result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "正常冲销采购退货单据,断言成功");

			result = openStockRefundService.rejectStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, false, "审核不通过采购退货单,冲销采购单据后再审核不通过采购退货单,断言失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase32() {
		ReporterCSS.title("测试点:  采购退货单过滤,时间输入为空,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date("");
			stockRefundSheetFiterParam.setEnd_date("");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,时间输入为空,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase33() {
		ReporterCSS.title("测试点:  采购退货单过滤,时间输入为非法格式,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date("2019.11.11");
			stockRefundSheetFiterParam.setEnd_date("2019.11.11");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,时间输入为非法格式,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase34() {
		ReporterCSS.title("测试点:  采购退货单过滤,输入起始时间晚于结束时间,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date("2019-11-11");
			stockRefundSheetFiterParam.setEnd_date("2019-11-01");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,输入起始时间晚于结束时间,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase35() {
		ReporterCSS.title("测试点:  采购退货单过滤,状态值输入非法值,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setStatus("A");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,状态值输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase36() {
		ReporterCSS.title("测试点:  采购退货单过滤,状态值输入非候选值,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setStatus("6");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,状态值输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase37() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页offset输入非数值,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setOffset("a");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页offset输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase38() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页offset输入负数,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setOffset("-1");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页offset输入负数,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase39() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页offset输入小数,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setOffset("1.2");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页offset输入小数,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase40() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页limit输入非数字,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setLimit("A");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页limit输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase41() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页limit输入负数,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setLimit("-20");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页limit输入负数,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase42() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页limit输入0,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setLimit("0");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页limit输入0,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase43() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页limit输入小数,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setLimit("20.1");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页limit输入小数,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}

	@Test
	public void stockRefundAbnormalTestCase44() {
		ReporterCSS.title("测试点:  采购退货单过滤,分页limit输入过大值,断言失败");
		try {
			OpenStockRefundSheetFiterParam stockRefundSheetFiterParam = new OpenStockRefundSheetFiterParam();
			stockRefundSheetFiterParam.setSearch_type("1");
			stockRefundSheetFiterParam.setStart_date(todayStr);
			stockRefundSheetFiterParam.setEnd_date(todayStr);
			stockRefundSheetFiterParam.setLimit("101");
			List<OpenStockRefundSheetBean> stockRefundSheetList = openStockRefundService
					.queryStockRefundSheet(stockRefundSheetFiterParam);
			Assert.assertEquals(stockRefundSheetList, null, "采购退货单过滤,分页limit输入过大值,断言失败");
		} catch (Exception e) {
			logger.error("采购退货单过滤遇到错误", e);
			Assert.fail("采购退货单过滤遇到错误", e);
		}
	}
}
