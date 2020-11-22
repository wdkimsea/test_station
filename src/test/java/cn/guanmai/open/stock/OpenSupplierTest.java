package cn.guanmai.open.stock;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierCommonParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenSupplierTest extends AccessToken {

	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenSupplierServiceImpl openSupplierService;
	private OpenCategoryServiceImpl categoryService;
	private SupplierServiceImpl supplierService;

	@BeforeTest
	public void initData() {
		String access_token = getAccess_token();
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		categoryService = new OpenCategoryServiceImpl(access_token);
		supplierService = new SupplierServiceImpl(getSt_headers());
	}

	@Test
	public void supplierTestCase01() {
		Reporter.log("测试点: 获取供应商列表");
		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		try {
			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertNotNull(supplierList, "拉取供应商列表失败");
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierTestCase02() {
		Reporter.log("测试点: 获取供应商列表,搜索条件,二级分类id");

		OpenSupplierFilterParam supplierFilter = new OpenSupplierFilterParam();
		List<OpenSupplierBean> supplierList = new ArrayList<>();
		try {
			supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertNotNull(supplierList, "搜索供应商失败");
			Assert.assertNotEquals(supplierList.size(), 0, "未查询到供应商,无法执行此用例");

			OpenSupplierBean openSupplier = NumberUtil.roundNumberInList(supplierList);

			String operSupplier_id = openSupplier.getSupplier_id();
			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(operSupplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详情信息失败");

			List<OpenSupplierDetailBean.Category2> category2List = openSupplierDetail.getCategory2();

			String category2_id = NumberUtil.roundNumberInList(category2List).getCategory2_id();

			supplierFilter.setCategory2_id(category2_id);
			supplierList = openSupplierService.querySupplier(supplierFilter);
			Assert.assertNotNull(supplierList, "搜索供应商失败");

			OpenSupplierBean tempOpenSupplier = supplierList.stream()
					.filter(s -> s.getSupplier_id().equals(operSupplier_id)).findAny().orElse(null);
			Assert.assertNotEquals(tempOpenSupplier, null, "按二级分类ID过滤供应商,没有找到目标供应商");

			for (OpenSupplierBean supplier : supplierList) {
				String supplier_id = supplier.getSupplier_id();
				openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
				Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详情信息失败");
				OpenSupplierDetailBean.Category2 tempCategory2 = openSupplierDetail.getCategory2().stream()
						.filter(c -> c.getCategory2_id().equals(category2_id)).findAny().orElse(null);
				Assert.assertNotEquals(tempCategory2, null, "按二级分类ID过滤供应商,过滤出了不符合条件的供应商");
			}
		} catch (Exception e) {
			logger.error("获取供应商列表遇到错误: ", e);
			Assert.fail("获取供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void supplierTestCase03() {
		Reporter.log("测试点: 新建供应商");
		String supplierNo = "OPENAPI" + StringUtil.getRandomNumber(4);
		String supplierName = "OPENAPI-" + StringUtil.getRandomString(6).toUpperCase();
		String pay_method = "1";
		String supplier_phone = StringUtil.getRandomNumber(11);
		String company_name = "观麦科技";
		String company_address = "广东身深圳市南山区科苑路15科兴科学园B3栋-14层";
		String location_lon = "113.944705";
		String location_lat = "22.548926";

		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		String supplier_id = null;

		try {
			List<OpenCategory2Bean> category2List = categoryService.getCategory2List(null, 0, 10);
			Assert.assertNotEquals(category2List, null, "获取二级分类失败");

			List<String> category2_ids = category2List.stream().map(c -> c.getId()).collect(Collectors.toList());

			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2_ids);
			openSupplierCreateParam.setPay_method(pay_method);
			openSupplierCreateParam.setSupplier_phone(supplier_phone);
			openSupplierCreateParam.setCompany_name(company_name);
			openSupplierCreateParam.setCompany_address(company_address);
			openSupplierCreateParam.setLocation_lat(location_lat);
			openSupplierCreateParam.setLocation_lon(location_lon);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertNotNull(supplier_id, "新建供应商失败");

			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详情信息失败");

			String msg = null;
			boolean result = true;
			if (!openSupplierDetail.getSupplier_name().equals(supplierName)) {
				msg = String.format("新建的供应商%s填写的名称和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplierName,
						openSupplierDetail.getSupplier_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getSupplier_no().equals(supplierNo)) {
				msg = String.format("新建的供应商%s填写的编号和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplierNo,
						openSupplierDetail.getSupplier_no());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getPay_method().equals(pay_method)) {
				msg = String.format("新建的供应商%s填写的结算方式和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, pay_method,
						openSupplierDetail.getPay_method());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getSupplier_phone().equals(supplier_phone)) {
				msg = String.format("新建的供应商%s填写的联系电话和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplier_phone,
						openSupplierDetail.getSupplier_phone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getCompany_name().equals(company_name)) {
				msg = String.format("新建的供应商%s填写的公司名称和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, company_name,
						openSupplierDetail.getCompany_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getCompany_address().equals(company_address)) {
				msg = String.format("新建的供应商%s填写的公司地址和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, company_address,
						openSupplierDetail.getCompany_address());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getLocation_lat().equals(location_lat)) {
				msg = String.format("新建的供应商%s填写的公司地址经度和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, location_lat,
						openSupplierDetail.getLocation_lat());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getLocation_lon().equals(location_lon)) {
				msg = String.format("新建的供应商%s填写的公司地址纬度和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, location_lon,
						openSupplierDetail.getLocation_lon());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> temp_category2_ids = openSupplierDetail.getCategory2().stream().map(c -> c.getCategory2_id())
					.collect(Collectors.toList());
			if (temp_category2_ids.size() != category2_ids.size() || !temp_category2_ids.containsAll(category2_ids)) {
				msg = String.format("新建的供应商%s绑定的二级分类和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, category2_ids,
						temp_category2_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建供应商填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误", e);
			Assert.fail("新建供应商遇到错误: ", e);
		} finally {
			if (supplier_id != null) {
				try {
					boolean result = supplierService.deleteSupplier(supplier_id);
					Assert.assertEquals(result, true, "后置处理,删除供应商失败");
				} catch (Exception e) {
					logger.error("后置处理,删除供应商遇到错误", e);
					Assert.fail("后置处理,删除供应商遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void supplierTestCase04() {
		Reporter.log("测试点: 修改供应商");
		String supplierNo = "OPENAPI" + StringUtil.getRandomNumber(4);
		String supplierName = "OPENAPI-" + StringUtil.getRandomString(6).toUpperCase();
		String pay_method = "1";
		String supplier_phone = StringUtil.getRandomNumber(11);
		OpenSupplierCommonParam openSupplierCreateParam = new OpenSupplierCommonParam();

		String supplier_id = null;

		try {
			List<OpenCategory2Bean> category2List = categoryService.getCategory2List(null, 0, 10);
			Assert.assertNotEquals(category2List, null, "获取二级分类失败");

			List<String> category2_ids = category2List.stream().map(c -> c.getId()).collect(Collectors.toList());

			openSupplierCreateParam.setSupplier_name(supplierName);
			openSupplierCreateParam.setSupplier_no(supplierNo);
			openSupplierCreateParam.setCategory2_ids(category2_ids);
			openSupplierCreateParam.setPay_method(pay_method);
			openSupplierCreateParam.setSupplier_phone(supplier_phone);

			supplier_id = openSupplierService.createSupplier(openSupplierCreateParam);
			Assert.assertNotNull(supplier_id, "新建供应商失败");

			supplierNo = "OPENAPI" + StringUtil.getRandomNumber(4);
			supplierName = "OPENAPI-" + StringUtil.getRandomString(6).toUpperCase();
			pay_method = "1";
			supplier_phone = StringUtil.getRandomNumber(11);
			if (category2_ids.size() >= 2) {
				category2_ids.remove(0);
			}

			String company_name = "观麦科技";
			String company_address = "广东身深圳市南山区科苑路15科兴科学园B3栋-14层";
			String location_lon = "113.944705";
			String location_lat = "22.548926";
			OpenSupplierCommonParam openSupplierUpdateParam = new OpenSupplierCommonParam();
			openSupplierUpdateParam.setSupplier_id(supplier_id);
			openSupplierUpdateParam.setPay_method(pay_method);
			openSupplierUpdateParam.setCategory2_ids(category2_ids);
			openSupplierUpdateParam.setSupplier_name(supplierName);
			openSupplierUpdateParam.setSupplier_no(supplierNo);
			openSupplierUpdateParam.setSupplier_phone(supplier_phone);
			openSupplierUpdateParam.setCompany_name(company_name);
			openSupplierUpdateParam.setCompany_address(company_address);
			openSupplierUpdateParam.setLocation_lat(location_lat);
			openSupplierUpdateParam.setLocation_lon(location_lon);

			boolean result = openSupplierService.updateSupplier(openSupplierUpdateParam);
			Assert.assertEquals(result, true, "修改供应商信息失败");

			OpenSupplierDetailBean openSupplierDetail = openSupplierService.getSupplierDetail(supplier_id);
			Assert.assertNotEquals(openSupplierDetail, null, "获取供应商详情信息失败");

			String msg = null;
			if (!openSupplierDetail.getSupplier_name().equals(supplierName)) {
				msg = String.format("修改的供应商%s填写的名称和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplierName,
						openSupplierDetail.getSupplier_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getSupplier_no().equals(supplierNo)) {
				msg = String.format("修改的供应商%s填写的编号和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplierNo,
						openSupplierDetail.getSupplier_no());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getPay_method().equals(pay_method)) {
				msg = String.format("修改的供应商%s填写的结算方式和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, pay_method,
						openSupplierDetail.getPay_method());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getSupplier_phone().equals(supplier_phone)) {
				msg = String.format("修改的供应商%s填写的联系电话和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, supplier_phone,
						openSupplierDetail.getSupplier_phone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getCompany_name().equals(company_name)) {
				msg = String.format("修改的供应商%s填写的公司名称和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, company_name,
						openSupplierDetail.getCompany_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getCompany_address().equals(company_address)) {
				msg = String.format("修改的供应商%s填写的公司地址和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, company_address,
						openSupplierDetail.getCompany_address());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getLocation_lat().equals(location_lat)) {
				msg = String.format("修改的供应商%s填写的公司地址经度和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, location_lat,
						openSupplierDetail.getLocation_lat());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!openSupplierDetail.getLocation_lon().equals(location_lon)) {
				msg = String.format("修改的供应商%s填写的公司地址纬度和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, location_lon,
						openSupplierDetail.getLocation_lon());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> temp_category2_ids = openSupplierDetail.getCategory2().stream().map(c -> c.getCategory2_id())
					.collect(Collectors.toList());
			if (temp_category2_ids.size() != category2_ids.size() || !temp_category2_ids.containsAll(category2_ids)) {
				msg = String.format("修改的供应商%s绑定的二级分类和详细信息页面显示的不一致,预期:%s,实际:%s", supplier_id, category2_ids,
						temp_category2_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "修改供应商填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误", e);
			Assert.fail("修改供应商遇到错误: ", e);
		} finally {
			if (supplier_id != null) {
				try {
					boolean result = supplierService.deleteSupplier(supplier_id);
					Assert.assertEquals(result, true, "后置处理,删除供应商失败");
				} catch (Exception e) {
					logger.error("后置处理,删除供应商遇到错误", e);
					Assert.fail("后置处理,删除供应商遇到错误: ", e);
				}
			}
		}
	}

}
