package cn.guanmai.open.purchase.abnormal;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class OpenPurcahseSpecSearchAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryServiceImpl openCategoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();

		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void openSpecAbnormalTestCase01() {
		ReporterCSS.title("测试点: 获取采购规格列表,无效的一级分类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			openPurchaseSpecFilterParam.setCategory1_id("B001");
			List<OpenPurchaseSpecBean> res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);

			Assert.assertNull(res, "获取到无效的采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void openSpecAbnormalTestCase02() {
		ReporterCSS.title("测试点: 获取采购规格列表,无效的二级分类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			openPurchaseSpecFilterParam.setCategory2_id("A001");
			List<OpenPurchaseSpecBean> res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);

			Assert.assertNull(res, "获取到无效的采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void openSpecAbnormalTestCase03() {
		ReporterCSS.title("测试点: 获取采购规格列表,无效的品类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			openPurchaseSpecFilterParam.setPinlei_id("A001");
			List<OpenPurchaseSpecBean> res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);

			Assert.assertNull(res, "获取到无效的采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}
}