package cn.guanmai.open.interfaces.product;

import java.util.List;

import cn.guanmai.open.bean.product.OpenServiceTimeBean;

/**
 * @author: liming
 * @Date: 2020年2月7日 上午11:24:25
 * @description: 报价单设置
 * @version: 1.0
 */

public interface OpenServiceTimeService {
	/**
	 * 获取报价单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenServiceTimeBean> getServiceTimes() throws Exception;
}
