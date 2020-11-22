package cn.guanmai.open.stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.stock.OpenStockBean;
import cn.guanmai.open.bean.stock.param.OpenStockFilterParam;
import cn.guanmai.open.impl.stock.OpenStockServiceImpl;
import cn.guanmai.open.interfaces.stock.OpenStockService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午6:01:29
 * @des TODO
 */

public class OpenStockTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockTest.class);
	private OpenStockService openStockService;
	private StockCheckService stockCheckService;

	@BeforeClass
	public void initData() {
		openStockService = new OpenStockServiceImpl(getAccess_token());
		stockCheckService = new StockCheckServiceImpl(getSt_headers());

	}

	@Test
	public void openStockTestCase01() {
		ReporterCSS.title("测试点: 商品盘点,搜索过滤");
		try {
			List<OpenStockBean> openStockList = openStockService.queryStock(new OpenStockFilterParam());
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockTestCase02() {
		ReporterCSS.title("测试点: 商品盘点,搜索过滤");
		try {
			List<OpenStockBean> openStockList = openStockService.queryStock(new OpenStockFilterParam());
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setOffset(0);
			stockCheckFilterParam.setLimit(20);
			List<SpuStockBean> spuStockList = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStockList, null, "st库存盘点搜索过滤失败");

			Assert.assertEquals(openStockList.size(), spuStockList.size(), "开放平台和ST接口库存盘点搜索到的数据条目数不一致");

			boolean reuslt = compareData(openStockList, spuStockList);
			Assert.assertEquals(reuslt, true, "开放平台和ST接口库存盘点搜索到的数据信息一致");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockTestCase03() {
		ReporterCSS.title("测试点: 商品盘点,搜索过滤");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setLimit("20");
			openStockFilterParam.setOffset("20");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setLimit(20);
			stockCheckFilterParam.setOffset(20);

			List<SpuStockBean> spuStockList = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStockList, null, "st库存盘点搜索过滤失败");

			Assert.assertEquals(openStockList.size(), spuStockList.size(), "开放平台和ST接口库存盘点搜索到的数据条目数不一致");

			boolean reuslt = compareData(openStockList, spuStockList);
			Assert.assertEquals(reuslt, true, "开放平台和ST接口库存盘点搜索到的数据信息一致");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockTestCase04() {
		ReporterCSS.title("测试点: 商品盘点,搜索过滤");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			OpenStockBean openStock = NumberUtil.roundNumberInList(openStockList);
			String category1_id = openStock.getCategory1_id();
			String category2_id = openStock.getCategory2_id();

			openStockFilterParam.setCategory1_id(category1_id);
			openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");
			List<OpenStockBean> tempOpenStockList = openStockList.stream()
					.filter(s -> !s.getCategory1_id().equals(category1_id)).collect(Collectors.toList());
			Assert.assertEquals(tempOpenStockList.size(), 0, "商品盘点,按一级分类过滤,过滤出了不符合过滤条件的数据");

			openStockFilterParam.setCategory2_id(category2_id);
			openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");
			tempOpenStockList = openStockList.stream().filter(s -> !s.getCategory2_id().equals(category2_id))
					.collect(Collectors.toList());
			Assert.assertEquals(tempOpenStockList.size(), 0, "商品盘点,按二级分类过滤,过滤出了不符合过滤条件的数据");

		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockTestCase05() {
		ReporterCSS.title("测试点: 商品盘点,按库存状态过滤");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setStatus("1");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,按库存状态过滤");

			List<OpenStockBean> tempOpenStockList = openStockList.stream()
					.filter(s -> s.getStock().compareTo(new BigDecimal("0")) <= 0).collect(Collectors.toList());
			Assert.assertEquals(tempOpenStockList.size(), 0, "商品盘点,搜索库存数大于0的商品,过滤出了不符合过滤条件的数据");

			openStockFilterParam.setStatus("2");

			openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,按库存状态过滤");

			tempOpenStockList = openStockList.stream().filter(s -> s.getStock().compareTo(new BigDecimal("0")) != 0)
					.collect(Collectors.toList());
			Assert.assertEquals(tempOpenStockList.size(), 0, "商品盘点,搜索库存数等于0的商品,过滤出了不符合过滤条件的数据");

			openStockFilterParam.setStatus("3");

			openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,按库存状态过滤");

			tempOpenStockList = openStockList.stream().filter(s -> s.getStock().compareTo(new BigDecimal("0")) > 0)
					.collect(Collectors.toList());
			Assert.assertEquals(tempOpenStockList.size(), 0, "商品盘点,搜索库存数小于0的商品,过滤出了不符合过滤条件的数据");

		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockTestCase06() {
		ReporterCSS.title("测试点: 商品盘点,修改库存");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertNotEquals(openStockList, null, "商品盘点,拉取商品列表失败");

			Assert.assertEquals(openStockList.size() > 0, true, "列表无值,无法盘点");

			OpenStockBean openStock = NumberUtil.roundNumberInList(openStockList);

			String spu_id = openStock.getSpu_id();

			boolean result = openStockService.updateStock(spu_id, "0", "las");
			Assert.assertEquals(result, true, "商品盘点,修改库存,修改失败");

			SpuStockBean spuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(spuStock, null, "业务平台获取商品" + spu_id + " 库存信息失败");

			Assert.assertEquals(spuStock.getRemain().compareTo(new BigDecimal("0")) == 0, true,
					"商品盘点,修改库存,实际查询,没有修改成功");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	public boolean compareData(List<OpenStockBean> openStockList, List<SpuStockBean> spuStockList) {
		OpenStockBean openStock = null;
		SpuStockBean spuStock = null;
		String msg = null;
		boolean result = true;
		for (int i = 0; i < openStockList.size(); i++) {
			openStock = openStockList.get(i);
			spuStock = spuStockList.get(i);
			if (!openStock.getSpu_id().equals(spuStock.getSpu_id())) {
				msg = String.format("排序不一致,序号%d,开放平台%s,业务平台 %s", i, openStock.getSpu_id(), spuStock.getSpu_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (openStock.getStock().compareTo(spuStock.getRemain()) != 0) {
				msg = String.format("库存不一致,商品%s,开放平台%s,业务平台 %s", openStock.getSpu_id(), openStock.getStock(),
						spuStock.getRemain());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (openStock.getFrozen_stock().compareTo(spuStock.getFrozen()) != 0) {
				msg = String.format("冻结库存不一致,商品%s,开放平台%s,业务平台 %s", openStock.getSpu_id(), openStock.getFrozen_stock(),
						spuStock.getFrozen());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}
}
