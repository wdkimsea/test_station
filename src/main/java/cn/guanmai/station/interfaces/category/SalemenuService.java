package cn.guanmai.station.interfaces.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;

/* 
* @author liming 
* @date Feb 15, 2019 10:22:37 AM 
* @todo 报价单相关接口
* @version 1.0 
*/
public interface SalemenuService {
	/**
	 * 根据ID获取报价单详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SalemenuBean getSalemenuById(String id) throws Exception;

	/**
	 * 获取报价单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SalemenuBean> searchSalemenu(SalemenuFilterParam param) throws Exception;

	/**
	 * 创建报价单
	 * 
	 * @param salemenu
	 * @return
	 * @throws Exception
	 */
	public String createSalemenu(SalemenuBean salemenu) throws Exception;

	/**
	 * 修改报价单
	 * 
	 * @param salemenu
	 * @return
	 * @throws Exception
	 */
	public boolean updateSalemenu(SalemenuBean salemenu) throws Exception;

	/**
	 * 获取销售对象站点列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SalemenuBean.Target> getTargetArray() throws Exception;

	/**
	 * 删除报价单
	 * 
	 * @param salemenu_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSalemenu(String salemenu_id) throws Exception;

	/**
	 * 分享报价单
	 * 
	 * @param salemenu_id
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createShareSalemenu(String salemenu_id) throws Exception;

	/**
	 * 获取报价单分享信息
	 * 
	 * @param share_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, SkuBean> getShareSalemenu(BigDecimal share_id) throws Exception;

	/**
	 * 获取报价单里的所有销售SKU
	 * 
	 * @param salemenu_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, SkuBean> getSalemenuSkus(String salemenu_id) throws Exception;

	/**
	 * 获取所有的报价单
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray getAllSalemenu() throws Exception;

	/**
	 * 获取报价单列表
	 * 
	 * @param type
	 * @param is_active
	 * @return
	 * @throws Exception
	 */
	public List<SalemenuBean> getSalemenuList(int type, int is_active) throws Exception;

}
