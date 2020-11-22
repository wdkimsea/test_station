package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.bean.invoicing.SplitPlanBean;
import cn.guanmai.station.bean.invoicing.SplitPlanDetailBean;
import cn.guanmai.station.bean.invoicing.SplitSheetBean;
import cn.guanmai.station.bean.invoicing.SplitSheetDetailBean;
import cn.guanmai.station.bean.invoicing.SplitStockInRecordBean;
import cn.guanmai.station.bean.invoicing.SplitStockOutRecordBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.param.SplitPlanFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetFiterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.SplitServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.SplitService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月30日 下午4:25:06
 * @description:
 * @version: 1.0
 */

public class SplitSheetTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SplitPlanTest.class);

	private SplitService splitService;
	private StockCheckService stockCheckService;
	private StockRecordService stockRecordService;
	private CategoryService categoryService;
	private LoginUserInfoService loginUserInfoService;
	private InitDataBean initData;
	private String spu_id;
	private String split_plan1_id;
	private int split_plan1_version;
	private String split_plan2_id;
	private int split_plan2_version;
	private int stock_type;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		splitService = new SplitServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		initData = getInitData();

		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号相关信息失败");

			JSONArray permissions = loginUserInfo.getUser_permission();
			Assert.assertEquals(permissions.contains("add_split_plan"), true, "登录账号没有新建分切方案的权限");

			SpuBean spu = initData.getSpu();
			spu_id = spu.getId();
			String name = spu.getName();

			// 进销存类型
			stock_type = loginUserInfo.getStock_method();
			if (stock_type == 1) {
				// 加权平均-把分割SPU库存盘点为100
				boolean result = stockCheckService.editSpuStock(spu_id, new BigDecimal("100"), "分切盘点");
				Assert.assertEquals(result, true, "SPU库存盘点失败");
			} else {
				// 先进先出
				StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
				stockCheckFilterParam.setOffset(0);
				stockCheckFilterParam.setLimit(10);
				stockCheckFilterParam.setText(spu_id);

				List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(stockCheckFilterParam);
				Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

				SpuStockBean spuStock = stockCheckList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(spuStock, null, "库存盘点,没有找到SPU " + spu_id + " 的库存记录");

				// 先判断库存总数是否大于等于100
				if (spuStock.getRemain().compareTo(new BigDecimal("100")) < 0) {
					List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
					Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");
					// 不足的话查询对应的批次信息,有批次就进行批次盘点
					if (stockBatchList.size() > 0) {
						// 需要增加的批次量数量
						BigDecimal add_stock = spuStock.getRemain().subtract(new BigDecimal("100")).abs();
						StockBatchBean stockBatch = NumberUtil.roundNumberInList(stockBatchList);
						BigDecimal batchRemain = stockBatch.getRemain();
						BigDecimal edit_stock = batchRemain.add(add_stock);
						if (edit_stock.compareTo(new BigDecimal("0")) < 0) {
							// 如果批次加上了增幅还是小于0,那么直接把批次库存直接盘点为0
							boolean result = stockCheckService.editBatchStock(stockBatch.getBatch_number(),
									new BigDecimal("0"), "批次盘点");
							Assert.assertEquals(result, true, "批次盘点失败");
						} else {
							boolean result = stockCheckService.editBatchStock(stockBatch.getBatch_number(), edit_stock,
									"批次盘点");
							Assert.assertEquals(result, true, "批次盘点失败");
						}
					} else {
						// 如果没有批次则需要进行入库操作
						InStockTool stockInTool = new InStockTool(headers);
						SupplierDetailBean supplier = initData.getSupplier();
						String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(supplier.getId(),
								new String[] { initData.getSpu().getName() });
						Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

						stockBatchList = stockCheckService.searchStockBatch(spu_id, 0, 10);
						Assert.assertNotEquals(stockBatchList, null, "获取商品 " + spu_id + " 对应的批次列表信息失败");

						Assert.assertEquals(stockBatchList.size() > 0, true, "SPU商品 " + spu_id + "没有对应的入库批次");

						StockBatchBean stockBatch = NumberUtil.roundNumberInList(stockBatchList);
						boolean result = stockCheckService.editBatchStock(stockBatch.getBatch_number(),
								new BigDecimal("100"), "批次盘点");
						Assert.assertEquals(result, true, "批次盘点失败");
					}
				}
			}
			String split_name1 = name + "(切割分成自己)";
			String remark1 = "自动化使用到的分切方案,勿删除、勿修改，谢谢";

			SplitPlanFilterParam splitPlanFilterParam = new SplitPlanFilterParam();
			splitPlanFilterParam.setQ(split_name1);

			List<SplitPlanBean> splitPlans = splitService.searchSplitPlan(splitPlanFilterParam);
			Assert.assertNotEquals(splitPlans, null, "搜索过滤分割分案失败");

			SplitPlanBean splitPlan = splitPlans.stream()
					.filter(s -> s.getName().equals(split_name1) && s.getRemark().equals(remark1)).findAny()
					.orElse(null);

			SplitPlanParam splitPlanParam = new SplitPlanParam();
			splitPlanParam.setName(split_name1);
			splitPlanParam.setRemark(remark1);
			splitPlanParam.setSource_spu_id(spu_id);
			splitPlanParam.setIs_deleted(false);

			List<SplitPlanParam.GainSpu> gainSpus = new ArrayList<SplitPlanParam.GainSpu>();
			SplitPlanParam.GainSpu gainSpu = splitPlanParam.new GainSpu();
			gainSpu = splitPlanParam.new GainSpu();
			gainSpu.setSpu_id(spu_id);
			gainSpu.setSplit_ratio(new BigDecimal("0.8"));
			gainSpus.add(gainSpu);

			splitPlanParam.setGain_spus(gainSpus);

			if (splitPlan == null) {
				split_plan1_id = splitService.createSplitPlan(splitPlanParam);
				Assert.assertNotEquals(split_plan1_id, null, "新建分割方案失败");
				split_plan1_version = 1;
			} else {
				split_plan1_id = splitPlan.getId();
				splitPlanParam.setId(split_plan1_id);
				splitPlanParam.setVersion(splitPlan.getVersion());
				split_plan1_version = splitPlan.getVersion() + 1;
				boolean result = splitService.updateSplitPlan(splitPlanParam);
				Assert.assertEquals(result, true, "修改分切方案失败");
			}

			// 第二个分切方案
			String split_name2 = name + "(切割分成其他)";
			String remark2 = "自动化使用到的分切方案,勿删除、勿修改，谢谢";

			splitPlanFilterParam = new SplitPlanFilterParam();
			splitPlanFilterParam.setQ(split_name2);

			splitPlans = splitService.searchSplitPlan(splitPlanFilterParam);
			Assert.assertNotEquals(splitPlans, null, "搜索过滤分割分案失败");

			splitPlan = splitPlans.stream()
					.filter(s -> s.getName().equals(split_name2) && s.getRemark().equals(remark2)).findAny()
					.orElse(null);

			splitPlanParam = new SplitPlanParam();
			splitPlanParam.setName(split_name2);
			splitPlanParam.setRemark(remark2);
			splitPlanParam.setSource_spu_id(spu_id);
			splitPlanParam.setIs_deleted(false);

			gainSpus = new ArrayList<SplitPlanParam.GainSpu>();

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(50);
			spuIndexFilterParam.setQ("a");
			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "新建分割方案,搜索过滤获得品失败");

			spuIndexList = spuIndexList.stream().filter(s -> s.getStd_unit_name().equals(spu.getStd_unit_name()))
					.collect(Collectors.toList());

			spuIndexList = NumberUtil.roundNumberInList(spuIndexList, 3);
			Assert.assertEquals(spuIndexList.size() >= 2, true, "商品库没有找到合适的分割获得品");
			BigDecimal split_ratio = new BigDecimal("1").divide(new BigDecimal(String.valueOf(spuIndexList.size())), 1,
					BigDecimal.ROUND_HALF_DOWN);

			for (SpuIndexBean spuIndex : spuIndexList) {
				if (spuIndex.getStd_unit_name().equals(spu.getStd_unit_name())) {
					gainSpu = splitPlanParam.new GainSpu();
					gainSpu.setSpu_id(spuIndex.getSpu_id());
					gainSpu.setSplit_ratio(split_ratio);
					gainSpus.add(gainSpu);
				}
			}
			splitPlanParam.setGain_spus(gainSpus);

			if (splitPlan == null) {
				split_plan2_id = splitService.createSplitPlan(splitPlanParam);
				Assert.assertNotEquals(split_plan2_id, null, "新建分割方案失败");
				split_plan2_version = 1;
			} else {
				split_plan2_id = splitPlan.getId();
				splitPlanParam.setId(split_plan2_id);
				splitPlanParam.setVersion(splitPlan.getVersion());
				split_plan2_version = splitPlan.getVersion() + 1;
				logger.info(split_plan2_version + "");
				boolean result = splitService.updateSplitPlan(splitPlanParam);
				Assert.assertEquals(result, true, "修改分切方案失败");
			}
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase01() {
		ReporterCSS.title("测试点: 新建分割单据,商品分切生成自己");
		try {
			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan1_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan1_version);
			splitSheetCreateParam.setSource_quantity(new BigDecimal("10"));
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));

			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();
			SplitSheetParam.GainSpu gainSpuParam = splitSheetCreateParam.new GainSpu();
			gainSpuParam.setSpu_id(spu_id);
			gainSpuParam.setReal_quantity(new BigDecimal("8"));
			gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(3, 5, 1));
			gainSpuParams.add(gainSpuParam);
			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			SplitSheetFiterParam splitSheetFiterParam = new SplitSheetFiterParam();
			splitSheetFiterParam.setBegin(today);
			splitSheetFiterParam.setEnd(today);
			splitSheetFiterParam.setLimit(10);
			splitSheetFiterParam.setOffset(0);

			List<SplitSheetBean> splitSheets = splitService.searchSplitSheet(splitSheetFiterParam);
			Assert.assertNotEquals(splitSheets, null, "搜索过滤分割单据失败");

			SplitSheetBean splitSheet = splitSheets.stream().filter(s -> s.getId().equals(split_sheet_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(splitSheet, null, "新建的分割单据在分割单据列表没有找到");

			SplitSheetDetailBean splitSheetDetail = splitService.getSplitSheetDetail(split_sheet_id);
			Assert.assertNotEquals(splitSheetDetail, null, "获取分割单据详情失败");

			boolean result = compareSplitSheet(splitSheetCreateParam, splitSheetDetail);

			String msg = null;
			if (splitSheetDetail.getStatus() != 1) {
				msg = String.format("新建的分割单据%s状态值与预期不符,预期:1,实际:%s", split_sheet_id, splitSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的分割单据信息结果与预期不一致");
		} catch (Exception e) {
			logger.error("新建分切单据遇到错误: ", e);
			Assert.fail("新建分切单据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase02() {
		ReporterCSS.title("测试点: 新建分割单据,商品分切生成自己,并提交");
		try {
			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan1_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan1_version);
			splitSheetCreateParam.setSource_quantity(new BigDecimal("10"));
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();
			SplitSheetParam.GainSpu gainSpuParam = splitSheetCreateParam.new GainSpu();
			gainSpuParam.setSpu_id(spu_id);
			gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(2, 4, 1));
			gainSpuParam.setReal_quantity(new BigDecimal("8"));
			gainSpuParams.add(gainSpuParam);
			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			SpuStockBean beforeSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(beforeSpuStock, null, "获取商品" + spu_id + "库存信息失败");

			boolean result = splitService.updateSplitSheetStatus(split_sheet_id, 2);
			Assert.assertEquals(result, true, "更新分割单据状态为已审核失败");

			SplitSheetDetailBean splitSheetDetail = splitService.getSplitSheetDetail(split_sheet_id);
			Assert.assertNotEquals(splitSheetDetail, null, "获取分割单据详细信息失败");

			BigDecimal actual_split_loss = splitSheetDetail.getSplit_loss().setScale(2, BigDecimal.ROUND_HALF_UP);

			SpuStockBean afterSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(afterSpuStock, null, "获取商品" + spu_id + "库存信息失败");

			BigDecimal expeted_split_loss = beforeSpuStock.getRemain().subtract(afterSpuStock.getRemain());

			String msg = null;
			if (actual_split_loss.compareTo(expeted_split_loss) != 0) {
				msg = String.format("分割单据%s提交后,商品%s的库存数与预期不符,预期:%s,实际:%s", split_sheet_id, spu_id, expeted_split_loss,
						actual_split_loss);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (splitSheetDetail.getStatus() != 2) {
				msg = String.format("分割单据%s提交后,状态值与预期不符,预期:%s,实际:%s", split_sheet_id, 2, splitSheetDetail.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			StockRecordFilterParam stockRecordFilterParam = new StockRecordFilterParam();
			stockRecordFilterParam.setBegin(today);
			stockRecordFilterParam.setEnd(today);
			stockRecordFilterParam.setTime_type(1);

			List<SplitStockOutRecordBean> splitStockOutRecords = stockRecordService
					.splitStockOutRecords(stockRecordFilterParam);
			Assert.assertNotEquals(splitStockOutRecords, null, "搜索过滤分割出库记录失败");

			SplitStockOutRecordBean splitStockOutRecord = splitStockOutRecords.stream()
					.filter(s -> s.getSplit_sheet_no().equals(splitSheetDetail.getSheet_no())).findAny().orElse(null);
			if (splitStockOutRecord == null) {
				msg = String.format("分割单据%s提交后,对应的分割出库记录没有找到", splitSheetDetail.getSheet_no());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (splitStockOutRecord.getOut_stock_quantity()
						.compareTo(splitSheetCreateParam.getSource_quantity()) != 0) {
					msg = String.format("分割单据%s提交后,对应的商品%s出库数与预期不符,预期:%s,实际:%s", splitSheetDetail.getSheet_no(), spu_id,
							splitSheetCreateParam.getSource_quantity(), splitStockOutRecord.getOut_stock_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			List<SplitStockInRecordBean> splitStockInRecords = stockRecordService
					.splitStockInRecords(stockRecordFilterParam);
			Assert.assertNotEquals(splitStockInRecords, null, "分割入库记录查询失败");

			SplitStockInRecordBean splitStockInRecord = splitStockInRecords.stream()
					.filter(s -> s.getSplit_sheet_no().equals(splitSheetDetail.getSheet_no())).findAny().orElse(null);
			if (splitStockInRecord == null) {
				msg = String.format("分割单据%s提交后,对应的分割入库记录没有找到", splitSheetDetail.getSheet_no());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (gainSpuParam.getReal_quantity().compareTo(splitStockInRecord.getIn_stock_quantity()) != 0) {
					msg = String.format("分割单据%s提交后,对应的商品%s出库数与预期不符,预期:%s,实际:%s", splitSheetDetail.getSheet_no(), spu_id,
							gainSpuParam.getReal_quantity(), splitStockInRecord.getIn_stock_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "分割单据提交后,相关信息与预期不符");
		} catch (Exception e) {
			logger.error("新建分切单据遇到错误: ", e);
			Assert.fail("新建分切单据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase03() {
		ReporterCSS.title("测试点: 新建分割单据,商品分切生成其他,并提交");
		try {
			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(split_plan2_id);
			Assert.assertNotEquals(splitPlanDetail, null, "获取分割单据失败");

			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan2_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan2_version);
			splitSheetCreateParam.setSource_quantity(new BigDecimal("10"));
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();

			Map<String, BigDecimal> expectedStockMap = new HashMap<String, BigDecimal>();
			SplitSheetParam.GainSpu gainSpuParam = null;
			String temp_spu_id = null;
			BigDecimal real_quantity = null;
			for (SplitPlanDetailBean.GainSpu gainSpu : splitPlanDetail.getGain_spus()) {
				temp_spu_id = gainSpu.getSpu_id();
				SpuStockBean beforeSpuStock = stockCheckService.getSpuStock(temp_spu_id);

				gainSpuParam = splitSheetCreateParam.new GainSpu();
				gainSpuParam.setSpu_id(temp_spu_id);

				// 分割单据参数
				real_quantity = gainSpu.getSplit_ratio().multiply(splitSheetCreateParam.getSource_quantity())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(2, 5, 1));
				gainSpuParam.setReal_quantity(real_quantity);
				gainSpuParams.add(gainSpuParam);

				if (beforeSpuStock == null) {
					expectedStockMap.put(temp_spu_id, real_quantity);
				} else {
					expectedStockMap.put(temp_spu_id, beforeSpuStock.getRemain().add(real_quantity));
				}
			}

			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			SplitSheetDetailBean splitSheetDetail = splitService.getSplitSheetDetail(split_sheet_id);
			Assert.assertNotEquals(splitSheetDetail, null, "获取分割单据" + split_sheet_id + "详细信息失败");

			// 原库存
			SpuStockBean beforeSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(beforeSpuStock, null, "获取商品" + spu_id + "库存信息失败");

			boolean result = splitService.updateSplitSheetStatus(split_sheet_id, 2);
			Assert.assertEquals(result, true, "更新分割单据状态为已审核失败");

			// 出库后库存
			SpuStockBean afterSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(afterSpuStock, null, "获取商品" + spu_id + "库存信息失败");

			String msg = null;
			BigDecimal expeted_stock = beforeSpuStock.getRemain().subtract(splitSheetCreateParam.getSource_quantity());
			if (expeted_stock.compareTo(afterSpuStock.getRemain()) != 0) {
				msg = String.format("分割单据%s提交后,分割商品%s的库存数与预期不符,预期:%s,实际:%s", splitSheetDetail.getSheet_no(), spu_id,
						expeted_stock, afterSpuStock.getRemain());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 分割入库商品库存校验
			for (String spu_id : expectedStockMap.keySet()) {
				afterSpuStock = stockCheckService.getSpuStock(spu_id);
				Assert.assertNotEquals(afterSpuStock, null, "获取商品" + spu_id + "库存信息失败");

				if (expectedStockMap.get(spu_id).compareTo(afterSpuStock.getRemain()) != 0) {
					msg = String.format("分割单据%s提交后,分割入库商品%s的库存数与预期不符,预期:%s,实际:%s", splitSheetDetail.getSheet_no(),
							spu_id, expectedStockMap.get(spu_id), afterSpuStock.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			StockRecordFilterParam stockRecordFilterParam = new StockRecordFilterParam();
			stockRecordFilterParam.setBegin(today);
			stockRecordFilterParam.setEnd(today);
			stockRecordFilterParam.setTime_type(1);

			List<SplitStockInRecordBean> splitStockInRecords = stockRecordService
					.splitStockInRecords(stockRecordFilterParam);
			Assert.assertNotEquals(splitStockInRecords, null, "分割入库记录查询失败");

			// 分割入库商品入库记录校验
			String split_sheet_no = splitSheetDetail.getSheet_no();
			SplitStockInRecordBean splitStockInRecord = null;
			for (SplitSheetParam.GainSpu gainSpu : gainSpuParams) {
				splitStockInRecord = splitStockInRecords.stream().filter(
						s -> s.getSpu_id().equals(gainSpu.getSpu_id()) && s.getSplit_sheet_no().equals(split_sheet_no))
						.findAny().orElse(null);
				if (splitStockInRecord == null) {
					msg = String.format("分割单据%s里的入库商品%s,没有找到对应的入库记录", split_sheet_no, gainSpu.getSpu_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (gainSpu.getReal_quantity().compareTo(splitStockInRecord.getIn_stock_quantity()) != 0) {
					msg = String.format("分割单据%s里的入库商品%s,对应的入库数与预期不符,预期:%s,实际:%s", split_sheet_no, gainSpu.getSpu_id(),
							gainSpu.getReal_quantity(), splitStockInRecord.getIn_stock_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (gainSpu.getIn_stock_price().compareTo(splitStockInRecord.getIn_stock_price()) != 0) {
					msg = String.format("分割单据%s里的入库商品%s,对应的入库单价不符,预期:%s,实际:%s", split_sheet_no, gainSpu.getSpu_id(),
							gainSpu.getIn_stock_price(), splitStockInRecord.getIn_stock_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			// 如果站点是先进先出站点,则需要检测入库批次
			if (stock_type == 2) {
				List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch("", 0, 10);
				Assert.assertNotEquals(stockBatchList, null, "获取商品 批次列表信息失败");

				for (SplitSheetParam.GainSpu gainSpu : gainSpuParams) {
					StockBatchBean stockBatch = stockBatchList.stream()
							.filter(s -> s.getBatch_number().contains(split_sheet_no)
									&& s.getSpu_id().equals(gainSpu.getSpu_id()))
							.findAny().orElse(null);
					if (stockBatch == null) {
						msg = String.format("分割单据%s里的入库商品%s,没有找到对应的入库批次", split_sheet_no, gainSpu.getSpu_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (stockBatch.getRemain().compareTo(gainSpu.getReal_quantity()) != 0) {
						msg = String.format("分割单据%s里的入库商品%s,对应的批次%s入库数与预期不符,预期:%s,实际:%s", split_sheet_no,
								gainSpu.getSpu_id(), stockBatch.getBatch_number(), gainSpu.getReal_quantity(),
								stockBatch.getRemain());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (gainSpu.getIn_stock_price().compareTo(stockBatch.getPrice()) != 0) {
						msg = String.format("分割单据%s里的入库商品%s,对应的批次%s入库单价不符,预期:%s,实际:%s", split_sheet_no,
								gainSpu.getSpu_id(), stockBatch.getBatch_number(), gainSpu.getIn_stock_price(),
								stockBatch.getPrice());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "分割单据提交后,信息与预期不符");
		} catch (Exception e) {
			logger.error("新建分切单据遇到错误: ", e);
			Assert.fail("新建分切单据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase04() {
		ReporterCSS.title("测试点: 修改分割单据,商品分切生成其他,并提交");
		try {
			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(split_plan2_id);
			Assert.assertNotEquals(splitPlanDetail, null, "获取分割单据失败");

			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan2_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan2_version);
			BigDecimal source_quantity = new BigDecimal("10");
			splitSheetCreateParam.setSource_quantity(source_quantity);
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();

			SplitSheetParam.GainSpu gainSpuParam = null;
			for (SplitPlanDetailBean.GainSpu gainSpu : splitPlanDetail.getGain_spus()) {
				gainSpuParam = splitSheetCreateParam.new GainSpu();
				gainSpuParam.setSpu_id(gainSpu.getSpu_id());
				gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(2, 5, 1));
				gainSpuParam.setReal_quantity(
						gainSpu.getSplit_ratio().multiply(splitSheetCreateParam.getSource_quantity()));
				gainSpuParams.add(gainSpuParam);
			}

			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			SplitSheetParam splitSheetUpdateParam = new SplitSheetParam();
			splitSheetUpdateParam.setId(split_sheet_id);
			splitSheetUpdateParam.setSource_quantity(source_quantity);
			splitSheetUpdateParam.setGain_spus(gainSpuParams);

			boolean result = splitService.updateSplitSheet(splitSheetUpdateParam);
			Assert.assertEquals(result, true, "修改分割单据失败");

		} catch (Exception e) {
			logger.error("修改分切单据遇到错误: ", e);
			Assert.fail("修改分切单据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase05() {
		ReporterCSS.title("测试点: 分割单据,提交后进行冲销操作");
		try {
			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(split_plan2_id);
			Assert.assertNotEquals(splitPlanDetail, null, "获取分割单据失败");

			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan2_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan2_version);
			BigDecimal source_quantity = new BigDecimal("10");
			splitSheetCreateParam.setSource_quantity(source_quantity);
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();

			// 待分割商品期待库存库存
			SpuStockBean expetedSourceSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(expetedSourceSpuStock, null, "获取商品" + spu_id + "库存信息失败");

			// 分割入库商品,审核不通过期待的库存
			Map<String, BigDecimal> expectedSpuStockMap = new HashMap<String, BigDecimal>();

			SplitSheetParam.GainSpu gainSpuParam = null;
			for (SplitPlanDetailBean.GainSpu gainSpu : splitPlanDetail.getGain_spus()) {
				gainSpuParam = splitSheetCreateParam.new GainSpu();
				gainSpuParam.setSpu_id(gainSpu.getSpu_id());

				// 待分割商品期待库存库存
				SpuStockBean tempSpuStock = stockCheckService.getSpuStock(gainSpu.getSpu_id());
				if (tempSpuStock == null) {
					expectedSpuStockMap.put(gainSpu.getSpu_id(), new BigDecimal("0"));
				} else {
					expectedSpuStockMap.put(gainSpu.getSpu_id(), tempSpuStock.getRemain());
				}

				gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(2, 5, 1));
				gainSpuParam.setReal_quantity(
						gainSpu.getSplit_ratio().multiply(splitSheetCreateParam.getSource_quantity()));
				gainSpuParams.add(gainSpuParam);
			}

			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			boolean result = splitService.updateSplitSheetStatus(split_sheet_id, 2);
			Assert.assertEquals(result, true, "更新分割单据状态为已审核失败");

			SplitSheetDetailBean splitSheetDetail = splitService.getSplitSheetDetail(split_sheet_id);
			Assert.assertNotEquals(splitSheetDetail, null, "获取分割单据 " + split_sheet_id + "详情失败");

			String sheet_no = splitSheetDetail.getSheet_no();

			result = splitService.deleteSplitSheet(split_sheet_id);
			Assert.assertEquals(result, true, "冲销分割单据" + split_sheet_id + "失败");

			// 待分割商品期待库存库存
			SpuStockBean actualSourceSpuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(actualSourceSpuStock, null, "获取商品" + spu_id + "库存信息失败");
			String msg = null;
			if (actualSourceSpuStock.getRemain().compareTo(expetedSourceSpuStock.getRemain()) != 0) {
				msg = String.format("分割单据%s冲销后,待分割商品%s库存数与预期不一致,预期:%s,实际:%s", sheet_no, spu_id,
						expetedSourceSpuStock.getRemain(), actualSourceSpuStock.getRemain());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			for (SplitPlanDetailBean.GainSpu gainSpu : splitPlanDetail.getGain_spus()) {
				String temp_spu_id = gainSpu.getSpu_id();
				SpuStockBean tempSpuStock = stockCheckService.getSpuStock(temp_spu_id);
				if (tempSpuStock.getRemain().compareTo(expectedSpuStockMap.get(temp_spu_id)) != 0) {
					msg = String.format("分割单据%s冲销后,获得商品%s库存数与预期不一致,预期:%s,实际:%s", sheet_no, temp_spu_id,
							expectedSpuStockMap.get(temp_spu_id), tempSpuStock.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			// 如果站点是先进先出站点,则需要检测入库批次
			if (stock_type == 2) {
				List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch("", 0, 10);
				Assert.assertNotEquals(stockBatchList, null, "获取商品 批次列表信息失败");

				List<String> stock_batch_nos = stockBatchList.stream()
						.filter(s -> s.getBatch_number().contains(sheet_no)).map(s -> s.getBatch_number())
						.collect(Collectors.toList());

				if (stock_batch_nos.size() > 0) {
					msg = String.format("分割单据%s冲销后,对应的批次信息没有回滚", sheet_no, stock_batch_nos);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			}
			Assert.assertEquals(result, true, "分割单据" + sheet_no + "回滚后,对应的信息与预期不符");
		} catch (Exception e) {
			logger.error("修改分切单据遇到错误: ", e);
			Assert.fail("修改分切单据遇到错误: ", e);
		}
	}

	@Test
	public void splitSheetTestCase06() {
		ReporterCSS.title("测试点: 审核不通过分割单据");
		try {
			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(split_plan2_id);
			Assert.assertNotEquals(splitPlanDetail, null, "获取分割单据失败");

			SplitSheetParam splitSheetCreateParam = new SplitSheetParam();
			splitSheetCreateParam.setPlan_id(split_plan2_id);
			splitSheetCreateParam.setSource_spu_id(spu_id);
			splitSheetCreateParam.setPlan_version(split_plan2_version);
			BigDecimal source_quantity = new BigDecimal("10");
			splitSheetCreateParam.setSource_quantity(source_quantity);
			splitSheetCreateParam.setSplit_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			List<SplitSheetParam.GainSpu> gainSpuParams = new ArrayList<SplitSheetParam.GainSpu>();

			SplitSheetParam.GainSpu gainSpuParam = null;
			for (SplitPlanDetailBean.GainSpu gainSpu : splitPlanDetail.getGain_spus()) {
				gainSpuParam = splitSheetCreateParam.new GainSpu();
				gainSpuParam.setSpu_id(gainSpu.getSpu_id());
				gainSpuParam.setIn_stock_price(NumberUtil.getRandomNumber(2, 5, 1));
				gainSpuParam.setReal_quantity(
						gainSpu.getSplit_ratio().multiply(splitSheetCreateParam.getSource_quantity()));
				gainSpuParams.add(gainSpuParam);
			}

			splitSheetCreateParam.setGain_spus(gainSpuParams);

			String split_sheet_id = splitService.createSplitSheet(splitSheetCreateParam);
			Assert.assertNotEquals(split_sheet_id, null, "新建分割单据失败");

			boolean result = splitService.updateSplitSheetStatus(split_sheet_id, 3);
			Assert.assertEquals(result, true, "分割单据" + split_sheet_id + "审核不通过失败");

			SplitSheetDetailBean splitSheetDetail = splitService.getSplitSheetDetail(split_sheet_id);
			Assert.assertNotEquals(splitSheetDetail, null, "获取分割单据" + split_sheet_id + "详情失败");

			Assert.assertEquals(splitSheetDetail.getStatus(), 3, "分割单据" + split_sheet_id + "审核不通过后,状态值与预期不一致");
		} catch (Exception e) {
			logger.error("修改分切单据遇到错误: ", e);
			Assert.fail("修改分切单据遇到错误: ", e);
		}
	}

	public boolean compareSplitSheet(SplitSheetParam splitSheetCreateParam, SplitSheetDetailBean splitSheetDetail) {
		boolean result = true;
		String msg = null;
		String split_sheet_no = splitSheetDetail.getSheet_no();

		if (!splitSheetCreateParam.getSource_spu_id().equals(splitSheetDetail.getSource_spu().getSpu_id())) {
			msg = String.format("新建的分割单据%s待分割商品与预期不一致,预期:%s,实际:%s", split_sheet_no,
					splitSheetCreateParam.getSource_spu_id(), splitSheetDetail.getSource_spu().getSpu_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		BigDecimal source_quantity = splitSheetCreateParam.getSource_quantity();
		if (source_quantity.compareTo(splitSheetDetail.getSource_spu().getQuantity()) != 0) {
			msg = String.format("新建的分割单据%s待分割品消耗量与预期不一致,预期:%s,实际:%s", split_sheet_no, source_quantity,
					splitSheetDetail.getSource_spu().getQuantity());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		BigDecimal total_real_quantity = new BigDecimal("0");
		List<SplitSheetDetailBean.GainSpu> gainSpuResults = splitSheetDetail.getGain_spus();
		List<SplitSheetParam.GainSpu> gainSpuParams = splitSheetCreateParam.getGain_spus();
		for (SplitSheetParam.GainSpu gainSpuParam : gainSpuParams) {
			total_real_quantity = total_real_quantity.add(gainSpuParam.getReal_quantity());
			SplitSheetDetailBean.GainSpu gainSpuResult = gainSpuResults.stream()
					.filter(g -> g.getSpu_id().equals(gainSpuParam.getSpu_id())).findAny().orElse(null);
			if (gainSpuResult == null) {
				msg = String.format("新建的分割单据%s分割获得品%s没有找到", split_sheet_no, gainSpuParam.getSpu_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (gainSpuParam.getReal_quantity().compareTo(gainSpuResult.getReal_quantity()) != 0) {
				msg = String.format("新建的分割单据%s分割获得品%s的实际获得量与预期不符,预期:%s,实际:%s", split_sheet_no, gainSpuParam.getSpu_id(),
						gainSpuParam.getReal_quantity(), gainSpuResult.getReal_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (gainSpuParam.getIn_stock_price()
					.compareTo(gainSpuResult.getIn_stock_price().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
				msg = String.format("新建的分割单据%s分割获得品%s的入库价与预期不符,预期:%s,实际:%s", split_sheet_no, gainSpuParam.getSpu_id(),
						gainSpuParam.getIn_stock_price(), gainSpuResult.getIn_stock_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		BigDecimal expeted_split_loss_quantity = source_quantity.subtract(total_real_quantity);
		if (expeted_split_loss_quantity.compareTo(splitSheetDetail.getSplit_loss()) != 0) {
			msg = String.format("新建的分割单据%s分割损耗与预期不一致,预期:%s,实际:%s", split_sheet_no, expeted_split_loss_quantity,
					splitSheetDetail.getSplit_loss());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;
	}

	@AfterClass
	public void afterCalss() {
		try {
			SplitPlanFilterParam splitPlanFilterParam = new SplitPlanFilterParam();
			splitPlanFilterParam.setQ("AT-");
			splitPlanFilterParam.setLimit(20);
			splitPlanFilterParam.setOffset(0);

			List<SplitPlanBean> splitPlans = splitService.searchSplitPlan(splitPlanFilterParam);
			Assert.assertNotEquals(splitPlans, null, "搜索过滤分割方案失败");

			for (SplitPlanBean splitPlan : splitPlans) {
				boolean result = splitService.deleteSplitPlan(splitPlan.getId(), splitPlan.getVersion());
				Assert.assertEquals(result, true, "删除分割方案失败");
			}
		} catch (Exception e) {
			logger.error("删除分割方案遇到错误: ", e);
			Assert.fail("删除分割方案遇到错误: ", e);
		}
	}

}
