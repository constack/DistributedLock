package com.redis.lua;


import java.util.Arrays;


public class TestSeckillService {
    public static void main(String[] args) {
    	String prefix="goodsId:";
    	SeckillService service = new SeckillService();
    	String [] keys=new String[]{prefix+"1",prefix+"2",prefix+"3",prefix+"4",prefix+"5",prefix+"6"};
        for (int i = 0; i < 1000; i++) {
        	SeckillServiceThread seckillService2=new SeckillServiceThread(service, Arrays.asList(keys), Arrays.asList("1","2","3","6","9","10"));
        	seckillService2.start();
        }
    }
    
    
    
}
