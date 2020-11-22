package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.CategoriesBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockChangeLogBean;
import cn.guanmai.station.bean.invoicing.param.SpuStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockAvgPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.StockChangeLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 28, 2019 10:42:23 AM 
* @des 商品盘点测试
* @version 1.0 
*/
public class StockCheckTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockCheckTest.class);
	private StockCheckService stockCheckService;
	private AsyncService asyncService;
	private LoginUserInfoService loginUserInfoService;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private JSONArray user_permissions;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockCheckService = new StockCheckServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		try {
			user_permissions = loginUserInfoService.getLoginUserInfo().getUser_permission();
			Assert.assertNotEquals(user_permissions, null, "获取登录用户的权限信息失败");
		} catch (Exception e) {
			logger.error("获取登录用户的相关信息遇到错误: ", e);
			Assert.fail("获取登录用户的相关信息遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase01() {
		ReporterCSS.title("测试点: 商品盘点页面数据拉取");
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(10);
		try {
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
		} catch (Exception e) {
			logger.error("获取商品盘点列表遇到错误: ", e);
			Assert.fail("获取商品盘点列表遇到错误: ", e);
		}

	}

	@Test
	public void stockCheckTestCase02() {
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(10);

		ReporterCSS.title("测试点: 对商品库存进行盘点操作");
		try {
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			Assert.assertEquals(stockCheckList.size() > 0, true, "商品判断页面无数据,无法进行商品盘点操作");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(stockCheckList);

			String spu_id = spuStock.getSpu_id();

			boolean result = stockCheckService.editSpuStock(spu_id, new BigDecimal("0"), "自动化");
			Assert.assertEquals(result, true, "商品盘点操作失败");

			spuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(spuStock, null, "获取指定SPU " + spu_id + " 的盘点数据失败");

			result = spuStock.getMaterial().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)
					.compareTo(new BigDecimal("0")) == 0;

			Assert.assertEquals(result, true, "商品 " + spu_id + " 的库存应该为0");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase03() {
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(5);

		ReporterCSS.title("测试点: 对商品库存进行一键报损报溢操作");
		try {
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			Assert.assertEquals(stockCheckList.size() > 0, true, "商品判断页面无数据,无法进行商品盘点操作");

			List<SpuStockCheckParam> batchStockCheckList = new ArrayList<SpuStockCheckParam>();
			SpuStockCheckParam batchStockCheckParam = null;
			for (SpuStockBean spuStock : stockCheckList) {
				batchStockCheckParam = new SpuStockCheckParam();
				batchStockCheckParam.setSpu_id(spuStock.getSpu_id());
				batchStockCheckParam.setNew_stock(NumberUtil.getRandomNumber(1, 10, 2));
				batchStockCheckParam.setRemark(StringUtil.getRandomString(4));
				batchStockCheckList.add(batchStockCheckParam);
			}

			BigDecimal task_id = stockCheckService.spuBatchStockCheck(batchStockCheckList);
			Assert.assertNotEquals(task_id, true, "批量盘点SPU库存,异步任务创建失败");

			Thread.sleep(1000);

			List<AsyncTaskResultBean> asyncTaskResultList = asyncService.getAsyncTaskResultList();
			Assert.assertNotEquals(asyncTaskResultList, null, "异步任务列表获取失败");

			AsyncTaskResultBean asyncTaskResult = asyncTaskResultList.stream()
					.filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny().orElse(null);
			Assert.assertNotEquals(asyncTaskResult, null, "批量盘点SPU库存的异步任务在异步任务列表中没有找到");

			boolean result = asyncService.getAsyncTaskResult(asyncTaskResult.getTask_id(), "盘点库存完成");
			Assert.assertEquals(result, true, "批量盘点SPU库存的异步任务执行失败");

			String spu_id = null;
			SpuStockBean spuStock = null;
			String msg = null;
			for (SpuStockCheckParam batchStockCheck : batchStockCheckList) {
				spu_id = batchStockCheck.getSpu_id();
				spuStock = stockCheckService.getSpuStock(spu_id);
				if (spuStock.getRemain().compareTo(batchStockCheck.getNew_stock()) != 0) {
					msg = String.format("商品%s经过批量盘点,库存数与预期的不一致,预期:%s,实际:%s", spu_id, batchStockCheck.getNew_stock(),
							spuStock.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "批量盘点过后,商品的实际库存和预期的不一致");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase04() {
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(5);

		ReporterCSS.title("测试点: 对商品库存进行修复库存均价操作");
		try {
			Assert.assertEquals(user_permissions.contains("import_stock_avg_price_repair"), true, "登录用户无修复库存均价操作权限");

			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			Assert.assertEquals(stockCheckList.size() > 0, true, "商品判断页面无数据,无法进行商品盘点操作");

			List<StockAvgPriceUpdateParam> updateParamList = new ArrayList<StockAvgPriceUpdateParam>();
			StockAvgPriceUpdateParam stockAvgPriceUpdateParam = null;
			for (SpuStockBean spuStock : stockCheckList) {
				stockAvgPriceUpdateParam = new StockAvgPriceUpdateParam();
				stockAvgPriceUpdateParam.setSpu_id(spuStock.getSpu_id());
				stockAvgPriceUpdateParam.setPrice(NumberUtil.getRandomNumber(3, 6, 1));
				updateParamList.add(stockAvgPriceUpdateParam);
			}

			boolean result = stockCheckService.updateStockAveragePrice(updateParamList);
			Assert.assertEquals(result, true, "修复库存均价,异步任务创建失败");

			Thread.sleep(1000);

			List<AsyncTaskResultBean> asyncTaskResultList = asyncService.getAsyncTaskResultList();
			Assert.assertNotEquals(asyncTaskResultList, null, "异步任务列表获取失败");

			AsyncTaskResultBean asyncTaskResult = asyncTaskResultList.stream().filter(a -> a.getType() == 0).findFirst()
					.orElse(null);
			Assert.assertNotEquals(asyncTaskResult, null, "批量盘点SPU库存的异步任务在异步任务列表中没有找到");

			result = asyncService.getAsyncTaskResult(asyncTaskResult.getTask_id(),
					"修改完成, 修复" + updateParamList.size() + "个");
			Assert.assertEquals(result, true, "批量盘点SPU库存的异步任务执行失败");

			String spu_id = null;
			SpuStockBean spuStock = null;
			String msg = null;
			for (StockAvgPriceUpdateParam stockAvgPrice : updateParamList) {
				spu_id = stockAvgPrice.getSpu_id();
				spuStock = stockCheckService.getSpuStock(spu_id);
				if (spuStock.getAvg_price().compareTo(stockAvgPrice.getPrice()) != 0) {
					msg = String.format("商品%s经过库存均价修复后,库存均价与预期的不一致,预期:%s,实际:%s", spu_id, stockAvgPrice.getPrice(),
							spuStock.getAvg_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "库存均价修复过后,商品的实际库存均价与预期的不一致");
		} catch (Exception e) {
			logger.error("商品盘点操作遇到错误: ", e);
			Assert.fail("商品盘点操作遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase05() {
		ReporterCSS.title("测试点: 导出批量盘点模板");
		try {
			BigDecimal task_id = stockCheckService.downloadStockCheckTemplate();
			Assert.assertNotEquals(task_id, null, "导出批量盘点模板异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "导出批量盘点模板异步任务执行失败");

			AsyncTaskResultBean asyncTaskResult = asyncService.getAsyncTaskResult(task_id);
			Assert.assertNotEquals(asyncTaskResult, null, "获取导出批量盘点模板异步任务详情失败");

			// 下载导出的批量盘点模板
			String link = asyncTaskResult.getResult().getLink();
			String file_path = stockCheckService.downloadStockCheckTemplateStep2(link);
			Assert.assertNotEquals(file_path, null, "下载导出的批量盘点模板失败");
		} catch (Exception e) {
			logger.error("批量盘点模板下载遇到错误: ", e);
			Assert.fail("批量盘点模板下载遇到错误: ", e);
		}

	}

	@Test
	public void stockCheckTestCase06() {
		ReporterCSS.title("测试点: 商品盘点数据导出");
		try {
			StockCheckFilterParam filterParam = new StockCheckFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setExport(1);

			boolean result = stockCheckService.downloadStockCheckFile(filterParam);
			Assert.assertEquals(result, true, "商品盘点数据导出失败");
		} catch (Exception e) {
			logger.error("商品盘点数据导出遇到错误: ", e);
			Assert.fail("商品盘点数据导出遇到错误: ", e);
		}

	}

	@Test
	public void stockCheckTestCase07() {
		ReporterCSS.title("测试点: 安全库存设置及提醒");
		String spu_id = null;
		try {
			Assert.assertEquals(user_permissions.contains("edit_stock_threshold"), true, "登录账号没有编辑安全库存的权限");

			StockCheckFilterParam filterParam = new StockCheckFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);

			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			Assert.assertEquals(stockCheckList.size() > 0, true, "商品盘点页面无数据,无法进行商品盘点操作");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(stockCheckList);

			spu_id = spuStock.getSpu_id();

			BigDecimal lower_threshold = new BigDecimal("20");
			boolean result = stockCheckService.setSpuStockLowerThreshold(spu_id, new BigDecimal("20"));
			Assert.assertEquals(result, true, "设置商品 " + spu_id + " 安全库存下限阈值失败");

			BigDecimal upper_threshold = new BigDecimal("100");
			result = stockCheckService.setSpuStockUpperThreshold(spu_id, new BigDecimal("100"));
			Assert.assertEquals(result, true, "设置商品 " + spu_id + " 安全库存上限阈值失败");

			spuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(spuStock, null, "获取商品" + spu_id + "库存信息失败");

			String msg = null;
			if (spuStock.getThreshold().compareTo(lower_threshold) != 0) {
				msg = String.format("商品%s显示的安全库存下限阈值与预期不符,预期:%s,实际:%s", spu_id, lower_threshold,
						spuStock.getThreshold());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (spuStock.getUpper_threshold().compareTo(upper_threshold) != 0) {
				msg = String.format("商品%s显示的安全库存上限阈值与预期不符,预期:%s,实际:%s", spu_id, lower_threshold,
						spuStock.getThreshold());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (user_permissions.contains("get_stock_threshold_warning")) {
				String stock_msg = stockCheckService.stockWarning();
				Assert.assertNotEquals(stock_msg, null, "安全库存提醒接口调用失败");
			} else {
				Assert.fail("登录用户没有库存不足提醒功能的权限");
			}
		} catch (Exception e) {
			logger.error("调用安全库存提醒接口遇到错误: ", e);
			Assert.fail("调用安全库存提醒接口遇到错误: ", e);
		} finally {
			if (spu_id != null) {
				try {
					boolean result = stockCheckService.cancelSpuStockLowerThreshold(spu_id);
					Assert.assertEquals(result, true, "取消商品 " + spu_id + " 库存告警下限阈值失败");
				} catch (Exception e) {
					logger.error("调用安全库存提醒接口遇到错误: ", e);
					Assert.fail("调用安全库存提醒接口遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void stockCheckTestCase08() {
		ReporterCSS.title("测试点: 获取SPU库存变动记录");
		StockCheckFilterParam filterParam = new StockCheckFilterParam();
		filterParam.setOffset(0);
		filterParam.setLimit(10);

		ReporterCSS.title("测试点: 获取商品盘点列表");
		try {
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");
			Assert.assertEquals(stockCheckList.size() > 0, true, "商品判断页面无数据,无法进行商品盘点操作");

			String spu_id = stockCheckList.get(0).getSpu_id();

			StockChangeLogFilterParam logFilterParam = new StockChangeLogFilterParam(spu_id, today, today);

			List<StockChangeLogBean> spuStockLogList = stockCheckService.getSpuStockChangeLogList(logFilterParam);
			Assert.assertNotEquals(spuStockLogList, null, "获取SPU库存变动记录失败");
		} catch (Exception e) {
			logger.error("获取SPU库存变动记录遇到错误: ", e);
			Assert.fail("获取SPU库存变动记录遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase09() {
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
	public void stockCheckTestCase10() {
		ReporterCSS.title("测试点: 商品盘点页面,使用商品一级分类进行过滤搜索");
		try {
			List<CategoriesBean> categories = stockCheckService.getCategories();
			Assert.assertNotNull(categories, "获取站点所有的一二级分类接口调用失败");

			List<CategoriesBean> category1_list = categories.stream().filter(c -> c.getId().startsWith("A"))
					.collect(Collectors.toList());

			String category1_id = category1_list.get(0).getId();

			StockCheckFilterParam filterParam = new StockCheckFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setCategory_id_1(category1_id);

			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			List<SpuStockBean> temp_list = stockCheckList.stream()
					.filter(s -> !s.getCategory_id_1().equals(category1_id)).collect(Collectors.toList());
			Assert.assertEquals(temp_list.size(), 0, "商品盘点页面,按一级分类过滤筛选,过滤出了不符合条件的数据");
		} catch (Exception e) {
			logger.error("商品盘点页面,使用商品一级分类进行过滤搜索遇到错误: ", e);
			Assert.fail("商品盘点页面,使用商品一级分类进行过滤搜索遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase11() {
		ReporterCSS.title("测试点: 商品盘点页面,使用商品二级分类进行过滤搜索");
		try {
			List<CategoriesBean> categories = stockCheckService.getCategories();
			Assert.assertNotNull(categories, "获取站点所有的一二级分类接口调用失败");

			List<CategoriesBean> category1_list = categories.stream().filter(c -> c.getId().startsWith("A"))
					.collect(Collectors.toList());

			String category1_id = category1_list.get(0).getId();

			List<CategoriesBean> category2_list = categories.stream()
					.filter(c -> c.getUpstream_id() != null && c.getUpstream_id().equals(category1_id))
					.collect(Collectors.toList());

			String category2_id = category2_list.get(0).getId();

			StockCheckFilterParam filterParam = new StockCheckFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setCategory_id_1(category1_id);
			filterParam.setCategory_id_2(category2_id);

			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			List<SpuStockBean> temp_list = stockCheckList.stream()
					.filter(s -> !s.getCategory_id_2().equals(category2_id)).collect(Collectors.toList());
			Assert.assertEquals(temp_list.size(), 0, "商品盘点页面,按二级分类过滤筛选,过滤出了不符合条件的数据");
		} catch (Exception e) {
			logger.error("商品盘点页面,使用商品二级分类进行过滤搜索遇到错误: ", e);
			Assert.fail("商品盘点页面,使用商品二级分类进行过滤搜索遇到错误: ", e);
		}
	}

	@Test
	public void stockCheckTestCase12() {
		ReporterCSS.title("测试点: 商品盘点页面,使用库存状态进行过滤搜索");
		try {
			StockCheckFilterParam filterParam = new StockCheckFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setRemain_status(1);

			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			List<SpuStockBean> temp_list = stockCheckList.stream()
					.filter(s -> s.getRemain().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
			boolean result = true;
			if (temp_list.size() > 0) {
				Reporter.log("商品盘点页面,使用库存状态(库存大于0)进行过滤搜索,过滤出了不符合条件的数据");
				result = false;
			}

			filterParam.setRemain_status(2);
			stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			temp_list = stockCheckList.stream().filter(s -> s.getRemain().compareTo(BigDecimal.ZERO) != 0)
					.collect(Collectors.toList());
			if (temp_list.size() > 0) {
				Reporter.log("商品盘点页面,使用库存状态(库存等于0)进行过滤搜索,过滤出了不符合条件的数据");
				result = false;
			}

			filterParam.setRemain_status(3);
			stockCheckList = stockCheckService.searchStockCheck(filterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			temp_list = stockCheckList.stream().filter(s -> s.getRemain().compareTo(BigDecimal.ZERO) >= 0)
					.collect(Collectors.toList());
			if (temp_list.size() > 0) {
				Reporter.log("商品盘点页面,使用库存状态(库存小于0)进行过滤搜索,过滤出了不符合条件的数据");
				result = false;
			}

			Assert.assertEquals(result, true, "商品盘点页面,使用库存状态进行过滤搜索,搜索结果与过滤条件不符");
		} catch (Exception e) {
			logger.error("商品盘点页面,使用库存状态进行过滤搜索遇到错误: ", e);
			Assert.fail("商品盘点页面,使用库存状态进行过滤搜索遇到错误: ", e);
		}
	}

}
