package cn.guanmai.open.purchase.abnormal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
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
import cn.guanmai.open.purchase.OpenPurchaseSheetTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.DeepCloneUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月14日
 * @time 下午3:29:04
 * @des TODO
 */

public class OpenPurchaseSheetAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseSheetTest.class);
	private OpenPurcahseService openPurcahseService;
	private OpenSupplierService openSupplierService;
	private OpenCategoryService openCategoryService;

	private OpenPurchaseSheetCommonParam purchaseSheetCreateParam;
	private OpenPurchaseSheetCommonParam copyPurchaseSheetCreateParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	public OpenPurchaseSheetCommonParam initParams() {
		try {
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

			Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

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
			purchaseSheetCreateParam.setPurcahser_name(openPurcahser.getPurchaser_name());

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
		return purchaseSheetCreateParam;
	}

	@BeforeMethod
	public void beforeMethod() {
		if (purchaseSheetCreateParam == null) {
			purchaseSheetCreateParam = initParams();
		}
		copyPurchaseSheetCreateParam = DeepCloneUtil.deepClone(purchaseSheetCreateParam);
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,输入非法格式时间,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy.MM.dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,输入非法格式时间,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,输入非法时间,开始时间晚于结束时间");
		try {
			String startDate = TimeUtil.getCurrentTime("yyyy-MM-dd");
			String endDate = TimeUtil.calculateTime("yyyy-MM-dd", startDate, -2, Calendar.DATE);
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(startDate);
			openPurchaseSheetFilterParam.setEnd_date(endDate);
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,输入非法时间,启示时间晚于结束时间");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,状态值输入非数值,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setStatus("a");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,状态值输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,状态值输入非候选值,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setStatus("1");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,状态值输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,供应商ID输入错误值,断言结果为空");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSupplier_id("T7936");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,供应商ID输入错误值,断言结果为空");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,供应商ID输入错误值,断言结果为空");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setSupplier_id("A7936");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,供应商ID输入错误值,断言结果为空");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,offset输入非数值,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setOffset("a");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,offset输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,offset输入负数,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setOffset("-1");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,offset输入负数,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,offset输入小数,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setOffset("1.2");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,offset输入小数,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,limit输入非数值,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setLimit("a");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,limit输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,limit输入负数,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setLimit("-1");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,limit输入负数,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购单据,limit输入小数,断言失败");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenPurchaseSheetFilterParam openPurchaseSheetFilterParam = new OpenPurchaseSheetFilterParam();
			openPurchaseSheetFilterParam.setStart_date(todayStr);
			openPurchaseSheetFilterParam.setEnd_date(todayStr);
			openPurchaseSheetFilterParam.setLimit("1.2");
			List<OpenPurchaseSheetBean> openPurchaseSheetList = openPurcahseService
					.queryPurchaseSheet(openPurchaseSheetFilterParam);
			Assert.assertEquals(openPurchaseSheetList, null, "异常测试,搜索过滤采购单据,limit输入小数,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,获取采购单据详情传入空值,断言失败");
		try {
			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService.getPurchaseSheetDetail("");
			Assert.assertEquals(openPurchaseSheetDetail, null, "异常测试,获取采购单据详情传入空值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常测试,获取采购单据详情传入错误值,断言失败");
		try {
			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService.getPurchaseSheetDetail("TVF");
			Assert.assertEquals(openPurchaseSheetDetail, null, "异常测试,获取采购单据详情传入空值,断言失败");
		} catch (Exception e) {
			logger.error("查询采购单据遇到错误: ", e);
			Assert.fail("查询采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,供应商传入空值,断言失败");
		try {
			copyPurchaseSheetCreateParam.setSupplier_id("");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,供应商传入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,供应商传入错误值,断言失败");
		try {
			copyPurchaseSheetCreateParam.setSupplier_id("T7936");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,供应商传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase17() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购员ID输入为空,断言失败");
		try {
			copyPurchaseSheetCreateParam.setPurchaser_id("");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购员ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase18() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购员ID输入错误值,断言失败");
		try {
			copyPurchaseSheetCreateParam.setPurchaser_id("110");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购员ID输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase19() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购详细输入为空,断言失败");
		try {
			copyPurchaseSheetCreateParam.setDetails(new ArrayList<>());
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购详细输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase20() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购详细输入为空,断言失败");
		try {
			copyPurchaseSheetCreateParam.setDetails(new ArrayList<>());
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购详细输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase21() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购规格输入为空,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setSpec_id("");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购规格输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase22() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购规格输入错误值,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setSpec_id("D12273761");
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购规格输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase23() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购规格输入销售规格ID,断言失败");
		try {
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(new OpenSaleSkuFilterParam());
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤采购规格失败");

			OpenSaleSkuBean openSaleSku = NumberUtil.roundNumberInList(openSaleSkus);

			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);

			detail.setSpec_id(openSaleSku.getId());

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购规格输入销售规格ID,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase24() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购规格输入销售规格ID,断言失败");
		try {
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(new OpenSaleSkuFilterParam());
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤采购规格失败");

			OpenSaleSkuBean openSaleSku = NumberUtil.roundNumberInList(openSaleSkus);

			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);

			detail.setSpec_id(openSaleSku.getId());

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购规格输入销售规格ID,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase25() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的价格输入为空,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_price("");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的价格输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase26() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的价格输入非数值,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_price("a");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的价格输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase27() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的价格输入为负数,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_price("-12");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的价格输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase28() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的价格输入多位小数,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_price("1.234");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的价格输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase29() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的价格输入过大值,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_price("10000001");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的价格输入过大值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase30() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入为空,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase31() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入非数值,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("a");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase32() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入负数,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("-2");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase33() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入为0,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("0");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入为0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase34() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入为多位小数,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("1.234");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入为多位小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase35() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的采购数输入过大值,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setPurchase_count("10000001");

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的采购数输入过大值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase36() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购条目的备注输入过长字符,断言失败");
		try {
			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setRemark(StringUtil.getRandomString(33));

			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购条目的备注输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase37() {
		ReporterCSS.title("测试点: 异常测试,新建采购单据,采购备注输入过长字符,断言失败");
		try {
			copyPurchaseSheetCreateParam.setRemark(StringUtil.getRandomString(33));
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(purchase_sheet_id, null, "异常测试,新建采购单据,采购备注输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建采购单据遇到错误: ", e);
			Assert.fail("新建采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase38() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,不传入采购单据单号,断言失败");
		try {
			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,不传入采购单据单号,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase39() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,供应商传入空格,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			copyPurchaseSheetCreateParam.setSupplier_id("  ");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,供应商传入空格,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase40() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,供应商传入错误值,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			copyPurchaseSheetCreateParam.setSupplier_id("T7936");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,供应商传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase41() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,采购员输入空格,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			copyPurchaseSheetCreateParam.setPurchaser_id("  ");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,采购员输入空格,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase42() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,采购员输入错误值,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);
			copyPurchaseSheetCreateParam.setPurchaser_id("122");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,采购员输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase43() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,采购条目传入错误的detail_id,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);

			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);
			detail.setDetail_id("5dcd1675e3417702d3a456e2");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,采购条目传入错误的detail_id,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase44() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,采购数目输入空,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);

			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();
			OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
					.filter(d -> d.getSpec_id().equals(detail.getSpec_id())).findAny().orElse(null);

			detail.setDetail_id(resultDetail.getDetail_id());
			detail.setPurchase_count(" ");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,采购数目输入空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase45() {
		ReporterCSS.title("测试点: 异常测试,修改采购单据,采购数目输入负数,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			OpenPurchaseSheetDetailBean openPurchaseSheetDetail = openPurcahseService
					.getPurchaseSheetDetail(purchase_sheet_id);
			Assert.assertNotEquals(openPurchaseSheetDetail, null, "获取采购单据详细信息失败");

			copyPurchaseSheetCreateParam.setPurchase_sheet_id(purchase_sheet_id);

			List<OpenPurchaseSheetCommonParam.Detail> details = copyPurchaseSheetCreateParam.getDetails();
			OpenPurchaseSheetCommonParam.Detail detail = NumberUtil.roundNumberInList(details);

			List<OpenPurchaseSheetDetailBean.Detail> resultDetails = openPurchaseSheetDetail.getDetails();
			OpenPurchaseSheetDetailBean.Detail resultDetail = resultDetails.stream()
					.filter(d -> d.getSpec_id().equals(detail.getSpec_id())).findAny().orElse(null);

			detail.setDetail_id(resultDetail.getDetail_id());
			detail.setPurchase_count("-10");

			boolean result = openPurcahseService.updatePurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购单据,采购数目输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase46() {
		ReporterCSS.title("测试点: 异常测试,提交采购单据,采购单据数量输入为空,断言失败");
		try {
			boolean result = openPurcahseService.submitPurchaseSheet("");
			Assert.assertEquals(result, false, "异常测试,提交采购单据,采购单据数量输入为空,断言失败");
		} catch (Exception e) {
			logger.error("提交采购单据遇到错误: ", e);
			Assert.fail("提交采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase47() {
		ReporterCSS.title("测试点: 异常测试,提交采购单据,采购单据数量输入为错误值,断言失败");
		try {
			boolean result = openPurcahseService.submitPurchaseSheet("T23821-CGD-2019-11-14-00001");
			Assert.assertEquals(result, false, "异常测试,提交采购单据,采购单据数量输入为错误值,断言失败");
		} catch (Exception e) {
			logger.error("提交采购单据遇到错误: ", e);
			Assert.fail("提交采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase48() {
		ReporterCSS.title("测试点: 异常测试,提交已经删除的采购单据,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			boolean result = openPurcahseService.deletePurcahseSheet(purchase_sheet_id);
			Assert.assertEquals(result, true, "删除采购单据失败");

			result = openPurcahseService.submitPurchaseSheet(purchase_sheet_id);
			Assert.assertEquals(result, false, "异常测试,提交已经删除的采购单据,断言失败");
		} catch (Exception e) {
			logger.error("修改采购单据遇到错误: ", e);
			Assert.fail("修改采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase49() {
		ReporterCSS.title("测试点: 异常测试,删除已经提交的采购单据,断言失败");
		try {
			String purchase_sheet_id = openPurcahseService.createPurchaseSheet(copyPurchaseSheetCreateParam);
			Assert.assertNotEquals(purchase_sheet_id, null, "创建采购单据失败");

			boolean result = openPurcahseService.submitPurchaseSheet(purchase_sheet_id);
			Assert.assertEquals(result, true, "提交采购单据失败");

			result = openPurcahseService.deletePurcahseSheet(purchase_sheet_id);
			Assert.assertEquals(result, false, "异常测试,删除已经提交的采购单据,断言失败");
		} catch (Exception e) {
			logger.error("删除采购单据遇到错误: ", e);
			Assert.fail("删除采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase50() {
		ReporterCSS.title("测试点: 异常测试,删除采购单据,单据号传入空值,断言失败");
		try {
			boolean result = openPurcahseService.deletePurcahseSheet(" ");
			Assert.assertEquals(result, false, "异常测试,删除采购单据,单据号传入空值,断言失败");
		} catch (Exception e) {
			logger.error("删除采购单据遇到错误: ", e);
			Assert.fail("删除采购单据遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseSheetAbnormalTestCase51() {
		ReporterCSS.title("测试点: 异常测试,删除采购单据,单据号传入错误值,断言失败");
		try {
			boolean result = openPurcahseService.deletePurcahseSheet("T23821-CGD-2019-11-14-00001");
			Assert.assertEquals(result, false, "异常测试,删除采购单据,单据号传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("删除采购单据遇到错误: ", e);
			Assert.fail("删除采购单据遇到错误: ", e);
		}
	}
}
