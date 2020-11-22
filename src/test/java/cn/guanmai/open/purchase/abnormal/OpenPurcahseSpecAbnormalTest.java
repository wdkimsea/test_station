package cn.guanmai.open.purchase.abnormal;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.param.OpenPurchaseSpecParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.purchase.OpenPurcahseSpecTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年2月7日 下午5:21:14
 * @description:
 * @version: 1.0
 */

public class OpenPurcahseSpecAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurcahseSpecTest.class);
	private OpenCategoryServiceImpl openCategoryService;
	private String spu_id;
	private String spec_id;
	private OpenPurchaseSpecParam purchaseSpecParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCategoryService = new OpenCategoryServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();

		InitDataService initDataService = new InitDataServiceImpl(st_headers);
		try {
			InitDataBean initData = initDataService.getInitData();
			spu_id = initData.getSpu().getId();

			String spec_name = "测试新建";
			String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpu_id(spu_id);
			purchaseSpecParam.setSpec_name(spec_name);
			purchaseSpecParam.setPurchase_ratio("2");
			purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);

			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertNotEquals(spec_id, null, "新建采购规格失败");

		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		String spec_name = "测试新建";
		String purcahse_unit_name = StringUtil.getRandomString(2).toUpperCase();
		purchaseSpecParam = new OpenPurchaseSpecParam();
		purchaseSpecParam.setSpu_id(spu_id);
		purchaseSpecParam.setSpec_name(spec_name);
		purchaseSpecParam.setPurchase_ratio("2");
		purchaseSpecParam.setPurchase_unit_name(purcahse_unit_name);
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常新建采购规格,不传入SPU_ID");
		try {
			purchaseSpecParam.setSpu_id(null);
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入SPU_ID,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入空的SPU_ID");
		try {
			purchaseSpecParam.setSpu_id("");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入空的SPU_ID,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入错误的SPU_ID");
		try {
			purchaseSpecParam.setSpu_id("C100011");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入空的SPU_ID,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常新建采购规格,不传入purchase_unit_name");
		try {
			purchaseSpecParam.setPurchase_unit_name(null);
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入purchase_unit_name,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入空的purchase_unit_name");
		try {
			purchaseSpecParam.setPurchase_unit_name("");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入purchase_unit_name,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入过长的purchase_unit_name(大于12个字符)");
		try {
			purchaseSpecParam.setPurchase_unit_name(StringUtil.getRandomString(13));
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入过长的purchase_unit_name(大于12个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常新建采购规格,不传入purchase_ratio");
		try {
			purchaseSpecParam.setPurchase_ratio(null);
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入purchase_ratio,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入空的purchase_ratio");
		try {
			purchaseSpecParam.setPurchase_ratio("");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入purchase_ratio,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入purchase_ratio的值为0");
		try {
			purchaseSpecParam.setPurchase_ratio("0");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入purchase_ratio的值为0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入purchase_ratio的值为多位小数");
		try {
			purchaseSpecParam.setPurchase_ratio("0.123");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入purchase_ratio的值为多位小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入purchase_ratio的值为负数");
		try {
			purchaseSpecParam.setPurchase_ratio("-1");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入purchase_ratio的值为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入purchase_ratio的值过大(大于10000)");
		String spec_id = null;
		try {
			purchaseSpecParam.setPurchase_ratio("1000001");
			spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入purchase_ratio的值过大(大于10000),断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		} finally {
			try {
				if (spec_id != null) {
					openCategoryService.deletePurcahseSpec(spec_id);
				}
			} catch (Exception e) {
				logger.error("删除采购规格遇到错误: ", e);
				Assert.fail("删除采购规格遇到错误: ", e);
			}
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常新建采购规格,不传入spec_name");
		try {
			purchaseSpecParam.setSpec_name(null);
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,不传入spec_name,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入spec_name的为空");
		try {
			purchaseSpecParam.setSpec_name("");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入spec_name的位空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常新建采购规格,传入spec_name的值超过最大限制(32个字符)");
		try {
			purchaseSpecParam.setSpec_name("");
			String spec_id = openCategoryService.createPurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(spec_id, null, "异常新建采购规格,传入spec_name的值超过最大限制(32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常修改采购规格,不传入spec_id");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,不传入spec_id,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase17() {
		ReporterCSS.title("测试点: 异常修改采购规格,传入错误的spec_id");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id("D123456");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,不传入spec_id,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase18() {
		ReporterCSS.title("测试点: 异常修改采购规格,传入空的purchase_unit_name");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_unit_name("");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,传入空的purchase_unit_name,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase19() {
		ReporterCSS.title("测试点: 异常修改采购规格,传入过长的purchase_unit_name(大于12个字符)");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_unit_name(StringUtil.getRandomString(13));

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,传入过长的purchase_unit_name(大于12个字符),断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase20() {
		ReporterCSS.title("测试点: 异常修改采购规格,传入空的purchase_ratio");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,传入空的purchase_ratio,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase21() {
		ReporterCSS.title("测试点: 异常修改采购规格,传入非数字的purchase_ratio");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("A");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,传入非数字的purchase_ratio,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase22() {
		ReporterCSS.title("测试点: 异常修改采购规格,purchase_ratio传入为0");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("0");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,purchase_ratio传入为0,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase23() {
		ReporterCSS.title("测试点: 异常修改采购规格,purchase_ratio传入为负数");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("-1");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,purchase_ratio传入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase24() {
		ReporterCSS.title("测试点: 异常修改采购规格,purchase_ratio传入多位小数值");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("1.234");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,purchase_ratio传入多位小数值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase25() {
		ReporterCSS.title("测试点: 异常修改采购规格,purchase_ratio传入过大值");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setPurchase_ratio("1000001");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,purchase_ratio传入过大值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase26() {
		ReporterCSS.title("测试点: 异常修改采购规格,spec_name传入空值");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setSpec_name("");

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,spec_name传入空值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase27() {
		ReporterCSS.title("测试点: 异常修改采购规格,spec_name传入过长值(大于32个字符)");
		try {
			OpenPurchaseSpecParam purchaseSpecParam = new OpenPurchaseSpecParam();
			purchaseSpecParam.setSpec_id(spec_id);
			purchaseSpecParam.setSpec_name(StringUtil.getRandomString(33));

			boolean result = openCategoryService.updatePurchaseSpec(purchaseSpecParam);
			Assert.assertEquals(result, false, "异常修改采购规格,spec_name传入过长值(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("修改采购规格遇到错误: ", e);
			Assert.fail("修改采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase28() {
		ReporterCSS.title("测试点: 异常删除采购规格,采购规格ID输入为空");
		try {
			boolean result = openCategoryService.deletePurcahseSpec("");
			Assert.assertEquals(result, false, "异常删除采购规格,采购规格ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("删除采购规格遇到错误: ", e);
			Assert.fail("删除采购规格遇到错误: ", e);
		}
	}

	@Test
	public void openPurcahseSpecAbnormalTestCase29() {
		ReporterCSS.title("测试点: 异常删除采购规格,采购规格ID输入错误值");
		try {
			boolean result = openCategoryService.deletePurcahseSpec("D111123");
			Assert.assertEquals(result, false, "异常删除采购规格,采购规格ID输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("删除采购规格遇到错误: ", e);
			Assert.fail("删除采购规格遇到错误: ", e);
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			openCategoryService.deletePurcahseSpec(spec_id);
		} catch (Exception e) {
			logger.error("删除采购规格遇到错误: ", e);
			Assert.fail("删除采购规格遇到错误: ", e);
		}

	}

}
