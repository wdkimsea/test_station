package cn.guanmai.open.stock;

import java.util.List;
import java.util.stream.Collectors;

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
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午5:29:47
 * @des TODO
 */

public class OpenStockSaleRefundTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockSaleRefundTest.class);
	private OpenStockSaleRefundService openStockSaleRefundService;
	private OpenStockSaleRefundFilterParam openStockSaleRefundFilterParam;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		openStockSaleRefundService = new OpenStockSaleRefundServiceImpl(getAccess_token());
	}

	@BeforeMethod
	public void beforeMethod() {
		openStockSaleRefundFilterParam = new OpenStockSaleRefundFilterParam();
		openStockSaleRefundFilterParam.setStart_date(todayStr);
		openStockSaleRefundFilterParam.setEnd_date(todayStr);
	}

	@Test
	public void openStockSaleRefundTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤商户退货单");
		try {
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundTestCase02() {
		ReporterCSS.title("测试点: 搜索过滤商户退货单");
		try {
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			Assert.assertEquals(openStockSaleRefundList.size() > 0, true, "商户退货无条目数,无法进行后续测试");

			String order_id = NumberUtil.roundNumberInList(openStockSaleRefundList).getOrder_id();

			openStockSaleRefundFilterParam.setOrder_id(order_id);

			openStockSaleRefundList = openStockSaleRefundService.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			Assert.assertEquals(openStockSaleRefundList.size() >= 1, true, "商户退货入库列表,以订单ID过滤,过滤出的条目数应该大于等于1");

			List<OpenStockSaleRefundBean> tempList = openStockSaleRefundList.stream()
					.filter(s -> !s.getOrder_id().equals(order_id)).collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "商户退货入库列表,以订单号过滤,过滤出了不合符条件的数据");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundTestCase03() {
		ReporterCSS.title("测试点: 搜索过滤商户退货单");
		try {
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			Assert.assertEquals(openStockSaleRefundList.size() > 0, true, "商户退货无条目数,无法进行后续测试");

			String address_id = NumberUtil.roundNumberInList(openStockSaleRefundList).getAddress_id();

			openStockSaleRefundFilterParam.setAddress_id(address_id);

			openStockSaleRefundList = openStockSaleRefundService.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			Assert.assertEquals(openStockSaleRefundList.size() >= 1, true, "商户退货入库列表,以商户ID过滤,过滤出的条目数应该大于等于1");

			List<OpenStockSaleRefundBean> tempList = openStockSaleRefundList.stream()
					.filter(s -> !s.getAddress_id().equals(address_id)).collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "商户退货入库列表,以商户ID过滤,过滤出了不合符条件的数据");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundTestCase04() {
		ReporterCSS.title("测试点: 搜索过滤商户退货单");
		try {
			openStockSaleRefundFilterParam.setState("1");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			List<OpenStockSaleRefundBean> tempList = openStockSaleRefundList.stream().filter(s -> s.getState() != 1)
					.collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "商户退货入库列表,以状态值过滤,过滤出了不合符条件的数据");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

	@Test
	public void openStockSaleRefundTestCase05() {
		ReporterCSS.title("测试点: 搜索过滤商户退货单");
		try {
			openStockSaleRefundFilterParam.setState("2");
			List<OpenStockSaleRefundBean> openStockSaleRefundList = openStockSaleRefundService
					.filterStockSaleRefund(openStockSaleRefundFilterParam);
			Assert.assertNotEquals(openStockSaleRefundList, null, "搜索过滤商户退货单失败");

			List<OpenStockSaleRefundBean> tempList = openStockSaleRefundList.stream().filter(s -> s.getState() != 2)
					.collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "商户退货入库列表,以状态值过滤,过滤出了不合符条件的数据");
		} catch (Exception e) {
			logger.error("搜索过滤商户退货单遇到错误: ", e);
			Assert.fail("搜索过滤商户退货单遇到错误: ", e);
		}
	}

}
