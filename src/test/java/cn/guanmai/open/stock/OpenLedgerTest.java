package cn.guanmai.open.stock;

import cn.guanmai.open.Product.SalemenuFilterTest;
import cn.guanmai.open.bean.stock.OpenStockLedgerBean;
import cn.guanmai.open.bean.stock.OpenStockLedgerDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockLegerFilterParam;
import cn.guanmai.open.impl.stock.OpenStockLedgerServiceImpl;
import cn.guanmai.open.interfaces.stock.OpenStockLedgerService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OpenLedgerTest extends AccessToken {
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
		ReporterCSS.title("测试点: 拉取总账列表");
		OpenStockLegerFilterParam stockLegerFilterParam = new OpenStockLegerFilterParam();
		try {
			stockLegerFilterParam.setStart_date(today);
			stockLegerFilterParam.setEnd_date(today);
			OpenStockLedgerBean ledger = openStockLedgerService.queryStockLeger(stockLegerFilterParam);
			Assert.assertNotNull(ledger, "拉取总账列表失败");
		} catch (Exception e) {
			logger.error("拉取总账列表遇到错误: ", e);
			Assert.fail("拉取总账列表遇到错误: ", e);
		}
	}

	@Test
	public void LedgerTestCase02() {
		ReporterCSS.title("测试点: 拉取总账详情");
		OpenStockLegerFilterParam stockLegerFilterParam = new OpenStockLegerFilterParam();

		try {
			stockLegerFilterParam.setStart_date(today);
			stockLegerFilterParam.setEnd_date(today);
			OpenStockLedgerBean ledger = openStockLedgerService.queryStockLeger(stockLegerFilterParam);
			Assert.assertNotNull(ledger, "拉取总账列表");

			Assert.assertNotEquals(ledger.getDetails().size(), 0, "无数据，无法执行此用例");

			stockLegerFilterParam.setSupplier_id(ledger.getDetails().get(0).getSupplier_id());

			OpenStockLedgerDetailBean ledgerDetail = openStockLedgerService.getStockLegerDetail(stockLegerFilterParam);
			Assert.assertNotNull(ledgerDetail, "拉取总账详情失败");

//			Assert.assertNotEquals(ledgerDetail.getDetails().size(), 0, "拉取总账详情失败");
		} catch (Exception e) {
			logger.error("拉取总账详遇到错误: ", e);
			Assert.fail("拉取总账详遇到错误: ", e);
		}
	}

}
