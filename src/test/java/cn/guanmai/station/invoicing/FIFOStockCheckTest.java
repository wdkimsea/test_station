package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;

import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.CategoriesBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.invoicing.BatchLogBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.StockBatchCheckResultBean;
import cn.guanmai.station.bean.invoicing.StockBatchModel;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.param.BatchStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckTemplateParam;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Mar 27, 2019 11:34:28 AM 
* @des 先进先出站点批次盘点
* @version 1.0 
*/
public class FIFOStockCheckTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(FIFOStockCheckTest.class);
	private StockCheckService stockCheckService;
	private AsyncService asyncService;
	private CategoryService categoryService;
	private InitDataBean initData;
	private String spu_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockCheckService = new StockCheckServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		try {
			initData = getInitData();
			spu_id = initData.getSpu().getId();

			List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
			Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");
			// 没有批次则进行入库
			if (stockBatchList.size() == 0) {
				InStockTool stockInTool = new InStockTool(headers);
				SupplierDetailBean supplier = initData.getSupplier();
				String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(supplier.getId(),
						new String[] { "大叶茼蒿" });
				Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

				stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
				Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

				Assert.assertEquals(stockBatchList.size() > 0, true, "SPU商品 " + spu_id + "没有对应的入库批次");
			}
		} catch (Exception e) {
			logger.error("初始化站点数据过程中出现错误: ", e);
			Assert.fail("初始化站点数据过程中出现错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase01() {
		ReporterCSS.title("测试点: 获取指定SPU对应的批次列表信息");
		try {
			List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
			Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");
		} catch (Exception e) {
			logger.error("获取商品对应的批次列表信息出现错误: ", e);
			Assert.fail("获取商品对应的批次列表信息出现错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase02() {
		ReporterCSS.title("测试点: 对批次库存进行盘点操作");
		try {
			List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
			Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

			StockBatchBean stockBatch = stockBatchList.get(0);

			String batch_number = stockBatch.getBatch_number();

			boolean reuslt = stockCheckService.editBatchStock(batch_number, new BigDecimal("0"), "自动化侧测试");
			Assert.assertEquals(reuslt, true, "先进先出批次盘点操作失败");

		} catch (Exception e) {
			logger.error("获取商品对应的批次列表信息出现错误: ", e);
			Assert.fail("获取商品对应的批次列表信息出现错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase03() {
		ReporterCSS.title("测试点: 获取指定批次对应的库存变动历史记录");
		try {
			List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
			Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

			StockBatchBean stockBatch = stockBatchList.get(0);

			String batch_number = stockBatch.getBatch_number();

			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd");

			String start_time = TimeUtil.calculateTime("yyyy-MM-dd", end_time, -7, Calendar.DATE);

			List<BatchLogBean> batchLogs = stockCheckService.getBatchLog(start_time, end_time, batch_number);
			Assert.assertEquals(batchLogs != null, true, "获取批次 " + batch_number + " 的库存历史记录失败");

		} catch (Exception e) {
			logger.error("获取商品对应的批次列表信息出现错误: ", e);
			Assert.fail("获取商品对应的批次列表信息出现错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase04() {
		ReporterCSS.title("测试点: 获取站点所有的一二级分类,用于进销存分类过滤");
		try {
			List<CategoriesBean> categories = stockCheckService.getCategories();
			Assert.assertNotNull(categories, "获取站点所有的一二级分类接口调用失败");
		} catch (Exception e) {
			logger.error("获取站点所有的一二级分类遇到错误: ", e);
			Assert.fail("获取站点所有的一二级分类遇到错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase05() {
		ReporterCSS.title("测试点: 下载批量盘点模板");
		try {
			StockCheckTemplateParam stockCheckTemplateParam = new StockCheckTemplateParam();
			stockCheckTemplateParam.setBegin_time(todayStr);
			stockCheckTemplateParam.setEnd_time(todayStr);
			stockCheckTemplateParam.setExport_type(1);
			stockCheckTemplateParam.setCategory_id_1(new ArrayList<String>());
			stockCheckTemplateParam.setCategory_id_2(new ArrayList<String>());
			stockCheckTemplateParam.setPinlei_ids(new ArrayList<String>());

			BigDecimal task_id = stockCheckService.downloadStockCheckTemplateStep1(stockCheckTemplateParam);
			Assert.assertNotEquals(task_id, null, "下载批量盘点模板异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "先进先出站点,导出批量盘点模板异步任务执行失败");

			List<AsyncTaskResultBean> asyncTaskResultList = asyncService.getAsyncTaskResultList();
			AsyncTaskResultBean asyncTaskResult = asyncTaskResultList.stream()
					.filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny().orElse(null);

			String link = asyncTaskResult.getResult().getLink();

			String file_path = stockCheckService.downloadStockCheckTemplateStep2(link);
			Assert.assertNotEquals(file_path, null, "下载批量模板文件失败");

		} catch (Exception e) {
			logger.error("下载批量盘点模板遇到错误: ", e);
			Assert.fail("下载批量盘点模板遇到错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase06() {
		ReporterCSS.title("测试点: 分类过滤后再下载批量盘点模板");
		try {
			List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch("", 0, 10);
			Assert.assertNotEquals(stockBatchList, null, "按批次盘点页面搜索过滤失败");

			Assert.assertEquals(stockBatchList.size() > 0, true, "没有批次相关信息,无法进行分类搜索过滤");

			StockBatchBean stockBatch = stockBatchList.get(0);
			String spec_id = stockBatch.getSku_id();

			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格 " + spec_id + " 详细信息失败");
			String category1_id = purchaseSpec.getCategory_1();
			String category2_id = purchaseSpec.getCategory_2();
			String pinlei_id = purchaseSpec.getPinlei();

			StockCheckTemplateParam stockCheckTemplateParam = new StockCheckTemplateParam();
			stockCheckTemplateParam.setBegin_time(todayStr);
			stockCheckTemplateParam.setEnd_time(todayStr);
			stockCheckTemplateParam.setExport_type(1);
			stockCheckTemplateParam.setCategory_id_1(Arrays.asList(category1_id));
			stockCheckTemplateParam.setCategory_id_2(Arrays.asList(category2_id));
			stockCheckTemplateParam.setPinlei_ids(Arrays.asList(pinlei_id));

			BigDecimal task_id = stockCheckService.downloadStockCheckTemplateStep1(stockCheckTemplateParam);
			Assert.assertNotEquals(task_id, null, "批量盘点模板导出,异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "先进先出站点,导出批量盘点模板异步任务执行失败");

			List<AsyncTaskResultBean> asyncTaskResultList = asyncService.getAsyncTaskResultList();
			AsyncTaskResultBean asyncTaskResult = asyncTaskResultList.stream()
					.filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny().orElse(null);

			String link = asyncTaskResult.getResult().getLink();

			String file_path = stockCheckService.downloadStockCheckTemplateStep2(link);
			Assert.assertNotEquals(file_path, null, "下载批量模板文件失败");

		} catch (Exception e) {
			logger.error("下载批量盘点模板遇到错误: ", e);
			Assert.fail("下载批量盘点模板遇到错误: ", e);
		}
	}

	@Test
	public void fIFOStockCheckTestCase07() {
		ReporterCSS.title("测试点: 先进先出批次库存批量盘点");
		try {
			StockCheckTemplateParam stockCheckTemplateParam = new StockCheckTemplateParam();
			stockCheckTemplateParam.setBegin_time(todayStr);
			stockCheckTemplateParam.setEnd_time(todayStr);
			stockCheckTemplateParam.setExport_type(1);
			stockCheckTemplateParam.setCategory_id_1(new ArrayList<String>());
			stockCheckTemplateParam.setCategory_id_2(new ArrayList<String>());
			stockCheckTemplateParam.setPinlei_ids(new ArrayList<String>());

			final BigDecimal task_id = stockCheckService.downloadStockCheckTemplateStep1(stockCheckTemplateParam);
			Assert.assertNotEquals(task_id, null, "先进先出站点,批量盘点模板导出异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "先进先出站点,导出批量盘点模板异步任务执行失败");

			List<AsyncTaskResultBean> asyncTaskResultList = asyncService.getAsyncTaskResultList();
			Assert.assertNotEquals(asyncTaskResultList, null, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTaskResult = asyncTaskResultList.stream()
					.filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny().orElse(null);

			String link = asyncTaskResult.getResult().getLink();

			String file_path = stockCheckService.downloadStockCheckTemplateStep2(link);
			Assert.assertNotEquals(file_path, null, "下载批量模板文件失败");

			List<StockBatchModel> stockBatchModelList = EasyExcelFactory.read(file_path).sheet(0)
					.head(StockBatchModel.class).doReadSync();

			// 取四个批次信息进行盘点
			stockBatchModelList = NumberUtil.roundNumberInList(stockBatchModelList, 4);
			double old_stock = 0;
			for (StockBatchModel stockBatchModel : stockBatchModelList) {
				old_stock = stockBatchModel.getOld_stock();
				if (old_stock < 0) {
					stockBatchModel.setNew_stock(0);
				} else {
					stockBatchModel.setNew_stock(old_stock += 1);
				}
				stockBatchModel.setRemark(StringUtil.getRandomString(6));
			}

			EasyExcel.write(file_path, StockBatchModel.class).sheet(0, "批次").doWrite(stockBatchModelList);

			List<StockBatchCheckResultBean> stockBatchCheckResultList = stockCheckService
					.uploadStockCheckTemplate(file_path);
			Assert.assertNotEquals(stockBatchCheckResultList, null, "批次盘点模板上传失败");

			StockBatchCheckResultBean stockBatchCheckResult = null;
			String msg = null;
			List<BatchStockCheckParam> stock_details = new ArrayList<BatchStockCheckParam>(); // 批量盘点参数
			BatchStockCheckParam stock_detail = null;
			for (StockBatchModel stockBatch : stockBatchModelList) {
				String batch_number = stockBatch.getBatch_number();

				stock_detail = new BatchStockCheckParam();
				stock_detail.setBatch_number(batch_number);
				stock_detail.setNew_stock(stockBatch.getNew_stock());
				stock_detail.setRemark(stockBatch.getRemark());
				stock_details.add(stock_detail);

				stockBatchCheckResult = stockBatchCheckResultList.stream()
						.filter(s -> s.getBatch_number().equals(batch_number)).findAny().orElse(null);
				if (stockBatchCheckResult == null) {
					msg = String.format("批次盘点模板中填写的批次号%s记录信息没上传成功", batch_number);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (stockBatch.getNew_stock() != stockBatchCheckResult.getNew_stock()) {
					msg = String.format("批次盘点模板中填写的批次号%s对应的实盘数与预期不一致,预期:%s,实际:%s", batch_number,
							stockBatch.getNew_stock(), stockBatchCheckResult.getNew_stock());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!stockBatch.getRemark().equals(stockBatchCheckResult.getRemark())) {
					msg = String.format("批次盘点模板中填写的批次号%s对应的备注信息与预期不一致,预期:%s,实际:%s", batch_number,
							stockBatch.getRemark(), stockBatchCheckResult.getRemark());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			boolean success = stockCheckService.checkBatchStock(stock_details);
			Assert.assertEquals(success, true, "批次批量盘点失败");

			Assert.assertEquals(result, true, "批次盘点上传模板后返回的信息与模板中的不一致");

			Thread.sleep(1000);

			asyncTaskResultList = asyncService.getAsyncTaskResultList();
			Assert.assertNotEquals(asyncTaskResultList, null, "获取异步任务列表失败");

			asyncTaskResult = asyncTaskResultList.stream().filter(a -> a.getTask_name().startsWith("盘点库存_")).findFirst()
					.orElse(null);
			Assert.assertNotEquals(asyncTaskResult, null, "获取批次批量盘点异步任务失败");

			BigDecimal batch_stock_check_task_id = asyncTaskResult.getTask_id();

			result = asyncService.getAsyncTaskResult(batch_stock_check_task_id,
					"盘点库存完成，成功" + stock_details.size() + "，失败0");
			Assert.assertEquals(result, true, "批次批量盘点异步任务执行失败");

			String batch_number = null;
			BigDecimal expected_remain = null;
			for (BatchStockCheckParam batchStockCheckParam : stock_details) {
				batch_number = batchStockCheckParam.getBatch_number();
				StockBatchBean stockBatch = stockCheckService.getStockBatch(batch_number);
				expected_remain = new BigDecimal(String.valueOf(batchStockCheckParam.getNew_stock()));
				if (stockBatch.getRemain().compareTo(expected_remain) != 0) {
					msg = String.format("批次批量盘点,批次%s库存没有修改成功,预期:%s,实际:%s", batch_number,
							batchStockCheckParam.getNew_stock(), stockBatch.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "批次批量盘点,批次库存数没有修改成功");
		} catch (Exception e) {
			logger.error("先进先出批次库存批量盘点遇到错误: ", e);
			Assert.fail("先进先出批次库存批量盘点遇到错误: ", e);
		}
	}

}
