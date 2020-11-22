package cn.guanmai.open.Product.abnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;

/* 
* @author liming 
* @date Jun 10, 2019 6:45:15 PM 
* @des 删除SPU异常测试
* @version 1.0 
*/
public class SpuDeleteAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryService categoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void spuDeleteAbnormalTestCase01() {
		Reporter.log("测试点: 删除SPU,SPU ID传入空值,断言失败");
		try {
			boolean result = categoryService.deleteSpu("");
			Assert.assertEquals(result, false, "删除SPU,SPU ID传入空值,断言失败");
		} catch (Exception e) {
			logger.error("删除SPU遇到错误: ", e);
			Assert.fail("删除SPU遇到错误: ", e);
		}
	}

	@Test
	public void spuDeleteAbnormalTestCase02() {
		Reporter.log("测试点: 删除SPU,SPU ID传入别的GROUP下的SPU ID,断言失败");
		try {
			boolean result = categoryService.deleteSpu("C369758");
			Assert.assertEquals(result, false, "删除SPU,SPU ID传入别的GROUP下的SPU ID,断言失败");
		} catch (Exception e) {
			logger.error("删除SPU遇到错误: ", e);
			Assert.fail("删除SPU遇到错误: ", e);
		}
	}

}
