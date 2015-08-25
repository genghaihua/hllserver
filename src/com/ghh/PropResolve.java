package com.ghh;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析jar命令参数  
 * @author geng
 *
 */
public class PropResolve {

	private static Logger logger=LoggerFactory.getLogger(PropResolve.class); 
	/**
	 * 参数个数必须为偶数
	 * @param args
	 * @return
	 */
	public Properties getProperties(String[] args){
		Properties pro=new Properties();
		//参数为偶数
		if(args.length%2==0){
			for(int i=0;i<args.length;i=i+2){
				if(args[i].indexOf("-")!=-1)
				pro.put(args[i].substring(1), args[i+1]);
				else {
					logger.info("参数类型错误，请检查是否有-符号");
					return null;
				}
			}
			return pro;
		}
		else{
			logger.info("参数解析失败，请检查");
			return null;
		}
	}
}
