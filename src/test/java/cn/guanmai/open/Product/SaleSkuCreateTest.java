package cn.guanmai.open.Product;

import cn.guanmai.open.bean.product.OpenSaleSkuDetailBean;
import cn.guanmai.open.bean.product.param.OpenSkuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSkuUpdateParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class SaleSkuCreateTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private OpenCategoryService openCategoryService;
	private InitDataService initDataService;
	private InitDataBean initData;
	private OpenSkuCreateParam openSkuCreateParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();
		initDataService = new InitDataServiceImpl(st_headers);
		try {
			initData = initDataService.getInitData();
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		openSkuCreateParam = new OpenSkuCreateParam();
		String supplier_id = initData.getSupplier().getId();
		String salemenu_id = initData.getSalemenu().getId();
		String spec_id = initData.getPurchaseSpec().getId();

		openSkuCreateParam.setSupplier_id(supplier_id);
		openSkuCreateParam.setSalemenu_id(salemenu_id);
		openSkuCreateParam.setSpec_id(spec_id);
		openSkuCreateParam.setSku_name(StringUtil.getRandomString(6));
		openSkuCreateParam.setSale_price("10");
		openSkuCreateParam.setSale_num_least("1");
		openSkuCreateParam.setSale_ratio("1");
		openSkuCreateParam.setSale_unit_name("包");
		openSkuCreateParam.setWeighing("1");
		openSkuCreateParam.setState("1");
		openSkuCreateParam.setIs_price_timing("0");
		openSkuCreateParam.setDesc("test");
		openSkuCreateParam.setSku_outer_id("S" + StringUtil.getRandomString(6));
	}

	@Test
	public void saleSkuCreateTestCase01() {
		Reporter.log("测试点: 新建销售SKU");
		String sku_id = null;
		try {

			sku_id = openCategoryService.createSaleSku(openSkuCreateParam);
			Assert.assertNotNull(sku_id, "新建销售SKU失败");

			OpenSaleSkuDetailBean openSaleSkuDetail = openCategoryService.getSaleSkuDetail(sku_id);
			Assert.assertNotEquals(openSaleSkuDetail, null, "获取供应商详情失败");

			String msg = null;
			boolean result = true;
			if (!openSkuCreateParam.getSku_name().equals(openSaleSkuDetail.getSku_name())) {
				msg = String.format("新建的销售SKU名称与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getSku_name(),
						openSaleSkuDetail.getSku_name());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getSale_ratio().equals(openSaleSkuDetail.getSale_ratio())) {
				msg = String.format("新建的销售SKU销售规格与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getSale_ratio(),
						openSaleSkuDetail.getSale_ratio());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getSalemenu_id().equals(openSaleSkuDetail.getSalemenu_id())) {
				msg = String.format("新建的销售SKU绑定的报价单与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getSalemenu_id(),
						openSaleSkuDetail.getSalemenu_id());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getState().equals(openSaleSkuDetail.getState())) {
				msg = String.format("新建的销售SKU的状态值与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getState(),
						openSaleSkuDetail.getState());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getWeighing().equals(openSaleSkuDetail.getWeighing())) {
				msg = String.format("新建的销售SKU的是否计重值与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getWeighing(),
						openSaleSkuDetail.getWeighing());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getIs_price_timing().equals(openSaleSkuDetail.getIs_price_timing())) {
				msg = String.format("新建的销售SKU的是否时价值与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getIs_price_timing(),
						openSaleSkuDetail.getIs_price_timing());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getSale_unit_name().equals(openSaleSkuDetail.getSale_unit_name())) {
				msg = String.format("新建的销售SKU的销售单位与预期不符,预期:%s,实际:%s", sku_id, openSkuCreateParam.getSale_unit_name(),
						openSaleSkuDetail.getSale_unit_name());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getSale_price().equals(openSaleSkuDetail.getSale_price())) {
				msg = String.format("新建的销售SKU的销售价格与预期不符,预期:%s,实际:%s", openSkuCreateParam.getSale_price(),
						openSaleSkuDetail.getSale_price());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSkuCreateParam.getSku_outer_id().toUpperCase().equals(openSaleSkuDetail.getSku_outer_id())) {
				msg = String.format("新建的销售SKU的自定义ID与预期不符,预期:%s,实际:%s",
						openSkuCreateParam.getSku_outer_id().toUpperCase(), openSaleSkuDetail.getSku_outer_id());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建SKU填写的信息与查询到的不一致");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		} finally {
			if (sku_id != null) {
				try {
					boolean result = openCategoryService.deleteSaleSku(sku_id);
					Assert.assertEquals(result, true, "删除销售SKU失败");
				} catch (Exception e) {
					logger.error("后置处理,删除销售SKU遇到错误: ", e);
					Assert.fail("后置处理,删除销售SKU遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void saleSkuUpdateTestCase02() {
		Reporter.log("测试点: 修改销售SKU");
		String sku_id = null;
		try {

			sku_id = openCategoryService.createSaleSku(openSkuCreateParam);
			Assert.assertNotNull(sku_id, "新建销售SKU失败");

			OpenSkuUpdateParam updateParam = new OpenSkuUpdateParam();
			updateParam.setSku_id(sku_id);
			updateParam.setNew_sku_outer_id("S" + StringUtil.getRandomString(5));
			updateParam.setSku_name(StringUtil.getRandomString(6));
			updateParam.setSale_price("10");
			updateParam.setSale_num_least("2");
			updateParam.setSale_ratio("2");
			updateParam.setSale_unit_name("NM");
			updateParam.setDesc(StringUtil.getRandomString(6));
			updateParam.setWeighing("0");
			updateParam.setState("0");

			boolean res = openCategoryService.updateSaleSku(updateParam);
			Assert.assertNotEquals(res, null, "修改sku失败");

			OpenSaleSkuDetailBean openSaleSkuDetail = openCategoryService
					.getSaleSkuDetailByOuterId(updateParam.getNew_sku_outer_id());
			Assert.assertNotEquals(openSaleSkuDetail, null, "获取sku详情失败");

			String msg = null;
			boolean result = true;
			if (!updateParam.getSku_name().equals(openSaleSkuDetail.getSku_name())) {
				msg = String.format("新建的销售SKU名称与预期不符,预期:%s,实际:%s", updateParam.getSku_name(),
						openSaleSkuDetail.getSku_name());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getSale_ratio().equals(openSaleSkuDetail.getSale_ratio())) {
				msg = String.format("新建的销售SKU销售规格与预期不符,预期:%s,实际:%s", updateParam.getSale_ratio(),
						openSaleSkuDetail.getSale_ratio());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getState().equals(openSaleSkuDetail.getState())) {
				msg = String.format("新建的销售SKU的状态值与预期不符,预期:%s,实际:%s", updateParam.getState(),
						openSaleSkuDetail.getState());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getWeighing().equals(openSaleSkuDetail.getWeighing())) {
				msg = String.format("新建的销售SKU的是否计重值与预期不符,预期:%s,实际:%s", updateParam.getWeighing(),
						openSaleSkuDetail.getWeighing());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getSale_unit_name().equals(openSaleSkuDetail.getSale_unit_name())) {
				msg = String.format("新建的销售SKU的销售单位与预期不符,预期:%s,实际:%s", updateParam.getSale_unit_name(),
						openSaleSkuDetail.getSale_unit_name());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getSale_price().equals(openSaleSkuDetail.getSale_price())) {
				msg = String.format("新建的销售SKU的销售价格与预期不符,预期:%s,实际:%s", updateParam.getSale_price(),
						openSaleSkuDetail.getSale_price());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			if (!updateParam.getNew_sku_outer_id().toUpperCase().equals(openSaleSkuDetail.getSku_outer_id())) {
				msg = String.format("新建的销售SKU的自定义ID与预期不符,预期:%s,实际:%s", updateParam.getNew_sku_outer_id().toUpperCase(),
						openSaleSkuDetail.getSku_outer_id());
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建SKU填写的信息与查询到的不一致");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		} finally {
			if (sku_id != null) {
				try {
					boolean result = openCategoryService.deleteSaleSku(sku_id);
					Assert.assertEquals(result, true, "删除销售SKU失败");
				} catch (Exception e) {
					logger.error("后置处理,删除销售SKU遇到错误: ", e);
					Assert.fail("后置处理,删除销售SKU遇到错误: ", e);
				}
			}
		}
	}
}
