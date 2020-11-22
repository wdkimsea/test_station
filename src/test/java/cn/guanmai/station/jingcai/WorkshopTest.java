package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.account.RoleBean;
import cn.guanmai.station.bean.jingcai.TechnicBean;
import cn.guanmai.station.bean.jingcai.TechnicCategoryBean;
import cn.guanmai.station.bean.jingcai.WorkshopBean;
import cn.guanmai.station.bean.jingcai.param.TechnicFilterParam;
import cn.guanmai.station.bean.jingcai.param.WorkshopFilterParam;
import cn.guanmai.station.bean.jingcai.param.WorkshopParam;
import cn.guanmai.station.impl.jingcai.TechnicServiceImpl;
import cn.guanmai.station.impl.jingcai.WorkshopServiceImpl;
import cn.guanmai.station.impl.system.AccountServiceImpl;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.interfaces.jingcai.WorkshopService;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:52:51
 * @description:
 * @version: 1.0
 */

public class WorkshopTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(WorkshopTest.class);
	private WorkshopService workshopService;
	private AccountService accountService;
	private TechnicService technicService;
	private String technic_id;
	private WorkshopParam workshopParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		workshopService = new WorkshopServiceImpl(headers);
		accountService = new AccountServiceImpl(headers);
		technicService = new TechnicServiceImpl(headers);
		try {
			TechnicFilterParam technicFilterParam = new TechnicFilterParam();
			technicFilterParam.setQ("清洗");
			List<TechnicBean> technics = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technics, null, "搜索过滤工艺信息失败");

			TechnicBean technic = technics.stream().filter(t -> t.getName().equals("清洗")).findFirst().orElse(null);
			if (technic == null) {
				String technic_category_name = "手工";
				List<TechnicCategoryBean> technicCategorys = technicService
						.searchTechnicCategory(technic_category_name);
				Assert.assertNotEquals(technicCategorys, null, "获取工艺类型失败");

				TechnicCategoryBean technicCategory = technicCategorys.stream()
						.filter(t -> t.getName().equals(technic_category_name)).findFirst().orElse(null);

				String technic_category_id = null;
				if (technicCategory == null) {
					technic_category_id = technicService.createTechnicCategory(technic_category_name);
					Assert.assertNotEquals(technic_category_id, null, "新建工艺类型失败");
				} else {
					technic_category_id = technicCategory.getId();
				}

				List<RoleBean> roles = accountService.searchRole(null, null);
				Assert.assertNotEquals(roles, null, "拉取站点角色失败");

				RoleBean role = NumberUtil.roundNumberInList(roles);
				BigDecimal role_id = role.getId();

				TechnicBean technicParam = new TechnicBean();
				String technic_name = "清洗";
				String custom_id = "GY-" + StringUtil.getRandomNumber(4);
				String desc = "自动化创建";
				technicParam.setName(technic_name);
				technicParam.setCustom_id(custom_id);
				technicParam.setDesc(desc);
				technicParam.setTechnic_category_id(technic_category_id);
				technicParam.setDefault_role(role_id);

				List<TechnicBean.CustomCol> customCols = new ArrayList<>();
				TechnicBean.CustomCol customCol = technicParam.new CustomCol();
				customCol.setCol_name("清洗");

				List<TechnicBean.CustomCol.Param> params = new ArrayList<>();
				TechnicBean.CustomCol.Param param = customCol.new Param();
				param.setParam_name("浸泡");
				params.add(param);

				param = customCol.new Param();
				param.setParam_name("刷洗");
				params.add(param);

				customCol.setParams(params);

				customCols.add(customCol);
				technicParam.setCustom_cols(customCols);

				technic_id = technicService.createTechnic(technicParam);
				Assert.assertNotEquals(technic_id, null, "新建工艺信息失败");
			} else {
				technic_id = technic.getId();
			}
		} catch (Exception e) {
			logger.error("初始化工艺信息出现错误: ", e);
			Assert.fail("初始化工艺信息出现错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		workshopParam = new WorkshopParam();
		workshopParam.setName(StringUtil.getRandomString(6).toUpperCase());
		workshopParam.setCustom_id("TMP-" + StringUtil.getRandomNumber(4));
		workshopParam.setTechnics(Arrays.asList(technic_id));
	}

	@Test
	public void workshopTestCase01() {
		ReporterCSS.title("测试点: 新建车间");
		try {
			String workshop_id = workshopService.createWorkshop(workshopParam);
			Assert.assertNotEquals(workshop_id, null, "新建车间失败");

			WorkshopFilterParam workshopFilterParam = new WorkshopFilterParam();
			workshopFilterParam.setQ(workshopParam.getName());

			List<WorkshopBean> workshops = workshopService.searchWorkshop(workshopFilterParam);
			Assert.assertNotEquals(workshops, null, "搜索过滤车间失败");

			WorkshopBean workshop = workshops.stream()
					.filter(w -> w.getCustom_id().equals(workshopParam.getCustom_id())).findAny().orElse(null);
			Assert.assertNotEquals(workshop, null, "车间搜索过滤,没有找到新建的车间条目信息");

			boolean result = compareResult(workshopParam, workshop);
			Assert.assertEquals(result, true, "新建车间所填信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建车间遇到错误: ", e);
			Assert.fail("新建车间遇到错误: ", e);
		}
	}

	@Test
	public void workshopTestCase02() {
		ReporterCSS.title("测试点: 修改车间信息");
		try {
			String workshop_id = workshopService.createWorkshop(workshopParam);
			Assert.assertNotEquals(workshop_id, null, "新建车间失败");

			WorkshopParam workshopUpdateParam = new WorkshopParam();
			workshopUpdateParam.setWorkshop_id(workshop_id);
			workshopUpdateParam.setName(StringUtil.getRandomString(6).toUpperCase());
			workshopUpdateParam.setCustom_id("TMP-" + StringUtil.getRandomNumber(4));
			workshopUpdateParam.setTechnics(new ArrayList<String>());

			boolean result = workshopService.updateWorkshop(workshopUpdateParam);
			Assert.assertEquals(result, true, "修改车间信息失败");

			WorkshopBean workshop = workshopService.getWorkshop(workshop_id);
			Assert.assertNotEquals(workshop, null, "获取车间信息失败");

			result = compareResult(workshopUpdateParam, workshop);
			Assert.assertEquals(result, true, "新建车间所填信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("修改车间信息遇到错误: ", e);
			Assert.fail("修改车间信息遇到错误: ", e);
		}
	}

	@Test
	public void workshopTestCase03() {
		ReporterCSS.title("测试点: 搜索过滤车间信息");
		try {
			String workshop_id = workshopService.createWorkshop(workshopParam);
			Assert.assertNotEquals(workshop_id, null, "新建车间失败");

			WorkshopFilterParam workshopFilterParam = new WorkshopFilterParam();
			workshopFilterParam.setQ(workshopParam.getName());

			List<WorkshopBean> workshops = workshopService.searchWorkshop(workshopFilterParam);
			Assert.assertNotEquals(workshops, null, "搜索过滤车间失败");

			WorkshopBean workshop = workshops.stream()
					.filter(w -> w.getCustom_id().equals(workshopParam.getCustom_id())).findAny().orElse(null);
			Assert.assertNotEquals(workshop, null, "车间搜索过滤,没有找到新建的车间条目信息");

			List<WorkshopBean> tempWorkshops = workshops.stream()
					.filter(s -> !s.getName().contains(workshopFilterParam.getQ())
							&& !s.getCustom_id().contains(workshopFilterParam.getQ()))
					.collect(Collectors.toList());

			Assert.assertEquals(tempWorkshops.size(), 0, "输入车间名称搜索过滤车间信息,过滤出了不符合条件的数据");

		} catch (Exception e) {
			logger.error("新建车间遇到错误: ", e);
			Assert.fail("新建车间遇到错误: ", e);
		}
	}

	@Test
	public void workshopTestCase04() {
		ReporterCSS.title("测试点: 搜索过滤车间信息");
		try {
			String workshop_id = workshopService.createWorkshop(workshopParam);
			Assert.assertNotEquals(workshop_id, null, "新建车间失败");

			WorkshopFilterParam workshopFilterParam = new WorkshopFilterParam();
			workshopFilterParam.setQ(workshopParam.getCustom_id());

			List<WorkshopBean> workshops = workshopService.searchWorkshop(workshopFilterParam);
			Assert.assertNotEquals(workshops, null, "搜索过滤车间失败");

			WorkshopBean workshop = workshops.stream()
					.filter(w -> w.getCustom_id().equals(workshopParam.getCustom_id())).findAny().orElse(null);
			Assert.assertNotEquals(workshop, null, "车间搜索过滤,没有找到新建的车间条目信息");

			List<WorkshopBean> tempWorkshops = workshops.stream()
					.filter(s -> !s.getName().contains(workshopFilterParam.getQ())
							&& !s.getCustom_id().contains(workshopFilterParam.getQ()))
					.collect(Collectors.toList());

			Assert.assertEquals(tempWorkshops.size(), 0, "输入车间编号搜索过滤车间信息,过滤出了不符合条件的数据");
		} catch (Exception e) {
			logger.error("新建车间遇到错误: ", e);
			Assert.fail("新建车间遇到错误: ", e);
		}
	}

	@Test
	public void workshopTestCase05() {
		ReporterCSS.title("测试点: 删除车间");
		try {
			String workshop_id = workshopService.createWorkshop(workshopParam);
			Assert.assertNotEquals(workshop_id, null, "新建车间失败");

			boolean result = workshopService.deleteWorkshop(workshop_id);
			Assert.assertEquals(result, true, "删除车间失败");

			WorkshopFilterParam workshopFilterParam = new WorkshopFilterParam();
			workshopFilterParam.setQ(workshopParam.getCustom_id());

			List<WorkshopBean> workshops = workshopService.searchWorkshop(workshopFilterParam);
			Assert.assertNotEquals(workshops, null, "搜索过滤车间失败");

			WorkshopBean workshop = workshops.stream()
					.filter(w -> w.getCustom_id().equals(workshopParam.getCustom_id())).findAny().orElse(null);
			Assert.assertEquals(workshop, null, "车间删除没有真正删除成功");
		} catch (Exception e) {
			logger.error("删除车间遇到错误: ", e);
			Assert.fail("删除车间遇到错误: ", e);
		}
	}

	public boolean compareResult(WorkshopParam workshopParam, WorkshopBean workshop) {
		String msg = null;
		boolean result = true;
		if (!workshopParam.getName().equals(workshop.getName())) {
			msg = String.format("新建车间所填的名称与预期的不一致,预期:%s,实际:%s", workshopParam.getName(), workshop.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!workshopParam.getCustom_id().equals(workshop.getCustom_id())) {
			msg = String.format("新建车间所填的车间编号与预期的不一致,预期:%s,实际:%s", workshopParam.getCustom_id(),
					workshop.getCustom_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<String> technics = workshop.getTechnics().stream().map(w -> w.getTechnic_id())
				.collect(Collectors.toList());

		if (!workshopParam.getTechnics().equals(technics)) {
			msg = String.format("新建车间所填的关联工艺与预期的不一致,预期:%s,实际:%s", workshopParam.getTechnics(), technics);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;
	}

	@AfterClass
	public void afterClass() {
		ReporterCSS.title("后置处理: 删除车间信息");
		WorkshopFilterParam workshopFilterParam = new WorkshopFilterParam();
		workshopFilterParam.setQ("TMP");
		try {
			List<WorkshopBean> workshops = workshopService.searchWorkshop(workshopFilterParam);
			Assert.assertNotEquals(workshops, null, "搜索过滤车间失败");
			for (WorkshopBean workshop : workshops) {
				if (workshop.getCustom_id().startsWith("TMP")) {
					boolean result = workshopService.deleteWorkshop(workshop.getWorkshop_id());
					Assert.assertEquals(result, true, "删除车间信息失败");
				}
			}
		} catch (Exception e) {
			logger.error("删除车间遇到错误: ", e);
			Assert.fail("删除车间遇到错误: ", e);
		}
	}
}
