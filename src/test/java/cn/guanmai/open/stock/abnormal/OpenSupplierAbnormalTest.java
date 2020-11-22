package cn.guanmai.open.stock.abnormal;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierCommonParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class OpenSupplierAbnormalTest extends AccessToken {

	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenSupplierServiceImpl openSupplierService;
	private OpenCategoryService openCategoryService;

	private CategoryService categoryService;

	@BeforeTest
	public void initData() {
		String access_token = getAccess_token();
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
	}

	public List<String> getCategory2List() {
		List<String> category2_ids = null;
		try {
			List<OpenCategory2Bean> category2List = openCategoryService.getCategory2List(null, 0, 10);
			Assert.assertNotEquals(category2List, null, "获取商品二级分类列表失败");
			category2_ids = category2List.stream().map(c -> c.getId()).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("获取二级分类列表遇到错误: ", e);
			Assert.fail("获取二级分类列表遇到错误: ", e);
		}
		return category2_ids;
	}

	@Test
	public void supplierAbnormalTestCase01() {
		ReporterCSS.title("测试点: 获取供应商列表传入错误二级分类id");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			supplierFilter.setCategory2_id("A01");
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertNotEquals(supplierList, null, "搜索过滤供应商失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase02() {
		ReporterCSS.title("测试点: 获取供应商列表传入负数值offset");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			supplierFilter.setOffset("-1");
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertEquals(supplierList, null, "搜索过滤供应商失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase03() {
		ReporterCSS.title("测试点: 获取供应商列表limit值传入非数值");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			supplierFilter.setLimit("A");
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertEquals(supplierList, null, "搜索过滤供应商失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase04() {
		ReporterCSS.title("测试点: 获取供应商列表offset值传入非数值");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			supplierFilter.setOffset("A");
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertEquals(supplierList, null, "搜索过滤供应商失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase05() {
		ReporterCSS.title("测试点: 搜索供应商,limit值传入101");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			supplierFilter.setLimit("101");
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertEquals(supplierList, null, "搜索过滤供应商失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase06() {
		ReporterCSS.title("测试点: 获取供应商详情传入错误供应商id");
		try {
			OpenSupplierDetailBean supplier = openSupplierService.getSupplierDetail("T2422");
			Assert.assertEquals(supplier, null, "获取供应商详情传入错误供应商id,应该获取为空");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase07() {
		ReporterCSS.title("测试点: 获取供应商详情传入空的供应商id");
		try {
			OpenSupplierDetailBean supplier = openSupplierService.getSupplierDetail(" ");
			Assert.assertEquals(supplier, null, "获取供应商详情传入空的供应商id,应该获取为空");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase08() {
		ReporterCSS.title("测试点: 新建供应商传入空的二级分类id列表");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		List<String> category2List = new ArrayList<>();
		String supplier_id = null;

		try {
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的二级分类id,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase09() {
		ReporterCSS.title("测试点: 新建供应商传入错误的二级分类id列表");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		List<String> category2List = new ArrayList<>();
		String supplier_id = null;
		try {
			category2List.add("A13");
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的二级分类id,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase10() {
		ReporterCSS.title("测试点: 新建供应商传入已经删除的二级分类ID列表");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		List<String> category2List = new ArrayList<>();
		String supplier_id = null;
		String category1_id = null;
		String category2_id = null;
		try {

			Category1Bean category1 = new Category1Bean();
			category1.setName(StringUtil.getRandomString(6));
			category1.setIcon(1);
			category1_id = categoryService.createCategory1(category1);
			Assert.assertNotEquals(category1_id, null, "新建一级分类ID失败");

			category2_id = categoryService
					.createCategory2(new Category2Bean(category1_id, StringUtil.getRandomString(6)));
			Assert.assertNotEquals(category2_id, null, "新建二级分类ID失败");

			boolean result = categoryService.deleteCategory2ById(category2_id);
			Assert.assertEquals(result, true, "删除二级分类失败");

			category2List.add(category2_id);

			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入已经删除的二级分类ID列表,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		} finally {
			try {
				if (category2_id != null) {
					categoryService.deleteCategory2ById(category2_id);
					categoryService.deleteCategory1ById(category1_id);
				}
			} catch (Exception e) {
				logger.error("删除商品分类遇到错误: ", e);
				Assert.fail("删除商品分类遇到错误: ", e);
			}
		}
	}

	@Test
	public void supplierAbnormalTestCase11() {
		ReporterCSS.title("测试点: 新建供应商传入别的站点的二级分类id列表");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		List<String> category2List = new ArrayList<>();
		String supplier_id = null;
		try {
			category2List.add("B414");
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的二级分类id,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase12() {
		ReporterCSS.title("测试点: 新建供应商传入空的供应商编号");
		String supplierNo = "";
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase13() {
		ReporterCSS.title("测试点: 新建供应商供应商编号传入空格");
		String supplierNo = " ";
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商供应商编号传入空格,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase14() {
		ReporterCSS.title("测试点: 新建供应商传入非法字符的供应商编号");
		String supplierNo = StringUtil.getRandomString(6) + "-1";
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入非法字符的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase15() {
		ReporterCSS.title("测试点: 新建供应商传入非法字符的供应商编号");
		String supplierNo = StringUtil.getRandomString(6) + "一";
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入非法字符的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase16() {
		ReporterCSS.title("测试点: 新建供应商传入过长的供应商编号");
		String supplierNo = StringUtil.getRandomString(33);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入过长的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase17() {
		ReporterCSS.title("测试点: 新建供应商传入本站已经使用的供应商编号");
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();

			List<OpenSupplierBean> openSupplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(openSupplierList, null, "拉取供应商列表失败");

			String supplierNo = openSupplierList.get(0).getSupplier_no();

			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入本站已经使用的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase18() {
		ReporterCSS.title("测试点: 新建供应商传入空的供应商名称");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = "";
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的供应商名称,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase19() {
		ReporterCSS.title("测试点: 新建供应商供应商名称传入空格");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = " ";
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的供应商名称,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase20() {
		ReporterCSS.title("测试点: 新建供应商供应商名称传入过长字符");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(33);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商传入空的供应商名称,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase21() {
		ReporterCSS.title("测试点: 新建供应商结款方式传入非数值");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("A");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商结款方式传入非数值,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase22() {
		ReporterCSS.title("测试点: 新建供应商结款方式传入区间外的值");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("5");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商结款方式传入非数值,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase23() {
		ReporterCSS.title("测试点: 新建供应商公司名称传入过长字符");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setCompany_name(StringUtil.getRandomString(257));

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司名称传入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase24() {
		ReporterCSS.title("测试点: 新建供应商公司地址传入过长字符");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setCompany_address(StringUtil.getRandomString(257));

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司名称传入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase25() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置纬度传入非坐标值(字符)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lat("a");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置纬度传入非坐标值(字符),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase26() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置纬度传入非坐标值(超出范畴)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lat("-91");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置纬度传入非坐标值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase27() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置纬度传入非坐标值(超出范畴)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lat("91");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置纬度传入非坐标值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase28() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置经度传入非坐标值(字符)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lon("a");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置经度传入非坐标值(字符),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase29() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置经度传入非坐标值(超出范畴)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lon("-181");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置经度传入非坐标值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase30() {
		ReporterCSS.title("测试点: 新建供应商公司地理位置经度传入非坐标值(超出范畴)");
		String supplierNo = StringUtil.getRandomString(6);
		String supplierName = StringUtil.getRandomString(6);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();
		String supplier_id = null;
		try {
			List<String> category2List = getCategory2List();
			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2List);
			openSupplierCreateParam.setPay_method("1");
			openSupplierCreateParam.setLocation_lon("181");

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertEquals(supplier_id, null, "新建供应商公司地理位置经度传入非坐标值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase31() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为空");
		try {
			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_no(StringUtil.getRandomString(6));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase32() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为别的站点的供应商ID");
		try {
			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id("T2422");
			openSupplierUpdateParam.setSupplier_no(StringUtil.getRandomString(6));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为别的站点的供应商ID,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase33() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为空");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no("");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase34() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为空格");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no(" ");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase35() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为非法字符");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no(StringUtil.getRandomString(6) + "-A");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为非法字符,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase36() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为非法字符");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no(StringUtil.getRandomString(6) + "一A");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为非法字符,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase37() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入为过长字符串");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no(StringUtil.getRandomString(33));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入为过长字符串,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase38() {
		ReporterCSS.title("测试点: 修改供应商,供应商编号输入已经占用的供应商编号");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = supplierList.get(0);
			String supplier_id = openSupplier.getSupplier_id();

			String supplier_no = supplierList.get(1).getSupplier_no();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_no(supplier_no);

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商编号输入已经占用的供应商编号,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase39() {
		ReporterCSS.title("测试点: 修改供应商,供应商名称输入为空");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_name("");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase40() {
		ReporterCSS.title("测试点: 修改供应商,供应商名称输入为空格");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_name(" ");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商名称输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase41() {
		ReporterCSS.title("测试点: 修改供应商,供应商名称输入过长字符串");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setSupplier_name(StringUtil.getRandomString(33));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商名称输入过长字符串,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase42() {
		ReporterCSS.title("测试点: 修改供应商,供应商结款方式输入为空");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setPay_method("");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商结款方式输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase43() {
		ReporterCSS.title("测试点: 修改供应商,供应商结款方式输入非候选值");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setPay_method("5");

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商结款方式输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase44() {
		ReporterCSS.title("测试点: 修改供应商,供应二级分类列表输入为空");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCategory2_ids(new ArrayList<String>());

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应二级分类列表输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase45() {
		ReporterCSS.title("测试点: 修改供应商,供应二级分类列表输入非法值");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCategory2_ids(Arrays.asList("A13"));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应二级分类列表输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase46() {
		ReporterCSS.title("测试点: 修改供应商,供应二级分类列表输入非本站点的二级分类");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详情失败");

			List<String> category2_ids = openSupplierDetail.getCategory2().stream().map(c -> c.getCategory2_id())
					.collect(Collectors.toList());
			category2_ids.add("B414");

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCategory2_ids(category2_ids);

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应二级分类列表输入非本站点的二级分类,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase47() {
		ReporterCSS.title("测试点: 修改供应商,供应商名称输入过长字符");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCompany_name(StringUtil.getRandomString(257));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商名称输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase48() {
		ReporterCSS.title("测试点: 修改供应商,供应商地址输入过长字符");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setCompany_address(StringUtil.getRandomString(257));

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地址输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase49() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标纬度输入非法值(字符)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("a");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标纬度输入非法值(字符),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase50() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标纬度输入非法值(超出范畴)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("-91");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标纬度输入非法值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase51() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标纬度输入非法值(超出范畴)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("91");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标纬度输入非法值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase52() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标经度输入非法值(字符)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("a");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标经度输入非法值(字符),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase53() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标经度输入非法值(超出范畴)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("-181");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标经度输入非法值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierAbnormalTestCase54() {
		ReporterCSS.title("测试点: 修改供应商,供应商地理坐标经度输入非法值(超出范畴)");
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);
			String supplier_id = openSupplier.getSupplier_id();

			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setLocation_lat("181");
			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, false, "修改供应商,供应商地理坐标经度输入非法值(超出范畴),断言失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}
}
