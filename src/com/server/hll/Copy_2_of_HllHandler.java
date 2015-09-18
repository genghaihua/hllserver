package com.server.hll;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class Copy_2_of_HllHandler implements HttpHandler {
	Object object=new Object();
	private  int seed = 1234567890;
	HashFunction hash = Hashing.murmur3_128(seed);
    private HashMap<String, HLL> infoHLLMap=new HashMap<String, HLL>();
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
		//System.err.println("del "+name+" true!");
		return "OK";
		//return "del "+name+" true!";	
	}

	protected String dealGet(Map<String, String> datas) {
		String name=datas.get("key");
		if(name==null||name.replaceAll(" ", "").length()<=0)
			return "NO NAME!";
		if(infoHLLMap.containsKey(name)){
			//System.err.println("key="+name+"|||"+infoHLLMap.get(name).cardinality());
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
		synchronized (object) {
			if(infoHLLMap.containsKey(name)){
				infoHLLMap.get(name).addRaw(hash.newHasher().putBytes(content.getBytes()).hash().asLong());
			}
			else{
				//HLL hll = new HLL(13, 5); 
				HLL hll = new HLL(30, 8); 
				hll.addRaw(hash.newHasher().putBytes(content.getBytes()).hash().asLong());
				infoHLLMap.put(name, hll);
			}
		}
//		System.err.println("已添加类别:"+name+"  元素:"+content);
		return "OK";
	}
}
