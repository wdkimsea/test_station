package cn.guanmai.manage.interfaces.custommange;

import java.util.List;

import cn.guanmai.manage.bean.custommange.param.MgSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgSalesReportFiterParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportFilterParam;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportBean;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportDetailBean;
import cn.guanmai.manage.bean.custommange.result.MgStationSalesReportDetailBean;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午3:58:18
 * @description:
 * @version: 1.0
 */

public interface MgSalesReportService {
	/**
	 * 搜索过滤销售报表
	 * 
	 * @param salesReportFiterParam
	 * @return
	 * @throws Exception
	 */
	public List<MgSalesReportBean> searchSalesReport(MgSalesReportFiterParam salesReportFiterParam) throws Exception;

	/**
	 * 导出销售报表
	 * 
	 * @param salesReportFiterParam
	 * @return
	 * @throws Exception
	 */
	public boolean exportSalesReport(MgSalesReportFiterParam salesReportFiterParam) throws Exception;

	/**
	 * 获取销售报表详情
	 * 
	 * @param mgSalesReportDetailParam
	 * @return
	 * @throws Exception
	 */
	public MgSalesReportDetailBean getSalesReportDetail(MgSalesReportDetailParam mgSalesReportDetailParam)
			throws Exception;

	/**
	 * 获取销售报表基本信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getSalesReportBaseInfo() throws Exception;

	/**
	 * 按站点统计销售报表
	 * 
	 * @param mgStationSalesReportFilterParam
	 * @return
	 * @throws Exception
	 */
	public boolean searchStationSalesReport(MgStationSalesReportFilterParam mgStationSalesReportFilterParam)
			throws Exception;

	/**
	 * 按站点统计销售报表导出
	 * 
	 * @param mgStationSalesReportFilterParam
	 * @return
	 * @throws Exception
	 */
	public boolean exportStationSalesReport(MgStationSalesReportFilterParam mgStationSalesReportFilterParam)
			throws Exception;

	/**
	 * 获取站点统计销售报表详情
	 * 
	 * @param mgStationSalesReportDetailParam
	 * @return
	 * @throws Exception
	 */
	public MgStationSalesReportDetailBean getMgStationSalesReportDetail(
			MgStationSalesReportDetailParam mgStationSalesReportDetailParam) throws Exception;
}
