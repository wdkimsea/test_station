package cn.guanmai.station.interfaces.category;

import java.util.List;
import java.util.Map;

import cn.guanmai.station.bean.category.SpuRemarkBean;
import cn.guanmai.station.bean.category.param.SpuRemarkFiterParam;

/* 
* @author liming 
* @date Feb 18, 2019 5:11:48 PM 
* @des 商品备注相关接口
* @version 1.0 
*/
public interface SpuRemarkService {
	/**
	 * 商品备注页面,搜索过滤商户
	 * 
	 * @param keyword
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> searchCustomer(String keyword, int offset, int limit) throws Exception;

	/**
	 * 获取商户能看到的所有SPU ID 列表
	 * 
	 * @param spuRemarkFiterParam
	 * @return
	 * @throws Exception
	 */
	public List<SpuRemarkBean> searchSpuRemark(SpuRemarkFiterParam spuRemarkFiterParam) throws Exception;

	/**
	 * 设置商品备注
	 * 
	 * @param address_id
	 * @param spu_id
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	public boolean updateSpuRemark(String address_id, String spu_id, String remark) throws Exception;

	/**
	 * 随机取一个用户ID和对应的名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getCustomerMap() throws Exception;;

}
