package cn.guanmai.open.purchase.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.impl.purchase.OpenPurcahseServiceImpl;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;

/**
 * @author liming
 * @date 2019年11月14日
 * @time 上午10:20:24
 * @des TODO
 */

public class OpenPurchaserAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaserAbnormalTest.class);
	private OpenPurcahseService openPurcahseService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
	}

	@Test
	public void openPurchaserAbnormalTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤采购员,输入不存在的供应商名称查找,预期结果为空");
		try {
			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			openPurchaserFilterParam.setSupplier_id("T7936");
			List<OpenPurcahserBean> openPurcahsers = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
			Assert.assertEquals(openPurcahsers, null, "搜索过滤采购员失败");
		} catch (Exception e) {
			logger.error("根据供应商查询采购员遇到错误: ", e);
			Assert.fail("根据供应商查询采购员遇到错误: ", e);
		}
	}
}
