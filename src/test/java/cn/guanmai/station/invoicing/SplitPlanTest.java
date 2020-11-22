package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.bean.invoicing.SplitPlanBean;
import cn.guanmai.station.bean.invoicing.SplitPlanDetailBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.param.SplitPlanFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.SplitServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.SplitService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午4:12:03
 * @description:
 * @version: 1.0
 */

public class SplitPlanTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SplitPlanTest.class);

	private SplitService splitService;
	private StockCheckService stockCheckService;
	private CategoryService categoryService;
	private LoginUserInfoService loginUserInfoService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		splitService = new SplitServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			JSONArray permissions = loginUserInfoService.getLoginUserInfo().getUser_permission();
			Assert.assertEquals(permissions.contains("add_split_plan"), true, "登录账号没有新建分割方法的权限");
		} catch (Exception e) {
			logger.error("获取登录用户权限遇到错误: ", e);
			Assert.fail("获取登录用户权限遇到错误: ", e);
		}
	}

	@Test
	public void splitPlanTestCase01() {
		ReporterCSS.title("测试点: 新建分割方案");
		try {
			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setRemain_status(1);
			stockCheckFilterParam.setText("a");
			stockCheckFilterParam.setLimit(20);
			List<SpuStockBean> spuStocks = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStocks, null, "新建分割方案,查询待分割商品失败");

			spuStocks = spuStocks.stream().filter(s -> s.getStd_unit_name().equals("斤")).collect(Collectors.toList());
			Assert.assertEquals(spuStocks.size() > 0, true, "新建分割方案,查询待分割商品,查询结果为空,没有查询到基本单位为斤的商品,无法进行新建分割方案");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStocks);

			String name = "AT-" + StringUtil.getRandomNumber(4);
			String remark = StringUtil.getRandomNumber(6);
			boolean is_deleted = false;
			String source_spu_id = spuStock.getSpu_id();

			SplitPlanParam splitPlanParam = new SplitPlanParam();
			splitPlanParam.setName(name);
			splitPlanParam.setRemark(remark);
			splitPlanParam.setSource_spu_id(source_spu_id);
			splitPlanParam.setIs_deleted(is_deleted);

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(20);
			spuIndexFilterParam.setQ("a");
			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "新建分割方案,搜索过滤获得品失败");

			spuIndexList = spuIndexList.stream().filter(s -> s.getStd_unit_name().equals("斤"))
					.collect(Collectors.toList());
			Assert.assertEquals(spuIndexList.size() > 0, true, "商品库搜索,没有找到基本单位为斤的商品,无法进行新建分割方案");

			spuIndexList = NumberUtil.roundNumberInList(spuIndexList, 4);

			BigDecimal split_ratio = new BigDecimal("1").divide(new BigDecimal(spuIndexList.size())).setScale(2,
					BigDecimal.ROUND_HALF_DOWN);

			List<SplitPlanParam.GainSpu> gainSpus = new ArrayList<SplitPlanParam.GainSpu>();
			SplitPlanParam.GainSpu gainSpu = null;
			for (SpuIndexBean spuIndex : spuIndexList) {
				gainSpu = splitPlanParam.new GainSpu();
				gainSpu.setSpu_id(spuIndex.getSpu_id());
				gainSpu.setSplit_ratio(split_ratio);
				gainSpus.add(gainSpu);
			}

			splitPlanParam.setGain_spus(gainSpus);

			String splitPlanId = splitService.createSplitPlan(splitPlanParam);
			Assert.assertNotEquals(splitPlanId, null, "新建分割方案失败");

			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(splitPlanId);
			Assert.assertNotEquals(splitPlanDetail, null, "获取新建的分割方案详情信息失败");

			boolean result = compareSplitPlan(splitPlanParam, splitPlanDetail);
			Assert.assertEquals(result, true, "新建的分割方案详情信息与预期的不一致");
		} catch (Exception e) {
			logger.error("新建分割方案遇到错误: ", e);
			Assert.fail("新建分割方案遇到错误: ", e);
		}
	}

	@Test
	public void splitPlanTestCase02() {
		ReporterCSS.title("测试点: 修改分割方案");
		try {
			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setRemain_status(1);
			stockCheckFilterParam.setText("a");
			stockCheckFilterParam.setLimit(20);
			List<SpuStockBean> spuStocks = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStocks, null, "新建分割方案,查询待分割商品失败");

			spuStocks = spuStocks.stream().filter(s -> s.getStd_unit_name().equals("斤")).collect(Collectors.toList());
			Assert.assertEquals(spuStocks.size() > 0, true, "新建分割方案,查询待分割商品,查询结果为空,没有查询到基本单位为斤的商品,无法进行新建分割方案");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStocks);

			String name = "AT-" + StringUtil.getRandomNumber(4);
			String remark = StringUtil.getRandomNumber(6);
			boolean is_deleted = false;
			String source_spu_id = spuStock.getSpu_id();

			SplitPlanParam splitPlanParam = new SplitPlanParam();
			splitPlanParam.setName(name);
			splitPlanParam.setRemark(remark);
			splitPlanParam.setSource_spu_id(source_spu_id);
			splitPlanParam.setIs_deleted(is_deleted);

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(20);
			spuIndexFilterParam.setQ("a");
			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "新建分割方案,搜索过滤获得品失败");

			spuIndexList = spuIndexList.stream().filter(s -> s.getStd_unit_name().equals("斤"))
					.collect(Collectors.toList());
			Assert.assertEquals(spuIndexList.size() > 0, true, "商品库搜索,没有找到基本单位为斤的商品,无法进行新建分割方案");

			List<SpuIndexBean> createspuIndexList = NumberUtil.roundNumberInList(spuIndexList, 4);

			BigDecimal split_ratio = new BigDecimal("1").divide(new BigDecimal(createspuIndexList.size())).setScale(2,
					BigDecimal.ROUND_HALF_DOWN);

			List<SplitPlanParam.GainSpu> gainSpus = new ArrayList<SplitPlanParam.GainSpu>();
			SplitPlanParam.GainSpu gainSpu = null;
			for (SpuIndexBean spuIndex : createspuIndexList) {
				gainSpu = splitPlanParam.new GainSpu();
				gainSpu.setSpu_id(spuIndex.getSpu_id());
				gainSpu.setSplit_ratio(split_ratio);
				gainSpus.add(gainSpu);
			}

			splitPlanParam.setGain_spus(gainSpus);

			String splitPlanId = splitService.createSplitPlan(splitPlanParam);
			Assert.assertNotEquals(splitPlanId, null, "新建分割方案失败");

			splitPlanParam.setId(splitPlanId);
			String new_name = "AT-" + StringUtil.getRandomNumber(4);
			String new_remark = StringUtil.getRandomString(6);
			splitPlanParam.setName(new_name);
			splitPlanParam.setRemark(new_remark);
			SpuStockBean updateSpuStock = NumberUtil.roundNumberInList(spuStocks);
			splitPlanParam.setSource_spu_id(updateSpuStock.getSpu_id());

			List<SplitPlanParam.GainSpu> updateGainSpus = new ArrayList<SplitPlanParam.GainSpu>();
			List<SpuIndexBean> updateSpuIndexList = NumberUtil.roundNumberInList(spuIndexList, 4);
			for (SpuIndexBean spuIndex : updateSpuIndexList) {
				gainSpu = splitPlanParam.new GainSpu();
				gainSpu.setSpu_id(spuIndex.getSpu_id());
				gainSpu.setSplit_ratio(split_ratio);
				updateGainSpus.add(gainSpu);
			}

			splitPlanParam.setGain_spus(updateGainSpus);
			splitPlanParam.setVersion(1);

			boolean result = splitService.updateSplitPlan(splitPlanParam);
			Assert.assertEquals(result, true, "修改分割方案失败");

			SplitPlanDetailBean splitPlanDetail = splitService.getSplitPlanDetail(splitPlanId);
			Assert.assertNotEquals(splitPlanDetail, null, "获取新建的分割方案详情信息失败");

			result = compareSplitPlan(splitPlanParam, splitPlanDetail);
			Assert.assertEquals(result, true, "修改分割方案详情信息与预期的不一致");
		} catch (Exception e) {
			logger.error("修改分割方案遇到错误: ", e);
			Assert.fail("修改分割方案遇到错误: ", e);
		}
	}

	@Test
	public void splitPlanTestCase03() {
		ReporterCSS.title("测试点: 删除分割方案");
		try {
			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setRemain_status(1);
			stockCheckFilterParam.setText("a");
			stockCheckFilterParam.setLimit(20);
			List<SpuStockBean> spuStocks = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStocks, null, "新建分割方案,查询待分割商品失败");

			spuStocks = spuStocks.stream().filter(s -> s.getStd_unit_name().equals("斤")).collect(Collectors.toList());
			Assert.assertEquals(spuStocks.size() > 0, true, "新建分割方案,查询待分割商品,查询结果为空,没有查询到基本单位为斤的商品,无法进行新建分割方案");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStocks);

			String name = "AT-" + StringUtil.getRandomNumber(4);
			String remark = StringUtil.getRandomNumber(6);
			boolean is_deleted = false;
			String source_spu_id = spuStock.getSpu_id();

			SplitPlanParam splitPlanParam = new SplitPlanParam();
			splitPlanParam.setName(name);
			splitPlanParam.setRemark(remark);
			splitPlanParam.setSource_spu_id(source_spu_id);
			splitPlanParam.setIs_deleted(is_deleted);

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(20);
			spuIndexFilterParam.setQ("a");
			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "新建分割方案,搜索过滤获得品失败");

			spuIndexList = spuIndexList.stream().filter(s -> s.getStd_unit_name().equals("斤"))
					.collect(Collectors.toList());
			Assert.assertEquals(spuIndexList.size() > 0, true, "商品库搜索,没有找到基本单位为斤的商品,无法进行新建分割方案");

			spuIndexList = NumberUtil.roundNumberInList(spuIndexList, 4);

			BigDecimal split_ratio = new BigDecimal("1").divide(new BigDecimal(spuIndexList.size())).setScale(2,
					BigDecimal.ROUND_HALF_DOWN);

			List<SplitPlanParam.GainSpu> gainSpus = new ArrayList<SplitPlanParam.GainSpu>();
			SplitPlanParam.GainSpu gainSpu = null;
			for (SpuIndexBean spuIndex : spuIndexList) {
				gainSpu = splitPlanParam.new GainSpu();
				gainSpu.setSpu_id(spuIndex.getSpu_id());
				gainSpu.setSplit_ratio(split_ratio);
				gainSpus.add(gainSpu);
			}

			splitPlanParam.setGain_spus(gainSpus);

			String split_plan_id = splitService.createSplitPlan(splitPlanParam);
			Assert.assertNotEquals(split_plan_id, null, "新建分割方案失败");

			boolean result = splitService.deleteSplitPlan(split_plan_id, 1);
			Assert.assertEquals(result, true, "删除分割方案失败");

			SplitPlanFilterParam splitPlanFilterParam = new SplitPlanFilterParam();
			splitPlanFilterParam.setQ(name);
			splitPlanFilterParam.setLimit(20);
			splitPlanFilterParam.setOffset(0);

			List<SplitPlanBean> splitPlans = splitService.searchSplitPlan(splitPlanFilterParam);
			Assert.assertNotEquals(splitPlans, null, "搜索过滤分割方案失败");

			SplitPlanBean splitPlan = splitPlans.stream().filter(s -> s.getId().equals(split_plan_id)).findAny()
					.orElse(null);
			Assert.assertEquals(splitPlan, null, "删除的分割方案实际没有删除");
		} catch (Exception e) {
			logger.error("删除分割方案遇到错误: ", e);
			Assert.fail("删除分割方案遇到错误: ", e);
		}
	}

	/**
	 * 参数与结果对比
	 * 
	 * @param splitPlanParam
	 * @param splitPlanDetail
	 * @return
	 */
	public boolean compareSplitPlan(SplitPlanParam splitPlanParam, SplitPlanDetailBean splitPlanDetail) {
		String msg = null;
		boolean result = true;

		if (!splitPlanParam.getName().equals(splitPlanDetail.getName())) {
			msg = String.format("新建/修改的分割方案,方案名称与预期的不一致,预期:%s,实际:%s", splitPlanParam.getName(),
					splitPlanDetail.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		if (!splitPlanParam.getRemark().equals(splitPlanDetail.getRemark())) {
			msg = String.format("新建/修改的分割方案,备注与预期的不一致,预期:%s,实际:%s", splitPlanParam.getName(),
					splitPlanDetail.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		if (!splitPlanParam.getSource_spu_id().equals(splitPlanDetail.getSource_spu_id())) {
			msg = String.format("新建/修改的分割方案,待分割商品与预期的不一致,预期:%s,实际:%s", splitPlanParam.getName(),
					splitPlanDetail.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<SplitPlanParam.GainSpu> gainSpuParams = splitPlanParam.getGain_spus();
		List<SplitPlanDetailBean.GainSpu> gainSpuResults = splitPlanDetail.getGain_spus();

		for (SplitPlanParam.GainSpu gainSpuParam : gainSpuParams) {
			SplitPlanDetailBean.GainSpu gainSpuResult = gainSpuResults.stream()
					.filter(s -> s.getSpu_id().equals(gainSpuParam.getSpu_id())).findAny().orElse(null);
			if (gainSpuResult == null) {
				msg = String.format("新建/修改的分割方案,获得品:%s没有在详情中找到", gainSpuParam.getSpu_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (gainSpuParam.getSplit_ratio().compareTo(gainSpuResult.getSplit_ratio()) != 0) {
					msg = String.format("新建/修改的分割方案,获得品:%s的分割系数与预期的不一致,预期:%s,实际:%s", gainSpuParam.getSpu_id(),
							gainSpuParam.getSplit_ratio(), gainSpuResult.getSplit_ratio());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}
		return result;
	}

	@AfterClass
	public void afterClass() {
		ReporterCSS.title("后置处理: 删除新建的分割方案");
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
