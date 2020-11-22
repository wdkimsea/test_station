package cn.guanmai.open.purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.param.OpenQuotePriceParam;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.util.ReporterCSS;

/**
 * @author: liming
 * @Date: 2020年6月3日 上午11:34:46
 * @description:
 * @version: 1.0
 */

public class OpenPurchaseSpecQuotePriceTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseSpecQuotePriceTest.class);
	private OpenCategoryServiceImpl openCategoryService;
	private OpenSupplierService openSupplierService;
	private CategoryService categoryService;
	private String settle_supplier_id;
	private String spec_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();

		InitDataService initDataService = new InitDataServiceImpl(st_headers);
		categoryService = new CategoryServiceImpl(st_headers);

		try {
			InitDataBean initData = initDataService.getInitData();
			settle_supplier_id = initData.getSupplier().getId();

			spec_id = initData.getPurchaseSpec().getId();

		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceTestCase01() {
		ReporterCSS.title("测试点: 采购规格进行询价");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("5.12");
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "采购规格进行询价失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "ST接口获取采购规格详细信息失败");

			List<PurchaseSpecBean.LastQuotedDetail> last_quoted_details = purchaseSpec.getLast_quoted_details();
			Assert.assertNotEquals(last_quoted_details, null, "对应的采购规格询价信息为空,与预期不符");

			PurchaseSpecBean.LastQuotedDetail last_quoted_detail = last_quoted_details.stream()
					.filter(l -> l.getSupplier_id().equals(settle_supplier_id)).findAny().orElse(null);
			Assert.assertNotEquals(last_quoted_detail, null, "对应的采购规格没有供应商 " + settle_supplier_id + "的询价信息,与预期不符");

			String msg = null;
			if (last_quoted_detail.getPrice().compareTo(new BigDecimal(openQuotePriceParam.getPurchase_price())) != 0) {
				msg = String.format("采购规格%s对应的供应商%s询价信息与预期不符,预期:%s,实际:%s", spec_id, settle_supplier_id,
						openQuotePriceParam.getPurchase_price(), last_quoted_detail.getPrice());
				logger.warn(msg);
				ReporterCSS.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购规格询价后预期结果与实际不符");
		} catch (Exception e) {
			logger.error("采购规格询价遇到错误: ", e);
			Assert.fail("采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceTestCase02() {
		ReporterCSS.title("测试点: 采购规格进行询价");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("0.01");
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "采购规格进行询价失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "ST接口获取采购规格详细信息失败");

			List<PurchaseSpecBean.LastQuotedDetail> last_quoted_details = purchaseSpec.getLast_quoted_details();
			Assert.assertNotEquals(last_quoted_details, null, "对应的采购规格询价信息为空,与预期不符");

			PurchaseSpecBean.LastQuotedDetail last_quoted_detail = last_quoted_details.stream()
					.filter(l -> l.getSupplier_id().equals(settle_supplier_id)).findAny().orElse(null);
			Assert.assertNotEquals(last_quoted_detail, null, "对应的采购规格没有供应商 " + settle_supplier_id + "的询价信息,与预期不符");

			String msg = null;
			if (last_quoted_detail.getPrice().compareTo(new BigDecimal(openQuotePriceParam.getPurchase_price())) != 0) {
				msg = String.format("采购规格%s对应的供应商%s询价信息与预期不符,预期:%s,实际:%s", spec_id, settle_supplier_id,
						openQuotePriceParam.getPurchase_price(), last_quoted_detail.getPrice());
				logger.warn(msg);
				ReporterCSS.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购规格询价后预期结果与实际不符");

		} catch (Exception e) {
			logger.error("采购规格询价遇到错误: ", e);
			Assert.fail("采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceTestCase03() {
		ReporterCSS.title("测试点: 采购规格进行询价");
		try {
			OpenSupplierFilterParam supplierFilterParam = new OpenSupplierFilterParam();
			supplierFilterParam.setOffset("0");
			supplierFilterParam.setLimit("10");
			List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(supplierFilterParam);
			OpenSupplierBean supplier = suppliers.stream().filter(s -> !s.getSupplier_id().equals(settle_supplier_id))
					.findFirst().orElse(null);
			Assert.assertNotEquals(supplier, null, "此站点无别的供应商,无法使用不同的供应商对同一个采购规格进行询价");
			String settle_supplier_id = supplier.getSupplier_id();

			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("2");
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "采购规格进行询价失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "ST接口获取采购规格详细信息失败");

			List<PurchaseSpecBean.LastQuotedDetail> last_quoted_details = purchaseSpec.getLast_quoted_details();
			Assert.assertNotEquals(last_quoted_details, null, "对应的采购规格询价信息为空,与预期不符");

			PurchaseSpecBean.LastQuotedDetail last_quoted_detail = last_quoted_details.stream()
					.filter(l -> l.getSupplier_id().equals(settle_supplier_id)).findAny().orElse(null);
			Assert.assertNotEquals(last_quoted_detail, null, "对应的采购规格没有供应商 " + settle_supplier_id + "的询价信息,与预期不符");

			String msg = null;
			if (last_quoted_detail.getPrice().compareTo(new BigDecimal(openQuotePriceParam.getPurchase_price())) != 0) {
				msg = String.format("采购规格%s对应的供应商%s询价信息与预期不符,预期:%s,实际:%s", spec_id, settle_supplier_id,
						openQuotePriceParam.getPurchase_price(), last_quoted_detail.getPrice());
				logger.warn(msg);
				ReporterCSS.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购规格询价后预期结果与实际不符");

		} catch (Exception e) {
			logger.error("采购规格询价遇到错误: ", e);
			Assert.fail("采购规格询价遇到错误: ", e);
		}
	}
}
