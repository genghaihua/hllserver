package com.ghh;

import java.util.Map;

/**
 * url参数处理类
 * input :  http://localhost:9983/?sdate=20150303&edate=20150305&rand=1237
 * 
 * @author geng
 *
 */
public interface ProcessUrl {
	public String getUrlResponse(Map<String, String> datas);
}
