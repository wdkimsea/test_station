package cn.guanmai.open.stock.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.stock.OpenStockSaleRefundBean;
import cn.guanmai.open.bean.stock.param.OpenStockSaleRefundFilterParam;
import cn.guanmai.open.impl.stock.OpenStockSaleRefundServiceImpl;
import cn.guanmai.open.interfaces.stock.OpenStockSaleRefundService;
import cn.guanmai.open.stock.OpenStockSaleRefundTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午5:49:37
 * @des TODO
 */

public class OpenStockSaleRefundAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockSaleRefundTest.class);
	private OpenStockSaleRefundService openStockSaleRefundService;
	private OpenStockSaleRefundFilterParam openStockSaleRefundFilterParam;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockSaleRefundService = new OpenStockSaleRefundServiceImpl(access_token);
	}

	@BeforeMethod
	public void beforeMethod() {
		openStockSaleRefundFilterParam = new OpenStockSaleRefundFilterParam();
		openStockSaleRefundFilterParam.setStart_date(todayStr);
		openStockSaleRefundFilterParam.setEnd_date(todayStr);
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,搜索时间输入为空");
		try {
			openStockSaleRefundFilterParam.setStart_date("");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,搜索时间输入为空");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,时间格式输入不合法格式");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
			openStockSaleRefundFilterParam.setStart_date(todayStr);
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,时间格式输入不合法格式");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,状态值输入非法值");
		try {
			openStockSaleRefundFilterParam.setState("A");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,状态值输入非法值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,状态值输入非候选值");
		try {
			openStockSaleRefundFilterParam.setState("3");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,状态值输入非候选值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,offset设置非法值");
		try {
			openStockSaleRefundFilterParam.setOffset("A");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,offset设置非法值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,offset设置非法值");
		try {
			openStockSaleRefundFilterParam.setOffset("-1");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,offset设置非法值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,limit设置非法值");
		try {
			openStockSaleRefundFilterParam.setLimit("A");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,limit设置非法值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,limit设置非法值");
		try {
			openStockSaleRefundFilterParam.setLimit("-20");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,limit设置非法值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤商户退货单,limit设置过大值");
		try {
			openStockSaleRefundFilterParam.setLimit("101");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertEquals(openStockSaleRefundList, null, "异常测试,搜索过滤商户退货单,limit设置过大值");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

}
