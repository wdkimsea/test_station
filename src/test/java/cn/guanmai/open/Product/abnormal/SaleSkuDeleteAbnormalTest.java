package cn.guanmai.open.Product.abnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.SalemenuFilterTest;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;

/* 
* @author liming 
* @date Jun 10, 2019 6:56:43 PM 
* @des 删除销售SKU异常测试
* @version 1.0 
*/
public class SaleSkuDeleteAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private OpenCategoryService openCategoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void saleSkuDeleteAbnormalTestCase01() {
		Reporter.log("测试点: 删除销售SKU,SKU ID传入空值,断言删除失败");
		try {
			boolean result = openCategoryService.deleteSaleSku("");
			Assert.assertEquals(result, false, "删除销售SKU,SKU ID传入空值,断言删除失败");
		} catch (Exception e) {
			logger.error("删除销售SKU遇到错误: ", e);
			Assert.fail("删除销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void saleSkuDeleteAbnormalTestCase02() {
		Reporter.log("测试点: 删除销售SKU,SKU ID传入其他站点下的SKU ID,断言删除失败");
		try {
			boolean result = openCategoryService.deleteSaleSku("D3239965");
			Assert.assertEquals(result, false, "删除销售SKU,SKU ID传入其他站点下的SKU ID,断言删除失败");
		} catch (Exception e) {
			logger.error("删除销售SKU遇到错误: ", e);
			Assert.fail("删除销售SKU遇到错误: ", e);
		}
	}

}
