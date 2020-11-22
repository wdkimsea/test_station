package cn.guanmai.station.interfaces.jingcai;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.jingcai.LabelBean;

/**
 * @author: liming
 * @Date: 2020年4月28日 上午10:39:50
 * @description:
 * @version: 1.0
 */

public interface LabelService {
	/**
	 * 新建商品加工标签
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createLabel(String name) throws Exception;

	/**
	 * 删除商品加工标签
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteLabel(BigDecimal id) throws Exception;

	/**
	 * 搜索过滤商品加工标签
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<LabelBean> searchLabel(String q) throws Exception;

	/**
	 * 根据名称获取标签
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public LabelBean getLabelByName(String name) throws Exception;

	/**
	 * 根据ID获取标签
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public LabelBean getLabelByID(BigDecimal id) throws Exception;

	/**
	 * 初始化净菜商品标签(标签名为 : 净菜)
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal initLabel() throws Exception;
}
