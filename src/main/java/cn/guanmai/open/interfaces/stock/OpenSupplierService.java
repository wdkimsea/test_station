package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierCommonParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午2:27:27
 * @des TODO
 */

public interface OpenSupplierService {
	/**
	 * 查询过滤供应商
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenSupplierBean> querySupplier(OpenSupplierFilterParam filterParam) throws Exception;

	/**
	 * 获取供应商详情
	 * 
	 * @param supplier_id
	 * @return
	 * @throws Exception
	 */
	public OpenSupplierDetailBean getSupplierDetail(String supplier_id) throws Exception;

	/**
	 * 新建供应商
	 * 
	 * @param openSupplierCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createSupplier(OpenSupplierCommonParam openSupplierCreateParam) throws Exception;

	/**
	 * 修改供应商
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean updateSupplier(OpenSupplierCommonParam openSupplierUpdateParam) throws Exception;
}
