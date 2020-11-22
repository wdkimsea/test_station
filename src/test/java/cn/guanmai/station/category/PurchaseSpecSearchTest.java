package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.param.PurchaseSpecBatchEditParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecFilterParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecQuotePriceParam;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.base.DownloadServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.base.DownloadService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年7月3日 上午10:56:46
 * @description: 采购规格搜索过滤以及下载模板
 * @version: 1.0
 */

public class PurchaseSpecSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseSpecSearchTest.class);
	private CategoryService categoryService;
	private AsyncService asyncService;
	private DownloadService downloadService;
	private PurchaseSpecFilterParam purchaseSpecFilterParam;

	private InitDataBean initData;

	@BeforeClass
	public void initData() {
		Map<String, String> st_headers = getStationCookie();
		categoryService = new CategoryServiceImpl(st_headers);
		asyncService = new AsyncServiceImpl(st_headers);
		downloadService = new DownloadServiceImpl(st_headers);

		initData = getInitData();

	}

	@BeforeMethod
	public void beforeMethod() {
		purchaseSpecFilterParam = new PurchaseSpecFilterParam();
		purchaseSpecFilterParam.setOffset(0);
		purchaseSpecFilterParam.setLimit(10);
	}

	@Test
	public void purchaseSpecSearchTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤采购规格");
		try {
			List<PurchaseSpecBean> purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "搜索过滤采购规格失败");

			PurchaseSpecBean purchaseSpec = NumberUtil.roundNumberInList(purchaseSpecList);
			String category1_id = purchaseSpec.getCategory_1();
			String category2_id = purchaseSpec.getCategory_2();
			String pinlei_id = purchaseSpec.getPinlei();

			purchaseSpecFilterParam.setCategory_id_1(Arrays.asList(category1_id));
			purchaseSpecFilterParam.setCategory_id_2(Arrays.asList(category2_id));
			purchaseSpecFilterParam.setPinlei_id(Arrays.asList(pinlei_id));

			purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "使用分类搜索过滤采购规格失败");

			String msg = null;
			boolean result = true;
			for (PurchaseSpecBean tempPurchaseSpec : purchaseSpecList) {
				if (!tempPurchaseSpec.getCategory_1().equals(category1_id)) {
					msg = String.format("一级分类输入[%s]过滤采购规格,过滤出了[%s]的商品", purchaseSpec.getCategory_1_name(),
							tempPurchaseSpec.getCategory_1_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!tempPurchaseSpec.getCategory_2().equals(category2_id)) {
					msg = String.format("二级分类输入[%s]过滤采购规格,过滤出了[%s]的商品", purchaseSpec.getCategory_2_name(),
							tempPurchaseSpec.getCategory_2_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!tempPurchaseSpec.getPinlei().equals(pinlei_id)) {
					msg = String.format("品类分类输入[%s]过滤采购规格,过滤出了[%s]的商品", purchaseSpec.getPinlei_name(),
							tempPurchaseSpec.getPinlei_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "使用商品分类过滤采购规格,过滤出的数据不符合过滤条件");

		} catch (Exception e) {
			logger.error("搜索过滤采购规格遇到错误: ", e);
			Assert.fail("搜索过滤采购规格遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase02() {
		ReporterCSS.title("测试点: 按关键词搜索过滤采购规格");
		try {
			List<PurchaseSpecBean> purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "搜索过滤采购规格失败");

			PurchaseSpecBean purchaseSpec = NumberUtil.roundNumberInList(purchaseSpecList);
			String name = purchaseSpec.getName();
			purchaseSpecFilterParam.setSearch_text(name);
			purchaseSpecFilterParam.setLimit(50);

			purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "使用关键词搜索过滤采购规格失败");

			String msg = null;
			boolean result = true;
			for (PurchaseSpecBean tempPurchaseSpec : purchaseSpecList) {
				if (!(tempPurchaseSpec.getName().contains(name) || tempPurchaseSpec.getId().contains(name))) {
					msg = String.format("关键词输入采购规格名称[%s]过滤采购规格,过滤出了名称为[%s]的商品", name, tempPurchaseSpec.getName());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "使用关键词过滤采购规格,过滤出的数据不符合过滤条件");

		} catch (Exception e) {
			logger.error("搜索过滤采购规格遇到错误: ", e);
			Assert.fail("搜索过滤采购规格遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase03() {
		ReporterCSS.title("测试点: 按采购规格ID搜索过滤采购规格");
		try {
			List<PurchaseSpecBean> purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "搜索过滤采购规格失败");

			PurchaseSpecBean purchaseSpec = NumberUtil.roundNumberInList(purchaseSpecList);
			String id = purchaseSpec.getId();
			purchaseSpecFilterParam.setSearch_text(id);
			purchaseSpecFilterParam.setLimit(50);

			purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "使用关键词搜索过滤采购规格失败");

			String msg = null;
			boolean result = true;
			for (PurchaseSpecBean tempPurchaseSpec : purchaseSpecList) {
				if (!(tempPurchaseSpec.getName().contains(id) || tempPurchaseSpec.getId().contains(id))) {
					msg = String.format("关键词输入采购规格ID[%s]过滤采购规格,过滤出了ID为[%s]的商品", id, tempPurchaseSpec.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "使用关键词过滤采购规格,过滤出的数据不符合过滤条件");

		} catch (Exception e) {
			logger.error("搜索过滤采购规格遇到错误: ", e);
			Assert.fail("搜索过滤采购规格遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase04() {
		ReporterCSS.title("测试点: 导出采购规格列表");
		try {
			BigDecimal task_id = categoryService.exportPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(task_id, null, "导出采购规格列表,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出采购规格的异步任务执行失败");

		} catch (Exception e) {
			logger.error("导出采购规格列表遇到错误: ", e);
			Assert.fail("导出采购规格列表遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase05() {
		ReporterCSS.title("测试点: 导出采购规格询价模板");
		try {
			BigDecimal task_id = categoryService.exportPurchaseSpecQuotePriceTemplate();
			Assert.assertNotEquals(task_id, null, "导出采购规格询价模板,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出采购规格询价模板的异步任务执行失败");

		} catch (Exception e) {
			logger.error("导出采购规格询价模板遇到错误: ", e);
			Assert.fail("导出采购规格询价模板遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase06() {
		ReporterCSS.title("测试点: 采购规格询价导入");
		try {
			BigDecimal task_id = categoryService.exportPurchaseSpecQuotePriceTemplate();
			Assert.assertNotEquals(task_id, null, "导出采购规格询价模板,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出采购规格询价模板的异步任务执行失败");

			AsyncTaskResultBean asyncTaskResult = asyncService.getAsyncTaskResult(task_id);
			Assert.assertNotEquals(asyncTaskResult, null, "获取导出采购规格询价的异步任务详细失败");

			String link = asyncTaskResult.getResult().getLink();

			String file_path = downloadService.downloadFile(link);
			Assert.assertNotEquals(file_path, null, "采购规格询价模板下载失败");
		} catch (Exception e) {
			logger.error("导出采购规格询价模板遇到错误: ", e);
			Assert.fail("导出采购规格询价模板遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase07() {
		ReporterCSS.title("测试点: 采购规格批量询价");
		try {
			List<PurchaseSpecBean> purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "搜索过滤采购规格失败");

			PurchaseSpecBean purchaseSpec = NumberUtil.roundNumberInList(purchaseSpecList);

			String purcahse_spec_id = purchaseSpec.getId();
			BigDecimal std_unit_price = NumberUtil.getRandomNumber(2, 5, 2);
			SupplierDetailBean supplierDetail = initData.getSupplier();
			String supplier_customer_id = supplierDetail.getCustomer_id();
			String supplier_id = supplierDetail.getId();

			PurchaseSpecQuotePriceParam purchaseSpecQuotePriceParam = new PurchaseSpecQuotePriceParam();
			purchaseSpecQuotePriceParam.setSpec_id(purcahse_spec_id);
			purchaseSpecQuotePriceParam.setStd_unit_price(std_unit_price);
			purchaseSpecQuotePriceParam.setCustomer_id(supplier_customer_id);
			String remark = StringUtil.getRandomString(6);
			purchaseSpecQuotePriceParam.setRemark(remark);
			String origin_place = StringUtil.getRandomString(2).toUpperCase();
			purchaseSpecQuotePriceParam.setOrigin_place(origin_place);

			List<PurchaseSpecQuotePriceParam> purchaseSpecQuotePriceParams = new ArrayList<PurchaseSpecQuotePriceParam>();
			purchaseSpecQuotePriceParams.add(purchaseSpecQuotePriceParam);

			BigDecimal task_id = categoryService.importPurchaseSpecQuotePrice(purchaseSpecQuotePriceParams);
			Assert.assertNotEquals(task_id, null, "采购规格询价导入,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "采购规格询价导入,异步任务执行失败");

			purchaseSpec = categoryService.getPurchaseSpecById(purcahse_spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格" + purcahse_spec_id + "详细数据失败");

			List<PurchaseSpecBean.LastQuotedDetail> lastQuotedDetails = purchaseSpec.getLast_quoted_details();
			PurchaseSpecBean.LastQuotedDetail lastQuotedDetail = lastQuotedDetails.stream()
					.filter(l -> l.getSupplier_id().equals(supplier_id)).findAny().orElse(null);

			Assert.assertNotEquals(lastQuotedDetail, null,
					"采购规格" + purcahse_spec_id + "此次导入的供应商" + supplier_id + "对应的询价记录没有找到");

			String msg = null;
			if (lastQuotedDetail.getPrice().compareTo(std_unit_price) != 0) {
				msg = String.format("采购规格%s此次导入的供应商%s对应的询价价格与预期不一致,预期:%s,实际:%s", purcahse_spec_id, supplier_id,
						std_unit_price, lastQuotedDetail.getPrice());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!lastQuotedDetail.getOrigin_place().equals(origin_place)) {
				msg = String.format("采购规格%s此次导入的供应商%s对应的询价原产地与预期不一致,预期:%s,实际:%s", purcahse_spec_id, supplier_id,
						origin_place, lastQuotedDetail.getOrigin_place());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!lastQuotedDetail.getRemark().equals(remark)) {
				msg = String.format("采购规格%s此次导入的供应商%s对应的询价备注与预期不一致,预期:%s,实际:%s", purcahse_spec_id, supplier_id, remark,
						lastQuotedDetail.getRemark());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "采购规格询价记录导入后与预期结果不一致");
		} catch (Exception e) {
			logger.error("采购规格批量询价遇到错误: ", e);
			Assert.fail("采购规格批量询价遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSpecSearchTestCase08() {
		ReporterCSS.title("测试点: 采购规格批量导入修改");
		PurchaseSpecBean purchaseSpec = null;
		try {
			List<PurchaseSpecBean> purchaseSpecList = categoryService.searchPurchaseSpec(purchaseSpecFilterParam);
			Assert.assertNotEquals(purchaseSpecList, null, "搜索过滤采购规格失败");

			purchaseSpec = NumberUtil.roundNumberInList(purchaseSpecList);

			String purchase_spec_id = purchaseSpec.getId();

			List<PurchaseSpecBatchEditParam> purchaseSpecBatchEditParams = new ArrayList<PurchaseSpecBatchEditParam>();
			PurchaseSpecBatchEditParam purchaseSpecBatchEditParam = new PurchaseSpecBatchEditParam();
			purchaseSpecBatchEditParam.setPur_spec_id(purchase_spec_id);
			String new_name = purchaseSpec.getName() + StringUtil.getRandomString(2);
			purchaseSpecBatchEditParam.setName(new_name);
			String desc = StringUtil.getRandomString(6);
			purchaseSpecBatchEditParam.setPurchase_desc(desc);
			String purchase_unit = "N";
			purchaseSpecBatchEditParam.setPurchase_unit(purchase_unit);
			BigDecimal ratio = new BigDecimal("2");
			purchaseSpecBatchEditParam.setRatio(new BigDecimal("2"));

			purchaseSpecBatchEditParams.add(purchaseSpecBatchEditParam);

			BigDecimal task_id = categoryService.importEditPurchaseSpecs(purchaseSpecBatchEditParams);
			Assert.assertNotEquals(task_id, null, "采购规格批量导入修改,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "存在失败信息0条");
			Assert.assertEquals(result, true, "采购规格批量导入修改,异步任务执行失败");

			PurchaseSpecBean tempPurchaseSpec = categoryService.getPurchaseSpecById(purchase_spec_id);
			Assert.assertNotEquals(tempPurchaseSpec, null, "获取采购规格" + purchase_spec_id + "信息失败");

			String msg = null;
			if (!tempPurchaseSpec.getName().equals(new_name)) {
				msg = String.format("采购规格%s批量导入修改,名称与预期不一致,预期:%s,实际:%s", purchase_spec_id, new_name,
						tempPurchaseSpec.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPurchaseSpec.getDescription().equals(desc)) {
				msg = String.format("采购规格%s批量导入修改,描述与预期不一致,预期:%s,实际:%s", purchase_spec_id, desc,
						tempPurchaseSpec.getDescription());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (tempPurchaseSpec.getRatio().compareTo(ratio) != 0) {
				msg = String.format("采购规格%s批量导入修改,采购比例与预期不一致,预期:%s,实际:%s", purchase_spec_id, ratio,
						tempPurchaseSpec.getRatio());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!tempPurchaseSpec.getPurchase_unit().equals(purchase_unit)) {
				msg = String.format("采购规格%s批量导入修改,采购规格与预期不一致,预期:%s,实际:%s", purchase_spec_id, purchase_unit,
						tempPurchaseSpec.getPurchase_unit());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "采购规格批量导入修改,修改信息与预期不一致");
		} catch (Exception e) {
			logger.error("采购规格批量导入修改遇到错误: ", e);
			Assert.fail("采购规格批量导入修改遇到错误: ", e);
		} finally {
			if (purchaseSpec != null) {
				try {
					purchaseSpec.setUnit_name(purchaseSpec.getPurchase_unit());
					boolean result = categoryService.updatePurchaseSpec(purchaseSpec);
					Assert.assertEquals(result, true, "采购规格修改失败");
				} catch (Exception e) {
					logger.error("采购规格修改遇到错误: ", e);
					Assert.fail("采购规格修改遇到错误: ", e);
				}
			}
		}
	}
}
