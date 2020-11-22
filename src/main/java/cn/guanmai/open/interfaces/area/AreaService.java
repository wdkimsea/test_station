package cn.guanmai.open.interfaces.area;

import java.util.List;

import cn.guanmai.open.bean.area.AreaBean;

/* 
* @author liming 
* @date Jun 6, 2019 10:33:48 AM 
* @des 地理标签相关接口
* @version 1.0 
*/
public interface AreaService {
	/**
	 * 获取地区列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AreaBean> getAreaList() throws Exception;
}
