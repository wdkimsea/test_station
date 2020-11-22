package cn.guanmai.station.invoicing;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.param.StockCostReportFilterParam;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年7月24日 下午4:04:22
 * @des 货值成本表
 * @version 1.0
 */
public class StockCostTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockCheckTest.class);
	private StockService stockService;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private StockCostReportFilterParam filterParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockService = new StockServiceImpl(headers);

		try {
			filterParam = new StockCostReportFilterParam();
			String begin_time = TimeUtil.calculateTime("yyyy-MM-dd", today, -7, Calendar.DATE);
			filterParam.setBegin(begin_time);
			filterParam.setEnd(today);
			filterParam.setLimit(10);
			filterParam.setOffset(0);
		} catch (ParseException e) {
			logger.error("参数封装遇到错误: ", e);
			Assert.fail("参数封装遇到错误: ", e);
		}
	}

	@Test
	public void stockCostTestCase01() {
		try {
			Reporter.log("测试点: 按商品分类查看货值成本数据");
			filterParam.setView_type(1);
			boolean result = stockService.getStockCostReport(filterParam);
			Assert.assertEquals(result, true, "获取货值成本表数据失败");
		} catch (Exception e) {
			logger.error("获取货值成本表数据遇到错误: ", e);
			Assert.fail("获取货值成本表数据遇到错误: ", e);
		}
	}

	@Test
	public void stockCostTestCase02() {
		try {
			Reporter.log("测试点: 按一级分类查看货值成本数据");
			filterParam.setView_type(2);
			boolean result = stockService.getStockCostReport(filterParam);
			Assert.assertEquals(result, true, "获取货值成本表数据失败");
		} catch (Exception e) {
			logger.error("获取货值成本表数据遇到错误: ", e);
			Assert.fail("获取货值成本表数据遇到错误: ", e);
		}
	}

	@Test
	public void stockCostTestCase03() {
		try {
			Reporter.log("测试点: 按二级分类查看货值成本数据");
			filterParam.setView_type(3);
			boolean result = stockService.getStockCostReport(filterParam);
			Assert.assertEquals(result, true, "获取货值成本表数据失败");
		} catch (Exception e) {
			logger.error("获取货值成本表数据遇到错误: ", e);
			Assert.fail("获取货值成本表数据遇到错误: ", e);
		}
	}

}
