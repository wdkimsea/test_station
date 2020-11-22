package cn.guanmai.station.system;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.system.AreaBean;
import cn.guanmai.station.impl.system.AreaServiceImpl;
import cn.guanmai.station.interfaces.system.AreaService;
import cn.guanmai.station.tools.LoginStation;

/* 
* @author liming 
* @date Apr 3, 2019 4:33:31 PM 
* @des 地区编码
* @version 1.0 
*/
public class AreaTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(AreaTest.class);
	private AreaService areaService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		areaService = new AreaServiceImpl(headers);
	}

	@Test
	public void areaTestCase01() {
		try {
			Reporter.log("测试点: 获取地区编号");
			List<AreaBean> area_dict = areaService.getAreaDict();
			Assert.assertNotEquals(area_dict, null, "获取地区编号失败");
		} catch (Exception e) {
			logger.error("获取地区编码遇到错误: ", e);
			Assert.fail("获取地区编码遇到错误: ", e);
		}
	}

}
