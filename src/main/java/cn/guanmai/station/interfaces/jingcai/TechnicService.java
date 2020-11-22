package cn.guanmai.station.interfaces.jingcai;

import java.util.List;

import cn.guanmai.station.bean.jingcai.TechnicBean;
import cn.guanmai.station.bean.jingcai.TechnicCategoryBean;
import cn.guanmai.station.bean.jingcai.TechnicFlowBean;
import cn.guanmai.station.bean.jingcai.param.TechnicFilterParam;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowCreateParam;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowUpdateParam;

/**
 * @author liming
 * @date 2019年8月7日 上午10:11:34
 * @des 净菜站点的加工相关接口
 * @version 1.0
 */
public interface TechnicService {
	/**
	 * 新建工艺类型
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String createTechnicCategory(String name) throws Exception;

	/**
	 * 删除工艺类型
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTechnicCategory(String id) throws Exception;

	/**
	 * 获取工艺类型列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TechnicCategoryBean> searchTechnicCategory(String q) throws Exception;

	/**
	 * 添加工艺
	 * 
	 * @param technicCreateParam
	 * @return
	 */
	public String createTechnic(TechnicBean technicCreateParam) throws Exception;

	/**
	 * 修改工艺信息
	 * 
	 * @param technic
	 * @return
	 * @throws Exception
	 */
	public boolean updateTechnic(TechnicBean technic) throws Exception;

	/**
	 * 搜索工艺信息
	 * 
	 * @param technicFilterParam
	 * @return
	 */
	public List<TechnicBean> searchTechnic(TechnicFilterParam technicFilterParam) throws Exception;

	/**
	 * 获取所有的工艺信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TechnicBean> getAllTechnics() throws Exception;

	/**
	 * 获取指定的工艺信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TechnicBean getTechnic(String id) throws Exception;

	/**
	 * 删除工艺信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTechnic(String id) throws Exception;

	/**
	 * 批量导入工艺信息
	 * 
	 * @param technics
	 * @return
	 * @throws Exception
	 */
	public boolean importTechnic(List<TechnicBean> technics) throws Exception;

	/**
	 * 初始化工艺信息
	 * 
	 * @throws Exception
	 */
	public void initTechnic() throws Exception;

	/**
	 * 获取指定净菜SKU的原料加工工艺信息
	 * 
	 * @param sku_id
	 * @param ingredient_id
	 * @return
	 * @throws Exception
	 */
	public List<TechnicFlowBean> getTechnicFlows(String sku_id, String ingredient_id) throws Exception;

	/**
	 * 为净菜SKU添加具体工艺信息
	 * 
	 * @param technicFlowCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createTechnicFlow(TechnicFlowCreateParam technicFlowCreateParam) throws Exception;

	/**
	 * 修改净菜SKU的具体工艺信息
	 * 
	 * @param technicFlowUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateTechnicFlow(TechnicFlowUpdateParam technicFlowUpdateParam) throws Exception;

	/**
	 * 删除净菜SKU的具体工艺信息
	 * 
	 * @param technicFlowUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTechnicFlow(String id) throws Exception;

	/**
	 * 净菜SKU的工艺切换顺序
	 * 
	 * @param id
	 * @param next_id
	 * @return
	 * @throws Exception
	 */
	public boolean changeTechnicFlow(String id, String next_id) throws Exception;
}
