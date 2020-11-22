package cn.guanmai.station.interfaces.system;

/* 
* @author liming 
* @date Jan 8, 2019 5:39:05 PM 
* @des 首页数据相关统计类
* @version 1.0 
*/
public interface DataCenterService {
	/**
	 * ST首页数据统计接口
	 * 
	 * @param start_date
	 * @param end_date
	 * @param query_type
	 * @return
	 * @throws Exception
	 */
	public boolean dailyProfit(String start_date, String end_date, int query_type) throws Exception;

	/**
	 * 未读消息统计数
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer unReadMessageCount() throws Exception;
}
