package cn.guanmai.open.purchase;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.param.OpenPurchaseSpecParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年2月7日 下午4:16:34
 * @description:
 * @version: 1.0
 */

public class OpenPurcahseSpecTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurcahseSpecTest.class);
	private OpenCategoryServiceImpl openCategoryService;
	private CategoryService categoryService;
	private String spu_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();

		categoryService = new CategoryServiceImpl(st_headers);
		InitDataService initDataService = new InitDataServiceImpl(st_headers);
		try {
			InitDataBean initData = initDataService.getInitData();
			spu_id = initData.getSpu().getId();
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecTestCase01() {
		ReporterCSS.title("测试点: 新建采购规格");
		String spec_id = null;
		try {
			String spec_name = "测试新建";
			String purchase_ratio = "2";
			String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpu_id(spu_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio(purchase_ratio);
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertNotEquals(spec_id, null, "新建采购规格失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "从ST接口获取采购规格信息失败");

			String msg = null;
			boolean result = true;
			if (!purchaseSpec.getName().equals(spec_name)) {
				msg = String.format("新建的采购规格名称与预期的不一致,预期:%s,实际:%s", spec_name, purchaseSpec.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!purchaseSpec.getPurchase_unit().equals(purcahse_unit_name)) {
				msg = String.format("新建的采购规格的采购名称与预期的不一致,预期:%s,实际:%s", purcahse_unit_name,
						purchaseSpec.getPurchase_unit_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!purchaseSpec.getSpu_id().equals(spu_id)) {
				msg = String.format("新建的采购规格的对应的SPU_ID与预期的不一致,预期:%s,实际:%s", spu_id, purchaseSpec.getSpu_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (purchaseSpec.getRatio().compareTo(new BigDecimal(purchase_ratio)) != 0) {
				msg = String.format("新建的采购规格的对应的purchase_ratio与预期的不一致,预期:%s,实际:%s", purchase_ratio,
						purchaseSpec.getRatio());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的采购规格信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		} finally {
			try {
				ReporterCSS.title("后置处理: 删除采购规格");
				boolean result = openCategoryService.deletePurcahseSpec(spec_id);
				Assert.assertEquals(result, true, "删除采购规格失败");
			} catch (Exception e) {
				logger.error("删除采购规格遇到错误: ", e);
				Assert.fail("删除采购规格遇到错误: ", e);
			}
		}
	}

	@Test
	public void openPurcahseSpecTestCase02() {
		ReporterCSS.title("测试点: 新建采购规格,使用相同的采购规格,断言失败");
		String spec_id = null;
		try {
			String spec_name = "测试新建";
			String purchase_ratio = "2";
			String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpu_id(spu_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio(purchase_ratio);
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertNotEquals(spec_id, null, "新建采购规格失败");

			String tem_spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(tem_spec_id, null, "新建采购规格,使用相同的采购规格,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		} finally {
			try {
				ReporterCSS.title("后置处理: 删除采购规格");
				boolean result = openCategoryService.deletePurcahseSpec(spec_id);
				Assert.assertEquals(result, true, "删除采购规格失败");
			} catch (Exception e) {
				logger.error("删除采购规格遇到错误: ", e);
				Assert.fail("删除采购规格遇到错误: ", e);
			}
		}
	}

	@Test
	public void openPurcahseSpecTestCase03() {
		ReporterCSS.title("测试点: 修改采购规格");
		String spec_id = null;
		try {
			String spec_name = "测试新建";
			String purchase_ratio = "2";
			String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpu_id(spu_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio(purchase_ratio);
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertNotEquals(spec_id, null, "新建采购规格失败");

			spec_name = "测试新建New";
			purchase_ratio = "2.1";
			purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio(purchase_ratio);
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, true, "修改采购规格失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "从ST接口获取采购规格信息失败");

			String msg = null;

			if (!purchaseSpec.getName().equals(spec_name)) {
				msg = String.format("修改的采购规格名称与预期的不一致,预期:%s,实际:%s", spec_name, purchaseSpec.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!purchaseSpec.getPurchase_unit().equals(purcahse_unit_name)) {
				msg = String.format("修改的采购规格的采购名称与预期的不一致,预期:%s,实际:%s", purcahse_unit_name,
						purchaseSpec.getPurchase_unit_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (purchaseSpec.getRatio().compareTo(new BigDecimal(purchase_ratio)) != 0) {
				msg = String.format("修改的采购规格的对应的purchase_ratio与预期的不一致,预期:%s,实际:%s", purchase_ratio,
						purchaseSpec.getRatio());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的采购规格信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		} finally {
			try {
				ReporterCSS.title("后置处理: 删除采购规格");
				boolean result = openCategoryService.deletePurcahseSpec(spec_id);
				Assert.assertEquals(result, true, "删除采购规格失败");
			} catch (Exception e) {
				logger.error("删除采购规格遇到错误: ", e);
				Assert.fail("删除采购规格遇到错误: ", e);
			}
		}
	}

	@Test
	public void openPurcahseSpecTestCase04() {
		ReporterCSS.title("测试点: 删除采购规格");
		String spec_id = null;
		try {
			String spec_name = "测试新建";
			String purchase_ratio = "2";
			String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpu_id(spu_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio(purchase_ratio);
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertNotEquals(spec_id, null, "新建采购规格失败");

			boolean result = openCategoryService.deletePurcahseSpec(spec_id);
			Assert.assertEquals(result, true, "删除采购规格失败");

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertEquals(purchaseSpec, null, "采购规格没有删除成功,还可以查询到");

		} catch (Exception e) {
			logger.error("删除采购规格遇到错误: ", e);
			Assert.fail("删除采购规格遇到错误: ", e);
		}
	}
}
