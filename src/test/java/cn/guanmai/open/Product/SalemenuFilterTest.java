package cn.guanmai.open.Product;

import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class SalemenuFilterTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuFilterTest.class);
	private OpenSalemenuService salemenu_srv;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();

		salemenu_srv = new OpenSalemenuServiceImpl(access_token);
	}

	@Test
	public void salemenuFilterTestCase01() {
		try {
			Reporter.log("测试点: 拉取站点所有的报价单");
			List<OpenSalemenuBean> salemenu_list = salemenu_srv.searchSalemenu(null, null);
			Assert.assertNotEquals(salemenu_list, null, "获取报价单列表失败");
			Assert.assertNotEquals(salemenu_list.size(), 0, "获取报价单列表为空");
		} catch (Exception e) {
			logger.error("获取获取报价单列表过程中遇到错误: ", e);
			Assert.fail("获取获取报价单列表过程中遇到错误: ", e);
		}
	}

	@Test
	public void salemenuFilterTestCase02() {
		try {
			Reporter.log("测试点: 搜索过滤报价单,customer_id传入别的站点的商户ID,搜索结果应该为空");
			List<OpenSalemenuBean> salemenu_list = salemenu_srv.searchSalemenu("S061040", null);
			Assert.assertNull(salemenu_list, "获取报价单列表失败");
		} catch (Exception e) {
			logger.error("获取获取报价单列表过程中遇到错误: ", e);
			Assert.fail("获取获取报价单列表过程中遇到错误: ", e);
		}
	}
}
