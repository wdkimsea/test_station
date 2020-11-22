package cn.guanmai.open.interfaces.product;

import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.bean.product.param.OpenSalemenuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSalemenuUpdateParam;

import java.util.List;

public interface OpenSalemenuService {
	/**
	 * 获取报价单
	 * 
	 * @param customer_id
	 * @param is_active
	 * @return
	 * @throws Exception
	 */
	public List<OpenSalemenuBean> searchSalemenu(String customer_id, Integer is_active) throws Exception;

	/**
	 * 新建报价单
	 * 
	 * @param openSalemenuCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createSalemenu(OpenSalemenuCreateParam openSalemenuCreateParam) throws Exception;

	/**
	 * 修改报价单
	 * 
	 * @param openSalemenuUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSalemenu(OpenSalemenuUpdateParam openSalemenuUpdateParam) throws Exception;
}
