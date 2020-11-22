package cn.guanmai.open.Product.abnormal;

import cn.guanmai.open.Product.SalemenuFilterTest;
import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.product.param.OpenSkuCreateParam;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class SaleSkuCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private OpenCategoryService openCategoryService;
	private OpenSupplierService openSupplierService;
	private OpenSalemenuService salemenu_srv;

	private OpenSkuCreateParam openSkuCreateParam = null;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		salemenu_srv = new OpenSalemenuServiceImpl(access_token);

	}

	private OpenSkuCreateParam get_default_param() {

		if (openSkuCreateParam == null) {
			openSkuCreateParam = new OpenSkuCreateParam();
			OpenSupplierFilterParam openSupplierFilterParam = new OpenSupplierFilterParam();
			OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

			try {
				openSupplierFilterParam.setLimit("5");
				List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(openSupplierFilterParam);
				Assert.assertNotEquals(suppliers.size(), 0, "未查到符合条件的供应商，无法执行该用例");

				OpenSupplierDetailBean supplier = null;
				List<OpenPurchaseSpecBean> specs = null;

				for (OpenSupplierBean _supplier : suppliers) {
					OpenSupplierDetailBean supplier_detail = openSupplierService
							.getSupplierDetail(_supplier.getSupplier_id());
					if (supplier_detail.getCategory2().size() > 0) {
						openPurchaseSpecFilterParam
								.setCategory2_id(supplier_detail.getCategory2().get(0).getCategory2_id());
						openPurchaseSpecFilterParam.setLimit("5");
						specs = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);
						if (specs.size() > 0) {
							supplier = supplier_detail;
							break;
						}
					}
				}

				Assert.assertNotNull(supplier, "未查到符合条件的供应商，无法执行该用例");

				List<OpenSalemenuBean> salemenus = salemenu_srv.searchSalemenu(null, 1);
				Assert.assertNotEquals(salemenus.size(), 0, "未查到符合条件的报价单，无法执行该用例");

				openSkuCreateParam.setSupplier_id(supplier.getSupplier_id());
				openSkuCreateParam.setSalemenu_id(salemenus.get(0).getId());
				openSkuCreateParam.setSpec_id(specs.get(0).getSpec_id());
				openSkuCreateParam.setSku_name(StringUtil.getRandomString(6));
				openSkuCreateParam.setSale_price("10");
				openSkuCreateParam.setSale_num_least("1");
				openSkuCreateParam.setSale_ratio("1");
				openSkuCreateParam.setSale_unit_name("斤");
				openSkuCreateParam.setWeighing("1");
				openSkuCreateParam.setState("1");
				openSkuCreateParam.setIs_price_timing("0");
				openSkuCreateParam.setDesc("test");

				String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

				Assert.assertNotNull(sku_id, "新建销售SKU");

				openCategoryService.deleteSaleSku(sku_id);

			} catch (Exception e) {
				logger.error("新建销售SKU遇到错误: ", e);
				Assert.fail("新建销售SKU遇到错误: ", e);
			}
		}

		return openSkuCreateParam.clone();
	}

	@Test
	public void SaleSkuCreateTestCase01() {
		ReporterCSS.title("测试点: 新建销售SKU, 传入无效的供应商id");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();

			openSkuCreateParam.setSupplier_id("T001");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase02() {
		ReporterCSS.title("测试点: 新建销售SKU, 传入无效的报价单id");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();

			openSkuCreateParam.setSalemenu_id("S001");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase03() {
		ReporterCSS.title("测试点: 新建销售SKU, 传入无效的采购规格id");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();

			openSkuCreateParam.setSpec_id("C001");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase04() {
		ReporterCSS.title("测试点: 新建销售SKU, 传入过长的sku名");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();

			openSkuCreateParam.setSku_name(StringUtil.getRandomString(33));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase05() {
		ReporterCSS.title("测试点: 新建销售SKU, 传入空的sku名");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();

			openSkuCreateParam.setSku_name("");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase06() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_price传0");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_price("0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase07() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_price传过大值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_price("10000000");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase08() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_price传非float类型");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_price("K0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase09() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_num_least传非float类型");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_num_least("K0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase10() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_num_least传0");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_num_least("0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase11() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_num_least传过大值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_num_least("10000000");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase12() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_ratio传0");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_ratio("0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase13() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_ratio传过大值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_ratio("10000000");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase14() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_ratio传非float类型");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_ratio("k0");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase15() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_unit_name传空值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_unit_name("");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase16() {
		ReporterCSS.title("测试点: 新建销售SKU, weighing传无效值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setWeighing("10");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase17() {
		ReporterCSS.title("测试点: 新建销售SKU, state传无效值");

		OpenSkuCreateParam openSkuCreateParam;

		try {

			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setState("10");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase18() {
		ReporterCSS.title("测试点: 新建销售SKU, is_price_timing传无效值");

		OpenSkuCreateParam openSkuCreateParam;

		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setIs_price_timing("10");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase19() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入空格 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id("   ");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase20() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入过短字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id("W");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase21() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入过长字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id("W" + StringUtil.getRandomString(20));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase22() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入以D开头字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id("D" + StringUtil.getRandomString(5));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase23() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入以d开头字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id("d" + StringUtil.getRandomString(5));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase24() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_outer_id 传入以空格开头字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_outer_id(" A" + StringUtil.getRandomString(5));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase25() {
		ReporterCSS.title("测试点: 新建销售SKU, sku_name 输入以空格开头字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSku_name(" A" + StringUtil.getRandomString(5));

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");
		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void SaleSkuCreateTestCase26() {
		ReporterCSS.title("测试点: 新建销售SKU, sale_unit_name 输入以空格开头字符 ,断言失败");
		OpenSkuCreateParam openSkuCreateParam;
		try {
			openSkuCreateParam = this.get_default_param();
			openSkuCreateParam.setSale_unit_name(" A");

			String sku_id = openCategoryService.createSaleSku(openSkuCreateParam);

			Assert.assertNull(sku_id, "新建销售SKU");
		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

}
