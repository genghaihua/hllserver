package com.server.hll;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.agkn.hll.HLL;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 初始化HttpHandler 若改变，只需要实现processurl接口即可
 * 
 * @author geng
 *
 */
public class HllHandler implements HttpHandler {
	private static int seed = 123456;
	HashFunction hash = Hashing.murmur3_128(seed);
	private static HashMap<String, HLL> infoHLLMap=new HashMap<String, HLL>();
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		ProcessUrl processUrl = new ProcessUrl() {
			@Override
			public String getUrlResponse(Map<String, String> datas) {
				String theme = datas.get("fun");
				if(theme==null)
					return "fun参数为设定";
				if(theme.equals("add")){
					return dealAdd(datas);
				}
				else if(theme.equals("get")){
					return dealGet(datas);
				}
				else if(theme.equals("del")){
					return dealDel(datas);
				}
				else{
					return "输入参数错误";
				}
			}
		};
		RunServerTasks rq = new RunServerTasks(httpExchange, processUrl);
		new Thread(rq).start();
	}

	protected String dealDel(Map<String, String> datas) {
		String name=datas.get("key");
		if(name==null||name.replaceAll(" ", "").length()<=0)
			return "NO NAME!";
		if(infoHLLMap.containsKey(name)){
			infoHLLMap.get(name).clear();
			infoHLLMap.remove(name);
		}
		return "del "+name+" true!";	
	}

	protected String dealGet(Map<String, String> datas) {
		String name=datas.get("key");
		if(name==null||name.replaceAll(" ", "").length()<=0)
			return "NO NAME!";
		if(infoHLLMap.containsKey(name)){
			return ""+infoHLLMap.get(name).cardinality();
		}
		else{
			return "0";
		}		
	}

	protected String dealAdd(Map<String, String> datas) {
		String name=datas.get("key");
		if(name==null||name.replaceAll(" ", "").length()<=0)
			return "NO NAME!";
		String content=datas.get("val");
		if(content==null||content.replaceAll(" ", "").length()<=0)
			return "NO content";
		if(infoHLLMap.containsKey(name)){
			infoHLLMap.get(name).addRaw(hash.newHasher().putBytes(content.getBytes()).hash().asLong());
		}
		else{
			HLL hll = new HLL(13, 5); 
			hll.addRaw(hash.newHasher().putBytes(content.getBytes()).hash().asLong());
			infoHLLMap.put(name, hll);
		}
		return "已添加类别:"+name+"  元素:"+content;
	}

	

	
}
