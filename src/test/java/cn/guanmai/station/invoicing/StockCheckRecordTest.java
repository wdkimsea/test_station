package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.StockIncreaseRecordBean;
import cn.guanmai.station.bean.invoicing.StockLossRecordBean;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 8, 2019 7:59:01 PM 
* @des 库存记录测试用例
* @version 1.0 
*/
public class StockCheckRecordTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockCheckRecordTest.class);
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private StockRecordService stockRecordService;
	private StockCheckService stockCheckService;
	private InitDataBean initData;
	private int stock_method;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户信息失败");

			stock_method = loginUserInfo.getStock_method();
			Assert.assertEquals(stock_method == 1 || stock_method == 2, true, "获取的进销存类型值不是列表中的值 " + stock_method);

			initData = getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");

			InStockTool stockInTool = new InStockTool(headers);
			String supplier_id = initData.getSupplier().getId();
			String[] texts = new String[] { "e", "j" };
			String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(supplier_id, texts);
			Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}

	}

	@Test
	public void inCreaseRecordTestCase01() {
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(10);
		filterParam.setRemain_status(3);

		ReporterCSS.title("测试点: 商品盘点,报溢日志查询");
		try {
			BigDecimal increase_amount = new BigDecimal("10");
			BigDecimal increase_price = null;
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
			if (stockCheckList.size() == 0) {
				filterParam.setRemain_status(null);
				stockCheckList = stockCheckService.searchStockCheck(filterParam);
				Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
			}

			SpuStockBean spuStock = NumberUtil.roundNumberInList(stockCheckList);
			String spu_id = spuStock.getSpu_id();
			if (stock_method == 1) {
				// 现有库存
				increase_price = spuStock.getAvg_price();
				BigDecimal new_stock = null;
				BigDecimal old_stock = spuStock.getMaterial().getAmount();
				if (old_stock.compareTo(new BigDecimal("0")) < 0) {
					increase_amount = old_stock.abs();
					new_stock = new BigDecimal("0");
				} else {
					// 在现有的库存上加10
					new_stock = old_stock.add(increase_amount);
				}
				boolean result = stockCheckService.editSpuStock(spu_id, new_stock, "报溢测试");
				Assert.assertEquals(result, true, "加权平局站点商品盘点报溢操作失败");

			} else {
				List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 20);
				Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

				// 找一个负库存的批次
				StockBatchBean tempStockBatch = stockBatchList.stream()
						.filter(s -> s.getRemain().compareTo(new BigDecimal("0")) < 0).findFirst().orElse(null);

				BigDecimal new_remain = null;
				BigDecimal remain = null;
				if (tempStockBatch == null) {
					tempStockBatch = NumberUtil.roundNumberInList(stockBatchList);
					remain = tempStockBatch.getRemain();
					new_remain = remain.add(increase_amount);
				} else {
					remain = tempStockBatch.getRemain();
					increase_amount = remain.abs();
					new_remain = new BigDecimal("0");
				}
				increase_price = tempStockBatch.getPrice();

				String batch_number = tempStockBatch.getBatch_number();

				boolean reuslt = stockCheckService.editBatchStock(batch_number, new_remain, "自动化侧测试");
				Assert.assertEquals(reuslt, true, "先进先出批次盘点操作失败");
			}

			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockIncreaseRecordBean> stockIncreaseArray = stockRecordService
					.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			StockIncreaseRecordBean stockIncreaseRecord = stockIncreaseArray.stream()
					.filter(s -> s.getSpu_id().equals(spu_id)).findFirst().orElse(null);

			Assert.assertNotEquals(stockIncreaseRecord, null, "商品" + spu_id + "的报溢记录没有找到");

			boolean result = true;
			String msg = null;
			if (stockIncreaseRecord.getIncrease_amount().compareTo(increase_amount) != 0) {
				msg = String.format("商品%s的报溢数与预期不一致,预期:%s,实际:%s", spu_id, increase_amount,
						stockIncreaseRecord.getIncrease_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockIncreaseRecord.getPrice().compareTo(increase_price) != 0) {
				msg = String.format("商品%s的报溢单价与预期不一致,预期:%s,实际:%s", spu_id, increase_price,
						stockIncreaseRecord.getPrice());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商品报溢记录与预期不一致");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inCreaseRecordTestCase01" }, alwaysRun = true)
	public void inCreaseRecordTestCase02() {
		ReporterCSS.title("测试点: 报溢时间+商品ID 查询报溢记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockIncreaseRecordBean> stockIncreaseArray = stockRecordService
					.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() > 0, true, "没有商品报溢日志,无法进行过滤筛选");

			StockIncreaseRecordBean stockIncreaseRecord = NumberUtil.roundNumberInList(stockIncreaseArray);

			String spu_id = stockIncreaseRecord.getSpu_id();
			stockRecordParam.setText(spu_id);
			stockIncreaseArray = stockRecordService.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() >= 1, true, "按报溢时间+商品ID 过滤报溢记录,没有过滤出数据");

			List<String> spu_ids = stockIncreaseArray.stream().filter(s -> !s.getSpu_id().equals(spu_id))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(spu_ids.size(), 0, "按报溢时间+商品ID 过滤报溢记录,过滤出了如下不合符过滤条件的报溢记录 " + spu_ids);
		} catch (Exception e) {
			logger.error("查询报溢记录遇到错误: ", e);
			Assert.fail("查询报溢记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inCreaseRecordTestCase01" }, alwaysRun = true)
	public void inCreaseRecordTestCase03() {
		ReporterCSS.title("测试点: 报溢时间+商品ID 查询报溢记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockIncreaseRecordBean> stockIncreaseArray = stockRecordService
					.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() > 0, true, "没有商品报溢日志,无法进行过滤筛选");

			StockIncreaseRecordBean stockIncreaseRecord = NumberUtil.roundNumberInList(stockIncreaseArray);

			String spu_name = stockIncreaseRecord.getName();
			stockRecordParam.setText(spu_name);
			stockIncreaseArray = stockRecordService.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() >= 1, true, "按报溢时间+商品ID 过滤报溢记录,没有过滤出数据");

			List<String> spu_names = stockIncreaseArray.stream().filter(s -> !s.getName().contains(spu_name))
					.map(s -> s.getName()).collect(Collectors.toList());
			Assert.assertEquals(spu_names.size(), 0, "按报溢时间+商品名称 过滤报溢记录,过滤出了如下不合符过滤条件的报溢记录 " + spu_names);
		} catch (Exception e) {
			logger.error("查询报溢记录遇到错误: ", e);
			Assert.fail("查询报溢记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inCreaseRecordTestCase01" }, alwaysRun = true)
	public void inCreaseRecordTestCase04() {
		ReporterCSS.title("测试点: 报溢时间+商品分类 查询报溢记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockIncreaseRecordBean> stockIncreaseArray = stockRecordService
					.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() > 0, true, "没有商品报溢日志,无法进行过滤筛选");

			StockIncreaseRecordBean stockIncreaseRecord = NumberUtil.roundNumberInList(stockIncreaseArray);
			String category1_id = stockIncreaseRecord.getCategory_id_1();
			String category2_id = stockIncreaseRecord.getCategory_id_2();
			stockRecordParam.setCategory_id_1(Arrays.asList(category1_id));
			stockRecordParam.setCategory_id_2(Arrays.asList(category2_id));

			stockIncreaseArray = stockRecordService.increaseStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockIncreaseArray, null, "查询商品盘点报溢日志失败");

			Assert.assertEquals(stockIncreaseArray.size() >= 1, true, "按报溢时间+商品分类 过滤报溢记录,没有过滤出数据");

			List<String> spu_ids = stockIncreaseArray.stream().filter(
					s -> !s.getCategory_id_1().equals(category1_id) || !s.getCategory_id_2().equals(category2_id))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(spu_ids.size(), 0, "按报溢时间+商品分类 过滤报溢记录,过滤出了如下不合符过滤条件的报溢记录 " + spu_ids);
		} catch (Exception e) {
			logger.error("查询报溢记录遇到错误: ", e);
			Assert.fail("查询报溢记录遇到错误: ", e);
		}
	}

	@Test
	public void lossStockRecordTestCase01() {
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(10);
		filterParam.setRemain_status(1);

		ReporterCSS.title("测试点: 商品盘点,报溢日志查询");
		try {
			BigDecimal loss_amount = null;
			BigDecimal loss_price = null;
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
			if (stockCheckList.size() == 0) {
				filterParam.setRemain_status(null);
				stockCheckList = stockCheckService.searchStockCheck(filterParam);
				Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
			}

			SpuStockBean spuStock = NumberUtil.roundNumberInList(stockCheckList);
			String spu_id = spuStock.getSpu_id();
			if (stock_method == 1) {
				loss_price = spuStock.getAvg_price().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal old_stock = spuStock.getMaterial().getAmount();
				if (old_stock.compareTo(new BigDecimal("0")) <= 0) {
					boolean result = stockCheckService.editSpuStock(spu_id, loss_amount, "先报溢");
					Assert.assertEquals(result, true, "商品" + spu_id + "报溢操作失败");
				} else {
					loss_amount = spuStock.getRemain();
				}

				boolean result = stockCheckService.editSpuStock(spu_id, new BigDecimal("0"), "报损测试");
				Assert.assertEquals(result, true, "加权平局站点商品盘点报损操作失败");
			} else {
				List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 20);
				Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

				// 找一个正库存的批次
				StockBatchBean tempStockBatch = stockBatchList.stream()
						.filter(s -> s.getRemain().compareTo(new BigDecimal("0")) > 0).findFirst().orElse(null);

				if (tempStockBatch == null) {
					tempStockBatch = stockBatchList.get(0);
					String batch_number = tempStockBatch.getBatch_number();
					boolean reuslt = stockCheckService.editBatchStock(batch_number, loss_amount, "自动化侧测试");
					Assert.assertEquals(reuslt, true, "先进先出批次报溢操作失败");
				} else {
					loss_amount = tempStockBatch.getRemain();
				}
				loss_price = tempStockBatch.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);

				String batch_number = tempStockBatch.getBatch_number();

				boolean reuslt = stockCheckService.editBatchStock(batch_number, new BigDecimal("0"), "自动化侧测试");
				Assert.assertEquals(reuslt, true, "先进先出批次报损操作失败");
			}

			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockLossRecordBean> stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			StockLossRecordBean stockLossRecord = stockLossRecords.stream().filter(s -> s.getSpu_id().equals(spu_id))
					.findFirst().orElse(null);

			Assert.assertNotEquals(stockLossRecord, null, "商品" + spu_id + "的报损记录没有找到");

			boolean result = true;
			String msg = null;
			if (stockLossRecord.getLoss_amount().compareTo(loss_amount) != 0) {
				msg = String.format("商品%s的报溢数与预期不一致,预期:%s,实际:%s", spu_id, loss_amount,
						stockLossRecord.getLoss_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockLossRecord.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(loss_price) != 0) {
				msg = String.format("商品%s的报溢单价与预期不一致,预期:%s,实际:%s", spu_id,
						loss_price.setScale(2, BigDecimal.ROUND_HALF_UP), stockLossRecord.getPrice());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商品报损记录与预期不一致");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "lossStockRecordTestCase01" }, alwaysRun = true)
	public void lossStockRecordTestCase02() {
		ReporterCSS.title("测试点: 报损时间+商品ID 查询报损记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockLossRecordBean> stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() > 0, true, "没有商品报损日志,无法进行过滤筛选");

			StockLossRecordBean stockLossRecord = NumberUtil.roundNumberInList(stockLossRecords);

			String spu_id = stockLossRecord.getSpu_id();
			stockRecordParam.setText(spu_id);
			stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() >= 1, true, "按报损时间+商品ID 过滤报损记录,没有过滤出数据");

			List<String> spu_ids = stockLossRecords.stream().filter(s -> !s.getSpu_id().equals(spu_id))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(spu_ids.size(), 0, "按报损时间+商品ID 过滤报损记录,过滤出了如下不合符过滤条件的报损记录 " + spu_ids);
		} catch (Exception e) {
			logger.error("查询报溢记录遇到错误: ", e);
			Assert.fail("查询报溢记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "lossStockRecordTestCase01" }, alwaysRun = true)
	public void lossStockRecordTestCase03() {
		ReporterCSS.title("测试点: 报损时间+商品名称 查询报损记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockLossRecordBean> stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() > 0, true, "没有商品报损日志,无法进行过滤筛选");

			StockLossRecordBean stockLossRecord = NumberUtil.roundNumberInList(stockLossRecords);

			String spu_name = stockLossRecord.getName();
			stockRecordParam.setText(spu_name);
			stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() >= 1, true, "按报损时间+商品名称 过滤报损记录,没有过滤出数据");

			List<String> spu_names = stockLossRecords.stream().filter(s -> !s.getName().contains(spu_name))
					.map(s -> s.getName()).collect(Collectors.toList());
			Assert.assertEquals(spu_names.size(), 0, "按报损时间+商品名称 过滤报损记录,过滤出了如下不合符过滤条件的报损记录 " + spu_names);
		} catch (Exception e) {
			logger.error("查询报损记录遇到错误: ", e);
			Assert.fail("查询报损记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "lossStockRecordTestCase01" }, alwaysRun = true)
	public void lossStockRecordTestCase04() {
		ReporterCSS.title("测试点: 报损时间+商品分类 查询报损记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int offset = 0;
			int limit = 10;
			stockRecordParam.setOffset(offset);
			stockRecordParam.setLimit(limit);

			List<StockLossRecordBean> stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() > 0, true, "没有商品报溢日志,无法进行过滤筛选");

			StockLossRecordBean stockLossRecord = NumberUtil.roundNumberInList(stockLossRecords);

			String category1_id = stockLossRecord.getCategory_id_1();
			String category2_id = stockLossRecord.getCategory_id_2();
			List<String> category_id_1 = new ArrayList<String>();
			List<String> category_id_2 = new ArrayList<String>();
			category_id_1.add(category1_id);
			category_id_2.add(category2_id);

			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			stockLossRecords = stockRecordService.lossStockRecords(stockRecordParam);
			Assert.assertNotEquals(stockLossRecords, null, "查询商品盘点报损日志失败");

			Assert.assertEquals(stockLossRecords.size() >= 1, true, "按报溢时间+商品分类 过滤报损记录,没有过滤出数据");

			List<String> spu_ids = stockLossRecords.stream().filter(
					s -> !s.getCategory_id_1().contains(category1_id) || !s.getCategory_id_2().contains(category2_id))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(spu_ids.size(), 0, "按报损时间+商品分类 过滤报损记录,过滤出了如下不合符过滤条件的报损记录 " + spu_ids);
		} catch (Exception e) {
			logger.error("查询报损记录遇到错误: ", e);
			Assert.fail("查询报损记录遇到错误: ", e);
		}
	}

}
