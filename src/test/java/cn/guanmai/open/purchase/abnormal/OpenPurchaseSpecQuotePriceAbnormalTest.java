package cn.guanmai.open.purchase.abnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.param.OpenQuotePriceParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年6月3日 上午11:34:46
 * @description:
 * @version: 1.0
 */

public class OpenPurchaseSpecQuotePriceAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseSpecQuotePriceAbnormalTest.class);
	private OpenCategoryServiceImpl openCategoryService;
	private String settle_supplier_id;
	private String spec_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);

		InitDataService initDataService = new InitDataServiceImpl(getSt_headers());
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
	public void quotePriceAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价不传入供应商ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价不传入供应商ID,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入空的供应商ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id("");
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入空的供应商ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入供应商ID为站点ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id("T10001");
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入供应商ID为站点ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入别的站点的供应商ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id("T2422");
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入别的站点的供应商ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价不传入采购规格ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入别的站点的供应商ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价不传入采购规格ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价不传入采购规格ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入空的采购规格ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id("");
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入空的采购规格ID,预期失败");

		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入别的站点的采购规格ID");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id("D1414098");
			openQuotePriceParam.setPurchase_price("5.12");

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入空的采购规格ID,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价不传入价格信息");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);

			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价不传入价格信息,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入空的价格信息");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("");
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入空的价格信息,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入字符型参数");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("a");
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入字符型参数,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入负数");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("-1");
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入负数,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入小数点多位数值");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("1.234");
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入小数点多位数值,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价传入过大值");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("10000000");
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入过大值,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常测试,采购规格询价描述信息输入过长字符");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("10");
			openQuotePriceParam.setRemark(StringUtil.getRandomString(65));
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入过大值,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}

	@Test
	public void quotePriceAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常测试,采购规格产地信息输入过长字符");
		try {
			OpenQuotePriceParam openQuotePriceParam = new OpenQuotePriceParam();
			openQuotePriceParam.setSettle_supplier_id(settle_supplier_id);
			openQuotePriceParam.setSpec_id(spec_id);
			openQuotePriceParam.setPurchase_price("10");
			openQuotePriceParam.setOrigin_place(StringUtil.getRandomString(65));
			boolean result = openCategoryService.updateQuotePrice(openQuotePriceParam);
			Assert.assertEquals(result, true, "异常测试,采购规格询价传入过大值,预期失败");
		} catch (Exception e) {
			logger.error("进行采购规格询价遇到错误: ", e);
			Assert.fail("进行采购规格询价遇到错误: ", e);
		}
	}
}
