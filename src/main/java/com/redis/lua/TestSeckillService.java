package com.redis.lua;


import java.util.Arrays;

/**
 * Created by liuyang on 2017/4/20.
 */
public class TestSeckillService {
    public static void main(String[] args) {
    	String prefix="goodsId:";
    	SeckillService service = new SeckillService();
    	String [] keys=new String[]{prefix+"1",prefix+"2",prefix+"3",prefix+"4",prefix+"5",prefix+"6"};
        for (int i = 0; i < 1000; i++) {
        	ThreadSeckillServiceTest seckillService2=new ThreadSeckillServiceTest(service, Arrays.asList(keys), Arrays.asList("1","21","3","6","9","10"));
        	seckillService2.start();
        }
    }
    
    
    
}
