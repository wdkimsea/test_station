package cn.guanmai.station.category;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;

/**
 * @author: liming
 * @Date: 2020年5月18日 下午5:29:12
 * @description:
 * @version: 1.0
 */

public class MerchandiseTest extends LoginStation {
	private static Logger logger = LoggerFactory.getLogger(LoginStation.class);
	private static Map<String, String> headers;

	private CategoryService categoryService;

	@BeforeClass
	public void InitData() {
		headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
	}

	@Test
	public void merchandiseTestCase01() {
		ReporterCSS.title("测试点: 商品库分类导出");
		try {
			boolean result = categoryService.exportMerchandise();
			Assert.assertEquals(result, true, "商品库分类导出失败");
		} catch (Exception e) {
			logger.error("商品库分类导出遇到错误: ", e);
			Assert.fail("商品库分类导出遇到错误: ", e);
		}
	}
}
