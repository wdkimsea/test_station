package cn.guanmai.manage.impl.base;

import java.util.HashMap;
import java.util.Map;

import cn.guanmai.manage.interfaces.base.MgDownLoadService;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;

/**
 * @author: liming
 * @Date: 2020年7月22日 下午4:16:59
 * @description:
 * @version: 1.0
 */

public class MgDownloadServiceImpl implements MgDownLoadService {
	private BaseRequest baseRequest;

	public MgDownloadServiceImpl(Map<String, String> headers) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String downloadFile(String download_url) throws Exception {
		String file_path = baseRequest.baseExport(download_url, RequestType.GET, new HashMap<String, String>(),
				"temp.xlsx");
		return file_path;
	}

}
