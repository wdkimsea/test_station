package cn.guanmai.open.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetBean;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetDetailBean;
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
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.DeepCloneUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年10月30日
 * @time 下午4:42:59
 * @des TODO
 */

public class OpenStockRefundTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockInTest.class);
	private OpenStockRefundService openStockRefundService;
	private OpenCategoryService openCategoryService;
	private OpenSupplierService openSupplierService;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockRefundService = new OpenStockRefundServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
	}

	public OpenStockRefundSheetCommonParam initStockRefundSheetParam() {
		OpenStockRefundSheetCommonParam stockRefundSheetCommonParam = new OpenStockRefundSheetCommonParam();
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
		return stockRefundSheetCommonParam;

	}

	@Test
	public void openStockRefundTestCase01() {
		ReporterCSS.title("测试点: 新建采购退货单");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();
			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			boolean result = compareDetail(stockRefundSheetCreateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "新建采购退货单显示的详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase02() {
		ReporterCSS.title("测试点: 新建采购退货单,并添加折扣");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();
			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			boolean result = compareDetail(stockRefundSheetCreateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "新建采购退货单显示的详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase03() {
		ReporterCSS.title("测试点: 修改采购单退货单,增加退货商品");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();
			OpenStockRefundSheetCommonParam stockRefundSheetUpdateParam = DeepCloneUtil
					.deepClone(stockRefundSheetCreateParam);
			stockRefundSheetCreateParam.getDetails().remove(0);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			stockRefundSheetUpdateParam.setSupplier_refund_sheet_id(stock_refund_sheet_id);
			boolean result = openStockRefundService.updateStockRefundSheet(stockRefundSheetUpdateParam);
			Assert.assertEquals(result, true, "修改采购单退货单,增加退货商品失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			result = compareDetail(stockRefundSheetUpdateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "修改后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase04() {
		ReporterCSS.title("测试点: 修改采购单退货单,删除退货商品");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			OpenStockRefundSheetCommonParam stockRefundSheetUpdateParam = stockRefundSheetCreateParam;
			stockRefundSheetUpdateParam.getDetails().remove(0);

			stockRefundSheetUpdateParam.setSupplier_refund_sheet_id(stock_refund_sheet_id);

			boolean result = openStockRefundService.updateStockRefundSheet(stockRefundSheetUpdateParam);
			Assert.assertEquals(result, true, "修改采购单退货单,删除退货商品失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			result = compareDetail(stockRefundSheetUpdateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "修改后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase05() {
		ReporterCSS.title("测试点: 修改采购单退货单,添加折扣信息");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			OpenStockRefundSheetCommonParam stockRefundSheetUpdateParam = stockRefundSheetCreateParam;
			stockRefundSheetUpdateParam.setSupplier_refund_sheet_id(stock_refund_sheet_id);
			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetUpdateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetUpdateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetUpdateParam.setDiscounts(paramDiscounts);

			boolean result = openStockRefundService.updateStockRefundSheet(stockRefundSheetUpdateParam);
			Assert.assertEquals(result, true, "修改采购单退货单,添加折扣信息");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			result = compareDetail(stockRefundSheetUpdateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "修改后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase06() {
		ReporterCSS.title("测试点: 修改采购单退货单,删除折扣信息");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			OpenStockRefundSheetCommonParam stockRefundSheetUpdateParam = stockRefundSheetCreateParam;
			stockRefundSheetUpdateParam.setSupplier_refund_sheet_id(stock_refund_sheet_id);
			stockRefundSheetUpdateParam.setDiscounts(new ArrayList<>());

			boolean result = openStockRefundService.updateStockRefundSheet(stockRefundSheetUpdateParam);
			Assert.assertEquals(result, true, "修改采购单退货单,添加折扣信息");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			stockRefundSheetUpdateParam.setDiscounts(paramDiscounts);
			result = compareDetail(stockRefundSheetUpdateParam, stockRefundSheetDetail);
			Assert.assertEquals(result, true, "修改后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase07() {
		ReporterCSS.title("测试点: 提交采购退货单");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			boolean result = openStockRefundService.submitStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "提交采购退货单" + stock_refund_sheet_id + "失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			result = compareDetail(stockRefundSheetCreateParam, stockRefundSheetDetail);
			String msg = null;
			if (!stockRefundSheetDetail.getStatus().equals("2")) {
				msg = String.format("采购退货单%s的状态值与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, 2,
						stockRefundSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "提交后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase08() {
		ReporterCSS.title("测试点: 冲销采购退货单,待提交状态冲销");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			boolean result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "冲销采购单据 " + stock_refund_sheet_id + "失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			String msg = null;
			if (!stockRefundSheetDetail.getStatus().equals("-1")) {
				msg = String.format("采购退货单%s的状态值与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, -1,
						stockRefundSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "冲销后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase09() {
		ReporterCSS.title("测试点: 冲销采购退货单,已提交状态冲销");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			boolean result = openStockRefundService.submitStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "提交采购退货单据 " + stock_refund_sheet_id + "失败");

			result = openStockRefundService.revertStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "冲销采购退货单据 " + stock_refund_sheet_id + "失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			String msg = null;
			if (!stockRefundSheetDetail.getStatus().equals("-1")) {
				msg = String.format("采购退货单%s的状态值与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, -1,
						stockRefundSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "冲销后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase10() {
		ReporterCSS.title("测试点: 审核不通过采购退货单");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();

			List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = new ArrayList<>();
			OpenStockRefundSheetCommonParam.Discount paramDiscount1 = stockRefundSheetCreateParam.new Discount();
			paramDiscount1.setDiscount_reason("1");
			paramDiscount1.setDiscount_action("1");
			paramDiscount1.setDiscount_amount("2");
			paramDiscount1.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount1);

			OpenStockRefundSheetCommonParam.Discount paramDiscount2 = stockRefundSheetCreateParam.new Discount();
			paramDiscount2.setDiscount_reason("1");
			paramDiscount2.setDiscount_action("2");
			paramDiscount2.setDiscount_amount("4");
			paramDiscount2.setRemark(StringUtil.getRandomNumber(6));
			paramDiscounts.add(paramDiscount2);

			stockRefundSheetCreateParam.setDiscounts(paramDiscounts);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			boolean result = openStockRefundService.submitStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "提交采购退货单据 " + stock_refund_sheet_id + "失败");

			result = openStockRefundService.rejectStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "审核不通过采购退货单据 " + stock_refund_sheet_id + "失败");

			OpenStockRefundSheetDetailBean stockRefundSheetDetail = openStockRefundService
					.getStockRefundSheetDetail(stock_refund_sheet_id);
			Assert.assertNotEquals(stockRefundSheetDetail, null, "获取采购退货单" + stock_refund_sheet_id + "详细信息失败");

			String msg = null;
			if (!stockRefundSheetDetail.getStatus().equals("0")) {
				msg = String.format("采购退货单%s的状态值与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, -1,
						stockRefundSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "冲销后的采购退货单,详细信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误", e);
			Assert.fail("新建采购退货单遇到错误", e);
		}
	}

	@Test
	public void openStockRefundTestCase11() {
		ReporterCSS.title("测试点: 搜索过滤采购退货单");
		try {
			OpenStockRefundSheetFiterParam filterParam = new OpenStockRefundSheetFiterParam();
			filterParam.setSearch_type("2");
			filterParam.setStart_date(todayStr);
			filterParam.setEnd_date(todayStr);

			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");
			List<OpenStockRefundSheetBean> stockRefundSheets = null;
			String supplier_id = null;
			suppliers = suppliers.subList(0, suppliers.size() > 5 ? 5 : suppliers.size());
			for (int i = -1; i < 5; i++) {
				for (OpenSupplierBean supplier : suppliers) {
					filterParam.setStatus("" + i);
					supplier_id = supplier.getSupplier_id();
					filterParam.setSupplier_id(supplier_id);
					stockRefundSheets = openStockRefundService.queryStockRefundSheet(filterParam);
					Assert.assertNotEquals(stockRefundSheets, null, "搜索过滤采购退货单失败");
					for (OpenStockRefundSheetBean stockRefundSheet : stockRefundSheets) {
						Assert.assertEquals(stockRefundSheet.getSupplier_id().equals(supplier_id)
								&& stockRefundSheet.getStatus().equals("" + i), true, "搜索过滤出来的采购退货单与过滤条件不一致");
					}
				}
			}
		} catch (Exception e) {
			logger.error("搜索过滤采购退货单遇到错误", e);
			Assert.fail("搜索过滤采购退货单遇到错误", e);
		}
	}

	@Test(timeOut = 10000)
	public void openStockRefundTestCase12() {
		ReporterCSS.title("测试点: 按退货时间过滤采购退货单");
		try {
			OpenStockRefundSheetCommonParam stockRefundSheetCreateParam = initStockRefundSheetParam();
			String submit_date = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, 7, Calendar.DATE);
			stockRefundSheetCreateParam.setSubmit_date(submit_date);

			String stock_refund_sheet_id = openStockRefundService.createStockRefundSheet(stockRefundSheetCreateParam);
			Assert.assertNotEquals(stock_refund_sheet_id, null, "新建采购退货单失败");

			boolean result = openStockRefundService.submitStockRefundSheet(stock_refund_sheet_id);
			Assert.assertEquals(result, true, "采购退货单提交失败");

			int offset = 0;
			int limit = 50;
			OpenStockRefundSheetFiterParam filterParam = new OpenStockRefundSheetFiterParam();
			filterParam.setSearch_type("1");
			filterParam.setStart_date(submit_date);
			filterParam.setEnd_date(submit_date);
			filterParam.setLimit(String.valueOf(limit));

			List<OpenStockRefundSheetBean> tempStockRefundSheets = null;
			List<OpenStockRefundSheetBean> stockRefundSheets = new ArrayList<OpenStockRefundSheetBean>();
			while (true) {
				filterParam.setOffset(String.valueOf(offset));
				tempStockRefundSheets = openStockRefundService.queryStockRefundSheet(filterParam);
				Assert.assertNotEquals(tempStockRefundSheets, null, "搜索过滤采购退货单失败");
				stockRefundSheets.addAll(tempStockRefundSheets);
				if (tempStockRefundSheets.size() < limit) {
					break;
				}
				offset += limit;
			}

			OpenStockRefundSheetBean stockRefundSheet = stockRefundSheets.stream()
					.filter(r -> r.getSupplier_refund_sheet_id().equals(stock_refund_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(stockRefundSheet, null, "目标采购退货单" + stock_refund_sheet_id + "没有找到");
		} catch (Exception e) {
			logger.error("搜索过滤采购退货单遇到错误", e);
			Assert.fail("搜索过滤采购退货单遇到错误", e);
		}
	}

	public boolean compareDetail(OpenStockRefundSheetCommonParam param,
			OpenStockRefundSheetDetailBean stockRefundSheetDetail) {
		String stock_refund_sheet_id = stockRefundSheetDetail.getSupplier_refund_sheet_id();
		boolean result = true;
		String msg = null;
		if (!param.getSupplier_id().equals(stockRefundSheetDetail.getSupplier_id())) {
			msg = String.format("采购退货单%s供应商ID与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, param.getSupplier_id(),
					stockRefundSheetDetail.getSupplier_id());
			ReporterCSS.title(msg);
			logger.warn(msg);
			result = false;
		}

		List<OpenStockRefundSheetCommonParam.Detail> paramDetails = param.getDetails();
		List<OpenStockRefundSheetDetailBean.Detail> resultDetails = stockRefundSheetDetail.getDetails();

		BigDecimal expected_sku_amount = BigDecimal.ZERO;
		for (OpenStockRefundSheetCommonParam.Detail paramDetail : paramDetails) {
			String spec_id = paramDetail.getSpec_id();
			BigDecimal refund_count = new BigDecimal(paramDetail.getRefund_count());
			BigDecimal refund_unit_price = new BigDecimal(paramDetail.getRefund_unit_price());
			expected_sku_amount = expected_sku_amount
					.add(refund_count.multiply(refund_unit_price).setScale(2, BigDecimal.ROUND_HALF_UP));
			OpenStockRefundSheetDetailBean.Detail resultDetail = resultDetails.parallelStream()
					.filter(d -> d.getSpec_id().equals(spec_id)).findAny().orElse(null);
			if (resultDetail == null) {
				msg = String.format("采购退货单%s中缺少商品%s", stock_refund_sheet_id, spec_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}
			BigDecimal expected_refund_count = new BigDecimal(resultDetail.getRefund_count());
			BigDecimal actual_refund_count = new BigDecimal(paramDetail.getRefund_count());
			if (expected_refund_count.compareTo(actual_refund_count) != 0) {
				msg = String.format("采购退货单%s中商品%s退货数与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, spec_id,
						paramDetail.getRefund_count(), resultDetail.getRefund_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			BigDecimal expected_refund_unit_price = new BigDecimal(resultDetail.getRefund_unit_price());
			BigDecimal actual_refund_unit_price = new BigDecimal(paramDetail.getRefund_unit_price());
			if (expected_refund_unit_price.compareTo(actual_refund_unit_price) != 0) {
				msg = String.format("采购退货单%s中商品%s退货单价与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, spec_id,
						paramDetail.getRefund_unit_price(), resultDetail.getRefund_unit_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		expected_sku_amount = expected_sku_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal actual_sku_amount = new BigDecimal(stockRefundSheetDetail.getSku_amount());
		if (expected_sku_amount.compareTo(actual_sku_amount) != 0) {
			msg = String.format("采购退货单%s商品总额与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id, expected_sku_amount,
					actual_sku_amount);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<OpenStockRefundSheetCommonParam.Discount> paramDiscounts = param.getDiscounts();
		BigDecimal expected_discount_amount = new BigDecimal(0);
		if (paramDiscounts != null && paramDiscounts.size() > 0) {
			List<OpenStockRefundSheetDetailBean.Discount> resultDiscounts = stockRefundSheetDetail.getDiscounts();
			for (OpenStockRefundSheetCommonParam.Discount paramDiscount : paramDiscounts) {
				if (paramDiscount.getDiscount_action().equals("1")) {
					expected_discount_amount = expected_discount_amount
							.add(new BigDecimal(paramDiscount.getDiscount_amount()));
				} else {
					expected_discount_amount = expected_discount_amount
							.subtract(new BigDecimal(paramDiscount.getDiscount_amount()));
				}
				OpenStockRefundSheetDetailBean.Discount resultDiscount = resultDiscounts.parallelStream()
						.filter(d -> d.getRemark().equals(paramDiscount.getRemark())).findAny().orElse(null);
				if (resultDiscount == null) {
					msg = String.format("采购退货单%s详细里缺少备注为%s的折扣条目", stock_refund_sheet_id, paramDiscount.getRemark());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!resultDiscount.getDiscount_reason().equals(paramDiscount.getDiscount_reason())) {
					msg = String.format("采购退货单%s详细里备注为%s的折扣条目折扣原因与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id,
							paramDiscount.getRemark(), paramDiscount.getDiscount_reason(),
							resultDiscount.getDiscount_reason());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!resultDiscount.getDiscount_reason().equals(paramDiscount.getDiscount_reason())) {
					msg = String.format("采购退货单%s详细里备注为%s的折扣条目折扣原因与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id,
							paramDiscount.getRemark(), paramDiscount.getDiscount_reason(),
							resultDiscount.getDiscount_reason());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			BigDecimal actual_discount_amount = new BigDecimal(stockRefundSheetDetail.getDiscount_amount());
			if (actual_discount_amount.compareTo(expected_discount_amount) != 0) {
				msg = String.format("采购退货单%s详细显示的折让金额与预期不一致,预期:%s,实际:%s", stock_refund_sheet_id,
						expected_discount_amount, actual_discount_amount);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

		}

		BigDecimal expected_total_amount = expected_sku_amount.add(expected_discount_amount);
		BigDecimal actual_total_amount = new BigDecimal(stockRefundSheetDetail.getTotal_amount());
		if (expected_total_amount.compareTo(actual_total_amount) != 0) {
			msg = String.format("采购退货单%s详细显示的总金额与预期的不一致,预期:%s,实际:%s, 折让:%s", stock_refund_sheet_id,
					expected_total_amount, actual_total_amount, expected_discount_amount);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;

	}

}
