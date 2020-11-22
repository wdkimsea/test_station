package cn.guanmai.open.stock.abnormal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
import cn.guanmai.open.bean.stock.param.OpenStockOutCommonParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockOutServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.stock.OpenStockOutService;
import cn.guanmai.open.stock.OpenStockInTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年11月4日
 * @time 下午4:15:40
 * @des TODO
 */

public class OpenStockOutAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockInTest.class);
	private OpenStockOutService openStockOutService;
	private OpenCategoryService openCategoryService;
	private OpenStockOutCommonParam openStockOutCommonParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockOutService = new OpenStockOutServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	@BeforeMethod
	public void beforeMethod() {
		openStockOutCommonParam = new OpenStockOutCommonParam();
		openStockOutCommonParam.setCustomer_name("OPENAPI");
		openStockOutCommonParam.setOut_stock_sheet_id("OPENAPI" + StringUtil.getRandomString(6).toUpperCase());
		List<OpenStockOutCommonParam.Detail> details = new ArrayList<>();

		try {
			OpenSaleSkuFilterParam openSaleSkuFilterParam = new OpenSaleSkuFilterParam();
			openSaleSkuFilterParam.setOffset(0);
			openSaleSkuFilterParam.setLimit(100);
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(openSaleSkuFilterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索SKU失败");

			openSaleSkus = openSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList());
			Assert.assertEquals(openSaleSkus.size() > 0, true, "没有销售状态的商品");

			OpenStockOutCommonParam.Detail detail = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				detail = openStockOutCommonParam.new Detail();
				detail.setSku_id(openSaleSku.getId());
				detail.setOut_stock_count(NumberUtil.getRandomNumber(5, 15, 2).toString());
				details.add(detail);
				if (details.size() > 10) {
					break;
				}
			}
			openStockOutCommonParam.setDetails(details);
		} catch (Exception e) {
			logger.error("初始化出库单参数遇到错误", e);
			Assert.fail("初始化出库单参数遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase01() {
		ReporterCSS.title("测试点: 新建出库单,出库单输入为空,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id("");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase02() {
		ReporterCSS.title("测试点: 新建出库单,出库单ID输入以PL开头,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id("PLV0001");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单ID输入以PL开头,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase03() {
		ReporterCSS.title("测试点: 新建出库单,出库单ID输入以LK开头,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id("LKV0001");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单ID输入以LK开头,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase04() {
		ReporterCSS.title("测试点: 新建出库单,出库单ID输入过短字符,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id(StringUtil.getRandomString(4));
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单ID输入过短字符,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase05() {
		ReporterCSS.title("测试点: 新建出库单,出库单ID输入过长字符,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id(StringUtil.getRandomString(33));
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单ID输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase06() {
		ReporterCSS.title("测试点: 新建出库单,出库单ID输入包含空格,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id(StringUtil.getRandomString(8) + " ");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单ID输入包含空格,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase07() {
		ReporterCSS.title("测试点: 新建出库单,出库单对象输入过长字符,断言失败");
		try {
			openStockOutCommonParam.setCustomer_name(StringUtil.getRandomString(33));
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单对象输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase08() {
		ReporterCSS.title("测试点: 新建出库单,出库单对象输入空格,断言失败");
		try {
			openStockOutCommonParam.setCustomer_name("   ");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单对象输入空格,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase09() {
		ReporterCSS.title("测试点: 新建出库单,出库单对象输入包含空格,断言失败");
		try {
			openStockOutCommonParam.setCustomer_name("CBD ");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单对象输入包含空格,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase10() {
		ReporterCSS.title("测试点: 新建出库单,出库SKU输入为空,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setSku_id("");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库SKU输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase11() {
		ReporterCSS.title("测试点: 新建出库单,出库SKU输入错误值,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setSku_id("D123323");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库SKU输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase12() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入为空,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase13() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入为非数字,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("a");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase14() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入为负数,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("-12");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase15() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入为0,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("0");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入为0,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase16() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入过大值,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("10000001");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入为0,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase17() {
		ReporterCSS.title("测试点: 新建出库单,出库数输入过长浮点数,断言失败");
		try {
			openStockOutCommonParam.getDetails().get(0).setOut_stock_count("1.234");
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库数输入过长浮点数,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase18() {
		ReporterCSS.title("测试点: 新建出库单,出库单输入重复ID,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单,断言成功");

			stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(stock_out_sheet_id, null, "新建出库单,出库单输入重复ID,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase19() {
		ReporterCSS.title("测试点: 修改出库单,出库单ID输入为空,断言失败");
		try {
			openStockOutCommonParam.setOut_stock_sheet_id("");
			boolean result = openStockOutService.updateStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(result, false, "修改出库单,出库单ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase20() {
		ReporterCSS.title("测试点: 修改出库单,修改已经出库的出库单,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean reuslt = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, true, "提交出库单失败");

			reuslt = openStockOutService.updateStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(reuslt, false, "修改出库单,修改已经出库的出库单,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase21() {
		ReporterCSS.title("测试点: 修改出库单,修改已经冲销的出库单,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean reuslt = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, true, "冲销出库单失败");

			reuslt = openStockOutService.updateStockOutSheet(openStockOutCommonParam);
			Assert.assertEquals(reuslt, false, "修改出库单,修改已经冲销的出库单,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase22() {
		ReporterCSS.title("测试点: 提交出库单,提交已经提交的出库单,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean reuslt = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, true, "提交出库单失败");

			reuslt = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, false, "提交出库单,提交已经提交的出库单,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase23() {
		ReporterCSS.title("测试点: 冲销出库单,冲销已经冲销的出库单,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean reuslt = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, true, "冲销出库单失败");

			reuslt = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, false, "冲销出库单,冲销已经冲销的出库单,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutAbnormalTestCase24() {
		ReporterCSS.title("测试点: 提交出库单,提交已经冲销的出库单,断言失败");
		try {
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCommonParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean reuslt = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, true, "冲销出库单失败");

			reuslt = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(reuslt, false, "提交出库单,提交已经冲销的出库单,断言失败");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

}
