package cn.guanmai.station.jingcai;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.jingcai.TechnicCategoryBean;
import cn.guanmai.station.impl.jingcai.TechnicServiceImpl;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年4月27日 上午11:26:45
 * @description:
 * @version: 1.0
 */

public class TechnicCategoryTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(TechnicTest.class);
	private TechnicService technicService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		technicService = new TechnicServiceImpl(headers);
	}

	@Test
	public void technicCategoryTestCase01() {
		ReporterCSS.title("测试点: 新建工艺类型");
		TechnicCategoryBean technicCategory = null;
		try {
			String name = "TMP" + StringUtil.getRandomNumber(4);
			String id = technicService.createTechnicCategory(name);
			Assert.assertNotEquals(id, null, "新建工艺类型失败");

			List<TechnicCategoryBean> technicCategorys = technicService.searchTechnicCategory(name);
			Assert.assertNotEquals(technicCategorys, null, "获取工艺类型列表失败");

			technicCategory = technicCategorys.stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);

			Assert.assertNotEquals(technicCategory, null, "新建的工艺类型没有在列表中找到");

			Assert.assertEquals(technicCategory.getName(), name, "新建的工艺类型名称与预期的不一致");
		} catch (Exception e) {
			logger.error("新建工艺类型出现错误: ", e);
			Assert.fail("新建工艺类型出现错误: ", e);
		} finally {
			if (technicCategory != null) {
				try {
					technicService.deleteTechnicCategory(technicCategory.getId());
				} catch (Exception e) {
					logger.error("删除工艺类型出现错误: ", e);
					Assert.fail("删除工艺类型出现错误: ", e);
				}
			}
		}
	}

	@Test
	public void technicCategoryTestCase02() {
		ReporterCSS.title("测试点: 删除工艺类型");
		try {
			String name = "TMP" + StringUtil.getRandomNumber(4);
			String id = technicService.createTechnicCategory(name);
			Assert.assertNotEquals(id, null, "新建工艺类型失败");

			List<TechnicCategoryBean> technicCategorys = technicService.searchTechnicCategory(name);
			Assert.assertNotEquals(technicCategorys, null, "获取工艺类型列表失败");

			TechnicCategoryBean technicCategory = technicCategorys.stream().filter(t -> t.getId().equals(id)).findAny()
					.orElse(null);

			Assert.assertNotEquals(technicCategory, null, "新建的工艺类型没有在列表中找到");

			boolean result = technicService.deleteTechnicCategory(technicCategory.getId());

			Assert.assertEquals(result, true, "删除工艺类型失败");

			technicCategorys = technicService.searchTechnicCategory(name);
			Assert.assertNotEquals(technicCategorys, null, "获取工艺类型列表失败");

			technicCategory = technicCategorys.stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);

			Assert.assertEquals(technicCategory, null, "工艺类型没有真正删除成功");
		} catch (Exception e) {
			logger.error("删除工艺类型出现错误: ", e);
			Assert.fail("删除工艺类型出现错误: ", e);
		}
	}

	@AfterClass
	public void afterClasss() {
		try {
			List<TechnicCategoryBean> technicCategorys = technicService.searchTechnicCategory("TMP");
			Assert.assertNotEquals(technicCategorys, null, "获取工艺类型列表失败");

			for (TechnicCategoryBean technicCategory : technicCategorys) {
				boolean result = technicService.deleteTechnicCategory(technicCategory.getId());

				Assert.assertEquals(result, true, "删除工艺类型失败");
			}
		} catch (Exception e) {
			logger.error("删除工艺类型出现错误: ", e);
			Assert.fail("删除工艺类型出现错误: ", e);
		}
	}
}
