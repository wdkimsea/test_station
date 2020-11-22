package cn.guanmai.open.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.category.CategoryService;

/* 
* @author liming 
* @date Jun 10, 2019 7:14:43 PM 
* @des 开放平台销售SKU过滤异常测试
* @version 1.0 
*/
public class SaleSkuFilterTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private InitDataService initDataService;
	private OpenCategoryService openCategoryService;
	private CategoryService categoryService;
	private OpenSaleSkuFilterParam filterParam;
	private SkuBean sku;
	private String spu_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		Map<String, String> st_headers = getSt_headers();
		categoryService = new CategoryServiceImpl(st_headers);
		initDataService = new InitDataServiceImpl(st_headers);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		try {
			InitDataBean initData = initDataService.getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");

			SpuBean spu = initData.getSpu();
			spu_id = initData.getSpu().getId();

			List<SkuBean> skus = categoryService.getSaleSkus(spu_id);
			Assert.assertNotEquals(skus, null, "ST获取指定SPU下的销售规格失败");

			String sku_id = null;
			if (skus.size() == 0) {
				sku = new SkuBean();
				// 填写SKU相关属性值,创建SKU必填
				sku = new SkuBean();
				sku.setSpu_id(spu_id);
				sku.setOuter_id("");
				sku.setStd_sale_price(new BigDecimal("400"));
				sku.setPartframe(1);
				sku.setStd_unit_name(spu.getStd_unit_name());
				sku.setSlitting(1);
				sku.setSale_num_least(new BigDecimal("1"));
				sku.setStocks("-99999");
				sku.setSale_ratio(new BigDecimal("1"));
				sku.setSale_unit_name(spu.getStd_unit_name());
				sku.setDesc("beforeMethod");
				sku.setSupplier_id(initData.getSupplier().getId());
				sku.setIs_price_timing(0);
				sku.setIs_weigh(1);
				sku.setPurchase_spec_id(initData.getPurchaseSpec().getId());
				sku.setAttrition_rate(BigDecimal.ZERO);
				sku.setStock_type(1);
				sku.setName(spu.getName() + "|" + spu.getStd_unit_name());
				sku.setSalemenu_id(initData.getSalemenu().getId());
				sku.setState(1);
				sku.setSale_price(new BigDecimal("400"));
				sku.setRemark_type(7);

				sku_id = categoryService.createSaleSku(sku);
				Assert.assertNotEquals(sku_id, null, "ST新建销售SKU失败");
			} else {
				sku_id = skus.get(0).getId();
			}

			sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "ST获取SKU详细信息失败");
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		filterParam = new OpenSaleSkuFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(100);
	}

	@Test
	public void saleSkuFilterTestCase01() {
		Reporter.log("测试点: 搜索过滤销售SKU,传入其他站点的SPU ID,搜索结果应该为空");
		try {
			filterParam.setSpu_id("C116450");
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");
			Assert.assertEquals(openSaleSkus.size(), 0, "搜索过滤销售SKU,传入其他站点的SPU ID,搜索结果应该为空");
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuFilterTestCase02() {
		Reporter.log("测试点: 搜索过滤销售SKU,传入其他站点的salemenu_id值,搜索结果应该为空");
		try {
			filterParam.setSalemenu_id("S1006");
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");
			Assert.assertEquals(openSaleSkus.size(), 0, "搜索过滤销售SKU,传入其他站点的SPU ID,搜索结果应该为空");
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuFilterTestCase03() {
		Reporter.log("测试点: 搜索过滤销售SKU,使用SPU ID进行搜索");
		try {
			filterParam.setSpu_id(spu_id);
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");

			OpenSaleSkuBean openSaleSku = openSaleSkus.stream().filter(s -> s.getId().equals(sku.getId())).findAny()
					.orElse(null);
			Assert.assertNotEquals(openSaleSku, null, "搜索过滤销售SKU,使用SPU ID进行搜索,没有搜索到销售SKU " + sku.getId());
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuFilterTestCase04() {
		Reporter.log("测试点: 搜索过滤销售SKU,使用salemenu_id进行搜索");
		try {
			filterParam.setSalemenu_id(sku.getSalemenu_id());
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");

			OpenSaleSkuBean openSaleSku = openSaleSkus.stream().filter(s -> s.getId().equals(sku.getId())).findAny()
					.orElse(null);
			Assert.assertNotEquals(openSaleSku, null, "搜索过滤销售SKU,使用salemenu_id进行搜索,没有搜索到销售SKU " + sku.getId());
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuFilterTestCase05() {
		Reporter.log("测试点: 搜索过滤销售SKU,使用search_text进行搜索");
		try {
			filterParam.setSearch_text(sku.getName());
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");

			OpenSaleSkuBean openSaleSku = openSaleSkus.stream().filter(s -> s.getId().equals(sku.getId())).findAny()
					.orElse(null);
			Assert.assertNotEquals(openSaleSku, null, "搜索过滤销售SKU,使用search_text进行搜索,没有搜索到销售SKU " + sku.getId());
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuFilterTestCase06() {
		Reporter.log("测试点: 搜索过滤销售SKU,使用所有添加进行组合搜索");
		try {
			filterParam.setSearch_text(sku.getName());
			filterParam.setSalemenu_id(sku.getSalemenu_id());
			filterParam.setSpu_id(sku.getSpu_id());
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(filterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索过滤销售SKU失败");

			OpenSaleSkuBean openSaleSku = openSaleSkus.stream().filter(s -> s.getId().equals(sku.getId())).findAny()
					.orElse(null);
			Assert.assertNotEquals(openSaleSku, null, "搜索过滤销售SKU,使用所有添加进行组合搜索,没有搜索到销售SKU " + sku.getId());
		} catch (Exception e) {
			logger.error("搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("搜索过滤销售SKU遇到错误: ", e);
		}
	}
}
