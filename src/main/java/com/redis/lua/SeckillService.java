package com.redis.lua;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SeckillService {
	public static final String SUCCESS_FLAG = "1";
	private static JedisPool pool = null;
    static String script;
    static String key="goodsId";

    static {
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(200);
		// 设置最大空闲数
		config.setMaxIdle(8);
		// 设置最大等待时间
		config.setMaxWaitMillis(1000 * 100);
		// 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
		config.setTestOnBorrow(true);
		pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
		Jedis resource = pool.getResource();
		String prefix="goodsId:";
		String [] keys=new String[]{prefix+SUCCESS_FLAG,prefix+"2",prefix+"3",prefix+"4",prefix+"5",prefix+"6"};
		for(String key:keys){
			Map<String, String> param=new HashMap<>();
			param.put("Total", "100");
			param.put("Booked", "0");
			resource.hmset(key, param);
		}
//	   script="local n = tonumber(ARGV[1])"
//			+"if not n  or n == 0 then"
//			+"    return 0 "      
//			+"end   "             
//			+"local vals = redis.call(\"HMGET\", KEYS[1], \"Total\", \"Booked\");"
//			+"local total = tonumber(vals[1])"
//			+"local blocked = tonumber(vals[2])"
//			+"if not total or not blocked then"
//			+" return 0   "    
//			+"end "               
//			+"if blocked + n <= total then"
//			+"    redis.call(\"HINCRBY\", KEYS[1], \"Booked\", n)  "                                 
//			+"    return n;   "
//			+"end     "           
//			+"return 0";
//		script="local size=#KEYS "
//				+"for i=1,size do "
//				+"local n = tonumber(ARGV[i]) "
//				+"if not n  or n == 0 then"
//				+"    return 0 "      
//				+"end   "             
//				+"local vals = redis.call(\"HMGET\", KEYS[i], \"Total\", \"Booked\");"
//				+"local total = tonumber(vals[1]) "
//				+"local blocked = tonumber(vals[2]) "
//				+"if not total or not blocked then "
//				+" return 0   "    
//				+"end "               
//				+"if blocked + n > total then"
//				+"    return 0;   "
//				+"end     "           
//				+"end"
//				+"for j=1,size do "
//				+"local n = tonumber(ARGV[j]) "
//				+"redis.call(\"HINCRBY\", KEYS[j], \"Booked\", n) "
//				+"end "
//				+"return 1";
		script = "local size=#KEYS " + "for i=1,size do " + "local n = tonumber(ARGV[i]) " + "if not n  or n == 0 then" + "    return 0 " + "end   " + "local vals = redis.call(\"HMGET\", KEYS[i], \"Total\", \"Booked\");" + "local total = tonumber(vals[1]) " + "local blocked = tonumber(vals[2]) " + "if not total or not blocked then " + " return 0   " + "end " + "if blocked + n > total then" + "    return 0;   " + "end     " + "end" + "  for j=1,size do " + "local n = tonumber(ARGV[j]) " + "redis.call(\"HINCRBY\", KEYS[j], \"Booked\", n) " + "end " + " return 1";
    }
    
    public Jedis getJedis(){
    	return pool.getResource();
    }
	
    public void seckill(String key,int count) {
    	Jedis jedis = getJedis();
    	String booked = jedis.eval(script, Arrays.asList(key), Arrays.asList(String.valueOf(count))).toString();
    	Map<String, String> map = jedis.hgetAll(key);
    	System.out.println(map);
    	if(booked.equals("50")){
    		System.out.println("预占库存成功");
    	}else{
    		System.out.println("库存不足");
    	}
    	jedis.close();
    }
    
    public boolean seckill(List<String> keys,List<String> count) {
    	Jedis jedis = getJedis();
    	String booked = jedis.eval(script, keys, count).toString();
    	jedis.close();
    	if(booked.equals(SUCCESS_FLAG)){
    		System.out.println("预占成功");
    		return true;
    	}else{
    		System.out.println("预占失败");
    		return false;
    	}
    }
    
    public boolean seckill(Map<String,String> goodsMap) {
		boolean flag=false;
		Set<String> keys = goodsMap.keySet();
		List<String> goodsIds=new ArrayList<>(keys.size());
		List<String> purchaseCounts=new ArrayList<>(keys.size());
		for(String key :keys){
			goodsIds.add(key);
			purchaseCounts.add(goodsMap.get(key));
		}
		flag=seckill(goodsIds, purchaseCounts);
    	return flag;
	}
    
}
