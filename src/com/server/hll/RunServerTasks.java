package com.server.hll;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * 执行http请求的线程处理类
 * @author geng
 *
 */
public class RunServerTasks implements Runnable{
	HttpExchange httpExchange;
	ProcessUrl proUrl;
	RunServerTasks(HttpExchange he,ProcessUrl proUrl) {
		this.httpExchange = he;
		this.proUrl=proUrl;
	}
	
	@Override
	public void run() {
		@SuppressWarnings("unchecked")
		Map<String, String> paramsMap = (Map<String, String>) httpExchange.getAttribute("parameters");
		String responseMsg = proUrl.getUrlResponse(paramsMap); // 响应信息
		try {
			httpExchange.sendResponseHeaders(200, responseMsg.getBytes().length);
		} catch (IOException e) {
			e.printStackTrace();
		} // 设置响应头属性及响应信息的长度
		OutputStream out = httpExchange.getResponseBody(); // 获得输出流
		try {
			out.write(responseMsg.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpExchange.close();
	}
}
