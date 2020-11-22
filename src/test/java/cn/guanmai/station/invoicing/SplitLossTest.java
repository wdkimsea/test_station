package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.invoicing.SplitLossBean;
import cn.guanmai.station.bean.invoicing.SplitLossCountBean;
import cn.guanmai.station.bean.invoicing.param.SplitLossFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.invoicing.SplitServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.SplitService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年7月7日 上午11:05:15
 * @description:
 * @version: 1.0
 */

public class SplitLossTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SplitLossTest.class);

	private SplitService splitService;
	private AsyncService asyncService;

	private SplitLossFilterParam splitLossFilterParam;

	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String yestoday;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		splitService = new SplitServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号相关信息失败");

			JSONArray permissions = loginUserInfo.getUser_permission();
			Assert.assertEquals(permissions.contains("get_split_loss_toc"), true, "登录账号没有查询分割损耗的权限");

			yestoday = TimeUtil.calculateTime("yyyy-MM-dd", today, -1, Calendar.DATE);
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		splitLossFilterParam = new SplitLossFilterParam();
		splitLossFilterParam.setBegin(yestoday);
		splitLossFilterParam.setEnd(today);
		splitLossFilterParam.setAggregate_by_day(0);
	}

	@Test
	public void splitLossTestCase01() {
		ReporterCSS.title("测试点: 分割损耗总计");
		try {
			SplitLossCountBean splitLossCount = splitService.getSplitLossCount(splitLossFilterParam);
			Assert.assertNotEquals(splitLossCount, null, "分割损耗总计获取失败");
		} catch (Exception e) {
			logger.error("分割损耗查询遇到错误: ", e);
			Assert.fail("分割损耗查询遇到错误: ", e);
		}
	}

	@Test
	public void splitLossTestCase02() {
		ReporterCSS.title("测试点: 分割损耗列表");
		try {
			splitLossFilterParam.setLimit(10);
			splitLossFilterParam.setOffset(0);
			splitLossFilterParam.setPeek(60);

			List<SplitLossBean> splitLossList = splitService.searchSplitLoss(splitLossFilterParam);
			Assert.assertNotEquals(splitLossList, null, "分割损耗列表获取失败");
		} catch (Exception e) {
			logger.error("分割损耗查询遇到错误: ", e);
			Assert.fail("分割损耗查询遇到错误: ", e);
		}
	}

	@Test
	public void splitLossTestCase03() {
		ReporterCSS.title("测试点: 分割损耗列表按天统计");
		try {
			splitLossFilterParam.setLimit(10);
			splitLossFilterParam.setOffset(0);
			splitLossFilterParam.setPeek(60);
			splitLossFilterParam.setAggregate_by_day(1);

			List<SplitLossBean> splitLossList = splitService.searchSplitLoss(splitLossFilterParam);
			Assert.assertNotEquals(splitLossList, null, "分割损耗列表获取失败");

			String q = "大叶茼蒿";
			if (splitLossList.size() > 0) {
				SplitLossBean splitLoss = NumberUtil.roundNumberInList(splitLossList);
				q = splitLoss.getSource_spu_name();
			}
			splitLossFilterParam.setQ(q);

			splitLossList = splitService.searchSplitLoss(splitLossFilterParam);
			Assert.assertNotEquals(splitLossList, null, "分割损耗列表获取失败");
		} catch (Exception e) {
			logger.error("分割损耗查询遇到错误: ", e);
			Assert.fail("分割损耗查询遇到错误: ", e);
		}
	}

	@Test
	public void splitLossTestCase04() {
		ReporterCSS.title("测试点: 分割损耗列表导出");
		try {
			splitLossFilterParam.setLimit(10);
			splitLossFilterParam.setOffset(0);
			splitLossFilterParam.setPeek(60);
			splitLossFilterParam.setAggregate_by_day(1);
			splitLossFilterParam.setExport(1);

			BigDecimal task_id = splitService.exportSplitLoss(splitLossFilterParam);
			Assert.assertNotEquals(task_id, null, "分割损耗列表导出异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "分割损耗的异步任务执行失败");
		} catch (Exception e) {
			logger.error("分割损耗查询遇到错误: ", e);
			Assert.fail("分割损耗查询遇到错误: ", e);
		}
	}

}
