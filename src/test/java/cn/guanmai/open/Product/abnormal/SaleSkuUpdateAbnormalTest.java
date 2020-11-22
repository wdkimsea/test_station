package cn.guanmai.open.Product.abnormal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.SalemenuFilterTest;
import cn.guanmai.open.bean.product.param.OpenSkuUpdateParam;
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
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 10, 2019 3:15:59 PM 
* @des OpenAPI 修改销售SKU
* @version 1.0 
*/
public class SaleSkuUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private InitDataService initDataService;
	private OpenCategoryService openCategoryService;
	private CategoryService categoryService;
	private OpenSkuUpdateParam updateParam;
	private String sku_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		Map<String, String> st_headers = new HashMap<>();

		categoryService = new CategoryServiceImpl(st_headers);
		initDataService = new InitDataServiceImpl(st_headers);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		try {
			InitDataBean initData = initDataService.getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");

			SpuBean spu = initData.getSpu();
			String spu_id = initData.getSpu().getId();

			List<SkuBean> skus = categoryService.getSaleSkus(spu_id);
			Assert.assertNotEquals(skus, null, "ST获取指定SPU下的销售规格失败");

			if (skus.size() == 0) {
				SkuBean sku = new SkuBean();
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
				Assert.assertNotEquals(sku_id, null, "新建销售SKU失败");
			} else {
				sku_id = skus.get(0).getId();
			}
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		updateParam = new OpenSkuUpdateParam();
		updateParam.setSku_id(sku_id);
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase01() {
		ReporterCSS.title("测试点: 修改销售SKU信息,SKU ID传入非本站点下的SKU对应的ID值,查看是否有验证");
		try {
			updateParam.setSku_id("D3803298");
			updateParam.setDesc("修改销售SKU信息,传入非本站点下的SKU ID,查看是否有验证");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,传入非本站点下的SKU ID,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase02() {
		ReporterCSS.title("测试点: 修改销售SKU信息,SKU ID传入为空的值,断言修改失败");
		try {
			updateParam.setSku_id("");
			updateParam.setDesc("修改销售SKU信息,SKU ID传入为空的值,断言修改失败");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,SKU ID传入为空的值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase03() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_name传入空的值,断言修改失败");
		try {
			updateParam.setSku_name("");
			updateParam.setDesc("修改销售SKU信息,传入空的sku_name值,断言修改失败");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,传入空的sku_name值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase04() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_name传入空格,断言修改失败");
		try {
			updateParam.setSku_name(" ");
			updateParam.setDesc("修改销售SKU信息,sku_name传入空格,断言修改失败");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_name传入空格,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase05() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_name传入过长的值,断言修改失败");
		try {
			updateParam.setSku_name(StringUtil.getRandomString(33).toUpperCase());
			updateParam.setDesc("修改销售SKU信息,传入过长的sku_name值,断言修改失败");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,传入过长的sku_name值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase06() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入为空的值,断言修改失败");
		try {
			updateParam.setSale_price("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,传入空的sale_price值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase07() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入非数值,断言修改失败");
		try {
			updateParam.setSale_price("A");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_price传入非数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase08() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入负数,断言修改失败");
		try {
			updateParam.setSale_price("-6");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_price传入负数,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase09() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入格式不对的数值,断言修改失败");
		try {
			updateParam.setSale_price("6.1.3");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_price传入格式不对的数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase10() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入过大的数值,断言修改失败");
		try {
			updateParam.setSale_price("10000000");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_price传入过大的数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase11() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_price传入小数位过长的小数,断言修改失败");
		try {
			updateParam.setSale_price("4.001");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_price传入小数位过长的小数,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase12() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入空值,断言修改失败");
		try {
			updateParam.setSale_num_least("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入空值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase13() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入非数值,断言修改失败");
		try {
			updateParam.setSale_num_least("A");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入非数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase14() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入负数值,断言修改失败");
		try {
			updateParam.setSale_num_least("-1");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入负数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase15() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入0,断言修改失败");
		try {
			updateParam.setSale_num_least("0");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入0,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase16() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入小数位多长的小数,断言修改失败");
		try {
			updateParam.setSale_num_least("0.001");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入0,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase17() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_num_least传入过大的数值,断言修改失败");
		try {
			updateParam.setSale_num_least("100000000");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_num_least传入过大的数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase18() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_ratio 传入空值,断言修改失败");
		try {
			updateParam.setSale_ratio("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_ratio 传入空值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase19() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_ratio 传入非数值,断言修改失败");
		try {
			updateParam.setSale_ratio("A");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_ratio 传入非数值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase20() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_ratio 传入负数,断言修改失败");
		try {
			updateParam.setSale_ratio("-1");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_ratio 传入负数,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase21() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_ratio 小数位过长的小数,断言修改失败");
		try {
			updateParam.setSale_ratio("1.009");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_ratio 小数位过长的小数,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase22() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_ratio 传入过大的数值,断言修改失败");
		try {
			updateParam.setSale_ratio("100000000");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_ratio 小数位过长的小数,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase23() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_unit_name 传入为空的值,断言修改失败");
		try {
			updateParam.setSale_unit_name("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_unit_name 传入为空的值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase24() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_unit_name 传入空格,断言修改失败");
		try {
			updateParam.setSale_unit_name(" ");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_unit_name 传入空格,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase25() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sale_unit_name 传入为过长的字符值,断言修改失败");
		try {
			updateParam.setSale_unit_name(StringUtil.getRandomString(33).toUpperCase());
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sale_unit_name 传入为过长的字符值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase26() {
		ReporterCSS.title("测试点: 修改销售SKU信息,is_weigh 传入非候选值,断言修改失败");
		try {
			updateParam.setWeighing("4");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,is_weigh 传入非候选值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase27() {
		ReporterCSS.title("测试点: 修改销售SKU信息,is_weigh 传入空值,断言修改失败");
		try {
			updateParam.setWeighing("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,is_weigh 传入空值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase28() {
		ReporterCSS.title("测试点: 修改销售SKU信息,state 传入空值,断言修改失败");
		try {
			updateParam.setState("");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,is_weigh 传入空值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase29() {
		ReporterCSS.title("测试点: 修改销售SKU信息,state 传入非候选值,断言修改失败");
		try {
			updateParam.setState("3");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,state 传入非候选值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase30() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_outer_id 传入空格");
		try {
			updateParam.setNew_sku_outer_id("  ");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_outer_id 传入空格");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase31() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_outer_id 过短字符,断言失败");
		try {
			updateParam.setNew_sku_outer_id("S");
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_outer_id 过短字符,断言失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase32() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_outer_id 过长字符,断言失败");
		try {
			updateParam.setNew_sku_outer_id("S" + StringUtil.getRandomString(20));
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_outer_id 过长字符,断言失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase33() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_outer_id 输入以D开头字符,断言失败");
		try {
			updateParam.setNew_sku_outer_id("D" + StringUtil.getRandomString(4));
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_outer_id 输入以D开头字符,断言失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuUpdateAbnormalTestCase34() {
		ReporterCSS.title("测试点: 修改销售SKU信息,sku_outer_id 输入以空格开头字符,断言失败");
		try {
			updateParam.setNew_sku_outer_id(" S" + StringUtil.getRandomString(4));
			boolean reuslt = openCategoryService.updateSaleSku(updateParam);
			Assert.assertEquals(reuslt, false, "修改销售SKU信息,sku_outer_id 输入以空格开头字符,断言失败");
		} catch (Exception e) {
			logger.error("修改销售SKU信息遇到错误: ", e);
			Assert.fail("修改销售SKU信息遇到错误: ", e);
		}
	}
}
