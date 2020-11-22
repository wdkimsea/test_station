package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.InStockRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.StockSummaryBean;
import cn.guanmai.station.bean.invoicing.StockSummaryCategoryDetailBean;
import cn.guanmai.station.bean.invoicing.InStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.param.InStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockSummaryFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.invoicing.StockSummaryServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.invoicing.StockSummaryService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年7月29日 下午7:14:56
 * @des 出入库汇总统计
 * @version 1.0
 */
public class StockSummaryTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockSummaryTest.class);
	private StockSummaryService stockSummaryService;
	private StockRecordService stockRecordService;
	private PurchaseTaskService purchaseTaskService;
	private AsyncService asyncService;
	private StockSummaryFilterParam stockSummaryFilterParam;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String summary_start_date;
	private String summary_end_date;
	// 自定义的差值误差范围
	private BigDecimal customize_deviation_value = new BigDecimal("0.5");

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockSummaryService = new StockSummaryServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		try {
			summary_start_date = TimeUtil.calculateTime("yyyy-MM-dd", today, -2, Calendar.DATE);
			summary_end_date = today;
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMenthod() {
		stockSummaryFilterParam = new StockSummaryFilterParam();
		stockSummaryFilterParam.setBegin(summary_start_date);
		stockSummaryFilterParam.setEnd(summary_end_date);
		stockSummaryFilterParam.setLimit(50);
		stockSummaryFilterParam.setOffset(0);
	}

	@Test
	public void inStockSummaryTestCase01() {
		ReporterCSS.title("测试点: 入库汇总,按SPU查看统计数据");
		try {
			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按SPU查看统计数据失败");
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看统计数据遇到错误: ", e);
		}
	}

	@Test
	public void inStockSummaryTestCase02() {
		ReporterCSS.title("测试点: 入库汇总,按SPU查看详细数据");
		try {
			List<InStockSummarySpuDetailBean> stockSummaryDetailList = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryDetailList, null, "入库汇总,按SPU查看详细数据失败");
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看详细数据遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看详细数据遇到错误: ", e);
		}
	}

	@Test
	public void inStockSummaryTestCase03() {
		ReporterCSS.title("测试点: 入库汇总,按分类查看查看统计数据");
		try {
			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按分类查看统计数据失败");
		} catch (Exception e) {
			logger.error("入库汇总,按分类查看统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按分类查看统计数据遇到错误: ", e);
		}
	}

	@Test
	public void inStockSummaryTestCase04() {
		ReporterCSS.title("测试点: 入库汇总,按分类查看详细数据");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryDetailList = stockSummaryService
					.inStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryDetailList, null, "入库汇总,按分类查看详细数据失败");
		} catch (Exception e) {
			logger.error("入库汇总,按分类查看统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按分类查看统计数据遇到错误: ", e);
		}
	}

	// 用作数据对比
	private List<InStockRecordBean> inStockRecords = new ArrayList<InStockRecordBean>();
	// 以SPU_ID 分组
	private Map<String, List<InStockRecordBean>> stockInRecordMap;

	@Test
	public void inStockSummaryTestCase05() {
		ReporterCSS.title("测试点: 查询入库记录,用作后面的数据对比");
		try {
			InStockRecordFilterParam stockInRecordFilterParam = new InStockRecordFilterParam();
			stockInRecordFilterParam.setTime_type(2);
			stockInRecordFilterParam.setBegin(summary_start_date);
			stockInRecordFilterParam.setEnd(summary_end_date);
			int limit = 50;
			stockInRecordFilterParam.setLimit(50);
			int offset = 0;
			List<InStockRecordBean> tempStockInRecords = null;
			while (true) {
				stockInRecordFilterParam.setOffset(offset);
				tempStockInRecords = stockRecordService.inStockRecords(stockInRecordFilterParam);
				Assert.assertNotEquals(tempStockInRecords, null, "查询入库记录失败");
				inStockRecords.addAll(tempStockInRecords);
				if (tempStockInRecords.size() < limit) {
					break;
				}
				offset += limit;
			}
			ReporterCSS.log(summary_start_date + "--" + summary_end_date + " 的入库条目数为" + inStockRecords.size());
			Assert.assertEquals(inStockRecords.size() > 0, true, "近三天无入库记录,无法进行入库汇总统计验证测试");

			stockInRecordMap = inStockRecords.stream().collect(Collectors.groupingBy(InStockRecordBean::getSpu_id));
		} catch (Exception e) {
			logger.error("查询入库记录遇到错误: ", e);
			Assert.fail("查询入库记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase06() {
		ReporterCSS.title("测试点: 入库汇总,统计" + summary_start_date + "——" + summary_end_date + "入库商品汇总");
		try {
			ReporterCSS.step("步骤一: 获取近一个星期的入库汇总统计");

			List<InStockSummarySpuDetailBean> stockSummarySpuDetails = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummarySpuDetails, null, "入库汇总,按商品分类查看详细数据失败");

			Assert.assertEquals(stockSummarySpuDetails.size() > 0, true, "入库汇总,上个星期没有入库数据,无法进行校验");

			String spu_id = null;
			BigDecimal amount = null;
			BigDecimal value = null;
			String supplier_name = null;
			String msg = null;
			boolean result = true;
			List<InStockRecordBean> tempStockInRecords = null;
			for (InStockSummarySpuDetailBean stockSummarySpuDetail : stockSummarySpuDetails) {
				amount = new BigDecimal("0");
				value = new BigDecimal("0");
				spu_id = stockSummarySpuDetail.getSpu_id();
				supplier_name = stockSummarySpuDetail.getSupplier_name();
				tempStockInRecords = stockInRecordMap.get(spu_id);

				for (InStockRecordBean stockInRecord : tempStockInRecords) {
					if (stockInRecord.getSupplier_name().equals(supplier_name)) {
						amount = amount.add(stockInRecord.getIn_stock_amount());
						value = value.add(stockInRecord.getAll_price());
					}
				}
				if (stockSummarySpuDetail.getAmount().compareTo(amount.setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
					msg = String.format("供应商:%s,入库商品:%s,统计的入库数与预期的不一致,预期:%s,实际:%s", supplier_name, spu_id,
							amount.setScale(2, BigDecimal.ROUND_HALF_UP), stockSummarySpuDetail.getAmount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (stockSummarySpuDetail.getValue().compareTo(value.setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
					msg = String.format("供应商:%s,入库商品:%s,统计的入库货值与预期的不一致,预期:%s,实际:%s", supplier_name, spu_id,
							value.setScale(2, BigDecimal.ROUND_HALF_UP), stockSummarySpuDetail.getValue());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "入库汇总统计的与预期的不一致");
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看统计数据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase07() {
		ReporterCSS.title("测试点: 入库汇总,统计" + summary_start_date + "——" + summary_end_date + "入库总金额");
		try {
			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "查询入库汇总总金额失败");

			BigDecimal total_value = new BigDecimal("0");
			for (InStockRecordBean stockInRecord : inStockRecords) {
				total_value = total_value.add(stockInRecord.getAll_price());
			}

			String msg = null;
			boolean result = true;
			if (stockSummary.getSpu_num() != stockInRecordMap.size()) {
				msg = String.format("入库汇总商品种数与预期不一致,预期:%s,实际:%s", stockInRecordMap.size(), stockSummary.getSpu_num());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			BigDecimal deviation_value = total_value.subtract(stockSummary.getTotal_value()).abs();
			if (deviation_value.compareTo(customize_deviation_value) > 0) {
				msg = String.format("入库汇总商品总金额与预期不一致,预期:%s,实际:%s,两值相差:%s", total_value, stockSummary.getTotal_value(),
						deviation_value);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "入库汇总总计的信息与预期的不一致");
		} catch (Exception e) {
			logger.error("入库汇总,查询入库总金额遇到错误: ", e);
			Assert.fail("入库汇总,查询入库总金额遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase08() {
		ReporterCSS.title("测试点: 入库汇总,按分类查看统计" + summary_start_date + "——" + summary_end_date + "入库汇总");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetails = stockSummaryService
					.inStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetails, null, "按分类查看统计失败");

			Map<String, List<InStockRecordBean>> stockInRecordMap = inStockRecords.stream()
					.collect(Collectors.groupingBy(InStockRecordBean::getSupplier_name));

			List<InStockRecordBean> tempStockInRecords = null;
			BigDecimal total_value = null;
			StockSummaryCategoryDetailBean stockSummaryCategoryDetail = null;
			String msg = null;
			boolean result = true;
			for (String supplier_name : stockInRecordMap.keySet()) {
				tempStockInRecords = stockInRecordMap.get(supplier_name);
				total_value = new BigDecimal("0");
				for (InStockRecordBean stockInRecord : tempStockInRecords) {
					total_value = total_value.add(stockInRecord.getAll_price());
				}
				stockSummaryCategoryDetail = stockSummaryCategoryDetails.stream()
						.filter(s -> s.getSupplier_name().equals(supplier_name)).findAny().orElse(null);
				if (stockSummaryCategoryDetail == null) {
					msg = String.format("入库汇总,按分类统计,没有供应商 %s 的记录,与预期不符", supplier_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
				total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (total_value.compareTo(
						stockSummaryCategoryDetail.getTotal_value().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
					msg = String.format("入库汇总,按分类统计,供应商 %s 的入库金额小计与预期不符,预期:%s,实际:%s", supplier_name, total_value,
							stockSummaryCategoryDetail.getTotal_value().setScale(2, BigDecimal.ROUND_HALF_UP));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<StockSummaryCategoryDetailBean.Category1Detail> category1Details = stockSummaryCategoryDetail
						.getCategory1_details();

				Map<String, List<InStockRecordBean>> category1StockInRecordMap = tempStockInRecords.stream()
						.collect(Collectors.groupingBy(InStockRecordBean::getCategory_name_1));
				for (String category1_name : category1StockInRecordMap.keySet()) {
					StockSummaryCategoryDetailBean.Category1Detail category1Detail = category1Details.stream()
							.filter(c -> c.getCategory1_name().equals(category1_name)).findAny().orElse(null);
					if (category1Detail == null) {
						msg = String.format("入库汇总,按分类统计,供应商 %s 中没有分类 %s 的统计,与预期不符", supplier_name, category1_name);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					List<InStockRecordBean> category1StockInRecords = category1StockInRecordMap.get(category1_name);
					total_value = new BigDecimal("0");
					for (InStockRecordBean stockInRecord : category1StockInRecords) {
						total_value = total_value.add(stockInRecord.getAll_price());
					}
					total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
					if (total_value.compareTo(category1Detail.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
						msg = String.format("入库汇总,按分类统计,供应商 %s 分类 %s 统计的入库金额与预期不符,预期:%s,实际:%s", supplier_name,
								category1_name, total_value,
								category1Detail.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "入库汇总,按分类统计,统计的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("入库汇总,按分类统计遇到错误: ", e);
			Assert.fail("入库汇总,按分类统计遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase09() {
		ReporterCSS.title("测试点: 入库汇总,按SPU查看,输入商品ID过滤数据");
		try {
			List<InStockSummarySpuDetailBean> stockSummarySpuDetailList = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummarySpuDetailList, null, "入库汇总,按SPU查看失败");
			if (stockSummarySpuDetailList.size() > 0) {
				InStockSummarySpuDetailBean inStockSummarySpuDetail = NumberUtil
						.roundNumberInList(stockSummarySpuDetailList);
				String spu_id = inStockSummarySpuDetail.getSpu_id();

				stockSummaryFilterParam.setQ(spu_id);

				List<InStockSummarySpuDetailBean> actualStockSummarySpuDetailList = stockSummaryService
						.inStockSummaryDetailBySpu(stockSummaryFilterParam);

				List<InStockRecordBean> targetStockInRecords = inStockRecords.stream()
						.filter(s -> s.getSpu_id().equals(spu_id)).collect(Collectors.toList());
				BigDecimal expected_total_value = new BigDecimal("0");
				Map<String, BigDecimal> supplier_value_map = new HashMap<String, BigDecimal>();
				String supplier_name = null;
				for (InStockRecordBean stockInRecord : targetStockInRecords) {
					expected_total_value = expected_total_value.add(stockInRecord.getAll_price());
					supplier_name = stockInRecord.getSupplier_name();
					if (supplier_value_map.containsKey(supplier_name)) {
						BigDecimal temp_total_value = supplier_value_map.get(supplier_name);
						temp_total_value = temp_total_value.add(stockInRecord.getAll_price());
						supplier_value_map.put(supplier_name, temp_total_value);
					} else {
						supplier_value_map.put(supplier_name, stockInRecord.getAll_price());
					}
				}

				String msg = null;
				boolean result = true;
				BigDecimal total_value = new BigDecimal("0");
				BigDecimal deviation_value = null;
				Set<String> actual_supplier_names = new HashSet<String>();
				for (InStockSummarySpuDetailBean actualStockInSummarySpuDetail : actualStockSummarySpuDetailList) {
					total_value = total_value.add(actualStockInSummarySpuDetail.getValue());
					supplier_name = actualStockInSummarySpuDetail.getSupplier_name();
					actual_supplier_names.add(supplier_name);
					if (!supplier_value_map.containsKey(supplier_name)) {
						msg = String.format("入库商品:%s 对应供应商 %s 预期无数据", spu_id, supplier_name);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}
					BigDecimal tempValue = supplier_value_map.get(supplier_name);
					deviation_value = tempValue.subtract(actualStockInSummarySpuDetail.getValue()).abs();
					if (deviation_value.compareTo(customize_deviation_value) > 0) {
						msg = String.format("入库汇总,按SPU查看,以SPU_ID:%s 过滤信息,供应商:%s 的入库金额与预期的不一致,预期:%s,实际:%s,两值相差:%s",
								spu_id, supplier_name, tempValue, actualStockInSummarySpuDetail.getValue(),
								deviation_value);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

				if (!supplier_value_map.keySet().containsAll(actual_supplier_names)) {
					actual_supplier_names.removeAll(supplier_value_map.keySet());
					msg = String.format("入库汇总,按SPU查看,以SPU_ID:%s 过滤信息,缺少了供应商 " + actual_supplier_names + " 的入库统计信息");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);

				StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "入库汇总,按SPU查看,汇总总计接口调用失败");

				if (total_value.compareTo(stockSummary.getTotal_value()) != 0) {
					msg = String.format("入库汇总,按SPU查看,以SPU_ID:%s 过滤信息,过滤出的商品入库总金额与预期不符,预期:%s,实际:%s", spu_id, total_value,
							stockSummary.getTotal_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "入库汇总,按SPU查看,输入商品ID过滤数据,过滤出的数据与预期不符");
			} else {
				stockSummaryFilterParam.setQ("a");
				stockSummarySpuDetailList = stockSummaryService.inStockSummaryDetailBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummarySpuDetailList, null, "入库汇总,按SPU查看失败");

				StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "入库汇总,汇总总计接口调用失败");
			}
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase10() {
		ReporterCSS.title("测试点: 入库汇总,按分类查看,输入供应商ID过滤数据");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetails = stockSummaryService
					.inStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetails, null, "入库汇总,按分类查看失败");

			if (stockSummaryCategoryDetails.size() > 0) {
				StockSummaryCategoryDetailBean stockSummaryCategoryDetail = NumberUtil
						.roundNumberInList(stockSummaryCategoryDetails);
				String supplier_num = stockSummaryCategoryDetail.getSupplier_num();
				stockSummaryFilterParam.setQ(supplier_num);

				List<StockSummaryCategoryDetailBean> actualStockSummaryCategoryDetails = stockSummaryService
						.inStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(actualStockSummaryCategoryDetails, null, "入库汇总,按分类查看,输入供应商ID过滤数据失败");

				List<StockSummaryCategoryDetailBean> expetedStockSummaryCategoryDetails = stockSummaryCategoryDetails
						.stream().filter(s -> s.getSupplier_num().contains(supplier_num)
								|| s.getSupplier_name().contentEquals(supplier_num))
						.collect(Collectors.toList());

				boolean result = true;
				String msg = null;
				for (StockSummaryCategoryDetailBean expetedStockSummaryCategoryDetail : expetedStockSummaryCategoryDetails) {
					String temp_supplier_num = expetedStockSummaryCategoryDetail.getSupplier_num();
					StockSummaryCategoryDetailBean actualStockSummaryCategoryDetail = actualStockSummaryCategoryDetails
							.stream().filter(s -> s.getSupplier_num().equals(temp_supplier_num)).findAny().orElse(null);
					if (actualStockSummaryCategoryDetail == null) {
						msg = String.format("入库汇总,按分类查看,输入供应商ID: %s过滤数据,没有过滤出供应商 [%s] 的数据", supplier_num,
								expetedStockSummaryCategoryDetail.getSupplier_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (expetedStockSummaryCategoryDetail.getTotal_value()
							.compareTo(actualStockSummaryCategoryDetail.getTotal_value()) != 0) {
						msg = String.format("入库汇总,按分类查看,输入供应商ID: %s过滤数据,过滤出供应商 [%s] 的小计金额与预期不一致,预期:%s,实际:%s",
								supplier_num, expetedStockSummaryCategoryDetail.getSupplier_name(),
								expetedStockSummaryCategoryDetail.getTotal_value(),
								actualStockSummaryCategoryDetail.getTotal_value());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					List<StockSummaryCategoryDetailBean.Category1Detail> expectedCategory1Details = expetedStockSummaryCategoryDetail
							.getCategory1_details();

					List<StockSummaryCategoryDetailBean.Category1Detail> actualCategory1Details = actualStockSummaryCategoryDetail
							.getCategory1_details();

					for (StockSummaryCategoryDetailBean.Category1Detail expectedCategory1Detail : expectedCategory1Details) {
						String category1_name = expectedCategory1Detail.getCategory1_name();
						StockSummaryCategoryDetailBean.Category1Detail actualCategory1Detail = actualCategory1Details
								.stream().filter(d -> d.getCategory1_name().equals(category1_name)).findAny()
								.orElse(null);
						if (actualCategory1Detail == null) {
							msg = String.format("入库汇总,按分类查看,输入供应商ID: %s过滤数据,过滤出供应商 [%s] 的信息中,少了分类 [%s] 的信息",
									supplier_num, expetedStockSummaryCategoryDetail.getSupplier_name(), category1_name);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
							continue;
						}

						if (actualCategory1Detail.getValue().compareTo(expectedCategory1Detail.getValue()) != 0) {
							msg = String.format(
									"入库汇总,按分类查看,输入供应商ID: %s过滤数据,过滤出供应商 [%s] 的信息中,分类 [%s] 对应的入库金额与预期不符,预期:%s,实际:%s",
									supplier_num, expetedStockSummaryCategoryDetail.getSupplier_name(), category1_name,
									expectedCategory1Detail.getValue(), actualCategory1Detail.getValue());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}

				Assert.assertEquals(result, true, "入库汇总,按分类查看,输入供应商ID: %s过滤数据,过滤出的数据与预期不一致");
			} else {
				stockSummaryFilterParam.setQ("a");
				stockSummaryCategoryDetails = stockSummaryService
						.inStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummaryCategoryDetails, null, "入库汇总,按分类查看失败");

				StockSummaryBean stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "入库汇总,按分类查看,汇总总计接口调用失败");
			}
		} catch (Exception e) {
			logger.error("入库汇总,按分类查看遇到错误: ", e);
			Assert.fail("入库汇总,按分类查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase11() {
		ReporterCSS.title("测试点: 入库汇总,按SPU查看,以商品分类过滤统计数据");
		try {
			InStockRecordBean stockInRecord = NumberUtil.roundNumberInList(inStockRecords);
			String category1_id = stockInRecord.getCategory_id_1();
			String category2_id = stockInRecord.getCategory_id_2();

			List<String> category1_ids = Arrays.asList(category1_id);
			List<String> category2_ids = Arrays.asList(category2_id);

			stockSummaryFilterParam.setCategory_id_1(category1_ids);
			stockSummaryFilterParam.setCategory_id_2(category2_ids);

			List<InStockSummarySpuDetailBean> inStockSummarySpuDetails = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(inStockSummarySpuDetails, null, "入库汇总,按SPU查看,以商品分类过滤数据失败");

			Map<String, List<InStockRecordBean>> actualStockInRecordMap = inStockRecords.stream()
					.filter(s -> s.getCategory_id_2().equals(category2_id))
					.collect(Collectors.groupingBy(s -> s.getSpu_id()));

			String msg = null;
			boolean result = true;
			for (InStockSummarySpuDetailBean inStockSummarySpuDetail : inStockSummarySpuDetails) {
				String spu_id = inStockSummarySpuDetail.getSpu_id();
				String supplier_name = inStockSummarySpuDetail.getSupplier_name();

				List<InStockRecordBean> tempStockInRecords = actualStockInRecordMap.get(spu_id);
				BigDecimal amount = new BigDecimal("0");
				BigDecimal value = new BigDecimal("0");
				for (InStockRecordBean tempStockInRecord : tempStockInRecords) {
					if (tempStockInRecord.getSupplier_name().equals(supplier_name)) {
						amount = amount.add(tempStockInRecord.getIn_stock_amount());
						value = value.add(tempStockInRecord.getAll_price());
					}
				}

				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (inStockSummarySpuDetail.getAmount().compareTo(amount) != 0) {
					msg = String.format("入库汇总,按SPU查看,以商品分类[%s-%s]过滤,条目[供应商:%s,商品ID:%s] 统计的入库数与预期不符,预期:%s,实际:%s",
							category1_id, category2_id, supplier_name, spu_id, amount,
							inStockSummarySpuDetail.getAmount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (inStockSummarySpuDetail.getValue().compareTo(value) != 0) {
					msg = String.format("入库汇总,按SPU查看,以商品分类[%s-%s]过滤,条目[供应商:%s,商品ID:%s] 统计的入库金额与预期不符,预期:%s,实际:%s",
							category1_id, category2_id, supplier_name, spu_id, value,
							inStockSummarySpuDetail.getValue());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按SPU查看,以商品分类获取统计总计信息失败");

			BigDecimal total_value = new BigDecimal("0");
			for (String spu_id : actualStockInRecordMap.keySet()) {
				List<InStockRecordBean> tempStockInRecords = actualStockInRecordMap.get(spu_id);
				for (InStockRecordBean tempStockInRecord : tempStockInRecords) {
					total_value = total_value.add(tempStockInRecord.getAll_price());
				}
			}
			total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (stockSummary.getTotal_value().compareTo(total_value) != 0) {
				msg = String.format("入库汇总,按SPU查看,以商品分类[%s-%s]过滤,统计的总入库金额与预期不符,预期:%s,实际:%s", category1_id, category2_id,
						total_value, stockSummary.getTotal_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "入库汇总,按SPU查看,以商品分类过滤数据与预期不符");
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看,以商品分类过滤数据遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看,以商品分类过滤数据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "inStockSummaryTestCase05" })
	public void inStockSummaryTestCase12() {
		ReporterCSS.title("测试点: 入库汇总,按SPU查看,以供应商过滤统计数据");
		try {

			Map<String, String> settleSupplierMap = purchaseTaskService.getPurchaseTaskSettleSuppliers();
			Assert.assertNotEquals(settleSupplierMap, null, "获取供应商列表信息失败");

			InStockRecordBean stockInRecord = NumberUtil.roundNumberInList(inStockRecords);
			String supplier_name = stockInRecord.getSupplier_name();

			String supplier_id = null;
			for (Map.Entry<String, String> entry : settleSupplierMap.entrySet()) {
				if (entry.getValue().equals(supplier_name)) {
					supplier_id = entry.getKey();
					break;
				}
			}

			stockSummaryFilterParam.setSupplier(supplier_id);
			List<InStockSummarySpuDetailBean> inStockSummarySpuDetails = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(inStockSummarySpuDetails, null, "入库汇总,按SPU查看,以供应商过滤统计数据失败");

			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按SPU查看,以供应商过滤统计数据失败");

			List<InStockRecordBean> expectedStockInRecords = inStockRecords.stream()
					.filter(s -> s.getSupplier_name().equals(supplier_name)).collect(Collectors.toList());
			boolean result = true;
			String msg = null;

			BigDecimal total_value = new BigDecimal("0");
			List<String> spu_ids = new ArrayList<String>();
			for (InStockRecordBean tempStockInRecord : expectedStockInRecords) {
				total_value = total_value.add(tempStockInRecord.getAll_price());
				if (!spu_ids.contains(tempStockInRecord.getSpu_id())) {
					spu_ids.add(tempStockInRecord.getSpu_id());
				}
			}
			total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
			if (stockSummary.getSpu_num() != spu_ids.size()) {
				msg = String.format("入库汇总,按SPU查看,以供应商过滤统计数据,统计的入库商品种数与预期不一致,预期:%s,实际:%s", spu_ids.size(),
						stockSummary.getSpu_num());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockSummary.getTotal_value().compareTo(total_value) != 0) {
				msg = String.format("入库汇总,按SPU查看,以供应商过滤统计数据,统计的入库总金额与预期不一致,预期:%s,实际:%s", total_value,
						stockSummary.getTotal_value());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Map<String, List<InStockRecordBean>> spuStockInRecordMap = expectedStockInRecords.stream()
					.collect(Collectors.groupingBy(InStockRecordBean::getSpu_id));

			for (InStockSummarySpuDetailBean inStockSummarySpuDetail : inStockSummarySpuDetails) {
				if (!inStockSummarySpuDetail.getSupplier_name().equals(supplier_name)) {
					msg = String.format("入库汇总,按SPU查看,以供应商[%s]过滤统计数据,结果中出现了供应商 [%s] 的数据", supplier_name,
							inStockSummarySpuDetail.getSupplier_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				String spu_id = inStockSummarySpuDetail.getSpu_id();
				if (spuStockInRecordMap.containsKey(spu_id)) {
					List<InStockRecordBean> inStockRecords = spuStockInRecordMap.get(spu_id);
					BigDecimal amount = new BigDecimal("0");
					BigDecimal value = new BigDecimal("0");
					for (InStockRecordBean sr : inStockRecords) {
						amount = amount.add(sr.getIn_stock_amount());
						value = value.add(sr.getAll_price());
					}
					amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
					value = value.setScale(2, BigDecimal.ROUND_HALF_UP);

					if (amount.compareTo(inStockSummarySpuDetail.getAmount()) != 0) {
						msg = String.format("入库汇总,按SPU查看,以供应商[%s]过滤统计数据,商品 [%s] 的入库数与预期不符,预期:%s,实际:%s", supplier_name,
								spu_id, amount, inStockSummarySpuDetail.getAmount());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (value.compareTo(inStockSummarySpuDetail.getValue()) != 0) {
						msg = String.format("入库汇总,按SPU查看,以供应商[%s]过滤统计数据,商品 [%s] 的入库金额与预期不符,预期:%s,实际:%s", supplier_name,
								spu_id, value, inStockSummarySpuDetail.getValue());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("入库汇总,按SPU查看,以供应商[%s]过滤统计数据,商品 [%s] 的入库记录没有统计", supplier_name, spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "入库汇总,按SPU查看,以供应商过滤数据,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("入库汇总,按SPU查看,以供应商过滤统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按SPU查看,以供应商过滤统计数据遇到错误: ", e);
		}
	}

	@Test
	public void inStockSummaryTestCase13() {
		ReporterCSS.title("测试点: 入库汇总,按分类查看,以供应商过滤统计数据");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetails = stockSummaryService
					.inStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetails, null, "入库汇总,按分类查看,以供应商过滤失败");
			stockSummaryFilterParam.setPage_obj(null);

			if (stockSummaryCategoryDetails.size() > 0) {
				StockSummaryCategoryDetailBean stockSummaryCategoryDetail = NumberUtil
						.roundNumberInList(stockSummaryCategoryDetails);
				String supplier_name = stockSummaryCategoryDetail.getSupplier_name();
				stockSummaryFilterParam.setQ(supplier_name);

				List<StockSummaryCategoryDetailBean> actualStockSummaryCategoryDetails = stockSummaryService
						.inStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(actualStockSummaryCategoryDetails, null, "入库汇总,按分类查看,以供应商名称过滤,详细接口调用失败");

				List<String> temp_supplier_names = actualStockSummaryCategoryDetails.stream()
						.filter(s -> !s.getSupplier_name().contains(supplier_name)
								&& !s.getSupplier_num().contains(supplier_name))
						.map(s -> s.getSupplier_name()).collect(Collectors.toList());
				String msg = null;
				boolean result = true;
				if (temp_supplier_names.size() > 0) {
					msg = String.format("入库汇总,按分类查看,以供应商名称[%s]过滤,过滤出了如下不符合过滤条件的记录,供应商名称列表:  [%s] ", supplier_name,
							temp_supplier_names);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<StockSummaryCategoryDetailBean> expectedStockSummaryCategoryDetails = stockSummaryCategoryDetails
						.stream().filter(s -> s.getSupplier_name().contains(supplier_name)
								|| s.getSupplier_num().contains(supplier_name))
						.collect(Collectors.toList());
				BigDecimal total_value = new BigDecimal("0");
				for (StockSummaryCategoryDetailBean expectedStockSummaryCategoryDetail : expectedStockSummaryCategoryDetails) {
					total_value = total_value.add(expectedStockSummaryCategoryDetail.getTotal_value());
					String supplier_num = expectedStockSummaryCategoryDetail.getSupplier_num();

					StockSummaryCategoryDetailBean actualStockSummaryCategoryDetail = actualStockSummaryCategoryDetails
							.stream().filter(s -> s.getSupplier_num().equals(supplier_num)).findAny().orElse(null);
					if (actualStockSummaryCategoryDetail == null) {
						msg = String.format("入库汇总,按分类查看,以供应商名称[%s]过滤,没有过滤出供应商[%s]对应的数据", supplier_name,
								expectedStockSummaryCategoryDetail.getSupplier_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (actualStockSummaryCategoryDetail.getTotal_value()
							.compareTo(expectedStockSummaryCategoryDetail.getTotal_value()) != 0) {
						msg = String.format("入库汇总,按分类查看,以供应商名称[%s]过滤,条目 [%s] 的金额小计与预期不符,预期:%s,实际:%s", supplier_name,
								expectedStockSummaryCategoryDetail.getSupplier_name(),
								expectedStockSummaryCategoryDetail.getTotal_value(),
								actualStockSummaryCategoryDetail.getTotal_value());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

				stockSummaryFilterParam.setPage_obj(null);
				StockSummaryBean stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "入库汇总,按分类查看,以供应商过滤,总计接口调用失败");

				if (stockSummary.getTotal_value().subtract(total_value).abs()
						.compareTo(customize_deviation_value) > 0) {
					msg = String.format("入库汇总,按分类查看,以供应商名称[%s]过滤,统计的入库总金额与预期不符,预期:%s,实际:%s", supplier_name, total_value,
							stockSummary.getTotal_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "入库汇总,按分类查看,以供应商过滤,过滤出的数据不正确");
			} else {
				stockSummaryFilterParam.setQ("a");
				stockSummaryCategoryDetails = stockSummaryService
						.inStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummaryCategoryDetails, null, "入库汇总,按分类查看,以供应商过滤,详细接口调用失败");

				StockSummaryBean stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "入库汇总,按分类查看,以供应商过滤,总计接口调用失败");
			}
		} catch (Exception e) {
			logger.error("入库汇总,按分类查看,以供应商过滤统计数据遇到错误: ", e);
			Assert.fail("入库汇总,按分类查看,以供应商过滤统计数据遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase01() {
		ReporterCSS.title("测试点: 出库汇总,按SPU查看统计数据");
		try {
			StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "出库汇总,按SPU查看统计数据失败");
		} catch (Exception e) {
			logger.error("出库汇总,按SPU查看统计数据遇到错误: ", e);
			Assert.fail("出库汇总,按SPU查看统计数据遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase02() {
		ReporterCSS.title("测试点: 出库汇总,按SPU查看详细数据");
		try {
			List<OutStockSummarySpuDetailBean> stockSummaryDetailList = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryDetailList, null, "出库汇总,按SPU查看详细数据失败");
		} catch (Exception e) {
			logger.error("出库汇总,按SPU查看详细数据遇到错误: ", e);
			Assert.fail("出库汇总,按SPU查看详细数据遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase03() {
		ReporterCSS.title("测试点: 出库汇总,按分类查看详细数据");
		try {
			StockSummaryBean stockSummary = stockSummaryService.outStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "出库汇总,按分类查看统计数据失败");
		} catch (Exception e) {
			logger.error("出库汇总,按分类查看统计数据遇到错误: ", e);
			Assert.fail("出库汇总,按分类查看统计数据遇到错误: ", e);
		}

	}

	@Test
	public void outStockSummaryTestCase04() {
		ReporterCSS.title("测试点: 出库汇总,按分类查看详细数据");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryDetailList = stockSummaryService
					.outStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryDetailList, null, "出库汇总,按分类查看详细数据失败");
		} catch (Exception e) {
			logger.error("出库汇总,按分类查看统计数据遇到错误: ", e);
			Assert.fail("出库汇总,按分类查看统计数据遇到错误: ", e);
		}

	}

	private List<OutStockRecordBean> stockOutRecords = new ArrayList<OutStockRecordBean>();
	private Map<String, List<OutStockRecordBean>> addressStockOutRecordMap;

	@Test
	public void outStockSummaryTestCase05() {
		ReporterCSS.title("测试点: 查询出库记录,为后面的统计验证做准备");
		try {
			OutStockRecordFilterParam stockRecordParam = new OutStockRecordFilterParam();
			stockRecordParam.setTime_type(1);
			stockRecordParam.setBegin(summary_start_date);
			stockRecordParam.setEnd(summary_end_date);

			int offset = 0;
			int limit = 50;
			stockRecordParam.setLimit(limit);
			List<OutStockRecordBean> tempStockOutRecords = null;
			while (true) {
				stockRecordParam.setOffset(offset);
				tempStockOutRecords = stockRecordService.outStockRecords(stockRecordParam);
				Assert.assertNotEquals(tempStockOutRecords, null, "按出库日期查询出库日志失败");
				stockOutRecords.addAll(tempStockOutRecords);
				if (tempStockOutRecords.size() < limit) {
					break;
				}
				offset += limit;
			}

			ReporterCSS.log(summary_start_date + "--" + summary_end_date + " 的出库记录条目数为" + stockOutRecords.size());

			// 按出库对象分组
			String address_id = null;
			String spu_id = null;
			String key = null;
			addressStockOutRecordMap = new HashMap<String, List<OutStockRecordBean>>();
			for (OutStockRecordBean stockOutRecord : stockOutRecords) {
				if (stockOutRecord.getOrder_id().startsWith("PL")) {
					address_id = stockOutRecord.getAddress_id();
				} else {
					address_id = "0";
				}
				spu_id = stockOutRecord.getSpu_id();
				key = address_id + "_" + spu_id;
				if (addressStockOutRecordMap.containsKey(key)) {
					tempStockOutRecords = addressStockOutRecordMap.get(key);
					tempStockOutRecords.add(stockOutRecord);
					addressStockOutRecordMap.put(key, tempStockOutRecords);
				} else {
					tempStockOutRecords = new ArrayList<OutStockRecordBean>();
					tempStockOutRecords.add(stockOutRecord);
					addressStockOutRecordMap.put(key, tempStockOutRecords);
				}
			}
		} catch (Exception e) {
			logger.error("查询出库记录遇到错误: ", e);
			Assert.fail("查询出库记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "outStockSummaryTestCase05" })
	public void outStockSummaryTestCase06() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按商品查看,验证总计数据是否正确");
		try {
			StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "出库汇总,按SPU查看统计数据失败");

			BigDecimal total_value = new BigDecimal("0");
			List<String> spu_ids = new ArrayList<String>();
			for (OutStockRecordBean stockOutRecord : stockOutRecords) {
				total_value = total_value.add(stockOutRecord.getPrice().multiply(stockOutRecord.getOut_stock_base())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				if (!spu_ids.contains(stockOutRecord.getSpu_id())) {
					spu_ids.add(stockOutRecord.getSpu_id());
				}
			}
			total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
			String msg = null;
			boolean result = true;
			if (stockSummary.getSpu_num() != spu_ids.size()) {
				msg = String.format("出库汇总,统计的出库商品种数与预期不一致,预期:%s,实际:%s", spu_ids.size(), stockSummary.getSpu_num());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 两值相差在1之内,就认为统计正确
			BigDecimal deviation_value = stockSummary.getTotal_value().subtract(total_value).abs();
			if (deviation_value.compareTo(new BigDecimal("1")) > 0) {
				msg = String.format("出库汇总,统计的出库总金额与预期不一致,预期:%s,实际:%s,两值相差:%s", total_value,
						stockSummary.getTotal_value().setScale(2, BigDecimal.ROUND_HALF_UP), deviation_value);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OutStockSummarySpuDetailBean> stockSummarySpuDetails = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummarySpuDetails, null, "出库汇总,按SPU查看,详细信息获取失败");

			List<String> stockOutSummarySpus = new ArrayList<String>();
			BigDecimal stockOutSummarySpuDetailTotalValue = new BigDecimal("0");
			for (OutStockSummarySpuDetailBean stockOutSummarySpuDetail : stockSummarySpuDetails) {
				if (!stockOutSummarySpus.contains(stockOutSummarySpuDetail.getSpu_id())) {
					stockOutSummarySpus.add(stockOutSummarySpuDetail.getSpu_id());
				}
				stockOutSummarySpuDetailTotalValue = stockOutSummarySpuDetailTotalValue
						.add(stockOutSummarySpuDetail.getValue());
			}

			if (stockOutSummarySpus.size() != stockSummary.getSpu_num()) {
				msg = String.format("出库汇总,按商品查看,系统统计的商品种数和列表显示的商品种数不一致,总计:%s,列表:%s", stockSummary.getSpu_num(),
						stockOutSummarySpus.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockOutSummarySpus.size() != stockSummary.getSpu_num()) {
				msg = String.format("出库汇总,按商品查看,系统统计的商品种数和列表显示的商品种数不一致,总计:%s,列表:%s", stockSummary.getSpu_num(),
						stockOutSummarySpus.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			deviation_value = stockOutSummarySpuDetailTotalValue.subtract(stockSummary.getTotal_value()).abs();
			if (deviation_value.compareTo(customize_deviation_value) > 0) {
				msg = String.format("出库汇总,按商品查看,系统统计的出库总金额和列表显示统计的总金额不一致,总计:%s,列表:%s", stockSummary.getTotal_value(),
						stockOutSummarySpuDetailTotalValue);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!(spu_ids.containsAll(stockOutSummarySpus) && stockOutSummarySpus.containsAll(spu_ids))) {
				if (spu_ids.size() >= stockOutSummarySpus.size()) {
					spu_ids.removeAll(stockOutSummarySpus);
					msg = String.format("出库汇总,按商品查看,列表显示的SPU比出库记录记录的SPU少: %s", spu_ids);
				} else {
					stockOutSummarySpus.removeAll(spu_ids);
					msg = String.format("出库汇总,按商品查看,列表显示的SPU比出库记录记录的SPU多: %s", stockOutSummarySpus);
				}
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "查询出库汇总,出库汇总的总计信息与预期不一致");
		} catch (Exception e) {
			logger.error("查询出库汇总,按分类查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按分类查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "outStockSummaryTestCase05" })
	public void outStockSummaryTestCase07() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按商品查看,验证详细数据是否正确");
		try {
			List<OutStockSummarySpuDetailBean> stockSummarySpuDetails = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummarySpuDetails, null, "出库汇总,按SPU查看详细信息失败");

			// 遍历验证
			String msg = null;
			boolean result = true;
			List<OutStockRecordBean> tempStockOutRecords = null;
			OutStockSummarySpuDetailBean stockOutSummarySpuDetail = null;
			for (String key : addressStockOutRecordMap.keySet()) {
				String address_id = key.split("_")[0];
				String spu_id = key.split("_")[1];
				stockOutSummarySpuDetail = stockSummarySpuDetails.stream()
						.filter(a -> a.getAddress_id().equals(address_id) && a.getSpu_id().equals(spu_id)).findAny()
						.orElse(null);
				if (stockOutSummarySpuDetail == null) {
					msg = String.format("出库汇总,按商品查看,没有商户:%s,商品:%s 的条目信息,与预期不符", address_id, spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				tempStockOutRecords = addressStockOutRecordMap.get(key);
				BigDecimal amount = new BigDecimal("0");
				BigDecimal value = new BigDecimal("0");
				for (OutStockRecordBean stockOutRecord : tempStockOutRecords) {
					amount = amount.add(stockOutRecord.getOut_stock_base());
					value = value.add(stockOutRecord.getPrice().multiply(stockOutRecord.getOut_stock_base()));
				}
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (stockOutSummarySpuDetail.getAmount().compareTo(amount) != 0) {
					msg = String.format("出库汇总,按商品查看,商户:%s,商品:%s 的条目信息,统计的出库数与预期不符,预期:%s,实际:%s", address_id, spu_id,
							amount, stockOutSummarySpuDetail.getAmount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				// 两值相差在0.5之内,就认为在误差范围内
				BigDecimal deviation_value = stockOutSummarySpuDetail.getValue().subtract(value).abs();
				if (deviation_value.compareTo(customize_deviation_value) > 0) {
					msg = String.format("出库汇总,按商品查看,商户:%s,商品:%s 的条目信息,统计的出库金额与预期不符,预期:%s,实际:%s,两值相差:%s", address_id,
							spu_id, value, stockOutSummarySpuDetail.getValue(), deviation_value);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "出库汇总,按商品查看,统计的条目信息与预期不符");
		} catch (Exception e) {
			logger.error("查询出库汇总,按分类查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按分类查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "outStockSummaryTestCase05" })
	public void outStockSummaryTestCase08() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按分类查看,验证详细数据是否正确");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetails = stockSummaryService
					.outStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetails, null, "出库汇总,按SPU查看详细信息失败");

			Map<String, List<OutStockRecordBean>> stockOutRecordMap = stockOutRecords.stream()
					.collect(Collectors.groupingBy(OutStockRecordBean::getAddress_id));

			List<OutStockRecordBean> tempStockOutRecords = null;
			BigDecimal total_value = null;
			StockSummaryCategoryDetailBean stockSummaryCategoryDetail = null;
			String msg = null;
			boolean result = true;
			for (String address_id : stockOutRecordMap.keySet()) {
				tempStockOutRecords = stockOutRecordMap.get(address_id);
				total_value = new BigDecimal("0");
				for (OutStockRecordBean stockOutRecord : tempStockOutRecords) {
					total_value = total_value
							.add(stockOutRecord.getOut_stock_base().multiply(stockOutRecord.getPrice()));
				}
				stockSummaryCategoryDetail = stockSummaryCategoryDetails.stream()
						.filter(s -> s.getAddress_id().equals(address_id)).findAny().orElse(null);
				if (stockSummaryCategoryDetail == null) {
					msg = String.format("出库汇总,按分类统计,没有出库对象 [%s] 的记录,与预期不符", address_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
				total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);

				// 两值相差在0.5之内,就认为在误差范围内
				BigDecimal deviation_value = total_value.subtract(stockSummaryCategoryDetail.getTotal_value()).abs();
				if (deviation_value.compareTo(customize_deviation_value) > 0) {
					msg = String.format("出库汇总,按分类统计,出库对象 [%s] 的出库金额小计与预期不符,预期:%s,实际:%s,两值相差:%s", address_id,
							total_value,
							stockSummaryCategoryDetail.getTotal_value().setScale(2, BigDecimal.ROUND_HALF_UP),
							deviation_value);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<StockSummaryCategoryDetailBean.Category1Detail> category1Details = stockSummaryCategoryDetail
						.getCategory1_details();

				Map<String, List<OutStockRecordBean>> category1StockOutRecordMap = tempStockOutRecords.stream()
						.collect(Collectors.groupingBy(OutStockRecordBean::getCategory_name_1));
				for (String category1_name : category1StockOutRecordMap.keySet()) {
					StockSummaryCategoryDetailBean.Category1Detail category1Detail = category1Details.stream()
							.filter(c -> c.getCategory1_name().equals(category1_name)).findAny().orElse(null);
					if (category1Detail == null) {
						msg = String.format("出库汇总,按分类统计,出库对象 [%s] 中没有分类 [%s] 的统计,与预期不符", address_id, category1_name);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					List<OutStockRecordBean> category1StockOutRecords = category1StockOutRecordMap.get(category1_name);
					total_value = new BigDecimal("0");
					for (OutStockRecordBean stockOutRecord : category1StockOutRecords) {
						total_value = total_value
								.add(stockOutRecord.getOut_stock_base().multiply(stockOutRecord.getPrice()));
					}
					// 两值相差在0.5之内,就认为在误差范围内
					deviation_value = total_value.subtract(category1Detail.getValue()).abs();
					if (deviation_value.compareTo(customize_deviation_value) > 1) {
						msg = String.format("出库汇总,按分类统计,出库对象 [%s] 分类 [%s] 统计的出库金额与预期不符,预期:%s,实际:%s,两值相差:%s", address_id,
								category1_name, total_value,
								category1Detail.getValue().setScale(2, BigDecimal.ROUND_HALF_UP), deviation_value);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "出库汇总,按分类统计,统计的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("查询出库汇总,按分类查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按分类查看遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase09() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按SPU查看,以商品ID过滤数据");
		try {
			List<OutStockSummarySpuDetailBean> stockOutSummarySpuDetails = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockOutSummarySpuDetails, null, "查询出库汇总,按SPU查看失败");

			if (stockOutSummarySpuDetails.size() > 0) {
				OutStockSummarySpuDetailBean stockOutSummarySpuDetail = NumberUtil
						.roundNumberInList(stockOutSummarySpuDetails);
				String spu_id = stockOutSummarySpuDetail.getSpu_id();

				stockSummaryFilterParam.setPage_obj(null);
				stockSummaryFilterParam.setQ(spu_id);
				List<OutStockSummarySpuDetailBean> actualStockOutSummarySpuDetails = stockSummaryService
						.outStockSummaryDetailBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockOutSummarySpuDetails, null, "查询出库汇总,按SPU查看,以SPU_ID过滤获取详细数据失败");

				List<OutStockSummarySpuDetailBean> expectedStockOutSummarySpuDetails = stockOutSummarySpuDetails
						.stream().filter(s -> s.getSpu_id().contains(spu_id) || s.getSpu_name().contains(spu_id))
						.collect(Collectors.toList());

				String msg = null;
				boolean result = true;
				for (OutStockSummarySpuDetailBean expectedStockOutSummarySpuDetail : expectedStockOutSummarySpuDetails) {
					String address_id = expectedStockOutSummarySpuDetail.getAddress_id();
					String temp_spu_id = expectedStockOutSummarySpuDetail.getSpu_id();
					OutStockSummarySpuDetailBean actualStockOutSummarySpuDetail = actualStockOutSummarySpuDetails
							.stream()
							.filter(s -> s.getAddress_id().equals(address_id) && s.getSpu_id().equals(temp_spu_id))
							.findAny().orElse(null);
					if (actualStockOutSummarySpuDetail == null) {
						msg = String.format("查询出库汇总,按SPU查看,以SPU_ID [%s] 过滤,没有过滤出商户:%s,SPU_ID:%s 的信息", spu_id,
								address_id, temp_spu_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (expectedStockOutSummarySpuDetail.getAmount()
							.compareTo(actualStockOutSummarySpuDetail.getAmount()) != 0) {
						msg = String.format(
								"查询出库汇总,按SPU查看,以SPU_ID [%s] 过滤,过滤出商户:%s,SPU_ID:%s 的信息的出库数与预期不一致,预期:%s,实际:%s", spu_id,
								address_id, temp_spu_id, expectedStockOutSummarySpuDetail.getAmount(),
								actualStockOutSummarySpuDetail.getAmount());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (expectedStockOutSummarySpuDetail.getValue()
							.compareTo(actualStockOutSummarySpuDetail.getValue()) != 0) {
						msg = String.format(
								"查询出库汇总,按SPU查看,以SPU_ID [%s] 过滤,过滤出商户:%s,SPU_ID:%s 的信息的出库金额与预期不一致,预期:%s,实际:%s", spu_id,
								address_id, temp_spu_id, expectedStockOutSummarySpuDetail.getValue(),
								actualStockOutSummarySpuDetail.getValue());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

				stockSummaryFilterParam.setPage_obj(null);
				StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "查询出库汇总,按SPU查看,获取总计信息失败");

				BigDecimal total_value = new BigDecimal("0");
				List<String> spu_ids = new ArrayList<String>();
				for (OutStockSummarySpuDetailBean sd : expectedStockOutSummarySpuDetails) {
					total_value = total_value.add(sd.getValue());
					if (!spu_ids.contains(sd.getSpu_id())) {
						spu_ids.add(sd.getSpu_id());
					}
				}
				total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);

				if (total_value.compareTo(stockSummary.getTotal_value()) != 0) {
					msg = String.format("查询出库汇总,按SPU查看,以SPU_ID [%s] 过滤,过滤出的出库总金额与预期不一致,预期:%s,实际:%s", spu_id,
							total_value, stockSummary.getTotal_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (spu_ids.size() != stockSummary.getSpu_num()) {
					msg = String.format("查询出库汇总,按SPU查看,以SPU_ID [%s] 过滤,过滤出的出库商品种数与预期不一致,预期:%s,实际:%s", spu_id,
							spu_ids.size(), stockSummary.getSpu_num());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "查询出库汇总,按SPU查看,以SPU_ID [" + spu_id + "]过滤,过滤出的信息与预期不符");
			} else {
				stockSummaryFilterParam.setQ("a");
				stockOutSummarySpuDetails = stockSummaryService.outStockSummaryDetailBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockOutSummarySpuDetails, null, "查询出库汇总,按SPU查看,获取详细数据失败");

				StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "查询出库汇总,按SPU查看,获取总计信息失败");
			}
		} catch (Exception e) {
			logger.error("查询出库汇总,按SPU查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按SPU查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "outStockSummaryTestCase05")
	public void outStockSummaryTestCase10() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按SPU查看,以商品分类过滤数据");
		try {
			Assert.assertEquals(stockOutRecords.size() > 0, true,
					summary_start_date + "——" + summary_end_date + "没有进行出库操作,无法进行统计");
			OutStockRecordBean stockOutRecord = NumberUtil.roundNumberInList(stockOutRecords);
			String category1_id = stockOutRecord.getCategory_id_1();
			String category2_id = stockOutRecord.getCategory_id_2();

			List<String> category1_ids = Arrays.asList(category1_id);
			List<String> category2_ids = Arrays.asList(category2_id);

			stockSummaryFilterParam.setCategory_id_1(category1_ids);
			stockSummaryFilterParam.setCategory_id_2(category2_ids);

			List<OutStockSummarySpuDetailBean> stockOutSummarySpuDetails = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockOutSummarySpuDetails, null, "查询出库汇总,按SPU查看,以商品分类过滤数据失败");

			List<OutStockRecordBean> targetStockOutRecords = stockOutRecords.stream()
					.filter(s -> s.getCategory_id_2().equals(category2_id)).collect(Collectors.toList());

			// 预期数据统计分组
			Map<String, List<OutStockRecordBean>> stockOutRecordsMap = new HashMap<String, List<OutStockRecordBean>>();
			String address_id = null;
			String spu_id = null;
			String key = null;
			List<OutStockRecordBean> tempStockOutRecords = null;
			BigDecimal total_value = new BigDecimal("0"); // 用作总计对比
			for (OutStockRecordBean targetStockOutRecord : targetStockOutRecords) {
				total_value = total_value
						.add(targetStockOutRecord.getOut_stock_base().multiply(targetStockOutRecord.getPrice()));
				address_id = targetStockOutRecord.getAddress_id();
				if (address_id == null) {
					address_id = "0";
				}
				spu_id = targetStockOutRecord.getSpu_id();
				key = address_id + "_" + spu_id;
				if (stockOutRecordsMap.containsKey(key)) {
					tempStockOutRecords = stockOutRecordsMap.get(key);
					tempStockOutRecords.add(targetStockOutRecord);
				} else {
					tempStockOutRecords = new ArrayList<OutStockRecordBean>();
					tempStockOutRecords.add(targetStockOutRecord);
					stockOutRecordsMap.put(key, tempStockOutRecords);
				}
			}

			String msg = null;
			boolean result = true;
			for (Entry<String, List<OutStockRecordBean>> sl : stockOutRecordsMap.entrySet()) {
				key = sl.getKey();
				String temp_address_id = key.split("_")[0];
				String temp_spu_id = key.split("_")[1];

				OutStockSummarySpuDetailBean stockOutSummarySpuDetail = stockOutSummarySpuDetails.stream()
						.filter(s -> s.getAddress_id().equals(temp_address_id) && s.getSpu_id().equals(temp_spu_id))
						.findAny().orElse(null);

				if (stockOutSummarySpuDetail == null) {
					msg = String.format("查询出库汇总,按SPU查看,以商品分类[%s-%s]过滤,没有过滤出商户ID: %s,商品ID: %s的数据", category1_id,
							category2_id, temp_address_id, temp_spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				tempStockOutRecords = sl.getValue();
				BigDecimal amount = new BigDecimal("0");
				BigDecimal value = new BigDecimal("0");
				for (OutStockRecordBean sr : tempStockOutRecords) {
					amount = amount.add(sr.getOut_stock_base());
					value = value.add(sr.getOut_stock_base().multiply(sr.getPrice()));
				}
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);

				if (stockOutSummarySpuDetail.getAmount().compareTo(amount) != 0) {
					msg = String.format("查询出库汇总,按SPU查看,以商品分类[%s-%s]过滤,过滤出商户ID: %s,商品ID: %s的数据对应的出库数与预期不一致,预期:%s,实际:%s",
							category1_id, category2_id, temp_address_id, temp_spu_id, amount,
							stockOutSummarySpuDetail.getAmount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				// 两值相差在0.5之内,就认为在误差范围内
				BigDecimal deviation_value = stockOutSummarySpuDetail.getValue().subtract(value).abs();
				if (deviation_value.compareTo(customize_deviation_value) > 0) {
					msg = String.format(
							"查询出库汇总,按SPU查看,以商品分类[%s-%s]过滤,过滤出商户ID: %s,商品ID: %s的数据对应的出库金额与预期不一致,预期:%s,实际:%s,两值相差:%s",
							category1_id, category2_id, temp_address_id, temp_spu_id, value,
							stockOutSummarySpuDetail.getValue(), deviation_value);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			stockSummaryFilterParam.setPage_obj(null);
			StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "查询出库汇总,按SPU查看,以商品分类过滤数据,总计统计失败");
			BigDecimal deviation_value = total_value.subtract(stockSummary.getTotal_value()).abs();

			if (deviation_value.compareTo(customize_deviation_value) > 0) {
				msg = String.format("查询出库汇总,按SPU查看,以商品分类[%s-%s]过滤,过滤后的出库总金额预期不一致,预期:%s,实际:%s,两值相差:%s", category1_id,
						category2_id, total_value, stockSummary.getTotal_value(), deviation_value);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "查询出库汇总,按SPU查看,以商品分类过滤数据,结果与预期不符");
		} catch (Exception e) {
			logger.error("查询出库汇总,按SPU查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按SPU查看遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "outStockSummaryTestCase05")
	public void outStockSummaryTestCase11() {
		ReporterCSS.title("测试点: 查询" + summary_start_date + "——" + summary_end_date + "出库汇总,按SPU查看,以商户过滤数据");
		try {
			Assert.assertEquals(stockOutRecords.size() > 0, true,
					summary_start_date + "——" + summary_end_date + "没有进行出库操作,无法进行统计");

			OutStockRecordBean stockOutRecord = NumberUtil.roundNumberInList(stockOutRecords);
			String address_id = stockOutRecord.getAddress_id();
			if (address_id == null) {
				address_id = "0";
				stockOutRecord.setAddress_id(address_id);
			}

			stockSummaryFilterParam.setRestaurant_id(address_id);

			List<OutStockSummarySpuDetailBean> stockOutSummarySpuDetails = stockSummaryService
					.outStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockOutSummarySpuDetails, null, "查询出库汇总,按SPU查看,以商户过滤数据失败");

			String msg = null;
			boolean result = true;

			List<String> address_ids = stockOutSummarySpuDetails.stream()
					.filter(s -> !s.getAddress_id().equals(stockOutRecord.getAddress_id())).map(s -> s.getAddress_id())
					.collect(Collectors.toList());
			if (address_ids.size() > 0) {
				msg = String.format("查询%s-%s的出库汇总,按SPU查看,以商户[%s]过滤,过滤出了如下不符合的商户%s数据", summary_start_date,
						summary_end_date, address_id, address_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 预期数据过滤
			List<OutStockRecordBean> targetStockOutRecords = null;
			if (address_id.equals("0")) {
				targetStockOutRecords = stockOutRecords.stream().filter(s -> s.getAddress_id() == null)
						.collect(Collectors.toList());
			} else {
				targetStockOutRecords = stockOutRecords.stream()
						.filter(s -> s.getAddress_id().equals(stockOutRecord.getAddress_id()))
						.collect(Collectors.toList());
			}

			Map<String, List<OutStockRecordBean>> stockOutRecordsMap = targetStockOutRecords.stream()
					.collect(Collectors.groupingBy(OutStockRecordBean::getSpu_id));

			for (Entry<String, List<OutStockRecordBean>> sor : stockOutRecordsMap.entrySet()) {
				String spu_id = sor.getKey();

				OutStockSummarySpuDetailBean stockOutSummarySpuDetail = stockOutSummarySpuDetails.stream()
						.filter(s -> s.getSpu_id().equals(spu_id)).findAny().orElse(null);
				if (stockOutSummarySpuDetail == null) {
					msg = String.format("查询%s-%s的出库汇总,按SPU查看,以商户[%s]过滤,商品[%s] 没有统计出数据", summary_start_date,
							summary_end_date, address_id, spu_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				List<OutStockRecordBean> tempStockOutRecords = sor.getValue();
				BigDecimal value = new BigDecimal("0");
				BigDecimal amount = new BigDecimal("0");
				for (OutStockRecordBean s : tempStockOutRecords) {
					amount = amount.add(s.getOut_stock_base());
					value = value.add(s.getOut_stock_base().multiply(s.getPrice()));
				}

				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
				amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

				if (stockOutSummarySpuDetail.getAmount().compareTo(amount) != 0) {
					msg = String.format("查询%s-%s的出库汇总,按SPU查看,以商户[%s]过滤,商品[%s] 统计的出库数与预期不符,预期:%s,实际:%s",
							summary_start_date, summary_end_date, address_id, spu_id, amount,
							stockOutSummarySpuDetail.getAmount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				BigDecimal deviation_value = stockOutSummarySpuDetail.getValue().subtract(value).abs();
				if (deviation_value.compareTo(customize_deviation_value) > 0) {
					msg = String.format("查询%s-%s的出库汇总,按SPU查看,以商户[%s]过滤,商品[%s]统计的出库金额与预期不符,预期:%s,实际:%s,两值相差:%s",
							summary_start_date, summary_end_date, address_id, spu_id, value,
							stockOutSummarySpuDetail.getValue(), deviation_value);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			stockSummaryFilterParam.setPage_obj(null);
			StockSummaryBean stockSummary = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "出库汇总,按SPU查看,以商户过滤,总计接口调用失败");

			Assert.assertEquals(result, true, "出库汇总,按SPU查看,以商户过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("查询出库汇总,按SPU查看遇到错误: ", e);
			Assert.fail("查询出库汇总,按SPU查看遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase12() {
		ReporterCSS.title("测试点: 出库汇总,按分类查看,输入商户ID过滤数据");
		try {
			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetails = stockSummaryService
					.outStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetails, null, "出库汇总,按分类查看,输入商户ID过滤数据失败");

			if (stockSummaryCategoryDetails.size() > 0) {
				StockSummaryCategoryDetailBean stockSummaryCategoryDetail = NumberUtil
						.roundNumberInList(stockSummaryCategoryDetails);
				String address_id = stockSummaryCategoryDetail.getAddress_id();
				String q = address_id;
				if (q.equals("0")) {
					q = "S000000";
				}

				stockSummaryFilterParam.setPage_obj(null);
				stockSummaryFilterParam.setQ(q);
				List<StockSummaryCategoryDetailBean> actualStockSummaryCategoryDetails = stockSummaryService
						.outStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(actualStockSummaryCategoryDetails, null, "出库汇总,按分类查看,输入商户ID,过滤失败");

				List<String> address_ids = actualStockSummaryCategoryDetails.stream().filter(
						s -> !s.getAddress_id().contains(address_id) && !s.getAddress_name().contains(address_id))
						.map(s -> s.getAddress_id()).collect(Collectors.toList());
				String msg = null;
				boolean result = true;
				if (address_ids.size() > 0) {
					msg = String.format("出库汇总,按分类查看,输入商户ID [%s]过滤数据,过滤出了如下不符合 %s 的数据", address_id, address_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<StockSummaryCategoryDetailBean> expectedStockSummaryCategoryDetails = stockSummaryCategoryDetails
						.stream()
						.filter(s -> s.getAddress_id().contains(address_id) || s.getAddress_name().contains(address_id))
						.collect(Collectors.toList());

				BigDecimal total_value = new BigDecimal("0");
				for (StockSummaryCategoryDetailBean expectedStockSummaryCategoryDetail : expectedStockSummaryCategoryDetails) {
					total_value = total_value.add(expectedStockSummaryCategoryDetail.getTotal_value());
					String temp_address_id = expectedStockSummaryCategoryDetail.getAddress_id();
					StockSummaryCategoryDetailBean actualStockSummaryCategoryDetail = actualStockSummaryCategoryDetails
							.stream().filter(s -> s.getAddress_id().equals(temp_address_id)).findAny().orElse(null);
					if (actualStockSummaryCategoryDetail == null) {
						msg = String.format("出库汇总,按分类查看,输入商户ID [%s]过滤数据,商户 [%s]的数据没有过滤出来", address_id, address_id,
								temp_address_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (actualStockSummaryCategoryDetail.getTotal_value()
							.compareTo(expectedStockSummaryCategoryDetail.getTotal_value()) != 0) {
						msg = String.format("出库汇总,按分类查看,输入商户ID [%s]过滤数据,商户 [%s]的数据对应的小计与预期不符,预期:%s,实际:%s", address_id,
								address_id, expectedStockSummaryCategoryDetail.getTotal_value(),
								actualStockSummaryCategoryDetail.getTotal_value());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

				stockSummaryFilterParam.setPage_obj(null);
				StockSummaryBean stockSummary = stockSummaryService.outStockSummaryByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "出库汇总,按分类查看,输入商户ID过滤数据,过滤出错");

				total_value = total_value.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (stockSummary.getTotal_value().compareTo(total_value) != 0) {
					msg = String.format("出库汇总,按分类查看,输入商户ID [%s]过滤数据,统计的出库总金额与预期不符,预期:%s,实际:%s", address_id, total_value,
							stockSummary.getTotal_value());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "出库汇总,按分类查看,输入商户ID过滤数据,过滤出的数据与预期不符");
			} else {
				stockSummaryFilterParam.setQ("a");

				stockSummaryCategoryDetails = stockSummaryService
						.outStockSummaryDetailByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummaryCategoryDetails, null, "出库汇总,按分类查看,输入商户ID过滤数据失败");

				StockSummaryBean stockSummary = stockSummaryService.outStockSummaryByCategory(stockSummaryFilterParam);
				Assert.assertNotEquals(stockSummary, null, "出库汇总,按分类查看,输入商户ID过滤数据,过滤出错");
			}
		} catch (Exception e) {
			logger.error("出库汇总,按分类查看,输入商户ID过滤数据遇到错误: ", e);
			Assert.fail("出库汇总,按分类查看,输入商户ID过滤数据遇到错误: ", e);
		}
	}

	@Test
	public void outStockSummaryTestCase13() {
		ReporterCSS.title("测试点: 出库统计汇总,分类统计和按商品统计对比");
		try {
			StockSummaryBean stockSummaryBySpu = stockSummaryService.outStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryBySpu, null, "出库统计按SPU统计失败");

			StockSummaryBean stockSummaryByCategory = stockSummaryService
					.outStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryByCategory, null, "出库统计按分类统计失败");

			Assert.assertEquals(stockSummaryBySpu.getTotal_value(), stockSummaryByCategory.getTotal_value(),
					"出库统计按分类统计和按SPU统计的数值不一致");
		} catch (Exception e) {
			logger.error("出库汇总遇到错误: ", e);
			Assert.fail("出库汇总遇到错误: ", e);
		}
	}

	@Test
	public void exportInStockSummaryTestCase01() {
		ReporterCSS.title("测试点: 导出入库汇总,按SPU统计的数据");
		try {
			String task_id = stockSummaryService.exportInStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(task_id, null, "导出入库汇总,按SPU统计的数据,异步任务创建失败");
			boolean reuslt = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "成功");
			Assert.assertEquals(reuslt, true, "导出入库汇总,按SPU统计,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出入库汇总,按SPU统计的数据遇到错误: ", e);
			Assert.fail("导出入库汇总,按SPU统计的数据遇到错误: ", e);
		}
	}

	@Test
	public void exportInStockSummaryTestCase02() {
		ReporterCSS.title("测试点: 导出入库汇总,按分类统计的数据");
		try {
			String task_id = stockSummaryService.exportInStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(task_id, null, "导出入库汇总,按分类统计的数据,异步任务创建失败");
			boolean reuslt = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "成功");
			Assert.assertEquals(reuslt, true, "导出入库汇总,按分类统计,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出入库汇总,按分类统计的数据遇到错误: ", e);
			Assert.fail("导出入库汇总,按分类统计的数据遇到错误: ", e);
		}
	}

	@Test
	public void exportOutStockSummaryTestCase01() {
		ReporterCSS.title("测试点: 导出出库汇总,按SPU统计的数据");
		try {
			String task_id = stockSummaryService.exportOutStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(task_id, null, "导出出库汇总,按SPU统计的数据,异步任务创建失败");
			boolean reuslt = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "成功");
			Assert.assertEquals(reuslt, true, "导出出库汇总,按SPU统计,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出出库汇总,按SPU统计的数据遇到错误: ", e);
			Assert.fail("导出出库汇总,按SPU统计的数据遇到错误: ", e);
		}
	}

	@Test
	public void exportOutStockSummaryTestCase02() {
		ReporterCSS.title("测试点: 导出入库汇总,按分类统计的数据");
		try {
			String task_id = stockSummaryService.exportOutStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(task_id, null, "导出库库汇总,按分类统计的数据,异步任务创建失败");
			boolean reuslt = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "成功");
			Assert.assertEquals(reuslt, true, "导出出库汇总,按分类统计,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出出库汇总,按分类统计的数据遇到错误: ", e);
			Assert.fail("导出出库汇总,按分类统计的数据遇到错误: ", e);
		}
	}

}
