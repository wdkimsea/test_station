package cn.guanmai.manage.interfaces.base;

/**
 * @author: liming
 * @Date: 2020年7月22日 下午4:15:50
 * @description:
 * @version: 1.0
 */

public interface MgDownLoadService {
	/**
	 * 下载文件
	 * 
	 * @param download_url
	 * @return
	 * @throws Exception
	 */
	public String downloadFile(String download_url) throws Exception;

}
