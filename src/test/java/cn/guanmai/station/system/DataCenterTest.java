package cn.guanmai.station.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.impl.system.DataCenterServiceImpl;
import cn.guanmai.station.interfaces.system.DataCenterService;
import cn.guanmai.station.tools.LoginStation;

/* 
* @author liming 
* @date Jan 8, 2019 6:00:42 PM 
* @des 数据统计
* @version 1.0 
*/
public class DataCenterTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(DataCenterTest.class);

	private DataCenterService dataCenterService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		dataCenterService = new DataCenterServiceImpl(headers);
	}

	@Test
	public void dataCenterTestCase01() {
		Reporter.log("ST首页数据统计");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String start_date = format.format(new Date().getTime() - 7 * 24 * 60 * 60 * 1000);
		String end_date = format.format(new Date());
		try {
			boolean result = dataCenterService.dailyProfit(start_date, end_date, 1);
			Assert.assertEquals(result, true, "数据中心,获取首页统计数据失败");
		} catch (Exception e) {
			logger.error("数据中心,获取首页统计数据遇到错误: ", e);
			Assert.fail("数据中心,获取首页统计数据遇到错误: ", e);
		}
	}

	@Test
	public void dataCenterTestCase02() {
		Reporter.log("ST未读消息数量统计");
		try {
			Integer count = dataCenterService.unReadMessageCount();
			Assert.assertNotNull(count, "获取ST未读消息数量失败");
		} catch (Exception e) {
			logger.error("获取ST未读消息数量遇到错误: ", e);
			Assert.fail("获取ST未读消息数量遇到错误: ", e);
		}
	}
}
