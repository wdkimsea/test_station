package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import cn.guanmai.station.bean.jingcai.param.TechnicFilterParam;
import cn.guanmai.station.impl.jingcai.TechnicServiceImpl;
import cn.guanmai.station.impl.system.AccountServiceImpl;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年8月7日 上午11:32:25
 * @des
 * @version 1.0
 */
public class TechnicTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(TechnicTest.class);
	private TechnicService technicService;
	private AccountService accountService;
	private TechnicBean technicParam;
	private String technic_category_id;
	private String technic_id;
	private BigDecimal role_id;
	private TechnicFilterParam technicFilterParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		technicService = new TechnicServiceImpl(headers);
		accountService = new AccountServiceImpl(headers);
		try {
			String technic_category_name = "手工";
			List<TechnicCategoryBean> technicCategorys = technicService.searchTechnicCategory(technic_category_name);
			Assert.assertNotEquals(technicCategorys, null, "获取工艺类型失败");

			TechnicCategoryBean technicCategory = technicCategorys.stream()
					.filter(t -> t.getName().equals(technic_category_name)).findFirst().orElse(null);

			if (technicCategory == null) {
				technic_category_id = technicService.createTechnicCategory(technic_category_name);
				Assert.assertNotEquals(technic_category_id, null, "新建工艺类型失败");
			} else {
				technic_category_id = technicCategory.getId();
			}

			List<RoleBean> roles = accountService.searchRole(null, null);
			Assert.assertNotEquals(roles, null, "拉取站点角色失败");

			RoleBean role = NumberUtil.roundNumberInList(roles);
			role_id = role.getId();
		} catch (Exception e) {
			logger.error("初始化工艺类型出现错误: ", e);
			Assert.fail("初始化工艺类型出现错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		technicParam = new TechnicBean();
		technicFilterParam = new TechnicFilterParam();
		String technic_name = StringUtil.getRandomString(6).toUpperCase();
		String custom_id = "TMP-" + StringUtil.getRandomNumber(4);
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
	}

	@Test(timeOut = 10000)
	public void technicTestCase01() {
		ReporterCSS.title("测试点: 新建工艺信息");
		try {
			technic_id = technicService.createTechnic(technicParam);
			Assert.assertNotEquals(technic_id, null, "新建工艺信息失败");

			TechnicBean technic = technicService.getTechnic(technic_id);
			Assert.assertNotEquals(technic, null, "获取工艺信息 " + technic_id + " 详细信息失败");

			boolean result = compareResult(technicParam, technic);
			Assert.assertEquals(result, true, "新建工艺信息填写的信息与查询到的不一致");

			technicFilterParam.setQ(technicParam.getName());
			List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

			technic = technicList.stream().filter(t -> t.getName().equals(technicParam.getName())).findAny()
					.orElse(null);
			Assert.assertNotEquals(technic, null, "新建的工艺信息在工艺列表中没有找到");

		} catch (Exception e) {
			logger.error("新建工艺出现错误: ", e);
			Assert.fail("新建工艺出现错误: ", e);
		}
	}

	@Test
	public void technicTestCase02() {
		ReporterCSS.title("测试点: 修改工艺信息");
		String technic_category_id = null;
		try {
			// 新建一个工艺类型
			technic_category_id = technicService.createTechnicCategory(StringUtil.getRandomString(4).toUpperCase());
			Assert.assertNotEquals(technic_category_id, null, "新建工艺类型失败");

			technic_id = technicService.createTechnic(technicParam);
			Assert.assertNotEquals(technic_id, null, "新建工艺信息失败");

			TechnicBean technic = technicService.getTechnic(technic_id);
			Assert.assertNotEquals(technic, null, "获取工艺信息 " + technic_id + " 详细信息失败");

			String technic_name = StringUtil.getRandomString(6).toUpperCase();
			String custom_id = "TMP-" + StringUtil.getRandomNumber(4);
			String desc = "自动化创建再修改";
			technic.setName(technic_name);
			technic.setCustom_id(custom_id);
			technic.setDefault_role(new BigDecimal("0"));
			technic.setDesc(desc);
			technic.setTechnic_category_id(technic_category_id);

			List<TechnicBean.CustomCol> customCols = technic.getCustom_cols();
			TechnicBean.CustomCol customCol = technic.new CustomCol();
			customCol.setCol_name("分切");

			List<TechnicBean.CustomCol.Param> params = new ArrayList<>();
			TechnicBean.CustomCol.Param param = customCol.new Param();
			param.setParam_name("切片");
			params.add(param);

			param = customCol.new Param();
			param.setParam_name("切丝");
			params.add(param);

			customCol.setParams(params);

			customCols.add(customCol);

			boolean result = technicService.updateTechnic(technic);
			Assert.assertEquals(result, true, "修改工艺信息失败");

			TechnicBean r_technic = technicService.getTechnic(technic_id);
			Assert.assertNotEquals(r_technic, null, "获取工艺信息 " + technic_id + " 详细信息失败");

			result = compareResult(technic, r_technic);
			Assert.assertEquals(result, true, "修改工艺信息失败");
		} catch (Exception e) {
			logger.error("修改工艺出现错误: ", e);
			Assert.fail("修改工艺出现错误: ", e);
		} finally {
			if (technic_category_id != null) {
				try {
					ReporterCSS.step("后置处理: 删除临时添加的工艺类型");
					boolean result = technicService.deleteTechnicCategory(technic_category_id);
					Assert.assertEquals(result, true, "删除工艺类型失败");
				} catch (Exception e) {
					logger.error("删除工艺类型出现错误: ", e);
					Assert.fail("删除工艺类型出现错误: ", e);
				}
			}
		}
	}

	@Test
	public void technicTestCase03() {
		ReporterCSS.title("测试点: 新建工艺信息后,删除所绑定的工艺类型");
		try {
			// 新建一个工艺类型
			String technic_category_id = technicService
					.createTechnicCategory(StringUtil.getRandomString(4).toUpperCase());
			Assert.assertNotEquals(technic_category_id, null, "新建工艺类型失败");
			technicParam.setTechnic_category_id(technic_category_id);

			technic_id = technicService.createTechnic(technicParam);
			Assert.assertNotEquals(technic_id, null, "新建工艺信息失败");

			boolean result = technicService.deleteTechnicCategory(technic_category_id);
			Assert.assertEquals(result, true, "删除工艺类型失败");

			TechnicBean technic = technicService.getTechnic(technic_id);
			Assert.assertNotEquals(technic, null, "获取工艺信息 " + technic_id + " 详细信息失败");

			String msg = null;
			if (technic.getTechnic_category_id() != null) {
				msg = String.format("新建工艺信息后,删除所绑定的工艺类型后,工艺类型对应的工艺类型字段值没有变为null");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建工艺信息后,删除所绑定的工艺类型后,工艺类型对应的工艺类型字段值没有变为null");
		} catch (Exception e) {
			logger.error("修改工艺出现错误: ", e);
			Assert.fail("修改工艺出现错误: ", e);
		}
	}

	@Test
	public void technicTestCase04() {
		ReporterCSS.title("测试点: 新建工艺信息后,绑定已经删除的工艺类型");
		try {
			// 新建一个工艺类型
			String technic_category_id = technicService
					.createTechnicCategory(StringUtil.getRandomString(4).toUpperCase());
			Assert.assertNotEquals(technic_category_id, null, "新建工艺类型失败");
			technicParam.setTechnic_category_id(technic_category_id);

			boolean result = technicService.deleteTechnicCategory(technic_category_id);
			Assert.assertEquals(result, true, "删除工艺类型失败");

			technic_id = technicService.createTechnic(technicParam);
			Assert.assertEquals(technic_id, null, "新建工艺信息后,绑定已经删除的工艺类型,断言创建失败");

		} catch (Exception e) {
			logger.error("新建工艺出现错误: ", e);
			Assert.fail("新建工艺出现错误: ", e);
		}
	}

	@Test(timeOut = 10000)
	public void technicTestCase05() {
		ReporterCSS.title("测试点: 删除工艺信息");
		try {
			String technic_id = technicService.createTechnic(technicParam);
			Assert.assertNotEquals(technic_id, null, "新建工艺信息失败");

			boolean result = technicService.deleteTechnic(technic_id);
			Assert.assertEquals(result, true, "删除工艺信息失败");

			technicFilterParam.setQ(technicParam.getName());
			List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

			TechnicBean technic = technicList.stream().filter(t -> t.getName().equals(technicParam.getName())).findAny()
					.orElse(null);
			Assert.assertEquals(technic, null, "删除的工艺信息没有真正删除");
		} catch (Exception e) {
			logger.error("删除工艺出现错误: ", e);
			Assert.fail("删除工艺出现错误: ", e);
		}
	}

	@Test(timeOut = 10000)
	public void technicTestCase06() {
		ReporterCSS.title("测试点: 批量导入工艺信息");
		try {
			List<TechnicBean> technicParams = new ArrayList<TechnicBean>();
			for (int i = 0; i < 30; i++) {
				TechnicBean technicParam = new TechnicBean();
				String technic_name = StringUtil.getRandomString(6).toUpperCase();
				String custom_id = "TMP-" + StringUtil.getRandomNumber(4);
				String desc = "自动化创建";
				technicParam.setName(technic_name);
				technicParam.setCustom_id(custom_id);
				technicParam.setDesc(desc);

				List<TechnicBean.CustomCol> customCols = new ArrayList<>();
				for (int j = 0; j < 3; j++) {
					TechnicBean.CustomCol customCol = technicParam.new CustomCol();
					customCol.setCol_name(StringUtil.getRandomString(4).toUpperCase());

					List<TechnicBean.CustomCol.Param> params = new ArrayList<>();
					TechnicBean.CustomCol.Param param = customCol.new Param();
					param.setParam_name(StringUtil.getRandomString(4).toUpperCase());
					params.add(param);

					param = customCol.new Param();
					param.setParam_name(StringUtil.getRandomString(4).toUpperCase());
					params.add(param);

					customCol.setParams(params);

					customCols.add(customCol);
					technicParam.setCustom_cols(customCols);
				}
				technicParams.add(technicParam);
			}

			boolean result = technicService.importTechnic(technicParams);
			Assert.assertEquals(result, true, "批量导入工艺信息失败");

			for (TechnicBean technicParam : technicParams) {
				String technic_name = technicParam.getName();

				technicFilterParam.setQ(technicParam.getName());
				List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
				Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

				TechnicBean technic = technicList.stream().filter(t -> t.getName().equals(technic_name)).findAny()
						.orElse(null);
				Assert.assertNotEquals(technic, null, "批量导入的工艺[" + technic_name + "]没有找到");

				technic = technicService.getTechnic(technic.getId());

				result = compareResult(technicParam, technic);
				Assert.assertEquals(result, true, "批量导入的工艺信息与查询到的不一致");
			}
		} catch (Exception e) {
			logger.error("批量导入工艺出现错误: ", e);
			Assert.fail("批量导入工艺出现错误: ", e);
		}
	}

	@Test
	public void technicTestCase07() {
		ReporterCSS.title("测试点: 搜索过滤工艺");
		try {
			List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

			if (technicList.size() > 0) {
				TechnicBean technic = NumberUtil.roundNumberInList(technicList);
				String technic_name = technic.getName();
				technicFilterParam = new TechnicFilterParam();
				technicFilterParam.setQ(technic_name);

				technicList = technicService.searchTechnic(technicFilterParam);
				Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

				TechnicBean tempTechnic = technicList.stream().filter(t -> t.getId().equals(technic.getId())).findAny()
						.orElse(null);
				Assert.assertNotEquals(tempTechnic, null, "按关键词搜索过滤工艺,没有找到目标工艺条目");
			}
		} catch (Exception e) {
			logger.error("搜索过滤工艺信息出现错误: ", e);
			Assert.fail("搜索过滤工艺信息出现错误: ", e);
		}

	}

	@Test
	public void technicTestCase08() {
		ReporterCSS.title("测试点: 按工艺类型搜索过滤工艺");
		try {
			technicFilterParam.setTechnic_category_id(technic_category_id);

			List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

			List<String> technic_category_ids = technicList.stream()
					.filter(t -> !t.getTechnic_category_id().equals(technic_category_id))
					.map(t -> t.getTechnic_category_id()).collect(Collectors.toList());

			Assert.assertEquals(technic_category_ids.size(), 0,
					"按工艺类型" + technic_category_id + "搜索过滤工艺,过滤出了其他工艺类型 " + technic_category_ids + "的数据");
		} catch (Exception e) {
			logger.error("搜索过滤工艺信息出现错误: ", e);
			Assert.fail("搜索过滤工艺信息出现错误: ", e);
		}

	}

	@Test
	public void technicTestCase09() {
		ReporterCSS.title("测试点: 拉取所有的工艺信息");
		try {
			List<TechnicBean> technicList = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technicList, null, "搜索查询工艺信息失败");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technicList.size(), technics.size(), "拉取工艺列表接口,当limit值为0时,没有拉取所有的数据返回");
		} catch (Exception e) {
			logger.error("搜索过滤工艺信息出现错误: ", e);
			Assert.fail("搜索过滤工艺信息出现错误: ", e);
		}

	}

	public boolean compareResult(TechnicBean technicParam, TechnicBean technic) {
		String msg = null;
		boolean result = true;
		if (!technicParam.getName().equals(technic.getName())) {
			msg = String.format("工艺信息,填写的工艺名称与查询到的不一致,预期:%s,实际:%s", technicParam.getName(), technic.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!technicParam.getCustom_id().equals(technic.getCustom_id())) {
			msg = String.format("工艺信息,填写的工艺编号与查询到的不一致,预期:%s,实际:%s", technicParam.getCustom_id(),
					technic.getCustom_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		String p_technic_category_id = technicParam.getTechnic_category_id() == null ? "null"
				: technicParam.getTechnic_category_id();
		String r_technic_category_id = technic.getTechnic_category_id() == null ? "null"
				: technic.getTechnic_category_id();

		if (!p_technic_category_id.equals(r_technic_category_id)) {
			msg = String.format("工艺信息,填写的工艺类型与查询到的不一致,预期:%s,实际:%s", p_technic_category_id, r_technic_category_id);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		BigDecimal p_default_role = technicParam.getDefault_role() == null ? new BigDecimal("0")
				: technicParam.getDefault_role();

		if (p_default_role.compareTo(technic.getDefault_role()) != 0) {
			msg = String.format("工艺信息,填写的默认操作角色与查询到的不一致,预期:%s,实际:%s", p_default_role, technic.getDefault_role());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!technicParam.getDesc().equals(technic.getDesc())) {
			msg = String.format("工艺信息,填写的工艺描述与查询到的不一致,预期:%s,实际:%s", technicParam.getDesc(), technic.getDesc());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<TechnicBean.CustomCol> paramCustomCols = technicParam.getCustom_cols();
		List<TechnicBean.CustomCol> customCols = technic.getCustom_cols();

		if (paramCustomCols.size() != customCols.size()) {
			msg = String.format("工艺信息,填写的自定义字段个数与查询到的不一致,预期:%s,实际:%s", paramCustomCols.size(), customCols.size());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		} else {
			for (TechnicBean.CustomCol paramCustomCol : paramCustomCols) {
				String col_name = paramCustomCol.getCol_name();
				TechnicBean.CustomCol customCol = customCols.stream().filter(c -> c.getCol_name().equals(col_name))
						.findAny().orElse(null);
				if (customCol == null) {
					msg = String.format("工艺信息,填写的自定义字段[%s]没有查询到", col_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				List<TechnicBean.CustomCol.Param> p_params = paramCustomCol.getParams();
				List<TechnicBean.CustomCol.Param> r_params = customCol.getParams();
				for (TechnicBean.CustomCol.Param p_param : p_params) {
					TechnicBean.CustomCol.Param r_param = r_params.stream()
							.filter(r -> r.getParam_name().equals(p_param.getParam_name())).findAny().orElse(null);
					if (r_param == null) {
						msg = String.format("工艺信息,填写的自定义字段[%s]中的参数描述[%s]没有找到", col_name, p_param.getParam_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}
				}

			}
		}
		return result;
	}

	@AfterClass(timeOut = 10000)
	public void afterClass() {
		ReporterCSS.title("后置处理,删除添加的工艺信息");
		try {
			technicFilterParam.setQ("TMP");
			List<TechnicBean> technics = technicService.searchTechnic(technicFilterParam);
			Assert.assertNotEquals(technics, null, "搜索查询工艺信息失败");
			for (TechnicBean technic : technics) {
				if (technic.getCustom_id().startsWith("TMP")) {
					boolean result = technicService.deleteTechnic(technic.getId());
					Assert.assertEquals(result, true, "删除工艺信息 " + technic.getCustom_id() + "失败");
				}
			}
		} catch (Exception e) {
			logger.error("删除工艺出现错误: ", e);
			Assert.fail("删除工艺出现错误: ", e);
		}
	}

}
