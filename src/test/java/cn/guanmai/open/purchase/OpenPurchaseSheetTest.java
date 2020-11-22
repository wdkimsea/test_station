package cn.guanmai.open.purchase;

import java.math.BigDecimal;
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
import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetDetailBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetCommonParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.purchase.OpenPurcahseServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.DeepCloneUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月13日
 * @time 上午10:09:47
 * @des TODO
 */

public class OpenPurchaseSheetTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseSheetTest.class);
	private OpenPurcahseService openPurcahseService;
	private OpenSupplierService openSupplierService;
	private OpenCategoryService openCategoryService;

	private OpenPurchaseSheetCommonParam purchaseSheetCreateParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);

	}

	@BeforeMethod
	public void beforeMethod() {
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

			purchaseSheetCreateParam = new OpenPurchaseSheetCommonParam();
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
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase01() {
		ReporterCSS.title("测试点: 新建采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			boolean result = comparePurchaseSheetDetailInfo(purchaseSheetCreateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "新建的采购单据 " + purchase_sheet_id + " 的详细信息与预期不一致");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase02() {
		ReporterCSS.title("测试点: 查询采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertNotEquals(openPurchaseSheetList, null, "查询采购单据遇到错误");

			Assert.assertEquals(openPurchaseSheetList.size() > 0, true, "采购单据列表为空,与预期不符");

			OpenPurchaseSheetBean openPurchaseSheet = openPurchaseSheetList.get(0);
			Assert.assertEquals(openPurchaseSheet.getPurchase_sheet_id(), purchase_sheet_id, "新建的采购单据应该排在第一页最上面");

			boolean reuslt = comparePurcahseSheetInfo(purchaseSheetCreateParam, openPurchaseSheet);
			Assert.assertEquals(reuslt, true, "采购单据" + purchase_sheet_id + "在列表显示的信息与预期不一致");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase03() {
		ReporterCSS.title("测试点: 以采购单据ID过滤采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSheet_id(purchase_sheet_id);
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);

			Assert.assertNotEquals(openPurchaseSheetList, null, "以采购单据ID过滤采购单据");

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(tempOpenPurchaseSheet, null, "以采购单据ID查询采购单据,没有搜索到目标单据");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseSheetTestCase04() {
		ReporterCSS.title("测试点: 以供应商过滤采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSupplier_id(purchaseSheetCreateParam.getSupplier_id());

			List<OpenPurchaseSheetBean> openPurchaseSheetList = new ArrayList<OpenPurchaseSheetBean>();
			int offset = Integer.valueOf(openPurchaseSheetFilterParam.getOffset());
			while (true) {
				List<OpenPurchaseSheetBean> tempOpenPurchaseSheetList = openPurcahseService
						.queryPurchaseSheet(openPurchaseSheetFilterParam);
				Assert.assertNotEquals(tempOpenPurchaseSheetList, null, "以供应商过滤采购单据失败");
				openPurchaseSheetList.addAll(tempOpenPurchaseSheetList);
				if (tempOpenPurchaseSheetList.size() < Integer.valueOf(openPurchaseSheetFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseSheetFilterParam.getLimit());
				openPurchaseSheetFilterParam.setOffset(String.valueOf(offset));
			}

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(tempOpenPurchaseSheet, null, "以供应商过滤采购单据,没有搜索到目标单据");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseSheetTestCase05() {
		ReporterCSS.title("测试点: 以状态过滤采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setStatus("3");

			List<OpenPurchaseSheetBean> openPurchaseSheetList = new ArrayList<OpenPurchaseSheetBean>();
			int offset = Integer.valueOf(openPurchaseSheetFilterParam.getOffset());
			while (true) {
				List<OpenPurchaseSheetBean> tempOpenPurchaseSheetList = openPurcahseService
						.queryPurchaseSheet(openPurchaseSheetFilterParam);
				Assert.assertNotEquals(tempOpenPurchaseSheetList, null, "以状态过滤采购单据失败");
				openPurchaseSheetList.addAll(tempOpenPurchaseSheetList);
				if (tempOpenPurchaseSheetList.size() < Integer.valueOf(openPurchaseSheetFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseSheetFilterParam.getLimit());
				openPurchaseSheetFilterParam.setOffset(String.valueOf(offset));
			}

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(tempOpenPurchaseSheet, null, "以状态过滤采购单据,没有搜索到目标单据");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseSheetTestCase06() {
		ReporterCSS.title("测试点: 多条件组合过滤采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSheet_id(purchase_sheet_id);
			openPurchaseSheetFilterParam.setSupplier_id(purchaseSheetCreateParam.getSupplier_id());
			openPurchaseSheetFilterParam.setStatus("3");

			List<OpenPurchaseSheetBean> openPurchaseSheetList = new ArrayList<OpenPurchaseSheetBean>();
			int offset = Integer.valueOf(openPurchaseSheetFilterParam.getOffset());
			while (true) {
				List<OpenPurchaseSheetBean> tempOpenPurchaseSheetList = openPurcahseService
						.queryPurchaseSheet(openPurchaseSheetFilterParam);
				Assert.assertNotEquals(tempOpenPurchaseSheetList, null, "多条件过滤采购单据失败");
				openPurchaseSheetList.addAll(tempOpenPurchaseSheetList);
				if (tempOpenPurchaseSheetList.size() < Integer.valueOf(openPurchaseSheetFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseSheetFilterParam.getLimit());
				openPurchaseSheetFilterParam.setOffset(String.valueOf(offset));
			}

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(tempOpenPurchaseSheet, null, "多条件组合过滤采购单据,没有搜索到目标单据");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseSheetTestCase07() {
		ReporterCSS.title("测试点: 输入不符合的条件过滤采购单据,应该过滤不到目标单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSheet_id(purchase_sheet_id);
			openPurchaseSheetFilterParam.setSupplier_id(purchaseSheetCreateParam.getSupplier_id());
			openPurchaseSheetFilterParam.setStatus("2");

			List<OpenPurchaseSheetBean> openPurchaseSheetList = new ArrayList<OpenPurchaseSheetBean>();
			int offset = Integer.valueOf(openPurchaseSheetFilterParam.getOffset());
			while (true) {
				List<OpenPurchaseSheetBean> tempOpenPurchaseSheetList = openPurcahseService
						.queryPurchaseSheet(openPurchaseSheetFilterParam);
				Assert.assertNotEquals(tempOpenPurchaseSheetList, null, "输入不符合的条件过滤采购单据失败");
				openPurchaseSheetList.addAll(tempOpenPurchaseSheetList);
				if (tempOpenPurchaseSheetList.size() < Integer.valueOf(openPurchaseSheetFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseSheetFilterParam.getLimit());
				openPurchaseSheetFilterParam.setOffset(String.valueOf(offset));
			}

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertEquals(tempOpenPurchaseSheet, null, "输入不符合的条件过滤采购单据,应该过滤不到目标单据");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase08() {
		ReporterCSS.title("测试点: 修改采购单据,不修改任何值");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			purchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);

			boolean result = openPurcahseService.updatePurchaseSheet(purchaseSheetCreateParam);
			Assert.assertEquals(result, true, "修改采购单据,不修改任何值,修改失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();
			List<OpenPurchaseSheetCommonParam.Detail> details = purchaseSheetCreateParam.getDetails();
			for (OpenPurchaseSheetCommonParam.Detail detail : details) {
				OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(detail.getSpec_id())).findAny().orElse(null);
				detail.setDetail_id(resultDetail.getDetail_id());
			}

			result = comparePurchaseSheetDetailInfo(purchaseSheetCreateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase09() {
		ReporterCSS.title("测试点: 修改采购单据,删除采购条目");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			purchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();
			List<OpenPurchaseSheetCommonParam.Detail> details = purchaseSheetCreateParam.getDetails();
			for (OpenPurchaseSheetCommonParam.Detail detail : details) {
				OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(detail.getSpec_id())).findAny().orElse(null);
				detail.setDetail_id(resultDetail.getDetail_id());
			}

			details = purchaseSheetCreateParam.getDetails();
			Assert.assertEquals(details.size() >= 2, true, "采购单据 " + purchase_sheet_id + " 里的条目数不足2条,无法进行删除采购条目操作");

			// 删除第二条采购条目
			details.remove(1);

			boolean result = openPurcahseService.updatePurchaseSheet(purchaseSheetCreateParam);
			Assert.assertEquals(result, true, "修改采购单据,删除采购条目,修改失败");

			openPurchaseSheetDetail = openPurcahseService.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			result = comparePurchaseSheetDetailInfo(purchaseSheetCreateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");

		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase10() {
		ReporterCSS.title("测试点: 修改采购单据,新增采购条目");
		try {
			OpenPurchaseSheetCommonParam purchaseSheetUpdateParam = DeepCloneUtil.deepClone(purchaseSheetCreateParam);

			List<OpenPurchaseSheetCommonParam.Detail> details = purchaseSheetCreateParam.getDetails();
			Assert.assertEquals(details.size() >= 2, true, "构建的采购单据条目数不足2条,无法进行后续操作");
			// 删除第二条采购条目
			details.remove(1);

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			purchaseSheetUpdateParam.setPurchase_sheet_id(purchase_sheet_id);

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();

			List<OpenPurchaseSheetCommonParam.Detail> updateDetails = purchaseSheetUpdateParam.getDetails();

			for (OpenPurchaseSheetCommonParam.Detail updateDetail : updateDetails) {
				OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(updateDetail.getSpec_id())).findAny().orElse(null);
				if (resultDetail != null) {
					updateDetail.setDetail_id(resultDetail.getDetail_id());
				}
			}

			boolean result = openPurcahseService.updatePurchaseSheet(purchaseSheetUpdateParam);
			Assert.assertEquals(result, true, "修改采购单据,新增采购条目,修改失败");

			openPurchaseSheetDetail = openPurcahseService.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			result = comparePurchaseSheetDetailInfo(purchaseSheetUpdateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");

		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase11() {
		ReporterCSS.title("测试点: 修改采购单据,修改单价、采购数、备注");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();
			List<OpenPurchaseSheetCommonParam.Detail> details = purchaseSheetCreateParam.getDetails();
			for (OpenPurchaseSheetCommonParam.Detail detail : details) {
				OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getSpec_id().equals(detail.getSpec_id())).findAny().orElse(null);
				detail.setDetail_id(resultDetail.getDetail_id());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setPurchase_count(NumberUtil.getRandomNumber(6, 15, 2).toString());
				detail.setPurchase_price(NumberUtil.getRandomNumber(5, 10, 1).toString());
			}

			purchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			boolean result = openPurcahseService.updatePurchaseSheet(purchaseSheetCreateParam);
			Assert.assertEquals(result, true, "修改采购单据,修改单价、采购数、备注失败");

			openPurchaseSheetDetail = openPurcahseService.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			result = comparePurchaseSheetDetailInfo(purchaseSheetCreateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase12() {
		ReporterCSS.title("测试点: 修改采购单据,修改供应商和采购员");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			String supplier_id = null;
			String purchaser_id = null;
			for (OpenSupplierBean supplier : suppliers) {
				supplier_id = supplier.getSupplier_id();
				if (supplier_id.equals(purchaseSheetCreateParam.getSupplier_id())) {
					continue;
				}

				OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
				openPurchaserFilterParam.setSupplier_id(supplier_id);

				List<OpenPurcahserBean> purcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
				Assert.assertNotEquals(purcahserList, null, "根据供应商查询采购员失败");

				if (purcahserList.size() > 0) {
					purchaser_id = NumberUtil.roundNumberInList(purcahserList).getPurchaser_id().toString();
					break;
				}

			}
			Assert.assertNotEquals(purchaser_id, null, "没有可以可用更换的供应商与采购员");

			purchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			purchaseSheetCreateParam.setPurchaser_id(purchaser_id);
			purchaseSheetCreateParam.setSupplier_id(supplier_id);
			purchaseSheetCreateParam.setRemark(StringUtil.getRandomString(6));

			OpenPurchaseSheetCommonParam purchaseSheetUpdateParam = DeepCloneUtil.deepClone(purchaseSheetCreateParam);

			purchaseSheetCreateParam.setDetails(null);

			boolean result = openPurcahseService.updatePurchaseSheet(purchaseSheetCreateParam);
			Assert.assertEquals(result, true, "修改采购单据,修改供应商和采购员失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			result = comparePurchaseSheetDetailInfo(purchaseSheetUpdateParam, openPurchaseSheetDetail);
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase13() {
		ReporterCSS.title("测试点: 提交采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			boolean result = openPurcahseService.submitPurchaseSheet(purchase_sheet_id);
			Assert.assertEquals(result, true, "提交采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据" + purchase_sheet_id + "详情失败");

			result = comparePurchaseSheetDetailInfo(purchaseSheetCreateParam, openPurchaseSheetDetail);

			if (openPurchaseSheetDetail.getStatus() != 2) {
				String msg = String.format("提交的采购单据%s的状态值不正确,预期:2,实际:%s", purchase_sheet_id,
						openPurchaseSheetDetail.getStatus());
				logger.warn(msg);
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "采购单据" + purchase_sheet_id + "详细信息与预期不一致");
		} catch (Exception e) {
			logger.error("提交采购单据遇到错误: ", e);
			Assert.fail("提交采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetTestCase14() {
		ReporterCSS.title("测试点: 删除采购单据");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(purchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "新建采购单据失败");

			boolean result = openPurcahseService.deletePurcahseSheet(purchase_sheet_id);
			Assert.assertEquals(result, true, "删除采购单据失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSheet_id(purchase_sheet_id);

			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);

			Assert.assertNotEquals(openPurchaseSheetList, null, "过滤采购单据失败");

			OpenPurchaseSheetBean tempOpenPurchaseSheet = openPurchaseSheetList.stream()
					.filter(p -> p.getPurchase_sheet_id().equals(purchase_sheet_id)).findAny().orElse(null);
			Assert.assertEquals(tempOpenPurchaseSheet, null, "删除的采购单据没有从采购单据列表真正移除");

		} catch (Exception e) {
			logger.error("删除采购单据遇到错误: ", e);
			Assert.fail("删除采购单据遇到错误: ", e);
		}
	}

	/**
	 * 检测输入信息
	 * 
	 * @param openPurchaseSheetCommonParam
	 * @param openPurchaseSheetDetail
	 * @return
	 */
	public boolean comparePurchaseSheetDetailInfo(OpenPurchaseSheetCommonParam openPurchaseSheetCommonParam,
			OpenPurchaseSheetDetailBean openPurchaseSheetDetail) {
		boolean result = true;
		String msg = null;
		if (!openPurchaseSheetCommonParam.getSupplier_id().equals(openPurchaseSheetDetail.getSupplier_id())) {
			msg = String.format("新建采购单据填的供应商和采购单据返回的供应商ID不一致,预期:%s,实际:%s",
					openPurchaseSheetCommonParam.getSupplier_id(), openPurchaseSheetDetail.getSupplier_id());
			ReporterCSS.title(msg);
			logger.warn(msg);
			result = false;
		}

		if (!openPurchaseSheetCommonParam.getPurchaser_id().equals(openPurchaseSheetDetail.getPurchaser_id())) {
			msg = String.format("新建采购单据填的采购员和采购单据返回的采购员ID不一致,预期:%s,实际:%s",
					openPurchaseSheetCommonParam.getPurchaser_id(), openPurchaseSheetDetail.getPurchaser_id());
			ReporterCSS.title(msg);
			logger.warn(msg);
			result = false;
		}

		if (!openPurchaseSheetCommonParam.getRemark().equals(openPurchaseSheetDetail.getRemark())) {
			msg = String.format("新建采购单据填的备注和采购单据返回的备注不一致,预期:%s,实际:%s", openPurchaseSheetCommonParam.getRemark(),
					openPurchaseSheetDetail.getRemark());
			ReporterCSS.title(msg);
			logger.warn(msg);
			result = false;
		}

		List<OpenPurchaseSheetDetailBean.Detail> details = openPurchaseSheetDetail.getDetails();
		OpenPurchaseSheetDetailBean.Detail detailActual = null;
		for (OpenPurchaseSheetCommonParam.Detail detail : openPurchaseSheetCommonParam.getDetails()) {
			detailActual = details.stream().filter(t -> t.getSpec_id().equals(detail.getSpec_id())).findAny()
					.orElse(null);
			if (detailActual == null) {
				msg = String.format("采购单详细里缺少预期商品%s", detail.getSpec_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (detailActual.getPlan_count().compareTo(new BigDecimal(detail.getPurchase_count())) != 0) {
				msg = String.format("采购单里商品%s的采购数量与预期不符,预期:%s,实际:%s", detail.getSpec_id(), detail.getPurchase_count(),
						detailActual.getPurchase_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detailActual.getPurchase_price().compareTo(new BigDecimal(detail.getPurchase_price())) != 0) {
				msg = String.format("采购单里商品%s的采购单价与预期不符,预期:%s,实际:%s", detail.getSpec_id(), detail.getPurchase_price(),
						detailActual.getPurchase_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!detailActual.getRemark().equals(detail.getRemark())) {
				msg = String.format("采购单里商品%s的采购备注与预期不符,预期:%s,实际:%s", detail.getSpec_id(), detail.getRemark(),
						detailActual.getRemark());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

	/**
	 * 检测采购单据信息
	 * 
	 * @param openPurchaseSheetCommonParam
	 * @param openPurchaseSheetBean
	 * @return
	 */
	public boolean comparePurcahseSheetInfo(OpenPurchaseSheetCommonParam openPurchaseSheetCommonParam,
			OpenPurchaseSheetBean openPurchaseSheet) {
		boolean result = true;
		String msg = null;
		List<OpenPurchaseSheetCommonParam.Detail> details = openPurchaseSheetCommonParam.getDetails();
		BigDecimal purchase_price = null;
		BigDecimal purchase_count = null;
		BigDecimal purchase_amount = new BigDecimal("0");
		for (OpenPurchaseSheetCommonParam.Detail detail : details) {
			purchase_price = new BigDecimal(detail.getPurchase_price());
			purchase_count = new BigDecimal(detail.getPurchase_count());
			purchase_amount = purchase_amount.add(purchase_price.multiply(purchase_count));
		}
		purchase_amount = purchase_amount.setScale(2, BigDecimal.ROUND_HALF_UP);

		if (purchase_amount.compareTo(openPurchaseSheet.getPurchase_amount()) != 0) {
			msg = String.format("采购单据的采购金额与预期不一致,预期:%s,实际:%s", purchase_amount, openPurchaseSheet.getPurchase_amount());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (details.size() != openPurchaseSheet.getPurchase_task_num()) {
			msg = String.format("采购单据的采购任务数与预期不一致,预期:%s,实际:%s", details.size(),
					openPurchaseSheet.getPurchase_task_num());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!openPurchaseSheetCommonParam.getPurchaser_id().equals(openPurchaseSheet.getPurchaser_id())) {
			msg = String.format("采购单据的采购员与预期不一致,预期:%s,实际:%s", openPurchaseSheetCommonParam.getPurchaser_id(),
					openPurchaseSheet.getPurchaser_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;

	}
}
