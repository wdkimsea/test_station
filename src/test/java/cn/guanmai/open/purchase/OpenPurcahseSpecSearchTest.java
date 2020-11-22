package cn.guanmai.open.purchase;

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

public class OpenPurcahseSpecSearchTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurcahseSpecSearchTest.class);
	private OpenCategoryServiceImpl openCategoryService;

	@BeforeClass
	public void initData() {

		String access_token = getAccess_token();

		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void openPurcahseSpecSearchTestCase01() {
		ReporterCSS.title("测试点: 获取采购规格列表");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			List<OpenPurchaseSpecBean> specList = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);
			Assert.assertNotNull(specList, "获取采购规格列表");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecSearchTestCase02() {
		ReporterCSS.title("测试点: 获取采购规格列表,一级分类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			List<OpenPurchaseSpecBean> specList = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);
			Assert.assertNotEquals(specList.size(), 0, "未查询到采购规格无法执行此用例");

			OpenPurchaseSpecBean spec = specList.get(0);

			openPurchaseSpecFilterParam.setCategory1_id(spec.getCategory1_id());
			OpenPurchaseSpecBean res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam).stream()
					.filter(s -> s.getSpec_id().equals(spec.getSpec_id())).findAny().orElse(null);

			Assert.assertNotNull(res, "未获取到所需采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecSearchTestCase03() {
		ReporterCSS.title("测试点: 获取采购规格列表,二级分类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			List<OpenPurchaseSpecBean> specList = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);
			Assert.assertNotEquals(specList.size(), 0, "未查询到采购规格无法执行此用例");

			OpenPurchaseSpecBean spec = specList.get(0);

			openPurchaseSpecFilterParam.setCategory2_id(spec.getCategory2_id());
			OpenPurchaseSpecBean res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam).stream()
					.filter(s -> s.getSpec_id().equals(spec.getSpec_id())).findAny().orElse(null);

			Assert.assertNotNull(res, "未获取到所需采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecSearchTestCase04() {
		ReporterCSS.title("测试点: 获取采购规格列表,品类id");

		OpenPurchaseSpecFilterParam openPurchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();

		try {
			List<OpenPurchaseSpecBean> specList = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam);
			Assert.assertNotEquals(specList.size(), 0, "未查询到采购规格无法执行此用例");

			OpenPurchaseSpecBean spec = specList.get(0);

			openPurchaseSpecFilterParam.setPinlei_id(spec.getPinlei_id());
			OpenPurchaseSpecBean res = openCategoryService.queryPurchaseSpec(openPurchaseSpecFilterParam).stream()
					.filter(s -> s.getSpec_id().equals(spec.getSpec_id())).findAny().orElse(null);

			Assert.assertNotNull(res, "未获取到所需采购规格");
		} catch (Exception e) {
			logger.error("获取采购规格列表遇到错误: ", e);
			Assert.fail("获取采购规格列表遇到错误: ", e);
		}
	}
}
