package com.server.hll;

import java.net.InetSocketAddress;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

/**
 * EcServer 的启动类
 * 
 * @author geng
 *
 */
public class HllServer {

	private static Logger logger = LoggerFactory.getLogger(HllServer.class);
	private Properties properties = new Properties();

	public void startServer(String[] args) {
		PropResolve propResolve = new PropResolve();
		properties = propResolve.getProperties(args);
		if (properties.getProperty("p") == null) {
			System.err.println("请设置端口号p");
			return;
		}
		try {
			HttpServer localHttpServer = HttpServer.create(
					new InetSocketAddress(Integer.parseInt(properties
							.getProperty("p"))), 0);
			logger.info("端口是：" + properties.getProperty("p"));
			HttpContext context = localHttpServer.createContext("/hll",
					new HllHandler());
			context.getFilters().add(new ResolveParameter());
			localHttpServer.setExecutor(null);
			localHttpServer.start();
			logger.info("start hll server");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	public static void runhllServer(String[] args){
		HllServer hllServer = new HllServer();
		hllServer.startServer(args);
	}
	public static void main(String[] args) {
		runhllServer(args);
	}

}
