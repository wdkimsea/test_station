package cn.guanmai.station.system;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.system.BoxTemplateBean;
import cn.guanmai.station.bean.system.DistributeTemplateBean;
import cn.guanmai.station.bean.system.PrintTagTemplateBean;
import cn.guanmai.station.bean.system.PurchaseTemplateBean;
import cn.guanmai.station.bean.system.SettleTemplateBean;
import cn.guanmai.station.bean.system.StockInTemplateBean;
import cn.guanmai.station.bean.system.StockOutTemplateBean;
import cn.guanmai.station.impl.system.TemplateServiceImpl;
import cn.guanmai.station.interfaces.system.TemplateService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Jun 20, 2019 7:36:26 PM 
* @des 打印模板测试
*      由于 ETag的问题,导致访问出现问题
* @version 1.0 
*/
public class PrintTemplateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PrintTemplateTest.class);

	private TemplateService printTemplateService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		printTemplateService = new TemplateServiceImpl(headers);
	}

	@Test
	public void printTemplateTestCase01() {
		try {
			ReporterCSS.title("测试点: 获取采购模板列表以及详细信息");
			List<PurchaseTemplateBean> printTemplateList = printTemplateService.getPurchaseTemplateList();
			Assert.assertNotEquals(printTemplateList, null, "获取采购模板列表失败");

			Assert.assertEquals(printTemplateList.size() > 0, true, "此站点无采购默认模板");

			String id = printTemplateList.get(0).getId();

			PurchaseTemplateBean.Content content = printTemplateService.getPurchaseTemplateDetail(id);
			Assert.assertNotEquals(content, null, "获取采购模板详细信息失败");
		} catch (Exception e) {
			logger.error("获取采购模板列表遇到错误: ", e);
			Assert.fail("获取采购模板列表遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase02() {
		try {
			ReporterCSS.title("测试点: 获取入库模板列表以及详细信息");
			List<StockInTemplateBean> stockInTemplateList = printTemplateService.getStockInTemplateList();
			Assert.assertNotEquals(stockInTemplateList, null, "获取入库模板列表失败");

			Assert.assertEquals(stockInTemplateList.size() > 0, true, "此站点无入库默认模板");

			String id = stockInTemplateList.get(0).getId();

			StockInTemplateBean.Content content = printTemplateService.getStockInTemplateDetail(id);
			Assert.assertNotEquals(content, null, "获取入库模板详细信息失败");
		} catch (Exception e) {
			logger.error("获取入库模板列表遇到错误: ", e);
			Assert.fail("获取入库模板列表遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase03() {
		try {
			ReporterCSS.title("测试点: 获取分拣标签模板列表以及详细信息");
			List<PrintTagTemplateBean> printTagTemplateList = printTemplateService.getPrintTagTemplateList();
			Assert.assertNotEquals(printTagTemplateList, null, "获取分拣标签模板列表失败");

			Assert.assertEquals(printTagTemplateList.size() > 0, true, "此站点无入库默认模板");

			String id = printTagTemplateList.get(0).getId();

			PrintTagTemplateBean.Content content = printTemplateService.getPrintTagTemplateDetail(id);
			Assert.assertNotEquals(content, null, "获取分拣标签模板详细信息失败");
		} catch (Exception e) {
			logger.error("获取分拣标签模板列表遇到错误: ", e);
			Assert.fail("获取分拣标签模板列表遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase04() {
		try {
			ReporterCSS.title("测试点: 获取配送模板列表以及详细信息");
			List<DistributeTemplateBean> distributeTemplateList = printTemplateService.getDistributeTemplateList();
			Assert.assertNotEquals(distributeTemplateList, null, "获取配送模板列表信息失败");

			Assert.assertEquals(distributeTemplateList.size() > 0, true, "此站点无配送默认模板");

			String id = distributeTemplateList.get(0).getId();

			DistributeTemplateBean distributeTemplate = printTemplateService.getDistributeTemplateDetailInfo(id);
			Assert.assertNotEquals(distributeTemplate, null, "获取配送模板详细信息失败");

		} catch (Exception e) {
			logger.error("获取配送模板列表以及详细信息遇到错误: ", e);
			Assert.fail("获取配送模板列表以及详细信息遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase05() {
		try {
			ReporterCSS.title("测试点: 获取结算模板列表以及详细信息");
			List<SettleTemplateBean> settleTemplateList = printTemplateService.getSettleTemplateList();
			Assert.assertNotEquals(settleTemplateList, null, "获取结算模板列表信息失败");

			Assert.assertEquals(settleTemplateList.size() > 0, true, "此站点无结款默认模板");

			String id = settleTemplateList.get(0).getId();

			SettleTemplateBean.Content settleTemplate = printTemplateService.getSettleTemplateDetailInfo(id);
			Assert.assertNotEquals(settleTemplate, null, "获取结款模板详细信息失败");

		} catch (Exception e) {
			logger.error("获取结款模板列表以及详细信息遇到错误: ", e);
			Assert.fail("获取结款模板列表以及详细信息遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase06() {
		try {
			ReporterCSS.title("测试点: 获取出库模板列表以及详细信息");
			List<StockOutTemplateBean> stockOutTemplateList = printTemplateService.getStockOutTemplateList();
			Assert.assertNotEquals(stockOutTemplateList, null, "获取出库模板列表失败");

			Assert.assertEquals(stockOutTemplateList.size() > 0, true, "此站点无入库默认模板");

			String id = stockOutTemplateList.get(0).getId();

			StockOutTemplateBean.Content content = printTemplateService.getStockOutTemplateDetail(id);
			Assert.assertNotEquals(content, null, "获取出库单模板详细信息失败");
		} catch (Exception e) {
			logger.error("获取入库模板列表遇到错误: ", e);
			Assert.fail("获取入库模板列表遇到错误: ", e);
		}
	}

	@Test
	public void printTemplateTestCase07() {
		try {
			ReporterCSS.title("测试点: 获取装箱模板列表以及详细信息");
			List<BoxTemplateBean> boxTemplateList = printTemplateService.getBoxTemplateList();
			Assert.assertNotEquals(boxTemplateList, null, "获取装箱模板列表失败");

			if (boxTemplateList.size() > 0) {
				String id = boxTemplateList.get(0).getId();

				BoxTemplateBean content = printTemplateService.getBoxTemplateDetail(id);
				Assert.assertNotEquals(content, null, "获取装箱模板详细信息失败");
			}
		} catch (Exception e) {
			logger.error("获取装箱模板列表遇到错误: ", e);
			Assert.fail("获取装箱模板列表遇到错误: ", e);
		}
	}

}
