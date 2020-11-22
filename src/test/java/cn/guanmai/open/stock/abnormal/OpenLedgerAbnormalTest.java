package cn.guanmai.open.stock.abnormal;

import cn.guanmai.open.Product.SalemenuFilterTest;
import cn.guanmai.open.bean.stock.OpenStockLedgerDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockLegerFilterParam;
import cn.guanmai.open.impl.stock.OpenStockLedgerServiceImpl;
import cn.guanmai.open.interfaces.stock.OpenStockLedgerService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OpenLedgerAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private OpenStockLedgerService openStockLedgerService;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeTest
	public void initData() {
		String access_token = getAccess_token();
		openStockLedgerService = new OpenStockLedgerServiceImpl(access_token);
	}

	@Test
	public void LedgerTestCase01() {
		Reporter.log("测试点: 拉取总账详情，传入错误的供应商id");

		OpenStockLegerFilterParam stockLegerFilterParam = new OpenStockLegerFilterParam();

		try {
			stockLegerFilterParam.setStart_date(today);
			stockLegerFilterParam.setEnd_date(today);

			stockLegerFilterParam.setSupplier_id("SS1");

			OpenStockLedgerDetailBean ledgerDetail = openStockLedgerService.getStockLegerDetail(stockLegerFilterParam);
			Assert.assertNull(ledgerDetail, "拉取总账详情");

		} catch (Exception e) {
			logger.error("拉取总账详遇到错误: ", e);
			Assert.fail("拉取总账详遇到错误: ", e);
		}
	}

}
