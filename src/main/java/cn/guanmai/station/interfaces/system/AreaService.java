package cn.guanmai.station.interfaces.system;

import java.util.List;

import cn.guanmai.station.bean.system.AreaBean;

/* 
* @author liming 
* @date Apr 3, 2019 4:34:37 PM 
* @des 地区编码相关接口
* @version 1.0 
*/
public interface AreaService {
	/**
	 * 获取地区列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AreaBean> getAreaDict() throws Exception;
}
