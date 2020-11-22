package cn.guanmai.open.stock.abnormal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.stock.OpenStockInSheetBean;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockInCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockInSheetFilterParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierCommonParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockInServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.stock.OpenStockInService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月1日
 * @time 下午2:31:32
 * @des TODO
 */

public class OpenStockInAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenSupplierServiceImpl openSupplierService;
	private OpenCategoryService openCategoryService;
	private OpenStockInService openStockInService;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OpenStockInCommonParam openStockInCommonParam;

	private CategoryService categoryService;
	private OpenSupplierDetailBean openSupplierDetail = null;

	@BeforeTest
	public void initData() {
		String access_token = getAccess_token();
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		openStockInService = new OpenStockInServiceImpl(access_token);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			String category2_id = null, supplier_id = null;
			if (openSupplierDetail == null) {
				List<OpenSupplierBean> suppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
				Assert.assertNotEquals(suppliers, null, "获取供应商列表遇到错误");

				Assert.assertEquals(suppliers.size() > 0, true, "供应商列表为空,无法创建入库单");

				OpenSupplierBean supplier = NumberUtil.roundNumberInList(suppliers);

				supplier_id = supplier.getSupplier_id();
				openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
				Assert.assertNotEquals(openSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

				List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
				Assert.assertEquals(category2List.size() > 0, true, "供应商" + supplier_id + "没有绑定二级分类,无法进行采购入库操作");

				category2_id = category2List.get(0).getCategory2_id();

			} else {
				supplier_id = openSupplierDetail.getSupplier_id();
				List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();
				category2_id = category2List.get(0).getCategory2_id();
			}

			OpenPurchaseSpecFilterParam purchaseSpecFilterParam = new OpenPurchaseSpecFilterParam();
			purchaseSpecFilterParam.setCategory2_id(category2_id);
			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");

			openStockInCommonParam = new OpenStockInCommonParam();
			openStockInCommonParam.setSupplier_id(supplier_id);
			openStockInCommonParam.setSubmit_date(todayStr);
			openStockInCommonParam.setDiscounts(new ArrayList<>());

			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = null;
			for (OpenPurchaseSpecBean openPurchaseSpec : openPurchaseSpecList) {
				detail = openStockInCommonParam.new Detail();
				detail.setIn_stock_count(NumberUtil.getRandomNumber(10, 20, 2).toString());
				detail.setIn_stock_unit_price(NumberUtil.getRandomNumber(5, 10, 2).toString());
				detail.setSpec_id(openPurchaseSpec.getSpec_id());
				detail.setRemark(StringUtil.getRandomString(6));
				details.add(detail);
				if (details.size() >= 20) {
					break;
				}
			}
			Assert.assertEquals(details.size() >= 1, true, "此供应商无对应采购规格");
			openStockInCommonParam.setDetails(details);
		} catch (Exception e) {
			logger.error("初始化参数遇到错误", e);
			Assert.fail("初始化参数遇到错误", e);
		}

	}

	@Test
	public void stockInAbnormalTestCase01() {
		ReporterCSS.title("测试点: 新建采购入库单,供应商输入空值");
		try {
			openStockInCommonParam.setSupplier_id("");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,供应商输入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase02() {
		ReporterCSS.title("测试点: 新建采购入库单,供应商输入空格");
		try {
			openStockInCommonParam.setSupplier_id(" ");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,供应商输入空格,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase03() {
		ReporterCSS.title("测试点: 新建采购入库单,供应商输入非法值");
		try {
			openStockInCommonParam.setSupplier_id("T248");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,供应商输入空格,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase04() {
		ReporterCSS.title("测试点: 新建采购入库单,供应商输入非法值");
		try {
			openStockInCommonParam.setSupplier_id("T248");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,供应商输入空格,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase05() {
		ReporterCSS.title("测试点: 新建采购入库单,供应商与采购规格不匹配(不能供应)");
		String category1_id = null;
		String category2_id = null;
		List<String> category2_ids = null;
		OpenSupplierCommonParam openSupplierUpdateParam = null;
		try {
			category1_id = categoryService.createCategory1(new Category1Bean(StringUtil.getRandomString(6), 1));
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");

			category2_id = categoryService
					.createCategory2(new Category2Bean(category1_id, StringUtil.getRandomString(6)));
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			String supplier_id = openStockInCommonParam.getSupplier_id();

			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详细信息失败");

			category2_ids = openSupplierDetail.getCategory2().stream().map(g -> g.getCategory2_id())
					.collect(Collectors.toList());

			openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCategory2_ids(Arrays.asList(category2_id));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, true, "修改供应商信息失败");

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,供应商与采购规格不匹配(不能供应),断言失败");

		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		} finally {
			if (category2_id != null) {
				try {
					categoryService.deleteCategory2ById(category2_id);
					categoryService.deleteCategory1ById(category1_id);
					openSupplierUpdateParam.setCategory2_ids(category2_ids);
					boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
					Assert.assertEquals(result, true, "修改供应商信息失败");
				} catch (Exception e) {
					logger.error("后置处理,恢复环境遇到错误", e);
					Assert.fail("后置处理,恢复环境遇到错误", e);
				}
			}
		}
	}

	@Test
	public void stockInAbnormalTestCase06() {
		ReporterCSS.title("测试点: 新建采购入库单,提交时间输入为空");
		try {
			openStockInCommonParam.setSubmit_date("");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,提交时间输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase07() {
		ReporterCSS.title("测试点: 新建采购入库单,提交时间输入非法格式");
		try {
			openStockInCommonParam.setSubmit_date("2019.11.01");
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,提交时间输入非法格式,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase08() {
		ReporterCSS.title("测试点: 新建采购入库单,采购详细输入为空");
		try {
			openStockInCommonParam.setDetails(new ArrayList<>());
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,采购详细输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase09() {
		ReporterCSS.title("测试点: 新建采购入库单,采购ID输入空值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setSpec_id("");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,采购详细输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase10() {
		ReporterCSS.title("测试点: 新建采购入库单,采购ID输入非法值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			String spec_id = null;
			for (OpenStockInCommonParam.Detail detail : details) {
				spec_id = detail.getSpec_id();
				BigDecimal new_id = new BigDecimal(spec_id.substring(1)).add(new BigDecimal("100000"));
				detail.setSpec_id("D" + new_id);
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,采购ID输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase11() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入为空");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase12() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入非数字");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("A");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase13() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入负数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("-10");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase14() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入0");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("0");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase15() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入不符合规定的小数(多位小数位)");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count(NumberUtil.getRandomNumber(2, 10, 3).toString());
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入不符合规定的小数(多位小数位),断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase16() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入过大数值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("1000001");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入过大数值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase17() {
		ReporterCSS.title("测试点: 新建采购入库单,入库数量输入过大数值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_count("1000001");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库数量输入过大数值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase18() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入为空");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price("");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase19() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入非数值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price("a");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase20() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入负数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price("-10");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase21() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入过长的小数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price(NumberUtil.getRandomNumber(5, 15, 3).toString());
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase22() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入过大的数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price("1000001");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase23() {
		ReporterCSS.title("测试点: 新建采购入库单,入库单价输入过大的数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_unit_price("1000001");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase24() {
		ReporterCSS.title("测试点: 新建采购入库单,入库条目金额输入为非数值");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_amount("A");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库单价输入过长的小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase25() {
		ReporterCSS.title("测试点: 新建采购入库单,入库条目金额输入为负数");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			for (OpenStockInCommonParam.Detail detail : details) {
				detail.setIn_stock_amount("-100");
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,入库条目金额输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase26() {
		ReporterCSS.title("测试点: 新建采购入库单,输入入库数*入库单价!=入库金额");
		try {
			List<OpenStockInCommonParam.Detail> details = openStockInCommonParam.getDetails();
			BigDecimal in_stock_count = null;
			BigDecimal in_stock_unit_price = null;
			BigDecimal in_stock_amount = null;
			for (OpenStockInCommonParam.Detail detail : details) {
				in_stock_count = new BigDecimal(detail.getIn_stock_count());
				in_stock_unit_price = new BigDecimal(detail.getIn_stock_unit_price());
				in_stock_amount = in_stock_count.multiply(in_stock_unit_price).add(new BigDecimal("5")).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				detail.setIn_stock_amount(in_stock_amount.toString());
			}
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,输入入库数*入库单价!=入库金额,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase27() {
		ReporterCSS.title("测试点: 新建采购入库单,折让原因输入为空");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("5");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让原因输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase28() {
		ReporterCSS.title("测试点: 新建采购入库单,折让原因输入非后选值");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("0");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("5");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让原因输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase29() {
		ReporterCSS.title("测试点: 新建采购入库单,折让类型输入为空");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("");
			discount.setDiscount_amount("5");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让原因输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase30() {
		ReporterCSS.title("测试点: 新建采购入库单,折让类型输入非后选值");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("0");
			discount.setDiscount_amount("5");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让类型输入非后选值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase31() {
		ReporterCSS.title("测试点: 新建采购入库单,折让金额输入为空");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让金额输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase32() {
		ReporterCSS.title("测试点: 新建采购入库单,折让金额输入负数");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("-5");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让金额输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase33() {
		ReporterCSS.title("测试点: 新建采购入库单,折让金额输入0");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("0");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让金额输入0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase34() {
		ReporterCSS.title("测试点: 新建采购入库单,折让金额输入多位小数");
		try {
			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInCommonParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("1.234");
			discounts.add(discount);

			openStockInCommonParam.setDiscounts(discounts);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertEquals(stock_in_sheet_id, null, "新建采购入库单,折让金额输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购入库单遇到错误", e);
			Assert.fail("新建采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase35() {
		ReporterCSS.title("测试点: 修改采购入库单,入库单ID输入为空");
		try {
			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id("");
			openStockInUpdateParam.setSubmit_date(todayStr);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,入库单ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase36() {
		ReporterCSS.title("测试点: 修改采购入库单,提交入库时间输入为空格,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			openStockInUpdateParam.setSubmit_date(" ");

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,提交入库时间输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase37() {
		ReporterCSS.title("测试点: 修改采购入库单,提交入库时间输入为非法格式,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			openStockInUpdateParam.setSubmit_date("2019.11.11");

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,提交入库时间输入为非法格式,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase38() {
		ReporterCSS.title("测试点: 修改采购入库单,折让原因输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让原因输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase39() {
		ReporterCSS.title("测试点: 修改采购入库单,折让原因输入为非数字,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("A");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, " 修改采购入库单,折让原因输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase40() {
		ReporterCSS.title("测试点: 修改采购入库单,折让原因输入为非候选值,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("0");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让原因输入为非候选值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase41() {
		ReporterCSS.title("测试点: 修改采购入库单,折让类型输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让类型输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase42() {
		ReporterCSS.title("测试点: 修改采购入库单,折让类型输入为非数字,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("A");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让类型输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase43() {
		ReporterCSS.title("测试点: 修改采购入库单,折让类型输入为非候选值,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("3");
			discount.setDiscount_amount("2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让类型输入为非候选值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase44() {
		ReporterCSS.title("测试点: 修改采购入库单,折让金额输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让金额输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase45() {
		ReporterCSS.title("测试点: 修改采购入库单,折让金额输入为非数字,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("1a");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让金额输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase46() {
		ReporterCSS.title("测试点: 修改采购入库单,折让金额输入为负数,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("-2");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让金额输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase47() {
		ReporterCSS.title("测试点: 修改采购入库单,折让金额输入为0,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);

			List<OpenStockInCommonParam.Discount> discounts = new ArrayList<OpenStockInCommonParam.Discount>();
			OpenStockInCommonParam.Discount discount = openStockInUpdateParam.new Discount();
			discount.setDiscount_reason("1");
			discount.setDiscount_action("1");
			discount.setDiscount_amount("0");
			discounts.add(discount);

			openStockInUpdateParam.setDiscounts(discounts);

			boolean result = openStockInService.updateStockInSheet(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单,折让金额输入为0,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase48() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单ID输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id("");
			openStockInUpdateParam.setDetails(openStockInCommonParam.getDetails());

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase49() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单ID输入为错误值,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id + "1");
			openStockInUpdateParam.setDetails(openStockInCommonParam.getDetails());

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单ID输入为错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase50() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库商品输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			openStockInUpdateParam.setDetails(new ArrayList<OpenStockInCommonParam.Detail>());

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库商品输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase51() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库商品规格输入为空,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setSpec_id("");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库商品输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase53() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库商品规格输入为错误值,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setSpec_id("D121211");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库商品输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase54() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为空,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase55() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为非数字,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("1b");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase56() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为负数,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("-10");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase57() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为0,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("0");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为0,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase58() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为0,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("0");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为0,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase59() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库数量输入为多位小数,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_count("1.234");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库数量输入为多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase60() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单价输入为空,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_unit_price("");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单价输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase61() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单价输入为非数字,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_unit_price("1c");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单价输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase62() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单价输入为负数,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_unit_price("-10");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单价输入为负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase63() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库单价输入多位小数,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_unit_price("1.234");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库单价输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase64() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库金额输入非数字,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_amount("1d");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库金额输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase65() {
		ReporterCSS.title("测试点: 新建采购入库单条目,入库金额输入负数,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);
			openStockInCommonParam.getDetails().remove(detail);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setIn_stock_amount("-20");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.addStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "新建采购入库单条目,入库金额输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase66() {
		ReporterCSS.title("测试点: 修改采购入库单条目,采购规格输入为空,断言失败");
		try {
			OpenStockInCommonParam.Detail detail = openStockInCommonParam.getDetails().get(0);

			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			detail.setSpec_id("");
			openStockInUpdateParam.setDetails(Arrays.asList(detail));

			boolean result = openStockInService.updateStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "修改采购入库单条目,采购规格输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase67() {
		ReporterCSS.title("测试点: 删除采购入库单条目,采购规格输入为空,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = openStockInUpdateParam.new Detail();
			detail.setSpec_id("");
			details.add(detail);

			boolean result = openStockInService.deleteStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "删除采购入库单条目,采购规格输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase68() {
		ReporterCSS.title("测试点: 删除采购入库单条目,采购规格输入为不存在的值,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			OpenStockInCommonParam openStockInUpdateParam = new OpenStockInCommonParam();
			openStockInUpdateParam.setIn_stock_sheet_id(stock_in_sheet_id);
			List<OpenStockInCommonParam.Detail> details = new ArrayList<OpenStockInCommonParam.Detail>();
			OpenStockInCommonParam.Detail detail = openStockInUpdateParam.new Detail();
			detail.setSpec_id("D123456");
			details.add(detail);

			boolean result = openStockInService.deleteStockInDetail(openStockInUpdateParam);
			Assert.assertEquals(result, false, "删除采购入库单条目,采购规格输入为不存在的值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase69() {
		ReporterCSS.title("测试点: 提交采购入库单,入库单ID输入为空,断言失败");
		try {
			boolean result = openStockInService.submitStockInSheet("");
			Assert.assertEquals(result, false, "提交采购入库单,入库单ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase70() {
		ReporterCSS.title("测试点: 提交采购入库单,提交状态为冲销状态的入库单,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			boolean result = openStockInService.revertStockInsheet(stock_in_sheet_id);
			Assert.assertEquals(result, true, "冲销采购入库单失败");

			result = openStockInService.submitStockInSheet(stock_in_sheet_id);
			Assert.assertEquals(result, false, "提交采购入库单,提交状态为冲销状态的入库单,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase71() {
		ReporterCSS.title("测试点: 提交采购入库单,提交已经提交过的采购入库单,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			boolean result = openStockInService.submitStockInSheet(stock_in_sheet_id);
			Assert.assertEquals(result, true, "正常提交采购入库单,断言成功");

			result = openStockInService.submitStockInSheet(stock_in_sheet_id);
			Assert.assertEquals(result, false, "提交采购入库单,提交已经提交过的采购入库单,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase72() {
		ReporterCSS.title("测试点: 冲销采购入库单,入库单输入为空,断言失败");
		try {
			boolean result = openStockInService.revertStockInsheet("");
			Assert.assertEquals(result, false, "冲销采购入库单,入库单输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase73() {
		ReporterCSS.title("测试点: 冲销采购入库单,冲销已经冲销的入库单,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			boolean result = openStockInService.revertStockInsheet(stock_in_sheet_id);
			Assert.assertEquals(result, true, "正常冲销入库单,断言成功");

			result = openStockInService.revertStockInsheet(stock_in_sheet_id);
			Assert.assertEquals(result, false, "冲销采购入库单,冲销已经冲销的入库单,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase74() {
		ReporterCSS.title("测试点: 采购入库单审核不通过,传入ID为空,断言失败");
		try {
			boolean result = openStockInService.rejectStockInSheet("");
			Assert.assertEquals(result, false, "采购入库单审核不通过,传入ID为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase75() {
		ReporterCSS.title("测试点: 采购入库单审核不通过,传入已经冲销的入库单,断言失败");
		try {
			String stock_in_sheet_id = openStockInService.createStockInSheet(openStockInCommonParam);
			Assert.assertNotEquals(stock_in_sheet_id, null, "新建采购入库单失败");

			boolean result = openStockInService.revertStockInsheet(stock_in_sheet_id);
			Assert.assertEquals(result, true, "正常冲销入库单,断言成功");

			result = openStockInService.rejectStockInSheet(stock_in_sheet_id);
			Assert.assertEquals(result, false, "采购入库单审核不通过,传入已经冲销的入库单,断言失败");
		} catch (Exception e) {
			logger.error("修改采购入库单遇到错误", e);
			Assert.fail("修改采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase76() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索类型输入为空,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索类型输入为空,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase77() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索类型输入为非数字,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("A");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索类型输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase78() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索类型输入为非候选值,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("0");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索类型输入为非候选值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase79() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索日期输入为空,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date("");
			openStockInSheetFilterParam.setEnd_date("");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索日期输入为空,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase80() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索日期输入非指定格式,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date("2019.11.11");
			openStockInSheetFilterParam.setEnd_date("2019.11.13");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索日期输入非指定格式,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase81() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,搜索启示日期晚于结束日期,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date("2019-11-11");
			openStockInSheetFilterParam.setEnd_date("2019-11-01");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,搜索启示日期晚于结束日期,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase82() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,状态值输入为非数值,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setStatus("A");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,状态值输入为非数值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase83() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,状态值输入为非候选值,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setStatus("6");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,状态值输入为非候选值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase84() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页offset输入为空格,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset(" ");
			openStockInSheetFilterParam.setLimit("20");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页offset输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase85() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页offset输入为字符,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset("A");
			openStockInSheetFilterParam.setLimit("20");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页offset输入为字符,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase86() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页offset输入负数,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset("-10");
			openStockInSheetFilterParam.setLimit("20");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页offset输入为字符,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase87() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页limit输入空格,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset("0");
			openStockInSheetFilterParam.setLimit(" ");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页limit输入空格,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase88() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页limit输入负数,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset("0");
			openStockInSheetFilterParam.setLimit("-20");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页limit输入负数,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

	@Test
	public void stockInAbnormalTestCase89() {
		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页limit输入0,断言失败 ");
		try {
			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
			openStockInSheetFilterParam.setSearch_type("1");
			openStockInSheetFilterParam.setStart_date(todayStr);
			openStockInSheetFilterParam.setEnd_date(todayStr);
			openStockInSheetFilterParam.setOffset("0");
			openStockInSheetFilterParam.setLimit("0");

			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
					.queryStockInSheet(openStockInSheetFilterParam);
			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页limit输入0,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误", e);
			Assert.fail("搜索过滤采购入库单遇到错误", e);
		}
	}

//	@Test
//	public void stockInAbnormalTestCase90() {
//		ReporterCSS.title("测试点: 搜索过滤采购入库单,分页limit输入1,断言失败 ");
//		try {
//			OpenStockInSheetFilterParam openStockInSheetFilterParam = new OpenStockInSheetFilterParam();
//			openStockInSheetFilterParam.setSearch_type("1");
//			openStockInSheetFilterParam.setStart_date(todayStr);
//			openStockInSheetFilterParam.setEnd_date(todayStr);
//			openStockInSheetFilterParam.setOffset("0");
//			openStockInSheetFilterParam.setLimit("1");
//
//			List<OpenStockInSheetBean> openStockInSheetList = openStockInService
//					.queryStockInSheet(openStockInSheetFilterParam);
//			Assert.assertEquals(openStockInSheetList, null, "搜索过滤采购入库单,分页limit输入1,断言失败");
//		} catch (Exception e) {
//			logger.error("搜索过滤采购入库单遇到错误", e);
//			Assert.fail("搜索过滤采购入库单遇到错误", e);
//		}
//	}

}
